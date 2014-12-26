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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.ff.e;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.NotifyTransactionStatusRequest;
import com.google.android.gms.wallet.WalletConstants;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;

public class jg extends ff<jb> {
    private final int acq;
    private final int mTheme;
    private final Activity nS;
    private final String wG;

    private static class a extends com.google.android.gms.internal.je.a {
        private a() {
        }

        public void a(int i, FullWallet fullWallet, Bundle bundle) {
        }

        public void a(int i, MaskedWallet maskedWallet, Bundle bundle) {
        }

        public void a(int i, boolean z, Bundle bundle) {
        }

        public void a(Status status, ix ixVar, Bundle bundle) {
        }

        public void f(int i, Bundle bundle) {
        }
    }

    final class b extends a {
        private final int CV;

        public b(int i) {
            super();
            this.CV = i;
        }

        public void a(int i, FullWallet fullWallet, Bundle bundle) {
            PendingIntent pendingIntent = null;
            if (bundle != null) {
                pendingIntent = (PendingIntent) bundle.getParcelable("com.google.android.gms.wallet.EXTRA_PENDING_INTENT");
            }
            ConnectionResult connectionResult = new ConnectionResult(i, pendingIntent);
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(jg.this.nS, this.CV);
                } catch (SendIntentException e) {
                    Log.w("WalletClientImpl", "Exception starting pending intent", e);
                }
            } else {
                int i2;
                Intent intent = new Intent();
                if (connectionResult.isSuccess()) {
                    i2 = -1;
                    intent.putExtra(WalletConstants.EXTRA_FULL_WALLET, fullWallet);
                } else {
                    i2 = i == 408 ? 0 : 1;
                    intent.putExtra(WalletConstants.EXTRA_ERROR_CODE, i);
                }
                PendingIntent createPendingResult = jg.this.nS.createPendingResult(this.CV, intent, 1073741824);
                if (createPendingResult == null) {
                    Log.w("WalletClientImpl", "Null pending result returned for onFullWalletLoaded");
                } else {
                    try {
                        createPendingResult.send(i2);
                    } catch (CanceledException e2) {
                        Log.w("WalletClientImpl", "Exception setting pending result", e2);
                    }
                }
            }
        }

        public void a(int i, MaskedWallet maskedWallet, Bundle bundle) {
            PendingIntent pendingIntent = null;
            if (bundle != null) {
                pendingIntent = (PendingIntent) bundle.getParcelable("com.google.android.gms.wallet.EXTRA_PENDING_INTENT");
            }
            ConnectionResult connectionResult = new ConnectionResult(i, pendingIntent);
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(jg.this.nS, this.CV);
                } catch (SendIntentException e) {
                    Log.w("WalletClientImpl", "Exception starting pending intent", e);
                }
            } else {
                int i2;
                Intent intent = new Intent();
                if (connectionResult.isSuccess()) {
                    i2 = -1;
                    intent.putExtra(WalletConstants.EXTRA_MASKED_WALLET, maskedWallet);
                } else {
                    i2 = i == 408 ? 0 : 1;
                    intent.putExtra(WalletConstants.EXTRA_ERROR_CODE, i);
                }
                PendingIntent createPendingResult = jg.this.nS.createPendingResult(this.CV, intent, 1073741824);
                if (createPendingResult == null) {
                    Log.w("WalletClientImpl", "Null pending result returned for onMaskedWalletLoaded");
                } else {
                    try {
                        createPendingResult.send(i2);
                    } catch (CanceledException e2) {
                        Log.w("WalletClientImpl", "Exception setting pending result", e2);
                    }
                }
            }
        }

        public void a(int i, boolean z, Bundle bundle) {
            Intent intent = new Intent();
            intent.putExtra(WalletConstants.EXTRA_IS_USER_PREAUTHORIZED, z);
            PendingIntent createPendingResult = jg.this.nS.createPendingResult(this.CV, intent, 1073741824);
            if (createPendingResult == null) {
                Log.w("WalletClientImpl", "Null pending result returned for onPreAuthorizationDetermined");
            } else {
                try {
                    createPendingResult.send(-1);
                } catch (CanceledException e) {
                    Log.w("WalletClientImpl", "Exception setting pending result", e);
                }
            }
        }

        public void f(int i, Object obj) {
            fq.b(obj, (Object)"Bundle should not be null");
            ConnectionResult connectionResult = new ConnectionResult(i, (PendingIntent) obj.getParcelable("com.google.android.gms.wallet.EXTRA_PENDING_INTENT"));
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(jg.this.nS, this.CV);
                } catch (SendIntentException e) {
                    Log.w("WalletClientImpl", "Exception starting pending intent", e);
                }
            } else {
                Log.e("WalletClientImpl", "Create Wallet Objects confirmation UI will not be shown connection result: " + connectionResult);
                Intent intent = new Intent();
                intent.putExtra(WalletConstants.EXTRA_ERROR_CODE, WalletConstants.ERROR_CODE_UNKNOWN);
                PendingIntent createPendingResult = jg.this.nS.createPendingResult(this.CV, intent, 1073741824);
                if (createPendingResult == null) {
                    Log.w("WalletClientImpl", "Null pending result returned for onWalletObjectsCreated");
                } else {
                    try {
                        createPendingResult.send(1);
                    } catch (CanceledException e2) {
                        Log.w("WalletClientImpl", "Exception setting pending result", e2);
                    }
                }
            }
        }
    }

    public jg(Context context, Looper looper, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, int i, String str, int i2) {
        super(context, looper, connectionCallbacks, onConnectionFailedListener, new String[0]);
        this.nS = context;
        this.acq = i;
        this.wG = str;
        this.mTheme = i2;
    }

    public static Bundle a(int i, String str, String str2, int i2) {
        Bundle bundle = new Bundle();
        bundle.putInt("com.google.android.gms.wallet.EXTRA_ENVIRONMENT", i);
        bundle.putString("androidPackageName", str);
        if (!TextUtils.isEmpty(str2)) {
            bundle.putParcelable("com.google.android.gms.wallet.EXTRA_BUYER_ACCOUNT", new Account(str2, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE));
        }
        bundle.putInt("com.google.android.gms.wallet.EXTRA_THEME", i2);
        return bundle;
    }

    private Bundle lX() {
        return a(this.acq, this.nS.getPackageName(), this.wG, this.mTheme);
    }

    protected void a(fm fmVar, e eVar) throws RemoteException {
        fmVar.a(eVar, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE);
    }

    public void a(FullWalletRequest fullWalletRequest, int i) {
        je bVar = new b(i);
        try {
            ((jb) eM()).a(fullWalletRequest, lX(), bVar);
        } catch (RemoteException e) {
            Log.e("WalletClientImpl", "RemoteException getting full wallet", e);
            bVar.a((int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, null, Bundle.EMPTY);
        }
    }

    public void a(MaskedWalletRequest maskedWalletRequest, int i) {
        Bundle lX = lX();
        je bVar = new b(i);
        try {
            ((jb) eM()).a(maskedWalletRequest, lX, bVar);
        } catch (RemoteException e) {
            Log.e("WalletClientImpl", "RemoteException getting masked wallet", e);
            bVar.a((int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, null, Bundle.EMPTY);
        }
    }

    public void a(NotifyTransactionStatusRequest notifyTransactionStatusRequest) {
        try {
            ((jb) eM()).a(notifyTransactionStatusRequest, lX());
        } catch (RemoteException e) {
        }
    }

    protected jb aY(IBinder iBinder) {
        return com.google.android.gms.internal.jb.a.aU(iBinder);
    }

    protected String bg() {
        return "com.google.android.gms.wallet.service.BIND";
    }

    protected String bh() {
        return "com.google.android.gms.wallet.internal.IOwService";
    }

    public void cD(int i) {
        Bundle lX = lX();
        je bVar = new b(i);
        try {
            ((jb) eM()).a(lX, bVar);
        } catch (RemoteException e) {
            Log.e("WalletClientImpl", "RemoteException during checkForPreAuthorization", e);
            bVar.a((int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, false, Bundle.EMPTY);
        }
    }

    public void d(String str, String str2, int i) {
        Bundle lX = lX();
        Object bVar = new b(i);
        try {
            ((jb) eM()).a(str, str2, lX, bVar);
        } catch (RemoteException e) {
            Log.e("WalletClientImpl", "RemoteException changing masked wallet", e);
            bVar.a((int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, null, Bundle.EMPTY);
        }
    }

    protected /* synthetic */ IInterface r(IBinder iBinder) {
        return aY(iBinder);
    }
}