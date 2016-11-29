package globals;

import java.io.Serializable;

public class Vector2D implements Serializable {
    public double x;
    public double y;
    public static final long serialVersionUID = 0;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distance(Vector2D other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    @Override
    public String toString() {
        return "["+Double.toString(this.x)+","+Double.toString(this.y)+"]";
    }
}
