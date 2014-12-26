package com.google.android.gms.internal;

import android.accounts.Account;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.identity.intents.AddressConstants.ErrorCodes;
import com.google.android.gms.identity.intents.AddressConstants.Extras;
import com.google.android.gms.identity.intents.UserAddressRequest;
import com.google.android.gms.internal.ff.e;

public class gw extends ff<gy> {
    private a NA;
    private final int mTheme;
    private Activity nS;
    private final String wG;

    public static final class a extends com.google.android.gms.internal.gx.a {
        private final int CV;
        private Activity nS;

        public a(int i, Activity activity) {
            this.CV = i;
            this.nS = activity;
        }

        private void setActivity(Activity activity) {
            this.nS = activity;
        }

        public void d(int i, Bundle bundle) {
            PendingIntent createPendingResult;
            if (i == 1) {
                Intent intent = new Intent();
                intent.putExtras(bundle);
                createPendingResult = this.nS.createPendingResult(this.CV, intent, 1073741824);
                if (createPendingResult != null) {
                    try {
                        createPendingResult.send(1);
                    } catch (CanceledException e) {
                        Log.w("AddressClientImpl", "Exception settng pending result", e);
                    }
                }
            } else {
                createPendingResult = null;
                if (bundle != null) {
                    createPendingResult = (PendingIntent) bundle.getParcelable("com.google.android.gms.identity.intents.EXTRA_PENDING_INTENT");
                }
                ConnectionResult connectionResult = new ConnectionResult(i, createPendingResult);
                if (connectionResult.hasResolution()) {
                    try {
                        connectionResult.startResolutionForResult(this.nS, this.CV);
                    } catch (SendIntentException e2) {
                        Log.w("AddressClientImpl", "Exception starting pending intent", e2);
                    }
                } else {
                    try {
                        createPendingResult = this.nS.createPendingResult(this.CV, new Intent(), 1073741824);
                        if (createPendingResult != null) {
                            createPendingResult.send(1);
                        }
                    } catch (CanceledException e3) {
                        Log.w("AddressClientImpl", "Exception setting pending result", e3);
                    }
                }
            }
        }
    }

    public gw(Context context, Looper looper, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, String str, int i) {
        super(context, looper, connectionCallbacks, onConnectionFailedListener, new String[0]);
        this.wG = str;
        this.nS = context;
        this.mTheme = i;
    }

    protected gy R(IBinder iBinder) {
        return com.google.android.gms.internal.gy.a.T(iBinder);
    }

    public void a(UserAddressRequest userAddressRequest, int i) {
        hO();
        this.NA = new a(i, this.nS);
        try {
            Bundle bundle = new Bundle();
            bundle.putString("com.google.android.gms.identity.intents.EXTRA_CALLING_PACKAGE_NAME", getContext().getPackageName());
            if (!TextUtils.isEmpty(this.wG)) {
                bundle.putParcelable("com.google.android.gms.identity.intents.EXTRA_ACCOUNT", new Account(this.wG, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE));
            }
            bundle.putInt("com.google.android.gms.identity.intents.EXTRA_THEME", this.mTheme);
            hN().a(this.NA, userAddressRequest, bundle);
        } catch (RemoteException e) {
            Log.e("AddressClientImpl", "Exception requesting user address", e);
            bundle = new Bundle();
            bundle.putInt(Extras.EXTRA_ERROR_CODE, ErrorCodes.ERROR_CODE_NO_APPLICABLE_ADDRESSES);
            this.NA.d(1, bundle);
        }
    }

    protected void a(fm fmVar, e eVar) throws RemoteException {
        fmVar.d(eVar, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, getContext().getPackageName());
    }

    protected String bg() {
        return "com.google.android.gms.identity.service.BIND";
    }

    protected String bh() {
        return "com.google.android.gms.identity.intents.internal.IAddressService";
    }

    public void disconnect() {
        super.disconnect();
        if (this.NA != null) {
            this.NA.setActivity(null);
            this.NA = null;
        }
    }

    protected gy hN() {
        return (gy) super.eM();
    }

    protected void hO() {
        super.bT();
    }

    protected /* synthetic */ IInterface r(IBinder iBinder) {
        return R(iBinder);
    }
}