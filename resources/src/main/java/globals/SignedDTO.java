package globals;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 * Contains the sender Certificate and a way to calculate and verify the signature of a dto
 */
public class SignedDTO extends DTO {
    public static final long serialVersionUID = 0;
	private X509Certificate senderCertificate;
	private DTO parentDTO;
	private byte[] signature;

	/**
	 * Builds a signed DTO
	 * @param	X509Certificate		certificate the entity sending this beacon
	 * @param	DTO					DTO to wich this signedDTO belongs to
	 * @param	PrivateKey			Sender private key (only used to generate signature, it wont be stored nor sent)
	 */
	public SignedDTO(X509Certificate senderCert, DTO parentDTO, PrivateKey pkey) {
		this.senderCertificate = senderCert;
		this.parentDTO = parentDTO;
		this.generateSignature(pkey);
	}

//  ------- GETTERS  ------------

	public X509Certificate getSenderCertificate() { return this.senderCertificate; }
	public DTO getDTO() { return this.parentDTO; }
	public byte[] getSignature() { return this.signature; }

	/**
	 * Returns the serialized value of this DTO
	 */
	@Override
	public byte[] serialize() {
		return this.senderCertificate.toString().getBytes();
	}

	/**
	 * Generates the signature for the DTO + senderCertificate in the constructor with given PrivateKey
	 * @param	PrivateKey	key used to signed this DTO
	 * @return	returns the signature generated
	 */
	public byte[] generateSignature(PrivateKey pKey) {
		byte[] serializedVal = this.parentDTO.serialize();
		byte[] sig = null;
		try {
			sig = Resources.makeDigitalSignature(serializedVal, pKey); }
		catch (Exception e) {
			System.out.println(Resources.ERROR_MSG("Failed to create signature: "+e.getMessage()));
			return null;
		}
		this.signature = sig;
		return this.signature;
	}

	/*
	 * Verify if the senderCertificate was signed by the given entity
	 * @param	X509Certificate	entity used to verify the senderCertificate in this DTO (usually the CA)
	 * @return	returns true if it was signed by given entity
	 */
	public boolean verifyCertificate(X509Certificate otherEntity) {
		return Resources.verifySignedCertificate(this.senderCertificate, otherEntity.getPublicKey());
	}

	/**
	 * Compares the received signature with the calculated DTO signature
	 * @return	returns true if the signature is correct
	 */
	public boolean verifySignature() {
		try { Resources.verifyDigitalSignature(this.signature, this.parentDTO.serialize(), this.senderCertificate.getPublicKey()); }
		catch (Exception e ) { return false; } // certificate was not signed by sender, beacon is dropped
		return true;
	}
}