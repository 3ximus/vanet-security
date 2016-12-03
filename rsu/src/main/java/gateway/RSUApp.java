package gateway;

import globals.Resources;
import globals.Vector2D;

import java.rmi.registry.LocateRegistry;

public class RSUApp
{

    public static final String RSU_ARGS_USAGE = "rsu <posX,posY>";

    public static void main(String[] args) {

        System.out.println("\n");
        
        // rsu creation argument
        Vector2D pos = new Vector2D(5, 5);

        // Constroi RSU
        RSU rsu;

        // Parse args
        if (args.length == 0)
            System.out.println(Resources.NOTIFY_MSG( "Assuming default values for position.\n\tTo specify the position you could call with: " + RSU_ARGS_USAGE + "."));
        else if(args.length == 1) {
            try {
                String [] pos_args = args[1].split(",");
                pos = new Vector2D(Float.parseFloat(pos_args[0]), Float.parseFloat(pos_args[1]));
            } catch (NumberFormatException e) {
                System.out.println( Resources.ERROR_MSG("Received the correct amount of arguments but couldn't convert to float."));
                return;
            }
        } else {
            System.out.println(Resources.ERROR_MSG("Incorrect amount of arguments. Expecting 1 but received" + args.length));
            System.out.println(Resources.NOTIFY_MSG("Usage: " + RSU_ARGS_USAGE));
            return;
        }

        rsu = new RSU(Resources.RSU_NAME, pos);

        System.out.println(Resources.OK_MSG("Started: " + rsu));

         // Create registry if it doesn't exist
        try {
            LocateRegistry.createRegistry(Resources.REGISTRY_PORT);
        } catch(Exception e) {
            // registry is already created
        }

        try {
            // Constroi objeto remoto
            RemoteRSUService rsu_service = rsu.getRemoteRSUService(); // lanca excecao caso nao encontre remoteCA/VANET no registry
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
