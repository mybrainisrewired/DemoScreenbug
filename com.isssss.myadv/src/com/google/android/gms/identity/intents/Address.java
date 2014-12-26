package com.google.android.gms.identity.intents;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions.HasOptions;
import com.google.android.gms.common.api.Api.b;
import com.google.android.gms.common.api.Api.c;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.fc;
import com.google.android.gms.internal.fq;
import com.google.android.gms.internal.gw;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;

public final class Address {
    public static final Api<AddressOptions> API;
    static final c<gw> wx;
    private static final b<gw, AddressOptions> wy;

    public static final class AddressOptions implements HasOptions {
        public final int theme;

        public AddressOptions() {
            this.theme = 0;
        }

        public AddressOptions(int theme) {
            this.theme = theme;
        }
    }

    private static abstract class a extends com.google.android.gms.common.api.a.b<Status, gw> {
        public a() {
            super(wx);
        }

        public /* synthetic */ Result d(Status status) {
            return f(status);
        }

        public Status f(Status status) {
            return status;
        }
    }

    static class AnonymousClass_2 extends a {
        final /* synthetic */ UserAddressRequest Nw;
        final /* synthetic */ int Nx;

        AnonymousClass_2(UserAddressRequest userAddressRequest, int i) {
            this.Nw = userAddressRequest;
            this.Nx = i;
        }

        protected void a(gw gwVar) throws RemoteException {
            gwVar.a(this.Nw, this.Nx);
            a(Status.Bv);
        }
    }

    static {
        wx = new c();
        wy = new b<gw, AddressOptions>() {
            public gw a(Context context, Looper looper, fc fcVar, com.google.android.gms.identity.intents.Address.AddressOptions addressOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
                fq.b(context instanceof Activity, (Object)"An Activity must be used for Address APIs");
                if (addressOptions == null) {
                    addressOptions = new com.google.android.gms.identity.intents.Address.AddressOptions();
                }
                return new gw((Activity) context, looper, connectionCallbacks, onConnectionFailedListener, fcVar.getAccountName(), addressOptions.theme);
            }

            public int getPriority() {
                return MoPubClientPositioning.NO_REPEAT;
            }
        };
        API = new Api(wy, wx, new Scope[0]);
    }

    public static void requestUserAddress(GoogleApiClient googleApiClient, UserAddressRequest request, int requestCode) {
        googleApiClient.a(new AnonymousClass_2(request, requestCode));
    }
}