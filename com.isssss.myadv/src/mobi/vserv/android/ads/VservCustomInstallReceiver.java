package mobi.vserv.android.ads;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Build.VERSION;
import android.support.v4.media.TransportMediator;
import com.inmobi.commons.analytics.iat.impl.AdTrackerConstants;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.millennialmedia.android.MMAdView;
import java.util.Iterator;

public class VservCustomInstallReceiver extends BroadcastReceiver {
    private final String INSTALL_REFERRER_ACTION;
    private String passReferrerTo;

    public VservCustomInstallReceiver() {
        this.passReferrerTo = "ALL";
        this.INSTALL_REFERRER_ACTION = InternalSDKUtil.ACTION_RECEIVER_REFERRER;
    }

    @SuppressLint({"InlinedApi"})
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences;
        if (VERSION.SDK_INT >= 11) {
            preferences = context.getSharedPreferences("VservInstallReferrer", MMAdView.TRANSITION_RANDOM);
        } else {
            preferences = context.getSharedPreferences("VservInstallReferrer", 0);
        }
        if (preferences.getBoolean("isFirstTime", true) && intent.getAction().equals(InternalSDKUtil.ACTION_RECEIVER_REFERRER)) {
            preferences.edit().putBoolean("isFirstTime", false).commit();
            String referrerString = intent.getStringExtra(AdTrackerConstants.REFERRER);
            if (referrerString != null && referrerString.toLowerCase().startsWith("vserv")) {
                new VservInstallReceiver().onReceive(context, intent);
                if (referrerString.indexOf("^^") != -1) {
                    Intent intent2 = intent;
                    intent2.putExtra(AdTrackerConstants.REFERRER, referrerString.substring(referrerString.indexOf("^^") + 2, referrerString.length()));
                } else {
                    intent.removeExtra(AdTrackerConstants.REFERRER);
                }
            }
            try {
                ActivityInfo app1 = context.getPackageManager().getReceiverInfo(new ComponentName(context, getClass().getName()), TransportMediator.FLAG_KEY_MEDIA_NEXT);
                if (app1.metaData.containsKey("PASS_REFERRER_TO")) {
                    this.passReferrerTo = app1.metaData.getString("PASS_REFERRER_TO");
                }
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            if (!this.passReferrerTo.equals("NONE")) {
                Iterator<ResolveInfo> iterator = context.getPackageManager().queryBroadcastReceivers(new Intent(InternalSDKUtil.ACTION_RECEIVER_REFERRER), 0).iterator();
                while (iterator.hasNext()) {
                    ResolveInfo resolveinfo = (ResolveInfo) iterator.next();
                    String s1 = intent.getAction();
                    if (resolveinfo.activityInfo.packageName.equals(context.getPackageName()) && InternalSDKUtil.ACTION_RECEIVER_REFERRER.equals(s1) && !getClass().getName().equals(resolveinfo.activityInfo.name)) {
                        try {
                            ((BroadcastReceiver) Class.forName(resolveinfo.activityInfo.name).newInstance()).onReceive(context, intent);
                        } catch (InstantiationException e2) {
                            e2.printStackTrace();
                        } catch (IllegalAccessException e3) {
                            e3.printStackTrace();
                        } catch (ClassNotFoundException e4) {
                            e4.printStackTrace();
                        }
                        if (this.passReferrerTo.equals("ONLY_NEXT")) {
                            return;
                        }
                    }
                }
            }
        }
    }
}