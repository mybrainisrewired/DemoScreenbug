package com.millennialmedia.android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.lang.ref.WeakReference;

abstract class MMLayout extends RelativeLayout implements MMAd, TransparentFix {
    static final String BOTTOM_CENTER = "bottom-center";
    static final String BOTTOM_LEFT = "bottom-left";
    static final String BOTTOM_RIGHT = "bottom-right";
    static final String CENTER = "center";
    private static final int CLOSE_AREA_SIZE = 50;
    public static final String KEY_HEIGHT = "height";
    public static final String KEY_WIDTH = "width";
    private static final String TAG = "MMLayout";
    static final String TOP_CENTER = "top-center";
    static final String TOP_LEFT = "top-left";
    static final String TOP_RIGHT = "top-right";
    private static boolean appInit;
    MMAdImpl adImpl;
    View blackView;
    View closeAreaView;
    private GestureDetector diagnosticDetector;
    String goalId;
    RelativeLayout inlineVideoLayout;
    InlineVideoView inlineVideoView;
    boolean isResizing;

    class AnonymousClass_1 implements Runnable {
        final /* synthetic */ String val$position;

        AnonymousClass_1(String str) {
            this.val$position = str;
        }

        public void run() {
            MMLayout.this.internalSetCloseArea(this.val$position);
        }
    }

    class AnonymousClass_3 implements Runnable {
        final /* synthetic */ InlineParams val$params;

        AnonymousClass_3(InlineParams inlineParams) {
            this.val$params = inlineParams;
        }

        public void run() {
            if (MMLayout.this.inlineVideoView != null) {
                MMLayout.this.inlineVideoView.adjustVideo(this.val$params);
            }
        }
    }

    private static class LayoutGestureListener extends SimpleOnGestureListener {
        WeakReference<MMLayout> layoutRef;

