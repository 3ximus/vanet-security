package gateway.exceptions;

public class CertificateAlreadyInCacheException extends Exception {

	@Override
	public String getMessage(){
		return "Certificate is already in cache.";
	}

}
