package com.wmt.frameworkbridge.video.html5;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.frameworkbridge.WmtHTML5Bridge.ITopbarBridge;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.IWindowManager;
import android.view.IWindowManager.Stub;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.AnimationUtils;
import android.webkit.HTML5VideoFullScreen;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.WmtRatingBar;
import com.wmt.opengl.GLView;
import com.wmt.opengl.grid.ItemAnimation;
import com.wmt.remotectrl.KeyTouchInputEvent;

public class HTML5FullScreenTopbar extends RelativeLayout implements OnClickListener, OnLongClickListener {
    private static final int DEFUALT_TIME_OUT = 5000;
    private static final int FADE_OUT = 1;
    public static final int SCALE_ACTUAL_SIZE = 2;
    public static final int SCALE_BEST_FIT = 0;
    public static final int SCALE_FULLSCREEN = 1;
    private static boolean sALLOWUPDATE_RATINGBAR;
    private static boolean sFullScreenExitting;
    private BroadcastReceiver VolumeReceiver;
    private ObjectAnimator mAnimator;
    private boolean mAnimatorCancel;
    private AudioManager mAudioManager;
    private Context mContext;
    private int mCurrentScale;
    private ImageView mEixtFullScreen;
    private Handler mHandler;
    private IWindowManager mIWindowManager;
    private HTML5MediaController mMediaController;
    private ImageView mScreenScale;
    private boolean mShowing;
    private Toast mToast;
    private ITopbarBridge mTopbarBridge;
    private HTML5VideoFullScreen mVideoFullScreen;
    private HTML5VideoSurfaceView mVideoView;
    private ImageView mVolumeAdd;
    private ImageView mVolumeButton;
    private WmtRatingBar mVolumeRatingBar;
    private ImageView mVolumeReduce;

    class AnonymousClass_3 implements AnimatorUpdateListener {
        final /* synthetic */ int val$fromHeight;
        final /* synthetic */ int val$fromWidth;
        final /* synthetic */ int val$heightOff;
        final /* synthetic */ int val$widthOff;

        AnonymousClass_3(int i, int i2, int i3, int i4) {
            this.val$fromWidth = i;
            this.val$widthOff = i2;
            this.val$fromHeight = i3;
            this.val$heightOff = i4;
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            float value = animation.getAnimatedFraction();
            HTML5FullScreenTopbar.this.mVideoView.setVideoSize((int) (((float) this.val$fromWidth) + ((float) this.val$widthOff) * value), (int) (((float) this.val$fromHeight) + ((float) this.val$heightOff) * value), SCALE_BEST_FIT);
        }
    }

    class AnonymousClass_4 implements AnimatorListener {
        final /* synthetic */ boolean val$show;

        AnonymousClass_4(boolean z) {
            this.val$show = z;
        }

        public void onAnimationCancel(Animator animation) {
            HTML5FullScreenTopbar.this.mAnimatorCancel = true;
        }

        public void onAnimationEnd(Animator animation) {
            if (!(this.val$show || HTML5FullScreenTopbar.this.mAnimatorCancel)) {
                HTML5FullScreenTopbar.this.setVisibility(ItemAnimation.CUR_ARC);
                HTML5FullScreenTopbar.this.mShowing = false;
            }
            if (HTML5FullScreenTopbar.this.mAnimatorCancel) {
                HTML5FullScreenTopbar.this.mAnimatorCancel = false;
            }
        }

        public void onAnimationRepeat(Animator animation) {
        }

        public void onAnimationStart(Animator animation) {
        }
    }

