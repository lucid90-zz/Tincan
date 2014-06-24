/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vipsclient.audio;

import vipsclient.entity.AudioOutput;

/**
 *
 * @author LucianDobre
 */
public class Playback extends Thread{

    AudioOutput stream;
    
    @Override
    public void run() {
        setName("StreamOutput");
    }

    public AudioOutput getStream() {
        return stream;
    }

    public void setStream(AudioOutput stream) {
        this.stream = stream;
    }
    
}
