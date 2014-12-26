package com.mopub.mobileads;

import android.app.Activity;
import android.net.Uri;
import com.inmobi.re.configs.Initializer;
import com.mopub.common.Preconditions;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Json;
import com.mopub.common.util.ResponseHeader;
import com.mopub.common.util.Strings;
import com.mopub.mobileads.util.HttpResponses;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

abstract class AdLoadTask {
    WeakReference<AdViewController> mWeakAdViewController;

    private static class TaskExtractor {
        private String adType;
        private String adTypeCustomEventName;
        private final AdViewController adViewController;
        private String fullAdType;
        private final HttpResponse response;

        TaskExtractor(HttpResponse response, AdViewController adViewController) {
            this.response = response;
            this.adViewController = adViewController;
        }

        private AdLoadTask createCustomEventAdLoadTask(String customEventData) {
            Map<String, String> paramsMap = new HashMap();
            paramsMap.put(ResponseHeader.CUSTOM_EVENT_NAME.getKey(), this.adTypeCustomEventName);
            if (customEventData != null) {
                paramsMap.put(ResponseHeader.CUSTOM_EVENT_DATA.getKey(), customEventData);
            }
            return new CustomEventAdLoadTask(this.adViewController, paramsMap);
        }

        private boolean eventDataIsInResponseBody(String adType) {
            return Initializer.PRODUCT_MRAID.equals(adType) || "html".equals(adType) || ("interstitial".equals(adType) && "vast".equals(this.fullAdType));
        }

        private AdLoadTask extractCustomEventAdLoadTask() {
            MoPubLog.i("Performing custom event.");
            this.adTypeCustomEventName = HttpResponses.extractHeader(this.response, ResponseHeader.CUSTOM_EVENT_NAME);
            if (this.adTypeCustomEventName != null) {
                return createCustomEventAdLoadTask(HttpResponses.extractHeader(this.response, ResponseHeader.CUSTOM_EVENT_DATA));
            }
            return new LegacyCustomEventAdLoadTask(this.adViewController, this.response.getFirstHeader(ResponseHeader.CUSTOM_SELECTOR.getKey()));
        }

        private AdLoadTask extractCustomEventAdLoadTaskFromNativeParams() throws IOException {
            return createCustomEventAdLoadTask(HttpResponses.extractHeader(this.response, ResponseHeader.NATIVE_PARAMS));
        }

        private AdLoadTask extractCustomEventAdLoadTaskFromResponseBody() throws IOException {
            HttpEntity entity = this.response.getEntity();
            String htmlData = entity != null ? Strings.fromStream(entity.getContent()) : Preconditions.EMPTY_ARGUMENTS;
            this.adViewController.getAdConfiguration().setResponseString(htmlData);
            String redirectUrl = HttpResponses.extractHeader(this.response, ResponseHeader.REDIRECT_URL);
            String clickthroughUrl = HttpResponses.extractHeader(this.response, ResponseHeader.CLICKTHROUGH_URL);
            boolean scrollingEnabled = HttpResponses.extractBooleanHeader(this.response, ResponseHeader.SCROLLABLE, false);
            Map<String, String> eventDataMap = new HashMap();
            eventDataMap.put(AdFetcher.HTML_RESPONSE_BODY_KEY, Uri.encode(htmlData));
            eventDataMap.put(AdFetcher.SCROLLABLE_KEY, Boolean.toString(scrollingEnabled));
            if (redirectUrl != null) {
                eventDataMap.put(AdFetcher.REDIRECT_URL_KEY, redirectUrl);
            }
            if (clickthroughUrl != null) {
                eventDataMap.put(AdFetcher.CLICKTHROUGH_URL_KEY, clickthroughUrl);
            }
            return createCustomEventAdLoadTask(Json.mapToJsonString(eventDataMap));
        }

        AdLoadTask extract() throws IOException {
            this.adType = HttpResponses.extractHeader(this.response, ResponseHeader.AD_TYPE);
            this.fullAdType = HttpResponses.extractHeader(this.response, ResponseHeader.FULL_AD_TYPE);
            MoPubLog.d(new StringBuilder("Loading ad type: ").append(AdTypeTranslator.getAdNetworkType(this.adType, this.fullAdType)).toString());
            this.adTypeCustomEventName = AdTypeTranslator.getCustomEventNameForAdType(this.adViewController.getMoPubView(), this.adType, this.fullAdType);
            if ("custom".equals(this.adType)) {
                return extractCustomEventAdLoadTask();
            }
            return eventDataIsInResponseBody(this.adType) ? extractCustomEventAdLoadTaskFromResponseBody() : extractCustomEventAdLoadTaskFromNativeParams();
        }
    }

    static class CustomEventAdLoadTask extends AdLoadTask {
        private Map<String, String> mParamsMap;

        public CustomEventAdLoadTask(AdViewController adViewController, Map<String, String> paramsMap) {
            super(adViewController);
            this.mParamsMap = paramsMap;
        }

        void cleanup() {
            this.mParamsMap = null;
        }

        void execute() {
            AdViewController adViewController = (AdViewController) this.mWeakAdViewController.get();
            if (adViewController != null && !adViewController.isDestroyed()) {
                adViewController.setNotLoading();
                adViewController.getMoPubView().loadCustomEvent(this.mParamsMap);
            }
        }

        @Deprecated
        Map<String, String> getParamsMap() {
            return this.mParamsMap;
        }
    }

    @Deprecated
    static class LegacyCustomEventAdLoadTask extends AdLoadTask {
        private Header mHeader;

        public LegacyCustomEventAdLoadTask(AdViewController adViewController, Header header) {
            super(adViewController);
            this.mHeader = header;
        }

        void cleanup() {
            this.mHeader = null;
        }

        void execute() {
            AdViewController adViewController = (AdViewController) this.mWeakAdViewController.get();
            if (adViewController != null && !adViewController.isDestroyed()) {
                adViewController.setNotLoading();
                MoPubView mpv = adViewController.getMoPubView();
                if (this.mHeader == null) {
                    MoPubLog.i("Couldn't call custom method because the server did not specify one.");
                    mpv.loadFailUrl(MoPubErrorCode.ADAPTER_NOT_FOUND);
                } else {
                    String methodName = this.mHeader.getValue();
                    MoPubLog.i(new StringBuilder("Trying to call method named ").append(methodName).toString());
                    Activity userActivity = mpv.getActivity();
                    try {
                        userActivity.getClass().getMethod(methodName, new Class[]{MoPubView.class}).invoke(userActivity, new Object[]{mpv});
                    } catch (NoSuchMethodException e) {
                        MoPubLog.d(new StringBuilder("Couldn't perform custom method named ").append(methodName).append("(MoPubView view) because your activity class has no such method").toString());
                        mpv.loadFailUrl(MoPubErrorCode.ADAPTER_NOT_FOUND);
                    } catch (Exception e2) {
                        MoPubLog.d(new StringBuilder("Couldn't perform custom method named ").append(methodName).toString());
                        mpv.loadFailUrl(MoPubErrorCode.ADAPTER_NOT_FOUND);
                    }
                }
            }
        }

        @Deprecated
        Header getHeader() {
            return this.mHeader;
        }
    }

    AdLoadTask(AdViewController adViewController) {
        this.mWeakAdViewController = new WeakReference(adViewController);
    }

    static AdLoadTask fromHttpResponse(HttpResponse response, AdViewController adViewController) throws IOException {
        return new TaskExtractor(response, adViewController).extract();
    }

    abstract void cleanup();

    abstract void execute();
}