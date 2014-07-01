/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tincan.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import tincan.Controller;
import tincan.audio.Capture;
import tincan.audio.Playback;
import tincan.entity.Contact;
import tincan.security.SecurityUtils;

/**
 *
 * @author LucianDobre
 */
public class CallEmiter extends Thread{
    
    public volatile boolean running = true;
    public volatile boolean inCall = false;
    boolean established = false;
    Capture capture;
    Contact partner;
    
    Controller ctrl;
    
    @Override
    public void run() {
        
        SocketChannel requestChannel = null;
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        Frame req = null, res = null;
        Contact c = null;
        
        byte frameSize, pos, len;
        byte[] frameBuffer = null;
 
        while (running) {

            try {

                if (inCall) {//If in call need to establish link
                    if (!established) { //initial part of protocol
                        
                        requestChannel = SocketChannel.open(
                                new InetSocketAddress(
                                        partner.getHostname(), 
                                        Integer.parseInt(partner.getPort())));
                        out = new ObjectOutputStream(requestChannel.socket().getOutputStream());
                        in = new ObjectInputStream(requestChannel.socket().getInputStream());
                        
                        //Send Call request
                        req = new Frame();
                        req.setFrom(getCtrl().getLoggedInContact().toString());
                        req.setType(""+Frame.CALL);
                        req.setSignedPK(SecurityUtils.sign(
                                getCtrl().getLoggedInContact().getKp().getPrivate(),
                                getCtrl().getLoggedInContact().getKp().getPublic().getEncoded()));
                        req.setPayload(new byte[0]);
                        
                        out.write(req.pack());
                        
                        
                        //Wait for proper response
                        while ( true ){
                            //Receive response
                            frameSize = in.readByte();
                            frameBuffer = new byte[frameSize];
                            pos = 0;

                            while (pos < frameSize) {
                                len = (byte) in.read(frameBuffer, pos, frameSize - pos);
                                pos += len;
                            }

                            res = Frame.unpack(frameBuffer);
                            
                            //Authenticate the response
                            c = getCtrl().getContactByName(res.getFrom());
                            if (!SecurityUtils.verifySignature(res.getSignedPK(), c.getKp().getPublic().getEncoded())) {
                                req = new Frame();
                                req.setFrom(getCtrl().getLoggedInContact().toString());
                                req.setType("" + Frame.REJECT);
                                req.setSignedPK(
                                        SecurityUtils.sign(
                                                getCtrl().getLoggedInContact().getKp().getPrivate(),
                                                getCtrl().getLoggedInContact().getKp().getPublic().getEncoded()
                                        ));
                                req.setPayload(new byte[0]);
                                out.write(req.pack());
                                continue;
                            }
                            break;
                        }
                        
                        
                            
                        if ( Integer.parseInt(res.getType()) != Frame.RESPOND ){
                            getCtrl().hang();
                            break;
                        }
                        
                        req = new Frame();
                        req.setFrom(getCtrl().getLoggedInContact().toString());
                        req.setType(""+Frame.ACKNOWLEDGE);
                        req.setSignedPK(SecurityUtils.sign(
                                getCtrl().getLoggedInContact().getKp().getPrivate(),
                                getCtrl().getLoggedInContact().getKp().getPublic().getEncoded()
                        ));
                        req.setPayload(new byte[0]);
                        out.write(req.pack());
                        
                    } else { // data streaming part of protocol
                        requestChannel = SocketChannel.open(
                                new InetSocketAddress(
                                        partner.getHostname(), 
                                        Integer.parseInt(partner.getPort())));
                        out = new ObjectOutputStream(requestChannel.socket().getOutputStream());
                        in = new ObjectInputStream(requestChannel.socket().getInputStream());
                        
                        //Send Call request
                        req = new Frame();
                        req.setFrom(getCtrl().getLoggedInContact().toString());
                        req.setType(""+Frame.DATA);
                        req.setSignedPK(SecurityUtils.sign(
                                getCtrl().getLoggedInContact().getKp().getPrivate(),
                                getCtrl().getLoggedInContact().getKp().getPublic().getEncoded()));
                        req.setPayload(getCapture().getStreamBytes());
                        
                        out.write(req.pack());
                    }
                }
            } catch (IOException ex) {
                System.out.println("Could not open socket for Emitter");
            }

        }
    }

    /**
     * Talks to the server to see if Contact is online
     * @param c
     * @return an updated Contact with hostname and port or null if not online
     */
    public Contact isOnline(Contact c){
        return null;
    }

    public Capture getCapture() {
        return capture;
    }

    public void setCapture(Capture capture) {
        this.capture = capture;
    }

    public Contact getPartner() {
        return partner;
    }

    public void setPartner(Contact partner) {
        this.partner = partner;
    }
    
    public void stopEmitting(){
        running = false;
    }
    
    public void stopCall(){
        setPartner(null);
        inCall = false;
    }
    
    public void startCall(){
        inCall = true;
    }

    public Controller getCtrl() {
        return ctrl;
    }

    public void setCtrl(Controller ctrl) {
        this.ctrl = ctrl;
    }
}
