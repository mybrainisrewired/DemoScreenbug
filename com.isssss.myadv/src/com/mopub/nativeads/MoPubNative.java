package com.mopub.nativeads;

import android.content.Context;
import android.view.View;
import com.mopub.common.DownloadResponse;
import com.mopub.common.DownloadTask;
import com.mopub.common.DownloadTask.DownloadTaskListener;
import com.mopub.common.GpsHelper;
import com.mopub.common.GpsHelper.GpsHelperListener;
import com.mopub.common.HttpClient;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.AsyncTasks;
import com.mopub.common.util.DeviceUtils;
import com.mopub.common.util.ManifestUtils;
import com.mopub.common.util.ResponseHeader;
import com.mopub.mobileads.MoPubView;
import com.mopub.nativeads.CustomEventNative.CustomEventNativeListener;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.methods.HttpUriRequest;

public class MoPubNative {
    static final MoPubNativeEventListener EMPTY_EVENT_LISTENER;
    static final MoPubNativeNetworkListener EMPTY_NETWORK_LISTENER;
    private final String mAdUnitId;
    private final WeakReference<Context> mContext;
    private Map<String, Object> mLocalExtras;
    private MoPubNativeEventListener mMoPubNativeEventListener;
    private MoPubNativeNetworkListener mMoPubNativeNetworkListener;

    public static interface MoPubNativeEventListener {
        void onNativeClick(View view);

        void onNativeImpression(View view);
    }

    public static interface MoPubNativeNetworkListener {
        void onNativeFail(NativeErrorCode nativeErrorCode);

        void onNativeLoad(NativeResponse nativeResponse);
    }

    @Deprecated
    public static interface MoPubNativeListener extends com.mopub.nativeads.MoPubNative.MoPubNativeNetworkListener, com.mopub.nativeads.MoPubNative.MoPubNativeEventListener {
    }

    class NativeGpsHelperListener implements GpsHelperListener {
        private final RequestParameters mRequestParameters;

        NativeGpsHelperListener(RequestParameters requestParameters) {
            this.mRequestParameters = requestParameters;
        }

        public void onFetchAdInfoCompleted() {
            MoPubNative.this.loadNativeAd(this.mRequestParameters);
        }
    }

    static {
        EMPTY_NETWORK_LISTENER = new MoPubNativeNetworkListener() {
            public void onNativeFail(NativeErrorCode errorCode) {
            }

            public void onNativeLoad(NativeResponse nativeResponse) {
                nativeResponse.destroy();
            }
        };
        EMPTY_EVENT_LISTENER = new MoPubNativeEventListener() {
            public void onNativeClick(View view) {
            }

            public void onNativeImpression(View view) {
            }
        };
    }

    @Deprecated
    public MoPubNative(Context context, String adUnitId, MoPubNativeNetworkListener moPubNativeListener) {
        this(context, adUnitId, moPubNativeListener);
        setNativeEventListener(moPubNativeListener);
    }

    public MoPubNative(Context context, String adUnitId, MoPubNativeNetworkListener moPubNativeNetworkListener) {
        if (context == null) {
            throw new IllegalArgumentException("Context may not be null.");
        } else if (adUnitId == null) {
            throw new IllegalArgumentException("AdUnitId may not be null.");
        } else if (moPubNativeNetworkListener == null) {
            throw new IllegalArgumentException("MoPubNativeNetworkListener may not be null.");
        } else {
            ManifestUtils.checkNativeActivitiesDeclared(context);
            this.mContext = new WeakReference(context);
            this.mAdUnitId = adUnitId;
            this.mMoPubNativeNetworkListener = moPubNativeNetworkListener;
            this.mMoPubNativeEventListener = EMPTY_EVENT_LISTENER;
            GpsHelper.asyncFetchAdvertisingInfo(context);
        }
    }

