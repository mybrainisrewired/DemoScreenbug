package mobi.vserv.org.ormma.controller.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.VideoView;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.Formatter;
import java.util.Locale;
import mobi.vserv.android.ads.VservAdController;
import mobi.vserv.android.ads.VservConstants;
import mobi.vserv.org.ormma.controller.Defines;
import mobi.vserv.org.ormma.controller.OrmmaController.Dimensions;
import mobi.vserv.org.ormma.controller.OrmmaController.PlayerProperties;
import mobi.vserv.org.ormma.view.OrmmaView;

public class OrmmaPlayer extends VideoView implements OnCompletionListener, OnErrorListener, OnPreparedListener, VservConstants, MediaPlayerControl, OnBufferingUpdateListener {
    private static String transientText;
    private String Cacheposturl;
    private AudioManager aManager;
    private TextView advLabel;
    private String contentURL;
    private Context context;
    private MediaController ctrl;
    private String elapsedtime;
    private boolean isCacheAd;
    private boolean isPause;
    private boolean isPrepared;
    private boolean isReleased;
    private boolean isVPlay0;
    private boolean isVPlay100;
    private boolean isVPlay25;
    private boolean isVPlay50;
    private boolean isVPlay75;
    private boolean isbufferListenerSet;
    private boolean iscomplete;
    private OrmmaPlayerListener listener;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private int mVideoHeight;
    private int mVideoWidth;
    private MediaPlayer mediaPlayer;
    private ImageView muteButtonImage;
    private int mutedVolume;
    public Dimensions objdim;
    private OrmmaView ormmaView;
    float percentage;
    private PlayerProperties playProperties;
    private ProgressBar progressbar;
    private ImageView replayButtonImage;
    private String requestId;
    private int skipDelay;
    private int stopPosition;
    int t_curposition;
    private TextView timerLabel;
    private String totaltime;
    private RelativeLayout transientLayout;
    private ImageView unmuteButtonImage;

    static {
        transientText = "Loading. Please Wait..";
    }

    public OrmmaPlayer(Context context, int height) {
        super(context);
        this.isCacheAd = false;
        this.Cacheposturl = null;
        this.isVPlay0 = false;
        this.isVPlay25 = false;
        this.isVPlay50 = false;
        this.isVPlay75 = false;
        this.isVPlay100 = false;
        this.isbufferListenerSet = false;
        this.requestId = null;
        this.iscomplete = false;
        this.isPause = false;
        this.isPrepared = false;
        this.stopPosition = 0;
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        this.skipDelay = 0;
        this.replayButtonImage = null;
        this.advLabel = null;
        this.muteButtonImage = null;
        this.unmuteButtonImage = null;
        this.timerLabel = null;
        this.progressbar = null;
        this.totaltime = "00:00";
        this.elapsedtime = "00:00";
        this.mediaPlayer = null;
        this.t_curposition = 0;
        this.percentage = 0.0f;
        this.context = context;
        this.aManager = (AudioManager) this.context.getSystemService("audio");
        DisplayMetrics metrics = this.context.getResources().getDisplayMetrics();
        this.mVideoWidth = metrics.widthPixels;
        this.mVideoHeight = metrics.heightPixels;
    }

