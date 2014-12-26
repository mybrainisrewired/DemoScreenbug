package com.google.android.gms.internal;

import android.text.TextUtils;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class gp {
    private static final Pattern Es;
    private static final Pattern Et;

    static {
        Es = Pattern.compile("\\\\.");
        Et = Pattern.compile("[\\\\\"/\b\f\n\r\t]");
    }

    public static String av(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        Matcher matcher = Et.matcher(str);
        StringBuffer stringBuffer = null;
        while (matcher.find()) {
            if (stringBuffer == null) {
                stringBuffer = new StringBuffer();
            }
            switch (matcher.group().charAt(0)) {
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    matcher.appendReplacement(stringBuffer, "\\\\b");
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    matcher.appendReplacement(stringBuffer, "\\\\t");
                    break;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    matcher.appendReplacement(stringBuffer, "\\\\n");
                    break;
                case ApiEventType.API_MRAID_RESIZE:
                    matcher.appendReplacement(stringBuffer, "\\\\f");
                    break;
                case ApiEventType.API_MRAID_CLOSE:
                    matcher.appendReplacement(stringBuffer, "\\\\r");
                    break;
                case ApiEventType.API_MRAID_UNMUTE_AUDIO:
                    matcher.appendReplacement(stringBuffer, "\\\\\\\"");
                    break;
                case ApiEventType.API_MRAID_PAUSE_VIDEO:
                    matcher.appendReplacement(stringBuffer, "\\\\/");
                    break;
                case '\\':
                    matcher.appendReplacement(stringBuffer, "\\\\\\\\");
                    break;
                default:
                    break;
            }
        }
        if (stringBuffer == null) {
            return str;
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    public static boolean d(Object obj, Object obj2) {
        if (obj instanceof JSONObject && obj2 instanceof JSONObject) {
            JSONObject jSONObject = (JSONObject) obj;
            JSONObject jSONObject2 = (JSONObject) obj2;
            if (jSONObject.length() != jSONObject2.length()) {
                return false;
            }
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str = (String) keys.next();
                if (!jSONObject2.has(str)) {
                    return false;
                }
                try {
                    if (!d(jSONObject.get(str), jSONObject2.get(str))) {
                        return false;
                    }
                } catch (JSONException e) {
                    return false;
                }
            }
            return true;
        } else if (!(obj instanceof JSONArray) || !(obj2 instanceof JSONArray)) {
            return obj.equals(obj2);
        } else {
            JSONArray jSONArray = (JSONArray) obj;
            JSONArray jSONArray2 = (JSONArray) obj2;
            if (jSONArray.length() != jSONArray2.length()) {
                return false;
            }
            int i = 0;
            while (i < jSONArray.length()) {
                try {
                    if (!d(jSONArray.get(i), jSONArray2.get(i))) {
                        return false;
                    }
                    i++;
                } catch (JSONException e2) {
                    return false;
                }
            }
            return true;
        }
    }
}