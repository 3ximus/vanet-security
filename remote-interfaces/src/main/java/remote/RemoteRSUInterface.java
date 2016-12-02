package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import globals.SignedCertificateDTO;


public interface RemoteRSUInterface extends Remote {

	/**
	 * Verifies revoked state of a given certificate ( uses RSU cache, if not found asks the CA )
	 * @param	SignedCertificateDTO	DTO containing certificate to be checked
	 * @return		 if its valid, false if its revoked
	 */
	public boolean isRevoked(SignedCertificateDTO dto) throws RemoteException;


	/**
	 * Ask RSU to revoke certificate, reroutes request to CA.
	 * CA will handle certificate revocation, RSU will only maintain a cache if result
	 * of revocation is true.
	 * @param	SignedCertificateDTO	DTO containing certificate to be revoked
	 */
	public boolean tryRevoke(SignedCertificateDTO dto) throws RemoteException;

	/**
	 * Share a new revoked certificate with RSU's in the vicinity
	 * Simulates a sort of ranged broadcast through the network
	 * @param	SignedCertificateDTO	DTO containing certificate to be shared
	 */
	public void shareRevoked(SignedCertificateDTO dto) throws RemoteException;


	/**
	 * Propagate new revoked certificates to vehicles in range
	 * @param	SignedCertificateDTO	DTO containing certificate to be shared
	 */
	public void informVehiclesOfRevocation(SignedCertificateDTO dto) throws RemoteException;

}
