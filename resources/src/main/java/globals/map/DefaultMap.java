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

    private static List<Waypoint> allWaypoints() {
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
        Waypoint w40 = new Waypoint(cross2Lane1Y, leftX);
        Waypoint w41 = new Waypoint(cross2Lane1Y, cross1LeftX);
        Waypoint w42 = new Waypoint(cross2Lane1Y, cross1RightX);
        Waypoint w43 = new Waypoint(cross2Lane1Y, cross2LeftX);
        Waypoint w44 = new Waypoint(cross2Lane1Y, cross2RightX);
        Waypoint w45 = new Waypoint(cross2Lane1Y, cross3LeftX);
        Waypoint w46 = new Waypoint(cross2Lane1Y, cross3RightX);
        Waypoint w47 = new Waypoint(cross2Lane1Y, rightX);

        // Eight row
        Waypoint w48 = new Waypoint(cross2Lane2Y, leftX);
        Waypoint w49 = new Waypoint(cross2Lane2Y, cross1LeftX);
        Waypoint w50 = new Waypoint(cross2Lane2Y, cross1RightX);
        Waypoint w51 = new Waypoint(cross2Lane2Y, cross2LeftX);
        Waypoint w52 = new Waypoint(cross2Lane2Y, cross2RightX);
        Waypoint w53 = new Waypoint(cross2Lane2Y, cross3LeftX);
        Waypoint w54 = new Waypoint(cross2Lane2Y, cross3RightX);
        Waypoint w55 = new Waypoint(cross2Lane2Y, rightX);

        // Ninth row
        Waypoint w56 = new Waypoint(cross2BottomY, cross1Lane1X);
        Waypoint w57 = new Waypoint(cross2BottomY, cross1Lane2X);
        Waypoint w58 = new Waypoint(cross2BottomY, cross2Lane1X);
        Waypoint w59 = new Waypoint(cross2BottomY, cross2Lane2X);
        Waypoint w60 = new Waypoint(cross2BottomY, cross3Lane1X);
        Waypoint w61 = new Waypoint(cross2BottomY, cross3Lane2X);

        // Tenth row
        Waypoint w62 = new Waypoint(cross3TopY, cross1Lane1X);
        Waypoint w63 = new Waypoint(cross3TopY, cross1Lane2X);
        Waypoint w64 = new Waypoint(cross3TopY, cross2Lane1X);
        Waypoint w65 = new Waypoint(cross3TopY, cross2Lane2X);
        Waypoint w66 = new Waypoint(cross3TopY, cross3Lane1X);
        Waypoint w67 = new Waypoint(cross3TopY, cross3Lane2X);

        // 11 row
        Waypoint w68 = new Waypoint(cross3Lane1Y, leftX);
        Waypoint w69 = new Waypoint(cross3Lane1Y, cross1LeftX);
        Waypoint w70 = new Waypoint(cross3Lane1Y, cross1RightX);
        Waypoint w71 = new Waypoint(cross3Lane1Y, cross2LeftX);
        Waypoint w72 = new Waypoint(cross3Lane1Y, cross2RightX);
        Waypoint w73 = new Waypoint(cross3Lane1Y, cross3LeftX);
        Waypoint w74 = new Waypoint(cross3Lane1Y, cross3RightX);
        Waypoint w75 = new Waypoint(cross3Lane1Y, rightX);

        // 12 row
        Waypoint w76 = new Waypoint(cross3Lane2Y, leftX);
        Waypoint w77 = new Waypoint(cross3Lane2Y, cross1LeftX);
        Waypoint w78 = new Waypoint(cross3Lane2Y, cross1RightX);
        Waypoint w79 = new Waypoint(cross3Lane2Y, cross2LeftX);
        Waypoint w80 = new Waypoint(cross3Lane2Y, cross2RightX);
        Waypoint w81 = new Waypoint(cross3Lane2Y, cross3LeftX);
        Waypoint w82 = new Waypoint(cross3Lane2Y, cross3RightX);
        Waypoint w83 = new Waypoint(cross3Lane2Y, rightX);

        // 13 row
        Waypoint w84 = new Waypoint(cross3BottomY, cross1Lane1X);
        Waypoint w85 = new Waypoint(cross3BottomY, cross1Lane2X);
        Waypoint w86 = new Waypoint(cross3BottomY, cross2Lane1X);
        Waypoint w87 = new Waypoint(cross3BottomY, cross2Lane2X);
        Waypoint w88 = new Waypoint(cross3BottomY, cross3Lane1X);
        Waypoint w89 = new Waypoint(cross3BottomY, cross3Lane2X);

        // 14 row
        Waypoint w90 = new Waypoint(bottomY, cross1Lane1X);
        Waypoint w91 = new Waypoint(bottomY, cross1Lane2X);
        Waypoint w92 = new Waypoint(bottomY, cross2Lane1X);
        Waypoint w93 = new Waypoint(bottomY, cross2Lane2X);
        Waypoint w94 = new Waypoint(bottomY, cross3Lane1X);
        Waypoint w95 = new Waypoint(bottomY, cross3Lane2X);

        r.add(w0);
        r.add(w1);
        r.add(w2);
        r.add(w3);
        r.add(w4);
        r.add(w5);
        r.add(w6);
        r.add(w7);
        r.add(w8);
        r.add(w9);
        r.add(w10);
        r.add(w11);
        r.add(w12);
        r.add(w13);
        r.add(w14);
        r.add(w15);
        r.add(w16);
        r.add(w17);
        r.add(w18);
        r.add(w19);
        r.add(w20);
        r.add(w21);
        r.add(w22);
        r.add(w23);
        r.add(w24);
        r.add(w25);
        r.add(w26);
        r.add(w27);
        r.add(w28);
        r.add(w29);
        r.add(w30);
        r.add(w31);
        r.add(w32);
        r.add(w33);
        r.add(w34);
        r.add(w35);
        r.add(w36);
        r.add(w37);
        r.add(w38);
        r.add(w39);
        r.add(w40);
        r.add(w41);
        r.add(w42);
        r.add(w43);
        r.add(w44);
        r.add(w45);
        r.add(w46);
        r.add(w47);
        r.add(w48);
        r.add(w49);
        r.add(w50);
        r.add(w51);
        r.add(w52);
        r.add(w53);
        r.add(w54);
        r.add(w55);
        r.add(w56);
        r.add(w57);
        r.add(w58);
        r.add(w59);
        r.add(w60);
        r.add(w61);
        r.add(w62);
        r.add(w63);
        r.add(w64);
        r.add(w65);
        r.add(w66);
        r.add(w67);
        r.add(w68);
        r.add(w69);
        r.add(w70);
        r.add(w71);
        r.add(w72);
        r.add(w73);
        r.add(w74);
        r.add(w75);
        r.add(w76);
        r.add(w77);
        r.add(w78);
        r.add(w79);
        r.add(w80);
        r.add(w81);
        r.add(w82);
        r.add(w83);
        r.add(w84);
        r.add(w85);
        r.add(w86);
        r.add(w87);
        r.add(w88);
        r.add(w89);
        r.add(w90);
        r.add(w91);
        r.add(w92);
        r.add(w93);
        r.add(w94);
        r.add(w95);


        return result;
    }
}