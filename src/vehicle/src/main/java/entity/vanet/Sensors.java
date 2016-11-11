package entity.vanet;

public class Sensors {
	private int[] position;
	private int[] velocity;

	public Sensors() {
		// TODO generate random starting position and associated velocity
	}

	// GETTERS
	public int[] getPosition() { return this.position; }
	public int[] getVelocity() { return this.velocity; }

	public void simulate() {
		// TODO function called by thread
	}

}
