package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.mediation.MediationAdapter;
import com.google.android.gms.ads.mediation.MediationBannerAdapter;
import com.google.android.gms.ads.mediation.MediationInterstitialAdapter;
import com.google.android.gms.dynamic.d;
import com.google.android.gms.dynamic.e;
import com.google.android.gms.internal.br.a;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import org.json.JSONObject;

public final class bu extends a {
    private final MediationAdapter nE;
    private final Bundle nF;

    public bu(MediationAdapter mediationAdapter, Bundle bundle) {
        this.nE = mediationAdapter;
        this.nF = bundle;
    }

    private Bundle a(String str, int i, String str2) throws RemoteException {
        dw.z("Server parameters: " + str);
        Bundle bundle = new Bundle();
        if (str != null) {
            JSONObject jSONObject = new JSONObject(str);
            Bundle bundle2 = new Bundle();
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str3 = (String) keys.next();
                bundle2.putString(str3, jSONObject.getString(str3));
            }
            bundle = bundle2;
        }
        if (this.nE instanceof AdMobAdapter) {
            bundle.putString("adJson", str2);
            bundle.putInt("tagForChildDirectedTreatment", i);
        }
        return bundle;
    }

    public void a(d dVar, ah ahVar, String str, bs bsVar) throws RemoteException {
        a(dVar, ahVar, str, null, bsVar);
    }

    public void a(d dVar, ah ahVar, String str, String str2, bs bsVar) throws RemoteException {
        if (this.nE instanceof MediationInterstitialAdapter) {
            dw.v("Requesting interstitial ad from adapter.");
            MediationInterstitialAdapter mediationInterstitialAdapter = (MediationInterstitialAdapter) this.nE;
            mediationInterstitialAdapter.requestInterstitialAd((Context) e.d(dVar), new bv(bsVar), a(str, ahVar.lL, str2), new bt(new Date(ahVar.lH), ahVar.lI, ahVar.lJ != null ? new HashSet(ahVar.lJ) : null, ahVar.lK, ahVar.lL), this.nF);
        } else {
            dw.z("MediationAdapter is not a MediationInterstitialAdapter: " + this.nE.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void a(d dVar, ak akVar, ah ahVar, String str, bs bsVar) throws RemoteException {
        a(dVar, akVar, ahVar, str, null, bsVar);
    }

    public void a(d dVar, ak akVar, ah ahVar, String str, String str2, bs bsVar) throws RemoteException {
        if (this.nE instanceof MediationBannerAdapter) {
            dw.v("Requesting banner ad from adapter.");
            MediationBannerAdapter mediationBannerAdapter = (MediationBannerAdapter) this.nE;
            mediationBannerAdapter.requestBannerAd((Context) e.d(dVar), new bv(bsVar), a(str, ahVar.lL, str2), com.google.android.gms.ads.a.a(akVar.width, akVar.height, akVar.lS), new bt(new Date(ahVar.lH), ahVar.lI, ahVar.lJ != null ? new HashSet(ahVar.lJ) : null, ahVar.lK, ahVar.lL), this.nF);
        } else {
            dw.z("MediationAdapter is not a MediationBannerAdapter: " + this.nE.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void destroy() throws RemoteException {
        this.nE.onDestroy();
    }

    public d getView() throws RemoteException {
        if (this.nE instanceof MediationBannerAdapter) {
            return e.h(((MediationBannerAdapter) this.nE).getBannerView());
        }
        dw.z("MediationAdapter is not a MediationBannerAdapter: " + this.nE.getClass().getCanonicalName());
        throw new RemoteException();
    }

    public void pause() throws RemoteException {
        this.nE.onPause();
    }

    public void resume() throws RemoteException {
        this.nE.onResume();
    }

    public void showInterstitial() throws RemoteException {
        if (this.nE instanceof MediationInterstitialAdapter) {
            dw.v("Showing interstitial from adapter.");
            ((MediationInterstitialAdapter) this.nE).showInterstitial();
        } else {
            dw.z("MediationAdapter is not a MediationInterstitialAdapter: " + this.nE.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }
}