package security;

import globals.*;
import java.rmi.*;

public class CAApp {
    public static void main( String[] args ) {
        try {
            RemoteCAService caService = new RemoteCAService();
            Registry reg = LocateRegistry.createRegistry(CA_PORT);
            reg.rebind(CA_NAME, caService);

            System.out.println(OK_MSG("CA Server Online."));
        } catch ( Exception e ) {
            System.out.println(ERROR_MSG("Launching CA Server: " + e.getMessage()));
        }
    }
}
