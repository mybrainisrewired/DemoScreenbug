package com.mopub.common.util;

import com.mopub.common.Preconditions;
import com.mopub.common.logging.MoPubLog;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Json {
    public static <T> T getJsonValue(JSONObject jsonObject, String key, Class<T> valueClass) {
        if (jsonObject == null || key == null || valueClass == null) {
            throw new IllegalArgumentException("Cannot pass any null argument to getJsonValue");
        }
        Object object = jsonObject.opt(key);
        if (object == null) {
            MoPubLog.w(new StringBuilder("Tried to get Json value with key: ").append(key).append(", but it was null").toString());
            return null;
        } else if (valueClass.isInstance(object)) {
            return valueClass.cast(object);
        } else {
            MoPubLog.w(new StringBuilder("Tried to get Json value with key: ").append(key).append(", of type: ").append(valueClass.toString()).append(", its type did not match").toString());
            return null;
        }
    }

    public static String[] jsonArrayToStringArray(String jsonString) {
        try {
            JSONArray jsonArray = ((JSONObject) new JSONTokener(new StringBuilder("{key:").append(jsonString).append("}").toString()).nextValue()).getJSONArray("key");
            String[] result = new String[jsonArray.length()];
            int i = 0;
            while (i < result.length) {
                result[i] = jsonArray.getString(i);
                i++;
            }
            return result;
        } catch (JSONException e) {
            return new String[0];
        }
    }

    public static Map<String, String> jsonStringToMap(String jsonParams) throws Exception {
        Map<String, String> jsonMap = new HashMap();
        if (!(jsonParams == null || jsonParams.equals(Preconditions.EMPTY_ARGUMENTS))) {
            JSONObject jsonObject = (JSONObject) new JSONTokener(jsonParams).nextValue();
            Iterator<?> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                jsonMap.put(key, jsonObject.getString(key));
            }
        }
        return jsonMap;
    }

    public static String mapToJsonString(Map<String, String> map) {
        if (map == null) {
            return "{}";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        boolean first = true;
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = (Entry) it.next();
            if (!first) {
                builder.append(",");
            }
            builder.append("\"");
            builder.append((String) entry.getKey());
            builder.append("\":\"");
            builder.append((String) entry.getValue());
            builder.append("\"");
            first = false;
        }
        builder.append("}");
        return builder.toString();
    }
}