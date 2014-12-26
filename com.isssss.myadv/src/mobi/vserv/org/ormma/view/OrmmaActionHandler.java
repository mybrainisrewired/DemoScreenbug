package mobi.vserv.org.ormma.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.media.TransportMediator;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.LocationRequest;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import java.util.HashMap;
import mobi.vserv.android.ads.VservAdCacheVideo;
import mobi.vserv.android.ads.VservConstants;
import mobi.vserv.android.ads.VservCountDownTimer;
import mobi.vserv.org.ormma.controller.Defines;
import mobi.vserv.org.ormma.controller.OrmmaController.Dimensions;
import mobi.vserv.org.ormma.controller.OrmmaController.PlayerProperties;
import mobi.vserv.org.ormma.controller.util.OrmmaPlayer;
import mobi.vserv.org.ormma.controller.util.OrmmaPlayerListener;
import mobi.vserv.org.ormma.controller.util.OrmmaUtils;
import mobi.vserv.org.ormma.view.OrmmaView.ACTION;

public class OrmmaActionHandler extends Activity implements VservConstants {
    private static /* synthetic */ int[] $SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ACTION = null;
    private static final int bottomLayoutId = 234;
    private static final int topLayoutId = 123;
    private HashMap<ACTION, Object> actionData;
    private LinearLayout bottomLayout;
    private String cachePostUrl;
    private ImageView closeButtonImage;
    private CountDownTimer countDownTimer;
    private MediaController ctrl;
    private boolean isCacheAd;
    private boolean isError;
    private boolean iscomplete;
    private RelativeLayout layout;
    private OrmmaPlayer player;
    private String requestId;
    private int skipDelay;
    private TextView skipLabel;
    public int t_skipDelay;
    private RelativeLayout topLayout;

    class AnonymousClass_2 implements OrmmaPlayerListener {
        private final /* synthetic */ OrmmaPlayer val$player;

        AnonymousClass_2(OrmmaPlayer ormmaPlayer) {
            this.val$player = ormmaPlayer;
        }

        public void onBufferingStarted() {
            if (OrmmaActionHandler.this.skipDelay > 0) {
                OrmmaActionHandler.this.t_skipDelay = OrmmaActionHandler.this.skipDelay;
                OrmmaActionHandler.this.skipLabel.setText(new StringBuilder("Skip in ").append(OrmmaActionHandler.this.t_skipDelay).toString());
                OrmmaActionHandler.this.skipLabel.setVisibility(0);
                new Thread() {
                    public void run() {
                        AnonymousClass_2.this.this$0.runOnUiThread(new Runnable() {

                            class AnonymousClass_1 extends VservCountDownTimer {
                                AnonymousClass_1(long $anonymous0, long $anonymous1) {
                                    super($anonymous0, $anonymous1);
                                }

                                public void onFinish() {
                                    if (Defines.ENABLE_lOGGING) {
                                        Log.i("vserv", "onFinish ");
                                    }
                                    AnonymousClass_1.this.this$2.this$1.this$0.skipLabel.setVisibility(MMAdView.TRANSITION_RANDOM);
                                    AnonymousClass_1.this.this$2.this$1.this$0.closeButtonImage.setVisibility(0);
                                    AnonymousClass_1.this.this$2.this$1.this$0.topLayout.addView(AnonymousClass_1.this.this$2.this$1.this$0.closeButtonImage);
                                    AnonymousClass_1.this.this$2.this$1.this$0.skipDelay = 0;
                                    AnonymousClass_1.this.this$2.this$1.this$0.t_skipDelay = 0;
                                    AnonymousClass_1.this.this$2.this$1.this$0.skipLabel.setText(Preconditions.EMPTY_ARGUMENTS);
                                }

                                public void onTick(long millisUntilFinished) {
                                    if (Defines.ENABLE_lOGGING) {
                                        Log.i("vserv", new StringBuilder("onTick ").append(millisUntilFinished / 1000).toString());
                                    }
                                    TextView access$7 = AnonymousClass_1.this.this$2.this$1.this$0.skipLabel;
                                    StringBuilder stringBuilder = new StringBuilder("Skip in ");
                                    OrmmaActionHandler access$0 = AnonymousClass_1.this.this$2.this$1.this$0;
                                    int i = access$0.t_skipDelay;
                                    access$0.t_skipDelay = i - 1;
                                    access$7.setText(stringBuilder.append(i).toString());
                                }
                            }

                            public void run() {
                                VservCountDownTimer actimer = new AnonymousClass_1((long) (AnonymousClass_1.this.this$1.this$0.skipDelay * 1000), 1000).start();
                            }
                        });
                    }
                }.start();
            }
        }

