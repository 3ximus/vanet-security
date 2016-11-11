package entity.vanet;

import java.util.Map;
import remote.VehicleDTO;

public class Vehicle {
	private String VIN;
	private int[] position;
	private int[] velocity;

	// TODO timestamp should be Date or Datetime object
	private Map<String, VehicleDTO> vicinity; // String is the pseudonim certificate
	private Sensors sensors;

	public Vehicle(String VIN) {
		this.VIN = VIN;
		this.position = new int[3];
		this.velocity = new int[3];

		this.sensors = new Sensors();
		// TODO initiate thread to simulate sensors
	}

	// ---------
	// GETTERS
	// ---------
	public int[] getPosition() { return this.position; }
	public int[] getVelocity() { return this.velocity; }

	public void beacon() {
		// REMOTE CALL TO ANOTHER VEHICLE
	}
}
