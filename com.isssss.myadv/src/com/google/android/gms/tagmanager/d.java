package com.google.android.gms.tagmanager;

import android.content.Context;
import android.net.Uri;
import com.google.android.gms.plus.PlusShare;
import com.inmobi.commons.analytics.iat.impl.AdTrackerConstants;
import java.util.Map;

class d implements b {
    private final Context kI;

    public d(Context context) {
        this.kI = context;
    }

    public void y(Map<String, Object> map) {
        Object obj;
        Object obj2 = map.get("gtm.url");
        if (obj2 == null) {
            obj = map.get("gtm");
            if (obj != null && obj instanceof Map) {
                obj = ((Map) obj).get(PlusShare.KEY_CALL_TO_ACTION_URL);
                if (obj != null && obj instanceof String) {
                    String queryParameter = Uri.parse((String) obj).getQueryParameter(AdTrackerConstants.REFERRER);
                    if (queryParameter != null) {
                        ay.e(this.kI, queryParameter);
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
        }
        obj = obj2;
    }
}