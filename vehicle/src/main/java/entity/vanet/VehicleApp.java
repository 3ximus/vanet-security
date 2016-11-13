package entity.vanet;

import remote.RemoteVehicleInterface;
import remote.Vector2Df;

/**
 * Hello world!
 *
 */
public class VehicleApp {
    public static final String VEHICLE_ARGS_USAGE = "vehicle <VIN> <posX> <posY> <velX> <velY>";

    public static void main(String[] args) {
        System.out.println("");
        System.out.println("");

        Vehicle vehicle;

        // Parse args
        if(args.length == 0) {
            System.out.println("[Vehicle] Assuming random values for position, velocity and VIN.");
            System.out.println("[Vehicle] To specify this values you could call with: " + VEHICLE_ARGS_USAGE + ".");
            vehicle = new Vehicle("VIN1", new Vector2Df(0, 0), new Vector2Df(0, 0));

        } else if(args.length == 5) {
            try {
                float px = Float.parseFloat(args[1]);
                float py = Float.parseFloat(args[2]);
                float vx = Float.parseFloat(args[3]);
                float vy = Float.parseFloat(args[4]);
                vehicle = new Vehicle(args[0], new Vector2Df(px, py), new Vector2Df(vx, vy));
            } catch (NumberFormatException e) {
                System.out.println("[Vehicle] [ERROR] Received the correct amount of arguments but couldn't convert to float.");
                return;
            }
        } else {
            System.out.println("[Vehicle] [ERROR] Incorrect amount of arguments.");
            System.out.println("[Vehicle] Usage: " + VEHICLE_ARGS_USAGE);
            return;
        }
        System.out.println("[Vehicle] Started: " + vehicle);

        // Publish remote vehicle
        // @TODO: Remove hardcode port and name to soething better
        RemoteVehicleService remoteVehicle = new RemoteVehicleService();
        remoteVehicle.publish("Vehicle1", 10500);

        // @TODO: Had itself to the network
        // @TODO: AKA call the remote network object


        try {
            System.out.println("[Vehicle] Press <enter> to kill the vehicle.");
            System.in.read();
        } catch (java.io.IOException e) {
            System.out.println("[Vehicle] [ERROR] Unable to read from input. Exiting.");

        }
        System.out.println("");
        System.out.println("");
        System.exit(0);
    }
}
