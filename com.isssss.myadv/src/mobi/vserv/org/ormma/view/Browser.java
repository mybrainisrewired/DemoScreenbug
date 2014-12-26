package mobi.vserv.org.ormma.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.media.TransportMediator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.inmobi.androidsdk.IMBrowserActivity;
import com.inmobi.commons.analytics.db.AnalyticsEvent;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import mobi.vserv.android.ads.VservConstants;
import mobi.vserv.org.ormma.controller.Defines;

public class Browser extends Activity implements VservConstants {
    public static final String EXIT_URL = "exitUrl";
    public static final String IS_SHARED = "isShared";
    public static final String SHARE_URL = "shareUrl";
    public static final String SHOW_BACK_EXTRA = "open_show_back";
    public static final String SHOW_FORWARD_EXTRA = "open_show_forward";
    public static final String SHOW_REFRESH_EXTRA = "open_show_refresh";
    public static final String SHOW_SNS_WIDGET = "snsPageLoaded";
    public static final String SUCCESS_MSG = "successMsg";
    public static final String URL_EXTRA = "extra_url";
    private ImageButton backButton;
    private ImageView closeButtonImage;
    private boolean didAppSetOrientation;
    private ImageButton forwardButton;
    private ProgressBar progressBar;
    private ImageButton refreshButton;
    private ImageButton rotateDeviceButton;
    private WebView webView;

    public Browser() {
        this.didAppSetOrientation = false;
    }

    private void closeCurrentActivity(boolean shouldClose) {
        if (Defines.ENABLE_lOGGING) {
            Log.i("vserv", new StringBuilder("closeCurrentActivity(").append(shouldClose).append(")").toString());
        }
        if (getIntent() != null && getIntent().hasExtra("start_from_ad_activity") && getIntent().getBooleanExtra("start_from_ad_activity", false)) {
            if (this.webView != null) {
                this.webView.clearHistory();
                this.webView.clearDisappearingChildren();
                this.webView.stopLoading();
                this.webView.removeAllViews();
                onStop();
                this.webView = null;
            }
            System.gc();
        } else {
            Intent intent = new Intent("mobi.vserv.ad.dismiss_screen");
            if (!shouldClose) {
                intent.putExtra(Event.INTENT_EXTERNAL_BROWSER, "close browser only");
            }
            if (this.webView != null) {
                this.webView.clearHistory();
                this.webView.clearDisappearingChildren();
                this.webView.stopLoading();
                this.webView.removeAllViews();
                onStop();
                onDestroy();
                this.webView = null;
            }
            System.gc();
            sendBroadcast(intent);
        }
        onDestroy();
        finish();
    }

    private InputStream makeHttpConnection(String requestUrl) {
        try {
            URLConnection conn = new URL(requestUrl).openConnection();
            if (conn instanceof HttpURLConnection) {
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                httpConn.getResponseCode();
                return null;
            } else {
                throw new IOException("Not an HTTP connection");
            }
        } catch (Exception e) {
        }
    }

    public void finish() {
        super.finish();
    }

