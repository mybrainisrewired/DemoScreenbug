package com.google.android.gms.panorama;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions.NoOptions;
import com.google.android.gms.common.api.Api.a;
import com.google.android.gms.common.api.Api.b;
import com.google.android.gms.common.api.Api.c;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.internal.fc;
import com.google.android.gms.internal.hw;
import com.google.android.gms.internal.hx;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;

public final class Panorama {
    public static final Api<NoOptions> API;
    public static final PanoramaApi PanoramaApi;
    public static final c<hx> wx;
    static final b<hx, NoOptions> wy;

    static {
        wx = new c();
        wy = new b<hx, NoOptions>() {
            public /* synthetic */ a a(Context context, Looper looper, fc fcVar, Object obj, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
                return c(context, looper, fcVar, (NoOptions) obj, connectionCallbacks, onConnectionFailedListener);
            }

            public hx c(Context context, Looper looper, fc fcVar, NoOptions noOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
                return new hx(context, looper, connectionCallbacks, onConnectionFailedListener);
            }

            public int getPriority() {
                return MoPubClientPositioning.NO_REPEAT;
            }
        };
        API = new Api(wy, wx, new Scope[0]);
        PanoramaApi = new hw();
    }

    private Panorama() {
    }
}