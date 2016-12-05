package entity.vanet;

import java.util.Map;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import globals.Resources;
import globals.Vector2D;
import globals.BeaconDTO;
import globals.SignedDTO;
import globals.SignedBeaconDTO;
import globals.SignedCertificateDTO;

import remote.RemoteVehicleNetworkInterface;
import remote.RemoteRSUInterface;

import java.rmi.RemoteException;
import java.security.cert.X509Certificate;
import java.security.PrivateKey;
import java.security.KeyStore;

public class Vehicle {
	private String VIN;
	private Vector2D position;
	private Vector2D velocity;
	private RemoteVehicleNetworkInterface VANET;
	private RemoteRSUInterface RSU;
	private String nameInVANET;

	private X509Certificate myCert;
	private X509Certificate caCert;
	private PrivateKey myPrKey;
	private KeyStore myKeystore;

	private Map<X509Certificate, BeaconDTO> vicinity = new HashMap<>(); // String is the pseudonim certificate (is it? Vasco: I think it has to be)


	private boolean inDanger = false;
	private Timer resetInDangerTimer = new Timer();
	class ResetInDangerTask extends TimerTask {
		@Override
		public void run() {
			inDanger = false;
		}
	}

	private Timer timer = new Timer();
	private TimerTask engineTask = new TimerTask() {
		@Override
		public void run() {
			simulatePositionUpdate();
			beacon();
		}
	};

//  -----------------------------------



//  ------- CONSTRUCTOR  ------------

	public Vehicle(String VIN, String certificateName, Vector2D position, Vector2D velocity) {
		this.VIN = VIN;
		this.position = position;
		this.velocity = velocity;

		String certsDir = Resources.CERT_DIR+certificateName+"/";
		// Read certificate file to a certificate object
		try {
			this.myCert = (X509Certificate)Resources.readCertificateFile(certsDir+certificateName+".cer"); }
		catch (Exception e) {
			System.out.println(Resources.ERROR_MSG("Error Loading certificate: "+e.getMessage()));
			System.out.println(Resources.ERROR_MSG("Exiting. Vehicle is useless without certificate"));
			System.exit(1);
		}

		try {
			this.myKeystore = Resources.readKeystoreFile(certsDir + certificateName + ".jks", Resources.STORE_PASS);
			this.myPrKey = Resources.getPrivateKeyFromKeystore(this.myKeystore, certificateName, Resources.KEY_PASS); }
		catch (Exception e) {
			System.out.println(Resources.ERROR_MSG("Error Loading PrivateKey: "+e.getMessage()));
			System.out.println(Resources.ERROR_MSG("Exiting. Vehicle is useless without PrivateKey"));
			System.exit(1);
		}
		
		try {
			this.caCert = (X509Certificate)Resources.getCertificateFromKeystore(this.myKeystore, Resources.CA_NAME); }
		catch (Exception e) {
			System.out.println(Resources.WARNING_MSG("Failed to get CA certificate from Keystore: " + e.getMessage()));
			System.out.println(Resources.ERROR_MSG("Exiting. Vehicle cannot authenticate messages without CACert"));
			System.exit(1);
		}
	}

//  ------- GETTERS  ------------

	public Vector2D getPosition() { return this.position; }
	public Vector2D getVelocity() { return this.velocity; }
	public X509Certificate getCertificate() { return this.myCert; }
	public X509Certificate getCACertificate() { return this.caCert; }
	public PrivateKey getPrivateKey() { return this.myPrKey; }
	public KeyStore getKeystore() { return this.myKeystore; }


	// Sets VANET, starts updating position and starts beaoning
	public void start(RemoteVehicleNetworkInterface VANET, RemoteRSUInterface RSU, String name) {
		this.VANET = VANET;
		this.RSU = RSU;
		this.nameInVANET = name;

		// Run the engine and beaconing on a timer
		timer.scheduleAtFixedRate(engineTask, 0, Resources.BEACON_INTERVAL);
	}

//  ------- MAIN METHODS ------------

