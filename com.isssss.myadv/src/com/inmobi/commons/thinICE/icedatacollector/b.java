package com.inmobi.commons.thinICE.icedatacollector;

import android.util.Log;

// compiled from: IceDataCollector.java
static class b implements Runnable {
    b() {
    }

    public void run() {
        synchronized (IceDataCollector.class) {
            if (BuildSettings.DEBUG) {
                Log.d(IceDataCollector.TAG, "** stop runnable");
            }
            if (IceDataCollector.e) {
                if (BuildSettings.DEBUG) {
                    Log.d(IceDataCollector.TAG, "terminating sampling and flushing");
                }
                IceDataCollector.o();
                IceDataCollector.flush();
                IceDataCollector.a = null;
                IceDataCollector.f = null;
                IceDataCollector.k = null;
            } else {
                if (BuildSettings.DEBUG) {
                    Log.d(IceDataCollector.TAG, "ignoring, stop not requested");
                }
            }
        }
    }
}