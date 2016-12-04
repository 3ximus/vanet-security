package vanet;

import globals.Resources;
import globals.Vector2D;
import globals.SignedCertificateDTO;

import remote.RemoteVehicleInterface;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;
import java.util.Set;

import java.util.Timer;
import java.util.TimerTask;

/*
	Simulate physical wireless network
*/
public class VehicleNetwork {
	private Map<String, RemoteVehicleInterface> vehicleList = new TreeMap<>();
	private Map<String, Vector2D> vehicleListPos = new TreeMap<>();
	private ArrayList<Vector2D> rsuList = new ArrayList<Vector2D>();

	private Timer timer = new Timer();
	private TimerTask vehiclePosUpdaterTask = new TimerTask() {
		@Override
		public void run() {
			for (Map.Entry<String, RemoteVehicleInterface> entry : vehicleList.entrySet()) {
				try {
					vehicleListPos.put(entry.getKey(), entry.getValue().simulateGetPosition());
				} catch(Exception e) {
					System.out.println(Resources.WARNING_MSG("Unable to update position for vehicle \"" + entry.getKey() + "\"."));
				}
			}
		}
	};

	public VehicleNetwork() {
		timer.scheduleAtFixedRate(vehiclePosUpdaterTask, 0, Resources.NETWORK_POSITION_UPDATE_INTERVAL);
	}

	public Set<Map.Entry<String, RemoteVehicleInterface>> getVehicleEntrySet() {
		return vehicleList.entrySet();
	}

	public boolean hasVehicle(String name) {
		return vehicleList.containsKey(name);
	}

	public void addVehicle(String name, RemoteVehicleInterface vehicleToAdd, Vector2D position) {
		System.out.println(Resources.OK_MSG("Adding vehicle \"" + name + "\" to the network."));
		vehicleList.put(name, vehicleToAdd);
		vehicleListPos.put(name, position);
	}

	public void removeVehicle(String name) {
		System.out.println(Resources.OK_MSG("Removing vehicle \"" + name + "\" from the network."));
		vehicleList.remove(name);
		vehicleListPos.remove(name);
	}

	public Vector2D getVehiclePos(String name) {
		return vehicleListPos.get(name);
	}

	public static boolean inRangeForBeacon(Vector2D pos1, Vector2D pos2) {
		return pos1.distance(pos2) <= Resources.MAX_BEACON_RANGE;
	}

	public static boolean inRangeForRsu(Vector2D pos1, Vector2D pos2) {
		return pos1.distance(pos2) <= Resources.MAX_RSU_RANGE;
	}


	private void addRSU(Vector2D rsu_position) {
		if(hasRSU(rsu_position))
			rsuList.add(rsu_position);
	}

	private boolean hasRSU(Vector2D rsu_position) {
		return rsuList.contains(rsu_position);
	}
}
