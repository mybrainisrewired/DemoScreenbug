package com.millennialmedia.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.PowerManager;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Map;

class InlineVideoView extends VideoView implements Serializable {
    private static final String ANCHOR_IN_PNG = "iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAABOpJREFUeNrUmmtIY0cUgCfJ3VBXUXxR0dp2Ya0aBSFi6yOtFPGF1d1WavrAXy1YwVcLLaQpttsH+lcbirYq0a3Ptd0WpKi1P1SMaAitJNptalcQIwV/iI+o2ST39kx6I9cY3STcuSYHhjuTzD33fJk5M+fciWh9fR0tLCwgrhwfH6OcnBwkl8ur9vb2DIODg5aIiAjkq1itViQWi1FYWBgiKYmJiQhptdpzX6SlpSGz2fwRwzDHR0dH83V1dYn+KK6oqLjR19enQISluLgYof7+/jMfZmRkIIvFcoc5K783NTU95YvSsrKyFLvd/s/a2pqRNEBJSclZAJlMhra2tjyNP4Vobm5OukxhaWlpysnJyQPceXl5WS8SicgDDAwMuBrp6enY+M+Zy2UFIJ72pqy8vDwFfOcvd0ebzWYoLCwkD9DR0YFSU1Ox8W2Mb7IKEM+4lWBnHRoaSnM6nX97jhhAkQfQ6/XXNjY27jD+iamhoeFZiqLQyMhIOrQfeuljhCklJg4AD7rFBCA0Tf+h0+luQ9V80UgBAEUaQAzrvB7qk/7eDA6alZeXdx+qKRd0wb++hPRKJIZNaru+vv4NqP/Gs25sPEUcIDw8HHV1de2BY1bzDCFmC1kA91rd2dmJIV6D6nRIjQC3ARD7LMQvPOmWCArAQlhbWlqUPEBIrgQAC2xuh42NjUoI5O5BkwmpEXCLRqM5XFlZ+Qaq9qB2Yq9jL5HgHRaW+bzvoSkNZic+9wA2tnmxpqbmB2jGB7sPUB67KzZeAcb/CM24kHBi9z6Ar8PDwy8plcr7PBgvnBND9uSa82D8y2D8TzwZLxgAhRN4yF+zwfhfeX7gdSjXiANgp11cXDyCjOxr1ick7MojYQ2g2Cu3fvpZcnKyOCEhQcL5xd1XK+QLu8QB2GDuTyjvB6IgKSkJZWZmejouLrTBYLAJvoz6KxaLxVVYcbLljOTm5oqioqLwlHJw+jB4+s7NzQX8bIfDQX6jwdLb2/upTCZ7kzWcZouTHSUnZHd2tv2IvdrZ4vC4uuv09va2eXx8XCMIgNVqxXPsOW/fZWdnB6TTZrNp8egJAgByyKey/f19LeTD7+IFSCwQgINH47+rrKx8Z2lpyRkZGSkYwCM+lBwcHHwLxtfB1ME+hN+ohM4IsMa/B8YzPuUDwQSwu7urAePrZ2dnGd73AdJTCGK1n2traxvBeP8ysmAZAQhHXlCpVK/Ex8dfKUCgaSkO8xMKCgpGJycnb3uDCBUnvi6Xy4cA4lZcXFxIAmAJA4iRqamp12NjY0+TsJDaB0CeAIjB6enpmujo6P8PE0NtJ8Y5CEDcnZiYUOJQngpBACzS/Pz8u1lZWfSVr0I7OzufrK6uqgMZCUjG3qauEgDCgy+qqqq+woftsFGJIGf40g+dD+fn51XnzolJCCQtb3meP21ubn7GPcXE89lkMql9POFab21tTYVMDwkCMDMzo+Q+fXR0VI1DYW/5tdFoVD3G+AdtbW2uY62ioiJhAKqrq1/lGo9PNy8S/P8HgPjwEuNvuvueO6knDTA2NvbxZcY/BmKtvb39JrefC6Cnp4c4AMQyT3Z3d5f6cw9+3aPT6T7AlkNEalKr1Tc8+ygUCiSC7B7BMkYUgKZpnIQjqVTqeo3pYxjtGglY65+H+ib40b949Lj/v4iJiUH/CTAAFI2ZNCJ5irUAAAAASUVORK5CYII=";
    private static final String ANCHOR_OUT_PNG = "iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAABEZJREFUeNrUml9IU1Ecx8+2uwnCrBlaGD2YEKiVla2ypAj8X+l8KYpkLxUFoT0VBT1YUYYP6ktQCGUQgQr+w0o3sgdDEv+AaBH9odicEq4XY25u7vY9dhe3m9N5d+8Z/uDHvRzPn+/nnN/5d6eG5/mNhJCjcAPcD18QngHhPSDxBVE+cfo/aXa7/afL5eKJipaSkkI4QfwzuBZOGwwKQoKS90jTqOvm5uZqrFbrYzUB8vPzFwEMgnhqGtq44LLN4/F8bmxsfEdUNo1GswjgF3peo0SlED9hsVhKbTbbV8LAtELMBhUSP1ZeXn6clXgxwEK0FSHmqfgTvb293whD45QAEMQfh3gHYWxa0eohy2ZmZt4i5o/19PQwFx8CmI9mBLq6umrQ804SI4t6BCoqKh5UVVWZYwVAsBMfhrv56GwKEPtYay8oKFgEyIH/4KO36crKyoOxADDTxnll7AcgDrEG2A2f5JUzCpHLEmAn3MEraxTiCCuAdPj3ZcR4ZEK4MbH3qw2w7FFidna2t6mpiU7M1zLqT8QSu43FMroV/vm/bvd47Dhvm2ge9OQGJL1ZTfc7nc77WVlZOhYhtAX+USLeVlhYaBJnXg0ExN/LyMhgNolT4B9CjeNg9hLiE5YqAAgTsthWEp+ens50Gd0If08b93q9r8KJF0GsR1Z7GPG3WfS8FCCBzgGfz/e6qKgoIZKCWCLXS0dicnKyOjMzk/1Roq+vj+AsfyA7OzthNYUBYYTuF/BALMT/BcDlW3YFubm58Qi5vampqTE5iFIATq/Xy66gv7/fg8eQ3PJ5eXmbdTrduqGhofdut1vOHZwQbFQx6b2GhoZMhN8n+K/h4eHypKSkiMvGxcXRsN9XXV29KSYA9fX1GRD+RbQGeAFxMhIIKr6zs/M0yvjh55gDQPx2ifiQ+QBxKjk5eSXxZwTx1C4xBYB4evL9tsw+6B0ZGVkSwmAwkI6ODip+XpT/MjOAurq6HSucev+OBCAqxBCCeKtEPLUrqgPQxtHzeyIUH7L50dHRs4mJiYt1tLe3W0VhI7arqgMUFxfHSw+LEZpvcHDwZHd39+kw4qnd4NQOnfHxcf/ExMQAdurV3g0MZrP5ufDROdyHZ71WbQCHw+HHjnkRIE9lfrda7qs5p2UxgV0ulxeXo3OAeKJw1TotqyV0enqajsR5hSEMzACoTU1NBXD4oxCNClXJMQUQwolCXMDEfqRAdXrmAAIETyf22NjYQ/Ln5621MwJiCIvFcjEQCAysuREI7dC1tbVVuA/simYEuFiJb2lpuVZaWlqz5iYxvQE2NzcrIZ59CHEYcPT89bKyshql+oMZAGKdtLa23oT4u0r2CRMAk8mkb2truwPxt5SeTpzf71cdIC0tzVhSUpKP1++hARFcu8RzqbSwBzoN1mOCXVFVgGAwSHJycuKMRmM8+fcfSqQg0jQuzN90Qj3O3wIMAN8Np0JgnxtnAAAAAElFTkSuQmCC";
    private static final String TAG = "InlineVideoView";
    static final int TIME_TO_UPDATE_SEEK_JS = 500;
    private int duration;
    InlineParams inlineParams;
    MediaController mediaController;
    WeakReference<MMLayout> mmLayoutRef;
    TransparentFix transFix;
    TransparentHandler transparentHandler;
    Handler videoHandler;

