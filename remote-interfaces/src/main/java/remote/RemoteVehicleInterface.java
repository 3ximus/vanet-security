package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.cert.Certificate;

public interface RemoteVehicleInterface extends Remote {

	/**
	 * Gets simulated vehicle position
	 */
	public Vector2Df simulateGetPosition() throws RemoteException;

	/**
	 * Receives a beacon message from other vehicle
	 * @param	VehicleDTO		data transfer object containing beacon data
	 * @param	Certificate		certificate of beacon's author
	 * @param	byte[]			message digital signature
	 */
	public void receiveBeaconMessage(VehicleDTO beacon, Certificate senderCertificate, byte[] signature) throws RemoteException;

}