package gateway;

import java.util.ArrayList;

import gateway.exceptions.CertificateAlreadyInCacheException;
import gateway.exceptions.CertificateNotInCacheException;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import java.security.cert.Certificate;

import globals.Resources;
import remote.RemoteCAInterface;
import remote.RemoteRSUInterface;


public class RSU {
	
	private ArrayList<Certificate> revokedCache;

	// Construtores //

	public RSU() {
		revokedCache = new ArrayList<Certificate>();
	}

	public RSU(ArrayList<Certificate> certificates) {
		revokedCache = certificates;
	}

	//////////////////


	public void addCertificateToCache(Certificate certificate) throws CertificateAlreadyInCacheException {
		if(isCertInCache(certificate))
			revokedCache.add(certificate);
		else
			throw new CertificateAlreadyInCacheException();
	}

	public void removeCertificateFromCache(Certificate certificate) throws CertificateNotInCacheException {
		if(isCertInCache(certificate))
			revokedCache.remove(certificate);
		else
			throw new CertificateNotInCacheException();
	}

	public boolean isCertInCache(Certificate certificate) {
		return revokedCache.contains(certificate);
	}

	public RemoteRSUService getRemoteRSUService(RSU rsu) throws Exception {

            // Locate the certificate authority service
            Registry registry = LocateRegistry.getRegistry(Resources.REGISTRY_PORT);
            RemoteCAInterface ca_service = (RemoteCAInterface) registry.lookup(Resources.CA_NAME);
            System.out.println(Resources.OK_MSG("Remote CA Interface located"));

            return new RemoteRSUService(rsu,ca_service);
	}

}