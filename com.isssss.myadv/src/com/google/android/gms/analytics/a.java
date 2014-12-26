package com.google.android.gms.analytics;

import android.content.Context;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import java.io.IOException;

class a implements m {
    private static Object sf;
    private static a sg;
    private Context mContext;
    private Info sh;
    private long si;

    static {
        sf = new Object();
    }

    a(Context context) {
        this.mContext = context;
    }

    private Info bQ() {
        Info info = null;
        try {
            return AdvertisingIdClient.getAdvertisingIdInfo(this.mContext);
        } catch (IllegalStateException e) {
            aa.z("IllegalStateException getting Ad Id Info. If you would like to see Audience reports, please ensure that you have added '<meta-data android:name=\"com.google.android.gms.version\" android:value=\"@integer/google_play_services_version\" />' to your application manifest file. See http://goo.gl/naFqQk for details.");
            return info;
        } catch (GooglePlayServicesRepairableException e2) {
            aa.z("GooglePlayServicesRepairableException getting Ad Id Info");
            return info;
        } catch (IOException e3) {
            aa.z("IOException getting Ad Id Info");
            return info;
        } catch (GooglePlayServicesNotAvailableException e4) {
            aa.z("GooglePlayServicesNotAvailableException getting Ad Id Info");
            return info;
        } catch (Exception e5) {
            aa.z("Unknown exception. Could not get the ad Id.");
            return info;
        }
    }

    public static m m(Context context) {
        if (sg == null) {
            synchronized (sf) {
                if (sg == null) {
                    sg = new a(context);
                }
            }
        }
        return sg;
    }

    public String getValue(String field) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.si > 1000) {
            this.sh = bQ();
            this.si = currentTimeMillis;
        }
        if (this.sh != null && "&adid".equals(field)) {
            return this.sh.getId();
        }
        if ("&ate".equals(field)) {
            return this.sh.isLimitAdTrackingEnabled() ? "0" : "1";
        }
        return null;
    }
}