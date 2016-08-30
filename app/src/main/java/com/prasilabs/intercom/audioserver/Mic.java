package com.prasilabs.intercom.audioserver;

import android.media.AudioRecord;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Mic extends Thread {
    public static DatagramSocket socket;
    private int audioFormat;
    public byte[] buffer;
    private int channelConfig;
    public String ip;
    int minBufSize;
    private int port;
    AudioRecord recorder;
    private int sampleRate;

    public Mic(String ip)
    {
        this.ip = ip;
        this.port = 55001;
        this.sampleRate = 44100;
        this.channelConfig = 2;
        this.audioFormat = 2;
        this.minBufSize = AudioRecord.getMinBufferSize(this.sampleRate, this.channelConfig, this.audioFormat);
    }

    public void run() {
        try {
            Log.d("print", "Mic is started");
            this.minBufSize += AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
            InetAddress addr = InetAddress.getByName(ip);
            DatagramSocket socket = new DatagramSocket();
            Log.d("VS", "Socket Created");
            byte[] buffer = new byte[this.minBufSize];
            Log.d("VS", "Buffer created of size " + this.minBufSize);
            System.out.println("Address is " + addr);
            Log.d("VS", "Address retrieved");
            this.recorder = new AudioRecord(1, this.sampleRate, this.channelConfig, this.audioFormat, this.minBufSize * 10);
            Log.d("VS", "Recorder initialized");
            this.recorder.startRecording();
            while (true) {
                Log.d("print", "At mic");
                this.minBufSize = this.recorder.read(buffer, 0, buffer.length);
                socket.send(new DatagramPacket(buffer, buffer.length, addr, this.port));
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.e("VS", "UnknownHostException");
        } catch (IOException e2) {
            e2.printStackTrace();
            Log.e("VS", "IOException");
        }
        interrupt();
    }
}