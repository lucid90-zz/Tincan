/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vipsclient.network;

import vipsclient.Controller;
import vipsclient.audio.Capture;
import vipsclient.audio.Playback;
import vipsclient.entity.Contact;

/**
 *
 * @author LucianDobre
 */
public class CallMaintainer extends Thread{
    
    public volatile boolean running = true;
    public boolean initiator;
    Capture capture;
    Playback playback;
    Contact partner;
    
    Controller ctrl;
    
    
    @Override
    public void run(){
        
        //If 
        if (initiator) {

            //Ask server if contact is online
            partner = isOnline(partner);

            if (partner == null) {
                System.out.println("Partner not online");
                return;
            }

            //Call contact through protocol
            //TODO
        }
        
        //Exchange frame data
        //TODO
        
        //Close call
        //TODO
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
    
    public void stopCall(){
        running = false;
    }

    public Controller getCtrl() {
        return ctrl;
    }

    public void setCtrl(Controller ctrl) {
        this.ctrl = ctrl;
    }

    public boolean isInitiator() {
        return initiator;
    }

    public void setInitiator(boolean initiator) {
        this.initiator = initiator;
    }
    
}