    private class VolumeRattingBarListener implements OnRatingBarChangeListener {
        private VolumeRattingBarListener() {
        }

        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            if (fromUser) {
                if (HTML5FullScreenTopbar.this.mMediaController != null) {
                    HTML5FullScreenTopbar.this.mMediaController.show();
                }
                HTML5FullScreenTopbar.this.updateVolume(ratingBar, rating);
                sALLOWUPDATE_RATINGBAR = false;
            }
        }
    }

    static {
        sFullScreenExitting = false;
        sALLOWUPDATE_RATINGBAR = true;
    }

    public HTML5FullScreenTopbar(Context context) {
        this(context, null);
    }

    public HTML5FullScreenTopbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HTML5FullScreenTopbar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCurrentScale = 0;
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SCALE_FULLSCREEN:
                        HTML5FullScreenTopbar.this.hide();
                    default:
                        break;
                }
            }
        };
        this.mTopbarBridge = new ITopbarBridge() {
            public void handleScreenScale(int scale, boolean fromUser) {
                HTML5FullScreenTopbar.this.handleScreenScale(scale, fromUser);
            }

            public void invokeScreenScale() {
                HTML5FullScreenTopbar.this.invokeScreenScale();
            }

            public void setVideoFullScreen(HTML5VideoFullScreen view) {
                HTML5FullScreenTopbar.this.setVideoFullScreen(view);
            }

            public void updateNetworkBandwidth(int bankwidth) {
                HTML5FullScreenTopbar.this.updateNetworkBandwidth(bankwidth);
            }

            public void updateVideoTitle(String title) {
                HTML5FullScreenTopbar.this.updateVideoTitle(title);
            }
        };
        this.mContext = context;
        init();
    }

    private void animateVideoViewSize(int fromWidth, int toWidth, int fromHeight, int toHeight) {
        int widthOff = toWidth - fromWidth;
        int heightOff = toHeight - fromHeight;
        ValueAnimator animator = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
        animator.setDuration(1000);
        animator.setInterpolator(AnimationUtils.loadInterpolator(this.mContext, 17432584));
        animator.addUpdateListener(new AnonymousClass_3(fromWidth, widthOff, fromHeight, heightOff));
        animator.start();
    }

    private void doExitFullScreen() {
        if (!sFullScreenExitting) {
            sFullScreenExitting = true;
            new Thread() {
                public void run() {
                    HTML5FullScreenTopbar.this.doInjectKeyEvent(new KeyEvent(0, 4));
                    HTML5FullScreenTopbar.this.doInjectKeyEvent(new KeyEvent(1, 4));
                    sFullScreenExitting = false;
                }
            }.start();
        }
    }

    private void doInjectKeyEvent(KeyEvent event) {
        if (this.mIWindowManager == null) {
            this.mIWindowManager = Stub.asInterface(ServiceManager.getService("window"));
        }
        try {
            this.mIWindowManager.injectKeyEvent(event, true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void findViewsById() {
        this.mEixtFullScreen = (ImageView) findViewById(16908907);
        this.mScreenScale = (ImageView) findViewById(16908908);
        this.mVolumeAdd = (ImageView) findViewById(16908906);
        this.mVolumeReduce = (ImageView) findViewById(16908904);
        this.mVolumeButton = (ImageView) findViewById(16908903);
        this.mVolumeRatingBar = (WmtRatingBar) findViewById(16908905);
    }

    private void init() {
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        this.VolumeReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals("android.media.VOLUME_CHANGED_ACTION")) {
                    if (intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1) != 3) {
                        return;
                    }
                    if (sALLOWUPDATE_RATINGBAR) {
                        HTML5FullScreenTopbar.this.updateRatingBar();
                    } else {
                        sALLOWUPDATE_RATINGBAR = true;
                    }
                } else if (action.equals("android.media.RINGER_MODE_CHANGED")) {
                    if (HTML5FullScreenTopbar.this.mAudioManager.isSilentMode()) {
                        HTML5FullScreenTopbar.this.mVolumeButton.setImageResource(17303032);
                    } else {
                        HTML5FullScreenTopbar.this.mVolumeButton.setImageResource(17303033);
                    }
                    HTML5FullScreenTopbar.this.updateRatingBar();
                }
            }
        };
    }

    private void setViewsListener() {
        this.mVolumeAdd.setOnClickListener(this);
        this.mVolumeReduce.setOnClickListener(this);
        this.mVolumeButton.setOnClickListener(this);
        this.mVolumeRatingBar.setOnRatingBarChangeListener(new VolumeRattingBarListener(null));
        this.mEixtFullScreen.setOnClickListener(this);
        this.mEixtFullScreen.setOnLongClickListener(this);
        this.mScreenScale.setOnClickListener(this);
        this.mScreenScale.setOnLongClickListener(this);
    }

    private void setupViews() {
        findViewsById();
        setViewsListener();
        updateRatingBar();
    }

    private void showToast(int resId, int x, int y) {
        if (this.mToast == null) {
            this.mToast = Toast.makeText(this.mContext, resId, SCALE_BEST_FIT);
        }
        this.mToast.setText(resId);
        this.mToast.setGravity(KeyTouchInputEvent.ABS_MT_WIDTH_MINOR, x, y);
        this.mToast.show();
    }

    private void topbarAnimation(boolean show, boolean anim) {
        float fromAlpha;
        float toAlpha = 1.0f;
        if (show) {
            fromAlpha = 0.0f;
        } else {
            fromAlpha = 1.0f;
        }
        if (!show) {
            toAlpha = 0.0f;
        }
        if (this.mAnimator != null) {
            this.mAnimator.cancel();
        }
        if (anim) {
            this.mAnimator = ObjectAnimator.ofFloat(this, "alpha", new float[]{fromAlpha, toAlpha});
            this.mAnimator.setDuration(500);
            this.mAnimator.addListener(new AnonymousClass_4(show));
            this.mAnimator.start();
        } else {
            if (!show) {
                setVisibility(ItemAnimation.CUR_ARC);
                this.mShowing = false;
            }
            setAlpha(toAlpha);
        }
    }

    private void updateRatingBar() {
        int max = this.mAudioManager.getStreamMaxVolume(ItemAnimation.CUR_ALPHA);
        this.mVolumeRatingBar.setRating((float) ((int) (((1.0f * ((float) this.mVolumeRatingBar.getNumStars())) * ((float) this.mAudioManager.getStreamVolume(ItemAnimation.CUR_ALPHA))) / ((float) max))));
    }

    private void updateVolume(RatingBar ratingBar, float rating) {
        this.mAudioManager.setStreamVolume(ItemAnimation.CUR_ALPHA, (int) ((((float) this.mAudioManager.getStreamMaxVolume(ItemAnimation.CUR_ALPHA)) * rating) / ((float) ratingBar.getNumStars())), SCALE_BEST_FIT);
    }

    public ITopbarBridge getBridgeImpl() {
        return this.mTopbarBridge;
    }

    public void handleScreenScale(int scale, boolean fromUser) {
        this.mVideoView.resetParamters();
        int[] loc = new int[2];
        this.mScreenScale.getLocationOnScreen(loc);
        int topbarHeight = getHeight();
        DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
        int windowWidth = displayMetrics.widthPixels;
        int windowHeight = displayMetrics.heightPixels;
        if (this.mVideoFullScreen.getMediaPlayer() != null) {
            MediaPlayer mp = this.mVideoFullScreen.getMediaPlayer();
            int actualWidth = mp.getVideoWidth();
            int actualHeight = mp.getVideoHeight();
            int width = actualWidth;
            int height = actualHeight;
            if (actualWidth != 0 && actualHeight != 0) {
                switch (scale) {
                    case SCALE_BEST_FIT:
                        this.mCurrentScale = 0;
                        this.mScreenScale.setImageResource(17302337);
                        if (fromUser) {
                            showToast(17040617, loc[0], topbarHeight);
                        }
                        if ((actualWidth << 16) / windowWidth >= (actualHeight << 16) / windowHeight) {
                            width = windowWidth;
                            height = (actualHeight * windowWidth) / actualWidth;
                        } else {
                            height = windowHeight;
                            width = (actualWidth * windowHeight) / actualHeight;
                        }
                        break;
                    case SCALE_FULLSCREEN:
                        this.mCurrentScale = 1;
                        width = windowWidth;
                        height = windowHeight;
                        if (fromUser) {
                            showToast(17040618, loc[0], topbarHeight);
                        }
                        this.mScreenScale.setImageResource(17302338);
                        break;
                    case SCALE_ACTUAL_SIZE:
                        this.mCurrentScale = 2;
                        if (fromUser) {
                            showToast(17040619, loc[0], topbarHeight);
                        }
                        width = actualWidth;
                        height = actualHeight;
                        this.mScreenScale.setImageResource(17302339);
                        break;
                }
                if (fromUser) {
                    animateVideoViewSize(this.mVideoView.getWidth(), width, this.mVideoView.getHeight(), height);
                } else {
                    this.mVideoView.setVideoSize(width, height, scale);
                }
            }
        }
    }

    public void hide() {
        boolean z;
        if (HTML5MediaController.isHdmiPlugIn()) {
            z = false;
        } else {
            z = true;
        }
        topbarAnimation(false, z);
    }

    public void invokeScreenScale() {
        if (this.mCurrentScale < 2) {
            this.mCurrentScale++;
        } else {
            this.mCurrentScale = 0;
        }
        handleScreenScale(this.mCurrentScale, true);
    }

    public boolean isShowing() {
        return this.mShowing;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.RINGER_MODE_CHANGED");
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        this.mContext.registerReceiver(this.VolumeReceiver, filter);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case 16908903:
                if (this.mAudioManager.isSilentMode()) {
                    this.mAudioManager.setRingerMode(SCALE_ACTUAL_SIZE);
                    this.mVolumeButton.setImageResource(17303033);
                } else {
                    this.mAudioManager.setRingerMode(SCALE_BEST_FIT);
                    this.mVolumeButton.setImageResource(17303032);
                }
                if (this.mMediaController != null) {
                    this.mMediaController.show();
                }
            case 16908907:
                doExitFullScreen();
            case 16908908:
                if (this.mCurrentScale < 2) {
                    this.mCurrentScale++;
                } else {
                    this.mCurrentScale = 0;
                }
                if (this.mMediaController != null) {
                    this.mMediaController.show();
                }
                handleScreenScale(this.mCurrentScale, true);
            default:
                break;
        }
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        handleScreenScale(this.mCurrentScale, false);
        updateVideoTitle(null);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mContext.unregisterReceiver(this.VolumeReceiver);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        setupViews();
    }

    public boolean onLongClick(View v) {
        int id = v.getId();
        int[] loc = new int[2];
        v.getLocationOnScreen(loc);
        int topbarHeight = getHeight();
        int stringId = -1;
        switch (id) {
            case 16908907:
                stringId = 17040615;
                break;
            case 16908908:
                stringId = 17040616;
                break;
        }
        if (stringId != -1) {
            showToast(stringId, loc[0], topbarHeight);
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mMediaController != null) {
            this.mMediaController.show();
        }
        return true;
    }

    public void setVideoFullScreen(HTML5VideoFullScreen fs) {
        this.mVideoFullScreen = fs;
        this.mMediaController = (HTML5MediaController) fs.getMediaController();
        this.mVideoView = (HTML5VideoSurfaceView) fs.getVideoSurfaceView();
    }

    public void show() {
        show(DEFUALT_TIME_OUT);
    }

    public void show(int time) {
        if (this.mShowing) {
            setAlpha(1.0f);
        } else {
            this.mShowing = true;
            topbarAnimation(true, !HTML5MediaController.isHdmiPlugIn());
        }
        setVisibility(SCALE_BEST_FIT);
        if (time > 0) {
            this.mHandler.removeMessages(SCALE_FULLSCREEN);
            this.mHandler.sendEmptyMessageDelayed(SCALE_FULLSCREEN, (long) time);
        }
    }

    public void updateNetworkBandwidth(int bandWidth) {
        TextView bandwidthView = (TextView) findViewById(16908909);
        if (bandWidth >= 0) {
            bandwidthView.setText(bandWidth + " kbps");
        } else {
            bandwidthView.setText("");
        }
    }

    public void updateVideoTitle(String title) {
        boolean port = true;
        TextView titleTextView = (TextView) findViewById(16908910);
        if (title == null) {
            title = titleTextView.getText().toString();
        }
        titleTextView.setText(title);
        if (this.mContext.getResources().getConfiguration().orientation != 1) {
            port = false;
        }
        if (port) {
            titleTextView.setVisibility(GLView.FLAG_POPUP);
        } else {
            titleTextView.setVisibility(SCALE_BEST_FIT);
        }
    }
}