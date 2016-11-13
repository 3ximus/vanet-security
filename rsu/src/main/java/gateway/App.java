package gateway;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import globals.Resources;

public class App 
{	
	// publishes RSU service to RMI
	public void publishRSU(RemoteRSUService rsu_service) {
		try {
            RemoteRSUService stub = (RemoteRSUService) UnicastRemoteObject.exportObject(rsu_service, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(Resources.RSU_NAME , stub);
            System.out.println(Resources.OK_MSG("RemoteRSUService bound"));
        } catch (Exception e) {
            System.err.println(Resources.ERROR_MSG("RemoteRSUService exception:"));
            e.printStackTrace();
        }

	}

    public static void main( String[] args )
    {	
    	RSU rsu = new RSU();

    	try {
    		
    		// TODO: uncomment qd existirem estas interfaces e variaveis em resources
    		// Locate the vehicular network service 
            // Registry registry = LocateRegistry.getRegistry(Resources.VN_PORT); //colocar porta correta
            // RemoteVehicleNetworkInterface vehicle_service 
            // 		= (RemoteVehicleNetworkService) registry.lookup(Resources.VN_NAME); //colocar nome correto
            
            // Locate the certificate authority service
            // Registry registry = LocateRegistry.getRegistry(Resources.CA_PORT);
            // RemoteCAInterface ca_service 
            // 		= (RemoteCAInterface) registry.lookup(Resources.CA_NAME);

        } catch (Exception e) {
            System.err.println("Remote lookup exception:");
            e.printStackTrace();
        }

    	// Constroi objeto remoto e publica-o para no RMI registry
    	// RemoteRSUService rsu_service = new RemoteRSUService(rsu,vehicle_service,ca_service); 
    	// publishRSU(rsu_service);        
    }
}
