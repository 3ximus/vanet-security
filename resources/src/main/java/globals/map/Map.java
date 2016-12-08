package globals.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class Map {
    private List<Waypoint> waypoints = new ArrayList<Waypoint>();

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public Waypoint getRandomWaypoint() {
        Random randomGenerator = new Random();
        int randomIndex = randomGenerator.nextInt(waypoints.size());
        return waypoints.get(randomIndex);
    }

    public void addWaypoint(Waypoint w) {
        waypoints.add(w);
    }
}