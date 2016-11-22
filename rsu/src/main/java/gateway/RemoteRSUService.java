package gateway;

import globals.Resources;
import remote.RemoteRSUInterface;
import remote.RemoteCAInterface;
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
	public boolean authenticate(Certificate certToVerify, Certificate senderCert, byte[] signature) {
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
		return true; // TODO: Remove
	}

	// Called by vehicle-network
	// forwards to ca
	// returns result to network
	@Override
	public void revokeCertificate(Certificate certToRevoke, Certificate senderCertificate, byte[] signature) {
		// TODO:
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