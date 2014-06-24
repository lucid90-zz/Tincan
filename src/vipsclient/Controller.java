/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vipsclient;

import java.util.Vector;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import vipsclient.audio.Capture;
import vipsclient.audio.Playback;
import vipsclient.entity.AudioInput;
import vipsclient.entity.AudioOutput;
import vipsclient.entity.Contact;
import vipsclient.gui.CallControlsPanel;
import vipsclient.gui.ContactListPanel;
import vipsclient.gui.MainFrame;
import vipsclient.gui.ServerControlsPanel;

/**
 *
 * @author LucianDobre
 */
public class Controller {
    
    /*GUI*/
    MainFrame mf;
    ContactListPanel jpContacts;
    CallControlsPanel jpCall;
    ServerControlsPanel jpServer;
    
    /*Coms*/
    AudioInput selectedAI;
    AudioOutput selectedAO;
    Capture capture;
    Playback playback;
    
    /*Volume*/
    int microphoneVolume, speakerVolume;
    
    /*Data*/
    Vector<Contact> contacts;
    

    public Controller(MainFrame mf, ContactListPanel jpContacts, CallControlsPanel jpCall, ServerControlsPanel jpServer) {
        
        //Set Controller references
        this.mf = mf;
        this.jpContacts = jpContacts;
        this.jpCall = jpCall;
        this.jpServer = jpServer;
        
        //Inject backReference
        jpContacts.setCtrl(this);
        jpCall.setCtrl(this);
        jpServer.setCtrl(this);
        
        /* Inject panels into main frame */
        mf.setJpContacts(jpContacts);
        mf.setJpCall(jpCall);
        mf.setJpServer(jpServer);
        
        /* Microphone initialization */
        selectedAI = new AudioInput();
        selectedAI.setAudioFormat(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0f, 16, 2, 4, 44100.0f, true));
        selectedAI.setLineInfo( new DataLine.Info(TargetDataLine.class,selectedAI.getAudioFormat()) );
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        for ( Mixer.Info info : mixerInfos ){
            Mixer m = AudioSystem.getMixer(info);
            Line.Info[] lineInfos = m.getSourceLineInfo();
            for (Line.Info lineInfo : lineInfos) {
                if ( lineInfo.equals(selectedAI.getLineInfo()) ){
                    selectedAI.setMixerName(m.getMixerInfo().getName());
                    break;
                }
            }
            if ( selectedAI.getMixerName() != null ) break;
        }
        try{
            selectedAI.setLine( (TargetDataLine) AudioSystem.getLine(selectedAI.getLineInfo()));
        } catch( LineUnavailableException lueEx){
            lueEx.printStackTrace();
            selectedAI = null;
        }
        
        /* Speaker initialization */
        selectedAO = new AudioOutput();
        selectedAO.setAudioFormat(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0f, 16, 2, 4, 44100.0f, true));
        selectedAO.setLineInfo( new DataLine.Info(SourceDataLine.class,selectedAO.getAudioFormat()) );
        mixerInfos = AudioSystem.getMixerInfo();
        for ( Mixer.Info info : mixerInfos ){
            Mixer m = AudioSystem.getMixer(info);
            Line.Info[] lineInfos = m.getSourceLineInfo();
            for (Line.Info lineInfo : lineInfos) {
                if ( lineInfo.equals(selectedAO.getLineInfo()) ){
                    selectedAO.setMixerName(m.getMixerInfo().getName());
                    break;
                }
            }
            if ( selectedAO.getMixerName() != null ) break;
        }
        try{
            selectedAO.setLine( (SourceDataLine) AudioSystem.getLine(selectedAO.getLineInfo()));
        } catch( LineUnavailableException lueEx){
            lueEx.printStackTrace();
            selectedAO = null;
        }
        
        microphoneVolume = 25;
        speakerVolume = 25;
        
        //TODO Load contacts from file
        contacts = new Vector();
        Contact c = new Contact();
        c.setFirstName("Lucian");
        c.setLastName("Dobre");
        c.setHostname("localhost");
        c.setPort("123456");
        c.setKp(null);
        contacts.add(c);
        
        //TODO Load general settings from file
    }
    
    public void call(Contact c){
        
    }
    
    public void hang(){
        
    }

    public MainFrame getMf() {
        return mf;
    }
    
    public void start(){
        getMf().assemble();
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

    public Vector<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Vector<Contact> contacts) {
        this.contacts = contacts;
    }

    public AudioInput getSelectedAI() {
        return selectedAI;
    }

    public void setSelectedAI(AudioInput selectedAI) {
        this.selectedAI = selectedAI;
    }

    public AudioOutput getSelectedAO() {
        return selectedAO;
    }

    public void setSelectedAO(AudioOutput selectedAO) {
        this.selectedAO = selectedAO;
    }

    public int getMicrophoneVolume() {
        return microphoneVolume;
    }

    public void setMicrophoneVolume(int microphoneVolume) {
        this.microphoneVolume = microphoneVolume;
    }

    public int getSpeakerVolume() {
        return speakerVolume;
    }

    public void setSpeakerVolume(int speakerVolume) {
        this.speakerVolume = speakerVolume;
    }

    public Capture getCapture() {
        return capture;
    }

    public void setCapture(Capture capture) {
        this.capture = capture;
    }

    public Playback getPlayback() {
        return playback;
    }

    public void setPlayback(Playback playback) {
        this.playback = playback;
    }
    
    
    
    
}
