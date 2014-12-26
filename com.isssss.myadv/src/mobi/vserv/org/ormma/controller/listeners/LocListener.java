package mobi.vserv.org.ormma.controller.listeners;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import mobi.vserv.org.ormma.controller.OrmmaLocationController;

public class LocListener implements LocationListener {
    private LocationManager mLocMan;
    OrmmaLocationController mOrmmaLocationController;
    private String mProvider;

    public LocListener(Context c, int interval, OrmmaLocationController ormmaLocationController, String provider) {
        this.mOrmmaLocationController = ormmaLocationController;
        this.mLocMan = (LocationManager) c.getSystemService("location");
        this.mProvider = provider;
    }

    public void onLocationChanged(Location location) {
        this.mOrmmaLocationController.success(location);
    }

    public void onProviderDisabled(String provider) {
        this.mOrmmaLocationController.fail();
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (status == 0) {
            this.mOrmmaLocationController.fail();
        }
    }

    public void start() {
        this.mLocMan.requestLocationUpdates(this.mProvider, 0, BitmapDescriptorFactory.HUE_RED, this);
    }

    public void stop() {
        this.mLocMan.removeUpdates(this);
    }
}