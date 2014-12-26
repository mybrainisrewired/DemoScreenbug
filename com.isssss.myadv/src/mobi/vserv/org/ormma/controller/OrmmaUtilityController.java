package mobi.vserv.org.ormma.controller;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import com.google.android.gms.drive.DriveFile;
import com.inmobi.commons.analytics.iat.impl.AdTrackerConstants;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import mobi.vserv.android.ads.VservAdManager;
import mobi.vserv.org.ormma.controller.Defines.Events;
import mobi.vserv.org.ormma.view.OrmmaView;

public class OrmmaUtilityController extends OrmmaController {
    public String cachePostActionUrl;
    public String calEvent;
    private Context context;
    private BroadcastReceiver installReceiver;
    private int iscachereq;
    private boolean isdisplayingCacheAD;
    private double lengthOfResponse;
    private OrmmaAssetController mAssetController;
    private OrmmaDisplayController mDisplayController;
    private OrmmaLocationController mLocationController;
    private OrmmaNetworkController mNetworkController;
    private OrmmaSensorController mSensorController;
    private String packageName;
    private boolean showDialog;
    private String url;
    private VservAdManager vservAdManageractivity;
    private OrmmaView webview;

    public OrmmaUtilityController(OrmmaView adView, Context context, int cachereq) {
        super(adView, context);
        this.url = null;
        this.lengthOfResponse = 0.0d;
        this.iscachereq = 0;
        this.isdisplayingCacheAD = false;
        this.cachePostActionUrl = null;
        this.calEvent = Preconditions.EMPTY_ARGUMENTS;
        this.context = context;
        this.webview = adView;
        this.iscachereq = cachereq;
        this.mAssetController = new OrmmaAssetController(adView, context);
        this.mDisplayController = new OrmmaDisplayController(adView, context);
        this.mLocationController = new OrmmaLocationController(adView, context);
        this.mNetworkController = new OrmmaNetworkController(adView, context);
        this.mSensorController = new OrmmaSensorController(adView, context);
        adView.addJavascriptInterface(this.mAssetController, new StringBuilder(String.valueOf("ORMMA")).append("AssetsControllerBridge").toString());
        adView.addJavascriptInterface(this.mDisplayController, "ORMMADisplayControllerBridge");
        adView.addJavascriptInterface(this.mLocationController, "ORMMALocationControllerBridge");
        adView.addJavascriptInterface(this.mNetworkController, "ORMMANetworkControllerBridge");
        adView.addJavascriptInterface(this.mSensorController, "ORMMASensorControllerBridge");
        setCalendarEvent("content://com.android.calendar/");
        setCalendarEvent("events");
    }

    public OrmmaUtilityController(OrmmaView adView, Context context, VservAdManager vservAdManageractivity, int cachereq) {
        super(adView, vservAdManageractivity);
        this.url = null;
        this.lengthOfResponse = 0.0d;
        this.iscachereq = 0;
        this.isdisplayingCacheAD = false;
        this.cachePostActionUrl = null;
        this.calEvent = Preconditions.EMPTY_ARGUMENTS;
        this.context = context;
        this.webview = adView;
        this.iscachereq = cachereq;
        this.vservAdManageractivity = vservAdManageractivity;
        this.mAssetController = new OrmmaAssetController(adView, context);
        this.mDisplayController = new OrmmaDisplayController(adView, context);
        this.mLocationController = new OrmmaLocationController(adView, context);
        this.mNetworkController = new OrmmaNetworkController(adView, context);
        this.mSensorController = new OrmmaSensorController(adView, context);
        adView.addJavascriptInterface(this.mAssetController, new StringBuilder(String.valueOf("ORMMA")).append("AssetsControllerBridge").toString());
        adView.addJavascriptInterface(this.mDisplayController, "ORMMADisplayControllerBridge");
        adView.addJavascriptInterface(this.mLocationController, "ORMMALocationControllerBridge");
        adView.addJavascriptInterface(this.mNetworkController, "ORMMANetworkControllerBridge");
        adView.addJavascriptInterface(this.mSensorController, "ORMMASensorControllerBridge");
        setCalendarEvent("content://com.android.calendar/");
        setCalendarEvent("events");
    }

