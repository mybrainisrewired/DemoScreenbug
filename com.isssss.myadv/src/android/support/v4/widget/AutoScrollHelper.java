package android.support.v4.widget;

import android.content.res.Resources;
import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.millennialmedia.android.MMAdView;

public abstract class AutoScrollHelper implements OnTouchListener {
    private static final int DEFAULT_ACTIVATION_DELAY;
    private static final int DEFAULT_EDGE_TYPE = 1;
    private static final float DEFAULT_MAXIMUM_EDGE = Float.MAX_VALUE;
    private static final int DEFAULT_MAXIMUM_VELOCITY_DIPS = 1575;
    private static final int DEFAULT_MINIMUM_VELOCITY_DIPS = 315;
    private static final int DEFAULT_RAMP_DOWN_DURATION = 500;
    private static final int DEFAULT_RAMP_UP_DURATION = 500;
    private static final float DEFAULT_RELATIVE_EDGE = 0.2f;
    private static final float DEFAULT_RELATIVE_VELOCITY = 1.0f;
    public static final int EDGE_TYPE_INSIDE = 0;
    public static final int EDGE_TYPE_INSIDE_EXTEND = 1;
    public static final int EDGE_TYPE_OUTSIDE = 2;
    private static final int HORIZONTAL = 0;
    public static final float NO_MAX = Float.MAX_VALUE;
    public static final float NO_MIN = 0.0f;
    public static final float RELATIVE_UNSPECIFIED = 0.0f;
    private static final int VERTICAL = 1;
    private int mActivationDelay;
    private boolean mAlreadyDelayed;
    private boolean mAnimating;
    private final Interpolator mEdgeInterpolator;
    private int mEdgeType;
    private boolean mEnabled;
    private boolean mExclusive;
    private float[] mMaximumEdges;
    private float[] mMaximumVelocity;
    private float[] mMinimumVelocity;
    private boolean mNeedsCancel;
    private boolean mNeedsReset;
    private float[] mRelativeEdges;
    private float[] mRelativeVelocity;
    private Runnable mRunnable;
    private final ClampedScroller mScroller;
    private final View mTarget;

    private static class ClampedScroller {
        private long mDeltaTime;
        private int mDeltaX;
        private int mDeltaY;
        private int mEffectiveRampDown;
        private int mRampDownDuration;
        private int mRampUpDuration;
        private long mStartTime;
        private long mStopTime;
        private float mStopValue;
        private float mTargetVelocityX;
        private float mTargetVelocityY;

        public ClampedScroller() {
            this.mStartTime = Long.MIN_VALUE;
            this.mStopTime = -1;
            this.mDeltaTime = 0;
            this.mDeltaX = 0;
            this.mDeltaY = 0;
        }

        private float getValueAt(long currentTime) {
            if (currentTime < this.mStartTime) {
                return RELATIVE_UNSPECIFIED;
            }
            if (this.mStopTime < 0 || currentTime < this.mStopTime) {
                return AutoScrollHelper.constrain(((float) (currentTime - this.mStartTime)) / ((float) this.mRampUpDuration), (float)RELATIVE_UNSPECIFIED, (float)DEFAULT_RELATIVE_VELOCITY) * 0.5f;
            }
            long elapsedSinceEnd = currentTime - this.mStopTime;
            return AutoScrollHelper.constrain(((float) elapsedSinceEnd) / ((float) this.mEffectiveRampDown), (float)RELATIVE_UNSPECIFIED, (float)DEFAULT_RELATIVE_VELOCITY) * this.mStopValue + 1.0f - this.mStopValue;
        }

        private float interpolateValue(float value) {
            return (-4.0f * value) * value + 4.0f * value;
        }

        public void computeScrollDelta() {
            if (this.mDeltaTime == 0) {
                throw new RuntimeException("Cannot compute scroll delta before calling start()");
            }
            long currentTime = AnimationUtils.currentAnimationTimeMillis();
            float scale = interpolateValue(getValueAt(currentTime));
            long elapsedSinceDelta = currentTime - this.mDeltaTime;
            this.mDeltaTime = currentTime;
            this.mDeltaX = (int) ((((float) elapsedSinceDelta) * scale) * this.mTargetVelocityX);
            this.mDeltaY = (int) ((((float) elapsedSinceDelta) * scale) * this.mTargetVelocityY);
        }

