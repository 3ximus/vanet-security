package remote;

import java.io.Serializable;

public class Vector2Df implements Serializable {
    public double x;
    public double y;

    public Vector2Df(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distance(Vector2Df other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    @Override
    public String toString() {
        return "["+Double.toString(this.x)+","+Double.toString(this.y)+"]";
    }
}
