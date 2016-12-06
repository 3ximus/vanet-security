package globals.map;

import java.util.ArrayList;
import java.util.List;



public class Map {
    private List<Waypoint> waypoints = new ArrayList<Waypoint>();
    private static Map defaulMap;

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void addWaypoint(Waypoint w) {
        waypoints.add(w);
    }

    public static Map getDefaultMap() {
        if(defaulMap == null) {
            // TODO
            defaulMap = new Map();
        }
        return defaulMap;
    }
}