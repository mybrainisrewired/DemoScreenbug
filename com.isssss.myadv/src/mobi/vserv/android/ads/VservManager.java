package mobi.vserv.android.ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.inmobi.commons.analytics.db.AnalyticsEvent;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.inmobi.monetization.internal.InvalidManifestConfigException;
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMRequest;
import com.mopub.common.Preconditions;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Vector;
import mobi.vserv.org.ormma.controller.Defines;
import mobi.vserv.org.ormma.controller.util.OrmmaPlayer;

public class VservManager {
    public static final int REQUEST_CODE = 888;
    private static OrmmaPlayer adCacheOrmmaPlayer;
    private static Context context;
    private static Hashtable<String, Object> hashtableCacheAdsVideo;
    private static VservManager instance;
    private static String zoneId;
    private String adCacheImpressionHeader;
    private String adCacheMarkup;
    private String adCachePostActionUrl;
    private Object adCacheType;
    private String[] adCacheUrl;
    private WebView adCacheView;
    private AdClickReceiver adClickReceiver;
    private AdDialog adDialog;
    protected String adOrientation;
    private String age;
    private String birthDate;
    private boolean cacheNextAd;
    private String cacheShowAt;
    private String city;
    private String country;
    private String[] deviceIds;
    private boolean displayAd;
    private String email;
    private String gender;
    private Hashtable<String, Object> hashtableCacheAdsDownloadNetworkUrl;
    private Hashtable<String, Object> hashtableCacheAdsImpressionHeader;
    private Hashtable<String, Object> hashtableCacheAdsOrientation;
    private Hashtable<String, Object> hashtableCacheAdsPostActionUrl;
    private Hashtable<String, Object> hashtableCacheAdsRequestId;
    private Hashtable<String, Object> hashtableCacheAdsSkipDelay;
    private Hashtable<String, Object> hashtableCacheAdsType;
    private Hashtable<String, Object> hashtableCacheAdsUrl;
    private Hashtable<String, Object> hashtableCacheAdsView;
    private String orientation;
    private String partnerid;
    private LinearLayout progressLayout;
    private VservAdController renderAdController;
    private String showAt;
    private String storeFrontId;

    class AnonymousClass_1 extends Thread {
        private final /* synthetic */ Exception val$e_obj;
        private final /* synthetic */ String val$extra_info;

        AnonymousClass_1(String str, Exception exception) {
            this.val$extra_info = str;
            this.val$e_obj = exception;
        }

