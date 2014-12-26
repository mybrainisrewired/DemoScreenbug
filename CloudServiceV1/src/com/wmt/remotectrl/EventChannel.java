package com.wmt.remotectrl;

import android.os.Handler;
import android.os.SystemClock;
import com.wmt.remotectrl.EventPacket.DataWrapper;
import java.io.IOException;
import java.nio.channels.SocketChannel;

public abstract class EventChannel {
    protected Handler mHandler;
    private long mLastAcknowledgeTime;
    private long mLastWriteTime;
    private SocketChannel mSocketChannel;
    protected Handler mWriteCheckHandler;
    private Object mWriteLock;

    EventChannel(Handler handler) {
        this.mWriteLock = new Object();
        this.mLastAcknowledgeTime = 0;
        this.mHandler = handler;
    }

    public abstract boolean onReceived(DataWrapper dataWrapper);

    public void ping() throws IOException {
        if (this.mLastAcknowledgeTime == 0) {
            this.mLastAcknowledgeTime = SystemClock.uptimeMillis();
        }
        long curTime = SystemClock.uptimeMillis();
        if (curTime - this.mLastAcknowledgeTime >= 30000 || curTime - this.mLastWriteTime >= 8000 || curTime - this.mLastAcknowledgeTime > 16000) {
            setWriteDataBuffer(EventPacket.EVENT_ALIVE_PING, EventPacket.intToByteArray(1));
        }
    }

    public void resetCheckHandler(Handler handler) {
        synchronized (this.mWriteLock) {
            this.mWriteCheckHandler = handler;
        }
    }

    public void setChannel4Write(SocketChannel sc) {
        this.mSocketChannel = sc;
    }

    public void setWriteDataBuffer(int id, byte[] buf) {
        setWriteDataBufferDirect(EventPacket.getSimpleDataFormat(id, buf));
    }

    public void setWriteDataBufferDirect(byte[] buf) {
        synchronized (this.mWriteLock) {
            try {
                if (this.mWriteCheckHandler != null) {
                    this.mWriteCheckHandler.sendEmptyMessageDelayed(ConnectionInstance.WRITING_TIME_OUT, 3000);
                }
                EventPacket.writeData(this.mSocketChannel, buf);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (this.mWriteCheckHandler != null) {
                this.mWriteCheckHandler.removeMessages(ConnectionInstance.WRITING_TIME_OUT);
            }
        }
    }

    public void updateAcknowlegeTimeStamp() {
        this.mLastAcknowledgeTime = SystemClock.uptimeMillis();
    }
}