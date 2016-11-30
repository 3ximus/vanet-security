package globals;

import java.io.Serializable;

public abstract class DTO implements Serializable {
    public static final long serialVersionUID = 0;

	/**
	 * Returns the serialized value of this DTO
	 */
	public abstract byte[] serialize();

}