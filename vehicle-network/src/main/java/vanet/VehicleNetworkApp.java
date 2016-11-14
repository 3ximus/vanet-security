package vanet;

import globals.Resources;

import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;

public class VehicleNetworkApp {
    public static void main(String[] args) {
        System.out.println("");
        System.out.println("");

        // Create registry if it doesn't exist
        try {
            LocateRegistry.createRegistry(Resources.REGISTRY_PORT);
        } catch(Exception e) {
            // registry is already created
        }

        VehicleNetwork vehicleNetwork = new VehicleNetwork();
        RemoteVehicleNetworkService VANET = new RemoteVehicleNetworkService(vehicleNetwork);
        VANET.publish();

        try {
            System.out.println("[Vehicle] Press <enter> to kill the vehicle.");
            System.in.read();
        } catch (java.io.IOException e) {
            System.out.println("[Vehicle] [ERROR] Unable to read from input. Exiting.");
        } finally {
            VANET.unpublish();
        }

        System.out.println("");
        System.out.println("");
        System.exit(0);
    }
}
