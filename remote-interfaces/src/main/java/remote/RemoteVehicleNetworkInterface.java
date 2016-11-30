package remote;

import globals.SignedBeaconDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.cert.Certificate;

public interface RemoteVehicleNetworkInterface extends Remote {

	/**
	 * Simulates beacon propagation to nearby network nodes
	 * @param	String			name of the vehicle in the network
	 * @param	SignedBeaconDTO		data transfer object containing signed beacon data
	 */
	public void simulateBeaconBroadcast(String name, SignedBeaconDTO beacon) throws RemoteException;

	/**
	 * Adds a node (vehicle) to the network
	 * @param	String	vehicle name in the netowork
	 * @return	boolean	true if node was added sucessfully
	 */
	public boolean addVehicle(String name) throws RemoteException;

	/**
	 * Removes a node (vehicle) to the network
	 * @param	String	vehicle name in the netowork
	 * @return	boolean	true if node was removed sucessfully
	 */
	public boolean removeVehicle(String name) throws RemoteException;

	/**
	 * Gets the next available name in the network
	 * @return	String	next available name
	 */
	public String getNextVehicleName() throws RemoteException;
}