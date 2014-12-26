package android.support.v4.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

class ConnectivityManagerCompatHoneycombMR2 {
    ConnectivityManagerCompatHoneycombMR2() {
    }

    public static boolean isActiveNetworkMetered(ConnectivityManager cm) {
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return true;
        }
        switch (info.getType()) {
            case MMAdView.TRANSITION_NONE:
            case MMAdView.TRANSITION_UP:
            case MMAdView.TRANSITION_DOWN:
            case MMAdView.TRANSITION_RANDOM:
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                return true;
            case MMAdView.TRANSITION_FADE:
            case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
            case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                return false;
            default:
                return true;
        }
    }
}