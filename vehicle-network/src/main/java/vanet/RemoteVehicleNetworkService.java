package vanet;

import entity.vanet.RemoteVehicleService;
import globals.Resources;
import remote.RemoteVehicleInterface;
import remote.RemoteVehicleNetworkInterface;
import remote.VehicleDTO;

import java.rmi.server.ExportException;
import java.util.Map;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RemoteVehicleNetworkService implements RemoteVehicleNetworkInterface {
    private boolean isPublished = false;
    private long nextVehicleNumber = 0;
    private VehicleNetwork vehicleNetwork;

    public RemoteVehicleNetworkService(VehicleNetwork vehicleNetwork) {
        this.vehicleNetwork = vehicleNetwork;
    }

    @Override
    public void simulateBeaconBroadcast(VehicleDTO messageToBeacon) throws RemoteException {
        for(Map.Entry<String, RemoteVehicleInterface> entry: vehicleNetwork.getVehicleEntrySet()) {
            // @TODO: Do something with this information
            entry.getValue().simulateGetPosition();
        }

        // @TODO: for every vehicle in range send message
    }

    @Override
    public boolean addVehicle(String name) throws RemoteException {
        if(vehicleNetwork.hasVehicle(name) == true)
            return false;

        RemoteVehicleInterface vehicleToAdd;
        try {
            Registry registry = LocateRegistry.getRegistry(1099); // @FIXME: only works for localhost
            vehicleToAdd = (RemoteVehicleInterface) registry.lookup(name);
        } catch(Exception e) {
            System.err.println("[VANET] Failed to add vehicle with name " + name + ". " + e.getClass() + ": " +  e.getMessage());
            return false;
        }

        vehicleNetwork.addVehicle(name, vehicleToAdd);
        return true;
    }

    @Override
    public boolean removeVehicle(String name) throws RemoteException {
        if(vehicleNetwork.hasVehicle(name) == true) {
            vehicleNetwork.removeVehicle(name);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized String getNextVehicleName() {
        return "V" + nextVehicleNumber++;
    }

    public void publish() {
        if(isPublished == true) {
            System.out.println("[VANET] Vanet is already published. Doing nothing.");
            return;
        }

        try {
            RemoteVehicleNetworkInterface stub = (RemoteVehicleNetworkInterface) UnicastRemoteObject.exportObject(this, 0);
            Registry registry;
            registry = LocateRegistry.getRegistry(Resources.REGISTRY_PORT);
            registry.rebind(Resources.VANET_NAME, stub);
            isPublished = true;
            System.out.println("[VANET] Remote VANET called \"" + Resources.VANET_NAME+ "\".");
        } catch (Exception e) {
            System.err.println("[VANET] Failed to publish remote VANET. " + e.getClass() + ": " +  e.getMessage());
        }
    }

    public void unpublish() {
        if(isPublished == false) {
            System.out.println("[VANET] Tried to unpublish. VANET was never published. Doing nothing.");
            return;
        }

        try {
            Registry registry = LocateRegistry.getRegistry(Resources.REGISTRY_PORT);
            registry.unbind(Resources.VANET_NAME);
            UnicastRemoteObject.unexportObject(this, true);
        } catch (Exception e) {
            System.err.println("[VANET] Failed to unpublish VANET. " + e.getClass() + ": " +  e.getMessage());
        }
    }
}
