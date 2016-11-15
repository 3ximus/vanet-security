package globals;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import java.lang.Thread;

public class Resources {

//  ------- NAMES ------------
	public static final String CA_NAME = "ca";
	public static final String RSU_NAME = "rsu";
	public static final String VANET_NAME = "vanet";

//  ------- PORTS ------------
	public static final int REGISTRY_PORT = 1099;

//  -------- VEHICLE-RANGES ---------
	public static final int MAX_BEACON_RANGE = 100;
	public static final int TOO_DANGEROUS_RANGE = 5;


//  ------- PATHS ------------
	public static final String CA_REVOKED = "cert/revoked/";
	public static final String CA_NODES = "cert/nodes/";
	public static final String CERT_DIR = "cert/";

//  ------- OTHER ------------
	public static final String CA_DIGEST = "SHA-256";

//  ------- PASSWORDS ------------
	public static final char[] STORE_PASS = "f4ncyP455WORd".toCharArray();
	public static final char[] KEY_PASS = "Y3tAn0th3rF4ncyPa5sW00rd".toCharArray();
	public static final char[] CA_CERTIFICATE_PASS="Th1sC4antB3.0neMorePa55?".toCharArray();

//  ------- OUTPUT METHODS ------------
	public static String ERROR_MSG(String msg) {
		StackTraceElement st = Thread.currentThread().getStackTrace()[2]; // caller stack element
		return "[\033[0;31mERRR\033[0m] [\033[1;35m"+st.getClassName()+"."+st.getMethodName()+"\033[0m] "+ msg; }
	public static String WARNING_MSG(String msg) {
		StackTraceElement st = Thread.currentThread().getStackTrace()[2]; // caller stack element
		return "[\033[0;33mWARN\033[0m] [\033[1;35m"+st.getClassName()+"."+st.getMethodName()+"\033[0m] "+msg; }
	public static String NOTIFY_MSG(String msg) {
		StackTraceElement st = Thread.currentThread().getStackTrace()[2]; // caller stack element
		return "[\033[0;34mNOTE\033[0m] [\033[1;35m"+st.getClassName()+"."+st.getMethodName()+"\033[0m] "+msg; }
	public static String OK_MSG(String msg) {
		StackTraceElement st = Thread.currentThread().getStackTrace()[2]; // caller stack element
		return "[\033[0;32m OK \033[0m] [\033[1;35m"+st.getClassName()+"."+st.getMethodName()+"\033[0m] "+msg; }
//  -----------------------------------


//  ------- KEYSTORES ------------

	public static KeyStore readKeystoreFile(String keyStoreFilePath, char[] keyStorePassword) throws Exception {
		FileInputStream fis;
		try { fis = new FileInputStream(keyStoreFilePath); }
		catch (FileNotFoundException e) {
			System.err.println(ERROR_MSG("Keystore File not found: " + keyStoreFilePath));
			return null;
		}
		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		keystore.load(fis, keyStorePassword);
		return keystore;
	}

	// OVERLOADED METHOD
	public static PrivateKey getPrivateKeyFromKeystore(String keyStoreFilePath, char[] keyStorePassword, String keyAlias, char[] keyPassword) throws Exception {
		KeyStore keystore = readKeystoreFile(keyStoreFilePath, keyStorePassword);
		return getPrivateKeyFromKeystore(keystore, keyAlias, keyPassword);
	}
	public static PrivateKey getPrivateKeyFromKeystore(KeyStore keystore, String keyAlias, char[] keyPassword) throws Exception {
		return (PrivateKey) keystore.getKey(keyAlias, keyPassword);
	}


	// OVERLOADED METHOD
	public static Certificate getCertificateFromKeystore(String keyStoreFilePath, char[] keyStorePassword, String certAlias) throws Exception {
		KeyStore keystore = readKeystoreFile(keyStoreFilePath, keyStorePassword);
		return getCertificateFromKeystore(keystore, certAlias);
	}
	public static Certificate getCertificateFromKeystore(KeyStore keystore, String certAlias) throws Exception {
		return keystore.getCertificate(certAlias);
	}
//  -----------------------------------



//  ------- CERTIFICATES ------------

	public static Certificate readCertificateFile(String certificateFilePath) throws Exception {
		FileInputStream fis;

		try { fis = new FileInputStream(certificateFilePath); }
		catch (FileNotFoundException e) {
			System.err.println(ERROR_MSG("Certificate File not found: " + certificateFilePath));
			return null;
		}
		BufferedInputStream bis = new BufferedInputStream(fis);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");

		if (bis.available() > 0) { return cf.generateCertificate(bis); }
		bis.close();
		fis.close();
		return null;
	}

	public static boolean verifySignedCertificate(Certificate certificate, PublicKey caPublicKey) {
		try { certificate.verify(caPublicKey); }
		catch (Exception e) {
			return false;
		}
		return true;
	}

//  -----------------------------------



//  ------- SIGNATURES ------------

	public static byte[] makeDigitalSignature(byte[] bytes, PrivateKey privateKey) throws Exception {
		Signature sig = Signature.getInstance("SHA1WithRSA");
		sig.initSign(privateKey);
		sig.update(bytes);
		byte[] signature = sig.sign();
		return signature;
	}

	public static boolean verifyDigitalSignature(byte[] cipherDigest, byte[] bytes, PublicKey publicKey) throws Exception {
		Signature sig = Signature.getInstance("SHA1WithRSA");
		sig.initVerify(publicKey);
		sig.update(bytes);
		try { return sig.verify(cipherDigest); }
		catch (SignatureException se) {
			System.err.println(WARNING_MSG("Invalid Signature :" + se.getMessage()));
			return false;
		}
	}

//  -----------------------------------


}
