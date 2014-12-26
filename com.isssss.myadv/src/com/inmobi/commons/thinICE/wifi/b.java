package com.inmobi.commons.thinICE.wifi;

// compiled from: WifiScanner.java
static class b implements Runnable {
    b() {
    }

    public void run() {
        WifiScanListener a = WifiScanner.b;
        WifiScanner.d();
        if (a != null) {
            a.onTimeout();
        }
    }
}