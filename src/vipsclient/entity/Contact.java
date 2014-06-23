/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vipsclient.entity;

import java.security.KeyPair;
import java.util.Vector;

/**
 *
 * @author LucianDobre
 */
public class Contact {
    final public static String[] header = {"FN","LN","HN","PORT","PK"};
    String firstName;
    String LastName;
    String hostname;
    String port;
    KeyPair kp;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public KeyPair getKp() {
        return kp;
    }

    public void setKp(KeyPair kp) {
        this.kp = kp;
    }
    
    public Vector toVector(){
        Vector toReturn = new Vector();
        toReturn.add(firstName);
        toReturn.add(LastName);
        toReturn.add(hostname);
        toReturn.add(port);
        toReturn.add(kp);
        return toReturn;
    }
    
    public static Vector toVector( Vector<Contact> data ){
        Vector toReturn = new Vector();
        for ( Contact c : data )
            toReturn.add(c.toVector());
        return toReturn;
    }
    
}
