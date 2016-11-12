package remote;

import java.sql.Timestamp;

public class VehicleDTO {
	private Vector2Df position;
	private Vector2Df velocity;
	private Timestamp timestamp;

	public VehicleDTO(Vector2Df pos, Vector2Df vel, Timestamp timestamp) {
		this.position = pos;
		this.velocity = vel;
		this.timestamp = timestamp;
	}
}
