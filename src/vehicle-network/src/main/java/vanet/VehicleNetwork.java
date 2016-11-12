package vanet;

import entity.vanet.RemoteVehicleService;
import entity.vanet.Vehicle;
import remote.RemoteVehicleInterface;
import remote.VehicleDTO;
import java.util.ArrayList;

/*
	Simulate physical wireless network
*/
public class VehicleNetwork {

	private ArrayList<RemoteVehicleInterface> network;

	public VehicleNetwork() {
		this.network = new ArrayList<RemoteVehicleInterface>();

		// @TODO: Launch vehicle process
		// @TODO: Have a way to add new vehicles and maybe even remove
	}


	public static void main( String[] args ) {
		System.out.println( "Hello World!" );

		VehicleNetwork vanet = new VehicleNetwork();
		// @TODO: Lauch RemoteVehicleNetworkService
	}
}
