package com.google.android.gms.internal;

import com.millennialmedia.android.MMAdView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class bj {
    public final List<bi> nc;
    public final long nd;
    public final List<String> ne;
    public final List<String> nf;
    public final List<String> ng;
    public final String nh;
    public final long ni;
    public int nj;
    public int nk;

    public bj(String str) throws JSONException {
        JSONObject jSONObject = new JSONObject(str);
        if (dw.n(MMAdView.TRANSITION_UP)) {
            dw.y("Mediation Response JSON: " + jSONObject.toString(MMAdView.TRANSITION_UP));
        }
        JSONArray jSONArray = jSONObject.getJSONArray("ad_networks");
        List arrayList = new ArrayList(jSONArray.length());
        int i = -1;
        int i2 = 0;
        while (i2 < jSONArray.length()) {
            bi biVar = new bi(jSONArray.getJSONObject(i2));
            arrayList.add(biVar);
            if (i < 0 && a(biVar)) {
                i = i2;
            }
            i2++;
        }
        this.nj = i;
        this.nk = jSONArray.length();
        this.nc = Collections.unmodifiableList(arrayList);
        this.nh = jSONObject.getString("qdata");
        JSONObject optJSONObject = jSONObject.optJSONObject("settings");
        if (optJSONObject != null) {
            this.nd = optJSONObject.optLong("ad_network_timeout_millis", -1);
            this.ne = bo.a(optJSONObject, "click_urls");
            this.nf = bo.a(optJSONObject, "imp_urls");
            this.ng = bo.a(optJSONObject, "nofill_urls");
            long optLong = optJSONObject.optLong("refresh", -1);
            this.ni = optLong > 0 ? optLong * 1000 : -1;
        } else {
            this.nd = -1;
            this.ne = null;
            this.nf = null;
            this.ng = null;
            this.ni = -1;
        }
    }

    private boolean a(bi biVar) {
        Iterator it = biVar.mY.iterator();
        while (it.hasNext()) {
            if (((String) it.next()).equals("com.google.ads.mediation.admob.AdMobAdapter")) {
                return true;
            }
        }
        return false;
    }
}