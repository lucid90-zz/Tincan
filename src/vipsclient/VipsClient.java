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
public class VipsClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new Controller(
                new MainFrame(),
                new ContactListPanel(),
                new CallControlsPanel(),
                new ServerControlsPanel()
        ).start();
    }
    
}
