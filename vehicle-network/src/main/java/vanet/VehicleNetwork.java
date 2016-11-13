package vanet;

import entity.vanet.RemoteVehicleService;
import entity.vanet.Vehicle;
import remote.RemoteVehicleInterface;
import java.util.TreeMap;
import java.util.Map;
import java.util.Set;

/*
	Simulate physical wireless network
*/
public class VehicleNetwork {

	private Map<String, RemoteVehicleInterface> vehicleList = new TreeMap<>();

	public VehicleNetwork() {
		// @TODO: Launch vehicle processes (maybe) (probably not?)
	}

	public Set<Map.Entry<String, RemoteVehicleInterface>> getVehicleEntrySet() {
		return vehicleList.entrySet();
	}

	public boolean hasVehicle(String name) {
		return vehicleList.containsKey(name);
	}

	public void addVehicle(String name, RemoteVehicleInterface vehicleToAdd) {
		System.out.println("Adding vehicle \"" + name + "\" to the network.");
		vehicleList.put(name, vehicleToAdd);
	}

	public void removeVehicle(String name) {
		System.out.println("Removing vehicle \"" + name + "\" from the network.");
		vehicleList.remove(name);
	}
}
