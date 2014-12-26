package com.millennialmedia.android;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.widget.RelativeLayout.LayoutParams;
import com.google.android.gms.location.LocationRequest;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

class MMAdImplController implements AdCacheTaskListener {
    static final long NO_ID_RETURNED = -4;
    private static final String TAG = "MMAdImplController";
    private static final Map<Long, MMAdImplController> saveableControllers;
    private static final Map<Long, WeakReference<MMAdImplController>> weakUnsaveableAdRef;
    volatile WeakReference<MMAdImpl> adImplRef;
    volatile long linkedAdImplId;
    RequestAdRunnable requestAdRunnable;
    volatile MMWebView webView;

    private class RequestAdRunnable implements Runnable {
        String adUrl;
        HttpMMHeaders mmHeaders;

        private RequestAdRunnable() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private boolean isAdUrlBuildable() {
            throw new UnsupportedOperationException("Method not decompiled: com.millennialmedia.android.MMAdImplController.RequestAdRunnable.isAdUrlBuildable():boolean");
            /*
            r13 = this;
            r8 = 1;
            r7 = 0;
            r13.adUrl = r7;
            r0 = 0;
            r7 = com.millennialmedia.android.MMAdImplController.this;
            r7 = r7.adImplRef;
            if (r7 == 0) goto L_0x0015;
        L_0x000b:
            r7 = com.millennialmedia.android.MMAdImplController.this;
            r7 = r7.adImplRef;
            r0 = r7.get();
            r0 = (com.millennialmedia.android.MMAdImpl) r0;
        L_0x0015:
            if (r0 == 0) goto L_0x00b3;
        L_0x0017:
            r6 = new java.util.TreeMap;	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r7 = java.lang.String.CASE_INSENSITIVE_ORDER;	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r6.<init>(r7);	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r0.insertUrlAdMetaValues(r6);	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r7 = r0.getContext();	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            com.millennialmedia.android.MMSDK.insertUrlCommonValues(r7, r6);	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r7 = "ua";
            r9 = r0.controller;	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r9 = r9.getUserAgent();	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r6.put(r7, r9);	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r1 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r7 = com.millennialmedia.android.HandShake.getAdUrl();	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r1.<init>(r7);	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r3 = r6.entrySet();	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r7 = "MMAdImplController";
            r9 = r3.toString();	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            com.millennialmedia.android.MMLog.d(r7, r9);	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r7 = r6.entrySet();	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r5 = r7.iterator();	 Catch:{ UnsupportedEncodingException -> 0x0080 }
        L_0x0051:
            r7 = r5.hasNext();	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            if (r7 == 0) goto L_0x008b;
        L_0x0057:
            r4 = r5.next();	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r4 = (java.util.Map.Entry) r4;	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r9 = "%s=%s&";
            r7 = 2;
            r10 = new java.lang.Object[r7];	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r7 = 0;
            r11 = r4.getKey();	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r10[r7] = r11;	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r11 = 1;
            r7 = r4.getValue();	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r7 = (java.lang.String) r7;	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r12 = "UTF-8";
            r7 = java.net.URLEncoder.encode(r7, r12);	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r10[r11] = r7;	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r7 = java.lang.String.format(r9, r10);	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r1.append(r7);	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            goto L_0x0051;
        L_0x0080:
            r2 = move-exception;
            r7 = new com.millennialmedia.android.MMException;
            r7.<init>(r2);
            r7 = r13.failWithErrorMessage(r7);
        L_0x008a:
            return r7;
        L_0x008b:
            r7 = r1.length();	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r7 = r7 + -1;
            r9 = r1.length();	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r1.delete(r7, r9);	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r7 = r1.toString();	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r13.adUrl = r7;	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r7 = "MMAdImplController";
            r9 = "Calling for an advertisement: %s";
            r10 = 1;
            r10 = new java.lang.Object[r10];	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r11 = 0;
            r12 = r13.adUrl;	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r10[r11] = r12;	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r9 = java.lang.String.format(r9, r10);	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            com.millennialmedia.android.MMLog.d(r7, r9);	 Catch:{ UnsupportedEncodingException -> 0x0080 }
        L_0x00b1:
            r7 = r8;
            goto L_0x008a;
        L_0x00b3:
            r7 = new com.millennialmedia.android.MMException;	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r9 = 25;
            r7.<init>(r9);	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            r13.failWithInfoMessage(r7);	 Catch:{ UnsupportedEncodingException -> 0x0080 }
            goto L_0x00b1;
            */
        }

