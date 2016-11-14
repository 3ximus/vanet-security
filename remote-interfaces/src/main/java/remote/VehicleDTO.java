package remote;

import java.io.Serializable;
import java.sql.Timestamp;

public class VehicleDTO implements Serializable {
	private Vector2Df position;
	private Vector2Df velocity;
	private Timestamp timestamp;

	public VehicleDTO(Vector2Df pos, Vector2Df vel, Timestamp timestamp) {
		this.position = pos;
		this.velocity = vel;
		this.timestamp = timestamp;
	}

	// -------
	// GETTERS
	// -------
	public Vector2Df getPosition() {
		return position;
	}

	public Vector2Df getVelocity() {
		return velocity;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}
}
