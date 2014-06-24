/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vipsclient.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.border.EtchedBorder;
import vipsclient.Controller;

/**
 *
 * @author LucianDobre
 */
public class MainFrame extends JFrame implements ActionListener{

    Controller ctrl;
    
    CallControlsPanel jpCall;
    ContactListPanel jpContacts;
    ServerControlsPanel jpServer;
    
    public MainFrame(){
        setTitle("Vips");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    /* Call assemble only after all dependencies have been injected*/
    public void assemble(){
        setLayout(new BorderLayout());

        if (jpContacts != null) {
            jpContacts.setBorder(
                    BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                            "Contacts"));
            jpContacts.assemble();
            getContentPane().add(jpContacts, BorderLayout.EAST);
        }

        if (jpCall != null) {
            jpCall.setBorder(
                    BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                            "Call Details"));
            jpCall.assemble();
            getContentPane().add(jpCall, BorderLayout.WEST);
        }

        if (jpServer != null) {
            jpServer.setBorder(
                    BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                            "Server Details"));
            jpServer.assemble();
            getContentPane().add(jpServer, BorderLayout.SOUTH);
        }
        
        setVisible(true);
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

    public Controller getCtrl() {
        return ctrl;
    }

    public void setCtrl(Controller ctrl) {
        this.ctrl = ctrl;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("MainFrame");
    }
}
