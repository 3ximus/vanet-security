package entity.vanet;

import java.util.Map;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import globals.Resources;
import remote.Vector2Df;
import remote.VehicleDTO;
import remote.RemoteVehicleNetworkInterface;
import java.security.cert.Certificate;
import java.security.PrivateKey;
import java.security.KeyStore;

public class Vehicle {
	public static final int BEACON_DELTA_MILISSECONDS = 200;
	public static final int IN_DANGER_RESET_MILISSECONDS = 1000;

	private String VIN;
	private Vector2Df position;
	private Vector2Df velocity;
	private RemoteVehicleNetworkInterface VANET;
	private String nameInVANET;
	private String internalName; // only used to retrieve the correct certificate

	private Certificate myCert;
	private PrivateKey myPrKey;

	private boolean inDanger = false;
	private Timer resetInDangerTimer = new Timer();
	class ResetInDangerTask extends TimerTask {
		@Override
		public void run() {
			inDanger = false;
		}
	}
	private TimerTask resetInDangerTask = new ResetInDangerTask();

	private Timer timer = new Timer();
	private TimerTask engineTask = new TimerTask() {
		@Override
		public void run() {
			simulatePositionUpdate();
			beacon();
		}
	};

	private Map<String, VehicleDTO> vicinity = new HashMap<>(); // String is the pseudonim certificate (is it?)

//  -----------------------------------



//  ------- CONSTRUCTOR  ------------

	public Vehicle(String VIN, String certificateName, Vector2Df position, Vector2Df velocity) {
		this.VIN = VIN;
		this.position = position;
		this.velocity = velocity;

		this.internalName=certificateName;

		String certsDir = Resources.CERT_DIR+this.internalName+"/";
		// Read certificate file to a certificate object
		try {
			this.myCert = Resources.readCertificateFile(certsDir+this.internalName+".cer"); }
		catch (Exception e) {
			System.out.println(Resources.ERROR_MSG("Error Loading certificate: "+e.getMessage()));
			System.out.println(Resources.ERROR_MSG("Exiting. Vehicle is useless without certificate"));
			System.exit(1);
		}
		try {
			KeyStore keystore = Resources.readKeystoreFile(certsDir+this.internalName+".jks", Resources.STORE_PASS);
			this.myPrKey = Resources.getPrivateKeyFromKeystore(keystore, this.internalName, Resources.KEY_PASS); }
		catch (Exception e) {
			System.out.println(Resources.ERROR_MSG("Error Loading PrivateKey: "+e.getMessage()));
			System.out.println(Resources.ERROR_MSG("Exiting. Vehicle is useless without PrivateKey"));
			System.exit(1);
		}
	}

//  ------- GETTERS  ------------

	public Vector2Df getPosition() { return this.position; }
	public Vector2Df getVelocity() { return this.velocity; }


	// Sets VANET, starts updating position and starts beaoning
	public void start(RemoteVehicleNetworkInterface VANET, String name) {
		this.VANET = VANET;
		this.nameInVANET = name;

		// Run the engine and beaconing on a timer
		timer.scheduleAtFixedRate(engineTask, 0, BEACON_DELTA_MILISSECONDS);
	}

//  ------- MAIN METHODS  ------------

	public void beacon() {
		if(VANET == null) return;

		// TODO as soon as the timestamp passed the real thing uncoment the real to string method in vehicleDTO
		VehicleDTO dto = new VehicleDTO(position, velocity, null); // @FIXME: change null to current time
		byte[] sig = null;
		// Calculate digital signature of the content
		String serializedMessage = nameInVANET + dto.toString() + myCert.toString();
		try {
			sig = Resources.makeDigitalSignature(serializedMessage.getBytes(), this.myPrKey); }
		catch (Exception e) {
			System.out.println(Resources.ERROR_MSG("Failed to create signature: "+e.getMessage()));
			return;
		}

		try {
			VANET.simulateBeaconBroadcast(nameInVANET, dto, myCert, sig);
		} catch(Exception e) {
			System.out.println(Resources.ERROR_MSG("Unable to beacon message: " + e.getMessage()));
			System.out.println(Resources.ERROR_MSG("VANET seems dead... Exiting..."));
			System.exit(-1);
		}
	}

	public void simulatePositionUpdate() {
		if(inDanger == false) {
			position.x = position.x + velocity.x;
			position.y = position.y + velocity.y;
		}
	}

	public void simulateBrain(VehicleDTO vehicleInfo) {
		System.out.println("Simulating brain..");

		// @TODO: Register on the list if new, or update if not
		// @TODO: If was already registered use a data trust function that , according to previous records of the
		// @TODO: same vehicle, decides whether to believe it or not

		if(isVehicleDangerous(vehicleInfo) == true) {
			if(inDanger = true) {
				resetInDangerTimer.cancel();
			}
			inDanger = true;
			resetInDangerTimer = new Timer();
			resetInDangerTimer.schedule(new ResetInDangerTask(), IN_DANGER_RESET_MILISSECONDS);

		}
	}

	private boolean isVehicleDangerous(VehicleDTO vehicleInfo) {
		System.out.println(Resources.WARNING_MSG("DANGER!"));
		return vehicleInfo.getPosition().distance(getPosition()) <= Resources.TOO_DANGEROUS_RANGE;
	}


//  ------- UTILITY ------------

	@Override
	public String toString() {
		String res;
		res = "Vehicle: <id>=" + VIN + "; ";
		res += "<pos>=(" + position.x + ", " + position.y + "); ";
		res += "<pos>=(" + position.x + ", " + position.y + ");";
		return res;
	}
}
