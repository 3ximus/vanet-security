package entity.vanet;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import java.rmi.RemoteException;

import remote.RemoteVehicleInterface;
import remote.Vector2Df;
import remote.VehicleDTO;

public class RemoteVehicleService implements RemoteVehicleInterface {
	private boolean isPublished = false;

	private Vehicle vehicle;

	public Vector2Df simulateGetPosition() throws RemoteException {
		return vehicle.getPosition();

	}

	public void receiveBeaconMessage(VehicleDTO vehicleInfo) throws RemoteException {
		// @TODO:
		// 1. Check security requirements
		vehicle.simulateSensors(vehicleInfo);
	}

	public void publish(String name, int port) {
		if(isPublished == true) {
			System.out.println("[Vehicle] Vehicle already published. Doing nothing.");
			return;
		}

		try {
			RemoteVehicleInterface stub = (RemoteVehicleInterface) UnicastRemoteObject.exportObject(this, port);
			Registry registry;
			registry = LocateRegistry.createRegistry(1099);
			registry.rebind(name, stub);
			isPublished = true;
			System.out.println("[Vehicle] Remote vehicle called \"" + name + "\" is now published on port " + port + ".");
		} catch (Exception e) {
			System.err.println("[Vehicle] Failed to publish remote vehicle. " + e.getClass() + ": " +  e.getMessage());
		}
	}
}