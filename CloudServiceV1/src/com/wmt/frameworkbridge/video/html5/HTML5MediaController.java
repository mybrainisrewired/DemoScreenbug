package com.wmt.frameworkbridge.video.html5;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.frameworkbridge.WmtHTML5Bridge.IControllerBridge;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemProperties;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.HTML5VideoFullScreen;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.wmt.frameworkbridge.video.VideoSeekTimeBar;
import com.wmt.frameworkbridge.video.VideoSeekTimeIndicator;
import com.wmt.opengl.grid.ItemAnimation;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

public class HTML5MediaController extends FrameLayout {
    private static final int DEFUALT_TIME_OUT = 5000;
    private static final int FADE_OUT = 1;
    private static final String HDMI_PLUGIN_KEY = "hw.hdmi.plugin";
    private static final int SHOW_PROGRESS = 2;
    private static final String TAG = "HTML5MediaController";
    private ObjectAnimator mAnimator;
    private boolean mAnimatorCancel;
    private Context mContext;
    private IControllerBridge mControllerBridge;
    private TextView mCurrentTime;
    private boolean mDragging;
    private TextView mEndTime;
    private ImageView mFfwdButton;
    private OnClickListener mFfwdListener;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private HTML5VideoFullScreen mFullScreen;
    private HTML5FullScreenTopbar mFullScreenTopbar;
    private Handler mHandler;
    private ImageView mPauseButton;
    private OnClickListener mPauseListener;
    private ImageView mRewButton;
    private OnClickListener mRewListener;
    private View mRootView;
    private int[] mSeekBarLoc;
    private OnSeekBarChangeListener mSeekListener;
    private boolean mShowing;
    private VideoSeekTimeIndicator mTimeIndicator;
    private BroadcastReceiver mTimeReceiver;
    private VideoSeekTimeBar mTimeSeekBar;

    class AnonymousClass_2 implements AnimatorListener {
        final /* synthetic */ boolean val$show;

        AnonymousClass_2(boolean z) {
            this.val$show = z;
        }

        public void onAnimationCancel(Animator animation) {
            HTML5MediaController.this.mAnimatorCancel = true;
        }

        public void onAnimationEnd(Animator animation) {
            if (!(this.val$show || HTML5MediaController.this.mAnimatorCancel)) {
                HTML5MediaController.this.mRootView.setVisibility(ItemAnimation.CUR_ARC);
                HTML5MediaController.this.mShowing = false;
            }
            if (HTML5MediaController.this.mAnimatorCancel) {
                HTML5MediaController.this.mAnimatorCancel = false;
            }
        }

        public void onAnimationRepeat(Animator animation) {
        }

        public void onAnimationStart(Animator animation) {
        }
    }

    public HTML5MediaController(Context context) {
        this(context, null);
    }

