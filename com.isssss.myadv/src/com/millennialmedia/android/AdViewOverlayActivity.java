package com.millennialmedia.android;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;

class AdViewOverlayActivity extends MMBaseActivity {
    private static final String TAG = "AdViewOverlayActivity";
    private AdViewOverlayView adViewOverlayView;
    boolean hasFocus;
    boolean isPaused;
    private OverlaySettings settings;

    AdViewOverlayActivity() {
    }

    private void lockOrientation() {
        if (this.activity.getRequestedOrientation() == 0) {
            setRequestedOrientation(0);
        } else if (this.activity.getRequestedOrientation() == 8) {
            setRequestedOrientation(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
        } else if (this.activity.getRequestedOrientation() == 9) {
            setRequestedOrientation(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES);
        } else {
            setRequestedOrientation(1);
        }
    }

    private void setRequestedOrientation(String orientation) {
        if ("landscape".equalsIgnoreCase(orientation)) {
            setRequestedOrientation(0);
        } else if ("portrait".equalsIgnoreCase(orientation)) {
            setRequestedOrientation(1);
        }
    }

    public void finish() {
        if (this.adViewOverlayView != null) {
            if (!this.adViewOverlayView.attachWebViewToLink()) {
                this.adViewOverlayView.killWebView();
            }
            this.adViewOverlayView.removeSelfAndAll();
        }
        this.adViewOverlayView = null;
        super.finish();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (this.adViewOverlayView != null) {
            this.adViewOverlayView.inlineConfigChange();
        }
        super.onConfigurationChanged(newConfig);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void onCreate(android.os.Bundle r11_savedInstanceState) {
        throw new UnsupportedOperationException("Method not decompiled: com.millennialmedia.android.AdViewOverlayActivity.onCreate(android.os.Bundle):void");
        /*
        r10 = this;
        r6 = 1;
        r9 = 0;
        r8 = -2;
        r4 = 16973840; // 0x1030010 float:2.4060945E-38 double:8.386191E-317;
        r10.setTheme(r4);
        super.onCreate(r11);
        r10.requestWindowFeature(r6);
        r4 = r10.getWindow();
        r5 = new android.graphics.drawable.ColorDrawable;
        r5.<init>(r9);
        r4.setBackgroundDrawable(r5);
        r4 = r10.getWindow();
        r5 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r4.clearFlags(r5);
        r4 = r10.getWindow();
        r5 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
        r4.addFlags(r5);
        r4 = r10.getWindow();
        r5 = 16777216; // 0x1000000 float:2.3509887E-38 double:8.289046E-317;
        r4.addFlags(r5);
        r1 = r10.getIntent();
        r4 = "settings";
        r4 = r1.getParcelableExtra(r4);
        r4 = (com.millennialmedia.android.OverlaySettings) r4;
        r10.settings = r4;
        r4 = r10.settings;
        if (r4 != 0) goto L_0x004f;
    L_0x0048:
        r4 = new com.millennialmedia.android.OverlaySettings;
        r4.<init>();
        r10.settings = r4;
    L_0x004f:
        r4 = r10.settings;
        r4.log();
        r4 = r10.settings;
        r4 = r4.orientation;
        if (r4 == 0) goto L_0x0061;
    L_0x005a:
        r4 = r10.settings;
        r4 = r4.orientation;
        r10.setRequestedOrientation(r4);
    L_0x0061:
        r4 = r10.settings;
        r4 = r4.allowOrientationChange;
        if (r4 == 0) goto L_0x00f8;
    L_0x0067:
        r10.unlockScreenOrientation();
    L_0x006a:
        if (r1 == 0) goto L_0x0085;
    L_0x006c:
        r3 = r1.getData();
        if (r3 == 0) goto L_0x0085;
    L_0x0072:
        r4 = "AdViewOverlayActivity";
        r5 = "Path: %s";
        r6 = new java.lang.Object[r6];
        r7 = r3.getLastPathSegment();
        r6[r9] = r7;
        r5 = java.lang.String.format(r5, r6);
        com.millennialmedia.android.MMLog.v(r4, r5);
    L_0x0085:
        r0 = new android.widget.RelativeLayout;
        r4 = r10.activity;
        r0.<init>(r4);
        r2 = new android.widget.RelativeLayout$LayoutParams;
        r2.<init>(r8, r8);
        r4 = 13;
        r2.addRule(r4);
        r4 = 885394873; // 0x34c60db9 float:3.6890359E-7 double:4.3744319E-315;
        r0.setId(r4);
        r0.setLayoutParams(r2);
        r4 = new com.millennialmedia.android.AdViewOverlayView;
        r5 = r10.settings;
        r4.<init>(r10, r5);
        r10.adViewOverlayView = r4;
        r4 = r10.adViewOverlayView;
        r0.addView(r4);
        r10.setContentView(r0);
        r4 = r10.getLastNonConfigurationInstance();
        if (r4 != 0) goto L_0x00f2;
    L_0x00b6:
        r4 = r10.settings;
        r4 = r4.isExpanded();
        if (r4 == 0) goto L_0x00fd;
    L_0x00be:
        r4 = r10.adViewOverlayView;
        r4 = r4.adImpl;
        if (r4 == 0) goto L_0x00e1;
    L_0x00c4:
        r4 = r10.adViewOverlayView;
        r4 = r4.adImpl;
        r4 = r4.controller;
        if (r4 == 0) goto L_0x00e1;
    L_0x00cc:
        r4 = r10.adViewOverlayView;
        r4 = r4.adImpl;
        r4 = r4.controller;
        r4 = r4.webView;
        if (r4 == 0) goto L_0x00e1;
    L_0x00d6:
        r4 = r10.adViewOverlayView;
        r4 = r4.adImpl;
        r4 = r4.controller;
        r4 = r4.webView;
        r4.setMraidExpanded();
    L_0x00e1:
        r4 = r10.settings;
        r4 = r4.hasExpandUrl();
        if (r4 == 0) goto L_0x00f2;
    L_0x00e9:
        r4 = r10.adViewOverlayView;
        r5 = r10.settings;
        r5 = r5.urlToLoad;
        r4.getWebContent(r5);
    L_0x00f2:
        r4 = r10.settings;
        r5 = 0;
        r4.orientation = r5;
        return;
    L_0x00f8:
        r10.lockOrientation();
        goto L_0x006a;
    L_0x00fd:
        r4 = r10.settings;
        r4 = r4.isExpanded();
        if (r4 != 0) goto L_0x00f2;
    L_0x0105:
        r4 = r10.adViewOverlayView;
        r5 = r10.settings;
        r5 = r5.content;
        r6 = r10.settings;
        r6 = r6.adUrl;
        r4.loadWebContent(r5, r6);
        goto L_0x00f2;
        */
    }

    protected void onDestroy() {
        super.onDestroy();
        MMLog.d(TAG, "Overlay onDestroy");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || event.getRepeatCount() != 0 || this.adViewOverlayView == null) {
            return super.onKeyDown(keyCode, event);
        }
        this.adViewOverlayView.finishOverlayWithAnimation();
        return true;
    }

    protected void onPause() {
        this.isPaused = true;
        MMLog.d(TAG, "Overlay onPause");
        Audio audio = Audio.sharedAudio(this.activity);
        if (audio != null) {
            synchronized (this) {
                audio.stop();
            }
        }
        if (this.adViewOverlayView != null) {
            this.adViewOverlayView.pauseVideo();
            if (!(this.adViewOverlayView.adImpl == null || this.adViewOverlayView.adImpl.controller == null || this.adViewOverlayView.adImpl.controller.webView == null)) {
                this.adViewOverlayView.adImpl.controller.webView.onPauseWebView();
            }
        }
        setResult(0);
        super.onPause();
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    protected void onResume() {
        this.isPaused = false;
        MMLog.d(TAG, "Overlay onResume");
        if (this.adViewOverlayView != null) {
            if (this.hasFocus) {
                this.adViewOverlayView.resumeVideo();
            }
            this.adViewOverlayView.addBlackView();
            if (!(this.adViewOverlayView.adImpl == null || this.adViewOverlayView.adImpl.controller == null || this.adViewOverlayView.adImpl.controller.webView == null)) {
                this.adViewOverlayView.adImpl.controller.webView.onResumeWebView();
            }
        }
        super.onResume();
    }

    public Object onRetainNonConfigurationInstance() {
        return this.adViewOverlayView != null ? this.adViewOverlayView.getNonConfigurationInstance() : null;
    }

    protected void onSaveInstanceState(Bundle outState) {
        if (this.adViewOverlayView != null) {
            outState.putInt("adViewId", this.adViewOverlayView.getId());
        }
        super.onSaveInstanceState(outState);
    }

    protected void onStop() {
        super.onStop();
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        this.hasFocus = hasWindowFocus;
        if (!this.isPaused && hasWindowFocus) {
            this.adViewOverlayView.resumeVideo();
        }
    }

    void setAllowOrientationChange(boolean allowOrientationChange) {
        this.settings.allowOrientationChange = allowOrientationChange;
        if (allowOrientationChange) {
            unlockScreenOrientation();
        } else {
            lockOrientation();
        }
    }

    void setRequestedOrientationLandscape() {
        this.settings.orientation = "landscape";
        this.settings.allowOrientationChange = false;
        setRequestedOrientation(0);
    }

    void setRequestedOrientationPortrait() {
        this.settings.orientation = "portrait";
        this.settings.allowOrientationChange = false;
        setRequestedOrientation(1);
    }

    void unlockScreenOrientation() {
        setRequestedOrientation(-1);
    }
}