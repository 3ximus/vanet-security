package remote;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * My Custom DtO
 */
public class VehicleDTO implements Serializable {
	private Vector2Df position;
	private Vector2Df velocity;
	private Timestamp timestamp;
    public static final long serialVersionUID = 0;

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

	@Override
	public String toString() {
		return "{P: "+this.position.toString()+", V: "+this.velocity.toString()+", T: "+this.timestamp.toString()+"}";
	}
}
