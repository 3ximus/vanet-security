package security;

import globals.Resources; 
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CAApp {
    public static void main( String[] args ) {
        try {
            RemoteCAService caService = new RemoteCAService();
            Registry reg = LocateRegistry.createRegistry(Resources.CA_PORT);
            reg.rebind(Resources.CA_NAME, caService);

            System.out.println(Resources.OK_MSG("CA Server Online."));
        } catch ( Exception e ) {
            System.out.println(Resources.ERROR_MSG("Launching CA Server: " + e.getMessage()));
        }
    }
}
