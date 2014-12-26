package com.inmobi.commons.thinICE.icedatacollector;

import android.net.wifi.ScanResult;
import android.util.Log;
import com.inmobi.commons.thinICE.wifi.WifiInfo;
import com.inmobi.commons.thinICE.wifi.WifiScanListener;
import com.inmobi.commons.thinICE.wifi.WifiUtil;
import java.util.Iterator;
import java.util.List;

// compiled from: IceDataCollector.java
static class a implements Runnable {

    // compiled from: IceDataCollector.java
    class a implements WifiScanListener {
        final /* synthetic */ boolean a;
        final /* synthetic */ boolean b;

        a(boolean z, boolean z2) {
            this.a = z;
            this.b = z2;
        }

        public void onResultsReceived(List<ScanResult> list) {
            if (BuildSettings.DEBUG) {
                Log.d(IceDataCollector.TAG, "Received Wi-Fi scan results " + list.size());
            }
            List scanResultsToWifiInfos = WifiUtil.scanResultsToWifiInfos(list, this.a, this.b);
            if (BuildSettings.DEBUG) {
                Log.d(IceDataCollector.TAG, "-- wifi scan:");
                Iterator it = scanResultsToWifiInfos.iterator();
                while (it.hasNext()) {
                    Log.d(IceDataCollector.TAG, "   + " + ((WifiInfo) it.next()));
                }
            }
            synchronized (IceDataCollector.j) {
                if (IceDataCollector.i != null) {
                    IceDataCollector.i.visibleWifiAp = scanResultsToWifiInfos;
                    a.this.a();
                }
            }
        }

        public void onTimeout() {
            if (BuildSettings.DEBUG) {
                Log.w(IceDataCollector.TAG, "Received Wi-Fi scan timeout");
            }
        }
    }

    a() {
    }

