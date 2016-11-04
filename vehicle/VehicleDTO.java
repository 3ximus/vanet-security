import java.sql.Timestamp

public class VehicleDTO {
	public int[] position;
	public int[] velocity;
	public Timestamp timestamp;

	public VehicleDTO(int x, int y, int z, int vx, int vy, int vz, Timestamp timestamp) {
		this.position = new int[] {x, y ,z};
		this.velocity = new int[] {vx, vy ,vz};
		this.timestamp = timestamp;
	}
