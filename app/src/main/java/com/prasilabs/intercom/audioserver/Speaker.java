package com.prasilabs.intercom.audioserver;

import android.media.AudioRecord;
import android.media.AudioTrack;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Speaker extends Thread {
    private int audioFormat;
    private int channelConfig;
    int minBufSize;
    int port;
    private int sampleRate;

    public Speaker() {
        this.port = 55001;
        this.sampleRate = 44100;
        this.channelConfig = 1;
        this.audioFormat = 2;
        this.minBufSize = AudioRecord.getMinBufferSize(this.sampleRate, this.channelConfig, this.audioFormat);
    }

    public void run() {
        Log.d("print", "Speaker is started");
        byte[] data = new byte[AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD];
        try {
            DatagramSocket dsk = new DatagramSocket(this.port);
            DatagramPacket dgp = new DatagramPacket(data, data.length);
            AudioTrack speaker = new AudioTrack(0, this.sampleRate, this.channelConfig, this.audioFormat, this.minBufSize, 1);
            while (true) {
                dsk.receive(dgp);
                speaker.write(dgp.getData(), 0, this.minBufSize);
                System.out.println("Playing");
                speaker.play();
            }
        } catch (SocketException e) {
            Log.d("OOPS", "Speaker server got exception");
            interrupt();
            start();
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}