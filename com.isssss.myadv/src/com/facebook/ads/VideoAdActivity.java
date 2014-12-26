package com.facebook.ads;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.VideoView;
import com.android.volley.DefaultRetryPolicy;
import com.facebook.ads.internal.HtmlAdDataModel;
import com.facebook.ads.internal.action.AdActionFactory;
import com.facebook.ads.internal.action.AppAdAction;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class VideoAdActivity extends Activity {
    private static final long CONTROLS_FADE_DELAY = 3000;
    public static final String MARKET_URI_INTENT_EXTRA = "adMarketUri";
    public static final String URI_INTENT_EXTRA = "adUri";
    public static final String VIDEO_PATH_INTENT_EXTRA = "adVideoPath";
    private List<ImageButton> mAllControls;
    private List<ImageButton> mBufferingView;
    private ImageButton mCloseButton;
    private int mCurrentPosition;
    private List<ImageButton> mEndControls;
    private List<ImageButton> mFullPlayingControls;
    private boolean mGoToAppStore;
    private Handler mHandler;
    private ImageButton mInstallButton;
    private boolean mIsMuted;
    private MediaPlayer mMediaPlayer;
    private List<ImageButton> mMinPlayingControls;
    private ImageButton mMuteButton;
    private List<ImageButton> mPausedControls;
    private View mRootView;
    private ImageButton mSkipButton;
    private PlayerState mState;
    private long mTimeOfLastTouch;
    private VideoView mVideoView;
    private Uri marketUri;
    private String path;
    private RelativeLayout relativeLayout;
    private String uniqueId;
    private Uri uri;

    static /* synthetic */ class AnonymousClass_12 {
        static final /* synthetic */ int[] $SwitchMap$com$facebook$ads$VideoAdActivity$PlayerState;

        static {
            $SwitchMap$com$facebook$ads$VideoAdActivity$PlayerState = new int[PlayerState.values().length];
            try {
                $SwitchMap$com$facebook$ads$VideoAdActivity$PlayerState[PlayerState.PLAYING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$facebook$ads$VideoAdActivity$PlayerState[PlayerState.PAUSED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$facebook$ads$VideoAdActivity$PlayerState[PlayerState.UNINITIALIZED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$facebook$ads$VideoAdActivity$PlayerState[PlayerState.INITIALIZED.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            $SwitchMap$com$facebook$ads$VideoAdActivity$PlayerState[PlayerState.COMPLETED.ordinal()] = 5;
        }
    }

    private enum PlayerState {
        UNINITIALIZED,
        INITIALIZED,
        PLAYING,
        PAUSED,
        COMPLETED
    }

    public VideoAdActivity() {
        this.mIsMuted = false;
        this.mCurrentPosition = -1;
        this.mHandler = new Handler();
        this.mGoToAppStore = false;
    }

    private void activateControlSet(List<ImageButton> controls) {
        Iterator i$ = this.mAllControls.iterator();
        while (i$.hasNext()) {
            ImageButton button = (ImageButton) i$.next();
            if (controls.contains(button)) {
                button.setVisibility(0);
            } else {
                button.setVisibility(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
            }
        }
    }

    private void bindModel() {
        setState(PlayerState.UNINITIALIZED);
        this.mInstallButton.setBackground(getResources().getDrawable(17301633));
        this.mSkipButton.setBackground(getResources().getDrawable(17301569));
        this.mMuteButton.setBackground(getResources().getDrawable(17301554));
        this.mCloseButton.setBackground(getResources().getDrawable(17301560));
        this.mVideoView.setVideoPath(this.path);
    }

    private void configureLayout() {
        this.relativeLayout.addView(this.mRootView);
        this.relativeLayout.addView(this.mInstallButton);
        this.relativeLayout.addView(this.mSkipButton);
        this.relativeLayout.addView(this.mMuteButton);
        this.relativeLayout.addView(this.mCloseButton);
    }

    private void configureViews() {
        this.mAllControls = Arrays.asList(new ImageButton[]{this.mInstallButton, this.mMuteButton, this.mCloseButton, this.mSkipButton});
        this.mFullPlayingControls = Arrays.asList(new ImageButton[]{this.mInstallButton, this.mMuteButton, this.mCloseButton, this.mSkipButton});
        this.mMinPlayingControls = Arrays.asList(new ImageButton[]{this.mCloseButton, this.mInstallButton, this.mSkipButton});
        this.mPausedControls = Arrays.asList(new ImageButton[]{this.mInstallButton, this.mMuteButton, this.mCloseButton, this.mSkipButton});
        this.mEndControls = Arrays.asList(new ImageButton[]{this.mInstallButton, this.mMuteButton, this.mCloseButton, this.mSkipButton});
        this.mBufferingView = Arrays.asList(new ImageButton[]{this.mInstallButton, this.mCloseButton, this.mSkipButton});
        OnTouchListener dimmerListener = new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    v.setAlpha(0.5f);
                } else if (event.getAction() == 1) {
                    v.setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                }
                return false;
            }
        };
        this.mInstallButton.setOnTouchListener(dimmerListener);
        this.mSkipButton.setOnTouchListener(dimmerListener);
        this.mMuteButton.setOnTouchListener(dimmerListener);
        this.mCloseButton.setOnTouchListener(dimmerListener);
        this.mInstallButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                VideoAdActivity.this.mGoToAppStore = true;
                VideoAdActivity.this.finish();
            }
        });
        this.mSkipButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                VideoAdActivity.this.displayInterstitial();
            }
        });
        this.mMuteButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                VideoAdActivity.this.videoMute();
            }
        });
        this.mCloseButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                VideoAdActivity.this.finish();
            }
        });
        this.mRootView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                VideoAdActivity.this.pulseControlsFromTouch();
                return false;
            }
        });
        this.mVideoView.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                VideoAdActivity.this.mMediaPlayer = mp;
                if (VideoAdActivity.this.mIsMuted) {
                    VideoAdActivity.this.mMediaPlayer.setVolume(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
                } else {
                    VideoAdActivity.this.mMediaPlayer.setVolume(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                }
                VideoAdActivity.this.updateVideoPlayerSize();
                if (VideoAdActivity.this.mState == PlayerState.PAUSED || VideoAdActivity.this.mState == PlayerState.COMPLETED) {
                    VideoAdActivity.this.mMediaPlayer.seekTo(VideoAdActivity.this.mCurrentPosition);
                } else if (VideoAdActivity.this.mState == PlayerState.PLAYING) {
                    VideoAdActivity.this.mMediaPlayer.seekTo(VideoAdActivity.this.mCurrentPosition);
                    VideoAdActivity.this.videoPlay();
                } else if (VideoAdActivity.this.mState == PlayerState.UNINITIALIZED) {
                    VideoAdActivity.this.setState(PlayerState.INITIALIZED);
                    VideoAdActivity.this.videoPlay();
                }
            }
        });
        this.mVideoView.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                VideoAdActivity.this.setState(PlayerState.COMPLETED);
                VideoAdActivity.this.displayInterstitial();
            }
        });
        this.mVideoView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                VideoAdActivity.this.pulseControlsFromTouch();
                return false;
            }
        });
    }

    private void displayInterstitial() {
        Intent intent = new Intent(this, InterstitialAdActivity.class);
        Display display = ((WindowManager) getSystemService("window")).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        intent.putExtra(InterstitialAd.DISPLAY_ROTATION_INTENT_EXTRA, display.getRotation());
        intent.putExtra(InterstitialAd.DISPLAY_WIDTH_INTENT_EXTRA, displayMetrics.widthPixels);
        intent.putExtra(InterstitialAd.DISPLAY_HEIGHT_INTENT_EXTRA, displayMetrics.heightPixels);
        intent.putExtra(InterstitialAd.INTERSTITIAL_UNIQUE_ID_EXTRA, this.uniqueId);
        HtmlAdDataModel.fromIntentExtra(getIntent()).addToIntentExtra(intent);
        startActivity(intent);
        finish();
    }

    private void getIntentExtras() {
        Bundle extra = getIntent().getExtras();
        this.uri = Uri.parse(extra.getString(URI_INTENT_EXTRA));
        this.marketUri = Uri.parse(extra.getString(MARKET_URI_INTENT_EXTRA));
        this.path = extra.getString(VIDEO_PATH_INTENT_EXTRA);
        this.uniqueId = extra.getString(InterstitialAd.INTERSTITIAL_UNIQUE_ID_EXTRA);
    }

    private void pulseControlsFromTouch() {
        this.mTimeOfLastTouch = System.currentTimeMillis();
        updateControlState();
        this.mHandler.removeCallbacksAndMessages(null);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                VideoAdActivity.this.updateControlState();
            }
        }, CONTROLS_FADE_DELAY);
    }

    private void setButtonPosition() {
        this.mInstallButton.setX(BitmapDescriptorFactory.HUE_VIOLET);
        this.mInstallButton.setY(1400.0f);
        this.mSkipButton.setX(700.0f);
        this.mSkipButton.setY(1400.0f);
        this.mMuteButton.setX(10.0f);
        this.mMuteButton.setY(1160.0f);
        this.mCloseButton.setX(970.0f);
        this.mInstallButton.setScaleX(1.5f);
        this.mInstallButton.setScaleY(1.5f);
        this.mSkipButton.setScaleX(1.5f);
        this.mSkipButton.setScaleY(1.5f);
    }

    private void setState(PlayerState state) {
        if (state != this.mState) {
            this.mState = state;
            pulseControlsFromTouch();
        }
    }

    private void updateControlState() {
        this.mInstallButton.setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        this.mSkipButton.setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        this.mCloseButton.setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        this.mVideoView.setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        switch (AnonymousClass_12.$SwitchMap$com$facebook$ads$VideoAdActivity$PlayerState[this.mState.ordinal()]) {
            case MMAdView.TRANSITION_FADE:
                if (System.currentTimeMillis() - this.mTimeOfLastTouch >= 3000) {
                    activateControlSet(this.mMinPlayingControls);
                    this.mInstallButton.setAlpha(0.5f);
                    this.mSkipButton.setAlpha(0.5f);
                    this.mCloseButton.setAlpha(0.5f);
                } else {
                    activateControlSet(this.mFullPlayingControls);
                }
            case MMAdView.TRANSITION_UP:
                activateControlSet(this.mPausedControls);
            case MMAdView.TRANSITION_DOWN:
                activateControlSet(this.mBufferingView);
            case MMAdView.TRANSITION_RANDOM:
                activateControlSet(this.mBufferingView);
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                activateControlSet(this.mEndControls);
                this.mMediaPlayer.seekTo((int) (((float) this.mMediaPlayer.getDuration()) * 0.75f));
                this.mVideoView.setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            default:
                break;
        }
    }

    private void updateVideoPlayerSize() {
        if (this.mMediaPlayer != null) {
            float scale = Math.min(((float) this.mRootView.getWidth()) / ((float) this.mMediaPlayer.getVideoWidth()), ((float) this.mRootView.getHeight()) / ((float) this.mMediaPlayer.getVideoHeight()));
            this.mVideoView.getHolder().setFixedSize((int) (((float) this.mMediaPlayer.getVideoWidth()) * scale), (int) (((float) this.mMediaPlayer.getVideoHeight()) * scale));
            this.mVideoView.requestLayout();
            this.mVideoView.invalidate();
        }
    }

    private void videoBackground() {
        if (this.mVideoView.isPlaying()) {
            this.mCurrentPosition = this.mVideoView.getCurrentPosition();
        }
        this.mVideoView.pause();
        this.mHandler.removeCallbacksAndMessages(null);
        this.mMediaPlayer = null;
    }

    private void videoMute() {
        if (this.mState != PlayerState.UNINITIALIZED) {
            if (this.mIsMuted) {
                this.mMediaPlayer.setVolume(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                this.mMuteButton.setBackground(getResources().getDrawable(17301554));
            } else {
                this.mMediaPlayer.setVolume(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
                this.mMuteButton.setBackground(getResources().getDrawable(17301553));
            }
            this.mIsMuted = !this.mIsMuted;
        }
    }

    private void videoPlay() {
        if (this.mState != PlayerState.UNINITIALIZED && this.mState != PlayerState.PLAYING) {
            if (this.mState == PlayerState.COMPLETED) {
                this.mVideoView.seekTo(0);
            }
            this.mVideoView.start();
            this.mCurrentPosition = this.mVideoView.getCurrentPosition();
            setState(PlayerState.PLAYING);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                VideoAdActivity.this.mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                VideoAdActivity.this.updateVideoPlayerSize();
            }
        });
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        this.relativeLayout = new RelativeLayout(this);
        LayoutParams lp = new LayoutParams(-1, -1);
        lp.addRule(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE);
        setContentView(this.relativeLayout, lp);
        this.mVideoView = new VideoView(this);
        this.mVideoView.setLayoutParams(lp);
        this.relativeLayout.addView(this.mVideoView);
        this.mRootView = new View(this);
        this.mInstallButton = new ImageButton(this);
        this.mSkipButton = new ImageButton(this);
        this.mMuteButton = new ImageButton(this);
        this.mCloseButton = new ImageButton(this);
        getIntentExtras();
        setVolumeControlStream(MMAdView.TRANSITION_DOWN);
        configureViews();
        bindModel();
        setButtonPosition();
        configureLayout();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mGoToAppStore) {
            ((AppAdAction) AdActionFactory.getAdAction(this, this.uri)).goToMarketURL();
        }
    }

    protected void onPause() {
        super.onPause();
        videoBackground();
    }

    protected void onResume() {
        super.onResume();
        if (this.mState == PlayerState.PLAYING && !this.mVideoView.isPlaying()) {
            this.mVideoView.seekTo(this.mCurrentPosition);
            this.mVideoView.start();
        }
        pulseControlsFromTouch();
    }

    protected void onStop() {
        super.onStop();
        videoBackground();
    }
}