package com.android.systemui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.RectF;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import com.android.systemui.recent.RecentsCallback;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.tablet.TabletStatusBar;

public class SwipeHelper implements Gefingerpoken {
    static final float ALPHA_FADE_END = 0.5f;
    public static float ALPHA_FADE_START = 0.0f;
    private static final boolean CONSTRAIN_SWIPE = true;
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_INVALIDATE = false;
    private static final boolean DISMISS_IF_SWIPED_FAR_ENOUGH = true;
    private static final boolean FADE_OUT_DURING_SWIPE = true;
    private static final boolean SLOW_ANIMATIONS = false;
    private static final int SNAP_ANIM_LEN = 150;
    static final String TAG = "com.android.systemui.SwipeHelper";
    public static final int X = 0;
    public static final int Y = 1;
    private static LinearInterpolator sLinearInterpolator;
    private int DEFAULT_ESCAPE_ANIMATION_DURATION;
    private int MAX_DISMISS_VELOCITY;
    private int MAX_ESCAPE_ANIMATION_DURATION;
    private float SWIPE_ESCAPE_VELOCITY;
    private Callback mCallback;
    private boolean mCanCurrViewBeDimissed;
    private View mCurrAnimView;
    private View mCurrView;
    private float mDensityScale;
    private boolean mDragging;
    private Handler mHandler;
    private float mInitialTouchPos;
    private OnLongClickListener mLongPressListener;
    private boolean mLongPressSent;
    private long mLongPressTimeout;
    private float mMinAlpha;
    private float mPagingTouchSlop;
    private int mSwipeDirection;
    private VelocityTracker mVelocityTracker;
    private Runnable mWatchLongPress;

    class AnonymousClass_2 extends AnimatorListenerAdapter {
        final /* synthetic */ View val$animView;
        final /* synthetic */ View val$view;

        AnonymousClass_2(View view, View view2) {
            this.val$view = view;
            this.val$animView = view2;
        }

        public void onAnimationEnd(Animator animation) {
            SwipeHelper.this.mCallback.onChildDismissed(this.val$view);
            this.val$animView.setLayerType(X, null);
        }
    }

    class AnonymousClass_3 implements AnimatorUpdateListener {
        final /* synthetic */ View val$animView;
        final /* synthetic */ boolean val$canAnimViewBeDismissed;

        AnonymousClass_3(boolean z, View view) {
            this.val$canAnimViewBeDismissed = z;
            this.val$animView = view;
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            if (this.val$canAnimViewBeDismissed) {
                this.val$animView.setAlpha(SwipeHelper.this.getAlphaForOffset(this.val$animView));
            }
            SwipeHelper.invalidateGlobalRegion(this.val$animView);
        }
    }

    class AnonymousClass_4 implements AnimatorUpdateListener {
        final /* synthetic */ View val$animView;
        final /* synthetic */ boolean val$canAnimViewBeDismissed;

        AnonymousClass_4(boolean z, View view) {
            this.val$canAnimViewBeDismissed = z;
            this.val$animView = view;
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            if (this.val$canAnimViewBeDismissed) {
                this.val$animView.setAlpha(SwipeHelper.this.getAlphaForOffset(this.val$animView));
            }
            SwipeHelper.invalidateGlobalRegion(this.val$animView);
        }
    }

    public static interface Callback {
        boolean canChildBeDismissed(View view);

        View getChildAtPosition(MotionEvent motionEvent);

        View getChildContentView(View view);

        void onBeginDrag(View view);

        void onChildDismissed(View view);

        void onDragCancelled(View view);
    }

    static {
        sLinearInterpolator = new LinearInterpolator();
        ALPHA_FADE_START = 0.0f;
    }