        public void run() {
            String appname = Preconditions.EMPTY_ARGUMENTS;
            String exceptionMessage = new StringBuilder(String.valueOf(this.val$extra_info)).append(" : ").append(Log.getStackTraceString(this.val$e_obj)).toString();
            if (context != null) {
                try {
                    if (context.getPackageName() != null) {
                        appname = URLEncoder.encode(context.getPackageName(), "UTF-8");
                    }
                } catch (UnsupportedEncodingException e) {
                }
            }
            String exceptionUrl = "http://www.google-analytics.com/collect?";
            try {
                if (VservManager.this.isInternetReachable()) {
                    VservManager.this.makeHttpPostConnection(exceptionUrl, new StringBuilder("payload_data&v=1&tid=UA-52309237-1&cid=555&t=exception&exd=").append(URLEncoder.encode(exceptionMessage, "UTF-8")).append("&exf=1").append("&an=").append(URLEncoder.encode("AndroidAdSDK A-AN-2.2.9", "UTF-8")).append("&aid=").append(URLEncoder.encode(appname, "UTF-8")).toString());
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    protected class AdClickReceiver extends BroadcastReceiver {
        protected AdClickReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            Log.i("vserv", new StringBuilder("onReceive::").append(intent.hasExtra(Event.INTENT_EXTERNAL_BROWSER)).toString());
            if (intent.hasExtra("clickToVideo")) {
                Log.i("vserv", new StringBuilder("onReceive 1::").append(intent.hasExtra("clickToVideo")).toString());
                if (VservManager.this.adDialog.countDownTimer != null) {
                    VservManager.this.adDialog.countDownTimer.onFinish();
                    VservManager.this.adDialog.countDownTimer = null;
                    VservManager.this.adDialog.setCloseButton();
                }
            } else {
                Log.i("vserv", new StringBuilder("onReceive else::").append(intent.hasExtra("clickToVideo")).toString());
                if (VservManager.this.renderAdController != null) {
                    VservManager.this.renderAdController.currentSkipDelay = 0;
                }
                VservManager.this.adDialog.setCloseButton();
                if (!(intent.hasExtra(Event.INTENT_EXTERNAL_BROWSER) || VservManager.this.adDialog == null || !VservManager.this.adDialog.isShowing())) {
                    if (VservManager.this.renderAdController == null || VservManager.this.renderAdController.adComponentView == null || VservManager.this.renderAdController.adComponentView.videoPlayer == null) {
                        Log.i("vserv", "else2 onReceive");
                    } else {
                        Log.i("vserv", "if1 onReceive");
                        VservManager.this.renderAdController.adComponentView.videoPlayer.releasePlayer();
                    }
                    VservManager.this.adDialog.dismiss();
                }
                if (VservManager.this.renderAdController != null && VservManager.this.renderAdController.adComponentView != null && VservManager.this.renderAdController.adComponentView.videoPlayer != null) {
                    Log.i("vserv", "if onReceive");
                    VservManager.this.renderAdController.adComponentView.videoPlayer.releasePlayer();
                }
            }
        }
    }

    protected class AdDialog extends Dialog implements OnTouchListener {
        private IClickNotify clickNotify;
        private VservCountDownTimer countDownTimer;
        private int tempSkipdelay;
        private View view;

        public AdDialog(Context context, View view, IClickNotify clickNotify) {
            super(context);
            this.countDownTimer = null;
            this.tempSkipdelay = 0;
            this.view = view;
            this.clickNotify = clickNotify;
        }

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(1);
            getWindow().setBackgroundDrawable(new ColorDrawable(0));
            setContentView(context.getResources().getIdentifier("vserv_dialog_layout", "layout", context.getPackageName()));
            ((FrameLayout) findViewById(context.getResources().getIdentifier("adContainer", AnalyticsEvent.EVENT_ID, context.getPackageName()))).addView(this.view);
            new Thread() {
                public void run() {
                    if (AdDialog.this.this$0.renderAdController == null) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            public void run() {
                                ((ImageView) AnonymousClass_1.this.this$1.findViewById(context.getResources().getIdentifier("closeButton", AnalyticsEvent.EVENT_ID, context.getPackageName()))).setVisibility(0);
                            }
                        });
                    } else if (AdDialog.this.this$0.renderAdController.currentSkipDelay != -1) {
                        ((Activity) context).runOnUiThread(new Runnable() {

                            class AnonymousClass_1 extends VservCountDownTimer {
                                AnonymousClass_1(long $anonymous0, long $anonymous1) {
                                    super($anonymous0, $anonymous1);
                                }

                                public void onFinish() {
                                    if (Defines.ENABLE_lOGGING) {
                                        Log.i("vserv", "onFinish ");
                                    }
                                    AnonymousClass_1.this.this$2.this$1.this$0.renderAdController.currentSkipDelay = 0;
                                    AnonymousClass_1.this.this$2.this$1.tempSkipdelay = 0;
                                    ((TextView) AnonymousClass_1.this.this$2.this$1.findViewById(context.getResources().getIdentifier("skiplabel", AnalyticsEvent.EVENT_ID, context.getPackageName()))).setText(Preconditions.EMPTY_ARGUMENTS);
                                    ((TextView) AnonymousClass_1.this.this$2.this$1.findViewById(context.getResources().getIdentifier("skiplabel", AnalyticsEvent.EVENT_ID, context.getPackageName()))).setVisibility(MMAdView.TRANSITION_RANDOM);
                                    ((ImageView) AnonymousClass_1.this.this$2.this$1.findViewById(context.getResources().getIdentifier("closeButton", AnalyticsEvent.EVENT_ID, context.getPackageName()))).setVisibility(0);
                                }

                                public void onTick(long millisUntilFinished) {
                                    if (Defines.ENABLE_lOGGING) {
                                        Log.i("vserv", new StringBuilder("onTick ").append(millisUntilFinished / 1000).toString());
                                    }
                                    TextView textView = (TextView) AnonymousClass_1.this.this$2.this$1.findViewById(context.getResources().getIdentifier("skiplabel", AnalyticsEvent.EVENT_ID, context.getPackageName()));
                                    StringBuilder stringBuilder = new StringBuilder("Skip in ");
                                    AdDialog access$0 = AnonymousClass_1.this.this$2.this$1;
                                    int access$1 = access$0.tempSkipdelay;
                                    access$0.tempSkipdelay = access$1 - 1;
                                    textView.setText(stringBuilder.append(access$1).toString());
                                }
                            }

                            public void run() {
                                if (AnonymousClass_1.this.this$1.this$0.renderAdController.currentSkipDelay > 0) {
                                    if (Defines.ENABLE_lOGGING) {
                                        Log.i("vserv", new StringBuilder("currentSkipDelay ").append(AnonymousClass_1.this.this$1.this$0.renderAdController.currentSkipDelay).toString());
                                    }
                                    ((TextView) AnonymousClass_1.this.this$1.findViewById(context.getResources().getIdentifier("skiplabel", AnalyticsEvent.EVENT_ID, context.getPackageName()))).setVisibility(0);
                                    ((TextView) AnonymousClass_1.this.this$1.findViewById(context.getResources().getIdentifier("skiplabel", AnalyticsEvent.EVENT_ID, context.getPackageName()))).setText(new StringBuilder("Skip in ").append(AnonymousClass_1.this.this$1.this$0.renderAdController.currentSkipDelay).toString());
                                    AnonymousClass_1.this.this$1.tempSkipdelay = AnonymousClass_1.this.this$1.this$0.renderAdController.currentSkipDelay;
                                    AnonymousClass_1.this.this$1.countDownTimer = new AnonymousClass_1((long) (AnonymousClass_1.this.this$1.this$0.renderAdController.currentSkipDelay * 1000), 1000).start();
                                } else {
                                    ((ImageView) AnonymousClass_1.this.this$1.findViewById(context.getResources().getIdentifier("closeButton", AnalyticsEvent.EVENT_ID, context.getPackageName()))).setVisibility(0);
                                }
                            }
                        });
                    }
                }
            }.start();
            ((ImageView) findViewById(context.getResources().getIdentifier("closeButton", AnalyticsEvent.EVENT_ID, context.getPackageName()))).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    AdDialog.this.clickNotify.onClick();
                    AdDialog.this.dismiss();
                }
            });
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    AdDialog.this.getWindow().setLayout(-1, InvalidManifestConfigException.MISSING_ACTIVITY_DECLARATION);
                }
            }, 150);
        }

        public boolean onKeyDown(int keyCode, KeyEvent event) {
            try {
                if (VservManager.this.renderAdController == null || VservManager.this.renderAdController.currentSkipDelay == -1) {
                    Toast.makeText(context, "Please wait while the video completes", 0).show();
                    return false;
                } else {
                    if (VservManager.this.renderAdController.currentSkipDelay == 0) {
                        VservManager.getInstance(context).adOrientation = null;
                        if (VservManager.this.adClickReceiver != null) {
                            context.unregisterReceiver(VservManager.this.adClickReceiver);
                            VservManager.this.adClickReceiver = null;
                            VservManager.this.adDialog = null;
                        }
                        if (!(VservManager.this.renderAdController == null || VservManager.this.renderAdController.adComponentView == null || VservManager.this.renderAdController.adComponentView.videoPlayer == null)) {
                            VservManager.this.renderAdController.adComponentView.videoPlayer.releasePlayer();
                        }
                    } else if (VservManager.this.renderAdController.currentSkipDelay >= -1) {
                        return false;
                    } else {
                        VservManager.getInstance(context).adOrientation = null;
                        if (VservManager.this.adClickReceiver != null) {
                            context.unregisterReceiver(VservManager.this.adClickReceiver);
                            VservManager.this.adClickReceiver = null;
                            VservManager.this.adDialog = null;
                        }
                        if (!(VservManager.this.renderAdController == null || VservManager.this.renderAdController.adComponentView == null || VservManager.this.renderAdController.adComponentView.videoPlayer == null)) {
                            VservManager.this.renderAdController.adComponentView.videoPlayer.releasePlayer();
                        }
                    }
                    return super.onKeyDown(keyCode, event);
                }
            } catch (Exception e) {
            }
        }

        public boolean onTouch(View v, MotionEvent event) {
            Log.i("vserv", "onTouch of frame ");
            return false;
        }

        public void setCloseButton() {
            ((ImageView) findViewById(context.getResources().getIdentifier("closeButton", AnalyticsEvent.EVENT_ID, context.getPackageName()))).setVisibility(0);
        }
    }

    protected static interface IClickNotify {
        void onClick();
    }

    class AnonymousClass_2 implements IAddCallback {
        private final /* synthetic */ ProgressDialog val$progressBar;

        AnonymousClass_2(ProgressDialog progressDialog) {
            this.val$progressBar = progressDialog;
        }

        public void TimeOutOccured() {
            if (this.val$progressBar != null && this.val$progressBar.isShowing()) {
                this.val$progressBar.dismiss();
                VservManager.this.displayAd = false;
            }
        }

        public void onLoadFailure() {
            VservManager.getInstance(context).adOrientation = null;
            if (this.val$progressBar.isShowing()) {
                this.val$progressBar.dismiss();
            }
        }

        public void onLoadSuccess(View view) {
            if (VservManager.this.adDialog == null && VservManager.this.displayAd) {
                if (this.val$progressBar.isShowing()) {
                    this.val$progressBar.dismiss();
                }
                VservManager.this.adDialog = new AdDialog(context, view, new IClickNotify() {
                    public void onClick() {
                        if (AnonymousClass_2.this.this$0.renderAdController.adComponentView != null && AnonymousClass_2.this.this$0.renderAdController.adComponentView.videoPlayer != null) {
                            AnonymousClass_2.this.this$0.renderAdController.adComponentView.videoPlayer.releasePlayer();
                        }
                    }
                });
                VservManager.this.adClickReceiver = new AdClickReceiver();
                context.registerReceiver(VservManager.this.adClickReceiver, new IntentFilter("mobi.vserv.ad.dismiss_screen"));
                VservManager.this.adDialog.setCanceledOnTouchOutside(false);
                VservManager.this.adDialog.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        try {
                            VservManager.getInstance(context).adOrientation = null;
                            if (AnonymousClass_2.this.this$0.adClickReceiver != null) {
                                context.unregisterReceiver(AnonymousClass_2.this.this$0.adClickReceiver);
                                AnonymousClass_2.this.this$0.adClickReceiver = null;
                                AnonymousClass_2.this.this$0.adDialog = null;
                            }
                            if (AnonymousClass_2.this.this$0.renderAdController != null && AnonymousClass_2.this.this$0.renderAdController.adComponentView != null && AnonymousClass_2.this.this$0.renderAdController.adComponentView.videoPlayer != null) {
                                AnonymousClass_2.this.this$0.renderAdController.adComponentView.videoPlayer.releasePlayer();
                            }
                        } catch (Exception e) {
                        }
                    }
                });
                VservManager.this.adDialog.setOnCancelListener(new OnCancelListener() {
                    public void onCancel(DialogInterface arg0) {
                        try {
                            VservManager.getInstance(context).adOrientation = null;
                            if (AnonymousClass_2.this.this$0.adClickReceiver != null) {
                                context.unregisterReceiver(AnonymousClass_2.this.this$0.adClickReceiver);
                                AnonymousClass_2.this.this$0.adClickReceiver = null;
                                AnonymousClass_2.this.this$0.adDialog = null;
                            }
                            if (AnonymousClass_2.this.this$0.renderAdController != null && AnonymousClass_2.this.this$0.renderAdController.adComponentView != null && AnonymousClass_2.this.this$0.renderAdController.adComponentView.videoPlayer != null) {
                                AnonymousClass_2.this.this$0.renderAdController.adComponentView.videoPlayer.releasePlayer();
                            }
                        } catch (Exception e) {
                        }
                    }
                });
                VservManager.this.adDialog.show();
            }
        }

        public void onNoFill() {
            VservManager.getInstance(context).adOrientation = null;
            if (this.val$progressBar.isShowing()) {
                this.val$progressBar.dismiss();
            }
        }

        public void showProgressBar() {
            this.val$progressBar.show();
        }
    }

    class AnonymousClass_3 implements IAddCallback {
        private final /* synthetic */ ViewGroup val$group;

        AnonymousClass_3(ViewGroup viewGroup) {
            this.val$group = viewGroup;
        }

        public void TimeOutOccured() {
        }

        public void onLoadFailure() {
            this.val$group.removeAllViews();
        }

        public void onLoadSuccess(View view) {
            this.val$group.removeAllViews();
            this.val$group.addView(view);
        }

        public void onNoFill() {
            this.val$group.removeAllViews();
        }

        public void showProgressBar() {
            this.val$group.removeAllViews();
            VservManager.this.progressLayout = new LinearLayout(context);
            VservManager.this.progressLayout.setOrientation(1);
            VservManager.this.progressLayout.setGravity(ApiEventType.API_MRAID_GET_SCREEN_SIZE);
            ProgressBar progressBar = new ProgressBar(context, null, 16842871);
            progressBar.setVisibility(0);
            progressBar.setLayoutParams(new LayoutParams(-2, -2));
            VservManager.this.progressLayout.addView(progressBar);
            this.val$group.addView(VservManager.this.progressLayout);
        }
    }

    class AnonymousClass_4 implements IAddCallback {
        private final /* synthetic */ AdLoadCallback val$callback;
        private final /* synthetic */ String val$zoneId;

        AnonymousClass_4(String str, AdLoadCallback adLoadCallback) {
            this.val$zoneId = str;
            this.val$callback = adLoadCallback;
        }

        public void TimeOutOccured() {
        }

        public void onLoadFailure() {
            this.val$callback.onLoadFailure();
        }

        public void onLoadSuccess(View view) {
            VservAd adObject = new VservAd();
            adObject.zoneId = this.val$zoneId;
            this.val$callback.onLoadSuccess(adObject);
        }

        public void onNoFill() {
            this.val$callback.onNoFill();
        }

        public void showProgressBar() {
        }
    }

    static {
        zoneId = null;
        instance = null;
        adCacheOrmmaPlayer = null;
        hashtableCacheAdsVideo = null;
    }

    private VservManager() {
        this.orientation = null;
        this.cacheNextAd = true;
        this.showAt = null;
        this.cacheShowAt = "mid";
        this.partnerid = null;
        this.storeFrontId = null;
        this.birthDate = null;
        this.age = null;
        this.gender = null;
        this.city = null;
        this.country = null;
        this.email = null;
        this.deviceIds = null;
        this.displayAd = true;
        this.adCacheView = null;
        this.adCacheMarkup = null;
        this.adCacheUrl = null;
        this.adCacheType = null;
        this.adCachePostActionUrl = null;
        this.adCacheImpressionHeader = null;
        this.adOrientation = null;
        this.hashtableCacheAdsView = null;
        this.hashtableCacheAdsUrl = null;
        this.hashtableCacheAdsType = null;
        this.hashtableCacheAdsPostActionUrl = null;
        this.hashtableCacheAdsDownloadNetworkUrl = null;
        this.hashtableCacheAdsImpressionHeader = null;
        this.hashtableCacheAdsOrientation = null;
        this.hashtableCacheAdsSkipDelay = null;
        this.hashtableCacheAdsRequestId = null;
    }

    private void displayAd(String zoneId, AdType adType, AdOrientation adOrientation) {
        zoneId = zoneId;
        try {
            Bundle bundle = getPreparedBundle();
            bundle.putString("cf", "0");
            bundle.putString("ao", URLEncoder.encode(context.getResources().getConfiguration().orientation == 1 ? "p" : "l", "UTF-8"));
            if (adOrientation != null) {
                if (adOrientation == AdOrientation.LANDSCAPE) {
                    bundle.putString("ro", "l");
                } else if (adOrientation == AdOrientation.PORTRAIT) {
                    bundle.putString("ro", "p");
                }
            }
            if (isCacheNextAd()) {
                bundle.putBoolean("cacheNextAd", true);
                bundle.putString("cacheShowAt", this.cacheShowAt);
            }
            if (getShowAt() != null) {
                bundle.putString("showAt", getShowAt());
            } else {
                bundle.putString("showAt", "mid");
            }
            if (adType == null || adType != AdType.OVERLAY) {
                Intent vservAdIntent = new Intent(context, VservAdManager.class);
                vservAdIntent.putExtras(bundle);
                ((Activity) context).startActivityForResult(vservAdIntent, REQUEST_CODE);
            } else {
                ProgressDialog progressBar = new ProgressDialog(context);
                progressBar.setProgressStyle(0);
                progressBar.setCancelable(false);
                bundle.putString("refreshInterval", "no");
                bundle.putString("considerTimeout", Preconditions.EMPTY_ARGUMENTS);
                bundle.putBoolean("frameAd", true);
                this.renderAdController = new VservAdController(context, bundle, new AnonymousClass_2(progressBar));
                this.displayAd = true;
                this.renderAdController.requestAddView();
            }
        } catch (Exception e) {
            Exception e2 = e;
            trackExceptions(e2, "displayAd");
            e2.printStackTrace();
        }
    }

    protected static OrmmaPlayer getAdVideo() {
        if (hashtableCacheAdsVideo == null || !hashtableCacheAdsVideo.containsKey(zoneId)) {
            return null;
        }
        Object t_objurls = hashtableCacheAdsVideo.get(zoneId);
        if (t_objurls instanceof Vector) {
            adCacheOrmmaPlayer = (OrmmaPlayer) ((Vector) t_objurls).firstElement();
            return adCacheOrmmaPlayer;
        } else {
            adCacheOrmmaPlayer = (OrmmaPlayer) hashtableCacheAdsVideo.get(zoneId);
            return adCacheOrmmaPlayer;
        }
    }

    @SuppressLint({"InlinedApi"})
    public static VservManager getInstance(Context ctx) {
        context = ctx;
        synchronized (VservManager.class) {
            if (instance == null) {
                SharedPreferences adPreference;
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", "Instance is null vservManager");
                }
                instance = new VservManager();
                if (VERSION.SDK_INT >= 11) {
                    adPreference = context.getSharedPreferences("vserv_unique_add_app_session", MMAdView.TRANSITION_RANDOM);
                } else {
                    adPreference = context.getSharedPreferences("vserv_unique_add_app_session", 0);
                }
                adPreference.edit().clear().commit();
            } else if (Defines.ENABLE_lOGGING) {
                Log.i("vserv", "Instance is not null so not clearing resource");
            }
        }
        return instance;
    }

    protected static void removeAdVideo() {
        if (hashtableCacheAdsVideo != null && hashtableCacheAdsVideo.containsKey(zoneId)) {
            Object t_objviews = hashtableCacheAdsVideo.get(zoneId);
            if (t_objviews instanceof Vector) {
                Vector t_vplayer = (Vector) t_objviews;
                adCacheOrmmaPlayer = (OrmmaPlayer) t_vplayer.firstElement();
                t_vplayer.remove(adCacheOrmmaPlayer);
                if (t_vplayer.size() == 0) {
                    hashtableCacheAdsVideo.remove(zoneId);
                } else if (t_vplayer.size() > 1) {
                    hashtableCacheAdsVideo.put(zoneId, t_vplayer);
                } else {
                    adCacheOrmmaPlayer = (OrmmaPlayer) t_vplayer.firstElement();
                    hashtableCacheAdsVideo.put(zoneId, adCacheOrmmaPlayer);
                }
            } else {
                hashtableCacheAdsVideo.remove(zoneId);
            }
        }
    }

    private void setPartnerId(String partnerId) {
        this.partnerid = partnerId;
    }

    public void addTestDevice(String... Ids) {
        this.deviceIds = Ids;
    }

    public void displayAd(String zoneId) {
        displayAd(zoneId, null, null);
    }

    public void displayAd(String zoneId, AdOrientation adOrientation) {
        displayAd(zoneId, null, adOrientation);
    }

    public void displayAd(String zoneId, AdType adType) {
        displayAd(zoneId, adType, null);
    }

    public void getAd(String zoneId, AdLoadCallback callback) {
        getAd(zoneId, null, callback);
    }

    public void getAd(String zoneId, AdOrientation adOrientation, AdLoadCallback callback) {
        zoneId = zoneId;
        try {
            Bundle bundle = getPreparedBundle();
            if (getShowAt() != null) {
                bundle.putString("getAdPos", getShowAt());
            } else {
                bundle.putString("getAdPos", "mid");
            }
            bundle.putString("showAt", AnalyticsEvent.IN_APP);
            bundle.putString("adType", "getAd");
            bundle.putString("cf", "2");
            if (adOrientation != null) {
                if (adOrientation == AdOrientation.LANDSCAPE) {
                    bundle.putString("ro", "l");
                } else if (adOrientation == AdOrientation.PORTRAIT) {
                    bundle.putString("ro", "p");
                }
            }
            new VservAdController(context, bundle, new AnonymousClass_4(zoneId, callback)).requestAddView();
        } catch (Exception e) {
            Exception e2 = e;
            trackExceptions(e2, "getAd");
            e2.printStackTrace();
        }
    }

    protected Object getAdType() {
        if (this.hashtableCacheAdsType == null || !this.hashtableCacheAdsType.containsKey(zoneId)) {
            return null;
        }
        Object t_objadstype = this.hashtableCacheAdsType.get(zoneId);
        if (t_objadstype instanceof Vector) {
            this.adCacheType = ((Vector) t_objadstype).firstElement();
            return this.adCacheType;
        } else {
            this.adCacheType = this.hashtableCacheAdsType.get(zoneId);
            return this.adCacheType;
        }
    }

    protected String[] getAdURLs() {
        if (this.hashtableCacheAdsUrl == null || !this.hashtableCacheAdsUrl.containsKey(zoneId)) {
            return null;
        }
        Object t_objurls = this.hashtableCacheAdsUrl.get(zoneId);
        if (t_objurls instanceof Vector) {
            this.adCacheUrl = (String[]) ((Vector) t_objurls).firstElement();
            return this.adCacheUrl;
        } else {
            this.adCacheUrl = (String[]) this.hashtableCacheAdsUrl.get(zoneId);
            return this.adCacheUrl;
        }
    }

    protected Object getAdview() {
        if (this.hashtableCacheAdsView == null || !this.hashtableCacheAdsView.containsKey(zoneId)) {
            return null;
        }
        Object t_objads = this.hashtableCacheAdsView.get(zoneId);
        Object adview;
        if (t_objads instanceof Vector) {
            adview = ((Vector) t_objads).firstElement();
            if (adview instanceof WebView) {
                this.adCacheView = (WebView) adview;
                return this.adCacheView;
            } else {
                this.adCacheMarkup = (String) adview;
                return this.adCacheMarkup;
            }
        } else {
            adview = this.hashtableCacheAdsView.get(zoneId);
            if (adview instanceof WebView) {
                this.adCacheView = (WebView) adview;
                return this.adCacheView;
            } else {
                this.adCacheMarkup = (String) adview;
                return this.adCacheMarkup;
            }
        }
    }

    String getAge() {
        return this.age;
    }

    String getBirthDate() {
        return this.birthDate;
    }

    protected Object getCacheAdOrientation() {
        if (this.hashtableCacheAdsOrientation == null || !this.hashtableCacheAdsOrientation.containsKey(zoneId)) {
            return null;
        }
        Object t_objurls = this.hashtableCacheAdsOrientation.get(zoneId);
        return t_objurls instanceof Vector ? ((Vector) t_objurls).firstElement() : this.hashtableCacheAdsOrientation.get(zoneId);
    }

    protected Object getCacheAdRequestId() {
        if (this.hashtableCacheAdsRequestId == null || !this.hashtableCacheAdsRequestId.containsKey(zoneId)) {
            return null;
        }
        Object t_objurls = this.hashtableCacheAdsRequestId.get(zoneId);
        return t_objurls instanceof Vector ? ((Vector) t_objurls).firstElement() : this.hashtableCacheAdsRequestId.get(zoneId);
    }

    protected Object getCacheAdSkipDelay() {
        if (this.hashtableCacheAdsSkipDelay == null || !this.hashtableCacheAdsSkipDelay.containsKey(zoneId)) {
            return null;
        }
        Object t_objurls = this.hashtableCacheAdsSkipDelay.get(zoneId);
        return t_objurls instanceof Vector ? ((Vector) t_objurls).firstElement() : this.hashtableCacheAdsSkipDelay.get(zoneId);
    }

    protected String getCacheImpression() {
        if (this.hashtableCacheAdsImpressionHeader == null || !this.hashtableCacheAdsImpressionHeader.containsKey(zoneId)) {
            return null;
        }
        Object t_objurls = this.hashtableCacheAdsImpressionHeader.get(zoneId);
        if (t_objurls instanceof Vector) {
            this.adCacheImpressionHeader = (String) ((Vector) t_objurls).firstElement();
            return this.adCacheImpressionHeader;
        } else {
            this.adCacheImpressionHeader = (String) this.hashtableCacheAdsImpressionHeader.get(zoneId);
            return this.adCacheImpressionHeader;
        }
    }

    String getCity() {
        return this.city;
    }

    String getCountry() {
        return this.country;
    }

    protected Object getDownloadNetworkURL() {
        if (this.hashtableCacheAdsDownloadNetworkUrl == null || !this.hashtableCacheAdsDownloadNetworkUrl.containsKey(zoneId)) {
            return null;
        }
        Object t_objurls = this.hashtableCacheAdsDownloadNetworkUrl.get(zoneId);
        return t_objurls instanceof Vector ? ((Vector) t_objurls).firstElement() : this.hashtableCacheAdsPostActionUrl.get(zoneId);
    }

    String getEmail() {
        return this.email;
    }

    String getGender() {
        return this.gender;
    }

    String getPartnerid() {
        return this.partnerid;
    }

    protected String getPostActionURL() {
        if (this.hashtableCacheAdsPostActionUrl == null || !this.hashtableCacheAdsPostActionUrl.containsKey(zoneId)) {
            return null;
        }
        Object t_objurls = this.hashtableCacheAdsPostActionUrl.get(zoneId);
        if (t_objurls instanceof Vector) {
            this.adCachePostActionUrl = (String) ((Vector) t_objurls).firstElement();
            return this.adCachePostActionUrl;
        } else {
            this.adCachePostActionUrl = (String) this.hashtableCacheAdsPostActionUrl.get(zoneId);
            return this.adCachePostActionUrl;
        }
    }

    Bundle getPreparedBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("zoneId", getZoneId());
        if (!TextUtils.isEmpty(getEmail())) {
            bundle.putString("em", getEmail());
        }
        if (!TextUtils.isEmpty(getPartnerid())) {
            bundle.putString("partnerid", getPartnerid());
        }
        if (VERSION.SDK_INT >= 9 && this.deviceIds != null) {
            bundle.putStringArray("deviceIds", this.deviceIds);
        }
        if (!TextUtils.isEmpty(getStoreFrontId())) {
            bundle.putString("sf", getStoreFrontId());
        }
        if (!TextUtils.isEmpty(getBirthDate())) {
            bundle.putString("dob", getBirthDate());
        }
        if (!TextUtils.isEmpty(getAge())) {
            bundle.putString("ag", getAge());
        }
        if (!TextUtils.isEmpty(getGender())) {
            bundle.putString("gn", getGender());
        }
        if (!TextUtils.isEmpty(getCity())) {
            bundle.putString("ci", getCity());
        }
        if (!TextUtils.isEmpty(getCountry())) {
            bundle.putString("co", getCountry());
        }
        return bundle;
    }

    String getShowAt() {
        return this.showAt;
    }

    String getStoreFrontId() {
        return this.storeFrontId;
    }

    String getZoneId() {
        return zoneId;
    }

    boolean isCacheNextAd() {
        return this.cacheNextAd;
    }

    protected boolean isInternetReachable() {
        boolean z = false;
        try {
            return InetAddress.getByName("www.google-analytics.com") == null ? z : true;
        } catch (Exception e) {
            return z;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected java.lang.String makeHttpPostConnection(java.lang.String r20_requestUrl, java.lang.String r21_dataToBePosted) {
        throw new UnsupportedOperationException("Method not decompiled: mobi.vserv.android.ads.VservManager.makeHttpPostConnection(java.lang.String, java.lang.String):java.lang.String");
        /*
        r19 = this;
        r3 = 0;
        r2 = 0;
        r5 = 0;
        r10 = 0;
        r14 = -1;
        r11 = 0;
    L_0x0006:
        r9 = new java.net.URL;	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r0 = r20;
        r9.<init>(r0);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r16 = r9.openConnection();	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r0 = r16;
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r2 = r0;
        r8 = r21;
        r16 = "POST";
        r0 = r16;
        r2.setRequestMethod(r0);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r16 = "Content-Type";
        r17 = "application/x-www-form-urlencoded";
        r0 = r16;
        r1 = r17;
        r2.setRequestProperty(r0, r1);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r16 = "Content-length";
        r17 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r17.<init>();	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r18 = r8.getBytes();	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r0 = r18;
        r0 = r0.length;	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r18 = r0;
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r17 = r17.toString();	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r0 = r16;
        r1 = r17;
        r2.setRequestProperty(r0, r1);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r16 = 1;
        r0 = r16;
        r2.setDoOutput(r0);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r16 = 1;
        r0 = r16;
        r2.setDoInput(r0);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r2.connect();	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r15 = new java.io.DataOutputStream;	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r16 = r2.getOutputStream();	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r15.<init>(r16);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r15.writeBytes(r8);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r15.flush();	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r15.close();	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r13 = r2.getResponseCode();	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r16 = "vservmanager";
        r17 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r18 = "Sending 'POST' request to URL : ";
        r17.<init>(r18);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r0 = r17;
        r1 = r20;
        r17 = r0.append(r1);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r17 = r17.toString();	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        android.util.Log.i(r16, r17);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r16 = "vservmanager";
        r17 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r18 = "Post parameters : ";
        r17.<init>(r18);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r0 = r17;
        r17 = r0.append(r8);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r17 = r17.toString();	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        android.util.Log.i(r16, r17);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r16 = "vservmanager";
        r17 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r18 = "Response Code : ";
        r17.<init>(r18);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r0 = r17;
        r17 = r0.append(r13);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r17 = r17.toString();	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        android.util.Log.i(r16, r17);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r4 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r16 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r17 = r2.getInputStream();	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r16.<init>(r17);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r0 = r16;
        r4.<init>(r0);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r14 = r13;
        r16 = 302; // 0x12e float:4.23E-43 double:1.49E-321;
        r0 = r16;
        if (r14 == r0) goto L_0x00d1;
    L_0x00cb:
        r16 = 301; // 0x12d float:4.22E-43 double:1.487E-321;
        r0 = r16;
        if (r14 != r0) goto L_0x00f2;
    L_0x00d1:
        r16 = "location";
        r0 = r16;
        r20 = r2.getHeaderField(r0);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r2.disconnect();	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        goto L_0x0006;
    L_0x00de:
        r16 = move-exception;
    L_0x00df:
        if (r5 == 0) goto L_0x00e4;
    L_0x00e1:
        r5.close();	 Catch:{ Exception -> 0x016d }
    L_0x00e4:
        if (r10 == 0) goto L_0x00e9;
    L_0x00e6:
        r10.close();	 Catch:{ Exception -> 0x016d }
    L_0x00e9:
        if (r2 == 0) goto L_0x00ee;
    L_0x00eb:
        r2.disconnect();	 Catch:{ Exception -> 0x016d }
    L_0x00ee:
        java.lang.System.gc();	 Catch:{ Exception -> 0x016d }
    L_0x00f1:
        return r11;
    L_0x00f2:
        r16 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r0 = r16;
        if (r14 != r0) goto L_0x012b;
    L_0x00f8:
        r5 = r2.getInputStream();	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r6 = r2.getContentLength();	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        if (r6 <= 0) goto L_0x014d;
    L_0x0102:
        r3 = new byte[r6];	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r6 = 0;
    L_0x0105:
        r0 = r3.length;	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r16 = r0;
        r0 = r16;
        if (r6 != r0) goto L_0x0140;
    L_0x010c:
        r12 = new java.lang.String;	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r12.<init>(r3);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r11 = r12.trim();	 Catch:{ Exception -> 0x016f, all -> 0x016a }
        r16 = "vservmanager";
        r17 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r18 = "vservmanager Response:: ";
        r17.<init>(r18);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r0 = r17;
        r17 = r0.append(r11);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r17 = r17.toString();	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        android.util.Log.i(r16, r17);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
    L_0x012b:
        if (r5 == 0) goto L_0x0130;
    L_0x012d:
        r5.close();	 Catch:{ Exception -> 0x013e }
    L_0x0130:
        if (r10 == 0) goto L_0x0135;
    L_0x0132:
        r10.close();	 Catch:{ Exception -> 0x013e }
    L_0x0135:
        if (r2 == 0) goto L_0x013a;
    L_0x0137:
        r2.disconnect();	 Catch:{ Exception -> 0x013e }
    L_0x013a:
        java.lang.System.gc();	 Catch:{ Exception -> 0x013e }
        goto L_0x00f1;
    L_0x013e:
        r16 = move-exception;
        goto L_0x00f1;
    L_0x0140:
        r0 = r3.length;	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r16 = r0;
        r16 = r16 - r6;
        r0 = r16;
        r7 = r5.read(r3, r6, r0);	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        r6 = r6 + r7;
        goto L_0x0105;
    L_0x014d:
        r16 = 1;
        r0 = r16;
        r3 = new byte[r0];	 Catch:{ Exception -> 0x00de, all -> 0x0154 }
        goto L_0x010c;
    L_0x0154:
        r16 = move-exception;
    L_0x0155:
        if (r5 == 0) goto L_0x015a;
    L_0x0157:
        r5.close();	 Catch:{ Exception -> 0x0168 }
    L_0x015a:
        if (r10 == 0) goto L_0x015f;
    L_0x015c:
        r10.close();	 Catch:{ Exception -> 0x0168 }
    L_0x015f:
        if (r2 == 0) goto L_0x0164;
    L_0x0161:
        r2.disconnect();	 Catch:{ Exception -> 0x0168 }
    L_0x0164:
        java.lang.System.gc();	 Catch:{ Exception -> 0x0168 }
    L_0x0167:
        throw r16;
    L_0x0168:
        r17 = move-exception;
        goto L_0x0167;
    L_0x016a:
        r16 = move-exception;
        r11 = r12;
        goto L_0x0155;
    L_0x016d:
        r16 = move-exception;
        goto L_0x00f1;
    L_0x016f:
        r16 = move-exception;
        r11 = r12;
        goto L_0x00df;
        */
    }

    @SuppressLint({"InlinedApi"})
    public void release(Context context) {
        try {
            SharedPreferences cachedAdPreference;
            SharedPreferences gpHitPreference;
            SharedPreferences contextAdPreference;
            if (VERSION.SDK_INT >= 11) {
                cachedAdPreference = context.getSharedPreferences("vserv_cached_ad", MMAdView.TRANSITION_RANDOM);
                gpHitPreference = context.getSharedPreferences("vserv_gp_preference", MMAdView.TRANSITION_RANDOM);
                contextAdPreference = context.getSharedPreferences("vserv_unique_add_app_session", MMAdView.TRANSITION_RANDOM);
            } else {
                cachedAdPreference = context.getSharedPreferences("vserv_cached_ad", 0);
                gpHitPreference = context.getSharedPreferences("vserv_gp_preference", 0);
                contextAdPreference = context.getSharedPreferences("vserv_unique_add_app_session", 0);
            }
            cachedAdPreference.edit().clear().commit();
            gpHitPreference.edit().clear().commit();
            contextAdPreference.edit().clear().commit();
            if (this.renderAdController != null) {
                this.renderAdController.clearAdCachePanel();
                this.renderAdController.clearAdPanel();
            }
            this.hashtableCacheAdsPostActionUrl = null;
            this.hashtableCacheAdsImpressionHeader = null;
            this.hashtableCacheAdsType = null;
            this.hashtableCacheAdsView = null;
            hashtableCacheAdsVideo = null;
            this.hashtableCacheAdsUrl = null;
            this.hashtableCacheAdsSkipDelay = null;
            this.hashtableCacheAdsRequestId = null;
            this.hashtableCacheAdsOrientation = null;
            this.hashtableCacheAdsImpressionHeader = null;
            this.hashtableCacheAdsDownloadNetworkUrl = null;
            adCacheOrmmaPlayer = null;
            this.adCacheView = null;
            this.adCachePostActionUrl = null;
            this.adCacheType = null;
            this.adCacheUrl = null;
            this.renderAdController = null;
            File file = new File(context.getFilesDir(), "getAdFile.txt");
            if (file.exists()) {
                file.delete();
            }
            Log.i("vserv", "Releasedddddd");
        } catch (Exception e) {
            Exception e2 = e;
            trackExceptions(e2, "release");
            e2.printStackTrace();
        }
    }

    protected void removeAdType() {
        if (this.hashtableCacheAdsType != null && this.hashtableCacheAdsType.containsKey(zoneId)) {
            Object t_objads = this.hashtableCacheAdsType.get(zoneId);
            if (t_objads instanceof Vector) {
                Vector t_vads = (Vector) t_objads;
                this.adCacheType = t_vads.firstElement();
                t_vads.remove(this.adCacheType);
                if (t_vads.size() == 0) {
                    this.hashtableCacheAdsType.remove(zoneId);
                } else if (t_vads.size() > 1) {
                    this.hashtableCacheAdsType.put(zoneId, t_vads);
                } else {
                    this.adCacheType = t_vads.firstElement();
                    this.hashtableCacheAdsType.put(zoneId, this.adCacheType);
                }
            } else {
                this.hashtableCacheAdsType.remove(zoneId);
            }
        }
    }

    protected void removeAdUrls() {
        if (this.hashtableCacheAdsUrl != null && this.hashtableCacheAdsUrl.containsKey(zoneId)) {
            Object t_objurls = this.hashtableCacheAdsUrl.get(zoneId);
            if (t_objurls instanceof Vector) {
                Vector t_vurls = (Vector) t_objurls;
                this.adCacheUrl = (String[]) t_vurls.firstElement();
                t_vurls.remove(this.adCacheUrl);
                if (t_vurls.size() == 0) {
                    this.hashtableCacheAdsUrl.remove(zoneId);
                } else if (t_vurls.size() > 1) {
                    this.hashtableCacheAdsUrl.put(zoneId, t_vurls);
                } else {
                    this.adCacheUrl = (String[]) t_vurls.firstElement();
                    this.hashtableCacheAdsUrl.put(zoneId, this.adCacheUrl);
                }
            } else {
                this.hashtableCacheAdsUrl.remove(zoneId);
            }
        }
    }

    protected void removeAdview() {
        if (this.hashtableCacheAdsView != null && this.hashtableCacheAdsView.containsKey(zoneId)) {
            Object t_objads = this.hashtableCacheAdsView.get(zoneId);
            if (t_objads instanceof Vector) {
                Vector t_vads = (Vector) t_objads;
                t_vads.remove(t_vads.firstElement());
                if (t_vads.size() == 0) {
                    this.hashtableCacheAdsView.remove(zoneId);
                } else if (t_vads.size() > 1) {
                    this.hashtableCacheAdsView.put(zoneId, t_vads);
                } else {
                    this.hashtableCacheAdsView.put(zoneId, t_vads.firstElement());
                }
            } else {
                this.hashtableCacheAdsView.remove(zoneId);
            }
        }
    }

    protected void removeCacheAdOrientation() {
        if (this.hashtableCacheAdsOrientation != null && this.hashtableCacheAdsOrientation.containsKey(zoneId)) {
            Object t_objurls = this.hashtableCacheAdsOrientation.get(zoneId);
            if (t_objurls instanceof Vector) {
                Vector t_vurls = (Vector) t_objurls;
                t_vurls.remove(t_vurls.firstElement());
                if (t_vurls.size() == 0) {
                    this.hashtableCacheAdsOrientation.remove(zoneId);
                } else if (t_vurls.size() > 1) {
                    this.hashtableCacheAdsOrientation.put(zoneId, t_vurls);
                } else {
                    this.hashtableCacheAdsOrientation.put(zoneId, t_vurls.firstElement());
                }
            } else {
                this.hashtableCacheAdsOrientation.remove(zoneId);
            }
        }
    }

    protected void removeCacheAdRequestId() {
        if (this.hashtableCacheAdsRequestId != null && this.hashtableCacheAdsRequestId.containsKey(zoneId)) {
            Object t_objurls = this.hashtableCacheAdsRequestId.get(zoneId);
            if (t_objurls instanceof Vector) {
                Vector t_vurls = (Vector) t_objurls;
                t_vurls.remove(t_vurls.firstElement());
                if (t_vurls.size() == 0) {
                    this.hashtableCacheAdsRequestId.remove(zoneId);
                } else if (t_vurls.size() > 1) {
                    this.hashtableCacheAdsRequestId.put(zoneId, t_vurls);
                } else {
                    this.hashtableCacheAdsRequestId.put(zoneId, t_vurls.firstElement());
                }
            } else {
                this.hashtableCacheAdsRequestId.remove(zoneId);
            }
        }
    }

    protected void removeCacheAdSkipDelay() {
        if (this.hashtableCacheAdsSkipDelay != null && this.hashtableCacheAdsSkipDelay.containsKey(zoneId)) {
            Object t_objurls = this.hashtableCacheAdsSkipDelay.get(zoneId);
            if (t_objurls instanceof Vector) {
                Vector t_vurls = (Vector) t_objurls;
                t_vurls.remove(t_vurls.firstElement());
                if (t_vurls.size() == 0) {
                    this.hashtableCacheAdsSkipDelay.remove(zoneId);
                } else if (t_vurls.size() > 1) {
                    this.hashtableCacheAdsSkipDelay.put(zoneId, t_vurls);
                } else {
                    this.hashtableCacheAdsSkipDelay.put(zoneId, t_vurls.firstElement());
                }
            } else {
                this.hashtableCacheAdsSkipDelay.remove(zoneId);
            }
        }
    }

    protected void removeCacheImpressionHeader() {
        if (this.hashtableCacheAdsImpressionHeader != null && this.hashtableCacheAdsImpressionHeader.containsKey(zoneId)) {
            Object t_objimprheader = this.hashtableCacheAdsImpressionHeader.get(zoneId);
            if (t_objimprheader instanceof Vector) {
                Vector t_vurls = (Vector) t_objimprheader;
                this.adCacheImpressionHeader = (String) t_vurls.firstElement();
                t_vurls.remove(this.adCacheImpressionHeader);
                if (t_vurls.size() == 0) {
                    this.hashtableCacheAdsImpressionHeader.remove(zoneId);
                } else if (t_vurls.size() > 1) {
                    this.hashtableCacheAdsImpressionHeader.put(zoneId, t_vurls);
                } else {
                    this.adCacheImpressionHeader = (String) t_vurls.firstElement();
                    this.hashtableCacheAdsImpressionHeader.put(zoneId, this.adCacheImpressionHeader);
                }
            } else {
                this.hashtableCacheAdsImpressionHeader.remove(zoneId);
            }
        }
    }

    protected void removeDownloadNetworkURL() {
        if (this.hashtableCacheAdsDownloadNetworkUrl != null && this.hashtableCacheAdsDownloadNetworkUrl.containsKey(zoneId)) {
            Object t_objurls = this.hashtableCacheAdsDownloadNetworkUrl.get(zoneId);
            if (t_objurls instanceof Vector) {
                Vector t_vurls = (Vector) t_objurls;
                t_vurls.remove(t_vurls.firstElement());
                if (t_vurls.size() == 0) {
                    this.hashtableCacheAdsDownloadNetworkUrl.remove(zoneId);
                } else if (t_vurls.size() > 1) {
                    this.hashtableCacheAdsDownloadNetworkUrl.put(zoneId, t_vurls);
                } else {
                    this.hashtableCacheAdsDownloadNetworkUrl.put(zoneId, t_vurls.firstElement());
                }
            } else {
                this.hashtableCacheAdsDownloadNetworkUrl.remove(zoneId);
            }
        }
    }

    protected void removePostActionURL() {
        if (this.hashtableCacheAdsPostActionUrl != null && this.hashtableCacheAdsPostActionUrl.containsKey(zoneId)) {
            Object t_objurls = this.hashtableCacheAdsPostActionUrl.get(zoneId);
            if (t_objurls instanceof Vector) {
                Vector t_vurls = (Vector) t_objurls;
                this.adCachePostActionUrl = (String) t_vurls.firstElement();
                t_vurls.remove(this.adCachePostActionUrl);
                if (t_vurls.size() == 0) {
                    this.hashtableCacheAdsPostActionUrl.remove(zoneId);
                } else if (t_vurls.size() > 1) {
                    this.hashtableCacheAdsPostActionUrl.put(zoneId, t_vurls);
                } else {
                    this.adCachePostActionUrl = (String) t_vurls.firstElement();
                    this.hashtableCacheAdsPostActionUrl.put(zoneId, this.adCachePostActionUrl);
                }
            } else {
                this.hashtableCacheAdsPostActionUrl.remove(zoneId);
            }
        }
    }

    public VservController renderAd(String zoneId, ViewGroup group) throws ViewNotEmptyException {
        zoneId = zoneId;
        try {
            Bundle bundle = getPreparedBundle();
            bundle.putString("cf", "1");
            bundle.putString("showAt", AnalyticsEvent.IN_APP);
            bundle.putString("refreshInterval", "yes");
            bundle.putBoolean("renderAd", true);
            this.renderAdController = new VservAdController(context, bundle, new AnonymousClass_3(group));
            if (group == null || group.getChildCount() > 0) {
                throw new ViewNotEmptyException("ViewGroup should be empty and non-null");
            }
            this.renderAdController.requestAddView();
            VservController controller = new VservController();
            controller.adController = this.renderAdController;
            return controller;
        } catch (Exception e) {
            Exception e2 = e;
            trackExceptions(e2, "renderAd");
            e2.printStackTrace();
            return null;
        }
    }

    protected void setAdType(Object t_adType) {
        if (t_adType != null) {
            if (this.hashtableCacheAdsType == null) {
                this.hashtableCacheAdsType = new Hashtable();
            }
            if (this.hashtableCacheAdsType.containsKey(zoneId)) {
                Object t_obj = this.hashtableCacheAdsType.get(zoneId);
                Vector t_v;
                if (t_obj instanceof Vector) {
                    t_v = (Vector) t_obj;
                    t_v.add(t_adType);
                    this.hashtableCacheAdsType.put(zoneId, t_v);
                } else {
                    t_v = new Vector();
                    if (t_obj instanceof String) {
                        t_v.add((String) t_obj);
                    } else if (t_obj instanceof Bundle) {
                        t_v.add((Bundle) t_obj);
                    }
                    t_v.add(t_adType);
                    this.hashtableCacheAdsType.put(zoneId, t_v);
                }
            } else {
                this.hashtableCacheAdsType.put(zoneId, t_adType);
            }
        }
    }

    protected void setAdUrl(String[] urls) {
        if (urls != null) {
            if (this.hashtableCacheAdsUrl == null) {
                this.hashtableCacheAdsUrl = new Hashtable();
            }
            if (this.hashtableCacheAdsUrl.containsKey(zoneId)) {
                Object t_obj = this.hashtableCacheAdsUrl.get(zoneId);
                Vector t_v;
                if (t_obj instanceof Vector) {
                    t_v = (Vector) t_obj;
                    t_v.add(urls);
                    this.hashtableCacheAdsUrl.put(zoneId, t_v);
                } else {
                    t_v = new Vector();
                    t_v.add((String[]) t_obj);
                    t_v.add(urls);
                    this.hashtableCacheAdsUrl.put(zoneId, t_v);
                }
            } else {
                this.hashtableCacheAdsUrl.put(zoneId, urls);
            }
        }
    }

    protected void setAdVideo(OrmmaPlayer t_ormmPlayer) {
        if (t_ormmPlayer != null) {
            if (hashtableCacheAdsVideo == null) {
                hashtableCacheAdsVideo = new Hashtable();
            }
            if (hashtableCacheAdsVideo.containsKey(zoneId)) {
                Object t_obj = hashtableCacheAdsVideo.get(zoneId);
                Vector t_v;
                if (t_obj instanceof Vector) {
                    t_v = (Vector) t_obj;
                    t_v.add(t_ormmPlayer);
                    hashtableCacheAdsVideo.put(zoneId, t_v);
                } else {
                    t_v = new Vector();
                    t_v.add((OrmmaPlayer) t_obj);
                    t_v.add(t_ormmPlayer);
                    hashtableCacheAdsVideo.put(zoneId, t_v);
                }
            } else {
                hashtableCacheAdsVideo.put(zoneId, t_ormmPlayer);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void setAdview(java.lang.Object r7_t_adobj) {
        throw new UnsupportedOperationException("Method not decompiled: mobi.vserv.android.ads.VservManager.setAdview(java.lang.Object):void");
        /*
        r6 = this;
        if (r7 == 0) goto L_0x0031;
    L_0x0002:
        r4 = r6.hashtableCacheAdsView;	 Catch:{ Exception -> 0x0045 }
        if (r4 != 0) goto L_0x000d;
    L_0x0006:
        r4 = new java.util.Hashtable;	 Catch:{ Exception -> 0x0045 }
        r4.<init>();	 Catch:{ Exception -> 0x0045 }
        r6.hashtableCacheAdsView = r4;	 Catch:{ Exception -> 0x0045 }
    L_0x000d:
        r4 = r6.hashtableCacheAdsView;	 Catch:{ Exception -> 0x0045 }
        r5 = zoneId;	 Catch:{ Exception -> 0x0045 }
        r4 = r4.containsKey(r5);	 Catch:{ Exception -> 0x0045 }
        if (r4 == 0) goto L_0x004a;
    L_0x0017:
        r4 = r6.hashtableCacheAdsView;	 Catch:{ Exception -> 0x0045 }
        r5 = zoneId;	 Catch:{ Exception -> 0x0045 }
        r2 = r4.get(r5);	 Catch:{ Exception -> 0x0045 }
        r4 = r2 instanceof java.util.Vector;	 Catch:{ Exception -> 0x0045 }
        if (r4 == 0) goto L_0x0032;
    L_0x0023:
        r0 = r2;
        r0 = (java.util.Vector) r0;	 Catch:{ Exception -> 0x0045 }
        r3 = r0;
        r3.add(r7);	 Catch:{ Exception -> 0x0045 }
        r4 = r6.hashtableCacheAdsView;	 Catch:{ Exception -> 0x0045 }
        r5 = zoneId;	 Catch:{ Exception -> 0x0045 }
        r4.put(r5, r3);	 Catch:{ Exception -> 0x0045 }
    L_0x0031:
        return;
    L_0x0032:
        r3 = new java.util.Vector;	 Catch:{ Exception -> 0x0045 }
        r3.<init>();	 Catch:{ Exception -> 0x0045 }
        r3.add(r2);	 Catch:{ Exception -> 0x0045 }
        r3.add(r7);	 Catch:{ Exception -> 0x0045 }
        r4 = r6.hashtableCacheAdsView;	 Catch:{ Exception -> 0x0045 }
        r5 = zoneId;	 Catch:{ Exception -> 0x0045 }
        r4.put(r5, r3);	 Catch:{ Exception -> 0x0045 }
        goto L_0x0031;
    L_0x0045:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0031;
    L_0x004a:
        r4 = r6.hashtableCacheAdsView;	 Catch:{ Exception -> 0x0045 }
        r5 = zoneId;	 Catch:{ Exception -> 0x0045 }
        r4.put(r5, r7);	 Catch:{ Exception -> 0x0045 }
        goto L_0x0031;
        */
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    protected void setCacheAdOrientation(String ori) {
        if (ori != null) {
            if (this.hashtableCacheAdsOrientation == null) {
                this.hashtableCacheAdsOrientation = new Hashtable();
            }
            if (this.hashtableCacheAdsOrientation.containsKey(zoneId)) {
                Object t_obj = this.hashtableCacheAdsOrientation.get(zoneId);
                Vector t_v;
                if (t_obj instanceof Vector) {
                    t_v = (Vector) t_obj;
                    t_v.add(ori);
                    this.hashtableCacheAdsOrientation.put(zoneId, t_v);
                } else {
                    t_v = new Vector();
                    t_v.add((String) t_obj);
                    t_v.add(ori);
                    this.hashtableCacheAdsOrientation.put(zoneId, t_v);
                }
            } else {
                this.hashtableCacheAdsOrientation.put(zoneId, ori);
            }
        }
    }

    protected void setCacheAdRequestId(String t_reqId) {
        if (this.hashtableCacheAdsRequestId == null) {
            this.hashtableCacheAdsRequestId = new Hashtable();
        }
        if (this.hashtableCacheAdsRequestId.containsKey(zoneId)) {
            Object t_obj = this.hashtableCacheAdsRequestId.get(zoneId);
            Vector t_v;
            if (t_obj instanceof Vector) {
                t_v = (Vector) t_obj;
                t_v.add(t_reqId);
                this.hashtableCacheAdsRequestId.put(zoneId, t_v);
            } else {
                t_v = new Vector();
                t_v.add((String) t_obj);
                t_v.add(t_reqId);
                this.hashtableCacheAdsRequestId.put(zoneId, t_v);
            }
        } else {
            this.hashtableCacheAdsRequestId.put(zoneId, t_reqId);
        }
    }

    protected void setCacheAdSkipDelay(int t_delay) {
        if (this.hashtableCacheAdsSkipDelay == null) {
            this.hashtableCacheAdsSkipDelay = new Hashtable();
        }
        if (this.hashtableCacheAdsSkipDelay.containsKey(zoneId)) {
            Object t_obj = this.hashtableCacheAdsSkipDelay.get(zoneId);
            Vector t_v;
            if (t_obj instanceof Vector) {
                t_v = (Vector) t_obj;
                t_v.add(Integer.valueOf(t_delay));
                this.hashtableCacheAdsSkipDelay.put(zoneId, t_v);
            } else {
                t_v = new Vector();
                t_v.add(t_obj);
                t_v.add(Integer.valueOf(t_delay));
                this.hashtableCacheAdsSkipDelay.put(zoneId, t_v);
            }
        } else {
            this.hashtableCacheAdsSkipDelay.put(zoneId, Integer.valueOf(t_delay));
        }
    }

    protected void setCacheImpression(String t_impressionheader) {
        if (t_impressionheader != null) {
            if (this.hashtableCacheAdsImpressionHeader == null) {
                this.hashtableCacheAdsImpressionHeader = new Hashtable();
            }
            if (this.hashtableCacheAdsImpressionHeader.containsKey(zoneId)) {
                Object t_obj = this.hashtableCacheAdsImpressionHeader.get(zoneId);
                Vector t_v;
                if (t_obj instanceof Vector) {
                    t_v = (Vector) t_obj;
                    t_v.add(t_impressionheader);
                    this.hashtableCacheAdsImpressionHeader.put(zoneId, t_v);
                } else {
                    t_v = new Vector();
                    t_v.add((String) t_obj);
                    t_v.add(t_impressionheader);
                    this.hashtableCacheAdsImpressionHeader.put(zoneId, t_v);
                }
            } else {
                this.hashtableCacheAdsImpressionHeader.put(zoneId, t_impressionheader);
            }
        }
    }

    public void setCacheNextAd(boolean cacheNextAd) {
        this.cacheNextAd = cacheNextAd;
        this.cacheShowAt = "mid";
    }

    public void setCacheNextAd(boolean cacheNextAd, AdPosition adPosition) {
        this.cacheNextAd = cacheNextAd;
        if (adPosition == null || adPosition == AdPosition.IN) {
            this.cacheShowAt = "mid";
        } else if (adPosition == AdPosition.START) {
            this.cacheShowAt = VservConstants.VPLAY0;
        } else if (adPosition == AdPosition.END) {
            this.cacheShowAt = "end";
        }
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    protected void setDownloadNetworkUrl(String urls) {
        if (urls != null) {
            if (this.hashtableCacheAdsDownloadNetworkUrl == null) {
                this.hashtableCacheAdsDownloadNetworkUrl = new Hashtable();
            }
            if (this.hashtableCacheAdsDownloadNetworkUrl.containsKey(zoneId)) {
                Object t_obj = this.hashtableCacheAdsDownloadNetworkUrl.get(zoneId);
                Vector t_v;
                if (t_obj instanceof Vector) {
                    t_v = (Vector) t_obj;
                    t_v.add(urls);
                    this.hashtableCacheAdsDownloadNetworkUrl.put(zoneId, t_v);
                } else {
                    t_v = new Vector();
                    t_v.add((String) t_obj);
                    t_v.add(urls);
                    this.hashtableCacheAdsDownloadNetworkUrl.put(zoneId, t_v);
                }
            } else {
                this.hashtableCacheAdsDownloadNetworkUrl.put(zoneId, urls);
            }
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(Gender adGender) {
        if (adGender == null) {
            return;
        }
        if (adGender == Gender.MALE) {
            this.gender = MMRequest.GENDER_MALE;
        } else if (adGender == Gender.FEMALE) {
            this.gender = MMRequest.GENDER_FEMALE;
        }
    }

    protected void setOrientation(String t_Orientation) {
        this.orientation = t_Orientation;
    }

    protected void setPostActionUrl(String urls) {
        if (urls != null) {
            if (this.hashtableCacheAdsPostActionUrl == null) {
                this.hashtableCacheAdsPostActionUrl = new Hashtable();
            }
            if (this.hashtableCacheAdsPostActionUrl.containsKey(zoneId)) {
                Object t_obj = this.hashtableCacheAdsPostActionUrl.get(zoneId);
                Vector t_v;
                if (t_obj instanceof Vector) {
                    t_v = (Vector) t_obj;
                    t_v.add(urls);
                    this.hashtableCacheAdsPostActionUrl.put(zoneId, t_v);
                } else {
                    t_v = new Vector();
                    t_v.add((String) t_obj);
                    t_v.add(urls);
                    this.hashtableCacheAdsPostActionUrl.put(zoneId, t_v);
                }
            } else {
                this.hashtableCacheAdsPostActionUrl.put(zoneId, urls);
            }
        }
    }

    public void setShowAt(AdPosition adPosition) {
        if (adPosition == null || adPosition == AdPosition.IN) {
            this.showAt = "mid";
        } else if (adPosition == AdPosition.START) {
            this.showAt = VservConstants.VPLAY0;
        } else if (adPosition == AdPosition.END) {
            this.showAt = "end";
        }
    }

    public void setStoreFrontId(String storeFrontId) {
        this.storeFrontId = storeFrontId;
    }

    protected void setZoneID(String t_zoneId) {
        zoneId = t_zoneId;
    }

    public void stopRefresh() {
        if (this.renderAdController != null) {
            this.renderAdController.stopRefresh();
        }
    }

    protected void trackExceptions(Exception e_obj, String extra_info) {
        new AnonymousClass_1(extra_info, e_obj).start();
    }
}