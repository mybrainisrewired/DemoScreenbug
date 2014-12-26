package mobi.vserv.org.ormma.controller;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.support.v4.os.EnvironmentCompat;
import android.webkit.JavascriptInterface;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.inmobi.monetization.internal.InvalidManifestErrorMessages;
import com.millennialmedia.android.MMAdView;
import mobi.vserv.org.ormma.controller.util.OrmmaNetworkBroadcastReceiver;
import mobi.vserv.org.ormma.view.OrmmaView;

public class OrmmaNetworkController extends OrmmaController {
    private static /* synthetic */ int[] $SWITCH_TABLE$android$net$NetworkInfo$State;
    private OrmmaNetworkBroadcastReceiver mBroadCastReceiver;
    private ConnectivityManager mConnectivityManager;
    private IntentFilter mFilter;
    private int mNetworkListenerCount;

    static /* synthetic */ int[] $SWITCH_TABLE$android$net$NetworkInfo$State() {
        int[] iArr = $SWITCH_TABLE$android$net$NetworkInfo$State;
        if (iArr == null) {
            iArr = new int[State.values().length];
            try {
                iArr[State.CONNECTED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[State.CONNECTING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[State.DISCONNECTED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[State.DISCONNECTING.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[State.SUSPENDED.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[State.UNKNOWN.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            $SWITCH_TABLE$android$net$NetworkInfo$State = iArr;
        }
        return iArr;
    }

    public OrmmaNetworkController(OrmmaView adView, Context context) {
        super(adView, context);
        this.mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
    }

    @JavascriptInterface
    public String getNetwork() {
        NetworkInfo ni = this.mConnectivityManager.getActiveNetworkInfo();
        String networkType = EnvironmentCompat.MEDIA_UNKNOWN;
        if (ni == null) {
            return "offline";
        }
        switch ($SWITCH_TABLE$android$net$NetworkInfo$State()[ni.getState().ordinal()]) {
            case MMAdView.TRANSITION_DOWN:
                return "offline";
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                return EnvironmentCompat.MEDIA_UNKNOWN;
            default:
                int type = ni.getType();
                if (type == 0) {
                    return "cell";
                }
                return type == 1 ? "wifi" : networkType;
        }
    }

    public void onConnectionChanged() {
        this.mOrmmaView.injectJavaScript(new StringBuilder("window.ormmaview.fireChangeEvent({ network: '").append(getNetwork()).append("'});").toString());
    }

    public void startNetworkListener() {
        if (this.mNetworkListenerCount == 0) {
            this.mBroadCastReceiver = new OrmmaNetworkBroadcastReceiver(this);
            this.mFilter = new IntentFilter();
            this.mFilter.addAction(InvalidManifestErrorMessages.CONNECTIVITY_INTENT_ACTION);
        }
        this.mNetworkListenerCount++;
        this.mContext.registerReceiver(this.mBroadCastReceiver, this.mFilter);
    }

    public void stopAllListeners() {
        this.mNetworkListenerCount = 0;
        try {
            this.mContext.unregisterReceiver(this.mBroadCastReceiver);
        } catch (Exception e) {
        }
    }

    public void stopNetworkListener() {
        this.mNetworkListenerCount--;
        if (this.mNetworkListenerCount == 0) {
            this.mContext.unregisterReceiver(this.mBroadCastReceiver);
            this.mBroadCastReceiver = null;
            this.mFilter = null;
        }
    }
}