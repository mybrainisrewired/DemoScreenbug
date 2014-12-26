package com.wmt.remotectrl;

import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.wmt.remotectrl.EventPacket.OnSocketMsgListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerDiscovery extends Thread {
    private static final int DISCOVERY_BROADCAST = 1;
    private static String TAG;
    private boolean mEnableBroadcast;
    private Handler mHandler;
    private InetAddress mInetAddress;
    private final Object mKeepAlive;
    private int mLocalIpAddrIntValue;
    private OnSocketMsgListener mSml;
    private DatagramSocket mUdpSocket;
    private WifiManager mWm;

    static {
        TAG = "ServiceDiscovery";
    }

    public ServerDiscovery(WifiManager wm, OnSocketMsgListener srl) {
        this.mWm = null;
        this.mLocalIpAddrIntValue = 0;
        this.mInetAddress = null;
        this.mEnableBroadcast = true;
        this.mKeepAlive = new Object();
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DISCOVERY_BROADCAST:
                        if (ServerDiscovery.this.mEnableBroadcast) {
                            try {
                                ServerDiscovery.this.discoveryBroadcast();
                            } catch (Exception e) {
                                e.printStackTrace();
                                ServerDiscovery.this.disconnect();
                                ServerDiscovery.this.mSml.onMySocketError();
                            }
                        }
                    default:
                        break;
                }
            }
        };
        this.mWm = wm;
        this.mSml = srl;
    }

    private void discoveryBroadcast() throws IOException {
        String data = EventPacket.CLIENT_PREFIX + this.mLocalIpAddrIntValue;
        this.mUdpSocket.send(new DatagramPacket(data.getBytes(), data.length(), this.mInetAddress, 8710));
        Log.d(TAG, "Broadcast...");
        if (this.mHandler != null) {
            this.mHandler.removeMessages(DISCOVERY_BROADCAST);
            this.mHandler.sendEmptyMessageDelayed(DISCOVERY_BROADCAST, 1000);
        }
    }

    private void getBroadcastAddress() throws IOException {
        if (this.mWm != null) {
            DhcpInfo di = this.mWm.getDhcpInfo();
            if (di == null) {
                Log.d(TAG, "Could not get dhcp info!");
            } else {
                this.mLocalIpAddrIntValue = di.ipAddress;
                int broadcastValue = (di.ipAddress & di.netmask) | (di.netmask ^ -1);
                byte[] section = new byte[4];
                int i = 0;
                while (i < 4) {
                    section[i] = (byte) ((broadcastValue >> (i * 8)) & 255);
                    i++;
                }
                this.mInetAddress = InetAddress.getByAddress(section);
            }
        }
    }

    public void disconnect() {
        synchronized (this.mKeepAlive) {
            this.mKeepAlive.notify();
        }
        if (!(this.mUdpSocket == null || this.mUdpSocket.isClosed())) {
            this.mUdpSocket.close();
            this.mUdpSocket = null;
        }
        if (this.mHandler != null) {
            this.mHandler.removeMessages(DISCOVERY_BROADCAST);
            this.mHandler = null;
        }
    }

    public void enableBroadcast(boolean enable) {
        if (this.mHandler != null) {
            this.mEnableBroadcast = enable;
            if (this.mEnableBroadcast) {
                this.mHandler.sendEmptyMessage(DISCOVERY_BROADCAST);
            } else {
                this.mHandler.removeMessages(DISCOVERY_BROADCAST);
            }
        }
    }

    public void run() {
        try {
            this.mUdpSocket = new DatagramSocket(8710);
            this.mUdpSocket.setBroadcast(true);
            getBroadcastAddress();
            new ServerResponse(this.mUdpSocket, this.mSml).start();
            this.mEnableBroadcast = true;
            this.mHandler.sendEmptyMessage(DISCOVERY_BROADCAST);
            synchronized (this.mKeepAlive) {
                this.mKeepAlive.wait();
            }
        } catch (Exception e) {
            Exception e2 = e;
            Log.d(TAG, "Can not broadcast!!!");
            e2.printStackTrace();
            disconnect();
            this.mSml.onMySocketError();
        }
    }
}