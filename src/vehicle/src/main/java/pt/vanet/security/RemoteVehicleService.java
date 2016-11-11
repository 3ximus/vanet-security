package pt.vanet.security;

import vanet.RemoteVehicleInterface;

public class RemoteVehicleService implements RemoteVehicleInterface {

	private Vehicle vehicle;

	public int[] simulateGetPosition() throws RemoteException {
		return vehicle.getPosition();

	}

	public void receiveBeaconMessage(VehicleDTO beacon) throws RemoteException {

	}

}