    private void downloadJson(HttpUriRequest httpUriRequest) {
        DownloadTask jsonDownloadTask = new DownloadTask(new DownloadTaskListener() {

            class AnonymousClass_1 implements CustomEventNativeListener {
                private final /* synthetic */ DownloadResponse val$downloadResponse;

                AnonymousClass_1(DownloadResponse downloadResponse) {
                    this.val$downloadResponse = downloadResponse;
                }

                public void onNativeAdFailed(NativeErrorCode errorCode) {
                    AnonymousClass_3.this.this$0.requestNativeAd(this.val$downloadResponse.getFirstHeader(ResponseHeader.FAIL_URL));
                }

                public void onNativeAdLoaded(NativeAdInterface nativeAd) {
                    Context context = AnonymousClass_3.this.this$0.getContextOrDestroy();
                    if (context != null) {
                        AnonymousClass_3.this.this$0.mMoPubNativeNetworkListener.onNativeLoad(new NativeResponse(context, this.val$downloadResponse, AnonymousClass_3.this.this$0.mAdUnitId, nativeAd, AnonymousClass_3.this.this$0.mMoPubNativeEventListener));
                    }
                }
            }

            public void onComplete(String url, DownloadResponse downloadResponse) {
                if (downloadResponse == null) {
                    MoPubNative.this.mMoPubNativeNetworkListener.onNativeFail(NativeErrorCode.UNSPECIFIED);
                } else if (downloadResponse.getStatusCode() >= 500 && downloadResponse.getStatusCode() < 600) {
                    MoPubNative.this.mMoPubNativeNetworkListener.onNativeFail(NativeErrorCode.SERVER_ERROR_RESPONSE_CODE);
                } else if (downloadResponse.getStatusCode() != 200) {
                    MoPubNative.this.mMoPubNativeNetworkListener.onNativeFail(NativeErrorCode.UNEXPECTED_RESPONSE_CODE);
                } else if (downloadResponse.getContentLength() == 0) {
                    MoPubNative.this.mMoPubNativeNetworkListener.onNativeFail(NativeErrorCode.EMPTY_AD_RESPONSE);
                } else {
                    CustomEventNativeListener customEventNativeListener = new AnonymousClass_1(downloadResponse);
                    Context context = MoPubNative.this.getContextOrDestroy();
                    if (context != null) {
                        CustomEventNativeAdapter.loadNativeAd(context, MoPubNative.this.mLocalExtras, downloadResponse, customEventNativeListener);
                    }
                }
            }
        });
        try {
            AsyncTasks.safeExecuteOnExecutor(jsonDownloadTask, new HttpUriRequest[]{httpUriRequest});
        } catch (Exception e) {
            MoPubLog.d("Failed to download json", e);
            this.mMoPubNativeNetworkListener.onNativeFail(NativeErrorCode.UNSPECIFIED);
        }
    }

    public void destroy() {
        this.mContext.clear();
        this.mMoPubNativeNetworkListener = EMPTY_NETWORK_LISTENER;
        this.mMoPubNativeEventListener = EMPTY_EVENT_LISTENER;
    }

    Context getContextOrDestroy() {
        Context context = (Context) this.mContext.get();
        if (context == null) {
            destroy();
            MoPubLog.d("Weak reference to Activity Context in MoPubNative became null. This instance of MoPubNative is destroyed and No more requests will be processed.");
        }
        return context;
    }

    @Deprecated
    @VisibleForTesting
    MoPubNativeEventListener getMoPubNativeEventListener() {
        return this.mMoPubNativeEventListener;
    }

    @Deprecated
    @VisibleForTesting
    MoPubNativeNetworkListener getMoPubNativeNetworkListener() {
        return this.mMoPubNativeNetworkListener;
    }

    void loadNativeAd(RequestParameters requestParameters) {
        loadNativeAd(requestParameters, null);
    }

    void loadNativeAd(RequestParameters requestParameters, Integer sequenceNumber) {
        Context context = getContextOrDestroy();
        if (context != null) {
            NativeUrlGenerator generator = new NativeUrlGenerator(context).withAdUnitId(this.mAdUnitId).withRequest(requestParameters);
            if (sequenceNumber != null) {
                generator.withSequenceNumber(sequenceNumber.intValue());
            }
            String endpointUrl = generator.generateUrlString(MoPubView.HOST);
            if (endpointUrl != null) {
                MoPubLog.d(new StringBuilder("Loading ad from: ").append(endpointUrl).toString());
            }
            requestNativeAd(endpointUrl);
        }
    }

    public void makeRequest() {
        makeRequest(null);
    }

    void makeRequest(NativeGpsHelperListener nativeGpsHelperListener) {
        Context context = getContextOrDestroy();
        if (context != null) {
            if (DeviceUtils.isNetworkAvailable(context)) {
                GpsHelper.asyncFetchAdvertisingInfoIfNotCached(context, nativeGpsHelperListener);
            } else {
                this.mMoPubNativeNetworkListener.onNativeFail(NativeErrorCode.CONNECTION_ERROR);
            }
        }
    }

    public void makeRequest(RequestParameters requestParameters) {
        makeRequest(new NativeGpsHelperListener(requestParameters));
    }

    void requestNativeAd(String endpointUrl) {
        Context context = getContextOrDestroy();
        if (context != null) {
            if (endpointUrl == null) {
                this.mMoPubNativeNetworkListener.onNativeFail(NativeErrorCode.INVALID_REQUEST_URL);
            } else {
                try {
                    downloadJson(HttpClient.initializeHttpGet(endpointUrl, context));
                } catch (IllegalArgumentException e) {
                    this.mMoPubNativeNetworkListener.onNativeFail(NativeErrorCode.INVALID_REQUEST_URL);
                }
            }
        }
    }

    public void setLocalExtras(Map<String, Object> localExtras) {
        this.mLocalExtras = new HashMap(localExtras);
    }

    public void setNativeEventListener(MoPubNativeEventListener nativeEventListener) {
        if (nativeEventListener == null) {
            nativeEventListener = EMPTY_EVENT_LISTENER;
        }
        this.mMoPubNativeEventListener = nativeEventListener;
    }
}