        public int getDeltaX() {
            return this.mDeltaX;
        }

        public int getDeltaY() {
            return this.mDeltaY;
        }

        public int getHorizontalDirection() {
            return (int) (this.mTargetVelocityX / Math.abs(this.mTargetVelocityX));
        }

        public int getVerticalDirection() {
            return (int) (this.mTargetVelocityY / Math.abs(this.mTargetVelocityY));
        }

        public boolean isFinished() {
            return this.mStopTime > 0 && AnimationUtils.currentAnimationTimeMillis() > this.mStopTime + ((long) this.mEffectiveRampDown);
        }

        public void requestStop() {
            long currentTime = AnimationUtils.currentAnimationTimeMillis();
            this.mEffectiveRampDown = AutoScrollHelper.constrain((int) (currentTime - this.mStartTime), (int)HORIZONTAL, this.mRampDownDuration);
            this.mStopValue = getValueAt(currentTime);
            this.mStopTime = currentTime;
        }

        public void setRampDownDuration(int durationMillis) {
            this.mRampDownDuration = durationMillis;
        }

        public void setRampUpDuration(int durationMillis) {
            this.mRampUpDuration = durationMillis;
        }

        public void setTargetVelocity(float x, float y) {
            this.mTargetVelocityX = x;
            this.mTargetVelocityY = y;
        }

        public void start() {
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mStopTime = -1;
            this.mDeltaTime = this.mStartTime;
            this.mStopValue = 0.5f;
            this.mDeltaX = 0;
            this.mDeltaY = 0;
        }
    }

    private class ScrollAnimationRunnable implements Runnable {
        private ScrollAnimationRunnable() {
        }

        public void run() {
            if (AutoScrollHelper.this.mAnimating) {
                if (AutoScrollHelper.this.mNeedsReset) {
                    AutoScrollHelper.this.mNeedsReset = false;
                    AutoScrollHelper.this.mScroller.start();
                }
                ClampedScroller scroller = AutoScrollHelper.this.mScroller;
                if (scroller.isFinished() || !AutoScrollHelper.this.shouldAnimate()) {
                    AutoScrollHelper.this.mAnimating = false;
                } else {
                    if (AutoScrollHelper.this.mNeedsCancel) {
                        AutoScrollHelper.this.mNeedsCancel = false;
                        AutoScrollHelper.this.cancelTargetTouch();
                    }
                    scroller.computeScrollDelta();
                    AutoScrollHelper.this.scrollTargetBy(scroller.getDeltaX(), scroller.getDeltaY());
                    ViewCompat.postOnAnimation(AutoScrollHelper.this.mTarget, this);
                }
            }
        }
    }

    static {
        DEFAULT_ACTIVATION_DELAY = ViewConfiguration.getTapTimeout();
    }

    public AutoScrollHelper(View target) {
        this.mScroller = new ClampedScroller();
        this.mEdgeInterpolator = new AccelerateInterpolator();
        this.mRelativeEdges = new float[]{0.0f, 0.0f};
        this.mMaximumEdges = new float[]{Float.MAX_VALUE, Float.MAX_VALUE};
        this.mRelativeVelocity = new float[]{0.0f, 0.0f};
        this.mMinimumVelocity = new float[]{0.0f, 0.0f};
        this.mMaximumVelocity = new float[]{Float.MAX_VALUE, Float.MAX_VALUE};
        this.mTarget = target;
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int maxVelocity = (int) (1575.0f * metrics.density + 0.5f);
        int minVelocity = (int) (315.0f * metrics.density + 0.5f);
        setMaximumVelocity((float) maxVelocity, (float) maxVelocity);
        setMinimumVelocity((float) minVelocity, (float) minVelocity);
        setEdgeType(VERTICAL);
        setMaximumEdges(NO_MAX, NO_MAX);
        setRelativeEdges(DEFAULT_RELATIVE_EDGE, DEFAULT_RELATIVE_EDGE);
        setRelativeVelocity(DEFAULT_RELATIVE_VELOCITY, DEFAULT_RELATIVE_VELOCITY);
        setActivationDelay(DEFAULT_ACTIVATION_DELAY);
        setRampUpDuration(DEFAULT_RAMP_UP_DURATION);
        setRampDownDuration(DEFAULT_RAMP_UP_DURATION);
    }

