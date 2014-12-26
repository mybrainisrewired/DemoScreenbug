package mobi.vserv.android.ads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.millennialmedia.android.MMAdView;

public class VservTrackingManager {
    static SharedPreferences preferences;

    class AnonymousClass_1 extends Thread {
        private final /* synthetic */ Context val$context;

        AnonymousClass_1(Context context) {
            this.val$context = context;
        }

        public void run() {
            new VservInstallReceiver(this.val$context).hitUrl(preferences.getString("referrerUrl", null));
        }
    }

    @SuppressLint({"InlinedApi"})
    public static void process(Context context) {
        if (VERSION.SDK_INT >= 11) {
            preferences = context.getSharedPreferences("VservInstallReferrer", MMAdView.TRANSITION_RANDOM);
        } else {
            preferences = context.getSharedPreferences("VservInstallReferrer", 0);
        }
        if (preferences.contains("referrerUrl") && !TextUtils.isEmpty(preferences.getString("referrerUrl", null))) {
            new AnonymousClass_1(context).start();
        }
    }
}