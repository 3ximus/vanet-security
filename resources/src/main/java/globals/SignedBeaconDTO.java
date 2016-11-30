package globals;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;

/**
 * Extension of the BeaconDTO but with signature
 */
public class SignedBeaconDTO extends BeaconDTO {
    public static final long serialVersionUID = 0;
	private SignedDTO beaconSign;

	/**
	 * Builds a signed BeaconDTO
	 * @param	Vector2D			position vector
	 * @param	Vector2D			velocity vector
	 * @param	Timestamp			timestamp of this beacon
	 * @param	X509Certificate		certificate the entity sending this beacon
	 * @param	DTO					DTO to wich this signedDTO belongs to
	 * @param	PrivateKey			Sender private key (only used to generate signature, it wont be stored nor sent)
	 */
	public SignedBeaconDTO(Vector2D pos, Vector2D vel, Timestamp timestamp, X509Certificate senderCert, PrivateKey pkey) {
		super(pos, vel, timestamp);
		this.beaconSign = new SignedDTO(senderCert, this, pkey);
	}

	/**
	 * Builds a signed BeaconDTO
	 * @param	Vector2D			position vector
	 * @param	Vector2D			velocity vector
	 * @param	X509Certificate		certificate the entity sending this beacon
	 * @param	DTO					DTO to wich this signedDTO belongs to
	 * @param	PrivateKey			Sender private key (only used to generate signature, it wont be stored nor sent)
	 * NOTE since timestamp is omited a new one is created with current time
	 */
	public SignedBeaconDTO(Vector2D pos, Vector2D vel, X509Certificate senderCert, PrivateKey pkey) {
		super(pos, vel);
		this.beaconSign = new SignedDTO(senderCert, this, pkey);
	}

//  ------- GETTERS  ------------

	public X509Certificate getSenderCertificate() { return this.beaconSign.getSenderCertificate(); }
	public DTO getDTO() { return this.beaconSign.getDTO(); }
	public byte[] getSignature() { return this.beaconSign.getSignature(); }


	@Override
	public byte[] serialize() {
		return this.beaconSign.serialize();
	}

	/*
	 * Verify if the senderCertificate was signed by the given entity
	 * @param	X509Certificate	entity used to verify the senderCertificate in this DTO (usually the CA)
	 * @return	returns true if it was signed by given entity
	 */
	public boolean verifyCertificate(X509Certificate otherEntity) {
		return this.beaconSign.verifyCertificate(otherEntity);
	}

	/**
	 * Compares the received signature with the calculated DTO signature
	 * @return	returns true if the signature is correct
	 */
	public boolean verifySignature() {
		return this.beaconSign.verifySignature();
	}
}

