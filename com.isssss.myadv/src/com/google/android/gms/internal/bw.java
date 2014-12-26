package com.google.android.gms.internal;

import android.app.Activity;
import android.os.RemoteException;
import com.google.ads.mediation.MediationAdapter;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationServerParameters;
import com.google.ads.mediation.NetworkExtras;
import com.google.android.gms.dynamic.d;
import com.google.android.gms.dynamic.e;
import com.google.android.gms.internal.br.a;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;

public final class bw<NETWORK_EXTRAS extends NetworkExtras, SERVER_PARAMETERS extends MediationServerParameters> extends a {
    private final MediationAdapter<NETWORK_EXTRAS, SERVER_PARAMETERS> nH;
    private final NETWORK_EXTRAS nI;

    public bw(MediationAdapter<NETWORK_EXTRAS, SERVER_PARAMETERS> mediationAdapter, NETWORK_EXTRAS network_extras) {
        this.nH = mediationAdapter;
        this.nI = network_extras;
    }

    private SERVER_PARAMETERS b(String str, int i, String str2) throws RemoteException {
        Map hashMap;
        if (str != null) {
            JSONObject jSONObject = new JSONObject(str);
            hashMap = new HashMap(jSONObject.length());
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str3 = (String) keys.next();
                hashMap.put(str3, jSONObject.getString(str3));
            }
        } else {
            HashMap hashMap2 = new HashMap(0);
        }
        Class serverParametersType = this.nH.getServerParametersType();
        if (serverParametersType == null) {
            return null;
        }
        MediationServerParameters mediationServerParameters = (MediationServerParameters) serverParametersType.newInstance();
        mediationServerParameters.load(hashMap);
        return mediationServerParameters;
    }

    public void a(d dVar, ah ahVar, String str, bs bsVar) throws RemoteException {
        a(dVar, ahVar, str, null, bsVar);
    }

    public void a(d dVar, ah ahVar, String str, String str2, bs bsVar) throws RemoteException {
        if (this.nH instanceof MediationInterstitialAdapter) {
            dw.v("Requesting interstitial ad from adapter.");
            ((MediationInterstitialAdapter) this.nH).requestInterstitialAd(new bx(bsVar), (Activity) e.d(dVar), b(str, ahVar.lL, str2), by.e(ahVar), this.nI);
        } else {
            dw.z("MediationAdapter is not a MediationInterstitialAdapter: " + this.nH.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void a(d dVar, ak akVar, ah ahVar, String str, bs bsVar) throws RemoteException {
        a(dVar, akVar, ahVar, str, null, bsVar);
    }

    public void a(d dVar, ak akVar, ah ahVar, String str, String str2, bs bsVar) throws RemoteException {
        if (this.nH instanceof MediationBannerAdapter) {
            dw.v("Requesting banner ad from adapter.");
            ((MediationBannerAdapter) this.nH).requestBannerAd(new bx(bsVar), (Activity) e.d(dVar), b(str, ahVar.lL, str2), by.b(akVar), by.e(ahVar), this.nI);
        } else {
            dw.z("MediationAdapter is not a MediationBannerAdapter: " + this.nH.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void destroy() throws RemoteException {
        this.nH.destroy();
    }

    public d getView() throws RemoteException {
        if (this.nH instanceof MediationBannerAdapter) {
            return e.h(((MediationBannerAdapter) this.nH).getBannerView());
        }
        dw.z("MediationAdapter is not a MediationBannerAdapter: " + this.nH.getClass().getCanonicalName());
        throw new RemoteException();
    }

    public void pause() throws RemoteException {
        throw new RemoteException();
    }

    public void resume() throws RemoteException {
        throw new RemoteException();
    }

    public void showInterstitial() throws RemoteException {
        if (this.nH instanceof MediationInterstitialAdapter) {
            dw.v("Showing interstitial from adapter.");
            ((MediationInterstitialAdapter) this.nH).showInterstitial();
        } else {
            dw.z("MediationAdapter is not a MediationInterstitialAdapter: " + this.nH.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }
}