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
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import javax.swing.JOptionPane;
import tincan.Controller;
import tincan.audio.Playback;
import tincan.entity.Contact;
import tincan.security.SecurityUtils;

/**
 *
 * @author LucianDobre
 */
public class CallListener extends Thread{

    public volatile boolean running = true;
    public volatile boolean inCall = false;
    
    private ServerSocketChannel serverSocket = null;
    private SocketChannel requestSocket = null;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
    
    Playback playback;
    Contact partner;
    Controller ctrl;
    
    @Override
    public void run(){
        
        //Register with server
        //TODO - LAAAATER version
        if ( !registerClient() )
            JOptionPane.showMessageDialog(getCtrl().getMf(), "Could not register to default server.");
        
        
        Contact c = null;
        
        try {
            
        
            serverSocket = ServerSocketChannel.open();
            
            System.out.println(getCtrl().getLoggedInContact().getHostname()+"\n"+getCtrl().getLoggedInContact().getPort());
             
            serverSocket.socket().bind(
                    new InetSocketAddress(
                            getCtrl().getLoggedInContact().getHostname(), 
                            Integer.parseInt(getCtrl().getLoggedInContact().getPort())));
           
            //Listen on socket for call
            while ( running ){
                
                requestSocket = serverSocket.accept();
                in = new ObjectInputStream(requestSocket.socket().getInputStream());
                out = new ObjectOutputStream(requestSocket.socket().getOutputStream());
                
                byte frameSize = in.readByte();
                byte[] frameBuffer = new byte[frameSize];
                byte pos = 0, len;
                
                while ( pos < frameSize ){
                    len = (byte) in.read(frameBuffer, pos, frameSize - pos);
                    pos += len;
                }
                
                Frame req = Frame.unpack(frameBuffer);
                Frame res = null;
                
                switch( Integer.parseInt(req.getType()) ){
                    case Frame.CALL:
                        if ( inCall == true){ //Reject call because we are already in a call
                            res = new Frame();
                            res.setFrom(getCtrl().getLoggedInContact().toString());
                            res.setType(""+Frame.REJECT);
                            res.setSignedPK(
                                    SecurityUtils.sign(
                                            getCtrl().getLoggedInContact().getKp().getPrivate(),
                                            getCtrl().getLoggedInContact().getKp().getPublic().getEncoded()
                                    ));
                            res.setPayload(new byte[0]);
                            out.write(res.pack());
                            break;
                        }
                        
                        //Authenticate user
                        c = getCtrl().getContactByName(req.getFrom());
                        if ( !SecurityUtils.verifySignature( req.getSignedPK(), c.getKp().getPublic().getEncoded())){
                            res = new Frame();
                            res.setFrom(getCtrl().getLoggedInContact().toString());
                            res.setType(""+Frame.REJECT);
                            res.setSignedPK(
                                    SecurityUtils.sign(
                                            getCtrl().getLoggedInContact().getKp().getPrivate(),
                                            getCtrl().getLoggedInContact().getKp().getPublic().getEncoded()
                                    ));
                            res.setPayload(new byte[0]);
                            out.write(res.pack());
                            break;
                        }
                        
                        //Accept call
                        setPartner(getCtrl().getContactByName(req.getFrom()));
                        getCtrl().getEmitter().setPartner(partner);
                        res = new Frame();
                        res.setFrom(getCtrl().getLoggedInContact().toString());
                        res.setType(""+Frame.RESPOND);
                        res.setSignedPK(
                                SecurityUtils.sign(
                                        getCtrl().getLoggedInContact().getKp().getPrivate(),
                                        getCtrl().getLoggedInContact().getKp().getPublic().getEncoded()
                                ));
                        res.setPayload(new byte[0]);
                        
                        //Mark client as inside conversation
                        startCall();
                        getCtrl().getEmitter().startCall();
                        
                        //Send response
                        out.write(res.pack());
                        
                        //Await Three-way-handshake conclusion
                        while (true) {
                            requestSocket = serverSocket.accept();

                            frameSize = in.readByte();
                            frameBuffer = new byte[frameSize];
                            pos = 0;

                            while (pos < frameSize) {
                                len = (byte) in.read(frameBuffer, pos, frameSize - pos);
                                pos += len;
                            }

                            req = Frame.unpack(frameBuffer);

                            //Authenticate the response
                            c = getCtrl().getContactByName(req.getFrom());
                            if (!SecurityUtils.verifySignature(req.getSignedPK(), c.getKp().getPublic().getEncoded())) {
                                res = new Frame();
                                res.setFrom(getCtrl().getLoggedInContact().toString());
                                res.setType("" + Frame.REJECT);
                                res.setSignedPK(
                                        SecurityUtils.sign(
                                                getCtrl().getLoggedInContact().getKp().getPrivate(),
                                                getCtrl().getLoggedInContact().getKp().getPublic().getEncoded()
                                        ));
                                res.setPayload(new byte[0]);
                                out.write(res.pack());
                                continue;
                            }
                            break;
                        }
                        
                        //If partner does not aknowledge my credentials
                        if (Integer.parseInt(req.getType()) != Frame.ACKNOWLEDGE) {
                            getCtrl().hang();
                            break;
                        }

                        break;
                    case Frame.DATA:
                        if ( inCall == false){ //Reject data because we are not in a call
                            res = new Frame();
                            res.setFrom(getCtrl().getLoggedInContact().toString());
                            res.setType(""+Frame.REJECT);
                            res.setSignedPK(
                                    SecurityUtils.sign(
                                            getCtrl().getLoggedInContact().getKp().getPrivate(),
                                            getCtrl().getLoggedInContact().getKp().getPublic().getEncoded()
                                    ));
                            res.setPayload(new byte[0]);
                            out.write(res.pack());
                            break;
                        }
                        
                        //Authenticate user
                        c = getPartner();
                        if ( !SecurityUtils.verifySignature( req.getSignedPK(), c.getKp().getPublic().getEncoded())){
                            res = new Frame();
                            res.setFrom(getCtrl().getLoggedInContact().toString());
                            res.setType(""+Frame.REJECT);
                            res.setSignedPK(
                                    SecurityUtils.sign(
                                            getCtrl().getLoggedInContact().getKp().getPrivate(),
                                            getCtrl().getLoggedInContact().getKp().getPublic().getEncoded()
                                    ));
                            res.setPayload(new byte[0]);
                            out.write(res.pack());
                            break;
                        }
                        
                        //Send data for playback stream;
                        getPlayback().addBytesToStream(
                                SecurityUtils.decryptPayload(
                                        getCtrl().getLoggedInContact().getKp().getPrivate(),
                                        req.getPayload()));
                        break;
                    case Frame.CLOSE:
                        if ( inCall == false){ //Reject close request because we are not in a call
                            res = new Frame();
                            res.setFrom(getCtrl().getLoggedInContact().toString());
                            res.setType(""+Frame.REJECT);
                            res.setSignedPK(
                                    SecurityUtils.sign(
                                            getCtrl().getLoggedInContact().getKp().getPrivate(),
                                            getCtrl().getLoggedInContact().getKp().getPublic().getEncoded()
                                    ));
                            res.setPayload(new byte[0]);
                            out.write(res.pack());
                            break;
                        }
                        
                        //Authenticate user
                        c = getPartner();
                        if ( !SecurityUtils.verifySignature( req.getSignedPK(), c.getKp().getPublic().getEncoded())){
                            res = new Frame();
                            res.setFrom(getCtrl().getLoggedInContact().toString());
                            res.setType(""+Frame.REJECT);
                            res.setSignedPK(
                                    SecurityUtils.sign(
                                            getCtrl().getLoggedInContact().getKp().getPrivate(),
                                            getCtrl().getLoggedInContact().getKp().getPublic().getEncoded()
                                    ));
                            res.setPayload(new byte[0]);
                            out.write(res.pack());
                            break;
                        }
                        
                        //Hang the call
                        getCtrl().hang();
                        break;
                    default:
                        break;
                     
                }
                
            }
            
        } catch (IOException ex) {
            System.out.println("Network error starting listener");
        }        
    }

    /**
     * Registers this client with the selected Server;
     * @return 
     */
    public boolean registerClient(){
        return true;
    }
    
    /**
     * UnRegisters this client with the selected Server when switching servers;
     * @return 
     */
    public void unregisterClient(){
    }
    
    public Controller getCtrl() {
        return ctrl;
    }

    public void setCtrl(Controller ctrl) {
        this.ctrl = ctrl;
    }
    
    public void stopListening(){
        running = false;
    }
    
    public void stopCall(){
        setPartner(null);
        inCall = false;
    }
    
    public void startCall(){
        inCall = true;
    }

    public Playback getPlayback() {
        return playback;
    }

    public void setPlayback(Playback playback) {
        this.playback = playback;
    }

    public Contact getPartner() {
        return partner;
    }

    public void setPartner(Contact partner) {
        this.partner = partner;
    }
}
