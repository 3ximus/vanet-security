package globals;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 * Contains the sender Certificate and generic abstract methods for generating and verifiyng signatures
 */
public abstract class SignedDTO implements Serializable {
    public static final long serialVersionUID = 0;
	protected X509Certificate senderCertificate;
	protected byte[] signature = null;

//  ------- GETTERS / SETTER  ------------

	public X509Certificate getSenderCertificate() { return this.senderCertificate; }
	public byte[] getSignature() {
		if (this.signature == null)
			System.out.println(Resources.ERROR_MSG("Acessing signature that was not generated."));
 		return this.signature;
	}

	public void setCertificate(X509Certificate cert) { this.senderCertificate = cert; }

	/**
	 * Returns the serialized value of this DTO
	 */
	public abstract byte[] serialize();

//  ------- SIGNATURE / CERTIFICATE METHODS  ------------

	/**
	 * Generates the signature for the DTO + senderCertificate in the constructor with given PrivateKey
	 * @param	PrivateKey	key used to signed this DTO
	 * @return	returns the signature generated
	 */
	public abstract byte[] sign(PrivateKey pKey);

	/*
	 * Verify if the senderCertificate was signed by the given entity
	 * @param	X509Certificate	entity used to verify the senderCertificate in this DTO (usually the CA)
	 * @return	returns true if it was signed by given entity
	 */
	public abstract boolean verifyCertificate(X509Certificate otherEntity);

	/**
	 * Compares the received signature with the calculated DTO signature
	 * @return	returns true if the signature is correct
	 */
	public abstract boolean verifySignature();
}