    private String createTelUrl(String number) {
        if (TextUtils.isEmpty(number)) {
            return null;
        }
        StringBuilder buf = new StringBuilder("tel:");
        buf.append(number);
        return buf.toString();
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
                    Log.i(AdTrackerConstants.GOAL_DOWNLOAD, new StringBuilder("Response code is ").append(response).toString());
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

    private String getSupports() {
        boolean p;
        String supports = "supports: [ 'level-1', 'level-2', 'screen', 'orientation', 'network'";
        if (this.mLocationController.allowLocationServices() && (this.mContext.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == 0 || this.mContext.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0)) {
            p = true;
        } else {
            p = false;
        }
        if (p) {
            supports = new StringBuilder(String.valueOf(supports)).append(", 'location'").toString();
        }
        supports = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(supports)).append(", 'sms'").toString())).append(", 'phone'").toString();
        if (this.mContext.checkCallingOrSelfPermission("android.permission.WRITE_CALENDAR") == 0 && this.mContext.checkCallingOrSelfPermission("android.permission.READ_CALENDAR") == 0) {
            p = true;
        } else {
            p = false;
        }
        if (p) {
            supports = new StringBuilder(String.valueOf(supports)).append(", 'calendar'").toString();
        }
        return new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(supports)).append(", 'video'").toString())).append(", 'audio'").toString())).append(", 'map'").toString())).append(", 'email' ]").toString();
    }

    @JavascriptInterface
    public void activate(String event) {
        if (event.equalsIgnoreCase(Events.NETWORK_CHANGE)) {
            this.mNetworkController.startNetworkListener();
        } else if (this.mLocationController.allowLocationServices() && event.equalsIgnoreCase(Events.LOCATION_CHANGE)) {
            this.mLocationController.startLocationListener();
        } else if (event.equalsIgnoreCase(Events.SHAKE)) {
            if (this.mSensorController != null) {
                this.mSensorController.startShakeListener();
            }
        } else if (event.equalsIgnoreCase(Events.TILT_CHANGE)) {
            if (this.mSensorController != null) {
                this.mSensorController.startTiltListener();
            }
        } else if (event.equalsIgnoreCase(Events.HEADING_CHANGE)) {
            if (this.mSensorController != null) {
                this.mSensorController.startHeadingListener();
            }
        } else if (event.equalsIgnoreCase(Events.ORIENTATION_CHANGE)) {
            this.mDisplayController.startConfigurationListener();
        }
    }

    @JavascriptInterface
    @SuppressLint({"InlinedApi"})
    public void clearPostActionNotifyUrl() {
        if (this.iscachereq == 0 || this.isdisplayingCacheAD) {
            SharedPreferences postUrlPreference;
            if (VERSION.SDK_INT >= 11) {
                postUrlPreference = this.mContext.getSharedPreferences("vservPostActionNotifyUrlPref", MMAdView.TRANSITION_RANDOM);
            } else {
                postUrlPreference = this.mContext.getSharedPreferences("vservPostActionNotifyUrlPref", 0);
            }
            if (postUrlPreference.edit().clear().commit() && Defines.ENABLE_lOGGING) {
                Log.i("post", "saved setPostActionNotifyUrl in preference is cleared");
            }
        }
    }

    public String copyTextFromJarIntoAssetDir(String alias, String source) {
        return this.mAssetController.copyTextFromJarIntoAssetDir(alias, source);
    }

    @JavascriptInterface
    public void deactivate(String event) {
        if (event.equalsIgnoreCase(Events.NETWORK_CHANGE)) {
            this.mNetworkController.stopNetworkListener();
        } else if (event.equalsIgnoreCase(Events.LOCATION_CHANGE)) {
            this.mLocationController.stopAllListeners();
        } else if (event.equalsIgnoreCase(Events.SHAKE)) {
            if (this.mSensorController != null) {
                this.mSensorController.stopShakeListener();
            }
        } else if (event.equalsIgnoreCase(Events.TILT_CHANGE)) {
            if (this.mSensorController != null) {
                this.mSensorController.stopTiltListener();
            }
        } else if (event.equalsIgnoreCase(Events.HEADING_CHANGE)) {
            if (this.mSensorController != null) {
                this.mSensorController.stopHeadingListener();
            }
        } else if (event.equalsIgnoreCase(Events.ORIENTATION_CHANGE)) {
            this.mDisplayController.stopConfigurationListener();
        }
    }

    public void deleteOldAds() {
        this.mAssetController.deleteOldAds();
    }

    public void init(float density) {
        this.mOrmmaView.injectJavaScript(new StringBuilder("window.ormmaview.fireChangeEvent({ state: 'default', network: '").append(this.mNetworkController.getNetwork()).append("',").append(" size: ").append(this.mDisplayController.getSize()).append(",").append(" maxSize: ").append(this.mDisplayController.getMaxSize()).append(",").append(" screenSize: ").append(this.mDisplayController.getScreenSize()).append(",").append(" defaultPosition: { x:").append((int) (((float) this.mOrmmaView.getLeft()) / density)).append(", y: ").append((int) (((float) this.mOrmmaView.getTop()) / density)).append(", width: ").append((int) (((float) this.mOrmmaView.getWidth()) / density)).append(", height: ").append((int) (((float) this.mOrmmaView.getHeight()) / density)).append(" },").append(" orientation:").append(this.mDisplayController.getOrientation()).append(",").append(getSupports()).append(" });").toString());
    }

    @JavascriptInterface
    public void makeCall(String number) {
        if (!TextUtils.isEmpty(number)) {
            String url = createTelUrl(number);
            if (url == null) {
                this.mOrmmaView.raiseError("Bad Phone Number", "makeCall");
            } else {
                Intent i = new Intent("android.intent.action.DIAL", Uri.parse(url.toString()));
                if (this.vservAdManageractivity == null) {
                    this.mContext.startActivity(i);
                } else {
                    this.vservAdManageractivity.startActivityForResult(i, 0);
                }
            }
        }
    }

    @JavascriptInterface
    public void ready() {
        this.mOrmmaView.injectJavaScript(new StringBuilder("Ormma.setState(\"").append(this.mOrmmaView.getState()).append("\");").toString());
        this.mOrmmaView.injectJavaScript("ORMMAReady();");
    }

    @JavascriptInterface
    public void sendMail(String recipient, String subject, String body) {
        if (!TextUtils.isEmpty(recipient) && !TextUtils.isEmpty(subject) && !TextUtils.isEmpty(body)) {
            Intent i = new Intent("android.intent.action.SEND");
            i.setType("plain/text");
            i.putExtra("android.intent.extra.EMAIL", new String[]{recipient});
            i.putExtra("android.intent.extra.SUBJECT", subject);
            i.putExtra("android.intent.extra.TEXT", body);
            i.addFlags(DriveFile.MODE_READ_ONLY);
            this.mContext.startActivity(i);
        }
    }

    @JavascriptInterface
    public void sendSMS(String recipient, String body) {
        if (!TextUtils.isEmpty(recipient) && !TextUtils.isEmpty(body)) {
            Intent sendIntent = new Intent("android.intent.action.VIEW");
            sendIntent.putExtra("address", recipient);
            sendIntent.putExtra("sms_body", body);
            sendIntent.setType("vnd.android-dir/mms-sms");
            if (this.vservAdManageractivity == null) {
                this.mContext.startActivity(sendIntent);
            } else {
                this.vservAdManageractivity.startActivityForResult(sendIntent, 0);
            }
        }
    }

    public void setCalendarEvent(String t_calevent) {
        this.calEvent = new StringBuilder(String.valueOf(this.calEvent)).append(t_calevent).toString();
    }

    public void setDisplayCacheAd(boolean t_displayCacheAD) {
        this.isdisplayingCacheAD = t_displayCacheAD;
    }

    public void setMaxSize(int w, int h) {
        this.mDisplayController.setMaxSize(w, h);
    }

    @JavascriptInterface
    @SuppressLint({"InlinedApi"})
    public void setPostActionNotifyUrl(String postActionNotifyUrl) {
        if (Defines.ENABLE_lOGGING) {
            Log.i("post", new StringBuilder("calling  setPostActionNotifyUrl with ").append(postActionNotifyUrl).toString());
        }
        SharedPreferences postUrlPreference;
        if (this.iscachereq == 0) {
            this.cachePostActionUrl = null;
            if (VERSION.SDK_INT >= 11) {
                postUrlPreference = this.mContext.getSharedPreferences("vservPostActionNotifyUrlPref", MMAdView.TRANSITION_RANDOM);
            } else {
                postUrlPreference = this.mContext.getSharedPreferences("vservPostActionNotifyUrlPref", 0);
            }
            if (postUrlPreference.edit().putString("key", postActionNotifyUrl).commit() && Defines.ENABLE_lOGGING) {
                Log.i("post", "saved setPostActionNotifyUrl in preference");
            }
        } else if (this.isdisplayingCacheAD) {
            this.cachePostActionUrl = null;
            if (VERSION.SDK_INT >= 11) {
                postUrlPreference = this.mContext.getSharedPreferences("vservPostActionNotifyUrlPref", MMAdView.TRANSITION_RANDOM);
            } else {
                postUrlPreference = this.mContext.getSharedPreferences("vservPostActionNotifyUrlPref", 0);
            }
            if (postUrlPreference.edit().putString("key", postActionNotifyUrl).commit() && Defines.ENABLE_lOGGING) {
                Log.i("vserv", "saved setPostActionNotifyUrl in preference");
            }
        } else {
            if (Defines.ENABLE_lOGGING) {
                Log.i("vserv", "saved setPostActionNotifyUrl in Cache");
            }
            if (this.webview != null) {
                this.cachePostActionUrl = postActionNotifyUrl;
            }
        }
    }

    @JavascriptInterface
    public void showAlert(String message) {
    }

    public void stopAllListeners() {
        try {
            this.mAssetController.stopAllListeners();
            this.mDisplayController.stopAllListeners();
            this.mLocationController.stopAllListeners();
            this.mNetworkController.stopAllListeners();
            if (this.mSensorController != null) {
                this.mSensorController.stopAllListeners();
            }
        } catch (Exception e) {
        }
    }

    @JavascriptInterface
    public void vservMakeCall(String number) {
        if (!TextUtils.isEmpty(number)) {
            if (createTelUrl(number) == null) {
                this.mOrmmaView.raiseError("Bad Phone Number", "makeCall");
            } else {
                makeCall(number);
            }
        }
    }

    @JavascriptInterface
    public void vservOpenUrlInDefaultBrowser(String url) {
        if (!TextUtils.isEmpty(url) && this.mContext.checkCallingOrSelfPermission("android.permission.INTERNET") == 0) {
            this.mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url.toString())));
        }
    }

    @JavascriptInterface
    public void vservRecommendFriend(String message, String link) {
        if (!TextUtils.isEmpty(message)) {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            if (TextUtils.isEmpty(link)) {
                intent.putExtra("android.intent.extra.TEXT", message);
            } else {
                intent.putExtra("android.intent.extra.TEXT", new StringBuilder(String.valueOf(message)).append("\n\n").append(link).toString());
            }
            this.context.startActivity(intent);
        }
    }

    @JavascriptInterface
    public void vservSendSMS(String recipient, String body) {
        if (!TextUtils.isEmpty(recipient) && !TextUtils.isEmpty(body)) {
            sendSMS(recipient, body);
        }
    }

    public String writeToDiskWrap(InputStream is, String currentFile, boolean storeInHashedDirectory, String injection, String bridgePath, String ormmaPath) throws IllegalStateException, IOException {
        return this.mAssetController.writeToDiskWrap(is, currentFile, storeInHashedDirectory, injection, bridgePath, ormmaPath);
    }
}