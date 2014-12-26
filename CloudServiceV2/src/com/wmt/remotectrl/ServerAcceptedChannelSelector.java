package com.wmt.remotectrl;

import android.util.Log;
import com.wmt.remotectrl.EventPacket.DataWrapper;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.http.util.ByteArrayBuffer;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

public class ServerAcceptedChannelSelector extends Thread {
    private static final int CONNECT_INTERRUPT = 401;
    private static final int CONNECT_SUCCUSS = 400;
    private final String TAG;
    SelectionKey mAcceptKey;
    DataWrapperListener mEventsListener;
    int mHeight;
    boolean mQuit;
    ArrayList<SelectionKey> mSelectionKeys;
    Selector mSelector;
    private int mTcpPort;
    int mWidth;
    Object mWriteLock;

    public static interface DataWrapperListener {
        void handleDataWrapper(DataWrapper dataWrapper);
    }

    public ServerAcceptedChannelSelector(int port, DataWrapperListener listener, int w, int h) {
        this.TAG = "ServerAcceptedChannelSelector";
        this.mWriteLock = new Object();
        this.mSelectionKeys = new ArrayList();
        this.mTcpPort = port;
        this.mEventsListener = listener;
        this.mSelectionKeys.clear();
        this.mQuit = false;
        this.mWidth = w;
        this.mHeight = h;
    }