        private boolean isHandledHtmlResponse(HttpEntity httpEntity) {
            MMAdImpl adImpl = null;
            try {
                if (MMAdImplController.this.adImplRef != null) {
                    adImpl = (MMAdImpl) MMAdImplController.this.adImplRef.get();
                }
                if (adImpl != null) {
                    if (adImpl.isBanner()) {
                        if (adImpl.controller != null) {
                            adImpl.controller.setLastHeaders(this.mmHeaders);
                            adImpl.controller.setWebViewContent(HttpGetRequest.convertStreamToString(httpEntity.getContent()), this.adUrl);
                        }
                        Event.requestCompleted(adImpl);
                    } else {
                        InterstitialAd interstitialAd = new InterstitialAd();
                        interstitialAd.content = HttpGetRequest.convertStreamToString(httpEntity.getContent());
                        interstitialAd.setId(adImpl.adType);
                        interstitialAd.adUrl = this.adUrl;
                        interstitialAd.mmHeaders = this.mmHeaders;
                        if (MMSDK.logLevel >= 5) {
                            MMLog.v(TAG, String.format("Received interstitial ad with url %s.", new Object[]{interstitialAd.adUrl}));
                            MMLog.v(TAG, interstitialAd.content);
                        }
                        AdCache.save(adImpl.getContext(), interstitialAd);
                        AdCache.setNextCachedAd(adImpl.getContext(), adImpl.getCachedName(), interstitialAd.getId());
                        Event.fetchStartedCaching(adImpl);
                        Event.requestCompleted(adImpl);
                    }
                }
                return true;
            } catch (IOException e) {
                Exception ioe = e;
                return failWithErrorMessage(new MMException("Exception raised in HTTP stream: " + ioe, ioe));
            }
        }

        private boolean isHandledJsonResponse(HttpEntity httpEntity) {
            MMAdImpl adImpl = null;
            if (MMAdImplController.this.adImplRef != null) {
                adImpl = MMAdImplController.this.adImplRef.get();
            }
            if (adImpl != null && adImpl.isBanner()) {
                return failWithErrorMessage(new MMException("Millennial ad return unsupported format.", 15));
            }
            try {
                VideoAd videoAd = (VideoAd) CachedAd.parseJSON(HttpGetRequest.convertStreamToString(httpEntity.getContent()));
                if (videoAd != null && videoAd.isValid()) {
                    MMLog.i(TAG, "Cached video ad JSON received: " + videoAd.getId());
                    if (videoAd.isExpired()) {
                        MMLog.i(TAG, "New ad has expiration date in the past. Not downloading ad content.");
                        videoAd.delete(adImpl.getContext());
                        Event.requestFailed(adImpl, new MMException(15));
                    } else if (AdCache.loadNextCachedAd(adImpl.getContext(), adImpl.getCachedName()) != null) {
                        MMLog.i(TAG, "Previously fetched ad exists in the playback queue. Not downloading ad content.");
                        videoAd.delete(adImpl.getContext());
                        Event.requestFailed(adImpl, new MMException(17));
                    } else {
                        AdCache.save(adImpl.getContext(), videoAd);
                        if (videoAd.isOnDisk(adImpl.getContext())) {
                            MMLog.d(TAG, "Cached ad is valid. Moving it to the front of the queue.");
                            AdCache.setNextCachedAd(adImpl.getContext(), adImpl.getCachedName(), videoAd.getId());
                            Event.fetchStartedCaching(adImpl);
                            Event.requestCompleted(adImpl);
                        } else {
                            Event.logEvent(videoAd.cacheMissURL);
                            MMLog.d(TAG, "Downloading ad...");
                            Event.fetchStartedCaching(adImpl);
                            videoAd.downloadPriority = 3;
                            AdCache.startDownloadTask(adImpl.getContext(), adImpl.getCachedName(), videoAd, adImpl.controller);
                        }
                    }
                }
            } catch (IllegalStateException e) {
                Exception illegalE = e;
                illegalE.printStackTrace();
                return failWithInfoMessage(new MMException("Millennial ad return failed. Invalid response data.", illegalE));
            } catch (IOException e2) {
                Exception ioe = e2;
                ioe.printStackTrace();
                return failWithInfoMessage(new MMException("Millennial ad return failed. " + ioe, ioe));
            }
            return true;
        }

