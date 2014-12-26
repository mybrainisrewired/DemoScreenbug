package com.google.android.gms.wallet;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions.HasOptions;
import com.google.android.gms.common.api.Api.c;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.fc;
import com.google.android.gms.internal.fq;
import com.google.android.gms.internal.iu;
import com.google.android.gms.internal.jf;
import com.google.android.gms.internal.jg;
import com.google.android.gms.internal.ji;
import com.google.android.gms.internal.jj;
import com.google.android.gms.internal.ka;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;
import java.util.Locale;

public final class Wallet {
    public static final Api<WalletOptions> API;
    public static final Payments Payments;
    public static final ka aco;
    public static final iu acp;
    private static final c<jg> wx;
    private static final com.google.android.gms.common.api.Api.b<jg, WalletOptions> wy;

    public static final class WalletOptions implements HasOptions {
        public final int environment;
        public final int theme;

        public static final class Builder {
            private int acq;
            private int mTheme;

            public Builder() {
                this.acq = 0;
                this.mTheme = 0;
            }

            public com.google.android.gms.wallet.Wallet.WalletOptions build() {
                return new com.google.android.gms.wallet.Wallet.WalletOptions(null);
            }

            public com.google.android.gms.wallet.Wallet.WalletOptions.Builder setEnvironment(int environment) {
                if (environment == 0 || environment == 2 || environment == 1) {
                    this.acq = environment;
                    return this;
                } else {
                    throw new IllegalArgumentException(String.format(Locale.US, "Invalid environment value %d", new Object[]{Integer.valueOf(environment)}));
                }
            }

            public com.google.android.gms.wallet.Wallet.WalletOptions.Builder setTheme(int theme) {
                if (theme == 0 || theme == 1) {
                    this.mTheme = theme;
                    return this;
                } else {
                    throw new IllegalArgumentException(String.format(Locale.US, "Invalid theme value %d", new Object[]{Integer.valueOf(theme)}));
                }
            }
        }

        private WalletOptions() {
            this(new Builder());
        }

        private WalletOptions(Builder builder) {
            this.environment = builder.acq;
            this.theme = builder.mTheme;
        }
    }

    public static abstract class a<R extends Result> extends com.google.android.gms.common.api.a.b<R, jg> {
        public a() {
            super(wx);
        }
    }

    public static abstract class b extends com.google.android.gms.wallet.Wallet.a<Status> {
        protected /* synthetic */ Result d(Status status) {
            return f(status);
        }

        protected Status f(Status status) {
            return status;
        }
    }

    static {
        wx = new c();
        wy = new com.google.android.gms.common.api.Api.b<jg, WalletOptions>() {
            public jg a(Context context, Looper looper, fc fcVar, com.google.android.gms.wallet.Wallet.WalletOptions walletOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
                fq.b(context instanceof Activity, (Object)"An Activity must be used for Wallet APIs");
                Activity activity = (Activity) context;
                if (walletOptions == null) {
                    walletOptions = new com.google.android.gms.wallet.Wallet.WalletOptions();
                }
                return new jg(activity, looper, connectionCallbacks, onConnectionFailedListener, walletOptions.environment, fcVar.getAccountName(), walletOptions.theme);
            }

            public int getPriority() {
                return MoPubClientPositioning.NO_REPEAT;
            }
        };
        API = new Api(wy, wx, new Scope[0]);
        Payments = new jf();
        aco = new jj();
        acp = new ji();
    }

    private Wallet() {
    }

    @Deprecated
    public static void changeMaskedWallet(GoogleApiClient googleApiClient, String googleTransactionId, String merchantTransactionId, int requestCode) {
        Payments.changeMaskedWallet(googleApiClient, googleTransactionId, merchantTransactionId, requestCode);
    }

    @Deprecated
    public static void checkForPreAuthorization(GoogleApiClient googleApiClient, int requestCode) {
        Payments.checkForPreAuthorization(googleApiClient, requestCode);
    }

    @Deprecated
    public static void loadFullWallet(GoogleApiClient googleApiClient, FullWalletRequest request, int requestCode) {
        Payments.loadFullWallet(googleApiClient, request, requestCode);
    }

    @Deprecated
    public static void loadMaskedWallet(GoogleApiClient googleApiClient, MaskedWalletRequest request, int requestCode) {
        Payments.loadMaskedWallet(googleApiClient, request, requestCode);
    }

    @Deprecated
    public static void notifyTransactionStatus(GoogleApiClient googleApiClient, NotifyTransactionStatusRequest request) {
        Payments.notifyTransactionStatus(googleApiClient, request);
    }
}