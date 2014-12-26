package mobi.vserv.android.ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.media.TransportMediator;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.facebook.ads.internal.AdWebViewUtils;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.wallet.WalletConstants;
import com.inmobi.androidsdk.IMBrowserActivity;
import com.inmobi.commons.ads.cache.AdDatabaseHelper;
import com.inmobi.commons.cache.ProductCacheConfig;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.isssss.myadv.dao.AdvConfigTable;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.GpsHelper;
import com.mopub.common.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import mobi.vserv.org.ormma.controller.Defines;
import mobi.vserv.org.ormma.controller.util.OrmmaPlayer;
import mobi.vserv.org.ormma.view.OrmmaView;

public class VservAdController implements VservConstants {
    private static final int adcomponentLayoutId = 456;
    private static final int bottomLabelId = 567;
    private static Hashtable<String, byte[]> cache = null;
    protected static boolean closeVservActivity = false;
    public static String showAt = null;
    protected static boolean sns = false;
    public static boolean snsPageLoaded = false;
    public static String snsPosition = null;
    private static final int topLayoutId = 123;
    protected static boolean vservAdManagerActivityForced;
    private RelativeLayout CacheLayout;
    protected String[] adCacheUrls;
    public OrmmaView adCacheView;
    private LayoutParams adCacheViewlayoutParams;
    public OrmmaView adComponentView;
    private LayoutParams adComponentViewlayoutParams;
    private boolean adDeliverdSuccess;
    protected int adHeight;
    protected boolean adLoaded;
    private String adOrientation;
    private boolean adStatus;
    private String adType;
    protected int adWidth;
    private String advertiserId;
    private String androidId;
    private String appId;
    private boolean applyOrientation;
    private Object cachedObject;
    private ImageView closeButtonImage;
    private String configUrl;
    protected long connectionStartTime;
    protected boolean connectionTimedOut;
    private boolean considerTimeout;
    private Context context;
    private SharedPreferences contextAdPreference;
    public VservCountDownTimer countDownTimer;
    public int currentSkipDelay;
    private boolean decodeResponse;
    private int default_refresh_rate;
    private String directVideo;
    private String domain;
    private String downloadNetworkUrl;
    protected boolean enableWaitingScreen;
    private File file;
    private String gameIdUrl;
    private String gpHit;
    private Handler handler;
    private int height;
    private IAddCallback iAddCallback;
    protected String impressionHeader;
    private String inventoryType;
    private boolean isCacheAdHit;
    protected boolean isCacheHitInitiated;
    protected boolean isCachingEnabled;
    private boolean isDisplayAdHit;
    private boolean isDisplayFrameAd;
    protected boolean isDisplayGetAdEnabled;
    private boolean isLimitAdTrackingEnabled;
    protected String isMediaCacheDisabled;
    private boolean isRenderAd;
    protected boolean isShowAdRequest;
    private boolean isShowSkipButton;
    protected boolean isadClicked;
    protected boolean isgetAdRequest;
    private String jsinterfaceobj;
    private boolean loadAdResources;
    protected boolean loadCachedAd;
    private RelativeLayout mainLayout;
    private LayoutParams mainlayoutParams;
    private boolean makeAdRequest;
    private Hashtable<String, String> notifyCacheUrls;
    private Hashtable<String, String> notifyUrls;
    private Object object;
    private String ormmaReady_Cache_file_contents;
    private String ormmaReady_file_contents;
    private int page;
    private final String post_domain;
    private final String pre_domain;
    private long proceedStartTime;
    private int proceedTimeInSeconds;
    private ProgressBar progressBar;
    private Handler refreshHandler;
    protected int refreshRate;
    private Runnable refreshRunnable;
    public String requestId;
    private String responseString;
    private boolean sendPresentScreenBroadcast;
    private int serverRefreshRate;
    protected boolean shouldRefresh;
    public boolean showskipDelay;
    public int skipDelay;
    private TextView skipLabel;
    private String snsWidgetUrl;
    protected boolean startThread;
    public int tempSkipDelay;
    private int timeOutInSeconds;
    private String trailerPart;
    private File universalContextFile;
    final Runnable updateResults;
    private String userAgent;
    private String viewMandatoryExitLabel;
    private String viewMandatoryMessage;
    private String viewMandatoryRetryLabel;
    private VservAdControllerInterface vservAdControllerInterface;
    private VservAdManager vservAdManageractivity;
    public Bundle vservConfigBundle;
    private WebView web;
    private int width;
    protected String zoneId;
    private String zoneType;
    protected float zoomFactor;

    class AnonymousClass_2 extends Thread {
        private final /* synthetic */ Context val$context;
        private final /* synthetic */ String val$fileName;

        AnonymousClass_2(Context context, String str) {
            this.val$context = context;
            this.val$fileName = str;
        }

        public void run() {
            try {
                String str = Preconditions.EMPTY_ARGUMENTS;
                str = Defines.readFileFromInternalStorage(this.val$context, this.val$fileName);
                if (str.trim().length() <= 0) {
                    InputStream inputStream;
                    if (Arrays.asList(this.val$context.getResources().getAssets().list(Preconditions.EMPTY_ARGUMENTS)).contains(this.val$fileName)) {
                        if (Defines.ENABLE_lOGGING) {
                            Log.i("asset", "File exists in assets so reading from there!");
                        }
                        inputStream = this.val$context.getResources().getAssets().open(this.val$fileName);
                    } else {
                        if (Defines.ENABLE_lOGGING) {
                            Log.i("asset", "File not exists in assets so reading from there!");
                        }
                        inputStream = getClass().getResourceAsStream(new StringBuilder(String.valueOf("/mobi/vserv/android/js/")).append(this.val$fileName).toString());
                    }
                    if (inputStream != null) {
                        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        while (true) {
                            int len = inputStream.read(buffer);
                            if (len == -1) {
                                break;
                            }
                            byteBuffer.write(buffer, 0, len);
                        }
                        str = new String(byteBuffer.toByteArray());
                        if (this.val$fileName.contains("vserv_ormmaReady_A-AN-2.0.1.js")) {
                            VservAdController.this.ormmaReady_file_contents = str;
                        }
                        if (this.val$fileName.contains("vserv_Cache_ormmaReady_A-AN-2.0.1.js")) {
                            VservAdController.this.ormmaReady_Cache_file_contents = str;
                        }
                        Defines.writeFileToInternalStorage(this.val$context, this.val$fileName, str);
                    }
                } else {
                    if (this.val$fileName.contains("vserv_ormmaReady_A-AN-2.0.1.js")) {
                        VservAdController.this.ormmaReady_file_contents = str;
                    }
                    if (this.val$fileName.contains("vserv_Cache_ormmaReady_A-AN-2.0.1.js")) {
                        VservAdController.this.ormmaReady_Cache_file_contents = str;
                    }
                }
            } catch (Exception e) {
                Exception exception = e;
                exception.printStackTrace();
                Log.i("vserv", new StringBuilder("Inside loadOrmmaJavaScriptFiles:: ").append(exception).toString());
            }
        }
    }

    public class SimpleTask extends TimerTask {
        public void run() {
            if (VservAdController.this.connectionStartTime != 0 && System.currentTimeMillis() - VservAdController.this.connectionStartTime > ((long) (VservAdController.this.timeOutInSeconds * 1000)) && VservAdController.this.page == 0) {
                VservAdController.this.connectionStartTime = 0;
                VservAdController.this.connectionTimedOut = true;
                VservAdController.this.proceedStartTime = System.currentTimeMillis();
                VservAdController.this.page = MMAdView.TRANSITION_UP;
                VservAdController.this.handler.post(VservAdController.this.updateResults);
            } else if (VservAdController.this.connectionTimedOut && VservAdController.this.proceedTimeInSeconds != -1 && VservAdController.this.proceedStartTime != 0 && System.currentTimeMillis() - VservAdController.this.proceedStartTime >= ((long) (VservAdController.this.proceedTimeInSeconds * 1000))) {
                VservAdController.this.proceedStartTime = 0;
                VservAdController.this.connectionStartTime = 0;
                VservAdController.this.connectionTimedOut = false;
                VservAdController.this.loadMainApp(IMBrowserActivity.INTERSTITIAL_ACTIVITY, false);
            }
        }
    }

    public class VservAdControllerInterface {
        private String convertToMD5(String str) {
            char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            String md5 = Preconditions.EMPTY_ARGUMENTS;
            try {
                byte[] strTemp = str.getBytes();
                MessageDigest mdTemp = MessageDigest.getInstance("MD5");
                mdTemp.update(strTemp);
                byte[] md = mdTemp.digest();
                int j = md.length;
                char[] str1 = new char[(j * 2)];
                int i = 0;
                int k = 0;
                while (i < j) {
                    byte b = md[i];
                    int k2 = k + 1;
                    str1[k] = hexDigits[(b >> 4) & 15];
                    k = k2 + 1;
                    str1[k2] = hexDigits[b & 15];
                    i++;
                }
                return new String(str1);
            } catch (Exception e) {
                e.printStackTrace();
                return md5;
            }
        }

