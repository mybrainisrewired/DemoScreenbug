package com.wmt.opengl;

import android.os.SystemClock;
import android.util.Log;
import javax.microedition.khronos.opengles.GL11;

public abstract class GLAnimateView extends GLView {
    public static final long ANIMATION_LENGTH = 450;
    protected static final int HIDEOUT = 2;
    private static final int SHOWUP = 1;
    public static final String TAG = "GLAnimateView";
    protected int mAnimateTarget;
    private boolean mAnimating;
    private IAnimationFinishListener mAnimationFinishListener;
    private long mAnimationStartTime;
    private IAnimator mAnimator;

    public static interface IAnimationFinishListener {
        void onAnimationFinished();
    }

    public static interface IAnimator {
        boolean animate(GL11 gl11, long j, long j2, int i);
    }

    public static class CubeFlipAnimator implements com.wmt.opengl.GLAnimateView.IAnimator {
        public static String TAG = null;
        private static final int cubeFlipInEndDegree = 0;
        private static final int cubeFlipInStartDegree = 60;
        private static final int cubeFlipOutEndDegree = -60;
        private static final int cubeFlipOutStartDegree = 0;
        private float mShowPlaneX;
        private float mShowPlaneZ;

        static {
            TAG = "DefaultAnimator";
        }

        public CubeFlipAnimator(float showPlaneX, float showPlaneY, float showPlaneZ) {
            this.mShowPlaneX = showPlaneX;
            this.mShowPlaneZ = showPlaneZ;
        }

        public boolean animate(GL11 gl, long interval, long totalTime, int target) {
            float rotate = 0.0f;
            if (target == 1) {
                rotate = FloatAnim.getInterpolatedValue(0.0f, 60.0f, totalTime, interval);
            } else if (target == 2) {
                rotate = FloatAnim.getInterpolatedValue(-60.0f, 60.0f, totalTime, interval);
            }
            gl.glTranslatef(0.0f, 0.0f, -this.mShowPlaneZ);
            gl.glTranslatef(0.0f, 0.0f, (this.mShowPlaneX * 1.73f) / 2.0f);
            gl.glRotatef(rotate, 0.0f, 1.0f, 0.0f);
            gl.glTranslatef(0.0f, 0.0f, ((-this.mShowPlaneX) * 1.73f) / 2.0f);
            gl.glTranslatef(0.0f, 0.0f, this.mShowPlaneZ);
            return interval >= totalTime || interval < 0;
        }

        public void setShowPlaneParameter(float showPlaneX, float showPlaneY, float showPlaneZ) {
            this.mShowPlaneX = showPlaneX;
            this.mShowPlaneZ = showPlaneZ;
        }
    }

    public static class ZoomAnimator implements com.wmt.opengl.GLAnimateView.IAnimator {
        public boolean animate(GL11 gl, long interval, long totalTime, int target) {
            float z = 0.2f;
            if (target == 1) {
                z = FloatAnim.getInterpolatedValue(0.0f, 0.2f, totalTime, interval);
            } else if (target == 2) {
                z = FloatAnim.getInterpolatedValue(0.2f, -true, totalTime, interval);
            }
            gl.glTranslatef(0.0f, 0.0f, z);
            return interval >= totalTime || interval < 0;
        }
    }

    public GLAnimateView(GLContext glContext) {
        super(glContext, 0);
        this.mAnimating = false;
        this.mAnimateTarget = -1;
    }

    private void startAnimation(IAnimationFinishListener listener) {
        this.mAnimationFinishListener = listener;
        this.mAnimating = true;
        this.mAnimationStartTime = SystemClock.uptimeMillis();
    }

    public void do3DAnimation(GLContext glContext) {
        if (this.mAnimating) {
            if (!(this.mAnimateTarget == 1 || this.mAnimateTarget == 2)) {
                Log.e(TAG, "Invalid animate target, you should use showUp/hideOut if your view is derived from GLAniamteView");
            }
            boolean finish = true;
            if (this.mAnimator != null) {
                finish = this.mAnimator.animate(glContext.glCanvas().getGL(), SystemClock.uptimeMillis() - this.mAnimationStartTime, ANIMATION_LENGTH, this.mAnimateTarget);
            } else {
                Log.e(TAG, "mAnimator is null, you should set a animator to do the work");
            }
            if (finish) {
                if (this.mAnimateTarget == 2) {
                    setVisibility(false);
                }
                if (this.mAnimationFinishListener != null) {
                    this.mAnimationFinishListener.onAnimationFinished();
                }
                this.mAnimating = false;
            }
        }
    }

    public void hideOut(IAnimator animator, IAnimationFinishListener listener) {
        this.mAnimateTarget = 2;
        this.mAnimator = animator;
        startAnimation(listener);
    }

    public boolean isAnimating() {
        return this.mAnimating;
    }

    public void showUp(IAnimator animator, IAnimationFinishListener listener) {
        setVisibility(true);
        this.mAnimateTarget = 1;
        this.mAnimator = animator;
        startAnimation(listener);
    }
}