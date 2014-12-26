package com.wmt.frameworkbridge.video;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import com.wmt.frameworkbridge.video.MovieDragLayer.LayoutParams;

public class MovieSubtitleResizeLayout extends FrameLayout {
    private static final int ANIMATION_DURATION = 200;
    final float DIMMED_HANDLE_ALPHA;
    private int mBaselineHeight;
    private int mBaselineY;
    private boolean mBottomBorderActive;
    private int mDeltaY;
    private MovieDragLayer mDragLayer;
    private MovieDropContainer mDropContainer;
    private int mSubtitleHeight;
    private int mSubtitleLeft;
    private int mSubtitleTop;
    private View mSubtitleView;
    private int mSubtitleWidth;
    private boolean mTopBorderActive;
    private int mTouchTargetWidth;

    public MovieSubtitleResizeLayout(Context context, View subtitleView, MovieDragLayer dl) {
        super(context);
        this.DIMMED_HANDLE_ALPHA = 0.0f;
        this.mDragLayer = dl;
        this.mSubtitleView = subtitleView;
        this.mSubtitleLeft = subtitleView.getLeft();
        this.mSubtitleTop = subtitleView.getTop();
        this.mSubtitleWidth = subtitleView.getWidth();
        this.mSubtitleHeight = subtitleView.getHeight();
        setBackgroundResource(17302884);
        setPadding(0, 0, 0, 0);
        this.mTouchTargetWidth = 20;
    }

    private void animation2ActualSize(boolean animate) {
        LayoutParams lp = (LayoutParams) getLayoutParams();
        int[] minMax = this.mDropContainer.getMinMaxHeight();
        if (animate) {
            ObjectAnimator objectAnimator = null;
            if (minMax[0] > lp.height) {
                objectAnimator = ObjectAnimator.ofInt(lp, "height", new int[]{lp.height, minMax[0]});
            }
            if (minMax[1] < lp.height) {
                objectAnimator = ObjectAnimator.ofInt(lp, "height", new int[]{lp.height, minMax[1]});
            }
            if (objectAnimator == null) {
                objectAnimator = ObjectAnimator.ofInt(lp, "height", new int[]{lp.height, lp.height});
            }
            objectAnimator.setDuration(200);
            objectAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    MovieSubtitleResizeLayout.this.requestLayout();
                }
            });
            objectAnimator.start();
        } else {
            if (minMax[0] > lp.height) {
                lp.height = minMax[0];
            }
            if (minMax[1] < lp.height) {
                lp.height = minMax[1];
            }
            requestLayout();
        }
    }

    private void resizeSubtitleIfNeeded(int y, int height) {
        this.mDropContainer.notifySubtitleLayoutParamsChanged(y, height);
    }

    public boolean beginResizeIfPointInRegion(int x, int y) {
        boolean z;
        boolean anyBordersActive;
        if (y < this.mTouchTargetWidth) {
            z = true;
        } else {
            z = false;
        }
        this.mTopBorderActive = z;
        if (y > getHeight() - this.mTouchTargetWidth) {
            z = true;
        } else {
            z = false;
        }
        this.mBottomBorderActive = z;
        if (this.mTopBorderActive || this.mBottomBorderActive) {
            anyBordersActive = true;
        } else {
            anyBordersActive = false;
        }
        this.mBaselineHeight = getHeight();
        this.mBaselineY = getTop();
        return anyBordersActive;
    }

    public void commitResizeForDelta(int deltaX, int deltaY) {
        animation2ActualSize(true);
    }

    public void setDropContainer(MovieDropContainer dropContainer) {
        this.mDropContainer = dropContainer;
    }

    public void updateDeltas(LayoutParams lp, int deltaX, int deltaY) {
        if (this.mTopBorderActive) {
            this.mDeltaY = Math.max(-this.mBaselineY, deltaY);
            this.mDeltaY = Math.min(this.mBaselineHeight - this.mTouchTargetWidth * 2, this.mDeltaY);
        } else if (this.mBottomBorderActive) {
            this.mDeltaY = Math.min(this.mDragLayer.getHeight() - this.mBaselineY + this.mBaselineHeight, deltaY);
            this.mDeltaY = Math.max((-this.mBaselineHeight) + this.mTouchTargetWidth * 2, this.mDeltaY);
        }
    }

    public void updateLayout() {
        LayoutParams lp = (LayoutParams) getLayoutParams();
        lp.x = this.mSubtitleLeft;
        lp.y = this.mSubtitleTop;
        lp.width = this.mSubtitleWidth;
        lp.height = this.mSubtitleHeight;
        requestLayout();
    }

    public void visualizeResizeForDelta(int deltaX, int deltaY) {
        LayoutParams lp = (LayoutParams) getLayoutParams();
        updateDeltas(lp, deltaX, deltaY);
        if (this.mTopBorderActive) {
            lp.y = this.mBaselineY + this.mDeltaY;
            lp.height = this.mBaselineHeight - this.mDeltaY;
        } else if (this.mBottomBorderActive) {
            lp.height = this.mBaselineHeight + this.mDeltaY;
        }
        this.mSubtitleTop = lp.y;
        this.mSubtitleHeight = lp.height;
        resizeSubtitleIfNeeded(lp.y, lp.height);
        requestLayout();
    }
}