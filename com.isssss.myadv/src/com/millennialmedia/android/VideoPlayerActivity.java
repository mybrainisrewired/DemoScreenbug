package com.millennialmedia.android;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import android.widget.VideoView;
import com.google.android.gms.wallet.WalletConstants;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.inmobi.monetization.internal.InvalidManifestConfigException;
import java.lang.ref.WeakReference;

class VideoPlayerActivity extends MMBaseActivity implements OnCompletionListener, OnErrorListener, OnPreparedListener {
    private static final int CONTROLS_ID = 83756563;
    private static final String END_VIDEO = "endVideo";
    protected static final int MESSAGE_CHECK_PLAYING_VIDEO = 4;
    protected static final int MESSAGE_DELAYED_BUTTON = 3;
    protected static final int MESSAGE_INACTIVITY_ANIMATION = 1;
    protected static final int MESSAGE_ONE_SECOND_CHECK = 2;
    protected static final int MESSAGE_SET_TRANSPARENCY = 5;
    private static final String RESTART_VIDEO = "restartVideo";
    private static final String TAG = "VideoPlayerActivity";
    View blackView;
    protected int currentVideoPosition;
    protected boolean hasBottomBar;
    private boolean hasFocus;
    boolean isPaused;
    boolean isUserPausing;
    protected boolean isVideoCompleted;
    private boolean isVideoCompletedOnce;
    String lastOverlayOrientation;
    protected VideoView mVideoView;
    Button pausePlay;
    ProgressBar progBar;
    RedirectionListenerImpl redirectListenerImpl;
    private boolean shouldSetUri;
    TransparentHandler transparentHandler;
    RelativeLayout videoLayout;

    class AnonymousClass_1 implements Runnable {
        final /* synthetic */ String val$action;

        AnonymousClass_1(String str) {
            this.val$action = str;
        }

        public void run() {
            if (this.val$action.equalsIgnoreCase(RESTART_VIDEO)) {
                VideoPlayerActivity.this.restartVideo();
            } else if (this.val$action.equalsIgnoreCase(END_VIDEO)) {
                VideoPlayerActivity.this.endVideo();
            }
        }
    }

    private static class TransparentHandler extends Handler {
        private WeakReference<VideoPlayerActivity> activityRef;

        public TransparentHandler(VideoPlayerActivity activity) {
            this.activityRef = new WeakReference(activity);
        }

        public void handleMessage(Message msg) {
            VideoPlayerActivity activity = (VideoPlayerActivity) this.activityRef.get();
            if (activity != null) {
                activity.handleTransparentMessage(msg);
            }
        }
    }

    static class VideoRedirectionListener extends RedirectionListenerImpl {
        WeakReference<VideoPlayerActivity> activityRef;

        class AnonymousClass_1 implements Runnable {
            final /* synthetic */ VideoPlayerActivity val$activity;

            AnonymousClass_1(VideoPlayerActivity videoPlayerActivity) {
                this.val$activity = videoPlayerActivity;
            }

            public void run() {
                this.val$activity.enableButtons();
            }
        }

        public VideoRedirectionListener(VideoPlayerActivity activity) {
            if (activity != null) {
                this.activityRef = new WeakReference(activity);
                if (activity.activity != null) {
                    this.creatorAdImplInternalId = activity.activity.creatorAdImplInternalId;
                }
            }
        }

        public OverlaySettings getOverlaySettings() {
            VideoPlayerActivity activity = (VideoPlayerActivity) this.activityRef.get();
            if (activity == null || activity.lastOverlayOrientation == null) {
                return null;
            }
            OverlaySettings settings = new OverlaySettings();
            settings.orientation = activity.lastOverlayOrientation;
            return settings;
        }

        public boolean isHandlingMMVideo(Uri uri) {
            VideoPlayerActivity activity = (VideoPlayerActivity) this.activityRef.get();
            if (activity != null) {
                activity.runOnUiThread(new AnonymousClass_1(activity));
                if (uri != null && activity.isActionable(uri)) {
                    activity.processVideoPlayerUri(uri.getHost());
                    return true;
                }
            }
            return false;
        }
    }

    VideoPlayerActivity() {
        this.shouldSetUri = true;
        this.hasBottomBar = true;
        this.currentVideoPosition = 0;
        this.transparentHandler = new TransparentHandler(this);
        this.isUserPausing = false;
    }

