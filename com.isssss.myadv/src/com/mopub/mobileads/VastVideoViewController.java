package com.mopub.mobileads;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.VideoView;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.DownloadResponse;
import com.mopub.common.DownloadTask;
import com.mopub.common.DownloadTask.DownloadTaskListener;
import com.mopub.common.HttpClient;
import com.mopub.common.HttpResponses;
import com.mopub.common.MoPubBrowser;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.AsyncTasks;
import com.mopub.common.util.Dips;
import com.mopub.common.util.Drawables;
import com.mopub.common.util.Streams;
import com.mopub.common.util.VersionCode;
import com.mopub.mobileads.util.vast.VastCompanionAd;
import com.mopub.mobileads.util.vast.VastVideoConfiguration;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

public class VastVideoViewController extends BaseVideoViewController implements DownloadTaskListener {
    static final int DEFAULT_VIDEO_DURATION_FOR_CLOSE_BUTTON = 5000;
    private static final float FIRST_QUARTER_MARKER = 0.25f;
    static final int MAX_VIDEO_DURATION_FOR_CLOSE_BUTTON = 16000;
    private static final int MAX_VIDEO_RETRIES = 1;
    private static final float MID_POINT_MARKER = 0.5f;
    private static final int MOPUB_BROWSER_REQUEST_CODE = 1;
    private static final float THIRD_QUARTER_MARKER = 0.75f;
    static final String VAST_VIDEO_CONFIGURATION = "vast_video_configuration";
    private static final long VIDEO_PROGRESS_TIMER_CHECKER_DELAY = 50;
    private static final int VIDEO_VIEW_FILE_PERMISSION_ERROR = Integer.MIN_VALUE;
    private static final ThreadPoolExecutor sThreadPoolExecutor;
    private final OnTouchListener mClickThroughListener;
    private final ImageView mCompanionAdImageView;
    private final Handler mHandler;
    private boolean mIsFirstMarkHit;
    private boolean mIsSecondMarkHit;
    private boolean mIsStartMarkHit;
    private boolean mIsThirdMarkHit;
    private boolean mIsVideoFinishedPlaying;
    private boolean mIsVideoProgressShouldBeChecked;
    private int mSeekerPositionOnPause;
    private int mShowCloseButtonDelay;
    private boolean mShowCloseButtonEventFired;
    private final VastCompanionAd mVastCompanionAd;
    private final VastVideoConfiguration mVastVideoConfiguration;
    private final VastVideoToolbar mVastVideoToolbar;
    private final Runnable mVideoProgressCheckerRunnable;
    private int mVideoRetries;
    private final VideoView mVideoView;

    class AnonymousClass_6 implements OnCompletionListener {
        private final /* synthetic */ Context val$context;
        private final /* synthetic */ VideoView val$videoView;

        AnonymousClass_6(Context context, VideoView videoView) {
            this.val$context = context;
            this.val$videoView = videoView;
        }

        public void onCompletion(MediaPlayer mp) {
            VastVideoViewController.this.stopProgressChecker();
            VastVideoViewController.this.makeVideoInteractable();
            VastVideoViewController.this.videoCompleted(false);
            HttpClient.makeTrackingHttpRequest(VastVideoViewController.this.mVastVideoConfiguration.getCompleteTrackers(), this.val$context);
            VastVideoViewController.this.mIsVideoFinishedPlaying = true;
            this.val$videoView.setVisibility(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
            if (VastVideoViewController.this.mCompanionAdImageView.getDrawable() != null) {
                VastVideoViewController.this.mCompanionAdImageView.setVisibility(0);
            }
        }
    }

    static {
        sThreadPoolExecutor = new ThreadPoolExecutor(10, 50, 1, TimeUnit.SECONDS, new LinkedBlockingQueue());
    }

