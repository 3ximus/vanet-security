package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteRSUInterface extends Remote {
	
	// TODO: change certificate and key types. make sure of arguments passed
	// TODO missing security Arguments ( signature )
	public boolean receiveAuthenticationRequest (String senderCertificate, byte[] signature) throws RemoteException;
	public void receiveRevokeCertificateRequest (String senderCertificate, byte[] signature) throws RemoteException;
}