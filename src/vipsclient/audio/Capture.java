/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vipsclient.audio;

import vipsclient.entity.AudioInput;

/**
 *
 * @author LucianDobre
 */
public class Capture extends Thread{

    AudioInput stream;    
    
    @Override
    public void run() {
        setName("StreamInput");
    }

    public AudioInput getStream() {
        return stream;
    }

    public void setStream(AudioInput stream) {
        this.stream = stream;
    }
    
}
