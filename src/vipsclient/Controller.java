/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vipsclient;

import vipsclient.gui.CallControlsPanel;
import vipsclient.gui.ContactListPanel;
import vipsclient.gui.MainFrame;
import vipsclient.gui.ServerControlsPanel;

/**
 *
 * @author LucianDobre
 */
public class Controller {
    MainFrame mf;
    ContactListPanel jpContacts;
    CallControlsPanel jpCall;
    ServerControlsPanel jpServer;

    public Controller(MainFrame mf, ContactListPanel jpContacts, CallControlsPanel jpCall, ServerControlsPanel jpServer) {
        this.mf = mf;
        this.jpContacts = jpContacts;
        this.jpCall = jpCall;
        this.jpServer = jpServer;
        
        //TODO Load contacts from file
        
        //TODO Load general settings from file
        
        /* Inject panels into main frame */
        mf.setJpContacts(jpContacts);
        mf.setJpCall(jpCall);
        mf.setJpServer(jpServer);
    }

    public MainFrame getMf() {
        return mf;
    }
    
    public void start(){
        getMf().loadFrame();
        getMf().setVisible(true);
    }

    public void setMf(MainFrame mf) {
        this.mf = mf;
    }

    public ContactListPanel getJpContacts() {
        return jpContacts;
    }

    public void setJpContacts(ContactListPanel jpContacts) {
        this.jpContacts = jpContacts;
    }

    public CallControlsPanel getJpCall() {
        return jpCall;
    }

    public void setJpCall(CallControlsPanel jpCall) {
        this.jpCall = jpCall;
    }

    public ServerControlsPanel getJpServer() {
        return jpServer;
    }

    public void setJpServer(ServerControlsPanel jpServer) {
        this.jpServer = jpServer;
    }
    
    
}
