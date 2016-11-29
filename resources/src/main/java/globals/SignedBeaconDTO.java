package globals;

import java.io.Serializable;
import java.security.cert.Certificate;
import java.sql.Timestamp;

/**
	*	YEAH
	*/
public class SignedBeaconDTO extends BeaconDTO implements SignedDTO, Serializable {
    public static final long serialVersionUID = 0;

	/**
	 *	THIS IS A COMMENT
	 */
	public SignedBeaconDTO(Vector2D pos, Vector2D vel, Timestamp timestamp, Certificate senderCert) {
		super(pos, vel, timestamp);
	}
}

