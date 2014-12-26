package com.inmobi.monetization.internal;

import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import com.inmobi.androidsdk.IMBrowserActivity;
import com.inmobi.commons.InMobi;
import com.inmobi.commons.ads.cache.AdDatabaseHelper;
import com.inmobi.commons.analytics.bootstrapper.AnalyticsInitializer;
import com.inmobi.commons.data.DeviceInfo;
import com.inmobi.commons.internal.ActivityRecognitionManager;
import com.inmobi.commons.internal.Base64;
import com.inmobi.commons.internal.EncryptionUtils;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.internal.Log;
import com.inmobi.commons.internal.ThinICE;
import com.inmobi.commons.metric.EventLog;
import com.inmobi.commons.network.ErrorCode;
import com.inmobi.commons.network.Request;
import com.inmobi.commons.network.RequestBuilderUtils;
import com.inmobi.commons.network.Response;
import com.inmobi.commons.network.abstraction.INetworkListener;
import com.inmobi.commons.uid.UID;
import com.inmobi.monetization.internal.LtvpRuleProcessor.ActionsRule;
import com.inmobi.monetization.internal.carb.CARB;
import com.inmobi.monetization.internal.configs.Initializer;
import com.inmobi.monetization.internal.configs.NetworkEventType;
import com.inmobi.monetization.internal.imai.IMAIController;
import com.inmobi.monetization.internal.objects.LtvpRuleCache;
import com.inmobi.re.container.IMWebView;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Ad {
    public static final String AD_TYPE_NATIVE = "native";
    protected static final String DEFAULT_NO_OF_ADS = "1";
    protected static final String KEY_AD_FORMAT = "format";
    protected static final String KEY_AD_SIZE = "mk-ad-slot";
    protected static final String KEY_NO_OF_ADS = "mk-ads";
    protected static final String KEY_PLACEMENT_SIZE = "placement-size";
    private static ConnBroadcastReciever e;
    private String a;
    private long b;
    boolean c;
    private AtomicBoolean d;
    private Map<String, String> f;
    private String g;
    private String h;
    private String i;
    private i j;
    private c k;
    private ActionsRule l;
    private HashMap<String, String> m;
    protected IMAdListener mAdListener;
    protected boolean mEnableHardwareAcceleration;
    protected long mFetchStartTime;
    protected String mImpressionId;

    protected enum AD_FORMAT {
        IMAI,
        NATIVE;

        static {
            IMAI = new AD_FORMAT("IMAI", 0);
            NATIVE = new AD_FORMAT("NATIVE", 1);
            a = new AD_FORMAT[]{IMAI, NATIVE};
        }
    }

    static /* synthetic */ class b {
        static final /* synthetic */ int[] a;

        static {
            a = new int[ActionsRule.values().length];
            try {
                a[ActionsRule.ACTIONS_ONLY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                a[ActionsRule.ACTIONS_TO_MEDIATION.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                a[ActionsRule.MEDIATION.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            a[ActionsRule.NO_ADS.ordinal()] = 4;
        }
    }

    static class c extends Handler {
        private final WeakReference<Ad> a;

        public c(Ad ad) {
            this.a = new WeakReference(ad);
        }

        public void handleMessage(Message message) {
            Ad ad = (Ad) this.a.get();
            if (ad != null) {
                switch (message.what) {
                    case IMBrowserActivity.INTERSTITIAL_ACTIVITY:
                        long currentTimeMillis = System.currentTimeMillis() - ad.mFetchStartTime;
                        JSONObject jSONObject = new JSONObject();
                        try {
                            jSONObject.put("t", currentTimeMillis);
                            jSONObject.put("m", 1);
                            ad.a(jSONObject, NetworkEventType.CONNECT_ERROR);
                        } catch (JSONException e) {
                            Log.internal(Constants.LOG_TAG, "Error creating metric logs for error at " + System.currentTimeMillis());
                        }
                        ad.setDownloadingNewAd(false);
                        if (ad.mAdListener != null) {
                            ad.mAdListener.onAdRequestFailed(AdErrorCode.NETWORK_ERROR);
                        }
                    default:
                        break;
                }
            }
        }
    }

    class a implements INetworkListener {
        a() {
        }

        public void onRequestFailed(Request request, Response response) {
            if (Ad.this.l != ActionsRule.ACTIONS_TO_MEDIATION) {
                Ad.this.handleError((i) request, response);
            } else if (Ad.this.mAdListener != null) {
                Ad.this.mAdListener.onAdRequestFailed(AdErrorCode.DO_MONETIZE);
            }
            Ad.this.setDownloadingNewAd(false);
        }

        public void onRequestSucceded(Request request, Response response) {
            Response decryptedResponse = Ad.this.getDecryptedResponse((i) request, response);
            if (decryptedResponse == null) {
                Ad.this.handleError((i) request, response);
            } else {
                Log.internal(Constants.LOG_TAG, "Raw Ad Response: " + decryptedResponse.getResponseBody());
                Ad.this.handleResponse((i) request, decryptedResponse);
            }
            Ad.this.collectMetrics(response, System.currentTimeMillis() - Ad.this.mFetchStartTime, NetworkEventType.FETCH_COMPLETE);
            Ad.this.setDownloadingNewAd(false);
            Ad.this.k.removeMessages(IMBrowserActivity.INTERSTITIAL_ACTIVITY);
        }
    }

    static {
        e = null;
    }

    public Ad(long j) {
        this.a = null;
        this.b = 0;
        this.mEnableHardwareAcceleration = true;
        this.d = new AtomicBoolean();
        this.mFetchStartTime = 0;
        this.g = null;
        this.h = null;
        this.i = null;
        this.j = null;
        this.k = new c(this);
        this.mImpressionId = null;
        this.l = ActionsRule.MEDIATION;
        this.m = null;
        this.mAdListener = null;
        this.c = false;
        this.b = j;
    }

    public Ad(String str) {
        this.a = null;
        this.b = 0;
        this.mEnableHardwareAcceleration = true;
        this.d = new AtomicBoolean();
        this.mFetchStartTime = 0;
        this.g = null;
        this.h = null;
        this.i = null;
        this.j = null;
        this.k = new c(this);
        this.mImpressionId = null;
        this.l = ActionsRule.MEDIATION;
        this.m = null;
        this.mAdListener = null;
        this.c = false;
        if (str == null || Preconditions.EMPTY_ARGUMENTS.equals(str.trim())) {
            this.a = InMobi.getAppId();
        } else {
            this.a = str;
        }
    }

    private Map<String, String> a() {
        Map<String, String> hashMap = new HashMap();
        if (hashMap != null) {
            if (this.m != null) {
                Iterator it = this.m.keySet().iterator();
                while (it.hasNext()) {
                    String str = (String) it.next();
                    hashMap.put(str, this.m.get(str));
                }
            }
            if (!(this.h == null || this.i == null)) {
                hashMap.put(this.h, this.i);
            }
            if (this.g != null) {
                hashMap.put("p-keywords", this.g);
            }
        }
        return hashMap;
    }

    private void a(JSONObject jSONObject, NetworkEventType networkEventType) {
        if (this.c) {
            Initializer.getLogger().logEvent(new EventLog(networkEventType, jSONObject));
        }
    }

    private boolean b() {
        return this.d.get();
    }

    private ActionsRule c() {
        return LtvpRuleProcessor.getInstance().getLtvpRuleConfig(this.b);
    }

    protected void collectMetrics(Response response, long j, NetworkEventType networkEventType) {
        try {
            if (this.c) {
                JSONObject jSONObject = new JSONObject();
                if (response.getStatusCode() > 400) {
                    jSONObject.put("m", response.getError());
                } else if (response.getStatusCode() != 200) {
                    jSONObject.put("m", response.getStatusCode());
                } else {
                    Map headers = response.getHeaders();
                    if (headers != null) {
                        this.mImpressionId = (String) ((List) headers.get(InvalidManifestErrorMessages.IMP_ID_KEY)).get(0);
                        String str = (String) ((List) headers.get(InvalidManifestErrorMessages.SANDBOX_ERR_KEY)).get(0);
                        if (str != null) {
                            Log.debug(Constants.LOG_TAG, "Sandbox error Id: " + str);
                        }
                    }
                    jSONObject.put(AdDatabaseHelper.TABLE_AD, this.mImpressionId);
                }
                jSONObject.put("t", j);
                Initializer.getLogger().logEvent(new EventLog(networkEventType, jSONObject));
            }
        } catch (Exception e) {
            Log.internal(Constants.LOG_TAG, "Error creating metric logs for ad fetch at " + System.currentTimeMillis());
        }
    }

    public void destroy() {
        if (InternalSDKUtil.isInitializedSuccessfully()) {
            e = null;
        } else {
            Log.debug(Constants.LOG_TAG, "Please initialize the sdk");
        }
    }

    protected abstract Map<String, String> getAdFormatParams();

    protected Response getDecryptedResponse(i iVar, Response response) {
        Response response2 = null;
        try {
            return new Response(new String(EncryptionUtils.DeAe(Base64.decode(response.getResponseBody().getBytes(), 0), iVar.b(), iVar.a())), response.getStatusCode(), response.getHeaders());
        } catch (Exception e) {
            e.printStackTrace();
            return response2;
        }
    }

    protected void handleError(i iVar, Response response) {
        long currentTimeMillis = System.currentTimeMillis() - this.mFetchStartTime;
        if (this.mAdListener == null) {
            return;
        }
        if (response.getStatusCode() == 204) {
            this.mAdListener.onAdRequestFailed(AdErrorCode.NO_FILL);
            collectMetrics(response, currentTimeMillis, NetworkEventType.RESPONSE_ERROR);
        } else if (response.getStatusCode() == 400) {
            Log.debug(Constants.LOG_TAG, "Check the app Id passed in the ad");
            this.mAdListener.onAdRequestFailed(AdErrorCode.INVALID_APP_ID);
            collectMetrics(response, currentTimeMillis, NetworkEventType.RESPONSE_ERROR);
        } else if (response.getError() != null) {
            ErrorCode error = response.getError();
            if (error.equals(ErrorCode.INTERNAL_ERROR)) {
                this.mAdListener.onAdRequestFailed(AdErrorCode.INTERNAL_ERROR);
            } else if (error.equals(ErrorCode.INVALID_REQUEST)) {
                this.mAdListener.onAdRequestFailed(AdErrorCode.INVALID_REQUEST);
            } else if (error.equals(ErrorCode.NETWORK_ERROR)) {
                this.mAdListener.onAdRequestFailed(AdErrorCode.NETWORK_ERROR);
            } else if (error.equals(ErrorCode.CONNECTION_ERROR)) {
            }
            collectMetrics(response, currentTimeMillis, NetworkEventType.CONNECT_ERROR);
        } else {
            this.mAdListener.onAdRequestFailed(AdErrorCode.INTERNAL_ERROR);
        }
    }

    protected abstract void handleResponse(i iVar, Response response);

    protected boolean initialize() {
        if (!InternalSDKUtil.isInitializedSuccessfully()) {
            Log.debug(Constants.LOG_TAG, "Please initialize the sdk");
            return false;
        } else if ((this.a == null || Preconditions.EMPTY_ARGUMENTS.equals(this.a.trim())) && 0 == this.b) {
            android.util.Log.e(Constants.LOG_TAG, "Please create an instance of  ad with valid appId/ slotid");
            return false;
        } else {
            try {
                ThinICE.start(InternalSDKUtil.getContext());
            } catch (Exception e) {
                Log.internal(Constants.LOG_TAG, "Cannot start ice. Activity is null");
            }
            try {
                h.a(InternalSDKUtil.getContext());
            } catch (InvalidManifestConfigException e2) {
                Log.internal(Constants.LOG_TAG, "IMConfigException occured while initializing interstitial while validating adView", e2);
            }
            InternalSDKUtil.getUserAgent();
            CARB.getInstance().startCarb();
            UID.getInstance().printPublisherTestId();
            IMWebView.setIMAIController(IMAIController.class);
            if (e == null) {
                e = new ConnBroadcastReciever();
            }
            InternalSDKUtil.getContext().registerReceiver(e, new IntentFilter(InvalidManifestErrorMessages.CONNECTIVITY_INTENT_ACTION));
            if (this.b > 0) {
                this.l = c();
            }
            return true;
        }
    }

    protected void loadAd() {
        if (InternalSDKUtil.isInitializedSuccessfully()) {
            DeviceInfo.updateDeviceOrientation();
            DeviceInfo.updateNetworkConnectedInfo();
            UID.getInstance().printPublisherTestId();
            Log.debug(Constants.LOG_TAG, " >>>> Start loading new Ad <<<<");
            try {
                if (InternalSDKUtil.checkNetworkAvailibility(InternalSDKUtil.getContext())) {
                    if (!b()) {
                        if (getAdFormatParams() == null) {
                            this.f = new HashMap();
                        } else {
                            this.f = getAdFormatParams();
                        }
                        if (!this.f.containsKey(KEY_AD_FORMAT)) {
                            this.f.put(KEY_AD_FORMAT, AD_FORMAT.IMAI.toString().toLowerCase(Locale.getDefault()));
                        }
                        if (!this.f.containsKey(KEY_NO_OF_ADS)) {
                            this.f.put(KEY_NO_OF_ADS, DEFAULT_NO_OF_ADS);
                        }
                        if (!(this.a == null || Preconditions.EMPTY_ARGUMENTS.equals(this.a))) {
                            this.f.put(RequestBuilderUtils.KEY_MK_SITE_ID, this.a);
                        }
                        this.j = new i();
                        this.j.b(a());
                        if (this.b > 0 && this.l != null) {
                            switch (b.a[this.l.ordinal()]) {
                                case MMAdView.TRANSITION_FADE:
                                case MMAdView.TRANSITION_UP:
                                    this.f.put(RequestBuilderUtils.KEY_MK_SITE_SLOT_ID, Long.toString(this.b));
                                    this.f.put(RequestBuilderUtils.RULE_ID, LtvpRuleCache.getInstance(InternalSDKUtil.getContext()).getLtvpRuleId());
                                    int detectedActivity = ActivityRecognitionManager.getDetectedActivity();
                                    if (detectedActivity != -1) {
                                        this.f.put("u-activity-type", detectedActivity + Preconditions.EMPTY_ARGUMENTS);
                                    }
                                    if (this.j != null) {
                                        this.j.setUrl(AnalyticsInitializer.getConfigParams().getEndPoints().getHouseUrl());
                                    }
                                    break;
                                case MMAdView.TRANSITION_DOWN:
                                    if (this.mAdListener != null) {
                                        this.mAdListener.onAdRequestFailed(AdErrorCode.DO_MONETIZE);
                                    }
                                    break;
                                case MMAdView.TRANSITION_RANDOM:
                                    if (this.mAdListener != null) {
                                        this.mAdListener.onAdRequestFailed(AdErrorCode.DO_NOTHING);
                                    }
                                    break;
                                default:
                                    if (this.mAdListener != null) {
                                        this.mAdListener.onAdRequestFailed(AdErrorCode.NO_FILL);
                                    }
                                    break;
                            }
                            if (!(this.l == ActionsRule.ACTIONS_ONLY || this.l == ActionsRule.ACTIONS_TO_MEDIATION)) {
                                Log.internal(Constants.LOG_TAG, "No actions returned by rule");
                                return;
                            }
                        }
                        this.j.a(this.f);
                        setDownloadingNewAd(true);
                        b.a().a(this.a, this.j, new a());
                        this.mFetchStartTime = System.currentTimeMillis();
                        this.k.sendEmptyMessageDelayed(IMBrowserActivity.INTERSTITIAL_ACTIVITY, (long) Initializer.getConfigParams().getFetchTimeOut());
                        this.c = Initializer.getLogger().startNewSample();
                    } else if (this.mAdListener != null) {
                        this.mAdListener.onAdRequestFailed(AdErrorCode.AD_DOWNLOAD_IN_PROGRESS);
                    }
                } else if (this.mAdListener != null) {
                    this.mAdListener.onAdRequestFailed(AdErrorCode.NETWORK_ERROR);
                }
            } catch (Exception e) {
                Throwable th = e;
                handleError(this.j, new Response(ErrorCode.INTERNAL_ERROR));
                Log.debug(Constants.LOG_TAG, "Error in loading ad ", th);
            }
        } else {
            Log.debug(Constants.LOG_TAG, "Please initialize the sdk");
            if (this.mAdListener != null) {
                this.mAdListener.onAdRequestFailed(AdErrorCode.INVALID_REQUEST);
            }
        }
    }

    public void setAdListener(IMAdListener iMAdListener) {
        this.mAdListener = iMAdListener;
    }

    protected void setAdRequest(i iVar) {
        this.j = iVar;
    }

    public void setAppId(String str) {
        if (str == null || Preconditions.EMPTY_ARGUMENTS.equals(str)) {
            Log.debug(InvalidManifestErrorMessages.LOGGING_TAG, "AppId cannot be null or blank.");
        } else {
            this.a = str;
        }
    }

    protected void setDownloadingNewAd(boolean z) {
        this.d.set(z);
    }

    public void setKeywords(String str) {
        if (!InternalSDKUtil.isInitializedSuccessfully()) {
            Log.debug(Constants.LOG_TAG, "Please initialize the sdk");
        } else if (str != null && !Preconditions.EMPTY_ARGUMENTS.equals(str)) {
            this.g = str;
        }
    }

    public void setRequestParams(Map<String, String> map) {
        if (!InternalSDKUtil.isInitializedSuccessfully()) {
            Log.debug(Constants.LOG_TAG, "Please initialize the sdk");
        } else if (map != null && !map.isEmpty()) {
            if (this.m == null) {
                this.m = new HashMap();
            }
            Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                this.m.put(str, map.get(str));
            }
        }
    }

    public void setSlotId(long j) {
        if (j > 0) {
            this.b = j;
        } else {
            Log.debug(InvalidManifestErrorMessages.LOGGING_TAG, "Invalid slot id");
        }
    }

    public void stopLoading() {
        if (this.k.hasMessages(IMBrowserActivity.INTERSTITIAL_ACTIVITY)) {
            this.k.removeMessages(IMBrowserActivity.INTERSTITIAL_ACTIVITY);
            Log.debug(Constants.LOG_TAG, "Stopped loading an ad");
            if (this.mAdListener != null) {
                this.mAdListener.onAdRequestFailed(AdErrorCode.ADREQUEST_CANCELLED);
            }
        }
    }
}