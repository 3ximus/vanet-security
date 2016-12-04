package gateway;

import java.util.ArrayList;

import gateway.exceptions.CertificateAlreadyInCacheException;
import gateway.exceptions.CertificateNotInCacheException;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.PrivateKey;
import java.security.KeyStore;

import globals.Resources;
import globals.Vector2D;

import remote.RemoteCAInterface;
import remote.RemoteVehicleNetworkInterface;


public class RSU {

	private ArrayList<Certificate> revokedCache;
	private Vector2D position;

	// Security atributes
	private X509Certificate myCert;
	private X509Certificate caCert;
	private PrivateKey myPrKey;
	private KeyStore myKeystore;

	// Construtores //

	public RSU(String certificateName, Vector2D position) {

		revokedCache = new ArrayList<Certificate>();
		this.position = position;

		String certsDir = Resources.CERT_DIR+certificateName+"/";

		// Read certificate file to a certificate object
		try {
			this.myCert = (X509Certificate)Resources.readCertificateFile(certsDir+certificateName+".cer"); }
		catch (Exception e) {
			System.out.println(Resources.ERROR_MSG("Error Loading certificate: "+e.getMessage()));
			System.out.println(Resources.ERROR_MSG("Exiting. RSU is useless without certificate"));
			System.exit(1);
		}
		try {
			this.myKeystore = Resources.readKeystoreFile(certsDir + certificateName + ".jks", Resources.STORE_PASS);
			this.myPrKey = Resources.getPrivateKeyFromKeystore(this.myKeystore, certificateName, Resources.KEY_PASS); }
		catch (Exception e) {
			System.out.println(Resources.ERROR_MSG("Error Loading PrivateKey: "+e.getMessage()));
			System.out.println(Resources.ERROR_MSG("Exiting. RSU is useless without PrivateKey"));
			System.exit(1);
		}
		try {
			this.caCert = (X509Certificate)Resources.getCertificateFromKeystore(this.myKeystore, Resources.CA_NAME); }
		catch (Exception e) {
			System.out.println(Resources.WARNING_MSG("Failed to get CA certificate from Keystore: " + e.getMessage()));
			System.out.println(Resources.ERROR_MSG("Exiting. RSU cannot authenticate messages without CACert"));
			System.exit(1);
		}
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

	public RemoteRSUService getRemoteRSUService() throws Exception {

            // Locate the certificate authority service
            Registry registry = LocateRegistry.getRegistry(Resources.REGISTRY_PORT);
            RemoteCAInterface ca_service = (RemoteCAInterface) registry.lookup(Resources.CA_NAME);
            System.out.println(Resources.OK_MSG("Remote CA Interface located"));

            // Locate the vehicle Network service
            RemoteVehicleNetworkInterface vehicle_network_service
            		= (RemoteVehicleNetworkInterface) registry.lookup(Resources.VANET_NAME);
            System.out.println(Resources.OK_MSG("Remote Vehicle Netwrok Interface located"));

            return new RemoteRSUService(this, ca_service, vehicle_network_service);
	}

	// Getters
	public Vector2D getPosition() { return this.position; }
	public X509Certificate getCertificate()  { return this.myCert; }
	public X509Certificate getCACertificate() { return this.caCert; }
	public PrivateKey getPrivateKey() { return this.myPrKey; }
	public KeyStore getKeystore() { return this.myKeystore; }

	//  ------- UTILITY ------------
	@Override
	public String toString() {
		String res;
		res = "RSU: ";
		res += "<pos>=(" + this.position.x + ", " + this.position.y + "); ";
		return res;
	}

}