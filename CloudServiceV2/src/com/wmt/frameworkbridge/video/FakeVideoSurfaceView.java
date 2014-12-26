package com.wmt.frameworkbridge.video;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Configuration;
import android.frameworkbridge.IWmtVideoBridge;
import android.frameworkbridge.WmtHTML5Bridge.IFakeSurfaceViewBridge;
import android.media.MediaPlayer;
import android.os.SystemProperties;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.animation.AnimationUtils;

public class FakeVideoSurfaceView extends View {
    private static final String HDMI_PLUGIN_KEY = "hw.hdmi.plugin";
    private static final float MAX_FACTOR = 3.0f;
    private static final float MIN_FACTOR = 0.75f;
    private static final String TAG = "FakeVideoSurfaceView";
    private int REAL_WINDOW_HEIGHT;
    private int REAL_WINDOW_WIDTH;
    private Context mContext;
    private int mCurrentVideoHeight;
    private int mCurrentVideoWidth;
    private float mDistanceX;
    private float mDistanceY;
    private IFakeSurfaceViewBridge mFakeViewBridge;
    private float mFocusVideoX;
    private float mFocusVideoY;
    private GestureDetector mGestureDetector;
    private SimpleOnGestureListener mGestureListener;
    private boolean mHandledByTouchUp;
    private boolean mInScale;
    private boolean mInScroll;
    private boolean mIsPressed;
    private LimitState mLimitSate;
    private int[] mMaxMinHeight;
    private int[] mMaxMinWidth;
    private ScaleGestureDetector mScaleGestureDetector;
    private OnScaleGestureListener mScaleGestureListener;
    private int mScrollX;
    private int mScrollY;
    private IWmtVideoBridge mVideoBridge;
    private int[] mVideoSize;
    private IVideoSurfaceView mVideoView;

    class AnonymousClass_3 implements AnimatorUpdateListener {
        final /* synthetic */ int val$currentOffsetX;
        final /* synthetic */ int val$currentOffsetY;
        final /* synthetic */ boolean val$isBound_X;
        final /* synthetic */ boolean val$isBound_Y;
        final /* synthetic */ boolean val$isCenter_H;
        final /* synthetic */ boolean val$isCenter_V;
        final /* synthetic */ int val$revertOffsetX;
        final /* synthetic */ int val$revertOffsetY;

        AnonymousClass_3(boolean z, int i, int i2, boolean z2, boolean z3, int i3, int i4, boolean z4) {
            this.val$isBound_X = z;
            this.val$currentOffsetX = i;
            this.val$revertOffsetX = i2;
            this.val$isCenter_H = z2;
            this.val$isBound_Y = z3;
            this.val$currentOffsetY = i3;
            this.val$revertOffsetY = i4;
            this.val$isCenter_V = z4;
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            float fraction = animation.getAnimatedFraction();
            int x = 0;
            int y = 0;
            if (this.val$isBound_X) {
                x = this.val$currentOffsetX - ((int) (((float) (this.val$currentOffsetX - this.val$revertOffsetX)) * fraction));
            } else if (this.val$isCenter_H) {
                x = (int) (((float) this.val$currentOffsetX) - ((float) this.val$currentOffsetX) * fraction);
            } else if (!(this.val$isBound_X || this.val$isCenter_H)) {
                x = this.val$currentOffsetX;
            }
            if (this.val$isBound_Y) {
                y = this.val$currentOffsetY - ((int) (((float) (this.val$currentOffsetY - this.val$revertOffsetY)) * fraction));
            } else if (this.val$isCenter_V) {
                y = (int) (((float) this.val$currentOffsetY) - ((float) this.val$currentOffsetY) * fraction);
            } else if (!(this.val$isBound_Y || this.val$isCenter_V)) {
                y = this.val$currentOffsetY;
            }
            FakeVideoSurfaceView.this.mVideoView.setOffSetXY(x, y);
            FakeVideoSurfaceView.this.mVideoView.updateLayout();
        }
    }

    private enum LimitState {
        WIDTH_LIMIT,
        HEIGHT_LIMIT
    }

    public FakeVideoSurfaceView(Context context) {
        this(context, null);
    }

