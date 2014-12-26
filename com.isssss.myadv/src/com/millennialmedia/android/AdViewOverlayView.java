package com.millennialmedia.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.inmobi.monetization.internal.InvalidManifestConfigException;
import java.lang.ref.WeakReference;
import mobi.vserv.android.ads.VservConstants;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

class AdViewOverlayView extends MMLayout {
    private static final String MRAID_CLOSE_BUTTON_DESRIPTION = "mraidCloseButton";
    private static final String TAG = "AdViewOverlayView";
    private Button mraidCloseButton;
    CloseTopDrawable mraidCloseDrawable;
    WeakReference<AdViewOverlayActivity> overlayActivityRef;
    private ProgressBar progressBar;
    private boolean progressDone;
    OverlaySettings settings;

    private static class AnimationListener implements android.view.animation.Animation.AnimationListener {
        private WeakReference<AdViewOverlayView> overlayRef;

        public AnimationListener(AdViewOverlayView videoView) {
            this.overlayRef = new WeakReference(videoView);
        }

        public void onAnimationEnd(Animation animation) {
            AdViewOverlayView overlayView = (AdViewOverlayView) this.overlayRef.get();
            if (overlayView != null) {
                Activity activity = (Activity) overlayView.getContext();
                MMLog.d(TAG, "Finishing overlay this is in w/ anim finishOverLayWithAnim()");
                activity.finish();
            }
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
            AdViewOverlayView overlayView = (AdViewOverlayView) this.overlayRef.get();
            if (overlayView != null && overlayView.mraidCloseButton != null) {
                overlayView.mraidCloseButton.setVisibility(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
            }
        }
    }

    private static class CloseDrawable extends Drawable {
        protected boolean enabled;
        protected final Paint paint;

        CloseDrawable(boolean enabled) {
            this.enabled = true;
            this.enabled = enabled;
            this.paint = new Paint();
            this.paint.setAntiAlias(true);
            this.paint.setStyle(Style.STROKE);
        }

        public void draw(Canvas canvas) {
            Rect bounds = copyBounds();
            int width = bounds.right - bounds.left;
            int height = bounds.bottom - bounds.top;
            float strokeWidth = ((float) width) / 6.0f;
            this.paint.setStrokeWidth(strokeWidth);
            int grayScale = this.enabled ? 255 : VservConstants.THREAD_SLEEP;
            this.paint.setARGB(MotionEventCompat.ACTION_MASK, grayScale, grayScale, grayScale);
            canvas.drawLine(strokeWidth / 2.0f, strokeWidth / 2.0f, ((float) width) - strokeWidth / 2.0f, ((float) height) - strokeWidth / 2.0f, this.paint);
            canvas.drawLine(((float) width) - strokeWidth / 2.0f, strokeWidth / 2.0f, strokeWidth / 2.0f, ((float) height) - strokeWidth / 2.0f, this.paint);
        }

        public int getOpacity() {
            return InvalidManifestConfigException.MISSING_CONFIG_CHANGES;
        }

        public void setAlpha(int alpha) {
        }

        public void setColorFilter(ColorFilter cf) {
        }
    }

    private static class FetchWebViewContentTask extends AsyncTask<Void, Void, String> {
        private WeakReference<AdViewOverlayView> _overlayViewRef;
        private String baseUrl;
        private boolean cancelVideo;

        public FetchWebViewContentTask(AdViewOverlayView view, String baseUrl) {
            this.baseUrl = baseUrl;
            this._overlayViewRef = new WeakReference(view);
        }

        protected String doInBackground(Void... arg0) {
            this.cancelVideo = true;
            if (!TextUtils.isEmpty(this.baseUrl)) {
                try {
                    HttpResponse httpResponse = new HttpGetRequest().get(this.baseUrl);
                    if (httpResponse != null) {
                        StatusLine statusLine = httpResponse.getStatusLine();
                        if (!(httpResponse == null || statusLine == null || statusLine.getStatusCode() == 404)) {
                            HttpEntity httpEntity = httpResponse.getEntity();
                            if (httpEntity != null) {
                                String webContent = HttpGetRequest.convertStreamToString(httpEntity.getContent());
                                this.cancelVideo = false;
                                return webContent;
                            }
                        }
                    }
                } catch (Exception e) {
                    MMLog.e(TAG, "Unable to get weboverlay", e);
                }
            }
            return null;
        }

