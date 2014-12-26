package com.google.android.gms.drive;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions.NoOptions;
import com.google.android.gms.common.api.Api.a;
import com.google.android.gms.common.api.Api.b;
import com.google.android.gms.common.api.Api.c;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.internal.l;
import com.google.android.gms.drive.internal.n;
import com.google.android.gms.drive.internal.p;
import com.google.android.gms.internal.fc;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;
import java.util.List;

public final class Drive {
    public static final Api<NoOptions> API;
    public static final DriveApi DriveApi;
    public static final Scope EE;
    public static final Scope EF;
    public static final c EG;
    public static final Scope SCOPE_APPFOLDER;
    public static final Scope SCOPE_FILE;
    public static final c<n> wx;
    public static final b<n, NoOptions> wy;

    static {
        wx = new c();
        wy = new b<n, NoOptions>() {
            public /* synthetic */ a a(Context context, Looper looper, fc fcVar, Object obj, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
                return b(context, looper, fcVar, (NoOptions) obj, connectionCallbacks, onConnectionFailedListener);
            }

            public n b(Context context, Looper looper, fc fcVar, NoOptions noOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
                List eE = fcVar.eE();
                return new n(context, looper, fcVar, connectionCallbacks, onConnectionFailedListener, (String[]) eE.toArray(new String[eE.size()]));
            }

            public int getPriority() {
                return MoPubClientPositioning.NO_REPEAT;
            }
        };
        SCOPE_FILE = new Scope(Scopes.DRIVE_FILE);
        SCOPE_APPFOLDER = new Scope(Scopes.DRIVE_APPFOLDER);
        EE = new Scope(Scopes.DRIVE_FULL);
        EF = new Scope(Scopes.DRIVE_APPS);
        API = new Api(wy, wx, new Scope[0]);
        DriveApi = new l();
        EG = new p();
    }

    private Drive() {
    }
}