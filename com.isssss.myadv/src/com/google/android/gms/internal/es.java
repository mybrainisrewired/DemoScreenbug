package com.google.android.gms.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaStatus;
import com.inmobi.commons.analytics.db.AnalyticsSQLiteHelper;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class es extends em {
    private static final String NAMESPACE;
    private static final long zG;
    private static final long zH;
    private static final long zI;
    private static final long zJ;
    private final Handler mHandler;
    private long zK;
    private MediaStatus zL;
    private final ev zM;
    private final ev zN;
    private final ev zO;
    private final ev zP;
    private final ev zQ;
    private final ev zR;
    private final ev zS;
    private final ev zT;
    private final Runnable zU;
    private boolean zV;

    private class a implements Runnable {
        private a() {
        }

        public void run() {
            boolean z = false;
            es.this.zV = false;
            long elapsedRealtime = SystemClock.elapsedRealtime();
            es.this.zM.d(elapsedRealtime, MMAdView.TRANSITION_DOWN);
            es.this.zN.d(elapsedRealtime, MMAdView.TRANSITION_DOWN);
            es.this.zO.d(elapsedRealtime, MMAdView.TRANSITION_DOWN);
            es.this.zP.d(elapsedRealtime, MMAdView.TRANSITION_DOWN);
            es.this.zQ.d(elapsedRealtime, MMAdView.TRANSITION_DOWN);
            es.this.zR.d(elapsedRealtime, MMAdView.TRANSITION_DOWN);
            es.this.zS.d(elapsedRealtime, MMAdView.TRANSITION_DOWN);
            es.this.zT.d(elapsedRealtime, MMAdView.TRANSITION_DOWN);
            synchronized (ev.Ab) {
                if (es.this.zM.dU() || es.this.zQ.dU() || es.this.zR.dU() || es.this.zS.dU() || es.this.zT.dU()) {
                    z = true;
                }
            }
            es.this.w(z);
        }
    }

    static {
        NAMESPACE = eo.X("com.google.cast.media");
        zG = TimeUnit.HOURS.toMillis(24);
        zH = TimeUnit.HOURS.toMillis(24);
        zI = TimeUnit.HOURS.toMillis(24);
        zJ = TimeUnit.SECONDS.toMillis(1);
    }

    public es() {
        this(null);
    }

    public es(String str) {
        super(NAMESPACE, "MediaControlChannel", str);
        this.mHandler = new Handler(Looper.getMainLooper());
        this.zU = new a(null);
        this.zM = new ev(zH);
        this.zN = new ev(zG);
        this.zO = new ev(zG);
        this.zP = new ev(zG);
        this.zQ = new ev(zI);
        this.zR = new ev(zG);
        this.zS = new ev(zG);
        this.zT = new ev(zG);
        dS();
    }

    private void a(long j, JSONObject jSONObject) throws JSONException {
        boolean z = 1;
        boolean n = this.zM.n(j);
        int i = this.zQ.dU() && !this.zQ.n(j);
        if (!this.zR.dU() || this.zR.n(j)) {
            if (!this.zS.dU() || this.zS.n(j)) {
                z = false;
            }
        }
        i = i != 0 ? MMAdView.TRANSITION_UP : 0;
        if (i != 0) {
            i |= 1;
        }
        if (n || this.zL == null) {
            this.zL = new MediaStatus(jSONObject);
            this.zK = SystemClock.elapsedRealtime();
            i = ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES;
        } else {
            i = this.zL.a(jSONObject, i);
        }
        if ((i & 1) != 0) {
            this.zK = SystemClock.elapsedRealtime();
            onStatusUpdated();
        }
        if ((i & 2) != 0) {
            this.zK = SystemClock.elapsedRealtime();
            onStatusUpdated();
        }
        if ((i & 4) != 0) {
            onMetadataUpdated();
        }
        this.zM.c(j, 0);
        this.zN.c(j, 0);
        this.zO.c(j, 0);
        this.zP.c(j, 0);
        this.zQ.c(j, 0);
        this.zR.c(j, 0);
        this.zS.c(j, 0);
        this.zT.c(j, 0);
    }

    private void dS() {
        w(false);
        this.zK = 0;
        this.zL = null;
        this.zM.clear();
        this.zQ.clear();
        this.zR.clear();
    }

    private void w(boolean z) {
        if (this.zV != z) {
            this.zV = z;
            if (z) {
                this.mHandler.postDelayed(this.zU, zJ);
            } else {
                this.mHandler.removeCallbacks(this.zU);
            }
        }
    }

    public final void U(String str) {
        int i = MMAdView.TRANSITION_UP;
        int i2 = 0;
        int i3 = 1;
        er erVar = this.yY;
        String str2 = "message received: %s";
        Object[] objArr = new Object[i3];
        objArr[i2] = str;
        erVar.b(str2, objArr);
        try {
            JSONObject jSONObject = new JSONObject(str);
            str2 = jSONObject.getString(AnalyticsSQLiteHelper.EVENT_LIST_TYPE);
            long optLong = jSONObject.optLong("requestId", -1);
            if (str2.equals("MEDIA_STATUS")) {
                JSONArray jSONArray = jSONObject.getJSONArray("status");
                if (jSONArray.length() > 0) {
                    a(optLong, jSONArray.getJSONObject(0));
                } else {
                    this.zL = null;
                    onStatusUpdated();
                    onMetadataUpdated();
                    this.zT.c(optLong, 0);
                }
            } else if (str2.equals("INVALID_PLAYER_STATE")) {
                this.yY.d("received unexpected error: Invalid Player State.", new Object[0]);
                jSONObject = jSONObject.optJSONObject("customData");
                this.zM.b(optLong, 1, jSONObject);
                this.zN.b(optLong, 1, jSONObject);
                this.zO.b(optLong, 1, jSONObject);
                this.zP.b(optLong, 1, jSONObject);
                this.zQ.b(optLong, 1, jSONObject);
                this.zR.b(optLong, 1, jSONObject);
                this.zS.b(optLong, 1, jSONObject);
                this.zT.b(optLong, 1, jSONObject);
            } else if (str2.equals("LOAD_FAILED")) {
                this.zM.b(optLong, 1, jSONObject.optJSONObject("customData"));
            } else if (str2.equals("LOAD_CANCELLED")) {
                this.zM.b(optLong, MMAdView.TRANSITION_UP, jSONObject.optJSONObject("customData"));
            } else if (str2.equals("INVALID_REQUEST")) {
                this.yY.d("received unexpected error: Invalid Request.", new Object[0]);
                jSONObject = jSONObject.optJSONObject("customData");
                this.zM.b(optLong, 1, jSONObject);
                this.zN.b(optLong, 1, jSONObject);
                this.zO.b(optLong, 1, jSONObject);
                this.zP.b(optLong, 1, jSONObject);
                this.zQ.b(optLong, 1, jSONObject);
                this.zR.b(optLong, 1, jSONObject);
                this.zS.b(optLong, 1, jSONObject);
                this.zT.b(optLong, 1, jSONObject);
            }
        } catch (JSONException e) {
            JSONException jSONException = e;
            er erVar2 = this.yY;
            String str3 = "Message is malformed (%s); ignoring: %s";
            Object[] objArr2 = new Object[i];
            objArr2[i2] = jSONException.getMessage();
            objArr2[i3] = str;
            erVar2.d(str3, objArr2);
        }
    }

    public long a(eu euVar) throws IOException {
        JSONObject jSONObject = new JSONObject();
        long dE = dE();
        this.zT.a(dE, euVar);
        w(true);
        try {
            jSONObject.put("requestId", dE);
            jSONObject.put(AnalyticsSQLiteHelper.EVENT_LIST_TYPE, "GET_STATUS");
            if (this.zL != null) {
                jSONObject.put("mediaSessionId", this.zL.dC());
            }
        } catch (JSONException e) {
        }
        a(jSONObject.toString(), dE, null);
        return dE;
    }

    public long a(eu euVar, double d, JSONObject jSONObject) throws IOException, IllegalStateException, IllegalArgumentException {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            throw new IllegalArgumentException("Volume cannot be " + d);
        }
        JSONObject jSONObject2 = new JSONObject();
        long dE = dE();
        this.zR.a(dE, euVar);
        w(true);
        try {
            jSONObject2.put("requestId", dE);
            jSONObject2.put(AnalyticsSQLiteHelper.EVENT_LIST_TYPE, "SET_VOLUME");
            jSONObject2.put("mediaSessionId", dC());
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.put("level", d);
            jSONObject2.put("volume", jSONObject3);
            if (jSONObject != null) {
                jSONObject2.put("customData", jSONObject);
            }
        } catch (JSONException e) {
        }
        a(jSONObject2.toString(), dE, null);
        return dE;
    }

    public long a(eu euVar, long j, int i, JSONObject jSONObject) throws IOException, IllegalStateException {
        JSONObject jSONObject2 = new JSONObject();
        long dE = dE();
        this.zQ.a(dE, euVar);
        w(true);
        try {
            jSONObject2.put("requestId", dE);
            jSONObject2.put(AnalyticsSQLiteHelper.EVENT_LIST_TYPE, "SEEK");
            jSONObject2.put("mediaSessionId", dC());
            jSONObject2.put("currentTime", eo.m(j));
            if (i == 1) {
                jSONObject2.put("resumeState", "PLAYBACK_START");
            } else if (i == 2) {
                jSONObject2.put("resumeState", "PLAYBACK_PAUSE");
            }
            if (jSONObject != null) {
                jSONObject2.put("customData", jSONObject);
            }
        } catch (JSONException e) {
        }
        a(jSONObject2.toString(), dE, null);
        return dE;
    }

    public long a(eu euVar, MediaInfo mediaInfo, boolean z, long j, JSONObject jSONObject) throws IOException {
        JSONObject jSONObject2 = new JSONObject();
        long dE = dE();
        this.zM.a(dE, euVar);
        w(true);
        try {
            jSONObject2.put("requestId", dE);
            jSONObject2.put(AnalyticsSQLiteHelper.EVENT_LIST_TYPE, "LOAD");
            jSONObject2.put("media", mediaInfo.dB());
            jSONObject2.put("autoplay", z);
            jSONObject2.put("currentTime", eo.m(j));
            if (jSONObject != null) {
                jSONObject2.put("customData", jSONObject);
            }
        } catch (JSONException e) {
        }
        a(jSONObject2.toString(), dE, null);
        return dE;
    }

    public long a(eu euVar, JSONObject jSONObject) throws IOException {
        JSONObject jSONObject2 = new JSONObject();
        long dE = dE();
        this.zN.a(dE, euVar);
        w(true);
        try {
            jSONObject2.put("requestId", dE);
            jSONObject2.put(AnalyticsSQLiteHelper.EVENT_LIST_TYPE, "PAUSE");
            jSONObject2.put("mediaSessionId", dC());
            if (jSONObject != null) {
                jSONObject2.put("customData", jSONObject);
            }
        } catch (JSONException e) {
        }
        a(jSONObject2.toString(), dE, null);
        return dE;
    }

    public long a(eu euVar, boolean z, JSONObject jSONObject) throws IOException, IllegalStateException {
        JSONObject jSONObject2 = new JSONObject();
        long dE = dE();
        this.zS.a(dE, euVar);
        w(true);
        try {
            jSONObject2.put("requestId", dE);
            jSONObject2.put(AnalyticsSQLiteHelper.EVENT_LIST_TYPE, "SET_VOLUME");
            jSONObject2.put("mediaSessionId", dC());
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.put("muted", z);
            jSONObject2.put("volume", jSONObject3);
            if (jSONObject != null) {
                jSONObject2.put("customData", jSONObject);
            }
        } catch (JSONException e) {
        }
        a(jSONObject2.toString(), dE, null);
        return dE;
    }

    public void a(long j, int i) {
        this.zM.c(j, i);
        this.zN.c(j, i);
        this.zO.c(j, i);
        this.zP.c(j, i);
        this.zQ.c(j, i);
        this.zR.c(j, i);
        this.zS.c(j, i);
        this.zT.c(j, i);
    }

    public long b(eu euVar, JSONObject jSONObject) throws IOException {
        JSONObject jSONObject2 = new JSONObject();
        long dE = dE();
        this.zP.a(dE, euVar);
        w(true);
        try {
            jSONObject2.put("requestId", dE);
            jSONObject2.put(AnalyticsSQLiteHelper.EVENT_LIST_TYPE, "STOP");
            jSONObject2.put("mediaSessionId", dC());
            if (jSONObject != null) {
                jSONObject2.put("customData", jSONObject);
            }
        } catch (JSONException e) {
        }
        a(jSONObject2.toString(), dE, null);
        return dE;
    }

    public long c(eu euVar, JSONObject jSONObject) throws IOException, IllegalStateException {
        JSONObject jSONObject2 = new JSONObject();
        long dE = dE();
        this.zO.a(dE, euVar);
        w(true);
        try {
            jSONObject2.put("requestId", dE);
            jSONObject2.put(AnalyticsSQLiteHelper.EVENT_LIST_TYPE, "PLAY");
            jSONObject2.put("mediaSessionId", dC());
            if (jSONObject != null) {
                jSONObject2.put("customData", jSONObject);
            }
        } catch (JSONException e) {
        }
        a(jSONObject2.toString(), dE, null);
        return dE;
    }

    public long dC() throws IllegalStateException {
        if (this.zL != null) {
            return this.zL.dC();
        }
        throw new IllegalStateException("No current media session");
    }

    public void dF() {
        dS();
    }

    public long getApproximateStreamPosition() {
        MediaInfo mediaInfo = getMediaInfo();
        if (mediaInfo == null || this.zK == 0) {
            return 0;
        }
        double playbackRate = this.zL.getPlaybackRate();
        long streamPosition = this.zL.getStreamPosition();
        int playerState = this.zL.getPlayerState();
        if (playbackRate == 0.0d || playerState != 2) {
            return streamPosition;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime() - this.zK;
        long j = elapsedRealtime < 0 ? 0 : elapsedRealtime;
        if (j == 0) {
            return streamPosition;
        }
        elapsedRealtime = mediaInfo.getStreamDuration();
        streamPosition += (long) (((double) j) * playbackRate);
        if (streamPosition <= elapsedRealtime) {
            elapsedRealtime = streamPosition < 0 ? 0 : streamPosition;
        }
        return elapsedRealtime;
    }

    public MediaInfo getMediaInfo() {
        return this.zL == null ? null : this.zL.getMediaInfo();
    }

    public MediaStatus getMediaStatus() {
        return this.zL;
    }

    public long getStreamDuration() {
        MediaInfo mediaInfo = getMediaInfo();
        return mediaInfo != null ? mediaInfo.getStreamDuration() : 0;
    }

    protected void onMetadataUpdated() {
    }

    protected void onStatusUpdated() {
    }
}