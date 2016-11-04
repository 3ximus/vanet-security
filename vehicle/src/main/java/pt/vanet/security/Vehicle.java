package pt.vanet.security;

import java.util.Map;

public class Vehicle {
	private String VIN;
	private int[] position;
	private int[] velocity;
	
	// TODO timestamp should be Date or Datetime object
	private Map<int[], VehicleDTO> vicinity;
	private Sensors sensors;

	public Vehicle(String VIN) {
		this.VIN = VIN;
		this.position = new int[3];
		this.velocity = new int[3];

		this.sensors = new Sensors();
		// TODO initiate thread to simulate sensors
	}

	public void beacon() {
		// REMOTE CALL TO ANOTHER VEHICLE
	}
}
