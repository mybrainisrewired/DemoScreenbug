package mobi.vserv.android.ads;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.support.v4.media.TransportMediator;
import android.webkit.WebView;
import com.inmobi.commons.analytics.iat.impl.AdTrackerConstants;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class VservInstallReceiver extends BroadcastReceiver {
    private static SharedPreferences preferences = null;
    public static final String version = "0.2.0";
    private String installHitUrl;

    public VservInstallReceiver() {
        this.installHitUrl = "http://c.vserv.mobi/delivery/ti.php?app=1";
    }

    public VservInstallReceiver(Context context) {
        this.installHitUrl = "http://c.vserv.mobi/delivery/ti.php?app=1";
        if (VERSION.SDK_INT >= 11) {
            preferences = context.getSharedPreferences("VservInstallReferrer", MMAdView.TRANSITION_RANDOM);
        } else {
            preferences = context.getSharedPreferences("VservInstallReferrer", 0);
        }
    }

    private String getQueryString(String referrerString, Context context) {
        String androidId;
        PackageManager pm;
        String applicationName;
        String userAgent;
        String queryString = Preconditions.EMPTY_ARGUMENTS;
        try {
            String vserv;
            String deviceModel;
            int trackerIdIndex = referrerString.lastIndexOf("_") + 1;
            String trackerId = null;
            if (trackerIdIndex > 0) {
                trackerId = referrerString.substring(trackerIdIndex);
                vserv = referrerString.substring(referrerString.indexOf("_") + 1, trackerIdIndex - 1);
            } else {
                vserv = referrerString.substring(referrerString.indexOf("_") + 1);
            }
            if (trackerId == null || trackerId.equals(Preconditions.EMPTY_ARGUMENTS)) {
                queryString = new StringBuilder(String.valueOf(queryString)).append("&trackerid=TRACKERID").toString();
            } else {
                try {
                    queryString = new StringBuilder(String.valueOf(queryString)).append("&trackerid=").append(URLEncoder.encode(trackerId.trim(), "UTF-8")).toString();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            if (vserv != null) {
                if (!vserv.equals(Preconditions.EMPTY_ARGUMENTS)) {
                    try {
                        queryString = new StringBuilder(String.valueOf(queryString)).append("&vserv=").append(URLEncoder.encode(vserv.trim(), "UTF-8")).toString();
                    } catch (UnsupportedEncodingException e2) {
                        e2.printStackTrace();
                    }
                    try {
                        androidId = Secure.getString(context.getContentResolver(), "android_id");
                        if (androidId != null) {
                            queryString = new StringBuilder(String.valueOf(queryString)).append("&aid=").append(URLEncoder.encode(androidId.trim(), "UTF-8")).toString();
                        }
                    } catch (Exception e3) {
                    }
                    try {
                        pm = context.getPackageManager();
                        applicationName = (String) pm.getApplicationInfo(context.getPackageName(), TransportMediator.FLAG_KEY_MEDIA_NEXT).loadLabel(pm);
                        if (applicationName != null) {
                            queryString = new StringBuilder(String.valueOf(queryString)).append("&mn=").append(URLEncoder.encode(applicationName.trim(), "UTF-8")).toString();
                        }
                    } catch (Exception e4) {
                    }
                    deviceModel = Build.MODEL;
                    if (deviceModel != null) {
                        queryString = new StringBuilder(String.valueOf(queryString)).append("&m=").append(URLEncoder.encode(deviceModel.trim(), "UTF-8")).toString();
                    }
                    try {
                        userAgent = new WebView(context).getSettings().getUserAgentString();
                        return userAgent == null ? new StringBuilder(String.valueOf(queryString)).append("&ua=").append(URLEncoder.encode(userAgent.trim(), "UTF-8")).toString() : queryString;
                    } catch (Exception e5) {
                        return queryString;
                    }
                }
            }
            queryString = new StringBuilder(String.valueOf(queryString)).append("&vserv=VSERV").toString();
        } catch (Exception e6) {
        }
        androidId = Secure.getString(context.getContentResolver(), "android_id");
        if (androidId != null) {
            queryString = new StringBuilder(String.valueOf(queryString)).append("&aid=").append(URLEncoder.encode(androidId.trim(), "UTF-8")).toString();
        }
        pm = context.getPackageManager();
        applicationName = (String) pm.getApplicationInfo(context.getPackageName(), TransportMediator.FLAG_KEY_MEDIA_NEXT).loadLabel(pm);
        if (applicationName != null) {
            queryString = new StringBuilder(String.valueOf(queryString)).append("&mn=").append(URLEncoder.encode(applicationName.trim(), "UTF-8")).toString();
        }
        try {
            deviceModel = Build.MODEL;
            if (deviceModel != null) {
                queryString = new StringBuilder(String.valueOf(queryString)).append("&m=").append(URLEncoder.encode(deviceModel.trim(), "UTF-8")).toString();
            }
        } catch (Exception e7) {
        }
        userAgent = new WebView(context).getSettings().getUserAgentString();
        if (userAgent == null) {
        }
    }

    protected void hitUrl(String urlString) {
        while (true) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
                connection.setInstanceFollowRedirects(false);
                connection.setAllowUserInteraction(false);
                connection.setRequestMethod("GET");
                connection.connect();
                int response = connection.getResponseCode();
                if (response == 200) {
                    preferences.edit().remove("referrerUrl").commit();
                }
                if (response == 307 || response == 300 || response == 301 || response == 302 || response == 303) {
                    urlString = connection.getHeaderField("location");
                } else {
                    return;
                }
            } catch (MalformedURLException e) {
            } catch (Exception e2) {
            }
        }
    }

    public void onReceive(Context context, Intent intent) {
        if (VERSION.SDK_INT >= 11) {
            preferences = context.getSharedPreferences("VservInstallReferrer", MMAdView.TRANSITION_RANDOM);
        } else {
            preferences = context.getSharedPreferences("VservInstallReferrer", 0);
        }
        if (intent.getAction().equals(InternalSDKUtil.ACTION_RECEIVER_REFERRER) && intent.hasExtra(AdTrackerConstants.REFERRER)) {
            try {
                String referrerString = intent.getStringExtra(AdTrackerConstants.REFERRER).trim();
                if (referrerString.contains("^^")) {
                    referrerString = referrerString.substring(0, referrerString.indexOf("^^"));
                }
                this.installHitUrl = new StringBuilder(String.valueOf(this.installHitUrl)).append(getQueryString(referrerString, context)).toString();
                Editor editor = preferences.edit();
                editor.putString("referrerUrl", this.installHitUrl);
                editor.commit();
                new Thread() {
                    public void run() {
                        super.run();
                        VservInstallReceiver.this.hitUrl(VservInstallReceiver.this.installHitUrl);
                    }
                }.start();
            } catch (Exception e) {
            }
        }
    }
}