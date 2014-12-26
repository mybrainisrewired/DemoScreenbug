package mobi.vserv.org.ormma.controller.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.inmobi.monetization.internal.InvalidManifestErrorMessages;
import mobi.vserv.org.ormma.controller.OrmmaNetworkController;

public class OrmmaNetworkBroadcastReceiver extends BroadcastReceiver {
    private OrmmaNetworkController mOrmmaNetworkController;

    public OrmmaNetworkBroadcastReceiver(OrmmaNetworkController ormmaNetworkController) {
        this.mOrmmaNetworkController = ormmaNetworkController;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(InvalidManifestErrorMessages.CONNECTIVITY_INTENT_ACTION)) {
            this.mOrmmaNetworkController.onConnectionChanged();
        }
    }
}