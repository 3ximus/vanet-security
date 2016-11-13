package entity.vanet;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

import java.rmi.RemoteException;

import globals.Resources;
import remote.RemoteVehicleInterface;
import remote.Vector2Df;
import remote.VehicleDTO;

public class RemoteVehicleService implements RemoteVehicleInterface {
	private boolean isPublished = false;
	private String name;

	private Vehicle vehicle;

	public RemoteVehicleService(Vehicle vehicle, String name) {
		this.vehicle = vehicle;
		this.name = name;
	}

	public Vector2Df simulateGetPosition() throws RemoteException {
		return vehicle.getPosition();

	}

	public void receiveBeaconMessage(VehicleDTO vehicleInfo) throws RemoteException {
		// @TODO: Check security requirements
		vehicle.simulateSensors(vehicleInfo);
	}

	public void publish() {
		if(isPublished == true) {
			System.out.println("[Vehicle] Tried to publish. Vehicle already published. Doing nothing.");
			return;
		}

		try {
			RemoteVehicleInterface stub = (RemoteVehicleInterface) UnicastRemoteObject.exportObject(this, 0); // zero means anonymous port
			Registry registry;
			try {
				registry = LocateRegistry.createRegistry(Resources.REGISTRY_PORT);
			} catch(ExportException e) {
				registry = LocateRegistry.getRegistry(Resources.REGISTRY_PORT);
			}
			registry.rebind(name, stub);
			isPublished = true;
			System.out.println("[Vehicle] Remote vehicle called \"" + name + "\" is now published.");
		} catch (Exception e) {
			System.err.println("[Vehicle] Failed to publish remote vehicle. " + e.getClass() + ": " +  e.getMessage());
		}
	}

	public void unpublish() {
		if(isPublished == false) {
			System.out.println("[Vehicle] Tried to unpublish. Vehicle was never published. Doing nothing.");
			return;
		}

		try {
			Registry registry = LocateRegistry.getRegistry(Resources.REGISTRY_PORT);
			registry.unbind(name);
			UnicastRemoteObject.unexportObject(this, true);
		} catch (Exception e) {
			System.err.println("[Vehicle] Failed to unpublish remote vehicle. " + e.getClass() + ": " +  e.getMessage());
		}
	}
}