package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteVehicleNetworkInterface extends Remote {
    public void simulateBeaconBroadcast(VehicleDTO beacon) throws RemoteException;
}