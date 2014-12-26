package com.wmt.remotectrl;

import android.util.Log;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

public class DiscoveryListener extends Thread {
    private final String TAG;
    boolean mQuit;
    private boolean mStopUdpListening;
    DatagramSocket mUdpSocket;

    public DiscoveryListener() {
        this.TAG = "DiscoveryListener";
        this.mUdpSocket = null;
        this.mQuit = false;
    }

    private void listeningDiscovery() {
        while (!this.mStopUdpListening) {
            try {
                byte[] buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                Log.d("DiscoveryListener", "UDP block here to test");
                this.mUdpSocket.receive(packet);
                SocketAddress myAddr = packet.getSocketAddress();
                String s = new String(packet.getData(), 0, packet.getLength());
                Log.d("DiscoveryListener", "Received: " + s);
                if (validateRecvMsg(s)) {
                    responseToClient(myAddr);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void close(boolean quit) {
        this.mQuit = quit;
        if (this.mUdpSocket != null && !this.mUdpSocket.isClosed()) {
            this.mStopUdpListening = true;
            this.mUdpSocket.close();
            this.mUdpSocket = null;
        }
    }

    void init() throws SocketException {
        close(false);
        this.mUdpSocket = new DatagramSocket(8710);
        this.mUdpSocket.setReuseAddress(true);
    }

    void responseToClient(SocketAddress sa) throws IOException {
        String data = EventPacket.SERVER_PREFIX;
        this.mUdpSocket.send(new DatagramPacket(data.getBytes(), 0, data.length(), sa));
    }

    public void run() {
        do {
            try {
                init();
                this.mStopUdpListening = false;
                listeningDiscovery();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (!this.mQuit);
    }

    boolean validateRecvMsg(String msg) {
        return msg.length() > 0 && msg.startsWith(EventPacket.CLIENT_PREFIX);
    }
}