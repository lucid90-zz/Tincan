/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vipsclient.entity;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author LucianDobre
 */
public class AudioInput {
    private String mixerName;
    private AudioFormat audioFormat;
    private DataLine.Info lineInfo;
    private TargetDataLine line;

    public String getMixerName() {
        return mixerName;
    }

    public void setMixerName(String mixerName) {
        this.mixerName = mixerName;
    }

    public AudioFormat getAudioFormat() {
        return audioFormat;
    }

    public void setAudioFormat(AudioFormat audioFormat) {
        this.audioFormat = audioFormat;
    }

    public DataLine.Info getLineInfo() {
        return lineInfo;
    }

    public void setLineInfo(DataLine.Info lineInfo) {
        this.lineInfo = lineInfo;
    }

    public TargetDataLine getLine() {
        return line;
    }

    public void setLine(TargetDataLine line) {
        this.line = line;
    }    
}