    VastVideoViewController(Context context, Bundle bundle, long broadcastIdentifier, BaseVideoViewControllerListener baseVideoViewControllerListener) throws IllegalStateException {
        super(context, broadcastIdentifier, baseVideoViewControllerListener);
        this.mShowCloseButtonDelay = 5000;
        this.mHandler = new Handler();
        this.mIsVideoProgressShouldBeChecked = false;
        this.mSeekerPositionOnPause = -1;
        this.mVideoRetries = 0;
        Serializable serializable = bundle.getSerializable(VAST_VIDEO_CONFIGURATION);
        if (serializable == null || !serializable instanceof VastVideoConfiguration) {
            throw new IllegalStateException("VastVideoConfiguration is invalid");
        }
        this.mVastVideoConfiguration = (VastVideoConfiguration) serializable;
        if (this.mVastVideoConfiguration.getDiskMediaFileUrl() == null) {
            throw new IllegalStateException("VastVideoConfiguration does not have a video disk path");
        }
        this.mVastCompanionAd = this.mVastVideoConfiguration.getVastCompanionAd();
        this.mClickThroughListener = new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 1 && VastVideoViewController.this.shouldAllowClickThrough()) {
                    VastVideoViewController.this.handleClick(VastVideoViewController.this.mVastVideoConfiguration.getClickTrackers(), VastVideoViewController.this.mVastVideoConfiguration.getClickThroughUrl());
                }
                return true;
            }
        };
        createVideoBackground(context);
        this.mVideoView = createVideoView(context);
        this.mVideoView.requestFocus();
        this.mVastVideoToolbar = createVastVideoToolBar(context);
        getLayout().addView(this.mVastVideoToolbar);
        this.mCompanionAdImageView = createCompanionAdImageView(context);
        HttpClient.makeTrackingHttpRequest(this.mVastVideoConfiguration.getImpressionTrackers(), context);
        this.mVideoProgressCheckerRunnable = createVideoProgressCheckerRunnable();
    }

    private ImageView createCompanionAdImageView(Context context) {
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setGravity(ApiEventType.API_MRAID_GET_SCREEN_SIZE);
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        layoutParams.addRule(MMAdView.TRANSITION_DOWN, this.mVastVideoToolbar.getId());
        getLayout().addView(relativeLayout, layoutParams);
        ImageView imageView = new ImageView(context);
        imageView.setVisibility(MMAdView.TRANSITION_RANDOM);
        relativeLayout.addView(imageView, new LayoutParams(-1, -1));
        return imageView;
    }

    private VastVideoToolbar createVastVideoToolBar(Context context) {
        VastVideoToolbar vastVideoToolbar = new VastVideoToolbar(context);
        vastVideoToolbar.setCloseButtonOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 1) {
                    VastVideoViewController.this.getBaseVideoViewControllerListener().onFinish();
                }
                return true;
            }
        });
        vastVideoToolbar.setLearnMoreButtonOnTouchListener(this.mClickThroughListener);
        return vastVideoToolbar;
    }

    private void createVideoBackground(Context context) {
        GradientDrawable gradientDrawable = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{Color.argb(0, 0, 0, 0), Color.argb(MotionEventCompat.ACTION_MASK, 0, 0, 0)});
        getLayout().setBackgroundDrawable(new LayerDrawable(new Drawable[]{Drawables.THATCHED_BACKGROUND.decodeImage(context), gradientDrawable}));
    }

    private Runnable createVideoProgressCheckerRunnable() {
        return new Runnable() {
            public void run() {
                float videoLength = (float) VastVideoViewController.this.mVideoView.getDuration();
                float currentPosition = (float) VastVideoViewController.this.mVideoView.getCurrentPosition();
                if (videoLength > 0.0f) {
                    float progressPercentage = currentPosition / videoLength;
                    if (!VastVideoViewController.this.mIsStartMarkHit && currentPosition >= 1000.0f) {
                        VastVideoViewController.this.mIsStartMarkHit = true;
                        HttpClient.makeTrackingHttpRequest(VastVideoViewController.this.mVastVideoConfiguration.getStartTrackers(), VastVideoViewController.this.getContext());
                    }
                    if (!VastVideoViewController.this.mIsFirstMarkHit && progressPercentage > 0.25f) {
                        VastVideoViewController.this.mIsFirstMarkHit = true;
                        HttpClient.makeTrackingHttpRequest(VastVideoViewController.this.mVastVideoConfiguration.getFirstQuartileTrackers(), VastVideoViewController.this.getContext());
                    }
                    if (!VastVideoViewController.this.mIsSecondMarkHit && progressPercentage > 0.5f) {
                        VastVideoViewController.this.mIsSecondMarkHit = true;
                        HttpClient.makeTrackingHttpRequest(VastVideoViewController.this.mVastVideoConfiguration.getMidpointTrackers(), VastVideoViewController.this.getContext());
                    }
                    if (!VastVideoViewController.this.mIsThirdMarkHit && progressPercentage > 0.75f) {
                        VastVideoViewController.this.mIsThirdMarkHit = true;
                        HttpClient.makeTrackingHttpRequest(VastVideoViewController.this.mVastVideoConfiguration.getThirdQuartileTrackers(), VastVideoViewController.this.getContext());
                    }
                    if (VastVideoViewController.this.isLongVideo(VastVideoViewController.this.mVideoView.getDuration())) {
                        VastVideoViewController.this.mVastVideoToolbar.updateCountdownWidget(VastVideoViewController.this.mShowCloseButtonDelay - VastVideoViewController.this.mVideoView.getCurrentPosition());
                    }
                    if (VastVideoViewController.this.shouldBeInteractable()) {
                        VastVideoViewController.this.makeVideoInteractable();
                    }
                }
                VastVideoViewController.this.mVastVideoToolbar.updateDurationWidget(VastVideoViewController.this.mVideoView.getDuration() - VastVideoViewController.this.mVideoView.getCurrentPosition());
                if (VastVideoViewController.this.mIsVideoProgressShouldBeChecked) {
                    VastVideoViewController.this.mHandler.postDelayed(VastVideoViewController.this.mVideoProgressCheckerRunnable, VIDEO_PROGRESS_TIMER_CHECKER_DELAY);
                }
            }
        };
    }

    private VideoView createVideoView(Context context) {
        VideoView videoView = new VideoView(context);
        videoView.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                if (VastVideoViewController.this.mVideoView.getDuration() < 16000) {
                    VastVideoViewController.this.mShowCloseButtonDelay = VastVideoViewController.this.mVideoView.getDuration();
                }
            }
        });
        videoView.setOnTouchListener(this.mClickThroughListener);
        videoView.setOnCompletionListener(new AnonymousClass_6(context, videoView));
        videoView.setOnErrorListener(new OnErrorListener() {
            public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                if (VastVideoViewController.this.retryMediaPlayer(mediaPlayer, what, extra)) {
                    return true;
                }
                VastVideoViewController.this.stopProgressChecker();
                VastVideoViewController.this.makeVideoInteractable();
                VastVideoViewController.this.videoError(false);
                return false;
            }
        });
        videoView.setVideoPath(this.mVastVideoConfiguration.getDiskMediaFileUrl());
        return videoView;
    }

    private void downloadCompanionAd() {
        if (this.mVastCompanionAd != null) {
            try {
                HttpGet httpGet = HttpClient.initializeHttpGet(this.mVastCompanionAd.getImageUrl(), getContext());
                AsyncTasks.safeExecuteOnExecutor(new DownloadTask(this), new HttpUriRequest[]{httpGet});
            } catch (Exception e) {
                MoPubLog.d("Failed to download companion ad", e);
            }
        }
    }

    private void handleClick(Iterable clickThroughTrackers, String clickThroughUrl) {
        HttpClient.makeTrackingHttpRequest(clickThroughTrackers, getContext());
        videoClicked();
        Bundle bundle = new Bundle();
        bundle.putString(MoPubBrowser.DESTINATION_URL_KEY, clickThroughUrl);
        getBaseVideoViewControllerListener().onStartActivityForResult(MoPubBrowser.class, MOPUB_BROWSER_REQUEST_CODE, bundle);
    }

    private boolean isLongVideo(int duration) {
        return duration >= 16000;
    }

    private void makeVideoInteractable() {
        this.mShowCloseButtonEventFired = true;
        this.mVastVideoToolbar.makeInteractable();
    }

    private boolean shouldAllowClickThrough() {
        return this.mShowCloseButtonEventFired;
    }

    private boolean shouldBeInteractable() {
        return !this.mShowCloseButtonEventFired && this.mVideoView.getCurrentPosition() > this.mShowCloseButtonDelay;
    }

    private void startProgressChecker() {
        if (!this.mIsVideoProgressShouldBeChecked) {
            this.mIsVideoProgressShouldBeChecked = true;
            this.mHandler.post(this.mVideoProgressCheckerRunnable);
        }
    }

    private void stopProgressChecker() {
        if (this.mIsVideoProgressShouldBeChecked) {
            this.mIsVideoProgressShouldBeChecked = false;
            this.mHandler.removeCallbacks(this.mVideoProgressCheckerRunnable);
        }
    }

    boolean backButtonEnabled() {
        return this.mShowCloseButtonEventFired;
    }

    @Deprecated
    ImageView getCompanionAdImageView() {
        return this.mCompanionAdImageView;
    }

    @Deprecated
    boolean getIsVideoProgressShouldBeChecked() {
        return this.mIsVideoProgressShouldBeChecked;
    }

    @Deprecated
    int getShowCloseButtonDelay() {
        return this.mShowCloseButtonDelay;
    }

    @Deprecated
    Runnable getVideoProgressCheckerRunnable() {
        return this.mVideoProgressCheckerRunnable;
    }

    @Deprecated
    int getVideoRetries() {
        return this.mVideoRetries;
    }

    VideoView getVideoView() {
        return this.mVideoView;
    }

    @Deprecated
    boolean isShowCloseButtonEventFired() {
        return this.mShowCloseButtonEventFired;
    }

    @Deprecated
    boolean isVideoFinishedPlaying() {
        return this.mIsVideoFinishedPlaying;
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == -1) {
            getBaseVideoViewControllerListener().onFinish();
        }
    }

    public void onComplete(String url, DownloadResponse downloadResponse) {
        if (downloadResponse != null && downloadResponse.getStatusCode() == 200) {
            Bitmap companionAdBitmap = HttpResponses.asBitmap(downloadResponse);
            if (companionAdBitmap != null) {
                int width = Dips.dipsToIntPixels((float) companionAdBitmap.getWidth(), getContext());
                int height = Dips.dipsToIntPixels((float) companionAdBitmap.getHeight(), getContext());
                int imageViewWidth = this.mCompanionAdImageView.getMeasuredWidth();
                int imageViewHeight = this.mCompanionAdImageView.getMeasuredHeight();
                if (width < imageViewWidth && height < imageViewHeight) {
                    this.mCompanionAdImageView.getLayoutParams().width = width;
                    this.mCompanionAdImageView.getLayoutParams().height = height;
                }
                this.mCompanionAdImageView.setImageBitmap(companionAdBitmap);
                this.mCompanionAdImageView.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (VastVideoViewController.this.mVastCompanionAd != null) {
                            VastVideoViewController.this.handleClick(VastVideoViewController.this.mVastCompanionAd.getClickTrackers(), VastVideoViewController.this.mVastCompanionAd.getClickThroughUrl());
                        }
                    }
                });
            }
        }
    }

    void onCreate() {
        super.onCreate();
        getBaseVideoViewControllerListener().onSetRequestedOrientation(0);
        broadcastAction("com.mopub.action.interstitial.show");
        downloadCompanionAd();
    }

    void onDestroy() {
        stopProgressChecker();
        broadcastAction("com.mopub.action.interstitial.dismiss");
    }

    void onPause() {
        stopProgressChecker();
        this.mSeekerPositionOnPause = this.mVideoView.getCurrentPosition();
        this.mVideoView.pause();
    }

    void onResume() {
        this.mVideoRetries = 0;
        startProgressChecker();
        this.mVideoView.seekTo(this.mSeekerPositionOnPause);
        if (!this.mIsVideoFinishedPlaying) {
            this.mVideoView.start();
        }
    }

    boolean retryMediaPlayer(MediaPlayer mediaPlayer, int what, int extra) {
        if (!VersionCode.currentApiLevel().isBelow(VersionCode.JELLY_BEAN) || what != 1 || extra != Integer.MIN_VALUE || this.mVideoRetries >= 1) {
            return false;
        }
        FileInputStream inputStream = null;
        try {
            mediaPlayer.reset();
            FileInputStream inputStream2 = new FileInputStream(new File(this.mVastVideoConfiguration.getDiskMediaFileUrl()));
            try {
                mediaPlayer.setDataSource(inputStream2.getFD());
                mediaPlayer.prepareAsync();
                this.mVideoView.start();
                Streams.closeStream(inputStream2);
                this.mVideoRetries++;
                return true;
            } catch (Exception e) {
                inputStream = inputStream2;
                Streams.closeStream(inputStream);
                this.mVideoRetries++;
                return false;
            } catch (Throwable th) {
                th = th;
                inputStream = inputStream2;
                Streams.closeStream(inputStream);
                this.mVideoRetries++;
                throw th;
            }
        } catch (Exception e2) {
            Streams.closeStream(inputStream);
            this.mVideoRetries++;
            return false;
        } catch (Throwable th2) {
            Throwable th3 = th2;
            Streams.closeStream(inputStream);
            this.mVideoRetries++;
            throw th3;
        }
    }

    @Deprecated
    void setCloseButtonVisible(boolean visible) {
        this.mShowCloseButtonEventFired = visible;
    }
}