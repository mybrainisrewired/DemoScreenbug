package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.b;
import com.google.android.gms.internal.d.a;
import com.google.android.gms.tagmanager.cq.c;
import com.google.android.gms.tagmanager.cq.d;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class ba {
    public static c bG(String str) throws JSONException {
        a k = k(new JSONObject(str));
        d lh = c.lh();
        int i = 0;
        while (i < k.fP.length) {
            lh.a(cq.a.ld().b(b.cI.toString(), k.fP[i]).b(b.cx.toString(), dh.bX(m.ka())).b(m.kb(), k.fQ[i]).lg());
            i++;
        }
        return lh.lk();
    }

    private static a k(Object obj) throws JSONException {
        return dh.r(l(obj));
    }

    static Object l(Object obj) throws JSONException {
        if (obj instanceof JSONArray) {
            throw new RuntimeException("JSONArrays are not supported");
        } else if (JSONObject.NULL.equals(obj)) {
            throw new RuntimeException("JSON nulls are not supported");
        } else if (!(obj instanceof JSONObject)) {
            return obj;
        } else {
            JSONObject jSONObject = (JSONObject) obj;
            Map hashMap = new HashMap();
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str = (String) keys.next();
                hashMap.put(str, l(jSONObject.get(str)));
            }
            return hashMap;
        }
    }
}