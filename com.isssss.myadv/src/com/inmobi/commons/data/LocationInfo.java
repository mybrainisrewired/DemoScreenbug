package com.inmobi.commons.data;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.internal.Log;
import com.millennialmedia.android.MMAdView;
import java.util.List;

public class LocationInfo {
    public static int LOCATION_SET_BY_PUB;
    public static int LOCATION_SET_BY_SDK;
    private static LocationManager a;
    private static double b;
    private static double c;
    private static double d;
    private static boolean e;
    private static long f;
    private static int g;

    static {
        LOCATION_SET_BY_SDK = 1;
        LOCATION_SET_BY_PUB = 0;
        g = LOCATION_SET_BY_SDK;
    }

    private static synchronized LocationManager a() {
        LocationManager locationManager;
        synchronized (LocationInfo.class) {
            locationManager = a;
        }
        return locationManager;
    }

    private static void a(double d) {
        b = d;
    }

    private static void a(long j) {
        f = j;
    }

    static void a(Location location) {
        if (location != null) {
            a(true);
            a(location.getLatitude());
            b(location.getLongitude());
            c((double) location.getAccuracy());
            a(location.getTime());
        } else {
            a(false);
        }
    }

    private static synchronized void a(LocationManager locationManager) {
        synchronized (LocationInfo.class) {
            a = locationManager;
        }
    }

    static void a(boolean z) {
        e = z;
    }

    private static void b(double d) {
        c = d;
    }

    private static boolean b() {
        boolean z = false;
        try {
            if (a() == null) {
                a((LocationManager) InternalSDKUtil.getContext().getSystemService("location"));
            }
            if (a() != null) {
                LocationManager a = a();
                Criteria criteria = new Criteria();
                if (InternalSDKUtil.getContext().checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0) {
                    criteria.setAccuracy(1);
                } else if (InternalSDKUtil.getContext().checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == 0) {
                    criteria.setAccuracy(MMAdView.TRANSITION_UP);
                }
                criteria.setCostAllowed(false);
                String bestProvider = a.getBestProvider(criteria, true);
                if (bestProvider != null) {
                    boolean isProviderEnabled = a.isProviderEnabled("network");
                    boolean isProviderEnabled2 = a.isProviderEnabled("gps");
                    if (!isProviderEnabled && !isProviderEnabled2) {
                        return z;
                    }
                    Location lastKnownLocation = a.getLastKnownLocation(bestProvider);
                    if (lastKnownLocation == null) {
                        lastKnownLocation = c();
                        Log.debug(InternalSDKUtil.LOGGING_TAG, "lastKnownLocation: " + lastKnownLocation);
                    }
                    if (lastKnownLocation == null) {
                        return z;
                    }
                    Log.debug(InternalSDKUtil.LOGGING_TAG, "lastBestKnownLocation: " + lastKnownLocation);
                    a(lastKnownLocation);
                    return true;
                }
            }
            return true;
        } catch (Exception e) {
            Log.debug(InternalSDKUtil.LOGGING_TAG, "Error getting the Location Info ", e);
            return z;
        }
    }

    private static Location c() {
        if (a() == null) {
            a((LocationManager) InternalSDKUtil.getContext().getSystemService("location"));
        }
        if (a() != null) {
            LocationManager a = a();
            List providers = a.getProviders(true);
            int i = providers.size() - 1;
            while (i >= 0) {
                String str = (String) providers.get(i);
                if (a.isProviderEnabled(str)) {
                    Location lastKnownLocation = a.getLastKnownLocation(str);
                    if (lastKnownLocation != null) {
                        return lastKnownLocation;
                    }
                }
                i--;
            }
        }
        return null;
    }

    private static void c(double d) {
        d = d;
    }

    public static synchronized void collectLocationInfo() {
        synchronized (LocationInfo.class) {
            try {
                if (!isLocationPermissionsDenied() && b()) {
                    setSDKLocation(LOCATION_SET_BY_SDK);
                }
            } catch (Exception e) {
                Log.internal(InternalSDKUtil.LOGGING_TAG, "Exception updating loc info", e);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String currentLocationStr() {
        throw new UnsupportedOperationException("Method not decompiled: com.inmobi.commons.data.LocationInfo.currentLocationStr():java.lang.String");
        /*
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = com.inmobi.commons.data.DemogInfo.isLocationInquiryAllowed();
        if (r1 != 0) goto L_0x004f;
    L_0x000b:
        r1 = com.inmobi.commons.data.DemogInfo.getCurrentLocation();
        if (r1 == 0) goto L_0x004a;
    L_0x0011:
        r1 = com.inmobi.commons.data.DemogInfo.getCurrentLocation();
        a(r1);
    L_0x0018:
        r1 = LOCATION_SET_BY_PUB;
        setSDKLocation(r1);
    L_0x001d:
        if (r0 == 0) goto L_0x0068;
    L_0x001f:
        r1 = isValidGeoInfo();
        if (r1 == 0) goto L_0x0068;
    L_0x0025:
        r1 = getLat();
        r0.append(r1);
        r1 = ",";
        r0.append(r1);
        r1 = getLon();
        r0.append(r1);
        r1 = ",";
        r0.append(r1);
        r1 = getLocAccuracy();
        r1 = (int) r1;
        r0.append(r1);
        r0 = r0.toString();
    L_0x0049:
        return r0;
    L_0x004a:
        r1 = 0;
        a(r1);
        goto L_0x0018;
    L_0x004f:
        r1 = isValidGeoInfo();
        if (r1 != 0) goto L_0x001d;
    L_0x0055:
        r1 = com.inmobi.commons.data.DemogInfo.getCurrentLocation();
        if (r1 == 0) goto L_0x001d;
    L_0x005b:
        r1 = com.inmobi.commons.data.DemogInfo.getCurrentLocation();
        a(r1);
        r1 = LOCATION_SET_BY_PUB;
        setSDKLocation(r1);
        goto L_0x001d;
    L_0x0068:
        r0 = "";
        goto L_0x0049;
        */
    }

    public static long getGeoTS() {
        return f;
    }

    public static double getLat() {
        return b;
    }

    public static double getLocAccuracy() {
        return d;
    }

    public static double getLon() {
        return c;
    }

    public static boolean isLocationPermissionsDenied() {
        return (InternalSDKUtil.getContext().checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == 0 || InternalSDKUtil.getContext().checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0) ? false : true;
    }

    public static int isSDKSetLocation() {
        return g;
    }

    public static boolean isValidGeoInfo() {
        return e;
    }

    public static void setSDKLocation(int i) {
        g = i;
    }
}