    private static class DownloadRunnable implements Runnable {
        private WeakReference<InlineVideoView> inlineVideoRef;

        public DownloadRunnable(InlineVideoView videoView) {
            this.inlineVideoRef = new WeakReference(videoView);
        }

        public void run() {
            InlineVideoView inlineView = (InlineVideoView) this.inlineVideoRef.get();
            if (inlineView != null) {
                inlineView.downloadVideo();
            }
        }
    }

    static class InlineParams {
        boolean autoPlay;
        int bodyHeight;
        int bodyWidth;
        String cachedVideoID;
        String cachedVideoURI;
        int currentPosition;
        boolean goingFullScreen;
        int height;
        boolean isCompleted;
        boolean isInitialPlayBack;
        boolean isPlayingStreaming;
        boolean isStopped;
        int originalOrientation;
        float scaleFactor;
        boolean showControls;
        String streamVideoURI;
        String touchCallBack;
        int width;
        int x;
        int y;

        InlineParams(Map<String, String> parameters, Context context) {
            this.isInitialPlayBack = true;
            if (parameters.get("x") != null) {
                this.x = (int) Float.parseFloat((String) parameters.get("x"));
            }
            if (parameters.get("y") != null) {
                this.y = (int) Float.parseFloat((String) parameters.get("y"));
            }
            if (parameters.get(MMLayout.KEY_WIDTH) != null) {
                this.width = (int) Float.parseFloat((String) parameters.get(MMLayout.KEY_WIDTH));
            }
            if (parameters.get(MMLayout.KEY_HEIGHT) != null) {
                this.height = (int) Float.parseFloat((String) parameters.get(MMLayout.KEY_HEIGHT));
            }
            this.streamVideoURI = (String) parameters.get("streamVideoURI");
            this.cachedVideoURI = (String) parameters.get("cachedVideoURI");
            this.cachedVideoID = (String) parameters.get("cachedVideoID");
            if (parameters.get("autoPlay") != null) {
                this.autoPlay = Boolean.parseBoolean((String) parameters.get("autoPlay"));
            }
            if (parameters.get("showControls") != null) {
                this.showControls = Boolean.parseBoolean((String) parameters.get("showControls"));
            }
            if (parameters.get("bodyWidth") != null) {
                this.bodyWidth = (int) Float.parseFloat((String) parameters.get("bodyWidth"));
            }
            if (parameters.get("bodyHeight") != null) {
                this.bodyHeight = (int) Float.parseFloat((String) parameters.get("bodyHeight"));
            }
            this.touchCallBack = (String) parameters.get("touchCallback");
            this.scaleFactor = context.getResources().getDisplayMetrics().density;
        }

