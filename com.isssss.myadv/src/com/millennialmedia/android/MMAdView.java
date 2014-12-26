package com.millennialmedia.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import com.facebook.ads.internal.AdWebViewUtils;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.util.Map;
import java.util.Random;

public final class MMAdView extends MMLayout implements OnClickListener, AnimationListener {
    static final int DEFAULT_RESIZE_PARAM_VALUES = -50;
    private static final String TAG = "MMAdView";
    public static final int TRANSITION_DOWN = 3;
    public static final int TRANSITION_FADE = 1;
    public static final int TRANSITION_NONE = 0;
    public static final int TRANSITION_RANDOM = 4;
    public static final int TRANSITION_UP = 2;
    int height;
    int oldHeight;
    int oldWidth;
    ImageView refreshAnimationimageView;
    int transitionType;
    ResizeView view;
    int width;

    class BannerBounds {
        DTOResizeParameters resizeParams;
        int translationX;
        int translationY;

        private class BoundsResult {
            int size;
            int translation;

            private BoundsResult() {
            }
        }

        BannerBounds(DTOResizeParameters resizeParams) {
            this.resizeParams = resizeParams;
            this.translationX = resizeParams.offsetX;
            this.translationY = resizeParams.offsetY;
        }

        private BoundsResult calculateBounds(int screenPos, int offset, int size, int max) {
            int newStart = screenPos;
            int newSize = size;
            if (screenPos + size + offset > max) {
                newStart = max - size + offset;
                if (newStart < 0) {
                    newStart = TRANSITION_NONE;
                    newSize = max;
                } else if (newStart + size > max) {
                    newStart = max - size;
                }
            } else if (offset > 0) {
                newStart = offset;
            }
            BoundsResult result = new BoundsResult(null);
            result.translation = newStart - screenPos;
            result.size = newSize;
            return result;
        }

        private void calculateXBounds() {
            int[] loc = new int[2];
            MMAdView.this.getLocationInWindow(loc);
            BoundsResult resultX = calculateBounds(loc[0], this.resizeParams.offsetX, this.resizeParams.width, this.resizeParams.xMax);
            this.resizeParams.width = resultX.size;
            this.translationX = resultX.translation;
        }

        private void calculateYBounds() {
            int[] loc = new int[2];
            MMAdView.this.getLocationInWindow(loc);
            BoundsResult resultY = calculateBounds(loc[1], this.resizeParams.offsetY, this.resizeParams.height, this.resizeParams.yMax);
            this.resizeParams.height = resultY.size;
            this.translationY = resultY.translation;
        }

        void calculateOnScreenBounds() {
            calculateXBounds();
            calculateYBounds();
        }

        LayoutParams modifyLayoutParams(LayoutParams oldParams) {
            oldParams.width = this.resizeParams.width;
            oldParams.height = this.resizeParams.height;
            return oldParams;
        }
    }

    class ResizeView extends View {
        public ResizeView(Context context) {
            super(context);
        }

        synchronized void attachToContext(View view) {
            MMAdView.this.detachFromParent(view);
            if (getParent() != null && getParent() instanceof ViewGroup) {
                ((ViewGroup) getParent()).addView(view);
            }
        }

        protected void onRestoreInstanceState(Parcelable state) {
            MMLog.d(TAG, "onRestoreInstanceState");
            MMAdView.this.attachToWindow(MMAdView.this);
            super.onRestoreInstanceState(state);
        }

        protected Parcelable onSaveInstanceState() {
            MMLog.d(TAG, "onSaveInstanceState");
            attachToContext(MMAdView.this);
            return super.onSaveInstanceState();
        }
    }

    private static class MMAdViewWebViewClientListener extends BasicWebViewClientListener {
        MMAdViewWebViewClientListener(MMAdImpl adImpl) {
            super(adImpl);
        }

        public void onPageFinished(String url) {
            super.onPageFinished(url);
            MMAdImpl adImpl = (MMAdImpl) this.adImplRef.get();
            if (adImpl != null && adImpl.isTransitionAnimated()) {
                adImpl.animateTransition();
            }
        }
    }

    class MMAdViewMMAdImpl extends MMLayoutMMAdImpl {

