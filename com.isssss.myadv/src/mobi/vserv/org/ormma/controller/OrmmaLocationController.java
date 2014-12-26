package mobi.vserv.org.ormma.controller;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.webkit.JavascriptInterface;
import java.util.Iterator;
import mobi.vserv.org.ormma.controller.listeners.LocListener;
import mobi.vserv.org.ormma.view.OrmmaView;

public class OrmmaLocationController extends OrmmaController {
    final int INTERVAL;
    private boolean allowLocationServices;
    private boolean hasPermission;
    private LocListener mGps;
    private int mLocListenerCount;
    private LocationManager mLocationManager;
    private LocListener mNetwork;

    public OrmmaLocationController(OrmmaView adView, Context context) {
        super(adView, context);
        this.hasPermission = false;
        this.INTERVAL = 1000;
        this.allowLocationServices = false;
        try {
            this.mLocationManager = (LocationManager) context.getSystemService("location");
            if (this.mLocationManager.getProvider("gps") != null) {
                this.mGps = new LocListener(context, 1000, this, "gps");
            }
            if (this.mLocationManager.getProvider("network") != null) {
                this.mNetwork = new LocListener(context, 1000, this, "network");
            }
            this.hasPermission = true;
        } catch (SecurityException e) {
        }
    }

    private static String formatLocation(Location loc) {
        return new StringBuilder("{ lat: ").append(loc.getLatitude()).append(", lon: ").append(loc.getLongitude()).append(", acc: ").append(loc.getAccuracy()).append("}").toString();
    }

    public void allowLocationServices(boolean flag) {
        this.allowLocationServices = flag;
    }

    public boolean allowLocationServices() {
        return this.allowLocationServices;
    }

    public void fail() {
        this.mOrmmaView.injectJavaScript("window.ormmaview.fireErrorEvent(\"Location cannot be identified\", \"OrmmaLocationController\")");
    }

    @JavascriptInterface
    public String getLocation() {
        if (!this.hasPermission) {
            return null;
        }
        Iterator<String> provider = this.mLocationManager.getProviders(true).iterator();
        Location location = null;
        while (provider.hasNext()) {
            location = this.mLocationManager.getLastKnownLocation((String) provider.next());
            if (location != null) {
                break;
            }
        }
        return location != null ? formatLocation(location) : null;
    }

    public void startLocationListener() {
        if (this.mLocListenerCount == 0) {
            if (this.mNetwork != null) {
                this.mNetwork.start();
            }
            if (this.mGps != null) {
                this.mGps.start();
            }
        }
        this.mLocListenerCount++;
    }

    public void stopAllListeners() {
        this.mLocListenerCount = 0;
        try {
            this.mGps.stop();
        } catch (Exception e) {
        }
        try {
            this.mNetwork.stop();
        } catch (Exception e2) {
        }
    }

    public void stopLocationListener() {
        this.mLocListenerCount--;
        if (this.mLocListenerCount == 0) {
            if (this.mNetwork != null) {
                this.mNetwork.stop();
            }
            if (this.mGps != null) {
                this.mGps.stop();
            }
        }
    }

    public void success(Location loc) {
        this.mOrmmaView.injectJavaScript(new StringBuilder("window.ormmaview.fireChangeEvent({ location: ").append(formatLocation(loc)).append("})").toString());
    }
}