        static InlineParams getInlineParams(String gson) {
            return (InlineParams) new Gson().fromJson(gson, InlineParams.class);
        }

        void inflateFromGson(String gson) {
            InlineParams params = (InlineParams) new Gson().fromJson(gson, InlineParams.class);
            this.x = params.x;
            this.y = params.y;
            this.bodyWidth = params.bodyWidth;
            this.bodyHeight = params.bodyHeight;
            this.width = params.width;
            this.height = params.height;
            this.currentPosition = params.currentPosition;
            this.streamVideoURI = params.streamVideoURI;
            this.cachedVideoURI = params.cachedVideoURI;
            this.cachedVideoID = params.cachedVideoID;
            this.touchCallBack = params.touchCallBack;
            this.autoPlay = params.autoPlay;
            this.showControls = params.showControls;
            this.isInitialPlayBack = params.isInitialPlayBack;
            this.scaleFactor = params.scaleFactor;
            this.goingFullScreen = params.goingFullScreen;
            this.originalOrientation = params.originalOrientation;
            this.isCompleted = params.isCompleted;
            MMLog.d(TAG, "gson*****" + gson);
            MMLog.d(TAG, "PARAMS*****" + params);
        }

        public String toString() {
            return String.format("%s id, %d x, %d y, %d bWidth, %d bHeight, %d pos, %b autoPlay", new Object[]{this.cachedVideoID, Integer.valueOf(this.x), Integer.valueOf(this.y), Integer.valueOf(this.bodyWidth), Integer.valueOf(this.bodyHeight), Integer.valueOf(this.currentPosition), Boolean.valueOf(this.autoPlay)});
        }
    }

