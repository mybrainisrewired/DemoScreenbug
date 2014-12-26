package com.google.android.gms.internal;

import com.inmobi.androidsdk.IMBrowserActivity;
import com.inmobi.commons.ads.cache.AdDatabaseHelper;
import com.inmobi.commons.analytics.db.AnalyticsEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class bi {
    public final String mW;
    public final String mX;
    public final List<String> mY;
    public final String mZ;
    public final List<String> na;
    public final String nb;

    public bi(JSONObject jSONObject) throws JSONException {
        String str = null;
        this.mX = jSONObject.getString(AnalyticsEvent.EVENT_ID);
        JSONArray jSONArray = jSONObject.getJSONArray("adapters");
        List arrayList = new ArrayList(jSONArray.length());
        int i = 0;
        while (i < jSONArray.length()) {
            arrayList.add(jSONArray.getString(i));
            i++;
        }
        this.mY = Collections.unmodifiableList(arrayList);
        this.mZ = jSONObject.optString("allocation_id", null);
        this.na = bo.a(jSONObject, "imp_urls");
        JSONObject optJSONObject = jSONObject.optJSONObject(AdDatabaseHelper.TABLE_AD);
        this.mW = optJSONObject != null ? optJSONObject.toString() : null;
        optJSONObject = jSONObject.optJSONObject(IMBrowserActivity.EXPANDDATA);
        if (optJSONObject != null) {
            str = optJSONObject.toString();
        }
        this.nb = str;
    }
}