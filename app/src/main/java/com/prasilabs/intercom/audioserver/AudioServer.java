package com.prasilabs.intercom.audioserver;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class AudioServer extends Thread
{

    int port = 50005;
    //AudioFormat format = new AudioFormat.Builder().build();

    private int sampleRate = 16000;
    @SuppressWarnings("deprecation")
    private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);

    public void run()
    {
        byte[] data = new byte[3000];

        try
        {


            DatagramSocket dsk = new DatagramSocket(port);
            DatagramPacket dgp = new DatagramPacket(data, data.length);

//         ByteArrayInputStream bis = new ByteArrayInputStream(dgp.getData());

            AudioTrack speaker = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, channelConfig, audioFormat, minBufSize, AudioTrack.MODE_STREAM);

            while(true)
            {
                dsk.receive(dgp);

                byte[] addata = dgp.getData();


                Log.d("AS", "recieved " + addata.length);


                speaker.write(addata, 0, addata.length);

                Log.d("AS", "playing");
                System.out.println("Playing");
                speaker.play();
            }

        } catch(SocketException e)
        {
            e.printStackTrace();
        } catch(IOException e)
        {
            e.printStackTrace();
        }

        Log.d("AS", "ended");
    }

}