    @SuppressLint({"SetJavaScriptEnabled", "ResourceAsColor", "InlinedApi"})
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(getResources().getIdentifier("vserv_web_layout", "layout", getPackageName()));
        getWindow().addFlags(TransportMediator.FLAG_KEY_MEDIA_NEXT);
        this.progressBar = (ProgressBar) findViewById(getResources().getIdentifier("progressBar", AnalyticsEvent.EVENT_ID, getPackageName()));
        this.closeButtonImage = (ImageView) findViewById(getResources().getIdentifier("closeButton", AnalyticsEvent.EVENT_ID, getPackageName()));
        this.webView = (WebView) findViewById(getResources().getIdentifier("webView", AnalyticsEvent.EVENT_ID, getPackageName()));
        this.backButton = (ImageButton) findViewById(getResources().getIdentifier("backButton", AnalyticsEvent.EVENT_ID, getPackageName()));
        this.forwardButton = (ImageButton) findViewById(getResources().getIdentifier("forwardButton", AnalyticsEvent.EVENT_ID, getPackageName()));
        this.refreshButton = (ImageButton) findViewById(getResources().getIdentifier("refreshButton", AnalyticsEvent.EVENT_ID, getPackageName()));
        this.rotateDeviceButton = (ImageButton) findViewById(getResources().getIdentifier("rotateDeviceButton", AnalyticsEvent.EVENT_ID, getPackageName()));
        Intent i = getIntent();
        if (!i.getExtras().containsKey("adOrientation") || i.getStringExtra("adOrientation") == null) {
            SharedPreferences orientationPreference;
            if (VERSION.SDK_INT >= 11) {
                orientationPreference = getSharedPreferences("vserv_orientation", MMAdView.TRANSITION_RANDOM);
            } else {
                orientationPreference = getSharedPreferences("vserv_orientation", 0);
            }
            if (orientationPreference.getString("orientation", Preconditions.EMPTY_ARGUMENTS).equals("landscape")) {
                setRequestedOrientation(0);
                this.rotateDeviceButton.setImageResource(getResources().getIdentifier("vserv_rotate_to_portrait", "drawable", getPackageName()));
                this.rotateDeviceButton.setTag("dest_portrait");
                if (Defines.ENABLE_lOGGING) {
                    Log.i("orientation", "Setting requested orientation to landscape");
                }
                this.didAppSetOrientation = true;
            } else if (orientationPreference.getString("orientation", Preconditions.EMPTY_ARGUMENTS).equals("portrait")) {
                setRequestedOrientation(1);
                this.rotateDeviceButton.setImageResource(getResources().getIdentifier("vserv_rotate_to_landscape", "drawable", getPackageName()));
                this.rotateDeviceButton.setTag("dest_landscape");
                if (Defines.ENABLE_lOGGING) {
                    Log.i("orientation", "Setting requested orientation to portrait");
                }
                this.didAppSetOrientation = true;
            } else if (Defines.ENABLE_lOGGING) {
                Log.i("orientation", "Not Setting requested orientation");
            }
        } else {
            String t_orientation = i.getStringExtra("adOrientation");
            if (t_orientation.equals("landscape")) {
                setRequestedOrientation(0);
                this.rotateDeviceButton.setImageResource(getResources().getIdentifier("vserv_rotate_to_portrait", "drawable", getPackageName()));
                this.rotateDeviceButton.setTag("dest_portrait");
                if (Defines.ENABLE_lOGGING) {
                    Log.i("orientation", "Setting requested orientation to landscape");
                }
                this.didAppSetOrientation = true;
            } else if (t_orientation.equals("portrait")) {
                setRequestedOrientation(1);
                this.rotateDeviceButton.setImageResource(getResources().getIdentifier("vserv_rotate_to_landscape", "drawable", getPackageName()));
                this.rotateDeviceButton.setTag("dest_landscape");
                if (Defines.ENABLE_lOGGING) {
                    Log.i("orientation", "Setting requested orientation to portrait");
                }
                this.didAppSetOrientation = true;
            } else if (Defines.ENABLE_lOGGING) {
                Log.i("orientation", "Not Setting requested orientation");
            }
        }
        this.closeButtonImage.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Browser.this.setResult(IMBrowserActivity.INTERSTITIAL_ACTIVITY, Browser.this.getIntent());
                Browser.this.closeCurrentActivity(true);
            }
        });
        if (!i.getBooleanExtra(SHOW_BACK_EXTRA, true)) {
            this.backButton.setVisibility(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
        }
        this.backButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                if (Browser.this.webView.canGoBack()) {
                    Browser.this.webView.goBack();
                } else {
                    Browser.this.closeCurrentActivity(false);
                }
            }
        });
        if (!i.getBooleanExtra(SHOW_FORWARD_EXTRA, true)) {
            this.forwardButton.setVisibility(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
        }
        this.forwardButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Browser.this.webView.goForward();
            }
        });
        if (!i.getBooleanExtra(SHOW_REFRESH_EXTRA, true)) {
            this.refreshButton.setVisibility(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
        }
        this.refreshButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Browser.this.webView.reload();
            }
        });
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.loadUrl(i.getStringExtra(URL_EXTRA));
        this.webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Builder alertDialogBuilder;
                if (Browser.this.getIntent().getBooleanExtra(IS_SHARED, false) || url.indexOf("http://sns.vserv.mobi/sns/getWidget.php?") == -1) {
                    if (view.canGoForward()) {
                        Browser.this.forwardButton.setImageResource(Browser.this.getResources().getIdentifier("vserv_browser_rightarrow", "drawable", Browser.this.getPackageName()));
                    } else {
                        Browser.this.forwardButton.setImageResource(Browser.this.getResources().getIdentifier("vserv_browser_unrightarrow", "drawable", Browser.this.getPackageName()));
                    }
                    if (Browser.this.getIntent().getBooleanExtra(SHOW_SNS_WIDGET, false)) {
                        String[] allExitUrls = Browser.this.getIntent().getStringArrayExtra(EXIT_URL);
                        int index = 0;
                        while (allExitUrls != null && index < allExitUrls.length) {
                            if (Browser.this.getIntent().getBooleanExtra(IS_SHARED, false) || allExitUrls[index].trim().length() <= 0 || url.indexOf(allExitUrls[index].trim()) == -1) {
                                index++;
                            } else {
                                Browser.this.getIntent().putExtra(IS_SHARED, true);
                                Browser.this.makeHttpConnection(Browser.this.getIntent().getStringExtra(SHARE_URL));
                                alertDialogBuilder = new Builder(Browser.this);
                                alertDialogBuilder.setMessage(Browser.this.getIntent().getStringExtra(SUCCESS_MSG));
                                alertDialogBuilder.setCancelable(false);
                                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        AnonymousClass_5.this.this$0.closeCurrentActivity(false);
                                    }
                                });
                                alertDialogBuilder.show();
                                return;
                            }
                        }
                    }
                } else {
                    Browser.this.getIntent().putExtra(IS_SHARED, true);
                    Browser.this.makeHttpConnection(Browser.this.getIntent().getStringExtra(SHARE_URL));
                    alertDialogBuilder = new Builder(Browser.this);
                    alertDialogBuilder.setMessage(Browser.this.getIntent().getStringExtra(SUCCESS_MSG));
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            AnonymousClass_5.this.this$0.closeCurrentActivity(false);
                        }
                    });
                    alertDialogBuilder.show();
                }
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Browser.this.forwardButton.setImageResource(Browser.this.getResources().getIdentifier("vserv_browser_unrightarrow", "drawable", Browser.this.getPackageName()));
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        this.webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                Browser.this.progressBar.setProgress(progress);
                if (progress == 100) {
                    Browser.this.progressBar.setVisibility(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
                }
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        Log.i("vserv", "onKeyDown");
        if (this.webView.canGoBack()) {
            Log.i("vserv", "onKeyDown1");
            this.webView.goBack();
        } else {
            Log.i("vserv", "onKeyDown2");
            closeCurrentActivity(false);
        }
        return true;
    }

    protected void onPause() {
        super.onPause();
        CookieSyncManager.getInstance().stopSync();
    }

    protected void onResume() {
        super.onResume();
        if (!this.didAppSetOrientation) {
            if ((getResources().getConfiguration().orientation == 1 ? "p" : "l").equals("p")) {
                this.rotateDeviceButton.setImageResource(getResources().getIdentifier("vserv_rotate_to_landscape", "drawable", getPackageName()));
                this.rotateDeviceButton.setTag("dest_landscape");
            } else {
                this.rotateDeviceButton.setImageResource(getResources().getIdentifier("vserv_rotate_to_portrait", "drawable", getPackageName()));
                this.rotateDeviceButton.setTag("dest_portrait");
            }
        }
        this.rotateDeviceButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (Defines.ENABLE_lOGGING) {
                    Log.i("vserv", "Clicked on rotateDeviceButton");
                }
                if (Browser.this.rotateDeviceButton.getTag().equals("dest_landscape")) {
                    Browser.this.setRequestedOrientation(0);
                    Browser.this.rotateDeviceButton.setImageResource(Browser.this.getResources().getIdentifier("vserv_rotate_to_portrait", "drawable", Browser.this.getPackageName()));
                    Browser.this.rotateDeviceButton.setTag("dest_portrait");
                    if (Defines.ENABLE_lOGGING) {
                        Log.i("vserv", "Switching to portrait");
                    }
                } else {
                    Browser.this.setRequestedOrientation(1);
                    Browser.this.rotateDeviceButton.setImageResource(Browser.this.getResources().getIdentifier("vserv_rotate_to_landscape", "drawable", Browser.this.getPackageName()));
                    Browser.this.rotateDeviceButton.setTag("dest_landscape");
                    if (Defines.ENABLE_lOGGING) {
                        Log.i("vserv", "Switching to landscape");
                    }
                }
            }
        });
        CookieSyncManager.getInstance().startSync();
    }

    protected void onStop() {
        this.didAppSetOrientation = false;
        super.onStop();
    }
}