    private void cancelTargetTouch() {
        long eventTime = SystemClock.uptimeMillis();
        MotionEvent cancel = MotionEvent.obtain(eventTime, eventTime, MMAdView.TRANSITION_DOWN, RELATIVE_UNSPECIFIED, 0.0f, HORIZONTAL);
        this.mTarget.onTouchEvent(cancel);
        cancel.recycle();
    }

    private float computeTargetVelocity(int direction, float coordinate, float srcSize, float dstSize) {
        float value = getEdgeValue(this.mRelativeEdges[direction], srcSize, this.mMaximumEdges[direction], coordinate);
        if (value == 0.0f) {
            return RELATIVE_UNSPECIFIED;
        }
        float relativeVelocity = this.mRelativeVelocity[direction];
        float minimumVelocity = this.mMinimumVelocity[direction];
        float maximumVelocity = this.mMaximumVelocity[direction];
        float targetVelocity = relativeVelocity * dstSize;
        return value > 0.0f ? constrain(value * targetVelocity, minimumVelocity, maximumVelocity) : -constrain((-value) * targetVelocity, minimumVelocity, maximumVelocity);
    }

    private static float constrain(float value, float min, float max) {
        if (value > max) {
            return max;
        }
        return value < min ? min : value;
    }

    private static int constrain(int value, int min, int max) {
        if (value > max) {
            return max;
        }
        return value < min ? min : value;
    }

    private float constrainEdgeValue(float current, float leading) {
        if (leading == 0.0f) {
            return RELATIVE_UNSPECIFIED;
        }
        switch (this.mEdgeType) {
            case HORIZONTAL:
            case VERTICAL:
                if (current >= leading) {
                    return RELATIVE_UNSPECIFIED;
                }
                if (current >= 0.0f) {
                    return 1.0f - current / leading;
                }
                return (this.mAnimating && this.mEdgeType == 1) ? 1.0f : RELATIVE_UNSPECIFIED;
            case EDGE_TYPE_OUTSIDE:
                return current < 0.0f ? current / (-leading) : RELATIVE_UNSPECIFIED;
            default:
                return RELATIVE_UNSPECIFIED;
        }
    }

    private float getEdgeValue(float relativeValue, float size, float maxValue, float current) {
        float interpolated;
        float edgeSize = constrain(relativeValue * size, (float)RELATIVE_UNSPECIFIED, maxValue);
        float value = constrainEdgeValue(size - current, edgeSize) - constrainEdgeValue(current, edgeSize);
        if (value < 0.0f) {
            interpolated = -this.mEdgeInterpolator.getInterpolation(-value);
        } else if (value <= 0.0f) {
            return RELATIVE_UNSPECIFIED;
        } else {
            interpolated = this.mEdgeInterpolator.getInterpolation(value);
        }
        return constrain(interpolated, (float)GroundOverlayOptions.NO_DIMENSION, (float)DEFAULT_RELATIVE_VELOCITY);
    }

    private void requestStop() {
        if (this.mNeedsReset) {
            this.mAnimating = false;
        } else {
            this.mScroller.requestStop();
        }
    }

    private boolean shouldAnimate() {
        ClampedScroller scroller = this.mScroller;
        int verticalDirection = scroller.getVerticalDirection();
        int horizontalDirection = scroller.getHorizontalDirection();
        return (verticalDirection != 0 && canTargetScrollVertically(verticalDirection)) || (horizontalDirection != 0 && canTargetScrollHorizontally(horizontalDirection));
    }

