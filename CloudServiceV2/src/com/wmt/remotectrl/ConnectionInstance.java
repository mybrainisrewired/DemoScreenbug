package com.wmt.remotectrl;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.wmt.remotectrl.EventPacket.DataWrapper;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class ConnectionInstance {
    public static final int ALL_CONNECT_SUCCEED = 40;
    public static final int ALL_DISCONNECTED = 41;
    public static final int CONNECTION_FAILED = 45;
    public static final int SERVER_RESOLUTION_UPDATE = 42;
    public static final int SERVER_SCREEN_IMAGE_UPDATE = 43;
    public static final int WRITING_TIME_OUT = 44;
    private final String TAG;
    private boolean mActiveDisconnect;
    private WritingCheckHandler mCheckHandler;
    ArrayList<EventChannel> mEventChannels;
    public GsensorEventChannel mGsensorEventChannel;
    private Handler mHandler;
    public HashMap<SelectionKey, EventChannel> mHashMap;
    public KeyTouchEventChannel mKeyTouchEventChannel;
    private Object mLock;
    public OtherEventChannel mOtherEventChannel;
    public int[] mPortArray;
    private Selector mSelector;
    public String mServerIp;

    class WritingCheckHandler extends Handler {
        WritingCheckHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            if (msg.what == 44) {
                Log.d("ConnectionInstance", "WRITING_TIME_OUT...");
                ConnectionInstance.this.disconnectAll();
                ConnectionInstance.this.mKeyTouchEventChannel.resetCheckHandler(null);
                ConnectionInstance.this.mGsensorEventChannel.resetCheckHandler(null);
                ConnectionInstance.this.mOtherEventChannel.resetCheckHandler(null);
                ConnectionInstance.this.mHandler.sendEmptyMessage(CONNECTION_FAILED);
            }
        }
    }

    public ConnectionInstance(String serverIp, Handler handler) {
        this.TAG = "ConnectionInstance";
        this.mEventChannels = new ArrayList();
        this.mPortArray = new int[]{8711, 8713, 8714};
        this.mHashMap = new HashMap();
        this.mLock = new Object();
        this.mServerIp = serverIp;
        this.mHandler = handler;
        System.setProperty("java.net.preferIPv6Addresses", "false");
    }

    public void channelInit() {
        HandlerThread ht = new HandlerThread("write check");
        ht.start();
        if (this.mCheckHandler != null) {
            this.mCheckHandler.getLooper().quit();
            this.mCheckHandler = null;
        }
        this.mCheckHandler = new WritingCheckHandler(ht.getLooper());
        this.mKeyTouchEventChannel = new KeyTouchEventChannel(this.mHandler);
        this.mGsensorEventChannel = new GsensorEventChannel(this.mHandler);
        this.mOtherEventChannel = new OtherEventChannel(this.mHandler);
        this.mEventChannels.add(this.mKeyTouchEventChannel);
        this.mEventChannels.add(this.mGsensorEventChannel);
        this.mEventChannels.add(this.mOtherEventChannel);
        int i = 0;
        while (i < this.mEventChannels.size()) {
            ((EventChannel) this.mEventChannels.get(i)).resetCheckHandler(this.mCheckHandler);
            i++;
        }
        this.mActiveDisconnect = false;
    }

    public void connect2All(Handler handler) throws IOException {
        disconnectAll();
        channelInit();
        this.mSelector = Selector.open();
        int i = 0;
        while (i < this.mPortArray.length && !this.mActiveDisconnect) {
            Log.d("ConnectionInstance", "SocketChannel.open timeout i=" + i);
            SocketChannel sc = SocketChannel.open(new InetSocketAddress(this.mServerIp, this.mPortArray[i]));
            sc.configureBlocking(false);
            sc.socket().setTcpNoDelay(true);
            if (this.mSelector != null) {
                SelectionKey sk = sc.register(this.mSelector, 1);
                ((EventChannel) this.mEventChannels.get(i)).setChannel4Write(sc);
                this.mHashMap.put(sk, this.mEventChannels.get(i));
            } else {
                sc.close();
            }
            i++;
        }
        if (this.mActiveDisconnect) {
            disconnectAll();
        } else {
            handler.sendEmptyMessage(ALL_CONNECT_SUCCEED);
            Selector selector = this.mSelector;
            while (selector.isOpen()) {
                int ret = selector.select(30000);
                if (ret > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = (SelectionKey) iterator.next();
                        iterator.remove();
                        if (key.isValid() && key.isReadable()) {
                            doRead(key);
                        }
                    }
                } else if (ret == -1) {
                    Log.d("ConnectionInstance", "quit");
                    break;
                } else {
                    Log.d("ConnectionInstance", "selector ret = " + ret);
                }
            }
            disconnectAll();
            if (!this.mActiveDisconnect) {
                handler.sendEmptyMessage(ALL_DISCONNECTED);
            }
        }
    }

    public void disconnectAll() {
        if (this.mCheckHandler != null) {
            this.mCheckHandler.getLooper().quit();
            this.mCheckHandler = null;
        }
        synchronized (this.mLock) {
            try {
                if (this.mSelector != null) {
                    Iterator<Entry<SelectionKey, EventChannel>> iterator = this.mHashMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = (SelectionKey) ((Entry) iterator.next()).getKey();
                        key.cancel();
                        SocketChannel sc = (SocketChannel) key.channel();
                        if (sc != null) {
                            sc.close();
                        }
                    }
                    this.mSelector.close();
                    this.mSelector = null;
                }
                this.mEventChannels.clear();
                this.mHashMap.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnectAll(boolean active) {
        this.mActiveDisconnect = active;
        disconnectAll();
    }

    void doRead(SelectionKey key) throws IOException {
        SocketChannel readChannel = (SocketChannel) key.channel();
        EventChannel ec = (EventChannel) this.mHashMap.get(key);
        if (readChannel != null && ec != null) {
            DataWrapper dw = EventPacket.readDataWrapper(readChannel);
            if (dw.mId == -1) {
                Log.d("ConnectionInstance", "WHAT'S UP");
                throw new IOException();
            } else if (dw.mId == 101) {
                Log.d("ConnectionInstance", "Received acknowledge");
                ec.updateAcknowlegeTimeStamp();
            } else {
                ec.onReceived(dw);
            }
        }
    }

    public void sendPing2All() throws IOException {
        if (this.mOtherEventChannel != null) {
            this.mOtherEventChannel.ping();
        }
    }
}