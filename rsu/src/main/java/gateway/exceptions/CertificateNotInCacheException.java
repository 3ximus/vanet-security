package gateway.exceptions;

public class CertificateNotInCacheException extends Exception {

	@Override
	public String getMessage(){
		return "Certificate is not in cache.";
	}

}
