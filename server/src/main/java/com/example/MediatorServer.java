package com.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by prasi on 30/8/16.
 */
public class MediatorServer extends Thread
{
    int port;

    public MediatorServer()
    {
        this.port = 55001;
    }

    public void run() {
        //Log.getLog("print", "Speaker is started");
        byte[] data = new byte[4096];
        try {
            DatagramSocket dsk = new DatagramSocket(this.port);
            DatagramPacket dgp = new DatagramPacket(data, data.length);
            while (true)
            {
                dsk.receive(dgp);
            }
        } catch (SocketException e) {
            //Log.d("OOPS", "Speaker server got exception");
            interrupt();
            start();
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}
