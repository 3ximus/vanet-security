package gateway;

import java.util.ArrayList;
import gateway.exceptions.CertificateAlreadyInCacheException;
import gateway.exceptions.CertificateNotInCacheException;

public class RSU {
	// TODO: Change from String to Certificate type.
	private ArrayList<String> certificateCache;

	// Construtores //

	public RSU() {
		certificateCache = new ArrayList<String>();
	}

	public RSU(ArrayList<String> certificates) {
		certificateCache = certificates;
	}

	//////////////////


	public void addCertificateToCache(String certificate) throws CertificateAlreadyInCacheException {
		if(isCertInCache(certificate))
			certificateCache.add(certificate);
		else
			throw new CertificateAlreadyInCacheException();
	}

	public void removeCertificateFromCache(String certificate) throws CertificateNotInCacheException {
		if(isCertInCache(certificate))
			certificateCache.remove(certificate);
		else
			throw new CertificateNotInCacheException();
	}

	public boolean isCertInCache(String certificate) {
		return certificate.contains(certificate);
	}

}