    private void initBottomBar(RelativeLayout parent) {
        RelativeLayout controlsLayout = new RelativeLayout(this.activity);
        controlsLayout.setId(CONTROLS_ID);
        controlsLayout.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        LayoutParams controlsLp = new LayoutParams(-1, -2);
        controlsLayout.setLayoutParams(controlsLp);
        controlsLp.addRule(ApiEventType.API_MRAID_RESIZE);
        Button mRewind = new Button(this.activity);
        this.pausePlay = new Button(this.activity);
        Button mStop = new Button(this.activity);
        mRewind.setBackgroundResource(17301541);
        if (this.mVideoView.isPlaying()) {
            this.pausePlay.setBackgroundResource(17301539);
        } else {
            this.pausePlay.setBackgroundResource(17301540);
        }
        mStop.setBackgroundResource(17301560);
        LayoutParams pauseLp = new LayoutParams(-2, -2);
        LayoutParams stopLp = new LayoutParams(-2, -2);
        LayoutParams rewindLp = new LayoutParams(-2, -2);
        pauseLp.addRule(ApiEventType.API_MRAID_IS_VIEWABLE);
        controlsLayout.addView(this.pausePlay, pauseLp);
        rewindLp.addRule(0, this.pausePlay.getId());
        controlsLayout.addView(mRewind);
        stopLp.addRule(ApiEventType.API_MRAID_EXPAND);
        controlsLayout.addView(mStop, stopLp);
        mRewind.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (VideoPlayerActivity.this.mVideoView != null) {
                    VideoPlayerActivity.this.mVideoView.seekTo(0);
                }
            }
        });
        this.pausePlay.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (VideoPlayerActivity.this.mVideoView == null) {
                    return;
                }
                if (VideoPlayerActivity.this.mVideoView.isPlaying()) {
                    VideoPlayerActivity.this.pauseVideoByUser();
                    VideoPlayerActivity.this.pausePlay.setBackgroundResource(17301540);
                } else {
                    if (VideoPlayerActivity.this.isVideoCompleted) {
                        VideoPlayerActivity.this.playVideo(0);
                    } else if (!VideoPlayerActivity.this.isUserPausing || VideoPlayerActivity.this.isVideoCompleted) {
                        VideoPlayerActivity.this.playVideo(VideoPlayerActivity.this.currentVideoPosition);
                    } else {
                        VideoPlayerActivity.this.resumeVideo();
                    }
                    VideoPlayerActivity.this.pausePlay.setBackgroundResource(17301539);
                }
            }
        });
        mStop.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (VideoPlayerActivity.this.mVideoView != null) {
                    VideoPlayerActivity.this.shouldSetUri = true;
                    VideoPlayerActivity.this.dismiss();
                }
            }
        });
        parent.addView(controlsLayout, controlsLp);
    }

    private void initRedirectListener() {
        this.redirectListenerImpl = new VideoRedirectionListener(this);
    }

    private void initVideoListeners() {
        this.mVideoView.setOnCompletionListener(this);
        this.mVideoView.setOnPreparedListener(this);
        this.mVideoView.setOnErrorListener(this);
    }

    private void initWindow() {
        requestWindowFeature(MESSAGE_INACTIVITY_ANIMATION);
        getWindow().clearFlags(AccessibilityNodeInfoCompat.ACTION_PREVIOUS_HTML_ELEMENT);
        getWindow().addFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
    }

    private boolean isActionSupported(String action) {
        return action != null && (action.equalsIgnoreCase(RESTART_VIDEO) || action.equalsIgnoreCase(END_VIDEO));
    }

    private boolean isActionable(Uri actionUri) {
        if (actionUri.getScheme().equalsIgnoreCase("mmsdk") && isActionSupported(actionUri.getHost())) {
            return true;
        }
        MMLog.v(TAG, String.format("Unrecognized mmsdk:// URI %s.", new Object[]{actionUri}));
        return false;
    }

    private void makeTransparent() {
        if (!this.transparentHandler.hasMessages(MESSAGE_CHECK_PLAYING_VIDEO)) {
            this.transparentHandler.sendEmptyMessage(MESSAGE_CHECK_PLAYING_VIDEO);
        }
    }

    private void startVideo(int position) {
        this.mVideoView.requestFocus();
        this.mVideoView.seekTo(position);
        if (((PowerManager) getSystemService("power")).isScreenOn()) {
            if (this.progBar != null) {
                this.progBar.bringToFront();
                this.progBar.setVisibility(0);
            }
            if (this.pausePlay != null) {
                this.pausePlay.setBackgroundResource(17301539);
            }
            this.mVideoView.start();
            makeTransparent();
        }
    }

    protected boolean canFadeButtons() {
        return !this.isVideoCompleted;
    }

    protected void dismiss() {
        MMLog.d(TAG, "Video ad player closed");
        if (this.mVideoView != null) {
            if (this.mVideoView.isPlaying()) {
                this.mVideoView.stopPlayback();
            }
            this.mVideoView = null;
        }
        finish();
    }

    void dispatchButtonClick(String urlString) {
        if (urlString != null) {
            MMLog.d(TAG, String.format("Button Click with URL: %s", new Object[]{urlString}));
            this.redirectListenerImpl.url = urlString;
            this.redirectListenerImpl.weakContext = new WeakReference(this.activity);
            if (!this.redirectListenerImpl.isHandlingMMVideo(Uri.parse(urlString))) {
                HttpRedirection.startActivityFromUri(this.redirectListenerImpl);
            }
        }
    }

    protected void enableButtons() {
    }

    protected void endVideo() {
        MMLog.d(TAG, "End Video.");
        if (this.mVideoView != null) {
            this.shouldSetUri = true;
            dismiss();
        }
    }

    protected void errorPlayVideo(String error) {
        Toast.makeText(this.activity, "Sorry. There was a problem playing the video", MESSAGE_INACTIVITY_ANIMATION).show();
        if (this.mVideoView != null) {
            this.mVideoView.stopPlayback();
        }
    }

    void handleTransparentMessage(Message msg) {
        switch (msg.what) {
            case MESSAGE_CHECK_PLAYING_VIDEO:
                if (this.mVideoView == null || !this.mVideoView.isPlaying() || this.mVideoView.getCurrentPosition() <= 0) {
                    this.transparentHandler.sendEmptyMessageDelayed(MESSAGE_CHECK_PLAYING_VIDEO, 50);
                } else {
                    this.mVideoView.setBackgroundColor(0);
                    this.transparentHandler.sendEmptyMessageDelayed(MESSAGE_SET_TRANSPARENCY, 100);
                }
            case MESSAGE_SET_TRANSPARENCY:
                if (this.mVideoView != null && this.mVideoView.isPlaying() && this.mVideoView.getCurrentPosition() > 0) {
                    this.blackView.setVisibility(MESSAGE_CHECK_PLAYING_VIDEO);
                    this.progBar.setVisibility(MESSAGE_CHECK_PLAYING_VIDEO);
                }
            default:
                break;
        }
    }

    protected RelativeLayout initLayout() {
        RelativeLayout parent = new RelativeLayout(this.activity);
        parent.setId(400);
        parent.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        parent.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        this.videoLayout = new RelativeLayout(this.activity);
        this.videoLayout.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        this.videoLayout.setId(WalletConstants.ERROR_CODE_INVALID_TRANSACTION);
        LayoutParams videoContainerLp = new LayoutParams(-1, -2);
        LayoutParams videoLp = new LayoutParams(-1, -1);
        videoLp.addRule(ApiEventType.API_MRAID_CLOSE);
        videoContainerLp.addRule(ApiEventType.API_MRAID_CLOSE);
        this.mVideoView = new VideoView(this.activity);
        this.mVideoView.setId(WalletConstants.ERROR_CODE_AUTHENTICATION_FAILURE);
        this.mVideoView.getHolder().setFormat(InvalidManifestConfigException.MISSING_ACTIVITY_DECLARATION);
        this.mVideoView.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        initVideoListeners();
        this.videoLayout.addView(this.mVideoView, videoLp);
        this.blackView = new View(this.activity);
        this.blackView.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        LayoutParams blackViewParams = new LayoutParams(-1, -1);
        parent.addView(this.videoLayout, videoContainerLp);
        if (this.hasBottomBar) {
            blackViewParams.addRule(MESSAGE_ONE_SECOND_CHECK, CONTROLS_ID);
            initBottomBar(parent);
        }
        this.blackView.setLayoutParams(blackViewParams);
        parent.addView(this.blackView);
        this.progBar = new ProgressBar(this.activity);
        this.progBar.setIndeterminate(true);
        LayoutParams progParams = new LayoutParams(-2, -2);
        progParams.addRule(ApiEventType.API_MRAID_CLOSE);
        this.progBar.setLayoutParams(progParams);
        parent.addView(this.progBar);
        this.progBar.setVisibility(MESSAGE_CHECK_PLAYING_VIDEO);
        return parent;
    }

    protected void initSavedInstance(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.isVideoCompleted = savedInstanceState.getBoolean("videoCompleted");
            this.isVideoCompletedOnce = savedInstanceState.getBoolean("videoCompletedOnce");
            this.currentVideoPosition = savedInstanceState.getInt("videoPosition");
            this.hasBottomBar = savedInstanceState.getBoolean("hasBottomBar");
            this.shouldSetUri = savedInstanceState.getBoolean("shouldSetUri");
        }
    }

    protected boolean isPlayable() {
        return (this.mVideoView == null || this.mVideoView.isPlaying() || this.isVideoCompleted) ? false : true;
    }

    protected void logButtonEvent(VideoImage button) {
        MMLog.d(TAG, "Cached video button event logged");
        int i = 0;
        while (i < button.eventLoggingUrls.length) {
            Event.logEvent(button.eventLoggingUrls[i]);
            i++;
        }
    }

    public void onCompletion(MediaPlayer mp) {
        this.isVideoCompletedOnce = true;
        this.isVideoCompleted = true;
        if (!(this.pausePlay == null || this.mVideoView.isPlaying())) {
            this.pausePlay.setBackgroundResource(17301540);
        }
        MMLog.v(TAG, "Video player on complete");
    }

    public void onCreate(Bundle savedInstanceState) {
        setTheme(16973829);
        super.onCreate(savedInstanceState);
        MMLog.d(TAG, "Setting up the video player");
        initWindow();
        initSavedInstance(savedInstanceState);
        initRedirectListener();
        setContentView(initLayout());
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return (keyCode == 4 && event.getRepeatCount() == 0 && !this.isVideoCompletedOnce) ? true : super.onKeyDown(keyCode, event);
    }

    protected void onPause() {
        super.onPause();
        this.isPaused = true;
        MMLog.v(TAG, "VideoPlayer - onPause");
        pauseVideo();
    }

    public void onPrepared(MediaPlayer mp) {
        MMLog.d(TAG, "Video Prepared");
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        this.currentVideoPosition = savedInstanceState.getInt("currentVideoPosition");
        this.isVideoCompleted = savedInstanceState.getBoolean("isVideoCompleted");
        this.isVideoCompletedOnce = savedInstanceState.getBoolean("isVideoCompletedOnce");
        this.hasBottomBar = savedInstanceState.getBoolean("hasBottomBar", this.hasBottomBar);
        this.shouldSetUri = savedInstanceState.getBoolean("shouldSetUri", this.shouldSetUri);
        this.isUserPausing = savedInstanceState.getBoolean("isUserPausing", this.isUserPausing);
        this.isPaused = savedInstanceState.getBoolean("isPaused", this.isPaused);
        super.onRestoreInstanceState(savedInstanceState);
    }

    protected void onResume() {
        super.onResume();
        this.blackView.bringToFront();
        this.blackView.setVisibility(0);
        this.isPaused = false;
        MMLog.v(TAG, "VideoPlayer - onResume");
        if (this.hasFocus && !this.isUserPausing) {
            resumeVideo();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("currentVideoPosition", this.currentVideoPosition);
        outState.putBoolean("isVideoCompleted", this.isVideoCompleted);
        outState.putBoolean("isVideoCompletedOnce", this.isVideoCompletedOnce);
        outState.putBoolean("hasBottomBar", this.hasBottomBar);
        outState.putBoolean("shouldSetUri", this.shouldSetUri);
        outState.putBoolean("isUserPausing", this.isUserPausing);
        outState.putBoolean("isPaused", this.isPaused);
        super.onSaveInstanceState(outState);
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        this.hasFocus = hasWindowFocus;
        if (!this.isPaused && hasWindowFocus && !this.isUserPausing) {
            resumeVideo();
        }
    }

    protected void pauseVideo() {
        if (this.mVideoView != null && this.mVideoView.isPlaying()) {
            this.currentVideoPosition = this.mVideoView.getCurrentPosition();
            this.mVideoView.pause();
            MMLog.v(TAG, "Video paused");
        }
    }

    protected void pauseVideoByUser() {
        this.isUserPausing = true;
        pauseVideo();
    }

    protected void playVideo(int position) {
        try {
            this.isUserPausing = false;
            String fullPath = getIntent().getData().toString();
            MMLog.d(TAG, String.format("playVideo path: %s", new Object[]{fullPath}));
            if (fullPath == null || fullPath.length() == 0 || this.mVideoView == null) {
                errorPlayVideo("no name or null videoview");
            } else {
                this.isVideoCompleted = false;
                if (this.shouldSetUri && this.mVideoView != null) {
                    this.mVideoView.setVideoURI(Uri.parse(fullPath));
                }
                startVideo(position);
            }
        } catch (Exception e) {
            Exception e2 = e;
            MMLog.e(TAG, "playVideo error: ", e2);
            errorPlayVideo("error: " + e2);
        }
    }

    void processVideoPlayerUri(String action) {
        runOnUiThread(new AnonymousClass_1(action));
    }

    protected void restartVideo() {
        MMLog.d(TAG, "Restart Video.");
        if (this.mVideoView != null) {
            playVideo(0);
        }
    }

    protected void resumeVideo() {
        if (isPlayable()) {
            playVideo(this.currentVideoPosition);
        }
    }

    protected void setButtonAlpha(ImageButton button, float alpha) {
        AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
        animation.setDuration(0);
        animation.setFillEnabled(true);
        animation.setFillBefore(true);
        animation.setFillAfter(true);
        button.startAnimation(animation);
    }
}