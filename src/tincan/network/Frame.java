/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tincan.network;

import java.util.Arrays;

/**
 *
 * @author LucianDobre
 */
public class Frame {
    public static final int CONNECT       = 1;
    public static final int REQ_PK        = 2;
    public static final int RES_PK        = 3;
    public static final int CALL          = 4;
    public static final int RESPOND       = 5;
    public static final int REJECT        = 6;
    public static final int CLOSE         = 7;
    public static final int DATA          = 8;
    public static final int ACKNOWLEDGE   = 9;
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

    /**
     * Packs the frame into a byte array with one byte in front consisting of whole framesize
     * @return 
     */
    public byte[] pack(){
        int frameSize =
                from.getBytes().length +
                type.getBytes().length +
                signedPK.length +
                payload.length;
        
        byte[] toReturn = new byte[frameSize];
        
        
        
        
        for ( int i = 0 ; i < frameSize ; ++i ){
            
            //Placing frameSize
            if ( i == 0 ){
                toReturn[i] = (byte) frameSize;
                continue;
            }
            
            //Placing from size
            if ( i == 1 ){
                toReturn[i] = (byte) from.getBytes().length;
                continue;
            }
            
            //Placing from
            if ( i < from.getBytes().length + 2 ){
                toReturn[i] = from.getBytes()[i - 2];
                continue;
            }
            
            //Placing type length
            if ( i == from.getBytes().length + 2 ){
                toReturn[i] = (byte) type.getBytes().length;
                continue;
            }
            
            //Placing type
            if ( i < from.getBytes().length + type.getBytes().length + 3 ){
                toReturn[i] = type.getBytes()[
                        i - (from.getBytes().length + 3) ];
                continue;
            }
            
            //placing signedPK length
            if ( i == from.getBytes().length + type.getBytes().length + 3 ){
                toReturn[i] = (byte) signedPK.length;
                continue;
            }
            
            //placing signedPK
            if ( i < from.getBytes().length + type.getBytes().length + signedPK.length + 4 ){
                toReturn[i] = signedPK[
                        i - ( from.getBytes().length + type.getBytes().length + 4)];
                continue;
            }
            
            //placing payload length
            if ( i == from.getBytes().length + type.getBytes().length + signedPK.length + 4 ){
                toReturn[i] = (byte) payload.length;
                continue;
            }
            
            //placing payload
            toReturn[i] = payload[
                    i - (from.getBytes().length + type.getBytes().length + signedPK.length + 5)];
        }
        
        return toReturn;        
    }
    
    /**
     * Unpacks a frame from a byte array
     * @param data
     * @return a frame from data in the byte array
     */
    public static Frame unpack(byte[] data){
        int pos = 0;
        byte len = 0;
        Frame toReturn = new Frame();
        
        len = data[pos]; pos++;
        toReturn.setFrom(new String(data, pos, len));
        pos += len;
        
        len = data[pos]; pos++;
        toReturn.setType(new String(data, pos, len));
        pos += len;
        
        len = data[pos]; pos++;
        toReturn.setSignedPK(Arrays.copyOfRange(data, pos, pos + len));
        pos += len;
        
        len = data[pos]; pos++;
        toReturn.setPayload(Arrays.copyOfRange(data, pos, pos + len));
        
        return toReturn;
    }
    
}