        public LayoutGestureListener(MMLayout layout) {
            this.layoutRef = new WeakReference(layout);
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1 == null || e2 == null || Math.abs((int) (e2.getX() - e1.getX())) <= 200 || Math.abs(velocityX) <= Math.abs(velocityY)) {
                return false;
            }
            if (velocityX <= 0.0f) {
                MMLayout layout = (MMLayout) this.layoutRef.get();
                if (layout != null) {
                    MMSDK.printDiagnostics(layout.adImpl);
                }
            } else if (MMSDK.logLevel == 0) {
                MMLog.i(TAG, "Enabling debug and verbose logging.");
                MMSDK.logLevel = 3;
            } else {
                MMLog.i(TAG, "Disabling debug and verbose logging.");
                MMSDK.logLevel = 0;
            }
            return true;
        }
    }

    class MMLayoutMMAdImpl extends MMAdImpl {
        public MMLayoutMMAdImpl(Context context) {
            super(context);
        }

        public void addView(MMWebView webView, LayoutParams webLayoutParams) {
            MMLog.w(TAG, "MMLayout adding view (" + webView + ") to " + this);
            MMLayout.this.addView(webView, webLayoutParams);
        }

        MMLayout getCallingAd() {
            return MMLayout.this;
        }

        int getId() {
            return MMLayout.this.getId();
        }

        public void removeView(MMWebView mmWebView) {
            MMLayout.this.removeView(mmWebView);
        }

        public void setClickable(boolean clickable) {
            MMLayout.this.setClickable(clickable);
        }
    }

    protected MMLayout(Context context) {
        super(context);
        initLayout(context);
    }

    @Deprecated
    protected MMLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout(context);
    }

    private void initInlineVideoTransparentFix() {
        if (this.blackView != null) {
            ViewParent parent = this.blackView.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(this.blackView);
                this.blackView = null;
            }
        }
        this.blackView = new View(getContext());
        this.blackView.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        this.blackView.setLayoutParams(new LayoutParams(-1, -1));
        if (this.inlineVideoLayout != null && this.blackView.getParent() == null) {
            this.inlineVideoLayout.addView(this.blackView);
        }
    }

    private void internalSetCloseArea(String position) {
        if (this.closeAreaView == null) {
            this.closeAreaView = new View(getContext());
            float density = getContext().getResources().getDisplayMetrics().density;
            LayoutParams closeParams = new LayoutParams((int) (50.0f * density), (int) (50.0f * density));
            if (TOP_RIGHT.equals(position)) {
                closeParams.addRule(ApiEventType.API_MRAID_EXPAND);
            } else if (TOP_CENTER.equals(position)) {
                closeParams.addRule(ApiEventType.API_MRAID_IS_VIEWABLE);
            } else if (BOTTOM_LEFT.equals(position)) {
                closeParams.addRule(ApiEventType.API_MRAID_RESIZE);
            } else if (BOTTOM_CENTER.equals(position)) {
                closeParams.addRule(ApiEventType.API_MRAID_RESIZE);
                closeParams.addRule(ApiEventType.API_MRAID_IS_VIEWABLE);
            } else if (BOTTOM_RIGHT.equals(position)) {
                closeParams.addRule(ApiEventType.API_MRAID_RESIZE);
                closeParams.addRule(ApiEventType.API_MRAID_EXPAND);
            } else if (CENTER.equals(position)) {
                closeParams.addRule(ApiEventType.API_MRAID_CLOSE);
            }
            this.closeAreaView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    MMLayout.this.closeAreaTouched();
                }
            });
            addView(this.closeAreaView, closeParams);
        }
    }

    public void addBlackView() {
        initInlineVideoTransparentFix();
        if (this.blackView != null) {
            this.blackView.setVisibility(0);
        }
    }

    void addInlineVideo() {
        if (!(this.inlineVideoLayout == null || this.inlineVideoLayout.getParent() == null)) {
            ((ViewGroup) this.inlineVideoLayout.getParent()).removeView(this.inlineVideoLayout);
        }
        this.inlineVideoLayout = new RelativeLayout(getContext());
        this.inlineVideoLayout.setId(892934232);
        if (this.inlineVideoView.getParent() != null) {
            ((ViewGroup) this.inlineVideoView.getParent()).removeView(this.inlineVideoView);
        }
        this.inlineVideoLayout.addView(this.inlineVideoView);
        if (this.blackView != null) {
            if (this.blackView.getParent() == null) {
                this.inlineVideoLayout.addView(this.blackView);
            }
            this.blackView.bringToFront();
        }
        addView(this.inlineVideoLayout, this.inlineVideoView.getCustomLayoutParams());
    }

    boolean adjustVideo(InlineParams params) {
        MMSDK.runOnUiThread(new AnonymousClass_3(params));
        return false;
    }

    void closeAreaTouched() {
    }

    protected void finalize() throws Throwable {
        if (getId() == -1) {
            this.adImpl.isFinishing = true;
            MMLog.d(TAG, "finalize() for " + this.adImpl);
            MMAdImplController.removeAdViewController(this.adImpl);
        }
    }

    void fullScreenVideoLayout() {
    }

    public String getApid() {
        return this.adImpl.getApid();
    }

    public boolean getIgnoresDensityScaling() {
        return this.adImpl.getIgnoresDensityScaling();
    }

    public RequestListener getListener() {
        return this.adImpl.getListener();
    }

    public MMRequest getMMRequest() {
        return this.adImpl.getMMRequest();
    }

    void initInlineVideo(InlineParams params) {
        if (this.inlineVideoView != null) {
            ViewGroup vg = (ViewGroup) this.inlineVideoView.getParent();
            if (vg != null) {
                vg.removeView(this.inlineVideoView);
            }
            if (this.inlineVideoView.isPlaying()) {
                this.inlineVideoView.stopPlayback();
            }
            this.inlineVideoView = null;
        }
        this.inlineVideoView = new InlineVideoView(this);
        this.inlineVideoView.initInlineVideo(params);
        LayoutParams lp = new LayoutParams(-1, -1);
        lp.addRule(ApiEventType.API_MRAID_CLOSE, -1);
        this.inlineVideoView.setLayoutParams(lp);
        addInlineVideo();
    }

    protected final void initLayout(Context context) {
        try {
            MMLog.i(TAG, "Initializing MMLayout.");
            MMSDK.checkPermissions(context);
            MMSDK.checkActivity(context);
        } catch (Exception e) {
            Exception e2 = e;
            MMLog.e(TAG, "There was an exception initializing the MMAdView. ", e2);
            e2.printStackTrace();
        }
        this.diagnosticDetector = new GestureDetector(context.getApplicationContext(), new LayoutGestureListener(this));
        if (!appInit) {
            MMLog.d(TAG, "********** Millennial Device Id *****************");
            MMLog.d(TAG, MMSDK.getMMdid(context));
            MMLog.d(TAG, "Use the above identifier to register this device and receive test ads. Test devices can be registered and administered through your account at http://mmedia.com.");
            MMLog.d(TAG, "*************************************************");
            AdCache.cleanCache(context);
            appInit = true;
        }
    }

    boolean isVideoPlayingStreaming() {
        return this.inlineVideoView != null && this.inlineVideoView.isPlayingStreaming();
    }

    void loadUrl(String url) {
        if (!MMSDK.isConnected(getContext())) {
            MMLog.e(TAG, "No network available, can't load overlay.");
        } else if (this.adImpl.controller != null) {
            this.adImpl.controller.loadUrl(url);
        }
    }

    void loadWebContent(String content, String adUrl) {
        if (!MMSDK.isConnected(getContext())) {
            MMLog.e(TAG, "No network available, can't load overlay.");
        } else if (this.adImpl.controller != null) {
            this.adImpl.controller.loadWebContent(content, adUrl);
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            MMLog.d(TAG, "onAttachedToWindow for " + this.adImpl);
            if (getId() == -1) {
                MMLog.w(TAG, "MMAd missing id from getId(). Performance will be affected for configuration changes.");
            }
            if (!this.isResizing) {
                MMAdImplController.assignAdViewController(this.adImpl);
            }
            if (this.inlineVideoLayout != null) {
                this.inlineVideoLayout.bringToFront();
            }
            if (this.adImpl != null && this.adImpl.controller != null && this.adImpl.controller.webView != null) {
                this.adImpl.controller.webView.enableSendingSize();
            }
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        MMLog.d(TAG, "onDetachedFromWindow for" + this.adImpl);
        Context context = getContext();
        if (this.adImpl.adType == "i" && context != null && context instanceof Activity && ((Activity) context).isFinishing()) {
            this.adImpl.isFinishing = true;
        }
        if (!this.isResizing) {
            MMAdImplController.removeAdViewController(this.adImpl);
        }
    }

    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        long tempId = this.adImpl.internalId;
        this.adImpl.internalId = bundle.getLong("MMAdImplId");
        this.adImpl.linkForExpansionId = bundle.getLong("MMAdImplLinkedId");
        MMLog.d(TAG, "onRestoreInstanceState replacing adImpl-" + tempId + " with " + this.adImpl + " id=" + getId());
        String gsonInline = bundle.getString("inlineVideoViewGson");
        if (gsonInline != null) {
            initInlineVideo(InlineParams.getInlineParams(gsonInline));
        }
        super.onRestoreInstanceState(bundle.getParcelable("super"));
    }

    protected Parcelable onSaveInstanceState() {
        super.onSaveInstanceState();
        MMLog.d(TAG, "onSaveInstanceState saving - " + this.adImpl + " id=" + getId());
        Bundle bundle = new Bundle();
        bundle.putParcelable("super", super.onSaveInstanceState());
        bundle.putLong("MMAdImplId", this.adImpl.internalId);
        bundle.putLong("MMAdImplLinkedId", this.adImpl.linkForExpansionId);
        if (this.inlineVideoView != null) {
            if (this.inlineVideoView.isPlaying()) {
                this.inlineVideoView.inlineParams.currentPosition = this.inlineVideoView.getCurrentPosition();
            }
            bundle.putString("inlineVideoViewGson", this.inlineVideoView.getGsonState());
        }
        return bundle;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return this.diagnosticDetector.onTouchEvent(event) || !isClickable() || super.onTouchEvent(event);
    }

    @Deprecated
    public void onWindowFocusChanged(boolean windowInFocus) {
        super.onWindowFocusChanged(windowInFocus);
        if (windowInFocus) {
            if (this.inlineVideoView != null) {
                this.inlineVideoView.resumeVideo();
            }
        } else if (this.inlineVideoView != null) {
            this.inlineVideoView.pauseVideo();
        }
        MMLog.d(TAG, String.format("Window Focus Changed. For %s, Window in focus?: %b Controllers: %s", new Object[]{this.adImpl, Boolean.valueOf(windowInFocus), MMAdImplController.controllersToString()}));
        if (!(this.adImpl == null || this.adImpl.controller == null || this.adImpl.controller.webView == null)) {
            if (windowInFocus) {
                this.adImpl.controller.webView.onResumeWebView();
                this.adImpl.controller.webView.setMraidViewableVisible();
            } else {
                BridgeMMSpeechkit.releaseSpeechKit();
                this.adImpl.controller.webView.setMraidViewableHidden();
                this.adImpl.controller.webView.onPauseWebView();
            }
        }
        if (!windowInFocus && getContext() instanceof Activity) {
            Activity activity = (Activity) getContext();
            if (activity == null || (activity.isFinishing() && this.adImpl != null)) {
                this.adImpl.isFinishing = true;
                if (!(this.adImpl.controller == null || this.adImpl.controller.webView == null)) {
                    this.adImpl.controller.webView.setMraidHidden();
                    MMAdImplController.removeAdViewController(this.adImpl);
                }
            }
        }
        Audio audio = Audio.sharedAudio(getContext());
        if (audio != null) {
            synchronized (this) {
                audio.stop();
            }
        }
    }

    void pauseVideo() {
        if (this.inlineVideoView != null) {
            this.inlineVideoView.pauseVideo();
        }
    }

    void playVideo() {
        if (this.inlineVideoView != null) {
            this.inlineVideoView.playVideo();
        }
    }

    public void removeBlackView() {
        if (this.blackView != null) {
            this.blackView.setVisibility(MMAdView.TRANSITION_RANDOM);
        }
    }

    void removeCloseTouchDelegate() {
        if (this.closeAreaView != null && this.closeAreaView.getParent() != null && this.closeAreaView.getParent() instanceof ViewGroup) {
            ((ViewGroup) this.closeAreaView.getParent()).removeView(this.closeAreaView);
            this.closeAreaView = null;
        }
    }

    void removeVideo() {
        if (this.inlineVideoView != null) {
            this.inlineVideoView.removeVideo();
            this.inlineVideoView = null;
        }
    }

    void repositionVideoLayout() {
    }

    void resumeVideo() {
        if (this.inlineVideoView != null) {
            this.inlineVideoView.resumeVideo();
        }
    }

    public void setApid(String apid) {
        this.adImpl.setApid(apid);
    }

    void setCloseArea(String position) {
        post(new AnonymousClass_1(position));
    }

    public void setIgnoresDensityScaling(boolean ignoresDensityScaling) {
        this.adImpl.setIgnoresDensityScaling(ignoresDensityScaling);
    }

    public void setListener(RequestListener listener) {
        this.adImpl.setListener(listener);
    }

    public void setMMRequest(MMRequest request) {
        this.adImpl.setMMRequest(request);
    }

    void setMediaPlaybackRequiresUserGesture(boolean requiresGesture) {
        try {
            WebView.class.getMethod("setMediaPlaybackRequiresUserGesture", new Class[]{Boolean.TYPE}).invoke(this, new Object[]{Boolean.valueOf(requiresGesture)});
        } catch (Exception e) {
        }
    }

    void setVideoSource(String streamVideoURI) {
        if (this.inlineVideoView != null) {
            this.inlineVideoView.setVideoSource(streamVideoURI);
        }
    }

    void stopVideo() {
        if (this.inlineVideoView != null) {
            this.inlineVideoView.stopVideo();
        }
    }
}