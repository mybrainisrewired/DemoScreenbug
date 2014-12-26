package com.android.volley;

import android.os.SystemClock;
import android.util.Log;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class VolleyLog {
    public static final boolean DEBUG;
    public static String TAG;

    static class MarkerLog {
        public static final boolean ENABLED;
        private static final long MIN_DURATION_FOR_LOGGING_MS = 0;
        private boolean mFinished;
        private final List<Marker> mMarkers;

        private static class Marker {
            public final String name;
            public final long thread;
            public final long time;

            public Marker(String name, long thread, long time) {
                this.name = name;
                this.thread = thread;
                this.time = time;
            }
        }

        static {
            ENABLED = DEBUG;
        }

        MarkerLog() {
            this.mMarkers = new ArrayList();
            this.mFinished = false;
        }

        private long getTotalDuration() {
            if (this.mMarkers.size() == 0) {
                return 0;
            }
            return ((Marker) this.mMarkers.get(this.mMarkers.size() - 1)).time - ((Marker) this.mMarkers.get(0)).time;
        }

        public synchronized void add(String name, long threadId) {
            if (this.mFinished) {
                throw new IllegalStateException("Marker added to finished log");
            }
            this.mMarkers.add(new Marker(name, threadId, SystemClock.elapsedRealtime()));
        }

        protected void finalize() throws Throwable {
            if (!this.mFinished) {
                finish("Request on the loose");
                VolleyLog.e("Marker log finalized without finish() - uncaught exit point for request", new Object[0]);
            }
        }

        public synchronized void finish(String header) {
            this.mFinished = true;
            if (getTotalDuration() > 0) {
                long prevTime = ((Marker) this.mMarkers.get(0)).time;
                VolleyLog.d("(%-4d ms) %s", new Object[]{Long.valueOf(duration), header});
                Iterator it = this.mMarkers.iterator();
                while (it.hasNext()) {
                    VolleyLog.d("(+%-4d) [%2d] %s", new Object[]{Long.valueOf(((Marker) it.next()).time - prevTime), Long.valueOf(marker.thread), marker.name});
                    prevTime = thisTime;
                }
            }
        }
    }

    static {
        TAG = "Volley";
        DEBUG = Log.isLoggable(TAG, MMAdView.TRANSITION_UP);
    }

    private static String buildMessage(String format, Object... args) {
        String msg = args == null ? format : String.format(Locale.US, format, args);
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
        String caller = "<unknown>";
        int i = MMAdView.TRANSITION_UP;
        while (i < trace.length) {
            if (!trace[i].getClass().equals(VolleyLog.class)) {
                String callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass.lastIndexOf(ApiEventType.API_MRAID_SEEK_VIDEO) + 1);
                caller = new StringBuilder(String.valueOf(callingClass.substring(callingClass.lastIndexOf(ApiEventType.API_MRAID_SET_AUDIO_VOLUME) + 1))).append(".").append(trace[i].getMethodName()).toString();
                break;
            } else {
                i++;
            }
        }
        return String.format(Locale.US, "[%d] %s: %s", new Object[]{Long.valueOf(Thread.currentThread().getId()), caller, msg});
    }

    public static void d(String format, Object... args) {
        Log.d(TAG, buildMessage(format, args));
    }

    public static void e(String format, Object... args) {
        Log.e(TAG, buildMessage(format, args));
    }

    public static void e(Throwable tr, String format, Object... args) {
        Log.e(TAG, buildMessage(format, args), tr);
    }

    public static void v(String format, Object... args) {
        if (DEBUG) {
            Log.v(TAG, buildMessage(format, args));
        }
    }

    public static void wtf(String format, Object... args) {
        Log.wtf(TAG, buildMessage(format, args));
    }

    public static void wtf(Throwable tr, String format, Object... args) {
        Log.wtf(TAG, buildMessage(format, args), tr);
    }
}