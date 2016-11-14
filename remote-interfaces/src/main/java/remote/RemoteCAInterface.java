package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.cert.Certificate;

public interface RemoteCAInterface extends Remote {

	/**
	 * Verifies revoked state of a given certificate
	 * @param	Certificate		certificate to check validity
	 * @param	Certificate		senders certificate to verify message signature
	 * @param	byte[]			signed digest
	 * @return	boolean			true if its valid, false if its revoked
	 */
	public boolean checkCertificate(Certificate certToVerify, Certificate senderCert, byte[] signature) throws RemoteException;

	/**
	 * Ask CA to revoke certificate
	 * @param	Certificate		certifica to be revoked
	 * @param	Certificate		senders certificate to verify message signature
	 * @param	byte[]			signed digest
	 * @return	boolean			true if sucessfully revoked, false otherwise
	 */
	public boolean revokeCertificate(Certificate certToRevoke, Certificate senderCert, byte[] signature) throws RemoteException;
}