        private boolean isHandledResponse(HttpResponse httpResponse) {
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity == null) {
                failWithInfoMessage(new MMException("Null HTTP entity", 14));
                return false;
            } else if (httpEntity.getContentLength() == 0) {
                failWithInfoMessage(new MMException("Millennial ad return failed. Zero content length returned.", 14));
                return false;
            } else {
                saveMacId(httpResponse);
                Header httpHeader = httpEntity.getContentType();
                if (httpHeader == null || httpHeader.getValue() == null) {
                    failWithInfoMessage(new MMException("Millennial ad return failed. HTTP Header value null.", 15));
                    return false;
                } else {
                    if (httpHeader.getValue().toLowerCase().startsWith("application/json")) {
                        isHandledJsonResponse(httpEntity);
                    } else if (httpHeader.getValue().toLowerCase().startsWith("text/html")) {
                        Header xHeader = httpResponse.getFirstHeader("X-MM-Video");
                        this.mmHeaders = new HttpMMHeaders(httpResponse.getAllHeaders());
                        if (xHeader != null && xHeader.getValue().equalsIgnoreCase("true")) {
                            MMAdImpl adImpl = null;
                            if (MMAdImplController.this.adImplRef != null) {
                                adImpl = MMAdImplController.this.adImplRef.get();
                            }
                            if (adImpl != null) {
                                Context context = adImpl.getContext();
                                HandShake.sharedHandShake(context).updateLastVideoViewedTime(context, adImpl.adType);
                            }
                        }
                        isHandledHtmlResponse(httpEntity);
                    } else {
                        failWithInfoMessage(new MMException("Millennial ad return failed. Invalid (JSON/HTML expected) mime type returned.", 15));
                        return false;
                    }
                    return true;
                }
            }
        }

