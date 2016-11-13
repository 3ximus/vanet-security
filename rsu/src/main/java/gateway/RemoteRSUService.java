package gateway;


import remote.RemoteRSUInterface;
// import security.RemoteCAService;
import vanet.RemoteVehicleNetworkService;

public class RemoteRSUService implements RemoteRSUInterface {

	// private RemoteCAService ca;
	private RemoteVehicleNetworkService vehicle_network;

	// public RemoteRSUService(RemoteCAService ca, RemoteVehicleNetworkService network) {
	// 	this.ca = ca;
	// 	this.vehicle_network = network;
	// }	

	// TODO: remove. So aqui para compilar sem erros
	public RemoteRSUService(RemoteVehicleNetworkService network) {
		this.vehicle_network = network;
	}	

	// called by the vehicle network
	// forwards request to ca.
	public void receiveAuthenticationRequest(String pubKey) {
		// forward to ca
		// ca.receiveAuthenticationRequest(pubkey)
	}

	// called by the Certificate Authority
	public void receiveAuthenticationResponse(String pubkey) {
		//forward to vehicle network
		//vehicle_network.receiveAuthenticationResponse(pubkey);
	}

	public void receiveRevokeCertificateRequest(String certificate) {
		//TODO:
	}

}