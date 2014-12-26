package com.facebook.ads.internal;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.StatFs;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AdAnalogData {
    private static Sensor accelerometer;
    private static volatile float[] accelerometerValues;
    private static Map<String, Object> analogInfo;
    private static Sensor gyroscope;
    private static volatile float[] gyroscopeValues;
    private static String[] sensorDimensions;
    private static SensorManager sensorManager;

    private static class AdSensorEventListener implements SensorEventListener {
        private AdSensorEventListener() {
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            if (event.sensor == accelerometer) {
                accelerometerValues = event.values;
            } else if (event.sensor == gyroscope) {
                gyroscopeValues = event.values;
            }
            AdAnalogData.stopUpdate(this);
        }
    }

    static {
        sensorManager = null;
        accelerometer = null;
        gyroscope = null;
        analogInfo = new ConcurrentHashMap();
        sensorDimensions = new String[]{"x", "y", "z"};
    }

    public static Map<String, Object> getAnalogInfo() {
        Map<String, Object> currentAnalogInfo = new HashMap();
        currentAnalogInfo.putAll(analogInfo);
        putSensorData(currentAnalogInfo);
        return currentAnalogInfo;
    }

    private static void putBatteryData(Context context) {
        int i = 1;
        Intent batteryIntent = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (batteryIntent != null) {
            boolean isCharging;
            int level = batteryIntent.getIntExtra("level", -1);
            int scale = batteryIntent.getIntExtra("scale", -1);
            int status = batteryIntent.getIntExtra("status", -1);
            if (status == 2 || status == 5) {
                isCharging = true;
            } else {
                isCharging = false;
            }
            float batteryLevel = BitmapDescriptorFactory.HUE_RED;
            if (scale > 0) {
                batteryLevel = (((float) level) / ((float) scale)) * 100.0f;
            }
            analogInfo.put("battery", Float.valueOf(batteryLevel));
            Map map = analogInfo;
            String str = "charging";
            if (!isCharging) {
                i = 0;
            }
            map.put(str, Integer.valueOf(i));
        }
    }

    private static void putDiskInfo(Context context) {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        long availableBlocks = (long) stat.getAvailableBlocks();
        analogInfo.put("free_space", Long.valueOf(availableBlocks * ((long) stat.getBlockSize())));
    }

    private static void putMemoryInfo(Context context) {
        MemoryInfo mi = new MemoryInfo();
        ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(mi);
        analogInfo.put("available_memory", String.valueOf(mi.availMem));
    }

    private static void putSensorData(Map<String, Object> analogInfo) {
        int length;
        int i;
        float[] currentAccelerometerValues = accelerometerValues;
        float[] currentGyroscopeValues = gyroscopeValues;
        if (currentAccelerometerValues != null) {
            length = Math.min(sensorDimensions.length, currentAccelerometerValues.length);
            i = 0;
            while (i < length) {
                analogInfo.put("accelerometer_" + sensorDimensions[i], Float.valueOf(currentAccelerometerValues[i]));
                i++;
            }
        }
        if (currentGyroscopeValues != null) {
            length = Math.min(sensorDimensions.length, currentGyroscopeValues.length);
            i = 0;
            while (i < length) {
                analogInfo.put("rotation_" + sensorDimensions[i], Float.valueOf(currentGyroscopeValues[i]));
                i++;
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void startUpdate(android.content.Context r5_context) {
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.ads.internal.AdAnalogData.startUpdate(android.content.Context):void");
        /*
        r1 = com.facebook.ads.internal.AdAnalogData.class;
        monitor-enter(r1);
        putMemoryInfo(r5);	 Catch:{ all -> 0x005f }
        putDiskInfo(r5);	 Catch:{ all -> 0x005f }
        putBatteryData(r5);	 Catch:{ all -> 0x005f }
        r0 = sensorManager;	 Catch:{ all -> 0x005f }
        if (r0 != 0) goto L_0x0020;
    L_0x0010:
        r0 = "sensor";
        r0 = r5.getSystemService(r0);	 Catch:{ all -> 0x005f }
        r0 = (android.hardware.SensorManager) r0;	 Catch:{ all -> 0x005f }
        sensorManager = r0;	 Catch:{ all -> 0x005f }
        r0 = sensorManager;	 Catch:{ all -> 0x005f }
        if (r0 != 0) goto L_0x0020;
    L_0x001e:
        monitor-exit(r1);
        return;
    L_0x0020:
        r0 = accelerometer;	 Catch:{ all -> 0x005f }
        if (r0 != 0) goto L_0x002d;
    L_0x0024:
        r0 = sensorManager;	 Catch:{ all -> 0x005f }
        r2 = 1;
        r0 = r0.getDefaultSensor(r2);	 Catch:{ all -> 0x005f }
        accelerometer = r0;	 Catch:{ all -> 0x005f }
    L_0x002d:
        r0 = gyroscope;	 Catch:{ all -> 0x005f }
        if (r0 != 0) goto L_0x003a;
    L_0x0031:
        r0 = sensorManager;	 Catch:{ all -> 0x005f }
        r2 = 4;
        r0 = r0.getDefaultSensor(r2);	 Catch:{ all -> 0x005f }
        gyroscope = r0;	 Catch:{ all -> 0x005f }
    L_0x003a:
        r0 = accelerometer;	 Catch:{ all -> 0x005f }
        if (r0 == 0) goto L_0x004c;
    L_0x003e:
        r0 = sensorManager;	 Catch:{ all -> 0x005f }
        r2 = new com.facebook.ads.internal.AdAnalogData$AdSensorEventListener;	 Catch:{ all -> 0x005f }
        r3 = 0;
        r2.<init>();	 Catch:{ all -> 0x005f }
        r3 = accelerometer;	 Catch:{ all -> 0x005f }
        r4 = 3;
        r0.registerListener(r2, r3, r4);	 Catch:{ all -> 0x005f }
    L_0x004c:
        r0 = gyroscope;	 Catch:{ all -> 0x005f }
        if (r0 == 0) goto L_0x001e;
    L_0x0050:
        r0 = sensorManager;	 Catch:{ all -> 0x005f }
        r2 = new com.facebook.ads.internal.AdAnalogData$AdSensorEventListener;	 Catch:{ all -> 0x005f }
        r3 = 0;
        r2.<init>();	 Catch:{ all -> 0x005f }
        r3 = gyroscope;	 Catch:{ all -> 0x005f }
        r4 = 3;
        r0.registerListener(r2, r3, r4);	 Catch:{ all -> 0x005f }
        goto L_0x001e;
    L_0x005f:
        r0 = move-exception;
        monitor-exit(r1);
        throw r0;
        */
    }

    public static synchronized void stopUpdate(AdSensorEventListener sensorEventListener) {
        synchronized (AdAnalogData.class) {
            if (sensorManager != null) {
                sensorManager.unregisterListener(sensorEventListener);
            }
        }
    }
}