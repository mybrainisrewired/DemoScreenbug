package com.google.android.gms.internal;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.NotifyTransactionStatusRequest;
import com.google.android.gms.wallet.Payments;
import com.google.android.gms.wallet.Wallet.b;

public class jf implements Payments {

    class AnonymousClass_1 extends b {
        final /* synthetic */ int Nx;

        AnonymousClass_1(int i) {
            this.Nx = i;
        }

        protected void a(jg jgVar) {
            jgVar.cD(this.Nx);
            a(Status.Bv);
        }
    }

    class AnonymousClass_2 extends b {
        final /* synthetic */ int Nx;
        final /* synthetic */ MaskedWalletRequest acW;

        AnonymousClass_2(MaskedWalletRequest maskedWalletRequest, int i) {
            this.acW = maskedWalletRequest;
            this.Nx = i;
        }

        protected void a(jg jgVar) {
            jgVar.a(this.acW, this.Nx);
            a(Status.Bv);
        }
    }

    class AnonymousClass_3 extends b {
        final /* synthetic */ int Nx;
        final /* synthetic */ FullWalletRequest acX;

        AnonymousClass_3(FullWalletRequest fullWalletRequest, int i) {
            this.acX = fullWalletRequest;
            this.Nx = i;
        }

        protected void a(jg jgVar) {
            jgVar.a(this.acX, this.Nx);
            a(Status.Bv);
        }
    }

    class AnonymousClass_4 extends b {
        final /* synthetic */ int Nx;
        final /* synthetic */ String acY;
        final /* synthetic */ String acZ;

        AnonymousClass_4(String str, String str2, int i) {
            this.acY = str;
            this.acZ = str2;
            this.Nx = i;
        }

        protected void a(jg jgVar) {
            jgVar.d(this.acY, this.acZ, this.Nx);
            a(Status.Bv);
        }
    }

    class AnonymousClass_5 extends b {
        final /* synthetic */ NotifyTransactionStatusRequest ada;

        AnonymousClass_5(NotifyTransactionStatusRequest notifyTransactionStatusRequest) {
            this.ada = notifyTransactionStatusRequest;
        }

        protected void a(jg jgVar) {
            jgVar.a(this.ada);
            a(Status.Bv);
        }
    }

    public void changeMaskedWallet(GoogleApiClient googleApiClient, String googleTransactionId, String merchantTransactionId, int requestCode) {
        googleApiClient.a(new AnonymousClass_4(googleTransactionId, merchantTransactionId, requestCode));
    }

    public void checkForPreAuthorization(GoogleApiClient googleApiClient, int requestCode) {
        googleApiClient.a(new AnonymousClass_1(requestCode));
    }

    public void loadFullWallet(GoogleApiClient googleApiClient, FullWalletRequest request, int requestCode) {
        googleApiClient.a(new AnonymousClass_3(request, requestCode));
    }

    public void loadMaskedWallet(GoogleApiClient googleApiClient, MaskedWalletRequest request, int requestCode) {
        googleApiClient.a(new AnonymousClass_2(request, requestCode));
    }

    public void notifyTransactionStatus(GoogleApiClient googleApiClient, NotifyTransactionStatusRequest request) {
        googleApiClient.a(new AnonymousClass_5(request));
    }
}