/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vipsclient.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import vipsclient.Controller;

/**
 *
 * @author LucianDobre
 */
public class ControlsPanel extends JPanel implements ActionListener, ChangeListener{

    Controller ctrl;
    
    JButton jbCloseCall, jbStartCall;
    
    JSlider jsAudioInVolume, jsAudioOutVolume;
    
    JComboBox<String> jcbAudioIn, jcbAudioOut;
    
    public ControlsPanel() {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(230, 150));
    }
    
    /* Call assemble only after all dependencies have been injected*/
    public void assemble(){
        GridBagConstraints c = new GridBagConstraints();
        
        jbStartCall = new JButton("Call");
        c.gridy = 0;
        c.gridx = 0;
        c.ipady = 10;
        jbStartCall.addActionListener(this);
        add(jbStartCall, c);
        
        jbCloseCall = new JButton("Close");
        c.gridy = 0;
        c.gridx = 1;
        jbCloseCall.addActionListener(this);
        add(jbCloseCall, c);
        
        jsAudioInVolume = new JSlider(JSlider.VERTICAL, 0, 100, 25);
        jsAudioInVolume.setMinorTickSpacing(5);
        jsAudioInVolume.setMajorTickSpacing(25);
        jsAudioInVolume.setPaintTicks(true);
        jsAudioInVolume.setPaintLabels(true);
        jsAudioInVolume.setLabelTable(jsAudioInVolume.createStandardLabels(25));
        c.gridy = 1;
        c.gridx = 0;
        c.ipady = 40;
        jsAudioInVolume.addChangeListener(this);
        add(jsAudioInVolume, c);
        
        jsAudioOutVolume = new JSlider(JSlider.VERTICAL, 0, 100, 25);
        jsAudioOutVolume.setMinorTickSpacing(5);
        jsAudioOutVolume.setMajorTickSpacing(25);
        jsAudioOutVolume.setPaintTicks(true);
        jsAudioOutVolume.setPaintLabels(true);
        jsAudioOutVolume.setLabelTable(jsAudioOutVolume.createStandardLabels(25));
        c.gridy = 1;
        c.gridx = 1;
        jsAudioOutVolume.addChangeListener(this);
        add(jsAudioOutVolume, c);
        
        //TODO PROPER MICROPHONE SELECTOR
        Vector<String> mockInput = new Vector<>();
        mockInput.add("Default Microphone");
        jcbAudioIn = new JComboBox<>(mockInput);
        c.gridy = 2;
        c.gridx = 0;
        c.ipady = 10;
        c.ipadx = -15;
        jcbAudioIn.addActionListener(this);
        add(jcbAudioIn, c);
        
        
        //TODO PROPER SPEAKER SELECTOR
        Vector<String> mockOutput = new Vector<>();
        mockOutput.add("Default Speaker");
        jcbAudioOut = new JComboBox<>(mockOutput);
        c.gridy = 2;
        c.gridx = 1;
        c.ipady = 10;
        c.ipadx = -15;
        jcbAudioOut.addActionListener(this);
        add(jcbAudioOut, c);
    }

    public Controller getCtrl() {
        return ctrl;
    }

    public void setCtrl(Controller ctrl) {
        this.ctrl = ctrl;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("ControlsPanel");
        
        if ( e.getSource().equals(jbStartCall)){
            System.out.println("Pressed to Start Call");
        }
        
        if ( e.getSource().equals(jbCloseCall)){
            System.out.println("Pressed to End Call");
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        
        if ( e.getSource().equals(jsAudioInVolume) )
            if ( !jsAudioInVolume.getValueIsAdjusting() ){
                System.out.println("Adjusted microphone volume to "+ (int)jsAudioInVolume.getValue() );
                getCtrl().setMicrophoneVolume((int)jsAudioInVolume.getValue());
            }
        
        if ( e.getSource().equals(jsAudioOutVolume) )
            if ( !jsAudioOutVolume.getValueIsAdjusting() ){
                System.out.println("Adjusted speaker volume to "+ (int)jsAudioOutVolume.getValue() );
                getCtrl().setSpeakerVolume((int)jsAudioOutVolume.getValue());
            }
    }
}