        class AnonymousClass_1 implements Runnable {
            final /* synthetic */ Animation val$animFinal;

            AnonymousClass_1(Animation animation) {
                this.val$animFinal = animation;
            }

            public void run() {
                MMAdViewMMAdImpl.this.this$0.refreshAnimationimageView.startAnimation(this.val$animFinal);
            }
        }

        public MMAdViewMMAdImpl(Context context) {
            super(context);
            this.mmWebViewClientListener = new MMAdViewWebViewClientListener(this);
        }

        void animateTransition() {
            if (MMAdView.this.refreshAnimationimageView.getDrawable() != null) {
                Animation animation;
                int type = MMAdView.this.transitionType;
                if (type == 4) {
                    type = new Random().nextInt(TRANSITION_RANDOM);
                }
                switch (type) {
                    case TRANSITION_UP:
                        animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, -((float) MMAdView.this.getHeight()));
                        break;
                    case TRANSITION_DOWN:
                        animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) MMAdView.this.getHeight());
                        break;
                    default:
                        animation = new AlphaAnimation(1.0f, 0.0f);
                        break;
                }
                animation.setDuration(AdWebViewUtils.DEFAULT_IMPRESSION_DELAY_MS);
                animation.setAnimationListener(MMAdView.this);
                animation.setFillEnabled(true);
                animation.setFillBefore(true);
                animation.setFillAfter(true);
                MMSDK.runOnUiThread(new AnonymousClass_1(animation));
            }
        }

        String getReqType() {
            return "getad";
        }

        String getRequestCompletedAction() {
            return MMBroadcastReceiver.ACTION_GETAD_SUCCEEDED;
        }

        String getRequestFailedAction() {
            return MMBroadcastReceiver.ACTION_GETAD_FAILED;
        }

        public boolean hasCachedVideoSupport() {
            return false;
        }

        void insertUrlAdMetaValues(Map<String, String> paramsMap) {
            if (MMAdView.this.height > 0) {
                paramsMap.put(MMRequest.KEY_HEIGHT, String.valueOf(MMAdView.this.height));
            }
            if (MMAdView.this.width > 0) {
                paramsMap.put(MMRequest.KEY_WIDTH, String.valueOf(MMAdView.this.width));
            }
            super.insertUrlAdMetaValues(paramsMap);
        }

        public boolean isBanner() {
            return true;
        }

        boolean isLifecycleObservable() {
            return MMAdView.this.getWindowToken() != null;
        }

        boolean isTransitionAnimated() {
            return MMAdView.this.transitionType != 0;
        }

        void prepareTransition(Bitmap bitmap) {
            MMAdView.this.refreshAnimationimageView.setImageBitmap(bitmap);
            MMAdView.this.refreshAnimationimageView.setVisibility(TRANSITION_NONE);
            MMAdView.this.refreshAnimationimageView.bringToFront();
        }
    }

    public MMAdView(Context context) {
        super(context);
        this.transitionType = 4;
        this.height = 0;
        this.width = 0;
        this.oldHeight = -50;
        this.oldWidth = -50;
        this.adImpl = new MMAdViewMMAdImpl(context);
        init(context);
    }

    @Deprecated
    public MMAdView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Deprecated
    public MMAdView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.transitionType = 4;
        this.height = 0;
        this.width = 0;
        this.oldHeight = -50;
        this.oldWidth = -50;
        if (isInEditMode()) {
            initEclipseAd(context);
        } else {
            MMLog.d(TAG, "Creating MMAdView from XML layout.");
            this.adImpl = new MMAdViewMMAdImpl(context);
            if (attrs != null) {
                String namespace = "http://millennialmedia.com/android/schema";
                setApid(attrs.getAttributeValue(namespace, "apid"));
                this.adImpl.ignoreDensityScaling = attrs.getAttributeBooleanValue(namespace, "ignoreDensityScaling", false);
                String heightIn = attrs.getAttributeValue(namespace, MMLayout.KEY_HEIGHT);
                String widthIn = attrs.getAttributeValue(namespace, MMLayout.KEY_WIDTH);
                try {
                    if (!TextUtils.isEmpty(heightIn)) {
                        this.height = Integer.parseInt(heightIn);
                    }
                    if (!TextUtils.isEmpty(widthIn)) {
                        this.width = Integer.parseInt(widthIn);
                    }
                } catch (NumberFormatException e) {
                    MMLog.e(TAG, "Error reading attrs file from xml", e);
                }
                if (this.adImpl.mmRequest != null) {
                    this.adImpl.mmRequest.age = attrs.getAttributeValue(namespace, MMRequest.KEY_AGE);
                    this.adImpl.mmRequest.children = attrs.getAttributeValue(namespace, MMRequest.KEY_CHILDREN);
                    this.adImpl.mmRequest.education = attrs.getAttributeValue(namespace, MMRequest.KEY_EDUCATION);
                    this.adImpl.mmRequest.ethnicity = attrs.getAttributeValue(namespace, MMRequest.KEY_ETHNICITY);
                    this.adImpl.mmRequest.gender = attrs.getAttributeValue(namespace, MMRequest.KEY_GENDER);
                    this.adImpl.mmRequest.income = attrs.getAttributeValue(namespace, MMRequest.KEY_INCOME);
                    this.adImpl.mmRequest.keywords = attrs.getAttributeValue(namespace, MMRequest.KEY_KEYWORDS);
                    this.adImpl.mmRequest.marital = attrs.getAttributeValue(namespace, MMRequest.KEY_MARITAL_STATUS);
                    this.adImpl.mmRequest.politics = attrs.getAttributeValue(namespace, MMRequest.KEY_POLITICS);
                    this.adImpl.mmRequest.vendor = attrs.getAttributeValue(namespace, MMRequest.KEY_VENDOR);
                    this.adImpl.mmRequest.zip = attrs.getAttributeValue(namespace, MMRequest.KEY_ZIP_CODE);
                }
                this.goalId = attrs.getAttributeValue(namespace, "goalId");
            }
            init(context);
        }
    }

    private synchronized void attachToWindow(View view) {
        detachFromParent(view);
        Context context = getContext();
        if (context != null && context instanceof Activity) {
            Window win = ((Activity) context).getWindow();
            if (win != null) {
                View decorView = win.getDecorView();
                if (decorView != null && decorView instanceof ViewGroup) {
                    ((ViewGroup) decorView).addView(view);
                }
            }
        }
    }

    private void callSetTranslationX(int translationX) {
        try {
            View.class.getMethod("setTranslationX", new Class[]{Float.TYPE}).invoke(this, new Object[]{Integer.valueOf(translationX)});
        } catch (Exception e) {
            MMLog.e(TAG, "Unable to call setTranslationX", e);
        }
    }

    private void callSetTranslationY(int translationY) {
        try {
            View.class.getMethod("setTranslationY", new Class[]{Float.TYPE}).invoke(this, new Object[]{Integer.valueOf(translationY)});
        } catch (Exception e) {
            MMLog.e(TAG, "Unable to call setTranslationY", e);
        }
    }

    private synchronized void detachFromParent(View view) {
        if (view != null) {
            ViewParent parent = getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                if (view.getParent() != null) {
                    group.removeView(view);
                }
            }
        }
    }

    private void getAdInternal() {
        if (this.adImpl != null) {
            this.adImpl.requestAd();
        }
    }

    private boolean hasDefaultResizeParams() {
        return this.oldWidth == -50 && this.oldHeight == -50;
    }

    private void init(Context context) {
        setBackgroundColor(TRANSITION_NONE);
        this.adImpl.adType = "b";
        setOnClickListener(this);
        setFocusable(true);
        this.refreshAnimationimageView = new ImageView(context);
        this.refreshAnimationimageView.setScaleType(ScaleType.FIT_XY);
        this.refreshAnimationimageView.setVisibility(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
        addView(this.refreshAnimationimageView, new RelativeLayout.LayoutParams(-2, -2));
        setLayoutParams(new RelativeLayout.LayoutParams(-2, -2));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initEclipseAd(android.content.Context r17_context) {
        throw new UnsupportedOperationException("Method not decompiled: com.millennialmedia.android.MMAdView.initEclipseAd(android.content.Context):void");
        /*
        r16 = this;
        r10 = new android.widget.ImageView;
        r0 = r17;
        r10.<init>(r0);
        r8 = "http://images.millennialmedia.com/9513/192134.gif";
        r9 = 0;
        r11 = 0;
        r13 = "java.io.tmpdir";
        r5 = java.lang.System.getProperty(r13);	 Catch:{ Exception -> 0x00d9 }
        if (r5 == 0) goto L_0x002e;
    L_0x0013:
        r13 = java.io.File.separator;	 Catch:{ Exception -> 0x00d9 }
        r13 = r5.endsWith(r13);	 Catch:{ Exception -> 0x00d9 }
        if (r13 != 0) goto L_0x002e;
    L_0x001b:
        r13 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00d9 }
        r13.<init>();	 Catch:{ Exception -> 0x00d9 }
        r13 = r13.append(r5);	 Catch:{ Exception -> 0x00d9 }
        r14 = java.io.File.separator;	 Catch:{ Exception -> 0x00d9 }
        r13 = r13.append(r14);	 Catch:{ Exception -> 0x00d9 }
        r5 = r13.toString();	 Catch:{ Exception -> 0x00d9 }
    L_0x002e:
        r7 = new java.io.File;	 Catch:{ Exception -> 0x00d9 }
        r13 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00d9 }
        r13.<init>();	 Catch:{ Exception -> 0x00d9 }
        r13 = r13.append(r5);	 Catch:{ Exception -> 0x00d9 }
        r14 = "millenial355jns6u3l1nmedia.png";
        r13 = r13.append(r14);	 Catch:{ Exception -> 0x00d9 }
        r13 = r13.toString();	 Catch:{ Exception -> 0x00d9 }
        r7.<init>(r13);	 Catch:{ Exception -> 0x00d9 }
        r13 = r7.exists();	 Catch:{ Exception -> 0x00d9 }
        if (r13 != 0) goto L_0x0098;
    L_0x004c:
        r13 = new java.net.URL;	 Catch:{ Exception -> 0x00d9 }
        r14 = "http://images.millennialmedia.com/9513/192134.gif";
        r13.<init>(r14);	 Catch:{ Exception -> 0x00d9 }
        r4 = r13.openConnection();	 Catch:{ Exception -> 0x00d9 }
        r4 = (java.net.HttpURLConnection) r4;	 Catch:{ Exception -> 0x00d9 }
        r13 = 1;
        r4.setDoOutput(r13);	 Catch:{ Exception -> 0x00d9 }
        r13 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r4.setConnectTimeout(r13);	 Catch:{ Exception -> 0x00d9 }
        r4.connect();	 Catch:{ Exception -> 0x00d9 }
        r9 = r4.getInputStream();	 Catch:{ Exception -> 0x00d9 }
        r12 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x00d9 }
        r12.<init>(r7);	 Catch:{ Exception -> 0x00d9 }
        r13 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r2 = new byte[r13];	 Catch:{ Exception -> 0x007e, all -> 0x00d6 }
        r3 = 0;
    L_0x0073:
        r3 = r9.read(r2);	 Catch:{ Exception -> 0x007e, all -> 0x00d6 }
        if (r3 <= 0) goto L_0x0097;
    L_0x0079:
        r13 = 0;
        r12.write(r2, r13, r3);	 Catch:{ Exception -> 0x007e, all -> 0x00d6 }
        goto L_0x0073;
    L_0x007e:
        r6 = move-exception;
        r11 = r12;
    L_0x0080:
        r13 = "MMAdView";
        r14 = "Error with eclipse xml image display: ";
        com.millennialmedia.android.MMLog.e(r13, r14, r6);	 Catch:{ all -> 0x00bb }
        if (r9 == 0) goto L_0x008c;
    L_0x0089:
        r9.close();	 Catch:{ Exception -> 0x00d0 }
    L_0x008c:
        if (r11 == 0) goto L_0x0091;
    L_0x008e:
        r11.close();	 Catch:{ Exception -> 0x00d0 }
    L_0x0091:
        r0 = r16;
        r0.addView(r10);
        return;
    L_0x0097:
        r11 = r12;
    L_0x0098:
        r13 = r7.getAbsolutePath();	 Catch:{ Exception -> 0x00d9 }
        r1 = android.graphics.BitmapFactory.decodeFile(r13);	 Catch:{ Exception -> 0x00d9 }
        if (r10 == 0) goto L_0x00a7;
    L_0x00a2:
        if (r1 == 0) goto L_0x00a7;
    L_0x00a4:
        r10.setImageBitmap(r1);	 Catch:{ Exception -> 0x00d9 }
    L_0x00a7:
        if (r9 == 0) goto L_0x00ac;
    L_0x00a9:
        r9.close();	 Catch:{ Exception -> 0x00b2 }
    L_0x00ac:
        if (r11 == 0) goto L_0x0091;
    L_0x00ae:
        r11.close();	 Catch:{ Exception -> 0x00b2 }
        goto L_0x0091;
    L_0x00b2:
        r6 = move-exception;
        r13 = "MMAdView";
        r14 = "Error closing file";
    L_0x00b7:
        com.millennialmedia.android.MMLog.e(r13, r14, r6);
        goto L_0x0091;
    L_0x00bb:
        r13 = move-exception;
    L_0x00bc:
        if (r9 == 0) goto L_0x00c1;
    L_0x00be:
        r9.close();	 Catch:{ Exception -> 0x00c7 }
    L_0x00c1:
        if (r11 == 0) goto L_0x00c6;
    L_0x00c3:
        r11.close();	 Catch:{ Exception -> 0x00c7 }
    L_0x00c6:
        throw r13;
    L_0x00c7:
        r6 = move-exception;
        r14 = "MMAdView";
        r15 = "Error closing file";
        com.millennialmedia.android.MMLog.e(r14, r15, r6);
        goto L_0x00c6;
    L_0x00d0:
        r6 = move-exception;
        r13 = "MMAdView";
        r14 = "Error closing file";
        goto L_0x00b7;
    L_0x00d6:
        r13 = move-exception;
        r11 = r12;
        goto L_0x00bc;
    L_0x00d9:
        r6 = move-exception;
        goto L_0x0080;
        */
    }

    private void setUnresizeParameters() {
        if (hasDefaultResizeParams()) {
            LayoutParams oldParams = getLayoutParams();
            this.oldWidth = oldParams.width;
            this.oldHeight = oldParams.height;
            if (this.oldWidth <= 0) {
                this.oldWidth = getWidth();
            }
            if (this.oldHeight <= 0) {
                this.oldHeight = getHeight();
            }
        }
    }

    public /* bridge */ /* synthetic */ void addBlackView() {
        super.addBlackView();
    }

    void closeAreaTouched() {
        this.adImpl.unresizeToDefault();
    }

    public void getAd() {
        if (this.adImpl == null || this.adImpl.requestListener == null) {
            getAdInternal();
        } else {
            getAd(this.adImpl.requestListener);
        }
    }

    public void getAd(RequestListener listener) {
        if (this.adImpl != null) {
            this.adImpl.requestListener = listener;
        }
        getAdInternal();
    }

    public /* bridge */ /* synthetic */ String getApid() {
        return super.getApid();
    }

    public /* bridge */ /* synthetic */ boolean getIgnoresDensityScaling() {
        return super.getIgnoresDensityScaling();
    }

    public /* bridge */ /* synthetic */ RequestListener getListener() {
        return super.getListener();
    }

    public /* bridge */ /* synthetic */ MMRequest getMMRequest() {
        return super.getMMRequest();
    }

    synchronized void handleMraidResize(DTOResizeParameters resizeParams) {
        this.refreshAnimationimageView.setImageBitmap(null);
        if (MMSDK.hasSetTranslationMethod()) {
            if (this.view == null) {
                this.view = new ResizeView(getContext());
                this.view.setId(304025022);
                this.view.setLayoutParams(new RelativeLayout.LayoutParams(1, 1));
                this.view.setBackgroundColor(TRANSITION_NONE);
            }
            if (this.view.getParent() == null) {
                ViewParent adViewParent = getParent();
                if (adViewParent != null && adViewParent instanceof ViewGroup) {
                    ((ViewGroup) adViewParent).addView(this.view);
                }
            }
            BannerBounds bounds = new BannerBounds(resizeParams);
            if (!resizeParams.allowOffScreen) {
                bounds.calculateOnScreenBounds();
            }
            int[] location = new int[2];
            getLocationInWindow(location);
            attachToWindow(this);
            int[] locAfterAttach = new int[2];
            getLocationInWindow(locAfterAttach);
            setUnresizeParameters();
            int xOld = location[0] - locAfterAttach[0];
            int yOld = location[1] - locAfterAttach[1];
            bounds.modifyLayoutParams(getLayoutParams());
            callSetTranslationX(bounds.translationX + xOld);
            callSetTranslationY(bounds.translationY + yOld);
            setCloseArea(resizeParams.customClosePosition);
        }
    }

    synchronized void handleUnresize() {
        if (MMSDK.hasSetTranslationMethod()) {
            removeCloseTouchDelegate();
            if (!hasDefaultResizeParams()) {
                LayoutParams params = getLayoutParams();
                params.width = this.oldWidth;
                params.height = this.oldHeight;
                callSetTranslationX(TRANSITION_NONE);
                callSetTranslationY(TRANSITION_NONE);
                this.oldWidth = -50;
                this.oldHeight = -50;
            }
            if (this.view != null) {
                this.isResizing = true;
                this.view.attachToContext(this);
                ViewParent parent = getParent();
                if (parent != null && parent instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) parent;
                    if (this.view.getParent() != null) {
                        group.removeView(this.view);
                    }
                }
                this.isResizing = false;
            }
        }
    }

    @Deprecated
    public void onAnimationEnd(Animation animation) {
        this.refreshAnimationimageView.setVisibility(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
    }

    @Deprecated
    public void onAnimationRepeat(Animation animation) {
    }

    @Deprecated
    public void onAnimationStart(Animation animation) {
    }

    @Deprecated
    public void onClick(View v) {
        MMLog.d(TAG, "On click for " + v.getId() + " view, " + v + " adimpl" + this.adImpl);
        onTouchEvent(MotionEvent.obtain(0, System.currentTimeMillis(), TRANSITION_FADE, BitmapDescriptorFactory.HUE_RED, 0.0f, TRANSITION_NONE));
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        ThreadUtils.execute(new Runnable() {
            public void run() {
                float density = MMAdView.this.getContext().getResources().getDisplayMetrics().density;
                if (MMAdView.this.width <= 0) {
                    MMAdView.this.width = (int) (((float) MMAdView.this.getWidth()) / density);
                }
                if (MMAdView.this.height <= 0) {
                    MMAdView.this.height = (int) (((float) MMAdView.this.getHeight()) / density);
                }
            }
        });
    }

    public /* bridge */ /* synthetic */ boolean onTouchEvent(MotionEvent x0) {
        return super.onTouchEvent(x0);
    }

    public void onWindowFocusChanged(boolean windowInFocus) {
        super.onWindowFocusChanged(windowInFocus);
        if (windowInFocus && this.adImpl != null && this.adImpl.controller != null) {
            if (this.adImpl.controller.webView == null) {
                this.adImpl.controller.webView = MMAdImplController.getWebViewFromExistingAdImpl(this.adImpl);
            }
            MMWebView webView = this.adImpl.controller.webView;
            if (webView != null && !webView.isCurrentParent(this.adImpl.internalId) && !webView.mraidState.equals("expanded")) {
                webView.removeFromParent();
                addView(webView);
            }
        }
    }

    public /* bridge */ /* synthetic */ void removeBlackView() {
        super.removeBlackView();
    }

    public /* bridge */ /* synthetic */ void setApid(String x0) {
        super.setApid(x0);
    }

    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        if (this.adImpl != null && this.adImpl.controller != null && this.adImpl.controller.webView != null) {
            this.adImpl.controller.webView.setBackgroundColor(color);
        }
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public /* bridge */ /* synthetic */ void setIgnoresDensityScaling(boolean x0) {
        super.setIgnoresDensityScaling(x0);
    }

    public /* bridge */ /* synthetic */ void setListener(RequestListener x0) {
        super.setListener(x0);
    }

    public /* bridge */ /* synthetic */ void setMMRequest(MMRequest x0) {
        super.setMMRequest(x0);
    }

    public void setTransitionType(int type) {
        this.transitionType = type;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}