    private void a() {
        if (IceDataCollector.l != null) {
            IceDataCollector.l.onDataCollected();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        throw new UnsupportedOperationException("Method not decompiled: com.inmobi.commons.thinICE.icedatacollector.a.run():void");
        /*
        r9 = this;
        r6 = 2;
        r1 = 0;
        r4 = 0;
        r0 = 1;
        r2 = com.inmobi.commons.thinICE.icedatacollector.BuildSettings.DEBUG;
        if (r2 == 0) goto L_0x000f;
    L_0x0008:
        r2 = "IceDataCollector";
        r3 = "** sample runnable";
        android.util.Log.d(r2, r3);
    L_0x000f:
        r2 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.c;
        if (r2 != 0) goto L_0x0021;
    L_0x0015:
        r0 = com.inmobi.commons.thinICE.icedatacollector.BuildSettings.DEBUG;
        if (r0 == 0) goto L_0x0020;
    L_0x0019:
        r0 = "IceDataCollector";
        r1 = "sampling when looper is null, exiting";
        android.util.Log.w(r0, r1);
    L_0x0020:
        return;
    L_0x0021:
        r2 = android.os.Build.VERSION.SDK_INT;
        r3 = 14;
        if (r2 >= r3) goto L_0x005d;
    L_0x0027:
        r2 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.f;
        r2 = r2.hasWindowFocus();
        if (r2 != 0) goto L_0x005d;
    L_0x0031:
        r0 = com.inmobi.commons.thinICE.icedatacollector.BuildSettings.DEBUG;
        if (r0 == 0) goto L_0x003c;
    L_0x0035:
        r0 = "IceDataCollector";
        r1 = "activity no longer has focus, terminating";
        android.util.Log.d(r0, r1);
    L_0x003c:
        com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.o();
        r0 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.h;
        com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.flush();
        com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.a = r4;
        com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.f = r4;
        r1 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.k;
        if (r1 == 0) goto L_0x0059;
    L_0x0052:
        r1 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.k;
        r1.onSamplingTerminated(r0);
    L_0x0059:
        com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.k = r4;
        goto L_0x0020;
    L_0x005d:
        r2 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.d;
        r3 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.m;
        r4 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.b;
        r4 = r4.getSampleInterval();
        r2.postDelayed(r3, r4);
        r2 = com.inmobi.commons.thinICE.icedatacollector.BuildSettings.DEBUG;
        if (r2 == 0) goto L_0x009a;
    L_0x0074:
        r2 = "IceDataCollector";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "next sample in ";
        r3 = r3.append(r4);
        r4 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.b;
        r4 = r4.getSampleInterval();
        r3 = r3.append(r4);
        r4 = " ms";
        r3 = r3.append(r4);
        r3 = r3.toString();
        android.util.Log.d(r2, r3);
    L_0x009a:
        r2 = new com.inmobi.commons.thinICE.icedatacollector.Sample;
        r2.<init>();
        r3 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.b;
        r3 = r3.getWifiFlags();
        r4 = com.inmobi.commons.thinICE.icedatacollector.ThinICEConfigSettings.bitTest(r3, r6);
        if (r4 != 0) goto L_0x00ae;
    L_0x00ad:
        r1 = r0;
    L_0x00ae:
        r3 = com.inmobi.commons.thinICE.icedatacollector.ThinICEConfigSettings.bitTest(r3, r0);
        r4 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.b;
        r4 = r4.getCellOpFlags();
        r0 = com.inmobi.commons.thinICE.icedatacollector.ThinICEConfigSettings.bitTest(r4, r0);
        r4 = com.inmobi.commons.thinICE.icedatacollector.ThinICEConfigSettings.bitTest(r4, r6);
        r5 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.b;
        r5 = r5.isSampleCellOperatorEnabled();
        if (r5 == 0) goto L_0x0102;
    L_0x00cc:
        r5 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.a;	 Catch:{ Exception -> 0x01fa }
        r5 = com.inmobi.commons.thinICE.cellular.CellUtil.getCellNetworkInfo(r5);	 Catch:{ Exception -> 0x01fa }
        if (r0 == 0) goto L_0x00dc;
    L_0x00d6:
        r0 = -1;
        r5.simMcc = r0;	 Catch:{ Exception -> 0x01fa }
        r0 = -1;
        r5.simMnc = r0;	 Catch:{ Exception -> 0x01fa }
    L_0x00dc:
        if (r4 == 0) goto L_0x00e4;
    L_0x00de:
        r0 = -1;
        r5.currentMcc = r0;	 Catch:{ Exception -> 0x01fa }
        r0 = -1;
        r5.currentMnc = r0;	 Catch:{ Exception -> 0x01fa }
    L_0x00e4:
        r0 = com.inmobi.commons.thinICE.icedatacollector.BuildSettings.DEBUG;	 Catch:{ Exception -> 0x01fa }
        if (r0 == 0) goto L_0x0100;
    L_0x00e8:
        r0 = "IceDataCollector";
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01fa }
        r4.<init>();	 Catch:{ Exception -> 0x01fa }
        r6 = "-- cell operator: ";
        r4 = r4.append(r6);	 Catch:{ Exception -> 0x01fa }
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x01fa }
        r4 = r4.toString();	 Catch:{ Exception -> 0x01fa }
        android.util.Log.d(r0, r4);	 Catch:{ Exception -> 0x01fa }
    L_0x0100:
        r2.cellOperator = r5;	 Catch:{ Exception -> 0x01fa }
    L_0x0102:
        r0 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.b;
        r0 = r0.isSampleCellEnabled();
        if (r0 == 0) goto L_0x0140;
    L_0x010c:
        r0 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.a;	 Catch:{ Exception -> 0x0215 }
        r0 = com.inmobi.commons.thinICE.cellular.CellUtil.hasGetCurrentServingCellPermission(r0);	 Catch:{ Exception -> 0x0215 }
        if (r0 == 0) goto L_0x0208;
    L_0x0116:
        r0 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.a;	 Catch:{ Exception -> 0x0215 }
        r0 = com.inmobi.commons.thinICE.cellular.CellUtil.getCurrentCellTower(r0);	 Catch:{ Exception -> 0x0215 }
        r2.connectedCellTowerInfo = r0;	 Catch:{ Exception -> 0x0215 }
        r0 = com.inmobi.commons.thinICE.icedatacollector.BuildSettings.DEBUG;	 Catch:{ Exception -> 0x0215 }
        if (r0 == 0) goto L_0x0140;
    L_0x0124:
        r0 = "IceDataCollector";
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0215 }
        r4.<init>();	 Catch:{ Exception -> 0x0215 }
        r5 = "-- current serving cell: ";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x0215 }
        r5 = r2.connectedCellTowerInfo;	 Catch:{ Exception -> 0x0215 }
        r5 = r5.id;	 Catch:{ Exception -> 0x0215 }
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x0215 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0215 }
        android.util.Log.d(r0, r4);	 Catch:{ Exception -> 0x0215 }
    L_0x0140:
        r0 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.b;
        r0 = r0.isSampleVisibleCellTowerEnabled();
        if (r0 == 0) goto L_0x015e;
    L_0x014a:
        r0 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.a;	 Catch:{ Exception -> 0x0230 }
        r0 = com.inmobi.commons.thinICE.cellular.CellUtil.hasGetVisibleCellTowerPermission(r0);	 Catch:{ Exception -> 0x0230 }
        if (r0 == 0) goto L_0x0223;
    L_0x0154:
        r0 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.a;	 Catch:{ Exception -> 0x0230 }
        r0 = com.inmobi.commons.thinICE.cellular.CellUtil.getVisibleCellTower(r0);	 Catch:{ Exception -> 0x0230 }
        r2.visibleCellTowerInfo = r0;	 Catch:{ Exception -> 0x0230 }
    L_0x015e:
        r0 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.a;
        r0 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.getConnectedWifiInfo(r0);
        r2.connectedWifiAp = r0;
        r0 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.b;
        r0 = r0.isSampleLocationEnabled();
        if (r0 == 0) goto L_0x01c8;
    L_0x0172:
        r0 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.a;	 Catch:{ Exception -> 0x01bc }
        r0 = com.inmobi.commons.thinICE.location.LocationUtil.hasLocationPermission(r0);	 Catch:{ Exception -> 0x01bc }
        if (r0 == 0) goto L_0x0241;
    L_0x017c:
        r0 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.a;	 Catch:{ Exception -> 0x01bc }
        r4 = com.inmobi.commons.thinICE.location.LocationUtil.getLastKnownLocations(r0);	 Catch:{ Exception -> 0x01bc }
        r0 = com.inmobi.commons.thinICE.icedatacollector.BuildSettings.DEBUG;	 Catch:{ Exception -> 0x01bc }
        if (r0 == 0) goto L_0x023e;
    L_0x0188:
        r0 = "IceDataCollector";
        r5 = "-- locations:";
        android.util.Log.d(r0, r5);	 Catch:{ Exception -> 0x01bc }
        r0 = r4.values();	 Catch:{ Exception -> 0x01bc }
        r5 = r0.iterator();	 Catch:{ Exception -> 0x01bc }
    L_0x0197:
        r0 = r5.hasNext();	 Catch:{ Exception -> 0x01bc }
        if (r0 == 0) goto L_0x023e;
    L_0x019d:
        r0 = r5.next();	 Catch:{ Exception -> 0x01bc }
        r0 = (com.inmobi.commons.thinICE.location.LocationInfo) r0;	 Catch:{ Exception -> 0x01bc }
        r6 = "IceDataCollector";
        r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01bc }
        r7.<init>();	 Catch:{ Exception -> 0x01bc }
        r8 = "   + ";
        r7 = r7.append(r8);	 Catch:{ Exception -> 0x01bc }
        r0 = r7.append(r0);	 Catch:{ Exception -> 0x01bc }
        r0 = r0.toString();	 Catch:{ Exception -> 0x01bc }
        android.util.Log.d(r6, r0);	 Catch:{ Exception -> 0x01bc }
        goto L_0x0197;
    L_0x01bc:
        r0 = move-exception;
        r4 = com.inmobi.commons.thinICE.icedatacollector.BuildSettings.DEBUG;
        if (r4 == 0) goto L_0x01c8;
    L_0x01c1:
        r4 = "IceDataCollector";
        r5 = "Error getting location data";
        android.util.Log.e(r4, r5, r0);
    L_0x01c8:
        r4 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.j;
        monitor-enter(r4);
        com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.i = r2;	 Catch:{ all -> 0x01f7 }
        r0 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.h;	 Catch:{ all -> 0x01f7 }
        if (r0 == 0) goto L_0x024e;
    L_0x01d6:
        r0 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.h;	 Catch:{ all -> 0x01f7 }
        r0.add(r2);	 Catch:{ all -> 0x01f7 }
    L_0x01dd:
        r0 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.h;	 Catch:{ all -> 0x01f7 }
        r0 = r0.size();	 Catch:{ all -> 0x01f7 }
        r2 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.b;	 Catch:{ all -> 0x01f7 }
        r2 = r2.getSampleHistorySize();	 Catch:{ all -> 0x01f7 }
        if (r0 <= r2) goto L_0x024e;
    L_0x01ef:
        r0 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.h;	 Catch:{ all -> 0x01f7 }
        r0.removeFirst();	 Catch:{ all -> 0x01f7 }
        goto L_0x01dd;
    L_0x01f7:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x01f7 }
        throw r0;
    L_0x01fa:
        r0 = move-exception;
        r4 = com.inmobi.commons.thinICE.icedatacollector.BuildSettings.DEBUG;
        if (r4 == 0) goto L_0x0102;
    L_0x01ff:
        r4 = "IceDataCollector";
        r5 = "Error getting cell operator data";
        android.util.Log.e(r4, r5, r0);
        goto L_0x0102;
    L_0x0208:
        r0 = com.inmobi.commons.thinICE.icedatacollector.BuildSettings.DEBUG;	 Catch:{ Exception -> 0x0215 }
        if (r0 == 0) goto L_0x0140;
    L_0x020c:
        r0 = "IceDataCollector";
        r4 = "application does not have permission to access current serving cell";
        android.util.Log.w(r0, r4);	 Catch:{ Exception -> 0x0215 }
        goto L_0x0140;
    L_0x0215:
        r0 = move-exception;
        r4 = com.inmobi.commons.thinICE.icedatacollector.BuildSettings.DEBUG;
        if (r4 == 0) goto L_0x0140;
    L_0x021a:
        r4 = "IceDataCollector";
        r5 = "Error getting cell data";
        android.util.Log.e(r4, r5, r0);
        goto L_0x0140;
    L_0x0223:
        r0 = com.inmobi.commons.thinICE.icedatacollector.BuildSettings.DEBUG;	 Catch:{ Exception -> 0x0230 }
        if (r0 == 0) goto L_0x015e;
    L_0x0227:
        r0 = "IceDataCollector";
        r4 = "application does not have permission to access current serving cell";
        android.util.Log.w(r0, r4);	 Catch:{ Exception -> 0x0230 }
        goto L_0x015e;
    L_0x0230:
        r0 = move-exception;
        r4 = com.inmobi.commons.thinICE.icedatacollector.BuildSettings.DEBUG;
        if (r4 == 0) goto L_0x015e;
    L_0x0235:
        r4 = "IceDataCollector";
        r5 = "Error getting cell data";
        android.util.Log.e(r4, r5, r0);
        goto L_0x015e;
    L_0x023e:
        r2.lastKnownLocations = r4;	 Catch:{ Exception -> 0x01bc }
        goto L_0x01c8;
    L_0x0241:
        r0 = com.inmobi.commons.thinICE.icedatacollector.BuildSettings.DEBUG;	 Catch:{ Exception -> 0x01bc }
        if (r0 == 0) goto L_0x01c8;
    L_0x0245:
        r0 = "IceDataCollector";
        r4 = "application does not have permission to access location";
        android.util.Log.w(r0, r4);	 Catch:{ Exception -> 0x01bc }
        goto L_0x01c8;
    L_0x024e:
        monitor-exit(r4);	 Catch:{ all -> 0x01f7 }
        r0 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.b;
        r0 = r0.isSampleVisibleWifiEnabled();
        if (r0 == 0) goto L_0x0296;
    L_0x0259:
        r0 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.a;	 Catch:{ Exception -> 0x0278 }
        r2 = 0;
        r0 = com.inmobi.commons.thinICE.wifi.WifiUtil.hasWifiScanPermission(r0, r2);	 Catch:{ Exception -> 0x0278 }
        if (r0 == 0) goto L_0x0286;
    L_0x0264:
        r0 = new com.inmobi.commons.thinICE.icedatacollector.a$a;	 Catch:{ Exception -> 0x0278 }
        r0.<init>(r1, r3);	 Catch:{ Exception -> 0x0278 }
        r1 = com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.a;	 Catch:{ Exception -> 0x0278 }
        r0 = com.inmobi.commons.thinICE.wifi.WifiScanner.requestScan(r1, r0);	 Catch:{ Exception -> 0x0278 }
        if (r0 != 0) goto L_0x0020;
    L_0x0273:
        r9.a();	 Catch:{ Exception -> 0x0278 }
        goto L_0x0020;
    L_0x0278:
        r0 = move-exception;
        r1 = com.inmobi.commons.thinICE.icedatacollector.BuildSettings.DEBUG;
        if (r1 == 0) goto L_0x0020;
    L_0x027d:
        r1 = "IceDataCollector";
        r2 = "Error scanning for wifi";
        android.util.Log.e(r1, r2, r0);
        goto L_0x0020;
    L_0x0286:
        r9.a();	 Catch:{ Exception -> 0x0278 }
        r0 = com.inmobi.commons.thinICE.icedatacollector.BuildSettings.DEBUG;	 Catch:{ Exception -> 0x0278 }
        if (r0 == 0) goto L_0x0020;
    L_0x028d:
        r0 = "IceDataCollector";
        r1 = "application does not have permission to scan for wifi access points";
        android.util.Log.w(r0, r1);	 Catch:{ Exception -> 0x0278 }
        goto L_0x0020;
    L_0x0296:
        r9.a();
        goto L_0x0020;
        */
    }
}