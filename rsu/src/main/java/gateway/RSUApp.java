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
        RSU rsu = new RSU(Resources.RSU_NAME);

        try {
            // Constroi objeto remoto
            RemoteRSUService rsu_service = rsu.getRemoteRSUService(rsu); // lanca excecao caso nao encontre remoteCA/VANET no registry
            // publica servico
            rsu_service.publish();

            try {
                System.out.println("Press enter to kill the road side unit...");
                System.in.read();
            } catch (java.io.IOException e) {
                System.out.println(Resources.ERROR_MSG("Unable to read from input. Exiting."));
            } finally {
                // remove servico do registry
                rsu_service.unpublish();
            }

        } catch (Exception e) {
            System.err.println(Resources.ERROR_MSG("CA or VANET remote interfaces are not present in the RMI registry."));
        }

        System.out.println("\n");
        System.exit(0);

    }
}