        protected void onPostExecute(String webContent) {
            AdViewOverlayView view = (AdViewOverlayView) this._overlayViewRef.get();
            if (view != null) {
                if (this.cancelVideo) {
                    AdViewOverlayActivity overlayActivity = (AdViewOverlayActivity) view.overlayActivityRef.get();
                    if (overlayActivity != null) {
                        overlayActivity.finish();
                    } else {
                        view.removeProgressBar();
                    }
                }
                if (webContent != null && view.adImpl != null && view.adImpl.controller != null) {
                    view.adImpl.controller.setWebViewContent(webContent, this.baseUrl);
                }
            }
        }

        protected void onPreExecute() {
            AdViewOverlayView view = (AdViewOverlayView) this._overlayViewRef.get();
            if (view != null && view.progressBar == null) {
                view.initProgressBar();
            }
            super.onPreExecute();
        }
    }

    private static final class NonConfigurationInstance {
        MMAdImplController controller;
        boolean progressDone;
        OverlaySettings settings;

        private NonConfigurationInstance() {
        }
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR;
        public Object customInlineLayoutParams;
        String gson;

        static {
            CREATOR = new Creator<SavedState>() {
                public SavedState createFromParcel(Parcel in) {
                    return new SavedState(null);
                }

                public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            };
        }

        private SavedState(Parcel in) {
            super(in);
            this.gson = in.readString();
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(this.gson);
        }
    }

    private static class SetCloseButtonTouchDelegateRunnable implements Runnable {
        int bottom;
        private final Button closeButton;
        int left;
        int right;
        int top;

        SetCloseButtonTouchDelegateRunnable(Button closeButton, int topMargin, int leftMargin, int bottomMargin, int rightMargin) {
            this.closeButton = closeButton;
            this.top = topMargin;
            this.left = leftMargin;
            this.bottom = bottomMargin;
            this.right = rightMargin;
        }

        public void run() {
            Rect delegateArea = new Rect();
            this.closeButton.getHitRect(delegateArea);
            delegateArea.top += this.top;
            delegateArea.right += this.right;
            delegateArea.bottom += this.bottom;
            delegateArea.left += this.left;
            TouchDelegate expandedArea = new TouchDelegate(delegateArea, this.closeButton);
            if (View.class.isInstance(this.closeButton.getParent())) {
                ((View) this.closeButton.getParent()).setTouchDelegate(expandedArea);
            }
        }
    }

    private static class CloseTopDrawable extends CloseDrawable {
        final float dist;
        final float scale;

        CloseTopDrawable(boolean enabled, float scale) {
            super(enabled);
            this.scale = scale;
            this.dist = 4.0f * scale;
            this.paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        }

        public void draw(Canvas canvas) {
            Rect bounds = copyBounds();
            float strokeWidth = ((float) (bounds.right - bounds.left)) / 10.0f;
            float cx = ((float) bounds.right) - this.scale * 20.0f;
            float cy = ((float) bounds.top) + this.scale * 20.0f;
            this.paint.setStrokeWidth(strokeWidth);
            this.paint.setColor(ViewCompat.MEASURED_STATE_MASK);
            this.paint.setStyle(Style.STROKE);
            canvas.drawCircle(cx, cy, 12.0f * this.scale, this.paint);
            this.paint.setColor(-1);
            this.paint.setStyle(Style.FILL_AND_STROKE);
            canvas.drawCircle(cx, cy, this.scale * 10.0f, this.paint);
            this.paint.setColor(ViewCompat.MEASURED_STATE_MASK);
            canvas.drawCircle(cx, cy, 7.0f * this.scale, this.paint);
            this.paint.setColor(-1);
            this.paint.setStrokeWidth(strokeWidth / 2.0f);
            this.paint.setStyle(Style.STROKE);
            canvas.drawLine(cx - this.dist, cy - this.dist, cx + this.dist, cy + this.dist, this.paint);
            canvas.drawLine(cx + this.dist, cy - this.dist, cx - this.dist, cy + this.dist, this.paint);
        }
    }