    public SwipeHelper(int swipeDirection, Callback callback, float densityScale, float pagingTouchSlop) {
        this.SWIPE_ESCAPE_VELOCITY = 100.0f;
        this.DEFAULT_ESCAPE_ANIMATION_DURATION = 200;
        this.MAX_ESCAPE_ANIMATION_DURATION = 400;
        this.MAX_DISMISS_VELOCITY = 2000;
        this.mMinAlpha = 0.0f;
        this.mCallback = callback;
        this.mHandler = new Handler();
        this.mSwipeDirection = swipeDirection;
        this.mVelocityTracker = VelocityTracker.obtain();
        this.mDensityScale = densityScale;
        this.mPagingTouchSlop = pagingTouchSlop;
        this.mLongPressTimeout = (long) (((float) ViewConfiguration.getLongPressTimeout()) * 1.5f);
    }

    private ObjectAnimator createTranslationAnimation(View v, float newPos) {
        return ObjectAnimator.ofFloat(v, this.mSwipeDirection == 0 ? "translationX" : "translationY", new float[]{newPos});
    }

    private float getAlphaForOffset(View view) {
        float viewSize = getSize(view);
        float fadeSize = 0.5f * viewSize;
        float result = 1.0f;
        float pos = getTranslation(view);
        if (pos >= ALPHA_FADE_START * viewSize) {
            result = 1.0f - (pos - (ALPHA_FADE_START * viewSize)) / fadeSize;
        } else if (pos < (1.0f - ALPHA_FADE_START) * viewSize) {
            result = 1.0f + ((ALPHA_FADE_START * viewSize) + pos) / fadeSize;
        }
        return Math.max(this.mMinAlpha, result);
    }

    private float getPerpendicularVelocity(VelocityTracker vt) {
        return this.mSwipeDirection == 0 ? vt.getYVelocity() : vt.getXVelocity();
    }

    private float getPos(MotionEvent ev) {
        return this.mSwipeDirection == 0 ? ev.getX() : ev.getY();
    }

    private float getSize(View v) {
        return this.mSwipeDirection == 0 ? (float) v.getMeasuredWidth() : (float) v.getMeasuredHeight();
    }

    private float getTranslation(View v) {
        return this.mSwipeDirection == 0 ? v.getTranslationX() : v.getTranslationY();
    }

    private float getVelocity(VelocityTracker vt) {
        return this.mSwipeDirection == 0 ? vt.getXVelocity() : vt.getYVelocity();
    }

    public static void invalidateGlobalRegion(View view) {
        invalidateGlobalRegion(view, new RectF((float) view.getLeft(), (float) view.getTop(), (float) view.getRight(), (float) view.getBottom()));
    }

    public static void invalidateGlobalRegion(View view, RectF childBounds) {
        while (view.getParent() != null && view.getParent() instanceof View) {
            view = view.getParent();
            view.getMatrix().mapRect(childBounds);
            view.invalidate((int) Math.floor((double) childBounds.left), (int) Math.floor((double) childBounds.top), (int) Math.ceil((double) childBounds.right), (int) Math.ceil((double) childBounds.bottom));
        }
    }

    private void setTranslation(View v, float translate) {
        if (this.mSwipeDirection == 0) {
            v.setTranslationX(translate);
        } else {
            v.setTranslationY(translate);
        }
    }

