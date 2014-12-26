package com.inmobi.re.container.mraidimpl;

import android.app.Activity;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.inmobi.androidsdk.IMBrowserActivity;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.inmobi.commons.internal.Log;
import com.inmobi.commons.internal.WrapperFunctions;
import com.inmobi.re.configs.Initializer;
import com.inmobi.re.container.CustomView;
import com.inmobi.re.container.CustomView.SwitchIconType;
import com.inmobi.re.container.IMWebView;
import com.inmobi.re.container.IMWebView.ViewState;
import com.inmobi.re.controller.util.Constants;
import com.millennialmedia.android.MMAdView;
import java.util.concurrent.atomic.AtomicBoolean;

public class MRAIDInterstitialController {
    protected static final int INT_BACKGROUND_ID = 224;
    public static AtomicBoolean isInterstitialDisplayed;
    private IMWebView a;
    private Activity b;
    private long c;
    private int d;
    public boolean lockOrientationValueForInterstitial;
    public Message mMsgOnInterstitialClosed;
    public Message mMsgOnInterstitialShown;
    public Display mSensorDisplay;
    public String orientationValueForInterstitial;

    class a implements Runnable {
        final /* synthetic */ RelativeLayout a;
        final /* synthetic */ FrameLayout b;

        a(RelativeLayout relativeLayout, FrameLayout frameLayout) {
            this.a = relativeLayout;
            this.b = frameLayout;
        }

        public void run() {
            this.a.removeView(MRAIDInterstitialController.this.a);
            this.b.removeView(this.a);
        }
    }

    class b implements OnKeyListener {
        b() {
        }

        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (4 != keyEvent.getKeyCode() || keyEvent.getAction() != 0) {
                return false;
            }
            Log.debug(Constants.RENDERING_LOG_TAG, "Back Button pressed while Interstitial ad is in active state ");
            MRAIDInterstitialController.this.handleInterstitialClose();
            return MRAIDInterstitialController.this.c > 0;
        }
    }

    class c implements AnimationListener {
        c() {
        }

        public void onAnimationEnd(Animation animation) {
            MRAIDInterstitialController.this.dismissWebview();
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    class d implements OnTouchListener {
        d() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MMAdView.TRANSITION_NONE:
                    view.requestFocus();
                    break;
                case MMAdView.TRANSITION_FADE:
                    view.requestFocus();
                    break;
            }
            return false;
        }
    }

    static {
        isInterstitialDisplayed = new AtomicBoolean();
    }

    public MRAIDInterstitialController(IMWebView iMWebView, Activity activity) {
        this.lockOrientationValueForInterstitial = true;
        this.c = 0;
        this.a = iMWebView;
    }

    private CustomView a() {
        CustomView customView = new CustomView(this.a.getContext(), this.a.getDensity(), SwitchIconType.CLOSE_TRANSPARENT);
        customView.setId(IMBrowserActivity.CLOSE_REGION_VIEW_ID);
        customView.disableView(this.a.getDisableCloseRegion());
        return customView;
    }

    public void animateAndDismissWebview() {
        Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());
        alphaAnimation.setStartOffset(0);
        alphaAnimation.setDuration(this.c);
        alphaAnimation.setAnimationListener(new c());
        this.a.startAnimation(alphaAnimation);
    }

    public void changeContentAreaForInterstitials(long j) {
        try {
            this.c = j;
            int webviewBgColor = Initializer.getConfigParams().getWebviewBgColor();
            this.d = this.b.getRequestedOrientation();
            handleOrientationForInterstitial();
            FrameLayout frameLayout = (FrameLayout) this.b.findViewById(16908290);
            View relativeLayout = new RelativeLayout(this.a.getContext());
            LayoutParams layoutParams = new RelativeLayout.LayoutParams(WrapperFunctions.getParamFillParent(), WrapperFunctions.getParamFillParent());
            layoutParams.addRule(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE);
            this.a.setFocusable(true);
            this.a.setFocusableInTouchMode(true);
            relativeLayout.addView(this.a, layoutParams);
            LayoutParams layoutParams2 = new RelativeLayout.LayoutParams((int) (this.a.getDensity() * 50.0f), (int) (this.a.getDensity() * 50.0f));
            layoutParams2.addRule(ApiEventType.API_MRAID_EXPAND);
            relativeLayout.addView(a(), layoutParams2);
            View customView = new CustomView(this.a.getContext(), this.a.getDensity(), SwitchIconType.CLOSE_BUTTON);
            customView.setVisibility(this.a.getCustomClose() ? ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES : 0);
            customView.setId(IMBrowserActivity.CLOSE_BUTTON_VIEW_ID);
            relativeLayout.addView(customView, layoutParams2);
            layoutParams = new RelativeLayout.LayoutParams(WrapperFunctions.getParamFillParent(), WrapperFunctions.getParamFillParent());
            relativeLayout.setId(INT_BACKGROUND_ID);
            relativeLayout.setBackgroundColor(webviewBgColor);
            frameLayout.addView(relativeLayout, layoutParams);
            this.a.setBackgroundColor(webviewBgColor);
            this.a.requestFocus();
            this.a.setOnKeyListener(new b());
            this.a.setOnTouchListener(new d());
            isInterstitialDisplayed.set(true);
            this.a.fireOnShowAdScreen();
        } catch (Exception e) {
            Log.debug(Constants.RENDERING_LOG_TAG, "Failed showing interstitial ad", e);
        }
    }

    public void dismissWebview() {
        FrameLayout frameLayout = (FrameLayout) this.b.findViewById(16908290);
        this.b.runOnUiThread(new a((RelativeLayout) frameLayout.findViewById(INT_BACKGROUND_ID), frameLayout));
    }

    public void handleInterstitialClose() {
        IMWebView.userInitiatedClose = true;
        isInterstitialDisplayed.set(false);
        this.a.close();
    }

    public void handleOrientationForInterstitial() {
        this.a.lockExpandOrientation(this.b, this.lockOrientationValueForInterstitial, this.orientationValueForInterstitial);
    }

    public void resetContentsForInterstitials() {
        try {
            if (this.a.getParent() != null) {
                this.b.setRequestedOrientation(this.d);
                this.a.mAudioVideoController.releaseAllPlayers();
                if (((RelativeLayout) ((FrameLayout) this.b.findViewById(16908290)).findViewById(INT_BACKGROUND_ID)) != null) {
                    if (this.c > 0) {
                        animateAndDismissWebview();
                    } else {
                        dismissWebview();
                    }
                }
                this.a.fireOnDismissAdScreen();
                this.a.injectJavaScript("window.mraidview.unRegisterOrientationListener()");
                this.a.setState(ViewState.HIDDEN);
                this.b.finish();
            }
        } catch (Exception e) {
            Log.debug(Constants.RENDERING_LOG_TAG, "Failed to close the interstitial ad", e);
        }
    }

    public void setActivity(Activity activity) {
        if (activity != null) {
            this.b = activity;
        }
    }
}