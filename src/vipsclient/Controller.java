/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vipsclient;

import java.security.PublicKey;
import java.util.Vector;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import vipsclient.audio.AudioInput;
import vipsclient.audio.AudioOutput;
import vipsclient.audio.Capture;
import vipsclient.audio.Playback;
import vipsclient.entity.Contact;
import vipsclient.entity.Server;
import vipsclient.gui.CallControlsPanel;
import vipsclient.gui.ContactListPanel;
import vipsclient.gui.MainFrame;
import vipsclient.gui.ServerControlsPanel;
import vipsclient.network.CallMaintainer;
import vipsclient.network.CallListener;

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
    
    /*Audio*/
    AudioInput selectedAI;
    AudioOutput selectedAO;
    
    /*Volume*/
    int microphoneVolume, speakerVolume;
    
    /*Security*/
    PublicKey spk;
    
    /*Network*/
    CallListener listener;
    CallMaintainer maintainer;
    
    /*Data*/
    Vector<Contact> contacts;
    Vector<Server> servers;
    
    /*LoggedIn user*/
    Contact loggedInContact;
    Server selectedServer;
    

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
        
        /* Set default starting volumes */
        microphoneVolume = 25;
        speakerVolume = 25;
        
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
        
        //TODO Load contacts
        loggedInContact = new Contact();
        loggedInContact.setFirstName("Valentin");
        loggedInContact.setLastName("Dobre");
        loggedInContact.setHostname("localhost");
        loggedInContact.setPort("1234567");
        loggedInContact.setKp(null);
        
        contacts = new Vector();
        Contact c = new Contact();
        c.setFirstName("Lucian");
        c.setLastName("Dobre");
        c.setHostname("localhost");
        c.setPort("123456");
        c.setKp(null);
        contacts.add(c);
        
        //TODO Load Servers
        servers = new Vector<>();
        selectedServer = new Server();
        selectedServer.setHostname("localhost");
        selectedServer.setPort("666666");
        selectedServer.setKp(null);
        
        servers.add(selectedServer);
        
        
    }
    
    /**
     * Calls Contact c creating all needed threads and so on
     * @param c 
     */
    public void call(Contact c){
        maintainer = new CallMaintainer();
        maintainer.setPartner(c);
        maintainer.setCapture(new Capture());
        maintainer.setPlayback(new Playback());
        maintainer.setInitiator(true);
        maintainer.start();
    }
    
    public void hang(){
        maintainer.stopCall();
        maintainer = null;
    }

    public MainFrame getMf() {
        return mf;
    }
    
    public void start(){
        getMf().assemble();
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

    public Vector<Server> getServers() {
        return servers;
    }

    public void setServers(Vector<Server> servers) {
        this.servers = servers;
    }

    public Contact getLoggedInContact() {
        return loggedInContact;
    }

    public void setLoggedInContact(Contact loggedInContact) {
        this.loggedInContact = loggedInContact;
    }

    public Server getSelectedServer() {
        return selectedServer;
    }

    public void setSelectedServer(Server selectedServer) {
        this.selectedServer = selectedServer;
    }
    
}
