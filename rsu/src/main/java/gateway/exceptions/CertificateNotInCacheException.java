package gateway.exceptions;

public class CertificateNotInCacheException extends Exception {
    public static final long serialVersionUID = 0;

	@Override
	public String getMessage(){
		return "Certificate is not in cache.";
	}

}
