package remote;


import globals.Vector2D;
import globals.SignedBeaconDTO;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteVehicleInterface extends Remote {

	/**
	 * Gets simulated vehicle position
	 */
	public Vector2D simulateGetPosition() throws RemoteException;

	/**
	 * Receives a beacon message from other vehicle
	 * @param	SignedBeaconDTO		data transfer object containing signed beacon data
	 */
	public void receiveBeaconMessage(SignedBeaconDTO beacon) throws RemoteException;

}