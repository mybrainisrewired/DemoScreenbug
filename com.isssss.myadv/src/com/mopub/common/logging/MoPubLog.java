package com.mopub.common.logging;

import android.util.Log;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class MoPubLog {
    private static final Logger LOGGER;
    private static final String LOGTAG = "MoPub";
    private static final MoPubLogHandler LOG_HANDLER;

    private static final class MoPubLogHandler extends Handler {
        private static final Map<Level, Integer> LEVEL_TO_LOG;

        static {
            LEVEL_TO_LOG = new HashMap(7);
            LEVEL_TO_LOG.put(Level.FINEST, Integer.valueOf(MMAdView.TRANSITION_UP));
            LEVEL_TO_LOG.put(Level.FINER, Integer.valueOf(MMAdView.TRANSITION_UP));
            LEVEL_TO_LOG.put(Level.FINE, Integer.valueOf(MMAdView.TRANSITION_UP));
            LEVEL_TO_LOG.put(Level.CONFIG, Integer.valueOf(MMAdView.TRANSITION_DOWN));
            LEVEL_TO_LOG.put(Level.INFO, Integer.valueOf(MMAdView.TRANSITION_RANDOM));
            LEVEL_TO_LOG.put(Level.WARNING, Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES));
            LEVEL_TO_LOG.put(Level.SEVERE, Integer.valueOf(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES));
        }

        private MoPubLogHandler() {
        }

        public void close() {
        }

        public void flush() {
        }

        public void publish(LogRecord logRecord) {
            if (isLoggable(logRecord)) {
                int priority = LEVEL_TO_LOG.containsKey(logRecord.getLevel()) ? ((Integer) LEVEL_TO_LOG.get(logRecord.getLevel())).intValue() : MMAdView.TRANSITION_UP;
                String message = new StringBuilder(String.valueOf(logRecord.getMessage())).append("\n").toString();
                Throwable error = logRecord.getThrown();
                if (error != null) {
                    message = new StringBuilder(String.valueOf(message)).append(Log.getStackTraceString(error)).toString();
                }
                Log.println(priority, LOGTAG, message);
            }
        }
    }

    static {
        LOGGER = Logger.getLogger("com.mopub");
        LOG_HANDLER = new MoPubLogHandler();
        LogManager.getLogManager().addLogger(LOGGER);
        LOGGER.addHandler(LOG_HANDLER);
        LOGGER.setLevel(Level.FINE);
    }

    private MoPubLog() {
    }

    public static void c(String message) {
        c(message, null);
    }

    public static void c(String message, Throwable throwable) {
        LOGGER.log(Level.FINEST, message, throwable);
    }

    public static void d(String message) {
        d(message, null);
    }

    public static void d(String message, Throwable throwable) {
        LOGGER.log(Level.CONFIG, message, throwable);
    }

    public static void e(String message) {
        e(message, null);
    }

    public static void e(String message, Throwable throwable) {
        LOGGER.log(Level.SEVERE, message, throwable);
    }

    public static void i(String message) {
        i(message, null);
    }

    public static void i(String message, Throwable throwable) {
        LOGGER.log(Level.INFO, message, throwable);
    }

    public static void v(String message) {
        v(message, null);
    }

    public static void v(String message, Throwable throwable) {
        LOGGER.log(Level.FINE, message, throwable);
    }

    public static void w(String message) {
        w(message, null);
    }

    public static void w(String message, Throwable throwable) {
        LOGGER.log(Level.WARNING, message, throwable);
    }
}