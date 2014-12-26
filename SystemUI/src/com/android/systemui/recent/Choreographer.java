package com.android.systemui.recent;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.AnimatorSet.Builder;
import android.animation.ObjectAnimator;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import com.android.ex.carousel.CarouselView.DetailAlignment;
import com.android.systemui.R;
import com.android.systemui.statusbar.CommandQueue;

class Choreographer implements AnimatorListener {
    private static final int CLOSE_DURATION = 230;
    private static final boolean DEBUG = false;
    private static final int OPEN_DURATION = 136;
    private static final int SCRIM_DURATION = 400;
    private static final String TAG = "RecentsPanelView";
    final int HYPERSPACE_OFFRAMP;
    AnimatorSet mContentAnim;
    View mContentView;
    AnimatorListener mListener;
    View mNoRecentAppsView;
    int mPanelHeight;
    RecentsPanelView mRootView;
    View mScrimView;
    boolean mVisible;

    public Choreographer(RecentsPanelView root, View scrim, View content, View noRecentApps, AnimatorListener listener) {
        this.HYPERSPACE_OFFRAMP = 200;
        this.mRootView = root;
        this.mScrimView = scrim;
        this.mContentView = content;
        this.mListener = listener;
        this.mNoRecentAppsView = noRecentApps;
    }

    void createAnimation(boolean appearing) {
        float start;
        float end;
        float y = this.mContentView.getTranslationY();
        if (appearing) {
            start = y < 200.0f ? y : 200.0f;
            end = 0.0f;
        } else {
            start = y;
            end = y;
        }
        View view = this.mContentView;
        String str = "translationY";
        float[] fArr = new float[2];
        fArr[0] = start;
        fArr[1] = end;
        Animator posAnim = ObjectAnimator.ofFloat(view, str, fArr);
        posAnim.setInterpolator(appearing ? new DecelerateInterpolator(2.5f) : new AccelerateInterpolator(2.5f));
        posAnim.setDuration(appearing ? 136 : 230);
        View view2 = this.mContentView;
        String str2 = "alpha";
        float[] fArr2 = new float[2];
        fArr2[0] = this.mContentView.getAlpha();
        fArr2[1] = appearing ? 1065353216 : 0;
        Animator fadeAnim = ObjectAnimator.ofFloat(view2, str2, fArr2);
        fadeAnim.setInterpolator(appearing ? new AccelerateInterpolator(1.0f) : new AccelerateInterpolator(2.5f));
        fadeAnim.setDuration(appearing ? 136 : 230);
        Animator noRecentAppsFadeAnim = null;
        if (this.mNoRecentAppsView != null && this.mNoRecentAppsView.getVisibility() == 0) {
            view2 = this.mNoRecentAppsView;
            str2 = "alpha";
            fArr2 = new float[2];
            fArr2[0] = this.mContentView.getAlpha();
            fArr2[1] = appearing ? 1065353216 : 0;
            noRecentAppsFadeAnim = ObjectAnimator.ofFloat(view2, str2, fArr2);
            noRecentAppsFadeAnim.setInterpolator(appearing ? new AccelerateInterpolator(1.0f) : new DecelerateInterpolator(1.0f));
            noRecentAppsFadeAnim.setDuration(appearing ? 136 : 230);
        }
        this.mContentAnim = new AnimatorSet();
        Builder builder = this.mContentAnim.play(fadeAnim).with(posAnim);
        if (noRecentAppsFadeAnim != null) {
            builder.with(noRecentAppsFadeAnim);
        }
        Animator bgAnim;
        if (appearing) {
            Drawable background = this.mScrimView.getBackground();
            if (background != null) {
                str = "alpha";
                int[] iArr = new int[2];
                iArr[0] = appearing ? 0 : DetailAlignment.VERTICAL_ALIGNMENT_MASK;
                iArr[1] = appearing ? DetailAlignment.VERTICAL_ALIGNMENT_MASK : 0;
                bgAnim = ObjectAnimator.ofInt(background, str, iArr);
                bgAnim.setDuration(appearing ? 400 : 230);
                builder.with(bgAnim);
            }
        } else if (!this.mRootView.getResources().getBoolean(R.bool.config_recents_interface_for_tablets)) {
            View recentsTransitionBackground = this.mRootView.findViewById(R.id.recents_transition_background);
            recentsTransitionBackground.setVisibility(0);
            Drawable bgDrawable = new ColorDrawable(-16777216);
            recentsTransitionBackground.setBackground(bgDrawable);
            bgAnim = ObjectAnimator.ofInt(bgDrawable, "alpha", new int[]{0, 255});
            bgAnim.setDuration(230);
            bgAnim.setInterpolator(new AccelerateInterpolator(1.0f));
            builder.with(bgAnim);
        }
        this.mContentAnim.addListener(this);
        if (this.mListener != null) {
            this.mContentAnim.addListener(this.mListener);
        }
    }

    void jumpTo(boolean appearing) {
        this.mContentView.setTranslationY(appearing ? 0.0f : (float) this.mPanelHeight);
        if (this.mScrimView.getBackground() != null) {
            this.mScrimView.getBackground().setAlpha(appearing ? DetailAlignment.VERTICAL_ALIGNMENT_MASK : 0);
        }
        this.mRootView.findViewById(R.id.recents_transition_background).setVisibility(CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
        this.mRootView.requestLayout();
    }

    public void onAnimationCancel(Animator animation) {
        this.mVisible = false;
    }

    public void onAnimationEnd(Animator animation) {
        if (!this.mVisible) {
            this.mRootView.hideWindow();
        }
        this.mContentView.setLayerType(0, null);
        this.mContentView.setAlpha(1.0f);
        this.mContentAnim = null;
    }

    public void onAnimationRepeat(Animator animation) {
    }

    public void onAnimationStart(Animator animation) {
    }

    public void setPanelHeight(int h) {
        this.mPanelHeight = h;
    }

    void startAnimation(boolean appearing) {
        createAnimation(appearing);
        if (this.mContentView.isHardwareAccelerated()) {
            this.mContentView.setLayerType(CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL, null);
            this.mContentView.buildLayer();
        }
        this.mContentAnim.start();
        this.mVisible = appearing;
    }
}