        @JavascriptInterface
        public void SaveUrlsOnce() {
            if (VservAdController.this.adCacheUrls == null) {
                VservAdController.this.adCacheUrls = new String[VservAdController.this.notifyCacheUrls.size()];
            }
            if (VservAdController.this.adCacheUrls != null) {
                int i = 0;
                Iterator<String> urlSet = VservAdController.this.notifyCacheUrls.keySet().iterator();
                while (urlSet.hasNext()) {
                    VservAdController.this.adCacheUrls[i] = (String) urlSet.next();
                    if (Defines.ENABLE_lOGGING) {
                        Log.i("vserv", new StringBuilder("SaveUrlsOnce adCacheUrls[i]:: ").append(VservAdController.this.adCacheUrls[i]).toString());
                    }
                    i++;
                }
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", new StringBuilder("SaveUrlsOnce:: ").append(VservAdController.this.adCacheUrls.length).toString());
                }
                VservManager.getInstance(VservAdController.this.context).setAdUrl(VservAdController.this.adCacheUrls);
            }
            if (!VservAdController.this.isCacheHitInitiated) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv ", "GET AD response ");
                }
                ((Activity) VservAdController.this.context).runOnUiThread(new Runnable() {
                    public void run() {
                        if (mobi.vserv.android.ads.VservAdController.VservAdControllerInterface.this.this$0.iAddCallback != null) {
                            if (Defines.ENABLE_lOGGING) {
                                Log.i("vserv ", "SaveUrlsOnce send callback to dev ");
                            }
                            mobi.vserv.android.ads.VservAdController.VservAdControllerInterface.this.this$0.iAddCallback.onLoadSuccess(null);
                        }
                    }
                });
            }
        }

        @JavascriptInterface
        public void addNotifyUrlsToHashTable(String url) {
            if (!VservAdController.this.notifyUrls.containsKey(url)) {
                VservAdController.this.notifyUrls.put(url, "false");
            }
        }

        @JavascriptInterface
        public void addNotifyUrlsToHashTable(String url, String signature) {
            String clickUrlWithMD5Replaced = new StringBuilder(String.valueOf(url)).append("&ah=").append(VservAdController.this.urlEncode(convertToMD5(signature))).toString();
            if (!VservAdController.this.notifyUrls.containsKey(clickUrlWithMD5Replaced)) {
                VservAdController.this.notifyUrls.put(clickUrlWithMD5Replaced, "false");
            }
        }

        @JavascriptInterface
        public void notify(String url, String signature) {
            VservAdController.this.isadClicked = true;
            if (Defines.ENABLE_lOGGING) {
                Log.i("vserv", new StringBuilder("!!!Notifying Server for add click with url ").append(url).append(" and signature ").append(signature).toString());
            }
            VservAdController.this.makeHttpConnection(new StringBuilder(String.valueOf(url)).append("&ah=").append(URLEncoder.encode(convertToMD5(signature))).toString(), MMAdView.TRANSITION_UP);
        }

        @JavascriptInterface
        public void notifyOnce() {
            if (Defines.ENABLE_lOGGING) {
                Log.i("vserv", "Notify once got called:: ");
            }
            if (VservAdController.this.vservAdManageractivity != null && VservAdController.this.sendPresentScreenBroadcast) {
                VservAdController.this.sendPresentScreenBroadcast = false;
                VservAdController.this.context.sendBroadcast(new Intent("mobi.vserv.ad.present_screen"));
            }
            new Thread() {
                public void run() {
                    try {
                        if (Defines.ENABLE_lOGGING) {
                            Log.i("vserv", new StringBuilder("@@@@@@notifyUrls size is ").append(mobi.vserv.android.ads.VservAdController.VservAdControllerInterface.this.this$0.notifyUrls.size()).toString());
                        }
                        Iterator<String> urlSet = mobi.vserv.android.ads.VservAdController.VservAdControllerInterface.this.this$0.notifyUrls.keySet().iterator();
                        while (urlSet.hasNext()) {
                            String url = (String) urlSet.next();
                            if (mobi.vserv.android.ads.VservAdController.VservAdControllerInterface.this.this$0.notifyUrls.containsKey(url) && ((String) mobi.vserv.android.ads.VservAdController.VservAdControllerInterface.this.this$0.notifyUrls.get(url)).equals("false")) {
                                mobi.vserv.android.ads.VservAdController.VservAdControllerInterface.this.this$0.notifyUrls.put(url, "true");
                                if (Defines.ENABLE_lOGGING) {
                                    Log.i("video", "Notifying impression");
                                }
                                mobi.vserv.android.ads.VservAdController.VservAdControllerInterface.this.this$0.makeHttpConnection(url, MMAdView.TRANSITION_UP);
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }.start();
        }

        @JavascriptInterface
        public void populateCacheNotifyUrlsToHashTable(String url) {
            if (!VservAdController.this.notifyCacheUrls.containsKey(url)) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", new StringBuilder("populate and store url!!!!!!!!!!").append(url).toString());
                }
                VservAdController.this.notifyCacheUrls.put(url, "false");
            }
        }

        @JavascriptInterface
        public void showMsg(String msg) {
            if (Defines.ENABLE_lOGGING) {
                Log.i("vserv", new StringBuilder("showMsg is ").append(msg).toString());
            }
        }

        @JavascriptInterface
        public void showSkipPage() {
            if (VservAdController.this.isShowSkipButton) {
                VservAdController.this.isShowSkipButton = false;
                Log.i("vserv", "Inside JS showSkipPage ");
                VservAdController.this.page = ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES;
                VservAdController.this.handler.post(VservAdController.this.updateResults);
            }
        }
    }

    static {
        showAt = VservConstants.VPLAY0;
        vservAdManagerActivityForced = false;
        cache = new Hashtable();
        snsPageLoaded = false;
        sns = false;
        snsPosition = "1";
    }

    @SuppressLint({"InlinedApi"})
    public VservAdController(Context context) {
        this.pre_domain = "a.";
        this.post_domain = "vserv.mobi";
        this.domain = Preconditions.EMPTY_ARGUMENTS;
        this.notifyUrls = new Hashtable();
        this.notifyCacheUrls = new Hashtable();
        this.isCacheAdHit = false;
        this.isDisplayAdHit = false;
        this.isRenderAd = false;
        this.isDisplayFrameAd = false;
        this.connectionStartTime = 0;
        this.proceedStartTime = 0;
        this.configUrl = Preconditions.EMPTY_ARGUMENTS;
        this.zoneId = Preconditions.EMPTY_ARGUMENTS;
        this.width = 0;
        this.height = 0;
        this.adWidth = 0;
        this.adHeight = 0;
        this.isCacheHitInitiated = false;
        this.loadCachedAd = false;
        this.timeOutInSeconds = 20;
        this.proceedTimeInSeconds = -1;
        this.viewMandatoryMessage = "Data connection unavailable";
        this.viewMandatoryRetryLabel = "Retry";
        this.viewMandatoryExitLabel = "Exit";
        this.connectionTimedOut = false;
        this.makeAdRequest = false;
        this.isShowSkipButton = true;
        this.enableWaitingScreen = false;
        this.isCachingEnabled = false;
        this.isDisplayGetAdEnabled = false;
        this.snsWidgetUrl = "http://sns.vserv.mobi/sns/getWidget.php?";
        this.object = new Object();
        this.refreshRate = -1;
        this.serverRefreshRate = -1;
        this.default_refresh_rate = 60;
        this.shouldRefresh = false;
        this.adOrientation = "adaptive";
        this.adType = "billboard";
        this.gpHit = null;
        this.trailerPart = null;
        this.zoneType = "Billboard";
        this.inventoryType = "Application";
        this.directVideo = null;
        this.impressionHeader = null;
        this.isMediaCacheDisabled = null;
        this.decodeResponse = false;
        this.applyOrientation = false;
        this.sendPresentScreenBroadcast = true;
        this.ormmaReady_file_contents = Preconditions.EMPTY_ARGUMENTS;
        this.ormmaReady_Cache_file_contents = Preconditions.EMPTY_ARGUMENTS;
        this.advertiserId = null;
        this.isLimitAdTrackingEnabled = true;
        this.considerTimeout = false;
        this.jsinterfaceobj = Preconditions.EMPTY_ARGUMENTS;
        this.downloadNetworkUrl = null;
        this.countDownTimer = null;
        this.skipDelay = 0;
        this.currentSkipDelay = 0;
        this.tempSkipDelay = 0;
        this.requestId = null;
        this.showskipDelay = false;
        this.appId = Preconditions.EMPTY_ARGUMENTS;
        this.gameIdUrl = "http://in.sb.vserv.mobi/getGameId.php?";
        this.loadAdResources = false;
        this.file = null;
        this.updateResults = new Runnable() {
            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            @android.annotation.SuppressLint({"NewApi"})
            public void run() {
                throw new UnsupportedOperationException("Method not decompiled: mobi.vserv.android.ads.VservAdController.AnonymousClass_1.run():void");
                /*
                r28 = this;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x01a3;
            L_0x000c:
                r6 = new android.widget.RelativeLayout;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r6.<init>(r0);	 Catch:{ Exception -> 0x01c5 }
                r22 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
                r0 = r22;
                r6.setId(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0194;
            L_0x0030:
                r7 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22 = -1;
                r23 = -1;
                r0 = r22;
                r1 = r23;
                r7.<init>(r0, r1);	 Catch:{ Exception -> 0x01c5 }
            L_0x003d:
                r6.setLayoutParams(r7);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x00d6;
            L_0x004c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.ImageView;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r22.closeButtonImage = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.getResources();	 Catch:{ Exception -> 0x01c5 }
                r24 = "vserv_close_advertisement";
                r25 = "drawable";
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r26 = r26.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r26 = r26.getPackageName();	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.getIdentifier(r24, r25, r26);	 Catch:{ Exception -> 0x01c5 }
                r22.setImageResource(r23);	 Catch:{ Exception -> 0x01c5 }
                r3 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22 = -2;
                r23 = -2;
                r0 = r22;
                r1 = r23;
                r3.<init>(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r22 = 11;
                r0 = r22;
                r3.addRule(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r0.setLayoutParams(r3);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r6.addView(r0);	 Catch:{ Exception -> 0x01c5 }
            L_0x00d6:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.ProgressBar;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.context;	 Catch:{ Exception -> 0x01c5 }
                r25 = 0;
                r26 = 16842871; // 0x1010077 float:2.3693892E-38 double:8.321484E-317;
                r23.<init>(r24, r25, r26);	 Catch:{ Exception -> 0x01c5 }
                r22.progressBar = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r23 = 0;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
                r9 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22 = -2;
                r23 = -2;
                r0 = r22;
                r1 = r23;
                r9.<init>(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r22 = 13;
                r0 = r22;
                r9.addRule(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r0.setLayoutParams(r9);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r6.addView(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x014f;
            L_0x0140:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r0.setContentView(r6);	 Catch:{ Exception -> 0x01c5 }
            L_0x014f:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "refreshInterval";
                r22 = r22.containsKey(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0170;
            L_0x0163:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                r22.showProgressBar();	 Catch:{ Exception -> 0x01c5 }
            L_0x0170:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.makeAdRequest;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x017c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.makeAdRequest = r23;	 Catch:{ Exception -> 0x01c5 }
                r22 = new mobi.vserv.android.ads.VservAdController$1$1;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.start();	 Catch:{ Exception -> 0x01c5 }
            L_0x0193:
                return;
            L_0x0194:
                r7 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22 = -1;
                r23 = -2;
                r0 = r22;
                r1 = r23;
                r7.<init>(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x003d;
            L_0x01a3:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 1;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x01ee;
            L_0x01b5:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x01c5:
                r4 = move-exception;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;
                r22 = r0;
                r22 = r22.context;
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);
                r23 = "updateResultsRun";
                r0 = r22;
                r1 = r23;
                r0.trackExceptions(r4, r1);
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;
                r22 = r0;
                r23 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
                r24 = 0;
                r22.loadMainApp(r23, r24);
                r4.printStackTrace();
                goto L_0x0193;
            L_0x01ee:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 6;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x027e;
            L_0x0200:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x021b;
            L_0x020c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x021b:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0238;
            L_0x0229:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.clearView();	 Catch:{ Exception -> 0x01c5 }
            L_0x0238:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0263;
            L_0x0244:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.getVisibility();	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x0263;
            L_0x0254:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0263:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x026f:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                r22.removeAllViews();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x027e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x02aa;
            L_0x0290:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.isCacheAdHit;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x02aa;
            L_0x029c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.isCacheHitInitiated;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 != 0) goto L_0x02ca;
            L_0x02aa:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x0783;
            L_0x02bc:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.isgetAdRequest;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0783;
            L_0x02ca:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r0 = r23;
                r1 = r22;
                r1.isgetAdRequest = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.isCacheAdHit = r23;	 Catch:{ Exception -> 0x01c5 }
                r14 = 0;
                r15 = 0;
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0307;
            L_0x02e9:
                r22 = "vserv";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "&&&&&&&&SHOW_AD Called true:: ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.isCacheAdHit;	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0307:
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "start";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x058c;
            L_0x0311:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "startAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x0330:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 != 0) goto L_0x0500;
            L_0x033e:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0349;
            L_0x0342:
                r22 = "VservAdController";
                r23 = "Creating adCacheView";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0349:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x060d;
            L_0x0355:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new mobi.vserv.org.ormma.view.OrmmaView;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r26 = r26.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r27 = 1;
                r23.<init>(r24, r25, r26, r27);	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r22;
                r1.adCacheView = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r0 = r0.skipDelay;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r22.setSkipDelay(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x039d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x06e9;
            L_0x03a9:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                r23 = "Banner";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x06e9;
            L_0x03bb:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x064d;
            L_0x03c9:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x064d;
            L_0x03d7:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r24 = r24 * r25;
                r0 = r24;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r0 = r26;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r25 = r25 * r26;
                r0 = r25;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adCacheViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                r23 = "Billboard";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x044e;
            L_0x043f:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adCacheViewlayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r23 = 14;
                r22.addRule(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x044e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x047b;
            L_0x045a:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                r23 = "Banner";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x047b;
            L_0x046c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adCacheViewlayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r23 = 13;
                r22.addRule(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x047b:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.adCacheViewlayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22.setLayoutParams(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.getSettings();	 Catch:{ Exception -> 0x01c5 }
                r23 = 1;
                r22.setJavaScriptEnabled(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x04c7;
            L_0x04b5:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                r23 = "";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x04dd;
            L_0x04c7:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "VservAd";
                r22.setinterface(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "ControllerInterface";
                r22.setinterface(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x04dd:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.vservAdControllerInterface;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                r22.addJavascriptInterface(r23, r24);	 Catch:{ Exception -> 0x01c5 }
            L_0x0500:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.file;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.exists();	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x06fd;
            L_0x0510:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.file;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.ormmaReady_Cache_file_contents;	 Catch:{ Exception -> 0x01c5 }
                r22.loadFile(r23, r24);	 Catch:{ Exception -> 0x01c5 }
            L_0x0533:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.impressionHeader;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x075d;
            L_0x0541:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.impressionHeader;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "null-impression";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x075d;
            L_0x0555:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0579;
            L_0x0559:
                r22 = "vserv";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "inject 1:: ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.impressionHeader;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0579:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "var vservIsCachingEnabled=1";
                r22.injectJavaScript(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x058c:
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "mid";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x05b7;
            L_0x0596:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "midAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0330;
            L_0x05b7:
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "end";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x05e2;
            L_0x05c1:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "endAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0330;
            L_0x05e2:
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "inapp";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0330;
            L_0x05ec:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "inAppAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0330;
            L_0x060d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new mobi.vserv.org.ormma.view.OrmmaView;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.context;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r26 = 1;
                r23.<init>(r24, r25, r26);	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r22;
                r1.adCacheView = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r0 = r0.skipDelay;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r22.setSkipDelay(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x039d;
            L_0x064d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0691;
            L_0x065b:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r24 = r24 * r25;
                r0 = r24;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adCacheViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x044e;
            L_0x0691:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x06d5;
            L_0x069f:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r0 = r26;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r25 = r25 * r26;
                r0 = r25;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adCacheViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x044e;
            L_0x06d5:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adCacheViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x044e;
            L_0x06e9:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adCacheViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x044e;
            L_0x06fd:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0708;
            L_0x0701:
                r22 = "vserv";
                r23 = "file does not exists():: ";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0708:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0727;
            L_0x0716:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0727:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0742;
            L_0x0733:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 404; // 0x194 float:5.66E-43 double:1.996E-321;
                r24 = 0;
                r22.loadMainApp(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0533;
            L_0x0742:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x074e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                r22.onLoadFailure();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0533;
            L_0x075d:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x0761:
                r22 = "vserv";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "Not inject 1:: ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.impressionHeader;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x0783:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x07a1;
            L_0x0795:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.isDisplayAdHit;	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x07c1;
            L_0x07a1:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x17d1;
            L_0x07b3:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.isShowAdRequest;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x17d1;
            L_0x07c1:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r0 = r23;
                r1 = r22;
                r1.isShowAdRequest = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.isDisplayAdHit = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.cachedObject;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r0 = r0 instanceof android.webkit.WebView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0804;
            L_0x07ec:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.cachedObject;	 Catch:{ Exception -> 0x01c5 }
                r22 = (mobi.vserv.org.ormma.view.OrmmaView) r22;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r23;
                r1.adComponentView = r0;	 Catch:{ Exception -> 0x01c5 }
            L_0x0804:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0820;
            L_0x0812:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 1;
                r0 = r23;
                r1 = r22;
                r1.loadCachedAd = r0;	 Catch:{ Exception -> 0x01c5 }
            L_0x0820:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x08f6;
            L_0x082c:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0837;
            L_0x0830:
                r22 = "VservAdController";
                r23 = "Newly creating mainLayout";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0837:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.context;	 Catch:{ Exception -> 0x01c5 }
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r22.mainLayout = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0f5e;
            L_0x085b:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                r23 = "Banner";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0f5e;
            L_0x086d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0ec2;
            L_0x087b:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0ec2;
            L_0x0889:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r24 = r24 * r25;
                r0 = r24;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r0 = r26;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r25 = r25 * r26;
                r0 = r25;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.mainlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x08df:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.mainlayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22.setLayoutParams(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x08f6:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 != 0) goto L_0x1143;
            L_0x0904:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x090f;
            L_0x0908:
                r22 = "VservAdController";
                r23 = "Creating adComponentView";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x090f:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r0 = r0.skipDelay;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r1 = r22;
                r1.currentSkipDelay = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0f72;
            L_0x0933:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new mobi.vserv.org.ormma.view.OrmmaView;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r26 = r26.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r27 = 0;
                r23.<init>(r24, r25, r26, r27);	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r22;
                r1.adComponentView = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r0 = r0.currentSkipDelay;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r22.setSkipDelay(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x097b:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x099d;
            L_0x097f:
                r22 = "Vserv";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "@@@adType::: ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.adType;	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x099d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x104e;
            L_0x09a9:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                r23 = "Banner";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x104e;
            L_0x09bb:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0fb2;
            L_0x09c9:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0fb2;
            L_0x09d7:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r24 = r24 * r25;
                r0 = r24;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r0 = r26;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r25 = r25 * r26;
                r0 = r25;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adComponentViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x0a2d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.adComponentViewlayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22.setLayoutParams(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.requestFocus();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.getSettings();	 Catch:{ Exception -> 0x01c5 }
                r23 = 1;
                r22.setJavaScriptEnabled(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0a88;
            L_0x0a76:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                r23 = "";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0a9e;
            L_0x0a88:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "VservAd";
                r22.setinterface(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "ControllerInterface";
                r22.setinterface(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0a9e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.vservAdControllerInterface;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                r22.addJavascriptInterface(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "start";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1062;
            L_0x0acb:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "startAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x0aea:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 456; // 0x1c8 float:6.39E-43 double:2.253E-321;
                r22.setId(r23);	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0b1d;
            L_0x0aff:
                r22 = "network";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "loading file ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.ormmaReady_file_contents;	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0b1d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.file;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.exists();	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x10e3;
            L_0x0b2d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.file;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.ormmaReady_file_contents;	 Catch:{ Exception -> 0x01c5 }
                r22.loadFile(r23, r24);	 Catch:{ Exception -> 0x01c5 }
            L_0x0b50:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0b7d;
            L_0x0b5c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                r23 = "Banner";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x0b7d;
            L_0x0b6e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adComponentViewlayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r23 = 13;
                r22.addRule(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0b7d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0ba8;
            L_0x0b89:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.getVisibility();	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x0ba8;
            L_0x0b99:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0ba8:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x14db;
            L_0x0bb4:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0bbf;
            L_0x0bb8:
                r22 = "VservAdController";
                r23 = "Setting Content View ";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0bbf:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r22.addView(r23);	 Catch:{ Exception -> 0x01c5 }
                r20 = new android.widget.RelativeLayout;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r0 = r20;
                r1 = r22;
                r0.<init>(r1);	 Catch:{ Exception -> 0x01c5 }
                r22 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
                r0 = r20;
                r1 = r22;
                r0.setId(r1);	 Catch:{ Exception -> 0x01c5 }
                r21 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22 = -1;
                r23 = -2;
                r21.<init>(r22, r23);	 Catch:{ Exception -> 0x01c5 }
                r20.setLayoutParams(r21);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.TextView;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r22.skipLabel = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.skipLabel;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.skipLabel;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.getResources();	 Catch:{ Exception -> 0x01c5 }
                r24 = "vserv_skipdelay";
                r25 = "drawable";
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r26 = r26.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r26 = r26.getPackageName();	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.getIdentifier(r24, r25, r26);	 Catch:{ Exception -> 0x01c5 }
                r22.setBackgroundResource(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.skipLabel;	 Catch:{ Exception -> 0x01c5 }
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "Skip in";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.currentSkipDelay;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                r22.setText(r23);	 Catch:{ Exception -> 0x01c5 }
                r10 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22 = -2;
                r23 = -2;
                r0 = r22;
                r1 = r23;
                r10.<init>(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r22 = 11;
                r0 = r22;
                r10.addRule(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.skipLabel;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r0.setLayoutParams(r10);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.skipLabel;	 Catch:{ Exception -> 0x01c5 }
                r22.addView(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.ImageView;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r22.closeButtonImage = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.getResources();	 Catch:{ Exception -> 0x01c5 }
                r24 = "vserv_close_advertisement";
                r25 = "drawable";
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r26 = r26.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r26 = r26.getPackageName();	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.getIdentifier(r24, r25, r26);	 Catch:{ Exception -> 0x01c5 }
                r22.setImageResource(r23);	 Catch:{ Exception -> 0x01c5 }
                r3 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22 = -2;
                r23 = -2;
                r0 = r22;
                r1 = r23;
                r3.<init>(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r22 = 11;
                r0 = r22;
                r3.addRule(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r0.setLayoutParams(r3);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r0 = r20;
                r1 = r22;
                r0.addView(r1);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r20;
                r0.addView(r1);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.applyOrientation;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0de7;
            L_0x0d63:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.loadCachedAd;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x134a;
            L_0x0d71:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.getCacheADOrientation();	 Catch:{ Exception -> 0x01c5 }
                r22.adOrientation = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.removeCacheADOrientation();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r22;
                r1.adOrientation = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x130c;
            L_0x0db7:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.trim();	 Catch:{ Exception -> 0x01c5 }
                r23 = "landscape";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x130c;
            L_0x0dcd:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23 = 0;
                r22.setRequestedOrientation(r23);	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0de7;
            L_0x0de0:
                r22 = "orientation";
                r23 = "saving appOrientation landscape";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0de7:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                r22.setContentView(r23);	 Catch:{ Exception -> 0x01c5 }
                r22 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x01c5 }
                r23 = 9;
                r0 = r22;
                r1 = r23;
                if (r0 <= r1) goto L_0x0e18;
            L_0x0e08:
                r22 = new android.os.StrictMode$ThreadPolicy$Builder;	 Catch:{ Exception -> 0x01c5 }
                r22.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.permitAll();	 Catch:{ Exception -> 0x01c5 }
                r8 = r22.build();	 Catch:{ Exception -> 0x01c5 }
                android.os.StrictMode.setThreadPolicy(r8);	 Catch:{ Exception -> 0x01c5 }
            L_0x0e18:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.loadCachedAd;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0e71;
            L_0x0e26:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.notifyOnceCache();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.showSkipPageCache();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);	 Catch:{ Exception -> 0x01c5 }
                r16 = r22.getAdType();	 Catch:{ Exception -> 0x01c5 }
                if (r16 == 0) goto L_0x0e71;
            L_0x0e4c:
                r0 = r16;
                r0 = r0 instanceof java.lang.String;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x1457;
            L_0x0e54:
                r22 = "video";
                r0 = r16;
                r1 = r22;
                r22 = r0.equals(r1);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x142d;
            L_0x0e60:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.playVideoImpl(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0e71:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "cacheNextAd";
                r22 = r22.containsKey(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0ea4;
            L_0x0e85:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "cacheNextAd";
                r22 = r22.getBoolean(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0ea4;
            L_0x0e99:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 1;
                r22.isCacheAdHit = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x0ea4:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = r22.object;	 Catch:{ Exception -> 0x01c5 }
                monitor-enter(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ all -> 0x0ebf }
                r22 = r0;
                r22 = r22.object;	 Catch:{ all -> 0x0ebf }
                r22.notify();	 Catch:{ all -> 0x0ebf }
                monitor-exit(r23);	 Catch:{ all -> 0x0ebf }
                goto L_0x0193;
            L_0x0ebf:
                r22 = move-exception;
                monitor-exit(r23);	 Catch:{ all -> 0x0ebf }
                throw r22;	 Catch:{ Exception -> 0x01c5 }
            L_0x0ec2:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0f06;
            L_0x0ed0:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r24 = r24 * r25;
                r0 = r24;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.mainlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x08df;
            L_0x0f06:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0f4a;
            L_0x0f14:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r0 = r26;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r25 = r25 * r26;
                r0 = r25;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.mainlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x08df;
            L_0x0f4a:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.mainlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x08df;
            L_0x0f5e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.mainlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x08df;
            L_0x0f72:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new mobi.vserv.org.ormma.view.OrmmaView;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.context;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r26 = 0;
                r23.<init>(r24, r25, r26);	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r22;
                r1.adComponentView = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r0 = r0.currentSkipDelay;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r22.setSkipDelay(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x097b;
            L_0x0fb2:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0ff6;
            L_0x0fc0:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r24 = r24 * r25;
                r0 = r24;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adComponentViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0a2d;
            L_0x0ff6:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x103a;
            L_0x1004:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r0 = r26;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r25 = r25 * r26;
                r0 = r25;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adComponentViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0a2d;
            L_0x103a:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adComponentViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0a2d;
            L_0x104e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adComponentViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0a2d;
            L_0x1062:
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "mid";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x108d;
            L_0x106c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "midAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0aea;
            L_0x108d:
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "end";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x10b8;
            L_0x1097:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "endAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0aea;
            L_0x10b8:
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "inapp";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0aea;
            L_0x10c2:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "inAppAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0aea;
            L_0x10e3:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x10ee;
            L_0x10e7:
                r22 = "vserv";
                r23 = "file does not exists():: ";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x10ee:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x110d;
            L_0x10fc:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x110d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1128;
            L_0x1119:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 404; // 0x194 float:5.66E-43 double:1.996E-321;
                r24 = 0;
                r22.loadMainApp(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0b50;
            L_0x1128:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x1134:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                r22.onLoadFailure();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0b50;
            L_0x1143:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.getCacheADSkipDelay();	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r22;
                r1.currentSkipDelay = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.removeCacheADSkipDelay();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.getCacheAdRequestId();	 Catch:{ Exception -> 0x01c5 }
                r22 = (java.lang.String) r22;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r23;
                r1.requestId = r0;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x11a6;
            L_0x1186:
                r22 = "vserv";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "requestId:: ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.requestId;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x11a6:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.removeCacheADRequestId();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x12cc;
            L_0x11bb:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r25 = r25.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r22.setCacheViewActivity(r23, r24, r25);	 Catch:{ Exception -> 0x01c5 }
            L_0x11e4:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);	 Catch:{ Exception -> 0x01c5 }
                r13 = r22.getCacheImpression();	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1210;
            L_0x11fa:
                r22 = "VservAdController";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "Already Creating adComponentView:: ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r23 = r0.append(r13);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x1210:
                if (r13 == 0) goto L_0x12ed;
            L_0x1212:
                r22 = "null-impression";
                r0 = r22;
                r22 = r13.equals(r0);	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x12ed;
            L_0x121c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0.injectJavaScript(r13);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);	 Catch:{ Exception -> 0x01c5 }
                r22.removeCacheImpressionHeader();	 Catch:{ Exception -> 0x01c5 }
            L_0x123e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.requestFocus();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.getSettings();	 Catch:{ Exception -> 0x01c5 }
                r23 = 1;
                r22.setJavaScriptEnabled(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1280;
            L_0x126e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                r23 = "";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1296;
            L_0x1280:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "VservAd";
                r22.setinterface(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "ControllerInterface";
                r22.setinterface(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x1296:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.vservAdControllerInterface;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                r22.addJavascriptInterface(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 456; // 0x1c8 float:6.39E-43 double:2.253E-321;
                r22.setId(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0b7d;
            L_0x12cc:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.context;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r22.setCacheView(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x11e4;
            L_0x12ed:
                if (r13 == 0) goto L_0x123e;
            L_0x12ef:
                r22 = "null-impression";
                r0 = r22;
                r22 = r13.equals(r0);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x123e;
            L_0x12f9:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);	 Catch:{ Exception -> 0x01c5 }
                r22.removeCacheImpressionHeader();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x123e;
            L_0x130c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0de7;
            L_0x1318:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.trim();	 Catch:{ Exception -> 0x01c5 }
                r23 = "portrait";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0de7;
            L_0x132e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23 = 1;
                r22.setRequestedOrientation(r23);	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0de7;
            L_0x1341:
                r22 = "orientation";
                r23 = "saving appOrientation portrait";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0de7;
            L_0x134a:
                r22 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x01c5 }
                r23 = 11;
                r0 = r22;
                r1 = r23;
                if (r0 < r1) goto L_0x13c9;
            L_0x1354:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r23 = "vserv_orientation";
                r24 = 4;
                r22 = r22.getSharedPreferences(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                r5 = r22.edit();	 Catch:{ Exception -> 0x01c5 }
            L_0x136a:
                r22 = r5.clear();	 Catch:{ Exception -> 0x01c5 }
                r22.commit();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.applyOrientation = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x13e0;
            L_0x1388:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.trim();	 Catch:{ Exception -> 0x01c5 }
                r23 = "landscape";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x13e0;
            L_0x139e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23 = 0;
                r22.setRequestedOrientation(r23);	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x13b8;
            L_0x13b1:
                r22 = "orientation";
                r23 = "saving appOrientation landscape";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x13b8:
                r22 = "orientation";
                r23 = "landscape";
                r0 = r22;
                r1 = r23;
                r22 = r5.putString(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r22.commit();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0de7;
            L_0x13c9:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r23 = "vserv_orientation";
                r24 = 0;
                r22 = r22.getSharedPreferences(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                r5 = r22.edit();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x136a;
            L_0x13e0:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0de7;
            L_0x13ec:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.trim();	 Catch:{ Exception -> 0x01c5 }
                r23 = "portrait";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0de7;
            L_0x1402:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23 = 1;
                r22.setRequestedOrientation(r23);	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x141c;
            L_0x1415:
                r22 = "orientation";
                r23 = "saving appOrientation portrait";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x141c:
                r22 = "orientation";
                r23 = "portrait";
                r0 = r22;
                r1 = r23;
                r22 = r5.putString(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r22.commit();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0de7;
            L_0x142d:
                r22 = "audio";
                r0 = r16;
                r1 = r22;
                r22 = r0.equals(r1);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x144c;
            L_0x1439:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.playAudioImpl(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0e71;
            L_0x144c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.removeCacheAdType();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0e71;
            L_0x1457:
                r0 = r16;
                r0 = r0 instanceof android.os.Bundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0e71;
            L_0x145f:
                r0 = r16;
                r0 = (android.os.Bundle) r0;	 Catch:{ Exception -> 0x01c5 }
                r18 = r0;
                r22 = "action";
                r0 = r18;
                r1 = r22;
                r22 = r0.get(r1);	 Catch:{ Exception -> 0x01c5 }
                r12 = r22.toString();	 Catch:{ Exception -> 0x01c5 }
                if (r12 == 0) goto L_0x0e71;
            L_0x1475:
                r17 = "PLAY";
                r22 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r23 = java.lang.String.valueOf(r17);	 Catch:{ Exception -> 0x01c5 }
                r22.<init>(r23);	 Catch:{ Exception -> 0x01c5 }
                r23 = "_VIDEO";
                r22 = r22.append(r23);	 Catch:{ Exception -> 0x01c5 }
                r17 = r22.toString();	 Catch:{ Exception -> 0x01c5 }
                r11 = "PLAY";
                r22 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r23 = java.lang.String.valueOf(r11);	 Catch:{ Exception -> 0x01c5 }
                r22.<init>(r23);	 Catch:{ Exception -> 0x01c5 }
                r23 = "_AUDIO";
                r22 = r22.append(r23);	 Catch:{ Exception -> 0x01c5 }
                r11 = r22.toString();	 Catch:{ Exception -> 0x01c5 }
                r0 = r17;
                r22 = r12.equals(r0);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x14be;
            L_0x14a7:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r16 = (android.os.Bundle) r16;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r16;
                r0.playVideoFullScreen(r1);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0e71;
            L_0x14be:
                r22 = r12.equals(r11);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0e71;
            L_0x14c4:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r16 = (android.os.Bundle) r16;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r16;
                r0.playAudioFullScreenImpl(r1);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0e71;
            L_0x14db:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r22.onLoadSuccess(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.isDisplayFrameAd;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1533;
            L_0x1500:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "cacheNextAd";
                r22 = r22.containsKey(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1533;
            L_0x1514:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "cacheNextAd";
                r22 = r22.getBoolean(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1533;
            L_0x1528:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 1;
                r22.isCacheAdHit = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x1533:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.loadCachedAd;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x158c;
            L_0x1541:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.notifyOnceCache();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.showSkipPageCache();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);	 Catch:{ Exception -> 0x01c5 }
                r16 = r22.getAdType();	 Catch:{ Exception -> 0x01c5 }
                if (r16 == 0) goto L_0x158c;
            L_0x1567:
                r0 = r16;
                r0 = r0 instanceof java.lang.String;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x1725;
            L_0x156f:
                r22 = "video";
                r0 = r16;
                r1 = r22;
                r22 = r0.equals(r1);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x16fb;
            L_0x157b:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.playVideoImpl(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x158c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = r22.object;	 Catch:{ Exception -> 0x01c5 }
                monitor-enter(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ all -> 0x17a9 }
                r22 = r0;
                r22 = r22.object;	 Catch:{ all -> 0x17a9 }
                r22.notify();	 Catch:{ all -> 0x17a9 }
                monitor-exit(r23);	 Catch:{ all -> 0x17a9 }
                r19 = "refreshInterval";
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "refreshInterval";
                r22 = r22.containsKey(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x15bb:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r1 = r19;
                r22 = r0.getString(r1);	 Catch:{ Exception -> 0x01c5 }
                r23 = "yes";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x15d7:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.refreshRate;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = -1;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x1613;
            L_0x15eb:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.serverRefreshRate;	 Catch:{ Exception -> 0x01c5 }
                r24 = -1;
                r0 = r22;
                r1 = r24;
                if (r0 != r1) goto L_0x17ac;
            L_0x1603:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.default_refresh_rate;	 Catch:{ Exception -> 0x01c5 }
            L_0x160d:
                r0 = r22;
                r1 = r23;
                r1.refreshRate = r0;	 Catch:{ Exception -> 0x01c5 }
            L_0x1613:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.refreshRate;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0193;
            L_0x1621:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x164b;
            L_0x1625:
                r22 = "VservAdController";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "Will try to get new ad after ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.refreshRate;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r24 = " seconds ";
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x164b:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.refreshRunnable;	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x1669;
            L_0x1657:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new mobi.vserv.android.ads.VservAdController$1$2;	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.refreshRunnable = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x1669:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.refreshHandler;	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x17b8;
            L_0x1675:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.os.Handler;	 Catch:{ Exception -> 0x01c5 }
                r23.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.refreshHandler = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x1683:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.shouldRefresh;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0193;
            L_0x1691:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x16cb;
            L_0x1695:
                r22 = "abc";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "Will try to get new Ad after ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.refreshRate;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r24 = " seconds!! for zoneId ";
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.zoneId;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x16cb:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.refreshHandler;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.refreshRunnable;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.refreshRate;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0 * 1000;
                r24 = r0;
                r0 = r24;
                r0 = (long) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r22.postDelayed(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x16fb:
                r22 = "audio";
                r0 = r16;
                r1 = r22;
                r22 = r0.equals(r1);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x171a;
            L_0x1707:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.playAudioImpl(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x158c;
            L_0x171a:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.removeCacheAdType();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x158c;
            L_0x1725:
                r0 = r16;
                r0 = r0 instanceof android.os.Bundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x158c;
            L_0x172d:
                r0 = r16;
                r0 = (android.os.Bundle) r0;	 Catch:{ Exception -> 0x01c5 }
                r18 = r0;
                r22 = "action";
                r0 = r18;
                r1 = r22;
                r22 = r0.get(r1);	 Catch:{ Exception -> 0x01c5 }
                r12 = r22.toString();	 Catch:{ Exception -> 0x01c5 }
                if (r12 == 0) goto L_0x158c;
            L_0x1743:
                r17 = "PLAY";
                r22 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r23 = java.lang.String.valueOf(r17);	 Catch:{ Exception -> 0x01c5 }
                r22.<init>(r23);	 Catch:{ Exception -> 0x01c5 }
                r23 = "_VIDEO";
                r22 = r22.append(r23);	 Catch:{ Exception -> 0x01c5 }
                r17 = r22.toString();	 Catch:{ Exception -> 0x01c5 }
                r11 = "PLAY";
                r22 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r23 = java.lang.String.valueOf(r11);	 Catch:{ Exception -> 0x01c5 }
                r22.<init>(r23);	 Catch:{ Exception -> 0x01c5 }
                r23 = "_AUDIO";
                r22 = r22.append(r23);	 Catch:{ Exception -> 0x01c5 }
                r11 = r22.toString();	 Catch:{ Exception -> 0x01c5 }
                r0 = r17;
                r22 = r12.equals(r0);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x178c;
            L_0x1775:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r16 = (android.os.Bundle) r16;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r16;
                r0.playVideoFullScreen(r1);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x158c;
            L_0x178c:
                r22 = r12.equals(r11);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x158c;
            L_0x1792:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r16 = (android.os.Bundle) r16;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r16;
                r0.playAudioFullScreenImpl(r1);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x158c;
            L_0x17a9:
                r22 = move-exception;
                monitor-exit(r23);	 Catch:{ all -> 0x17a9 }
                throw r22;	 Catch:{ Exception -> 0x01c5 }
            L_0x17ac:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.serverRefreshRate;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x160d;
            L_0x17b8:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.refreshHandler;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.refreshRunnable;	 Catch:{ Exception -> 0x01c5 }
                r22.removeCallbacks(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x1683;
            L_0x17d1:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 5;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x183e;
            L_0x17e3:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = -1;
                r22.page = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x17fa:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1826;
            L_0x1806:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.currentSkipDelay;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = -1;
                r0 = r22;
                r1 = r23;
                if (r0 == r1) goto L_0x1826;
            L_0x181a:
                r22 = new mobi.vserv.android.ads.VservAdController$1$3;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.start();	 Catch:{ Exception -> 0x01c5 }
            L_0x1826:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = new mobi.vserv.android.ads.VservAdController$1$4;	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.setOnClickListener(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x183e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 8;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x18ad;
            L_0x1850:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x185c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1895;
            L_0x1868:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.isShown();	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x1895;
            L_0x1878:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r0 = r23;
                r1 = r22;
                r1.currentSkipDelay = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = 0;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x1895:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = new mobi.vserv.android.ads.VservAdController$1$5;	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.setOnClickListener(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x18ad:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 2;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x192a;
            L_0x18bf:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x18ca;
            L_0x18c3:
                r22 = "sample";
                r23 = "Will try to show cancel button";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x18ca:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x18ee;
            L_0x18d6:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                r22.TimeOutOccured();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 1;
                r22.considerTimeout = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x18ee:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x18fa:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1912;
            L_0x1906:
                r22 = new mobi.vserv.android.ads.VservAdController$1$6;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.start();	 Catch:{ Exception -> 0x01c5 }
            L_0x1912:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = new mobi.vserv.android.ads.VservAdController$1$7;	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.setOnClickListener(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x192a:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 3;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x1965;
            L_0x193c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x1948:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x1954:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x1965:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 7;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x0193;
            L_0x1977:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x1983:
                r2 = new android.app.AlertDialog$Builder;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r2.<init>(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.viewMandatoryMessage;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r2.setMessage(r0);	 Catch:{ Exception -> 0x01c5 }
                r22 = 0;
                r0 = r22;
                r2.setCancelable(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.viewMandatoryRetryLabel;	 Catch:{ Exception -> 0x01c5 }
                r23 = new mobi.vserv.android.ads.VservAdController$1$8;	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r23;
                r2.setPositiveButton(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.viewMandatoryExitLabel;	 Catch:{ Exception -> 0x01c5 }
                r23 = new mobi.vserv.android.ads.VservAdController$1$9;	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r23;
                r2.setNegativeButton(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r2.show();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = -1;
                r22.page = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
                */
            }
        };
        this.responseString = Preconditions.EMPTY_ARGUMENTS;
        this.adDeliverdSuccess = false;
        if (this.domain != null && this.domain.equals(Preconditions.EMPTY_ARGUMENTS)) {
            setDomain("a.");
            setDomain("vserv.mobi");
        }
        this.context = context;
    }

    @SuppressLint({"InlinedApi"})
    public VservAdController(Context context, Bundle vservConfigBundle, IAddCallback iAddCallback) {
        this.pre_domain = "a.";
        this.post_domain = "vserv.mobi";
        this.domain = Preconditions.EMPTY_ARGUMENTS;
        this.notifyUrls = new Hashtable();
        this.notifyCacheUrls = new Hashtable();
        this.isCacheAdHit = false;
        this.isDisplayAdHit = false;
        this.isRenderAd = false;
        this.isDisplayFrameAd = false;
        this.connectionStartTime = 0;
        this.proceedStartTime = 0;
        this.configUrl = Preconditions.EMPTY_ARGUMENTS;
        this.zoneId = Preconditions.EMPTY_ARGUMENTS;
        this.width = 0;
        this.height = 0;
        this.adWidth = 0;
        this.adHeight = 0;
        this.isCacheHitInitiated = false;
        this.loadCachedAd = false;
        this.timeOutInSeconds = 20;
        this.proceedTimeInSeconds = -1;
        this.viewMandatoryMessage = "Data connection unavailable";
        this.viewMandatoryRetryLabel = "Retry";
        this.viewMandatoryExitLabel = "Exit";
        this.connectionTimedOut = false;
        this.makeAdRequest = false;
        this.isShowSkipButton = true;
        this.enableWaitingScreen = false;
        this.isCachingEnabled = false;
        this.isDisplayGetAdEnabled = false;
        this.snsWidgetUrl = "http://sns.vserv.mobi/sns/getWidget.php?";
        this.object = new Object();
        this.refreshRate = -1;
        this.serverRefreshRate = -1;
        this.default_refresh_rate = 60;
        this.shouldRefresh = false;
        this.adOrientation = "adaptive";
        this.adType = "billboard";
        this.gpHit = null;
        this.trailerPart = null;
        this.zoneType = "Billboard";
        this.inventoryType = "Application";
        this.directVideo = null;
        this.impressionHeader = null;
        this.isMediaCacheDisabled = null;
        this.decodeResponse = false;
        this.applyOrientation = false;
        this.sendPresentScreenBroadcast = true;
        this.ormmaReady_file_contents = Preconditions.EMPTY_ARGUMENTS;
        this.ormmaReady_Cache_file_contents = Preconditions.EMPTY_ARGUMENTS;
        this.advertiserId = null;
        this.isLimitAdTrackingEnabled = true;
        this.considerTimeout = false;
        this.jsinterfaceobj = Preconditions.EMPTY_ARGUMENTS;
        this.downloadNetworkUrl = null;
        this.countDownTimer = null;
        this.skipDelay = 0;
        this.currentSkipDelay = 0;
        this.tempSkipDelay = 0;
        this.requestId = null;
        this.showskipDelay = false;
        this.appId = Preconditions.EMPTY_ARGUMENTS;
        this.gameIdUrl = "http://in.sb.vserv.mobi/getGameId.php?";
        this.loadAdResources = false;
        this.file = null;
        this.updateResults = new Runnable() {
            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            @android.annotation.SuppressLint({"NewApi"})
            public void run() {
                throw new UnsupportedOperationException("Method not decompiled: mobi.vserv.android.ads.VservAdController.AnonymousClass_1.run():void");
                /*
                r28 = this;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x01a3;
            L_0x000c:
                r6 = new android.widget.RelativeLayout;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r6.<init>(r0);	 Catch:{ Exception -> 0x01c5 }
                r22 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
                r0 = r22;
                r6.setId(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0194;
            L_0x0030:
                r7 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22 = -1;
                r23 = -1;
                r0 = r22;
                r1 = r23;
                r7.<init>(r0, r1);	 Catch:{ Exception -> 0x01c5 }
            L_0x003d:
                r6.setLayoutParams(r7);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x00d6;
            L_0x004c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.ImageView;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r22.closeButtonImage = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.getResources();	 Catch:{ Exception -> 0x01c5 }
                r24 = "vserv_close_advertisement";
                r25 = "drawable";
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r26 = r26.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r26 = r26.getPackageName();	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.getIdentifier(r24, r25, r26);	 Catch:{ Exception -> 0x01c5 }
                r22.setImageResource(r23);	 Catch:{ Exception -> 0x01c5 }
                r3 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22 = -2;
                r23 = -2;
                r0 = r22;
                r1 = r23;
                r3.<init>(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r22 = 11;
                r0 = r22;
                r3.addRule(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r0.setLayoutParams(r3);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r6.addView(r0);	 Catch:{ Exception -> 0x01c5 }
            L_0x00d6:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.ProgressBar;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.context;	 Catch:{ Exception -> 0x01c5 }
                r25 = 0;
                r26 = 16842871; // 0x1010077 float:2.3693892E-38 double:8.321484E-317;
                r23.<init>(r24, r25, r26);	 Catch:{ Exception -> 0x01c5 }
                r22.progressBar = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r23 = 0;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
                r9 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22 = -2;
                r23 = -2;
                r0 = r22;
                r1 = r23;
                r9.<init>(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r22 = 13;
                r0 = r22;
                r9.addRule(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r0.setLayoutParams(r9);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r6.addView(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x014f;
            L_0x0140:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r0.setContentView(r6);	 Catch:{ Exception -> 0x01c5 }
            L_0x014f:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "refreshInterval";
                r22 = r22.containsKey(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0170;
            L_0x0163:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                r22.showProgressBar();	 Catch:{ Exception -> 0x01c5 }
            L_0x0170:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.makeAdRequest;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x017c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.makeAdRequest = r23;	 Catch:{ Exception -> 0x01c5 }
                r22 = new mobi.vserv.android.ads.VservAdController$1$1;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.start();	 Catch:{ Exception -> 0x01c5 }
            L_0x0193:
                return;
            L_0x0194:
                r7 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22 = -1;
                r23 = -2;
                r0 = r22;
                r1 = r23;
                r7.<init>(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x003d;
            L_0x01a3:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 1;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x01ee;
            L_0x01b5:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x01c5:
                r4 = move-exception;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;
                r22 = r0;
                r22 = r22.context;
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);
                r23 = "updateResultsRun";
                r0 = r22;
                r1 = r23;
                r0.trackExceptions(r4, r1);
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;
                r22 = r0;
                r23 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
                r24 = 0;
                r22.loadMainApp(r23, r24);
                r4.printStackTrace();
                goto L_0x0193;
            L_0x01ee:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 6;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x027e;
            L_0x0200:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x021b;
            L_0x020c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x021b:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0238;
            L_0x0229:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.clearView();	 Catch:{ Exception -> 0x01c5 }
            L_0x0238:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0263;
            L_0x0244:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.getVisibility();	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x0263;
            L_0x0254:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0263:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x026f:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                r22.removeAllViews();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x027e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x02aa;
            L_0x0290:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.isCacheAdHit;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x02aa;
            L_0x029c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.isCacheHitInitiated;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 != 0) goto L_0x02ca;
            L_0x02aa:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x0783;
            L_0x02bc:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.isgetAdRequest;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0783;
            L_0x02ca:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r0 = r23;
                r1 = r22;
                r1.isgetAdRequest = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.isCacheAdHit = r23;	 Catch:{ Exception -> 0x01c5 }
                r14 = 0;
                r15 = 0;
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0307;
            L_0x02e9:
                r22 = "vserv";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "&&&&&&&&SHOW_AD Called true:: ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.isCacheAdHit;	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0307:
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "start";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x058c;
            L_0x0311:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "startAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x0330:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 != 0) goto L_0x0500;
            L_0x033e:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0349;
            L_0x0342:
                r22 = "VservAdController";
                r23 = "Creating adCacheView";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0349:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x060d;
            L_0x0355:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new mobi.vserv.org.ormma.view.OrmmaView;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r26 = r26.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r27 = 1;
                r23.<init>(r24, r25, r26, r27);	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r22;
                r1.adCacheView = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r0 = r0.skipDelay;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r22.setSkipDelay(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x039d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x06e9;
            L_0x03a9:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                r23 = "Banner";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x06e9;
            L_0x03bb:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x064d;
            L_0x03c9:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x064d;
            L_0x03d7:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r24 = r24 * r25;
                r0 = r24;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r0 = r26;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r25 = r25 * r26;
                r0 = r25;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adCacheViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                r23 = "Billboard";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x044e;
            L_0x043f:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adCacheViewlayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r23 = 14;
                r22.addRule(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x044e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x047b;
            L_0x045a:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                r23 = "Banner";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x047b;
            L_0x046c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adCacheViewlayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r23 = 13;
                r22.addRule(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x047b:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.adCacheViewlayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22.setLayoutParams(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.getSettings();	 Catch:{ Exception -> 0x01c5 }
                r23 = 1;
                r22.setJavaScriptEnabled(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x04c7;
            L_0x04b5:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                r23 = "";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x04dd;
            L_0x04c7:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "VservAd";
                r22.setinterface(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "ControllerInterface";
                r22.setinterface(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x04dd:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.vservAdControllerInterface;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                r22.addJavascriptInterface(r23, r24);	 Catch:{ Exception -> 0x01c5 }
            L_0x0500:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.file;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.exists();	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x06fd;
            L_0x0510:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.file;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.ormmaReady_Cache_file_contents;	 Catch:{ Exception -> 0x01c5 }
                r22.loadFile(r23, r24);	 Catch:{ Exception -> 0x01c5 }
            L_0x0533:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.impressionHeader;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x075d;
            L_0x0541:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.impressionHeader;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "null-impression";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x075d;
            L_0x0555:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0579;
            L_0x0559:
                r22 = "vserv";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "inject 1:: ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.impressionHeader;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0579:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "var vservIsCachingEnabled=1";
                r22.injectJavaScript(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x058c:
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "mid";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x05b7;
            L_0x0596:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "midAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0330;
            L_0x05b7:
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "end";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x05e2;
            L_0x05c1:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "endAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0330;
            L_0x05e2:
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "inapp";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0330;
            L_0x05ec:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "inAppAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0330;
            L_0x060d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new mobi.vserv.org.ormma.view.OrmmaView;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.context;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r26 = 1;
                r23.<init>(r24, r25, r26);	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r22;
                r1.adCacheView = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r0 = r0.skipDelay;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r22.setSkipDelay(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x039d;
            L_0x064d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0691;
            L_0x065b:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r24 = r24 * r25;
                r0 = r24;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adCacheViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x044e;
            L_0x0691:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x06d5;
            L_0x069f:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r0 = r26;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r25 = r25 * r26;
                r0 = r25;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adCacheViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x044e;
            L_0x06d5:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adCacheViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x044e;
            L_0x06e9:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adCacheViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x044e;
            L_0x06fd:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0708;
            L_0x0701:
                r22 = "vserv";
                r23 = "file does not exists():: ";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0708:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0727;
            L_0x0716:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0727:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0742;
            L_0x0733:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 404; // 0x194 float:5.66E-43 double:1.996E-321;
                r24 = 0;
                r22.loadMainApp(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0533;
            L_0x0742:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x074e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                r22.onLoadFailure();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0533;
            L_0x075d:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x0761:
                r22 = "vserv";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "Not inject 1:: ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.impressionHeader;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x0783:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x07a1;
            L_0x0795:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.isDisplayAdHit;	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x07c1;
            L_0x07a1:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x17d1;
            L_0x07b3:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.isShowAdRequest;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x17d1;
            L_0x07c1:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r0 = r23;
                r1 = r22;
                r1.isShowAdRequest = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.isDisplayAdHit = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.cachedObject;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r0 = r0 instanceof android.webkit.WebView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0804;
            L_0x07ec:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.cachedObject;	 Catch:{ Exception -> 0x01c5 }
                r22 = (mobi.vserv.org.ormma.view.OrmmaView) r22;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r23;
                r1.adComponentView = r0;	 Catch:{ Exception -> 0x01c5 }
            L_0x0804:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0820;
            L_0x0812:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 1;
                r0 = r23;
                r1 = r22;
                r1.loadCachedAd = r0;	 Catch:{ Exception -> 0x01c5 }
            L_0x0820:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x08f6;
            L_0x082c:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0837;
            L_0x0830:
                r22 = "VservAdController";
                r23 = "Newly creating mainLayout";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0837:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.context;	 Catch:{ Exception -> 0x01c5 }
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r22.mainLayout = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0f5e;
            L_0x085b:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                r23 = "Banner";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0f5e;
            L_0x086d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0ec2;
            L_0x087b:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0ec2;
            L_0x0889:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r24 = r24 * r25;
                r0 = r24;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r0 = r26;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r25 = r25 * r26;
                r0 = r25;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.mainlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x08df:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.mainlayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22.setLayoutParams(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x08f6:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 != 0) goto L_0x1143;
            L_0x0904:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x090f;
            L_0x0908:
                r22 = "VservAdController";
                r23 = "Creating adComponentView";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x090f:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r0 = r0.skipDelay;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r1 = r22;
                r1.currentSkipDelay = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0f72;
            L_0x0933:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new mobi.vserv.org.ormma.view.OrmmaView;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r26 = r26.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r27 = 0;
                r23.<init>(r24, r25, r26, r27);	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r22;
                r1.adComponentView = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r0 = r0.currentSkipDelay;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r22.setSkipDelay(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x097b:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x099d;
            L_0x097f:
                r22 = "Vserv";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "@@@adType::: ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.adType;	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x099d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x104e;
            L_0x09a9:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                r23 = "Banner";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x104e;
            L_0x09bb:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0fb2;
            L_0x09c9:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0fb2;
            L_0x09d7:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r24 = r24 * r25;
                r0 = r24;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r0 = r26;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r25 = r25 * r26;
                r0 = r25;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adComponentViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x0a2d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.adComponentViewlayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22.setLayoutParams(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.requestFocus();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.getSettings();	 Catch:{ Exception -> 0x01c5 }
                r23 = 1;
                r22.setJavaScriptEnabled(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0a88;
            L_0x0a76:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                r23 = "";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0a9e;
            L_0x0a88:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "VservAd";
                r22.setinterface(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "ControllerInterface";
                r22.setinterface(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0a9e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.vservAdControllerInterface;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                r22.addJavascriptInterface(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "start";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1062;
            L_0x0acb:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "startAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x0aea:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 456; // 0x1c8 float:6.39E-43 double:2.253E-321;
                r22.setId(r23);	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0b1d;
            L_0x0aff:
                r22 = "network";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "loading file ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.ormmaReady_file_contents;	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0b1d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.file;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.exists();	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x10e3;
            L_0x0b2d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.file;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.ormmaReady_file_contents;	 Catch:{ Exception -> 0x01c5 }
                r22.loadFile(r23, r24);	 Catch:{ Exception -> 0x01c5 }
            L_0x0b50:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0b7d;
            L_0x0b5c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                r23 = "Banner";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x0b7d;
            L_0x0b6e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adComponentViewlayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r23 = 13;
                r22.addRule(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0b7d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0ba8;
            L_0x0b89:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.getVisibility();	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x0ba8;
            L_0x0b99:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0ba8:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x14db;
            L_0x0bb4:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0bbf;
            L_0x0bb8:
                r22 = "VservAdController";
                r23 = "Setting Content View ";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0bbf:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r22.addView(r23);	 Catch:{ Exception -> 0x01c5 }
                r20 = new android.widget.RelativeLayout;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r0 = r20;
                r1 = r22;
                r0.<init>(r1);	 Catch:{ Exception -> 0x01c5 }
                r22 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
                r0 = r20;
                r1 = r22;
                r0.setId(r1);	 Catch:{ Exception -> 0x01c5 }
                r21 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22 = -1;
                r23 = -2;
                r21.<init>(r22, r23);	 Catch:{ Exception -> 0x01c5 }
                r20.setLayoutParams(r21);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.TextView;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r22.skipLabel = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.skipLabel;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.skipLabel;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.getResources();	 Catch:{ Exception -> 0x01c5 }
                r24 = "vserv_skipdelay";
                r25 = "drawable";
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r26 = r26.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r26 = r26.getPackageName();	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.getIdentifier(r24, r25, r26);	 Catch:{ Exception -> 0x01c5 }
                r22.setBackgroundResource(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.skipLabel;	 Catch:{ Exception -> 0x01c5 }
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "Skip in";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.currentSkipDelay;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                r22.setText(r23);	 Catch:{ Exception -> 0x01c5 }
                r10 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22 = -2;
                r23 = -2;
                r0 = r22;
                r1 = r23;
                r10.<init>(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r22 = 11;
                r0 = r22;
                r10.addRule(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.skipLabel;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r0.setLayoutParams(r10);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.skipLabel;	 Catch:{ Exception -> 0x01c5 }
                r22.addView(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.ImageView;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r22.closeButtonImage = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.getResources();	 Catch:{ Exception -> 0x01c5 }
                r24 = "vserv_close_advertisement";
                r25 = "drawable";
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r26 = r26.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r26 = r26.getPackageName();	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.getIdentifier(r24, r25, r26);	 Catch:{ Exception -> 0x01c5 }
                r22.setImageResource(r23);	 Catch:{ Exception -> 0x01c5 }
                r3 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22 = -2;
                r23 = -2;
                r0 = r22;
                r1 = r23;
                r3.<init>(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r22 = 11;
                r0 = r22;
                r3.addRule(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r0.setLayoutParams(r3);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r0 = r20;
                r1 = r22;
                r0.addView(r1);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r20;
                r0.addView(r1);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.applyOrientation;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0de7;
            L_0x0d63:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.loadCachedAd;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x134a;
            L_0x0d71:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.getCacheADOrientation();	 Catch:{ Exception -> 0x01c5 }
                r22.adOrientation = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.removeCacheADOrientation();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r22;
                r1.adOrientation = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x130c;
            L_0x0db7:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.trim();	 Catch:{ Exception -> 0x01c5 }
                r23 = "landscape";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x130c;
            L_0x0dcd:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23 = 0;
                r22.setRequestedOrientation(r23);	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0de7;
            L_0x0de0:
                r22 = "orientation";
                r23 = "saving appOrientation landscape";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0de7:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                r22.setContentView(r23);	 Catch:{ Exception -> 0x01c5 }
                r22 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x01c5 }
                r23 = 9;
                r0 = r22;
                r1 = r23;
                if (r0 <= r1) goto L_0x0e18;
            L_0x0e08:
                r22 = new android.os.StrictMode$ThreadPolicy$Builder;	 Catch:{ Exception -> 0x01c5 }
                r22.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.permitAll();	 Catch:{ Exception -> 0x01c5 }
                r8 = r22.build();	 Catch:{ Exception -> 0x01c5 }
                android.os.StrictMode.setThreadPolicy(r8);	 Catch:{ Exception -> 0x01c5 }
            L_0x0e18:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.loadCachedAd;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0e71;
            L_0x0e26:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.notifyOnceCache();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.showSkipPageCache();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);	 Catch:{ Exception -> 0x01c5 }
                r16 = r22.getAdType();	 Catch:{ Exception -> 0x01c5 }
                if (r16 == 0) goto L_0x0e71;
            L_0x0e4c:
                r0 = r16;
                r0 = r0 instanceof java.lang.String;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x1457;
            L_0x0e54:
                r22 = "video";
                r0 = r16;
                r1 = r22;
                r22 = r0.equals(r1);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x142d;
            L_0x0e60:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.playVideoImpl(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0e71:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "cacheNextAd";
                r22 = r22.containsKey(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0ea4;
            L_0x0e85:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "cacheNextAd";
                r22 = r22.getBoolean(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0ea4;
            L_0x0e99:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 1;
                r22.isCacheAdHit = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x0ea4:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = r22.object;	 Catch:{ Exception -> 0x01c5 }
                monitor-enter(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ all -> 0x0ebf }
                r22 = r0;
                r22 = r22.object;	 Catch:{ all -> 0x0ebf }
                r22.notify();	 Catch:{ all -> 0x0ebf }
                monitor-exit(r23);	 Catch:{ all -> 0x0ebf }
                goto L_0x0193;
            L_0x0ebf:
                r22 = move-exception;
                monitor-exit(r23);	 Catch:{ all -> 0x0ebf }
                throw r22;	 Catch:{ Exception -> 0x01c5 }
            L_0x0ec2:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0f06;
            L_0x0ed0:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r24 = r24 * r25;
                r0 = r24;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.mainlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x08df;
            L_0x0f06:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0f4a;
            L_0x0f14:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r0 = r26;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r25 = r25 * r26;
                r0 = r25;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.mainlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x08df;
            L_0x0f4a:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.mainlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x08df;
            L_0x0f5e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.mainlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x08df;
            L_0x0f72:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new mobi.vserv.org.ormma.view.OrmmaView;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.context;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r26 = 0;
                r23.<init>(r24, r25, r26);	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r22;
                r1.adComponentView = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r0 = r0.currentSkipDelay;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r22.setSkipDelay(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x097b;
            L_0x0fb2:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0ff6;
            L_0x0fc0:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r24 = r24 * r25;
                r0 = r24;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adComponentViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0a2d;
            L_0x0ff6:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x103a;
            L_0x1004:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r0 = r26;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r25 = r25 * r26;
                r0 = r25;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adComponentViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0a2d;
            L_0x103a:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adComponentViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0a2d;
            L_0x104e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adComponentViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0a2d;
            L_0x1062:
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "mid";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x108d;
            L_0x106c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "midAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0aea;
            L_0x108d:
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "end";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x10b8;
            L_0x1097:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "endAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0aea;
            L_0x10b8:
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "inapp";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0aea;
            L_0x10c2:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "inAppAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0aea;
            L_0x10e3:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x10ee;
            L_0x10e7:
                r22 = "vserv";
                r23 = "file does not exists():: ";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x10ee:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x110d;
            L_0x10fc:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x110d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1128;
            L_0x1119:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 404; // 0x194 float:5.66E-43 double:1.996E-321;
                r24 = 0;
                r22.loadMainApp(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0b50;
            L_0x1128:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x1134:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                r22.onLoadFailure();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0b50;
            L_0x1143:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.getCacheADSkipDelay();	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r22;
                r1.currentSkipDelay = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.removeCacheADSkipDelay();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.getCacheAdRequestId();	 Catch:{ Exception -> 0x01c5 }
                r22 = (java.lang.String) r22;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r23;
                r1.requestId = r0;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x11a6;
            L_0x1186:
                r22 = "vserv";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "requestId:: ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.requestId;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x11a6:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.removeCacheADRequestId();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x12cc;
            L_0x11bb:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r25 = r25.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r22.setCacheViewActivity(r23, r24, r25);	 Catch:{ Exception -> 0x01c5 }
            L_0x11e4:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);	 Catch:{ Exception -> 0x01c5 }
                r13 = r22.getCacheImpression();	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1210;
            L_0x11fa:
                r22 = "VservAdController";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "Already Creating adComponentView:: ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r23 = r0.append(r13);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x1210:
                if (r13 == 0) goto L_0x12ed;
            L_0x1212:
                r22 = "null-impression";
                r0 = r22;
                r22 = r13.equals(r0);	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x12ed;
            L_0x121c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0.injectJavaScript(r13);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);	 Catch:{ Exception -> 0x01c5 }
                r22.removeCacheImpressionHeader();	 Catch:{ Exception -> 0x01c5 }
            L_0x123e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.requestFocus();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.getSettings();	 Catch:{ Exception -> 0x01c5 }
                r23 = 1;
                r22.setJavaScriptEnabled(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1280;
            L_0x126e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                r23 = "";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1296;
            L_0x1280:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "VservAd";
                r22.setinterface(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "ControllerInterface";
                r22.setinterface(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x1296:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.vservAdControllerInterface;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                r22.addJavascriptInterface(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 456; // 0x1c8 float:6.39E-43 double:2.253E-321;
                r22.setId(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0b7d;
            L_0x12cc:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.context;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r22.setCacheView(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x11e4;
            L_0x12ed:
                if (r13 == 0) goto L_0x123e;
            L_0x12ef:
                r22 = "null-impression";
                r0 = r22;
                r22 = r13.equals(r0);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x123e;
            L_0x12f9:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);	 Catch:{ Exception -> 0x01c5 }
                r22.removeCacheImpressionHeader();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x123e;
            L_0x130c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0de7;
            L_0x1318:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.trim();	 Catch:{ Exception -> 0x01c5 }
                r23 = "portrait";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0de7;
            L_0x132e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23 = 1;
                r22.setRequestedOrientation(r23);	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0de7;
            L_0x1341:
                r22 = "orientation";
                r23 = "saving appOrientation portrait";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0de7;
            L_0x134a:
                r22 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x01c5 }
                r23 = 11;
                r0 = r22;
                r1 = r23;
                if (r0 < r1) goto L_0x13c9;
            L_0x1354:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r23 = "vserv_orientation";
                r24 = 4;
                r22 = r22.getSharedPreferences(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                r5 = r22.edit();	 Catch:{ Exception -> 0x01c5 }
            L_0x136a:
                r22 = r5.clear();	 Catch:{ Exception -> 0x01c5 }
                r22.commit();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.applyOrientation = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x13e0;
            L_0x1388:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.trim();	 Catch:{ Exception -> 0x01c5 }
                r23 = "landscape";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x13e0;
            L_0x139e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23 = 0;
                r22.setRequestedOrientation(r23);	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x13b8;
            L_0x13b1:
                r22 = "orientation";
                r23 = "saving appOrientation landscape";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x13b8:
                r22 = "orientation";
                r23 = "landscape";
                r0 = r22;
                r1 = r23;
                r22 = r5.putString(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r22.commit();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0de7;
            L_0x13c9:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r23 = "vserv_orientation";
                r24 = 0;
                r22 = r22.getSharedPreferences(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                r5 = r22.edit();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x136a;
            L_0x13e0:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0de7;
            L_0x13ec:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.trim();	 Catch:{ Exception -> 0x01c5 }
                r23 = "portrait";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0de7;
            L_0x1402:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23 = 1;
                r22.setRequestedOrientation(r23);	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x141c;
            L_0x1415:
                r22 = "orientation";
                r23 = "saving appOrientation portrait";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x141c:
                r22 = "orientation";
                r23 = "portrait";
                r0 = r22;
                r1 = r23;
                r22 = r5.putString(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r22.commit();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0de7;
            L_0x142d:
                r22 = "audio";
                r0 = r16;
                r1 = r22;
                r22 = r0.equals(r1);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x144c;
            L_0x1439:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.playAudioImpl(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0e71;
            L_0x144c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.removeCacheAdType();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0e71;
            L_0x1457:
                r0 = r16;
                r0 = r0 instanceof android.os.Bundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0e71;
            L_0x145f:
                r0 = r16;
                r0 = (android.os.Bundle) r0;	 Catch:{ Exception -> 0x01c5 }
                r18 = r0;
                r22 = "action";
                r0 = r18;
                r1 = r22;
                r22 = r0.get(r1);	 Catch:{ Exception -> 0x01c5 }
                r12 = r22.toString();	 Catch:{ Exception -> 0x01c5 }
                if (r12 == 0) goto L_0x0e71;
            L_0x1475:
                r17 = "PLAY";
                r22 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r23 = java.lang.String.valueOf(r17);	 Catch:{ Exception -> 0x01c5 }
                r22.<init>(r23);	 Catch:{ Exception -> 0x01c5 }
                r23 = "_VIDEO";
                r22 = r22.append(r23);	 Catch:{ Exception -> 0x01c5 }
                r17 = r22.toString();	 Catch:{ Exception -> 0x01c5 }
                r11 = "PLAY";
                r22 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r23 = java.lang.String.valueOf(r11);	 Catch:{ Exception -> 0x01c5 }
                r22.<init>(r23);	 Catch:{ Exception -> 0x01c5 }
                r23 = "_AUDIO";
                r22 = r22.append(r23);	 Catch:{ Exception -> 0x01c5 }
                r11 = r22.toString();	 Catch:{ Exception -> 0x01c5 }
                r0 = r17;
                r22 = r12.equals(r0);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x14be;
            L_0x14a7:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r16 = (android.os.Bundle) r16;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r16;
                r0.playVideoFullScreen(r1);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0e71;
            L_0x14be:
                r22 = r12.equals(r11);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0e71;
            L_0x14c4:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r16 = (android.os.Bundle) r16;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r16;
                r0.playAudioFullScreenImpl(r1);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0e71;
            L_0x14db:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r22.onLoadSuccess(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.isDisplayFrameAd;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1533;
            L_0x1500:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "cacheNextAd";
                r22 = r22.containsKey(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1533;
            L_0x1514:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "cacheNextAd";
                r22 = r22.getBoolean(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1533;
            L_0x1528:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 1;
                r22.isCacheAdHit = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x1533:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.loadCachedAd;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x158c;
            L_0x1541:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.notifyOnceCache();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.showSkipPageCache();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);	 Catch:{ Exception -> 0x01c5 }
                r16 = r22.getAdType();	 Catch:{ Exception -> 0x01c5 }
                if (r16 == 0) goto L_0x158c;
            L_0x1567:
                r0 = r16;
                r0 = r0 instanceof java.lang.String;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x1725;
            L_0x156f:
                r22 = "video";
                r0 = r16;
                r1 = r22;
                r22 = r0.equals(r1);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x16fb;
            L_0x157b:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.playVideoImpl(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x158c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = r22.object;	 Catch:{ Exception -> 0x01c5 }
                monitor-enter(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ all -> 0x17a9 }
                r22 = r0;
                r22 = r22.object;	 Catch:{ all -> 0x17a9 }
                r22.notify();	 Catch:{ all -> 0x17a9 }
                monitor-exit(r23);	 Catch:{ all -> 0x17a9 }
                r19 = "refreshInterval";
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "refreshInterval";
                r22 = r22.containsKey(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x15bb:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r1 = r19;
                r22 = r0.getString(r1);	 Catch:{ Exception -> 0x01c5 }
                r23 = "yes";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x15d7:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.refreshRate;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = -1;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x1613;
            L_0x15eb:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.serverRefreshRate;	 Catch:{ Exception -> 0x01c5 }
                r24 = -1;
                r0 = r22;
                r1 = r24;
                if (r0 != r1) goto L_0x17ac;
            L_0x1603:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.default_refresh_rate;	 Catch:{ Exception -> 0x01c5 }
            L_0x160d:
                r0 = r22;
                r1 = r23;
                r1.refreshRate = r0;	 Catch:{ Exception -> 0x01c5 }
            L_0x1613:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.refreshRate;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0193;
            L_0x1621:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x164b;
            L_0x1625:
                r22 = "VservAdController";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "Will try to get new ad after ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.refreshRate;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r24 = " seconds ";
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x164b:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.refreshRunnable;	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x1669;
            L_0x1657:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new mobi.vserv.android.ads.VservAdController$1$2;	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.refreshRunnable = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x1669:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.refreshHandler;	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x17b8;
            L_0x1675:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.os.Handler;	 Catch:{ Exception -> 0x01c5 }
                r23.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.refreshHandler = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x1683:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.shouldRefresh;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0193;
            L_0x1691:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x16cb;
            L_0x1695:
                r22 = "abc";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "Will try to get new Ad after ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.refreshRate;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r24 = " seconds!! for zoneId ";
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.zoneId;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x16cb:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.refreshHandler;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.refreshRunnable;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.refreshRate;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0 * 1000;
                r24 = r0;
                r0 = r24;
                r0 = (long) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r22.postDelayed(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x16fb:
                r22 = "audio";
                r0 = r16;
                r1 = r22;
                r22 = r0.equals(r1);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x171a;
            L_0x1707:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.playAudioImpl(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x158c;
            L_0x171a:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.removeCacheAdType();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x158c;
            L_0x1725:
                r0 = r16;
                r0 = r0 instanceof android.os.Bundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x158c;
            L_0x172d:
                r0 = r16;
                r0 = (android.os.Bundle) r0;	 Catch:{ Exception -> 0x01c5 }
                r18 = r0;
                r22 = "action";
                r0 = r18;
                r1 = r22;
                r22 = r0.get(r1);	 Catch:{ Exception -> 0x01c5 }
                r12 = r22.toString();	 Catch:{ Exception -> 0x01c5 }
                if (r12 == 0) goto L_0x158c;
            L_0x1743:
                r17 = "PLAY";
                r22 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r23 = java.lang.String.valueOf(r17);	 Catch:{ Exception -> 0x01c5 }
                r22.<init>(r23);	 Catch:{ Exception -> 0x01c5 }
                r23 = "_VIDEO";
                r22 = r22.append(r23);	 Catch:{ Exception -> 0x01c5 }
                r17 = r22.toString();	 Catch:{ Exception -> 0x01c5 }
                r11 = "PLAY";
                r22 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r23 = java.lang.String.valueOf(r11);	 Catch:{ Exception -> 0x01c5 }
                r22.<init>(r23);	 Catch:{ Exception -> 0x01c5 }
                r23 = "_AUDIO";
                r22 = r22.append(r23);	 Catch:{ Exception -> 0x01c5 }
                r11 = r22.toString();	 Catch:{ Exception -> 0x01c5 }
                r0 = r17;
                r22 = r12.equals(r0);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x178c;
            L_0x1775:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r16 = (android.os.Bundle) r16;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r16;
                r0.playVideoFullScreen(r1);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x158c;
            L_0x178c:
                r22 = r12.equals(r11);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x158c;
            L_0x1792:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r16 = (android.os.Bundle) r16;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r16;
                r0.playAudioFullScreenImpl(r1);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x158c;
            L_0x17a9:
                r22 = move-exception;
                monitor-exit(r23);	 Catch:{ all -> 0x17a9 }
                throw r22;	 Catch:{ Exception -> 0x01c5 }
            L_0x17ac:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.serverRefreshRate;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x160d;
            L_0x17b8:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.refreshHandler;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.refreshRunnable;	 Catch:{ Exception -> 0x01c5 }
                r22.removeCallbacks(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x1683;
            L_0x17d1:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 5;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x183e;
            L_0x17e3:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = -1;
                r22.page = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x17fa:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1826;
            L_0x1806:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.currentSkipDelay;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = -1;
                r0 = r22;
                r1 = r23;
                if (r0 == r1) goto L_0x1826;
            L_0x181a:
                r22 = new mobi.vserv.android.ads.VservAdController$1$3;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.start();	 Catch:{ Exception -> 0x01c5 }
            L_0x1826:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = new mobi.vserv.android.ads.VservAdController$1$4;	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.setOnClickListener(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x183e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 8;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x18ad;
            L_0x1850:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x185c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1895;
            L_0x1868:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.isShown();	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x1895;
            L_0x1878:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r0 = r23;
                r1 = r22;
                r1.currentSkipDelay = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = 0;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x1895:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = new mobi.vserv.android.ads.VservAdController$1$5;	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.setOnClickListener(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x18ad:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 2;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x192a;
            L_0x18bf:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x18ca;
            L_0x18c3:
                r22 = "sample";
                r23 = "Will try to show cancel button";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x18ca:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x18ee;
            L_0x18d6:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                r22.TimeOutOccured();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 1;
                r22.considerTimeout = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x18ee:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x18fa:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1912;
            L_0x1906:
                r22 = new mobi.vserv.android.ads.VservAdController$1$6;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.start();	 Catch:{ Exception -> 0x01c5 }
            L_0x1912:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = new mobi.vserv.android.ads.VservAdController$1$7;	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.setOnClickListener(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x192a:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 3;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x1965;
            L_0x193c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x1948:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x1954:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x1965:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 7;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x0193;
            L_0x1977:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x1983:
                r2 = new android.app.AlertDialog$Builder;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r2.<init>(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.viewMandatoryMessage;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r2.setMessage(r0);	 Catch:{ Exception -> 0x01c5 }
                r22 = 0;
                r0 = r22;
                r2.setCancelable(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.viewMandatoryRetryLabel;	 Catch:{ Exception -> 0x01c5 }
                r23 = new mobi.vserv.android.ads.VservAdController$1$8;	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r23;
                r2.setPositiveButton(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.viewMandatoryExitLabel;	 Catch:{ Exception -> 0x01c5 }
                r23 = new mobi.vserv.android.ads.VservAdController$1$9;	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r23;
                r2.setNegativeButton(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r2.show();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = -1;
                r22.page = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
                */
            }
        };
        this.responseString = Preconditions.EMPTY_ARGUMENTS;
        this.adDeliverdSuccess = false;
        if (this.domain != null && this.domain.equals(Preconditions.EMPTY_ARGUMENTS)) {
            setDomain("a.");
            setDomain("vserv.mobi");
        }
        this.context = context;
        this.vservConfigBundle = vservConfigBundle;
        this.zoneId = vservConfigBundle.getString("zoneId").trim();
        this.iAddCallback = iAddCallback;
        this.handler = new Handler();
        this.vservAdControllerInterface = new VservAdControllerInterface();
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        this.width = dm.widthPixels;
        this.height = dm.heightPixels;
        if (!vservConfigBundle.containsKey("skipLabel")) {
            vservConfigBundle.putString("skipLabel", "Skip Ad");
        }
        if (VERSION.SDK_INT >= 11) {
            this.contextAdPreference = context.getSharedPreferences("vserv_unique_add_app_session", MMAdView.TRANSITION_RANDOM);
        } else {
            this.contextAdPreference = context.getSharedPreferences("vserv_unique_add_app_session", 0);
        }
        if ((vservConfigBundle.containsKey("adType") && (vservConfigBundle.getString("adType").equalsIgnoreCase(AdvConfigTable.COLUMN_SHOW) || vservConfigBundle.getString("adType").equalsIgnoreCase("overlay"))) || (vservConfigBundle.containsKey("frameAd") && vservConfigBundle.getBoolean("frameAd"))) {
            this.cachedObject = VservManager.getInstance(context).getAdview();
            if (this.cachedObject != null) {
                VservManager.getInstance(context).removeAdview();
                if (this.cachedObject instanceof String) {
                    this.skipDelay = getCacheADSkipDelay();
                    removeCacheADSkipDelay();
                    this.requestId = (String) VservManager.getInstance(context).getCacheAdRequestId();
                    if (Defines.ENABLE_lOGGING) {
                        Log.i("vserv", new StringBuilder("requestId:: ").append(this.requestId).toString());
                    }
                    removeCacheADRequestId();
                }
            }
        }
    }

    @SuppressLint({"InlinedApi"})
    public VservAdController(Context context, VservAdManager vservAdManageractivity) {
        this.pre_domain = "a.";
        this.post_domain = "vserv.mobi";
        this.domain = Preconditions.EMPTY_ARGUMENTS;
        this.notifyUrls = new Hashtable();
        this.notifyCacheUrls = new Hashtable();
        this.isCacheAdHit = false;
        this.isDisplayAdHit = false;
        this.isRenderAd = false;
        this.isDisplayFrameAd = false;
        this.connectionStartTime = 0;
        this.proceedStartTime = 0;
        this.configUrl = Preconditions.EMPTY_ARGUMENTS;
        this.zoneId = Preconditions.EMPTY_ARGUMENTS;
        this.width = 0;
        this.height = 0;
        this.adWidth = 0;
        this.adHeight = 0;
        this.isCacheHitInitiated = false;
        this.loadCachedAd = false;
        this.timeOutInSeconds = 20;
        this.proceedTimeInSeconds = -1;
        this.viewMandatoryMessage = "Data connection unavailable";
        this.viewMandatoryRetryLabel = "Retry";
        this.viewMandatoryExitLabel = "Exit";
        this.connectionTimedOut = false;
        this.makeAdRequest = false;
        this.isShowSkipButton = true;
        this.enableWaitingScreen = false;
        this.isCachingEnabled = false;
        this.isDisplayGetAdEnabled = false;
        this.snsWidgetUrl = "http://sns.vserv.mobi/sns/getWidget.php?";
        this.object = new Object();
        this.refreshRate = -1;
        this.serverRefreshRate = -1;
        this.default_refresh_rate = 60;
        this.shouldRefresh = false;
        this.adOrientation = "adaptive";
        this.adType = "billboard";
        this.gpHit = null;
        this.trailerPart = null;
        this.zoneType = "Billboard";
        this.inventoryType = "Application";
        this.directVideo = null;
        this.impressionHeader = null;
        this.isMediaCacheDisabled = null;
        this.decodeResponse = false;
        this.applyOrientation = false;
        this.sendPresentScreenBroadcast = true;
        this.ormmaReady_file_contents = Preconditions.EMPTY_ARGUMENTS;
        this.ormmaReady_Cache_file_contents = Preconditions.EMPTY_ARGUMENTS;
        this.advertiserId = null;
        this.isLimitAdTrackingEnabled = true;
        this.considerTimeout = false;
        this.jsinterfaceobj = Preconditions.EMPTY_ARGUMENTS;
        this.downloadNetworkUrl = null;
        this.countDownTimer = null;
        this.skipDelay = 0;
        this.currentSkipDelay = 0;
        this.tempSkipDelay = 0;
        this.requestId = null;
        this.showskipDelay = false;
        this.appId = Preconditions.EMPTY_ARGUMENTS;
        this.gameIdUrl = "http://in.sb.vserv.mobi/getGameId.php?";
        this.loadAdResources = false;
        this.file = null;
        this.updateResults = new Runnable() {
            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            @android.annotation.SuppressLint({"NewApi"})
            public void run() {
                throw new UnsupportedOperationException("Method not decompiled: mobi.vserv.android.ads.VservAdController.AnonymousClass_1.run():void");
                /*
                r28 = this;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x01a3;
            L_0x000c:
                r6 = new android.widget.RelativeLayout;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r6.<init>(r0);	 Catch:{ Exception -> 0x01c5 }
                r22 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
                r0 = r22;
                r6.setId(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0194;
            L_0x0030:
                r7 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22 = -1;
                r23 = -1;
                r0 = r22;
                r1 = r23;
                r7.<init>(r0, r1);	 Catch:{ Exception -> 0x01c5 }
            L_0x003d:
                r6.setLayoutParams(r7);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x00d6;
            L_0x004c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.ImageView;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r22.closeButtonImage = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.getResources();	 Catch:{ Exception -> 0x01c5 }
                r24 = "vserv_close_advertisement";
                r25 = "drawable";
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r26 = r26.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r26 = r26.getPackageName();	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.getIdentifier(r24, r25, r26);	 Catch:{ Exception -> 0x01c5 }
                r22.setImageResource(r23);	 Catch:{ Exception -> 0x01c5 }
                r3 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22 = -2;
                r23 = -2;
                r0 = r22;
                r1 = r23;
                r3.<init>(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r22 = 11;
                r0 = r22;
                r3.addRule(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r0.setLayoutParams(r3);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r6.addView(r0);	 Catch:{ Exception -> 0x01c5 }
            L_0x00d6:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.ProgressBar;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.context;	 Catch:{ Exception -> 0x01c5 }
                r25 = 0;
                r26 = 16842871; // 0x1010077 float:2.3693892E-38 double:8.321484E-317;
                r23.<init>(r24, r25, r26);	 Catch:{ Exception -> 0x01c5 }
                r22.progressBar = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r23 = 0;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
                r9 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22 = -2;
                r23 = -2;
                r0 = r22;
                r1 = r23;
                r9.<init>(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r22 = 13;
                r0 = r22;
                r9.addRule(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r0.setLayoutParams(r9);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r6.addView(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x014f;
            L_0x0140:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r0.setContentView(r6);	 Catch:{ Exception -> 0x01c5 }
            L_0x014f:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "refreshInterval";
                r22 = r22.containsKey(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0170;
            L_0x0163:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                r22.showProgressBar();	 Catch:{ Exception -> 0x01c5 }
            L_0x0170:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.makeAdRequest;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x017c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.makeAdRequest = r23;	 Catch:{ Exception -> 0x01c5 }
                r22 = new mobi.vserv.android.ads.VservAdController$1$1;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.start();	 Catch:{ Exception -> 0x01c5 }
            L_0x0193:
                return;
            L_0x0194:
                r7 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22 = -1;
                r23 = -2;
                r0 = r22;
                r1 = r23;
                r7.<init>(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x003d;
            L_0x01a3:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 1;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x01ee;
            L_0x01b5:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x01c5:
                r4 = move-exception;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;
                r22 = r0;
                r22 = r22.context;
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);
                r23 = "updateResultsRun";
                r0 = r22;
                r1 = r23;
                r0.trackExceptions(r4, r1);
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;
                r22 = r0;
                r23 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
                r24 = 0;
                r22.loadMainApp(r23, r24);
                r4.printStackTrace();
                goto L_0x0193;
            L_0x01ee:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 6;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x027e;
            L_0x0200:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x021b;
            L_0x020c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x021b:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0238;
            L_0x0229:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.clearView();	 Catch:{ Exception -> 0x01c5 }
            L_0x0238:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0263;
            L_0x0244:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.getVisibility();	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x0263;
            L_0x0254:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0263:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x026f:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                r22.removeAllViews();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x027e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x02aa;
            L_0x0290:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.isCacheAdHit;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x02aa;
            L_0x029c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.isCacheHitInitiated;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 != 0) goto L_0x02ca;
            L_0x02aa:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x0783;
            L_0x02bc:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.isgetAdRequest;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0783;
            L_0x02ca:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r0 = r23;
                r1 = r22;
                r1.isgetAdRequest = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.isCacheAdHit = r23;	 Catch:{ Exception -> 0x01c5 }
                r14 = 0;
                r15 = 0;
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0307;
            L_0x02e9:
                r22 = "vserv";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "&&&&&&&&SHOW_AD Called true:: ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.isCacheAdHit;	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0307:
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "start";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x058c;
            L_0x0311:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "startAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x0330:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 != 0) goto L_0x0500;
            L_0x033e:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0349;
            L_0x0342:
                r22 = "VservAdController";
                r23 = "Creating adCacheView";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0349:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x060d;
            L_0x0355:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new mobi.vserv.org.ormma.view.OrmmaView;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r26 = r26.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r27 = 1;
                r23.<init>(r24, r25, r26, r27);	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r22;
                r1.adCacheView = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r0 = r0.skipDelay;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r22.setSkipDelay(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x039d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x06e9;
            L_0x03a9:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                r23 = "Banner";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x06e9;
            L_0x03bb:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x064d;
            L_0x03c9:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x064d;
            L_0x03d7:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r24 = r24 * r25;
                r0 = r24;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r0 = r26;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r25 = r25 * r26;
                r0 = r25;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adCacheViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                r23 = "Billboard";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x044e;
            L_0x043f:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adCacheViewlayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r23 = 14;
                r22.addRule(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x044e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x047b;
            L_0x045a:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                r23 = "Banner";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x047b;
            L_0x046c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adCacheViewlayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r23 = 13;
                r22.addRule(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x047b:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.adCacheViewlayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22.setLayoutParams(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.getSettings();	 Catch:{ Exception -> 0x01c5 }
                r23 = 1;
                r22.setJavaScriptEnabled(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x04c7;
            L_0x04b5:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                r23 = "";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x04dd;
            L_0x04c7:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "VservAd";
                r22.setinterface(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "ControllerInterface";
                r22.setinterface(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x04dd:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.vservAdControllerInterface;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                r22.addJavascriptInterface(r23, r24);	 Catch:{ Exception -> 0x01c5 }
            L_0x0500:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.file;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.exists();	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x06fd;
            L_0x0510:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.file;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.ormmaReady_Cache_file_contents;	 Catch:{ Exception -> 0x01c5 }
                r22.loadFile(r23, r24);	 Catch:{ Exception -> 0x01c5 }
            L_0x0533:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.impressionHeader;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x075d;
            L_0x0541:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.impressionHeader;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "null-impression";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x075d;
            L_0x0555:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0579;
            L_0x0559:
                r22 = "vserv";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "inject 1:: ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.impressionHeader;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0579:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "var vservIsCachingEnabled=1";
                r22.injectJavaScript(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x058c:
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "mid";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x05b7;
            L_0x0596:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "midAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0330;
            L_0x05b7:
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "end";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x05e2;
            L_0x05c1:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "endAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0330;
            L_0x05e2:
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "inapp";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0330;
            L_0x05ec:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "inAppAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0330;
            L_0x060d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new mobi.vserv.org.ormma.view.OrmmaView;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.context;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r26 = 1;
                r23.<init>(r24, r25, r26);	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r22;
                r1.adCacheView = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r0 = r0.skipDelay;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r22.setSkipDelay(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x039d;
            L_0x064d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0691;
            L_0x065b:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r24 = r24 * r25;
                r0 = r24;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adCacheViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x044e;
            L_0x0691:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x06d5;
            L_0x069f:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r0 = r26;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r25 = r25 * r26;
                r0 = r25;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adCacheViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x044e;
            L_0x06d5:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adCacheViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x044e;
            L_0x06e9:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adCacheViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x044e;
            L_0x06fd:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0708;
            L_0x0701:
                r22 = "vserv";
                r23 = "file does not exists():: ";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0708:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0727;
            L_0x0716:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adCacheView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0727:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0742;
            L_0x0733:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 404; // 0x194 float:5.66E-43 double:1.996E-321;
                r24 = 0;
                r22.loadMainApp(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0533;
            L_0x0742:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x074e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                r22.onLoadFailure();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0533;
            L_0x075d:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x0761:
                r22 = "vserv";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "Not inject 1:: ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.impressionHeader;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x0783:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x07a1;
            L_0x0795:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.isDisplayAdHit;	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x07c1;
            L_0x07a1:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x17d1;
            L_0x07b3:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.isShowAdRequest;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x17d1;
            L_0x07c1:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r0 = r23;
                r1 = r22;
                r1.isShowAdRequest = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.isDisplayAdHit = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.cachedObject;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r0 = r0 instanceof android.webkit.WebView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0804;
            L_0x07ec:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.cachedObject;	 Catch:{ Exception -> 0x01c5 }
                r22 = (mobi.vserv.org.ormma.view.OrmmaView) r22;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r23;
                r1.adComponentView = r0;	 Catch:{ Exception -> 0x01c5 }
            L_0x0804:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0820;
            L_0x0812:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 1;
                r0 = r23;
                r1 = r22;
                r1.loadCachedAd = r0;	 Catch:{ Exception -> 0x01c5 }
            L_0x0820:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x08f6;
            L_0x082c:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0837;
            L_0x0830:
                r22 = "VservAdController";
                r23 = "Newly creating mainLayout";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0837:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.context;	 Catch:{ Exception -> 0x01c5 }
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r22.mainLayout = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0f5e;
            L_0x085b:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                r23 = "Banner";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0f5e;
            L_0x086d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0ec2;
            L_0x087b:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0ec2;
            L_0x0889:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r24 = r24 * r25;
                r0 = r24;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r0 = r26;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r25 = r25 * r26;
                r0 = r25;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.mainlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x08df:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.mainlayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22.setLayoutParams(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x08f6:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 != 0) goto L_0x1143;
            L_0x0904:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x090f;
            L_0x0908:
                r22 = "VservAdController";
                r23 = "Creating adComponentView";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x090f:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r0 = r0.skipDelay;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r1 = r22;
                r1.currentSkipDelay = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0f72;
            L_0x0933:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new mobi.vserv.org.ormma.view.OrmmaView;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r26 = r26.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r27 = 0;
                r23.<init>(r24, r25, r26, r27);	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r22;
                r1.adComponentView = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r0 = r0.currentSkipDelay;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r22.setSkipDelay(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x097b:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x099d;
            L_0x097f:
                r22 = "Vserv";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "@@@adType::: ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.adType;	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x099d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x104e;
            L_0x09a9:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                r23 = "Banner";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x104e;
            L_0x09bb:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0fb2;
            L_0x09c9:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0fb2;
            L_0x09d7:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r24 = r24 * r25;
                r0 = r24;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r0 = r26;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r25 = r25 * r26;
                r0 = r25;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adComponentViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x0a2d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.adComponentViewlayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22.setLayoutParams(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.requestFocus();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.getSettings();	 Catch:{ Exception -> 0x01c5 }
                r23 = 1;
                r22.setJavaScriptEnabled(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0a88;
            L_0x0a76:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                r23 = "";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0a9e;
            L_0x0a88:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "VservAd";
                r22.setinterface(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "ControllerInterface";
                r22.setinterface(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0a9e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.vservAdControllerInterface;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                r22.addJavascriptInterface(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "start";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1062;
            L_0x0acb:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "startAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x0aea:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 456; // 0x1c8 float:6.39E-43 double:2.253E-321;
                r22.setId(r23);	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0b1d;
            L_0x0aff:
                r22 = "network";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "loading file ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.ormmaReady_file_contents;	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0b1d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.file;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.exists();	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x10e3;
            L_0x0b2d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.file;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.ormmaReady_file_contents;	 Catch:{ Exception -> 0x01c5 }
                r22.loadFile(r23, r24);	 Catch:{ Exception -> 0x01c5 }
            L_0x0b50:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0b7d;
            L_0x0b5c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adType;	 Catch:{ Exception -> 0x01c5 }
                r23 = "Banner";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x0b7d;
            L_0x0b6e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adComponentViewlayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r23 = 13;
                r22.addRule(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0b7d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0ba8;
            L_0x0b89:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.getVisibility();	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x0ba8;
            L_0x0b99:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.progressBar;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0ba8:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x14db;
            L_0x0bb4:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0bbf;
            L_0x0bb8:
                r22 = "VservAdController";
                r23 = "Setting Content View ";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0bbf:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r22.addView(r23);	 Catch:{ Exception -> 0x01c5 }
                r20 = new android.widget.RelativeLayout;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r0 = r20;
                r1 = r22;
                r0.<init>(r1);	 Catch:{ Exception -> 0x01c5 }
                r22 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
                r0 = r20;
                r1 = r22;
                r0.setId(r1);	 Catch:{ Exception -> 0x01c5 }
                r21 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22 = -1;
                r23 = -2;
                r21.<init>(r22, r23);	 Catch:{ Exception -> 0x01c5 }
                r20.setLayoutParams(r21);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.TextView;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r22.skipLabel = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.skipLabel;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.skipLabel;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.getResources();	 Catch:{ Exception -> 0x01c5 }
                r24 = "vserv_skipdelay";
                r25 = "drawable";
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r26 = r26.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r26 = r26.getPackageName();	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.getIdentifier(r24, r25, r26);	 Catch:{ Exception -> 0x01c5 }
                r22.setBackgroundResource(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.skipLabel;	 Catch:{ Exception -> 0x01c5 }
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "Skip in";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.currentSkipDelay;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                r22.setText(r23);	 Catch:{ Exception -> 0x01c5 }
                r10 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22 = -2;
                r23 = -2;
                r0 = r22;
                r1 = r23;
                r10.<init>(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r22 = 11;
                r0 = r22;
                r10.addRule(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.skipLabel;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r0.setLayoutParams(r10);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.skipLabel;	 Catch:{ Exception -> 0x01c5 }
                r22.addView(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.ImageView;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r22.closeButtonImage = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.getResources();	 Catch:{ Exception -> 0x01c5 }
                r24 = "vserv_close_advertisement";
                r25 = "drawable";
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r26 = r26.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r26 = r26.getPackageName();	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.getIdentifier(r24, r25, r26);	 Catch:{ Exception -> 0x01c5 }
                r22.setImageResource(r23);	 Catch:{ Exception -> 0x01c5 }
                r3 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r22 = -2;
                r23 = -2;
                r0 = r22;
                r1 = r23;
                r3.<init>(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r22 = 11;
                r0 = r22;
                r3.addRule(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r0.setLayoutParams(r3);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r0 = r20;
                r1 = r22;
                r0.addView(r1);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r20;
                r0.addView(r1);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.applyOrientation;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0de7;
            L_0x0d63:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.loadCachedAd;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x134a;
            L_0x0d71:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.getCacheADOrientation();	 Catch:{ Exception -> 0x01c5 }
                r22.adOrientation = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.removeCacheADOrientation();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r22;
                r1.adOrientation = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x130c;
            L_0x0db7:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.trim();	 Catch:{ Exception -> 0x01c5 }
                r23 = "landscape";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x130c;
            L_0x0dcd:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23 = 0;
                r22.setRequestedOrientation(r23);	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0de7;
            L_0x0de0:
                r22 = "orientation";
                r23 = "saving appOrientation landscape";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0de7:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.mainLayout;	 Catch:{ Exception -> 0x01c5 }
                r22.setContentView(r23);	 Catch:{ Exception -> 0x01c5 }
                r22 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x01c5 }
                r23 = 9;
                r0 = r22;
                r1 = r23;
                if (r0 <= r1) goto L_0x0e18;
            L_0x0e08:
                r22 = new android.os.StrictMode$ThreadPolicy$Builder;	 Catch:{ Exception -> 0x01c5 }
                r22.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.permitAll();	 Catch:{ Exception -> 0x01c5 }
                r8 = r22.build();	 Catch:{ Exception -> 0x01c5 }
                android.os.StrictMode.setThreadPolicy(r8);	 Catch:{ Exception -> 0x01c5 }
            L_0x0e18:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.loadCachedAd;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0e71;
            L_0x0e26:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.notifyOnceCache();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.showSkipPageCache();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);	 Catch:{ Exception -> 0x01c5 }
                r16 = r22.getAdType();	 Catch:{ Exception -> 0x01c5 }
                if (r16 == 0) goto L_0x0e71;
            L_0x0e4c:
                r0 = r16;
                r0 = r0 instanceof java.lang.String;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x1457;
            L_0x0e54:
                r22 = "video";
                r0 = r16;
                r1 = r22;
                r22 = r0.equals(r1);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x142d;
            L_0x0e60:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.playVideoImpl(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x0e71:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "cacheNextAd";
                r22 = r22.containsKey(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0ea4;
            L_0x0e85:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "cacheNextAd";
                r22 = r22.getBoolean(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0ea4;
            L_0x0e99:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 1;
                r22.isCacheAdHit = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x0ea4:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = r22.object;	 Catch:{ Exception -> 0x01c5 }
                monitor-enter(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ all -> 0x0ebf }
                r22 = r0;
                r22 = r22.object;	 Catch:{ all -> 0x0ebf }
                r22.notify();	 Catch:{ all -> 0x0ebf }
                monitor-exit(r23);	 Catch:{ all -> 0x0ebf }
                goto L_0x0193;
            L_0x0ebf:
                r22 = move-exception;
                monitor-exit(r23);	 Catch:{ all -> 0x0ebf }
                throw r22;	 Catch:{ Exception -> 0x01c5 }
            L_0x0ec2:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0f06;
            L_0x0ed0:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r24 = r24 * r25;
                r0 = r24;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.mainlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x08df;
            L_0x0f06:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0f4a;
            L_0x0f14:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r0 = r26;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r25 = r25 * r26;
                r0 = r25;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.mainlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x08df;
            L_0x0f4a:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.mainlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x08df;
            L_0x0f5e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.mainlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x08df;
            L_0x0f72:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new mobi.vserv.org.ormma.view.OrmmaView;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.context;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r26 = 0;
                r23.<init>(r24, r25, r26);	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r22;
                r1.adComponentView = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r0 = r0.currentSkipDelay;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r22.setSkipDelay(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x097b;
            L_0x0fb2:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0ff6;
            L_0x0fc0:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.adWidth;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r24 = r24 * r25;
                r0 = r24;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adComponentViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0a2d;
            L_0x0ff6:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x103a;
            L_0x1004:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = r0.adHeight;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r25;
                r0 = (float) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r0 = r26;
                r0 = r0.zoomFactor;	 Catch:{ Exception -> 0x01c5 }
                r26 = r0;
                r25 = r25 * r26;
                r0 = r25;
                r0 = (int) r0;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adComponentViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0a2d;
            L_0x103a:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adComponentViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0a2d;
            L_0x104e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ Exception -> 0x01c5 }
                r24 = -2;
                r25 = -2;
                r23.<init>(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.adComponentViewlayoutParams = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0a2d;
            L_0x1062:
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "mid";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x108d;
            L_0x106c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "midAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0aea;
            L_0x108d:
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "end";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x10b8;
            L_0x1097:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "endAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0aea;
            L_0x10b8:
                r22 = showAt;	 Catch:{ Exception -> 0x01c5 }
                r23 = "inapp";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0aea;
            L_0x10c2:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.responseString;	 Catch:{ Exception -> 0x01c5 }
                r25 = "inAppAd.html";
                r23 = r23.saveFileInCache(r24, r25);	 Catch:{ Exception -> 0x01c5 }
                r22.file = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0aea;
            L_0x10e3:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x10ee;
            L_0x10e7:
                r22 = "vserv";
                r23 = "file does not exists():: ";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x10ee:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x110d;
            L_0x10fc:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x110d:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1128;
            L_0x1119:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 404; // 0x194 float:5.66E-43 double:1.996E-321;
                r24 = 0;
                r22.loadMainApp(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0b50;
            L_0x1128:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x1134:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                r22.onLoadFailure();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0b50;
            L_0x1143:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.getCacheADSkipDelay();	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r22;
                r1.currentSkipDelay = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.removeCacheADSkipDelay();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.getCacheAdRequestId();	 Catch:{ Exception -> 0x01c5 }
                r22 = (java.lang.String) r22;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r23;
                r1.requestId = r0;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x11a6;
            L_0x1186:
                r22 = "vserv";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "requestId:: ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.requestId;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x11a6:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.removeCacheADRequestId();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x12cc;
            L_0x11bb:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r25 = r0;
                r25 = r25.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r22.setCacheViewActivity(r23, r24, r25);	 Catch:{ Exception -> 0x01c5 }
            L_0x11e4:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);	 Catch:{ Exception -> 0x01c5 }
                r13 = r22.getCacheImpression();	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1210;
            L_0x11fa:
                r22 = "VservAdController";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "Already Creating adComponentView:: ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r23 = r0.append(r13);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x1210:
                if (r13 == 0) goto L_0x12ed;
            L_0x1212:
                r22 = "null-impression";
                r0 = r22;
                r22 = r13.equals(r0);	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x12ed;
            L_0x121c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0.injectJavaScript(r13);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);	 Catch:{ Exception -> 0x01c5 }
                r22.removeCacheImpressionHeader();	 Catch:{ Exception -> 0x01c5 }
            L_0x123e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.requestFocus();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.getSettings();	 Catch:{ Exception -> 0x01c5 }
                r23 = 1;
                r22.setJavaScriptEnabled(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1280;
            L_0x126e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                r23 = "";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1296;
            L_0x1280:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "VservAd";
                r22.setinterface(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "ControllerInterface";
                r22.setinterface(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x1296:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.vservAdControllerInterface;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r24 = r24.jsinterfaceobj;	 Catch:{ Exception -> 0x01c5 }
                r22.addJavascriptInterface(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 456; // 0x1c8 float:6.39E-43 double:2.253E-321;
                r22.setId(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0b7d;
            L_0x12cc:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.context;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r22.setCacheView(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x11e4;
            L_0x12ed:
                if (r13 == 0) goto L_0x123e;
            L_0x12ef:
                r22 = "null-impression";
                r0 = r22;
                r22 = r13.equals(r0);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x123e;
            L_0x12f9:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);	 Catch:{ Exception -> 0x01c5 }
                r22.removeCacheImpressionHeader();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x123e;
            L_0x130c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0de7;
            L_0x1318:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.trim();	 Catch:{ Exception -> 0x01c5 }
                r23 = "portrait";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0de7;
            L_0x132e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23 = 1;
                r22.setRequestedOrientation(r23);	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0de7;
            L_0x1341:
                r22 = "orientation";
                r23 = "saving appOrientation portrait";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0de7;
            L_0x134a:
                r22 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x01c5 }
                r23 = 11;
                r0 = r22;
                r1 = r23;
                if (r0 < r1) goto L_0x13c9;
            L_0x1354:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r23 = "vserv_orientation";
                r24 = 4;
                r22 = r22.getSharedPreferences(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                r5 = r22.edit();	 Catch:{ Exception -> 0x01c5 }
            L_0x136a:
                r22 = r5.clear();	 Catch:{ Exception -> 0x01c5 }
                r22.commit();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.applyOrientation = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x13e0;
            L_0x1388:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.trim();	 Catch:{ Exception -> 0x01c5 }
                r23 = "landscape";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x13e0;
            L_0x139e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23 = 0;
                r22.setRequestedOrientation(r23);	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x13b8;
            L_0x13b1:
                r22 = "orientation";
                r23 = "saving appOrientation landscape";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x13b8:
                r22 = "orientation";
                r23 = "landscape";
                r0 = r22;
                r1 = r23;
                r22 = r5.putString(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r22.commit();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0de7;
            L_0x13c9:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r23 = "vserv_orientation";
                r24 = 0;
                r22 = r22.getSharedPreferences(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                r5 = r22.edit();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x136a;
            L_0x13e0:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0de7;
            L_0x13ec:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.adOrientation;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.trim();	 Catch:{ Exception -> 0x01c5 }
                r23 = "portrait";
                r22 = r22.equalsIgnoreCase(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0de7;
            L_0x1402:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r23 = 1;
                r22.setRequestedOrientation(r23);	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x141c;
            L_0x1415:
                r22 = "orientation";
                r23 = "saving appOrientation portrait";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x141c:
                r22 = "orientation";
                r23 = "portrait";
                r0 = r22;
                r1 = r23;
                r22 = r5.putString(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r22.commit();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0de7;
            L_0x142d:
                r22 = "audio";
                r0 = r16;
                r1 = r22;
                r22 = r0.equals(r1);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x144c;
            L_0x1439:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.playAudioImpl(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0e71;
            L_0x144c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.removeCacheAdType();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0e71;
            L_0x1457:
                r0 = r16;
                r0 = r0 instanceof android.os.Bundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0e71;
            L_0x145f:
                r0 = r16;
                r0 = (android.os.Bundle) r0;	 Catch:{ Exception -> 0x01c5 }
                r18 = r0;
                r22 = "action";
                r0 = r18;
                r1 = r22;
                r22 = r0.get(r1);	 Catch:{ Exception -> 0x01c5 }
                r12 = r22.toString();	 Catch:{ Exception -> 0x01c5 }
                if (r12 == 0) goto L_0x0e71;
            L_0x1475:
                r17 = "PLAY";
                r22 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r23 = java.lang.String.valueOf(r17);	 Catch:{ Exception -> 0x01c5 }
                r22.<init>(r23);	 Catch:{ Exception -> 0x01c5 }
                r23 = "_VIDEO";
                r22 = r22.append(r23);	 Catch:{ Exception -> 0x01c5 }
                r17 = r22.toString();	 Catch:{ Exception -> 0x01c5 }
                r11 = "PLAY";
                r22 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r23 = java.lang.String.valueOf(r11);	 Catch:{ Exception -> 0x01c5 }
                r22.<init>(r23);	 Catch:{ Exception -> 0x01c5 }
                r23 = "_AUDIO";
                r22 = r22.append(r23);	 Catch:{ Exception -> 0x01c5 }
                r11 = r22.toString();	 Catch:{ Exception -> 0x01c5 }
                r0 = r17;
                r22 = r12.equals(r0);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x14be;
            L_0x14a7:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r16 = (android.os.Bundle) r16;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r16;
                r0.playVideoFullScreen(r1);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0e71;
            L_0x14be:
                r22 = r12.equals(r11);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0e71;
            L_0x14c4:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r16 = (android.os.Bundle) r16;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r16;
                r0.playAudioFullScreenImpl(r1);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0e71;
            L_0x14db:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r23;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r22.onLoadSuccess(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.isDisplayFrameAd;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1533;
            L_0x1500:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "cacheNextAd";
                r22 = r22.containsKey(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1533;
            L_0x1514:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "cacheNextAd";
                r22 = r22.getBoolean(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1533;
            L_0x1528:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 1;
                r22.isCacheAdHit = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x1533:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.loadCachedAd;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x158c;
            L_0x1541:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.notifyOnceCache();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.showSkipPageCache();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.context;	 Catch:{ Exception -> 0x01c5 }
                r22 = mobi.vserv.android.ads.VservManager.getInstance(r22);	 Catch:{ Exception -> 0x01c5 }
                r16 = r22.getAdType();	 Catch:{ Exception -> 0x01c5 }
                if (r16 == 0) goto L_0x158c;
            L_0x1567:
                r0 = r16;
                r0 = r0 instanceof java.lang.String;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x1725;
            L_0x156f:
                r22 = "video";
                r0 = r16;
                r1 = r22;
                r22 = r0.equals(r1);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x16fb;
            L_0x157b:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.playVideoImpl(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x158c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = r22.object;	 Catch:{ Exception -> 0x01c5 }
                monitor-enter(r23);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ all -> 0x17a9 }
                r22 = r0;
                r22 = r22.object;	 Catch:{ all -> 0x17a9 }
                r22.notify();	 Catch:{ all -> 0x17a9 }
                monitor-exit(r23);	 Catch:{ all -> 0x17a9 }
                r19 = "refreshInterval";
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = "refreshInterval";
                r22 = r22.containsKey(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x15bb:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r1 = r19;
                r22 = r0.getString(r1);	 Catch:{ Exception -> 0x01c5 }
                r23 = "yes";
                r22 = r22.equals(r23);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x15d7:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.refreshRate;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = -1;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x1613;
            L_0x15eb:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.serverRefreshRate;	 Catch:{ Exception -> 0x01c5 }
                r24 = -1;
                r0 = r22;
                r1 = r24;
                if (r0 != r1) goto L_0x17ac;
            L_0x1603:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.default_refresh_rate;	 Catch:{ Exception -> 0x01c5 }
            L_0x160d:
                r0 = r22;
                r1 = r23;
                r1.refreshRate = r0;	 Catch:{ Exception -> 0x01c5 }
            L_0x1613:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.refreshRate;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 <= 0) goto L_0x0193;
            L_0x1621:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x164b;
            L_0x1625:
                r22 = "VservAdController";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "Will try to get new ad after ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.refreshRate;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r24 = " seconds ";
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x164b:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.refreshRunnable;	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x1669;
            L_0x1657:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new mobi.vserv.android.ads.VservAdController$1$2;	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.refreshRunnable = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x1669:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.refreshHandler;	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x17b8;
            L_0x1675:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = new android.os.Handler;	 Catch:{ Exception -> 0x01c5 }
                r23.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.refreshHandler = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x1683:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.shouldRefresh;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x0193;
            L_0x1691:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x16cb;
            L_0x1695:
                r22 = "abc";
                r23 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r24 = "Will try to get new Ad after ";
                r23.<init>(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.refreshRate;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r24 = " seconds!! for zoneId ";
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.zoneId;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r23 = r23.append(r24);	 Catch:{ Exception -> 0x01c5 }
                r23 = r23.toString();	 Catch:{ Exception -> 0x01c5 }
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x16cb:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.refreshHandler;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.refreshRunnable;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0.refreshRate;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r0 = r24;
                r0 = r0 * 1000;
                r24 = r0;
                r0 = r24;
                r0 = (long) r0;	 Catch:{ Exception -> 0x01c5 }
                r24 = r0;
                r22.postDelayed(r23, r24);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x16fb:
                r22 = "audio";
                r0 = r16;
                r1 = r22;
                r22 = r0.equals(r1);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x171a;
            L_0x1707:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r22.playAudioImpl(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x158c;
            L_0x171a:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22.removeCacheAdType();	 Catch:{ Exception -> 0x01c5 }
                goto L_0x158c;
            L_0x1725:
                r0 = r16;
                r0 = r0 instanceof android.os.Bundle;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                if (r22 == 0) goto L_0x158c;
            L_0x172d:
                r0 = r16;
                r0 = (android.os.Bundle) r0;	 Catch:{ Exception -> 0x01c5 }
                r18 = r0;
                r22 = "action";
                r0 = r18;
                r1 = r22;
                r22 = r0.get(r1);	 Catch:{ Exception -> 0x01c5 }
                r12 = r22.toString();	 Catch:{ Exception -> 0x01c5 }
                if (r12 == 0) goto L_0x158c;
            L_0x1743:
                r17 = "PLAY";
                r22 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r23 = java.lang.String.valueOf(r17);	 Catch:{ Exception -> 0x01c5 }
                r22.<init>(r23);	 Catch:{ Exception -> 0x01c5 }
                r23 = "_VIDEO";
                r22 = r22.append(r23);	 Catch:{ Exception -> 0x01c5 }
                r17 = r22.toString();	 Catch:{ Exception -> 0x01c5 }
                r11 = "PLAY";
                r22 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c5 }
                r23 = java.lang.String.valueOf(r11);	 Catch:{ Exception -> 0x01c5 }
                r22.<init>(r23);	 Catch:{ Exception -> 0x01c5 }
                r23 = "_AUDIO";
                r22 = r22.append(r23);	 Catch:{ Exception -> 0x01c5 }
                r11 = r22.toString();	 Catch:{ Exception -> 0x01c5 }
                r0 = r17;
                r22 = r12.equals(r0);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x178c;
            L_0x1775:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r16 = (android.os.Bundle) r16;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r16;
                r0.playVideoFullScreen(r1);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x158c;
            L_0x178c:
                r22 = r12.equals(r11);	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x158c;
            L_0x1792:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.adComponentView;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r16 = (android.os.Bundle) r16;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r16;
                r0.playAudioFullScreenImpl(r1);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x158c;
            L_0x17a9:
                r22 = move-exception;
                monitor-exit(r23);	 Catch:{ all -> 0x17a9 }
                throw r22;	 Catch:{ Exception -> 0x01c5 }
            L_0x17ac:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.serverRefreshRate;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x160d;
            L_0x17b8:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.refreshHandler;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r23 = r0;
                r23 = r23.refreshRunnable;	 Catch:{ Exception -> 0x01c5 }
                r22.removeCallbacks(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x1683;
            L_0x17d1:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 5;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x183e;
            L_0x17e3:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = -1;
                r22.page = r23;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x17fa:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1826;
            L_0x1806:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r0 = r22;
                r0 = r0.currentSkipDelay;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = -1;
                r0 = r22;
                r1 = r23;
                if (r0 == r1) goto L_0x1826;
            L_0x181a:
                r22 = new mobi.vserv.android.ads.VservAdController$1$3;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.start();	 Catch:{ Exception -> 0x01c5 }
            L_0x1826:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = new mobi.vserv.android.ads.VservAdController$1$4;	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.setOnClickListener(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x183e:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 8;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x18ad;
            L_0x1850:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x185c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1895;
            L_0x1868:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r22 = r22.isShown();	 Catch:{ Exception -> 0x01c5 }
                if (r22 != 0) goto L_0x1895;
            L_0x1878:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 0;
                r0 = r23;
                r1 = r22;
                r1.currentSkipDelay = r0;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = 0;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x1895:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = new mobi.vserv.android.ads.VservAdController$1$5;	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.setOnClickListener(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x18ad:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 2;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x192a;
            L_0x18bf:
                r22 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x18ca;
            L_0x18c3:
                r22 = "sample";
                r23 = "Will try to show cancel button";
                android.util.Log.i(r22, r23);	 Catch:{ Exception -> 0x01c5 }
            L_0x18ca:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x18ee;
            L_0x18d6:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.iAddCallback;	 Catch:{ Exception -> 0x01c5 }
                r22.TimeOutOccured();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = 1;
                r22.considerTimeout = r23;	 Catch:{ Exception -> 0x01c5 }
            L_0x18ee:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x18fa:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x1912;
            L_0x1906:
                r22 = new mobi.vserv.android.ads.VservAdController$1$6;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.start();	 Catch:{ Exception -> 0x01c5 }
            L_0x1912:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = new mobi.vserv.android.ads.VservAdController$1$7;	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r22.setOnClickListener(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x192a:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 3;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x1965;
            L_0x193c:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x1948:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x1954:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.closeButtonImage;	 Catch:{ Exception -> 0x01c5 }
                r23 = 4;
                r22.setVisibility(r23);	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
            L_0x1965:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.page;	 Catch:{ Exception -> 0x01c5 }
                r23 = 7;
                r0 = r22;
                r1 = r23;
                if (r0 != r1) goto L_0x0193;
            L_0x1977:
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                if (r22 == 0) goto L_0x0193;
            L_0x1983:
                r2 = new android.app.AlertDialog$Builder;	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.vservAdManageractivity;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r2.<init>(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.viewMandatoryMessage;	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r2.setMessage(r0);	 Catch:{ Exception -> 0x01c5 }
                r22 = 0;
                r0 = r22;
                r2.setCancelable(r0);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.viewMandatoryRetryLabel;	 Catch:{ Exception -> 0x01c5 }
                r23 = new mobi.vserv.android.ads.VservAdController$1$8;	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r23;
                r2.setPositiveButton(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r22 = r22.viewMandatoryExitLabel;	 Catch:{ Exception -> 0x01c5 }
                r23 = new mobi.vserv.android.ads.VservAdController$1$9;	 Catch:{ Exception -> 0x01c5 }
                r0 = r23;
                r1 = r28;
                r0.<init>();	 Catch:{ Exception -> 0x01c5 }
                r0 = r22;
                r1 = r23;
                r2.setNegativeButton(r0, r1);	 Catch:{ Exception -> 0x01c5 }
                r2.show();	 Catch:{ Exception -> 0x01c5 }
                r0 = r28;
                r0 = mobi.vserv.android.ads.VservAdController.this;	 Catch:{ Exception -> 0x01c5 }
                r22 = r0;
                r23 = -1;
                r22.page = r23;	 Catch:{ Exception -> 0x01c5 }
                goto L_0x0193;
                */
            }
        };
        this.responseString = Preconditions.EMPTY_ARGUMENTS;
        this.adDeliverdSuccess = false;
        if (this.domain != null && this.domain.equals(Preconditions.EMPTY_ARGUMENTS)) {
            setDomain("a.");
            setDomain("vserv.mobi");
        }
        this.context = context;
        this.vservAdManageractivity = vservAdManageractivity;
        clearAdPanel();
        this.zoneId = vservAdManageractivity.vservConfigBundle.getString("zoneId").trim();
        this.handler = new Handler();
        this.vservAdControllerInterface = new VservAdControllerInterface();
        this.width = VservAdManager.width;
        this.height = VservAdManager.height;
        if (VERSION.SDK_INT >= 11) {
            this.contextAdPreference = context.getSharedPreferences("vserv_unique_add_app_session", MMAdView.TRANSITION_RANDOM);
        } else {
            this.contextAdPreference = context.getSharedPreferences("vserv_unique_add_app_session", 0);
        }
        this.cachedObject = VservManager.getInstance(context).getAdview();
        if (this.cachedObject != null) {
            VservManager.getInstance(context).removeAdview();
            if (this.cachedObject instanceof String) {
                this.skipDelay = getCacheADSkipDelay();
                removeCacheADSkipDelay();
                this.requestId = (String) VservManager.getInstance(context).getCacheAdRequestId();
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", new StringBuilder("requestId:: ").append(this.requestId).toString());
                }
                removeCacheADRequestId();
            }
        }
    }

    private boolean checkExternalMedia() {
        String str = "mounted";
        return str.equals(Environment.getExternalStorageState());
    }

    private byte[] compress(String string) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(string.length());
        GZIPOutputStream gos = new GZIPOutputStream(os);
        gos.write(string.getBytes());
        gos.close();
        byte[] compressed = os.toByteArray();
        os.close();
        return compressed;
    }

    private String decompress(byte[] compressed) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(compressed);
        GZIPInputStream gis = new GZIPInputStream(is, 32);
        StringBuilder string = new StringBuilder();
        byte[] data = new byte[32];
        while (true) {
            int bytesRead = gis.read(data);
            if (bytesRead == -1) {
                gis.close();
                is.close();
                return string.toString();
            } else {
                string.append(new String(data, 0, bytesRead));
            }
        }
    }

    private void deleteCacheFile(String cacheFileName) {
        try {
            File cacheDir;
            if (this.vservAdManageractivity != null) {
                cacheDir = this.vservAdManageractivity.getCacheDir();
            } else {
                cacheDir = this.context.getCacheDir();
            }
            new File(cacheDir, cacheFileName).delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint({"InlinedApi", "NewApi"})
    private void embedV2Parameters() {
        try {
            Location location;
            if (this.vservConfigBundle.containsKey("cf") && !TextUtils.isEmpty(this.vservConfigBundle.getString("cf"))) {
                try {
                    StringBuilder stringBuilder = stringBuilder;
                    String str = String.valueOf(this.configUrl);
                    this.configUrl = stringBuilder.append("&cf=").append(URLEncoder.encode(this.vservConfigBundle.getString("cf"), "UTF-8")).toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!(this.advertiserId == null || this.isLimitAdTrackingEnabled)) {
                try {
                    stringBuilder = stringBuilder;
                    str = String.valueOf(this.configUrl);
                    this.configUrl = stringBuilder.append("&advid=").append(URLEncoder.encode(this.advertiserId, "UTF-8")).toString();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            try {
                if (this.vservConfigBundle.containsKey("deviceIds")) {
                    String[] deviceIds = this.vservConfigBundle.getStringArray("deviceIds");
                    if (this.advertiserId != null) {
                        int i = deviceIds.length;
                        int i2 = 0;
                        while (i2 < i) {
                            if (deviceIds[i2].equals(this.advertiserId)) {
                                Log.d("vserv", "Test Mode is switched on");
                                try {
                                    stringBuilder = stringBuilder;
                                    str = String.valueOf(this.configUrl);
                                    this.configUrl = stringBuilder.append("&tm=").append(URLEncoder.encode("1", "UTF-8")).toString();
                                    break;
                                } catch (UnsupportedEncodingException e3) {
                                    UnsupportedEncodingException e4 = e3;
                                    VservManager.getInstance(this.context).trackExceptions(e4, "tm");
                                    e4.printStackTrace();
                                }
                            } else {
                                i2++;
                            }
                        }
                    }
                }
            } catch (Exception e5) {
                e5.printStackTrace();
            }
            if (this.vservConfigBundle.containsKey("ci") && !TextUtils.isEmpty(this.vservConfigBundle.getString("ci"))) {
                try {
                    stringBuilder = stringBuilder;
                    str = String.valueOf(this.configUrl);
                    this.configUrl = stringBuilder.append("&ci=").append(URLEncoder.encode(this.vservConfigBundle.getString("ci"), "UTF-8")).toString();
                } catch (Exception e6) {
                    e6.printStackTrace();
                }
            }
            if (this.vservConfigBundle.containsKey("co") && !TextUtils.isEmpty(this.vservConfigBundle.getString("co"))) {
                try {
                    stringBuilder = stringBuilder;
                    str = String.valueOf(this.configUrl);
                    this.configUrl = stringBuilder.append("&co=").append(URLEncoder.encode(this.vservConfigBundle.getString("co"), "UTF-8")).toString();
                } catch (UnsupportedEncodingException e7) {
                    e7.printStackTrace();
                }
            }
            if (this.vservConfigBundle.containsKey("ag") && !TextUtils.isEmpty(this.vservConfigBundle.getString("ag"))) {
                try {
                    stringBuilder = stringBuilder;
                    str = String.valueOf(this.configUrl);
                    this.configUrl = stringBuilder.append("&ag=").append(URLEncoder.encode(this.vservConfigBundle.getString("ag"), "UTF-8")).toString();
                } catch (UnsupportedEncodingException e8) {
                    e8.printStackTrace();
                }
            }
            if (this.vservConfigBundle.containsKey("gn") && !TextUtils.isEmpty(this.vservConfigBundle.getString("gn"))) {
                try {
                    stringBuilder = stringBuilder;
                    str = String.valueOf(this.configUrl);
                    this.configUrl = stringBuilder.append("&gn=").append(URLEncoder.encode(this.vservConfigBundle.getString("gn"), "UTF-8")).toString();
                } catch (UnsupportedEncodingException e9) {
                    e9.printStackTrace();
                }
            }
            if (this.vservConfigBundle.containsKey("dob") && !TextUtils.isEmpty(this.vservConfigBundle.getString("dob"))) {
                try {
                    stringBuilder = stringBuilder;
                    str = String.valueOf(this.configUrl);
                    this.configUrl = stringBuilder.append("&dob=").append(URLEncoder.encode(this.vservConfigBundle.getString("dob"), "UTF-8")).toString();
                } catch (UnsupportedEncodingException e10) {
                    e10.printStackTrace();
                }
            }
            if (this.vservConfigBundle.containsKey("partnerid") && !TextUtils.isEmpty(this.vservConfigBundle.getString("partnerid"))) {
                try {
                    stringBuilder = stringBuilder;
                    str = String.valueOf(this.configUrl);
                    this.configUrl = stringBuilder.append("&partnerid=").append(URLEncoder.encode(this.vservConfigBundle.getString("partnerid"), "UTF-8")).toString();
                } catch (UnsupportedEncodingException e11) {
                    e11.printStackTrace();
                }
            }
            if (this.vservConfigBundle.containsKey("sf") && !TextUtils.isEmpty(this.vservConfigBundle.getString("sf"))) {
                try {
                    stringBuilder = stringBuilder;
                    str = String.valueOf(this.configUrl);
                    this.configUrl = stringBuilder.append("&sf=").append(URLEncoder.encode(this.vservConfigBundle.getString("sf"), "UTF-8")).toString();
                } catch (UnsupportedEncodingException e12) {
                    e12.printStackTrace();
                }
            }
            if (this.context.getResources().getConfiguration().touchscreen != 1) {
                try {
                    stringBuilder = stringBuilder;
                    str = String.valueOf(this.configUrl);
                    this.configUrl = stringBuilder.append("&ts=").append(URLEncoder.encode("1", "UTF-8")).toString();
                } catch (UnsupportedEncodingException e13) {
                    e13.printStackTrace();
                }
            }
            try {
                String usrTimeZoneName = TimeZone.getDefault().getDisplayName();
                if (!TextUtils.isEmpty(usrTimeZoneName)) {
                    stringBuilder = stringBuilder;
                    str = String.valueOf(this.configUrl);
                    this.configUrl = stringBuilder.append("&tz=").append(URLEncoder.encode(usrTimeZoneName, "UTF-8")).toString();
                }
            } catch (Exception e14) {
                e14.printStackTrace();
            }
            if (this.vservConfigBundle.containsKey("em") && !TextUtils.isEmpty(this.vservConfigBundle.getString("em"))) {
                try {
                    stringBuilder = stringBuilder;
                    str = String.valueOf(this.configUrl);
                    this.configUrl = stringBuilder.append("&em=").append(URLEncoder.encode(this.vservConfigBundle.getString("em"), "UTF-8")).toString();
                } catch (Exception e15) {
                    e15.printStackTrace();
                }
            }
            try {
                if (!TextUtils.isEmpty(this.context.getPackageName())) {
                    stringBuilder = stringBuilder;
                    str = String.valueOf(this.configUrl);
                    this.configUrl = stringBuilder.append("&ai=").append(URLEncoder.encode(this.context.getPackageName(), "UTF-8")).toString();
                    if (Defines.ENABLE_lOGGING) {
                        Log.i("vipul", new StringBuilder("Application package name is ").append(this.context.getPackageName()).toString());
                    }
                }
            } catch (Exception e16) {
                e16.printStackTrace();
            }
            try {
                PackageInfo pInfo = this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 0);
                if (pInfo != null) {
                    String appVersion = pInfo.versionName;
                    if (!TextUtils.isEmpty(appVersion)) {
                        stringBuilder = stringBuilder;
                        str = String.valueOf(this.configUrl);
                        this.configUrl = stringBuilder.append("&av=").append(URLEncoder.encode(appVersion, "UTF-8")).toString();
                    }
                }
            } catch (UnsupportedEncodingException e17) {
                e17.printStackTrace();
            } catch (Exception e18) {
                e18.printStackTrace();
            }
            if (this.vservConfigBundle.containsKey("ro")) {
                try {
                    if (Defines.ENABLE_lOGGING) {
                        Log.i("abc", new StringBuilder("Sent RO to server as ").append(this.vservConfigBundle.getString("ro")).toString());
                    }
                    stringBuilder = stringBuilder;
                    str = String.valueOf(this.configUrl);
                    this.configUrl = stringBuilder.append("&ro=").append(URLEncoder.encode(this.vservConfigBundle.getString("ro"), "UTF-8")).toString();
                } catch (Exception e19) {
                    e19.printStackTrace();
                }
            }
            if (this.vservConfigBundle.containsKey("ao")) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", new StringBuilder("Using bundle ao value as ").append(this.vservConfigBundle.getString("ao")).toString());
                }
                stringBuilder = stringBuilder;
                str = String.valueOf(this.configUrl);
                this.configUrl = stringBuilder.append("&ao=").append(this.vservConfigBundle.getString("ao")).toString();
            } else {
                try {
                    String ao = URLEncoder.encode(this.context.getResources().getConfiguration().orientation == 1 ? "p" : "l", "UTF-8");
                    if (Defines.ENABLE_lOGGING) {
                        Log.i("vserv", new StringBuilder("Calculated ao value is ").append(ao).toString());
                    }
                    stringBuilder = stringBuilder;
                    str = String.valueOf(this.configUrl);
                    this.configUrl = stringBuilder.append("&ao=").append(ao).toString();
                } catch (Exception e20) {
                    e20.printStackTrace();
                }
            }
            try {
                stringBuilder = stringBuilder;
                str = String.valueOf(this.configUrl);
                this.configUrl = stringBuilder.append("&cea=").append(URLEncoder.encode("0", "UTF-8")).toString();
            } catch (Exception e21) {
                Exception e22 = e21;
                VservManager.getInstance(this.context).trackExceptions(e22, "cea");
                e22.printStackTrace();
            }
            if (VERSION.SDK_INT >= 9) {
                try {
                    if (!TextUtils.isEmpty(Build.SERIAL)) {
                        stringBuilder = stringBuilder;
                        str = String.valueOf(this.configUrl);
                        this.configUrl = stringBuilder.append("&sn=").append(URLEncoder.encode(Build.SERIAL, "UTF-8")).toString();
                    }
                } catch (Exception e23) {
                    e23.printStackTrace();
                }
            }
            try {
                if (this.context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == 0) {
                    ConnectivityManager connectivityManager = (ConnectivityManager) this.context.getSystemService("connectivity");
                    if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting()) {
                        String typeValue;
                        String connectedNetworkType = connectivityManager.getActiveNetworkInfo().getTypeName();
                        if (connectedNetworkType == null || !connectedNetworkType.trim().contains("WIFI")) {
                            typeValue = "2";
                        } else {
                            typeValue = "1";
                        }
                        try {
                            stringBuilder = stringBuilder;
                            str = String.valueOf(this.configUrl);
                            this.configUrl = stringBuilder.append("&ap=").append(URLEncoder.encode(typeValue, "UTF-8")).toString();
                        } catch (UnsupportedEncodingException e24) {
                            e24.printStackTrace();
                        }
                    }
                }
            } catch (Exception e25) {
                e25.printStackTrace();
            }
            try {
                if (this.context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == 0) {
                    String imeiORmeid = ((TelephonyManager) this.context.getSystemService("phone")).getDeviceId();
                    if (Defines.ENABLE_lOGGING) {
                        Log.i("vipul", new StringBuilder("IMEI number is ").append(imeiORmeid).toString());
                    }
                    if (!TextUtils.isEmpty(imeiORmeid)) {
                        stringBuilder = stringBuilder;
                        str = String.valueOf(this.configUrl);
                        this.configUrl = stringBuilder.append("&im=").append(URLEncoder.encode(imeiORmeid, "UTF-8")).toString();
                    }
                }
            } catch (Exception e26) {
                e26.printStackTrace();
            }
            try {
                if (this.context.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE") == 0) {
                    String macAddr = ((WifiManager) this.context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
                    if (!TextUtils.isEmpty(macAddr)) {
                        stringBuilder = stringBuilder;
                        str = String.valueOf(this.configUrl);
                        this.configUrl = stringBuilder.append("&ma=").append(URLEncoder.encode(macAddr, "UTF-8")).toString();
                    }
                }
            } catch (Exception e27) {
                e27.printStackTrace();
            }
            try {
                if (VERSION.SDK_INT >= 5 && this.context.checkCallingOrSelfPermission("android.permission.BLUETOOTH") == 0) {
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter != null) {
                        String bluetoothAddress = mBluetoothAdapter.getAddress();
                        if (!TextUtils.isEmpty(bluetoothAddress)) {
                            stringBuilder = stringBuilder;
                            str = String.valueOf(this.configUrl);
                            this.configUrl = stringBuilder.append("&bt=").append(URLEncoder.encode(bluetoothAddress, "UTF-8")).toString();
                        }
                    }
                }
            } catch (Exception e28) {
                e28.printStackTrace();
            }
            try {
                if (this.context.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0) {
                    location = ((LocationManager) this.context.getSystemService("location")).getLastKnownLocation("gps");
                    if (location != null) {
                        Double lat = Double.valueOf(location.getLatitude());
                        Double lon = Double.valueOf(location.getLongitude());
                        stringBuilder = stringBuilder;
                        str = String.valueOf(this.configUrl);
                        this.configUrl = stringBuilder.append("&lat=").append(URLEncoder.encode(String.valueOf(lat), "UTF-8")).toString();
                        stringBuilder = stringBuilder;
                        str = String.valueOf(this.configUrl);
                        this.configUrl = stringBuilder.append("&lon=").append(URLEncoder.encode(String.valueOf(lon), "UTF-8")).toString();
                    }
                }
            } catch (Exception e29) {
                e29.printStackTrace();
            }
            if (this.configUrl.indexOf("&lat") == -1 && this.configUrl.indexOf("&lon") == -1) {
                try {
                    location = this.context.getSystemService("location").getLastKnownLocation("network");
                    if (location != null) {
                        lat = Double.valueOf(location.getLatitude());
                        lon = Double.valueOf(location.getLongitude());
                        stringBuilder = stringBuilder;
                        str = String.valueOf(this.configUrl);
                        this.configUrl = stringBuilder.append("&lat=").append(URLEncoder.encode(String.valueOf(lat), "UTF-8")).toString();
                        stringBuilder = stringBuilder;
                        str = String.valueOf(this.configUrl);
                        this.configUrl = stringBuilder.append("&lon=").append(URLEncoder.encode(String.valueOf(lon), "UTF-8")).toString();
                    }
                } catch (Exception e30) {
                    e30.printStackTrace();
                }
            }
        } catch (Exception e31) {
            e31.printStackTrace();
        }
    }

    private void extractParameters() {
        try {
            if (VERSION.SDK_INT >= 10) {
                this.configUrl = new StringBuilder(String.valueOf(this.configUrl)).append("&h5=1").toString();
            }
            setAdConfigurationParameters();
            String serviceName = "phone";
            TelephonyManager telephonyManager = null;
            try {
                if (this.vservAdManageractivity != null) {
                    telephonyManager = (TelephonyManager) this.vservAdManageractivity.getSystemService(serviceName);
                } else {
                    telephonyManager = (TelephonyManager) this.context.getSystemService(serviceName);
                }
            } catch (Exception e) {
            }
            try {
                String networkOperator = telephonyManager.getNetworkOperator();
                if (!(networkOperator == null || networkOperator.equals(Preconditions.EMPTY_ARGUMENTS))) {
                    this.configUrl = new StringBuilder(String.valueOf(this.configUrl)).append("&no=").append(URLEncoder.encode(networkOperator, "UTF-8")).toString();
                }
            } catch (Exception e2) {
            }
            try {
                String simOperator = telephonyManager.getSimOperator();
                if (!(simOperator == null || simOperator.equals(Preconditions.EMPTY_ARGUMENTS))) {
                    this.configUrl = new StringBuilder(String.valueOf(this.configUrl)).append("&so=").append(URLEncoder.encode(simOperator, "UTF-8")).toString();
                }
            } catch (Exception e3) {
            }
            if (telephonyManager != null) {
                try {
                    if (telephonyManager.getPhoneType() == 1) {
                        if (((GsmCellLocation) telephonyManager.getCellLocation()) != null) {
                            int locationAreaCode = ((GsmCellLocation) telephonyManager.getCellLocation()).getLac();
                            if (locationAreaCode != -1) {
                                setLac("&lac=");
                                setLac(URLEncoder.encode(locationAreaCode, "UTF-8"));
                            }
                        }
                        if (!(telephonyManager == null || ((GsmCellLocation) telephonyManager.getCellLocation()) == null)) {
                            int cellId = ((GsmCellLocation) telephonyManager.getCellLocation()).getCid();
                            if (cellId != -1) {
                                setce("&ce=");
                                setce(URLEncoder.encode(cellId, "UTF-8"));
                            }
                        }
                    }
                } catch (Exception e4) {
                }
            }
            try {
                String locale = Locale.getDefault().getLanguage();
                if (locale != null) {
                    setLocale("&lc=");
                    setLocale(URLEncoder.encode(locale, "UTF-8"));
                }
            } catch (Exception e5) {
            }
            try {
                PackageManager pm = this.context.getPackageManager();
                String applicationName = (String) pm.getApplicationInfo(this.context.getPackageName(), TransportMediator.FLAG_KEY_MEDIA_NEXT).loadLabel(pm);
                if (applicationName != null) {
                    this.configUrl = new StringBuilder(String.valueOf(this.configUrl)).append("&mn=").append(URLEncoder.encode(applicationName, "UTF-8")).toString();
                }
            } catch (Exception e6) {
            }
        } catch (Exception e7) {
        }
    }

    private boolean getAdResponse(String url, String zoneId) {
        boolean z = false;
        InputStream is = null;
        try {
            is = makeHttpConnection(url, 1);
            if (is != null) {
                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                while (true) {
                    int len = is.read(buffer);
                    if (len == -1) {
                        cache.put(zoneId, byteBuffer.toByteArray());
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                return z;
                            }
                        }
                        return true;
                    } else {
                        byteBuffer.write(buffer, 0, len);
                    }
                }
            } else if (is == null) {
                return z;
            } else {
                try {
                    is.close();
                    return z;
                } catch (IOException e2) {
                    return z;
                }
            }
        } catch (Exception e3) {
            try {
                VservManager.getInstance(this.context).trackExceptions(e3, "getAdResponse");
                if (is == null) {
                    return z;
                }
                try {
                    is.close();
                    return z;
                } catch (IOException e4) {
                    return z;
                }
            } catch (Throwable th) {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e5) {
                        return z;
                    }
                }
            }
        }
    }

    @android.annotation.SuppressLint({"InlinedApi"})
    private void getMetaData() {
        throw new UnsupportedOperationException("Method not decompiled: mobi.vserv.android.ads.VservAdController.getMetaData():void");
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.JadxRuntimeException: Try/catch wrap count limit reached in mobi.vserv.android.ads.VservAdController.getMetaData():void
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:52)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:40)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:16)
	at jadx.core.ProcessClass.process(ProcessClass.java:22)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:209)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:133)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
	at java.lang.Thread.run(Thread.java:745)
