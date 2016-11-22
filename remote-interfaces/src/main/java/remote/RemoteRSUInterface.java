package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.cert.Certificate;


public interface RemoteRSUInterface extends Remote {

	/**
	 * Verifies revoked state of a given certificate ( uses RSU cache, if not found asks the CA )
	 * @param	Certificate		certificate to check validity
	 * @param	Certificate		senders certificate to verify message signature
	 * @param	byte[]			message signature
	 * @return	boolean			true if its valid, false if its revoked
	 */
	public boolean authenticate(Certificate certToVerify, Certificate senderCert, byte[] signature) throws RemoteException;


	/**
	 * Ask RSU to revoke certificate, reroutes request to CA.
	 * CA will handle certificate revocation, RSU will only maintain a cache if result
	 * of revocation is true.
	 * @param	Certificate		reported target certificate to be revoked
	 * @param	Certificate		author of the report
	 * @param	byte[]			message signature
	 */
	public void revokeCertificate(Certificate certToRevoke, Certificate senderCertificate, byte[] signature) throws RemoteException;


	/**
	 * Connectivity test
	 * @param	String	message to ping;
	 * @return	String	pong
	 */
	public String ping(String msg) throws RemoteException;
}
