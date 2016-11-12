package entity.vanet;

import java.rmi.RemoteException;

import remote.RemoteVehicleInterface;
import remote.Vector2Df;
import remote.VehicleDTO;

public class RemoteVehicleService implements RemoteVehicleInterface {

	private Vehicle vehicle;

	public Vector2Df simulateGetPosition() throws RemoteException {
		return vehicle.getPosition();

	}

	public void receiveBeaconMessage(VehicleDTO beacon) throws RemoteException {
		// @TODO:
		// 1. Check security requirements
		// 2. Tell the sensors this information

	}

}