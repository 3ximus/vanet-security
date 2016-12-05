package entity.vanet;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import java.security.cert.X509Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;

import globals.Resources;
import globals.Vector2D;
import globals.SignedBeaconDTO;
import globals.SignedCertificateDTO;
import globals.SignedDTO;


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
		// TODO: Maybe here or just have a function that receives a normal beaconDTO

		// Verify that sender is trustworthy		
		if(!authenticateBeaconMessage(beacon))
			return;
		
		vehicle.updateVicinity(beacon.getSenderCertificate(), beacon.getPosition());
		
		// Process beacon
		vehicle.simulateBrain(beacon);
	}

	@Override
	public void addRevokedCertificate(SignedCertificateDTO dto) {
		// verify that dto is from a trustworthy RSU
		if(!authenticateAddRevokedCertificate(dto))
			return;
		
		// TODO: Add revoked ceritificate to cache 
	}

// --------------------------------
// ------ INTERNAL METHODS --------
// --------------------------------
	// Authenticate a beaconing message
	private boolean authenticateBeaconMessage(SignedBeaconDTO dto) {
		// If we have it in the cache: 
		//    - We DO NOT need to check if the certificate is signed by the CA or if it is revoked
		//    - We DO need to confirm the expiration date and if the message signature is correct 
		X509Certificate senderCert = dto.getSenderCertificate();
		if(!vehicle.vicinityContains(senderCert)) {
			if(!authenticateSenderCert(dto)) 
				return false;
			
			vehicle.updateVicinity(senderCert, dto.getPosition());
		}

		return authenticateSenderSignature(dto);
	}

	// Authenticate a message received from de RSU
	private boolean authenticateAddRevokedCertificate(SignedDTO dto) {
		if(!authenticateSenderCert(dto))
			return false;
		return authenticateSenderSignature(dto);
	}

	/*
	 * Verifies if certificate was signed by the CA
	 * Verifies if its not revoked (cached or contact CA through rsu)
	*/
	private boolean authenticateSenderCert(SignedDTO dto) {
		// TODO: Verify if it is in the revoked cache

		
		// Verify if certificate was signed by CA
		if (!dto.verifyCertificate(this.vehicle.getCACertificate())) {
			System.out.println(Resources.WARNING_MSG("Invalid CA Signature on beacon: " + dto.toString()));
			return false;  // certificate was not signed by CA, beacon is dropped
		}

		// Contact RSU, to ensure it is not revoked
		try {
			if(vehicle.isRevoked(dto)) {
				System.out.println(Resources.WARNING_MSG("Sender's Certificate is revoked"));
				return false; // certificate was revoked, beacon is dropped
			}
		} catch(RemoteException e) {
			System.out.println(Resources.ERROR_MSG("RSU seems dead... Cause: " + e.getMessage() + ". Exiting..."));
			System.exit(-1);
			return false;
		}

		return true;
	}

	/*
	 * Verifies if certificate has expired
	 * Verifies dto is signed by the sender
	 */
	private boolean authenticateSenderSignature(SignedDTO dto) {
		// Verify if certificate has expired
		try { 
			dto.getSenderCertificate().checkValidity(); 

		} catch (CertificateExpiredException e) {
			System.out.println(Resources.WARNING_MSG("Sender's Certificate has expired: " + dto.toString()));
			return false;  // certificate has expired, isRevoked request is dropped

		} catch (CertificateNotYetValidException e) {
			System.out.println(Resources.WARNING_MSG("Sender's Certificate is not yet valid: " + dto.toString()));
			return false;  // certificate was not yet valid, isRevoked request is dropped
		} 
		
		// Verify digital signature
		if (!dto.verifySignature()) {
			System.out.println(Resources.WARNING_MSG("Invalid digital signature on beacon: " + dto.toString()));
			return false;  // certificate was not signed by sender, beacon is dropped
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