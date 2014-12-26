package mobi.vserv.org.ormma.controller.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import mobi.vserv.org.ormma.controller.OrmmaDisplayController;

public class OrmmaConfigurationBroadcastReceiver extends BroadcastReceiver {
    private int mLastOrientation;
    private OrmmaDisplayController mOrmmaDisplayController;

    public OrmmaConfigurationBroadcastReceiver(OrmmaDisplayController ormmaDisplayController) {
        this.mOrmmaDisplayController = ormmaDisplayController;
        this.mLastOrientation = this.mOrmmaDisplayController.getOrientation();
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.CONFIGURATION_CHANGED")) {
            int orientation = this.mOrmmaDisplayController.getOrientation();
            if (orientation != this.mLastOrientation) {
                this.mLastOrientation = orientation;
                this.mOrmmaDisplayController.onOrientationChanged(this.mLastOrientation);
            }
        }
    }
}