*/
        /*
        r12 = this;
        r11 = 100;
        r10 = 0;
        r9 = 1;
        r6 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;
        if (r6 == 0) goto L_0x000f;
    L_0x0008:
        r6 = "vserv";
        r7 = "Calling getMetaData inside function";
        android.util.Log.i(r6, r7);
    L_0x000f:
        r6 = r12.context;
        r6 = mobi.vserv.android.ads.VservManager.getInstance(r6);
        r7 = r12.zoneId;
        r6.setZoneID(r7);
        r6 = r12.vservConfigBundle;
        r7 = "ro";
        r6 = r6.containsKey(r7);
        if (r6 == 0) goto L_0x0035;
    L_0x0024:
        r6 = r12.context;
        r6 = mobi.vserv.android.ads.VservManager.getInstance(r6);
        r7 = r12.vservConfigBundle;
        r8 = "ro";
        r7 = r7.getString(r8);
        r6.setOrientation(r7);
    L_0x0035:
        r6 = r12.vservConfigBundle;
        r7 = "zoneId";
        r6 = r6.containsKey(r7);
        if (r6 == 0) goto L_0x038b;
    L_0x003f:
        r6 = r12.vservConfigBundle;
        r7 = "adType";
        r6 = r6.containsKey(r7);
        if (r6 == 0) goto L_0x012d;
    L_0x0049:
        r6 = r12.vservConfigBundle;
        r7 = "adType";
        r6 = r6.getString(r7);
        r7 = "show";
        r6 = r6.equalsIgnoreCase(r7);
        if (r6 != 0) goto L_0x0069;
    L_0x0059:
        r6 = r12.vservConfigBundle;
        r7 = "adType";
        r6 = r6.getString(r7);
        r7 = "overlay";
        r6 = r6.equalsIgnoreCase(r7);
        if (r6 == 0) goto L_0x012d;
    L_0x0069:
        r6 = r12.cachedObject;
        if (r6 != 0) goto L_0x0077;
    L_0x006d:
        r6 = r12.iAddCallback;
        if (r6 == 0) goto L_0x0077;
    L_0x0071:
        r6 = r12.iAddCallback;
        r6.onNoFill();
    L_0x0076:
        return;
    L_0x0077:
        r6 = r12.context;
        r7 = "android.permission.ACCESS_NETWORK_STATE";
        r6 = r6.checkCallingOrSelfPermission(r7);
        if (r6 != 0) goto L_0x011d;
    L_0x0081:
        r6 = r12.context;
        r7 = "connectivity";
        r0 = r6.getSystemService(r7);
        r0 = (android.net.ConnectivityManager) r0;
        r6 = r0.getActiveNetworkInfo();
        if (r6 == 0) goto L_0x00a1;
    L_0x0091:
        r6 = r0.getActiveNetworkInfo();
        r6 = r6.isConnectedOrConnecting();
        if (r6 == 0) goto L_0x00a1;
    L_0x009b:
        r12.isDisplayGetAdEnabled = r9;
        r12.isShowAdRequest = r9;
        r12.adStatus = r9;
    L_0x00a1:
        r6 = 0;
        r12.connectionStartTime = r6;
        r12.connectionTimedOut = r10;
        r6 = 3;
        r12.page = r6;
        r6 = r12.handler;
        r7 = r12.updateResults;
        r6.post(r7);
        r6 = r12.adStatus;
        if (r6 == 0) goto L_0x031b;
    L_0x00b5:
        r6 = r12.vservAdManageractivity;
        if (r6 == 0) goto L_0x00bd;
    L_0x00b9:
        r6 = r12.vservAdManageractivity;
        r6.dataConnectionAvailable = r9;
    L_0x00bd:
        r6 = r12.isDisplayGetAdEnabled;
        if (r6 == 0) goto L_0x021a;
    L_0x00c1:
        r6 = r12.cachedObject;
        r6 = r6 instanceof java.lang.String;
        if (r6 == 0) goto L_0x0215;
    L_0x00c7:
        r6 = r12.cachedObject;
        r6 = (java.lang.String) r6;
        r6 = r6.getBytes();
        r12.setAdvsFlow(r6);
    L_0x00d2:
        r6 = r12.vservConfigBundle;
        r7 = "cacheNextAd";
        r6 = r6.containsKey(r7);
        if (r6 == 0) goto L_0x0076;
    L_0x00dc:
        r6 = r12.vservConfigBundle;
        r7 = "cacheNextAd";
        r6 = r6.getBoolean(r7);
        if (r6 == 0) goto L_0x0076;
    L_0x00e6:
        r6 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;
        if (r6 == 0) goto L_0x00f1;
    L_0x00ea:
        r6 = "Vserv";
        r7 = "!!!!!!!!Caching next ad for user";
        android.util.Log.i(r6, r7);
    L_0x00f1:
        r6 = r12.vservConfigBundle;
        r7 = "showAt";
        r5 = r6.getString(r7);
        r6 = "inapp";
        r6 = r5.contains(r6);
        if (r6 != 0) goto L_0x0076;
    L_0x0101:
        r6 = "end";
        r6 = r5.contains(r6);
        if (r6 != 0) goto L_0x0076;
    L_0x0109:
        r6 = r12.vservConfigBundle;
        r7 = "adType";
        r6 = r6.containsKey(r7);
        if (r6 != 0) goto L_0x0076;
    L_0x0113:
        r6 = new mobi.vserv.android.ads.VservAdController$5;
        r6.<init>();
        r6.start();
        goto L_0x0076;
    L_0x011d:
        r6 = r12.iAddCallback;
        if (r6 == 0) goto L_0x0128;
    L_0x0121:
        r6 = r12.iAddCallback;
        r6.onLoadFailure();
        goto L_0x0076;
    L_0x0128:
        r12.loadMainApp(r11, r10);
        goto L_0x0076;
    L_0x012d:
        r6 = r12.vservAdManageractivity;
        if (r6 == 0) goto L_0x0166;
    L_0x0131:
        r6 = r12.cachedObject;
        if (r6 == 0) goto L_0x0166;
    L_0x0135:
        r6 = r12.context;
        r7 = "android.permission.ACCESS_NETWORK_STATE";
        r6 = r6.checkCallingOrSelfPermission(r7);
        if (r6 != 0) goto L_0x0161;
    L_0x013f:
        r6 = r12.context;
        r7 = "connectivity";
        r0 = r6.getSystemService(r7);
        r0 = (android.net.ConnectivityManager) r0;
        r6 = r0.getActiveNetworkInfo();
        if (r6 == 0) goto L_0x00a1;
    L_0x014f:
        r6 = r0.getActiveNetworkInfo();
        r6 = r6.isConnectedOrConnecting();
        if (r6 == 0) goto L_0x00a1;
    L_0x0159:
        r12.isCachingEnabled = r9;
        r12.isDisplayAdHit = r9;
        r12.adStatus = r9;
        goto L_0x00a1;
    L_0x0161:
        r12.loadMainApp(r11, r10);
        goto L_0x0076;
    L_0x0166:
        r6 = r12.isDisplayFrameAd;
        if (r6 == 0) goto L_0x019f;
    L_0x016a:
        r6 = r12.cachedObject;
        if (r6 == 0) goto L_0x019f;
    L_0x016e:
        r6 = r12.context;
        r7 = "android.permission.ACCESS_NETWORK_STATE";
        r6 = r6.checkCallingOrSelfPermission(r7);
        if (r6 != 0) goto L_0x019a;
    L_0x0178:
        r6 = r12.context;
        r7 = "connectivity";
        r0 = r6.getSystemService(r7);
        r0 = (android.net.ConnectivityManager) r0;
        r6 = r0.getActiveNetworkInfo();
        if (r6 == 0) goto L_0x00a1;
    L_0x0188:
        r6 = r0.getActiveNetworkInfo();
        r6 = r6.isConnectedOrConnecting();
        if (r6 == 0) goto L_0x00a1;
    L_0x0192:
        r12.isCachingEnabled = r9;
        r12.isDisplayAdHit = r9;
        r12.adStatus = r9;
        goto L_0x00a1;
    L_0x019a:
        r12.loadMainApp(r11, r10);
        goto L_0x0076;
    L_0x019f:
        r6 = r12.vservConfigBundle;
        r7 = "showAt";
        r3 = r6.getString(r7);
        r6 = r12.vservConfigBundle;
        r7 = "getAdPos";
        r6 = r6.containsKey(r7);
        if (r6 == 0) goto L_0x01b9;
    L_0x01b1:
        r6 = r12.vservConfigBundle;
        r7 = "getAdPos";
        r3 = r6.getString(r7);
    L_0x01b9:
        r6 = r3.trim();
        r7 = "mid";
        r6 = r6.contains(r7);
        if (r6 != 0) goto L_0x01d1;
    L_0x01c5:
        r6 = r3.trim();
        r7 = "inapp";
        r6 = r6.contains(r7);
        if (r6 == 0) goto L_0x01d3;
    L_0x01d1:
        r3 = "in";
    L_0x01d3:
        r6 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;
        if (r6 == 0) goto L_0x01de;
    L_0x01d7:
        r6 = "ad";
        r7 = "Calling getAdResponse for non cache ad";
        android.util.Log.i(r6, r7);
    L_0x01de:
        r6 = new java.lang.StringBuilder;
        r7 = r12.configUrl;
        r7 = java.lang.String.valueOf(r7);
        r6.<init>(r7);
        r7 = "&showat=";
        r6 = r6.append(r7);
        r7 = java.net.URLEncoder.encode(r3);
        r6 = r6.append(r7);
        r7 = "&zoneid=";
        r6 = r6.append(r7);
        r7 = r12.zoneId;
        r7 = java.net.URLEncoder.encode(r7);
        r6 = r6.append(r7);
        r6 = r6.toString();
        r7 = r12.zoneId;
        r6 = r12.getAdResponse(r6, r7);
        r12.adStatus = r6;
        goto L_0x00a1;
    L_0x0215:
        r12.setAdvertisementPage();
        goto L_0x00d2;
    L_0x021a:
        r6 = r12.vservAdManageractivity;
        if (r6 != 0) goto L_0x02e7;
    L_0x021e:
        r6 = r12.isRenderAd;
        if (r6 == 0) goto L_0x0237;
    L_0x0222:
        r2 = 0;
        r6 = cache;
        r7 = r12.zoneId;
        r2 = r6.get(r7);
        r2 = (byte[]) r2;
        r6 = 0;
        r12.adComponentView = r6;
        r12.isDisplayAdHit = r9;
        r12.setAdvsFlow(r2);
        goto L_0x00d2;
    L_0x0237:
        r6 = r12.isDisplayFrameAd;
        if (r6 == 0) goto L_0x026b;
    L_0x023b:
        r6 = r12.cachedObject;
        if (r6 != 0) goto L_0x0251;
    L_0x023f:
        r2 = 0;
        r6 = cache;
        r7 = r12.zoneId;
        r2 = r6.get(r7);
        r2 = (byte[]) r2;
        r12.isDisplayAdHit = r9;
        r12.setAdvsFlow(r2);
        goto L_0x00d2;
    L_0x0251:
        r6 = r12.cachedObject;
        r6 = r6 instanceof java.lang.String;
        if (r6 == 0) goto L_0x0266;
    L_0x0257:
        r12.isDisplayAdHit = r9;
        r6 = r12.cachedObject;
        r6 = (java.lang.String) r6;
        r6 = r6.getBytes();
        r12.setAdvsFlow(r6);
        goto L_0x00d2;
    L_0x0266:
        r12.setAdvertisementPage();
        goto L_0x00d2;
    L_0x026b:
        r6 = r12.isMediaCacheDisabled;
        if (r6 == 0) goto L_0x02d3;
    L_0x026f:
        r6 = r12.isMediaCacheDisabled;
        r7 = "1";
        r6 = r6.equals(r7);
        if (r6 == 0) goto L_0x02d3;
    L_0x0279:
        r2 = 0;
        r6 = cache;
        r7 = r12.zoneId;
        r2 = r6.get(r7);
        r2 = (byte[]) r2;
        r4 = 0;
        r6 = r12.decodeResponse;
        if (r6 == 0) goto L_0x02cd;
    L_0x0289:
        r12.decodeResponse = r10;
        r4 = r12.decompress(r2);	 Catch:{ IOException -> 0x02c8 }
    L_0x028f:
        r12.isCachingEnabled = r10;
        r12.isgetAdRequest = r10;
        r6 = r12.context;
        r6 = mobi.vserv.android.ads.VservManager.getInstance(r6);
        r6.setAdview(r4);
        r6 = r12.requestId;
        if (r6 == 0) goto L_0x02ab;
    L_0x02a0:
        r6 = r12.context;
        r6 = mobi.vserv.android.ads.VservManager.getInstance(r6);
        r7 = r12.requestId;
        r6.setCacheAdRequestId(r7);
    L_0x02ab:
        r6 = r12.context;
        r6 = mobi.vserv.android.ads.VservManager.getInstance(r6);
        r7 = r12.skipDelay;
        r6.setCacheAdSkipDelay(r7);
        r6 = r12.isCacheHitInitiated;
        if (r6 != 0) goto L_0x00d2;
    L_0x02ba:
        r6 = r12.context;
        r6 = (android.app.Activity) r6;
        r7 = new mobi.vserv.android.ads.VservAdController$4;
        r7.<init>();
        r6.runOnUiThread(r7);
        goto L_0x00d2;
    L_0x02c8:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x028f;
    L_0x02cd:
        r4 = new java.lang.String;
        r4.<init>(r2);
        goto L_0x028f;
    L_0x02d3:
        r2 = 0;
        r6 = cache;
        r7 = r12.zoneId;
        r2 = r6.get(r7);
        r2 = (byte[]) r2;
        r12.isCachingEnabled = r9;
        r12.isgetAdRequest = r9;
        r12.setAdvsFlow(r2);
        goto L_0x00d2;
    L_0x02e7:
        r6 = r12.vservAdManageractivity;
        if (r6 == 0) goto L_0x0301;
    L_0x02eb:
        r6 = r12.cachedObject;
        if (r6 != 0) goto L_0x0301;
    L_0x02ef:
        r2 = 0;
        r6 = cache;
        r7 = r12.zoneId;
        r2 = r6.get(r7);
        r2 = (byte[]) r2;
        r12.isDisplayAdHit = r9;
        r12.setAdvsFlow(r2);
        goto L_0x00d2;
    L_0x0301:
        r6 = r12.cachedObject;
        r6 = r6 instanceof java.lang.String;
        if (r6 == 0) goto L_0x0316;
    L_0x0307:
        r12.isDisplayAdHit = r9;
        r6 = r12.cachedObject;
        r6 = (java.lang.String) r6;
        r6 = r6.getBytes();
        r12.setAdvsFlow(r6);
        goto L_0x00d2;
    L_0x0316:
        r12.setAdvertisementPage();
        goto L_0x00d2;
    L_0x031b:
        r6 = r12.vservConfigBundle;
        r7 = "viewMandatory";
        r6 = r6.containsKey(r7);
        if (r6 == 0) goto L_0x0354;
    L_0x0325:
        r6 = r12.vservConfigBundle;
        r7 = "viewMandatory";
        r6 = r6.getString(r7);
        r7 = "true";
        r6 = r6.equalsIgnoreCase(r7);
        if (r6 == 0) goto L_0x0354;
    L_0x0335:
        r6 = r12.vservConfigBundle;
        r7 = "showAt";
        r6 = r6.getString(r7);
        r7 = "start";
        r6 = r6.equalsIgnoreCase(r7);
        if (r6 == 0) goto L_0x0354;
    L_0x0345:
        r6 = r12.vservAdManageractivity;
        if (r6 == 0) goto L_0x0076;
    L_0x0349:
        r6 = r12.vservAdManageractivity;
        r6 = r6.runExitInstance;
        if (r6 != 0) goto L_0x0076;
    L_0x034f:
        r12.showViewMandatory();
        goto L_0x0076;
    L_0x0354:
        r6 = r12.vservConfigBundle;
        r7 = "adType";
        r6 = r6.containsKey(r7);
        if (r6 == 0) goto L_0x036e;
    L_0x035e:
        r6 = r12.vservConfigBundle;
        r7 = "adType";
        r6 = r6.getString(r7);
        r7 = "getAd";
        r6 = r6.equalsIgnoreCase(r7);
        if (r6 != 0) goto L_0x0378;
    L_0x036e:
        r6 = r12.vservConfigBundle;
        r7 = "refreshInterval";
        r6 = r6.containsKey(r7);
        if (r6 == 0) goto L_0x0386;
    L_0x0378:
        r6 = r12.context;
        r6 = (android.app.Activity) r6;
        r7 = new mobi.vserv.android.ads.VservAdController$6;
        r7.<init>();
        r6.runOnUiThread(r7);
        goto L_0x0076;
    L_0x0386:
        r12.loadMainApp(r11, r10);
        goto L_0x0076;
    L_0x038b:
        r12.loadMainApp(r11, r10);
        goto L_0x0076;
        */
    }

    private String getModifiedHTML(String html) {
        String image = new String();
        if (html.indexOf("file:///android_asset/") == -1) {
            return html;
        }
        int i = html.indexOf("file:///android_asset/");
        int j = html.indexOf("\"", i);
        if (!(i == -1 || j == -1 || i == j)) {
            image = html.substring(i + 22, j);
        }
        if (getTag(image) == null) {
            return null;
        }
        StringBuffer str = new StringBuffer();
        str.append(html.substring(0, i + 22));
        str.append(getStaticImageName(image));
        str.append(html.substring(j));
        return new String(str);
    }

    private String getRedirectUrl(String url) {
        String networkUrl = url;
        while (true) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(networkUrl).openConnection();
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("GET");
                connection.connect();
                int response = connection.getResponseCode();
                if (Defines.ENABLE_lOGGING) {
                    Log.i("abc", new StringBuilder("Response code is ").append(response).toString());
                }
                if (response != 307 && response != 300 && response != 301 && response != 302 && response != 303) {
                    return networkUrl;
                }
                networkUrl = connection.getHeaderField("location");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    private String getStaticImageName(String name) {
        int Width;
        int Height;
        if (this.vservAdManageractivity != null) {
            Width = VservAdManager.width;
            Height = VservAdManager.height;
        } else {
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) this.context).getWindowManager().getDefaultDisplay().getMetrics(dm);
            Width = dm.widthPixels;
            Height = dm.heightPixels;
        }
        if (Defines.ENABLE_lOGGING) {
            Log.i("testing", new StringBuilder("Width ").append(this.width).append("\nHeight ").append(this.height).toString());
        }
        int closestWidth = 0;
        int closestHeight = 0;
        String[] screenResolutionsStr = split(this.vservConfigBundle.get("supportedScreens").toString(), "|");
        int i = 0;
        while (i < screenResolutionsStr.length) {
            if (Defines.ENABLE_lOGGING) {
                Log.i("testing", new StringBuilder("Height & Width array is ").append(screenResolutionsStr[i]).toString());
            }
            i++;
        }
        int[] supportedWidths = new int[screenResolutionsStr.length];
        int[] supportedHeights = new int[screenResolutionsStr.length];
        i = 0;
        while (i < screenResolutionsStr.length) {
            String[] wh = split(screenResolutionsStr[i].toLowerCase(), "x");
            supportedWidths[i] = Integer.parseInt(String.valueOf(wh[0]));
            supportedHeights[i] = Integer.parseInt(String.valueOf(wh[1]));
            i++;
        }
        i = 0;
        while (i < screenResolutionsStr.length - 1) {
            String temp = screenResolutionsStr[i];
            int tempWidth = supportedWidths[i];
            int tempWidth1 = supportedWidths[i];
            int tempHeight1 = supportedHeights[i];
            int index = i;
            int j = i;
            while (j < screenResolutionsStr.length - 1) {
                if (supportedWidths[j + 1] > tempWidth) {
                    tempWidth = supportedWidths[j + 1];
                    index = j + 1;
                }
                j++;
            }
            screenResolutionsStr[i] = screenResolutionsStr[index];
            supportedWidths[i] = supportedWidths[index];
            supportedHeights[i] = supportedHeights[index];
            screenResolutionsStr[index] = temp;
            supportedWidths[index] = tempWidth1;
            supportedHeights[index] = tempHeight1;
            i++;
        }
        i = 0;
        while (i < screenResolutionsStr.length - 1) {
            temp = screenResolutionsStr[i];
            int tempHeight = supportedHeights[i];
            tempHeight1 = supportedHeights[i];
            tempWidth1 = supportedWidths[i];
            index = i;
            j = i;
            while (j < screenResolutionsStr.length - 1) {
                if (supportedHeights[j + 1] > tempHeight && supportedWidths[j + 1] == tempWidth1) {
                    tempHeight = supportedHeights[j + 1];
                    tempWidth1 = supportedWidths[j + 1];
                    index = j + 1;
                }
                j++;
            }
            screenResolutionsStr[i] = screenResolutionsStr[index];
            supportedWidths[i] = supportedWidths[index];
            supportedHeights[i] = supportedHeights[index];
            screenResolutionsStr[index] = temp;
            supportedWidths[index] = tempWidth1;
            supportedHeights[index] = tempHeight1;
            i++;
        }
        i = 0;
        while (i < screenResolutionsStr.length) {
            if (Width >= supportedWidths[i] && Height >= supportedHeights[i]) {
                closestWidth = supportedWidths[i];
                closestHeight = supportedHeights[i];
                break;
            } else {
                i++;
            }
        }
        if (closestWidth == 0 || closestHeight == 0 || Width == 0 || Height == 0) {
            closestWidth = supportedWidths[screenResolutionsStr.length - 1];
            closestHeight = supportedHeights[screenResolutionsStr.length - 1];
        }
        String[] img = split(name, ".");
        String imgFormat = img[img.length - 1];
        String tempUrl = Preconditions.EMPTY_ARGUMENTS;
        i = 0;
        while (i < img.length - 1) {
            tempUrl = new StringBuilder(String.valueOf(tempUrl)).append(img[i]).append(".").toString();
            i++;
        }
        String imgUrl = tempUrl.substring(0, tempUrl.length() - 1);
        boolean imagePresent = isImageInAsset(new StringBuilder(String.valueOf(imgUrl)).append("_").append(closestWidth).append("x").append(closestHeight).append(".").append(imgFormat).toString());
        name = new StringBuilder(String.valueOf(imgUrl)).append("_").append(closestWidth).append("x").append(closestHeight).append(".").append(imgFormat).toString();
        if (!imagePresent) {
            imagePresent = isImageInAsset(new StringBuilder(String.valueOf(imgUrl)).append("_").append(closestWidth).append("X").append(closestHeight).append(".").append(imgFormat).toString());
            name = new StringBuilder(String.valueOf(imgUrl)).append("_").append(closestWidth).append("X").append(closestHeight).append(".").append(imgFormat).toString();
        }
        if (imagePresent) {
            return name;
        }
        imagePresent = isImageInAsset(new StringBuilder(String.valueOf(imgUrl)).append(".").append(imgFormat).toString());
        return new StringBuilder(String.valueOf(imgUrl)).append(".").append(imgFormat).toString();
    }

    private String getTag(String fileName) {
        InputStream is = null;
        try {
            if (this.vservAdManageractivity != null) {
                is = this.vservAdManageractivity.getAssets().open(fileName);
            } else {
                is = this.context.getAssets().open(fileName);
            }
            if (is == null || is.available() == 0) {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e) {
                    }
                }
                return null;
            } else {
                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                while (true) {
                    int len = is.read(buffer);
                    if (len == -1) {
                        break;
                    }
                    byteBuffer.write(buffer, 0, len);
                }
                String data = new String(byteBuffer.toByteArray()).replaceAll("\n", Preconditions.EMPTY_ARGUMENTS).replaceAll("\r", Preconditions.EMPTY_ARGUMENTS);
                if (is == null) {
                    return data;
                }
                try {
                    is.close();
                    return data;
                } catch (Exception e2) {
                    return data;
                }
            }
        } catch (Exception e3) {
            try {
                e3.printStackTrace();
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e4) {
                    }
                }
            } catch (Throwable th) {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e5) {
                    }
                }
            }
        }
    }

    @SuppressLint({"InlinedApi"})
    private boolean isAdBlockingEnabled() {
        SharedPreferences blockPreference;
        if (VERSION.SDK_INT >= 11) {
            blockPreference = this.context.getSharedPreferences(this.vservConfigBundle.getString("preferenceName"), MMAdView.TRANSITION_RANDOM);
        } else {
            blockPreference = this.context.getSharedPreferences(this.vservConfigBundle.getString("preferenceName"), 0);
        }
        String flagDataType = this.vservConfigBundle.getString("flagDataType");
        String targetValue;
        String preferenceValue;
        if (blockPreference.contains(this.vservConfigBundle.getString("preAdFlagName"))) {
            if (flagDataType.equals("boolean")) {
                if (blockPreference.getBoolean(this.vservConfigBundle.getString("preAdFlagName"), false) == Boolean.parseBoolean(this.vservConfigBundle.getString("flagValueToCompare"))) {
                    return true;
                }
            } else if (flagDataType.equals("int")) {
                if (blockPreference.getInt(this.vservConfigBundle.getString("preAdFlagName"), 0) == Integer.parseInt(this.vservConfigBundle.getString("flagValueToCompare"))) {
                    return true;
                }
            } else if (flagDataType.equals("string")) {
                targetValue = this.vservConfigBundle.getString("flagValueToCompare");
                preferenceValue = blockPreference.getString(this.vservConfigBundle.getString("preAdFlagName"), Preconditions.EMPTY_ARGUMENTS);
                if (preferenceValue.length() > 0 && preferenceValue.equalsIgnoreCase(targetValue)) {
                    return true;
                }
            } else if (flagDataType.equals("float")) {
                if (blockPreference.getFloat(this.vservConfigBundle.getString("preAdFlagName"), BitmapDescriptorFactory.HUE_RED) == Float.parseFloat(this.vservConfigBundle.getString("flagValueToCompare"))) {
                    return true;
                }
            }
        } else if (blockPreference.contains(this.vservConfigBundle.getString("postAdFlagName"))) {
            if (flagDataType.equals("boolean")) {
                if (blockPreference.getBoolean(this.vservConfigBundle.getString("postAdFlagName"), false) == Boolean.parseBoolean(this.vservConfigBundle.getString("flagValueToCompare"))) {
                    return true;
                }
            } else if (flagDataType.equals("int")) {
                if (blockPreference.getInt(this.vservConfigBundle.getString("postAdFlagName"), 0) == Integer.parseInt(this.vservConfigBundle.getString("flagValueToCompare"))) {
                    return true;
                }
            } else if (flagDataType.equals("string")) {
                targetValue = this.vservConfigBundle.getString("flagValueToCompare");
                preferenceValue = blockPreference.getString(this.vservConfigBundle.getString("postAdFlagName"), Preconditions.EMPTY_ARGUMENTS);
                if (preferenceValue.length() > 0 && preferenceValue.equalsIgnoreCase(targetValue)) {
                    return true;
                }
            } else if (flagDataType.equals("float")) {
                if (blockPreference.getFloat(this.vservConfigBundle.getString("postAdFlagName"), BitmapDescriptorFactory.HUE_RED) == Float.parseFloat(this.vservConfigBundle.getString("flagValueToCompare"))) {
                    return true;
                }
            }
        } else if (this.vservConfigBundle.getString("showAdsOnNoFlags").contains("false")) {
            return true;
        }
        return false;
    }

    private boolean isImageInAsset(String strName) {
        try {
            InputStream istr = this.context.getAssets().open(strName);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void loadOrmmaJavaScriptFiles(Context context, String fileName) {
        new AnonymousClass_2(context, fileName).start();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @android.annotation.SuppressLint({"InlinedApi"})
    private java.io.InputStream makeHttpConnection(java.lang.String r39_requestUrl, int r40_requestCode) {
        throw new UnsupportedOperationException("Method not decompiled: mobi.vserv.android.ads.VservAdController.makeHttpConnection(java.lang.String, int):java.io.InputStream");
        /*
        r38 = this;
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;
        if (r33 == 0) goto L_0x001c;
    L_0x0004:
        r33 = "vserv";
        r34 = new java.lang.StringBuilder;
        r35 = "Calling makeHttpConnection on ";
        r34.<init>(r35);
        r0 = r34;
        r1 = r39;
        r34 = r0.append(r1);
        r34 = r34.toString();
        android.util.Log.i(r33, r34);
    L_0x001c:
        r17 = 0;
        r21 = 0;
        r23 = "";
        r22 = "";
        r0 = r38;
        r0 = r0.context;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r34 = "connectivity";
        r9 = r33.getSystemService(r34);	 Catch:{ Exception -> 0x0248 }
        r9 = (android.net.ConnectivityManager) r9;	 Catch:{ Exception -> 0x0248 }
        r33 = r9.getActiveNetworkInfo();	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x10f1;
    L_0x0038:
        r33 = r9.getActiveNetworkInfo();	 Catch:{ Exception -> 0x0248 }
        r33 = r33.isConnectedOrConnecting();	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x10df;
    L_0x0042:
        r18 = r38.isInternetReachable();	 Catch:{ Exception -> 0x0248 }
        if (r18 == 0) goto L_0x10cd;
    L_0x0048:
        r26 = -1;
        r33 = 1;
        r0 = r40;
        r1 = r33;
        if (r0 != r1) goto L_0x00ac;
    L_0x0052:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x005d;
    L_0x0056:
        r33 = "context";
        r34 = "Trying to fetch ADD";
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x005d:
        r33 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r34 = 0;
        r35 = "?";
        r0 = r39;
        r1 = r35;
        r35 = r0.indexOf(r1);	 Catch:{ Exception -> 0x0248 }
        r0 = r39;
        r1 = r34;
        r2 = r35;
        r34 = r0.substring(r1, r2);	 Catch:{ Exception -> 0x0248 }
        r34 = java.lang.String.valueOf(r34);	 Catch:{ Exception -> 0x0248 }
        r33.<init>(r34);	 Catch:{ Exception -> 0x0248 }
        r34 = "?";
        r33 = r33.append(r34);	 Catch:{ Exception -> 0x0248 }
        r0 = r38;
        r0 = r0.appId;	 Catch:{ Exception -> 0x0248 }
        r34 = r0;
        r33 = r33.append(r34);	 Catch:{ Exception -> 0x0248 }
        r23 = r33.toString();	 Catch:{ Exception -> 0x0248 }
        r33 = "?";
        r0 = r39;
        r1 = r33;
        r33 = r0.indexOf(r1);	 Catch:{ Exception -> 0x0248 }
        r33 = r33 + 1;
        r0 = r39;
        r1 = r33;
        r33 = r0.substring(r1);	 Catch:{ Exception -> 0x0248 }
        r22 = r33.trim();	 Catch:{ Exception -> 0x0248 }
        r39 = r23.trim();	 Catch:{ Exception -> 0x0248 }
    L_0x00ac:
        r16 = 0;
        r8 = 0;
        r31 = new java.net.URL;	 Catch:{ Exception -> 0x0248 }
        r0 = r31;
        r1 = r39;
        r0.<init>(r1);	 Catch:{ Exception -> 0x0248 }
        r8 = r31.openConnection();	 Catch:{ Exception -> 0x0248 }
        r0 = r38;
        r0 = r0.userAgent;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        if (r33 == 0) goto L_0x00d3;
    L_0x00c4:
        r33 = "User-Agent";
        r0 = r38;
        r0 = r0.userAgent;	 Catch:{ Exception -> 0x0248 }
        r34 = r0;
        r0 = r33;
        r1 = r34;
        r8.setRequestProperty(r0, r1);	 Catch:{ Exception -> 0x0248 }
    L_0x00d3:
        r33 = 1;
        r0 = r40;
        r1 = r33;
        if (r0 != r1) goto L_0x023a;
    L_0x00db:
        r0 = r38;
        r0 = r0.contextAdPreference;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r34 = "context";
        r35 = "";
        r33 = r33.getString(r34, r35);	 Catch:{ Exception -> 0x0248 }
        r33 = r33.length();	 Catch:{ Exception -> 0x0248 }
        if (r33 <= 0) goto L_0x0144;
    L_0x00ef:
        r33 = "X-VSERV-CONTEXT";
        r0 = r38;
        r0 = r0.contextAdPreference;	 Catch:{ Exception -> 0x0248 }
        r34 = r0;
        r35 = "context";
        r36 = "";
        r34 = r34.getString(r35, r36);	 Catch:{ Exception -> 0x0248 }
        r0 = r33;
        r1 = r34;
        r8.setRequestProperty(r0, r1);	 Catch:{ Exception -> 0x0248 }
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0133;
    L_0x010a:
        r33 = "VservAdController";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "Setting X-VSERV-CONTEXT to ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r0 = r38;
        r0 = r0.contextAdPreference;	 Catch:{ Exception -> 0x0248 }
        r35 = r0;
        r36 = "context";
        r37 = "";
        r35 = r35.getString(r36, r37);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
        r33 = "VservAdController";
        r34 = "Clearing Context!!!";
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0133:
        r0 = r38;
        r0 = r0.contextAdPreference;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r33 = r33.edit();	 Catch:{ Exception -> 0x0248 }
        r33 = r33.clear();	 Catch:{ Exception -> 0x0248 }
        r33.commit();	 Catch:{ Exception -> 0x0248 }
    L_0x0144:
        r0 = r38;
        r0 = r0.context;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r34 = "vserv_unique_add_app_lifecycle";
        r35 = 0;
        r24 = r33.getSharedPreferences(r34, r35);	 Catch:{ Exception -> 0x0248 }
        r33 = "key";
        r34 = "";
        r0 = r24;
        r1 = r33;
        r2 = r34;
        r32 = r0.getString(r1, r2);	 Catch:{ Exception -> 0x0248 }
        r33 = r32.trim();	 Catch:{ Exception -> 0x0248 }
        r33 = r33.length();	 Catch:{ Exception -> 0x0248 }
        if (r33 <= 0) goto L_0x018f;
    L_0x016a:
        r33 = "X-VSERV-STORE";
        r0 = r33;
        r1 = r32;
        r8.setRequestProperty(r0, r1);	 Catch:{ Exception -> 0x0248 }
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x018f;
    L_0x0177:
        r33 = "VservAdController";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "Setting X-VSERV-STORE to ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r0 = r34;
        r1 = r32;
        r34 = r0.append(r1);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x018f:
        r33 = new java.io.File;	 Catch:{ Exception -> 0x0248 }
        r34 = android.os.Environment.getExternalStorageDirectory();	 Catch:{ Exception -> 0x0248 }
        r35 = ".addWrapper.txt";
        r33.<init>(r34, r35);	 Catch:{ Exception -> 0x0248 }
        r0 = r33;
        r1 = r38;
        r1.universalContextFile = r0;	 Catch:{ Exception -> 0x0248 }
        r15 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r15.<init>();	 Catch:{ Exception -> 0x0248 }
        r6 = 0;
        r33 = "AES";
        r5 = javax.crypto.Cipher.getInstance(r33);	 Catch:{ Exception -> 0x1117, all -> 0x02aa }
        r33 = 2;
        r34 = new javax.crypto.spec.SecretKeySpec;	 Catch:{ Exception -> 0x1117, all -> 0x02aa }
        r35 = "296@!$^&*!)~123#";
        r35 = r35.getBytes();	 Catch:{ Exception -> 0x1117, all -> 0x02aa }
        r36 = "AES";
        r34.<init>(r35, r36);	 Catch:{ Exception -> 0x1117, all -> 0x02aa }
        r0 = r33;
        r1 = r34;
        r5.init(r0, r1);	 Catch:{ Exception -> 0x1117, all -> 0x02aa }
        r7 = new javax.crypto.CipherInputStream;	 Catch:{ Exception -> 0x1117, all -> 0x02aa }
        r33 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x1117, all -> 0x02aa }
        r0 = r38;
        r0 = r0.universalContextFile;	 Catch:{ Exception -> 0x1117, all -> 0x02aa }
        r34 = r0;
        r33.<init>(r34);	 Catch:{ Exception -> 0x1117, all -> 0x02aa }
        r0 = r33;
        r7.<init>(r0, r5);	 Catch:{ Exception -> 0x1117, all -> 0x02aa }
    L_0x01d4:
        r25 = r7.read();	 Catch:{ Exception -> 0x02a1, all -> 0x1113 }
        r33 = -1;
        r0 = r25;
        r1 = r33;
        if (r0 != r1) goto L_0x0295;
    L_0x01e0:
        if (r7 == 0) goto L_0x01e5;
    L_0x01e2:
        r7.close();	 Catch:{ Exception -> 0x0248 }
    L_0x01e5:
        r6 = r7;
    L_0x01e6:
        r33 = r15.toString();	 Catch:{ Exception -> 0x0248 }
        r33 = r33.length();	 Catch:{ Exception -> 0x0248 }
        if (r33 <= 0) goto L_0x0219;
    L_0x01f0:
        r33 = "X-VSERV-UNIVERSAL";
        r34 = r15.toString();	 Catch:{ Exception -> 0x0248 }
        r0 = r33;
        r1 = r34;
        r8.setRequestProperty(r0, r1);	 Catch:{ Exception -> 0x0248 }
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0219;
    L_0x0201:
        r33 = "VservAdController";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "Setting X-VSERV-UNIVERSAL to ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = r15.toString();	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0219:
        r33 = "Content-Encoding";
        r34 = "gzip";
        r0 = r33;
        r1 = r34;
        r8.setRequestProperty(r0, r1);	 Catch:{ Exception -> 0x0248 }
        r33 = "Accept-Encoding";
        r34 = "gzip";
        r0 = r33;
        r1 = r34;
        r8.setRequestProperty(r0, r1);	 Catch:{ Exception -> 0x0248 }
        r33 = "Connection";
        r34 = "close";
        r0 = r33;
        r1 = r34;
        r8.setRequestProperty(r0, r1);	 Catch:{ Exception -> 0x0248 }
    L_0x023a:
        r0 = r8 instanceof java.net.HttpURLConnection;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        if (r33 != 0) goto L_0x02b1;
    L_0x0240:
        r33 = new java.io.IOException;	 Catch:{ Exception -> 0x0248 }
        r34 = "Not an HTTP connection";
        r33.<init>(r34);	 Catch:{ Exception -> 0x0248 }
        throw r33;	 Catch:{ Exception -> 0x0248 }
    L_0x0248:
        r13 = move-exception;
        r0 = r38;
        r0 = r0.context;
        r33 = r0;
        r33 = mobi.vserv.android.ads.VservManager.getInstance(r33);
        r34 = "makeHttpConnection";
        r0 = r33;
        r1 = r34;
        r0.trackExceptions(r13, r1);
        r13.printStackTrace();
        r33 = "vserv";
        r34 = new java.lang.StringBuilder;
        r35 = "Exception makehttp: ";
        r34.<init>(r35);
        r35 = r13.getMessage();
        r34 = r34.append(r35);
        r34 = r34.toString();
        android.util.Log.i(r33, r34);
        r33 = 0;
        r0 = r33;
        r2 = r38;
        r2.connectionStartTime = r0;
        r33 = 0;
        r0 = r33;
        r1 = r38;
        r1.connectionTimedOut = r0;
    L_0x0287:
        if (r17 != 0) goto L_0x0294;
    L_0x0289:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;
        if (r33 == 0) goto L_0x0294;
    L_0x028d:
        r33 = "vipul";
        r34 = "in is null";
        android.util.Log.i(r33, r34);
    L_0x0294:
        return r17;
    L_0x0295:
        r0 = r25;
        r0 = (char) r0;
        r33 = r0;
        r0 = r33;
        r15.append(r0);	 Catch:{ Exception -> 0x02a1, all -> 0x1113 }
        goto L_0x01d4;
    L_0x02a1:
        r33 = move-exception;
        r6 = r7;
    L_0x02a3:
        if (r6 == 0) goto L_0x01e6;
    L_0x02a5:
        r6.close();	 Catch:{ Exception -> 0x0248 }
        goto L_0x01e6;
    L_0x02aa:
        r33 = move-exception;
    L_0x02ab:
        if (r6 == 0) goto L_0x02b0;
    L_0x02ad:
        r6.close();	 Catch:{ Exception -> 0x0248 }
    L_0x02b0:
        throw r33;	 Catch:{ Exception -> 0x0248 }
    L_0x02b1:
        r0 = r8;
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x0248 }
        r16 = r0;
        r33 = 1;
        r0 = r40;
        r1 = r33;
        if (r0 != r1) goto L_0x0878;
    L_0x02be:
        r33 = 0;
        r0 = r16;
        r1 = r33;
        r0.setInstanceFollowRedirects(r1);	 Catch:{ Exception -> 0x0248 }
        r33 = "POST";
        r0 = r16;
        r1 = r33;
        r0.setRequestMethod(r1);	 Catch:{ Exception -> 0x0248 }
        r33 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r0 = r16;
        r1 = r33;
        r0.setConnectTimeout(r1);	 Catch:{ Exception -> 0x0248 }
        r33 = 1;
        r0 = r16;
        r1 = r33;
        r0.setDoOutput(r1);	 Catch:{ Exception -> 0x0248 }
        r33 = 1;
        r0 = r16;
        r1 = r33;
        r0.setDoInput(r1);	 Catch:{ Exception -> 0x0248 }
        r33 = "Content-Type";
        r34 = "application/x-www-form-urlencoded";
        r0 = r16;
        r1 = r33;
        r2 = r34;
        r0.setRequestProperty(r1, r2);	 Catch:{ Exception -> 0x0248 }
        r33 = "Content-length";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r34.<init>();	 Catch:{ Exception -> 0x0248 }
        r0 = r38;
        r1 = r22;
        r35 = r0.compress(r1);	 Catch:{ Exception -> 0x0248 }
        r0 = r35;
        r0 = r0.length;	 Catch:{ Exception -> 0x0248 }
        r35 = r0;
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        r0 = r16;
        r1 = r33;
        r2 = r34;
        r0.setRequestProperty(r1, r2);	 Catch:{ Exception -> 0x0248 }
        r21 = r16.getOutputStream();	 Catch:{ Exception -> 0x0248 }
        r0 = r38;
        r1 = r22;
        r33 = r0.compress(r1);	 Catch:{ Exception -> 0x0248 }
        r0 = r21;
        r1 = r33;
        r0.write(r1);	 Catch:{ Exception -> 0x0248 }
        r21.flush();	 Catch:{ Exception -> 0x0248 }
        r21.close();	 Catch:{ Exception -> 0x0248 }
    L_0x0336:
        r33 = 2;
        r0 = r40;
        r1 = r33;
        if (r0 == r1) goto L_0x035c;
    L_0x033e:
        r0 = r38;
        r0 = r0.connectionStartTime;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r35 = 0;
        r33 = (r33 > r35 ? 1 : (r33 == r35 ? 0 : -1));
        if (r33 != 0) goto L_0x035c;
    L_0x034a:
        r33 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0248 }
        r0 = r33;
        r2 = r38;
        r2.connectionStartTime = r0;	 Catch:{ Exception -> 0x0248 }
        r33 = 0;
        r0 = r33;
        r1 = r38;
        r1.connectionTimedOut = r0;	 Catch:{ Exception -> 0x0248 }
    L_0x035c:
        r16.connect();	 Catch:{ Exception -> 0x0248 }
        r26 = r16.getResponseCode();	 Catch:{ Exception -> 0x0248 }
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x038d;
    L_0x0367:
        r33 = "vserv";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "Response code is ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r0 = r34;
        r1 = r26;
        r34 = r0.append(r1);	 Catch:{ Exception -> 0x0248 }
        r35 = " for url ";
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r0 = r34;
        r1 = r39;
        r34 = r0.append(r1);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x038d:
        r33 = 307; // 0x133 float:4.3E-43 double:1.517E-321;
        r0 = r26;
        r1 = r33;
        if (r0 == r1) goto L_0x03b5;
    L_0x0395:
        r33 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        r0 = r26;
        r1 = r33;
        if (r0 == r1) goto L_0x03b5;
    L_0x039d:
        r33 = 301; // 0x12d float:4.22E-43 double:1.487E-321;
        r0 = r26;
        r1 = r33;
        if (r0 == r1) goto L_0x03b5;
    L_0x03a5:
        r33 = 302; // 0x12e float:4.23E-43 double:1.49E-321;
        r0 = r26;
        r1 = r33;
        if (r0 == r1) goto L_0x03b5;
    L_0x03ad:
        r33 = 303; // 0x12f float:4.25E-43 double:1.497E-321;
        r0 = r26;
        r1 = r33;
        if (r0 != r1) goto L_0x0a4e;
    L_0x03b5:
        r33 = "location";
        r0 = r16;
        r1 = r33;
        r33 = r0.getHeaderField(r1);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0895;
    L_0x03c1:
        r33 = "location";
        r0 = r16;
        r1 = r33;
        r39 = r0.getHeaderField(r1);	 Catch:{ Exception -> 0x0248 }
        r33 = "abc";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "requestUrl found: ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r0 = r34;
        r1 = r39;
        r34 = r0.append(r1);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x03e3:
        r29 = r16.getInputStream();	 Catch:{ Exception -> 0x0248 }
        if (r29 == 0) goto L_0x0859;
    L_0x03e9:
        r4 = new java.io.ByteArrayOutputStream;	 Catch:{ Exception -> 0x0248 }
        r4.<init>();	 Catch:{ Exception -> 0x0248 }
        r33 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0 = r33;
        r3 = new byte[r0];	 Catch:{ Exception -> 0x0248 }
        r19 = 0;
    L_0x03f6:
        r0 = r29;
        r19 = r0.read(r3);	 Catch:{ Exception -> 0x0248 }
        r33 = -1;
        r0 = r19;
        r1 = r33;
        if (r0 != r1) goto L_0x08af;
    L_0x0404:
        r12 = r4.toByteArray();	 Catch:{ Exception -> 0x0248 }
        r33 = "Content-Encoding";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x08ba;
    L_0x0412:
        r33 = "Content-Encoding";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r33 = r33.trim();	 Catch:{ Exception -> 0x0248 }
        r34 = "gzip";
        r33 = r33.equalsIgnoreCase(r34);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x08ba;
    L_0x0426:
        r0 = r38;
        r33 = r0.decompress(r12);	 Catch:{ Exception -> 0x0248 }
        r0 = r33;
        r1 = r38;
        r1.trailerPart = r0;	 Catch:{ Exception -> 0x0248 }
    L_0x0432:
        r33 = "X-VSERV-CONTEXT";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0477;
    L_0x043c:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x045c;
    L_0x0440:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-CONTEXT is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-CONTEXT";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x045c:
        r0 = r38;
        r0 = r0.contextAdPreference;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r33 = r33.edit();	 Catch:{ Exception -> 0x0248 }
        r34 = "context";
        r35 = "X-VSERV-CONTEXT";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r33 = r33.putString(r34, r35);	 Catch:{ Exception -> 0x0248 }
        r33.commit();	 Catch:{ Exception -> 0x0248 }
    L_0x0477:
        r33 = "X-VSERV-STORE";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x04d4;
    L_0x0481:
        r33 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0248 }
        r34 = 11;
        r0 = r33;
        r1 = r34;
        if (r0 < r1) goto L_0x08c9;
    L_0x048b:
        r0 = r38;
        r0 = r0.context;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r34 = "vserv_unique_add_app_lifecycle";
        r35 = 4;
        r33 = r33.getSharedPreferences(r34, r35);	 Catch:{ Exception -> 0x0248 }
        r27 = r33.edit();	 Catch:{ Exception -> 0x0248 }
    L_0x049d:
        r33 = "key";
        r34 = "X-VSERV-STORE";
        r0 = r34;
        r34 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r0 = r27;
        r1 = r33;
        r2 = r34;
        r33 = r0.putString(r1, r2);	 Catch:{ Exception -> 0x0248 }
        r33.commit();	 Catch:{ Exception -> 0x0248 }
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x04d4;
    L_0x04b8:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-STORE is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-STORE";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x04d4:
        r33 = "X-VSERV-UNIVERSAL";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0569;
    L_0x04de:
        r0 = r38;
        r0 = r0.context;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r34 = "android.permission.WRITE_EXTERNAL_STORAGE";
        r33 = r33.checkCallingOrSelfPermission(r34);	 Catch:{ Exception -> 0x0248 }
        if (r33 != 0) goto L_0x0569;
    L_0x04ec:
        r33 = r38.checkExternalMedia();	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0569;
    L_0x04f2:
        r10 = 0;
        r33 = "AES";
        r5 = javax.crypto.Cipher.getInstance(r33);	 Catch:{ Exception -> 0x08dd, all -> 0x08e5 }
        r33 = 1;
        r34 = new javax.crypto.spec.SecretKeySpec;	 Catch:{ Exception -> 0x08dd, all -> 0x08e5 }
        r35 = "296@!$^&*!)~123#";
        r35 = r35.getBytes();	 Catch:{ Exception -> 0x08dd, all -> 0x08e5 }
        r36 = "AES";
        r34.<init>(r35, r36);	 Catch:{ Exception -> 0x08dd, all -> 0x08e5 }
        r0 = r33;
        r1 = r34;
        r5.init(r0, r1);	 Catch:{ Exception -> 0x08dd, all -> 0x08e5 }
        r33 = new java.io.File;	 Catch:{ Exception -> 0x08dd, all -> 0x08e5 }
        r34 = android.os.Environment.getExternalStorageDirectory();	 Catch:{ Exception -> 0x08dd, all -> 0x08e5 }
        r35 = ".addWrapper.txt";
        r33.<init>(r34, r35);	 Catch:{ Exception -> 0x08dd, all -> 0x08e5 }
        r0 = r33;
        r1 = r38;
        r1.universalContextFile = r0;	 Catch:{ Exception -> 0x08dd, all -> 0x08e5 }
        r11 = new javax.crypto.CipherOutputStream;	 Catch:{ Exception -> 0x08dd, all -> 0x08e5 }
        r33 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x08dd, all -> 0x08e5 }
        r0 = r38;
        r0 = r0.universalContextFile;	 Catch:{ Exception -> 0x08dd, all -> 0x08e5 }
        r34 = r0;
        r33.<init>(r34);	 Catch:{ Exception -> 0x08dd, all -> 0x08e5 }
        r0 = r33;
        r11.<init>(r0, r5);	 Catch:{ Exception -> 0x08dd, all -> 0x08e5 }
        r33 = "X-VSERV-UNIVERSAL";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x110f, all -> 0x110b }
        r33 = r33.getBytes();	 Catch:{ Exception -> 0x110f, all -> 0x110b }
        r0 = r33;
        r11.write(r0);	 Catch:{ Exception -> 0x110f, all -> 0x110b }
        if (r11 == 0) goto L_0x0548;
    L_0x0545:
        r11.close();	 Catch:{ Exception -> 0x0248 }
    L_0x0548:
        r10 = r11;
    L_0x0549:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0569;
    L_0x054d:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-UNIVERSAL is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-UNIVERSAL";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0569:
        r33 = "X-VSERV-AD-ORIENTATION";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x05b3;
    L_0x0573:
        r33 = "X-VSERV-AD-ORIENTATION";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r20 = r33.trim();	 Catch:{ Exception -> 0x0248 }
        r33 = "portrait";
        r0 = r20;
        r1 = r33;
        r33 = r0.equalsIgnoreCase(r1);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x08ec;
    L_0x058b:
        r33 = "portrait";
        r0 = r33;
        r1 = r38;
        r1.adOrientation = r0;	 Catch:{ Exception -> 0x0248 }
    L_0x0593:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x05b3;
    L_0x0597:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-AD-ORIENTATION is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-AD-ORIENTATION";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x05b3:
        r33 = "X-VSERV-AD-REFRESH-RATE";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x05ef;
    L_0x05bd:
        r33 = "X-VSERV-AD-REFRESH-RATE";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x090c }
        r33 = java.lang.Integer.parseInt(r33);	 Catch:{ Exception -> 0x090c }
        r0 = r33;
        r1 = r38;
        r1.serverRefreshRate = r0;	 Catch:{ Exception -> 0x090c }
    L_0x05cf:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x05ef;
    L_0x05d3:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-AD-REFRESH-RATE is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-AD-REFRESH-RATE";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x05ef:
        r33 = "X-VSERV-DIRECT-VIDEO";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0912;
    L_0x05f9:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0619;
    L_0x05fd:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-DIRECT-VIDEO is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-DIRECT-VIDEO";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0619:
        r33 = "X-VSERV-DIRECT-VIDEO";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r0 = r33;
        r1 = r38;
        r1.directVideo = r0;	 Catch:{ Exception -> 0x0248 }
    L_0x0627:
        r33 = "X-VSERV-GP-TRACKING";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0658;
    L_0x0631:
        r33 = "X-VSERV-GP-TRACKING";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x093c }
        r28 = java.lang.Integer.parseInt(r33);	 Catch:{ Exception -> 0x093c }
        r33 = 1;
        r0 = r28;
        r1 = r33;
        if (r0 != r1) goto L_0x0658;
    L_0x0645:
        r33 = "yes";
        r0 = r33;
        r1 = r38;
        r1.gpHit = r0;	 Catch:{ Exception -> 0x093c }
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x093c }
        if (r33 == 0) goto L_0x0658;
    L_0x0651:
        r33 = "headers";
        r34 = "header X-VSERV-GP-TRACKING so setting gp to 1";
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x093c }
    L_0x0658:
        r33 = "X-VSERV-AD-TYPE";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0690;
    L_0x0662:
        r33 = "X-VSERV-AD-TYPE";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0942 }
        r0 = r33;
        r1 = r38;
        r1.adType = r0;	 Catch:{ Exception -> 0x0942 }
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0942 }
        if (r33 == 0) goto L_0x0690;
    L_0x0674:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0942 }
        r35 = "header X-VSERV-AD-TYPE is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0942 }
        r35 = "X-VSERV-AD-TYPE";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0942 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0942 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0942 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0942 }
    L_0x0690:
        r33 = "X-VSERV-ZONE-TYPE";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0707;
    L_0x069a:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x06ba;
    L_0x069e:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-ZONE-TYPE is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-ZONE-TYPE";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x06ba:
        r33 = "X-VSERV-ZONE-TYPE";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r0 = r33;
        r1 = r38;
        r1.zoneType = r0;	 Catch:{ Exception -> 0x0248 }
        r0 = r38;
        r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        if (r33 == 0) goto L_0x0707;
    L_0x06d0:
        r0 = r38;
        r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r34 = "cf";
        r33 = r33.containsKey(r34);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0707;
    L_0x06de:
        r0 = r38;
        r0 = r0.zoneType;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r34 = "Banner";
        r33 = r33.equalsIgnoreCase(r34);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0948;
    L_0x06ec:
        r0 = r38;
        r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r34 = "cf";
        r33 = r33.getString(r34);	 Catch:{ Exception -> 0x0248 }
        r34 = "0";
        r33 = r33.equalsIgnoreCase(r34);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0707;
    L_0x0700:
        r33 = "error";
        r34 = "wrong Zone type! Expected Zone type as Billboard";
        android.util.Log.e(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0707:
        r33 = "X-VSERV-IMPRESSION";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0973;
    L_0x0711:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0731;
    L_0x0715:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-IMPRESSION is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-IMPRESSION";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0731:
        r33 = "X-VSERV-IMPRESSION";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r0 = r33;
        r1 = r38;
        r1.impressionHeader = r0;	 Catch:{ Exception -> 0x0248 }
    L_0x073f:
        r33 = "X-VSERV-DISABLE-MEDIACACHE";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x099d;
    L_0x0749:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0769;
    L_0x074d:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-DISABLE-MEDIACACHE is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-DISABLE-MEDIACACHE";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0769:
        r33 = "X-VSERV-DISABLE-MEDIACACHE";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r0 = r33;
        r1 = r38;
        r1.isMediaCacheDisabled = r0;	 Catch:{ Exception -> 0x0248 }
    L_0x0777:
        r33 = "X-VSERV-DELAY";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x09c7;
    L_0x0781:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x07a1;
    L_0x0785:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-DELAY is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-DELAY";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x07a1:
        r33 = "X-VSERV-DELAY";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r33 = java.lang.Integer.parseInt(r33);	 Catch:{ Exception -> 0x0248 }
        r0 = r33;
        r1 = r38;
        r1.skipDelay = r0;	 Catch:{ Exception -> 0x0248 }
    L_0x07b3:
        r33 = "X-VSERV-REQUEST-ID";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x09f1;
    L_0x07bd:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x07dd;
    L_0x07c1:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-REQUEST-ID is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-REQUEST-ID";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x07dd:
        r33 = "X-VSERV-REQUEST-ID";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r0 = r33;
        r1 = r38;
        r1.requestId = r0;	 Catch:{ Exception -> 0x0248 }
    L_0x07eb:
        r33 = "X-VSERV-INVENTORY-TYPE";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0838;
    L_0x07f5:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0815;
    L_0x07f9:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-INVENTORY-TYPE  is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-INVENTORY-TYPE";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0815:
        r33 = "X-VSERV-INVENTORY-TYPE";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r0 = r33;
        r1 = r38;
        r1.inventoryType = r0;	 Catch:{ Exception -> 0x0248 }
        r0 = r38;
        r0 = r0.inventoryType;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r34 = "Application";
        r33 = r33.equalsIgnoreCase(r34);	 Catch:{ Exception -> 0x0248 }
        if (r33 != 0) goto L_0x0838;
    L_0x0831:
        r33 = "error";
        r34 = "wrong Invenotry type! Expected Inventory type as Application";
        android.util.Log.e(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0838:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0856;
    L_0x083c:
        r33 = "abc";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "302 response is ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r0 = r38;
        r0 = r0.trailerPart;	 Catch:{ Exception -> 0x0248 }
        r35 = r0;
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0856:
        r29.close();	 Catch:{ Exception -> 0x0248 }
    L_0x0859:
        r30 = new java.net.URL;	 Catch:{ Exception -> 0x0248 }
        r33 = r38.getRedirectUrl(r39);	 Catch:{ Exception -> 0x0248 }
        r0 = r30;
        r1 = r33;
        r0.<init>(r1);	 Catch:{ Exception -> 0x0248 }
        r8 = r30.openConnection();	 Catch:{ Exception -> 0x0248 }
        r0 = r8 instanceof java.net.HttpURLConnection;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        if (r33 != 0) goto L_0x0a1b;
    L_0x0870:
        r33 = new java.io.IOException;	 Catch:{ Exception -> 0x0248 }
        r34 = "Not an HTTP connection";
        r33.<init>(r34);	 Catch:{ Exception -> 0x0248 }
        throw r33;	 Catch:{ Exception -> 0x0248 }
    L_0x0878:
        r33 = 0;
        r0 = r16;
        r1 = r33;
        r0.setAllowUserInteraction(r1);	 Catch:{ Exception -> 0x0248 }
        r33 = 0;
        r0 = r16;
        r1 = r33;
        r0.setInstanceFollowRedirects(r1);	 Catch:{ Exception -> 0x0248 }
        r33 = "GET";
        r0 = r16;
        r1 = r33;
        r0.setRequestMethod(r1);	 Catch:{ Exception -> 0x0248 }
        goto L_0x0336;
    L_0x0895:
        r33 = "abc";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "no requestUrl: ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r0 = r34;
        r1 = r39;
        r34 = r0.append(r1);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
        goto L_0x03e3;
    L_0x08af:
        r33 = 0;
        r0 = r33;
        r1 = r19;
        r4.write(r3, r0, r1);	 Catch:{ Exception -> 0x0248 }
        goto L_0x03f6;
    L_0x08ba:
        r33 = new java.lang.String;	 Catch:{ Exception -> 0x0248 }
        r0 = r33;
        r0.<init>(r12);	 Catch:{ Exception -> 0x0248 }
        r0 = r33;
        r1 = r38;
        r1.trailerPart = r0;	 Catch:{ Exception -> 0x0248 }
        goto L_0x0432;
    L_0x08c9:
        r0 = r38;
        r0 = r0.context;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r34 = "vserv_unique_add_app_lifecycle";
        r35 = 0;
        r33 = r33.getSharedPreferences(r34, r35);	 Catch:{ Exception -> 0x0248 }
        r27 = r33.edit();	 Catch:{ Exception -> 0x0248 }
        goto L_0x049d;
    L_0x08dd:
        r33 = move-exception;
    L_0x08de:
        if (r10 == 0) goto L_0x0549;
    L_0x08e0:
        r10.close();	 Catch:{ Exception -> 0x0248 }
        goto L_0x0549;
    L_0x08e5:
        r33 = move-exception;
    L_0x08e6:
        if (r10 == 0) goto L_0x08eb;
    L_0x08e8:
        r10.close();	 Catch:{ Exception -> 0x0248 }
    L_0x08eb:
        throw r33;	 Catch:{ Exception -> 0x0248 }
    L_0x08ec:
        r33 = "landscape";
        r0 = r20;
        r1 = r33;
        r33 = r0.equalsIgnoreCase(r1);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0902;
    L_0x08f8:
        r33 = "landscape";
        r0 = r33;
        r1 = r38;
        r1.adOrientation = r0;	 Catch:{ Exception -> 0x0248 }
        goto L_0x0593;
    L_0x0902:
        r33 = "adaptive";
        r0 = r33;
        r1 = r38;
        r1.adOrientation = r0;	 Catch:{ Exception -> 0x0248 }
        goto L_0x0593;
    L_0x090c:
        r14 = move-exception;
        r14.printStackTrace();	 Catch:{ Exception -> 0x0248 }
        goto L_0x05cf;
    L_0x0912:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0932;
    L_0x0916:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-DIRECT-VIDEO is null :::  ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-DIRECT-VIDEO";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0932:
        r33 = 0;
        r0 = r33;
        r1 = r38;
        r1.directVideo = r0;	 Catch:{ Exception -> 0x0248 }
        goto L_0x0627;
    L_0x093c:
        r14 = move-exception;
        r14.printStackTrace();	 Catch:{ Exception -> 0x0248 }
        goto L_0x0658;
    L_0x0942:
        r14 = move-exception;
        r14.printStackTrace();	 Catch:{ Exception -> 0x0248 }
        goto L_0x0690;
    L_0x0948:
        r0 = r38;
        r0 = r0.zoneType;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r34 = "Billboard";
        r33 = r33.equalsIgnoreCase(r34);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0707;
    L_0x0956:
        r0 = r38;
        r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r34 = "cf";
        r33 = r33.getString(r34);	 Catch:{ Exception -> 0x0248 }
        r34 = "1";
        r33 = r33.equalsIgnoreCase(r34);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0707;
    L_0x096a:
        r33 = "error";
        r34 = "wrong Zone type! Expected Zone type as Banner";
        android.util.Log.e(r33, r34);	 Catch:{ Exception -> 0x0248 }
        goto L_0x0707;
    L_0x0973:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0993;
    L_0x0977:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-IMPRESSION is null :::  ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-IMPRESSION";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0993:
        r33 = 0;
        r0 = r33;
        r1 = r38;
        r1.impressionHeader = r0;	 Catch:{ Exception -> 0x0248 }
        goto L_0x073f;
    L_0x099d:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x09bd;
    L_0x09a1:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-DISABLE-MEDIACACHE is null :::  ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-DISABLE-MEDIACACHE";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x09bd:
        r33 = 0;
        r0 = r33;
        r1 = r38;
        r1.isMediaCacheDisabled = r0;	 Catch:{ Exception -> 0x0248 }
        goto L_0x0777;
    L_0x09c7:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x09e7;
    L_0x09cb:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-DELAY is null :::  ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-DELAY";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x09e7:
        r33 = 0;
        r0 = r33;
        r1 = r38;
        r1.skipDelay = r0;	 Catch:{ Exception -> 0x0248 }
        goto L_0x07b3;
    L_0x09f1:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0a11;
    L_0x09f5:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-REQUEST-ID is null :::  ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-REQUEST-ID";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0a11:
        r33 = 0;
        r0 = r33;
        r1 = r38;
        r1.requestId = r0;	 Catch:{ Exception -> 0x0248 }
        goto L_0x07eb;
    L_0x0a1b:
        r0 = r8;
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x0248 }
        r16 = r0;
        r26 = r16.getResponseCode();	 Catch:{ Exception -> 0x0248 }
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0a4e;
    L_0x0a28:
        r33 = "abc";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "Redirection Response code ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r0 = r34;
        r1 = r26;
        r34 = r0.append(r1);	 Catch:{ Exception -> 0x0248 }
        r35 = " for final Url ";
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r0 = r34;
        r1 = r30;
        r34 = r0.append(r1);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0a4e:
        r33 = 0;
        r0 = r33;
        r2 = r38;
        r2.connectionStartTime = r0;	 Catch:{ Exception -> 0x0248 }
        r33 = 0;
        r0 = r33;
        r1 = r38;
        r1.connectionTimedOut = r0;	 Catch:{ Exception -> 0x0248 }
        r33 = 2;
        r0 = r40;
        r1 = r33;
        if (r0 == r1) goto L_0x10ae;
    L_0x0a66:
        r33 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r0 = r26;
        r1 = r33;
        if (r0 != r1) goto L_0x10ae;
    L_0x0a6e:
        r17 = r16.getInputStream();	 Catch:{ Exception -> 0x0248 }
        r33 = 1;
        r0 = r40;
        r1 = r33;
        if (r0 != r1) goto L_0x0287;
    L_0x0a7a:
        r33 = "X-VSERV-CONTEXT";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0abf;
    L_0x0a84:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0aa4;
    L_0x0a88:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-CONTEXT is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-CONTEXT";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0aa4:
        r0 = r38;
        r0 = r0.contextAdPreference;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r33 = r33.edit();	 Catch:{ Exception -> 0x0248 }
        r34 = "context";
        r35 = "X-VSERV-CONTEXT";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r33 = r33.putString(r34, r35);	 Catch:{ Exception -> 0x0248 }
        r33.commit();	 Catch:{ Exception -> 0x0248 }
    L_0x0abf:
        r33 = "X-VSERV-STORE";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0b1c;
    L_0x0ac9:
        r33 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0248 }
        r34 = 11;
        r0 = r33;
        r1 = r34;
        if (r0 < r1) goto L_0x0f20;
    L_0x0ad3:
        r0 = r38;
        r0 = r0.context;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r34 = "vserv_unique_add_app_lifecycle";
        r35 = 4;
        r33 = r33.getSharedPreferences(r34, r35);	 Catch:{ Exception -> 0x0248 }
        r27 = r33.edit();	 Catch:{ Exception -> 0x0248 }
    L_0x0ae5:
        r33 = "key";
        r34 = "X-VSERV-STORE";
        r0 = r34;
        r34 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r0 = r27;
        r1 = r33;
        r2 = r34;
        r33 = r0.putString(r1, r2);	 Catch:{ Exception -> 0x0248 }
        r33.commit();	 Catch:{ Exception -> 0x0248 }
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0b1c;
    L_0x0b00:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-STORE is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-STORE";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0b1c:
        r33 = "X-VSERV-UNIVERSAL";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0bb1;
    L_0x0b26:
        r0 = r38;
        r0 = r0.context;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r34 = "android.permission.WRITE_EXTERNAL_STORAGE";
        r33 = r33.checkCallingOrSelfPermission(r34);	 Catch:{ Exception -> 0x0248 }
        if (r33 != 0) goto L_0x0bb1;
    L_0x0b34:
        r33 = r38.checkExternalMedia();	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0bb1;
    L_0x0b3a:
        r10 = 0;
        r33 = "AES";
        r5 = javax.crypto.Cipher.getInstance(r33);	 Catch:{ Exception -> 0x0f34, all -> 0x0f3c }
        r33 = 1;
        r34 = new javax.crypto.spec.SecretKeySpec;	 Catch:{ Exception -> 0x0f34, all -> 0x0f3c }
        r35 = "296@!$^&*!)~123#";
        r35 = r35.getBytes();	 Catch:{ Exception -> 0x0f34, all -> 0x0f3c }
        r36 = "AES";
        r34.<init>(r35, r36);	 Catch:{ Exception -> 0x0f34, all -> 0x0f3c }
        r0 = r33;
        r1 = r34;
        r5.init(r0, r1);	 Catch:{ Exception -> 0x0f34, all -> 0x0f3c }
        r33 = new java.io.File;	 Catch:{ Exception -> 0x0f34, all -> 0x0f3c }
        r34 = android.os.Environment.getExternalStorageDirectory();	 Catch:{ Exception -> 0x0f34, all -> 0x0f3c }
        r35 = ".addWrapper.txt";
        r33.<init>(r34, r35);	 Catch:{ Exception -> 0x0f34, all -> 0x0f3c }
        r0 = r33;
        r1 = r38;
        r1.universalContextFile = r0;	 Catch:{ Exception -> 0x0f34, all -> 0x0f3c }
        r11 = new javax.crypto.CipherOutputStream;	 Catch:{ Exception -> 0x0f34, all -> 0x0f3c }
        r33 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x0f34, all -> 0x0f3c }
        r0 = r38;
        r0 = r0.universalContextFile;	 Catch:{ Exception -> 0x0f34, all -> 0x0f3c }
        r34 = r0;
        r33.<init>(r34);	 Catch:{ Exception -> 0x0f34, all -> 0x0f3c }
        r0 = r33;
        r11.<init>(r0, r5);	 Catch:{ Exception -> 0x0f34, all -> 0x0f3c }
        r33 = "X-VSERV-UNIVERSAL";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x1107, all -> 0x1103 }
        r33 = r33.getBytes();	 Catch:{ Exception -> 0x1107, all -> 0x1103 }
        r0 = r33;
        r11.write(r0);	 Catch:{ Exception -> 0x1107, all -> 0x1103 }
        if (r11 == 0) goto L_0x0b90;
    L_0x0b8d:
        r11.close();	 Catch:{ Exception -> 0x0248 }
    L_0x0b90:
        r10 = r11;
    L_0x0b91:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0bb1;
    L_0x0b95:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-UNIVERSAL is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-UNIVERSAL";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0bb1:
        r33 = "X-VSERV-AD-ORIENTATION";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0bfb;
    L_0x0bbb:
        r33 = "X-VSERV-AD-ORIENTATION";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r20 = r33.trim();	 Catch:{ Exception -> 0x0248 }
        r33 = "portrait";
        r0 = r20;
        r1 = r33;
        r33 = r0.equalsIgnoreCase(r1);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0f43;
    L_0x0bd3:
        r33 = "portrait";
        r0 = r33;
        r1 = r38;
        r1.adOrientation = r0;	 Catch:{ Exception -> 0x0248 }
    L_0x0bdb:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0bfb;
    L_0x0bdf:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-AD-ORIENTATION is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-AD-ORIENTATION";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0bfb:
        r33 = "X-VSERV-AD-REFRESH-RATE";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0c37;
    L_0x0c05:
        r33 = "X-VSERV-AD-REFRESH-RATE";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0f63 }
        r33 = java.lang.Integer.parseInt(r33);	 Catch:{ Exception -> 0x0f63 }
        r0 = r33;
        r1 = r38;
        r1.serverRefreshRate = r0;	 Catch:{ Exception -> 0x0f63 }
    L_0x0c17:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0c37;
    L_0x0c1b:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-AD-REFRESH-RATE is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-AD-REFRESH-RATE";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0c37:
        r33 = "X-VSERV-DIRECT-VIDEO";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0f69;
    L_0x0c41:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0c61;
    L_0x0c45:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-DIRECT-VIDEO is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-DIRECT-VIDEO";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0c61:
        r33 = "X-VSERV-DIRECT-VIDEO";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r0 = r33;
        r1 = r38;
        r1.directVideo = r0;	 Catch:{ Exception -> 0x0248 }
    L_0x0c6f:
        r33 = "X-VSERV-IMPRESSION";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0f8b;
    L_0x0c79:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0c99;
    L_0x0c7d:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-IMPRESSION is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-IMPRESSION";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0c99:
        r33 = "X-VSERV-IMPRESSION";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r0 = r33;
        r1 = r38;
        r1.impressionHeader = r0;	 Catch:{ Exception -> 0x0248 }
    L_0x0ca7:
        r33 = "X-VSERV-DISABLE-MEDIACACHE";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0fb5;
    L_0x0cb1:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0cd1;
    L_0x0cb5:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-DISABLE-MEDIACACHE is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-DISABLE-MEDIACACHE";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0cd1:
        r33 = "X-VSERV-DISABLE-MEDIACACHE";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r0 = r33;
        r1 = r38;
        r1.isMediaCacheDisabled = r0;	 Catch:{ Exception -> 0x0248 }
    L_0x0cdf:
        r33 = "X-VSERV-DELAY";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0fdf;
    L_0x0ce9:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0d09;
    L_0x0ced:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-DELAY is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-DELAY";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0d09:
        r33 = "X-VSERV-DELAY";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r33 = java.lang.Integer.parseInt(r33);	 Catch:{ Exception -> 0x0248 }
        r0 = r33;
        r1 = r38;
        r1.skipDelay = r0;	 Catch:{ Exception -> 0x0248 }
    L_0x0d1b:
        r33 = "X-VSERV-REQUEST-ID";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x1009;
    L_0x0d25:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0d45;
    L_0x0d29:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-REQUEST-ID is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-REQUEST-ID";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0d45:
        r33 = "X-VSERV-REQUEST-ID";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r0 = r33;
        r1 = r38;
        r1.requestId = r0;	 Catch:{ Exception -> 0x0248 }
    L_0x0d53:
        r33 = "X-VSERV-IMAGE-WIDTH";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x1033;
    L_0x0d5d:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0d7d;
    L_0x0d61:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-IMAGE-WIDTH is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-IMAGE-WIDTH";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0d7d:
        r33 = "X-VSERV-IMAGE-WIDTH";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r33 = java.lang.Integer.parseInt(r33);	 Catch:{ Exception -> 0x0248 }
        r0 = r33;
        r1 = r38;
        r1.adWidth = r0;	 Catch:{ Exception -> 0x0248 }
    L_0x0d8f:
        r33 = "X-VSERV-IMAGE-HEIGHT";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x1055;
    L_0x0d99:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0db9;
    L_0x0d9d:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-IMAGE-HEIGHT is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-IMAGE-HEIGHT";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0db9:
        r33 = "X-VSERV-IMAGE-HEIGHT";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r33 = java.lang.Integer.parseInt(r33);	 Catch:{ Exception -> 0x0248 }
        r0 = r33;
        r1 = r38;
        r1.adHeight = r0;	 Catch:{ Exception -> 0x0248 }
    L_0x0dcb:
        r33 = "X-VSERV-GP-TRACKING";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0dfc;
    L_0x0dd5:
        r33 = "X-VSERV-GP-TRACKING";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x1077 }
        r28 = java.lang.Integer.parseInt(r33);	 Catch:{ Exception -> 0x1077 }
        r33 = 1;
        r0 = r28;
        r1 = r33;
        if (r0 != r1) goto L_0x0dfc;
    L_0x0de9:
        r33 = "yes";
        r0 = r33;
        r1 = r38;
        r1.gpHit = r0;	 Catch:{ Exception -> 0x1077 }
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x1077 }
        if (r33 == 0) goto L_0x0dfc;
    L_0x0df5:
        r33 = "headers";
        r34 = "header X-VSERV-GP-TRACKING so setting gp to 1";
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x1077 }
    L_0x0dfc:
        r33 = "X-VSERV-AD-TYPE";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0e34;
    L_0x0e06:
        r33 = "X-VSERV-AD-TYPE";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x107d }
        r0 = r33;
        r1 = r38;
        r1.adType = r0;	 Catch:{ Exception -> 0x107d }
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x107d }
        if (r33 == 0) goto L_0x0e34;
    L_0x0e18:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x107d }
        r35 = "header X-VSERV-AD-TYPE is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x107d }
        r35 = "X-VSERV-AD-TYPE";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x107d }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x107d }
        r34 = r34.toString();	 Catch:{ Exception -> 0x107d }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x107d }
    L_0x0e34:
        r33 = "Content-Encoding";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0e5a;
    L_0x0e3e:
        r33 = "Content-Encoding";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r33 = r33.trim();	 Catch:{ Exception -> 0x0248 }
        r34 = "gzip";
        r33 = r33.equalsIgnoreCase(r34);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0e5a;
    L_0x0e52:
        r33 = 1;
        r0 = r33;
        r1 = r38;
        r1.decodeResponse = r0;	 Catch:{ Exception -> 0x0248 }
    L_0x0e5a:
        r33 = "X-VSERV-ZONE-TYPE";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0ed1;
    L_0x0e64:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0e84;
    L_0x0e68:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-ZONE-TYPE is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-ZONE-TYPE";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0e84:
        r33 = "X-VSERV-ZONE-TYPE";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r0 = r33;
        r1 = r38;
        r1.zoneType = r0;	 Catch:{ Exception -> 0x0248 }
        r0 = r38;
        r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        if (r33 == 0) goto L_0x0ed1;
    L_0x0e9a:
        r0 = r38;
        r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r34 = "cf";
        r33 = r33.containsKey(r34);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0ed1;
    L_0x0ea8:
        r0 = r38;
        r0 = r0.zoneType;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r34 = "Banner";
        r33 = r33.equalsIgnoreCase(r34);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x1083;
    L_0x0eb6:
        r0 = r38;
        r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r34 = "cf";
        r33 = r33.getString(r34);	 Catch:{ Exception -> 0x0248 }
        r34 = "0";
        r33 = r33.equalsIgnoreCase(r34);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0ed1;
    L_0x0eca:
        r33 = "error";
        r34 = "wrong Zone type! Expected Zone type as Billboard";
        android.util.Log.e(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0ed1:
        r33 = "X-VSERV-INVENTORY-TYPE";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0287;
    L_0x0edb:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0efb;
    L_0x0edf:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-INVENTORY-TYPE  is not null so saving it as ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-INVENTORY-TYPE";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0efb:
        r33 = "X-VSERV-INVENTORY-TYPE";
        r0 = r33;
        r33 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r0 = r33;
        r1 = r38;
        r1.inventoryType = r0;	 Catch:{ Exception -> 0x0248 }
        r0 = r38;
        r0 = r0.inventoryType;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r34 = "Application";
        r33 = r33.equalsIgnoreCase(r34);	 Catch:{ Exception -> 0x0248 }
        if (r33 != 0) goto L_0x0287;
    L_0x0f17:
        r33 = "error";
        r34 = "wrong Invenotry type! Expected Inventory type as Application";
        android.util.Log.e(r33, r34);	 Catch:{ Exception -> 0x0248 }
        goto L_0x0287;
    L_0x0f20:
        r0 = r38;
        r0 = r0.context;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r34 = "vserv_unique_add_app_lifecycle";
        r35 = 0;
        r33 = r33.getSharedPreferences(r34, r35);	 Catch:{ Exception -> 0x0248 }
        r27 = r33.edit();	 Catch:{ Exception -> 0x0248 }
        goto L_0x0ae5;
    L_0x0f34:
        r33 = move-exception;
    L_0x0f35:
        if (r10 == 0) goto L_0x0b91;
    L_0x0f37:
        r10.close();	 Catch:{ Exception -> 0x0248 }
        goto L_0x0b91;
    L_0x0f3c:
        r33 = move-exception;
    L_0x0f3d:
        if (r10 == 0) goto L_0x0f42;
    L_0x0f3f:
        r10.close();	 Catch:{ Exception -> 0x0248 }
    L_0x0f42:
        throw r33;	 Catch:{ Exception -> 0x0248 }
    L_0x0f43:
        r33 = "landscape";
        r0 = r20;
        r1 = r33;
        r33 = r0.equalsIgnoreCase(r1);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0f59;
    L_0x0f4f:
        r33 = "landscape";
        r0 = r33;
        r1 = r38;
        r1.adOrientation = r0;	 Catch:{ Exception -> 0x0248 }
        goto L_0x0bdb;
    L_0x0f59:
        r33 = "adaptive";
        r0 = r33;
        r1 = r38;
        r1.adOrientation = r0;	 Catch:{ Exception -> 0x0248 }
        goto L_0x0bdb;
    L_0x0f63:
        r14 = move-exception;
        r14.printStackTrace();	 Catch:{ Exception -> 0x0248 }
        goto L_0x0c17;
    L_0x0f69:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0c6f;
    L_0x0f6d:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-DIRECT-VIDEO is null :::  ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-DIRECT-VIDEO";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
        goto L_0x0c6f;
    L_0x0f8b:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0fab;
    L_0x0f8f:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-IMPRESSION is null :::  ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-IMPRESSION";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0fab:
        r33 = 0;
        r0 = r33;
        r1 = r38;
        r1.impressionHeader = r0;	 Catch:{ Exception -> 0x0248 }
        goto L_0x0ca7;
    L_0x0fb5:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0fd5;
    L_0x0fb9:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-DISABLE-MEDIACACHE is null :::  ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-DISABLE-MEDIACACHE";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0fd5:
        r33 = 0;
        r0 = r33;
        r1 = r38;
        r1.isMediaCacheDisabled = r0;	 Catch:{ Exception -> 0x0248 }
        goto L_0x0cdf;
    L_0x0fdf:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0fff;
    L_0x0fe3:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-DELAY is null :::  ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-DELAY";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x0fff:
        r33 = 0;
        r0 = r33;
        r1 = r38;
        r1.skipDelay = r0;	 Catch:{ Exception -> 0x0248 }
        goto L_0x0d1b;
    L_0x1009:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x1029;
    L_0x100d:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-REQUEST-ID is null :::  ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-REQUEST-ID";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
    L_0x1029:
        r33 = 0;
        r0 = r33;
        r1 = r38;
        r1.requestId = r0;	 Catch:{ Exception -> 0x0248 }
        goto L_0x0d53;
    L_0x1033:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0d8f;
    L_0x1037:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-IMAGE-WIDTH is null :::  ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-IMAGE-WIDTH";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
        goto L_0x0d8f;
    L_0x1055:
        r33 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0dcb;
    L_0x1059:
        r33 = "headers";
        r34 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0248 }
        r35 = "header X-VSERV-IMAGE-HEIGHT is null :::  ";
        r34.<init>(r35);	 Catch:{ Exception -> 0x0248 }
        r35 = "X-VSERV-IMAGE-HEIGHT";
        r0 = r35;
        r35 = r8.getHeaderField(r0);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.append(r35);	 Catch:{ Exception -> 0x0248 }
        r34 = r34.toString();	 Catch:{ Exception -> 0x0248 }
        android.util.Log.i(r33, r34);	 Catch:{ Exception -> 0x0248 }
        goto L_0x0dcb;
    L_0x1077:
        r14 = move-exception;
        r14.printStackTrace();	 Catch:{ Exception -> 0x0248 }
        goto L_0x0dfc;
    L_0x107d:
        r14 = move-exception;
        r14.printStackTrace();	 Catch:{ Exception -> 0x0248 }
        goto L_0x0e34;
    L_0x1083:
        r0 = r38;
        r0 = r0.zoneType;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r34 = "Billboard";
        r33 = r33.equalsIgnoreCase(r34);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0ed1;
    L_0x1091:
        r0 = r38;
        r0 = r0.vservConfigBundle;	 Catch:{ Exception -> 0x0248 }
        r33 = r0;
        r34 = "cf";
        r33 = r33.getString(r34);	 Catch:{ Exception -> 0x0248 }
        r34 = "1";
        r33 = r33.equalsIgnoreCase(r34);	 Catch:{ Exception -> 0x0248 }
        if (r33 == 0) goto L_0x0ed1;
    L_0x10a5:
        r33 = "error";
        r34 = "wrong Zone type! Expected Zone type as Banner";
        android.util.Log.e(r33, r34);	 Catch:{ Exception -> 0x0248 }
        goto L_0x0ed1;
    L_0x10ae:
        r33 = 2;
        r0 = r40;
        r1 = r33;
        if (r0 == r1) goto L_0x0287;
    L_0x10b6:
        r33 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r0 = r26;
        r1 = r33;
        if (r0 == r1) goto L_0x0287;
    L_0x10be:
        r33 = 96;
        r34 = 0;
        r0 = r38;
        r1 = r33;
        r2 = r34;
        r0.loadMainApp(r1, r2);	 Catch:{ Exception -> 0x0248 }
        goto L_0x0287;
    L_0x10cd:
        r33 = 0;
        r0 = r33;
        r2 = r38;
        r2.connectionStartTime = r0;	 Catch:{ Exception -> 0x0248 }
        r33 = 0;
        r0 = r33;
        r1 = r38;
        r1.connectionTimedOut = r0;	 Catch:{ Exception -> 0x0248 }
        goto L_0x0287;
    L_0x10df:
        r33 = 0;
        r0 = r33;
        r2 = r38;
        r2.connectionStartTime = r0;	 Catch:{ Exception -> 0x0248 }
        r33 = 0;
        r0 = r33;
        r1 = r38;
        r1.connectionTimedOut = r0;	 Catch:{ Exception -> 0x0248 }
        goto L_0x0287;
    L_0x10f1:
        r33 = 0;
        r0 = r33;
        r2 = r38;
        r2.connectionStartTime = r0;	 Catch:{ Exception -> 0x0248 }
        r33 = 0;
        r0 = r33;
        r1 = r38;
        r1.connectionTimedOut = r0;	 Catch:{ Exception -> 0x0248 }
        goto L_0x0287;
    L_0x1103:
        r33 = move-exception;
        r10 = r11;
        goto L_0x0f3d;
    L_0x1107:
        r33 = move-exception;
        r10 = r11;
        goto L_0x0f35;
    L_0x110b:
        r33 = move-exception;
        r10 = r11;
        goto L_0x08e6;
    L_0x110f:
        r33 = move-exception;
        r10 = r11;
        goto L_0x08de;
    L_0x1113:
        r33 = move-exception;
        r6 = r7;
        goto L_0x02ab;
    L_0x1117:
        r33 = move-exception;
        goto L_0x02a3;
        */
    }

    private File saveFileInCache(String htmlResponseString, String cacheFileName) {
        File cacheFile;
        Exception e;
        Throwable th;
        synchronized (this) {
            try {
                File cacheDir;
                if (this.vservAdManageractivity != null) {
                    cacheDir = this.vservAdManageractivity.getCacheDir();
                } else {
                    cacheDir = this.context.getCacheDir();
                }
                cacheFile = new File(cacheDir, cacheFileName);
                cacheFile.delete();
                if (cacheFile.createNewFile()) {
                    FileOutputStream fileOut = null;
                    try {
                        FileOutputStream fileOut2 = new FileOutputStream(cacheFile);
                        try {
                            fileOut2.write(htmlResponseString.getBytes());
                            if (fileOut2 != null) {
                                fileOut2.close();
                            }
                        } catch (Exception e2) {
                            e = e2;
                            fileOut = fileOut2;
                            e.printStackTrace();
                            if (fileOut != null) {
                                fileOut.close();
                            }
                            cacheFile.deleteOnExit();
                            return cacheFile;
                        } catch (Throwable th2) {
                            th = th2;
                            fileOut = fileOut2;
                            if (fileOut != null) {
                                fileOut.close();
                            }
                            throw th;
                        }
                    } catch (Exception e3) {
                        e = e3;
                        try {
                            e.printStackTrace();
                            if (fileOut != null) {
                                fileOut.close();
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            if (fileOut != null) {
                                fileOut.close();
                            }
                            throw th;
                        }
                        cacheFile.deleteOnExit();
                        return cacheFile;
                    }
                }
                cacheFile.deleteOnExit();
            } catch (Exception e4) {
                e4.printStackTrace();
                loadMainApp(500, false);
                return null;
            }
        }
        return cacheFile;
    }

    private void setAdConfigurationParameters() {
        try {
            if (this.vservAdManageractivity != null && this.vservConfigBundle != null) {
                if (this.vservConfigBundle.containsKey("minimumSessionTime")) {
                    this.vservAdManageractivity.minimumSessionTimeInSeconds = Integer.parseInt(this.vservConfigBundle.get("minimumSessionTime").toString().trim());
                }
                if (this.vservConfigBundle.containsKey("mustSeeAdMsg")) {
                    this.vservAdManageractivity.mustSeeAdMsg = this.vservConfigBundle.get("mustSeeAdMsg").toString().trim();
                }
                if (this.vservConfigBundle.containsKey("timeout")) {
                    this.timeOutInSeconds = Integer.parseInt(this.vservConfigBundle.get("timeout").toString());
                }
                if (this.vservConfigBundle.containsKey("proceedTime") && this.vservConfigBundle.getString("proceedTime").indexOf("<") == -1) {
                    this.proceedTimeInSeconds = Integer.parseInt(this.vservConfigBundle.get("proceedTime").toString());
                }
                if (this.vservConfigBundle.containsKey("viewMandatoryMessage")) {
                    this.viewMandatoryMessage = this.vservConfigBundle.get("viewMandatoryMessage").toString();
                }
                if (this.vservConfigBundle.containsKey("viewMandatoryRetryLabel")) {
                    this.viewMandatoryRetryLabel = this.vservConfigBundle.get("viewMandatoryRetryLabel").toString();
                }
                if (this.vservConfigBundle.containsKey("viewMandatoryExitLabel")) {
                    this.viewMandatoryExitLabel = this.vservConfigBundle.get("viewMandatoryExitLabel").toString();
                }
            }
        } catch (Exception e) {
            VservManager.getInstance(this.context).trackExceptions(e, "setAdConfigurationParameters");
        }
    }

    private void setAdvertisementPage() {
        this.adLoaded = true;
        this.page = 4;
        if (Defines.ENABLE_lOGGING) {
            Log.i("vserv", new StringBuilder("Setting AdvertisementPage for ").append(this.responseString).toString());
        }
        if (this.isCacheAdHit) {
            if ((this.impressionHeader == null || this.impressionHeader.equals(Preconditions.EMPTY_ARGUMENTS)) && (this.responseString.trim().length() == 0 || this.responseString.startsWith("{"))) {
                this.impressionHeader = null;
            } else if ((this.impressionHeader == null || this.impressionHeader.equals(Preconditions.EMPTY_ARGUMENTS)) && this.responseString.trim().length() > 0 && !this.responseString.startsWith("{")) {
                this.impressionHeader = "null-impression";
            }
        }
        this.handler.post(this.updateResults);
        this.applyOrientation = true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @android.annotation.SuppressLint({"InlinedApi"})
    private void setAdvsFlow(byte[] r11_responseByteData) {
        throw new UnsupportedOperationException("Method not decompiled: mobi.vserv.android.ads.VservAdController.setAdvsFlow(byte[]):void");
        /*
        r10 = this;
        r6 = -1;
        r9 = 0;
        r8 = 1;
        r7 = 0;
        r4 = r10.decodeResponse;	 Catch:{ IOException -> 0x006b }
        if (r4 == 0) goto L_0x0063;
    L_0x0008:
        r4 = 0;
        r10.decodeResponse = r4;	 Catch:{ IOException -> 0x006b }
        r4 = r10.decompress(r11);	 Catch:{ IOException -> 0x006b }
        r10.responseString = r4;	 Catch:{ IOException -> 0x006b }
    L_0x0011:
        r4 = r10.responseString;
        r4 = r4.trim();
        r4 = r4.length();
        if (r4 == 0) goto L_0x0027;
    L_0x001d:
        r4 = r10.responseString;
        r5 = "{";
        r4 = r4.startsWith(r5);
        if (r4 == 0) goto L_0x0081;
    L_0x0027:
        r4 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;
        if (r4 == 0) goto L_0x0032;
    L_0x002b:
        r4 = "vserv";
        r5 = "Ad length is 0 or starts with {";
        android.util.Log.i(r4, r5);
    L_0x0032:
        r4 = r10.vservConfigBundle;
        r5 = "adType";
        r4 = r4.containsKey(r5);
        if (r4 == 0) goto L_0x004c;
    L_0x003c:
        r4 = r10.vservConfigBundle;
        r5 = "adType";
        r4 = r4.getString(r5);
        r5 = "getAd";
        r4 = r4.equalsIgnoreCase(r5);
        if (r4 != 0) goto L_0x0056;
    L_0x004c:
        r4 = r10.vservConfigBundle;
        r5 = "refreshInterval";
        r4 = r4.containsKey(r5);
        if (r4 == 0) goto L_0x0070;
    L_0x0056:
        r4 = r10.context;
        r4 = (android.app.Activity) r4;
        r5 = new mobi.vserv.android.ads.VservAdController$7;
        r5.<init>();
        r4.runOnUiThread(r5);
    L_0x0062:
        return;
    L_0x0063:
        r4 = new java.lang.String;	 Catch:{ IOException -> 0x006b }
        r4.<init>(r11);	 Catch:{ IOException -> 0x006b }
        r10.responseString = r4;	 Catch:{ IOException -> 0x006b }
        goto L_0x0011;
    L_0x006b:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0011;
    L_0x0070:
        r10.adDeliverdSuccess = r7;
        r10.page = r8;
        r4 = r10.handler;
        r5 = r10.updateResults;
        r4.post(r5);
        r4 = 99;
        r10.loadMainApp(r4, r7);
        goto L_0x0062;
    L_0x0081:
        r4 = r10.isgetAdRequest;
        if (r4 != 0) goto L_0x0089;
    L_0x0085:
        r4 = r10.isCacheHitInitiated;
        if (r4 == 0) goto L_0x0101;
    L_0x0089:
        r4 = r10.isMediaCacheDisabled;
        if (r4 == 0) goto L_0x00fe;
    L_0x008d:
        r4 = r10.isMediaCacheDisabled;
        r5 = "1";
        r4 = r4.equals(r5);
        if (r4 != 0) goto L_0x00fe;
    L_0x0097:
        r4 = r10.gpHit;
        if (r4 == 0) goto L_0x00fb;
    L_0x009b:
        r4 = r10.responseString;
        r5 = "vserv.download";
        r4 = r4.indexOf(r5);
        if (r4 == r6) goto L_0x00fb;
    L_0x00a5:
        r4 = r10.responseString;
        r5 = r10.responseString;
        r6 = "vserv.download('";
        r5 = r5.indexOf(r6);
        r5 = r5 + 16;
        r2 = r4.substring(r5);
        r4 = "'";
        r4 = r2.indexOf(r4);
        r3 = r2.substring(r7, r4);
        r10.downloadNetworkUrl = r3;
    L_0x00c1:
        r4 = r10.trailerPart;
        if (r4 == 0) goto L_0x00de;
    L_0x00c5:
        r4 = r10.responseString;
        r5 = new java.lang.StringBuilder;
        r4 = java.lang.String.valueOf(r4);
        r5.<init>(r4);
        r4 = r10.trailerPart;
        r4 = r5.append(r4);
        r4 = r4.toString();
        r10.responseString = r4;
        r10.trailerPart = r9;
    L_0x00de:
        r4 = r10.vservConfigBundle;
        r5 = "considerTimeout";
        r4 = r4.containsKey(r5);
        if (r4 == 0) goto L_0x014e;
    L_0x00e8:
        r4 = r10.considerTimeout;
        if (r4 == 0) goto L_0x014e;
    L_0x00ec:
        r10.considerTimeout = r7;
        r4 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;
        if (r4 == 0) goto L_0x0062;
    L_0x00f2:
        r4 = "sample";
        r5 = "Not showing ad since timeout occured";
        android.util.Log.i(r4, r5);
        goto L_0x0062;
    L_0x00fb:
        r10.downloadNetworkUrl = r9;
        goto L_0x00c1;
    L_0x00fe:
        r10.downloadNetworkUrl = r9;
        goto L_0x00c1;
    L_0x0101:
        r4 = r10.gpHit;
        if (r4 == 0) goto L_0x00c1;
    L_0x0105:
        r4 = r10.responseString;
        r5 = "vserv.download";
        r4 = r4.indexOf(r5);
        if (r4 == r6) goto L_0x00c1;
    L_0x010f:
        r4 = r10.responseString;
        r5 = r10.responseString;
        r6 = "vserv.download('";
        r5 = r5.indexOf(r6);
        r5 = r5 + 16;
        r2 = r4.substring(r5);
        r4 = "'";
        r4 = r2.indexOf(r4);
        r3 = r2.substring(r7, r4);
        r4 = android.os.Build.VERSION.SDK_INT;
        r5 = 11;
        if (r4 < r5) goto L_0x0145;
    L_0x012f:
        r4 = r10.context;
        r5 = "vserv_gp_preference";
        r6 = 4;
        r1 = r4.getSharedPreferences(r5, r6);
    L_0x0138:
        r4 = r1.edit();
        r4 = r4.putBoolean(r3, r8);
        r4.commit();
        goto L_0x00c1;
    L_0x0145:
        r4 = r10.context;
        r5 = "vserv_gp_preference";
        r1 = r4.getSharedPreferences(r5, r7);
        goto L_0x0138;
    L_0x014e:
        r4 = r10.vservConfigBundle;
        r5 = "adType";
        r4 = r4.containsKey(r5);
        if (r4 == 0) goto L_0x016d;
    L_0x0158:
        r4 = r10.vservConfigBundle;
        r5 = "adType";
        r4 = r4.getString(r5);
        r5 = "getAd";
        r4 = r4.equalsIgnoreCase(r5);
        if (r4 == 0) goto L_0x016d;
    L_0x0168:
        r10.setAdvertisementPage();
        goto L_0x0062;
    L_0x016d:
        r10.adDeliverdSuccess = r8;
        r10.setAdvertisementPage();
        goto L_0x0062;
        */
    }

    private void setDomain(String t_domain) {
        this.domain = new StringBuilder(String.valueOf(this.domain)).append(t_domain).toString();
    }

    private void setHeight_and_Width(String t_width) {
        this.configUrl = new StringBuilder(String.valueOf(this.configUrl)).append(t_width).toString();
    }

    private void setLac(String t_lac) {
        this.configUrl = new StringBuilder(String.valueOf(this.configUrl)).append(t_lac).toString();
    }

    private void setLocale(String t_locale) {
        this.configUrl = new StringBuilder(String.valueOf(this.configUrl)).append(t_locale).toString();
    }

    private void setce(String t_ce) {
        this.configUrl = new StringBuilder(String.valueOf(this.configUrl)).append(t_ce).toString();
    }

    private void setinterface(String t_jsInterface) {
        this.jsinterfaceobj = new StringBuilder(String.valueOf(this.jsinterfaceobj)).append(t_jsInterface).toString();
    }

    private void showViewMandatory() {
        this.page = 7;
        this.handler.post(this.updateResults);
    }

    private String[] split(String originalStr, String separatorStr) {
        Vector<String> nodes = new Vector();
        String separator = separatorStr;
        int index = originalStr.indexOf(separator);
        while (index >= 0) {
            nodes.addElement(originalStr.substring(0, index));
            originalStr = originalStr.substring(separator.length() + index);
            index = originalStr.indexOf(separator);
        }
        nodes.addElement(originalStr);
        String[] result = new String[nodes.size()];
        if (nodes.size() > 0) {
            int loop = 0;
            while (loop < nodes.size()) {
                result[loop] = ((String) nodes.elementAt(loop)).trim();
                loop++;
            }
        }
        return result;
    }

    private String urlEncode(String sUrl) {
        if (sUrl == null) {
            return null;
        }
        StringBuffer urlOK = new StringBuffer();
        int i = 0;
        while (i < sUrl.length()) {
            char ch = sUrl.charAt(i);
            switch (ch) {
                case ApiEventType.API_MRAID_PLAY_AUDIO:
                    urlOK.append("%20");
                    break;
                case ApiEventType.API_MRAID_GET_VIDEO_VOLUME:
                    urlOK.append("%2D");
                    break;
                case ApiEventType.API_MRAID_PAUSE_VIDEO:
                    urlOK.append("%2F");
                    break;
                case ':':
                    urlOK.append("%3A");
                    break;
                case ProductCacheConfig.DEFAULT_INTERVAL:
                    urlOK.append("%3C");
                    break;
                case '>':
                    urlOK.append("%3E");
                    break;
                default:
                    urlOK.append(ch);
                    break;
            }
            i++;
        }
        return urlOK.toString();
    }

    public void SendQuartileTracking(String t_requestId, String event) {
        if (t_requestId != null && event != null) {
            try {
                String trackingUrl = new StringBuilder("http://").append(this.domain).append("/delivery/e.php?").append("vserv=").append(URLEncoder.encode(t_requestId, "UTF-8")).append("&event=").append("vPlay(").append(URLEncoder.encode(event, "UTF-8")).append(")").toString();
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", new StringBuilder("vservAd Controller Sending video info to server on URL \n").append(trackingUrl).toString());
                }
                makeHttpConnection(trackingUrl, MMAdView.TRANSITION_UP);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void clearAdCachePanel() {
        try {
            if (this.adCacheView != null) {
                this.adCacheView.clearCache(true);
                this.adCacheView.clearHistory();
                this.adCacheView.clearView();
                this.adCacheView = null;
                System.gc();
            }
        } catch (Exception e) {
        }
    }

    public void clearAdPanel() {
        try {
            if (this.adComponentView != null) {
                this.adComponentView.clearCache(true);
                this.adComponentView.clearHistory();
                this.adComponentView.clearView();
                System.gc();
            }
            if (this.mainLayout != null) {
                this.mainLayout.removeAllViews();
            }
        } catch (Exception e) {
        }
    }

    public String getCacheADOrientation() {
        return (String) VservManager.getInstance(this.context).getCacheAdOrientation();
    }

    public int getCacheADSkipDelay() {
        return VservManager.getInstance(this.context).getCacheAdSkipDelay() != null ? ((Integer) VservManager.getInstance(this.context).getCacheAdSkipDelay()).intValue() : 0;
    }

    public Object getCacheDownloadNetworkUrl() {
        return VservManager.getInstance(this.context).getDownloadNetworkURL();
    }

    public OrmmaPlayer getCachePlayer() {
        VservManager.getInstance(this.context);
        return VservManager.getAdVideo();
    }

    public String getOrientation() {
        return VservManager.getInstance(this.context).adOrientation;
    }

    public String getPostActionUrl() {
        return VservManager.getInstance(this.context).getPostActionURL();
    }

    @SuppressLint({"DefaultLocale", "InlinedApi"})
    public void invokeApplication() {
        try {
            Editor orientationEditor;
            if (Defines.ENABLE_lOGGING) {
                Log.i("vserv", "invoke application called!!");
            }
            if (this.vservAdManageractivity != null) {
                this.vservConfigBundle = this.vservAdManageractivity.vservConfigBundle;
            }
            loadOrmmaJavaScriptFiles(this.context, "vserv_ormmaReady_A-AN-2.0.1.js");
            loadOrmmaJavaScriptFiles(this.context, "ormma_A-AN-2.0.1.js");
            loadOrmmaJavaScriptFiles(this.context, "ormma_bridge_A-AN-2.0.1.js");
            loadOrmmaJavaScriptFiles(this.context, "vserv_Cache_ormmaReady_A-AN-2.0.1.js");
            showAt = this.vservConfigBundle.getString("showAt");
            if (this.vservConfigBundle.containsKey("renderAd")) {
                this.isRenderAd = this.vservConfigBundle.getBoolean("renderAd");
            }
            if (this.vservConfigBundle.containsKey("frameAd")) {
                this.isDisplayFrameAd = this.vservConfigBundle.getBoolean("frameAd");
            }
            if (VERSION.SDK_INT >= 11) {
                orientationEditor = this.context.getSharedPreferences("vserv_orientation", MMAdView.TRANSITION_RANDOM).edit();
            } else {
                orientationEditor = this.context.getSharedPreferences("vserv_orientation", 0).edit();
            }
            orientationEditor.clear().commit();
            if (!(this.vservConfigBundle.getString("ro") == null || this.vservAdManageractivity == null)) {
                if (this.vservConfigBundle.getString("ro").equals("l")) {
                    this.vservAdManageractivity.setRequestedOrientation(0);
                    if (Defines.ENABLE_lOGGING) {
                        Log.i("orientation", "saving appOrientation landscape");
                    }
                    orientationEditor.putString("orientation", "landscape").commit();
                } else {
                    this.vservAdManageractivity.setRequestedOrientation(1);
                    if (Defines.ENABLE_lOGGING) {
                        Log.i("orientation", "saving appOrientation portrait");
                    }
                    orientationEditor.putString("orientation", "portrait").commit();
                }
            }
            this.startThread = true;
            new Timer().scheduleAtFixedRate(new SimpleTask(), 0, AdWebViewUtils.DEFAULT_IMPRESSION_DELAY_MS);
            if (this.vservAdManageractivity != null) {
                this.web = new WebView(this.vservAdManageractivity.getApplicationContext());
            } else {
                this.web = new WebView(this.context);
            }
            this.userAgent = this.web.getSettings().getUserAgentString();
            this.zoomFactor = this.web.getScale();
            if (Defines.ENABLE_lOGGING) {
                Log.i("vserv", new StringBuilder("zoomFactor is ").append(this.zoomFactor).toString());
            }
            if (Defines.ENABLE_lOGGING) {
                Log.i("vserv", new StringBuilder("User Agent Screen is ").append(this.userAgent).toString());
            }
            this.configUrl = new StringBuilder("http://").append(this.domain).append("/delivery/adapi.php?").append("ua=").append(URLEncoder.encode(this.userAgent, "UTF-8")).append("&vr=").append(URLEncoder.encode(VservConstants.LIBRARY_VERSION, "UTF-8")).append("&app=1").append("&ml=html").append("&zf=").append(URLEncoder.encode(this.zoomFactor, "UTF-8")).toString();
            if (this.width < this.height) {
                setHeight_and_Width("&sw=");
                setHeight_and_Width(this.width);
                setHeight_and_Width("&sh=");
                setHeight_and_Width(this.height);
            } else {
                setHeight_and_Width("&sw=");
                setHeight_and_Width(this.height);
                this.configUrl = new StringBuilder(String.valueOf(this.configUrl)).append("&sh=").append(this.width).toString();
            }
            extractParameters();
            if (this.advertiserId != null) {
                embedV2Parameters();
                showWaitingScreen();
                this.makeAdRequest = true;
            } else {
                if (Defines.ENABLE_lOGGING) {
                    Log.d("vserv", "Will try to fetch Advertiser ID");
                }
                new Thread() {
                    public void run() {
                        Activity ctx = null;
                        if (VservAdController.this.vservAdManageractivity != null) {
                            ctx = VservAdController.this.vservAdManageractivity;
                        } else if (VservAdController.this.context instanceof Activity) {
                            ctx = VservAdController.this.context;
                        }
                        if (ctx != null) {
                            try {
                                Class<?> googlePlayServicesUtilClass = Class.forName("com.google.android.gms.common.GooglePlayServicesUtil");
                                Class[] paramContext = new Class[1];
                                paramContext[0] = Context.class;
                                Method isGooglePlayServicesAvailableMethod = googlePlayServicesUtilClass.getMethod("isGooglePlayServicesAvailable", paramContext);
                                Object[] objArr = new Object[1];
                                objArr[0] = ctx;
                                int isGooglePlayServicesAvailableMethodReturnValue = ((Number) isGooglePlayServicesAvailableMethod.invoke(null, objArr)).intValue();
                                int connectionResultSuccessReturnValue = ((Number) Class.forName("com.google.android.gms.common.ConnectionResult").getField("SUCCESS").get(null)).intValue();
                                if (Defines.ENABLE_lOGGING) {
                                    Log.i("google", new StringBuilder("connectionResultSuccessReturnValue:").append(connectionResultSuccessReturnValue).toString());
                                    Log.i("google", new StringBuilder("isGooglePlayServicesAvailableMethodReturnValue:").append(isGooglePlayServicesAvailableMethodReturnValue).toString());
                                }
                                if (isGooglePlayServicesAvailableMethodReturnValue == connectionResultSuccessReturnValue) {
                                    Method getAdvertisingIdInfoMethod = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient").getMethod("getAdvertisingIdInfo", paramContext);
                                    objArr = new Object[1];
                                    objArr[0] = ctx;
                                    Object object = getAdvertisingIdInfoMethod.invoke(null, objArr);
                                    Class[] noparams = new Class[0];
                                    Class<?> infoClass = object.getClass();
                                    boolean isLAT = ((Boolean) infoClass.getMethod(GpsHelper.IS_LIMIT_AD_TRACKING_ENABLED_KEY, noparams).invoke(object, new Object[0])).booleanValue();
                                    if (Defines.ENABLE_lOGGING) {
                                        Log.i("google", new StringBuilder("LAT is ====> ").append(isLAT).toString());
                                    }
                                    VservAdController.this.isLimitAdTrackingEnabled = isLAT;
                                    if (VservAdController.this.isLimitAdTrackingEnabled) {
                                        VservAdController.this.advertiserId = null;
                                        if (Defines.ENABLE_lOGGING) {
                                            Log.i("google", "Cant get ADVID");
                                        }
                                    } else {
                                        String advID = (String) infoClass.getMethod("getId", noparams).invoke(object, new Object[0]);
                                        if (Defines.ENABLE_lOGGING) {
                                            Log.i("google", new StringBuilder("ADVID is ====> ").append(advID).toString());
                                        }
                                        VservAdController.this.advertiserId = advID;
                                        if (VservAdController.this.advertiserId != null) {
                                            Log.d("vserv", new StringBuilder("Device Advertisement Id: ").append(VservAdController.this.advertiserId).toString());
                                        }
                                    }
                                } else if (Defines.ENABLE_lOGGING) {
                                    Log.i("google", "Cant get ADVID");
                                }
                            } catch (ClassNotFoundException e) {
                            } catch (NoSuchMethodException e2) {
                            } catch (IllegalAccessException e3) {
                            } catch (IllegalArgumentException e4) {
                            } catch (InvocationTargetException e5) {
                            } catch (NoSuchFieldException e6) {
                            } catch (Exception e7) {
                            }
                            AnonymousClass_1 anonymousClass_1 = anonymousClass_1;
                            AnonymousClass_3 anonymousClass_3 = this;
                            ctx.runOnUiThread(anonymousClass_1);
                        }
                    }
                }.start();
            }
        } catch (Exception e) {
            VservManager.getInstance(this.context).trackExceptions(e, "invokeapplication");
        }
    }

    public boolean isInternetReachable() {
        boolean z = false;
        try {
            DNSResolver dnsRes = new DNSResolver(this.domain);
            Thread t = new Thread(dnsRes);
            t.start();
            t.join(10000);
            return dnsRes.get() == null ? z : true;
        } catch (Exception e) {
            VservManager.getInstance(this.context).trackExceptions(e, "isInternetReachable");
            return z;
        }
    }

    public void loadMainApp(int debug, boolean vservAdManagerActivityForced) {
        if (this.vservAdManageractivity != null) {
            try {
                this.page = 6;
                this.handler.post(this.updateResults);
                if (this.vservConfigBundle.containsKey("zoneId") && cache.containsKey(this.zoneId)) {
                    cache.remove(this.zoneId);
                }
                deleteCacheFile(VservConstants.START_AD_CACHE_FILE_NAME);
                deleteCacheFile(VservConstants.END_AD_CACHE_FILE_NAME);
                VservManager.getInstance(this.context).adOrientation = null;
                this.vservAdManageractivity.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void logGetAd() {
        ObjectInputStream inputStream = null;
        try {
            ObjectInputStream inputStream2 = new ObjectInputStream(this.context.openFileInput("getAdFile.txt"));
            try {
                Iterator it = ((Map) inputStream2.readObject()).entrySet().iterator();
                while (it.hasNext()) {
                    Entry<Object, Object> entry = (Entry) it.next();
                    if (Defines.ENABLE_lOGGING) {
                        Log.i("vserv", new StringBuilder("get ad key=>").append(entry.getKey()).toString());
                    }
                    Iterator it2 = ((ArrayList) entry.getValue()).iterator();
                    while (it2.hasNext()) {
                        String string = (String) it2.next();
                        if (Defines.ENABLE_lOGGING) {
                            Log.i("vserv", new StringBuilder("get ad value=>").append(string).toString());
                        }
                    }
                }
                if (inputStream2 != null) {
                    try {
                        inputStream2.close();
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e2) {
                inputStream = inputStream2;
            } catch (Throwable th) {
                th = th;
                inputStream = inputStream2;
            }
        } catch (Exception e3) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        } catch (Throwable th2) {
            Throwable th3;
            th3 = th2;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
            }
            throw th3;
        }
    }

    public void notifyOnceCache() {
        if (Defines.ENABLE_lOGGING) {
            Log.i("vserv", "notifyOnceCache got called:: ");
        }
        if (this.vservAdManageractivity != null && this.sendPresentScreenBroadcast) {
            this.sendPresentScreenBroadcast = false;
            Log.i(AdDatabaseHelper.TABLE_AD, "Present Screen");
            this.context.sendBroadcast(new Intent("mobi.vserv.ad.present_screen"));
        }
        new Thread() {
            public void run() {
                try {
                    if (Defines.ENABLE_lOGGING) {
                        Log.i("vserv", new StringBuilder("@@@@@@notifyOnceCache size is ").append(VservManager.getInstance(VservAdController.this.context).getAdURLs().length).toString());
                    }
                    String[] urlSet = VservManager.getInstance(VservAdController.this.context).getAdURLs();
                    VservManager.getInstance(VservAdController.this.context).removeAdUrls();
                    VservAdController.this.notifyCacheUrls.clear();
                    int i = 0;
                    while (i < urlSet.length) {
                        String url = urlSet[i];
                        if (Defines.ENABLE_lOGGING) {
                            Log.i("video", "Notifying impression");
                        }
                        if (url.trim().length() > 0) {
                            VservAdController.this.makeHttpConnection(url, MMAdView.TRANSITION_UP);
                        }
                        i++;
                    }
                } catch (Exception e) {
                }
            }
        }.start();
    }

    public void onReceiveError() {
        if (Defines.ENABLE_lOGGING) {
            Log.i("vserv", "onReceiveError:: ");
        }
        if (this.adComponentView != null) {
            this.adComponentView.setVisibility(MMAdView.TRANSITION_RANDOM);
        }
        if (this.vservAdManageractivity != null) {
            loadMainApp(WalletConstants.ERROR_CODE_INVALID_PARAMETERS, false);
        } else if (this.iAddCallback != null) {
            this.iAddCallback.onLoadFailure();
        }
    }

    public void removeCacheADOrientation() {
        VservManager.getInstance(this.context).removeCacheAdOrientation();
    }

    public void removeCacheADRequestId() {
        VservManager.getInstance(this.context).removeCacheAdRequestId();
    }

    public void removeCacheADSkipDelay() {
        VservManager.getInstance(this.context).removeCacheAdSkipDelay();
    }

    public void removeCacheAdType() {
        VservManager.getInstance(this.context).removeAdType();
    }

    public void removeCacheDownloadNetworkUrl() {
        VservManager.getInstance(this.context).removeDownloadNetworkURL();
    }

    public void removeCachePlayer() {
        VservManager.getInstance(this.context);
        VservManager.removeAdVideo();
    }

    public void removePostActionUrl() {
        VservManager.getInstance(this.context).removePostActionURL();
    }

    public void requestAddView() {
        if (this.vservConfigBundle.containsKey("mccExclusionList")) {
            String codes = this.vservConfigBundle.getString("mccExclusionList");
            if (codes != null && codes.trim().length() > 0) {
                String[] arrCodes = codes.split(",");
                String mcc = null;
                try {
                    mcc = ((TelephonyManager) this.context.getSystemService("phone")).getNetworkOperator();
                } catch (Exception e) {
                }
                if (mcc != null && mcc.trim().length() > 0) {
                    mcc = mcc.substring(0, MMAdView.TRANSITION_DOWN);
                    int i = 0;
                    while (i < arrCodes.length) {
                        if (!arrCodes[i].trim().equals(mcc.trim())) {
                            i++;
                        } else {
                            return;
                        }
                    }
                }
            }
        }
        if (!this.vservConfigBundle.containsKey("blockAds") || !Boolean.parseBoolean(this.vservConfigBundle.getString("blockAds")) || !isAdBlockingEnabled()) {
            this.shouldRefresh = true;
            invokeApplication();
        }
    }

    public void resumeRefresh() {
        this.shouldRefresh = true;
        if (this.vservConfigBundle.containsKey("refreshInterval")) {
            if (this.refreshRate == -1) {
                int i;
                if (this.serverRefreshRate == -1) {
                    i = this.default_refresh_rate;
                } else {
                    i = this.serverRefreshRate;
                }
                this.refreshRate = i;
            }
            if (this.refreshRate > 0) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("VservAdController", new StringBuilder("Will try to get new ad after ").append(this.refreshRate).append(" seconds ").toString());
                }
                if (this.refreshRunnable == null) {
                    this.refreshRunnable = new Runnable() {
                        public void run() {
                            VservAdController.this.notifyUrls.clear();
                            VservAdController.this.invokeApplication();
                        }
                    };
                }
                if (this.refreshHandler == null) {
                    this.refreshHandler = new Handler();
                } else {
                    this.refreshHandler.removeCallbacks(this.refreshRunnable);
                }
                if (this.shouldRefresh) {
                    if (Defines.ENABLE_lOGGING) {
                        Log.i("abc", new StringBuilder("Will try to get new Ad after ").append(this.refreshRate).append(" seconds!! for zoneId ").append(this.zoneId).toString());
                    }
                    this.refreshHandler.postDelayed(this.refreshRunnable, (long) (this.refreshRate * 1000));
                }
            }
        }
    }

    public void sendDownloadNotificationToServer(String requestUrl, int requestCode) {
        if (Defines.ENABLE_lOGGING) {
            Log.i("VservAdController", new StringBuilder("Sending download info to server on URL \n").append(requestUrl).toString());
        }
        makeHttpConnection(requestUrl, requestCode);
    }

    public void showClose() {
        this.page = 8;
        this.handler.post(this.updateResults);
    }

    public void showSkipPageCache() {
        this.page = 5;
        this.handler.post(this.updateResults);
    }

    public void showSkipPage_onpageFinished() {
        this.page = 5;
        this.handler.post(this.updateResults);
    }

    public void showWaitingScreen() {
        this.page = 0;
        this.handler.post(this.updateResults);
    }

    public void stopRefresh() {
        this.shouldRefresh = false;
        if (this.refreshHandler != null && this.refreshRunnable != null) {
            if (Defines.ENABLE_lOGGING) {
                Log.i("abc", "Stopping Refresh and Cancelling Runnable");
            }
            this.refreshHandler.removeCallbacks(this.refreshRunnable);
            this.refreshHandler = null;
            this.refreshRunnable = null;
        }
    }

    public void storeCacheADOrientation() {
        VservManager.getInstance(this.context).setCacheAdOrientation(this.adOrientation);
    }

    public void storeCacheADRequestId() {
        VservManager.getInstance(this.context).setCacheAdRequestId(this.requestId);
    }

    public void storeCacheADSkipDelay() {
        VservManager.getInstance(this.context).setCacheAdSkipDelay(this.skipDelay);
    }

    public void storeCacheAdType(Object t_adtype) {
        VservManager.getInstance(this.context).setAdType(t_adtype);
    }

    public void storeCacheAdView() {
        VservManager.getInstance(this.context).setAdview(this.adCacheView);
    }

    public void storeCacheDownloadNetworkUrl() {
        if (this.downloadNetworkUrl != null) {
            VservManager.getInstance(this.context).setDownloadNetworkUrl(this.downloadNetworkUrl);
        }
    }

    public void storeCacheImpressionHeader() {
        VservManager.getInstance(this.context).setCacheImpression(this.impressionHeader);
    }

    public void storeCachePlayer(OrmmaPlayer t_ormmPlayer) {
        VservManager.getInstance(this.context).setAdVideo(t_ormmPlayer);
    }

    public void storePostActionUrl(String t_url) {
        VservManager.getInstance(this.context).setPostActionUrl(t_url);
    }
}