package com.inmobi.monetization;

import android.app.Activity;
import com.google.android.gms.location.LocationRequest;
import com.inmobi.androidsdk.IMBrowserActivity;
import com.inmobi.commons.internal.Log;
import com.inmobi.monetization.internal.AdErrorCode;
import com.inmobi.monetization.internal.Constants;
import com.inmobi.monetization.internal.IMAdListener;
import com.inmobi.monetization.internal.InterstitialAd;
import com.inmobi.monetization.internal.InvalidManifestErrorMessages;
import com.mopub.common.Preconditions;
import java.util.HashMap;
import java.util.Map;

public class IMInterstitial {
    InterstitialAd a;
    boolean b;
    IMAdListener c;
    private IMInterstitialListener d;
    private IMIncentivisedListener e;
    private long f;
    private Activity g;
    private State h;
    private String i;

    public enum State {
        INIT,
        ACTIVE,
        LOADING,
        READY,
        UNKNOWN;

        static {
            INIT = new com.inmobi.monetization.IMInterstitial.State("INIT", 0);
            ACTIVE = new com.inmobi.monetization.IMInterstitial.State("ACTIVE", 1);
            LOADING = new com.inmobi.monetization.IMInterstitial.State("LOADING", 2);
            READY = new com.inmobi.monetization.IMInterstitial.State("READY", 3);
            UNKNOWN = new com.inmobi.monetization.IMInterstitial.State("UNKNOWN", 4);
            a = new com.inmobi.monetization.IMInterstitial.State[]{INIT, ACTIVE, LOADING, READY, UNKNOWN};
        }
    }

    class a implements Runnable {
        final /* synthetic */ int a;
        final /* synthetic */ AdErrorCode b;
        final /* synthetic */ Map c;

        a(int i, AdErrorCode adErrorCode, Map map) {
            this.a = i;
            this.b = adErrorCode;
            this.c = map;
        }

        public void run() {
            switch (this.a) {
                case LocationRequest.PRIORITY_HIGH_ACCURACY:
                    IMInterstitial.this.onInterstitialLoaded(IMInterstitial.this);
                case IMBrowserActivity.INTERSTITIAL_ACTIVITY:
                    IMInterstitial.this.onInterstitialFailed(IMInterstitial.this, IMErrorCode.a(this.b));
                case LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY:
                    IMInterstitial.this.onShowInterstitialScreen(IMInterstitial.this);
                case 103:
                    IMInterstitial.this.onDismissInterstitialScreen(IMInterstitial.this);
                case LocationRequest.PRIORITY_LOW_POWER:
                    IMInterstitial.this.onLeaveApplication(IMInterstitial.this);
                case LocationRequest.PRIORITY_NO_POWER:
                    IMInterstitial.this.onInterstitialInteraction(IMInterstitial.this, this.c);
                case 106:
                    if (IMInterstitial.this.e != null) {
                        IMInterstitial.this.e.onIncentCompleted(IMInterstitial.this, this.c);
                    }
                default:
                    Log.debug(Constants.LOG_TAG, this.b.toString());
            }
        }
    }

    public IMInterstitial(Activity activity, long j) {
        this.f = -1;
        this.h = State.INIT;
        this.i = null;
        this.b = false;
        this.c = new a(this);
        this.g = activity;
        this.f = j;
        a();
    }

    public IMInterstitial(Activity activity, String str) {
        this.f = -1;
        this.h = State.INIT;
        this.i = null;
        this.b = false;
        this.c = new a(this);
        this.g = activity;
        this.i = str;
        a();
    }

    private void a() {
        if (this.f > 0) {
            this.a = new InterstitialAd(this.g, this.f);
        } else {
            this.a = new InterstitialAd(this.g, this.i);
        }
        this.a.setAdListener(this.c);
    }

