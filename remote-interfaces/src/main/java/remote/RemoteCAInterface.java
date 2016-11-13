package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteCAInterface extends Remote {
	public boolean verifyRevokedState(String certificate) throws RemoteException;

}