    public OrmmaPlayer(OrmmaView ormmaView, Context context, int height) {
        super(context);
        this.isCacheAd = false;
        this.Cacheposturl = null;
        this.isVPlay0 = false;
        this.isVPlay25 = false;
        this.isVPlay50 = false;
        this.isVPlay75 = false;
        this.isVPlay100 = false;
        this.isbufferListenerSet = false;
        this.requestId = null;
        this.iscomplete = false;
        this.isPause = false;
        this.isPrepared = false;
        this.stopPosition = 0;
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        this.skipDelay = 0;
        this.replayButtonImage = null;
        this.advLabel = null;
        this.muteButtonImage = null;
        this.unmuteButtonImage = null;
        this.timerLabel = null;
        this.progressbar = null;
        this.totaltime = "00:00";
        this.elapsedtime = "00:00";
        this.mediaPlayer = null;
        this.t_curposition = 0;
        this.percentage = 0.0f;
        this.context = context;
        this.ormmaView = ormmaView;
        this.aManager = (AudioManager) this.context.getSystemService("audio");
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
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

    void addReplayButton() {
        if (Defines.ENABLE_lOGGING) {
            Log.i("vserv", "Inside addReplayButton");
        }
        if (this.transientLayout == null) {
            this.transientLayout = new RelativeLayout(this.context);
            this.transientLayout.setLayoutParams(getLayoutParams());
            LinearLayout linearLayout = new LinearLayout(this.context);
            linearLayout.setGravity(1);
            LayoutParams msgparams = new LayoutParams(-2, -2);
            msgparams.addRule(ApiEventType.API_MRAID_CLOSE);
            TextView transientView = new TextView(this.context);
            transientView.setText(transientText);
            transientView.setTextColor(-1);
            LayoutParams transientViewLayoutParams = new LayoutParams(-2, -2);
            transientViewLayoutParams.addRule(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE);
            transientView.setLayoutParams(transientViewLayoutParams);
            this.replayButtonImage = new ImageView(this.context);
            this.replayButtonImage.setImageResource(getResources().getIdentifier("vserv_replay", "drawable", this.context.getPackageName()));
            LayoutParams replayLayoutParams = new LayoutParams(-2, -2);
            replayLayoutParams.addRule(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE);
            this.replayButtonImage.setLayoutParams(replayLayoutParams);
            linearLayout.addView(this.replayButtonImage);
            this.transientLayout.addView(linearLayout, msgparams);
            ((ViewGroup) getParent()).addView(this.transientLayout);
        }
    }

    void addTransientMessage() {
        if (Defines.ENABLE_lOGGING) {
            Log.i("vserv", "Inside addTransientMessage");
        }
        if (!this.playProperties.inline && this.transientLayout == null) {
            this.transientLayout = new RelativeLayout(this.context);
            this.transientLayout.setLayoutParams(getLayoutParams());
            LinearLayout linearLayout = new LinearLayout(this.context);
            linearLayout.setGravity(1);
            LayoutParams msgparams = new LayoutParams(-2, -2);
            msgparams.addRule(ApiEventType.API_MRAID_CLOSE);
            TextView transientView = new TextView(this.context);
            transientView.setText(transientText);
            transientView.setTextColor(-1);
            LayoutParams transientViewLayoutParams = new LayoutParams(-2, -2);
            transientViewLayoutParams.addRule(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE);
            transientView.setLayoutParams(transientViewLayoutParams);
            ProgressBar progressBar = new ProgressBar(this.context, null, 16842871);
            LayoutParams progressLayoutParams = new LayoutParams(-2, -2);
            progressLayoutParams.addRule(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE);
            progressBar.setLayoutParams(progressLayoutParams);
            linearLayout.addView(progressBar);
            this.transientLayout.addView(linearLayout, msgparams);
            ((ViewGroup) getParent()).addView(this.transientLayout);
        }
    }

    public void cacheAudio() {
        if (this.playProperties.doMute()) {
            this.mutedVolume = this.aManager.getStreamVolume(MMAdView.TRANSITION_DOWN);
            this.aManager.setStreamVolume(MMAdView.TRANSITION_DOWN, 0, MMAdView.TRANSITION_RANDOM);
        }
        loadCacheContent();
    }

    public void cacheVideo() {
        if (this.playProperties.doMute()) {
            this.mutedVolume = this.aManager.getStreamVolume(MMAdView.TRANSITION_DOWN);
            this.aManager.setStreamVolume(MMAdView.TRANSITION_DOWN, 0, MMAdView.TRANSITION_RANDOM);
        }
        loadCacheContent();
    }

    void clearReplayButton() {
        try {
            if (Defines.ENABLE_lOGGING) {
                Log.i("vserv", "Inside clearReplayButton");
            }
            if (this.transientLayout != null) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", "Inside clearReplayButton");
                }
                this.transientLayout.removeAllViews();
                ((ViewGroup) getParent()).removeView(this.transientLayout);
                this.transientLayout = null;
            }
        } catch (Exception e) {
            Exception e2 = e;
            e2.printStackTrace();
            Log.i("vserv", new StringBuilder("Inside clearTransientMessage Exception: ").append(e2).toString());
        }
    }

    void clearTransientMessage() {
        try {
            if (Defines.ENABLE_lOGGING) {
                Log.i("vserv", "Inside clearTransientMessage");
            }
            if (this.transientLayout != null) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", "Inside clearTransientMessage2");
                }
                this.transientLayout.removeAllViews();
                ((ViewGroup) getParent()).removeView(this.transientLayout);
                this.transientLayout = null;
            }
        } catch (Exception e) {
            Exception e2 = e;
            e2.printStackTrace();
            Log.i("vserv", new StringBuilder("Inside clearTransientMessage Exception: ").append(e2).toString());
        }
    }

    void displayControl() {
        try {
            if (this.requestId == null || !this.requestId.equals("ormma")) {
                if (this.playProperties.isFullScreen()) {
                    this.unmuteButtonImage = new ImageView(this.context);
                    this.unmuteButtonImage.setImageResource(getResources().getIdentifier("vserv_unmute", "drawable", this.context.getPackageName()));
                    this.muteButtonImage = new ImageView(this.context);
                    this.muteButtonImage.setImageResource(getResources().getIdentifier("vserv_mute", "drawable", this.context.getPackageName()));
                    if (this.playProperties.doMute()) {
                        this.muteButtonImage.setVisibility(0);
                        this.unmuteButtonImage.setVisibility(MMAdView.TRANSITION_RANDOM);
                    } else {
                        this.muteButtonImage.setVisibility(MMAdView.TRANSITION_RANDOM);
                        this.unmuteButtonImage.setVisibility(0);
                    }
                    RelativeLayout unmuteLayout = new RelativeLayout(this.context);
                    ViewGroup.LayoutParams unmuteLayoutParams = new LayoutParams(-2, -2);
                    unmuteLayoutParams.addRule(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES);
                    unmuteLayout.setLayoutParams(unmuteLayoutParams);
                    unmuteLayout.addView(this.unmuteButtonImage);
                    unmuteLayout.addView(this.muteButtonImage);
                    this.advLabel = new TextView(this.context);
                    this.advLabel.setText("Advertisement");
                    RelativeLayout advLabelLayout = new RelativeLayout(this.context);
                    LinearLayout.LayoutParams advLabelLayoutParams = new LinearLayout.LayoutParams(-2, -2);
                    advLabelLayoutParams.leftMargin = 8;
                    advLabelLayoutParams.rightMargin = 8;
                    advLabelLayout.setLayoutParams(advLabelLayoutParams);
                    advLabelLayout.addView(this.advLabel);
                    this.timerLabel = new TextView(this.context);
                    this.timerLabel.setText(new StringBuilder("(").append(this.elapsedtime).append("/").append(this.totaltime).append(")").toString());
                    RelativeLayout timerLabelLayout = new RelativeLayout(this.context);
                    LinearLayout.LayoutParams timerLabelLayoutParams = new LinearLayout.LayoutParams(-2, -2);
                    timerLabelLayoutParams.leftMargin = 8;
                    timerLabelLayout.setLayoutParams(timerLabelLayoutParams);
                    timerLabelLayout.addView(this.timerLabel);
                    this.progressbar = new ProgressBar(this.context, null, 16842872);
                    this.progressbar.setProgressDrawable(getResources().getDrawable(getResources().getIdentifier("vserv_green_progress", "drawable", this.context.getPackageName())));
                    RelativeLayout progressbarLayout = new RelativeLayout(this.context);
                    LayoutParams progressbarLayoutParams = new LayoutParams(-1, 8);
                    progressbarLayoutParams.addRule(5);
                    this.progressbar.setLayoutParams(progressbarLayoutParams);
                    progressbarLayout.setLayoutParams(progressbarLayoutParams);
                    progressbarLayout.addView(this.progressbar);
                    LinearLayout bottomLayout = new LinearLayout(this.context);
                    LinearLayout.LayoutParams bottomLayoutParams = new LinearLayout.LayoutParams(-2, -2, 0.5f);
                    bottomLayout.setGravity(17);
                    bottomLayout.setLayoutParams(bottomLayoutParams);
                    bottomLayout.addView(unmuteLayout, unmuteLayoutParams);
                    bottomLayout.addView(advLabelLayout);
                    bottomLayout.addView(progressbarLayout);
                    LinearLayout bottom2Layout = new LinearLayout(this.context);
                    LayoutParams bottom2LayoutParams = new LayoutParams(-2, -2);
                    bottom2LayoutParams.addRule(12);
                    bottomLayout.setWeightSum(1.0f);
                    bottom2Layout.setLayoutParams(bottom2LayoutParams);
                    bottom2Layout.addView(bottomLayout);
                    bottom2Layout.addView(timerLabelLayout);
                    getParent().addView(bottom2Layout);
                }
            } else if (this.playProperties.showControl()) {
                this.ctrl = new MediaController(this.context);
                bottomLayout = new RelativeLayout(this.context);
                bottomLayoutParams = new LayoutParams(-1, -2);
                bottomLayout.setBackgroundColor(-1);
                bottomLayoutParams.addRule(12);
                bottomLayout.setLayoutParams(bottomLayoutParams);
                ViewGroup parent = (ViewGroup) getParent();
                parent.addView(bottomLayout);
                this.ctrl.setAnchorView(parent);
                setMediaController(this.ctrl);
            } else {
                bottomLayout = new RelativeLayout(this.context);
                bottomLayoutParams = new LayoutParams(-1, -2);
                bottomLayout.setBackgroundColor(-1);
                bottomLayoutParams.addRule(12);
                bottomLayout.setLayoutParams(bottomLayoutParams);
                getParent().addView(bottomLayout);
            }
        } catch (Exception e) {
            Exception e2 = e;
            Log.i("vserv", "Inside displayControl Exception:: ");
            e2.printStackTrace();
        }
    }

    public PlayerProperties getPlayerProperties() {
        return this.playProperties != null ? this.playProperties : null;
    }

    void loadCacheContent() {
        this.contentURL = this.contentURL.trim();
        this.contentURL = OrmmaUtils.convert(this.contentURL);
        if (this.contentURL != null || this.listener == null) {
            setVideoURI(Uri.parse(this.contentURL));
            setOnCompletionListener(this);
            setOnErrorListener(this);
            setOnPreparedListener(this);
            start();
        } else {
            removeView();
            this.listener.onError();
        }
    }

    void loadContent() {
        this.contentURL = this.contentURL.trim();
        this.contentURL = OrmmaUtils.convert(this.contentURL);
        if (this.contentURL != null || this.listener == null) {
            setVideoURI(Uri.parse(this.contentURL));
            displayControl();
            startContent();
        } else {
            removeView();
            this.listener.onError();
        }
    }

    void mute() {
        this.mutedVolume = this.aManager.getStreamVolume(MMAdView.TRANSITION_DOWN);
        this.aManager.setStreamVolume(MMAdView.TRANSITION_DOWN, 0, MMAdView.TRANSITION_RANDOM);
    }

    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        try {
            if (this.requestId == null || this.requestId.equals("ormma")) {
                this.t_curposition = mp.getCurrentPosition();
                this.percentage = (float) ((this.t_curposition * 100) / mp.getDuration());
                if (this.percentage >= 1.0f && !this.isbufferListenerSet) {
                    this.isbufferListenerSet = true;
                    if (this.listener != null) {
                        this.listener.onBufferingStarted();
                    }
                }
            } else {
                this.elapsedtime = stringForTime(mp.getCurrentPosition());
                if (this.unmuteButtonImage != null) {
                    this.unmuteButtonImage.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            Log.i("vserv", "unMute clicked ");
                            OrmmaPlayer.this.muteButtonImage.setVisibility(0);
                            OrmmaPlayer.this.unmuteButtonImage.setVisibility(MMAdView.TRANSITION_RANDOM);
                            OrmmaPlayer.this.mute();
                        }
                    });
                }
                if (this.muteButtonImage != null) {
                    this.muteButtonImage.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            OrmmaPlayer.this.muteButtonImage.setVisibility(MMAdView.TRANSITION_RANDOM);
                            OrmmaPlayer.this.unmuteButtonImage.setVisibility(0);
                            OrmmaPlayer.this.unMute();
                        }
                    });
                }
                if (this.t_curposition == 0 || this.t_curposition != mp.getCurrentPosition()) {
                    this.t_curposition = mp.getCurrentPosition();
                    this.percentage = (float) ((this.t_curposition * 100) / mp.getDuration());
                    if (this.timerLabel != null) {
                        this.timerLabel.setText(new StringBuilder("(").append(this.elapsedtime).append("/").append(this.totaltime).append(")").toString());
                    }
                    if (this.progressbar != null) {
                        this.progressbar.setProgress((int) this.percentage);
                    }
                    boolean z = Defines.ENABLE_lOGGING;
                    if (this.percentage >= 1.0f && !this.isbufferListenerSet) {
                        this.isbufferListenerSet = true;
                        if (this.listener != null) {
                            this.listener.onBufferingStarted();
                        }
                    }
                    if (((double) this.percentage) >= 0.0d && ((double) this.percentage) <= 10.0d && !this.isVPlay0) {
                        this.isVPlay0 = true;
                        new Thread() {
                            public void run() {
                                new VservAdController(OrmmaPlayer.this.context).SendQuartileTracking(OrmmaPlayer.this.requestId, VservConstants.VPLAY0);
                            }
                        }.start();
                    } else if (((double) this.percentage) > 20.0d && ((double) this.percentage) <= 30.0d && !this.isVPlay25) {
                        this.isVPlay25 = true;
                        new Thread() {
                            public void run() {
                                new VservAdController(OrmmaPlayer.this.context).SendQuartileTracking(OrmmaPlayer.this.requestId, VservConstants.VPLAY25);
                            }
                        }.start();
                    } else if (((double) this.percentage) > 45.0d && ((double) this.percentage) <= 55.0d && !this.isVPlay50) {
                        this.isVPlay50 = true;
                        new Thread() {
                            public void run() {
                                new VservAdController(OrmmaPlayer.this.context).SendQuartileTracking(OrmmaPlayer.this.requestId, VservConstants.VPLAY50);
                            }
                        }.start();
                    } else if (((double) this.percentage) > 70.0d && ((double) this.percentage) <= 80.0d && !this.isVPlay75) {
                        this.isVPlay75 = true;
                        new Thread() {
                            public void run() {
                                new VservAdController(OrmmaPlayer.this.context).SendQuartileTracking(OrmmaPlayer.this.requestId, VservConstants.VPLAY75);
                            }
                        }.start();
                    } else if (((double) this.percentage) > 90.0d && !this.isVPlay100) {
                        this.isVPlay100 = true;
                        new Thread() {
                            public void run() {
                                new VservAdController(OrmmaPlayer.this.context).SendQuartileTracking(OrmmaPlayer.this.requestId, VservConstants.VPLAY100);
                            }
                        }.start();
                    }
                }
            }
        } catch (Exception e) {
            Exception e2 = e;
            e2.printStackTrace();
            Log.i("vserv", new StringBuilder("Exception e: ").append(e2).toString());
        }
    }

    public void onCompletion(MediaPlayer mp) {
        this.iscomplete = true;
        this.t_curposition = 0;
        this.percentage = 0.0f;
        this.isVPlay0 = false;
        this.isVPlay25 = false;
        this.isVPlay50 = false;
        this.isVPlay75 = false;
        this.isVPlay100 = false;
        if (this.listener != null) {
            this.listener.onComplete();
        }
        if (Defines.ENABLE_lOGGING) {
            Log.i("vserv", new StringBuilder("playProperties.exitOnComplete() ").append(this.playProperties.exitOnComplete()).toString());
        }
        if (Defines.ENABLE_lOGGING) {
            Log.i("video", new StringBuilder("playProperties.inline ").append(this.playProperties.inline).toString());
        }
        seekTo(0);
        if (this.playProperties.doLoop()) {
            start();
        } else if (this.playProperties.exitOnComplete() || this.playProperties.inline) {
            this.playProperties.exitOnComplete();
            mp.setOnBufferingUpdateListener(null);
            releasePlayer();
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        try {
            this.isPause = true;
            clearTransientMessage();
            removeView();
            if (this.listener != null) {
                this.listener.onError();
            }
        } catch (Exception e) {
        }
        return false;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!this.playProperties.isFullScreen()) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else if (getResources().getConfiguration().orientation == 2) {
            int width = getDefaultSize(this.mVideoWidth, widthMeasureSpec);
            int height = getDefaultSize(this.mVideoHeight, heightMeasureSpec);
            if (this.mVideoWidth > 0 && this.mVideoHeight > 0) {
                if (this.mVideoWidth * height > this.mVideoHeight * width) {
                    height = (this.mVideoHeight * width) / this.mVideoWidth;
                } else if (this.mVideoWidth * height < this.mVideoHeight * width) {
                    width = (this.mVideoWidth * height) / this.mVideoHeight;
                }
            }
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void onPrepared(MediaPlayer mp) {
        if (Defines.ENABLE_lOGGING) {
            Log.i("vserv", new StringBuilder("Inside onPrepared :: ").append(getBufferPercentage()).toString());
        }
        this.isPrepared = true;
        this.mediaPlayer = mp;
        if (!this.isPause) {
            mp.setOnBufferingUpdateListener(this);
            clearTransientMessage();
            this.mFormatBuilder = new StringBuilder();
            this.mFormatter = new Formatter(this.mFormatBuilder, Locale.getDefault());
            this.totaltime = stringForTime(mp.getDuration());
            if (this.listener != null) {
                this.listener.onPrepared();
            }
        }
    }

    public void pause() {
        try {
            this.isPause = true;
            this.isPrepared = false;
            super.pause();
            if (!this.iscomplete) {
                this.stopPosition = getCurrentPosition();
                if (!isPlaying()) {
                    clearTransientMessage();
                    addReplayButton();
                }
            }
        } catch (Exception e) {
            Exception e2 = e;
            e2.printStackTrace();
            Log.i("vserv", new StringBuilder("player Exception:: ").append(e2).toString());
        }
    }

    public void playAudio() {
        loadContent();
    }

    public void playCacheAudio() {
        this.isCacheAd = true;
        loadContent();
    }

    public void playCacheVideo() {
        this.isCacheAd = true;
        if (this.playProperties.doMute()) {
            this.mutedVolume = this.aManager.getStreamVolume(MMAdView.TRANSITION_DOWN);
            this.aManager.setStreamVolume(MMAdView.TRANSITION_DOWN, 0, MMAdView.TRANSITION_RANDOM);
        }
        setVideoURI(Uri.parse(this.contentURL));
        displayControl();
        setOnCompletionListener(this);
        setOnErrorListener(this);
        setOnPreparedListener(this);
        if (!this.playProperties.inline) {
            addTransientMessage();
        }
        if (this.playProperties.isAutoPlay()) {
            start();
        }
    }

    public void playVideo() {
        if (Defines.ENABLE_lOGGING) {
            Log.i("Video", "*******Inside playVideo");
        }
        if (this.playProperties.doMute()) {
            this.mutedVolume = this.aManager.getStreamVolume(MMAdView.TRANSITION_DOWN);
            this.aManager.setStreamVolume(MMAdView.TRANSITION_DOWN, 0, MMAdView.TRANSITION_RANDOM);
        }
        loadContent();
    }

    public void releasePlayer() {
        if (Defines.ENABLE_lOGGING) {
            Log.i("vserv", "Inside releasePlayer");
        }
        if (!this.isReleased) {
            if (this.ormmaView != null) {
                this.ormmaView.videoPlayer = null;
            }
            this.isbufferListenerSet = false;
            this.isReleased = true;
            stopPlayback();
            removeView();
            if (this.playProperties != null && this.playProperties.doMute()) {
                unMute();
            }
            if (this.listener != null) {
                this.listener.onComplete();
            }
        }
    }

    void removeView() {
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            parent.removeView(this);
        }
    }

    public void resume() {
        try {
            if (this.replayButtonImage != null) {
                this.replayButtonImage.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (Defines.ENABLE_lOGGING) {
                            Log.i("vserv", "Clicked on replay button");
                        }
                        OrmmaPlayer.this.resumeplayer(OrmmaPlayer.this.isPrepared);
                    }
                });
            }
            if (this.requestId != null && this.requestId.equals("ormma") && this.replayButtonImage != null && this.replayButtonImage.isShown()) {
                clearReplayButton();
            }
        } catch (Exception e) {
            Exception e2 = e;
            e2.printStackTrace();
            Log.i("vserv", new StringBuilder("resume exception:: ").append(e2).toString());
        }
    }

    public void resumeplayer(boolean t_isPrepared) {
        if (this.isPause) {
            this.isPause = false;
            if (t_isPrepared) {
                if (this.mediaPlayer != null) {
                    this.mediaPlayer.setOnBufferingUpdateListener(this);
                }
                if (this.mFormatter == null && this.mFormatter == null) {
                    this.mFormatBuilder = new StringBuilder();
                    this.mFormatter = new Formatter(this.mFormatBuilder, Locale.getDefault());
                    this.totaltime = stringForTime(this.mediaPlayer.getDuration());
                }
                clearReplayButton();
                seekTo(this.stopPosition);
                start();
            } else {
                clearReplayButton();
                addTransientMessage();
                if (isPlaying()) {
                    clearTransientMessage();
                }
                seekTo(this.stopPosition);
                start();
            }
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setDimensions(Dimensions t_dimen) {
        this.objdim = t_dimen;
    }

    public void setListener(OrmmaPlayerListener listener) {
        this.listener = listener;
    }

    public void setPlayData(PlayerProperties properties, String url) {
        this.isReleased = false;
        this.playProperties = properties;
        this.contentURL = url;
        Log.i("@@@", new StringBuilder("setPlayData: ").append(this.mVideoWidth).toString());
    }

    public void setPostURL(String t_url) {
        this.Cacheposturl = t_url;
    }

    public void setRequestId(String t_requestId) {
        this.requestId = t_requestId;
    }

    public void start() {
        if (Defines.ENABLE_lOGGING) {
            Log.i("vserv", "*******start video:: ");
        }
        super.start();
        if (isPlaying()) {
            clearTransientMessage();
        }
    }

    void startContent() {
        setOnCompletionListener(this);
        setOnErrorListener(this);
        setOnPreparedListener(this);
        if (!this.playProperties.inline) {
            addTransientMessage();
        }
        if (this.playProperties.isAutoPlay()) {
            start();
        }
    }

    public void stopPlayback() {
        this.iscomplete = true;
        super.stopPlayback();
    }

    void unMute() {
        this.aManager.setStreamVolume(MMAdView.TRANSITION_DOWN, this.mutedVolume, MMAdView.TRANSITION_RANDOM);
    }
}