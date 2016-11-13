package gateway;


import remote.RemoteRSUInterface;
import remote.RemoteVehicleNetworkInterface;
// import remote.RemoteCAInterface;

public class RemoteRSUService implements RemoteRSUInterface {

	private RSU rsu;
	// private RemoteCAInterface ca;
	private RemoteVehicleNetworkInterface vehicle_network;	

	// TODO: Adicionar RemoteCaInterface qd tiver neste branch.
	public RemoteRSUService(RSU rsu, RemoteVehicleNetworkInterface network) {
		this.rsu = rsu;
		this.vehicle_network = network;
		// this.ca = ca;
	}	

	// Called by the vehicle network
	// forwards request to ca.
	public void receiveAuthenticationRequest(String pubKey) {
		// ca.receiveAuthenticationRequest(pubkey)
	}

	// Called by the Certificate Authority
	// forwards request to vehicle network
	public void receiveAuthenticationResponse(String certificate) {
		//vehicle_network.receiveAuthenticationResponse(pubkey);
		try {
			rsu.addCertificateToCache(certificate);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// Called by vehicle-network??
	public void receiveRevokeCertificateRequest(String certificate) {
		// TODO: adicionar condicoes para remover. Nao pode remover a cada pedido.
		try {
			rsu.removeCertificateFromCache(certificate);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}