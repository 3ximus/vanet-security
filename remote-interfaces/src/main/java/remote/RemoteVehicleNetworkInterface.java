package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.cert.Certificate;

public interface RemoteVehicleNetworkInterface extends Remote {
	public void simulateBeaconBroadcast(String name, VehicleDTO beacon, Certificate senderCertificate, byte[] signature) throws RemoteException;
	public boolean addVehicle(String name) throws RemoteException;
	public boolean removeVehicle(String name) throws RemoteException;
	public String getNextVehicleName() throws RemoteException;
}