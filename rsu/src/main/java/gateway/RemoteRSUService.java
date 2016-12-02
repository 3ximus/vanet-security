package gateway;

import globals.Resources;
import globals.SignedBeaconDTO;
import globals.Vector2D;
import remote.RemoteRSUInterface;
import remote.RemoteCAInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.cert.Certificate;

public class RemoteRSUService implements RemoteRSUInterface {

	private RSU rsu;
	private boolean isPublished = false;
	private RemoteCAInterface ca;

	public RemoteRSUService(RSU rsu, RemoteCAInterface ca) {
		this.rsu = rsu;
		this.ca = ca;
	}

	// Called by the vehicle network
	// forwards request to ca
	// returns result to network
	@Override
	public boolean isRevoked(Certificate certToVerify, Certificate senderCert, byte[] signature) throws RemoteException {
		// verify signature sent
		// create my own signature
		// send msg to ca

		// boolean isCertificateValid
		//		= ca.checkCertificate(senderCertificate, myCertificate, mysignature);

		// if(isCertificateValid) {
		// 	try {
		// 		rsu.addCertificateToCache(senderCertificate);
		// 	} catch (Exception e) {
		// 		System.out.println(e.getMessage());
		// 	}
		//}

		// Enviar resultado para a vanet
		// return isCertificateValid;
		return true; 
	}

	@Override
	public boolean tryRevoke(Certificate certToRevoke, Certificate senderCertificate, byte[] signature) throws RemoteException {
		// Called by a vehicle
		// Check if sender vehicle has sent too many tryRevoke requests
		// Forward to CA either:
		// - sender's certificate (due to having too many tries)
		// - the certificate "to be" revoked
		// return true if it was actually revoked
		ca.tryRevoke(certToRevoke, senderCertificate, signature);
		return true;
	}

	public void shareRevoked(/* TODO add argumensts */) throws RemoteException {

	}

	public void enforceRevocation(/* TODO add argumensts */) throws RemoteException {

	}

	@Override
	public String ping(String msg) {
		String out_msg = Resources.OK_MSG("ping:" + msg);
		System.out.println(out_msg);
		return out_msg;
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