    private static class OverlayWebViewClientListener extends BasicWebViewClientListener {
        OverlayWebViewClientListener(MMAdImpl adImpl) {
            super(adImpl);
        }

        public void onPageFinished(String url) {
            super.onPageFinished(url);
            MMAdImpl adImpl = (MMAdImpl) this.adImplRef.get();
            if (adImpl != null) {
                adImpl.removeProgressBar();
            }
        }
    }

    class AdViewOverlayViewMMAdImpl extends MMLayoutMMAdImpl {
        public AdViewOverlayViewMMAdImpl(Context context) {
            super(context);
            this.mmWebViewClientListener = new OverlayWebViewClientListener(this);
        }

        MMWebViewClient getMMWebViewClient() {
            MMLog.d(TAG, "Returning a client for user: OverlayWebViewClient, adimpl=" + AdViewOverlayView.this.adImpl);
            MMWebViewClient bannerExpandedWebViewClient;
            if (AdViewOverlayView.this.adImpl.linkForExpansionId != 0 || AdViewOverlayView.this.settings.hasExpandUrl()) {
                bannerExpandedWebViewClient = new BannerExpandedWebViewClient(this.mmWebViewClientListener, new OverlayRedirectionListenerImpl(this));
                this.mmWebViewClient = bannerExpandedWebViewClient;
                return bannerExpandedWebViewClient;
            } else {
                bannerExpandedWebViewClient = new InterstitialWebViewClient(this.mmWebViewClientListener, new OverlayRedirectionListenerImpl(this));
                this.mmWebViewClient = bannerExpandedWebViewClient;
                return bannerExpandedWebViewClient;
            }
        }

        boolean isExpandingToUrl() {
            return AdViewOverlayView.this.settings.hasExpandUrl() && !AdViewOverlayView.this.settings.hasLoadedExpandUrl();
        }

        void removeProgressBar() {
            AdViewOverlayView.this.removeProgressBar();
        }
    }

    static class OverlayRedirectionListenerImpl extends MMAdImplRedirectionListenerImpl {
        public OverlayRedirectionListenerImpl(MMAdImpl adImpl) {
            super(adImpl);
        }

