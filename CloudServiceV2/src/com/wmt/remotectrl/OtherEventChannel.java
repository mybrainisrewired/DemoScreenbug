package com.wmt.remotectrl;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.wmt.remotectrl.EventPacket.DataWrapper;
import org.codehaus.jackson.impl.JsonWriteContext;

public class OtherEventChannel extends EventChannel {
    private final String TAG;

    OtherEventChannel(Handler handler) {
        super(handler);
        this.TAG = "OtherEventChannel";
    }

    public void actionSnapshot() {
        setWriteDataBuffer(JsonWriteContext.STATUS_EXPECT_VALUE, EventPacket.intToByteArray(1));
    }

    public boolean onReceived(DataWrapper dw) {
        Message msg;
        if (dw.mId == 8712) {
            String flag = new String(dw.mData);
            if (flag.equals(EventPacket.INPUT_SHOW)) {
                this.mHandler.sendEmptyMessage(EventPacket.SHOW_IME);
                Log.d("OtherEventChannel", "Show IME");
            } else if (flag.equals(EventPacket.INPUT_HIDE)) {
                this.mHandler.sendEmptyMessage(EventPacket.HIDE_IME);
                Log.d("OtherEventChannel", "Hide IME");
            } else {
                msg = new Message();
                msg.obj = flag;
                msg.what = 203;
                this.mHandler.sendMessage(msg);
                Log.d("OtherEventChannel", "SYNC_INFO");
            }
        } else if (dw.mId == 102) {
            int w = EventPacket.byteArrayToInt(dw.mData, 0);
            int h = EventPacket.byteArrayToInt(dw.mData, JsonWriteContext.STATUS_EXPECT_VALUE);
            Log.d("OtherEventChannel", "Server resolution is " + w + ":" + h);
            msg = new Message();
            msg.arg1 = w;
            msg.arg2 = h;
            msg.what = 42;
            this.mHandler.sendMessage(msg);
        } else if (dw.mId == 103) {
            msg = new Message();
            msg.obj = dw.mData;
            msg.what = 43;
            this.mHandler.sendMessage(msg);
        }
        return false;
    }
}