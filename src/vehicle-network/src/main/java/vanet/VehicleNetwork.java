package vanet;

import entity.vanet.Vehicle;
import remote.VehicleDTO;
import java.util.ArrayList;

/*
	Simulate physical wireless network
*/
public class VehicleNetwork {

	private ArrayList<Vehicle> network;

	public VehicleNetwork() {
		// FIXME NOT Vehicle, RemoteVehicleINterface instead
		this.network = new ArrayList<Vehicle>();
		// TODO Launch vehicle process
	}

	public void simulateBeaconBroadcast(VehicleDTO vdto) {
		// TODO:
	}

	public static void main( String[] args ) {
		System.out.println( "Hello World!" );
    }
}
