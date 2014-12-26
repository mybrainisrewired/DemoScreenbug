package com.inmobi.monetization;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.google.android.gms.location.LocationRequest;
import com.inmobi.androidsdk.IMBrowserActivity;
import com.inmobi.commons.AnimationType;
import com.inmobi.commons.internal.Log;
import com.inmobi.commons.internal.ThinICE;
import com.inmobi.monetization.internal.AdErrorCode;
import com.inmobi.monetization.internal.BannerAd;
import com.inmobi.monetization.internal.Constants;
import com.inmobi.monetization.internal.IMAdListener;
import com.inmobi.monetization.internal.InvalidManifestErrorMessages;
import com.inmobi.monetization.internal.configs.Initializer;
import com.inmobi.monetization.internal.imai.RequestResponseManager;
import com.mopub.common.Preconditions;
import java.util.HashMap;
import java.util.Map;

public final class IMBanner extends RelativeLayout {
    public static final int INMOBI_AD_UNIT_120X600 = 13;
    public static final int INMOBI_AD_UNIT_300X250 = 10;
    public static final int INMOBI_AD_UNIT_320X48 = 9;
    public static final int INMOBI_AD_UNIT_320X50 = 15;
    public static final int INMOBI_AD_UNIT_468X60 = 12;
    public static final int INMOBI_AD_UNIT_728X90 = 11;
    public static final int REFRESH_INTERVAL_MINIMUM = 0;
    public static final int REFRESH_INTERVAL_OFF = -1;
    IMAdListener a;
    private BannerAd b;
    private IMBannerListener c;
    private boolean d;
    private Activity e;
    private String f;
    private long g;
    private int h;

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
            try {
                switch (this.a) {
                    case LocationRequest.PRIORITY_HIGH_ACCURACY:
                        IMBanner.this.c.onBannerRequestSucceeded(IMBanner.this);
                    case IMBrowserActivity.INTERSTITIAL_ACTIVITY:
                        IMBanner.this.c.onBannerRequestFailed(IMBanner.this, IMErrorCode.a(this.b));
                    case LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY:
                        IMBanner.this.c.onShowBannerScreen(IMBanner.this);
                    case 103:
                        IMBanner.this.c.onDismissBannerScreen(IMBanner.this);
                    case LocationRequest.PRIORITY_LOW_POWER:
                        IMBanner.this.c.onLeaveApplication(IMBanner.this);
                    case LocationRequest.PRIORITY_NO_POWER:
                        IMBanner.this.c.onBannerInteraction(IMBanner.this, this.c);
                    default:
                        Log.debug(Constants.LOG_TAG, this.b.toString());
                }
            } catch (Exception e) {
                Log.debug(Constants.LOG_TAG, "Exception giving callback to the publisher ", e);
            }
        }
    }

    public IMBanner(Activity activity, long j) {
        super(activity);
        this.d = true;
        this.f = null;
        this.g = -1;
        this.h = 15;
        this.a = new c(this);
        this.e = activity;
        this.g = j;
        a();
    }

    public IMBanner(Activity activity, String str, int i) {
        super(activity);
        this.d = true;
        this.f = null;
        this.g = -1;
        this.h = 15;
        this.a = new c(this);
        this.f = str;
        this.h = i;
        this.e = activity;
        a();
    }

    public IMBanner(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.d = true;
        this.f = null;
        this.g = -1;
        this.h = 15;
        this.a = new c(this);
        this.e = (Activity) context;
        try {
            this.g = Long.parseLong(attributeSet.getAttributeValue(null, "slotId"));
            a();
        } catch (Exception e) {
            try {
                this.h = Integer.parseInt(attributeSet.getAttributeValue(null, "adSize"));
            } catch (Exception e2) {
            }
            try {
                this.f = attributeSet.getAttributeValue(null, "appId");
            } catch (Exception e3) {
            }
            a();
        }
    }

    private void a() {
        if (this.g > 0) {
            this.b = new BannerAd(this.e, this, this.g, 15);
        } else {
            this.b = new BannerAd(this.e, this, this.f, this.h);
        }
        this.b.setAdListener(this.a);
        setRefreshInterval(Initializer.getConfigParams().getDefaultRefreshRate());
        addView(this.b.getView(), new LayoutParams(-1, -1));
    }

    private void a(int i, AdErrorCode adErrorCode, Map<?, ?> map) {
        if (!this.d) {
            Log.debug(Constants.LOG_TAG, InvalidManifestErrorMessages.MSG_CALL_BACK);
        } else if (this.c != null) {
            this.e.runOnUiThread(new a(i, adErrorCode, map));
        }
    }

    public void destroy() {
        if (this.b != null) {
            this.b.destroy();
        }
    }

    public void disableHardwareAcceleration() {
        if (this.b != null) {
            this.b.disableHardwareAcceleration();
        }
    }

    public void loadBanner() {
        if (this.b != null) {
            this.b.loadAd();
            removeAllViews();
            addView(this.b.getView(), new LayoutParams(-1, -1));
        } else {
            IMErrorCode iMErrorCode = IMErrorCode.INVALID_REQUEST;
            String str = "Banner Ad instance not created with valid paramters";
            iMErrorCode.setMessage(str);
            this.c.onBannerRequestFailed(this, iMErrorCode);
            Log.verbose(Constants.LOG_TAG, str);
        }
    }

    protected void onAttachedToWindow() {
        Log.debug(Constants.LOG_TAG, "onAttachedToWindow");
        this.d = true;
    }

    protected void onDetachedFromWindow() {
        this.d = false;
        if (this.b != null) {
            this.b.destroy();
            super.onDetachedFromWindow();
        }
    }

    protected final void onWindowVisibilityChanged(int i) {
        if (i == 0) {
            try {
                ThinICE.start(this.e);
            } catch (Exception e) {
                Log.internal(InvalidManifestErrorMessages.LOGGING_TAG, "Cannot start ice. Activity is null");
            }
            if (this.b != null) {
                this.b.refreshAd();
            }
        } else if (this.b != null) {
            this.b.stopRefresh();
        }
        RequestResponseManager requestResponseManager = new RequestResponseManager();
        requestResponseManager.init();
        requestResponseManager.processClick(this.e.getApplicationContext(), null);
    }

    public void setAdSize(int i) {
        if (this.b != null) {
            this.b.setAdSize(i);
        }
    }

    public void setAnimationType(AnimationType animationType) {
        if (this.b != null) {
            this.b.setAnimation(animationType);
        }
    }

    public void setAppId(String str) {
        if (this.b != null) {
            this.b.setAppId(str);
        }
    }

    public void setIMBannerListener(IMBannerListener iMBannerListener) {
        this.c = iMBannerListener;
    }

    public void setKeywords(String str) {
        if (str == null || Preconditions.EMPTY_ARGUMENTS.equals(str.trim())) {
            Log.debug(InvalidManifestErrorMessages.LOGGING_TAG, "Keywords cannot be null or blank.");
        } else if (this.b != null) {
            this.b.setKeywords(str);
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
            if (this.b != null) {
                this.b.setRequestParams(hashMap);
            }
        }
    }

    public void setRefreshInterval(int i) {
        if (this.b != null) {
            this.b.setRefreshInterval(i);
        }
    }

    public void setRequestParams(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            Log.debug(InvalidManifestErrorMessages.LOGGING_TAG, "Request params cannot be null or empty.");
        }
        if (this.b != null) {
            this.b.setRequestParams(map);
        }
    }

    public void setSlotId(long j) {
        if (this.b != null) {
            this.b.setSlotId(j);
        }
    }

    public void stopLoading() {
        if (this.b != null) {
            this.b.stopLoading();
        }
    }
}