package com.mopub.common;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.mopub.common.logging.MoPubLog;
import java.math.BigDecimal;

public class LocationService {

    public enum LocationAwareness {
        NORMAL,
        TRUNCATED,
        DISABLED;

        static {
            NORMAL = new com.mopub.common.LocationService.LocationAwareness("NORMAL", 0);
            TRUNCATED = new com.mopub.common.LocationService.LocationAwareness("TRUNCATED", 1);
            DISABLED = new com.mopub.common.LocationService.LocationAwareness("DISABLED", 2);
            ENUM$VALUES = new com.mopub.common.LocationService.LocationAwareness[]{NORMAL, TRUNCATED, DISABLED};
        }

        @Deprecated
        public static com.mopub.common.LocationService.LocationAwareness fromMoPubLocationAwareness(com.mopub.common.MoPub.LocationAwareness awareness) {
            if (awareness == com.mopub.common.MoPub.LocationAwareness.DISABLED) {
                return DISABLED;
            }
            return awareness == com.mopub.common.MoPub.LocationAwareness.TRUNCATED ? TRUNCATED : NORMAL;
        }

        @Deprecated
        public com.mopub.common.MoPub.LocationAwareness getNewLocationAwareness() {
            if (this == TRUNCATED) {
                return com.mopub.common.MoPub.LocationAwareness.TRUNCATED;
            }
            return this == DISABLED ? com.mopub.common.MoPub.LocationAwareness.DISABLED : com.mopub.common.MoPub.LocationAwareness.NORMAL;
        }
    }

    public static Location getLastKnownLocation(Context context, int locationPrecision, com.mopub.common.MoPub.LocationAwareness locationLocationAwareness) {
        if (locationLocationAwareness == com.mopub.common.MoPub.LocationAwareness.DISABLED) {
            return null;
        }
        LocationManager locationManager = (LocationManager) context.getSystemService("location");
        Location gpsLocation = null;
        try {
            gpsLocation = locationManager.getLastKnownLocation("gps");
        } catch (SecurityException e) {
            MoPubLog.d("Failed to retrieve GPS location: access appears to be disabled.");
        } catch (IllegalArgumentException e2) {
            MoPubLog.d("Failed to retrieve GPS location: device has no GPS provider.");
        }
        Location networkLocation = null;
        try {
            networkLocation = locationManager.getLastKnownLocation("network");
        } catch (SecurityException e3) {
            MoPubLog.d("Failed to retrieve network location: access appears to be disabled.");
        } catch (IllegalArgumentException e4) {
            MoPubLog.d("Failed to retrieve network location: device has no network provider.");
        }
        if (gpsLocation == null && networkLocation == null) {
            return null;
        }
        Location result;
        if (gpsLocation == null || networkLocation == null) {
            if (gpsLocation != null) {
                result = gpsLocation;
            } else {
                result = networkLocation;
            }
        } else if (gpsLocation.getTime() > networkLocation.getTime()) {
            result = gpsLocation;
        } else {
            result = networkLocation;
        }
        if (locationLocationAwareness != com.mopub.common.MoPub.LocationAwareness.TRUNCATED) {
            return result;
        }
        result.setLatitude(BigDecimal.valueOf(result.getLatitude()).setScale(locationPrecision, ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES).doubleValue());
        result.setLongitude(BigDecimal.valueOf(result.getLongitude()).setScale(locationPrecision, ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES).doubleValue());
        return result;
    }
}