    private void startAnimating() {
        if (this.mRunnable == null) {
            this.mRunnable = new ScrollAnimationRunnable(null);
        }
        this.mAnimating = true;
        this.mNeedsReset = true;
        if (this.mAlreadyDelayed || this.mActivationDelay <= 0) {
            this.mRunnable.run();
        } else {
            ViewCompat.postOnAnimationDelayed(this.mTarget, this.mRunnable, (long) this.mActivationDelay);
        }
        this.mAlreadyDelayed = true;
    }

    public abstract boolean canTargetScrollHorizontally(int i);

    public abstract boolean canTargetScrollVertically(int i);

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public boolean isExclusive() {
        return this.mExclusive;
    }

    public boolean onTouch(View v, MotionEvent event) {
        boolean z = true;
        if (!this.mEnabled) {
            return false;
        }
        switch (MotionEventCompat.getActionMasked(event)) {
            case HORIZONTAL:
                this.mNeedsCancel = true;
                this.mAlreadyDelayed = false;
                this.mScroller.setTargetVelocity(computeTargetVelocity(HORIZONTAL, event.getX(), (float) v.getWidth(), (float) this.mTarget.getWidth()), computeTargetVelocity(VERTICAL, event.getY(), (float) v.getHeight(), (float) this.mTarget.getHeight()));
                if (!this.mAnimating && shouldAnimate()) {
                    startAnimating();
                }
                break;
            case VERTICAL:
            case MMAdView.TRANSITION_DOWN:
                requestStop();
                break;
            case EDGE_TYPE_OUTSIDE:
                this.mScroller.setTargetVelocity(computeTargetVelocity(HORIZONTAL, event.getX(), (float) v.getWidth(), (float) this.mTarget.getWidth()), computeTargetVelocity(VERTICAL, event.getY(), (float) v.getHeight(), (float) this.mTarget.getHeight()));
                startAnimating();
                break;
        }
        if (!(this.mExclusive && this.mAnimating)) {
            z = false;
        }
        return z;
    }

    public abstract void scrollTargetBy(int i, int i2);

    public AutoScrollHelper setActivationDelay(int delayMillis) {
        this.mActivationDelay = delayMillis;
        return this;
    }

    public AutoScrollHelper setEdgeType(int type) {
        this.mEdgeType = type;
        return this;
    }

    public AutoScrollHelper setEnabled(boolean enabled) {
        if (this.mEnabled && !enabled) {
            requestStop();
        }
        this.mEnabled = enabled;
        return this;
    }

    public AutoScrollHelper setExclusive(boolean exclusive) {
        this.mExclusive = exclusive;
        return this;
    }

    public AutoScrollHelper setMaximumEdges(float horizontalMax, float verticalMax) {
        this.mMaximumEdges[0] = horizontalMax;
        this.mMaximumEdges[1] = verticalMax;
        return this;
    }

    public AutoScrollHelper setMaximumVelocity(float horizontalMax, float verticalMax) {
        this.mMaximumVelocity[0] = horizontalMax / 1000.0f;
        this.mMaximumVelocity[1] = verticalMax / 1000.0f;
        return this;
    }

    public AutoScrollHelper setMinimumVelocity(float horizontalMin, float verticalMin) {
        this.mMinimumVelocity[0] = horizontalMin / 1000.0f;
        this.mMinimumVelocity[1] = verticalMin / 1000.0f;
        return this;
    }

    public AutoScrollHelper setRampDownDuration(int durationMillis) {
        this.mScroller.setRampDownDuration(durationMillis);
        return this;
    }

    public AutoScrollHelper setRampUpDuration(int durationMillis) {
        this.mScroller.setRampUpDuration(durationMillis);
        return this;
    }

    public AutoScrollHelper setRelativeEdges(float horizontal, float vertical) {
        this.mRelativeEdges[0] = horizontal;
        this.mRelativeEdges[1] = vertical;
        return this;
    }

    public AutoScrollHelper setRelativeVelocity(float horizontal, float vertical) {
        this.mRelativeVelocity[0] = horizontal / 1000.0f;
        this.mRelativeVelocity[1] = vertical / 1000.0f;
        return this;
    }
}