	public void beacon() {
		if(VANET == null || RSU == null) return;

		SignedBeaconDTO dto = new SignedBeaconDTO(this.position, this.velocity, this.myCert, this.myPrKey);

		try {
			VANET.simulateBeaconBroadcast(nameInVANET, dto);
		} catch(Exception e) {
			// TODO maybe try to reconect??
			System.out.println(Resources.ERROR_MSG("Unable to beacon message. Cause: " + e));
			System.out.println(Resources.ERROR_MSG("VANET seems dead... Exiting..."));
			System.exit(-1);
		}
	}

	public void simulatePositionUpdate() {
		if(inDanger == false) {
			position.update(velocity, 1); //FIXME: delta
		}
	}

	public void simulateBrain(SignedBeaconDTO dto) {
		X509Certificate beaconCert = dto.getSenderCertificate();
		BeaconDTO oldBeacon = vicinity.get(beaconCert);
		BeaconDTO newBeacon = dto.beaconDTO();
		
		// Check if received position is dangerous
		if(isVehicleDangerous(newBeacon.getPosition()) == true) {
			if(inDanger = true) {
				resetInDangerTimer.cancel();
			}
			inDanger = true;
			resetInDangerTimer = new Timer();
			resetInDangerTimer.schedule(new ResetInDangerTask(), Resources.DANGER_RESET_INTERVAL);
		}

		// Data trust
		if(oldBeacon != null) {
			if(isDataTrustworthy(oldBeacon, newBeacon) == false) {
				SignedCertificateDTO certToRevoke = new SignedCertificateDTO(beaconCert, this.getCertificate(), this.getPrivateKey());
				try {
					RSU.tryRevoke(certToRevoke);
				} catch(RemoteException e) {
					System.out.println(Resources.ERROR_MSG("RSU seems dead... Cause: " + e.getMessage() + ". Exiting..."));
					System.exit(-1);
				}

			}
		}

		updateVicinity(beaconCert, dto.beaconDTO());
	}

	private boolean isVehicleDangerous(Vector2D otherPos) {
		if (position.inRange(otherPos, Resources.TOO_DANGEROUS_RANGE)) {
			//System.out.println(Resources.WARNING_MSG("Proximity Alert: Vehicle in " + distance + "m." ));
			return true;
		}
		return false;
	}

	private boolean isDataTrustworthy(BeaconDTO oldBeacon, BeaconDTO newBeacon) {
		Vector2D predictedPosition = oldBeacon.getPosition().predictedNext(oldBeacon.getVelocity(), 1); // FIXME: delta (use time stamps to have a relative ideia)
		return predictedPosition.inRange(newBeacon.getPosition(), Resources.ACCEPTABLE_DATA_TRUST_VARIANCE);
	}

	public boolean isRevoked(SignedDTO beacon) throws RemoteException {
		SignedCertificateDTO certToRevoke = new SignedCertificateDTO(beacon.getSenderCertificate(), this.getCertificate(), this.getPrivateKey());
		return RSU.isRevoked(certToRevoke);
	}


	// --------------------------
	// --- VICINITY FUNCTIONS ---
	// --------------------------
	public boolean vicinityContains(X509Certificate cert) {
		return vicinity.containsKey(cert);
	}

	public void updateVicinity(X509Certificate cert, BeaconDTO beacon) {
		vicinity.put(cert, beacon);
	}

	public void removeToVicinity(X509Certificate cert) {
		vicinity.remove(cert);
	}


	// -------------------------
	// --- UTILITY FUNCTIONS ---
	// -------------------------
	@Override
	public String toString() {
		String res;
		res = "Vehicle: <id>=" + VIN + "; ";
		res += "<pos>=(" + this.position.x + ", " + this.position.y + "); ";
		res += "<vel>=(" + this.velocity.x + ", " + this.velocity.y + ");";
		return res;
	}
}
