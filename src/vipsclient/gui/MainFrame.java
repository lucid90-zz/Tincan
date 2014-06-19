/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vipsclient.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author LucianDobre
 */
public class MainFrame extends JFrame{

    CallControlsPanel jpCall;
    ContactListPanel jpContacts;
    ServerControlsPanel jpServer;
    
    public MainFrame(){
        setTitle("Vips");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    public void loadFrame(){
        setLayout( new BorderLayout() );
        jpContacts.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                        "Contacts"));
        getContentPane().add(jpContacts, BorderLayout.EAST);
        
        jpCall.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                        "Call Details"));
        getContentPane().add(jpCall, BorderLayout.WEST);
        
        jpServer.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                        "Server Details"));
        getContentPane().add(jpServer, BorderLayout.SOUTH);
    }

    public CallControlsPanel getJpCall() {
        return jpCall;
    }

    public void setJpCall(CallControlsPanel jpCall) {
        this.jpCall = jpCall;
    }

    public ContactListPanel getJpContacts() {
        return jpContacts;
    }

    public void setJpContacts(ContactListPanel jpContacts) {
        this.jpContacts = jpContacts;
    }

    public ServerControlsPanel getJpServer() {
        return jpServer;
    }

    public void setJpServer(ServerControlsPanel jpServer) {
        this.jpServer = jpServer;
    }
    
    
    
}
