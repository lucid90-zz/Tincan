/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vipsclient.network;

import vipsclient.Controller;

/**
 *
 * @author LucianDobre
 */
public class CallListener extends Thread{

    public static final int LISTEN_PORT = 55500;
    
    Controller ctrl;
    
    @Override
    public void run() {
        
        //Register with server
        //TODO
        
        //Listen on socket for call
        
    }

    /**
     * Registers this client with the selected Server;
     * @return 
     */
    public boolean registerClient(){
        return true;
    }
    
    public Controller getCtrl() {
        return ctrl;
    }

    public void setCtrl(Controller ctrl) {
        this.ctrl = ctrl;
    }    

}
