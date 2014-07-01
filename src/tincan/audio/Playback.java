/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tincan.audio;

import tincan.Controller;

/**
 *
 * @author LucianDobre
 */
public class Playback extends Thread{

    public volatile boolean running = true;
    AudioOutput stream;
    Controller ctrl;
    
    @Override
    public void run() {
        setName("StreamOutput");
    }

    public void addBytesToStream(byte[] data){
        
    }
    
    public AudioOutput getStream() {
        return stream;
    }

    public void setStream(AudioOutput stream) {
        this.stream = stream;
    }
    
    public void stopPlaying(){
        running = false;
    }

    public Controller getCtrl() {
        return ctrl;
    }

    public void setCtrl(Controller ctrl) {
        this.ctrl = ctrl;
    }
    
}
