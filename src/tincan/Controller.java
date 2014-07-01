/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tincan;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import tincan.audio.AudioInput;
import tincan.audio.AudioOutput;
import tincan.audio.Capture;
import tincan.audio.Playback;
import tincan.entity.Contact;
import tincan.entity.Server;
import tincan.gui.CallControlsPanel;
import tincan.gui.ContactListPanel;
import tincan.gui.MainFrame;
import tincan.gui.ServerControlsPanel;
import tincan.network.CallEmiter;
import tincan.network.CallListener;
import tincan.security.SecurityUtils;

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
    CallEmiter emitter;
    
    /*Data*/
    Vector<Contact> contacts;
    Vector<Server> servers;
    
    /*LoggedIn user*/
    Contact loggedInContact;
    Server selectedServer;

    public Controller(String configFilesPath, MainFrame mf, ContactListPanel jpContacts, CallControlsPanel jpCall, ServerControlsPanel jpServer) {
        
        //Set Controller references
        this.mf = mf;
        this.jpContacts = jpContacts;
        this.jpCall = jpCall;
        this.jpServer = jpServer;
        
        //Inject backReference
        jpContacts.setCtrl(this);
        jpCall.setCtrl(this);
        jpServer.setCtrl(this);
        mf.setCtrl(this);
        
        /* Inject panels into main frame */
        mf.setJpContacts(jpContacts);
        mf.setJpCall(jpCall);
        mf.setJpServer(jpServer);
        mf.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {}

            @Override
            public void componentMoved(ComponentEvent e) {}

            @Override
            public void componentShown(ComponentEvent e) {}

            @Override
            public void componentHidden(ComponentEvent e) {
                ((MainFrame)e.getSource()).getCtrl().getListener().stopListening();
                ((MainFrame)e.getSource()).getCtrl().getEmitter().stopEmitting();
                ((MainFrame)e.getSource()).dispose();
            }
        });
        
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
        
        System.out.println(configFilesPath+"\\config");
        //Load from configFile
        try {
            BufferedReader br = new BufferedReader(new FileReader(configFilesPath + "\\config"));

            //TODO Load contacts
            loggedInContact = new Contact();
            loggedInContact.setFirstName(br.readLine());
            loggedInContact.setLastName(br.readLine());
            loggedInContact.setHostname(br.readLine());
            loggedInContact.setPort(br.readLine());
            loggedInContact.setKp(new KeyPair(
                    SecurityUtils.loadPublicKey(configFilesPath+"\\"+br.readLine(), "RSA"),
                    SecurityUtils.loadPrivateKey(configFilesPath+"\\"+br.readLine(), "RSA"))
            );
            
            contacts = new Vector();
            
            for (String line; (line = br.readLine()) != null;) {
                Contact c = new Contact();
                c.setFirstName(line);
                c.setLastName(br.readLine());
                c.setHostname(br.readLine());
                c.setPort(br.readLine());
                c.setKp(new KeyPair(
                        SecurityUtils.loadPublicKey(configFilesPath+"\\"+br.readLine(), "RSA"),
                        null)
                );
                contacts.add(c);
            }
    

            //TODO Load Servers
            servers = new Vector<>();
            selectedServer = new Server();
            selectedServer.setHostname("localhost");
            selectedServer.setPort("666666");
            selectedServer.setKp(null);
            servers.add(selectedServer);
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //TODO start general call listener
        listener = new CallListener();
        listener.setCtrl(this);
        listener.setPlayback(new Playback());
        listener.start();
        
        //TODO start general call emitter
        emitter = new CallEmiter();
        emitter.setCtrl(this);
        emitter.setCapture(new Capture());
        emitter.start();
    }
    
    /**
     * Calls Contact c
     * @param c 
     */
    public void call(Contact c){
 
    }
 
     /**
     * Hangs the currently ongoing call
     */
    public void hang(){
        listener.stopCall();
        emitter.stopCall();
    }

    public MainFrame getMf() {
        return mf;
    }
    
    public void start(){
        getMf().assemble();
    }
    
    public Contact getContactByPK( PublicKey pk ){
        for ( Contact c : contacts ){
            if ( c.getKp().getPublic().equals(pk))
                return c;
        }
        return null;
    }
    
    public Contact getContactByName(String name){
        for ( Contact c : contacts ){
            if ( c.toString().equals(name))
                return c;
        }
        return null;
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

    public CallListener getListener() {
        return listener;
    }

    public void setListener(CallListener listener) {
        this.listener = listener;
    }

    public CallEmiter getEmitter() {
        return emitter;
    }

    public void setEmitter(CallEmiter emitter) {
        this.emitter = emitter;
    }
    
}
