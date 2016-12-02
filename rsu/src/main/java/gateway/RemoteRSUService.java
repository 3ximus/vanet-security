package gateway;

import globals.Resources;
import remote.RemoteRSUInterface;
import remote.RemoteCAInterface;

import globals.SignedCertificateDTO;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RemoteRSUService implements RemoteRSUInterface {

	private RSU rsu;
	private boolean isPublished = false;
	private RemoteCAInterface ca;

	public RemoteRSUService(RSU rsu, RemoteCAInterface ca) {
		this.rsu = rsu;
		this.ca = ca;
	}

	// Called by vehicle
	// forwards request to ca
	// returns result to vehicle
	@Override
	public boolean isRevoked(SignedCertificateDTO dto) throws RemoteException {

		// verify if certificate was signed by CA
		if (!dto.verifyCertificate(this.rsu.getCACertificate())) {
			System.out.println(Resources.WARNING_MSG("Invalid CA Signature on isRevoked request: " + dto.toString()));
			return false;  // certificate was not signed by CA, isRevoked  request is dropped
		}

		// if certificate was revoked, request is dropped
		if(rsu.isCertInCache(dto.getSenderCertificate())) {
			System.out.println(Resources.ERROR_MSG("Sender's Certificate is revoked"));
			return false;
		}

		// create my own signature
		// send msg to ca
		// Contact RSU:
		// String certificateCheckForRSU = senderCertificate.toString() + vehicle.getCertificate().toString();
		// byte[] sig = Resources.makeDigitalSignature(certificateCheckForRSU.getBytes(), vehicle.getPrivateKey());
		// RSU.checkCertificate(senderCertificate, vehicle.getCertificate(), sig);
		// if ^^^^^ false; then return;

		// verify signature sent
		if (!dto.verifySignature()) {
			System.out.println(Resources.WARNING_MSG("Invalid digital signature on isRevoked request: " + dto.toString()));
			return false;  // certificate was not signed by sender, isRevoked is dropped
		}

		// end of security checks

		// verify if revoked certificate is in cache
		if(rsu.isCertInCache(dto.getCertificate())) {
			System.out.println(Resources.ERROR_MSG("Certificate is revoked"));
			return true;
		}

		// Contact CA with possible revoked certificate
		// create my own signature
		// send msg to ca

		// String certificateCheckForRSU = senderCertificate.toString() + vehicle.getCertificate().toString();
		// byte[] sig = Resources.makeDigitalSignature(certificateCheckForRSU.getBytes(), vehicle.getPrivateKey());
		// RSU.checkCertificate(senderCertificate, vehicle.getCertificate(), sig);
		// if ^^^^^ false; then return;

		// if(isCertificateValid) {
		// 	try {
		// 		rsu.addCertificateToCache(senderCertificate);
		// 	} catch (Exception e) {
		// 		System.out.println(e.getMessage());
		// 	}
		//}

		return true;
	}

	@Override
	public boolean tryRevoke(SignedCertificateDTO dto) throws RemoteException {
		// Called by a vehicle
		// Check if sender vehicle has sent too many tryRevoke requests
		// Forward to CA either:
		// - sender's certificate (due to having too many tries)
		// - the certificate "to be" revoked
		// return true if it was actually revoked
		ca.tryRevoke(dto);
		return true;
	}

	public void shareRevoked(SignedCertificateDTO dto) throws RemoteException {

	}

	public void informVehiclesOfRevocation(SignedCertificateDTO dto) throws RemoteException {

	}

	// ------ REGISTRY METHODS --------

	public void publish() {
		if(isPublished) {
			System.out.println(Resources.WARNING_MSG(Resources.RSU_NAME+" already published."));
			return;
		}

		try {
			RemoteRSUInterface stub = (RemoteRSUInterface) UnicastRemoteObject.exportObject(this, 0);
			Registry registry;
			registry = LocateRegistry.getRegistry(Resources.REGISTRY_PORT);
			registry.rebind(Resources.RSU_NAME, stub);
			isPublished = true;
			System.out.println(Resources.OK_MSG(Resources.RSU_NAME+" published to registry."));
		} catch (Exception e) {
			System.err.println(Resources.ERROR_MSG("Failed to publish remote RSU: " + e.getMessage()));
		}
	}

	public void unpublish() {
		if(!isPublished) {
			System.out.println(Resources.WARNING_MSG("Unpublishing "+Resources.RSU_NAME+" that was never published."));
			return;
		}

		try {
			Registry registry = LocateRegistry.getRegistry(Resources.REGISTRY_PORT);
			registry.unbind(Resources.RSU_NAME);
			UnicastRemoteObject.unexportObject(this, true);
		} catch (Exception e) {
			System.err.println(Resources.ERROR_MSG("Unpublishing RSU: " + e.getMessage()));
		}
	}

}