    public HTML5MediaController(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HTML5MediaController(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mSeekBarLoc = new int[2];
        this.mTimeReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals("android.intent.action.TIME_SET") || action.equals("android.intent.action.TIME_TICK") || action.equals("android.intent.action.TIMEZONE_CHANGED")) {
                    HTML5MediaController.this.updateTimeView();
                }
            }
        };
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FADE_OUT:
                        HTML5MediaController.this.hide();
                    case SHOW_PROGRESS:
                        int pos = HTML5MediaController.this.updateProgress();
                        if (!HTML5MediaController.this.mDragging && HTML5MediaController.this.mShowing && HTML5MediaController.this.mFullScreen.isPlaying()) {
                            sendMessageDelayed(obtainMessage(SHOW_PROGRESS), (long) (1000 - pos % 1000));
                        }
                    default:
                        break;
                }
            }
        };
        this.mPauseListener = new OnClickListener() {
            public void onClick(View v) {
                HTML5MediaController.this.doPauseResume();
                HTML5MediaController.this.show(DEFUALT_TIME_OUT);
            }
        };
        this.mSeekListener = new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
                if (fromuser && HTML5MediaController.this.mTimeSeekBar.isEnableShow()) {
                    String timeString = HTML5MediaController.this.stringForTime((((long) progress) * ((long) HTML5MediaController.this.mFullScreen.getDuration())) / 1000);
                    IBinder token = HTML5MediaController.this.getWindowToken();
                    if (!HTML5MediaController.this.mTimeIndicator.isShowing()) {
                        HTML5MediaController.this.mTimeIndicator.show(token, HTML5MediaController.this.calTimeIndicatorX(bar.getProgress()), HTML5MediaController.this.calTimeIndicatorY());
                    }
                    if (HTML5MediaController.this.mTimeIndicator.isShowing()) {
                        HTML5MediaController.this.mTimeIndicator.setText(timeString);
                        HTML5MediaController.this.mTimeIndicator.updatePosition(HTML5MediaController.this.calTimeIndicatorX(progress), HTML5MediaController.this.calTimeIndicatorY());
                    }
                }
            }

            public void onStartTrackingTouch(SeekBar bar) {
                HTML5MediaController.this.show(360000);
                HTML5MediaController.this.mDragging = true;
                HTML5MediaController.this.mHandler.removeMessages(SHOW_PROGRESS);
            }

            public void onStopTrackingTouch(SeekBar bar) {
                HTML5MediaController.this.mDragging = false;
                long newposition = (((long) bar.getProgress()) * ((long) HTML5MediaController.this.mFullScreen.getDuration())) / 1000;
                HTML5MediaController.this.mFullScreen.seekTo((int) newposition);
                if (HTML5MediaController.this.mCurrentTime != null) {
                    HTML5MediaController.this.mCurrentTime.setText(HTML5MediaController.this.stringForTime((long) ((int) newposition)));
                }
                HTML5MediaController.this.updateProgress();
                HTML5MediaController.this.updatePausePlay();
                HTML5MediaController.this.show(DEFUALT_TIME_OUT);
                HTML5MediaController.this.mHandler.sendEmptyMessage(SHOW_PROGRESS);
                if (HTML5MediaController.this.mTimeIndicator != null && HTML5MediaController.this.mTimeIndicator.isShowing()) {
                    HTML5MediaController.this.mTimeIndicator.dismiss();
                    HTML5MediaController.this.mTimeSeekBar.disableShowSeekbar();
                }
            }
        };
        this.mRewListener = new OnClickListener() {
            public void onClick(View v) {
                HTML5MediaController.this.mFullScreen.seekTo(HTML5MediaController.this.mFullScreen.getCurrentPosition() - 5000);
                HTML5MediaController.this.updateProgress();
                HTML5MediaController.this.show(DEFUALT_TIME_OUT);
            }
        };
        this.mFfwdListener = new OnClickListener() {
            public void onClick(View v) {
                HTML5MediaController.this.mFullScreen.seekTo(HTML5MediaController.this.mFullScreen.getCurrentPosition() + 5000);
                HTML5MediaController.this.updateProgress();
                HTML5MediaController.this.show(DEFUALT_TIME_OUT);
            }
        };
        this.mControllerBridge = new IControllerBridge() {
            public boolean isShowing() {
                return HTML5MediaController.this.isShowing();
            }

            public void setEnabled(boolean enable) {
                HTML5MediaController.this.setEnabled(enable);
            }

            public void setVideoFullScreen(HTML5VideoFullScreen view) {
                HTML5MediaController.this.mFullScreen = view;
                HTML5MediaController.this.mFullScreenTopbar = (HTML5FullScreenTopbar) view.getFullScreenTopbar();
                HTML5MediaController.this.updatePausePlay();
            }

            public void showOrHideController(boolean show) {
                if (show) {
                    HTML5MediaController.this.show();
                } else {
                    HTML5MediaController.this.hide();
                }
            }

            public void updateMediaController() {
                HTML5MediaController.this.updateMediaController();
            }
        };
        this.mContext = context;
        initMediaTimeIndicator();
        createControllerView();
        initControllerView();
        updateTimeView();
    }

    private int calTimeIndicatorX(int progress) {
        this.mTimeSeekBar.getLocationInWindow(this.mSeekBarLoc);
        return this.mSeekBarLoc[0] - this.mTimeSeekBar.getThumbOffset() + (((this.mTimeSeekBar.getWidth() - this.mTimeSeekBar.getPaddingLeft()) - this.mTimeSeekBar.getPaddingRight()) * progress) / this.mTimeSeekBar.getMax();
    }

    private int calTimeIndicatorY() {
        this.mTimeSeekBar.getLocationInWindow(this.mSeekBarLoc);
        return this.mSeekBarLoc[1] - this.mTimeIndicator.getHeight() - (this.mRootView.getHeight() - this.mTimeSeekBar.getHeight()) / 2;
    }

    private void createControllerView() {
        View view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(17367110, null);
        addView(view, new LayoutParams(-2, -1));
        this.mRootView = view;
    }

    private void disableUnsupportedButtons() {
        try {
            if (!(this.mPauseButton == null || this.mFullScreen.canPause())) {
                this.mPauseButton.setEnabled(false);
            }
            if (!(this.mRewButton == null || this.mFullScreen.canSeekBackward())) {
                this.mRewButton.setEnabled(false);
            }
            if (this.mFfwdButton != null && !this.mFullScreen.canSeekForward()) {
                this.mFfwdButton.setEnabled(false);
            }
        } catch (IncompatibleClassChangeError e) {
        }
    }

    private void doPauseResume() {
        if (this.mFullScreen.isPlaying()) {
            this.mFullScreen.pauseAndDispatch();
        } else if (this.mFullScreen.isReleased()) {
            this.mFullScreen.restart();
        } else {
            this.mFullScreen.startAndDispatch();
        }
        updatePausePlay();
    }

    private void initControllerView() {
        View v = this.mRootView;
        this.mPauseButton = (ImageView) v.findViewById(16908933);
        if (this.mPauseButton != null) {
            this.mPauseButton.requestFocus();
            this.mPauseButton.setOnClickListener(this.mPauseListener);
        }
        this.mFfwdButton = (ImageView) v.findViewById(16908938);
        if (this.mFfwdButton != null) {
            this.mFfwdButton.setOnClickListener(this.mFfwdListener);
        }
        this.mRewButton = (ImageView) v.findViewById(16908934);
        if (this.mRewButton != null) {
            this.mRewButton.setOnClickListener(this.mRewListener);
        }
        this.mTimeSeekBar = (VideoSeekTimeBar) v.findViewById(16908936);
        if (this.mTimeSeekBar != null) {
            this.mTimeSeekBar.setOnSeekBarChangeListener(this.mSeekListener);
            this.mTimeSeekBar.setMax(1000);
        }
        this.mEndTime = (TextView) v.findViewById(16908937);
        this.mCurrentTime = (TextView) v.findViewById(16908935);
        this.mFormatBuilder = new StringBuilder();
        this.mFormatter = new Formatter(this.mFormatBuilder, Locale.getDefault());
    }

    private void initMediaTimeIndicator() {
        this.mTimeIndicator = (VideoSeekTimeIndicator) ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(17367145, null);
    }

    public static boolean isHdmiPlugIn() {
        return Integer.valueOf(SystemProperties.get(HDMI_PLUGIN_KEY, "0")).intValue() == 1;
    }

    private void mediaControllerAnimation(boolean show, boolean anim) {
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
            this.mAnimator = ObjectAnimator.ofFloat(this.mRootView, "alpha", new float[]{fromAlpha, toAlpha});
            this.mAnimator.setDuration(500);
            this.mAnimator.addListener(new AnonymousClass_2(show));
            this.mAnimator.start();
        } else {
            this.mRootView.setAlpha(toAlpha);
            if (!show) {
                this.mRootView.setVisibility(ItemAnimation.CUR_ARC);
                this.mShowing = false;
            }
        }
    }

    private String stringForTime(long timeMs) {
        int totalSeconds = (int) (timeMs / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        this.mFormatBuilder.setLength(0);
        if (hours > 0) {
            return this.mFormatter.format("%d:%02d:%02d", new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
        } else {
            return this.mFormatter.format("%02d:%02d", new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
        }
    }

    private void updatePausePlay() {
        if (this.mFullScreen.isPlaying()) {
            this.mPauseButton.setImageResource(17302529);
        } else {
            this.mPauseButton.setImageResource(17302532);
        }
    }

    private int updateProgress() {
        if (this.mDragging) {
            return 0;
        }
        if (this.mFullScreen == null) {
            if (this.mTimeSeekBar != null) {
                this.mTimeSeekBar.setProgress(0);
            }
            return 0;
        } else {
            int position = this.mFullScreen.getCurrentPosition();
            int duration = this.mFullScreen.getDuration();
            if (this.mTimeSeekBar != null) {
                this.mTimeSeekBar.setSecondaryProgress(this.mFullScreen.getBufferPercentage() * 10);
                if (duration > 0) {
                    this.mTimeSeekBar.setProgress((int) ((1000 * ((long) position)) / ((long) duration)));
                } else if (this.mFullScreen.isReleased()) {
                    this.mTimeSeekBar.setProgress(1000);
                } else {
                    this.mTimeSeekBar.setProgress(0);
                    this.mTimeSeekBar.setSecondaryProgress(0);
                }
            }
            if (!this.mFullScreen.isReleased()) {
                if (this.mEndTime != null) {
                    this.mEndTime.setText(stringForTime((long) duration));
                }
                if (this.mCurrentTime == null) {
                    return position;
                }
                this.mCurrentTime.setText(stringForTime((long) position));
                return position;
            } else if (this.mCurrentTime == null) {
                return position;
            } else {
                this.mCurrentTime.setText(this.mEndTime.getText());
                return position;
            }
        }
    }

    private void updateTimeView() {
        String timeFormat = "kk:mm";
        if (!DateFormat.is24HourFormat(this.mContext)) {
            timeFormat = "hh:mm";
        }
        String time = DateFormat.format(timeFormat, new Date()).toString();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean dispatchKeyEvent(android.view.KeyEvent r6_event) {
        throw new UnsupportedOperationException("Method not decompiled: com.wmt.frameworkbridge.video.html5.HTML5MediaController.dispatchKeyEvent(android.view.KeyEvent):boolean");
        /*
        r5 = this;
        r4 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
        r2 = 1;
        r0 = r6.getKeyCode();
        r3 = r6.getRepeatCount();
        if (r3 != 0) goto L_0x0032;
    L_0x000d:
        r3 = r6.getAction();
        if (r3 != 0) goto L_0x0032;
    L_0x0013:
        r1 = r2;
    L_0x0014:
        r3 = 79;
        if (r0 == r3) goto L_0x0020;
    L_0x0018:
        r3 = 85;
        if (r0 == r3) goto L_0x0020;
    L_0x001c:
        r3 = 62;
        if (r0 != r3) goto L_0x0034;
    L_0x0020:
        if (r1 == 0) goto L_0x0031;
    L_0x0022:
        r5.doPauseResume();
        r5.show(r4);
        r3 = r5.mPauseButton;
        if (r3 == 0) goto L_0x0031;
    L_0x002c:
        r3 = r5.mPauseButton;
        r3.requestFocus();
    L_0x0031:
        return r2;
    L_0x0032:
        r1 = 0;
        goto L_0x0014;
    L_0x0034:
        r3 = 126; // 0x7e float:1.77E-43 double:6.23E-322;
        if (r0 != r3) goto L_0x004e;
    L_0x0038:
        if (r1 == 0) goto L_0x0031;
    L_0x003a:
        r3 = r5.mFullScreen;
        r3 = r3.isPlaying();
        if (r3 != 0) goto L_0x0031;
    L_0x0042:
        r3 = r5.mFullScreen;
        r3.start();
        r5.updatePausePlay();
        r5.show(r4);
        goto L_0x0031;
    L_0x004e:
        r3 = 86;
        if (r0 == r3) goto L_0x0056;
    L_0x0052:
        r3 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        if (r0 != r3) goto L_0x006c;
    L_0x0056:
        if (r1 == 0) goto L_0x0031;
    L_0x0058:
        r3 = r5.mFullScreen;
        r3 = r3.isPlaying();
        if (r3 == 0) goto L_0x0031;
    L_0x0060:
        r3 = r5.mFullScreen;
        r3.pause();
        r5.updatePausePlay();
        r5.show(r4);
        goto L_0x0031;
    L_0x006c:
        r3 = 25;
        if (r0 == r3) goto L_0x0078;
    L_0x0070:
        r3 = 24;
        if (r0 == r3) goto L_0x0078;
    L_0x0074:
        r3 = 164; // 0xa4 float:2.3E-43 double:8.1E-322;
        if (r0 != r3) goto L_0x007d;
    L_0x0078:
        r2 = super.dispatchKeyEvent(r6);
        goto L_0x0031;
    L_0x007d:
        r3 = 4;
        if (r0 == r3) goto L_0x0084;
    L_0x0080:
        r3 = 82;
        if (r0 != r3) goto L_0x008a;
    L_0x0084:
        if (r1 == 0) goto L_0x0031;
    L_0x0086:
        r5.hide();
        goto L_0x0031;
    L_0x008a:
        r5.show(r4);
        r2 = super.dispatchKeyEvent(r6);
        goto L_0x0031;
        */
    }

    public IControllerBridge getBridgeImpl() {
        return this.mControllerBridge;
    }

    public void hide() {
        boolean z;
        if (this.mFullScreenTopbar != null) {
            this.mFullScreenTopbar.hide();
        }
        if (this.mFullScreen != null) {
            this.mFullScreen.notifyVisibleChanged(false);
        }
        if (isHdmiPlugIn()) {
            z = false;
        } else {
            z = true;
        }
        mediaControllerAnimation(false, z);
    }

    public boolean isShowing() {
        return this.mShowing;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.TIME_TICK");
        filter.addAction("android.intent.action.TIME_SET");
        filter.addAction("android.intent.action.TIMEZONE_CHANGED");
        this.mContext.registerReceiver(this.mTimeReceiver, filter);
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mTimeSeekBar.getLayoutParams().width = getResources().getDimensionPixelSize(17104905);
        requestLayout();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mHandler.removeMessages(FADE_OUT);
        this.mHandler.removeMessages(SHOW_PROGRESS);
        this.mContext.unregisterReceiver(this.mTimeReceiver);
    }

    public boolean onTouchEvent(MotionEvent event) {
        show(DEFUALT_TIME_OUT);
        return true;
    }

    public void setEnabled(boolean enabled) {
        if (this.mPauseButton != null) {
            this.mPauseButton.setEnabled(enabled);
        }
        if (this.mFfwdButton != null) {
            this.mFfwdButton.setEnabled(enabled);
        }
        if (this.mRewButton != null) {
            this.mRewButton.setEnabled(enabled);
        }
        if (this.mTimeSeekBar != null) {
            this.mTimeSeekBar.setEnabled(enabled);
        }
        disableUnsupportedButtons();
        super.setEnabled(enabled);
    }

    public void show() {
        show(DEFUALT_TIME_OUT);
    }

    public void show(int timeout) {
        if (this.mFullScreenTopbar != null) {
            this.mFullScreenTopbar.show(timeout);
        }
        updateProgress();
        if (this.mShowing) {
            this.mRootView.setAlpha(1.0f);
        } else {
            boolean z;
            if (this.mPauseButton != null) {
                this.mPauseButton.requestFocus();
            }
            this.mShowing = true;
            if (isHdmiPlugIn()) {
                z = false;
            } else {
                z = true;
            }
            mediaControllerAnimation(true, z);
        }
        if (this.mFullScreen != null) {
            this.mFullScreen.notifyVisibleChanged(true);
        }
        this.mRootView.setVisibility(0);
        updatePausePlay();
        this.mHandler.sendEmptyMessage(SHOW_PROGRESS);
        Message msg = this.mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            this.mHandler.removeMessages(FADE_OUT);
            this.mHandler.sendMessageDelayed(msg, (long) timeout);
        }
    }

    public void updateMediaController() {
        this.mHandler.removeMessages(SHOW_PROGRESS);
        this.mHandler.sendEmptyMessage(SHOW_PROGRESS);
        updatePausePlay();
    }
}