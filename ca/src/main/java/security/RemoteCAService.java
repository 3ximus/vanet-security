package security;

import globals.Resources;
import remote.RemoteCAInterface;

import java.rmi.RemoteException;
import java.security.cert.Certificate;
import java.security.MessageDigest;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

public class RemoteCAService implements RemoteCAInterface {
	public RemoteCAService() {

	}

// ------ INTERFACE METHOD --------

	public boolean checkCertificate(Certificate certToVerify, Certificate senderCert, byte[] signature) throws RemoteException {
		return true;
	}

	public boolean revokeCertificate(Certificate certToRevoke, Certificate senderCert, byte[] signature) throws RemoteException {
		return true;
	}


// -------------------------------

	private boolean isRevoked(Certificate certToVerify) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		String hexDigest = printHexBinary(digest.digest(certToVerify.toString().getBytes()));
		File revoked_dir = new File(Resources.CA_REVOKED);
		if ( ! revoked_dir.exists() || ! revoked_dir.isDirectory()) return false;
		for (File iter : revoked_dir.listFiles()) {
			if (iter.getName().contains(hexDigest)) {
				FileInputStream in = new FileInputStream(iter.getName());

			}

		}





		return true;
	}
}