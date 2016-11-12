package entity.vanet;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import remote.Vector2Df;
import remote.VehicleDTO;

public class Vehicle {
	public static final int DELTA_MILISSECONDS = 200;
	public static final int IN_DANGER_RESET_MILISSECONDS = 1000;

	private String VIN;
	private Vector2Df position;
	private Vector2Df velocity;

	boolean inDanger = false;
	private Timer resetInDangerTimer = new Timer();
	private TimerTask resetInDangerTask = new TimerTask() {
		@Override
		public void run() {
			inDanger = false;
		}
	};

	private Timer timer = new Timer();
	private TimerTask engineTask = new TimerTask() {
		@Override
		public void run() {
			simulatePositionUpdate();
		}
	};


	// TODO timestamp should be Date or Datetime object
	private Map<String, VehicleDTO> vicinity; // String is the pseudonim certificate

	public Vehicle(String VIN, Vector2Df position, Vector2Df velocity) {
		this.VIN = VIN;
		this.position = position;
		this.velocity = velocity;

		// Run the engine on a timer
		timer.scheduleAtFixedRate(engineTask, 0, DELTA_MILISSECONDS);
	}

	// ---------
	// GETTERS
	// ---------
	public Vector2Df getPosition() { return this.position; }
	public Vector2Df getVelocity() { return this.velocity; }

	public void beacon() {
		// @TODO: Remote call the network (which will simulate the message being sent to the nearby cars)

	}

	public void simulatePositionUpdate() {
		System.out.println("Simulating update of position..");
		if(inDanger == false) {
			position.x = position.x + velocity.x;
			position.y = position.y + velocity.y;
		}
	}

	public void simulateSensors(VehicleDTO vehicleInfo) {
		System.out.println("simulating sensors..");
		if(isVehicleDangerous(vehicleInfo) == true) {
			if(inDanger = true) {
				resetInDangerTimer.cancel();
			}
			inDanger = true;
			resetInDangerTimer.schedule(resetInDangerTask, IN_DANGER_RESET_MILISSECONDS);

		} else {
			// @TODO: Register on the list if new, or update if not
			// @TODO: If was already registered use a data trust function that , according to previous records of the
			// @TODO: same vehicle, decides whether to believe it or not
		}

	}

	private boolean isVehicleDangerous(VehicleDTO vehicleInfo) {
		// @TODO
		return false;
	}


	// -------------
	// -- UTILITY --
	// -------------
	@Override
	public String toString() {
		String res;
		res = "Vehicle: <id>=" + VIN + "; ";
		res += "<pos>=(" + position.x + ", " + position.y + "); ";
		res += "<pos>=(" + position.x + ", " + position.y + ");";
		return res;
	}
}