    public void cleanup(boolean quit) {
        int i = 0;
        while (i < this.mSelectionKeys.size()) {
            SelectionKey sk = (SelectionKey) this.mSelectionKeys.get(i);
            sk.cancel();
            SocketChannel sc = (SocketChannel) sk.channel();
            if (sc != null) {
                try {
                    sc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            i++;
        }
        if (this.mAcceptKey != null) {
            this.mAcceptKey.cancel();
            ServerSocketChannel sc2 = (ServerSocketChannel) this.mAcceptKey.channel();
            if (sc2 != null) {
                try {
                    sc2.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
        this.mSelectionKeys.clear();
        if (this.mSelector != null) {
            try {
                this.mSelector.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            this.mSelector = null;
        }
    }

    void doAccept(SelectionKey key) throws IOException {
        Log.d("ServerAcceptedChannelSelector", "Accept is ready");
        SocketChannel sc = ((ServerSocketChannel) key.channel()).accept().socket().getChannel();
        sc.configureBlocking(false);
        this.mSelectionKeys.add(sc.register(this.mSelector, 1));
        DataWrapper dw = new DataWrapper();
        dw.mId = 400;
        dw.mData = sc.socket().getInetAddress().getHostAddress().getBytes();
        if (this.mEventsListener != null) {
            this.mEventsListener.handleDataWrapper(dw);
        }
        ByteArrayBuffer bab = new ByteArrayBuffer(16);
        bab.append(EventPacket.intToByteArray((int)Opcodes.FSUB), 0, EventPacket.BYTES_PER_INT);
        bab.append(EventPacket.intToByteArray(EventPacket.BYTES_PER_INT * 2), 0, EventPacket.BYTES_PER_INT);
        bab.append(EventPacket.intToByteArray(this.mWidth), 0, EventPacket.BYTES_PER_INT);
        bab.append(EventPacket.intToByteArray(this.mHeight), 0, EventPacket.BYTES_PER_INT);
        setWriteDataBuffer(bab.toByteArray());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void doRead(java.nio.channels.SelectionKey r9_key) throws java.io.IOException {
        throw new UnsupportedOperationException("Method not decompiled: com.wmt.remotectrl.ServerAcceptedChannelSelector.doRead(java.nio.channels.SelectionKey):void");
        /*
        r8 = this;
        r4 = r9.channel();
        r4 = (java.nio.channels.SocketChannel) r4;
        if (r4 == 0) goto L_0x0018;
    L_0x0008:
        r1 = com.wmt.remotectrl.EventPacket.readDataWrapper(r4);	 Catch:{ Exception -> 0x0035 }
        r6 = r1.mId;	 Catch:{ Exception -> 0x0035 }
        r7 = -1;
        if (r6 != r7) goto L_0x0019;
    L_0x0011:
        r6 = "ServerAcceptedChannelSelector";
        r7 = "Read error";
        android.util.Log.d(r6, r7);	 Catch:{ Exception -> 0x0035 }
    L_0x0018:
        return;
    L_0x0019:
        r6 = r1.mId;	 Catch:{ Exception -> 0x0035 }
        r7 = 100;
        if (r6 != r7) goto L_0x0071;
    L_0x001f:
        r6 = "ServerAcceptedChannelSelector";
        r7 = "Received ping and acknowledge to sender";
        android.util.Log.d(r6, r7);	 Catch:{ Exception -> 0x0035 }
        r6 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        r7 = 1;
        r7 = com.wmt.remotectrl.EventPacket.intToByteArray(r7);	 Catch:{ Exception -> 0x0035 }
        r0 = com.wmt.remotectrl.EventPacket.getSimpleDataFormat(r6, r7);	 Catch:{ Exception -> 0x0035 }
        r8.setWriteDataBuffer(r0);	 Catch:{ Exception -> 0x0035 }
        goto L_0x0018;
    L_0x0035:
        r2 = move-exception;
        r2.printStackTrace();
        r9.cancel();
        r5 = r9.channel();
        r5 = (java.nio.channels.SocketChannel) r5;
        r1 = new com.wmt.remotectrl.EventPacket$DataWrapper;
        r1.<init>();
        r6 = 401; // 0x191 float:5.62E-43 double:1.98E-321;
        r1.mId = r6;
        r6 = r5.socket();
        r6 = r6.getInetAddress();
        r6 = r6.getHostAddress();
        r6 = r6.getBytes();
        r1.mData = r6;
        r6 = r8.mEventsListener;
        if (r6 == 0) goto L_0x0066;
    L_0x0061:
        r6 = r8.mEventsListener;
        r6.handleDataWrapper(r1);
    L_0x0066:
        if (r5 == 0) goto L_0x006b;
    L_0x0068:
        r5.close();	 Catch:{ IOException -> 0x007b }
    L_0x006b:
        r6 = r8.mSelectionKeys;
        r6.remove(r9);
        goto L_0x0018;
    L_0x0071:
        r6 = r8.mEventsListener;	 Catch:{ Exception -> 0x0035 }
        if (r6 == 0) goto L_0x0018;
    L_0x0075:
        r6 = r8.mEventsListener;	 Catch:{ Exception -> 0x0035 }
        r6.handleDataWrapper(r1);	 Catch:{ Exception -> 0x0035 }
        goto L_0x0018;
    L_0x007b:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x006b;
        */
    }

    void doWrite(byte[] buf) throws IOException {
        synchronized (this.mWriteLock) {
            int i = 0;
            while (i < this.mSelectionKeys.size()) {
                SelectionKey sk = (SelectionKey) this.mSelectionKeys.get(i);
                if (sk.isValid()) {
                    SocketChannel writeChannel = (SocketChannel) sk.channel();
                    if (writeChannel != null) {
                        EventPacket.writeData(writeChannel, buf);
                    }
                }
                i++;
            }
        }
    }

    public void run() {
        while (true) {
            try {
                startAccept();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!this.mQuit) {
                try {
                    Thread.sleep(5000);
                } catch (Exception e2) {
                }
            } else {
                return;
            }
        }
    }

    public void setWriteDataBuffer(int id, byte[] data) {
        setWriteDataBuffer(EventPacket.getSimpleDataFormat(id, data));
    }

    public void setWriteDataBuffer(byte[] buf) {
        try {
            doWrite(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void startAccept() throws IOException {
        cleanup(false);
        this.mSelector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(this.mTcpPort));
        ssc.configureBlocking(false);
        this.mAcceptKey = ssc.register(this.mSelector, Segment.TOKENS_PER_SEGMENT);
        Selector selector = this.mSelector;
        while (selector.isOpen()) {
            int ret = selector.select(30000);
            if (ret > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = (SelectionKey) iterator.next();
                    iterator.remove();
                    if (key.isValid()) {
                        if (key.isAcceptable()) {
                            doAccept(key);
                        } else if (key.isReadable()) {
                            doRead(key);
                        }
                    }
                }
            } else if (ret == -1) {
                return;
            }
        }
    }
}