    private static class MediaController extends android.widget.MediaController {
        BitmapDrawable inDrawable;
        private WeakReference<InlineVideoView> inlineVideoRef;
        BitmapDrawable outDrawable;

        static final class MediaClickListener implements OnClickListener {
            private WeakReference<InlineVideoView> inlineVideoRef;

            public MediaClickListener(InlineVideoView inlineView) {
                this.inlineVideoRef = new WeakReference(inlineView);
            }

            public void onClick(View arg0) {
                InlineVideoView inlineView = (InlineVideoView) this.inlineVideoRef.get();
                if (inlineView != null) {
                    inlineView.onMediaControllerClick(arg0);
                }
            }
        }

        public MediaController(InlineVideoView videoView) {
            super(videoView.getContext());
            this.inlineVideoRef = new WeakReference(videoView);
        }

        public void setAnchorView(View view) {
            super.setAnchorView(view);
            Button fullScreenButton = new Button(getContext());
            if (this.inDrawable == null) {
                try {
                    this.inDrawable = new BitmapDrawable(new ByteArrayInputStream(Base64.decode(ANCHOR_IN_PNG)));
                } catch (Exception e) {
                    MMLog.e(TAG, "Exception setting image anchorView inDrawable:", e);
                }
            }
            if (this.outDrawable == null) {
                try {
                    this.outDrawable = new BitmapDrawable(new ByteArrayInputStream(Base64.decode(ANCHOR_OUT_PNG)));
                } catch (Exception e2) {
                    MMLog.e(TAG, "Exception setting image anchorView outDrawable:", e2);
                }
            }
            InlineVideoView inlineView = (InlineVideoView) this.inlineVideoRef.get();
            if (inlineView != null) {
                if (inlineView.inlineParams.goingFullScreen) {
                    fullScreenButton.setBackgroundDrawable(this.inDrawable);
                } else {
                    fullScreenButton.setBackgroundDrawable(this.outDrawable);
                }
                fullScreenButton.setOnClickListener(new MediaClickListener(inlineView));
            }
            LayoutParams params = new LayoutParams(-2, -2, 5);
            params.setMargins(0, ApiEventType.API_MRAID_GET_MAX_SIZE, ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, 0);
            addView(fullScreenButton, params);
        }
    }

    static interface TransparentFix {
        void removeBlackView();
    }

    private static class TransparentHandler extends Handler {
        private WeakReference<InlineVideoView> inlineVideoRef;

        public TransparentHandler(InlineVideoView videoView) {
            this.inlineVideoRef = new WeakReference(videoView);
        }

        public void handleMessage(Message msg) {
            InlineVideoView inlineView = (InlineVideoView) this.inlineVideoRef.get();
            if (inlineView != null) {
                inlineView.handleTransparentMessage(msg);
            }
        }
    }

    private static class VideoCompletionListener implements OnCompletionListener {
        private WeakReference<InlineVideoView> inlineVideoRef;

        public VideoCompletionListener(InlineVideoView videoView) {
            this.inlineVideoRef = new WeakReference(videoView);
        }

        public void onCompletion(MediaPlayer mp) {
            InlineVideoView inlineView = (InlineVideoView) this.inlineVideoRef.get();
            if (inlineView != null) {
                inlineView.onCompletion(mp);
            }
        }
    }

    private static class VideoErrorListener implements OnErrorListener {
        private WeakReference<InlineVideoView> inlineVideoRef;

        public VideoErrorListener(InlineVideoView videoView) {
            this.inlineVideoRef = new WeakReference(videoView);
        }

        public boolean onError(MediaPlayer mp, int what, int extra) {
            InlineVideoView inlineView = (InlineVideoView) this.inlineVideoRef.get();
            if (inlineView != null) {
                inlineView.onError(mp, what, extra);
            }
            return true;
        }
    }

    private static class VideoHandler extends Handler {
        private WeakReference<InlineVideoView> inlineVideoRef;