        public void onComplete() {
            try {
                OrmmaActionHandler.this.iscomplete = true;
                if (!(OrmmaActionHandler.this.closeButtonImage == null || OrmmaActionHandler.this.closeButtonImage.isShown())) {
                    OrmmaActionHandler.this.closeButtonImage.setVisibility(0);
                }
                PlayerProperties t_properties = this.val$player.getPlayerProperties();
                OrmmaActionHandler.this.setResult(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, OrmmaActionHandler.this.getIntent());
                if (t_properties.exitOnComplete() || t_properties.inline) {
                    OrmmaActionHandler.this.closeCurrentActivity(true);
                } else {
                    OrmmaActionHandler.this.closeCurrentActivity(false);
                }
            } catch (Exception e) {
                Exception e2 = e;
                e2.printStackTrace();
                Log.i("vserv", new StringBuilder("onComplete ormmahandler::  ").append(e2).toString());
            }
        }

        public void onError() {
            OrmmaActionHandler.this.isError = true;
            OrmmaActionHandler.this.finish();
        }

        public void onPrepared() {
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ACTION() {
        int[] iArr = $SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ACTION;
        if (iArr == null) {
            iArr = new int[ACTION.values().length];
            try {
                iArr[ACTION.PLAY_AUDIO.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ACTION.PLAY_VIDEO.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ACTION = iArr;
        }
        return iArr;
    }

    public OrmmaActionHandler() {
        this.actionData = new HashMap();
        this.isCacheAd = false;
        this.cachePostUrl = null;
        this.skipDelay = 0;
        this.countDownTimer = null;
        this.requestId = null;
        this.iscomplete = false;
        this.isError = false;
        this.t_skipDelay = 0;
    }

    private void closeCurrentActivity(boolean shouldClose) {
        if (Defines.ENABLE_lOGGING) {
            Log.i("testing", "Finishing video player activity");
        }
        if (!(getIntent() != null && getIntent().hasExtra("start_from_ad_activity") && getIntent().getBooleanExtra("start_from_ad_activity", false))) {
            Intent intent = new Intent("mobi.vserv.ad.dismiss_screen");
            if (!shouldClose) {
                intent.putExtra(Event.INTENT_EXTERNAL_BROWSER, "close browser only");
            }
            sendBroadcast(intent);
        }
        finish();
    }

    private void doAction(Bundle data) {
        try {
            this.isCacheAd = data.getBoolean("isCacheAd");
            this.cachePostUrl = data.getString("posturl");
            String actionData = data.getString(OrmmaView.ACTION_KEY);
            if (actionData != null) {
                ACTION actionType = ACTION.valueOf(actionData);
                VservAdCacheVideo vservAdCacheVideo;
                switch ($SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ACTION()[actionType.ordinal()]) {
                    case MMAdView.TRANSITION_FADE:
                        if (this.isCacheAd) {
                            vservAdCacheVideo = new VservAdCacheVideo();
                            this.player = vservAdCacheVideo.getCachePlayer();
                            vservAdCacheVideo.removeCachePlayer();
                            this.player = initPlayer(data, actionType);
                            if (this.player != null) {
                                this.player.playCacheAudio();
                            }
                        } else {
                            this.player = initPlayer(data, actionType);
                            this.player.playAudio();
                        }
                    case MMAdView.TRANSITION_UP:
                        if (this.isCacheAd) {
                            vservAdCacheVideo = new VservAdCacheVideo();
                            this.player = vservAdCacheVideo.getCachePlayer();
                            vservAdCacheVideo.removeCachePlayer();
                            this.player = initPlayer(data, actionType);
                            if (this.player != null) {
                                this.player.playCacheVideo();
                            }
                        } else {
                            this.player = initPlayer(data, actionType);
                            this.player.playVideo();
                        }
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPlayerListener(OrmmaPlayer player) {
        player.setListener(new AnonymousClass_2(player));
    }

    public int getResourceIdentifier(String resourceName) {
        try {
            return getResources().getIdentifier(resourceName, "drawable", getPackageName());
        } catch (Exception e) {
            return -1;
        }
    }

    OrmmaPlayer initPlayer(Bundle playData, ACTION actionType) {
        OrmmaPlayer player;
        LayoutParams lp;
        PlayerProperties properties = (PlayerProperties) playData.getParcelable(OrmmaView.PLAYER_PROPERTIES);
        Dimensions playDimensions = (Dimensions) playData.getParcelable(OrmmaView.DIMENSIONS);
        if (this.isCacheAd) {
            player = this.player;
            if (player != null) {
                player.setContext(this);
                player.setPlayData(properties, OrmmaUtils.getData(OrmmaView.EXPAND_URL, playData));
                if (this.requestId != null) {
                    player.setRequestId(this.requestId);
                }
                player.setPostURL(this.cachePostUrl);
            }
        } else {
            player = new OrmmaPlayer(this, playDimensions.height);
            if (this.requestId != null) {
                player.setRequestId(this.requestId);
            }
            player.setPlayData(properties, OrmmaUtils.getData(OrmmaView.EXPAND_URL, playData));
        }
        if (playDimensions == null) {
            lp = new LayoutParams(-1, -1);
            lp.addRule(ApiEventType.API_MRAID_CLOSE);
        } else {
            lp = new LayoutParams(-1, -1);
            lp.addRule(ApiEventType.API_MRAID_CLOSE);
        }
        if (player != null) {
            player.setLayoutParams(lp);
        }
        this.layout.addView(player);
        this.actionData.put(actionType, player);
        setPlayerListener(player);
        return player;
    }

    @SuppressLint({"InlinedApi"})
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().addFlags(TransportMediator.FLAG_KEY_MEDIA_NEXT);
        Bundle data = getIntent().getExtras();
        if (!data.containsKey("adOrientation") || data.getString("adOrientation") == null) {
            SharedPreferences orientationPreference;
            if (VERSION.SDK_INT >= 11) {
                orientationPreference = getSharedPreferences("vserv_orientation", MMAdView.TRANSITION_RANDOM);
            } else {
                orientationPreference = getSharedPreferences("vserv_orientation", 0);
            }
            if (orientationPreference.getString("orientation", Preconditions.EMPTY_ARGUMENTS).equals("landscape")) {
                setRequestedOrientation(0);
                if (Defines.ENABLE_lOGGING) {
                    Log.i("orientation", "Setting requested orientation to landscape");
                }
            } else if (orientationPreference.getString("orientation", Preconditions.EMPTY_ARGUMENTS).equals("portrait")) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("orientation", "Setting requested orientation to portrait");
                }
                setRequestedOrientation(1);
            } else if (Defines.ENABLE_lOGGING) {
                Log.i("orientation", "Not Setting requested orientation");
            }
        } else {
            String t_orientation = data.getString("adOrientation");
            if (t_orientation.equals("landscape")) {
                setRequestedOrientation(0);
                if (Defines.ENABLE_lOGGING) {
                    Log.i("orientation", "Setting requested orientation to landscape");
                }
            } else if (t_orientation.equals("portrait")) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("orientation", "Setting requested orientation to portrait");
                }
                setRequestedOrientation(1);
            } else if (Defines.ENABLE_lOGGING) {
                Log.i("orientation", "Not Setting requested orientation");
            }
        }
        if (data.containsKey("requestid")) {
            this.requestId = data.getString("requestid");
            if (Defines.ENABLE_lOGGING) {
                Log.i("vserv", new StringBuilder("ormmaaction handler requestId: ").append(this.requestId).toString());
            }
        }
        if (data.containsKey("skipDelay")) {
            this.skipLabel = new TextView(this);
            this.skipLabel.setVisibility(MMAdView.TRANSITION_RANDOM);
            this.skipLabel.setTextColor(Color.parseColor("#ffffff"));
            this.skipLabel.setBackgroundResource(getResources().getIdentifier("vserv_skipdelay", "drawable", getPackageName()));
            this.skipLabel.setText(this.skipDelay);
            LayoutParams skipLabelLayoutParams = new LayoutParams(-2, -2);
            skipLabelLayoutParams.addRule(ApiEventType.API_MRAID_EXPAND);
            this.skipLabel.setLayoutParams(skipLabelLayoutParams);
            this.skipDelay = data.getInt("skipDelay");
            if (Defines.ENABLE_lOGGING) {
                Log.i("vserv", new StringBuilder("if OrmmaActionHandler skipDelay:: ").append(this.skipDelay).toString());
            }
        } else {
            this.skipDelay = 0;
            if (Defines.ENABLE_lOGGING) {
                Log.i("vserv", new StringBuilder("else OrmmaActionHandler skipDelay:: ").append(this.skipDelay).toString());
            }
        }
        this.topLayout = new RelativeLayout(this);
        this.topLayout.setLayoutParams(new LayoutParams(-2, -2));
        this.closeButtonImage = new ImageView(this);
        this.closeButtonImage.setImageResource(getResources().getIdentifier("vserv_close_advertisement", "drawable", getPackageName()));
        LayoutParams closeButtonLayoutParams = new LayoutParams(-2, -2);
        closeButtonLayoutParams.addRule(ApiEventType.API_MRAID_EXPAND);
        this.closeButtonImage.setLayoutParams(closeButtonLayoutParams);
        if (this.skipDelay != -1) {
            if (this.skipDelay > 0) {
                this.skipLabel.setVisibility(MMAdView.TRANSITION_RANDOM);
                this.topLayout.addView(this.skipLabel);
            } else {
                this.skipLabel.setVisibility(MMAdView.TRANSITION_RANDOM);
                this.topLayout.addView(this.closeButtonImage);
            }
        }
        this.closeButtonImage.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (OrmmaActionHandler.this.player != null) {
                    OrmmaActionHandler.this.player.releasePlayer();
                }
                OrmmaActionHandler.this.setResult(LocationRequest.PRIORITY_HIGH_ACCURACY, OrmmaActionHandler.this.getIntent());
                OrmmaActionHandler.this.closeCurrentActivity(true);
            }
        });
        this.layout = new RelativeLayout(this);
        this.layout.setLayoutParams(new LayoutParams(-2, -2));
        RelativeLayout rootLayout = new RelativeLayout(this);
        rootLayout.setLayoutParams(new LayoutParams(-2, -2));
        rootLayout.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        rootLayout.addView(this.layout);
        rootLayout.addView(this.topLayout);
        setContentView(rootLayout);
        doAction(data);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        PlayerProperties t_properties = null;
        if (this.skipDelay == -1) {
            if (Defines.ENABLE_lOGGING) {
                Log.i("vserv", new StringBuilder("else ormaactionhandler onKeyDown:: ").append(this.skipDelay).toString());
            }
            Toast.makeText(this, "Please wait while the video completes", 0).show();
            return false;
        } else if (this.skipDelay == 0) {
            if (this.player != null) {
                t_properties = this.player.getPlayerProperties();
                this.player.releasePlayer();
            }
            if (t_properties == null) {
                closeCurrentActivity(false);
            } else if (t_properties.exitOnComplete() || t_properties.inline) {
                closeCurrentActivity(true);
            } else {
                closeCurrentActivity(false);
            }
            return super.onKeyDown(keyCode, event);
        } else if (this.skipDelay >= -1) {
            return false;
        } else {
            if (this.player != null) {
                t_properties = this.player.getPlayerProperties();
                this.player.releasePlayer();
            }
            if (t_properties == null) {
                closeCurrentActivity(false);
            } else if (t_properties.exitOnComplete() || t_properties.inline) {
                closeCurrentActivity(true);
            } else {
                closeCurrentActivity(false);
            }
            return super.onKeyDown(keyCode, event);
        }
    }

    protected void onPause() {
        if (Defines.ENABLE_lOGGING) {
            Log.i("vserv", "ormmahandler onPause");
        }
        if (this.player != null) {
            this.player.pause();
        }
        super.onPause();
    }

    protected void onResume() {
        try {
            if (Defines.ENABLE_lOGGING) {
                Log.i("vserv", "ormmahandler onResume");
            }
            if (this.player != null) {
                this.player.resume();
            }
            super.onResume();
        } catch (Exception e) {
            Exception e2 = e;
            e2.printStackTrace();
            Log.i("vserv", new StringBuilder("onresume exception:: ").append(e2).toString());
        }
    }
}