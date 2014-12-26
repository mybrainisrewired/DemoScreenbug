package com.nostra13.universalimageloader.utils;

import android.util.Log;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.nostra13.universalimageloader.core.ImageLoader;

public final class L {
    private static volatile boolean DISABLED = false;
    private static final String LOG_FORMAT = "%1$s\n%2$s";

    static {
        DISABLED = false;
    }

    private L() {
    }

    public static void d(String message, Object... args) {
        log(MMAdView.TRANSITION_DOWN, null, message, args);
    }

    public static void disableLogging() {
        DISABLED = true;
    }

    public static void e(String message, Object... args) {
        log(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, null, message, args);
    }

    public static void e(Throwable ex) {
        log(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, ex, null, new Object[0]);
    }

    public static void e(Throwable ex, String message, Object... args) {
        log(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, ex, message, args);
    }

    public static void enableLogging() {
        DISABLED = false;
    }

    public static void i(String message, Object... args) {
        log(MMAdView.TRANSITION_RANDOM, null, message, args);
    }

    private static void log(int priority, Throwable ex, String message, Object... args) {
        if (!DISABLED) {
            String log;
            if (args.length > 0) {
                message = String.format(message, args);
            }
            if (ex == null) {
                log = message;
            } else {
                String logMessage;
                if (message == null) {
                    logMessage = ex.getMessage();
                } else {
                    logMessage = message;
                }
                String logBody = Log.getStackTraceString(ex);
                log = String.format(LOG_FORMAT, new Object[]{logMessage, logBody});
            }
            Log.println(priority, ImageLoader.TAG, log);
        }
    }

    public static void w(String message, Object... args) {
        log(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, null, message, args);
    }
}