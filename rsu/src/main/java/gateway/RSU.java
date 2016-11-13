package gateway;

import java.util.ArrayList;
import gateway.exceptions.CertificateAlreadyInCacheException;

public class RSU {
	//TODO: Change from String to Certificate type.
	private ArrayList<String> certificateCache;

	public RSU() {
		certificateCache = new ArrayList<String>();
	}

	public RSU(ArrayList<String> certificates) {
		certificateCache = certificates;
	}

	public void addCert(String certificate) throws CertificateAlreadyInCacheException {
		if(isCertInCache(certificate))
			certificateCache.add(certificate);
		else
			throw new CertificateAlreadyInCacheException();
	}

	public boolean isCertInCache(String certificate) {
		return certificate.contains(certificate);
	}

}