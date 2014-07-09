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
public class Capture extends Thread{

    public volatile boolean running = true;
    public volatile Deque<Frame> buffer = new ConcurrentLinkedDeque<Frame>();
    AudioInput stream;
    Controller ctrl;
    
    @Override
    public void run() {
        setName("StreamInput");
        
        Frame c = null;
        
        try {
            stream.getLine().open(
                    stream.getAudioFormat(),
                    stream.getLine().getBufferSize());
        } catch (LineUnavailableException ex) {
            System.out.println("Unable to open the line: \n" + ex);
            return;
        }
        
        int frameSizeInBytes = stream.getAudioFormat().getFrameSize();
        int bufferLengthInFrames = stream.getLine().getBufferSize() / 8 ;
        int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
        byte[] data = new byte[bufferLengthInBytes];
        
        stream.getLine().start();
        
        System.out.println("Capture [Starting to capture]");
        
        while ( running ){
            //Create a frame
            stream.getLine().read(data, 0 , bufferLengthInBytes);
            
            System.out.println("Capture [Recording frame]");
            
            //Add new frame to captureBuffer
            c = new Frame();
            c.setFrom(getCtrl().getLoggedInContact().toString());
            c.setType("" + Frame.DATA);
            c.setSignedPK(SecurityUtils.sign(
                    getCtrl().getLoggedInContact().getKp().getPrivate(),
                    getCtrl().getLoggedInContact().getKp().getPublic().getEncoded()));
            c.setPayload(data);
            buffer.addFirst(c);
        }
        
        stream.getLine().stop();
        stream.getLine().close();
        
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
    
    public Frame getStreamBytes(){
        while ( buffer.isEmpty() );
        return buffer.removeLast();
    }
}
