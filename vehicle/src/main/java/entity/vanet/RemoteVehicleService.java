package entity.vanet;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import globals.Resources;
import globals.Vector2D;
import globals.SignedBeaconDTO;
import globals.SignedCertificateDTO;

import remote.RemoteVehicleInterface;

public class RemoteVehicleService implements RemoteVehicleInterface {
	private boolean isPublished = false;
	private String name;

	private Vehicle vehicle;

	public RemoteVehicleService(Vehicle vehicle, String name) {
		this.vehicle = vehicle;
		this.name = name;
	}

// ------ INTERFACE METHODS --------
	@Override
	public Vector2D simulateGetPosition() throws RemoteException {
		return vehicle.getPosition();

	}

	@Override
	public void receiveBeaconMessage(SignedBeaconDTO beacon) throws RemoteException {
		
		// TODO: Only do this security check periodically, not for every beacon
		// Verify that sender is trustworthy
		try {
			if(!authenticateSender(beacon))
				return;
		} catch(RemoteException e) {
			System.out.println(Resources.ERROR_MSG("RSU seems dead... Exiting..."));
			System.exit(-1);
		}

		// end of security checks, process beacon

		vehicle.simulateBrain(beacon);
	}

	@Override
	public void addRevokedCertificate(SignedCertificateDTO dto) {
		// TODO1: verify that dto is from RSU 
		// TODO2: Add cache to revoked ceritificate cache to vehicle if it isnt there already
	}

// ------ INTERNAL METHODS --------

	/**
	 * Verifies if certificate was signed by the CA
	 * Verifies if its not revoked (cached or contact CA through rsu)
	 * Verfies Signature
	 * If no verification fails returns true
	 */

	private boolean authenticateSender(SignedBeaconDTO beacon) throws RemoteException {
		// verify if certificate was signed by CA
		if (! beacon.verifyCertificate(this.vehicle.getCACertificate())) {
			System.out.println(Resources.WARNING_MSG("Invalid CA Signature on beacon: " + beacon.toString()));
			return false;  // certificate was not signed by CA, beacon is dropped
		}

		// Contact RSU:
		if(vehicle.isRevoked(beacon)) {
			System.out.println(Resources.WARNING_MSG("Sender's Certificate is revoked"));
			return false; // certificate was revoked, beacon is dropped
		}

		// Verify digital signature
		if ( ! beacon.verifySignature()) {
			System.out.println(Resources.WARNING_MSG("Invalid digital signature on beacon: " + beacon.toString()));
			return false;  // certificate was not signed by sender, beacon is dropped
		}

		return true;
	}

	/**
	 * Verifies if certificate was signed by the CA
	 * Verfies Signature
	 * If no verification fails returns true
	 */
	
	// private boolean authenticateSender(SignedCertificateDTO rsu_dto) throws RemoteException {

	// } 


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