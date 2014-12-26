package mobi.vserv.org.ormma.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import com.google.android.gms.drive.DriveFile;
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMRequest;
import com.mopub.common.Preconditions;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import mobi.vserv.android.ads.VservAdController;
import mobi.vserv.android.ads.VservAdManager;
import mobi.vserv.android.ads.VservConstants;
import mobi.vserv.org.ormma.controller.Defines;
import mobi.vserv.org.ormma.controller.OrmmaController.Dimensions;
import mobi.vserv.org.ormma.controller.OrmmaController.PlayerProperties;
import mobi.vserv.org.ormma.controller.OrmmaController.Properties;
import mobi.vserv.org.ormma.controller.OrmmaUtilityController;
import mobi.vserv.org.ormma.controller.util.OrmmaPlayer;
import mobi.vserv.org.ormma.controller.util.OrmmaPlayerListener;

@SuppressLint({"HandlerLeak"})
public class OrmmaView extends WebView implements OnGlobalLayoutListener {
    public static final String ACTION_KEY = "action";
    private static final String AD_PATH = "AD_PATH";
    protected static final int BACKGROUND_ID = 101;
    private static final String CURRENT_FILE = "_ormma_current";
    public static final String DIMENSIONS = "expand_dimensions";
    private static final String ERROR_ACTION = "action";
    private static final String ERROR_MESSAGE = "message";
    private static final String EXPAND_PROPERTIES = "expand_properties";
    public static final String EXPAND_URL = "expand_url";
    private static final String LOG_TAG = "OrmmaView";
    private static final int MESSAGE_CACHE_AUDIO = 1011;
    private static final int MESSAGE_CACHE_VIDEO = 1010;
    private static final int MESSAGE_CLOSE = 1001;
    private static final int MESSAGE_EXPAND = 1004;
    private static final int MESSAGE_HIDE = 1002;
    private static final int MESSAGE_OPEN = 1006;
    private static final int MESSAGE_PLAY_AUDIO = 1008;
    private static final int MESSAGE_PLAY_VIDEO = 1007;
    private static final int MESSAGE_RAISE_ERROR = 1009;
    private static final int MESSAGE_RESIZE = 1000;
    private static final int MESSAGE_SEND_EXPAND_CLOSE = 1005;
    private static final int MESSAGE_SHOW = 1003;
    public static final int ORMMA_ID = 102;
    protected static final int PLACEHOLDER_ID = 100;
    public static final String PLAYER_PROPERTIES = "player_properties";
    private static final String RESIZE_HEIGHT = "resize_height";
    private static final String RESIZE_WIDTH = "resize_width";
    private static final String TAG = "OrmmaView";
    private static int[] attrs;
    private static String mBridgeScriptPath;
    private static String mScriptPath;
    private int CurrentAd;
    public OrmmaPlayer audioPlayer;
    private boolean bGotLayoutParams;
    private boolean bKeyboardOut;
    private boolean bPageFinished;
    public boolean cacheAudio;
    public Bundle cacheData;
    public boolean cacheVideo;
    private Context context;
    private boolean displayCacheAd;
    private String[] exitUrl;
    private boolean isCachereq;
    private boolean isOnClicked;
    public boolean isVideo_AudioFullScreen;
    private int landscapeHeight;
    private int landscapeWidth;
    private int mContentViewHeight;
    private int mDefaultHeight;
    private int mDefaultWidth;
    public float mDensity;
    private GestureDetector mGestureDetector;
    private Handler mHandler;
    private int mIndex;
    private int mInitLayoutHeight;
    private int mInitLayoutWidth;
    private OrmmaViewListener mListener;
    private String mLocalFilePath;
    private OrmmaUtilityController mUtilityController;
    private ViewState mViewState;
    WebChromeClient mWebChromeClient;
    WebViewClient mWebViewClient;
    private String mapAPIKey;
    private boolean onPageFinishedCalled;
    private boolean onPageProgressFinished;
    private boolean onPageReceivedError;
    private int portraitHeight;
    private int portraitWidth;
    private final HashSet<String> registeredProtocols;
    private String shareUrl;
    private boolean sizeChanged;
    public int skipdelay;
    private String successMessage;
    public OrmmaPlayer videoPlayer;
    public VservAdController vservAdController;
    public VservAdManager vservAdManageractivity;

    class AnonymousClass_7 implements Runnable {
        private final /* synthetic */ Intent val$intent;

        AnonymousClass_7(Intent intent) {
            this.val$intent = intent;
        }

        public void run() {
            OrmmaView.this.getContext().startActivity(this.val$intent);
            if (OrmmaView.this.vservAdController.currentSkipDelay == -1) {
                OrmmaView.this.vservAdController.showClose();
            }
        }
    }

    class AnonymousClass_8 implements Runnable {
        private final /* synthetic */ Intent val$intent;

        AnonymousClass_8(Intent intent) {
            this.val$intent = intent;
        }

        public void run() {
            OrmmaView.this.vservAdManageractivity.startActivityForResult(this.val$intent, 0);
        }
    }

    public enum ACTION {
        PLAY_AUDIO,
        PLAY_VIDEO;

        static {
            PLAY_AUDIO = new mobi.vserv.org.ormma.view.OrmmaView.ACTION("PLAY_AUDIO", 0);
            PLAY_VIDEO = new mobi.vserv.org.ormma.view.OrmmaView.ACTION("PLAY_VIDEO", 1);
            ENUM$VALUES = new mobi.vserv.org.ormma.view.OrmmaView.ACTION[]{PLAY_AUDIO, PLAY_VIDEO};
        }
    }

    public static abstract class NewLocationReciever {
        public abstract void OnNewLocation(mobi.vserv.org.ormma.view.OrmmaView.ViewState viewState);
    }

    public static interface OrmmaViewListener {
        void handleRequest(String str);

        boolean onEventFired();

        boolean onExpand();

        boolean onExpandClose();

        boolean onReady();

        boolean onResize();

        boolean onResizeClose();
    }

    class ScrollEater extends SimpleOnGestureListener {
        ScrollEater() {
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }
    }

    public enum ViewState {
        DEFAULT,
        RESIZED,
        EXPANDED,
        HIDDEN,
        LEFT_BEHIND,
        OPENED;

        static {
            DEFAULT = new mobi.vserv.org.ormma.view.OrmmaView.ViewState("DEFAULT", 0);
            RESIZED = new mobi.vserv.org.ormma.view.OrmmaView.ViewState("RESIZED", 1);
            EXPANDED = new mobi.vserv.org.ormma.view.OrmmaView.ViewState("EXPANDED", 2);
            HIDDEN = new mobi.vserv.org.ormma.view.OrmmaView.ViewState("HIDDEN", 3);
            LEFT_BEHIND = new mobi.vserv.org.ormma.view.OrmmaView.ViewState("LEFT_BEHIND", 4);
            OPENED = new mobi.vserv.org.ormma.view.OrmmaView.ViewState("OPENED", 5);
            ENUM$VALUES = new mobi.vserv.org.ormma.view.OrmmaView.ViewState[]{DEFAULT, RESIZED, EXPANDED, HIDDEN, LEFT_BEHIND, OPENED};
        }
    }

    class AnonymousClass_10 implements OrmmaPlayerListener {
        private final /* synthetic */ PlayerProperties val$properties;

        AnonymousClass_10(PlayerProperties playerProperties) {
            this.val$properties = playerProperties;
        }

        public void onBufferingStarted() {
            if (OrmmaView.this.isOnClicked) {
                OrmmaView.this.vservAdController.currentSkipDelay = 0;
                OrmmaView.this.vservAdController.showskipDelay = false;
                OrmmaView.this.vservAdController.showSkipPage_onpageFinished();
            } else {
                OrmmaView.this.vservAdController.currentSkipDelay = OrmmaView.this.skipdelay;
                OrmmaView.this.vservAdController.showskipDelay = false;
                OrmmaView.this.vservAdController.showSkipPage_onpageFinished();
            }
        }

        public void onComplete() {
            if (Defines.ENABLE_lOGGING) {
                Log.i("vserv", "Video is complete playing OrmmaView");
                if (OrmmaView.this.vservAdManageractivity == null) {
                    Intent intent = new Intent("mobi.vserv.ad.dismiss_screen");
                    if (!(this.val$properties.exitOnComplete() || this.val$properties.inline)) {
                        intent.putExtra(Event.INTENT_EXTERNAL_BROWSER, "close browser only");
                    }
                    OrmmaView.this.context.sendBroadcast(intent);
                } else {
                    OrmmaView.this.vservAdController.showClose();
                }
            }
            FrameLayout background = (FrameLayout) OrmmaView.this.getRootView().findViewById(BACKGROUND_ID);
            if (background != null) {
                ((ViewGroup) background.getParent()).removeView(background);
            }
            OrmmaView.this.setVisibility(0);
        }

        public void onError() {
            onComplete();
        }

        public void onPrepared() {
            if (Defines.ENABLE_lOGGING) {
                Log.i("vserv", "ormmaview Video is onPrepared");
            }
        }
    }

    class AnonymousClass_12 implements OrmmaPlayerListener {
        private final /* synthetic */ PlayerProperties val$properties;

        AnonymousClass_12(PlayerProperties playerProperties) {
            this.val$properties = playerProperties;
        }

        public void onBufferingStarted() {
            if (OrmmaView.this.isOnClicked) {
                OrmmaView.this.vservAdController.currentSkipDelay = 0;
                OrmmaView.this.vservAdController.showskipDelay = false;
                OrmmaView.this.vservAdController.showSkipPage_onpageFinished();
            } else {
                OrmmaView.this.vservAdController.currentSkipDelay = OrmmaView.this.skipdelay;
                OrmmaView.this.vservAdController.showskipDelay = false;
                OrmmaView.this.vservAdController.showSkipPage_onpageFinished();
            }
        }

        public void onComplete() {
            if (Defines.ENABLE_lOGGING) {
                Log.i("vserv", "Video is complete playing OrmmaView");
                if (OrmmaView.this.vservAdManageractivity == null) {
                    Intent intent = new Intent("mobi.vserv.ad.dismiss_screen");
                    if (this.val$properties == null || !(this.val$properties.exitOnComplete() || this.val$properties.inline)) {
                        intent.putExtra(Event.INTENT_EXTERNAL_BROWSER, "close browser only");
                    }
                    OrmmaView.this.context.sendBroadcast(intent);
                } else {
                    OrmmaView.this.vservAdController.showClose();
                }
            }
            FrameLayout background = (FrameLayout) OrmmaView.this.getRootView().findViewById(BACKGROUND_ID);
            if (background != null) {
                ((ViewGroup) background.getParent()).removeView(background);
            }
            OrmmaView.this.setVisibility(0);
        }

        public void onError() {
            onComplete();
        }

        public void onPrepared() {
            if (Defines.ENABLE_lOGGING) {
                Log.i("vserv", "ormmaview Video is onPrepared");
            }
        }
    }

    static {
        attrs = new int[]{16843039, 16843040};
        mScriptPath = "//android_asset/js/ormma.js";
        mBridgeScriptPath = "//android_asset/js/ormma_bridge.js";
    }

