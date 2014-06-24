/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vipsclient.audio;

import java.io.ByteArrayOutputStream;
import javax.sound.sampled.LineUnavailableException;
import vipsclient.Controller;

/**
 *
 * @author LucianDobre
 */
public class Capture extends Thread{

    public volatile boolean running = true;
    AudioInput stream;
    Controller ctrl;
    
    @Override
    public void run() {
        setName("StreamInput");
        
        try {
            stream.getLine().open(
                    stream.getAudioFormat(),
                    stream.getLine().getBufferSize());
        } catch (LineUnavailableException ex) {
            System.out.println("Unable to open the line: \n" + ex);
            return;
        }
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int frameSizeInBytes = stream.getAudioFormat().getFrameSize();
        int bufferLengthInFrames = stream.getLine().getBufferSize() / 8 ;
        int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
        byte[] data = new byte[bufferLengthInBytes];
        
        int numBytesRead;
        
        stream.getLine().start();
        
        while ( running ){
            //Create a frame
            
            //Send frame over network
        }
        
        
    }

    public AudioInput getStream() {
        return stream;
    }

    public void setStream(AudioInput stream) {
        this.stream = stream;
    }

    public void stopCapturing(){
        running = false;
    }

    public Controller getCtrl() {
        return ctrl;
    }

    public void setCtrl(Controller ctrl) {
        this.ctrl = ctrl;
    }
    
}
