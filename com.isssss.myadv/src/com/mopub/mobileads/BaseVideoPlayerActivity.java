package com.mopub.mobileads;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import com.google.android.gms.drive.DriveFile;
import com.inmobi.re.configs.Initializer;
import com.mopub.common.logging.MoPubLog;
import com.mopub.mobileads.util.vast.VastVideoConfiguration;

class BaseVideoPlayerActivity extends Activity {
    static final String VIDEO_CLASS_EXTRAS_KEY = "video_view_class_name";
    static final String VIDEO_URL = "video_url";

    BaseVideoPlayerActivity() {
    }

    static Intent createIntentMraid(Context context, String videoUrl, AdConfiguration adConfiguration) {
        Intent intentVideoPlayerActivity = new Intent(context, MraidVideoPlayerActivity.class);
        intentVideoPlayerActivity.setFlags(DriveFile.MODE_READ_ONLY);
        intentVideoPlayerActivity.putExtra(VIDEO_CLASS_EXTRAS_KEY, Initializer.PRODUCT_MRAID);
        intentVideoPlayerActivity.putExtra(VIDEO_URL, videoUrl);
        intentVideoPlayerActivity.putExtra(AdFetcher.AD_CONFIGURATION_KEY, adConfiguration);
        return intentVideoPlayerActivity;
    }

    static Intent createIntentVast(Context context, VastVideoConfiguration vastVideoConfiguration, AdConfiguration adConfiguration) {
        Intent intentVideoPlayerActivity = new Intent(context, MraidVideoPlayerActivity.class);
        intentVideoPlayerActivity.setFlags(DriveFile.MODE_READ_ONLY);
        intentVideoPlayerActivity.putExtra(VIDEO_CLASS_EXTRAS_KEY, "vast");
        intentVideoPlayerActivity.putExtra("vast_video_configuration", vastVideoConfiguration);
        intentVideoPlayerActivity.putExtra(AdFetcher.AD_CONFIGURATION_KEY, adConfiguration);
        return intentVideoPlayerActivity;
    }

    static void startMraid(Context context, String videoUrl, AdConfiguration adConfiguration) {
        try {
            context.startActivity(createIntentMraid(context, videoUrl, adConfiguration));
        } catch (ActivityNotFoundException e) {
            MoPubLog.d("Activity MraidVideoPlayerActivity not found. Did you declare it in your AndroidManifest.xml?");
        }
    }

    static void startVast(Context context, VastVideoConfiguration vastVideoConfiguration, AdConfiguration adConfiguration) {
        try {
            context.startActivity(createIntentVast(context, vastVideoConfiguration, adConfiguration));
        } catch (ActivityNotFoundException e) {
            MoPubLog.d("Activity MraidVideoPlayerActivity not found. Did you declare it in your AndroidManifest.xml?");
        }
    }
}