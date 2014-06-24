/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vipsclient.network;

/**
 *
 * @author LucianDobre
 */
public class Frame {
    public static final int CONNECT_FRAME = 1;
    public static final int REQ_PK = 2;
    public static final int RES_PK = 3;
    public static final int CALL = 4;
    public static final int RESPOND = 5;
    public static final int REJECT = 6;;
    public static final int ACKNOWLEDGE = 7;
    private String from;
    private String type;
    byte[] signedPK;
    byte[] payload;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getSignedPK() {
        return signedPK;
    }

    public void setSignedPK(byte[] signedPK) {
        this.signedPK = signedPK;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public byte[] compile(){
        int frameSize =
                from.getBytes().length +
                type.getBytes().length +
                signedPK.length +
                payload.length;
        
        byte[] toReturn = new byte[frameSize];
        
        for ( int i = 0 ; i < frameSize ; ++i ){
            //Placing from
            if ( i < from.getBytes().length ){
                toReturn[i] = from.getBytes()[i];
                continue;
            }
            
            //Placing type
            if ( i < from.getBytes().length + type.getBytes().length ){
                toReturn[i] = type.getBytes()[
                        i % from.getBytes().length];
                continue;
            }
            
            //placing signedPK
            if ( i < from.getBytes().length + type.getBytes().length + signedPK.length ){
                toReturn[i] = signedPK[
                        i % ( from.getBytes().length + type.getBytes().length) ];
                continue;
            }
            
            //placing payload
            toReturn[i] = payload[
                    i % (from.getBytes().length + type.getBytes().length + signedPK.length) ];
        }
        
        return toReturn;        
    }
    
}
