package entity.vanet;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import globals.Resources;
import remote.RemoteVehicleInterface;
import remote.Vector2Df;
import remote.VehicleDTO;

import java.security.PublicKey;


import java.security.cert.Certificate;

import javax.sound.sampled.ReverbType;

public class RemoteVehicleService implements RemoteVehicleInterface {
	private boolean isPublished = false;
	private String name;

	private Vehicle vehicle;

	public RemoteVehicleService(Vehicle vehicle, String name) {
		this.vehicle = vehicle;
		this.name = name;
	}

// ------ INTERFACE METHODS --------

	public Vector2Df simulateGetPosition() throws RemoteException {
		return vehicle.getPosition();

	}

	public void receiveBeaconMessage(VehicleDTO vehicleInfo, Certificate senderCertificate, byte[] signature) throws RemoteException {

		// Secure beacon

		// verify if certificate was signed by CA
		Certificate caCert = null;
		try { caCert = Resources.getCertificateFromKeystore(this.vehicle.getKeystore(), Resources.CA_NAME); }
		catch (Exception e) {
			System.out.println(Resources.WARNING_MSG("Failed to verify CA digital signature of beacon sender: " + e.getMessage()));
			System.out.println(Resources.WARNING_MSG("Droping Beacon: " + vehicleInfo.toString()));
			return;
		}
		if (! Resources.verifySignedCertificate(senderCertificate, caCert.getPublicKey())) {
			System.out.println(Resources.WARNING_MSG("Invalid CA Signature on beacon: " + vehicleInfo.toString()));
			return;  // certificate was not signed by CA, beacon is dropped
		}

		// Contact RSU:
		//String certificateCheckForRSU = senderCertificate.toString() + vehicle.getCertificate().toString();
		//byte[] sig = Resources.makeDigitalSignature(certificateCheckForRSU.getBytes(), vehicle.getPrivateKey());
		//RSU.checkCertificate(senderCertificate, vehicle.getCertificate(), sig);
		// if ^^^^^ false; then return;

		PublicKey senderPubKey = senderCertificate.getPublicKey();
		String messageContent = vehicleInfo.toString() + senderCertificate.toString();
		try {
			Resources.verifyDigitalSignature(signature, messageContent.getBytes(), senderPubKey); }
		catch (Exception e ) {
			System.out.println(Resources.WARNING_MSG("Invalid digital signature on beacon: " + vehicleInfo.toString()));
			return;  // certificate was not signed by sender, beacon is dropped
		}

		// end of security checks, process beacon

		vehicle.simulateBrain(vehicleInfo);
	}
// -------------------------------



// ------ REGISTRY METHODS --------

	public void publish() {
		if(isPublished == true) {
			System.out.println(Resources.WARNING_MSG("Vehicle already published."));
			return;
		}

		try {
			RemoteVehicleInterface stub = (RemoteVehicleInterface) UnicastRemoteObject.exportObject(this, 0); // zero means anonymous port
			Registry registry = LocateRegistry.getRegistry(Resources.REGISTRY_PORT);
			registry.rebind(name, stub);
			isPublished = true;
			System.out.println(Resources.OK_MSG("Published vehicle: " + name + "."));
		} catch (Exception e) {
			System.err.println(Resources.ERROR_MSG("Failed to publish remote vehicle: " +  e.getMessage()));
		}
	}

	public void unpublish() {
		if(isPublished == false) {
			System.out.println(Resources.WARNING_MSG("Unpublishing vehicle that was never published"));
			return;
		}

		try {
			Registry registry = LocateRegistry.getRegistry(Resources.REGISTRY_PORT);
			registry.unbind(name);
			UnicastRemoteObject.unexportObject(this, true);
		} catch (Exception e) {
			System.err.println(Resources.ERROR_MSG("Failed to unpublish remote vehicle: " + e.getMessage()));
		}
	}
}