    public FakeVideoSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FakeVideoSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mInScroll = false;
        this.mInScale = false;
        this.mIsPressed = false;
        this.mHandledByTouchUp = false;
        this.mLimitSate = LimitState.WIDTH_LIMIT;
        this.mVideoSize = new int[2];
        this.mMaxMinWidth = new int[2];
        this.mMaxMinHeight = new int[2];
        this.mGestureListener = new SimpleOnGestureListener() {
            public boolean onDoubleTap(MotionEvent e) {
                FakeVideoSurfaceView.this.mIsPressed = false;
                if (FakeVideoSurfaceView.this.mVideoBridge != null) {
                    FakeVideoSurfaceView.this.mVideoBridge.invokeScreenScale();
                }
                return true;
            }

            public boolean onDown(MotionEvent e) {
                FakeVideoSurfaceView.this.mScrollX = FakeVideoSurfaceView.this.mVideoView.getOffsetX();
                FakeVideoSurfaceView.this.mScrollY = FakeVideoSurfaceView.this.mVideoView.getOffsetY();
                return true;
            }

            public void onLongPress(MotionEvent e) {
                FakeVideoSurfaceView.this.mIsPressed = true;
            }

            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (FakeVideoSurfaceView.this.mInScale || (FakeVideoSurfaceView.this.mVideoView.getWidth() <= FakeVideoSurfaceView.this.REAL_WINDOW_WIDTH && FakeVideoSurfaceView.this.mVideoView.getHeight() <= FakeVideoSurfaceView.this.REAL_WINDOW_HEIGHT)) {
                    FakeVideoSurfaceView.this.mIsPressed = true;
                } else {
                    FakeVideoSurfaceView.this.mInScroll = true;
                    int top = FakeVideoSurfaceView.this.mVideoView.getTop();
                    int bottom = FakeVideoSurfaceView.this.mVideoView.getBottom();
                    int left = FakeVideoSurfaceView.this.mVideoView.getLeft();
                    int right = FakeVideoSurfaceView.this.mVideoView.getRight();
                    if (distanceX < 0.0f) {
                        if (left > 0) {
                            distanceX /= 2.0f;
                        }
                    } else if (right < FakeVideoSurfaceView.this.REAL_WINDOW_WIDTH) {
                        distanceX /= 2.0f;
                    }
                    if (distanceY < 0.0f) {
                        if (top > 0) {
                            distanceY /= 2.0f;
                        }
                    } else if (bottom < FakeVideoSurfaceView.this.REAL_WINDOW_HEIGHT) {
                        distanceY /= 2.0f;
                    }
                    if (FakeVideoSurfaceView.this.mVideoView.getWidth() > FakeVideoSurfaceView.this.REAL_WINDOW_WIDTH) {
                        FakeVideoSurfaceView.access$016(FakeVideoSurfaceView.this, -distanceX);
                    }
                    if (FakeVideoSurfaceView.this.mVideoView.getHeight() > FakeVideoSurfaceView.this.REAL_WINDOW_HEIGHT) {
                        FakeVideoSurfaceView.access$216(FakeVideoSurfaceView.this, -distanceY);
                    }
                    FakeVideoSurfaceView.this.mVideoView.setOffSetXY(FakeVideoSurfaceView.this.mScrollX, FakeVideoSurfaceView.this.mScrollY);
                    FakeVideoSurfaceView.this.mVideoView.updateLayout();
                }
                return true;
            }

            public void onShowPress(MotionEvent e) {
                FakeVideoSurfaceView.this.mIsPressed = true;
            }

