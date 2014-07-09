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
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import tincan.Controller;
import tincan.audio.Capture;
import tincan.entity.Contact;
import tincan.security.SecurityUtils;

/**
 *
 * @author LucianDobre
 */
public class CallEmitter extends Thread{
    
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
        
        int frameSize, pos, len;
        byte[] frameBuffer = null;
        
        System.out.println("STARTING EMITTER");
 
        while (running) {

            try {

                if (inCall) {//If in call need to establish link
                    if (!established) { //initial part of protocol
                        System.out.println("Proceeding to establish call to \nH-["+partner.getHostname()+"]\nP-["+partner.getPort()+"]");
                        requestChannel = SocketChannel.open(
                                new InetSocketAddress(
                                        partner.getHostname(), 
                                        Integer.parseInt(partner.getPort())));
                        out = new ObjectOutputStream(requestChannel.socket().getOutputStream());
                        in  = new ObjectInputStream( requestChannel.socket().getInputStream() );
                        
                        //Send Call request
                        req = new Frame();
                        req.setFrom(getCtrl().getLoggedInContact().toString());
                        req.setType(""+Frame.CALL);
                        req.setSignedPK(SecurityUtils.sign(
                                getCtrl().getLoggedInContact().getKp().getPrivate(),
                                getCtrl().getLoggedInContact().getKp().getPublic().getEncoded()));
                        req.setPayload(new byte[0]);

                        System.out.println("Emitter [Tx][CALL]");
                        out.write(req.pack());
                        out.reset();
                        
                        
                        //Wait for proper response
                        while ( true ){
                            //Receive response
                            frameSize = in.readInt();
                            frameBuffer = new byte[frameSize];
                            pos = 0;

                            while (pos < frameSize) {
                                len = in.read(frameBuffer, pos, frameSize - pos);
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
                                
                                System.out.println("Emitter [Tx][REJECT]");
                                out.write(req.pack());
                                out.reset();
                                continue;
                            }
                            break;
                        }
                        
                        
                            
                        if ( Integer.parseInt(res.getType()) != Frame.RESPOND ){
                            getCtrl().hang();
                            break;
                        }
                        System.out.println("Emitter [Rx][RESPOND]");
                        
                        req = new Frame();
                        req.setFrom(getCtrl().getLoggedInContact().toString());
                        req.setType(""+Frame.ACKNOWLEDGE);
                        req.setSignedPK(SecurityUtils.sign(
                                getCtrl().getLoggedInContact().getKp().getPrivate(),
                                getCtrl().getLoggedInContact().getKp().getPublic().getEncoded()
                        ));
                        req.setPayload(new byte[0]);
                        
                        System.out.println("Emitter [Tx][ACKNOWLEDGE]");
                        out.write(req.pack());
                        out.reset();
                        
                        capture = new Capture();
                        capture.setStream(getCtrl().getSelectedAI());
                        capture.setCtrl(getCtrl());
                        capture.start();
                        
                        established = true;
                        
                    } else { // data streaming part of protocol
                        System.out.println("Emitter [Tx][DATA1]");
                        requestChannel = SocketChannel.open(
                                new InetSocketAddress(
                                        partner.getHostname(), 
                                        Integer.parseInt(partner.getPort())));
                        out = new ObjectOutputStream(requestChannel.socket().getOutputStream());
                        in = new ObjectInputStream(requestChannel.socket().getInputStream());
                        
                        //Send Call request
                        req = getCapture().getStreamBytes();
                        
                        System.out.println("Emitter [Tx][DATA2]");
                        out.write(req.pack());
                        out.reset();
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