        public boolean isExpandingToUrl() {
            MMAdImpl adImpl = (MMAdImpl) this.adImplRef.get();
            return (adImpl == null || !adImpl instanceof AdViewOverlayViewMMAdImpl) ? false : adImpl.isExpandingToUrl();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    AdViewOverlayView(com.millennialmedia.android.AdViewOverlayActivity r11_overlayActivity, com.millennialmedia.android.OverlaySettings r12_settingsIn) {
        throw new UnsupportedOperationException("Method not decompiled: com.millennialmedia.android.AdViewOverlayView.<init>(com.millennialmedia.android.AdViewOverlayActivity, com.millennialmedia.android.OverlaySettings):void");
        /*
        r10 = this;
        r9 = 0;
        r8 = -1;
        r4 = r11.activity;
        r10.<init>(r4);
        r4 = new java.lang.ref.WeakReference;
        r4.<init>(r11);
        r10.overlayActivityRef = r4;
        r4 = new com.millennialmedia.android.AdViewOverlayView$AdViewOverlayViewMMAdImpl;
        r5 = r11.activity;
        r4.<init>(r5);
        r10.adImpl = r4;
        r4 = 15062; // 0x3ad6 float:2.1106E-41 double:7.4416E-320;
        r10.setId(r4);
        r4 = r10.adImpl;
        r5 = "i";
        r4.adType = r5;
        r10.settings = r12;
        r0 = 0;
        r4 = r11.activity;
        r4 = r4 instanceof android.app.Activity;
        if (r4 == 0) goto L_0x0078;
    L_0x002b:
        r4 = r11.activity;
        r0 = r4.getLastNonConfigurationInstance();
        r0 = (com.millennialmedia.android.AdViewOverlayView.NonConfigurationInstance) r0;
        if (r0 == 0) goto L_0x0169;
    L_0x0035:
        r4 = r0.progressDone;
        r10.progressDone = r4;
        r4 = r10.adImpl;
        r5 = r0.controller;
        r4.controller = r5;
        r4 = r0.settings;
        r10.settings = r4;
        r4 = r10.adImpl;
        if (r4 == 0) goto L_0x005e;
    L_0x0047:
        r4 = r10.adImpl;
        r4 = r4.controller;
        if (r4 == 0) goto L_0x005e;
    L_0x004d:
        r4 = r10.adImpl;
        r4 = r4.controller;
        r4 = r4.webView;
        if (r4 == 0) goto L_0x005e;
    L_0x0055:
        r4 = r10.adImpl;
        r4 = r4.controller;
        r4 = r4.webView;
        r10.addView(r4);
    L_0x005e:
        r4 = "AdViewOverlayView";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Restoring configurationinstance w/ controller= ";
        r5 = r5.append(r6);
        r6 = r0.controller;
        r5 = r5.append(r6);
        r5 = r5.toString();
        com.millennialmedia.android.MMLog.d(r4, r5);
    L_0x0078:
        r4 = r11.activity;
        r4 = r4.getResources();
        r4 = r4.getDisplayMetrics();
        r2 = r4.density;
        r4 = r10.settings;
        r4 = r4.height;
        if (r4 == 0) goto L_0x0172;
    L_0x008a:
        r4 = r10.settings;
        r4 = r4.width;
        if (r4 == 0) goto L_0x0172;
    L_0x0090:
        r1 = new android.widget.RelativeLayout$LayoutParams;
        r4 = r10.settings;
        r4 = r4.width;
        r4 = (float) r4;
        r4 = r4 * r2;
        r4 = (int) r4;
        r5 = r10.settings;
        r5 = r5.height;
        r5 = (float) r5;
        r5 = r5 * r2;
        r5 = (int) r5;
        r1.<init>(r4, r5);
    L_0x00a3:
        r4 = 13;
        r1.addRule(r4);
        r10.setLayoutParams(r1);
        r4 = 1031798784; // 0x3d800000 float:0.0625 double:5.097763326E-315;
        r4 = r4 * r2;
        r5 = r10.settings;
        r5 = r5.shouldResizeOverlay;
        r5 = (float) r5;
        r4 = r4 * r5;
        r4 = (int) r4;
        r3 = java.lang.Integer.valueOf(r4);
        r4 = r3.intValue();
        r5 = r3.intValue();
        r6 = r3.intValue();
        r7 = r3.intValue();
        r10.setPadding(r4, r5, r6, r7);
        r4 = r11.activity;
        r4 = r10.initMRaidCloseButton(r4, r2);
        r10.mraidCloseButton = r4;
        r4 = r10.settings;
        r4 = r4.isExpanded();
        if (r4 == 0) goto L_0x00ec;
    L_0x00dc:
        r4 = r10.settings;
        r4 = r4.hasExpandUrl();
        if (r4 != 0) goto L_0x00ec;
    L_0x00e4:
        r4 = r10.adImpl;
        r5 = r10.settings;
        r5 = r5.creatorAdImplId;
        r4.linkForExpansionId = r5;
    L_0x00ec:
        r4 = r10.adImpl;
        com.millennialmedia.android.MMAdImplController.assignAdViewController(r4);
        r4 = r10.mraidCloseButton;
        if (r4 == 0) goto L_0x00fa;
    L_0x00f5:
        r4 = r10.mraidCloseButton;
        r10.addView(r4);
    L_0x00fa:
        r4 = r10.progressDone;
        if (r4 != 0) goto L_0x0111;
    L_0x00fe:
        r4 = r10.settings;
        r4 = r4.isExpanded();
        if (r4 != 0) goto L_0x0111;
    L_0x0106:
        r4 = r10.settings;
        r4 = r4.isFromInterstitial();
        if (r4 != 0) goto L_0x0111;
    L_0x010e:
        r10.initProgressBar();
    L_0x0111:
        r4 = r10.settings;
        r4 = r4.getIsTransparent();
        if (r4 == 0) goto L_0x0179;
    L_0x0119:
        r4 = r10.adImpl;
        if (r4 == 0) goto L_0x0134;
    L_0x011d:
        r4 = r10.adImpl;
        r4 = r4.controller;
        if (r4 == 0) goto L_0x0134;
    L_0x0123:
        r4 = r10.adImpl;
        r4 = r4.controller;
        r4 = r4.webView;
        if (r4 == 0) goto L_0x0134;
    L_0x012b:
        r4 = r10.adImpl;
        r4 = r4.controller;
        r4 = r4.webView;
        r4.setBackgroundColor(r9);
    L_0x0134:
        r10.setBackgroundColor(r9);
    L_0x0137:
        r4 = r10.settings;
        r4 = r4.enableHardwareAccel();
        if (r4 == 0) goto L_0x015a;
    L_0x013f:
        r4 = r10.adImpl;
        if (r4 == 0) goto L_0x015a;
    L_0x0143:
        r4 = r10.adImpl;
        r4 = r4.controller;
        if (r4 == 0) goto L_0x015a;
    L_0x0149:
        r4 = r10.adImpl;
        r4 = r4.controller;
        r4 = r4.webView;
        if (r4 == 0) goto L_0x015a;
    L_0x0151:
        r4 = r10.adImpl;
        r4 = r4.controller;
        r4 = r4.webView;
        r4.enableHardwareAcceleration();
    L_0x015a:
        if (r0 != 0) goto L_0x015f;
    L_0x015c:
        r10.animateView();
    L_0x015f:
        r4 = r10.settings;
        r4 = r4.getUseCustomClose();
        r10.setUseCustomClose(r4);
        return;
    L_0x0169:
        r4 = "AdViewOverlayView";
        r5 = "Null configurationinstance ";
        com.millennialmedia.android.MMLog.d(r4, r5);
        goto L_0x0078;
    L_0x0172:
        r1 = new android.widget.RelativeLayout$LayoutParams;
        r1.<init>(r8, r8);
        goto L_0x00a3;
    L_0x0179:
        r4 = r10.adImpl;
        if (r4 == 0) goto L_0x0194;
    L_0x017d:
        r4 = r10.adImpl;
        r4 = r4.controller;
        if (r4 == 0) goto L_0x0194;
    L_0x0183:
        r4 = r10.adImpl;
        r4 = r4.controller;
        r4 = r4.webView;
        if (r4 == 0) goto L_0x0194;
    L_0x018b:
        r4 = r10.adImpl;
        r4 = r4.controller;
        r4 = r4.webView;
        r4.setBackgroundColor(r8);
    L_0x0194:
        r10.setBackgroundColor(r8);
        goto L_0x0137;
        */
    }

    private void animateView() {
        Animation animation;
        if (this.settings.getTransition().equals("slideup")) {
            animation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
            MMLog.v(TAG, "Translate up");
        } else if (this.settings.getTransition().equals("slidedown")) {
            animation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, -1.0f, 1, 0.0f);
            MMLog.v(TAG, "Translate down");
        } else if (this.settings.getTransition().equals("explode")) {
            Animation scaleAnimation = animation;
            float f = 0.9f;
            int i = 1;
            int i2 = 1;
            float f2 = 0.5f;
            MMLog.v(TAG, "Explode");
        } else {
            return;
        }
        animation.setDuration(this.settings.getTransitionDurationInMillis());
        startAnimation(animation);
    }

