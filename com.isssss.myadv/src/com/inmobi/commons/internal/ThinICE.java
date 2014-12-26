package com.inmobi.commons.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import com.inmobi.commons.analytics.bootstrapper.AnalyticsInitializer;
import com.inmobi.commons.analytics.bootstrapper.ThinICEConfig;
import com.inmobi.commons.cache.RetryMechanism;
import com.inmobi.commons.cache.RetryMechanism.RetryRunnable;
import com.inmobi.commons.internal.ApplicationFocusManager.FocusChangedListener;
import com.inmobi.commons.thinICE.icedatacollector.IceDataCollector;
import com.inmobi.commons.thinICE.icedatacollector.IceDataCollector.ThinIceDataCollectedListener;
import com.inmobi.commons.thinICE.icedatacollector.Sample;
import com.inmobi.commons.thinICE.icedatacollector.ThinICEConfigSettings;
import com.inmobi.commons.thinICE.icedatacollector.ThinICEListener;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Timer;

public final class ThinICE {
    private static boolean a;
    private static boolean b;
    private static ThinICEListener c;
    private static Timer d;

    static class a implements RetryRunnable {
        final /* synthetic */ String a;

        a(String str) {
            this.a = str;
        }

        public void completed() {
        }

        public void run() throws Exception {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "Sending ThinICE data to server " + AnalyticsInitializer.getConfigParams().getThinIceConfig().getEndpointUrl());
            if (InternalSDKUtil.checkNetworkAvailibility(InternalSDKUtil.getContext())) {
                ThinICE.b(this.a);
            } else {
                throw new Exception("Device not connected.");
            }
        }
    }

    static class b implements FocusChangedListener {
        b() {
        }

        public void onFocusChanged(boolean z) {
            if (z) {
                Log.internal(InternalSDKUtil.LOGGING_TAG, "App comes in foreground");
                b = true;
                IceDataCollector.start(InternalSDKUtil.getContext());
            } else {
                Log.internal(InternalSDKUtil.LOGGING_TAG, "App goes in background");
                b = false;
                ThinICE.c();
            }
        }
    }

    static class c implements ThinIceDataCollectedListener {
        c() {
        }

        public void onDataCollected() {
            if (b) {
                b = false;
                ThinICE.c();
                IceDataCollector.start(InternalSDKUtil.getContext());
            }
        }
    }

    static {
        a = false;
        b = false;
        c = new b();
        d = new Timer();
    }

    private ThinICE() {
    }

    @SuppressLint({"NewApi"})
    private static void a(Context context) throws CommonsException {
        if (a || context != null) {
            if (!a) {
                if (VERSION.SDK_INT >= 14) {
                    ApplicationFocusManager.init(context);
                    ApplicationFocusManager.addFocusChangedListener(new b());
                    IceDataCollector.start(context.getApplicationContext());
                } else {
                    IceDataCollector.setListener(c);
                }
                a = true;
            }
            InternalSDKUtil.initialize(context.getApplicationContext());
            IceDataCollector.setIceDataCollectionListener(new c());
        } else {
            throw new CommonsException(1);
        }
    }

    private static void b(String str) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(AnalyticsInitializer.getConfigParams().getThinIceConfig().getEndpointUrl()).openConnection();
        httpURLConnection.setRequestProperty("User-Agent", InternalSDKUtil.getUserAgent());
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(false);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
        outputStreamWriter.write(str);
        outputStreamWriter.flush();
        outputStreamWriter.close();
        httpURLConnection.getResponseCode();
    }

    private static void b(List<Sample> list) {
        if (list.size() == 0 && ActivityRecognitionSampler.getCollectedList().size() == 0) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "No ThinICE data is collected. NoOp.");
        } else if (AnalyticsInitializer.getConfigParams().getThinIceConfig().isEnabled()) {
            RetryMechanism retryMechanism = new RetryMechanism((int) AnalyticsInitializer.getConfigParams().getThinIceConfig().getMaxRetry(), ((int) AnalyticsInitializer.getConfigParams().getThinIceConfig().getRetryInterval()) * 1000, d);
            String toPayloadString = new JSONPayloadCreator().toPayloadString(list, ActivityRecognitionSampler.getCollectedList(), InternalSDKUtil.getContext());
            Log.internal(InternalSDKUtil.LOGGING_TAG, "Sending " + list.size() + " ThinICE params to server " + toPayloadString);
            retryMechanism.rescheduleTimer(new a(toPayloadString));
        } else {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "ThisICE disabled. Not sending data. NoOp.");
        }
    }

    private static void c() {
        List data = IceDataCollector.getData();
        IceDataCollector.stop();
        b(data);
        ActivityRecognitionSampler.stop();
    }

    public static void setConfig(ThinICEConfig thinICEConfig) {
        if (thinICEConfig != null) {
            ThinICEConfigSettings thinICEConfigSettings = new ThinICEConfigSettings();
            thinICEConfigSettings.setEnabled(thinICEConfig.isEnabled());
            thinICEConfigSettings.setSampleCellEnabled(thinICEConfig.isCellEnabled());
            thinICEConfigSettings.setSampleCellOperatorEnabled(thinICEConfig.isOperatorEnabled());
            thinICEConfigSettings.setSampleConnectedWifiEnabled(thinICEConfig.isConnectedWifiEnabled());
            thinICEConfigSettings.setSampleHistorySize(thinICEConfig.getSampleHistorySize());
            thinICEConfigSettings.setSampleInterval(thinICEConfig.getSampleInterval() * 1000);
            thinICEConfigSettings.setSampleLocationEnabled(true);
            thinICEConfigSettings.setSampleVisibleWifiEnabled(thinICEConfig.isVisibleWifiEnabled());
            thinICEConfigSettings.setStopRequestTimeout(thinICEConfig.getStopRequestTimeout() * 1000);
            thinICEConfigSettings.setWifiFlags(thinICEConfig.getWifiFlags());
            thinICEConfigSettings.setCellOpFlags(thinICEConfig.getCellOpsFlag());
            IceDataCollector.setConfig(thinICEConfigSettings);
        }
    }

    public static void start(Context context) throws CommonsException {
        if (InternalSDKUtil.isInitializedSuccessfully(false)) {
            a(context);
            if (VERSION.SDK_INT < 14) {
                IceDataCollector.start(context);
            }
            ActivityRecognitionSampler.start();
        }
    }

    public static void stop(Context context) throws CommonsException {
        a(context);
        if (VERSION.SDK_INT < 14) {
            c();
        }
    }
}