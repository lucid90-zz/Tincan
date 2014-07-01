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
import javax.swing.JPanel;
import tincan.Controller;

/**
 *
 * @author LucianDobre
 */
public class WavePanel extends JPanel implements ActionListener{
    
    Controller ctrl;

    public WavePanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(230, 200));
    }
    
    /* Call assemble only after all dependencies have been injected*/
    public void assemble(){
        
    }

    public Controller getCtrl() {
        return ctrl;
    }

    public void setCtrl(Controller ctrl) {
        this.ctrl = ctrl;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("WavePanel");
    }
    
    
}
