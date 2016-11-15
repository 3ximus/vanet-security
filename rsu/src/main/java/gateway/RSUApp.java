package gateway;

import globals.Resources;
import java.rmi.registry.LocateRegistry;

public class RSUApp
{
    public static void main( String[] args ) {

         System.out.println("\n");

        // Create registry if it doesn't exist
        try {
            LocateRegistry.createRegistry(Resources.REGISTRY_PORT);
        } catch(Exception e) {
            // registry is already created
        }

        // Constroi RSU
        RSU rsu = new RSU();

        // Constroi objeto remoto
        RemoteRSUService rsu_service = rsu.getRemoteRSUService(rsu);

        // publica servico
        rsu_service.publish();

        try {
            System.out.println("Press enter to kill the network.");
            System.in.read();
        } catch (java.io.IOException e) {
            System.out.println(Resources.ERROR_MSG("Unable to read from input. Exiting."));
        } finally {
            // remove servico do registry
            rsu_service.unpublish();
        }

        System.out.println("\n");
        System.exit(0);

    }
}
