/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tincan;

import tincan.gui.CallControlsPanel;
import tincan.gui.ContactListPanel;
import tincan.gui.MainFrame;
import tincan.gui.ServerControlsPanel;

/**
 *
 * @author LucianDobre
 */
public class Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        if ( args.length < 1 ){
            System.out.println("Not enough arguments, use something like:\njava tincan pathToConfigFile");
            return;
        }
        
        new Controller(
                args[0],
                new MainFrame(),
                new ContactListPanel(),
                new CallControlsPanel(),
                new ServerControlsPanel()
        ).start();
    }
    
}
