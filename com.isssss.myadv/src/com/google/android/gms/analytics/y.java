package com.google.android.gms.analytics;

import android.text.TextUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

class y {
    static String a(x xVar, long j) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(xVar.cO());
        if (xVar.cQ() > 0) {
            long cQ = j - xVar.cQ();
            if (cQ >= 0) {
                stringBuilder.append("&qt").append("=").append(cQ);
            }
        }
        stringBuilder.append("&z").append("=").append(xVar.cP());
        return stringBuilder.toString();
    }

    static String encode(String input) {
        try {
            return URLEncoder.encode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("URL encoding failed for: " + input);
        }
    }

    static Map<String, String> v(Map<String, String> map) {
        Map<String, String> hashMap = new HashMap();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            if (((String) entry.getKey()).startsWith("&") && entry.getValue() != null) {
                CharSequence substring = ((String) entry.getKey()).substring(1);
                if (!TextUtils.isEmpty(substring)) {
                    hashMap.put(substring, entry.getValue());
                }
            }
        }
        return hashMap;
    }
}