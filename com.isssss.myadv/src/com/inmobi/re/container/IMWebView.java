package com.inmobi.re.container;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.VideoView;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.plus.PlusShare;
import com.inmobi.androidsdk.IMBrowserActivity;
import com.inmobi.commons.data.DeviceInfo;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.internal.WrapperFunctions;
import com.inmobi.monetization.internal.imai.IMAIController;
import com.inmobi.re.configs.Initializer;
import com.inmobi.re.container.mraidimpl.MRAIDAudioVideoController;
import com.inmobi.re.container.mraidimpl.MRAIDBasic;
import com.inmobi.re.container.mraidimpl.MRAIDExpandController;
import com.inmobi.re.container.mraidimpl.MRAIDInterstitialController;
import com.inmobi.re.container.mraidimpl.MRAIDResizeController;
import com.inmobi.re.container.mraidimpl.ResizeDimensions;
import com.inmobi.re.controller.JSController.Dimensions;
import com.inmobi.re.controller.JSController.ExpandProperties;
import com.inmobi.re.controller.JSController.PlayerProperties;
import com.inmobi.re.controller.JSController.ResizeProperties;
import com.inmobi.re.controller.JSUtilityController;
import com.inmobi.re.controller.util.AVPlayer;
import com.inmobi.re.controller.util.AVPlayer.playerState;
import com.inmobi.re.controller.util.Constants;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import mobi.vserv.org.ormma.view.OrmmaView;
import org.json.JSONException;
import org.json.JSONObject;

public class IMWebView extends WebView implements Serializable {
    public static final String DIMENSIONS = "expand_dimensions";
    public static final String EXPAND_URL = "expand_url";
    private static Class<?> G = null;
    protected static final int IMWEBVIEW_INTERSTITIAL_ID = 117;
    public static final String PLAYER_PROPERTIES = "player_properties";
    private static int[] d = null;
    private static final long serialVersionUID = 7098506283154473782L;
    public static boolean userInitiatedClose;
    private i A;
    private ArrayList<String> B;
    private AtomicBoolean C;
    private ViewParent D;
    private int E;
    private boolean F;
    private WebViewClient H;
    private WebChromeClient I;
    private OnCompletionListener J;
    private boolean K;
    double a;
    public AtomicBoolean acqMutexcPos;
    public AtomicBoolean acqMutexdPos;
    AtomicBoolean b;
    InstantVideoCallbackCallback c;
    public JSONObject curPosition;
    public JSONObject defPosition;
    public AtomicBoolean doNotFireVisibilityChanged;
    private boolean e;
    private boolean f;
    private JSUtilityController g;
    private float h;
    private int i;
    public AtomicBoolean isMutexAquired;
    public boolean isTablet;
    private int j;
    private ViewState k;
    private IMWebViewPlayableListener l;
    private VideoView m;
    public MRAIDAudioVideoController mAudioVideoController;
    public MRAIDExpandController mExpandController;
    public MRAIDInterstitialController mInterstitialController;
    public boolean mIsInterstitialAd;
    public boolean mIsViewable;
    public IMWebViewListener mListener;
    public MRAIDBasic mMraidBasic;
    public IMWebView mOriginalWebviewForExpandUrl;
    public MRAIDResizeController mResizeController;
    public boolean mWebViewIsBrowserActivity;
    protected boolean mraidLoaded;
    public Object mutex;
    public Object mutexcPos;
    public Object mutexdPos;
    private View n;
    private CustomViewCallback o;
    private ViewGroup p;
    public int publisherOrientation;
    private FrameLayout q;
    private ArrayList<g> r;
    private boolean s;
    private boolean t;
    private boolean u;
    private boolean v;
    private Message w;
    public String webviewUserAgent;
    private Message x;
    private Activity y;
    private WebViewClient z;

    public static interface IMWebViewListener {
        void onDismissAdScreen();

        void onDisplayFailed();

        void onError();

        void onExpand();

        void onExpandClose();

        void onIncentCompleted(Map<Object, Object> map);

        void onLeaveApplication();

        void onResize(ResizeDimensions resizeDimensions);

        void onResizeClose();

        void onShowAdScreen();

        void onUserInteraction(Map<String, String> map);
    }

    public static interface IMWebViewPlayableListener {
        void onPlayableSettingsReceived(Map<String, Object> map);
    }

    public static interface InstantVideoCallbackCallback {
        void postFailed(int i);

        void postSuccess();
    }

    public enum ViewState {
        LOADING,
        DEFAULT,
        RESIZED,
        EXPANDED,
        EXPANDING,
        HIDDEN,
        RESIZING;

        static {
            LOADING = new com.inmobi.re.container.IMWebView.ViewState("LOADING", 0);
            DEFAULT = new com.inmobi.re.container.IMWebView.ViewState("DEFAULT", 1);
            RESIZED = new com.inmobi.re.container.IMWebView.ViewState("RESIZED", 2);
            EXPANDED = new com.inmobi.re.container.IMWebView.ViewState("EXPANDED", 3);
            EXPANDING = new com.inmobi.re.container.IMWebView.ViewState("EXPANDING", 4);
            HIDDEN = new com.inmobi.re.container.IMWebView.ViewState("HIDDEN", 5);
            RESIZING = new com.inmobi.re.container.IMWebView.ViewState("RESIZING", 6);
            a = new com.inmobi.re.container.IMWebView.ViewState[]{LOADING, DEFAULT, RESIZED, EXPANDED, EXPANDING, HIDDEN, RESIZING};
        }
    }

    class a implements Runnable {
        final /* synthetic */ String a;

        a(String str) {
            this.a = str;
        }

        public void run() {
            IMWebView.this.injectJavaScript(this.a);
        }
    }

    class b implements Runnable {
        final /* synthetic */ String a;
        final /* synthetic */ String b;

        b(String str, String str2) {
            this.a = str;
            this.b = str2;
        }

        public void run() {
            if (this.a != null) {
                IMWebView.this.injectJavaScript("window.imraid.broadcastEvent('" + this.b + "','" + this.a + "');");
            } else {
                IMWebView.this.injectJavaScript("window.imraid.broadcastEvent('" + this.b + "');");
            }
        }
    }

    class c implements Runnable {
        final /* synthetic */ Object a;
        final /* synthetic */ String b;

        c(Object obj, String str) {
            this.a = obj;
            this.b = str;
        }

        public void run() {
            if (this.a != null) {
                IMWebView.this.injectJavaScript("window.imraid.broadcastEvent('" + this.b + "'," + this.a + ");");
            } else {
                IMWebView.this.injectJavaScript("window.imraid.broadcastEvent('" + this.b + "');");
            }
        }
    }

    class d implements OnClickListener {
        final /* synthetic */ SslError a;

        d(SslError sslError) {
            this.a = sslError;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.cancel();
            IMWebView.this.a(WrapperFunctions.getSSLErrorUrl(this.a));
        }
    }

    class e implements OnClickListener {
        final /* synthetic */ SslErrorHandler a;

        e(SslErrorHandler sslErrorHandler) {
            this.a = sslErrorHandler;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.cancel();
            this.a.proceed();
        }
    }

    class f implements OnClickListener {
        final /* synthetic */ SslErrorHandler a;

        f(SslErrorHandler sslErrorHandler) {
            this.a = sslErrorHandler;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.cancel();
            this.a.cancel();
        }
    }

    class g extends AsyncTask<Void, Void, String> {
        File a;
        String b;
        String c;
        int d;
        String e;

        g(File file, String str, String str2) {
            this.d = -1;
            this.e = Preconditions.EMPTY_ARGUMENTS;
            this.a = file;
            this.b = str;
            this.c = str2;
            IMWebView.this.r.add(this);
        }

        public String a() {
            return this.c;
        }