        private void saveMacId(HttpResponse httpResponse) {
            Header[] arr$ = httpResponse.getHeaders("Set-Cookie");
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                String value = arr$[i$].getValue();
                int index = value.indexOf("MAC-ID=");
                if (index >= 0) {
                    int endIndex = value.indexOf(59, index);
                    if (endIndex > index) {
                        MMSDK.macId = value.substring(index + 7, endIndex);
                    }
                }
                i$++;
            }
        }

        boolean fail(MMException mmError) {
            MMAdImpl adImpl = null;
            if (MMAdImplController.this.adImplRef != null) {
                adImpl = MMAdImplController.this.adImplRef.get();
            }
            Event.requestFailed(adImpl, mmError);
            return false;
        }

        boolean failWithErrorMessage(MMException mmError) {
            MMLog.e(TAG, mmError.getMessage());
            return fail(mmError);
        }

        boolean failWithInfoMessage(MMException mmError) {
            MMLog.i(TAG, mmError.getMessage());
            return fail(mmError);
        }

        public void run() {
            RequestAdRunnable requestAdRunnable = null;
            try {
                if (MMAdImplController.this.adImplRef != null) {
                    MMAdImpl adImpl = (MMAdImpl) MMAdImplController.this.adImplRef.get();
                    if (adImpl == null || !MMSDK.isConnected(adImpl.getContext())) {
                        failWithInfoMessage(new MMException("No network available, can't call for ads.", 11));
                        MMAdImplController.this.requestAdRunnable = requestAdRunnable;
                        return;
                    } else if (isAdUrlBuildable()) {
                        try {
                            HttpResponse httpResponse = new HttpGetRequest().get(this.adUrl);
                            if (httpResponse == null) {
                                failWithErrorMessage(new MMException("HTTP response is null.", 14));
                                MMAdImplController.this.requestAdRunnable = requestAdRunnable;
                                return;
                            } else if (!isHandledResponse(httpResponse)) {
                                MMAdImplController.this.requestAdRunnable = requestAdRunnable;
                                return;
                            }
                        } catch (Exception e) {
                            failWithErrorMessage(new MMException("Ad request HTTP error. " + e.getMessage(), 14));
                            MMAdImplController.this.requestAdRunnable = requestAdRunnable;
                        }
                    } else {
                        MMAdImplController.this.requestAdRunnable = requestAdRunnable;
                        return;
                    }
                }
                MMAdImplController.this.requestAdRunnable = requestAdRunnable;
            } catch (Exception e2) {
                failWithInfoMessage(new MMException("Request not filled, can't call for ads.", 14));
                MMAdImplController.this.requestAdRunnable = requestAdRunnable;
            }
        }
    }

    static {
        saveableControllers = new ConcurrentHashMap();
        weakUnsaveableAdRef = new ConcurrentHashMap();
    }

    private MMAdImplController(MMAdImpl adImpl) {
        MMLog.d(TAG, "**************** creating new controller.");
        this.adImplRef = new WeakReference(adImpl);
        if (adImpl.linkForExpansionId != 0) {
            linkForExpansion(adImpl);
            this.webView = getWebViewFromExistingAdImpl(adImpl);
        } else if (!(adImpl instanceof MMInterstitialAdImpl)) {
            if (adImpl.isBanner()) {
                this.webView = new MMWebView(adImpl.getContext().getApplicationContext(), adImpl.internalId);
                this.webView.requiresPreAdSizeFix = true;
            } else {
                this.webView = new MMWebView(adImpl.getContext(), adImpl.internalId);
            }
        }
    }

    static synchronized void assignAdViewController(MMAdImpl adImpl) {
        synchronized (MMAdImplController.class) {
            if (adImpl.controller != null) {
                if (!saveableControllers.containsValue(adImpl.controller)) {
                    if (adImpl.isLifecycleObservable()) {
                        saveableControllers.put(Long.valueOf(adImpl.internalId), adImpl.controller);
                        if (weakUnsaveableAdRef.containsKey(Long.valueOf(adImpl.internalId))) {
                            weakUnsaveableAdRef.remove(Long.valueOf(adImpl.internalId));
                        }
                    } else if (!weakUnsaveableAdRef.containsKey(Long.valueOf(adImpl.internalId))) {
                        weakUnsaveableAdRef.put(Long.valueOf(adImpl.internalId), new WeakReference(adImpl.controller));
                    }
                }
                MMLog.d(TAG, adImpl + " - Has a controller");
            } else {
                MMLog.d(TAG, "*****************************************assignAdViewController for " + adImpl);
                MMAdImplController controller = (MMAdImplController) saveableControllers.get(Long.valueOf(adImpl.internalId));
                if (controller == null) {
                    WeakReference<MMAdImplController> controllerRef = (WeakReference) weakUnsaveableAdRef.get(Long.valueOf(adImpl.internalId));
                    if (controllerRef != null) {
                        controller = controllerRef.get();
                    }
                    if (controller == null) {
                        controller = new MMAdImplController(adImpl);
                        if (adImpl.isLifecycleObservable()) {
                            saveableControllers.put(Long.valueOf(adImpl.internalId), controller);
                        } else {
                            weakUnsaveableAdRef.put(Long.valueOf(adImpl.internalId), new WeakReference(controller));
                        }
                    }
                }
                adImpl.controller = controller;
                controller.adImplRef = new WeakReference(adImpl);
                if (!(controller.webView == null || adImpl instanceof MMInterstitialAdImpl)) {
                    setupWebView(adImpl);
                }
            }
        }
    }

    static synchronized boolean attachWebViewFromOverlay(MMAdImpl overlayAdImpl) {
        boolean z = false;
        synchronized (MMAdImplController.class) {
            if (overlayAdImpl != null) {
                MMLog.d(TAG, "attachWebViewFromOverlay with " + overlayAdImpl);
                if (!(overlayAdImpl.controller == null || overlayAdImpl.controller.webView == null)) {
                    overlayAdImpl.controller.webView.resetSpeechKit();
                }
                MMAdImpl bannerAdImpl = getAdImplWithId(overlayAdImpl.linkForExpansionId);
                if (!(bannerAdImpl == null || bannerAdImpl.controller == null)) {
                    if (bannerAdImpl.controller.webView == null && !(overlayAdImpl.controller == null || overlayAdImpl.controller.webView == null)) {
                        bannerAdImpl.controller.webView = overlayAdImpl.controller.webView;
                        overlayAdImpl.removeView(overlayAdImpl.controller.webView);
                        overlayAdImpl.controller.webView = null;
                        bannerAdImpl.controller.webView.setMraidDefault();
                        bannerAdImpl.controller.webView.setWebViewClient(bannerAdImpl.getMMWebViewClient());
                        z = true;
                    }
                }
            }
        }
        return z;
    }

    static String controllersToString() {
        return weakUnsaveableAdRef.toString() + " SAVED:" + saveableControllers.toString();
    }

    static void destroyOtherInlineVideo(Context context) {
        Iterator i$ = saveableControllers.entrySet().iterator();
        while (i$.hasNext()) {
            MMAdImplController controller = (MMAdImplController) ((Entry) i$.next()).getValue();
            if (controller != null) {
                MMAdImpl adImpl = (MMAdImpl) controller.adImplRef.get();
                if (adImpl != null) {
                    MMAd ad = adImpl.getCallingAd();
                    if (ad != null && ad instanceof MMLayout) {
                        ((MMLayout) ad).removeVideo();
                    }
                }
            }
        }
    }

    static synchronized MMAdImpl getAdImplWithId(long internalId) {
        MMAdImpl mMAdImpl = null;
        synchronized (MMAdImplController.class) {
            if (internalId != -4) {
                MMAdImplController controller = (MMAdImplController) saveableControllers.get(Long.valueOf(internalId));
                if (controller == null) {
                    WeakReference<MMAdImplController> controllerRef = (WeakReference) weakUnsaveableAdRef.get(Long.valueOf(internalId));
                    if (controllerRef != null) {
                        controller = controllerRef.get();
                    }
                }
                if (controller != null) {
                    mMAdImpl = (MMAdImpl) controller.adImplRef.get();
                }
            }
        }
        return mMAdImpl;
    }

    static synchronized MMWebView getWebViewFromExistingAdImpl(MMAdImpl requestorAdImpl) {
        MMWebView mmWebView;
        synchronized (MMAdImplController.class) {
            MMLog.i(TAG, "getWebViewFromExistingLayout(" + requestorAdImpl.internalId + " taking from " + requestorAdImpl.linkForExpansionId + ")");
            mmWebView = null;
            MMAdImpl holderAdImpl = getAdImplWithId(requestorAdImpl.linkForExpansionId);
            if (!(holderAdImpl == null || holderAdImpl.controller == null)) {
                mmWebView = holderAdImpl.controller.webView;
                holderAdImpl.controller.webView = null;
            }
        }
        return mmWebView;
    }

    private synchronized boolean isDownloadingCachedAd(MMAdImpl adImpl) {
        boolean z = true;
        synchronized (this) {
            Context context = adImpl.getContext();
            if (HandShake.sharedHandShake(context).isAdTypeDownloading(adImpl.adType)) {
                MMLog.i(TAG, "There is a download in progress. Defering call for new ad");
                Event.requestFailed(adImpl, new MMException(12));
            } else {
                MMLog.d(TAG, "No download in progress.");
                CachedAd incompleteAd = AdCache.loadIncompleteDownload(context, adImpl.getCachedName());
                if (incompleteAd != null) {
                    MMLog.i(TAG, "Last ad wasn't fully downloaded. Download again.");
                    Event.fetchStartedCaching(adImpl);
                    AdCache.startDownloadTask(context, adImpl.getCachedName(), incompleteAd, this);
                } else {
                    MMLog.i(TAG, "No incomplete downloads.");
                    z = false;
                }
            }
        }
        return z;
    }

    static synchronized void removeAdViewController(MMAdImpl adImpl) {
        synchronized (MMAdImplController.class) {
            if (adImpl.controller != null) {
                if (adImpl.isLifecycleObservable()) {
                    saveableControllers.put(Long.valueOf(adImpl.internalId), adImpl.controller);
                    if (weakUnsaveableAdRef.get(Long.valueOf(adImpl.internalId)) != null) {
                        weakUnsaveableAdRef.remove(Long.valueOf(adImpl.internalId));
                    }
                } else {
                    weakUnsaveableAdRef.put(Long.valueOf(adImpl.internalId), new WeakReference(adImpl.controller));
                }
                MMLog.d(TAG, "****************RemoveAdviewcontroller - " + adImpl);
                if (adImpl.isFinishing) {
                    saveableControllers.remove(Long.valueOf(adImpl.internalId));
                    weakUnsaveableAdRef.remove(Long.valueOf(adImpl.internalId));
                }
                MMAdImplController controller = adImpl.controller;
                adImpl.controller = null;
                MMLog.d(TAG, "****************RemoveAdviewcontroller - controllers " + controllersToString());
                if (controller.webView != null) {
                    MMLog.d(TAG, "****************RemoveAdviewcontroller - controller!=null, expanding=" + controller.webView.isExpanding);
                    adImpl.removeView(controller.webView);
                    controller.webView.isExpanding = false;
                    if (adImpl.isFinishing && adImpl.linkForExpansionId == 0) {
                        controller.webView.loadData("<html></html>", "text/html", "UTF-8");
                        controller.webView.resetSpeechKit();
                        controller.webView = null;
                    }
                }
            }
        }
    }

    private void requestAdInternal(MMAdImpl adImpl) {
        if (adImpl.apid == null) {
            MMException error = new MMException("MMAdView found with a null apid. New ad requests on this MMAdView are disabled until an apid has been assigned.", 1);
            MMLog.e(TAG, error.getMessage());
            Event.requestFailed(adImpl, error);
        } else if (adImpl.isBanner() || !isDownloadingCachedAd(adImpl)) {
            synchronized (this) {
                if (this.requestAdRunnable != null) {
                    MMLog.i(TAG, MMException.getErrorCodeMessage(ApiEventType.API_MRAID_RESIZE));
                    Event.requestFailed(adImpl, new MMException(12));
                } else {
                    this.requestAdRunnable = new RequestAdRunnable(null);
                    ThreadUtils.execute(this.requestAdRunnable);
                }
            }
        }
    }

    private static synchronized void setupWebView(MMAdImpl adImpl) {
        synchronized (MMAdImplController.class) {
            MMAdImplController controller = adImpl.controller;
            if (controller.webView != null) {
                controller.webView.setWebViewClient(adImpl.getMMWebViewClient());
                if (!controller.webView.isCurrentParent(adImpl.internalId)) {
                    LayoutParams layoutParams;
                    if (adImpl.isBanner()) {
                        layoutParams = new LayoutParams(-2, -2);
                        if (controller.webView.isMraidResized()) {
                            controller.webView.unresizeToDefault(adImpl);
                        }
                    } else {
                        layoutParams = new LayoutParams(-2, -1);
                    }
                    controller.webView.removeFromParent();
                    adImpl.addView(controller.webView, layoutParams);
                }
            }
        }
    }

    int checkReason(MMAdImpl adImpl, CachedAd ad) {
        if (ad.isExpired()) {
            MMLog.d(TAG, String.format("%s is expired.", new Object[]{ad.getId()}));
            return ApiEventType.API_MRAID_POST_TO_SOCIAL;
        } else if (!ad.isOnDisk(adImpl.getContext())) {
            MMLog.d(TAG, String.format("%s is not on disk.", new Object[]{ad.getId()}));
            return ApiEventType.API_MRAID_SUPPORTS;
        } else if (HandShake.sharedHandShake(adImpl.getContext()).canDisplayCachedAd(adImpl.adType, ad.deferredViewStart)) {
            return LocationRequest.PRIORITY_HIGH_ACCURACY;
        } else {
            MMLog.d(TAG, String.format("%s cannot be shown at this time.", new Object[]{ad.getId()}));
            return ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE;
        }
    }

    int display(MMAdImpl adImpl) {
        CachedAd ad = AdCache.loadNextCachedAd(adImpl.getContext(), adImpl.getCachedName());
        if (ad == null) {
            return ApiEventType.API_MRAID_GET_MAX_SIZE;
        }
        if (!ad.canShow(adImpl.getContext(), adImpl, true)) {
            return checkReason(adImpl, ad);
        }
        Event.displayStarted(adImpl);
        AdCache.setNextCachedAd(adImpl.getContext(), adImpl.getCachedName(), null);
        ad.show(adImpl.getContext(), adImpl.internalId);
        HandShake.sharedHandShake(adImpl.getContext()).updateLastVideoViewedTime(adImpl.getContext(), adImpl.adType);
        return 0;
    }

    public void downloadCompleted(CachedAd ad, boolean success) {
        MMAdImpl adImpl = (MMAdImpl) this.adImplRef.get();
        if (adImpl == null) {
            MMLog.e(TAG, MMException.getErrorCodeMessage(ApiEventType.API_MRAID_GET_GALLERY_IMAGE));
        } else {
            if (success) {
                AdCache.setNextCachedAd(adImpl.getContext(), adImpl.getCachedName(), ad.getId());
            }
            if (success) {
                Event.requestCompleted(adImpl);
            } else {
                Event.requestFailed(adImpl, new MMException(15));
            }
        }
    }

    public void downloadStart(CachedAd ad) {
    }

    public String getDefaultUserAgentString(Context context) {
        return System.getProperty("http.agent");
    }

    HttpMMHeaders getLastHeaders() {
        return this.webView == null ? null : this.webView.getLastHeaders();
    }

    String getUserAgent() {
        String userAgent = null;
        MMAdImpl adImpl = (MMAdImpl) this.adImplRef.get();
        if (adImpl != null) {
            Context context = adImpl.getContext();
            if (context != null) {
                userAgent = getDefaultUserAgentString(context);
            }
        }
        return TextUtils.isEmpty(userAgent) ? Build.MODEL : userAgent;
    }

    int isAdAvailable(MMAdImpl adImpl) {
        CachedAd ad = AdCache.loadNextCachedAd(adImpl.getContext(), adImpl.getCachedName());
        if (ad != null) {
            return ad.canShow(adImpl.getContext(), adImpl, true) ? 0 : checkReason(adImpl, ad);
        } else {
            MMLog.i(TAG, "No next ad.");
            return ApiEventType.API_MRAID_GET_MAX_SIZE;
        }
    }

    void linkForExpansion(MMAdImpl expansionAdImpl) {
        MMAdImpl adImpl = getAdImplWithId(expansionAdImpl.linkForExpansionId);
        if (adImpl != null) {
            this.linkedAdImplId = expansionAdImpl.linkForExpansionId;
            adImpl.controller.linkedAdImplId = expansionAdImpl.internalId;
            adImpl.linkForExpansionId = expansionAdImpl.internalId;
        }
    }

    void loadUrl(String url) {
        if (!TextUtils.isEmpty(url) && this.webView != null) {
            this.webView.loadUrl(url);
        }
    }

    void loadWebContent(String content, String adUrl) {
        MMAdImpl adImpl = (MMAdImpl) this.adImplRef.get();
        if (adImpl != null && this.webView != null) {
            this.webView.setWebViewContent(content, adUrl, adImpl);
        }
    }

    void requestAd() {
        MMAdImpl adImpl = (MMAdImpl) this.adImplRef.get();
        if (adImpl == null) {
            MMLog.e(TAG, MMException.getErrorCodeMessage(ApiEventType.API_MRAID_GET_GALLERY_IMAGE));
            Event.requestFailed(adImpl, new MMException(25));
        } else if (!adImpl.isRefreshable()) {
            Event.requestFailed(adImpl, new MMException(16));
        } else if (!MMSDK.isUiThread()) {
            MMLog.e(TAG, MMException.getErrorCodeMessage(MMAdView.TRANSITION_DOWN));
            Event.requestFailed(adImpl, new MMException(3));
        } else if (HandShake.sharedHandShake(adImpl.getContext()).kill) {
            MMLog.i(TAG, "The server is no longer allowing ads.");
            Event.requestFailed(adImpl, new MMException(16));
        } else {
            try {
                MMLog.d(TAG, "adLayout - requestAd");
                requestAdInternal(adImpl);
            } catch (Exception e) {
                Exception e2 = e;
                MMLog.e(TAG, "There was an exception with the ad request. ", e2);
                e2.printStackTrace();
            }
        }
    }

    void setLastHeaders(HttpMMHeaders lastHeaders) {
        if (this.webView != null) {
            this.webView.setLastHeaders(lastHeaders);
        }
    }

    void setWebViewContent(String webContent, String url) {
        if (this.webView != null) {
            this.webView.setWebViewContent(webContent, url, (MMAdImpl) this.adImplRef.get());
        }
    }

    public String toString() {
        MMAdImpl adImpl = (MMAdImpl) this.adImplRef.get();
        StringBuilder sb = new StringBuilder();
        if (adImpl != null) {
            sb.append(adImpl + "-LinkInC=" + this.linkedAdImplId);
        }
        return sb.toString() + " w/" + this.webView;
    }

    void unresizeToDefault() {
        if (this.webView != null) {
            this.webView.unresizeToDefault((MMAdImpl) this.adImplRef.get());
        }
    }
}