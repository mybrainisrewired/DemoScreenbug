package android.support.v4.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class SwipeRefreshLayout extends ViewGroup {
    private static final float ACCELERATE_INTERPOLATION_FACTOR = 1.5f;
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2.0f;
    private static final int INVALID_POINTER = -1;
    private static final int[] LAYOUT_ATTRS;
    private static final String LOG_TAG;
    private static final float MAX_SWIPE_DISTANCE_FACTOR = 0.6f;
    private static final float PROGRESS_BAR_HEIGHT = 4.0f;
    private static final int REFRESH_TRIGGER_DISTANCE = 120;
    private static final long RETURN_TO_ORIGINAL_POSITION_TIMEOUT = 300;
    private final AccelerateInterpolator mAccelerateInterpolator;
    private int mActivePointerId;
    private final Animation mAnimateToStartPosition;
    private final Runnable mCancel;
    private float mCurrPercentage;
    private int mCurrentTargetOffsetTop;
    private final DecelerateInterpolator mDecelerateInterpolator;
    private float mDistanceToTriggerSync;
    private int mFrom;
    private float mFromPercentage;
    private float mInitialMotionY;
    private boolean mIsBeingDragged;
    private float mLastMotionY;
    private OnRefreshListener mListener;
    private int mMediumAnimationDuration;
    private int mOriginalOffsetTop;
    private SwipeProgressBar mProgressBar;
    private int mProgressBarHeight;
    private boolean mRefreshing;
    private final Runnable mReturnToStartPosition;
    private final AnimationListener mReturnToStartPositionListener;
    private boolean mReturningToStart;
    private final AnimationListener mShrinkAnimationListener;
    private Animation mShrinkTrigger;
    private View mTarget;
    private int mTouchSlop;

    private class BaseAnimationListener implements AnimationListener {
        private BaseAnimationListener() {
        }

        public void onAnimationEnd(Animation animation) {
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    public static interface OnRefreshListener {
        void onRefresh();
    }

    static {
        LOG_TAG = SwipeRefreshLayout.class.getSimpleName();
        LAYOUT_ATTRS = new int[]{16842766};
    }

    public SwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public SwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mRefreshing = false;
        this.mDistanceToTriggerSync = -1.0f;
        this.mFromPercentage = 0.0f;
        this.mCurrPercentage = 0.0f;
        this.mActivePointerId = -1;
        this.mAnimateToStartPosition = new Animation() {
            public void applyTransformation(float interpolatedTime, Transformation t) {
                int targetTop = 0;
                if (SwipeRefreshLayout.this.mFrom != SwipeRefreshLayout.this.mOriginalOffsetTop) {
                    targetTop = SwipeRefreshLayout.this.mFrom + ((int) (((float) (SwipeRefreshLayout.this.mOriginalOffsetTop - SwipeRefreshLayout.this.mFrom)) * interpolatedTime));
                }
                int offset = targetTop - SwipeRefreshLayout.this.mTarget.getTop();
                int currentTop = SwipeRefreshLayout.this.mTarget.getTop();
                if (offset + currentTop < 0) {
                    offset = 0 - currentTop;
                }
                SwipeRefreshLayout.this.setTargetOffsetTopAndBottom(offset);
            }
        };
        this.mShrinkTrigger = new Animation() {
            public void applyTransformation(float interpolatedTime, Transformation t) {
                SwipeRefreshLayout.this.mProgressBar.setTriggerPercentage(SwipeRefreshLayout.this.mFromPercentage + (0.0f - SwipeRefreshLayout.this.mFromPercentage) * interpolatedTime);
            }
        };
        this.mReturnToStartPositionListener = new BaseAnimationListener() {
            {
                super(null);
            }

            public void onAnimationEnd(Animation animation) {
                SwipeRefreshLayout.this.mCurrentTargetOffsetTop = 0;
            }
        };
        this.mShrinkAnimationListener = new BaseAnimationListener() {
            {
                super(null);
            }

            public void onAnimationEnd(Animation animation) {
                SwipeRefreshLayout.this.mCurrPercentage = BitmapDescriptorFactory.HUE_RED;
            }
        };
        this.mReturnToStartPosition = new Runnable() {
            public void run() {
                SwipeRefreshLayout.this.mReturningToStart = true;
                SwipeRefreshLayout.this.animateOffsetToStartPosition(SwipeRefreshLayout.this.mCurrentTargetOffsetTop + SwipeRefreshLayout.this.getPaddingTop(), SwipeRefreshLayout.this.mReturnToStartPositionListener);
            }
        };
        this.mCancel = new Runnable() {
            public void run() {
                SwipeRefreshLayout.this.mReturningToStart = true;
                if (SwipeRefreshLayout.this.mProgressBar != null) {
                    SwipeRefreshLayout.this.mFromPercentage = SwipeRefreshLayout.this.mCurrPercentage;
                    SwipeRefreshLayout.this.mShrinkTrigger.setDuration((long) SwipeRefreshLayout.this.mMediumAnimationDuration);
                    SwipeRefreshLayout.this.mShrinkTrigger.setAnimationListener(SwipeRefreshLayout.this.mShrinkAnimationListener);
                    SwipeRefreshLayout.this.mShrinkTrigger.reset();
                    SwipeRefreshLayout.this.mShrinkTrigger.setInterpolator(SwipeRefreshLayout.this.mDecelerateInterpolator);
                    SwipeRefreshLayout.this.startAnimation(SwipeRefreshLayout.this.mShrinkTrigger);
                }
                SwipeRefreshLayout.this.animateOffsetToStartPosition(SwipeRefreshLayout.this.mCurrentTargetOffsetTop + SwipeRefreshLayout.this.getPaddingTop(), SwipeRefreshLayout.this.mReturnToStartPositionListener);
            }
        };
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mMediumAnimationDuration = getResources().getInteger(17694721);
        setWillNotDraw(false);
        this.mProgressBar = new SwipeProgressBar(this);
        this.mProgressBarHeight = (int) (getResources().getDisplayMetrics().density * 4.0f);
        this.mDecelerateInterpolator = new DecelerateInterpolator(2.0f);
        this.mAccelerateInterpolator = new AccelerateInterpolator(1.5f);
        TypedArray a = context.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
        setEnabled(a.getBoolean(0, true));
        a.recycle();
    }

    private void animateOffsetToStartPosition(int from, AnimationListener listener) {
        this.mFrom = from;
        this.mAnimateToStartPosition.reset();
        this.mAnimateToStartPosition.setDuration((long) this.mMediumAnimationDuration);
        this.mAnimateToStartPosition.setAnimationListener(listener);
        this.mAnimateToStartPosition.setInterpolator(this.mDecelerateInterpolator);
        this.mTarget.startAnimation(this.mAnimateToStartPosition);
    }

    private void ensureTarget() {
        if (this.mTarget == null) {
            if (getChildCount() <= 1 || isInEditMode()) {
                this.mTarget = getChildAt(0);
                this.mOriginalOffsetTop = this.mTarget.getTop() + getPaddingTop();
            } else {
                throw new IllegalStateException("SwipeRefreshLayout can host only one direct child");
            }
        }
        if (this.mDistanceToTriggerSync == -1.0f && getParent() != null && ((View) getParent()).getHeight() > 0) {
            this.mDistanceToTriggerSync = (float) ((int) Math.min(((float) ((View) getParent()).getHeight()) * 0.6f, 120.0f * getResources().getDisplayMetrics().density));
        }
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = MotionEventCompat.getActionIndex(ev);
        if (MotionEventCompat.getPointerId(ev, pointerIndex) == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mLastMotionY = MotionEventCompat.getY(ev, newPointerIndex);
            this.mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

    private void setTargetOffsetTopAndBottom(int offset) {
        this.mTarget.offsetTopAndBottom(offset);
        this.mCurrentTargetOffsetTop = this.mTarget.getTop();
    }

    private void setTriggerPercentage(float percent) {
        if (percent == 0.0f) {
            this.mCurrPercentage = 0.0f;
        } else {
            this.mCurrPercentage = percent;
            this.mProgressBar.setTriggerPercentage(percent);
        }
    }

    private void startRefresh() {
        removeCallbacks(this.mCancel);
        this.mReturnToStartPosition.run();
        setRefreshing(true);
        this.mListener.onRefresh();
    }

    private void updateContentOffsetTop(int targetTop) {
        int currentTop = this.mTarget.getTop();
        if (((float) targetTop) > this.mDistanceToTriggerSync) {
            targetTop = (int) this.mDistanceToTriggerSync;
        } else if (targetTop < 0) {
            targetTop = 0;
        }
        setTargetOffsetTopAndBottom(targetTop - currentTop);
    }

    private void updatePositionTimeout() {
        removeCallbacks(this.mCancel);
        postDelayed(this.mCancel, RETURN_TO_ORIGINAL_POSITION_TIMEOUT);
    }

    public boolean canChildScrollUp() {
        if (VERSION.SDK_INT >= 14) {
            return ViewCompat.canScrollVertically(this.mTarget, INVALID_POINTER);
        }
        if (!(this.mTarget instanceof AbsListView)) {
            return this.mTarget.getScrollY() > 0;
        } else {
            AbsListView absListView = (AbsListView) this.mTarget;
            return absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.mProgressBar.draw(canvas);
    }

    public boolean isRefreshing() {
        return this.mRefreshing;
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        removeCallbacks(this.mCancel);
        removeCallbacks(this.mReturnToStartPosition);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this.mReturnToStartPosition);
        removeCallbacks(this.mCancel);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTarget();
        int action = MotionEventCompat.getActionMasked(ev);
        if (this.mReturningToStart && action == 0) {
            this.mReturningToStart = false;
        }
        if (!isEnabled() || this.mReturningToStart || canChildScrollUp()) {
            return false;
        }
        switch (action) {
            case MMAdView.TRANSITION_NONE:
                float y = ev.getY();
                this.mInitialMotionY = y;
                this.mLastMotionY = y;
                this.mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                this.mIsBeingDragged = false;
                this.mCurrPercentage = 0.0f;
                break;
            case MMAdView.TRANSITION_FADE:
            case MMAdView.TRANSITION_DOWN:
                this.mIsBeingDragged = false;
                this.mCurrPercentage = 0.0f;
                this.mActivePointerId = -1;
                break;
            case MMAdView.TRANSITION_UP:
                if (this.mActivePointerId == -1) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
                    return false;
                } else {
                    int pointerIndex = MotionEventCompat.findPointerIndex(ev, this.mActivePointerId);
                    if (pointerIndex < 0) {
                        Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                        return false;
                    } else {
                        float y2 = MotionEventCompat.getY(ev, pointerIndex);
                        if (y2 - this.mInitialMotionY > ((float) this.mTouchSlop)) {
                            this.mLastMotionY = y2;
                            this.mIsBeingDragged = true;
                        }
                    }
                }
                break;
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                onSecondaryPointerUp(ev);
                break;
        }
        return this.mIsBeingDragged;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        this.mProgressBar.setBounds(0, 0, width, this.mProgressBarHeight);
        if (getChildCount() != 0) {
            View child = getChildAt(0);
            int childLeft = getPaddingLeft();
            int childTop = this.mCurrentTargetOffsetTop + getPaddingTop();
            child.layout(childLeft, childTop, childLeft + width - getPaddingLeft() - getPaddingRight(), childTop + height - getPaddingTop() - getPaddingBottom());
        }
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() > 1 && !isInEditMode()) {
            throw new IllegalStateException("SwipeRefreshLayout can host only one direct child");
        } else if (getChildCount() > 0) {
            getChildAt(0).measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), 1073741824), MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), 1073741824));
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        if (this.mReturningToStart && action == 0) {
            this.mReturningToStart = false;
        }
        if (!isEnabled() || this.mReturningToStart || canChildScrollUp()) {
            return false;
        }
        switch (action) {
            case MMAdView.TRANSITION_NONE:
                float y = ev.getY();
                this.mInitialMotionY = y;
                this.mLastMotionY = y;
                this.mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                this.mIsBeingDragged = false;
                this.mCurrPercentage = 0.0f;
                return true;
            case MMAdView.TRANSITION_FADE:
            case MMAdView.TRANSITION_DOWN:
                this.mIsBeingDragged = false;
                this.mCurrPercentage = 0.0f;
                this.mActivePointerId = -1;
                return false;
            case MMAdView.TRANSITION_UP:
                int pointerIndex = MotionEventCompat.findPointerIndex(ev, this.mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                } else {
                    float y2 = MotionEventCompat.getY(ev, pointerIndex);
                    float yDiff = y2 - this.mInitialMotionY;
                    if (!this.mIsBeingDragged && yDiff > ((float) this.mTouchSlop)) {
                        this.mIsBeingDragged = true;
                    }
                    if (this.mIsBeingDragged) {
                        if (yDiff > this.mDistanceToTriggerSync) {
                            startRefresh();
                        } else {
                            setTriggerPercentage(this.mAccelerateInterpolator.getInterpolation(yDiff / this.mDistanceToTriggerSync));
                            updateContentOffsetTop((int) yDiff);
                            if (this.mLastMotionY <= y2 || this.mTarget.getTop() != getPaddingTop()) {
                                updatePositionTimeout();
                            } else {
                                removeCallbacks(this.mCancel);
                            }
                        }
                        this.mLastMotionY = y2;
                    }
                    return true;
                }
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                int index = MotionEventCompat.getActionIndex(ev);
                this.mLastMotionY = MotionEventCompat.getY(ev, index);
                this.mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                return true;
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                onSecondaryPointerUp(ev);
                return true;
            default:
                return true;
        }
    }

    public void requestDisallowInterceptTouchEvent(boolean b) {
    }

    @Deprecated
    public void setColorScheme(int colorRes1, int colorRes2, int colorRes3, int colorRes4) {
        setColorSchemeResources(colorRes1, colorRes2, colorRes3, colorRes4);
    }

    public void setColorSchemeColors(int color1, int color2, int color3, int color4) {
        ensureTarget();
        this.mProgressBar.setColorScheme(color1, color2, color3, color4);
    }

    public void setColorSchemeResources(int colorRes1, int colorRes2, int colorRes3, int colorRes4) {
        Resources res = getResources();
        setColorSchemeColors(res.getColor(colorRes1), res.getColor(colorRes2), res.getColor(colorRes3), res.getColor(colorRes4));
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mListener = listener;
    }

    public void setRefreshing(boolean refreshing) {
        if (this.mRefreshing != refreshing) {
            ensureTarget();
            this.mCurrPercentage = 0.0f;
            this.mRefreshing = refreshing;
            if (this.mRefreshing) {
                this.mProgressBar.start();
            } else {
                this.mProgressBar.stop();
            }
        }
    }
}