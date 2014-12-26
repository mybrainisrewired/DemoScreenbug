package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;

class de {
    private GoogleAnalytics aaB;
    private Context mContext;
    private Tracker sB;

    static class a implements Logger {
        a() {
        }

        private static int ci(int i) {
            switch (i) {
                case MMAdView.TRANSITION_UP:
                    return 0;
                case MMAdView.TRANSITION_DOWN:
                case MMAdView.TRANSITION_RANDOM:
                    return 1;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    return MMAdView.TRANSITION_UP;
                default:
                    return MMAdView.TRANSITION_DOWN;
            }
        }

        public void error(Exception exception) {
            bh.b(Preconditions.EMPTY_ARGUMENTS, exception);
        }

        public void error(String message) {
            bh.w(message);
        }

        public int getLogLevel() {
            return ci(bh.getLogLevel());
        }

        public void info(String message) {
            bh.x(message);
        }

        public void setLogLevel(int logLevel) {
            bh.z("GA uses GTM logger. Please use TagManager.setLogLevel(int) instead.");
        }

        public void verbose(String message) {
            bh.y(message);
        }

        public void warn(String message) {
            bh.z(message);
        }
    }

    de(Context context) {
        this.mContext = context;
    }

    private synchronized void bV(String str) {
        if (this.aaB == null) {
            this.aaB = GoogleAnalytics.getInstance(this.mContext);
            this.aaB.setLogger(new a());
            this.sB = this.aaB.newTracker(str);
        }
    }

    public Tracker bU(String str) {
        bV(str);
        return this.sB;
    }
}