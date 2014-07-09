/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tincan.network;

import java.nio.ByteBuffer;
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
                from.getBytes().length + 4 + 
                type.getBytes().length + 4 +
                signedPK.length + 4 +
                payload.length + 4;
        
        ByteBuffer b = ByteBuffer.allocate(frameSize + 4);
        //Placing frameSize
        b.putInt(frameSize);
        
        //Placing from size and content
        b.putInt(from.getBytes().length);
        b.put(from.getBytes());
        
        //Placing type size and content
        b.putInt(type.getBytes().length);
        b.put(type.getBytes());
        
        //Placing signedPK size and content
        b.putInt(signedPK.length);
        b.put(signedPK);
        
        b.putInt(payload.length);
        b.put(payload);
        
        return b.array();        
    }
    
    /**
     * Unpacks a frame from a byte array
     * @param data
     * @return a frame from data in the byte array
     */
    public static Frame unpack(byte[] data){
        byte[] local = null;
        ByteBuffer b = ByteBuffer.wrap(data);
        Frame toReturn = new Frame();
        
        local = new byte[b.getInt()];
        b.get(local, 0, local.length);
        toReturn.setFrom(new String(local));
        
        local = new byte[b.getInt()];
        b.get(local, 0, local.length);
        toReturn.setType(new String(local));
        
        local = new byte[b.getInt()];
        b.get(local, 0, local.length);
        toReturn.setSignedPK(local);
        
        local = new byte[b.getInt()];
        b.get(local, 0, local.length);
        toReturn.setPayload(local);
        
        return toReturn;
    }
    
}
