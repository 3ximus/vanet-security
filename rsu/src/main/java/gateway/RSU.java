package gateway;

import java.util.ArrayList;

import gateway.exceptions.CertificateAlreadyInCacheException;
import gateway.exceptions.CertificateNotInCacheException;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import globals.Resources;
import remote.RemoteCAInterface;
import remote.RemoteRSUInterface;


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

	public RemoteRSUService getRemoteRSUService(RSU rsu) {

		RemoteRSUService service = null;

		try {
            // Locate the certificate authority service
            Registry registry = LocateRegistry.getRegistry(Resources.REGISTRY_PORT);
            RemoteCAInterface ca_service = (RemoteCAInterface) registry.lookup(Resources.CA_NAME);
            System.out.println(Resources.OK_MSG("Remote Certificate Authority Interface located"));
            service = new RemoteRSUService(rsu,ca_service);

        } catch (Exception e) {
            System.err.println(Resources.ERROR_MSG("Cannot CA remote interface is not present in the RMI registry."));
        }

        return service;
	}

}