package com.mopub.mobileads;

import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.VideoView;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.mopub.common.util.Dips;
import com.mopub.common.util.Drawables;

public class MraidVideoViewController extends BaseVideoViewController {
    private static final float CLOSE_BUTTON_PADDING = 8.0f;
    private static final float CLOSE_BUTTON_SIZE = 50.0f;
    private int mButtonPadding;
    private int mButtonSize;
    private ImageButton mCloseButton;
    private final VideoView mVideoView;

    MraidVideoViewController(Context context, Bundle bundle, long broadcastIdentifier, BaseVideoViewControllerListener baseVideoViewControllerListener) {
        super(context, broadcastIdentifier, baseVideoViewControllerListener);
        this.mVideoView = new VideoView(context);
        this.mVideoView.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                MraidVideoViewController.this.mCloseButton.setVisibility(0);
                MraidVideoViewController.this.videoCompleted(true);
            }
        });
        this.mVideoView.setOnErrorListener(new OnErrorListener() {
            public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                MraidVideoViewController.this.mCloseButton.setVisibility(0);
                MraidVideoViewController.this.videoError(false);
                return false;
            }
        });
        this.mVideoView.setVideoPath(bundle.getString("video_url"));
    }

    private void createInterstitialCloseButton() {
        this.mCloseButton = new ImageButton(getContext());
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{-16842919}, Drawables.INTERSTITIAL_CLOSE_BUTTON_NORMAL.decodeImage(getContext()));
        states.addState(new int[]{16842919}, Drawables.INTERSTITIAL_CLOSE_BUTTON_PRESSED.decodeImage(getContext()));
        this.mCloseButton.setImageDrawable(states);
        this.mCloseButton.setBackgroundDrawable(null);
        this.mCloseButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MraidVideoViewController.this.getBaseVideoViewControllerListener().onFinish();
            }
        });
        LayoutParams buttonLayout = new LayoutParams(this.mButtonSize, this.mButtonSize);
        buttonLayout.addRule(ApiEventType.API_MRAID_EXPAND);
        buttonLayout.setMargins(this.mButtonPadding, 0, this.mButtonPadding, 0);
        getLayout().addView(this.mCloseButton, buttonLayout);
    }

    VideoView getVideoView() {
        return this.mVideoView;
    }

    void onCreate() {
        super.onCreate();
        this.mButtonSize = Dips.asIntPixels(CLOSE_BUTTON_SIZE, getContext());
        this.mButtonPadding = Dips.asIntPixels(CLOSE_BUTTON_PADDING, getContext());
        createInterstitialCloseButton();
        this.mCloseButton.setVisibility(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
        this.mVideoView.start();
    }

    void onDestroy() {
    }

    void onPause() {
    }

    void onResume() {
    }
}