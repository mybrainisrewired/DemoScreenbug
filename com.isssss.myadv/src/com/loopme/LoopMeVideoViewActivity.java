package com.loopme;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;
import com.loopme.utilites.Drawables;
import com.loopme.utilites.ScreenMetrics;
import com.millennialmedia.android.MMAdView;

public class LoopMeVideoViewActivity extends Activity {
    private static final String EVENT_NAME = "eventName";
    private static final String FINISH_EVENT = "finish";
    private static final String JS_INTENT_ACTION = "com.loopme.jsevent";
    private static final String LOG_TAG;
    private static final String X_SECONDS_EVENT = "x_seconds";
    private ImageButton mCloseButton;
    private boolean mIsCloseAdListActivity;
    private FrameLayout mLayout;
    private String mVideoUrl;
    private VideoView mVideoView;
    private View mView;

    class AnonymousClass_1 implements OnPreparedListener {
        private final /* synthetic */ String val$jsEventName;
        private final /* synthetic */ String val$jsTime;

        AnonymousClass_1(String str, String str2) {
            this.val$jsEventName = str;
            this.val$jsTime = str2;
        }

        public void onPrepared(MediaPlayer mp) {
            LoopMeVideoViewActivity.this.mVideoView.start();
            if (this.val$jsEventName != null && this.val$jsEventName.equalsIgnoreCase(X_SECONDS_EVENT)) {
                Integer time = Integer.valueOf(0);
                if (this.val$jsTime != null && this.val$jsTime.length() > 0) {
                    try {
                        time = Integer.valueOf(Integer.valueOf(Integer.parseInt(this.val$jsTime)).intValue() * 1000);
                    } catch (NumberFormatException e) {
                        Utilities.log(LOG_TAG, new StringBuilder("Exception: ").append(e.getMessage()).toString(), LogLevel.ERROR);
                    }
                }
                LoopMeVideoViewActivity.this.mVideoView.postDelayed(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(JS_INTENT_ACTION);
                        intent.putExtra(EVENT_NAME, X_SECONDS_EVENT);
                        LocalBroadcastManager.getInstance(AnonymousClass_1.this.this$0).sendBroadcast(intent);
                    }
                }, (long) time.intValue());
            }
        }
    }

    class AnonymousClass_2 implements OnCompletionListener {
        private final /* synthetic */ String val$jsEventName;

        AnonymousClass_2(String str) {
            this.val$jsEventName = str;
        }

        public void onCompletion(MediaPlayer mp) {
            Utilities.log(LOG_TAG, "Video playing completed", LogLevel.DEBUG);
            if (this.val$jsEventName != null && this.val$jsEventName.equalsIgnoreCase(FINISH_EVENT)) {
                Intent intent = new Intent(JS_INTENT_ACTION);
                intent.putExtra(EVENT_NAME, FINISH_EVENT);
                LocalBroadcastManager.getInstance(LoopMeVideoViewActivity.this).sendBroadcast(intent);
            }
            LoopMeVideoViewActivity.this.finish();
        }
    }

    static {
        LOG_TAG = LoopMeVideoViewActivity.class.getSimpleName();
    }

    private FrameLayout buildLayout() {
        this.mLayout = new FrameLayout(this);
        LayoutParams params = new LayoutParams(-1, -1, 17);
        this.mLayout.setLayoutParams(params);
        this.mLayout.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        this.mVideoView = new VideoView(this);
        this.mVideoView.setLayoutParams(params);
        this.mLayout.addView(this.mVideoView);
        this.mCloseButton = new ImageButton(this);
        LayoutParams btn_close_params = new LayoutParams(ScreenMetrics.getInstance().getBtnCloseHeight(this), ScreenMetrics.getInstance().getBtnCloseHeight(this), 5);
        btn_close_params.rightMargin = 20;
        btn_close_params.topMargin = 20;
        this.mCloseButton.setLayoutParams(btn_close_params);
        if (VERSION.SDK_INT < 16) {
            this.mCloseButton.setBackgroundDrawable(Drawables.BTN_CLOSE_LIST.decodeImage(this));
        } else {
            this.mCloseButton.setBackground(Drawables.BTN_CLOSE_LIST.decodeImage(this));
        }
        this.mCloseButton.setVisibility(MMAdView.TRANSITION_RANDOM);
        this.mLayout.addView(this.mCloseButton);
        this.mView = new View(this);
        this.mView.setLayoutParams(new LayoutParams(ScreenMetrics.getInstance().getCloseTouchAreaHeight(this) + 20, ScreenMetrics.getInstance().getCloseTouchAreaHeight(this) + 20, 5));
        this.mView.setBackgroundColor(0);
        this.mLayout.addView(this.mView);
        return this.mLayout;
    }

    private void closeParentActivity() {
        LoopMeAdListActivity.finishAdListActivity();
    }

    private void setCloseBtnDelay(int delay) {
        this.mCloseButton.postDelayed(new Runnable() {
            public void run() {
                Utilities.log(LOG_TAG, "Close button did appear", LogLevel.DEBUG);
                LoopMeVideoViewActivity.this.mCloseButton.setVisibility(0);
            }
        }, (long) delay);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        Utilities.log(LOG_TAG, "onConfigurationChanged", LogLevel.DEBUG);
        super.onConfigurationChanged(newConfig);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(buildLayout());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.mVideoUrl = extras.getString("videourl");
        }
        LoopMeInterstitial interstitial = (LoopMeInterstitial) BaseLoopMeHolder.get();
        String jsEventName = interstitial.getJsEventName();
        String jsTime = interstitial.getJsTime();
        int delay = interstitial.getCloseBtnDelay();
        try {
            MediaController mediacontroller = new MediaController(this);
            mediacontroller.setAnchorView(this.mVideoView);
            this.mVideoView.setMediaController(mediacontroller);
            this.mVideoView.setVideoPath(this.mVideoUrl);
        } catch (Exception e) {
            Utilities.log(LOG_TAG, new StringBuilder("Exception: ").append(e.getMessage()).toString(), LogLevel.ERROR);
        }
        this.mVideoView.requestFocus();
        this.mVideoView.setOnPreparedListener(new AnonymousClass_1(jsEventName, jsTime));
        this.mVideoView.setOnCompletionListener(new AnonymousClass_2(jsEventName));
        this.mView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LoopMeVideoViewActivity.this.mVideoView.stopPlayback();
                LoopMeVideoViewActivity.this.mIsCloseAdListActivity = true;
                LoopMeVideoViewActivity.this.finish();
            }
        });
        setCloseBtnDelay(delay);
    }

    protected void onDestroy() {
        this.mLayout.removeAllViews();
        super.onDestroy();
    }

    protected void onPause() {
        super.onPause();
        finish();
        if (this.mIsCloseAdListActivity) {
            closeParentActivity();
        }
    }
}