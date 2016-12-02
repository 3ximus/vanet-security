package gateway;

import globals.Resources;
import remote.RemoteRSUInterface;
import remote.RemoteCAInterface;
import remote.RemoteVehicleNetworkInterface;

import globals.SignedCertificateDTO;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RemoteRSUService implements RemoteRSUInterface {

	private RSU rsu;
	private boolean isPublished = false;
	private RemoteCAInterface ca;
	private RemoteVehicleNetworkInterface vehicle_network;

	public RemoteRSUService(RSU rsu, RemoteCAInterface ca, RemoteVehicleNetworkInterface vehicle_network) {
		this.rsu = rsu;
		this.ca = ca;
		this.vehicle_network = vehicle_network;
	}

	// Called by vehicle
	// forwards request to ca
	// returns result to vehicle
	@Override
	public boolean isRevoked(SignedCertificateDTO dto) throws RemoteException {

		if(!authenticateSender(dto))
			return false;

		// verify if revoked certificate is in cache
		if(rsu.isCertInCache(dto.getCertificate())) {
			System.out.println(Resources.OK_MSG("Certificate is revoked"));
			return true;
		}

		// Contact CA with possible revoked certificate
		if(ca.isRevoked(new SignedCertificateDTO(dto.getCertificate() ,rsu.getCertificate(), rsu.getPrivateKey()))) {

			try { rsu.addCertificateToCache(dto.getCertificate());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			System.out.println(Resources.OK_MSG("Certificate is revoked"));
			return true;
		}

		return false;
	}

	@Override
	public boolean tryRevoke(SignedCertificateDTO dto) throws RemoteException {

		if(!authenticateSender(dto))
			return false;

		if(ca.tryRevoke(dto)) {
			try {
				rsu.addCertificateToCache(dto.getCertificate());
			} catch(Exception e) {
				System.out.println(Resources.WARNING_MSG(e.getMessage()));
			}

			informVehiclesOfRevocation(
						new SignedCertificateDTO(dto.getCertificate(), rsu.getCertificate(), rsu.getPrivateKey()));
			return true;
		}
		return false;
	}

	// Called by the CA
	public void shareRevoked(SignedCertificateDTO dto) throws RemoteException {

	}

	// ------ INTERNAL METHODS --------

	/**
	 * Verifies if certificate was signed by the CA
	 * Verifies if its not revoked (cached or contact CA)
	 * Verfies Signature
	 * If no verification fails returns true
	 */
	private boolean authenticateSender(SignedCertificateDTO dto) throws RemoteException {

		// verify if certificate was signed by CA
		if (!dto.verifyCertificate(this.rsu.getCACertificate())) {
			System.out.println(Resources.WARNING_MSG("Invalid CA Signature on isRevoked request: " + dto.toString()));
			return false;  // certificate was not signed by CA, isRevoked  request is dropped
		}

		// if certificate was revoked, request is dropped
		if(rsu.isCertInCache(dto.getSenderCertificate())) {
			System.out.println(Resources.WARNING_MSG("Sender's Certificate is revoked"));
			return false;
		}

		// Contact CA to check if senders certificate is revoked
		if(ca.isRevoked(new SignedCertificateDTO(dto.getSenderCertificate() ,rsu.getCertificate(), rsu.getPrivateKey()))) {
			System.out.println(Resources.WARNING_MSG("Sender's Certificate is revoked"));
			return false;
		}

		// verify signature sent
		if (!dto.verifySignature()) {
			System.out.println(Resources.WARNING_MSG("Invalid digital signature on isRevoked request: " + dto.toString()));
			return false;  // certificate was not signed by sender
		}

		return true; // Sender is authenticated
	}

	private void informVehiclesOfRevocation(SignedCertificateDTO dto) throws RemoteException {
		vehicle_network.informVehiclesOfRevocation(dto);
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