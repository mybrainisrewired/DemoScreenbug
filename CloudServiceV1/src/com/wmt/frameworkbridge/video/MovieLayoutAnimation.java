package com.wmt.frameworkbridge.video;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import com.wmt.opengl.grid.ItemAnimation;

public final class MovieLayoutAnimation {
    private static void appearAnim(LayoutTransition transition) {
        Animator anim = ObjectAnimator.ofFloat(null, "rotationY", new float[]{90.0f, 0.0f}).setDuration(transition.getDuration(ItemAnimation.CUR_Z));
        anim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                ((View) ((ObjectAnimator) anim).getTarget()).setRotationY(0.0f);
            }
        });
        transition.setAnimator(ItemAnimation.CUR_Z, anim);
    }

    private static void appearChangingAnim(Object target, LayoutTransition transition) {
        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", new int[]{0, 1});
        PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", new int[]{0, 1});
        PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", new int[]{0, 1});
        PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom", new int[]{0, 1});
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX", new float[]{1.0f, 0.0f, 1.0f});
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY", new float[]{1.0f, 0.0f, 1.0f});
        Animator anim = ObjectAnimator.ofPropertyValuesHolder(target, new PropertyValuesHolder[]{pvhLeft, pvhTop, pvhRight, pvhBottom, pvhScaleX, pvhScaleY}).setDuration(transition.getDuration(0));
        anim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setScaleX(1.0f);
                view.setScaleY(1.0f);
            }
        });
        transition.setAnimator(0, anim);
    }

    private static void disappearAnim(LayoutTransition transition) {
        Animator anim = ObjectAnimator.ofFloat(null, "rotationY", new float[]{0.0f, 90.0f}).setDuration(transition.getDuration(ItemAnimation.CUR_ALPHA));
        anim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                ((View) ((ObjectAnimator) anim).getTarget()).setRotationX(0.0f);
            }
        });
        transition.setAnimator(ItemAnimation.CUR_ALPHA, anim);
    }

    private static void disappearChangingAnim(Object target, LayoutTransition transition) {
        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", new int[]{0, 1});
        PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", new int[]{0, 1});
        PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", new int[]{0, 1});
        PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom", new int[]{0, 1});
        Keyframe kf0 = Keyframe.ofFloat(0.0f, 0.0f);
        Keyframe kf1 = Keyframe.ofFloat(0.9999f, 360.0f);
        Keyframe kf2 = Keyframe.ofFloat(1.0f, 0.0f);
        PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofKeyframe("rotation", new Keyframe[]{kf0, kf1, kf2});
        Animator anim = ObjectAnimator.ofPropertyValuesHolder(target, new PropertyValuesHolder[]{pvhLeft, pvhTop, pvhRight, pvhBottom, pvhRotation}).setDuration(transition.getDuration(1));
        anim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                ((View) ((ObjectAnimator) anim).getTarget()).setRotation(0.0f);
            }
        });
        transition.setAnimator(1, anim);
    }

    public static void setupTransition(Object target, LayoutTransition transition) {
        appearAnim(transition);
        disappearAnim(transition);
    }
}