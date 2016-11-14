package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.cert.Certificate;

public interface RemoteRSUInterface extends Remote {

	// TODO: change certificate and key types. make sure of arguments passed
	// TODO missing security Arguments ( signature )


	// @ANDRE este metodo faz o que?
	public boolean receiveAuthenticationRequest (Certificate senderCertificate, byte[] signature) throws RemoteException;
	public void receiveRevokeCertificateRequest (Certificate certToRevoke, Certificate senderCertificate, byte[] signature) throws RemoteException;
	public void ping (String msg) throws RemoteException;
}