    private LayoutParams getCloseAreaParams(float scale) {
        int closeHeight = (int) (50.0f * scale + 0.5f);
        LayoutParams closeButParams = new LayoutParams(closeHeight, closeHeight);
        closeButParams.addRule(ApiEventType.API_MRAID_EXPAND);
        closeButParams.addRule(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE);
        return closeButParams;
    }

    private Button initMRaidCloseButton(Context context, float scale) {
        Button mraidCloseButton = new Button(context);
        mraidCloseButton.setId(301);
        mraidCloseButton.setContentDescription(MRAID_CLOSE_BUTTON_DESRIPTION);
        this.mraidCloseDrawable = new CloseTopDrawable(true, scale);
        mraidCloseButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MMLog.v(TAG, "Close button clicked.");
                AdViewOverlayView.this.finishOverlayWithAnimation();
            }
        });
        LayoutParams closeButParams = getCloseAreaParams(scale);
        mraidCloseButton.setLayoutParams(closeButParams);
        mraidCloseButton.post(new SetCloseButtonTouchDelegateRunnable(mraidCloseButton, closeButParams.topMargin, closeButParams.leftMargin, closeButParams.bottomMargin, closeButParams.rightMargin));
        return mraidCloseButton;
    }

    private void initProgressBar() {
        AdViewOverlayActivity overlayActivity = (AdViewOverlayActivity) this.overlayActivityRef.get();
        if (overlayActivity != null) {
            this.progressBar = new ProgressBar(overlayActivity.activity);
            this.progressBar.setIndeterminate(true);
            this.progressBar.setVisibility(0);
            LayoutParams progParams = new LayoutParams(-2, -2);
            progParams.addRule(ApiEventType.API_MRAID_CLOSE);
            addView(this.progressBar, progParams);
        }
    }

    private void removeProgressBar() {
        if (!this.progressDone && this.progressBar != null) {
            this.progressDone = true;
            this.progressBar.setVisibility(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
            removeView(this.progressBar);
            this.progressBar = null;
        }
    }

    void addInlineVideo() {
        super.addInlineVideo();
        bringMraidCloseToFront();
    }

    boolean attachWebViewToLink() {
        return (this.adImpl == null || this.adImpl.linkForExpansionId == 0 || !MMAdImplController.attachWebViewFromOverlay(this.adImpl)) ? false : true;
    }

    void bringMraidCloseToFront() {
        if (this.mraidCloseButton != null) {
            this.mraidCloseButton.bringToFront();
        }
    }

    void closeAreaTouched() {
        post(new Runnable() {
            public void run() {
                AdViewOverlayView.this.finishOverlayWithAnimation();
            }
        });
    }

    void finishOverlayWithAnimation() {
        MMLog.d(TAG, "Ad overlay closed");
        if (((Activity) getContext()) != null) {
            AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
            animation.setAnimationListener(new AnimationListener(this));
            animation.setFillEnabled(true);
            animation.setFillBefore(true);
            animation.setFillAfter(true);
            animation.setDuration(400);
            startAnimation(animation);
        }
    }

    void fullScreenVideoLayout() {
        removeView(this.inlineVideoLayout);
        addView(this.inlineVideoLayout, new LayoutParams(-1, -1));
        bringMraidCloseToFront();
    }

    Object getNonConfigurationInstance() {
        NonConfigurationInstance nonConfigurationInstance = new NonConfigurationInstance();
        if (this.adImpl != null) {
            MMLog.d(TAG, "Saving getNonConfigurationInstance for " + this.adImpl);
            if (!(this.adImpl.controller == null || this.adImpl.controller.webView == null)) {
                this.adImpl.controller.webView.removeFromParent();
            }
            nonConfigurationInstance.controller = this.adImpl.controller;
        }
        nonConfigurationInstance.progressDone = this.progressDone;
        nonConfigurationInstance.settings = this.settings;
        return nonConfigurationInstance;
    }

    void getWebContent(String urlToLoad) {
        new FetchWebViewContentTask(this, urlToLoad).execute(new Void[0]);
    }

    void injectJS(String jsString) {
        if (this.adImpl.controller != null) {
            this.adImpl.controller.loadUrl(jsString);
        }
    }

    void inlineConfigChange() {
        if (this.inlineVideoView != null && this.inlineVideoLayout != null) {
            this.inlineVideoLayout.setLayoutParams(this.inlineVideoView.getCustomLayoutParams());
            bringMraidCloseToFront();
        }
    }

    void killWebView() {
        BridgeMMSpeechkit.releaseSpeechKit();
        if (this.adImpl != null && this.adImpl.controller != null && this.adImpl.controller.webView != null) {
            this.adImpl.controller.webView.clearFocus();
            this.adImpl.controller.webView.setMraidViewableHidden();
            if (this.adImpl.adType == "i") {
                this.adImpl.controller.webView.setMraidHidden();
            }
            this.adImpl.controller.webView.onPauseWebView();
        }
    }

    void removeSelfAndAll() {
        removeAllViews();
        ViewParent parent = getParent();
        if (parent != null && parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(this);
        }
    }

    void repositionVideoLayout() {
        removeView(this.inlineVideoLayout);
        addView(this.inlineVideoLayout, this.inlineVideoView.getCustomLayoutParams());
        bringMraidCloseToFront();
    }

    void setUseCustomClose(boolean isHideDrawable) {
        this.settings.setUseCustomClose(isHideDrawable);
        this.mraidCloseButton.setBackgroundDrawable(isHideDrawable ? null : this.mraidCloseDrawable);
    }
}