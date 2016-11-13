package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteVehicleNetworkInterface extends Remote {
    void simulateBeaconBroadcast(VehicleDTO beacon) throws RemoteException;
    boolean addVehicle(String name) throws RemoteException;
    boolean removeVehicle(String name) throws RemoteException;
    String getNextVehicleName() throws RemoteException;
}