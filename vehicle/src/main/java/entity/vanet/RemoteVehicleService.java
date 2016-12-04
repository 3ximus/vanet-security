package entity.vanet;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;

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
		// TODO: Add revoked cache to vehicles
		
		// verify that dto is from a trustworthy RSU
		if(!authenticateSender(dto))
			return;
		
		// TODO: Add revoked ceritificate to cache 
		//		 if it isnt there already

	}

// ------ INTERNAL METHODS --------

	/**
	 * Verifies if certificate was signed by the CA
	 * Verifies if certificate has expired
	 * Verifies if its not revoked (cached or contact CA through rsu)
	 * Verifies beacon_dto is signed by the sender
	 * If no verification fails returns true
	 */

	private boolean authenticateSender(SignedBeaconDTO beacon) throws RemoteException {
		// verify if certificate was signed by CA
		if (! beacon.verifyCertificate(this.vehicle.getCACertificate())) {
			System.out.println(Resources.WARNING_MSG("Invalid CA Signature on beacon: " + beacon.toString()));
			return false;  // certificate was not signed by CA, beacon is dropped
		}

		// verify if certificate has expired
		try { beacon.getSenderCertificate().checkValidity(); 
		} catch (CertificateExpiredException e) {
			System.out.println(Resources.WARNING_MSG("Sender's Certificate has expired: " + beacon.toString()));
			return false;  // certificate has expired, isRevoked  request is dropped

		} catch (CertificateNotYetValidException e) {
			System.out.println(Resources.WARNING_MSG("Sender's Certificate is not yet valid: " + beacon.toString()));
			return false;  // certificate was not yet valid, isRevoked  request is dropped
		} 

		// Contact RSU:
		if(vehicle.isRevoked(beacon)) {
			System.out.println(Resources.WARNING_MSG("Sender's Certificate is revoked"));
			return false; // certificate was revoked, beacon is dropped
		}

		// Verify digital signature
		if (! beacon.verifySignature()) {
			System.out.println(Resources.WARNING_MSG("Invalid digital signature on beacon: " + beacon.toString()));
			return false;  // certificate was not signed by sender, beacon is dropped
		}

		return true;
	}

	/**
	 * Verifies if certificate was signed by the CA
	 * Verifies if rsu_dto was signed by the rsu
	 * If no verification fails returns true
	 */
	
	private boolean authenticateSender(SignedCertificateDTO rsu_dto) {
		// verify if certificate was signed by CA
		if (! rsu_dto.verifyCertificate(this.vehicle.getCACertificate())) {
			System.out.println(Resources.WARNING_MSG("Invalid CA Signature on RSU: " + rsu_dto.toString()));
			return false;  // certificate was not signed by CA, rsu inform of revocation is dropped
		}

		// TODO: more security checks?

		// Verify digital signature
		if (! rsu_dto.verifySignature()) {
			System.out.println(Resources.WARNING_MSG("Invalid digital signature on RSU: " + rsu_dto.toString()));
			return false;  // certificate was not signed by sender, rsu inform of revocation is dropped
		}

		return true;
	} 


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