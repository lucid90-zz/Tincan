/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vipsclient.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import vipsclient.Controller;

/**
 *
 * @author LucianDobre
 */
public class CallControlsPanel extends JPanel implements ActionListener{

    Controller ctrl;
    WavePanel jpWavePanel;
    ControlsPanel jpControls;
    
    public CallControlsPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(240,350));    
    }
    
    public void assemble(){
        jpWavePanel = new WavePanel();
        jpWavePanel.setBorder( BorderFactory.createLoweredBevelBorder() );
        jpWavePanel.setCtrl(ctrl);
        jpWavePanel.assemble();
        add(jpWavePanel, BorderLayout.CENTER);
        
        jpControls = new ControlsPanel();
        jpControls.setCtrl(ctrl);
        jpControls.assemble();
        add(jpControls, BorderLayout.SOUTH);
    }
    
    public Controller getCtrl() {
        return ctrl;
    }

    public void setCtrl(Controller ctrl) {
        this.ctrl = ctrl;
    }

    public WavePanel getJpWavePanel() {
        return jpWavePanel;
    }

    public void setJpWavePanel(WavePanel jpWavePanel) {
        this.jpWavePanel = jpWavePanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("CallControlsPanel");
    }
}
