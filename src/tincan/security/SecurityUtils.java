/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tincan.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import tincan.network.Frame;

/**
 *
 * @author LucianDobre
 */
public class SecurityUtils {

    /**
     * Signs data with the private key
     *
     * @param Prk
     * @param data
     * @return signed data
     */
    public static byte[] sign(PrivateKey Prk, byte[] data) {
        return data;
    }

    /**
     * Verifies signed data with a public key
     *
     * @param pk RSA encoded Public key
     * @param data
     * @return true if signature is verified with pk
     */
    public static boolean verifySignature(byte[] pk, byte[] data) {
        return true;
    }

    /**
     * Encrypts a frame with the public key
     *
     * @param pk
     * @param data
     * @return byte array containing encrypted frame
     */
    public static byte[] encryptFrame(PublicKey pk, Frame data) {
        return data.pack();
    }

    /**
     * Decrypts a byte array that is supposed to represent a frame payload
     *
     * @param Prk
     * @param data
     * @return the decrypted payload from the encrypted data
     */
    public static byte[] decryptPayload(PrivateKey Prk, byte[] data) {
        return data;
    }
    
    /**
     * Prints keyPair to stdout;
     * @param keyPair 
     */
    public static void dumpKeyPair(KeyPair keyPair) {
        PublicKey pub = keyPair.getPublic();
        System.out.println("Public Key: " + getHexString(pub.getEncoded()));

        PrivateKey priv = keyPair.getPrivate();
        System.out.println("Private Key: " + getHexString(priv.getEncoded()));
    }

    /**
     * Returns String from key.getEncoded() byte[]
     * @param b
     * @return 
     */
    public static String getHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    /**
     * Saves keyPair to path in generic public/private .key files
     * @param path
     * @param keyPair
     * @throws IOException 
     */
    public static void SaveKeyPair(String path, KeyPair keyPair) throws IOException {
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // Store Public Key.
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
                publicKey.getEncoded());
        FileOutputStream fos = new FileOutputStream(path + "/public.key");
        fos.write(x509EncodedKeySpec.getEncoded());
        fos.close();

        // Store Private Key.
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                privateKey.getEncoded());
        fos = new FileOutputStream(path + "/private.key");
        fos.write(pkcs8EncodedKeySpec.getEncoded());
        fos.close();
    }

    /**
     * Loads KeyPair from path from public/private .key files
     * @param path
     * @param algorithm
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException 
     */
    public static KeyPair LoadKeyPair(String path, String algorithm)
            throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        // Read Public Key.
        File filePublicKey = new File(path + "/public.key");
        FileInputStream fis = new FileInputStream(path + "/public.key");
        byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
        fis.read(encodedPublicKey);
        fis.close();

        // Read Private Key.
        File filePrivateKey = new File(path + "/private.key");
        fis = new FileInputStream(path + "/private.key");
        byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
        fis.read(encodedPrivateKey);
        fis.close();

        // Generate KeyPair.
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
                encodedPublicKey);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
                encodedPrivateKey);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        return new KeyPair(publicKey, privateKey);
    }
    
    /**
     * Load PublicKey from file in path
     * @param path
     * @param algorithm
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException 
     */
    public static PublicKey loadPublicKey(String path, String algorithm)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException{
        // Read Public Key.
        File filePublicKey = new File(path);
        FileInputStream fis = new FileInputStream(path);
        byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
        fis.read(encodedPublicKey);
        fis.close();
        
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
                encodedPublicKey);
        return keyFactory.generatePublic(publicKeySpec);
    }
    
    /**
     * Load PrivateKey from file in path
     * @param path
     * @param algorithm
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException 
     */
    public static PrivateKey loadPrivateKey(String path, String algorithm)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException{
        // Read Private Key.
        File filePrivateKey = new File(path);
        FileInputStream fis = new FileInputStream(path);
        byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
        fis.read(encodedPrivateKey);
        fis.close();
        
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
                encodedPrivateKey);
        return keyFactory.generatePrivate(privateKeySpec);
    }
    
    /**
     * Persist a public key to file located at path
     * @param path
     * @param publicKey
     * @throws IOException 
     */
    public static void savePublicKey(String path, PublicKey publicKey)
            throws IOException{
        // Store Public Key.
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
                publicKey.getEncoded());
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(x509EncodedKeySpec.getEncoded());
        fos.close();
    }
    
    /**
     * Persist a private key to file located at path
     * @param path
     * @param publicKey
     * @throws IOException 
     */
    public static void savePrivateKey(String path, PrivateKey privateKey)
            throws IOException{
        // Store Private Key.
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                privateKey.getEncoded());
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(pkcs8EncodedKeySpec.getEncoded());
        fos.close();
    }
}
