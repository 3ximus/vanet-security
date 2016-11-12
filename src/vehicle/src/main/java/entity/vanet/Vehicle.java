package entity.vanet;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import remote.Vector2Df;
import remote.VehicleDTO;

public class Vehicle {
	public static final int DELTA_MILISSECONDS = 200;
	private String VIN;
	private Vector2Df position;
	private Vector2Df velocity;
	private Engine engine;

	// TODO timestamp should be Date or Datetime object
	private Map<String, VehicleDTO> vicinity; // String is the pseudonim certificate

	public Vehicle(String VIN, Vector2Df position, Vector2Df velocity) {
		this.VIN = VIN;
		this.position = position;
		this.velocity = velocity;

		this.engine = new Engine(this);

		// Run the engine on a timer
		Timer timer = new Timer();
		TimerTask engineTask = new TimerTask() {
			@Override
			public void run() {
				simulatePositionUpdate();
			}
		};

		timer.scheduleAtFixedRate(engineTask, 0, DELTA_MILISSECONDS);
	}

	// ---------
	// GETTERS
	// ---------
	public Vector2Df getPosition() { return this.position; }
	public Vector2Df getVelocity() { return this.velocity; }

	public void simulatePositionUpdate() {
		position.x = position.x + velocity.x;
		position.y = position.y + velocity.y;
	}

	public void beacon() {
		// @TODO: Remote call the network (which will simulate the message being sent to the nearby cars)

	}

	@Override
	public String toString() {
		String res;
		res = "Vehicle: <id>=" + VIN + "; ";
		res += "<pos>=(" + position.x + ", " + position.y + "); ";
		res += "<pos>=(" + position.x + ", " + position.y + ");";
		return res;
	}
}