        public VideoHandler(InlineVideoView videoView) {
            this.inlineVideoRef = new WeakReference(videoView);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MMAdView.TRANSITION_UP:
                    InlineVideoView inlineView = (InlineVideoView) this.inlineVideoRef.get();
                    if (inlineView != null) {
                        if (inlineView.isPlaying()) {
                            inlineView.updateVideoSeekTime();
                        }
                        inlineView.videoHandler.sendMessageDelayed(Message.obtain(inlineView.videoHandler, MMAdView.TRANSITION_UP), 500);
                    }
                default:
                    break;
            }
        }
    }

    private static class VideoPreparedListener implements OnPreparedListener {
        private WeakReference<InlineVideoView> inlineVideoRef;

        public VideoPreparedListener(InlineVideoView videoView) {
            this.inlineVideoRef = new WeakReference(videoView);
        }

        public void onPrepared(MediaPlayer mp) {
            InlineVideoView inlineView = (InlineVideoView) this.inlineVideoRef.get();
            if (inlineView != null) {
                inlineView.onPrepared(mp);
            }
        }
    }

    private static class VideoTouchListener implements OnTouchListener {
        private WeakReference<InlineVideoView> inlineVideoRef;

        public VideoTouchListener(InlineVideoView videoView) {
            this.inlineVideoRef = new WeakReference(videoView);
        }

        public boolean onTouch(View v, MotionEvent event) {
            InlineVideoView inlineView = (InlineVideoView) this.inlineVideoRef.get();
            return inlineView != null ? inlineView.onTouch(v, event) : true;
        }
    }

    public InlineVideoView(MMLayout mmLayout) {
        super(mmLayout.getContext());
        this.transparentHandler = new TransparentHandler(this);
        setId(8832429);
        setFocusable(true);
        MMAdImplController.destroyOtherInlineVideo(mmLayout.getContext());
        this.mmLayoutRef = new WeakReference(mmLayout);
    }

    private OnCompletionListener createOnCompletionListener() {
        return new VideoCompletionListener(this);
    }

    private OnErrorListener createOnErrorListener() {
        return new VideoErrorListener(this);
    }

    private OnPreparedListener createOnPreparedListener() {
        return new VideoPreparedListener(this);
    }

    private OnTouchListener createOnTouchListener() {
        return new VideoTouchListener(this);
    }

    private Handler createVideoHandler() {
        return new VideoHandler(this);
    }

    private void downloadCacheVideo() {
        ThreadUtils.execute(new DownloadRunnable(this));
    }

    private Uri getVideoUri() {
        if (hasCachedVideo() && !this.inlineParams.isPlayingStreaming) {
            this.inlineParams.isPlayingStreaming = false;
            return VideoAd.getVideoUri(getContext(), this.inlineParams.cachedVideoID);
        } else if (TextUtils.isEmpty(this.inlineParams.streamVideoURI)) {
            return null;
        } else {
            this.inlineParams.isPlayingStreaming = true;
            return Uri.parse(this.inlineParams.streamVideoURI);
        }
    }

    private boolean hasCachedVideo() {
        return !TextUtils.isEmpty(this.inlineParams.cachedVideoID) && VideoAd.hasVideoFile(getContext(), this.inlineParams.cachedVideoID);
    }

    private void initInternalInlineVideo() {
        this.videoHandler = createVideoHandler();
        setVideoURI(getVideoUri());
        setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        setClickable(true);
        setOnErrorListener(createOnErrorListener());
        setOnCompletionListener(createOnCompletionListener());
        setOnPreparedListener(createOnPreparedListener());
        setOnTouchListener(createOnTouchListener());
        if (this.inlineParams.autoPlay) {
            seekTo(this.inlineParams.currentPosition);
            startInternal();
            if (!(this.videoHandler == null || this.videoHandler.hasMessages(MMAdView.TRANSITION_UP))) {
                this.videoHandler.sendMessageDelayed(Message.obtain(this.videoHandler, MMAdView.TRANSITION_UP), 500);
            }
        }
        if (this.inlineParams.showControls) {
            this.mediaController = new MediaController(this);
            setMediaController(this.mediaController);
            this.mediaController.show();
        }
        MMLog.e(TAG, "Finished inserting inlineVideo player");
    }

    private void makeTransparent() {
        if (!this.transparentHandler.hasMessages(MMAdView.TRANSITION_RANDOM)) {
            this.transparentHandler.sendEmptyMessage(MMAdView.TRANSITION_RANDOM);
        }
    }

    private void removeFromParent() {
        ViewGroup currentParent = (ViewGroup) getParent();
        if (currentParent != null) {
            currentParent.removeView(this);
        }
    }

    private void removeKeyboardFocusViewJira1642() {
        ((InputMethodManager) getContext().getSystemService("input_method")).hideSoftInputFromWindow(getWindowToken(), 0);
        requestFocus();
    }

    private void resumeInternal(boolean wasPlaying) {
        if (!this.inlineParams.isCompleted) {
            seekTo(this.inlineParams.currentPosition);
            if (wasPlaying || this.inlineParams.autoPlay) {
                startInternal();
                if (this.videoHandler != null && !this.videoHandler.hasMessages(MMAdView.TRANSITION_UP)) {
                    this.videoHandler.sendMessageDelayed(Message.obtain(this.videoHandler, MMAdView.TRANSITION_UP), 500);
                }
            }
        }
    }

    private void setInlineVideoParams(InlineParams parameters) {
        this.inlineParams = parameters;
    }

    synchronized boolean adjustVideo(InlineParams params) {
        setAdjustVideoParams(params);
        MMLog.e(TAG, "Called initInlineVideo inside reposition section code");
        boolean wasPlaying = isPlaying();
        stopPlayback();
        MMLayout mmLayout = (MMLayout) this.mmLayoutRef.get();
        if (mmLayout != null) {
            mmLayout.addInlineVideo();
        }
        resumeInternal(wasPlaying);
        return true;
    }

    void downloadVideo() {
        VideoAd.downloadVideoFile(getContext(), this.inlineParams.cachedVideoURI, this.inlineParams.cachedVideoID);
    }

    public RelativeLayout.LayoutParams getCustomLayoutParams() {
        if (this.inlineParams.goingFullScreen) {
            return new RelativeLayout.LayoutParams(-1, -1);
        }
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int) (this.inlineParams.scaleFactor * ((float) this.inlineParams.width)), (int) (this.inlineParams.scaleFactor * ((float) this.inlineParams.height)));
        lp.topMargin = (int) (this.inlineParams.scaleFactor * ((float) this.inlineParams.y));
        lp.leftMargin = (int) (this.inlineParams.scaleFactor * ((float) this.inlineParams.x));
        MMLog.d(TAG, "lp height = " + lp.height);
        return lp;
    }

    String getGsonState() {
        return new Gson().toJson(this.inlineParams);
    }

    void handleTransparentMessage(Message msg) {
        switch (msg.what) {
            case MMAdView.TRANSITION_RANDOM:
                if (!isPlaying() || getCurrentPosition() <= 0) {
                    this.transparentHandler.sendEmptyMessageDelayed(MMAdView.TRANSITION_RANDOM, 50);
                } else {
                    this.transparentHandler.sendEmptyMessageDelayed(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, 100);
                }
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                if (isPlaying() && getCurrentPosition() > 0) {
                    setBackgroundColor(0);
                    if (this.mmLayoutRef != null && this.mmLayoutRef.get() != null) {
                        ((MMLayout) this.mmLayoutRef.get()).removeBlackView();
                    }
                }
            default:
                break;
        }
    }

    synchronized void initInlineVideo(InlineParams parameters) {
        setInlineVideoParams(parameters);
        if (!TextUtils.isEmpty(this.inlineParams.cachedVideoURI)) {
            downloadCacheVideo();
        }
        if (isValid()) {
            initInternalInlineVideo();
        } else {
            MMLog.e(TAG, "The videoURI attribute was not specified on the video marker div.");
        }
    }

    boolean isPlayingStreaming() {
        return this.inlineParams != null && this.inlineParams.isPlayingStreaming;
    }

    boolean isValid() {
        return !TextUtils.isEmpty(this.inlineParams.streamVideoURI) || hasCachedVideo();
    }

    void onCompletion(MediaPlayer mp) {
        if (this.videoHandler != null && this.videoHandler.hasMessages(MMAdView.TRANSITION_UP)) {
            this.videoHandler.removeMessages(MMAdView.TRANSITION_UP);
        }
        this.inlineParams.isCompleted = true;
        this.inlineParams.currentPosition = this.duration;
        if (this.inlineParams.currentPosition == -1) {
            this.inlineParams.currentPosition = 0;
        }
        updateVideoSeekTimeFinal();
    }

    boolean onError(MediaPlayer mp, int what, int extra) {
        if (this.videoHandler != null && this.videoHandler.hasMessages(MMAdView.TRANSITION_UP)) {
            this.videoHandler.removeMessages(MMAdView.TRANSITION_UP);
        }
        MMLayout mmLayout = (MMLayout) this.mmLayoutRef.get();
        if (mmLayout == null) {
            MMLog.w(TAG, "MMLayout weak reference broken");
            return false;
        } else {
            mmLayout.loadUrl("javascript:MMJS.setError(" + String.format("Error while playing, %d - %d", new Object[]{Integer.valueOf(what), Integer.valueOf(extra)}) + ");");
            return true;
        }
    }

    void onMediaControllerClick(View arg0) {
        MMLayout mmLayout = (MMLayout) this.mmLayoutRef.get();
        if (mmLayout != null) {
            setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
            if (isPlaying()) {
                this.inlineParams.currentPosition = getCurrentPosition();
            }
            Activity activity;
            boolean wasPlaying;
            if (this.inlineParams.goingFullScreen) {
                this.inlineParams.goingFullScreen = false;
                if (this.inlineParams.originalOrientation == 1) {
                    activity = (Activity) getContext();
                    if (activity != null) {
                        activity.setRequestedOrientation(1);
                    }
                } else {
                    wasPlaying = isPlaying();
                    stopPlayback();
                    mmLayout.repositionVideoLayout();
                    resumeInternal(wasPlaying);
                }
            } else {
                this.inlineParams.originalOrientation = getContext().getResources().getConfiguration().orientation;
                this.inlineParams.goingFullScreen = true;
                if (this.inlineParams.originalOrientation != 2) {
                    activity = getContext();
                    if (activity != null) {
                        activity.setRequestedOrientation(0);
                    }
                } else {
                    wasPlaying = isPlaying();
                    stopPlayback();
                    mmLayout.fullScreenVideoLayout();
                    resumeInternal(wasPlaying);
                }
            }
        }
    }

    void onPrepared(MediaPlayer mp) {
        if (this.inlineParams.autoPlay) {
            makeTransparent();
        }
        seekTo(this.inlineParams.currentPosition);
        if (this.inlineParams.autoPlay || !this.inlineParams.isInitialPlayBack) {
            getHeight();
        } else {
            this.inlineParams.isInitialPlayBack = false;
        }
        this.duration = getDuration();
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState ss = (SavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            this.inlineParams.inflateFromGson(ss.gson);
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        if (isPlaying()) {
            this.inlineParams.currentPosition = getCurrentPosition();
        }
        ss.gson = getGsonState();
        return ss;
    }

    boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == 1) {
            MMLayout mmLayout = (MMLayout) this.mmLayoutRef.get();
            if (mmLayout == null) {
                MMLog.w(TAG, "MMLayout weak reference broken");
                return false;
            } else {
                if (!TextUtils.isEmpty(this.inlineParams.touchCallBack)) {
                    mmLayout.loadUrl(String.format("javascript:" + this.inlineParams.touchCallBack + "(%f,%f)", new Object[]{Float.valueOf(event.getX()), Float.valueOf(event.getY())}));
                }
                if (!(!this.inlineParams.showControls || this.mediaController == null || this.mediaController.isShowing())) {
                    this.mediaController.show();
                    return true;
                }
            }
        }
        return true;
    }

    void pauseVideo() {
        if (this.videoHandler != null && this.videoHandler.hasMessages(MMAdView.TRANSITION_UP)) {
            this.videoHandler.removeMessages(MMAdView.TRANSITION_UP);
        }
        if (isPlaying()) {
            this.inlineParams.currentPosition = getCurrentPosition();
            pause();
        }
    }

    void playVideo() {
        if (!isPlaying()) {
            if (this.inlineParams.isStopped && getVideoUri() != null) {
                this.inlineParams.isStopped = false;
                setVideoURI(getVideoUri());
                seekTo(0);
            } else if (this.inlineParams.isCompleted) {
                seekTo(0);
            }
            this.inlineParams.isCompleted = false;
            startInternal();
        }
        if (this.videoHandler != null && !this.videoHandler.hasMessages(MMAdView.TRANSITION_UP)) {
            this.videoHandler.sendMessageDelayed(Message.obtain(this.videoHandler, MMAdView.TRANSITION_UP), 500);
        }
    }

    void removeVideo() {
        if (this.videoHandler != null && this.videoHandler.hasMessages(MMAdView.TRANSITION_UP)) {
            this.videoHandler.removeMessages(MMAdView.TRANSITION_UP);
        }
        if (isPlaying()) {
            stopPlayback();
        }
        setOnCompletionListener(null);
        setOnErrorListener(null);
        setOnPreparedListener(null);
        setOnTouchListener(null);
        removeFromParent();
    }

    void resumeVideo() {
        if (!isPlaying() && !this.inlineParams.isCompleted) {
            startInternal();
            if (this.videoHandler != null && !this.videoHandler.hasMessages(MMAdView.TRANSITION_UP)) {
                this.videoHandler.sendMessageDelayed(Message.obtain(this.videoHandler, MMAdView.TRANSITION_UP), 500);
            }
        }
    }

    void setAdjustVideoParams(InlineParams parameters) {
        this.inlineParams.x = parameters.x;
        this.inlineParams.y = parameters.y;
        this.inlineParams.width = parameters.width;
        this.inlineParams.height = parameters.height;
    }

    void setVideoSource(String streamVideoURI) {
        if (isPlaying()) {
            stopPlayback();
        }
        this.inlineParams.currentPosition = 0;
        setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        setVideoURI(Uri.parse(streamVideoURI));
        startInternal();
    }

    public void start() {
        makeTransparent();
        removeKeyboardFocusViewJira1642();
        super.start();
    }

    public void startInternal() {
        if (((PowerManager) getContext().getSystemService("power")).isScreenOn()) {
            start();
        }
    }

    public void stopPlayback() {
        if (this.videoHandler != null && this.videoHandler.hasMessages(MMAdView.TRANSITION_UP)) {
            this.videoHandler.removeMessages(MMAdView.TRANSITION_UP);
        }
        if (isPlaying()) {
            this.inlineParams.currentPosition = 0;
        }
        super.stopPlayback();
    }

    void stopVideo() {
        if (this.videoHandler != null && this.videoHandler.hasMessages(MMAdView.TRANSITION_UP)) {
            this.videoHandler.removeMessages(MMAdView.TRANSITION_UP);
        }
        if (isPlaying()) {
            this.inlineParams.isStopped = true;
            this.inlineParams.currentPosition = 0;
            if (!(this.mmLayoutRef == null || this.mmLayoutRef.get() == null)) {
                ((MMLayout) this.mmLayoutRef.get()).addBlackView();
            }
            stopPlayback();
        }
    }

    public String toString() {
        return this.inlineParams.toString();
    }

    void updateVideoSeekTime() {
        int time = getCurrentPosition();
        if (time >= 0) {
            MMLog.d(TAG, "Time is " + time);
            updateVideoSeekTime(Math.floor((double) (((float) time) / 1000.0f)));
        }
    }

    void updateVideoSeekTime(double timeInSeconds) {
        MMLayout mmLayout = (MMLayout) this.mmLayoutRef.get();
        if (mmLayout == null) {
            MMLog.w(TAG, "MMLayout weak reference broken");
        }
        mmLayout.loadUrl("javascript:MMJS.inlineVideo.updateVideoSeekTime(" + timeInSeconds + ");");
    }

    void updateVideoSeekTimeFinal() {
        if (this.duration > 0) {
            MMLog.d(TAG, "Time is " + this.duration);
            updateVideoSeekTime(Math.ceil((double) (((float) this.duration) / 1000.0f)));
        }
    }
}