    private void a(int i, AdErrorCode adErrorCode, Map<?, ?> map) {
        if (this.d != null) {
            this.g.runOnUiThread(new a(i, adErrorCode, map));
        }
    }

    public void destroy() {
        if (this.a != null) {
            this.a.destroy();
        }
    }

    public void disableHardwareAcceleration() {
        this.b = true;
    }

    public State getState() {
        return this.h;
    }

    public void loadInterstitial() {
        IMErrorCode iMErrorCode;
        if (this.a == null) {
            iMErrorCode = IMErrorCode.INVALID_REQUEST;
            Log.debug(Constants.LOG_TAG, InvalidManifestErrorMessages.MSG_INVALID_AD_CONFIG);
            this.d.onInterstitialFailed(this, iMErrorCode);
        } else if (this.h == State.LOADING) {
            iMErrorCode = IMErrorCode.INVALID_REQUEST;
            iMErrorCode.setMessage(InvalidManifestErrorMessages.MSG_AD_DOWNLOAD);
            Log.debug(Constants.LOG_TAG, InvalidManifestErrorMessages.MSG_AD_DOWNLOAD);
            this.d.onInterstitialFailed(this, iMErrorCode);
        } else if (this.h == State.ACTIVE) {
            iMErrorCode = IMErrorCode.INVALID_REQUEST;
            iMErrorCode.setMessage(InvalidManifestErrorMessages.MSG_INVALID_AD_CONFIG);
            Log.debug(Constants.LOG_TAG, InvalidManifestErrorMessages.MSG_INVALID_AD_CONFIG);
            this.d.onInterstitialFailed(this, iMErrorCode);
        } else {
            this.a.loadAd();
            this.h = State.LOADING;
        }
    }

    public void setAppId(String str) {
        if (this.a != null) {
            this.a.setAppId(str);
        }
    }

    public void setIMIncentivisedListener(IMIncentivisedListener iMIncentivisedListener) {
        this.e = iMIncentivisedListener;
    }

    public void setIMInterstitialListener(IMInterstitialListener iMInterstitialListener) {
        this.d = iMInterstitialListener;
    }

    public void setKeywords(String str) {
        if (str == null || Preconditions.EMPTY_ARGUMENTS.equals(str.trim())) {
            Log.debug(InvalidManifestErrorMessages.LOGGING_TAG, "Keywords cannot be null or blank.");
        } else if (this.a != null) {
            this.a.setKeywords(str);
        }
    }

    @Deprecated
    public void setRefTagParam(String str, String str2) {
        if (str == null || str2 == null) {
            Log.debug(Constants.LOG_TAG, InvalidManifestErrorMessages.MSG_NIL_KEY_VALUE);
        } else if (str.trim().equals(Preconditions.EMPTY_ARGUMENTS) || str2.trim().equals(Preconditions.EMPTY_ARGUMENTS)) {
            Log.debug(Constants.LOG_TAG, InvalidManifestErrorMessages.MSG_EMPTY_KEY_VALUE);
        } else {
            Map hashMap = new HashMap();
            hashMap.put(str, str2);
            if (this.a != null) {
                this.a.setRequestParams(hashMap);
            }
        }
    }

    public void setRequestParams(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            Log.debug(InvalidManifestErrorMessages.LOGGING_TAG, "Request params cannot be null or empty.");
        }
        if (this.a != null) {
            this.a.setRequestParams(map);
        }
    }

    public void setSlotId(long j) {
        if (this.a != null) {
            this.a.setSlotId(j);
        }
    }

    public void show() {
        if (this.a == null || this.h != State.READY) {
            Log.debug(Constants.LOG_TAG, "Interstitial ad is not in the 'READY' state. Current state: " + this.h);
        } else {
            this.a.show();
        }
    }

    @Deprecated
    public void show(long j) {
        if (this.a != null) {
            this.a.show(j);
        }
    }

    public void stopLoading() {
        if (this.a != null) {
            this.a.stopLoading();
        }
    }
}