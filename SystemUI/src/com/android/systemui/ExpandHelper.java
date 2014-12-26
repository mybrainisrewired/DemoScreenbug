package com.android.systemui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.android.systemui.recent.RecentsCallback;
import com.android.systemui.statusbar.CommandQueue;

public class ExpandHelper implements Gefingerpoken, OnClickListener {
    protected static final boolean DEBUG = false;
    private static final long EXPAND_DURATION = 250;
    private static final float GLOW_BASE = 0.5f;
    private static final long GLOW_DURATION = 150;
    private static final float STRETCH_INTERVAL = 2.0f;
    private static final String TAG = "ExpandHelper";
    private static final boolean USE_DRAG = true;
    private static final boolean USE_SPAN = true;
    private Callback mCallback;
    private Context mContext;
    private View mCurrView;
    private View mCurrViewBottomGlow;
    private View mCurrViewTopGlow;
    private ScaleGestureDetector mDetector;
    private View mEventSource;
    private AnimatorSet mGlowAnimationSet;
    private ObjectAnimator mGlowBottomAnimation;
    private ObjectAnimator mGlowTopAnimation;
    private int mGravity;
    private float mInitialTouchFocusY;
    private float mInitialTouchSpan;
    private int mLargeSize;
    private float mMaximumStretch;
    private float mNaturalHeight;
    private float mOldHeight;
    private ObjectAnimator mScaleAnimation;
    private ViewScaler mScaler;
    private int mSmallSize;
    private boolean mStretching;

    public static interface Callback {
        boolean canChildBeExpanded(View view);

        View getChildAtPosition(float f, float f2);

        View getChildAtRawPosition(float f, float f2);

        boolean setUserExpandedChild(View view, boolean z);
    }

    private class ViewScaler {
        View mView;

        public float getHeight() {
            int height = this.mView.getLayoutParams().height;
            if (height < 0) {
                height = this.mView.getMeasuredHeight();
            }
            return (float) height;
        }

        public int getNaturalHeight(int maximum) {
            LayoutParams lp = this.mView.getLayoutParams();
            int oldHeight = lp.height;
            lp.height = -2;
            this.mView.setLayoutParams(lp);
            this.mView.measure(MeasureSpec.makeMeasureSpec(this.mView.getMeasuredWidth(), 1073741824), MeasureSpec.makeMeasureSpec(maximum, Integer.MIN_VALUE));
            lp.height = oldHeight;
            this.mView.setLayoutParams(lp);
            return this.mView.getMeasuredHeight();
        }

        public void setHeight(float h) {
            LayoutParams lp = this.mView.getLayoutParams();
            lp.height = (int) h;
            this.mView.setLayoutParams(lp);
            this.mView.requestLayout();
        }

        public void setView(View v) {
            this.mView = v;
        }
    }

