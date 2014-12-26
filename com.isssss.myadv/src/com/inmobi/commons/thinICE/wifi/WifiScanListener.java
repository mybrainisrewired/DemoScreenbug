package com.inmobi.commons.thinICE.wifi;

import android.net.wifi.ScanResult;
import java.util.List;

public interface WifiScanListener {
    void onResultsReceived(List<ScanResult> list);

    void onTimeout();
}