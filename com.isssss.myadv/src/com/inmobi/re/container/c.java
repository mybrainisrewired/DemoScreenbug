package com.inmobi.re.container;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v4.view.ViewCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebView;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.VideoView;
import com.inmobi.commons.internal.Log;
import com.inmobi.re.controller.util.Constants;

// compiled from: IMWebView.java
class c extends WebChromeClient {
    final /* synthetic */ IMWebView a;

    // compiled from: IMWebView.java
    class a implements OnClickListener {
        final /* synthetic */ JsResult a;

        a(JsResult jsResult) {
            this.a = jsResult;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            this.a.confirm();
        }
    }

    // compiled from: IMWebView.java
    class b implements OnClickListener {
        final /* synthetic */ JsResult a;

        b(JsResult jsResult) {
            this.a = jsResult;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            this.a.confirm();
        }
    }

    // compiled from: IMWebView.java
    class c implements OnTouchListener {
        c() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }
    }

    // compiled from: IMWebView.java
    class d implements OnClickListener {
        final /* synthetic */ JsResult a;

        d(JsResult jsResult) {
            this.a = jsResult;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            this.a.cancel();
        }
    }

    // compiled from: IMWebView.java
    class e implements OnKeyListener {
        e() {
        }

        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (4 != keyEvent.getKeyCode() || keyEvent.getAction() != 0) {
                return false;
            }
            Log.debug(Constants.RENDERING_LOG_TAG, "Back Button pressed when html5 video is playing");
            c.this.m.stopPlayback();
            c.this.f();
            return true;
        }
    }

    // compiled from: IMWebView.java
    class f implements OnKeyListener {
        f() {
        }

        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (4 != keyEvent.getKeyCode() || keyEvent.getAction() != 0) {
                return false;
            }
            Log.debug(Constants.RENDERING_LOG_TAG, "Back Button pressed when html5 video is playing");
            c.this.f();
            return true;
        }
    }

    // compiled from: IMWebView.java
    class g implements OnTouchListener {
        g() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            return true;
        }
    }

    // compiled from: IMWebView.java
    class h implements OnFocusChangeListener {
        h() {
        }

        public void onFocusChange(View view, boolean z) {
            c.this.m.requestFocus();
        }
    }

    // compiled from: IMWebView.java
    class i implements OnClickListener {
        final /* synthetic */ Callback a;
        final /* synthetic */ String b;

        i(Callback callback, String str) {
            this.a = callback;
            this.b = str;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            this.a.invoke(this.b, false, false);
        }
    }

    // compiled from: IMWebView.java
    class j implements OnClickListener {
        final /* synthetic */ Callback a;
        final /* synthetic */ String b;

        j(Callback callback, String str) {
            this.a = callback;
            this.b = str;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            this.a.invoke(this.b, true, false);
        }
    }

    c(IMWebView iMWebView) {
        this.a = iMWebView;
    }

    public void onGeolocationPermissionsShowPrompt(String str, Callback callback) {
        boolean z = false;
        try {
            Builder builder = new Builder(this.a.y);
            builder.setTitle("Locations access");
            builder.setMessage("Allow location access").setCancelable(true).setPositiveButton("Accept", new j(callback, str)).setNegativeButton("Decline", new i(callback, str));
            builder.create().show();
            super.onGeolocationPermissionsShowPrompt(str, callback);
        } catch (Exception e) {
            Log.internal(Constants.RENDERING_LOG_TAG, "Exception while accessing location from creative ", e);
            callback.invoke(str, z, z);
        }
    }

    public boolean onJsAlert(WebView webView, String str, String str2, JsResult jsResult) {
        Log.debug(Constants.RENDERING_LOG_TAG, "IMWebView-> onJsAlert, " + str2);
        try {
            Context expandedActivity;
            if (this.a.isExpanded() || this.a.mIsInterstitialAd) {
                expandedActivity = this.a.getExpandedActivity();
            } else {
                expandedActivity = webView.getContext();
            }
            new Builder(expandedActivity).setTitle(str).setMessage(str2).setPositiveButton(17039370, new b(jsResult)).setCancelable(false).create().show();
        } catch (Exception e) {
            Log.internal(Constants.RENDERING_LOG_TAG, "webchrome client exception onJSAlert ", e);
        }
        return true;
    }

    public boolean onJsConfirm(WebView webView, String str, String str2, JsResult jsResult) {
        Log.debug(Constants.RENDERING_LOG_TAG, "IMWebView-> onJsConfirm, " + str2);
        try {
            Context expandedActivity;
            if (this.a.isExpanded() || this.a.mIsInterstitialAd) {
                expandedActivity = this.a.getExpandedActivity();
            } else {
                expandedActivity = webView.getContext();
            }
            Builder positiveButton = new Builder(expandedActivity).setTitle(str).setMessage(str2).setPositiveButton(17039370, new a(jsResult));
            positiveButton.setNegativeButton(17039360, new d(jsResult));
            positiveButton.setCancelable(false).create().show();
        } catch (Exception e) {
            Log.internal(Constants.RENDERING_LOG_TAG, "webchrome client exception onJSConfirm ", e);
        }
        return true;
    }

    public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
        this.a.n = view;
        this.a.o = customViewCallback;
        Log.debug(Constants.RENDERING_LOG_TAG, "onShowCustomView ******************************" + view);
        try {
            this.a.a(this.a.n, new c());
            this.a.n.setOnTouchListener(new g());
            if (view instanceof FrameLayout) {
                this.a.q = (FrameLayout) view;
                FrameLayout frameLayout = (FrameLayout) this.a.y.findViewById(16908290);
                if (this.a.q.getFocusedChild() instanceof VideoView) {
                    Context expandedActivity;
                    this.a.m = (VideoView) this.a.q.getFocusedChild();
                    if (this.a.isExpanded() || this.a.mIsInterstitialAd) {
                        expandedActivity = this.a.getExpandedActivity();
                    } else {
                        expandedActivity = view.getContext();
                    }
                    this.a.m.setMediaController(new MediaController(expandedActivity));
                    this.a.q.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
                    this.a.m.setOnCompletionListener(this.a.J);
                    this.a.m.setOnFocusChangeListener(new h());
                    frameLayout.addView(this.a.n, new LayoutParams(-1, -1, 0, 0));
                    Log.debug(Constants.RENDERING_LOG_TAG, "Registering");
                    this.a.a(this.a.n, new e());
                } else {
                    this.a.n = view;
                    view.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
                    Log.debug(Constants.RENDERING_LOG_TAG, "adding " + view);
                    frameLayout.addView(view, new LayoutParams(-1, -1, 0, 0));
                    this.a.a(this.a.n, new f());
                }
            }
        } catch (Exception e) {
            Log.internal(Constants.RENDERING_LOG_TAG, "IMWebview onShowCustomView exception ", e);
        }
    }
}