    public OrmmaView(Context context, AttributeSet set) {
        super(context, set);
        this.isCachereq = false;
        this.displayCacheAd = false;
        this.CurrentAd = 0;
        this.cacheVideo = false;
        this.cacheAudio = false;
        this.isVideo_AudioFullScreen = false;
        this.cacheData = null;
        this.onPageFinishedCalled = true;
        this.onPageProgressFinished = true;
        this.onPageReceivedError = false;
        this.landscapeWidth = 0;
        this.landscapeHeight = 0;
        this.portraitWidth = 0;
        this.portraitHeight = 0;
        this.sizeChanged = false;
        this.skipdelay = 0;
        this.isOnClicked = false;
        this.mViewState = ViewState.DEFAULT;
        this.registeredProtocols = new HashSet();
        this.mHandler = new Handler() {
            private static /* synthetic */ int[] $SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ViewState;

            static /* synthetic */ int[] $SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ViewState() {
                int[] iArr = $SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ViewState;
                if (iArr == null) {
                    iArr = new int[mobi.vserv.org.ormma.view.OrmmaView.ViewState.values().length];
                    try {
                        iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.DEFAULT.ordinal()] = 1;
                    } catch (NoSuchFieldError e) {
                    }
                    try {
                        iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.EXPANDED.ordinal()] = 3;
                    } catch (NoSuchFieldError e2) {
                    }
                    try {
                        iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.HIDDEN.ordinal()] = 4;
                    } catch (NoSuchFieldError e3) {
                    }
                    try {
                        iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.LEFT_BEHIND.ordinal()] = 5;
                    } catch (NoSuchFieldError e4) {
                    }
                    try {
                        iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.OPENED.ordinal()] = 6;
                    } catch (NoSuchFieldError e5) {
                    }
                    try {
                        iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.RESIZED.ordinal()] = 2;
                    } catch (NoSuchFieldError e6) {
                    }
                    $SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ViewState = iArr;
                }
                return iArr;
            }

            public void handleMessage(Message msg) {
                Bundle data = msg.getData();
                switch (msg.what) {
                    case MESSAGE_RESIZE:
                        Log.i("vserv", "&&&&&&&&&&&Inside MESSAGE_RESIZE");
                        OrmmaView.this.mViewState = mobi.vserv.org.ormma.view.OrmmaView.ViewState.RESIZED;
                        LayoutParams lp = OrmmaView.this.getLayoutParams();
                        lp.height = data.getInt(RESIZE_HEIGHT, lp.height);
                        lp.width = data.getInt(RESIZE_WIDTH, lp.width);
                        OrmmaView.this.injectJavaScript(new StringBuilder("window.ormmaview.fireChangeEvent({ state: 'resized', size: { width: ").append(lp.width).append(", ").append("height: ").append(lp.height).append("}});").toString());
                        OrmmaView.this.requestLayout();
                        if (OrmmaView.this.mListener != null) {
                            OrmmaView.this.mListener.onResize();
                        }
                        break;
                    case MESSAGE_CLOSE:
                        Log.i("vserv", new StringBuilder("&&&&&&&&&&&Inside MESSAGE_CLOSE:: ").append(OrmmaView.this.mViewState).toString());
                        switch ($SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ViewState()[OrmmaView.this.mViewState.ordinal()]) {
                            case MMAdView.TRANSITION_UP:
                                OrmmaView.this.closeResized();
                                break;
                            case MMAdView.TRANSITION_DOWN:
                                OrmmaView.this.closeExpanded();
                                break;
                            default:
                                break;
                        }
                    case MESSAGE_HIDE:
                        OrmmaView.this.setVisibility(MMAdView.TRANSITION_RANDOM);
                        OrmmaView.this.injectJavaScript("window.ormmaview.fireChangeEvent({ state: 'hidden' });");
                        break;
                    case MESSAGE_SHOW:
                        OrmmaView.this.injectJavaScript("window.ormmaview.fireChangeEvent({ state: 'default' });");
                        OrmmaView.this.setVisibility(0);
                        break;
                    case MESSAGE_EXPAND:
                        OrmmaView.this.doExpand(data);
                        break;
                    case MESSAGE_SEND_EXPAND_CLOSE:
                        if (OrmmaView.this.mListener != null) {
                            OrmmaView.this.mListener.onExpandClose();
                        }
                        break;
                    case MESSAGE_OPEN:
                        OrmmaView.this.mViewState = mobi.vserv.org.ormma.view.OrmmaView.ViewState.LEFT_BEHIND;
                        break;
                    case MESSAGE_PLAY_VIDEO:
                        OrmmaView.this.playVideoImpl(data);
                        break;
                    case MESSAGE_PLAY_AUDIO:
                        OrmmaView.this.playAudioImpl(data);
                        break;
                    case MESSAGE_RAISE_ERROR:
                        String strMsg = data.getString(ERROR_MESSAGE);
                        OrmmaView.this.injectJavaScript(new StringBuilder("window.ormmaview.fireErrorEvent(\"").append(strMsg).append("\", \"").append(data.getString(ERROR_ACTION)).append("\")").toString());
                        break;
                    case MESSAGE_CACHE_VIDEO:
                        OrmmaView.this.cacheVideoImpl(data);
                        break;
                    case MESSAGE_CACHE_AUDIO:
                        OrmmaView.this.cacheAudioImpl(data);
                        break;
                }
                super.handleMessage(msg);
            }
        };
        this.mWebViewClient = new WebViewClient() {
            public void onLoadResource(WebView view, String url) {
            }

            public void onPageFinished(WebView view, String url) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", "onPageFinished!!!!!!!!!!");
                }
                try {
                    OrmmaView.this.mDefaultHeight = (int) (((float) OrmmaView.this.getHeight()) / OrmmaView.this.mDensity);
                    OrmmaView.this.mDefaultWidth = (int) (((float) OrmmaView.this.getWidth()) / OrmmaView.this.mDensity);
                    OrmmaView.this.mUtilityController.init(OrmmaView.this.mDensity);
                    if (OrmmaView.this.CurrentAd == 0) {
                        if (OrmmaView.this.onPageFinishedCalled) {
                            OrmmaView.this.onPageFinishedCalled = false;
                            if (Defines.ENABLE_lOGGING) {
                                Log.i("vserv", "onPageFinished ORMMAReady11 init!!!!!!!!!!");
                            }
                            OrmmaView.this.injectJavaScript("ORMMAReady11();");
                        }
                    } else if (OrmmaView.this.onPageFinishedCalled) {
                        OrmmaView.this.onPageFinishedCalled = false;
                        if (!OrmmaView.this.onPageReceivedError) {
                            if (Defines.ENABLE_lOGGING) {
                                Log.i("vserv", new StringBuilder("if onPageFinished ORMMAReady12 init!!!!!!!!!!: ").append(OrmmaView.this.onPageReceivedError).toString());
                            }
                            if (OrmmaView.this.cacheVideo) {
                                OrmmaView.this.cacheVideo = false;
                                if (OrmmaView.this.videoPlayer != null) {
                                    OrmmaView.this.vservAdController.storeCacheAdView();
                                    OrmmaView.this.vservAdController.storeCacheAdType(MMRequest.MARITAL_OTHER);
                                    OrmmaView.this.vservAdController.storeCacheImpressionHeader();
                                    OrmmaView.this.vservAdController.removeCacheAdType();
                                    if (OrmmaView.this.isVideo_AudioFullScreen) {
                                        OrmmaView.this.isVideo_AudioFullScreen = false;
                                        OrmmaView.this.vservAdController.storeCacheAdType(OrmmaView.this.cacheData);
                                    } else {
                                        OrmmaView.this.isVideo_AudioFullScreen = false;
                                        OrmmaView.this.vservAdController.storeCacheAdType("video");
                                    }
                                    OrmmaView.this.vservAdController.storeCachePlayer(OrmmaView.this.videoPlayer);
                                    OrmmaView.this.cacheData = null;
                                    if (Defines.ENABLE_lOGGING) {
                                        Log.i("vserv", "Video is stored");
                                    }
                                }
                            } else if (OrmmaView.this.cacheAudio) {
                                OrmmaView.this.cacheAudio = false;
                                if (OrmmaView.this.audioPlayer != null) {
                                    OrmmaView.this.vservAdController.storeCacheAdView();
                                    OrmmaView.this.vservAdController.storeCacheAdType(MMRequest.MARITAL_OTHER);
                                    OrmmaView.this.vservAdController.storeCacheImpressionHeader();
                                    OrmmaView.this.vservAdController.storeCachePlayer(OrmmaView.this.audioPlayer);
                                    OrmmaView.this.vservAdController.removeCacheAdType();
                                    if (OrmmaView.this.isVideo_AudioFullScreen) {
                                        OrmmaView.this.isVideo_AudioFullScreen = false;
                                        OrmmaView.this.vservAdController.storeCacheAdType(OrmmaView.this.cacheData);
                                    } else {
                                        OrmmaView.this.isVideo_AudioFullScreen = false;
                                        OrmmaView.this.vservAdController.storeCacheAdType("audio");
                                    }
                                    OrmmaView.this.cacheData = null;
                                    if (Defines.ENABLE_lOGGING) {
                                        Log.i("vserv", "Audio is stored");
                                    }
                                }
                            } else {
                                OrmmaView.this.vservAdController.storeCacheAdView();
                                OrmmaView.this.vservAdController.storeCacheAdType(MMRequest.MARITAL_OTHER);
                                OrmmaView.this.vservAdController.storeCacheImpressionHeader();
                            }
                            if (OrmmaView.this.mUtilityController.cachePostActionUrl != null) {
                                OrmmaView.this.vservAdController.storePostActionUrl(OrmmaView.this.mUtilityController.cachePostActionUrl);
                            }
                            OrmmaView.this.vservAdController.storeCacheDownloadNetworkUrl();
                            OrmmaView.this.vservAdController.storeCacheADOrientation();
                            OrmmaView.this.vservAdController.storeCacheADSkipDelay();
                            OrmmaView.this.vservAdController.storeCacheADRequestId();
                            OrmmaView.this.injectJavaScript("ORMMAReady12();");
                        } else if (Defines.ENABLE_lOGGING) {
                            Log.i("vserv", new StringBuilder("else onPageFinished ORMMAReady12 init!!!!!!!!!!: ").append(OrmmaView.this.onPageReceivedError).toString());
                        }
                    }
                } catch (Exception e) {
                    e = e;
                    e.printStackTrace();
                    if (Defines.ENABLE_lOGGING) {
                        Exception e2;
                        Log.i("vserv", new StringBuilder("onPageFinished Exception:: ").append(e2).toString());
                    }
                }
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", new StringBuilder("onPageStarted!!!!!!!!!!").append(OrmmaView.this.CurrentAd).toString());
                }
            }

            public void onProgressChanged(WebView view, int progress) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", new StringBuilder("onProgressChanged!!!!!!!!!!").append(progress).toString());
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (Defines.ENABLE_lOGGING) {
                    Log.d("vserv", new StringBuilder("onReceivedError:").append(description).toString());
                    Log.d("vserv", new StringBuilder("onReceivedError2:").append(OrmmaView.this.CurrentAd).toString());
                }
                if (!OrmmaView.this.onPageReceivedError) {
                    OrmmaView.this.onPageReceivedError = true;
                    if (OrmmaView.this.vservAdController != null) {
                        if (Defines.ENABLE_lOGGING) {
                            Log.i("vserv", "onProgressChanged showskipPage!!!!!!!!!!");
                        }
                        OrmmaView.this.vservAdController.showSkipPage_onpageFinished();
                    }
                }
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if (Defines.ENABLE_lOGGING) {
                    Log.d("vserv", "onReceivedSslError:");
                }
                if (OrmmaView.this.vservAdController != null) {
                    OrmmaView.this.vservAdController.onReceiveError();
                }
                super.onReceivedSslError(view, handler, error);
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                boolean z = true;
                Uri uri = Uri.parse(url);
                try {
                    if (OrmmaView.this.mListener != null && OrmmaView.this.isRegisteredProtocol(uri)) {
                        OrmmaView.this.mListener.handleRequest(url);
                        return z;
                    } else if (url.startsWith("tel:")) {
                        intent = new Intent("android.intent.action.DIAL", Uri.parse(url));
                        intent.addFlags(DriveFile.MODE_READ_ONLY);
                        OrmmaView.this.getContext().startActivity(intent);
                        return z;
                    } else if (url.startsWith("mailto:")) {
                        intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                        intent.addFlags(DriveFile.MODE_READ_ONLY);
                        OrmmaView.this.getContext().startActivity(intent);
                        return z;
                    } else {
                        intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        intent.setData(uri);
                        intent.addFlags(DriveFile.MODE_READ_ONLY);
                        OrmmaView.this.getContext().startActivity(intent);
                        return z;
                    }
                } catch (Exception e) {
                    try {
                        Intent intent2;
                        intent2 = new Intent();
                        intent2.setAction("android.intent.action.VIEW");
                        intent2.setData(uri);
                        intent2.addFlags(DriveFile.MODE_READ_ONLY);
                        OrmmaView.this.getContext().startActivity(intent2);
                        return z;
                    } catch (Exception e2) {
                        return false;
                    }
                }
            }
        };
        this.mWebChromeClient = new WebChromeClient() {
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return false;
            }

            public void onProgressChanged(WebView view, int progress) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", new StringBuilder("onProgressChanged!!!!!!!!!!").append(progress).toString());
                }
                if (OrmmaView.this.onPageProgressFinished) {
                    OrmmaView.this.onPageProgressFinished = false;
                    if (OrmmaView.this.CurrentAd == 0 && OrmmaView.this.vservAdController != null) {
                        if (Defines.ENABLE_lOGGING) {
                            Log.i("vserv", "onProgressChanged showskipPage!!!!!!!!!!");
                        }
                        OrmmaView.this.vservAdController.showSkipPage_onpageFinished();
                    }
                }
            }
        };
        this.shareUrl = Preconditions.EMPTY_ARGUMENTS;
        this.successMessage = Preconditions.EMPTY_ARGUMENTS;
        initialize();
        TypedArray a = getContext().obtainStyledAttributes(set, attrs);
        int w = a.getDimensionPixelSize(0, -1);
        int h = a.getDimensionPixelSize(1, -1);
        if (w > 0 && h > 0) {
            this.mUtilityController.setMaxSize(w, h);
        }
        a.recycle();
    }

    public OrmmaView(Context context, VservAdController vservAdController, int cachereq) {
        super(context);
        this.isCachereq = false;
        this.displayCacheAd = false;
        this.CurrentAd = 0;
        this.cacheVideo = false;
        this.cacheAudio = false;
        this.isVideo_AudioFullScreen = false;
        this.cacheData = null;
        this.onPageFinishedCalled = true;
        this.onPageProgressFinished = true;
        this.onPageReceivedError = false;
        this.landscapeWidth = 0;
        this.landscapeHeight = 0;
        this.portraitWidth = 0;
        this.portraitHeight = 0;
        this.sizeChanged = false;
        this.skipdelay = 0;
        this.isOnClicked = false;
        this.mViewState = ViewState.DEFAULT;
        this.registeredProtocols = new HashSet();
        this.mHandler = new Handler() {
            private static /* synthetic */ int[] $SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ViewState;

            static /* synthetic */ int[] $SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ViewState() {
                int[] iArr = $SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ViewState;
                if (iArr == null) {
                    iArr = new int[mobi.vserv.org.ormma.view.OrmmaView.ViewState.values().length];
                    iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.DEFAULT.ordinal()] = 1;
                    iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.EXPANDED.ordinal()] = 3;
                    iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.HIDDEN.ordinal()] = 4;
                    iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.LEFT_BEHIND.ordinal()] = 5;
                    iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.OPENED.ordinal()] = 6;
                    iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.RESIZED.ordinal()] = 2;
                    $SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ViewState = iArr;
                }
                return iArr;
            }

            public void handleMessage(Message msg) {
                Bundle data = msg.getData();
                switch (msg.what) {
                    case MESSAGE_RESIZE:
                        Log.i("vserv", "&&&&&&&&&&&Inside MESSAGE_RESIZE");
                        OrmmaView.this.mViewState = mobi.vserv.org.ormma.view.OrmmaView.ViewState.RESIZED;
                        LayoutParams lp = OrmmaView.this.getLayoutParams();
                        lp.height = data.getInt(RESIZE_HEIGHT, lp.height);
                        lp.width = data.getInt(RESIZE_WIDTH, lp.width);
                        OrmmaView.this.injectJavaScript(new StringBuilder("window.ormmaview.fireChangeEvent({ state: 'resized', size: { width: ").append(lp.width).append(", ").append("height: ").append(lp.height).append("}});").toString());
                        OrmmaView.this.requestLayout();
                        if (OrmmaView.this.mListener != null) {
                            OrmmaView.this.mListener.onResize();
                        }
                        break;
                    case MESSAGE_CLOSE:
                        Log.i("vserv", new StringBuilder("&&&&&&&&&&&Inside MESSAGE_CLOSE:: ").append(OrmmaView.this.mViewState).toString());
                        switch ($SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ViewState()[OrmmaView.this.mViewState.ordinal()]) {
                            case MMAdView.TRANSITION_UP:
                                OrmmaView.this.closeResized();
                                break;
                            case MMAdView.TRANSITION_DOWN:
                                OrmmaView.this.closeExpanded();
                                break;
                            default:
                                break;
                        }
                    case MESSAGE_HIDE:
                        OrmmaView.this.setVisibility(MMAdView.TRANSITION_RANDOM);
                        OrmmaView.this.injectJavaScript("window.ormmaview.fireChangeEvent({ state: 'hidden' });");
                        break;
                    case MESSAGE_SHOW:
                        OrmmaView.this.injectJavaScript("window.ormmaview.fireChangeEvent({ state: 'default' });");
                        OrmmaView.this.setVisibility(0);
                        break;
                    case MESSAGE_EXPAND:
                        OrmmaView.this.doExpand(data);
                        break;
                    case MESSAGE_SEND_EXPAND_CLOSE:
                        if (OrmmaView.this.mListener != null) {
                            OrmmaView.this.mListener.onExpandClose();
                        }
                        break;
                    case MESSAGE_OPEN:
                        OrmmaView.this.mViewState = mobi.vserv.org.ormma.view.OrmmaView.ViewState.LEFT_BEHIND;
                        break;
                    case MESSAGE_PLAY_VIDEO:
                        OrmmaView.this.playVideoImpl(data);
                        break;
                    case MESSAGE_PLAY_AUDIO:
                        OrmmaView.this.playAudioImpl(data);
                        break;
                    case MESSAGE_RAISE_ERROR:
                        String strMsg = data.getString(ERROR_MESSAGE);
                        OrmmaView.this.injectJavaScript(new StringBuilder("window.ormmaview.fireErrorEvent(\"").append(strMsg).append("\", \"").append(data.getString(ERROR_ACTION)).append("\")").toString());
                        break;
                    case MESSAGE_CACHE_VIDEO:
                        OrmmaView.this.cacheVideoImpl(data);
                        break;
                    case MESSAGE_CACHE_AUDIO:
                        OrmmaView.this.cacheAudioImpl(data);
                        break;
                }
                super.handleMessage(msg);
            }
        };
        this.mWebViewClient = new WebViewClient() {
            public void onLoadResource(WebView view, String url) {
            }

            public void onPageFinished(WebView view, String url) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", "onPageFinished!!!!!!!!!!");
                }
                OrmmaView.this.mDefaultHeight = (int) (((float) OrmmaView.this.getHeight()) / OrmmaView.this.mDensity);
                OrmmaView.this.mDefaultWidth = (int) (((float) OrmmaView.this.getWidth()) / OrmmaView.this.mDensity);
                OrmmaView.this.mUtilityController.init(OrmmaView.this.mDensity);
                if (OrmmaView.this.CurrentAd == 0) {
                    if (OrmmaView.this.onPageFinishedCalled) {
                        OrmmaView.this.onPageFinishedCalled = false;
                        if (Defines.ENABLE_lOGGING) {
                            Log.i("vserv", "onPageFinished ORMMAReady11 init!!!!!!!!!!");
                        }
                        OrmmaView.this.injectJavaScript("ORMMAReady11();");
                    }
                } else if (OrmmaView.this.onPageFinishedCalled) {
                    OrmmaView.this.onPageFinishedCalled = false;
                    if (!OrmmaView.this.onPageReceivedError) {
                        if (Defines.ENABLE_lOGGING) {
                            Log.i("vserv", new StringBuilder("if onPageFinished ORMMAReady12 init!!!!!!!!!!: ").append(OrmmaView.this.onPageReceivedError).toString());
                        }
                        if (OrmmaView.this.cacheVideo) {
                            OrmmaView.this.cacheVideo = false;
                            if (OrmmaView.this.videoPlayer != null) {
                                OrmmaView.this.vservAdController.storeCacheAdView();
                                OrmmaView.this.vservAdController.storeCacheAdType(MMRequest.MARITAL_OTHER);
                                OrmmaView.this.vservAdController.storeCacheImpressionHeader();
                                OrmmaView.this.vservAdController.removeCacheAdType();
                                if (OrmmaView.this.isVideo_AudioFullScreen) {
                                    OrmmaView.this.isVideo_AudioFullScreen = false;
                                    OrmmaView.this.vservAdController.storeCacheAdType(OrmmaView.this.cacheData);
                                } else {
                                    OrmmaView.this.isVideo_AudioFullScreen = false;
                                    OrmmaView.this.vservAdController.storeCacheAdType("video");
                                }
                                OrmmaView.this.vservAdController.storeCachePlayer(OrmmaView.this.videoPlayer);
                                OrmmaView.this.cacheData = null;
                                if (Defines.ENABLE_lOGGING) {
                                    Log.i("vserv", "Video is stored");
                                }
                            }
                        } else if (OrmmaView.this.cacheAudio) {
                            OrmmaView.this.cacheAudio = false;
                            if (OrmmaView.this.audioPlayer != null) {
                                OrmmaView.this.vservAdController.storeCacheAdView();
                                OrmmaView.this.vservAdController.storeCacheAdType(MMRequest.MARITAL_OTHER);
                                OrmmaView.this.vservAdController.storeCacheImpressionHeader();
                                OrmmaView.this.vservAdController.storeCachePlayer(OrmmaView.this.audioPlayer);
                                OrmmaView.this.vservAdController.removeCacheAdType();
                                if (OrmmaView.this.isVideo_AudioFullScreen) {
                                    OrmmaView.this.isVideo_AudioFullScreen = false;
                                    OrmmaView.this.vservAdController.storeCacheAdType(OrmmaView.this.cacheData);
                                } else {
                                    OrmmaView.this.isVideo_AudioFullScreen = false;
                                    OrmmaView.this.vservAdController.storeCacheAdType("audio");
                                }
                                OrmmaView.this.cacheData = null;
                                if (Defines.ENABLE_lOGGING) {
                                    Log.i("vserv", "Audio is stored");
                                }
                            }
                        } else {
                            OrmmaView.this.vservAdController.storeCacheAdView();
                            OrmmaView.this.vservAdController.storeCacheAdType(MMRequest.MARITAL_OTHER);
                            OrmmaView.this.vservAdController.storeCacheImpressionHeader();
                        }
                        if (OrmmaView.this.mUtilityController.cachePostActionUrl != null) {
                            OrmmaView.this.vservAdController.storePostActionUrl(OrmmaView.this.mUtilityController.cachePostActionUrl);
                        }
                        OrmmaView.this.vservAdController.storeCacheDownloadNetworkUrl();
                        OrmmaView.this.vservAdController.storeCacheADOrientation();
                        OrmmaView.this.vservAdController.storeCacheADSkipDelay();
                        OrmmaView.this.vservAdController.storeCacheADRequestId();
                        OrmmaView.this.injectJavaScript("ORMMAReady12();");
                    } else if (Defines.ENABLE_lOGGING) {
                        Log.i("vserv", new StringBuilder("else onPageFinished ORMMAReady12 init!!!!!!!!!!: ").append(OrmmaView.this.onPageReceivedError).toString());
                    }
                }
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", new StringBuilder("onPageStarted!!!!!!!!!!").append(OrmmaView.this.CurrentAd).toString());
                }
            }

            public void onProgressChanged(WebView view, int progress) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", new StringBuilder("onProgressChanged!!!!!!!!!!").append(progress).toString());
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (Defines.ENABLE_lOGGING) {
                    Log.d("vserv", new StringBuilder("onReceivedError:").append(description).toString());
                    Log.d("vserv", new StringBuilder("onReceivedError2:").append(OrmmaView.this.CurrentAd).toString());
                }
                if (!OrmmaView.this.onPageReceivedError) {
                    OrmmaView.this.onPageReceivedError = true;
                    if (OrmmaView.this.vservAdController != null) {
                        if (Defines.ENABLE_lOGGING) {
                            Log.i("vserv", "onProgressChanged showskipPage!!!!!!!!!!");
                        }
                        OrmmaView.this.vservAdController.showSkipPage_onpageFinished();
                    }
                }
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if (Defines.ENABLE_lOGGING) {
                    Log.d("vserv", "onReceivedSslError:");
                }
                if (OrmmaView.this.vservAdController != null) {
                    OrmmaView.this.vservAdController.onReceiveError();
                }
                super.onReceivedSslError(view, handler, error);
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                boolean z = true;
                Uri uri = Uri.parse(url);
                if (OrmmaView.this.mListener != null && OrmmaView.this.isRegisteredProtocol(uri)) {
                    OrmmaView.this.mListener.handleRequest(url);
                    return z;
                } else if (url.startsWith("tel:")) {
                    intent2 = new Intent("android.intent.action.DIAL", Uri.parse(url));
                    intent2.addFlags(DriveFile.MODE_READ_ONLY);
                    OrmmaView.this.getContext().startActivity(intent2);
                    return z;
                } else if (url.startsWith("mailto:")) {
                    intent2 = new Intent("android.intent.action.VIEW", Uri.parse(url));
                    intent2.addFlags(DriveFile.MODE_READ_ONLY);
                    OrmmaView.this.getContext().startActivity(intent2);
                    return z;
                } else {
                    intent2 = new Intent();
                    intent2.setAction("android.intent.action.VIEW");
                    intent2.setData(uri);
                    intent2.addFlags(DriveFile.MODE_READ_ONLY);
                    OrmmaView.this.getContext().startActivity(intent2);
                    return z;
                }
            }
        };
        this.mWebChromeClient = new WebChromeClient() {
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return false;
            }

            public void onProgressChanged(WebView view, int progress) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", new StringBuilder("onProgressChanged!!!!!!!!!!").append(progress).toString());
                }
                if (OrmmaView.this.onPageProgressFinished) {
                    OrmmaView.this.onPageProgressFinished = false;
                    if (OrmmaView.this.CurrentAd == 0 && OrmmaView.this.vservAdController != null) {
                        if (Defines.ENABLE_lOGGING) {
                            Log.i("vserv", "onProgressChanged showskipPage!!!!!!!!!!");
                        }
                        OrmmaView.this.vservAdController.showSkipPage_onpageFinished();
                    }
                }
            }
        };
        this.shareUrl = Preconditions.EMPTY_ARGUMENTS;
        this.successMessage = Preconditions.EMPTY_ARGUMENTS;
        this.context = context;
        this.CurrentAd = cachereq;
        this.vservAdController = vservAdController;
        initialize();
    }

    public OrmmaView(Context context, VservAdController vservAdController, VservAdManager vservAdManageractivity, int cachereq) {
        super(context);
        this.isCachereq = false;
        this.displayCacheAd = false;
        this.CurrentAd = 0;
        this.cacheVideo = false;
        this.cacheAudio = false;
        this.isVideo_AudioFullScreen = false;
        this.cacheData = null;
        this.onPageFinishedCalled = true;
        this.onPageProgressFinished = true;
        this.onPageReceivedError = false;
        this.landscapeWidth = 0;
        this.landscapeHeight = 0;
        this.portraitWidth = 0;
        this.portraitHeight = 0;
        this.sizeChanged = false;
        this.skipdelay = 0;
        this.isOnClicked = false;
        this.mViewState = ViewState.DEFAULT;
        this.registeredProtocols = new HashSet();
        this.mHandler = new Handler() {
            private static /* synthetic */ int[] $SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ViewState;

            static /* synthetic */ int[] $SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ViewState() {
                int[] iArr = $SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ViewState;
                if (iArr == null) {
                    iArr = new int[mobi.vserv.org.ormma.view.OrmmaView.ViewState.values().length];
                    iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.DEFAULT.ordinal()] = 1;
                    iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.EXPANDED.ordinal()] = 3;
                    iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.HIDDEN.ordinal()] = 4;
                    iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.LEFT_BEHIND.ordinal()] = 5;
                    iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.OPENED.ordinal()] = 6;
                    iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.RESIZED.ordinal()] = 2;
                    $SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ViewState = iArr;
                }
                return iArr;
            }

            public void handleMessage(Message msg) {
                Bundle data = msg.getData();
                switch (msg.what) {
                    case MESSAGE_RESIZE:
                        Log.i("vserv", "&&&&&&&&&&&Inside MESSAGE_RESIZE");
                        OrmmaView.this.mViewState = mobi.vserv.org.ormma.view.OrmmaView.ViewState.RESIZED;
                        LayoutParams lp = OrmmaView.this.getLayoutParams();
                        lp.height = data.getInt(RESIZE_HEIGHT, lp.height);
                        lp.width = data.getInt(RESIZE_WIDTH, lp.width);
                        OrmmaView.this.injectJavaScript(new StringBuilder("window.ormmaview.fireChangeEvent({ state: 'resized', size: { width: ").append(lp.width).append(", ").append("height: ").append(lp.height).append("}});").toString());
                        OrmmaView.this.requestLayout();
                        if (OrmmaView.this.mListener != null) {
                            OrmmaView.this.mListener.onResize();
                        }
                        break;
                    case MESSAGE_CLOSE:
                        Log.i("vserv", new StringBuilder("&&&&&&&&&&&Inside MESSAGE_CLOSE:: ").append(OrmmaView.this.mViewState).toString());
                        switch ($SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ViewState()[OrmmaView.this.mViewState.ordinal()]) {
                            case MMAdView.TRANSITION_UP:
                                OrmmaView.this.closeResized();
                                break;
                            case MMAdView.TRANSITION_DOWN:
                                OrmmaView.this.closeExpanded();
                                break;
                            default:
                                break;
                        }
                    case MESSAGE_HIDE:
                        OrmmaView.this.setVisibility(MMAdView.TRANSITION_RANDOM);
                        OrmmaView.this.injectJavaScript("window.ormmaview.fireChangeEvent({ state: 'hidden' });");
                        break;
                    case MESSAGE_SHOW:
                        OrmmaView.this.injectJavaScript("window.ormmaview.fireChangeEvent({ state: 'default' });");
                        OrmmaView.this.setVisibility(0);
                        break;
                    case MESSAGE_EXPAND:
                        OrmmaView.this.doExpand(data);
                        break;
                    case MESSAGE_SEND_EXPAND_CLOSE:
                        if (OrmmaView.this.mListener != null) {
                            OrmmaView.this.mListener.onExpandClose();
                        }
                        break;
                    case MESSAGE_OPEN:
                        OrmmaView.this.mViewState = mobi.vserv.org.ormma.view.OrmmaView.ViewState.LEFT_BEHIND;
                        break;
                    case MESSAGE_PLAY_VIDEO:
                        OrmmaView.this.playVideoImpl(data);
                        break;
                    case MESSAGE_PLAY_AUDIO:
                        OrmmaView.this.playAudioImpl(data);
                        break;
                    case MESSAGE_RAISE_ERROR:
                        String strMsg = data.getString(ERROR_MESSAGE);
                        OrmmaView.this.injectJavaScript(new StringBuilder("window.ormmaview.fireErrorEvent(\"").append(strMsg).append("\", \"").append(data.getString(ERROR_ACTION)).append("\")").toString());
                        break;
                    case MESSAGE_CACHE_VIDEO:
                        OrmmaView.this.cacheVideoImpl(data);
                        break;
                    case MESSAGE_CACHE_AUDIO:
                        OrmmaView.this.cacheAudioImpl(data);
                        break;
                }
                super.handleMessage(msg);
            }
        };
        this.mWebViewClient = new WebViewClient() {
            public void onLoadResource(WebView view, String url) {
            }

            public void onPageFinished(WebView view, String url) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", "onPageFinished!!!!!!!!!!");
                }
                OrmmaView.this.mDefaultHeight = (int) (((float) OrmmaView.this.getHeight()) / OrmmaView.this.mDensity);
                OrmmaView.this.mDefaultWidth = (int) (((float) OrmmaView.this.getWidth()) / OrmmaView.this.mDensity);
                OrmmaView.this.mUtilityController.init(OrmmaView.this.mDensity);
                if (OrmmaView.this.CurrentAd == 0) {
                    if (OrmmaView.this.onPageFinishedCalled) {
                        OrmmaView.this.onPageFinishedCalled = false;
                        if (Defines.ENABLE_lOGGING) {
                            Log.i("vserv", "onPageFinished ORMMAReady11 init!!!!!!!!!!");
                        }
                        OrmmaView.this.injectJavaScript("ORMMAReady11();");
                    }
                } else if (OrmmaView.this.onPageFinishedCalled) {
                    OrmmaView.this.onPageFinishedCalled = false;
                    if (!OrmmaView.this.onPageReceivedError) {
                        if (Defines.ENABLE_lOGGING) {
                            Log.i("vserv", new StringBuilder("if onPageFinished ORMMAReady12 init!!!!!!!!!!: ").append(OrmmaView.this.onPageReceivedError).toString());
                        }
                        if (OrmmaView.this.cacheVideo) {
                            OrmmaView.this.cacheVideo = false;
                            if (OrmmaView.this.videoPlayer != null) {
                                OrmmaView.this.vservAdController.storeCacheAdView();
                                OrmmaView.this.vservAdController.storeCacheAdType(MMRequest.MARITAL_OTHER);
                                OrmmaView.this.vservAdController.storeCacheImpressionHeader();
                                OrmmaView.this.vservAdController.removeCacheAdType();
                                if (OrmmaView.this.isVideo_AudioFullScreen) {
                                    OrmmaView.this.isVideo_AudioFullScreen = false;
                                    OrmmaView.this.vservAdController.storeCacheAdType(OrmmaView.this.cacheData);
                                } else {
                                    OrmmaView.this.isVideo_AudioFullScreen = false;
                                    OrmmaView.this.vservAdController.storeCacheAdType("video");
                                }
                                OrmmaView.this.vservAdController.storeCachePlayer(OrmmaView.this.videoPlayer);
                                OrmmaView.this.cacheData = null;
                                if (Defines.ENABLE_lOGGING) {
                                    Log.i("vserv", "Video is stored");
                                }
                            }
                        } else if (OrmmaView.this.cacheAudio) {
                            OrmmaView.this.cacheAudio = false;
                            if (OrmmaView.this.audioPlayer != null) {
                                OrmmaView.this.vservAdController.storeCacheAdView();
                                OrmmaView.this.vservAdController.storeCacheAdType(MMRequest.MARITAL_OTHER);
                                OrmmaView.this.vservAdController.storeCacheImpressionHeader();
                                OrmmaView.this.vservAdController.storeCachePlayer(OrmmaView.this.audioPlayer);
                                OrmmaView.this.vservAdController.removeCacheAdType();
                                if (OrmmaView.this.isVideo_AudioFullScreen) {
                                    OrmmaView.this.isVideo_AudioFullScreen = false;
                                    OrmmaView.this.vservAdController.storeCacheAdType(OrmmaView.this.cacheData);
                                } else {
                                    OrmmaView.this.isVideo_AudioFullScreen = false;
                                    OrmmaView.this.vservAdController.storeCacheAdType("audio");
                                }
                                OrmmaView.this.cacheData = null;
                                if (Defines.ENABLE_lOGGING) {
                                    Log.i("vserv", "Audio is stored");
                                }
                            }
                        } else {
                            OrmmaView.this.vservAdController.storeCacheAdView();
                            OrmmaView.this.vservAdController.storeCacheAdType(MMRequest.MARITAL_OTHER);
                            OrmmaView.this.vservAdController.storeCacheImpressionHeader();
                        }
                        if (OrmmaView.this.mUtilityController.cachePostActionUrl != null) {
                            OrmmaView.this.vservAdController.storePostActionUrl(OrmmaView.this.mUtilityController.cachePostActionUrl);
                        }
                        OrmmaView.this.vservAdController.storeCacheDownloadNetworkUrl();
                        OrmmaView.this.vservAdController.storeCacheADOrientation();
                        OrmmaView.this.vservAdController.storeCacheADSkipDelay();
                        OrmmaView.this.vservAdController.storeCacheADRequestId();
                        OrmmaView.this.injectJavaScript("ORMMAReady12();");
                    } else if (Defines.ENABLE_lOGGING) {
                        Log.i("vserv", new StringBuilder("else onPageFinished ORMMAReady12 init!!!!!!!!!!: ").append(OrmmaView.this.onPageReceivedError).toString());
                    }
                }
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", new StringBuilder("onPageStarted!!!!!!!!!!").append(OrmmaView.this.CurrentAd).toString());
                }
            }

            public void onProgressChanged(WebView view, int progress) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", new StringBuilder("onProgressChanged!!!!!!!!!!").append(progress).toString());
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (Defines.ENABLE_lOGGING) {
                    Log.d("vserv", new StringBuilder("onReceivedError:").append(description).toString());
                    Log.d("vserv", new StringBuilder("onReceivedError2:").append(OrmmaView.this.CurrentAd).toString());
                }
                if (!OrmmaView.this.onPageReceivedError) {
                    OrmmaView.this.onPageReceivedError = true;
                    if (OrmmaView.this.vservAdController != null) {
                        if (Defines.ENABLE_lOGGING) {
                            Log.i("vserv", "onProgressChanged showskipPage!!!!!!!!!!");
                        }
                        OrmmaView.this.vservAdController.showSkipPage_onpageFinished();
                    }
                }
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if (Defines.ENABLE_lOGGING) {
                    Log.d("vserv", "onReceivedSslError:");
                }
                if (OrmmaView.this.vservAdController != null) {
                    OrmmaView.this.vservAdController.onReceiveError();
                }
                super.onReceivedSslError(view, handler, error);
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                boolean z = true;
                Uri uri = Uri.parse(url);
                if (OrmmaView.this.mListener != null && OrmmaView.this.isRegisteredProtocol(uri)) {
                    OrmmaView.this.mListener.handleRequest(url);
                    return z;
                } else if (url.startsWith("tel:")) {
                    intent2 = new Intent("android.intent.action.DIAL", Uri.parse(url));
                    intent2.addFlags(DriveFile.MODE_READ_ONLY);
                    OrmmaView.this.getContext().startActivity(intent2);
                    return z;
                } else if (url.startsWith("mailto:")) {
                    intent2 = new Intent("android.intent.action.VIEW", Uri.parse(url));
                    intent2.addFlags(DriveFile.MODE_READ_ONLY);
                    OrmmaView.this.getContext().startActivity(intent2);
                    return z;
                } else {
                    intent2 = new Intent();
                    intent2.setAction("android.intent.action.VIEW");
                    intent2.setData(uri);
                    intent2.addFlags(DriveFile.MODE_READ_ONLY);
                    OrmmaView.this.getContext().startActivity(intent2);
                    return z;
                }
            }
        };
        this.mWebChromeClient = new WebChromeClient() {
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return false;
            }

            public void onProgressChanged(WebView view, int progress) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", new StringBuilder("onProgressChanged!!!!!!!!!!").append(progress).toString());
                }
                if (OrmmaView.this.onPageProgressFinished) {
                    OrmmaView.this.onPageProgressFinished = false;
                    if (OrmmaView.this.CurrentAd == 0 && OrmmaView.this.vservAdController != null) {
                        if (Defines.ENABLE_lOGGING) {
                            Log.i("vserv", "onProgressChanged showskipPage!!!!!!!!!!");
                        }
                        OrmmaView.this.vservAdController.showSkipPage_onpageFinished();
                    }
                }
            }
        };
        this.shareUrl = Preconditions.EMPTY_ARGUMENTS;
        this.successMessage = Preconditions.EMPTY_ARGUMENTS;
        this.context = context;
        this.CurrentAd = cachereq;
        this.vservAdController = vservAdController;
        this.vservAdManageractivity = vservAdManageractivity;
        initialize();
    }

    public OrmmaView(Context context, OrmmaViewListener listener) {
        super(context);
        this.isCachereq = false;
        this.displayCacheAd = false;
        this.CurrentAd = 0;
        this.cacheVideo = false;
        this.cacheAudio = false;
        this.isVideo_AudioFullScreen = false;
        this.cacheData = null;
        this.onPageFinishedCalled = true;
        this.onPageProgressFinished = true;
        this.onPageReceivedError = false;
        this.landscapeWidth = 0;
        this.landscapeHeight = 0;
        this.portraitWidth = 0;
        this.portraitHeight = 0;
        this.sizeChanged = false;
        this.skipdelay = 0;
        this.isOnClicked = false;
        this.mViewState = ViewState.DEFAULT;
        this.registeredProtocols = new HashSet();
        this.mHandler = new Handler() {
            private static /* synthetic */ int[] $SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ViewState;

            static /* synthetic */ int[] $SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ViewState() {
                int[] iArr = $SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ViewState;
                if (iArr == null) {
                    iArr = new int[mobi.vserv.org.ormma.view.OrmmaView.ViewState.values().length];
                    iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.DEFAULT.ordinal()] = 1;
                    iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.EXPANDED.ordinal()] = 3;
                    iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.HIDDEN.ordinal()] = 4;
                    iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.LEFT_BEHIND.ordinal()] = 5;
                    iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.OPENED.ordinal()] = 6;
                    iArr[mobi.vserv.org.ormma.view.OrmmaView.ViewState.RESIZED.ordinal()] = 2;
                    $SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ViewState = iArr;
                }
                return iArr;
            }

            public void handleMessage(Message msg) {
                Bundle data = msg.getData();
                switch (msg.what) {
                    case MESSAGE_RESIZE:
                        Log.i("vserv", "&&&&&&&&&&&Inside MESSAGE_RESIZE");
                        OrmmaView.this.mViewState = mobi.vserv.org.ormma.view.OrmmaView.ViewState.RESIZED;
                        LayoutParams lp = OrmmaView.this.getLayoutParams();
                        lp.height = data.getInt(RESIZE_HEIGHT, lp.height);
                        lp.width = data.getInt(RESIZE_WIDTH, lp.width);
                        OrmmaView.this.injectJavaScript(new StringBuilder("window.ormmaview.fireChangeEvent({ state: 'resized', size: { width: ").append(lp.width).append(", ").append("height: ").append(lp.height).append("}});").toString());
                        OrmmaView.this.requestLayout();
                        if (OrmmaView.this.mListener != null) {
                            OrmmaView.this.mListener.onResize();
                        }
                        break;
                    case MESSAGE_CLOSE:
                        Log.i("vserv", new StringBuilder("&&&&&&&&&&&Inside MESSAGE_CLOSE:: ").append(OrmmaView.this.mViewState).toString());
                        switch ($SWITCH_TABLE$mobi$vserv$org$ormma$view$OrmmaView$ViewState()[OrmmaView.this.mViewState.ordinal()]) {
                            case MMAdView.TRANSITION_UP:
                                OrmmaView.this.closeResized();
                                break;
                            case MMAdView.TRANSITION_DOWN:
                                OrmmaView.this.closeExpanded();
                                break;
                            default:
                                break;
                        }
                    case MESSAGE_HIDE:
                        OrmmaView.this.setVisibility(MMAdView.TRANSITION_RANDOM);
                        OrmmaView.this.injectJavaScript("window.ormmaview.fireChangeEvent({ state: 'hidden' });");
                        break;
                    case MESSAGE_SHOW:
                        OrmmaView.this.injectJavaScript("window.ormmaview.fireChangeEvent({ state: 'default' });");
                        OrmmaView.this.setVisibility(0);
                        break;
                    case MESSAGE_EXPAND:
                        OrmmaView.this.doExpand(data);
                        break;
                    case MESSAGE_SEND_EXPAND_CLOSE:
                        if (OrmmaView.this.mListener != null) {
                            OrmmaView.this.mListener.onExpandClose();
                        }
                        break;
                    case MESSAGE_OPEN:
                        OrmmaView.this.mViewState = mobi.vserv.org.ormma.view.OrmmaView.ViewState.LEFT_BEHIND;
                        break;
                    case MESSAGE_PLAY_VIDEO:
                        OrmmaView.this.playVideoImpl(data);
                        break;
                    case MESSAGE_PLAY_AUDIO:
                        OrmmaView.this.playAudioImpl(data);
                        break;
                    case MESSAGE_RAISE_ERROR:
                        String strMsg = data.getString(ERROR_MESSAGE);
                        OrmmaView.this.injectJavaScript(new StringBuilder("window.ormmaview.fireErrorEvent(\"").append(strMsg).append("\", \"").append(data.getString(ERROR_ACTION)).append("\")").toString());
                        break;
                    case MESSAGE_CACHE_VIDEO:
                        OrmmaView.this.cacheVideoImpl(data);
                        break;
                    case MESSAGE_CACHE_AUDIO:
                        OrmmaView.this.cacheAudioImpl(data);
                        break;
                }
                super.handleMessage(msg);
            }
        };
        this.mWebViewClient = new WebViewClient() {
            public void onLoadResource(WebView view, String url) {
            }

            public void onPageFinished(WebView view, String url) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", "onPageFinished!!!!!!!!!!");
                }
                OrmmaView.this.mDefaultHeight = (int) (((float) OrmmaView.this.getHeight()) / OrmmaView.this.mDensity);
                OrmmaView.this.mDefaultWidth = (int) (((float) OrmmaView.this.getWidth()) / OrmmaView.this.mDensity);
                OrmmaView.this.mUtilityController.init(OrmmaView.this.mDensity);
                if (OrmmaView.this.CurrentAd == 0) {
                    if (OrmmaView.this.onPageFinishedCalled) {
                        OrmmaView.this.onPageFinishedCalled = false;
                        if (Defines.ENABLE_lOGGING) {
                            Log.i("vserv", "onPageFinished ORMMAReady11 init!!!!!!!!!!");
                        }
                        OrmmaView.this.injectJavaScript("ORMMAReady11();");
                    }
                } else if (OrmmaView.this.onPageFinishedCalled) {
                    OrmmaView.this.onPageFinishedCalled = false;
                    if (!OrmmaView.this.onPageReceivedError) {
                        if (Defines.ENABLE_lOGGING) {
                            Log.i("vserv", new StringBuilder("if onPageFinished ORMMAReady12 init!!!!!!!!!!: ").append(OrmmaView.this.onPageReceivedError).toString());
                        }
                        if (OrmmaView.this.cacheVideo) {
                            OrmmaView.this.cacheVideo = false;
                            if (OrmmaView.this.videoPlayer != null) {
                                OrmmaView.this.vservAdController.storeCacheAdView();
                                OrmmaView.this.vservAdController.storeCacheAdType(MMRequest.MARITAL_OTHER);
                                OrmmaView.this.vservAdController.storeCacheImpressionHeader();
                                OrmmaView.this.vservAdController.removeCacheAdType();
                                if (OrmmaView.this.isVideo_AudioFullScreen) {
                                    OrmmaView.this.isVideo_AudioFullScreen = false;
                                    OrmmaView.this.vservAdController.storeCacheAdType(OrmmaView.this.cacheData);
                                } else {
                                    OrmmaView.this.isVideo_AudioFullScreen = false;
                                    OrmmaView.this.vservAdController.storeCacheAdType("video");
                                }
                                OrmmaView.this.vservAdController.storeCachePlayer(OrmmaView.this.videoPlayer);
                                OrmmaView.this.cacheData = null;
                                if (Defines.ENABLE_lOGGING) {
                                    Log.i("vserv", "Video is stored");
                                }
                            }
                        } else if (OrmmaView.this.cacheAudio) {
                            OrmmaView.this.cacheAudio = false;
                            if (OrmmaView.this.audioPlayer != null) {
                                OrmmaView.this.vservAdController.storeCacheAdView();
                                OrmmaView.this.vservAdController.storeCacheAdType(MMRequest.MARITAL_OTHER);
                                OrmmaView.this.vservAdController.storeCacheImpressionHeader();
                                OrmmaView.this.vservAdController.storeCachePlayer(OrmmaView.this.audioPlayer);
                                OrmmaView.this.vservAdController.removeCacheAdType();
                                if (OrmmaView.this.isVideo_AudioFullScreen) {
                                    OrmmaView.this.isVideo_AudioFullScreen = false;
                                    OrmmaView.this.vservAdController.storeCacheAdType(OrmmaView.this.cacheData);
                                } else {
                                    OrmmaView.this.isVideo_AudioFullScreen = false;
                                    OrmmaView.this.vservAdController.storeCacheAdType("audio");
                                }
                                OrmmaView.this.cacheData = null;
                                if (Defines.ENABLE_lOGGING) {
                                    Log.i("vserv", "Audio is stored");
                                }
                            }
                        } else {
                            OrmmaView.this.vservAdController.storeCacheAdView();
                            OrmmaView.this.vservAdController.storeCacheAdType(MMRequest.MARITAL_OTHER);
                            OrmmaView.this.vservAdController.storeCacheImpressionHeader();
                        }
                        if (OrmmaView.this.mUtilityController.cachePostActionUrl != null) {
                            OrmmaView.this.vservAdController.storePostActionUrl(OrmmaView.this.mUtilityController.cachePostActionUrl);
                        }
                        OrmmaView.this.vservAdController.storeCacheDownloadNetworkUrl();
                        OrmmaView.this.vservAdController.storeCacheADOrientation();
                        OrmmaView.this.vservAdController.storeCacheADSkipDelay();
                        OrmmaView.this.vservAdController.storeCacheADRequestId();
                        OrmmaView.this.injectJavaScript("ORMMAReady12();");
                    } else if (Defines.ENABLE_lOGGING) {
                        Log.i("vserv", new StringBuilder("else onPageFinished ORMMAReady12 init!!!!!!!!!!: ").append(OrmmaView.this.onPageReceivedError).toString());
                    }
                }
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", new StringBuilder("onPageStarted!!!!!!!!!!").append(OrmmaView.this.CurrentAd).toString());
                }
            }

            public void onProgressChanged(WebView view, int progress) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", new StringBuilder("onProgressChanged!!!!!!!!!!").append(progress).toString());
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (Defines.ENABLE_lOGGING) {
                    Log.d("vserv", new StringBuilder("onReceivedError:").append(description).toString());
                    Log.d("vserv", new StringBuilder("onReceivedError2:").append(OrmmaView.this.CurrentAd).toString());
                }
                if (!OrmmaView.this.onPageReceivedError) {
                    OrmmaView.this.onPageReceivedError = true;
                    if (OrmmaView.this.vservAdController != null) {
                        if (Defines.ENABLE_lOGGING) {
                            Log.i("vserv", "onProgressChanged showskipPage!!!!!!!!!!");
                        }
                        OrmmaView.this.vservAdController.showSkipPage_onpageFinished();
                    }
                }
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if (Defines.ENABLE_lOGGING) {
                    Log.d("vserv", "onReceivedSslError:");
                }
                if (OrmmaView.this.vservAdController != null) {
                    OrmmaView.this.vservAdController.onReceiveError();
                }
                super.onReceivedSslError(view, handler, error);
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                boolean z = true;
                Uri uri = Uri.parse(url);
                if (OrmmaView.this.mListener != null && OrmmaView.this.isRegisteredProtocol(uri)) {
                    OrmmaView.this.mListener.handleRequest(url);
                    return z;
                } else if (url.startsWith("tel:")) {
                    intent2 = new Intent("android.intent.action.DIAL", Uri.parse(url));
                    intent2.addFlags(DriveFile.MODE_READ_ONLY);
                    OrmmaView.this.getContext().startActivity(intent2);
                    return z;
                } else if (url.startsWith("mailto:")) {
                    intent2 = new Intent("android.intent.action.VIEW", Uri.parse(url));
                    intent2.addFlags(DriveFile.MODE_READ_ONLY);
                    OrmmaView.this.getContext().startActivity(intent2);
                    return z;
                } else {
                    intent2 = new Intent();
                    intent2.setAction("android.intent.action.VIEW");
                    intent2.setData(uri);
                    intent2.addFlags(DriveFile.MODE_READ_ONLY);
                    OrmmaView.this.getContext().startActivity(intent2);
                    return z;
                }
            }
        };
        this.mWebChromeClient = new WebChromeClient() {
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return false;
            }

            public void onProgressChanged(WebView view, int progress) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", new StringBuilder("onProgressChanged!!!!!!!!!!").append(progress).toString());
                }
                if (OrmmaView.this.onPageProgressFinished) {
                    OrmmaView.this.onPageProgressFinished = false;
                    if (OrmmaView.this.CurrentAd == 0 && OrmmaView.this.vservAdController != null) {
                        if (Defines.ENABLE_lOGGING) {
                            Log.i("vserv", "onProgressChanged showskipPage!!!!!!!!!!");
                        }
                        OrmmaView.this.vservAdController.showSkipPage_onpageFinished();
                    }
                }
            }
        };
        this.shareUrl = Preconditions.EMPTY_ARGUMENTS;
        this.successMessage = Preconditions.EMPTY_ARGUMENTS;
        this.context = context;
        setListener(listener);
        initialize();
    }

    private FrameLayout changeContentArea(Dimensions d) {
        Log.i("vserv", "changeContentArea:");
        FrameLayout contentView = (FrameLayout) getRootView().findViewById(16908290);
        ViewGroup parent = (ViewGroup) getParent();
        FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(d.width, d.height);
        fl.topMargin = d.x;
        fl.leftMargin = d.y;
        if (Defines.ENABLE_lOGGING) {
            Log.i("expand", new StringBuilder("X: ").append(d.x).append("\nY: ").append(d.y).toString());
            Log.i("expand", new StringBuilder("Width: ").append(d.width).append("\nHeight: ").append(d.height).toString());
        }
        int count = parent.getChildCount();
        int index = 0;
        while (index < count && parent.getChildAt(index) != this) {
            index++;
        }
        this.mIndex = index;
        FrameLayout placeHolder = new FrameLayout(getContext());
        placeHolder.setId(PLACEHOLDER_ID);
        parent.addView(placeHolder, index, new LayoutParams(getWidth(), getHeight()));
        parent.removeView(this);
        FrameLayout backGround = new FrameLayout(getContext());
        backGround.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });
        FrameLayout.LayoutParams bgfl = new FrameLayout.LayoutParams(-1, -1);
        backGround.setId(BACKGROUND_ID);
        backGround.setPadding(d.x, d.y, 0, 0);
        backGround.addView(this, fl);
        contentView.addView(backGround, bgfl);
        return backGround;
    }

    private void closeResized() {
        Log.i("vserv", "&&&&&&&&&&&Inside closeResized:: ");
        if (this.mListener != null) {
            this.mListener.onResizeClose();
        }
        injectJavaScript(new StringBuilder("window.ormmaview.fireChangeEvent({ state: 'default', size: { width: ").append(this.mDefaultWidth).append(", ").append("height: ").append(this.mDefaultHeight).append("}").append("});").toString());
        resetLayout();
    }

    private void doExpand(Bundle data) {
        Log.i("vserv", "doExpand:");
        Dimensions d = (Dimensions) data.getParcelable(DIMENSIONS);
        String url = data.getString(EXPAND_URL);
        Properties p = (Properties) data.getParcelable(EXPAND_PROPERTIES);
        if (URLUtil.isValidUrl(url)) {
            loadUrl(url);
        }
        FrameLayout backGround = changeContentArea(d);
        if (p.useBackground) {
            backGround.setBackgroundColor(p.backgroundColor | (((int) (p.backgroundOpacity * 255.0f)) * 268435456));
        }
        injectJavaScript(new StringBuilder("window.ormmaview.fireChangeEvent({ state: 'expanded', size: { width: ").append((int) (((float) d.width) / this.mDensity)).append(", ").append("height: ").append((int) (((float) d.height) / this.mDensity)).append("}").append(" });").toString());
        if (this.mListener != null) {
            this.mListener.onExpand();
        }
        this.mViewState = ViewState.EXPANDED;
    }

    private int getContentViewHeight() {
        View contentView = getRootView().findViewById(16908290);
        return contentView != null ? contentView.getHeight() : -1;
    }

    private void initialize() {
        setScrollContainer(false);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        this.mGestureDetector = new GestureDetector(new ScrollEater());
        setBackgroundColor(0);
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) getContext().getSystemService("window")).getDefaultDisplay().getMetrics(metrics);
        this.mDensity = metrics.density;
        this.bPageFinished = false;
        getSettings().setJavaScriptEnabled(true);
        if (this.vservAdManageractivity != null) {
            this.mUtilityController = new OrmmaUtilityController(this, getContext(), this.vservAdManageractivity, this.CurrentAd);
        } else {
            this.mUtilityController = new OrmmaUtilityController(this, getContext(), this.CurrentAd);
        }
        addJavascriptInterface(this.mUtilityController, "ORMMAUtilityControllerBridge");
        setWebViewClient(this.mWebViewClient);
        setWebChromeClient(this.mWebChromeClient);
        setScriptPath();
        this.mContentViewHeight = getContentViewHeight();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (OrmmaView.this.vservAdController != null && OrmmaView.this.vservAdController.currentSkipDelay > 0) {
                    if (OrmmaView.this.vservAdController.countDownTimer != null) {
                        OrmmaView.this.vservAdController.countDownTimer.onFinish();
                        OrmmaView.this.vservAdController.countDownTimer = null;
                    }
                    Intent intent = new Intent("mobi.vserv.ad.dismiss_screen");
                    intent.putExtra("clickToVideo", "true");
                    OrmmaView.this.context.sendBroadcast(intent);
                    OrmmaView.this.isOnClicked = true;
                }
                switch (event.getAction()) {
                    case MMAdView.TRANSITION_NONE:
                    case MMAdView.TRANSITION_FADE:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });
    }

    private boolean isRegisteredProtocol(Uri uri) {
        String scheme = uri.getScheme();
        if (scheme == null) {
            return false;
        }
        Iterator it = this.registeredProtocols.iterator();
        while (it.hasNext()) {
            if (((String) it.next()).equalsIgnoreCase(scheme)) {
                return true;
            }
        }
        return false;
    }

    private void loadInputStream(InputStream is, String dataToInject) {
        try {
            mBridgeScriptPath = this.context.getFileStreamPath("ormma_bridge_A-AN-2.0.1.js").getAbsolutePath();
            mScriptPath = this.context.getFileStreamPath("ormma_A-AN-2.0.1.js").getAbsolutePath();
            this.mLocalFilePath = this.mUtilityController.writeToDiskWrap(is, CURRENT_FILE, true, dataToInject, mBridgeScriptPath, mScriptPath);
            String url = new StringBuilder("file://").append(this.mLocalFilePath).append(File.separator).append(CURRENT_FILE).toString();
            if (dataToInject != null) {
                injectJavaScript(dataToInject);
            }
            super.loadUrl(url);
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e2) {
                }
            }
        }
    }

    private String readFile(File file) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String ls = System.getProperty("line.separator");
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    return stringBuilder.toString();
                }
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void reset() {
        if (this.mViewState == ViewState.EXPANDED) {
            closeExpanded();
        } else if (this.mViewState == ViewState.RESIZED) {
            closeResized();
        }
        invalidate();
        this.mUtilityController.deleteOldAds();
        this.mUtilityController.stopAllListeners();
        resetLayout();
    }

    private void resetLayout() {
        LayoutParams lp = getLayoutParams();
        if (this.bGotLayoutParams) {
            lp.height = this.mInitLayoutHeight;
            lp.width = this.mInitLayoutWidth;
        }
        setVisibility(0);
        requestLayout();
    }

    private synchronized void setScriptPath() {
    }

    public void addJavascriptObject(Object obj, String name) {
        addJavascriptInterface(obj, name);
    }

    public void cacheAudioImpl(Bundle data) {
        try {
            PlayerProperties properties = (PlayerProperties) data.getParcelable(PLAYER_PROPERTIES);
            String url = data.getString(EXPAND_URL);
            this.audioPlayer = new OrmmaPlayer(getContext(), 0);
            this.audioPlayer.setPlayData(properties, url);
            this.audioPlayer.setLayoutParams(new LayoutParams(1, 1));
            this.audioPlayer.setListener(new OrmmaPlayerListener() {
                public void onBufferingStarted() {
                }

                public void onComplete() {
                    if (Defines.ENABLE_lOGGING) {
                        Log.i("vserv", "Audio is complete playing OrmmaView");
                    }
                }

                public void onError() {
                    onComplete();
                }

                public void onPrepared() {
                    OrmmaView.this.audioPlayer.pause();
                }
            });
            this.audioPlayer.cacheAudio();
            this.cacheAudio = true;
            if (properties.isFullScreen()) {
                this.isVideo_AudioFullScreen = true;
                this.cacheData = data;
            } else {
                this.isVideo_AudioFullScreen = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cacheVideoImpl(Bundle data) {
        try {
            PlayerProperties properties = (PlayerProperties) data.getParcelable(PLAYER_PROPERTIES);
            Dimensions d = (Dimensions) data.getParcelable(DIMENSIONS);
            String url = data.getString(EXPAND_URL);
            this.videoPlayer = new OrmmaPlayer(this, getContext(), d.height);
            this.videoPlayer.setPlayData(properties, url);
            this.videoPlayer.setDimensions(d);
            this.videoPlayer.setLayoutParams(new FrameLayout.LayoutParams(d.width, d.height));
            this.videoPlayer.setListener(new OrmmaPlayerListener() {
                public void onBufferingStarted() {
                }

                public void onComplete() {
                    if (Defines.ENABLE_lOGGING) {
                        Log.i("vserv", "Video is complete playing OrmmaView");
                    }
                }

                public void onError() {
                    onComplete();
                }

                public void onPrepared() {
                    OrmmaView.this.videoPlayer.pause();
                }
            });
            this.videoPlayer.cacheVideo();
            this.cacheVideo = true;
            if (properties.isFullScreen()) {
                this.isVideo_AudioFullScreen = true;
                this.cacheData = data;
            } else {
                this.isVideo_AudioFullScreen = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callOrmmaOpen(String invokeUrl, String[] exitUrl, String shareUrl, String successMessage) {
        this.exitUrl = exitUrl;
        this.shareUrl = shareUrl;
        this.successMessage = successMessage;
        open(invokeUrl, true, true, true);
    }

    public void clearView() {
        reset();
        super.clearView();
    }

    public void close() {
        this.mHandler.sendEmptyMessage(MESSAGE_CLOSE);
    }

    protected synchronized void closeExpanded() {
        Log.i("vserv", "&&&&&&&&&&&Inside closeExpanded:: ");
        resetContents();
        injectJavaScript(new StringBuilder("window.ormmaview.fireChangeEvent({ state: 'default', size: { width: ").append(this.mDefaultWidth).append(", ").append("height: ").append(this.mDefaultHeight).append("}").append("});").toString());
        this.mViewState = ViewState.DEFAULT;
        this.mHandler.sendEmptyMessage(MESSAGE_SEND_EXPAND_CLOSE);
        setVisibility(0);
    }

    protected void closeOpened(View openedFrame) {
        ((ViewGroup) ((Activity) getContext()).getWindow().getDecorView()).removeView(openedFrame);
        requestLayout();
    }

    public void deregisterProtocol(String protocol) {
        if (protocol != null) {
            this.registeredProtocols.remove(protocol.toLowerCase());
        }
    }

    public void dump() {
    }

    public void expand(Dimensions dimensions, String URL, Properties properties) {
        Message msg = this.mHandler.obtainMessage(MESSAGE_EXPAND);
        Bundle data = new Bundle();
        data.putParcelable(DIMENSIONS, dimensions);
        data.putString(EXPAND_URL, URL);
        data.putParcelable(EXPAND_PROPERTIES, properties);
        msg.setData(data);
        this.mHandler.sendMessage(msg);
    }

    public ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) getContext().getSystemService("connectivity");
    }

    public String getSize() {
        return new StringBuilder("{ width: ").append((int) (((float) getWidth()) / this.mDensity)).append(", ").append("height: ").append((int) (((float) getHeight()) / this.mDensity)).append("}").toString();
    }

    public String getState() {
        return this.mViewState.toString().toLowerCase();
    }

    public void hide() {
        this.mHandler.sendEmptyMessage(MESSAGE_HIDE);
    }

    @SuppressLint({"NewApi"})
    public void injectJavaScript(String str) {
        try {
            if (VERSION.SDK_INT >= 19) {
                evaluateJavascript(new StringBuilder("javascript:").append(str).toString(), new ValueCallback<String>() {
                    public void onReceiveValue(String value) {
                    }
                });
            } else if (str != null) {
                HitTestResult testResult = getHitTestResult();
                if (testResult == null || testResult.getType() != 9) {
                    super.loadUrl(new StringBuilder("javascript:").append(str).toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isExpanded() {
        return this.mViewState == ViewState.EXPANDED;
    }

    public boolean isPageFinished() {
        return this.bPageFinished;
    }

    public void loadFile(File f, String dataToInject) {
        try {
            loadInputStream(new FileInputStream(f), dataToInject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadUrl(String url) {
        loadUrl(url, false, null);
    }

    public void loadUrl(String url, String dataToInject) {
        loadUrl(url, false, dataToInject);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void loadUrl(java.lang.String r8_url, boolean r9_dontLoad, java.lang.String r10_dataToInject) {
        throw new UnsupportedOperationException("Method not decompiled: mobi.vserv.org.ormma.view.OrmmaView.loadUrl(java.lang.String, boolean, java.lang.String):void");
        /*
        r7 = this;
        r5 = android.webkit.URLUtil.isValidUrl(r8);
        if (r5 == 0) goto L_0x0034;
    L_0x0006:
        if (r9 != 0) goto L_0x0040;
    L_0x0008:
        r2 = 0;
        r5 = 0;
        r7.bPageFinished = r5;
        r4 = new java.net.URL;	 Catch:{ MalformedURLException -> 0x003f, IOException -> 0x003a }
        r4.<init>(r8);	 Catch:{ MalformedURLException -> 0x003f, IOException -> 0x003a }
        r3 = r4.getFile();	 Catch:{ MalformedURLException -> 0x003f, IOException -> 0x003a }
        r5 = "file:///android_asset/";
        r5 = r8.startsWith(r5);	 Catch:{ MalformedURLException -> 0x003f, IOException -> 0x003a }
        if (r5 == 0) goto L_0x0035;
    L_0x001d:
        r5 = "file:///android_asset/";
        r6 = "";
        r3 = r8.replace(r5, r6);	 Catch:{ MalformedURLException -> 0x003f, IOException -> 0x003a }
        r5 = r7.getContext();	 Catch:{ MalformedURLException -> 0x003f, IOException -> 0x003a }
        r0 = r5.getAssets();	 Catch:{ MalformedURLException -> 0x003f, IOException -> 0x003a }
        r2 = r0.open(r3);	 Catch:{ MalformedURLException -> 0x003f, IOException -> 0x003a }
    L_0x0031:
        r7.loadInputStream(r2, r10);	 Catch:{ MalformedURLException -> 0x003f, IOException -> 0x003a }
    L_0x0034:
        return;
    L_0x0035:
        r2 = r4.openStream();	 Catch:{ MalformedURLException -> 0x003f, IOException -> 0x003a }
        goto L_0x0031;
    L_0x003a:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0034;
    L_0x003f:
        r5 = move-exception;
    L_0x0040:
        super.loadUrl(r8);
        goto L_0x0034;
        */
    }

    protected void onAttachedToWindow() {
        if (!this.bGotLayoutParams) {
            LayoutParams lp = getLayoutParams();
            this.mInitLayoutHeight = lp.height;
            this.mInitLayoutWidth = lp.width;
            this.bGotLayoutParams = true;
        }
        super.onAttachedToWindow();
    }

    protected void onConfigurationChanged(Configuration newConfig) {
    }

    protected void onDetachedFromWindow() {
        this.mUtilityController.stopAllListeners();
        super.onDetachedFromWindow();
    }

    public void onGlobalLayout() {
        boolean z = this.bKeyboardOut;
        if (!this.bKeyboardOut && this.mContentViewHeight >= 0 && getContentViewHeight() >= 0 && this.mContentViewHeight != getContentViewHeight()) {
            z = true;
            injectJavaScript("window.ormmaview.fireChangeEvent({ keyboardState: true});");
        }
        if (this.bKeyboardOut && this.mContentViewHeight >= 0 && getContentViewHeight() >= 0 && this.mContentViewHeight == getContentViewHeight()) {
            z = false;
            injectJavaScript("window.ormmaview.fireChangeEvent({ keyboardState: false});");
        }
        if (this.mContentViewHeight < 0) {
            this.mContentViewHeight = getContentViewHeight();
        }
        this.bKeyboardOut = z;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mGestureDetector.onTouchEvent(ev)) {
            ev.setAction(MMAdView.TRANSITION_DOWN);
        }
        return super.onTouchEvent(ev);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void open(java.lang.String r6_url, boolean r7_back, boolean r8_forward, boolean r9_refresh) {
        throw new UnsupportedOperationException("Method not decompiled: mobi.vserv.org.ormma.view.OrmmaView.open(java.lang.String, boolean, boolean, boolean):void");
        /*
        r5 = this;
        r4 = 0;
        r0 = new android.content.Intent;
        r1 = r5.getContext();
        r2 = mobi.vserv.org.ormma.view.Browser.class;
        r0.<init>(r1, r2);
        r1 = "extra_url";
        r0.putExtra(r1, r6);
        r1 = "open_show_back";
        r0.putExtra(r1, r7);
        r1 = "open_show_forward";
        r0.putExtra(r1, r8);
        r1 = "open_show_refresh";
        r0.putExtra(r1, r9);
        r1 = "snsPageLoaded";
        r2 = mobi.vserv.android.ads.VservAdController.snsPageLoaded;
        r0.putExtra(r1, r2);
        r1 = "exitUrl";
        r2 = r5.exitUrl;
        r0.putExtra(r1, r2);
        r1 = "shareUrl";
        r2 = r5.shareUrl;
        r0.putExtra(r1, r2);
        r1 = "successMsg";
        r2 = r5.successMessage;
        r0.putExtra(r1, r2);
        r1 = "isShared";
        r0.putExtra(r1, r4);
        r1 = "adOrientation";
        r2 = r5.vservAdController;
        r2 = r2.getOrientation();
        r0.putExtra(r1, r2);
        r1 = r5.vservAdManageractivity;
        if (r1 == 0) goto L_0x00b5;
    L_0x0050:
        r1 = mobi.vserv.android.ads.VservAdController.showAt;
        r2 = "start";
        r1 = r1.equals(r2);
        if (r1 != 0) goto L_0x006e;
    L_0x005a:
        r1 = mobi.vserv.android.ads.VservAdController.showAt;
        r2 = "mid";
        r1 = r1.equals(r2);
        if (r1 != 0) goto L_0x006e;
    L_0x0064:
        r1 = mobi.vserv.android.ads.VservAdController.showAt;
        r2 = "inapp";
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x0092;
    L_0x006e:
        r1 = "skipLabel";
        r2 = r5.vservAdManageractivity;
        r2 = r2.vservAdController;
        r2 = r2.vservConfigBundle;
        r3 = "skipLabel";
        r2 = r2.getString(r3);
        r0.putExtra(r1, r2);
        r1 = "iconIdentifier";
        r2 = "vserv_ic_menu_skip";
        r0.putExtra(r1, r2);
    L_0x0086:
        r1 = r5.vservAdManageractivity;
        if (r1 != 0) goto L_0x00eb;
    L_0x008a:
        r1 = r5.getContext();
        r1.startActivity(r0);
    L_0x0091:
        return;
    L_0x0092:
        r1 = mobi.vserv.android.ads.VservAdController.showAt;
        r2 = "end";
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x0086;
    L_0x009c:
        r1 = "skipLabel";
        r2 = r5.vservAdManageractivity;
        r2 = r2.vservAdController;
        r2 = r2.vservConfigBundle;
        r3 = "exitLabel";
        r2 = r2.getString(r3);
        r0.putExtra(r1, r2);
        r1 = "iconIdentifier";
        r2 = "vserv_ic_menu_exit";
        r0.putExtra(r1, r2);
        goto L_0x0086;
    L_0x00b5:
        r1 = mobi.vserv.android.ads.VservAdController.showAt;
        r2 = "inapp";
        r1 = r1.equals(r2);
        if (r1 != 0) goto L_0x00c9;
    L_0x00bf:
        r1 = mobi.vserv.android.ads.VservAdController.showAt;
        r2 = "mid";
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x0086;
    L_0x00c9:
        r1 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;
        if (r1 == 0) goto L_0x00d4;
    L_0x00cd:
        r1 = "context";
        r2 = "fetching inapp bundle";
        android.util.Log.i(r1, r2);
    L_0x00d4:
        r1 = "skipLabel";
        r2 = r5.vservAdController;
        r2 = r2.vservConfigBundle;
        r3 = "skipLabel";
        r2 = r2.getString(r3);
        r0.putExtra(r1, r2);
        r1 = "iconIdentifier";
        r2 = "vserv_ic_menu_skip";
        r0.putExtra(r1, r2);
        goto L_0x0086;
    L_0x00eb:
        r1 = "start_from_ad_activity";
        r2 = 1;
        r0.putExtra(r1, r2);
        r1 = r5.vservAdManageractivity;
        r1.startActivityForResult(r0, r4);
        goto L_0x0091;
        */
    }

    public void playAudio(String url, boolean autoPlay, boolean controls, boolean loop, boolean position, String startStyle, String stopStyle) {
        if (this.CurrentAd == 1) {
            this.isCachereq = true;
        } else {
            this.isCachereq = false;
        }
        PlayerProperties properties = new PlayerProperties();
        properties.setProperties(false, autoPlay, controls, position, loop, startStyle, stopStyle);
        Bundle data = new Bundle();
        data.putString(ERROR_ACTION, ACTION.PLAY_AUDIO.toString());
        data.putString(EXPAND_URL, url);
        data.putParcelable(PLAYER_PROPERTIES, properties);
        Message msg;
        if (this.isCachereq) {
            msg = this.mHandler.obtainMessage(MESSAGE_CACHE_AUDIO);
            msg.setData(data);
            this.mHandler.sendMessage(msg);
        } else if (properties.isFullScreen()) {
            try {
                Intent intent = new Intent(getContext(), OrmmaActionHandler.class);
                intent.putExtra("adOrientation", this.vservAdController.getOrientation());
                intent.putExtra("isCacheAd", false);
                intent.putExtra("posturl", Preconditions.EMPTY_ARGUMENTS);
                intent.putExtra("skipDelay", this.vservAdController.currentSkipDelay);
                this.vservAdController.currentSkipDelay = 0;
                intent.putExtras(data);
                getContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            msg = this.mHandler.obtainMessage(MESSAGE_PLAY_AUDIO);
            msg.setData(data);
            this.mHandler.sendMessage(msg);
        }
    }

    public void playAudioFullScreenImpl(Bundle data) {
        try {
            this.vservAdController.removeCacheAdType();
            Intent intent = new Intent(getContext(), OrmmaActionHandler.class);
            intent.putExtra("isCacheAd", true);
            intent.putExtra("adOrientation", this.vservAdController.getOrientation());
            intent.putExtra("posturl", this.vservAdController.getPostActionUrl());
            intent.putExtra("skipDelay", this.vservAdController.currentSkipDelay);
            this.vservAdController.currentSkipDelay = 0;
            this.vservAdController.removePostActionUrl();
            intent.putExtras(data);
            getContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void playAudioImpl(Bundle data) {
        OrmmaPlayer audioPlayer;
        if (data != null) {
            PlayerProperties properties = (PlayerProperties) data.getParcelable(PLAYER_PROPERTIES);
            String url = data.getString(EXPAND_URL);
            audioPlayer = new OrmmaPlayer(getContext(), 0);
            audioPlayer.setPlayData(properties, url);
            audioPlayer.setLayoutParams(new LayoutParams(1, 1));
            ((ViewGroup) getParent()).addView(audioPlayer);
            audioPlayer.playAudio();
        } else {
            audioPlayer = this.vservAdController.getCachePlayer();
            audioPlayer.setContext(this.context);
            ((ViewGroup) getParent()).addView(audioPlayer);
            audioPlayer.playCacheAudio();
        }
    }

    public void playVideo(String url, boolean audioMuted, boolean autoPlay, boolean controls, boolean loop, Dimensions d, String startStyle, String stopStyle) {
        try {
            if (this.CurrentAd != 1) {
                this.isCachereq = false;
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", new StringBuilder("Playing Video now:: ").append(autoPlay).toString());
                }
            } else if (this.displayCacheAd) {
                this.isCachereq = false;
            } else {
                this.isCachereq = true;
            }
            PlayerProperties properties = new PlayerProperties();
            properties.setProperties(audioMuted, autoPlay, controls, false, loop, startStyle, stopStyle);
            Bundle data = new Bundle();
            data.putString(EXPAND_URL, url);
            data.putString(ERROR_ACTION, ACTION.PLAY_VIDEO.toString());
            data.putParcelable(PLAYER_PROPERTIES, properties);
            if (d != null) {
                data.putParcelable(DIMENSIONS, d);
            }
            Message msg;
            if (this.isCachereq) {
                msg = this.mHandler.obtainMessage(MESSAGE_CACHE_VIDEO);
                msg.setData(data);
                this.mHandler.sendMessage(msg);
            } else {
                msg = this.mHandler.obtainMessage(MESSAGE_PLAY_VIDEO);
                if (properties.isFullScreen()) {
                    try {
                        Intent intent = new Intent(getContext(), OrmmaActionHandler.class);
                        if (this.vservAdManageractivity == null) {
                            intent.putExtra("skipLabel", "Skip");
                            intent.putExtra("iconIdentifier", "vserv_ic_menu_skip");
                        } else if (VservAdController.showAt.equals(VservConstants.VPLAY0) || VservAdController.showAt.equals("mid")) {
                            intent.putExtra("skipLabel", this.vservAdManageractivity.vservAdController.vservConfigBundle.getString("skipLabel"));
                            intent.putExtra("iconIdentifier", "vserv_ic_menu_skip");
                        } else if (VservAdController.showAt.equals("end")) {
                            intent.putExtra("skipLabel", this.vservAdManageractivity.vservAdController.vservConfigBundle.getString("exitLabel"));
                            intent.putExtra("iconIdentifier", "vserv_ic_menu_exit");
                        } else if (VservAdController.showAt.equals("end")) {
                            intent.putExtra("skipLabel", this.vservAdManageractivity.vservAdController.vservConfigBundle.getString("exitLabel"));
                            intent.putExtra("iconIdentifier", "vserv_ic_menu_exit");
                        }
                        intent.putExtra("adOrientation", this.vservAdController.getOrientation());
                        intent.putExtra("isCacheAd", false);
                        intent.putExtra("posturl", Preconditions.EMPTY_ARGUMENTS);
                        if (this.isOnClicked) {
                            intent.putExtra("skipDelay", 0);
                        } else {
                            intent.putExtra("skipDelay", this.vservAdController.currentSkipDelay);
                        }
                        if (this.vservAdController.requestId != null) {
                            intent.putExtra("requestid", this.vservAdController.requestId);
                        }
                        intent.putExtras(data);
                        if (this.vservAdManageractivity == null) {
                            getContext().startActivity(intent);
                            if (this.vservAdController.currentSkipDelay == -1) {
                                this.vservAdController.showClose();
                            }
                        } else {
                            intent.putExtra("start_from_ad_activity", true);
                            this.vservAdManageractivity.startActivityForResult(intent, 0);
                        }
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (d != null) {
                    msg.setData(data);
                    this.mHandler.sendMessage(msg);
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void playVideoFullScreen(Bundle data) {
        try {
            this.vservAdController.removeCacheAdType();
            Intent intent = new Intent(getContext(), OrmmaActionHandler.class);
            if (this.vservAdManageractivity == null) {
                intent.putExtra("skipLabel", "Skip");
                intent.putExtra("iconIdentifier", "vserv_ic_menu_skip");
            } else if (VservAdController.showAt.equals(VservConstants.VPLAY0) || VservAdController.showAt.equals("mid")) {
                intent.putExtra("skipLabel", this.vservAdManageractivity.vservAdController.vservConfigBundle.getString("skipLabel"));
                intent.putExtra("iconIdentifier", "vserv_ic_menu_skip");
            } else if (VservAdController.showAt.equals("end")) {
                intent.putExtra("skipLabel", this.vservAdManageractivity.vservAdController.vservConfigBundle.getString("exitLabel"));
                intent.putExtra("iconIdentifier", "vserv_ic_menu_exit");
            } else if (VservAdController.showAt.equals("end")) {
                intent.putExtra("skipLabel", this.vservAdManageractivity.vservAdController.vservConfigBundle.getString("exitLabel"));
                intent.putExtra("iconIdentifier", "vserv_ic_menu_exit");
            }
            intent.putExtra("adOrientation", this.vservAdController.getOrientation());
            intent.putExtra("isCacheAd", true);
            intent.putExtra("posturl", this.vservAdController.getPostActionUrl());
            if (this.isOnClicked) {
                intent.putExtra("skipDelay", 0);
            } else {
                intent.putExtra("skipDelay", this.vservAdController.currentSkipDelay);
            }
            if (this.vservAdController.requestId != null) {
                intent.putExtra("requestid", this.vservAdController.requestId);
            }
            this.vservAdController.removePostActionUrl();
            intent.putExtras(data);
            if (this.vservAdManageractivity == null) {
                new Handler().postDelayed(new AnonymousClass_7(intent), 100);
            } else {
                intent.putExtra("start_from_ad_activity", true);
                new Handler().postDelayed(new AnonymousClass_8(intent), 100);
            }
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @android.annotation.SuppressLint({"InlinedApi"})
    public void playVideoImpl(android.os.Bundle r14_data) {
        throw new UnsupportedOperationException("Method not decompiled: mobi.vserv.org.ormma.view.OrmmaView.playVideoImpl(android.os.Bundle):void");
        /*
        r13 = this;
        r3 = 0;
        if (r14 == 0) goto L_0x0179;
    L_0x0003:
        r9 = "player_properties";
        r7 = r14.getParcelable(r9);	 Catch:{ Exception -> 0x016e }
        r7 = (mobi.vserv.org.ormma.controller.OrmmaController.PlayerProperties) r7;	 Catch:{ Exception -> 0x016e }
        r9 = "expand_dimensions";
        r9 = r14.getParcelable(r9);	 Catch:{ Exception -> 0x016e }
        r0 = r9;
        r0 = (mobi.vserv.org.ormma.controller.OrmmaController.Dimensions) r0;	 Catch:{ Exception -> 0x016e }
        r3 = r0;
        r9 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x016e }
        if (r9 == 0) goto L_0x0071;
    L_0x0019:
        r9 = "vserv";
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x016e }
        r11 = "d.x::";
        r10.<init>(r11);	 Catch:{ Exception -> 0x016e }
        r11 = r3.x;	 Catch:{ Exception -> 0x016e }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x016e }
        r10 = r10.toString();	 Catch:{ Exception -> 0x016e }
        android.util.Log.i(r9, r10);	 Catch:{ Exception -> 0x016e }
        r9 = "vserv";
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x016e }
        r11 = "d.y::";
        r10.<init>(r11);	 Catch:{ Exception -> 0x016e }
        r11 = r3.y;	 Catch:{ Exception -> 0x016e }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x016e }
        r10 = r10.toString();	 Catch:{ Exception -> 0x016e }
        android.util.Log.i(r9, r10);	 Catch:{ Exception -> 0x016e }
        r9 = "vserv";
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x016e }
        r11 = "&d.width::";
        r10.<init>(r11);	 Catch:{ Exception -> 0x016e }
        r11 = r3.width;	 Catch:{ Exception -> 0x016e }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x016e }
        r10 = r10.toString();	 Catch:{ Exception -> 0x016e }
        android.util.Log.i(r9, r10);	 Catch:{ Exception -> 0x016e }
        r9 = "vserv";
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x016e }
        r11 = "d.height::";
        r10.<init>(r11);	 Catch:{ Exception -> 0x016e }
        r11 = r3.height;	 Catch:{ Exception -> 0x016e }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x016e }
        r10 = r10.toString();	 Catch:{ Exception -> 0x016e }
        android.util.Log.i(r9, r10);	 Catch:{ Exception -> 0x016e }
    L_0x0071:
        r9 = "expand_url";
        r8 = r14.getString(r9);	 Catch:{ Exception -> 0x016e }
        r9 = new mobi.vserv.org.ormma.controller.util.OrmmaPlayer;	 Catch:{ Exception -> 0x016e }
        r10 = r13.getContext();	 Catch:{ Exception -> 0x016e }
        r11 = r3.height;	 Catch:{ Exception -> 0x016e }
        r9.<init>(r13, r10, r11);	 Catch:{ Exception -> 0x016e }
        r13.videoPlayer = r9;	 Catch:{ Exception -> 0x016e }
        r9 = r13.videoPlayer;	 Catch:{ Exception -> 0x016e }
        r9.setPlayData(r7, r8);	 Catch:{ Exception -> 0x016e }
        r9 = r13.vservAdController;	 Catch:{ Exception -> 0x016e }
        r9 = r9.requestId;	 Catch:{ Exception -> 0x016e }
        if (r9 == 0) goto L_0x0098;
    L_0x008f:
        r9 = r13.videoPlayer;	 Catch:{ Exception -> 0x016e }
        r10 = r13.vservAdController;	 Catch:{ Exception -> 0x016e }
        r10 = r10.requestId;	 Catch:{ Exception -> 0x016e }
        r9.setRequestId(r10);	 Catch:{ Exception -> 0x016e }
    L_0x0098:
        r5 = new android.widget.FrameLayout$LayoutParams;	 Catch:{ Exception -> 0x016e }
        r9 = r3.width;	 Catch:{ Exception -> 0x016e }
        r10 = r3.height;	 Catch:{ Exception -> 0x016e }
        r5.<init>(r9, r10);	 Catch:{ Exception -> 0x016e }
        r9 = r13.videoPlayer;	 Catch:{ Exception -> 0x016e }
        r9.setLayoutParams(r5);	 Catch:{ Exception -> 0x016e }
        r1 = new android.widget.FrameLayout;	 Catch:{ Exception -> 0x016e }
        r9 = r13.context;	 Catch:{ Exception -> 0x016e }
        r1.<init>(r9);	 Catch:{ Exception -> 0x016e }
        r9 = new mobi.vserv.org.ormma.view.OrmmaView$9;	 Catch:{ Exception -> 0x016e }
        r9.<init>();	 Catch:{ Exception -> 0x016e }
        r1.setOnTouchListener(r9);	 Catch:{ Exception -> 0x016e }
        r9 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        r1.setId(r9);	 Catch:{ Exception -> 0x016e }
        r9 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x016e }
        if (r9 == 0) goto L_0x00ea;
    L_0x00be:
        r9 = "aaa";
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x016e }
        r11 = "d.x value is ";
        r10.<init>(r11);	 Catch:{ Exception -> 0x016e }
        r11 = r3.x;	 Catch:{ Exception -> 0x016e }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x016e }
        r10 = r10.toString();	 Catch:{ Exception -> 0x016e }
        android.util.Log.i(r9, r10);	 Catch:{ Exception -> 0x016e }
        r9 = "aaa";
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x016e }
        r11 = "d.y value is ";
        r10.<init>(r11);	 Catch:{ Exception -> 0x016e }
        r11 = r3.y;	 Catch:{ Exception -> 0x016e }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x016e }
        r10 = r10.toString();	 Catch:{ Exception -> 0x016e }
        android.util.Log.i(r9, r10);	 Catch:{ Exception -> 0x016e }
    L_0x00ea:
        r9 = r3.x;	 Catch:{ Exception -> 0x016e }
        if (r9 <= 0) goto L_0x0162;
    L_0x00ee:
        r9 = r3.y;	 Catch:{ Exception -> 0x016e }
        r10 = r3.x;	 Catch:{ Exception -> 0x016e }
        r11 = 0;
        r12 = 0;
        r1.setPadding(r9, r10, r11, r12);	 Catch:{ Exception -> 0x016e }
    L_0x00f7:
        r9 = r13.getRootView();	 Catch:{ Exception -> 0x016e }
        r10 = 16908290; // 0x1020002 float:2.3877235E-38 double:8.353805E-317;
        r2 = r9.findViewById(r10);	 Catch:{ Exception -> 0x016e }
        r2 = (android.widget.FrameLayout) r2;	 Catch:{ Exception -> 0x016e }
        r6 = new android.widget.FrameLayout$LayoutParams;	 Catch:{ Exception -> 0x016e }
        r9 = -1;
        r10 = -1;
        r6.<init>(r9, r10);	 Catch:{ Exception -> 0x016e }
        r9 = r3.y;	 Catch:{ Exception -> 0x016e }
        if (r9 <= 0) goto L_0x0117;
    L_0x010f:
        r9 = 0;
        r10 = 120; // 0x78 float:1.68E-43 double:5.93E-322;
        r11 = 0;
        r12 = 0;
        r6.setMargins(r9, r10, r11, r12);	 Catch:{ Exception -> 0x016e }
    L_0x0117:
        r9 = r3.y;	 Catch:{ Exception -> 0x016e }
        if (r9 != 0) goto L_0x0127;
    L_0x011b:
        r9 = r3.x;	 Catch:{ Exception -> 0x016e }
        if (r9 != 0) goto L_0x0127;
    L_0x011f:
        r9 = 0;
        r10 = 50;
        r11 = 0;
        r12 = 0;
        r6.setMargins(r9, r10, r11, r12);	 Catch:{ Exception -> 0x016e }
    L_0x0127:
        r2.addView(r1, r6);	 Catch:{ Exception -> 0x016e }
        r9 = r13.videoPlayer;	 Catch:{ Exception -> 0x016e }
        r1.addView(r9);	 Catch:{ Exception -> 0x016e }
        r9 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x016e }
        if (r9 == 0) goto L_0x013a;
    L_0x0133:
        r9 = "vserv";
        r10 = "set Video ";
        android.util.Log.i(r9, r10);	 Catch:{ Exception -> 0x016e }
    L_0x013a:
        r9 = r13.videoPlayer;	 Catch:{ Exception -> 0x016e }
        r10 = new mobi.vserv.org.ormma.view.OrmmaView$10;	 Catch:{ Exception -> 0x016e }
        r10.<init>(r7);	 Catch:{ Exception -> 0x016e }
        r9.setListener(r10);	 Catch:{ Exception -> 0x016e }
        r9 = r13.skipdelay;	 Catch:{ Exception -> 0x016e }
        if (r9 <= 0) goto L_0x015c;
    L_0x0148:
        r9 = r13.isOnClicked;	 Catch:{ Exception -> 0x016e }
        if (r9 != 0) goto L_0x0173;
    L_0x014c:
        r9 = r13.vservAdController;	 Catch:{ Exception -> 0x016e }
        r10 = r13.skipdelay;	 Catch:{ Exception -> 0x016e }
        r9.currentSkipDelay = r10;	 Catch:{ Exception -> 0x016e }
        r9 = r13.vservAdController;	 Catch:{ Exception -> 0x016e }
        r10 = 1;
        r9.showskipDelay = r10;	 Catch:{ Exception -> 0x016e }
        r9 = r13.vservAdController;	 Catch:{ Exception -> 0x016e }
        r9.showSkipPage_onpageFinished();	 Catch:{ Exception -> 0x016e }
    L_0x015c:
        r9 = r13.videoPlayer;	 Catch:{ Exception -> 0x016e }
        r9.playVideo();	 Catch:{ Exception -> 0x016e }
    L_0x0161:
        return;
    L_0x0162:
        r9 = r3.y;	 Catch:{ Exception -> 0x016e }
        r10 = r3.x;	 Catch:{ Exception -> 0x016e }
        r10 = r10 + 22;
        r11 = 0;
        r12 = 0;
        r1.setPadding(r9, r10, r11, r12);	 Catch:{ Exception -> 0x016e }
        goto L_0x00f7;
    L_0x016e:
        r4 = move-exception;
        r4.printStackTrace();
        goto L_0x0161;
    L_0x0173:
        r9 = r13.vservAdController;	 Catch:{ Exception -> 0x016e }
        r10 = 0;
        r9.currentSkipDelay = r10;	 Catch:{ Exception -> 0x016e }
        goto L_0x015c;
    L_0x0179:
        r9 = r13.vservAdController;	 Catch:{ Exception -> 0x016e }
        r9 = r9.getCachePlayer();	 Catch:{ Exception -> 0x016e }
        r13.videoPlayer = r9;	 Catch:{ Exception -> 0x016e }
        r9 = r13.videoPlayer;	 Catch:{ Exception -> 0x016e }
        r10 = r13.context;	 Catch:{ Exception -> 0x016e }
        r9.setContext(r10);	 Catch:{ Exception -> 0x016e }
        r9 = r13.vservAdController;	 Catch:{ Exception -> 0x016e }
        r9 = r9.requestId;	 Catch:{ Exception -> 0x016e }
        if (r9 == 0) goto L_0x0197;
    L_0x018e:
        r9 = r13.videoPlayer;	 Catch:{ Exception -> 0x016e }
        r10 = r13.vservAdController;	 Catch:{ Exception -> 0x016e }
        r10 = r10.requestId;	 Catch:{ Exception -> 0x016e }
        r9.setRequestId(r10);	 Catch:{ Exception -> 0x016e }
    L_0x0197:
        r9 = r13.vservAdController;	 Catch:{ Exception -> 0x016e }
        r9.removeCachePlayer();	 Catch:{ Exception -> 0x016e }
        r9 = r13.vservAdController;	 Catch:{ Exception -> 0x016e }
        r9.removeCacheAdType();	 Catch:{ Exception -> 0x016e }
        r9 = r13.videoPlayer;	 Catch:{ Exception -> 0x016e }
        r3 = r9.objdim;	 Catch:{ Exception -> 0x016e }
        r9 = r13.videoPlayer;	 Catch:{ Exception -> 0x016e }
        r7 = r9.getPlayerProperties();	 Catch:{ Exception -> 0x016e }
        r1 = new android.widget.FrameLayout;	 Catch:{ Exception -> 0x016e }
        r9 = r13.context;	 Catch:{ Exception -> 0x016e }
        r1.<init>(r9);	 Catch:{ Exception -> 0x016e }
        r9 = new mobi.vserv.org.ormma.view.OrmmaView$11;	 Catch:{ Exception -> 0x016e }
        r9.<init>();	 Catch:{ Exception -> 0x016e }
        r1.setOnTouchListener(r9);	 Catch:{ Exception -> 0x016e }
        r9 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        r1.setId(r9);	 Catch:{ Exception -> 0x016e }
        r9 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;	 Catch:{ Exception -> 0x016e }
        if (r9 == 0) goto L_0x01ef;
    L_0x01c3:
        r9 = "aaa";
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x016e }
        r11 = "d.x value is ";
        r10.<init>(r11);	 Catch:{ Exception -> 0x016e }
        r11 = r3.x;	 Catch:{ Exception -> 0x016e }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x016e }
        r10 = r10.toString();	 Catch:{ Exception -> 0x016e }
        android.util.Log.i(r9, r10);	 Catch:{ Exception -> 0x016e }
        r9 = "aaa";
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x016e }
        r11 = "d.y value is ";
        r10.<init>(r11);	 Catch:{ Exception -> 0x016e }
        r11 = r3.y;	 Catch:{ Exception -> 0x016e }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x016e }
        r10 = r10.toString();	 Catch:{ Exception -> 0x016e }
        android.util.Log.i(r9, r10);	 Catch:{ Exception -> 0x016e }
    L_0x01ef:
        r9 = r3.y;	 Catch:{ Exception -> 0x016e }
        r10 = r3.x;	 Catch:{ Exception -> 0x016e }
        r10 = r10 + 20;
        r11 = 0;
        r12 = 0;
        r1.setPadding(r9, r10, r11, r12);	 Catch:{ Exception -> 0x016e }
        r9 = r13.getRootView();	 Catch:{ Exception -> 0x016e }
        r10 = 16908290; // 0x1020002 float:2.3877235E-38 double:8.353805E-317;
        r2 = r9.findViewById(r10);	 Catch:{ Exception -> 0x016e }
        r2 = (android.widget.FrameLayout) r2;	 Catch:{ Exception -> 0x016e }
        r6 = new android.widget.FrameLayout$LayoutParams;	 Catch:{ Exception -> 0x016e }
        r9 = -1;
        r10 = -1;
        r6.<init>(r9, r10);	 Catch:{ Exception -> 0x016e }
        r9 = 0;
        r10 = 50;
        r11 = 0;
        r12 = 0;
        r6.setMargins(r9, r10, r11, r12);	 Catch:{ Exception -> 0x016e }
        r2.addView(r1, r6);	 Catch:{ Exception -> 0x016e }
        r9 = r13.videoPlayer;	 Catch:{ Exception -> 0x016e }
        r1.addView(r9);	 Catch:{ Exception -> 0x016e }
        r9 = r13.videoPlayer;	 Catch:{ Exception -> 0x016e }
        r10 = new mobi.vserv.org.ormma.view.OrmmaView$12;	 Catch:{ Exception -> 0x016e }
        r10.<init>(r7);	 Catch:{ Exception -> 0x016e }
        r9.setListener(r10);	 Catch:{ Exception -> 0x016e }
        r9 = r13.skipdelay;	 Catch:{ Exception -> 0x016e }
        if (r9 <= 0) goto L_0x0240;
    L_0x022c:
        r9 = r13.isOnClicked;	 Catch:{ Exception -> 0x016e }
        if (r9 != 0) goto L_0x0247;
    L_0x0230:
        r9 = r13.vservAdController;	 Catch:{ Exception -> 0x016e }
        r10 = r13.skipdelay;	 Catch:{ Exception -> 0x016e }
        r9.currentSkipDelay = r10;	 Catch:{ Exception -> 0x016e }
        r9 = r13.vservAdController;	 Catch:{ Exception -> 0x016e }
        r10 = 1;
        r9.showskipDelay = r10;	 Catch:{ Exception -> 0x016e }
        r9 = r13.vservAdController;	 Catch:{ Exception -> 0x016e }
        r9.showSkipPage_onpageFinished();	 Catch:{ Exception -> 0x016e }
    L_0x0240:
        r9 = r13.videoPlayer;	 Catch:{ Exception -> 0x016e }
        r9.playCacheVideo();	 Catch:{ Exception -> 0x016e }
        goto L_0x0161;
    L_0x0247:
        r9 = r13.vservAdController;	 Catch:{ Exception -> 0x016e }
        r10 = 0;
        r9.currentSkipDelay = r10;	 Catch:{ Exception -> 0x016e }
        goto L_0x0240;
        */
    }

    public void raiseError(String strMsg, String action) {
        Message msg = this.mHandler.obtainMessage(MESSAGE_RAISE_ERROR);
        Bundle data = new Bundle();
        data.putString(ERROR_MESSAGE, strMsg);
        data.putString(ERROR_ACTION, action);
        msg.setData(data);
        this.mHandler.sendMessage(msg);
    }

    public void registerProtocol(String protocol) {
        if (protocol != null) {
            this.registeredProtocols.add(protocol.toLowerCase());
        }
    }

    public void removeListener() {
        this.mListener = null;
    }

    public void resetContents() {
        FrameLayout contentView = (FrameLayout) getRootView().findViewById(16908290);
        FrameLayout placeHolder = (FrameLayout) getRootView().findViewById(PLACEHOLDER_ID);
        FrameLayout background = (FrameLayout) getRootView().findViewById(BACKGROUND_ID);
        ViewGroup parent = (ViewGroup) placeHolder.getParent();
        background.removeView(this);
        contentView.removeView(background);
        resetLayout();
        parent.addView(this, this.mIndex);
        parent.removeView(placeHolder);
        parent.invalidate();
    }

    public void resize(int width, int height) {
        Message msg = this.mHandler.obtainMessage(MESSAGE_RESIZE);
        Bundle data = new Bundle();
        data.putInt(RESIZE_WIDTH, width);
        data.putInt(RESIZE_HEIGHT, height);
        msg.setData(data);
        this.mHandler.sendMessage(msg);
    }

    public WebBackForwardList restoreState(Bundle savedInstanceState) {
        this.mLocalFilePath = savedInstanceState.getString(AD_PATH);
        super.loadUrl(new StringBuilder("file://").append(this.mLocalFilePath).append(File.separator).append(CURRENT_FILE).toString());
        return null;
    }

    public WebBackForwardList saveState(Bundle outState) {
        outState.putString(AD_PATH, this.mLocalFilePath);
        return null;
    }

    public void setCacheView(Context context, VservAdController vservAdController) {
        this.context = context;
        this.vservAdController = vservAdController;
        this.displayCacheAd = true;
        if (!(vservAdController == null || vservAdController.adComponentView == null || vservAdController.adComponentView.mUtilityController == null)) {
            vservAdController.adComponentView.mUtilityController.setDisplayCacheAd(true);
        }
        this.vservAdManageractivity = null;
    }

    public void setCacheViewActivity(Context context, VservAdController vservAdController, VservAdManager vservAdManageractivity) {
        this.context = context;
        this.vservAdController = vservAdController;
        this.vservAdManageractivity = vservAdManageractivity;
        this.displayCacheAd = true;
        if (vservAdController != null && vservAdController.adComponentView != null && vservAdController.adComponentView.mUtilityController != null) {
            vservAdController.adComponentView.mUtilityController.setDisplayCacheAd(true);
        }
    }

    public void setListener(OrmmaViewListener listener) {
        this.mListener = listener;
    }

    public void setMapAPIKey(String key) {
        this.mapAPIKey = key;
    }

    public void setMaxSize(int w, int h) {
        this.mUtilityController.setMaxSize(w, h);
    }

    public void setSkipDelay(int t_skipdelay) {
        this.skipdelay = t_skipdelay;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setVideoType(java.lang.String r3_t_type) {
        throw new UnsupportedOperationException("Method not decompiled: mobi.vserv.org.ormma.view.OrmmaView.setVideoType(java.lang.String):void");
        /*
        r2 = this;
        r0 = "ormma";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x0013;
    L_0x0008:
        r0 = r2.vservAdController;
        if (r0 == 0) goto L_0x0012;
    L_0x000c:
        r0 = r2.vservAdController;
        r1 = "ormma";
        r0.requestId = r1;
    L_0x0012:
        return;
    L_0x0013:
        r0 = "vserv";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x0012;
    L_0x001b:
        r0 = r2.vservAdController;
        if (r0 == 0) goto L_0x0012;
    L_0x001f:
        r0 = r2.vservAdController;
        r0 = r0.requestId;
        if (r0 != 0) goto L_0x0012;
    L_0x0025:
        r0 = r2.vservAdController;
        r1 = "vserv";
        r0.requestId = r1;
        goto L_0x0012;
        */
    }

    public void show() {
        this.mHandler.sendEmptyMessage(MESSAGE_SHOW);
    }

    public void showClose() {
        Log.i("vserv", "ormmaview showclose");
        if (this.vservAdManageractivity == null) {
            PlayerProperties properties = null;
            if (this.videoPlayer != null) {
                properties = this.videoPlayer.getPlayerProperties();
            }
            Intent intent = new Intent("mobi.vserv.ad.dismiss_screen");
            if (properties == null || !(properties.exitOnComplete() || properties.inline)) {
                intent.putExtra(Event.INTENT_EXTERNAL_BROWSER, "close browser only");
            }
            this.context.sendBroadcast(intent);
        } else if (this.vservAdController != null) {
            this.vservAdController.showClose();
        }
    }
}