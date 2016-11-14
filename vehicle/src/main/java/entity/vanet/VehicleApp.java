package entity.vanet;

import globals.Resources;
import remote.RemoteVehicleInterface;
import remote.RemoteVehicleNetworkInterface;
import remote.Vector2Df;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
                System.out.println(Resources.ERROR_MSG("[Vehicle]Received the correct amount of arguments but couldn't convert to float."));
                return;
            }
        } else {
            System.out.println(Resources.ERROR_MSG("[Vehicle] Incorrect amount of arguments."));
            System.out.println("[Vehicle] Usage: " + VEHICLE_ARGS_USAGE);
            return;
        }
        System.out.println("[Vehicle] Started: " + vehicle);

        // Create registry if it doesn't exist
        try {
            LocateRegistry.createRegistry(Resources.REGISTRY_PORT);
        } catch(Exception e) {
            // registry is already created
        }

        // Connect to the VANET
        RemoteVehicleNetworkInterface VANET;
        String vehicleUniqueName;
        try {
            Registry registry = LocateRegistry.getRegistry(Resources.REGISTRY_PORT);
            VANET = (RemoteVehicleNetworkInterface) registry.lookup(Resources.VANET_NAME);
            // Get a unique name from the VANET
            vehicleUniqueName = VANET.getNextVehicleName();
        } catch(Exception e) {
            System.err.println(Resources.ERROR_MSG("[Vehicle] Failed to connect to VANET. " + e.getClass() + ": " +  e.getMessage()));
            System.exit(0); // Return seems to not work for some reason
            return;
        }

        // Publish remote vehicle
        RemoteVehicleService remoteVehicle = new RemoteVehicleService(vehicle, vehicleUniqueName);
        remoteVehicle.publish();

        // Start the vehicle
        vehicle.start(VANET, vehicleUniqueName);

        // Add vehicle to the VANET
        try {
            boolean result = VANET.addVehicle(vehicleUniqueName);
            if(result == false) {
                throw new Exception("Remote call to the VANET to add this vehicle failed.");
            }
        } catch(Exception e) {
            System.err.println(Resources.ERROR_MSG("[Vehicle] Failed add vehicle to the VANET. " + e.getClass() + ": " +  e.getMessage()));
            System.exit(0); // Return seems to not work for some reason
            return;
        }

        // Handle wait and removal
        try {
            System.out.println("[Vehicle] Press <enter> to kill the vehicle.");
            System.in.read();
            VANET.removeVehicle(vehicleUniqueName);
        } catch (java.io.IOException e) {
            System.out.println(Resources.ERROR_MSG("[Vehicle] Unable to read from input. Exiting."));
        } finally {
            remoteVehicle.unpublish();
            System.out.println("");
            System.out.println("");
            System.exit(0);
        }
    }
}
