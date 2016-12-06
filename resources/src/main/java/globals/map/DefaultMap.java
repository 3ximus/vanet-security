import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream.Builder;

import javax.swing.plaf.ViewportUI;

public class DefaultMap extends Map {
    private static DefaultMap instance;
    private static final int LANE_WIDTH = 100;
    private static final int HALF_LANE_WIDTH = LANE_WIDTH / 2;
    private static final int BUILDING_WIDTH = 300;

    public static DefaultMap getInstance() {
        if(instance == null) {
            instance = new DefaultMap();
            for(Waypoint w: allWaypoints())
            {
                instance.addWaypoint(w);
            }
        }
        return instance;
    }

    private List<Waypoint> allWaypoints() {
        List<Waypoint> r = new ArrayList<Waypoint>();
        int topY            = HALF_LANE_WIDTH;
        int cross1TopY      = BUILDING_WIDTH - HALF_LANE_WIDTH;
        int cross1Lane1Y    = cross1TopY + LANE_WIDTH;
        int cross1Lane2Y    = cross1Lane1Y + LANE_WIDTH;
        int cross1BottomY   = cross1Lane2Y + LANE_WIDTH;

        int cross2TopY      = 2*BUILDING_WIDTH + 2*LANE_WIDTH - HALF_LANE_WIDTH;
        int cross2Lane1Y    = cross2TopY + LANE_WIDTH;
        int cross2Lane2Y    = cross2Lane1Y + LANE_WIDTH;
        int cross2BottomY   = cross2Lane2Y + LANE_WIDTH;

        int cross3TopY      = 3*BUILDING_WIDTH + 4*LANE_WIDTH - HALF_LANE_WIDTH;
        int cross3Lane1Y    = cross3TopY + LANE_WIDTH;
        int cross3Lane2Y    = cross3Lane1Y + LANE_WIDTH;
        int cross3BottomY   = cross3Lane2Y + LANE_WIDTH;
        int bottomY         = 4*BUILDING_WIDTH + 6*LANE_WIDTH - HALF_LANE_WIDTH;

        int leftX           = HALF_LANE_WIDTH;
        int cross1LeftX     = BUILDING_WIDTH - HALF_LANE_WIDTH;
        int cross1Lane1X    = cross1LeftX + LANE_WIDTH;
        int cross1Lane2X    = cross1Lane1X + LANE_WIDTH;
        int cross1RightX    = cross1Lane2X + LANE_WIDTH;

        int cross2LeftX     = 2*BUILDING_WIDTH + 2*LANE_WIDTH - HALF_LANE_WIDTH;
        int cross2Lane1X    = cross2LeftX + LANE_WIDTH;
        int cross2Lane2X    = cross2Lane1X + LANE_WIDTH;
        int cross2RightX    = cross2Lane2X + LANE_WIDTH;

        int cross3LeftX     = 3*BUILDING_WIDTH + 4*LANE_WIDTH - HALF_LANE_WIDTH;
        int cross3Lane1X    = cross3LeftX + LANE_WIDTH;
        int cross3Lane2X    = cross3Lane1X + LANE_WIDTH;
        int cross3RightX    = cross3Lane2X + LANE_WIDTH;
        int rightX          = 4*BUILDING_WIDTH + 6*LANE_WIDTH - HALF_LANE_WIDTH;

        // First row
        Waypoint w0  = new Waypoint(topY, cross1Lane1X);
        Waypoint w1  = new Waypoint(topY, cross1Lane2X);
        Waypoint w2  = new Waypoint(topY, cross2Lane1X);
        Waypoint w3  = new Waypoint(topY, cross2Lane2X);
        Waypoint w4  = new Waypoint(topY, cross3Lane1X);
        Waypoint w5  = new Waypoint(topY, cross3Lane2X);

        // Second row
        Waypoint w6  = new Waypoint(cross1TopY, cross1Lane1X);
        Waypoint w7  = new Waypoint(cross1TopY, cross1Lane2X);
        Waypoint w8  = new Waypoint(cross1TopY, cross2Lane1X);
        Waypoint w9  = new Waypoint(cross1TopY, cross2Lane2X);
        Waypoint w10 = new Waypoint(cross1TopY, cross3Lane1X);
        Waypoint w11 = new Waypoint(cross1TopY, cross3Lane2X);

        //Third row ()
        Waypoint w12 = new Waypoint(cross1Lane1Y, leftX);
        Waypoint w14 = new Waypoint(cross1Lane1Y, cross1LeftX);
        Waypoint w15 = new Waypoint(cross1Lane1Y, cross1RightX);
        Waypoint w13 = new Waypoint(cross1Lane1Y, cross2LeftX);
        Waypoint w16 = new Waypoint(cross1Lane1Y, cross2RightX);
        Waypoint w17 = new Waypoint(cross1Lane1Y, cross3LeftX);
        Waypoint w18 = new Waypoint(cross1Lane1Y, cross3RightX);
        Waypoint w19 = new Waypoint(cross1Lane1Y, rightX);

        // Fourth row
        Waypoint w20 = new Waypoint(cross1Lane2Y, leftX);
        Waypoint w21 = new Waypoint(cross1Lane2Y, cross1LeftX);
        Waypoint w22 = new Waypoint(cross1Lane2Y, cross1RightX);
        Waypoint w23 = new Waypoint(cross1Lane2Y, cross2LeftX);
        Waypoint w24 = new Waypoint(cross1Lane2Y, cross2RightX);
        Waypoint w25 = new Waypoint(cross1Lane2Y, cross3LeftX);
        Waypoint w26 = new Waypoint(cross1Lane2Y, cross3RightX);
        Waypoint w27 = new Waypoint(cross1Lane2Y, rightX);

        // Fifth row
        Waypoint w28 = new Waypoint(cross1BottomY, cross1Lane1X);
        Waypoint w29 = new Waypoint(cross1BottomY, cross1Lane2X);
        Waypoint w30 = new Waypoint(cross1BottomY, cross2Lane1X);
        Waypoint w31 = new Waypoint(cross1BottomY, cross2Lane2X);
        Waypoint w32 = new Waypoint(cross1BottomY, cross3Lane1X);
        Waypoint w33 = new Waypoint(cross1BottomY, cross3Lane2X);

        // Sixth row
        Waypoint w34 = new Waypoint(cross2TopY, cross1Lane1X);
        Waypoint w35 = new Waypoint(cross2TopY, cross1Lane2X);
        Waypoint w36 = new Waypoint(cross2TopY, cross2Lane1X);
        Waypoint w37 = new Waypoint(cross2TopY, cross2Lane2X);
        Waypoint w38 = new Waypoint(cross2TopY, cross3Lane1X);
        Waypoint w39 = new Waypoint(cross2TopY, cross3Lane2X);

        // Seventh row
        Waypoint w = new Waypoint(cross2Lane1Y, leftX);
        Waypoint w = new Waypoint(cross2Lane1Y, cross1LeftX);
        Waypoint w = new Waypoint(cross2Lane1Y, cross1RightX);
        Waypoint w = new Waypoint(cross2Lane1Y, cross2LeftX);
        Waypoint w = new Waypoint(cross2Lane1Y, cross2RightX);
        Waypoint w = new Waypoint(cross2Lane1Y, cross3LeftX);
        Waypoint w = new Waypoint(cross2Lane1Y, cross3RightX);
        Waypoint w = new Waypoint(cross2Lane1Y, rightX);

        // Eight row
        Waypoint w = new Waypoint(cross2Lane2Y, leftX);
        Waypoint w = new Waypoint(cross2Lane2Y, cross1LeftX);
        Waypoint w = new Waypoint(cross2Lane2Y, cross1RightX);
        Waypoint w = new Waypoint(cross2Lane2Y, cross2LeftX);
        Waypoint w = new Waypoint(cross2Lane2Y, cross2RightX);
        Waypoint w = new Waypoint(cross2Lane2Y, cross3LeftX);
        Waypoint w = new Waypoint(cross2Lane2Y, cross3RightX);
        Waypoint w = new Waypoint(cross2Lane2Y, rightX);

        // Ninth row
        Waypoint w = new Waypoint(cross2BottomY, cross1Lane1X);
        Waypoint w = new Waypoint(cross2BottomY, cross1Lane2X);
        Waypoint w = new Waypoint(cross2BottomY, cross2Lane1X);
        Waypoint w = new Waypoint(cross2BottomY, cross2Lane2X);
        Waypoint w = new Waypoint(cross2BottomY, cross3Lane1X);
        Waypoint w = new Waypoint(cross2BottomY, cross3Lane2X);

        // Tenth row
        Waypoint w = new Waypoint(cross3TopY, cross1Lane1X);
        Waypoint w = new Waypoint(cross3TopY, cross1Lane2X);
        Waypoint w = new Waypoint(cross3TopY, cross2Lane1X);
        Waypoint w = new Waypoint(cross3TopY, cross2Lane2X);
        Waypoint w = new Waypoint(cross3TopY, cross3Lane1X);
        Waypoint w = new Waypoint(cross3TopY, cross3Lane2X);

        // 11 row
        Waypoint w = new Waypoint(cross3Lane1Y, leftX);
        Waypoint w = new Waypoint(cross3Lane1Y, cross1LeftX);
        Waypoint w = new Waypoint(cross3Lane1Y, cross1RightX);
        Waypoint w = new Waypoint(cross3Lane1Y, cross2LeftX);
        Waypoint w = new Waypoint(cross3Lane1Y, cross2RightX);
        Waypoint w = new Waypoint(cross3Lane1Y, cross3LeftX);
        Waypoint w = new Waypoint(cross3Lane1Y, cross3RightX);
        Waypoint w = new Waypoint(cross3Lane1Y, rightX);

        // 12 row
        Waypoint w = new Waypoint(cross3Lane2Y, leftX);
        Waypoint w = new Waypoint(cross3Lane2Y, cross1LeftX);
        Waypoint w = new Waypoint(cross3Lane2Y, cross1RightX);
        Waypoint w = new Waypoint(cross3Lane2Y, cross2LeftX);
        Waypoint w = new Waypoint(cross3Lane2Y, cross2RightX);
        Waypoint w = new Waypoint(cross3Lane2Y, cross3LeftX);
        Waypoint w = new Waypoint(cross3Lane2Y, cross3RightX);
        Waypoint w = new Waypoint(cross3Lane2Y, rightX);

        // 13 row
        Waypoint w = new Waypoint(cross3BottomY, cross1Lane1X);
        Waypoint w = new Waypoint(cross3BottomY, cross1Lane2X);
        Waypoint w = new Waypoint(cross3BottomY, cross2Lane1X);
        Waypoint w = new Waypoint(cross3BottomY, cross2Lane2X);
        Waypoint w = new Waypoint(cross3BottomY, cross3Lane1X);
        Waypoint w = new Waypoint(cross3BottomY, cross3Lane2X);

        // 14 row
        Waypoint w = new Waypoint(bottomY, cross1Lane1X);
        Waypoint w = new Waypoint(bottomY, cross1Lane2X);
        Waypoint w = new Waypoint(bottomY, cross2Lane1X);
        Waypoint w = new Waypoint(bottomY, cross2Lane2X);
        Waypoint w = new Waypoint(bottomY, cross3Lane1X);
        Waypoint w = new Waypoint(bottomY, cross3Lane2X);


        r.add(w0);
        // ADD all ...
        return result;
    }
}