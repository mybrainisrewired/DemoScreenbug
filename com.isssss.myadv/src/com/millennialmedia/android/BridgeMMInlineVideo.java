package com.millennialmedia.android;

import java.util.Map;
import java.util.concurrent.Callable;

class BridgeMMInlineVideo extends MMJSObject {
    private static final String ADJUST_VIDEO = "adjustVideo";
    private static final String INSERT_VIDEO = "insertVideo";
    private static final String PAUSE_VIDEO = "pauseVideo";
    private static final String PLAY_VIDEO = "playVideo";
    private static final String REMOVE_VIDEO = "removeVideo";
    private static final String RESUME_VIDEO = "resumeVideo";
    private static final String SET_STREAM_VIDEO_SOURCE = "setStreamVideoSource";
    private static final String STOP_VIDEO = "stopVideo";

    class AnonymousClass_1 implements Callable<MMJSResponse> {
        final /* synthetic */ Map val$parameters;

        AnonymousClass_1(Map map) {
            this.val$parameters = map;
        }

        public MMJSResponse call() {
            MMWebView mmWebView = (MMWebView) BridgeMMInlineVideo.this.mmWebViewRef.get();
            if (mmWebView == null) {
                return MMJSResponse.responseWithError();
            }
            MMLayout mmLayout = mmWebView.getMMLayout();
            mmLayout.initInlineVideo(new InlineParams(this.val$parameters, mmWebView.getContext()));
            return MMJSResponse.responseWithSuccess("usingStreaming=" + mmLayout.isVideoPlayingStreaming());
        }
    }

    class AnonymousClass_4 implements Callable<MMJSResponse> {
        final /* synthetic */ Map val$parameters;

        AnonymousClass_4(Map map) {
            this.val$parameters = map;
        }

        public MMJSResponse call() {
            MMWebView webView = (MMWebView) BridgeMMInlineVideo.this.mmWebViewRef.get();
            return (webView == null || webView == null || !webView.getMMLayout().adjustVideo(new InlineParams(this.val$parameters, webView.getContext()))) ? MMJSResponse.responseWithError() : MMJSResponse.responseWithSuccess();
        }
    }

    class AnonymousClass_8 implements Callable<MMJSResponse> {
        final /* synthetic */ Map val$parameters;

        AnonymousClass_8(Map map) {
            this.val$parameters = map;
        }

        public MMJSResponse call() {
            MMWebView webView = (MMWebView) BridgeMMInlineVideo.this.mmWebViewRef.get();
            if (webView != null) {
                MMLayout mmLayout = webView.getMMLayout();
                String streamVideoURI = (String) this.val$parameters.get("streamVideoURI");
                if (!(mmLayout == null || streamVideoURI == null)) {
                    mmLayout.setVideoSource(streamVideoURI);
                    return MMJSResponse.responseWithSuccess();
                }
            }
            return MMJSResponse.responseWithError();
        }
    }

    BridgeMMInlineVideo() {
    }

    public MMJSResponse adjustVideo(Map<String, String> parameters) {
        return runOnUiThreadFuture(new AnonymousClass_4(parameters));
    }

    MMJSResponse executeCommand(String name, Map<String, String> arguments) {
        if (ADJUST_VIDEO.equals(name)) {
            return adjustVideo(arguments);
        }
        if (INSERT_VIDEO.equals(name)) {
            return insertVideo(arguments);
        }
        if (PAUSE_VIDEO.equals(name)) {
            return pauseVideo(arguments);
        }
        if (PLAY_VIDEO.equals(name)) {
            return playVideo(arguments);
        }
        if (REMOVE_VIDEO.equals(name)) {
            return removeVideo(arguments);
        }
        if (RESUME_VIDEO.equals(name)) {
            return resumeVideo(arguments);
        }
        if (SET_STREAM_VIDEO_SOURCE.equals(name)) {
            return setStreamVideoSource(arguments);
        }
        return STOP_VIDEO.equals(name) ? stopVideo(arguments) : null;
    }

    public MMJSResponse insertVideo(Map<String, String> parameters) {
        return runOnUiThreadFuture(new AnonymousClass_1(parameters));
    }

    public MMJSResponse pauseVideo(Map<String, String> parameters) {
        return runOnUiThreadFuture(new Callable<MMJSResponse>() {
            public MMJSResponse call() {
                MMWebView webView = (MMWebView) BridgeMMInlineVideo.this.mmWebViewRef.get();
                if (webView != null) {
                    MMLayout mmLayout = webView.getMMLayout();
                    if (mmLayout != null) {
                        mmLayout.pauseVideo();
                        return MMJSResponse.responseWithSuccess();
                    }
                }
                return MMJSResponse.responseWithError();
            }
        });
    }

    public MMJSResponse playVideo(Map<String, String> parameters) {
        return runOnUiThreadFuture(new Callable<MMJSResponse>() {
            public MMJSResponse call() {
                MMWebView webView = (MMWebView) BridgeMMInlineVideo.this.mmWebViewRef.get();
                if (webView != null) {
                    MMLayout mmLayout = webView.getMMLayout();
                    if (mmLayout != null) {
                        mmLayout.playVideo();
                        return MMJSResponse.responseWithSuccess();
                    }
                }
                return MMJSResponse.responseWithError();
            }
        });
    }

    public MMJSResponse removeVideo(Map<String, String> parameters) {
        return runOnUiThreadFuture(new Callable<MMJSResponse>() {
            public MMJSResponse call() {
                MMWebView webView = (MMWebView) BridgeMMInlineVideo.this.mmWebViewRef.get();
                if (webView != null) {
                    MMLayout mmLayout = webView.getMMLayout();
                    if (mmLayout != null) {
                        mmLayout.removeVideo();
                        return MMJSResponse.responseWithSuccess();
                    }
                }
                return MMJSResponse.responseWithError();
            }
        });
    }

    public MMJSResponse resumeVideo(Map<String, String> parameters) {
        return runOnUiThreadFuture(new Callable<MMJSResponse>() {
            public MMJSResponse call() {
                MMWebView webView = (MMWebView) BridgeMMInlineVideo.this.mmWebViewRef.get();
                if (webView != null) {
                    MMLayout mmLayout = webView.getMMLayout();
                    if (mmLayout != null) {
                        mmLayout.resumeVideo();
                        return MMJSResponse.responseWithSuccess();
                    }
                }
                return MMJSResponse.responseWithError();
            }
        });
    }

    public MMJSResponse setStreamVideoSource(Map<String, String> parameters) {
        return runOnUiThreadFuture(new AnonymousClass_8(parameters));
    }

    public MMJSResponse stopVideo(Map<String, String> parameters) {
        return runOnUiThreadFuture(new Callable<MMJSResponse>() {
            public MMJSResponse call() {
                MMWebView webView = (MMWebView) BridgeMMInlineVideo.this.mmWebViewRef.get();
                if (webView != null) {
                    MMLayout mmLayout = webView.getMMLayout();
                    if (mmLayout != null) {
                        mmLayout.stopVideo();
                        return MMJSResponse.responseWithSuccess();
                    }
                }
                return MMJSResponse.responseWithError();
            }
        });
    }
}