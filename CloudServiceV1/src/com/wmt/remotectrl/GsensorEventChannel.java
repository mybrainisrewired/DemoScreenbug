package com.wmt.remotectrl;

import android.os.Handler;
import android.os.Message;
import com.wmt.remotectrl.EventPacket.DataWrapper;

public class GsensorEventChannel extends EventChannel {
    GsensorEventChannel(Handler handler) {
        super(handler);
    }

    public boolean onReceived(DataWrapper dw) {
        if (dw.mId == 8713) {
            int values = EventPacket.byteArrayToInt(dw.mData, 0);
            Message message = this.mHandler.obtainMessage(EventPacket.G_SENSOR_REGISTER);
            this.mHandler.removeMessages(message.what);
            message.arg1 = values;
            this.mHandler.sendMessageDelayed(message, 0);
        }
        return false;
    }
}