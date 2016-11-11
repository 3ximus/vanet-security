package entity.vanet;

import java.rmi.RemoteException;

import remote.RemoteVehicleInterface;
import remote.VehicleDTO;

public class RemoteVehicleService implements RemoteVehicleInterface {

	private Vehicle vehicle;

	public int[] simulateGetPosition() throws RemoteException {
		return vehicle.getPosition();

	}

	public void receiveBeaconMessage(VehicleDTO beacon) throws RemoteException {

	}

}