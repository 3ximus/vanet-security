package globals;

import java.io.Serializable;
import java.sql.Timestamp;

public class BeaconDTO implements Serializable {
	private Vector2D position;
	private Vector2D velocity;
	private Timestamp timestamp;
    public static final long serialVersionUID = 0;

	public BeaconDTO(Vector2D pos, Vector2D vel, Timestamp timestamp) {
		this.position = pos;
		this.velocity = vel;
		this.timestamp = timestamp;
	}

	// -------
	// GETTERS
	// -------
	public Vector2D getPosition() {
		return position;
	}

	public Vector2D getVelocity() {
		return velocity;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		return "{P: "+this.position.toString()+", V: "+this.velocity.toString()+", T: "+this.timestamp.toString()+"}";
	}
}
