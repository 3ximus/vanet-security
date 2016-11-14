package security;

import globals.Resources;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class App {
    public static void main( String[] args ) {
        System.out.println("------------------------------");

        // Create registry if it doesn't exist
        try { LocateRegistry.createRegistry(Resources.REGISTRY_PORT); }
        catch(Exception e) { } // registry is already created

        try {
            RemoteCAService caService = new RemoteCAService();
            Registry reg = LocateRegistry.createRegistry(Resources.REGISTRY_PORT);
            reg.rebind(Resources.CA_NAME, caService);

            System.out.println(Resources.OK_MSG("CA Server Online."));
        } catch ( Exception e ) {
            System.out.println(Resources.ERROR_MSG("Launching CA Server: " + e.getMessage()));
        }
    }
}
