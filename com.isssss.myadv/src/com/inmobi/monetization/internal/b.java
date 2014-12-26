package com.inmobi.monetization.internal;

import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.internal.Log;
import com.inmobi.commons.network.ErrorCode;
import com.inmobi.commons.network.Response;
import com.inmobi.commons.network.ServiceProvider;
import com.inmobi.commons.network.abstraction.INetworkListener;

// compiled from: AdController.java
class b {
    private static b c;
    private ServiceProvider a;
    private INetworkListener b;

    static {
        c = null;
    }

    private b() {
        this.a = ServiceProvider.getInstance();
    }

    public static b a() {
        if (c == null) {
            c = new b();
        }
        return c;
    }

    public void a(String str, i iVar, INetworkListener iNetworkListener) {
        this.b = iNetworkListener;
        if (InternalSDKUtil.checkNetworkAvailibility(InternalSDKUtil.getContext())) {
            Log.internal(Constants.LOG_TAG, "Fetching  Ads");
            this.a.executeTask(iVar, iNetworkListener);
        } else if (this.b != null) {
            this.b.onRequestFailed(iVar, new Response(ErrorCode.NETWORK_ERROR));
        }
    }
}