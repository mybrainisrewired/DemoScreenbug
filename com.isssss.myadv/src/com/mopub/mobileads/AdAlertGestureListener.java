package com.mopub.mobileads;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import com.millennialmedia.android.MMAdView;

public class AdAlertGestureListener extends SimpleOnGestureListener {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$mopub$mobileads$AdAlertGestureListener$ZigZagState = null;
    private static final float MAXIMUM_THRESHOLD_X_IN_DIPS = 100.0f;
    private static final float MAXIMUM_THRESHOLD_Y_IN_DIPS = 50.0f;
    private static final int MINIMUM_NUMBER_OF_ZIGZAGS_TO_FLAG = 4;
    private AdAlertReporter mAdAlertReporter;
    private AdConfiguration mAdConfiguration;
    private float mCurrentThresholdInDips;
    private ZigZagState mCurrentZigZagState;
    private boolean mHasCrossedLeftThreshold;
    private boolean mHasCrossedRightThreshold;
    private int mNumberOfZigZags;
    private float mPivotPositionX;
    private float mPreviousPositionX;
    private View mView;

    enum ZigZagState {
        UNSET,
        GOING_RIGHT,
        GOING_LEFT,
        FINISHED,
        FAILED;

        static {
            UNSET = new ZigZagState("UNSET", 0);
            GOING_RIGHT = new ZigZagState("GOING_RIGHT", 1);
            GOING_LEFT = new ZigZagState("GOING_LEFT", 2);
            FINISHED = new ZigZagState("FINISHED", 3);
            FAILED = new ZigZagState("FAILED", 4);
            ENUM$VALUES = new ZigZagState[]{UNSET, GOING_RIGHT, GOING_LEFT, FINISHED, FAILED};
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$mopub$mobileads$AdAlertGestureListener$ZigZagState() {
        int[] iArr = $SWITCH_TABLE$com$mopub$mobileads$AdAlertGestureListener$ZigZagState;
        if (iArr == null) {
            iArr = new int[ZigZagState.values().length];
            try {
                iArr[ZigZagState.FAILED.ordinal()] = 5;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ZigZagState.FINISHED.ordinal()] = 4;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ZigZagState.GOING_LEFT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[ZigZagState.GOING_RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[ZigZagState.UNSET.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            $SWITCH_TABLE$com$mopub$mobileads$AdAlertGestureListener$ZigZagState = iArr;
        }
        return iArr;
    }

    AdAlertGestureListener(View view, AdConfiguration adConfiguration) {
        this.mCurrentThresholdInDips = 100.0f;
        this.mCurrentZigZagState = ZigZagState.UNSET;
        if (view != null && view.getWidth() > 0) {
            this.mCurrentThresholdInDips = Math.min(MAXIMUM_THRESHOLD_X_IN_DIPS, ((float) view.getWidth()) / 3.0f);
        }
        this.mView = view;
        this.mAdConfiguration = adConfiguration;
    }

    private void incrementNumberOfZigZags() {
        this.mNumberOfZigZags++;
        if (this.mNumberOfZigZags >= 4) {
            this.mCurrentZigZagState = ZigZagState.FINISHED;
        }
    }

    private boolean isMovingLeft(float currentPositionX) {
        return currentPositionX < this.mPreviousPositionX;
    }

    private boolean isMovingRight(float currentPositionX) {
        return currentPositionX > this.mPreviousPositionX;
    }

    private boolean isTouchOutOfBoundsOnYAxis(float initialY, float currentY) {
        return Math.abs(currentY - initialY) > 50.0f;
    }

    private boolean leftThresholdReached(float currentPosition) {
        if (this.mHasCrossedLeftThreshold) {
            return true;
        }
        if (currentPosition > this.mPivotPositionX - this.mCurrentThresholdInDips) {
            return false;
        }
        this.mHasCrossedRightThreshold = false;
        this.mHasCrossedLeftThreshold = true;
        incrementNumberOfZigZags();
        return true;
    }

    private boolean rightThresholdReached(float currentPosition) {
        if (this.mHasCrossedRightThreshold) {
            return true;
        }
        if (currentPosition < this.mPivotPositionX + this.mCurrentThresholdInDips) {
            return false;
        }
        this.mHasCrossedLeftThreshold = false;
        this.mHasCrossedRightThreshold = true;
        return true;
    }

    private void updateInitialState(float currentPositionX) {
        if (currentPositionX > this.mPivotPositionX) {
            this.mCurrentZigZagState = ZigZagState.GOING_RIGHT;
        }
    }

    private void updateZag(float currentPositionX) {
        if (leftThresholdReached(currentPositionX) && isMovingRight(currentPositionX)) {
            this.mCurrentZigZagState = ZigZagState.GOING_RIGHT;
            this.mPivotPositionX = currentPositionX;
        }
    }

    private void updateZig(float currentPositionX) {
        if (rightThresholdReached(currentPositionX) && isMovingLeft(currentPositionX)) {
            this.mCurrentZigZagState = ZigZagState.GOING_LEFT;
            this.mPivotPositionX = currentPositionX;
        }
    }

    void finishGestureDetection() {
        if (this.mCurrentZigZagState == ZigZagState.FINISHED) {
            this.mAdAlertReporter = new AdAlertReporter(this.mView.getContext(), this.mView, this.mAdConfiguration);
            this.mAdAlertReporter.send();
        }
        reset();
    }

    @Deprecated
    AdAlertReporter getAdAlertReporter() {
        return this.mAdAlertReporter;
    }

    @Deprecated
    ZigZagState getCurrentZigZagState() {
        return this.mCurrentZigZagState;
    }

    @Deprecated
    float getMinimumDipsInZigZag() {
        return this.mCurrentThresholdInDips;
    }

    @Deprecated
    int getNumberOfZigzags() {
        return this.mNumberOfZigZags;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (this.mCurrentZigZagState == ZigZagState.FINISHED) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
        if (isTouchOutOfBoundsOnYAxis(e1.getY(), e2.getY())) {
            this.mCurrentZigZagState = ZigZagState.FAILED;
            return super.onScroll(e1, e2, distanceX, distanceY);
        } else {
            switch ($SWITCH_TABLE$com$mopub$mobileads$AdAlertGestureListener$ZigZagState()[this.mCurrentZigZagState.ordinal()]) {
                case MMAdView.TRANSITION_FADE:
                    this.mPivotPositionX = e1.getX();
                    updateInitialState(e2.getX());
                    break;
                case MMAdView.TRANSITION_UP:
                    updateZig(e2.getX());
                    break;
                case MMAdView.TRANSITION_DOWN:
                    updateZag(e2.getX());
                    break;
            }
            this.mPreviousPositionX = e2.getX();
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    void reset() {
        this.mNumberOfZigZags = 0;
        this.mCurrentZigZagState = ZigZagState.UNSET;
    }
}