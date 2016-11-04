package pt.vanet.security;

import java.sql.Timestamp;

public class VehicleDTO {
	private int[] position;
	private int[] velocity;
	private Timestamp timestamp;

	public VehicleDTO(int x, int y, int z, int vx, int vy, int vz, Timestamp timestamp) {
		this.position = new int[] {x, y ,z};
		this.velocity = new int[] {vx, vy ,vz};
		this.timestamp = timestamp;
	} 
}
