package com.wmt.remotectrl;

import android.os.Handler;
import com.wmt.remotectrl.EventPacket.DataWrapper;

public class KeyTouchEventChannel extends EventChannel {
    KeyTouchEventChannel(Handler handler) {
        super(handler);
    }

    public boolean onReceived(DataWrapper dw) {
        return false;
    }
}