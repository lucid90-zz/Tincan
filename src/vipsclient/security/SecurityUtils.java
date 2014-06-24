/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vipsclient.security;

import java.awt.Frame;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 *
 * @author LucianDobre
 */
public class SecurityUtils {
    
    /**
     * Signs data with the private key
     * @param Prk
     * @param data
     * @return signed data
     */
    public byte[] sign(PrivateKey Prk, byte[] data){
        return null;
    }
    
    /**
     * Verifies signed data with a public key
     * @param pk
     * @param data
     * @return true if signature is verified with pk
     */
    public boolean verifySignature(PublicKey pk, byte[] data){
        return true;
    }
    
    /**
     * Encrypts a frame with the public key
     * @param pk
     * @param data
     * @return byte array containing encrypted frame
     */
    public byte[] encryptFrame(PublicKey pk, Frame data){
        return null;
    }
    
    /**
     * Decrypts a byte array that is supposed to represent a frame
     * @param Prk
     * @param data
     * @return a frame from the decrypted data
     */
    public Frame decryptFrame(PrivateKey Prk, byte[] data){
        return null;
    }
    
}
