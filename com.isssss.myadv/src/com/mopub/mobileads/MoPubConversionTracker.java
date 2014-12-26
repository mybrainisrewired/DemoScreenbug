package com.mopub.mobileads;

import android.content.Context;
import android.content.SharedPreferences;
import com.inmobi.commons.analytics.db.AnalyticsEvent;
import com.mopub.common.BaseUrlGenerator;
import com.mopub.common.ClientMetadata;
import com.mopub.common.GpsHelper;
import com.mopub.common.GpsHelper.GpsHelperListener;
import com.mopub.common.SharedPreferencesHelper;
import com.mopub.common.logging.MoPubLog;
import com.mopub.mobileads.factories.HttpClientFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

public class MoPubConversionTracker {
    private static final String TRACK_HANDLER = "/m/open";
    private static final String TRACK_HOST = "ads.mopub.com";
    private Context mContext;
    private ConversionTrackerGpsHelperListener mConversionTrackerGpsHelperListener;
    private String mIsTrackedKey;
    private String mPackageName;
    private SharedPreferences mSharedPreferences;

    private class TrackOpen implements Runnable {
        private TrackOpen() {
        }

        public void run() {
            String url = new ConversionUrlGenerator(null).generateUrlString(TRACK_HOST);
            MoPubLog.d(new StringBuilder("Conversion track: ").append(url).toString());
            try {
                HttpResponse response = HttpClientFactory.create().execute(new HttpGet(url));
                if (response.getStatusLine().getStatusCode() != 200) {
                    MoPubLog.d("Conversion track failed: Status code != 200.");
                } else {
                    HttpEntity entity = response.getEntity();
                    if (entity == null || entity.getContentLength() == 0) {
                        MoPubLog.d("Conversion track failed: Response was empty.");
                    } else {
                        MoPubLog.d("Conversion track successful.");
                        MoPubConversionTracker.this.mSharedPreferences.edit().putBoolean(MoPubConversionTracker.this.mIsTrackedKey, true).commit();
                    }
                }
            } catch (Exception e) {
                MoPubLog.d(new StringBuilder("Conversion track failed [").append(e.getClass().getSimpleName()).append("]: ").append(url).toString());
            }
        }
    }

    class ConversionTrackerGpsHelperListener implements GpsHelperListener {
        ConversionTrackerGpsHelperListener() {
        }

        public void onFetchAdInfoCompleted() {
            new Thread(new TrackOpen(null)).start();
        }
    }

    private class ConversionUrlGenerator extends BaseUrlGenerator {
        private ConversionUrlGenerator() {
        }

        private void setPackageId(String packageName) {
            addParam(AnalyticsEvent.EVENT_ID, packageName);
        }

        public String generateUrlString(String serverHostname) {
            initUrlString(serverHostname, TRACK_HANDLER);
            setApiVersion("6");
            setPackageId(MoPubConversionTracker.this.mPackageName);
            ClientMetadata clientMetadata = ClientMetadata.getInstance(MoPubConversionTracker.this.mContext);
            setUdid(clientMetadata.getUdid());
            setDoNotTrack(GpsHelper.isLimitAdTrackingEnabled(MoPubConversionTracker.this.mContext));
            setAppVersion(clientMetadata.getAppVersion());
            return getFinalUrlString();
        }
    }

    public MoPubConversionTracker() {
        this.mConversionTrackerGpsHelperListener = new ConversionTrackerGpsHelperListener();
    }

    private boolean isAlreadyTracked() {
        return this.mSharedPreferences.getBoolean(this.mIsTrackedKey, false);
    }

    public void reportAppOpen(Context context) {
        if (context != null) {
            this.mContext = context;
            this.mPackageName = this.mContext.getPackageName();
            this.mIsTrackedKey = new StringBuilder(String.valueOf(this.mPackageName)).append(" tracked").toString();
            this.mSharedPreferences = SharedPreferencesHelper.getSharedPreferences(this.mContext);
            if (isAlreadyTracked()) {
                MoPubLog.d("Conversion already tracked");
            } else {
                GpsHelper.asyncFetchAdvertisingInfo(this.mContext, this.mConversionTrackerGpsHelperListener);
            }
        }
    }
}