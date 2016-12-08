package vanet;

import globals.Resources;

import java.rmi.registry.LocateRegistry;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class VehicleNetworkApp {
    public static void main(String[] args) {
        System.out.println("\n");

        // Create registry if it doesn't exist
        try {
            LocateRegistry.createRegistry(Resources.REGISTRY_PORT);
        } catch(Exception e) {
            // registry is already created
        }

        VehicleNetwork vehicleNetwork = new VehicleNetwork();
        RemoteVehicleNetworkService VANET = new RemoteVehicleNetworkService(vehicleNetwork);
        VANET.publish();

        // Launch GUI
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new vanet.gui.VanetGUI(), config);

        // Handle wait and unpublish
        try {
            System.out.println("Press enter to kill the network.");
            System.in.read();
        } catch (java.io.IOException e) {
            System.out.println(Resources.ERROR_MSG("Unable to read from input. Exiting."));
        } finally {
            VANET.unpublish();
        }

        System.out.println("\n");
        System.exit(0);
    }
}