    public void dismissChild(View view, float velocity) {
        float newPos;
        int duration;
        ObjectAnimator anim;
        View animView = this.mCallback.getChildContentView(view);
        boolean canAnimViewBeDismissed = this.mCallback.canChildBeDismissed(view);
        if (velocity >= 0.0f) {
            if ((velocity != 0.0f || getTranslation(animView) >= 0.0f) && !(velocity == 0.0f && getTranslation(animView) == 0.0f && this.mSwipeDirection == 1)) {
                newPos = getSize(animView);
                duration = this.MAX_ESCAPE_ANIMATION_DURATION;
                if (velocity == 0.0f) {
                    duration = Math.min(duration, (int) ((Math.abs(newPos - getTranslation(animView)) * 1000.0f) / Math.abs(velocity)));
                } else {
                    duration = this.DEFAULT_ESCAPE_ANIMATION_DURATION;
                }
                animView.setLayerType(CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL, null);
                anim = createTranslationAnimation(animView, newPos);
                anim.setInterpolator(sLinearInterpolator);
                anim.setDuration((long) duration);
                anim.addListener(new AnonymousClass_2(view, animView));
                anim.addUpdateListener(new AnonymousClass_3(canAnimViewBeDismissed, animView));
                anim.start();
            }
        }
        newPos = -getSize(animView);
        duration = this.MAX_ESCAPE_ANIMATION_DURATION;
        if (velocity == 0.0f) {
            duration = this.DEFAULT_ESCAPE_ANIMATION_DURATION;
        } else {
            duration = Math.min(duration, (int) ((Math.abs(newPos - getTranslation(animView)) * 1000.0f) / Math.abs(velocity)));
        }
        animView.setLayerType(CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL, null);
        anim = createTranslationAnimation(animView, newPos);
        anim.setInterpolator(sLinearInterpolator);
        anim.setDuration((long) duration);
        anim.addListener(new AnonymousClass_2(view, animView));
        anim.addUpdateListener(new AnonymousClass_3(canAnimViewBeDismissed, animView));
        anim.start();
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case X:
                this.mDragging = false;
                this.mLongPressSent = false;
                this.mCurrView = this.mCallback.getChildAtPosition(ev);
                this.mVelocityTracker.clear();
                if (this.mCurrView != null) {
                    this.mCurrAnimView = this.mCallback.getChildContentView(this.mCurrView);
                    this.mCanCurrViewBeDimissed = this.mCallback.canChildBeDismissed(this.mCurrView);
                    this.mVelocityTracker.addMovement(ev);
                    this.mInitialTouchPos = getPos(ev);
                    if (this.mLongPressListener != null) {
                        if (this.mWatchLongPress == null) {
                            this.mWatchLongPress = new Runnable() {
                                public void run() {
                                    if (SwipeHelper.this.mCurrView != null && !SwipeHelper.this.mLongPressSent) {
                                        SwipeHelper.this.mLongPressSent = FADE_OUT_DURING_SWIPE;
                                        SwipeHelper.this.mCurrView.sendAccessibilityEvent(CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL);
                                        SwipeHelper.this.mLongPressListener.onLongClick(SwipeHelper.this.mCurrView);
                                    }
                                }
                            };
                        }
                        this.mHandler.postDelayed(this.mWatchLongPress, this.mLongPressTimeout);
                    }
                }
                break;
            case Y:
            case RecentsCallback.SWIPE_DOWN:
                this.mDragging = false;
                this.mCurrView = null;
                this.mCurrAnimView = null;
                this.mLongPressSent = false;
                removeLongPressCallback();
                break;
            case CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL:
                if (!(this.mCurrView == null || this.mLongPressSent)) {
                    this.mVelocityTracker.addMovement(ev);
                    if (Math.abs(getPos(ev) - this.mInitialTouchPos) > this.mPagingTouchSlop) {
                        this.mCallback.onBeginDrag(this.mCurrView);
                        this.mDragging = true;
                        this.mInitialTouchPos = getPos(ev) - getTranslation(this.mCurrAnimView);
                        removeLongPressCallback();
                    }
                }
                break;
        }
        return this.mDragging;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mLongPressSent) {
            return FADE_OUT_DURING_SWIPE;
        }
        if (this.mDragging) {
            this.mVelocityTracker.addMovement(ev);
            switch (ev.getAction()) {
                case Y:
                case RecentsCallback.SWIPE_DOWN:
                    if (this.mCurrView != null) {
                        boolean childSwipedFastEnough;
                        boolean dismissChild;
                        View view;
                        this.mVelocityTracker.computeCurrentVelocity(TabletStatusBar.MSG_OPEN_NOTIFICATION_PANEL, ((float) this.MAX_DISMISS_VELOCITY) * this.mDensityScale);
                        float escapeVelocity = this.SWIPE_ESCAPE_VELOCITY * this.mDensityScale;
                        float velocity = getVelocity(this.mVelocityTracker);
                        float perpendicularVelocity = getPerpendicularVelocity(this.mVelocityTracker);
                        double abs = (double) Math.abs(getTranslation(this.mCurrAnimView));
                        View view2 = this.mCurrAnimView;
                        boolean childSwipedFarEnough = abs > 0.4d * ((double) getSize(view2)) ? FADE_OUT_DURING_SWIPE : SLOW_ANIMATIONS;
                        if (Math.abs(velocity) > escapeVelocity && Math.abs(velocity) > Math.abs(perpendicularVelocity)) {
                            if ((velocity > 0.0f ? Y : X) == (getTranslation(this.mCurrAnimView) > 0.0f ? Y : X)) {
                                childSwipedFastEnough = FADE_OUT_DURING_SWIPE;
                                dismissChild = (this.mCallback.canChildBeDismissed(this.mCurrView) && (childSwipedFastEnough || childSwipedFarEnough)) ? FADE_OUT_DURING_SWIPE : SLOW_ANIMATIONS;
                                if (dismissChild) {
                                    this.mCallback.onDragCancelled(this.mCurrView);
                                    snapChild(this.mCurrView, velocity);
                                } else {
                                    view = this.mCurrView;
                                    if (!childSwipedFastEnough) {
                                        velocity = 0.0f;
                                    }
                                    dismissChild(view, velocity);
                                }
                            }
                        }
                        childSwipedFastEnough = SLOW_ANIMATIONS;
                        if (dismissChild) {
                            this.mCallback.onDragCancelled(this.mCurrView);
                            snapChild(this.mCurrView, velocity);
                        } else {
                            view = this.mCurrView;
                            if (childSwipedFastEnough) {
                                velocity = 0.0f;
                            }
                            dismissChild(view, velocity);
                        }
                    }
                    break;
                case CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL:
                case CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL:
                    if (this.mCurrView != null) {
                        float delta = getPos(ev) - this.mInitialTouchPos;
                        if (!this.mCallback.canChildBeDismissed(this.mCurrView)) {
                            float size = getSize(this.mCurrAnimView);
                            float maxScrollDistance = 0.15f * size;
                            delta = Math.abs(delta) >= size ? delta > 0.0f ? maxScrollDistance : -maxScrollDistance : maxScrollDistance * ((float) Math.sin(((double) (delta / size)) * 1.5707963267948966d));
                        }
                        setTranslation(this.mCurrAnimView, delta);
                        if (this.mCanCurrViewBeDimissed) {
                            this.mCurrAnimView.setAlpha(getAlphaForOffset(this.mCurrAnimView));
                        }
                        invalidateGlobalRegion(this.mCurrView);
                    }
                    break;
            }
            return FADE_OUT_DURING_SWIPE;
        } else {
            removeLongPressCallback();
            return SLOW_ANIMATIONS;
        }
    }

    public void removeLongPressCallback() {
        if (this.mWatchLongPress != null) {
            this.mHandler.removeCallbacks(this.mWatchLongPress);
            this.mWatchLongPress = null;
        }
    }

    public void setDensityScale(float densityScale) {
        this.mDensityScale = densityScale;
    }

    public void setLongPressListener(OnLongClickListener listener) {
        this.mLongPressListener = listener;
    }

    public void setMinAlpha(float minAlpha) {
        this.mMinAlpha = minAlpha;
    }

    public void setPagingTouchSlop(float pagingTouchSlop) {
        this.mPagingTouchSlop = pagingTouchSlop;
    }

    public void snapChild(View view, float velocity) {
        View animView = this.mCallback.getChildContentView(view);
        boolean canAnimViewBeDismissed = this.mCallback.canChildBeDismissed(animView);
        ObjectAnimator anim = createTranslationAnimation(animView, 0.0f);
        anim.setDuration((long) 150);
        anim.addUpdateListener(new AnonymousClass_4(canAnimViewBeDismissed, animView));
        anim.start();
    }
}