            public boolean onSingleTapConfirmed(MotionEvent e) {
                FakeVideoSurfaceView.this.mIsPressed = false;
                if (!(FakeVideoSurfaceView.this.mVideoBridge == null || FakeVideoSurfaceView.this.mHandledByTouchUp)) {
                    FakeVideoSurfaceView.this.mVideoBridge.showMovieController();
                }
                FakeVideoSurfaceView.this.mHandledByTouchUp = false;
                return true;
            }
        };
        this.mScaleGestureListener = new OnScaleGestureListener() {
            public boolean onScale(ScaleGestureDetector detector) {
                FakeVideoSurfaceView.this.scaleVideoView(detector);
                return false;
            }

            public boolean onScaleBegin(ScaleGestureDetector detector) {
                FakeVideoSurfaceView.this.mInScale = true;
                FakeVideoSurfaceView.this.preparedBeforeScale(detector);
                return true;
            }

            public void onScaleEnd(ScaleGestureDetector detector) {
            }
        };
        this.mFakeViewBridge = new IFakeSurfaceViewBridge() {
            public void setVideoBridge(IWmtVideoBridge bridge) {
                FakeVideoSurfaceView.this.setVideoBridge(bridge);
            }
        };
        this.mContext = context;
        this.mScaleGestureDetector = new ScaleGestureDetector(context, this.mScaleGestureListener);
        this.mGestureDetector = new GestureDetector(context, this.mGestureListener);
        updateRealWindowWidthHeight();
    }

    static /* synthetic */ int access$016(FakeVideoSurfaceView x0, float x1) {
        int i = (int) (((float) x0.mScrollX) + x1);
        x0.mScrollX = i;
        return i;
    }

    static /* synthetic */ int access$216(FakeVideoSurfaceView x0, float x1) {
        int i = (int) (((float) x0.mScrollY) + x1);
        x0.mScrollY = i;
        return i;
    }

    private boolean isHdmiPlugIn() {
        return Integer.valueOf(SystemProperties.get(HDMI_PLUGIN_KEY, "0")).intValue() == 1;
    }

    private void preparedBeforeScale(ScaleGestureDetector detector) {
        this.mCurrentVideoWidth = this.mVideoView.getWidth();
        this.mCurrentVideoHeight = this.mVideoView.getHeight();
        if (this.mVideoBridge != null) {
            MediaPlayer mp = this.mVideoBridge.getMediaPlayer();
            if (mp != null) {
                this.mVideoSize[0] = mp.getVideoWidth();
                this.mVideoSize[1] = mp.getVideoHeight();
                if ((((float) this.mCurrentVideoWidth) * 1.0f) / ((float) this.REAL_WINDOW_WIDTH) >= (((float) this.mCurrentVideoHeight) * 1.0f) / ((float) this.REAL_WINDOW_HEIGHT)) {
                    this.mLimitSate = LimitState.WIDTH_LIMIT;
                } else {
                    this.mLimitSate = LimitState.HEIGHT_LIMIT;
                }
                this.mMaxMinWidth[0] = (int) (((float) this.REAL_WINDOW_WIDTH) * 3.0f);
                this.mMaxMinWidth[1] = Math.min((int) (((float) this.REAL_WINDOW_WIDTH) * 0.75f), this.mVideoSize[0]);
                this.mMaxMinHeight[0] = (int) (((float) this.REAL_WINDOW_HEIGHT) * 3.0f);
                this.mMaxMinHeight[1] = Math.min((int) (((float) this.REAL_WINDOW_HEIGHT) * 0.75f), this.mVideoSize[1]);
                this.mFocusVideoX = detector.getFocusX();
                this.mFocusVideoY = detector.getFocusY();
                this.mDistanceX = ((float) (this.mVideoView.getLeft() + this.mVideoView.getWidth() / 2)) - this.mFocusVideoX;
                this.mDistanceY = ((float) (this.mVideoView.getTop() + this.mVideoView.getHeight() / 2)) - this.mFocusVideoY;
                this.mScrollX = this.mVideoView.getOffsetX();
                this.mScrollY = this.mVideoView.getOffsetY();
            }
        }
    }

    private void resetVideoView2BoundOrCenter() {
        int offsetX;
        boolean reset2bound_x;
        boolean reset2center_h;
        int offsetY;
        boolean reset2bound_y;
        boolean reset2center_v;
        int currentOffsetX = this.mVideoView.getOffsetX();
        int currentOffsetY = this.mVideoView.getOffsetY();
        int top = this.mVideoView.getTop();
        int bottom = this.mVideoView.getBottom();
        int left = this.mVideoView.getLeft();
        int right = this.mVideoView.getRight();
        int width = this.mVideoView.getWidth();
        int height = this.mVideoView.getHeight();
        if (width > this.REAL_WINDOW_WIDTH) {
            if (left > 0) {
                offsetX = (width - this.REAL_WINDOW_WIDTH) / 2;
                reset2bound_x = true;
            } else if (right < this.REAL_WINDOW_WIDTH) {
                offsetX = (-(width - this.REAL_WINDOW_WIDTH)) / 2;
                reset2bound_x = true;
            } else {
                reset2bound_x = false;
                offsetX = currentOffsetX;
            }
            reset2center_h = false;
        } else {
            reset2bound_x = false;
            reset2center_h = true;
            offsetX = currentOffsetX;
        }
        if (height > this.REAL_WINDOW_HEIGHT) {
            if (top > 0) {
                offsetY = (height - this.REAL_WINDOW_HEIGHT) / 2;
                reset2bound_y = true;
            } else if (bottom < this.REAL_WINDOW_HEIGHT) {
                offsetY = (-(height - this.REAL_WINDOW_HEIGHT)) / 2;
                reset2bound_y = true;
            } else {
                reset2bound_y = false;
                offsetY = currentOffsetY;
            }
            reset2center_v = false;
        } else {
            reset2center_v = true;
            reset2bound_y = false;
            offsetY = currentOffsetY;
        }
        int revertOffsetX = offsetX;
        int revertOffsetY = offsetY;
        boolean isCenter_H = reset2center_h;
        boolean isCenter_V = reset2center_v;
        boolean isBound_X = reset2bound_x;
        boolean isBound_Y = reset2bound_y;
        if (isCenter_H || isCenter_V || isBound_X || isBound_Y) {
            ValueAnimator animator = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
            animator.setDuration(300);
            animator.setInterpolator(AnimationUtils.loadInterpolator(this.mContext, 17432580));
            animator.addUpdateListener(new AnonymousClass_3(isBound_X, currentOffsetX, revertOffsetX, isCenter_H, isBound_Y, currentOffsetY, revertOffsetY, isCenter_V));
            animator.addListener(new AnimatorListener() {
                public void onAnimationCancel(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    FakeVideoSurfaceView.this.mScrollX = FakeVideoSurfaceView.this.mVideoView.getOffsetX();
                    FakeVideoSurfaceView.this.mScrollY = FakeVideoSurfaceView.this.mVideoView.getOffsetY();
                }

                public void onAnimationRepeat(Animator animation) {
                }

                public void onAnimationStart(Animator animation) {
                }
            });
            animator.start();
        }
    }

    private void scaleVideoView(ScaleGestureDetector detector) {
        float factor = detector.getScaleFactor();
        float focusX = detector.getFocusX();
        float focusY = detector.getFocusY();
        int width = this.mCurrentVideoWidth;
        int height = this.mCurrentVideoHeight;
        int maxWidth = this.mMaxMinWidth[0];
        int minWidth = this.mMaxMinWidth[1];
        int maxHeight = this.mMaxMinHeight[0];
        int minHeight = this.mMaxMinHeight[1];
        int scrollX = this.mScrollX;
        int scrollY = this.mScrollY;
        if (this.mLimitSate == LimitState.WIDTH_LIMIT) {
            boolean adjustHeight = true;
            width = (int) (((float) width) * factor);
            if (width > maxWidth) {
                width = maxWidth;
                adjustHeight = false;
            } else if (width <= minWidth) {
                width = minWidth;
                adjustHeight = false;
            }
            if (adjustHeight) {
                height = (int) (((float) height) * factor);
            } else {
                height = (this.mCurrentVideoHeight * width) / this.mCurrentVideoWidth;
            }
        } else {
            boolean adjustWidth = true;
            height = (int) (((float) height) * factor);
            if (height > maxHeight) {
                height = maxHeight;
                adjustWidth = false;
            } else if (height <= minHeight) {
                height = minHeight;
                adjustWidth = false;
            }
            if (adjustWidth) {
                width = (int) (((float) width) * factor);
            } else {
                width = (this.mCurrentVideoWidth * height) / this.mCurrentVideoHeight;
            }
        }
        this.mVideoView.setOffSetXY(scrollX + ((int) (focusX - this.mFocusVideoX + this.mDistanceX * (((1.0f * ((float) width)) / ((float) this.mCurrentVideoWidth)) - 1.0f))), scrollY + ((int) (focusY - this.mFocusVideoY + this.mDistanceY * (((1.0f * ((float) height)) / ((float) this.mCurrentVideoHeight)) - 1.0f))));
        this.mVideoView.setVideoSize(width, height, 0);
    }

    private void updateRealWindowWidthHeight() {
        DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
        this.REAL_WINDOW_WIDTH = displayMetrics.widthPixels;
        this.REAL_WINDOW_HEIGHT = displayMetrics.heightPixels;
    }

    public IFakeSurfaceViewBridge getBridgeImpl() {
        return this.mFakeViewBridge;
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateRealWindowWidthHeight();
        if (this.mVideoView != null) {
            this.mVideoView.resetParamters();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!isHdmiPlugIn()) {
            try {
                this.mScaleGestureDetector.onTouchEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!this.mScaleGestureDetector.isInProgress()) {
                this.mGestureDetector.onTouchEvent(event);
                if (event.getAction() == 1) {
                    if (this.mInScroll || this.mInScale) {
                        resetVideoView2BoundOrCenter();
                    }
                    if (!this.mIsPressed || this.mInScale || this.mInScroll) {
                        this.mHandledByTouchUp = false;
                    } else {
                        this.mHandledByTouchUp = true;
                        this.mVideoBridge.showMovieController();
                    }
                    this.mInScale = false;
                    this.mInScroll = false;
                    this.mIsPressed = false;
                }
            }
        } else if (event.getAction() == 1) {
            this.mVideoBridge.showMovieController();
        }
        return true;
    }

    public void setVideoBridge(IWmtVideoBridge bridge) {
        this.mVideoBridge = bridge;
        this.mVideoView = (IVideoSurfaceView) bridge.getVideoSurfaceView();
    }
}