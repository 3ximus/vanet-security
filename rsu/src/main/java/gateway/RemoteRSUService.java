package gateway;

import globals.Resources;
import remote.RemoteRSUInterface;
import remote.RemoteVehicleNetworkInterface;
import remote.RemoteCAInterface;

public class RemoteRSUService implements RemoteRSUInterface {

	private RSU rsu;
	private RemoteCAInterface ca;
	private RemoteVehicleNetworkInterface vehicle_network;	

	public RemoteRSUService(RSU rsu, RemoteCAInterface ca, RemoteVehicleNetworkInterface network) {
		this.rsu = rsu;
		this.ca = ca;
		this.vehicle_network = network;
	}	

	// Called by the vehicle network
	// forwards request to ca
	// returns result to network
	public boolean receiveAuthenticationRequest(String senderCertificate, byte[] signature) {
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
	public void receiveRevokeCertificateRequest(String senderCertificate, byte[] signature) {
		// TODO:
	}

	public void ping(String msg) {
		System.out.println(Resources.OK_MSG("ping:" + msg));
	}

}