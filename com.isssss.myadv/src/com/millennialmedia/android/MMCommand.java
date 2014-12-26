package com.millennialmedia.android;

import android.net.Uri;
import android.text.TextUtils;
import com.mopub.common.Preconditions;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

class MMCommand implements Runnable {
    private static final String MM_BANNER = "MMBanner";
    private static final String MM_CACHED_VIDEO = "MMCachedVideo";
    private static final String MM_CALENDAR = "MMCalendar";
    private static final String MM_DEVICE = "MMDevice";
    private static final String MM_INLINE_VIDEO = "MMInlineVideo";
    private static final String MM_INTERSTITIAL = "MMInterstitial";
    private static final String MM_MEDIA = "MMMedia";
    private static final String MM_NOTIFICATION = "MMNotification";
    private static final String MM_SPEECHKIT = "MMSpeechkit";
    private static final String TAG = "MMCommand";
    private Map<String, String> arguments;
    private String bridgeService;
    private String callback;
    private String serviceMethod;
    private WeakReference<MMWebView> webViewRef;

    class AnonymousClass_1 implements Runnable {
        final /* synthetic */ MMJSResponse val$resp;
        final /* synthetic */ MMWebView val$webViewCallback;

        AnonymousClass_1(MMWebView mMWebView, MMJSResponse mMJSResponse) {
            this.val$webViewCallback = mMWebView;
            this.val$resp = mMJSResponse;
        }

        public void run() {
            if (MMCommand.this.serviceMethod.equals("expandWithProperties")) {
                this.val$webViewCallback.isExpanding = true;
            }
            this.val$webViewCallback.loadUrl(String.format("javascript:%s(%s);", new Object[]{MMCommand.this.callback, this.val$resp.toJSONString()}));
        }
    }

    static {
        ComponentRegistry.addBannerBridge(new BridgeMMBanner());
        ComponentRegistry.addCachedVideoBridge(new BridgeMMCachedVideo());
        ComponentRegistry.addCalendarBridge(new BridgeMMCalendar());
        ComponentRegistry.addDeviceBridge(new BridgeMMDevice());
        ComponentRegistry.addInlineVideoBridge(new BridgeMMInlineVideo());
        ComponentRegistry.addInterstitialBridge(new BridgeMMInterstitial());
        ComponentRegistry.addMediaBridge(new BridgeMMMedia());
        ComponentRegistry.addNotificationBridge(new BridgeMMNotification());
        ComponentRegistry.addSpeechkitBridge(new BridgeMMSpeechkit());
    }

    MMCommand(MMWebView webView, String uriString) {
        this.webViewRef = new WeakReference(webView);
        try {
            String[] components = Uri.parse(uriString).getHost().split("\\.");
            if (components.length == 2) {
                this.bridgeService = components[0];
                this.serviceMethod = components[1];
                this.arguments = new HashMap();
                String[] arr$ = uriString.substring(uriString.indexOf(63) + 1).split("&");
                int len$ = arr$.length;
                int i$ = 0;
                while (i$ < len$) {
                    String[] subComponents = arr$[i$].split("=");
                    if (subComponents.length >= 2) {
                        this.arguments.put(Uri.decode(subComponents[0]), Uri.decode(subComponents[1]));
                        if (subComponents[0].equalsIgnoreCase("callback")) {
                            this.callback = Uri.decode(subComponents[1]);
                        }
                    }
                    i$++;
                }
            }
        } catch (Exception e) {
            Exception e2 = e;
            MMLog.e(TAG, String.format("Exception while executing javascript call %s ", new Object[]{uriString}), e2);
            e2.printStackTrace();
        }
    }

    private MMJSObject getBridgeService(String bridgeName) {
        if (bridgeName == null) {
            return null;
        }
        if (MM_BANNER.equals(bridgeName)) {
            return ComponentRegistry.getBannerBridge();
        }
        if (MM_CACHED_VIDEO.equals(bridgeName)) {
            return ComponentRegistry.getCachedVideoBridge();
        }
        if (MM_CALENDAR.equals(bridgeName)) {
            return ComponentRegistry.getCalendarBridge();
        }
        if (MM_DEVICE.equals(bridgeName)) {
            return ComponentRegistry.getDeviceBridge();
        }
        if (MM_INLINE_VIDEO.equals(bridgeName)) {
            return ComponentRegistry.getInlineVideoBridge();
        }
        if (MM_INTERSTITIAL.equals(bridgeName)) {
            return ComponentRegistry.getInterstitialBridge();
        }
        if (MM_MEDIA.equals(bridgeName)) {
            return ComponentRegistry.getMediaBridge();
        }
        if (MM_NOTIFICATION.equals(bridgeName)) {
            return ComponentRegistry.getNotificationBridge();
        }
        return MM_SPEECHKIT.equals(bridgeName) ? ComponentRegistry.getSpeechkitBridge() : null;
    }

    private String getBridgeStrippedClassName() {
        return this.bridgeService.replaceFirst("Bridge", Preconditions.EMPTY_ARGUMENTS);
    }

    boolean isResizeCommand() {
        return this.serviceMethod != null ? "resize".equals(this.serviceMethod) : false;
    }

    public void run() {
        MMWebView webViewCallback;
        MMJSResponse response = null;
        try {
            if (this.bridgeService == null || this.serviceMethod == null) {
                response = MMJSResponse.responseWithError("The service or service method was not defined.");
            } else {
                try {
                    MMWebView webView = (MMWebView) this.webViewRef.get();
                    if (webView != null) {
                        MMJSObject service = getBridgeService(this.bridgeService);
                        if (service != null) {
                            service.setContext(webView.getContext());
                            service.setMMWebView(webView);
                            webView.updateArgumentsWithSettings(this.arguments);
                            response = service.executeCommand(this.serviceMethod, this.arguments);
                        } else {
                            response = MMJSResponse.responseWithError("Service: " + this.bridgeService + " does not exist.");
                        }
                    }
                } catch (Exception e) {
                    Exception e2 = e;
                    MMLog.e(TAG, "Exception while executing javascript call " + this.serviceMethod, e2);
                    response = MMJSResponse.responseWithError("Unexpected exception, " + e2.getClass().getName() + " received.");
                }
            }
            if (!TextUtils.isEmpty(this.callback)) {
                webViewCallback = (MMWebView) this.webViewRef.get();
                if (webViewCallback != null) {
                    if (response == null) {
                        response = MMJSResponse.responseWithError(this.serviceMethod);
                    }
                    if (response.methodName == null) {
                        response.methodName = this.serviceMethod;
                    }
                    if (response.className == null) {
                        response.className = getBridgeStrippedClassName();
                    }
                    MMSDK.runOnUiThread(new AnonymousClass_1(webViewCallback, response));
                }
            }
        } catch (Throwable th) {
            if (!TextUtils.isEmpty(this.callback)) {
                webViewCallback = this.webViewRef.get();
                if (webViewCallback != null) {
                    if (response == null) {
                        response = MMJSResponse.responseWithError(this.serviceMethod);
                    }
                    if (response.methodName == null) {
                        response.methodName = this.serviceMethod;
                    }
                    if (response.className == null) {
                        response.className = getBridgeStrippedClassName();
                    }
                    MMSDK.runOnUiThread(new AnonymousClass_1(webViewCallback, response));
                }
            }
        }
    }
}