        protected String a(Void... voidArr) {
            PackageManager packageManager = IMWebView.this.y.getPackageManager();
            if ((packageManager.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", packageManager.getNameForUid(Binder.getCallingUid())) == 0) == 0) {
                this.d = h.j.ordinal();
                return "failure";
            } else if (!InternalSDKUtil.checkNetworkAvailibility(InternalSDKUtil.getContext())) {
                this.d = h.i.ordinal();
                return "failure";
            } else if (!this.c.matches("[A-Za-z0-9]+") || this.c.equals(Preconditions.EMPTY_ARGUMENTS)) {
                this.d = h.c.ordinal();
                return "failure";
            } else if (this.b.equals(Preconditions.EMPTY_ARGUMENTS) || !URLUtil.isValidUrl(this.b)) {
                this.d = h.d.ordinal();
                return "failure";
            } else if (Environment.getExternalStorageState().equals("mounted")) {
                String replace = Initializer.getConfigParams().getAllowedContentType().replace("\\", Preconditions.EMPTY_ARGUMENTS);
                String substring = replace.substring(1, replace.length() - 1);
                String[] strArr;
                if (substring.contains(",")) {
                    strArr = substring.split(",");
                } else {
                    strArr = new String[]{substring};
                }
                int maxSaveContentSize = Initializer.getConfigParams().getMaxSaveContentSize();
                try {
                    boolean z;
                    long currentTimeMillis = System.currentTimeMillis();
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.b).openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT);
                    String contentType = httpURLConnection.getContentType();
                    Log.i(Constants.RENDERING_LOG_TAG, "contentType_url: " + contentType);
                    int i = 0;
                    while (i < Arr.length) {
                        if (Arr[i].substring(1, Arr[i].length() - 1).equals(contentType)) {
                            z = true;
                            break;
                        } else {
                            i++;
                        }
                    }
                    z = false;
                    if (i == 0) {
                        this.d = h.g.ordinal();
                        return "failure";
                    } else {
                        long contentLength = (long) httpURLConnection.getContentLength();
                        if (contentLength >= 0) {
                            Log.e(Constants.RENDERING_LOG_TAG, "content size: " + contentLength);
                            Log.e(Constants.RENDERING_LOG_TAG, "max size: " + ((maxSaveContentSize * 1024) * 1024));
                            if (contentLength > ((long) ((maxSaveContentSize * 1024) * 1024))) {
                                this.d = h.h.ordinal();
                                return "failure";
                            }
                        }
                        httpURLConnection.connect();
                        FileOutputStream fileOutputStream = new FileOutputStream(this.a);
                        InputStream inputStream = httpURLConnection.getInputStream();
                        byte[] bArr = new byte[1024];
                        long j = 0;
                        while (true) {
                            int read = inputStream.read(bArr);
                            if (read > 0) {
                                j += (long) read;
                                if (j > ((long) ((maxSaveContentSize * 1024) * 1024))) {
                                    this.d = h.h.ordinal();
                                    return "failure";
                                } else {
                                    fileOutputStream.write(bArr, 0, read);
                                }
                            } else {
                                fileOutputStream.close();
                                j = System.currentTimeMillis();
                                String str = "file://" + this.a.getAbsolutePath();
                                com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "file path of video: " + str);
                                JSONObject jSONObject = new JSONObject();
                                jSONObject.put(PlusShare.KEY_CALL_TO_ACTION_URL, this.b);
                                jSONObject.put("saved_url", str);
                                jSONObject.put("size_in_bytes", this.a.length());
                                jSONObject.put("download_started_at", currentTimeMillis);
                                jSONObject.put("download_ended_at", j);
                                this.e = jSONObject.toString().replace("\"", "\\\"");
                                return "success";
                            }
                        }
                    }
                } catch (SocketTimeoutException e) {
                    com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "SocketTimeoutException");
                    this.d = h.i.ordinal();
                    return "failure";
                } catch (FileNotFoundException e2) {
                    com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "FileNotFoundException");
                    this.d = h.e.ordinal();
                    return "failure";
                } catch (MalformedURLException e3) {
                    com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "MalformedURLException");
                    this.d = h.d.ordinal();
                    return "failure";
                } catch (ProtocolException e4) {
                    com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "ProtocolException");
                    this.d = h.i.ordinal();
                    return "failure";
                } catch (IOException e5) {
                    com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "IOException");
                    this.d = h.a.ordinal();
                    return "failure";
                } catch (JSONException e6) {
                    com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "JSONException");
                    this.d = h.a.ordinal();
                    return "failure";
                } catch (Exception e7) {
                    com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "Unknown Exception");
                    this.d = h.a.ordinal();
                    return "failure";
                }
            } else {
                this.d = h.k.ordinal();
                return "failure";
            }
        }

        protected void a(String str) {
            if (str.equals("success")) {
                IMWebView.this.injectJavaScript("window.mraid.sendSaveContentResult(\"saveContent_" + this.c + "\", 'success', \"" + this.e + "\");");
                if (IMWebView.this.c != null) {
                    IMWebView.this.c.postSuccess();
                }
            } else {
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put(PlusShare.KEY_CALL_TO_ACTION_URL, this.b);
                    jSONObject.put("reason", this.d);
                    IMWebView.this.injectJavaScript("window.mraid.sendSaveContentResult(\"saveContent_" + this.c + "\", 'failure', \"" + jSONObject.toString().replace("\"", "\\\"") + "\");");
                    if (IMWebView.this.c != null) {
                        IMWebView.this.c.postFailed(this.d);
                    }
                } catch (JSONException e) {
                }
            }
            super.onPostExecute(str);
        }

        protected /* bridge */ /* synthetic */ Object doInBackground(Object[] objArr) {
            return a((Void[]) objArr);
        }

        protected void onCancelled() {
            super.onCancelled();
        }

        protected /* bridge */ /* synthetic */ void onPostExecute(Object obj) {
            a((String) obj);
        }
    }

    private enum h {
        UNKNOWN_ERROR,
        MISSING_PARAMETER,
        CONETNT_ID_ERROR,
        CONTENT_URL_ERROR,
        CONTENT_URL_NOT_FOUND,
        NOT_SUPPORTED_SDK,
        CONTENT_TYPE_NOT_SUPPORTED,
        CONTENT_SIZE_NOT_SUPPORTED,
        NETWORK_ERROR,
        PERMISSION_ERROR,
        SD_CARD_ERROR;

        static {
            a = new h("UNKNOWN_ERROR", 0);
            b = new h("MISSING_PARAMETER", 1);
            c = new h("CONETNT_ID_ERROR", 2);
            d = new h("CONTENT_URL_ERROR", 3);
            e = new h("CONTENT_URL_NOT_FOUND", 4);
            f = new h("NOT_SUPPORTED_SDK", 5);
            g = new h("CONTENT_TYPE_NOT_SUPPORTED", 6);
            h = new h("CONTENT_SIZE_NOT_SUPPORTED", 7);
            i = new h("NETWORK_ERROR", 8);
            j = new h("PERMISSION_ERROR", 9);
            k = new h("SD_CARD_ERROR", 10);
            l = new h[]{a, b, c, d, e, f, g, h, i, j, k};
        }
    }

    static class i extends Handler {
        private final WeakReference<IMWebView> a;
        private final WeakReference<MRAIDExpandController> b;
        private final WeakReference<MRAIDResizeController> c;
        private final WeakReference<MRAIDBasic> d;
        private final WeakReference<MRAIDInterstitialController> e;
        private final WeakReference<MRAIDAudioVideoController> f;

        public i(IMWebView iMWebView, MRAIDBasic mRAIDBasic, MRAIDExpandController mRAIDExpandController, MRAIDInterstitialController mRAIDInterstitialController, MRAIDAudioVideoController mRAIDAudioVideoController, MRAIDResizeController mRAIDResizeController) {
            this.a = new WeakReference(iMWebView);
            this.b = new WeakReference(mRAIDExpandController);
            this.d = new WeakReference(mRAIDBasic);
            this.e = new WeakReference(mRAIDInterstitialController);
            this.f = new WeakReference(mRAIDAudioVideoController);
            this.c = new WeakReference(mRAIDResizeController);
        }

        public void handleMessage(Message message) {
            try {
                IMWebView iMWebView = (IMWebView) this.a.get();
                MRAIDBasic mRAIDBasic = (MRAIDBasic) this.d.get();
                MRAIDExpandController mRAIDExpandController = (MRAIDExpandController) this.b.get();
                MRAIDResizeController mRAIDResizeController = (MRAIDResizeController) this.c.get();
                MRAIDInterstitialController mRAIDInterstitialController = (MRAIDInterstitialController) this.e.get();
                MRAIDAudioVideoController mRAIDAudioVideoController = (MRAIDAudioVideoController) this.f.get();
                if (iMWebView != null) {
                    com.inmobi.commons.internal.Log.debug(Constants.RENDERING_LOG_TAG, "IMWebView->handleMessage: msg: " + message);
                    Bundle data = message.getData();
                    String string;
                    AVPlayer aVPlayer;
                    AVPlayer videoPlayer;
                    HashMap hashMap;
                    switch (message.what) {
                        case ApiEventType.API_IMAI_OPEN_EMBEDDED:
                            switch (n.a[iMWebView.k.ordinal()]) {
                                case MMAdView.TRANSITION_FADE:
                                case MMAdView.TRANSITION_UP:
                                    mRAIDResizeController.closeResized();
                                    break;
                                case MMAdView.TRANSITION_DOWN:
                                case MMAdView.TRANSITION_RANDOM:
                                    mRAIDExpandController.closeExpanded();
                                    mRAIDExpandController.mIsExpandUrlValid = false;
                                    break;
                                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                                    break;
                                default:
                                    if (iMWebView.mIsInterstitialAd) {
                                        mRAIDInterstitialController.resetContentsForInterstitials();
                                    } else {
                                        iMWebView.hide();
                                    }
                                    break;
                            }
                            break;
                        case ApiEventType.API_IMAI_OPEN_EXTERNAL:
                            iMWebView.setVisibility(MMAdView.TRANSITION_RANDOM);
                            iMWebView.setState(com.inmobi.re.container.IMWebView.ViewState.HIDDEN);
                            break;
                        case ApiEventType.API_IMAI_PING:
                            iMWebView.injectJavaScript("window.mraidview.fireChangeEvent({ state: 'default' });");
                            iMWebView.setVisibility(0);
                            break;
                        case ApiEventType.API_IMAI_PING_IN_WEB_VIEW:
                            if (iMWebView.k == com.inmobi.re.container.IMWebView.ViewState.EXPANDING) {
                                mRAIDExpandController.doExpand(data);
                            }
                            iMWebView.g.setWebViewClosed(false);
                            break;
                        case 1005:
                            if (iMWebView.mListener != null) {
                                iMWebView.mListener.onExpandClose();
                            }
                            break;
                        case 1006:
                            try {
                                mRAIDAudioVideoController.playVideoImpl(data, iMWebView.y);
                            } catch (Exception e) {
                                com.inmobi.commons.internal.Log.debug(Constants.RENDERING_LOG_TAG, "Play video failed ", e);
                            }
                            break;
                        case 1007:
                            try {
                                mRAIDAudioVideoController.playAudioImpl(data, iMWebView.y);
                            } catch (Exception e2) {
                                com.inmobi.commons.internal.Log.debug(Constants.RENDERING_LOG_TAG, "Play audio failed ", e2);
                            }
                            break;
                        case 1008:
                            string = data.getString("message");
                            iMWebView.injectJavaScript("window.mraid.broadcastEvent('error',\"" + string + "\", \"" + data.getString(OrmmaView.ACTION_KEY) + "\")");
                            break;
                        case 1009:
                            iMWebView.setCloseButton();
                            break;
                        case 1010:
                            aVPlayer = (AVPlayer) mRAIDAudioVideoController.audioPlayerList.get(data.getString("aplayerref"));
                            if (aVPlayer != null) {
                                aVPlayer.pause();
                            }
                            break;
                        case 1011:
                            AVPlayer videoPlayer2 = mRAIDAudioVideoController.getVideoPlayer(data.getString("pid"));
                            if (videoPlayer2 == null) {
                                string = "window.mraid.broadcastEvent('error',\"Invalid property ID\", \"pauseVideo\")";
                            } else if (videoPlayer2.getState() == playerState.PLAYING) {
                                videoPlayer2.pause();
                                return;
                            } else if (videoPlayer2.getState() != playerState.INIT) {
                                string = "window.mraid.broadcastEvent('error',\"Invalid player state\", \"pauseVideo\")";
                            } else if (!videoPlayer2.isPrepared()) {
                                videoPlayer2.setAutoPlay(false);
                            }
                            iMWebView.injectJavaScript(string);
                            break;
                        case 1012:
                            ((AVPlayer) message.obj).releasePlayer(false);
                            break;
                        case 1013:
                            string = data.getString("pid");
                            videoPlayer = mRAIDAudioVideoController.getVideoPlayer(string);
                            if (videoPlayer == null) {
                                string = "window.mraid.broadcastEvent('error',\"Invalid property ID\", \"hideVideo\")";
                            } else if (videoPlayer.getState() == playerState.RELEASED) {
                                string = "window.mraid.broadcastEvent('error',\"Invalid player state\", \"hideVideo\")";
                            } else {
                                mRAIDAudioVideoController.videoPlayerList.put(string, videoPlayer);
                                videoPlayer.hide();
                                return;
                            }
                            iMWebView.injectJavaScript(string);
                            break;
                        case 1014:
                            string = data.getString("pid");
                            videoPlayer = mRAIDAudioVideoController.getVideoPlayer(string);
                            if (videoPlayer == null) {
                                string = "window.mraid.broadcastEvent('error',\"Invalid property ID\", \"showVideo\")";
                            } else if (videoPlayer.getState() != playerState.RELEASED && videoPlayer.getState() != playerState.HIDDEN) {
                                string = "window.mraid.broadcastEvent('error',\"Invalid player state\", \"showVideo\")";
                            } else if (mRAIDAudioVideoController.videoPlayer == null || mRAIDAudioVideoController.videoPlayer.getPropertyID().equalsIgnoreCase(string)) {
                                mRAIDAudioVideoController.videoPlayerList.remove(string);
                                mRAIDAudioVideoController.videoPlayer = videoPlayer;
                                videoPlayer.show();
                                return;
                            } else {
                                string = "window.mraid.broadcastEvent('error',\"Show failed. There is already a video playing\", \"showVideo\")";
                            }
                            iMWebView.injectJavaScript(string);
                            break;
                        case 1015:
                            ((AVPlayer) message.obj).mute();
                            break;
                        case 1016:
                            ((AVPlayer) message.obj).unMute();
                            break;
                        case 1017:
                            ((AVPlayer) message.obj).setVolume(data.getInt("volume"));
                            break;
                        case 1018:
                            ((AVPlayer) message.obj).seekPlayer(data.getInt("seek") * 1000);
                            break;
                        case 1019:
                            aVPlayer = (AVPlayer) mRAIDAudioVideoController.audioPlayerList.get(data.getString("aplayerref"));
                            if (aVPlayer != null) {
                                aVPlayer.mute();
                            }
                            break;
                        case 1020:
                            aVPlayer = (AVPlayer) mRAIDAudioVideoController.audioPlayerList.get(data.getString("aplayerref"));
                            if (aVPlayer != null) {
                                aVPlayer.unMute();
                            }
                            break;
                        case 1021:
                            aVPlayer = (AVPlayer) mRAIDAudioVideoController.audioPlayerList.get(data.getString("aplayerref"));
                            if (aVPlayer != null) {
                                aVPlayer.setVolume(data.getInt("vol"));
                            }
                            break;
                        case 1022:
                            ((AVPlayer) message.obj).seekPlayer(data.getInt("seekaudio") * 1000);
                            break;
                        case 1023:
                            mRAIDAudioVideoController.hidePlayers();
                            break;
                        case AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT:
                            mRAIDBasic.open(data.getString(EXPAND_URL));
                            break;
                        case 1025:
                            string = data.getString("injectMessage");
                            if (string != null) {
                                iMWebView.loadUrl("javascript:" + string);
                            }
                            break;
                        case 1026:
                            mRAIDInterstitialController.handleOrientationForInterstitial();
                            break;
                        case 1027:
                            if (!iMWebView.mIsInterstitialAd) {
                                mRAIDResizeController.onOrientationChange();
                            }
                            break;
                        case 1028:
                            if (iMWebView.mListener != null) {
                                iMWebView.mListener.onDismissAdScreen();
                            }
                            break;
                        case 1029:
                            mRAIDBasic.getCurrentPosition();
                            break;
                        case 1030:
                            if (iMWebView.k == com.inmobi.re.container.IMWebView.ViewState.RESIZING) {
                                mRAIDResizeController.doResize(data);
                            }
                            break;
                        case 1031:
                            if (iMWebView.mListener != null) {
                                iMWebView.mListener.onResizeClose();
                            }
                            break;
                        case 1032:
                            mRAIDBasic.getDefaultPosition();
                            break;
                        case 1033:
                            hashMap = (HashMap) message.getData().getSerializable("map");
                            if (iMWebView.mListener != null) {
                                iMWebView.mListener.onUserInteraction(hashMap);
                            }
                            break;
                        case 1034:
                            hashMap = (HashMap) message.getData().getSerializable("incent_ad_map");
                            if (iMWebView.mListener != null) {
                                iMWebView.mListener.onIncentCompleted(hashMap);
                            }
                            break;
                        case 1035:
                            iMWebView.disableCloseRegion();
                            break;
                    }
                }
                super.handleMessage(message);
            } catch (Exception e3) {
                com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "Webview Handle Message Exception ", e3);
            }
        }
    }

    class j implements Runnable {
        final /* synthetic */ String a;

        j(String str) {
            this.a = str;
        }

        public void run() {
            IMWebView.this.injectJavaScript(this.a);
        }
    }

    class k implements Runnable {
        k() {
        }

        public void run() {
            try {
                IMWebView.this.injectJavaScript("window.mraid.broadcastEvent('vibrateComplete')");
            } catch (Exception e) {
                com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "Exception giviing vibration complete callback", e);
            }
        }
    }

    class l implements Runnable {
        final /* synthetic */ String a;

        l(String str) {
            this.a = str;
        }

        public void run() {
            IMWebView.this.injectJavaScript(this.a);
        }
    }

    class m implements Runnable {
        m() {
        }

        public void run() {
            if (IMWebView.this.getParent() != null) {
                ((ViewGroup) IMWebView.this.getParent()).removeView(IMWebView.this);
            }
            IMWebView.this.b.set(true);
            super.destroy();
        }
    }

    static /* synthetic */ class n {
        static final /* synthetic */ int[] a;

        static {
            a = new int[com.inmobi.re.container.IMWebView.ViewState.values().length];
            try {
                a[com.inmobi.re.container.IMWebView.ViewState.RESIZING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                a[com.inmobi.re.container.IMWebView.ViewState.RESIZED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                a[com.inmobi.re.container.IMWebView.ViewState.EXPANDING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                a[com.inmobi.re.container.IMWebView.ViewState.EXPANDED.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                a[com.inmobi.re.container.IMWebView.ViewState.HIDDEN.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            a[com.inmobi.re.container.IMWebView.ViewState.DEFAULT.ordinal()] = 6;
        }
    }

    static {
        d = new int[]{16843039, 16843040};
        userInitiatedClose = false;
        G = null;
    }

    public IMWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.doNotFireVisibilityChanged = new AtomicBoolean(false);
        this.f = false;
        this.k = ViewState.LOADING;
        this.o = null;
        this.r = new ArrayList();
        this.s = false;
        this.mIsInterstitialAd = false;
        this.isTablet = false;
        this.mOriginalWebviewForExpandUrl = null;
        this.mWebViewIsBrowserActivity = false;
        this.t = false;
        this.u = false;
        this.mutex = new Object();
        this.mutexcPos = new Object();
        this.mutexdPos = new Object();
        this.isMutexAquired = new AtomicBoolean(false);
        this.acqMutexcPos = new AtomicBoolean(true);
        this.acqMutexdPos = new AtomicBoolean(true);
        this.B = new ArrayList();
        this.C = new AtomicBoolean();
        this.F = false;
        this.H = new b(this);
        this.I = new c(this);
        this.mIsViewable = false;
        this.J = new a(this);
        this.a = -1.0d;
        this.b = new AtomicBoolean(false);
        this.K = true;
        this.c = null;
        this.y = (Activity) context;
        c();
        getContext().obtainStyledAttributes(attributeSet, d).recycle();
    }

    public IMWebView(Context context, IMWebViewListener iMWebViewListener) {
        super(context);
        this.doNotFireVisibilityChanged = new AtomicBoolean(false);
        this.f = false;
        this.k = ViewState.LOADING;
        this.o = null;
        this.r = new ArrayList();
        this.s = false;
        this.mIsInterstitialAd = false;
        this.isTablet = false;
        this.mOriginalWebviewForExpandUrl = null;
        this.mWebViewIsBrowserActivity = false;
        this.t = false;
        this.u = false;
        this.mutex = new Object();
        this.mutexcPos = new Object();
        this.mutexdPos = new Object();
        this.isMutexAquired = new AtomicBoolean(false);
        this.acqMutexcPos = new AtomicBoolean(true);
        this.acqMutexdPos = new AtomicBoolean(true);
        this.B = new ArrayList();
        this.C = new AtomicBoolean();
        this.F = false;
        this.H = new b(this);
        this.I = new c(this);
        this.mIsViewable = false;
        this.J = new a(this);
        this.a = -1.0d;
        this.b = new AtomicBoolean(false);
        this.K = true;
        this.c = null;
        this.mListener = iMWebViewListener;
        this.y = (Activity) context;
        c();
    }

    public IMWebView(Context context, IMWebViewListener iMWebViewListener, boolean z, boolean z2) {
        super(context);
        this.doNotFireVisibilityChanged = new AtomicBoolean(false);
        this.f = false;
        this.k = ViewState.LOADING;
        this.o = null;
        this.r = new ArrayList();
        this.s = false;
        this.mIsInterstitialAd = false;
        this.isTablet = false;
        this.mOriginalWebviewForExpandUrl = null;
        this.mWebViewIsBrowserActivity = false;
        this.t = false;
        this.u = false;
        this.mutex = new Object();
        this.mutexcPos = new Object();
        this.mutexdPos = new Object();
        this.isMutexAquired = new AtomicBoolean(false);
        this.acqMutexcPos = new AtomicBoolean(true);
        this.acqMutexdPos = new AtomicBoolean(true);
        this.B = new ArrayList();
        this.C = new AtomicBoolean();
        this.F = false;
        this.H = new b(this);
        this.I = new c(this);
        this.mIsViewable = false;
        this.J = new a(this);
        this.a = -1.0d;
        this.b = new AtomicBoolean(false);
        this.K = true;
        this.c = null;
        this.y = (Activity) context;
        this.mIsInterstitialAd = z;
        this.mWebViewIsBrowserActivity = z2;
        if (this.mIsInterstitialAd) {
            setId(IMWEBVIEW_INTERSTITIAL_ID);
        }
        this.mListener = iMWebViewListener;
        c();
    }

    private int a(Activity activity) {
        ResolveInfo resolveInfo;
        Iterator it = activity.getPackageManager().queryIntentActivities(new Intent(activity, activity.getClass()), Cast.MAX_MESSAGE_LENGTH).iterator();
        while (it.hasNext()) {
            resolveInfo = (ResolveInfo) it.next();
            if (resolveInfo.activityInfo.name.contentEquals(activity.getClass().getName())) {
                break;
            }
        }
        resolveInfo = null;
        return resolveInfo.activityInfo.configChanges;
    }

    private void a(int i, int i2) {
        injectJavaScript("window.mraid.broadcastEvent('sizeChange'," + i + "," + i2 + ");");
    }

    private void a(View view, OnKeyListener onKeyListener) {
        view.setOnKeyListener(onKeyListener);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int i = 0;
            int childCount = viewGroup.getChildCount();
            while (i < childCount) {
                a(viewGroup.getChildAt(i), onKeyListener);
                i++;
            }
        }
    }

    private void a(View view, OnTouchListener onTouchListener) {
        view.setOnTouchListener(onTouchListener);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int i = 0;
            int childCount = viewGroup.getChildCount();
            while (i < childCount) {
                a(viewGroup.getChildAt(i), onTouchListener);
                i++;
            }
        }
    }

    @TargetApi(8)
    private void a(SslErrorHandler sslErrorHandler, SslError sslError) {
        Builder builder = new Builder(this.y);
        builder.setPositiveButton("Continue", new e(sslErrorHandler));
        builder.setNegativeButton("Go Back", new f(sslErrorHandler));
        if (VERSION.SDK_INT >= 14) {
            builder.setNeutralButton("Open Browser", new d(sslError));
        }
        builder.setTitle("Security Warning");
        builder.setMessage("There are problems with the security certificate for this site.");
        try {
            builder.create().show();
        } catch (Exception e) {
            com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "Dialog could not be shown due to an exception.", e);
        }
    }

    private void a(String str) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.addFlags(DriveFile.MODE_READ_ONLY);
        this.y.startActivity(intent);
        fireOnLeaveApplication();
    }

    private void a(boolean z) {
        com.inmobi.commons.internal.Log.debug(Constants.RENDERING_LOG_TAG, "Viewable:" + z);
        injectJavaScript("window.mraid.broadcastEvent('viewableChange'," + isViewable() + ");");
    }

    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    private void c() {
        b();
        userInitiatedClose = false;
        setScrollContainer(false);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        this.webviewUserAgent = getSettings().getUserAgentString();
        InternalSDKUtil.getUserAgent();
        setBackgroundColor(Initializer.getConfigParams().getWebviewBgColor());
        ((WindowManager) getContext().getSystemService("window")).getDefaultDisplay().getMetrics(new DisplayMetrics());
        if (VERSION.SDK_INT >= 17) {
            getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        this.h = this.y.getResources().getDisplayMetrics().density;
        this.e = false;
        getSettings().setJavaScriptEnabled(true);
        getSettings().setGeolocationEnabled(true);
        this.g = new JSUtilityController(this, getContext());
        addJavascriptInterface(this.g, "utilityController");
        setWebViewClient(this.H);
        setWebChromeClient(this.I);
        this.mExpandController = new MRAIDExpandController(this, this.y);
        this.mResizeController = new MRAIDResizeController(this, this.y);
        this.mMraidBasic = new MRAIDBasic(this, this.y);
        this.mInterstitialController = new MRAIDInterstitialController(this, this.y);
        this.mAudioVideoController = new MRAIDAudioVideoController(this);
        this.A = new i(this, this.mMraidBasic, this.mExpandController, this.mInterstitialController, this.mAudioVideoController, this.mResizeController);
        this.mExpandController.mSensorDisplay = ((WindowManager) this.y.getSystemService("window")).getDefaultDisplay();
        this.mAudioVideoController.videoValidateWidth = this.y.getResources().getDisplayMetrics().widthPixels;
        try {
            addJavascriptInterface(G.getDeclaredConstructor(new Class[]{IMWebView.class}).newInstance(new Object[]{this}), IMAIController.IMAI_BRIDGE);
        } catch (Exception e) {
            com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "Error adding js interface imai controller");
        }
        this.g.setWebViewClosed(false);
    }

    private void d() {
        com.inmobi.commons.internal.Log.debug(Constants.RENDERING_LOG_TAG, "IMWebView-> initStates");
        this.k = ViewState.LOADING;
        this.C.set(false);
    }

    private void e() {
        if (this.k == ViewState.EXPANDED) {
            this.mExpandController.closeExpanded();
        }
        invalidate();
        this.g.stopAllListeners();
        resetLayout();
    }

    private void f() {
        if (this.n != null) {
            if (this.o != null) {
                this.o.onCustomViewHidden();
            }
            this.o = null;
            if (this.n.getParent() != null) {
                ((ViewGroup) this.n.getParent()).removeView(this.n);
            }
            this.n = null;
        }
    }

    private boolean g() {
        return this.b.get();
    }

    public static void setIMAIController(Class<?> cls) {
        G = cls;
    }

    void a() {
        try {
            if (this.r != null) {
                int i = 0;
                while (i < this.r.size()) {
                    g gVar = (g) this.r.get(i);
                    if (gVar.getStatus() == Status.RUNNING) {
                        gVar.cancel(true);
                    }
                    i++;
                }
                this.r.clear();
                this.r = null;
            }
            b();
        } catch (Exception e) {
            com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "Exception deleting saved content dirs and stopping download task");
        }
    }

    public void addJavascriptObject(Object obj, String str) {
        addJavascriptInterface(obj, str);
    }

    void b() {
        try {
            File file = new File(InternalSDKUtil.getContext().getExternalFilesDir(null) + "/im_cached_content/");
            if (file.exists() && file.isDirectory()) {
                String[] list = file.list();
                int i = 0;
                while (i < list.length) {
                    new File(file, list[i]).delete();
                    i++;
                }
                file.delete();
            }
        } catch (Exception e) {
            com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "Exception deleting saved content dirs and stopping download task");
        }
    }

    public void broadcastEventIMraid(String str, Object obj) {
        try {
            this.y.runOnUiThread(new c(obj, str));
        } catch (Exception e) {
            com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "Exception broadcasting events", e);
        }
    }

    public void broadcastEventIMraid(String str, String str2) {
        try {
            this.y.runOnUiThread(new b(str2, str));
        } catch (Exception e) {
            com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "Exception broadcasting events", e);
        }
    }

    public void cancelLoad() {
        this.C.set(true);
    }

    public void cancelSaveContent(String str) {
        if (this.r != null) {
            int i = 0;
            while (i < this.r.size()) {
                g gVar = (g) this.r.get(i);
                if (gVar.a().equals(str) && gVar.getStatus() == Status.RUNNING) {
                    gVar.cancel(true);
                    return;
                } else {
                    i++;
                }
            }
        }
    }

    public void clearView() {
        e();
        super.clearView();
    }

    public void close() {
        try {
            a();
            this.g.setWebViewClosed(true);
            if (!this.A.hasMessages(ApiEventType.API_IMAI_OPEN_EMBEDDED)) {
                this.A.sendEmptyMessage(ApiEventType.API_IMAI_OPEN_EMBEDDED);
            }
        } catch (Exception e) {
            com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "Exception closing webview. Webview not initialized properly", e);
        }
    }

    public void closeExpanded() {
        this.A.sendEmptyMessage(1005);
    }

    protected void closeOpened(View view) {
        ((ViewGroup) ((Activity) getContext()).getWindow().getDecorView()).removeView(view);
        requestLayout();
    }

    public void closeResized() {
        this.A.sendEmptyMessage(1031);
    }

    public void closeVideo(String str) {
        AVPlayer videoPlayer = this.mAudioVideoController.getVideoPlayer(str);
        if (videoPlayer == null) {
            raiseError("Invalid property ID", "closeVideo");
        } else if (videoPlayer.getState() == playerState.RELEASED) {
            raiseError("Invalid player state", "closeVideo");
        } else {
            this.mAudioVideoController.videoPlayerList.remove(str);
            Message obtainMessage = this.A.obtainMessage(1012);
            obtainMessage.obj = videoPlayer;
            this.A.sendMessage(obtainMessage);
        }
    }

    public void deinit() {
        if (getStateVariable() == ViewState.EXPANDED || getStateVariable() == ViewState.EXPANDING) {
            close();
        }
    }

    public void destroy() {
        com.inmobi.commons.internal.Log.debug(Constants.RENDERING_LOG_TAG, "IMWebView: Destroy called.");
        close();
        postInHandler(new m());
    }

    public void disableCloseRegion() {
        CustomView customView = (CustomView) ((ViewGroup) getRootView()).findViewById(IMBrowserActivity.CLOSE_REGION_VIEW_ID);
        if (customView != null) {
            customView.disableView(this.u);
        }
    }

    public void disableHardwareAcceleration() {
        this.K = false;
        com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "disableHardwareAcceleration called.");
        if (VERSION.SDK_INT >= 14) {
            WrapperFunctions.disableHardwareAccl(this);
            this.mExpandController.disableEnableHardwareAccelerationForExpandWithURLView();
        }
    }

    public void doHidePlayers() {
        this.A.sendEmptyMessage(1023);
    }

    public void expand(String str, ExpandProperties expandProperties) {
        setState(ViewState.EXPANDING);
        this.mExpandController.mIsExpandUrlValid = false;
        this.isMutexAquired.set(true);
        Message obtainMessage = this.A.obtainMessage(ApiEventType.API_IMAI_PING_IN_WEB_VIEW);
        Bundle bundle = new Bundle();
        bundle.putString(EXPAND_URL, str);
        obtainMessage.setData(bundle);
        this.mExpandController.expandProperties = expandProperties;
        com.inmobi.commons.internal.Log.debug(Constants.RENDERING_LOG_TAG, "Dimensions: {" + this.mExpandController.expandProperties.x + " ," + this.mExpandController.expandProperties.y + " ," + this.mExpandController.expandProperties.width + " ," + this.mExpandController.expandProperties.height + "}");
        this.mExpandController.tempExpPropsLock = this.mExpandController.expandProperties.lockOrientation;
        this.A.sendMessage(obtainMessage);
    }

    public void fireOnDismissAdScreen() {
        if (this.mListener != null) {
            this.mListener.onDismissAdScreen();
        }
    }

    public void fireOnLeaveApplication() {
        if (this.mListener != null) {
            this.mListener.onLeaveApplication();
        }
    }

    public void fireOnShowAdScreen() {
        if (this.mListener != null && getStateVariable() == ViewState.DEFAULT && !this.mIsInterstitialAd) {
            IMBrowserActivity.requestOnAdDismiss(this.A.obtainMessage(1028));
            this.mListener.onShowAdScreen();
        }
    }

    public Activity getActivity() {
        return this.y;
    }

    public int getAudioVolume(String str) {
        AVPlayer currentAudioPlayer = this.mAudioVideoController.getCurrentAudioPlayer(str);
        if (currentAudioPlayer != null) {
            return currentAudioPlayer.getVolume();
        }
        raiseError("Invalid property ID", "getAudioVolume");
        return -1;
    }

    public String getCurrentRotation(int i) {
        String str = "-1";
        switch (i) {
            case MMAdView.TRANSITION_NONE:
                return "0";
            case MMAdView.TRANSITION_FADE:
                return "90";
            case MMAdView.TRANSITION_UP:
                return "180";
            case MMAdView.TRANSITION_DOWN:
                return "270";
            default:
                return str;
        }
    }

    public boolean getCustomClose() {
        return this.t;
    }

    public float getDensity() {
        return this.h;
    }

    public boolean getDisableCloseRegion() {
        return this.u;
    }

    public int getDismissMessage() {
        return 1028;
    }

    public Activity getExpandedActivity() {
        return this.y;
    }

    public int getIntegerCurrentRotation() {
        int displayRotation = DeviceInfo.getDisplayRotation(((WindowManager) this.y.getSystemService("window")).getDefaultDisplay());
        if (DeviceInfo.isDefOrientationLandscape(displayRotation, this.y.getResources().getDisplayMetrics().widthPixels, this.y.getResources().getDisplayMetrics().heightPixels)) {
            displayRotation++;
            if (displayRotation > 3) {
                displayRotation = 0;
            }
            if (DeviceInfo.isTablet(this.y.getApplicationContext())) {
                this.isTablet = true;
            }
        }
        return displayRotation;
    }

    public double getLastGoodKnownMicValue() {
        return this.a;
    }

    public ArrayList<String> getMRAIDUrls() {
        return this.B;
    }

    public int getOriginalIndex() {
        return this.E;
    }

    public ViewParent getOriginalParent() {
        if (this.D == null) {
            saveOriginalViewParent();
        }
        return this.D;
    }

    public String getPlacementType() {
        return this.mIsInterstitialAd ? "interstitial" : "inline";
    }

    public IMWebViewPlayableListener getPlayableListener() {
        return this.l;
    }

    public String getSize() {
        return "{ width: " + ((int) (((float) getWidth()) / this.h)) + ", " + "height: " + ((int) (((float) getHeight()) / this.h)) + "}";
    }

    public String getState() {
        return this.k.toString().toLowerCase(Locale.ENGLISH);
    }

    public ViewState getStateVariable() {
        return this.k;
    }

    public int getVideoVolume(String str) {
        AVPlayer videoPlayer = this.mAudioVideoController.getVideoPlayer(str);
        if (videoPlayer != null) {
            return videoPlayer.getVolume();
        }
        raiseError("Invalid property ID", "getVideoVolume");
        return -1;
    }

    public ViewState getViewState() {
        return this.k;
    }

    public Handler getWebviewHandler() {
        return this.A;
    }

    public void hide() {
        this.A.sendEmptyMessage(ApiEventType.API_IMAI_OPEN_EXTERNAL);
    }

    public void hideVideo(String str) {
        Message obtainMessage = this.A.obtainMessage(1013);
        Bundle bundle = new Bundle();
        bundle.putString("pid", str);
        obtainMessage.setData(bundle);
        this.A.sendMessage(obtainMessage);
    }

    public void incentCompleted(HashMap<Object, Object> hashMap) {
        Message obtainMessage = this.A.obtainMessage(1034);
        Bundle bundle = new Bundle();
        bundle.putSerializable("incent_ad_map", hashMap);
        obtainMessage.setData(bundle);
        obtainMessage.sendToTarget();
    }

    public void injectJavaScript(String str) {
        if (str != null) {
            try {
                if (str.length() < 400) {
                    com.inmobi.commons.internal.Log.debug(Constants.RENDERING_LOG_TAG, "Injecting JavaScript: " + str);
                }
                if (!g()) {
                    super.loadUrl("javascript:try{" + str + "}catch(e){}");
                }
            } catch (Exception e) {
                com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "Error injecting javascript ", e);
            }
        }
    }

    public boolean isAudioMuted(String str) {
        AVPlayer currentAudioPlayer = this.mAudioVideoController.getCurrentAudioPlayer(str);
        if (currentAudioPlayer != null) {
            return currentAudioPlayer.isMediaMuted();
        }
        raiseError("Invalid property ID", "isAudioMuted");
        return false;
    }

    public boolean isBusy() {
        return this.s;
    }

    public boolean isConfigChangesListed(Activity activity) {
        int i = VERSION.SDK_INT;
        int a = a(activity);
        boolean z;
        if ((a & 16) == 0 || (a & 32) == 0 || (a & 128) == 0) {
            z = false;
        } else {
            z = true;
        }
        boolean z2;
        if (i < 13 || !((a & 1024) == 0 || (a & 2048) == 0)) {
            z2 = true;
        } else {
            z2 = false;
        }
        return (i == 0 || i == 0) ? false : true;
    }

    public boolean isEnabledHardwareAcceleration() {
        return this.K;
    }

    public boolean isExpanded() {
        return this.k == ViewState.EXPANDED;
    }

    public boolean isLandscapeSyncOrientation(int i) {
        return i == 1 || i == 3;
    }

    public boolean isModal() {
        return this.mIsInterstitialAd || this.k == ViewState.EXPANDED;
    }

    public boolean isPageFinished() {
        return this.e;
    }

    public boolean isPortraitSyncOrientation(int i) {
        return i == 0 || i == 2;
    }

    public boolean isVideoMuted(String str) {
        AVPlayer videoPlayer = this.mAudioVideoController.getVideoPlayer(str);
        if (videoPlayer != null) {
            return videoPlayer.isMediaMuted();
        }
        raiseError("Invalid property ID", "isVideoMuted");
        return false;
    }

    public boolean isViewable() {
        return this.mIsViewable;
    }

    public void loadData(String str, String str2, String str3) {
        this.f = false;
        super.loadData(str, str2, str3);
    }

    public void loadDataWithBaseURL(String str, String str2, String str3, String str4, String str5) {
        this.f = false;
        if (this.k != ViewState.EXPANDED) {
            d();
            super.loadDataWithBaseURL(str, str2, str3, str4, str5);
        }
    }

    public void loadUrl(String str) {
        this.f = false;
        if (this.k != ViewState.EXPANDED) {
            d();
            super.loadUrl(str);
        }
    }

    public void lockExpandOrientation(Activity activity, boolean z, String str) {
        try {
            if (isConfigChangesListed(activity)) {
                int requestedOrientation = activity.getRequestedOrientation();
                if (requestedOrientation != 0 && requestedOrientation != 1) {
                    if (VERSION.SDK_INT >= 9 && (requestedOrientation == 8 || requestedOrientation == 9 || requestedOrientation == 6 || requestedOrientation == 7)) {
                        return;
                    }
                    if (!z) {
                        requestedOrientation = getIntegerCurrentRotation();
                        this.mExpandController.initialExpandOrientation = activity.getRequestedOrientation();
                        if (str.equalsIgnoreCase("portrait")) {
                            this.mExpandController.useLockOrient = true;
                            activity.setRequestedOrientation(WrapperFunctions.getParamPortraitOrientation(requestedOrientation));
                        } else if (str.equalsIgnoreCase("landscape")) {
                            this.mExpandController.useLockOrient = true;
                            activity.setRequestedOrientation(WrapperFunctions.getParamLandscapeOrientation(requestedOrientation));
                        } else {
                            this.mExpandController.useLockOrient = true;
                            if (activity.getResources().getConfiguration().orientation == 2) {
                                com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "In allowFalse, none mode dev orientation:ORIENTATION_LANDSCAPE");
                                activity.setRequestedOrientation(0);
                            } else {
                                com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "In allowFalse, none mode dev orientation:ORIENTATION_PORTRAIT");
                                activity.setRequestedOrientation(1);
                            }
                        }
                    } else if (activity.getResources().getConfiguration().orientation == 2) {
                        com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "In allow true,  device orientation:ORIENTATION_LANDSCAPE");
                    } else {
                        com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "In allow true,  device orientation:ORIENTATION_PORTRAIT");
                    }
                }
            }
        } catch (Exception e) {
            com.inmobi.commons.internal.Log.debug(Constants.RENDERING_LOG_TAG, "Exception handling the orientation ", e);
        }
    }

    public void mediaPlayerReleased(AVPlayer aVPlayer) {
        this.mAudioVideoController.mediaPlayerReleased(aVPlayer);
    }

    public void muteAudio(String str) {
        AVPlayer currentAudioPlayer = this.mAudioVideoController.getCurrentAudioPlayer(str);
        if (currentAudioPlayer == null) {
            raiseError("Invalid property ID", "muteAudio");
        } else if (currentAudioPlayer.getState() == playerState.RELEASED) {
            raiseError("Invalid player state", "muteAudio");
        } else {
            Message obtainMessage = this.A.obtainMessage(1019);
            Bundle bundle = new Bundle();
            bundle.putString("aplayerref", str);
            obtainMessage.setData(bundle);
            obtainMessage.sendToTarget();
        }
    }

    public void muteVideo(String str) {
        AVPlayer videoPlayer = this.mAudioVideoController.getVideoPlayer(str);
        if (videoPlayer == null) {
            raiseError("Invalid property ID", "muteVideo");
        } else if (videoPlayer.getState() == playerState.RELEASED || videoPlayer.getState() == playerState.INIT) {
            raiseError("Invalid player state", "muteVideo");
        } else {
            Message obtainMessage = this.A.obtainMessage(1015);
            obtainMessage.obj = videoPlayer;
            this.A.sendMessage(obtainMessage);
        }
    }

    protected void onAttachedToWindow() {
        com.inmobi.commons.internal.Log.debug(Constants.RENDERING_LOG_TAG, "IMWebView-> onAttachedToWindow");
        saveOriginalViewParent();
        if (!this.v) {
            LayoutParams layoutParams = getLayoutParams();
            this.i = layoutParams.height;
            this.j = layoutParams.width;
            this.v = true;
        }
        this.g.registerBroadcastListener();
        super.onAttachedToWindow();
    }

    protected void onDetachedFromWindow() {
        com.inmobi.commons.internal.Log.debug(Constants.RENDERING_LOG_TAG, "IMWebView-> onDetatchedFromWindow");
        this.g.stopAllListeners();
        this.B.clear();
        this.g.unRegisterBroadcastListener();
        if (this.mIsInterstitialAd && !this.mWebViewIsBrowserActivity) {
            this.mInterstitialController.handleInterstitialClose();
        }
        super.onDetachedFromWindow();
    }

    protected void onIMWebviewVisibilityChanged(boolean z) {
        if (this.mIsViewable != z) {
            this.mIsViewable = z;
            if (!this.doNotFireVisibilityChanged.get()) {
                a(z);
            }
        }
    }

    public void onOrientationEventChange() {
        this.A.sendEmptyMessage(1027);
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (i != 0 && i2 != 0) {
            if (!this.F) {
                a((int) (((float) i) / getDensity()), (int) (((float) i2) / getDensity()));
            }
            this.F = false;
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        requestFocus();
        return super.onTouchEvent(motionEvent);
    }

    protected void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        onIMWebviewVisibilityChanged(i == 0);
        if (i != 0) {
            try {
                if (this.g.supports("vibrate")) {
                    ((Vibrator) this.y.getSystemService("vibrator")).cancel();
                }
            } catch (Exception e) {
                com.inmobi.commons.internal.Log.internal(Constants.RENDERING_LOG_TAG, "Failed to cancel existing vibration", e);
            }
        }
    }

    public void openExternal(String str) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(str));
        intent.addFlags(DriveFile.MODE_READ_ONLY);
        try {
            getContext().startActivity(intent);
            if (this.mListener != null) {
                this.mListener.onLeaveApplication();
            }
        } catch (Exception e) {
            raiseError("Request must specify a valid URL", "openExternal");
        }
    }

    public void openURL(String str) {
        if (isViewable()) {
            Message obtainMessage = this.A.obtainMessage(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
            Bundle bundle = new Bundle();
            bundle.putString(EXPAND_URL, str);
            obtainMessage.setData(bundle);
            this.A.sendMessage(obtainMessage);
        } else {
            raiseError("Cannot open URL.Ad is not viewable yet", "openURL");
        }
    }

    public void pageFinishedCallbackForAdCreativeTesting(Message message) {
        this.x = message;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void pauseAudio(java.lang.String r4) {
        throw new UnsupportedOperationException("Method not decompiled: com.inmobi.re.container.IMWebView.pauseAudio(java.lang.String):void");
        /*
        r3 = this;
        r0 = r3.mAudioVideoController;
        r0 = r0.getCurrentAudioPlayer(r4);
        if (r0 != 0) goto L_0x0010;
    L_0x0008:
        r0 = "Invalid property ID";
        r1 = "pauseAudio";
        r3.raiseError(r0, r1);
    L_0x000f:
        return;
    L_0x0010:
        r1 = r0.getState();
        r2 = com.inmobi.re.controller.util.AVPlayer.playerState.PLAYING;
        if (r1 == r2) goto L_0x0033;
    L_0x0018:
        r1 = r0.getState();
        r2 = com.inmobi.re.controller.util.AVPlayer.playerState.INIT;
        if (r1 != r2) goto L_0x002b;
    L_0x0020:
        r1 = r0.isPrepared();
        if (r1 != 0) goto L_0x002b;
    L_0x0026:
        r1 = 0;
        r0.setAutoPlay(r1);
        goto L_0x000f;
    L_0x002b:
        r0 = "Invalid player state";
        r1 = "pauseAudio";
        r3.raiseError(r0, r1);
        goto L_0x000f;
    L_0x0033:
        r0 = r0.isPlaying();
        if (r0 == 0) goto L_0x000f;
    L_0x0039:
        r0 = r3.A;
        r1 = 1010; // 0x3f2 float:1.415E-42 double:4.99E-321;
        r0 = r0.obtainMessage(r1);
        r1 = new android.os.Bundle;
        r1.<init>();
        r2 = "aplayerref";
        r1.putString(r2, r4);
        r0.setData(r1);
        r0.sendToTarget();
        goto L_0x000f;
        */
    }

    public void pauseVideo(String str) {
        Message obtainMessage = this.A.obtainMessage(1011);
        Bundle bundle = new Bundle();
        bundle.putString("pid", str);
        obtainMessage.setData(bundle);
        this.A.sendMessage(obtainMessage);
    }

    public void playAudio(String str, boolean z, boolean z2, boolean z3, String str2, String str3, String str4) {
        synchronized (this.mutex) {
            if (this.isMutexAquired.get()) {
                try {
                    this.mutex.wait();
                } catch (InterruptedException e) {
                    com.inmobi.commons.internal.Log.debug(Constants.RENDERING_LOG_TAG, "mutex failed ", e);
                }
            }
        }
        if (!this.mIsInterstitialAd && this.k != ViewState.EXPANDED) {
            raiseError("Cannot play audio.Ad is not in an expanded state", "playAudio");
        } else if (isViewable()) {
            Parcelable playerProperties = new PlayerProperties();
            playerProperties.setProperties(false, z, z2, z3, str2, str3, str4);
            Bundle bundle = new Bundle();
            bundle.putString(EXPAND_URL, str);
            bundle.putParcelable(PLAYER_PROPERTIES, playerProperties);
            Message obtainMessage = this.A.obtainMessage(1007);
            obtainMessage.setData(bundle);
            this.A.sendMessage(obtainMessage);
        } else {
            raiseError("Cannot play audio.Ad is not viewable yet", "playAudio");
        }
    }

    public void playVideo(String str, boolean z, boolean z2, boolean z3, boolean z4, Dimensions dimensions, String str2, String str3, String str4) {
        synchronized (this.mutex) {
            if (this.isMutexAquired.get()) {
                try {
                    this.mutex.wait();
                } catch (InterruptedException e) {
                    com.inmobi.commons.internal.Log.debug(Constants.RENDERING_LOG_TAG, "mutex failed ", e);
                }
            }
        }
        if (!this.mIsInterstitialAd && this.k != ViewState.EXPANDED) {
            raiseError("Cannot play video.Ad is not in an expanded state", "playVideo");
        } else if (!isViewable()) {
            raiseError("Cannot play video.Ad is not viewable yet", "playVideo");
        } else if (this.mAudioVideoController.videoPlayerList.isEmpty() || this.mAudioVideoController.videoPlayerList.size() < 5 || this.mAudioVideoController.videoPlayerList.containsKey(str4)) {
            Message obtainMessage = this.A.obtainMessage(1006);
            Parcelable playerProperties = new PlayerProperties();
            playerProperties.setProperties(z, z2, z3, z4, str2, str3, str4);
            Bundle bundle = new Bundle();
            bundle.putString(EXPAND_URL, str);
            bundle.putParcelable(PLAYER_PROPERTIES, playerProperties);
            com.inmobi.commons.internal.Log.debug(Constants.RENDERING_LOG_TAG, "Before validation dimension: (" + dimensions.x + ", " + dimensions.y + ", " + dimensions.width + ", " + dimensions.height + ")");
            this.mAudioVideoController.validateVideoDimensions(dimensions);
            com.inmobi.commons.internal.Log.debug(Constants.RENDERING_LOG_TAG, "After validation dimension: (" + dimensions.x + ", " + dimensions.y + ", " + dimensions.width + ", " + dimensions.height + ")");
            bundle.putParcelable(DIMENSIONS, dimensions);
            obtainMessage.setData(bundle);
            this.A.sendMessage(obtainMessage);
        } else {
            raiseError("Player Error. Exceeding permissible limit for saved play instances", "playVideo");
        }
    }

    public void postInHandler(Runnable runnable) {
        this.A.post(runnable);
    }

    public void postInjectJavaScript(String str) {
        if (str != null) {
            if (str.length() < 400) {
                com.inmobi.commons.internal.Log.debug(Constants.RENDERING_LOG_TAG, "Injecting JavaScript: " + str);
            }
            Message obtainMessage = this.A.obtainMessage(1025);
            Bundle bundle = new Bundle();
            bundle.putString("injectMessage", str);
            obtainMessage.setData(bundle);
            obtainMessage.sendToTarget();
        }
    }

    public void raiseCameraPictureCapturedEvent(String str, int i, int i2) {
        String str2 = "window.mraidview.fireCameraPictureCatpturedEvent('" + str + "'" + "," + "'" + i + "','" + i2 + "')";
        if (this.y != null) {
            this.y.runOnUiThread(new j(str2));
        }
    }

    public void raiseError(String str, String str2) {
        Message obtainMessage = this.A.obtainMessage(1008);
        Bundle bundle = new Bundle();
        bundle.putString("message", str);
        bundle.putString(OrmmaView.ACTION_KEY, str2);
        obtainMessage.setData(bundle);
        this.A.sendMessage(obtainMessage);
    }

    public void raiseGalleryImageSelectedEvent(String str, int i, int i2) {
        String str2 = "window.mraidview.fireGalleryImageSelectedEvent('" + str + "'" + "," + "'" + i + "','" + i2 + "')";
        if (this.y != null) {
            this.y.runOnUiThread(new l(str2));
        }
    }

    public void raiseMicEvent(double d) {
        this.a = d;
        String str = "window.mraid.broadcastEvent('micIntensityChange'," + d + ")";
        if (this.y != null) {
            this.y.runOnUiThread(new a(str));
        }
    }

    public void raiseVibrateCompleteEvent() {
        if (this.y != null) {
            this.y.runOnUiThread(new k());
        }
    }

    public void requestOnInterstitialClosed(Message message) {
        this.mInterstitialController.mMsgOnInterstitialClosed = message;
    }

    public void requestOnInterstitialShown(Message message) {
        this.mInterstitialController.mMsgOnInterstitialShown = message;
    }

    public void resetLayout() {
        LayoutParams layoutParams = getLayoutParams();
        if (this.v) {
            layoutParams.height = this.i;
            layoutParams.width = this.j;
        }
        setVisibility(0);
        requestLayout();
    }

    public void resetMraid() {
        this.mExpandController.reset();
        this.mResizeController.reset();
        this.g.reset();
    }

    public void resize(ResizeProperties resizeProperties) {
        setState(ViewState.RESIZING);
        this.isMutexAquired.set(true);
        Message obtainMessage = this.A.obtainMessage(1030);
        this.mResizeController.resizeProperties = resizeProperties;
        this.A.sendMessage(obtainMessage);
    }

    public void saveFile(File file, String str, String str2) {
        new g(file, str, str2).execute(new Void[0]);
    }

    public void saveOriginalViewParent() {
        if (this.D == null) {
            this.D = getParent();
            if (this.D != null) {
                int childCount = ((ViewGroup) this.D).getChildCount();
                int i = 0;
                while (i < childCount && ((ViewGroup) this.D).getChildAt(i) != this) {
                    i++;
                }
                this.E = i;
            }
        }
    }

    public void seekAudio(String str, int i) {
        AVPlayer currentAudioPlayer = this.mAudioVideoController.getCurrentAudioPlayer(str);
        if (currentAudioPlayer == null) {
            raiseError("Invalid property ID", "seekAudio");
        } else if (currentAudioPlayer.getState() == playerState.RELEASED) {
            raiseError("Invalid player state", "seekAudio");
        } else {
            Message obtainMessage = this.A.obtainMessage(1022);
            Bundle bundle = new Bundle();
            bundle.putInt("seekaudio", i);
            obtainMessage.setData(bundle);
            obtainMessage.obj = currentAudioPlayer;
            obtainMessage.sendToTarget();
        }
    }

    public void seekVideo(String str, int i) {
        AVPlayer videoPlayer = this.mAudioVideoController.getVideoPlayer(str);
        if (videoPlayer == null) {
            raiseError("Invalid property ID", "seekVideo");
        } else if (videoPlayer.getState() == playerState.RELEASED || videoPlayer.getState() == playerState.INIT) {
            raiseError("Invalid player state", "seekVideo");
        } else {
            Message obtainMessage = this.A.obtainMessage(1018);
            Bundle bundle = new Bundle();
            bundle.putInt("seek", i);
            obtainMessage.setData(bundle);
            obtainMessage.obj = videoPlayer;
            this.A.sendMessage(obtainMessage);
        }
    }

    public void sendToCPHandler() {
        this.A.sendEmptyMessage(1029);
    }

    public void sendToDPHandler() {
        this.A.sendEmptyMessage(1032);
    }

    public void sendasyncPing(String str) {
        this.g.asyncPing(str);
    }

    public void setActivity(Activity activity) {
        this.y = activity;
    }

    public void setAudioVolume(String str, int i) {
        if (this.mAudioVideoController.getCurrentAudioPlayer(str) == null) {
            raiseError("Invalid property ID", "setAudioVolume");
        } else {
            Message obtainMessage = this.A.obtainMessage(1021);
            Bundle bundle = new Bundle();
            bundle.putInt("vol", i);
            bundle.putString("aplayerref", str);
            obtainMessage.setData(bundle);
            obtainMessage.sendToTarget();
        }
    }

    public void setBrowserActivity(Activity activity) {
        if (activity != null) {
            this.y = (IMBrowserActivity) activity;
        }
    }

    public void setBusy(boolean z) {
        this.s = z;
    }

    public void setCallBack(InstantVideoCallbackCallback instantVideoCallbackCallback) {
        this.c = instantVideoCallbackCallback;
    }

    public void setCloseButton() {
        try {
            CustomView customView = (CustomView) ((ViewGroup) getRootView()).findViewById(IMBrowserActivity.CLOSE_BUTTON_VIEW_ID);
            if (customView != null) {
                customView.setVisibility(getCustomClose() ? ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES : 0);
            }
        } catch (Exception e) {
        }
    }

    public void setCustomClose(boolean z) {
        this.t = z;
        this.A.sendMessage(this.A.obtainMessage(1009));
    }

    public void setDisableCloseRegion(boolean z) {
        this.u = z;
        this.A.sendMessage(this.A.obtainMessage(1035));
    }

    public void setExpandPropertiesForInterstitial(boolean z, boolean z2, String str) {
        setCustomClose(z);
        this.mInterstitialController.orientationValueForInterstitial = str;
        this.mInterstitialController.lockOrientationValueForInterstitial = z2;
        if (this.mWebViewIsBrowserActivity) {
            this.mExpandController.handleOrientationFor2Piece();
        }
        if (isViewable() && this.mIsInterstitialAd) {
            this.A.sendEmptyMessage(1026);
        }
    }

    public void setExternalWebViewClient(WebViewClient webViewClient) {
        this.z = webViewClient;
    }

    public void setOrientationPropertiesForInterstitial(boolean z, String str) {
        this.mInterstitialController.orientationValueForInterstitial = str;
        this.mInterstitialController.lockOrientationValueForInterstitial = z;
        if (this.mWebViewIsBrowserActivity) {
            this.mExpandController.handleOrientationFor2Piece();
        }
        if (isViewable() && this.mIsInterstitialAd) {
            this.A.sendEmptyMessage(1026);
        }
    }

    public void setOriginalParent(ViewParent viewParent) {
        this.D = viewParent;
    }

    public void setPlayableListener(IMWebViewPlayableListener iMWebViewPlayableListener) {
        this.l = iMWebViewPlayableListener;
    }

    public void setState(ViewState viewState) {
        com.inmobi.commons.internal.Log.debug(Constants.RENDERING_LOG_TAG, "State changing from " + this.k + " to " + viewState);
        this.k = viewState;
        if (viewState != ViewState.EXPANDING && viewState != ViewState.RESIZING) {
            injectJavaScript("window.mraid.broadcastEvent('stateChange','" + getState() + "');");
        }
    }

    public void setVideoVolume(String str, int i) {
        AVPlayer videoPlayer = this.mAudioVideoController.getVideoPlayer(str);
        if (videoPlayer == null) {
            raiseError("Invalid property ID", "setVideoVolume");
        } else if (videoPlayer.getState() == playerState.RELEASED) {
            raiseError("Invalid player state", "setVideoVolume");
        } else {
            Message obtainMessage = this.A.obtainMessage(1017);
            Bundle bundle = new Bundle();
            bundle.putInt("volume", i);
            obtainMessage.setData(bundle);
            obtainMessage.obj = videoPlayer;
            this.A.sendMessage(obtainMessage);
        }
    }

    public void show() {
        this.A.sendEmptyMessage(ApiEventType.API_IMAI_PING);
    }

    public void showVideo(String str) {
        Message obtainMessage = this.A.obtainMessage(1014);
        Bundle bundle = new Bundle();
        bundle.putString("pid", str);
        obtainMessage.setData(bundle);
        this.A.sendMessage(obtainMessage);
    }

    public void unMuteAudio(String str) {
        AVPlayer currentAudioPlayer = this.mAudioVideoController.getCurrentAudioPlayer(str);
        if (currentAudioPlayer == null) {
            raiseError("Invalid property ID", "unmuteAudio");
        } else if (currentAudioPlayer.getState() == playerState.RELEASED) {
            raiseError("Invalid player state", "unmuteAudio");
        } else {
            Message obtainMessage = this.A.obtainMessage(1020);
            Bundle bundle = new Bundle();
            bundle.putString("aplayerref", str);
            obtainMessage.setData(bundle);
            obtainMessage.sendToTarget();
        }
    }

    public void unMuteVideo(String str) {
        AVPlayer videoPlayer = this.mAudioVideoController.getVideoPlayer(str);
        if (videoPlayer == null) {
            raiseError("Invalid property ID", "unMuteVideo");
        } else if (videoPlayer.getState() == playerState.RELEASED || videoPlayer.getState() == playerState.INIT) {
            raiseError("Invalid player state", "unMuteVideo");
        } else {
            Message obtainMessage = this.A.obtainMessage(1016);
            obtainMessage.obj = videoPlayer;
            this.A.sendMessage(obtainMessage);
        }
    }

    public void userInteraction(HashMap<String, String> hashMap) {
        Message obtainMessage = this.A.obtainMessage(1033);
        Bundle bundle = new Bundle();
        bundle.putSerializable("map", hashMap);
        obtainMessage.setData(bundle);
        obtainMessage.sendToTarget();
    }
}