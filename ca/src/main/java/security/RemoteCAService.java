package security;

import globals.Resources;
import globals.SignedCertificateDTO;
import remote.RemoteCAInterface;

import java.io.File;
import java.security.cert.Certificate;
import java.security.MessageDigest;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RemoteCAService implements RemoteCAInterface {
	private boolean isPublished = false;


	public RemoteCAService() {

	}

// ------ INTERFACE METHODS --------

	/**
	 * Verifies revoked state of a given certificate
	 * @param	SignedCertificateDTO		certificate to check validity
	 * @return	boolean			true if its valid, false if its revoked
	 */
	@Override
	public boolean isRevoked(SignedCertificateDTO dto) throws RemoteException {
		return this.isRevoked(dto.getCertificate());
}

	/**
	 * Ask CA to revoke certificate
	 * @param	SignedCertificateDTO		certifica to be revoked
	 * @return	boolean			true if sucessfully revoked, false otherwise
	 */
	@Override
	public boolean tryRevoke(SignedCertificateDTO dto) throws RemoteException {

		// Check if sender vehicle has sent too many tryRevoke requests
		// Forward to CA either:
		// - sender's certificate (due to having too many tries)
		// - the certificate "to be" revoked
		// return true if it was actually revoked

		if (! this.ponderateRevokeRequest(dto.getCertificate()))
			return false;

		// TODO instead of this only write the certificate to a file inside revoked;
		File localCert = this.findCertificateFileIn(dto.getCertificate(), Resources.CA_NODES);
		if (localCert == null)
			System.out.println(Resources.WARNING_MSG("Revoke request of unexistent certificate."));

		// Move certificate to Resources.CA_REVOKED
		else {
			File revokedDir = new File(Resources.CA_REVOKED);
			if (! revokedDir.exists()) revokedDir.mkdir(); // make dir if unexistent
			if (localCert.renameTo(new File(Resources.CA_REVOKED+localCert.getName()))) {
				System.out.println(Resources.OK_MSG("Sucessfully revoked: "+localCert.getName()));
				return true;
			}
			else System.out.println(Resources.ERROR_MSG("Failed to revoke: "+localCert.getName()));
		}

		return false;
	}

// -------------------------------



// ------ INTERNAL METHODS --------

	private boolean ponderateRevokeRequest(Certificate senderCert) {
		// TODO Debate over some ancient philosophical questions and eventually realize everything is binary
		return true;
	}

	private boolean isRevoked(Certificate certToVerify) {
		return this.findCertificateFileIn(certToVerify, Resources.CA_REVOKED) != null;
	}

	private File findCertificateFileIn(Certificate certToLocate, String dir) {
		MessageDigest digest = null;
		try { digest = MessageDigest.getInstance(Resources.CA_DIGEST); }
		catch (java.security.NoSuchAlgorithmException e) { return null; } // im confident it wont hapen
		String certString = certToLocate.toString();
		String hexDigest = printHexBinary(digest.digest(certString.getBytes())); // get digest from certificate
		File dirObj = new File(dir);
		if ( ! dirObj.exists() || ! dirObj.isDirectory()) return null;
		for (File iter : dirObj.listFiles())
			if (iter.getName().contains(hexDigest)) {
				String loadedCert = null;
				// load certificate from file and store it in a string for comparison
				try { loadedCert = Resources.readCertificateFile(dir+iter.getName()).toString(); }
				catch (Exception e) {
					System.out.println(Resources.ERROR_MSG("Reading file Certificate: "+e.getMessage()));
					return null;
				}
				if (certString == loadedCert)
					return iter;
			}
		return null;
	}
// -------------------------------

// ------ REGISTRY METHODS --------

	public void publish() {
		if (this.isPublished) {
			System.out.println(Resources.WARNING_MSG(Resources.CA_NAME+" already published."));
			return;
		}

		try {
			RemoteCAInterface stub = (RemoteCAInterface) UnicastRemoteObject.exportObject(this,0);
			Registry reg = LocateRegistry.getRegistry(Resources.REGISTRY_PORT);
			reg.rebind(Resources.CA_NAME, stub);
			this.isPublished = true;
			System.out.println(Resources.OK_MSG(Resources.CA_NAME+" published to registry."));
		}
		catch (Exception e) {
            System.err.println(Resources.ERROR_MSG("Failed to publish remote CA: " + e.getMessage()));
		 }
	}

	public void unpublish() {
        if(! isPublished) {
            System.out.println(Resources.WARNING_MSG("Unpublishing "+Resources.VANET_NAME+" that was never published."));
            return;
        }

        try {
            Registry registry = LocateRegistry.getRegistry(Resources.REGISTRY_PORT);
            registry.unbind(Resources.CA_NAME);
            UnicastRemoteObject.unexportObject(this, true);
        } catch (Exception e) {
            System.err.println(Resources.ERROR_MSG("Unpublishing CA: " + e.getMessage()));
        }
	}
}