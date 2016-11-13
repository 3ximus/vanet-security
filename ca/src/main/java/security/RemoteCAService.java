package security;

import remote.RemoteCAInterface;
import java.rmi.RemoteException;

public class RemoteCAService implements RemoteCAInterface {
	public RemoteCAService() {

	}

	public boolean verifyRevokedState(String certificate) throws RemoteException {
		return true;
	}
}