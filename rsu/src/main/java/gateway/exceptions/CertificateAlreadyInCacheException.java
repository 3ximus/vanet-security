package gateway.exceptions;

public class CertificateAlreadyInCacheException extends Exception {
    public static final long serialVersionUID = 0;

	@Override
	public String getMessage(){
		return "Certificate is already in cache.";
	}

}
