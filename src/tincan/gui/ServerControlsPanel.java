/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tincan.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import tincan.Controller;
import tincan.entity.Server;

/**
 *
 * @author LucianDobre
 */
public class ServerControlsPanel extends JPanel implements ActionListener{
    
    Controller ctrl;
    
    JButton jbConnect, jbSettings;
    JTextArea jtaPeerSelect;

    public ServerControlsPanel() {
        setPreferredSize(new Dimension(600,50));
        setLayout(new BorderLayout());
    }
    
    /* Call assemble only after all dependencies have been injected*/
    public void assemble(){
        jbConnect = new JButton("Find");
        jbConnect.addActionListener(this);
        add(jbConnect, BorderLayout.WEST);
        
        jbSettings = new JButton("Settings");
        jbSettings.addActionListener(this);
        add(jbSettings, BorderLayout.EAST );
        
        jtaPeerSelect = new JTextArea("Name of peer to find on network");
        add(jtaPeerSelect, BorderLayout.CENTER);
    }

    public Controller getCtrl() {
        return ctrl;
    }

    public void setCtrl(Controller ctrl) {
        this.ctrl = ctrl;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("ServerControlsPanel");
        
        if ( e.getSource().equals(jbSettings) ){
            System.out.println("Selected to modify settings");
        }
    }
    
}
