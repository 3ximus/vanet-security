package vanet;

import remote.RemoteVehicleNetworkInterface;
import remote.VehicleDTO;

import java.rmi.RemoteException;

public class RemoteVehicleNetworkService implements RemoteVehicleNetworkInterface {
    VehicleNetwork vehicleNetwork;

    public RemoteVehicleNetworkService(VehicleNetwork vehicleNetwork) {
        this.vehicleNetwork = vehicleNetwork;
    }

    @Override
    public void simulateBeaconBroadcast(VehicleDTO messageToBeacon) throws RemoteException {
        // @TODO: for every vehicle in range send message
    }
}
