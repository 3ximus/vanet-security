package globals;

import java.sql.Timestamp;

public class BeaconDTO extends DTO {
    public static final long serialVersionUID = 0;
	private Vector2D position;
	private Vector2D velocity;
	private Timestamp timestamp;

	/**
	 * Builds a signed BeaconDTO
	 * @param	Vector2D			position vector
	 * @param	Vector2D			velocity vector
	 * @param	Timestamp			timestamp of this beacon
	 */
	public BeaconDTO(Vector2D pos, Vector2D vel, Timestamp timestamp) {
		this.position = pos;
		this.velocity = vel;
		this.timestamp = timestamp;
	}

	/**
	 * Builds a signed BeaconDTO
	 * @param	Vector2D			position vector
	 * @param	Vector2D			velocity vector
	 * NOTE since timestamp is omited a new one is created with current time
	 */
	public BeaconDTO(Vector2D pos, Vector2D vel) {
		this(pos, vel, new Timestamp(System.currentTimeMillis()));
	}

//  ------- GETTERS  ------------

	public Vector2D getPosition() { return position; }
	public Vector2D getVelocity() { return velocity; }
	public Timestamp getTimestamp() { return timestamp; }

	@Override
	public String toString() {
		return "{P: "+this.position.toString()+", V: "+this.velocity.toString()+", T: "+this.timestamp.toString()+"}";
	}

	@Override
	public byte[] serialize() {
		return this.toString().getBytes();
	}
}
