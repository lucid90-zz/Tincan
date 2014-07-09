/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tincan.audio;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import javax.sound.sampled.LineUnavailableException;
import tincan.Controller;
import tincan.network.Frame;
import tincan.security.SecurityUtils;

/**
 *
 * @author LucianDobre
 */
public class Playback extends Thread{

    public volatile boolean running = true;
    public volatile Deque<Frame> buffer = new ConcurrentLinkedDeque<Frame>();
    AudioOutput stream;
    Controller ctrl;
    
    @Override
    public void run() {
        setName("StreamOutput");
        
        Frame c = null;
        int count;
        
        try{
            stream.getLine().open( 
                    stream.getAudioFormat(), 
                    stream.getLine().getBufferSize());
        } catch ( LineUnavailableException lue ){
            System.out.println("Unable to open the line: \n" + lue);
            return;
        }
        
        stream.getLine().start();
        
        System.out.println("Playback [Starting playback]");
        
        while ( running ){
            
            //Wait for frames to process
            while ( buffer.isEmpty() );
            c = buffer.removeLast();
            
            System.out.println("Playback [Playing back frame]");
            
            //Play frame
            count = c.getPayload().length;
            while ( count > 0) {
                count -= stream.getLine().write(c.getPayload() , c.getPayload().length - count , count);
            }
        }
    }
    
    public void addBytesToStream(Frame f){
        byte[] data = SecurityUtils.decryptPayload(
                getCtrl().getLoggedInContact().getKp().getPrivate(),
                f.getPayload());
        f.setPayload(data);
        buffer.add(f);
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
