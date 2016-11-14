package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.cert.Certificate;

public interface RemoteVehicleInterface extends Remote {
	public Vector2Df simulateGetPosition() throws RemoteException;

	// TODO missing security Arguments ( signature )
	public void receiveBeaconMessage(VehicleDTO beacon, Certificate senderCertificate, byte[] signature) throws RemoteException;

}