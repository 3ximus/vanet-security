package vanet;

import entity.vanet.RemoteVehicleService;
import remote.RemoteVehicleInterface;
import remote.RemoteVehicleNetworkInterface;

import globals.Vector2D;
import globals.Resources;
import globals.SignedBeaconDTO;
import globals.SignedCertificateDTO;

import java.util.Map;
import java.rmi.server.ExportException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.cert.Certificate;

public class RemoteVehicleNetworkService implements RemoteVehicleNetworkInterface {
	private boolean isPublished = false;
	private long nextVehicleNumber = 0;
	private VehicleNetwork vehicleNetwork;

	public RemoteVehicleNetworkService(VehicleNetwork vehicleNetwork) {
		this.vehicleNetwork = vehicleNetwork;
	}

// ------ INTERFACE METHODS --------

	@Override
	public void simulateBeaconBroadcast(String name, SignedBeaconDTO beacon) throws RemoteException {
		Vector2D sendingVehiclePos = beacon.getPosition();
		for(Map.Entry<String, RemoteVehicleInterface> entry: vehicleNetwork.getVehicleEntrySet()) {
			if(entry.getKey().equals(name)) continue;

			RemoteVehicleInterface remoteVehicle = entry.getValue();

			try {
				Vector2D remoteVehiclePos = remoteVehicle.simulateGetPosition();
				if(VehicleNetwork.inRange(sendingVehiclePos, remoteVehiclePos)) {
					remoteVehicle.receiveBeaconMessage(beacon);
				}
			} catch(RemoteException e) {
				System.out.println(Resources.WARNING_MSG("Vehicle \"" + entry.getKey() + "\" seems to be dead."));
				vehicleNetwork.removeVehicle(entry.getKey());
			}
		}
	}

	@Override
	public boolean addVehicle(String name) throws RemoteException {
		if(vehicleNetwork.hasVehicle(name))
			return false;

		RemoteVehicleInterface vehicleToAdd;
		try {
			Registry registry = LocateRegistry.getRegistry(Resources.REGISTRY_PORT); // @FIXME: only works for localhost
			vehicleToAdd = (RemoteVehicleInterface) registry.lookup(name);
		} catch(Exception e) {
			System.err.println(Resources.ERROR_MSG("Failed to add vehicle \"" + name + "\" : " + e.getMessage()));
			return false;
		}

		vehicleNetwork.addVehicle(name, vehicleToAdd);
		return true;
	}

	@Override
	public boolean removeVehicle(String name) throws RemoteException {
		if(vehicleNetwork.hasVehicle(name)) {
			vehicleNetwork.removeVehicle(name);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public synchronized String getNextVehicleName() {
		return "V" + nextVehicleNumber++; // to match certificate names
	}

	@Override 
	public void informVehiclesOfRevocation(SignedCertificateDTO dto) {
		try { 
			this.vehicleNetwork.informVehiclesOfRevocation(dto);
		} catch(RemoteException e) {
			System.out.println(Resources.ERROR_MSG(e.getMessage()));
		}
	}

// ------ REGISTRY METHODS --------

	public void publish() {
		if(isPublished) {
			System.out.println(Resources.WARNING_MSG(Resources.VANET_NAME+" already published."));
			return;
		}

		try {
			RemoteVehicleNetworkInterface stub = (RemoteVehicleNetworkInterface) UnicastRemoteObject.exportObject(this, 0);
			Registry registry;
			registry = LocateRegistry.getRegistry(Resources.REGISTRY_PORT);
			registry.rebind(Resources.VANET_NAME, stub);
			isPublished = true;
			System.out.println(Resources.OK_MSG(Resources.VANET_NAME+" published to registry."));
		} catch (Exception e) {
			System.err.println(Resources.ERROR_MSG("Failed to publish remote VANET: " + e.getMessage()));
		}
	}

	public void unpublish() {
		if(! isPublished) {
			System.out.println(Resources.WARNING_MSG("Unpublishing "+Resources.VANET_NAME+" that was never published."));
			return;
		}

		try {
			Registry registry = LocateRegistry.getRegistry(Resources.REGISTRY_PORT);
			registry.unbind(Resources.VANET_NAME);
			UnicastRemoteObject.unexportObject(this, true);
		} catch (Exception e) {
			System.err.println(Resources.ERROR_MSG("Unpublishing VANET: " + e.getMessage()));
		}
	}
}