    public ExpandHelper(Context context, Callback callback, int small, int large) {
        this.mSmallSize = small;
        this.mMaximumStretch = ((float) this.mSmallSize) * 2.0f;
        this.mLargeSize = large;
        this.mContext = context;
        this.mCallback = callback;
        this.mScaler = new ViewScaler();
        this.mGravity = 48;
        this.mScaleAnimation = ObjectAnimator.ofFloat(this.mScaler, "height", new float[]{0.0f});
        this.mScaleAnimation.setDuration(EXPAND_DURATION);
        AnimatorListenerAdapter glowVisibilityController = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                View target = (View) ((ObjectAnimator) animation).getTarget();
                if (target.getAlpha() <= 0.0f) {
                    target.setVisibility(CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
                }
            }

            public void onAnimationStart(Animator animation) {
                View target = (View) ((ObjectAnimator) animation).getTarget();
                if (target.getAlpha() <= 0.0f) {
                    target.setVisibility(0);
                }
            }
        };
        this.mGlowTopAnimation = ObjectAnimator.ofFloat(null, "alpha", new float[]{0.0f});
        this.mGlowTopAnimation.addListener(glowVisibilityController);
        this.mGlowBottomAnimation = ObjectAnimator.ofFloat(null, "alpha", new float[]{0.0f});
        this.mGlowBottomAnimation.addListener(glowVisibilityController);
        this.mGlowAnimationSet = new AnimatorSet();
        this.mGlowAnimationSet.play(this.mGlowTopAnimation).with(this.mGlowBottomAnimation);
        this.mGlowAnimationSet.setDuration(GLOW_DURATION);
        this.mDetector = new ScaleGestureDetector(context, new SimpleOnScaleGestureListener() {
            public boolean onScale(ScaleGestureDetector detector) {
                float f;
                float span = (Math.abs(detector.getCurrentSpan()) - ExpandHelper.this.mInitialTouchSpan) * 1.0f;
                float drag = (detector.getFocusY() - ExpandHelper.this.mInitialTouchFocusY) * 1.0f;
                if (ExpandHelper.this.mGravity == 80) {
                    f = -1.0f;
                } else {
                    f = 1.0f;
                }
                drag *= f;
                float pull = Math.abs(drag) + Math.abs(span) + 1.0f;
                float hand = (Math.abs(drag) * drag) / pull + (Math.abs(span) * span) / pull + ExpandHelper.this.mOldHeight;
                float target = hand;
                if (hand < ((float) ExpandHelper.this.mSmallSize)) {
                    hand = (float) ExpandHelper.this.mSmallSize;
                } else if (hand > ((float) ExpandHelper.this.mLargeSize)) {
                    hand = (float) ExpandHelper.this.mLargeSize;
                }
                if (hand > ExpandHelper.this.mNaturalHeight) {
                    hand = ExpandHelper.this.mNaturalHeight;
                }
                ExpandHelper.this.mScaler.setHeight(hand);
                ExpandHelper.this.setGlow((1.0f / (((float) Math.pow(2.718281828459045d, (double) (((8.0f * Math.abs((target - hand) / ExpandHelper.this.mMaximumStretch)) - 5.0f) * -1.0f))) + 1.0f)) * 0.5f + 0.5f);
                return USE_SPAN;
            }

            public boolean onScaleBegin(ScaleGestureDetector detector) {
                View v;
                float x = detector.getFocusX();
                float y = detector.getFocusY();
                if (ExpandHelper.this.mEventSource != null) {
                    int[] location = new int[2];
                    ExpandHelper.this.mEventSource.getLocationOnScreen(location);
                    v = ExpandHelper.this.mCallback.getChildAtRawPosition(x + ((float) location[0]), y + ((float) location[1]));
                } else {
                    v = ExpandHelper.this.mCallback.getChildAtPosition(x, y);
                }
                ExpandHelper.this.mInitialTouchFocusY = detector.getFocusY();
                ExpandHelper.this.mInitialTouchSpan = Math.abs(detector.getCurrentSpan());
                ExpandHelper.this.mStretching = ExpandHelper.this.initScale(v);
                return ExpandHelper.this.mStretching;
            }

            public void onScaleEnd(ScaleGestureDetector detector) {
                ExpandHelper.this.finishScale(DEBUG);
            }
        });
    }

    private void clearView() {
        this.mCurrView = null;
        this.mCurrViewTopGlow = null;
        this.mCurrViewBottomGlow = null;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void finishScale(boolean r8_force) {
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.ExpandHelper.finishScale(boolean):void");
        /*
        r7 = this;
        r2 = 1;
        r3 = 0;
        r4 = r7.mScaler;
        r0 = r4.getHeight();
        r4 = r7.mOldHeight;
        r5 = r7.mSmallSize;
        r5 = (float) r5;
        r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1));
        if (r4 != 0) goto L_0x0056;
    L_0x0011:
        r1 = r2;
    L_0x0012:
        if (r1 == 0) goto L_0x005c;
    L_0x0014:
        if (r8 != 0) goto L_0x001d;
    L_0x0016:
        r4 = r7.mSmallSize;
        r4 = (float) r4;
        r4 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1));
        if (r4 <= 0) goto L_0x0058;
    L_0x001d:
        r0 = r7.mNaturalHeight;
    L_0x001f:
        r4 = r7.mScaleAnimation;
        r4 = r4.isRunning();
        if (r4 == 0) goto L_0x002c;
    L_0x0027:
        r4 = r7.mScaleAnimation;
        r4.cancel();
    L_0x002c:
        r4 = r7.mScaleAnimation;
        r5 = new float[r2];
        r5[r3] = r0;
        r4.setFloatValues(r5);
        r4 = r7.mScaleAnimation;
        r4.setupStartValues();
        r4 = r7.mScaleAnimation;
        r4.start();
        r7.mStretching = r3;
        r4 = 0;
        r7.setGlow(r4);
        r4 = r7.mCallback;
        r5 = r7.mCurrView;
        r6 = r7.mNaturalHeight;
        r6 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1));
        if (r6 != 0) goto L_0x006b;
    L_0x004f:
        r4.setUserExpandedChild(r5, r2);
        r7.clearView();
        return;
    L_0x0056:
        r1 = r3;
        goto L_0x0012;
    L_0x0058:
        r4 = r7.mSmallSize;
        r0 = (float) r4;
        goto L_0x001f;
    L_0x005c:
        if (r8 != 0) goto L_0x0064;
    L_0x005e:
        r4 = r7.mNaturalHeight;
        r4 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1));
        if (r4 >= 0) goto L_0x0068;
    L_0x0064:
        r4 = r7.mSmallSize;
        r0 = (float) r4;
    L_0x0067:
        goto L_0x001f;
    L_0x0068:
        r0 = r7.mNaturalHeight;
        goto L_0x0067;
    L_0x006b:
        r2 = r3;
        goto L_0x004f;
        */
    }

    private void handleGlowVisibility() {
        int i;
        int i2 = CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL;
        View view = this.mCurrViewTopGlow;
        if (this.mCurrViewTopGlow.getAlpha() <= 0.0f) {
            i = 4;
        } else {
            i = 0;
        }
        view.setVisibility(i);
        View view2 = this.mCurrViewBottomGlow;
        if (this.mCurrViewBottomGlow.getAlpha() > 0.0f) {
            i2 = 0;
        }
        view2.setVisibility(i2);
    }

    private boolean initScale(View v) {
        if (v != null) {
            this.mStretching = true;
            setView(v);
            setGlow(GLOW_BASE);
            this.mScaler.setView(v);
            this.mOldHeight = this.mScaler.getHeight();
            if (this.mCallback.canChildBeExpanded(v)) {
                this.mNaturalHeight = (float) this.mScaler.getNaturalHeight(this.mLargeSize);
            } else {
                this.mNaturalHeight = this.mOldHeight;
            }
            v.getParent().requestDisallowInterceptTouchEvent(USE_SPAN);
        }
        return this.mStretching;
    }

    private void setView(View v) {
        this.mCurrView = v;
        if (v instanceof ViewGroup) {
            ViewGroup g = (ViewGroup) v;
            this.mCurrViewTopGlow = g.findViewById(R.id.top_glow);
            this.mCurrViewBottomGlow = g.findViewById(R.id.bottom_glow);
        }
    }

    public void onClick(View v) {
        initScale(v);
        finishScale(USE_SPAN);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        this.mDetector.onTouchEvent(ev);
        return this.mStretching;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (this.mStretching) {
            this.mDetector.onTouchEvent(ev);
        }
        switch (action) {
            case CommandQueue.FLAG_EXCLUDE_SEARCH_PANEL:
            case RecentsCallback.SWIPE_DOWN:
                this.mStretching = false;
                clearView();
                break;
        }
        return USE_SPAN;
    }

    public void setEventSource(View eventSource) {
        this.mEventSource = eventSource;
    }

    public void setGlow(float glow) {
        if (!this.mGlowAnimationSet.isRunning() || glow == 0.0f) {
            if (this.mGlowAnimationSet.isRunning()) {
                this.mGlowAnimationSet.cancel();
            }
            if (this.mCurrViewTopGlow != null && this.mCurrViewBottomGlow != null) {
                if (glow == 0.0f || this.mCurrViewTopGlow.getAlpha() == 0.0f) {
                    this.mGlowTopAnimation.setTarget(this.mCurrViewTopGlow);
                    this.mGlowBottomAnimation.setTarget(this.mCurrViewBottomGlow);
                    this.mGlowTopAnimation.setFloatValues(new float[]{glow});
                    this.mGlowBottomAnimation.setFloatValues(new float[]{glow});
                    this.mGlowAnimationSet.setupStartValues();
                    this.mGlowAnimationSet.start();
                } else {
                    this.mCurrViewTopGlow.setAlpha(glow);
                    this.mCurrViewBottomGlow.setAlpha(glow);
                    handleGlowVisibility();
                }
            }
        }
    }

    public void setGravity(int gravity) {
        this.mGravity = gravity;
    }
}