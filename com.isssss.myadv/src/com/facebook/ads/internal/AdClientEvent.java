package com.facebook.ads.internal;

import com.inmobi.androidsdk.IMBrowserActivity;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class AdClientEvent {
    private Map<String, Object> data;
    private String name;
    private int time;

    public AdClientEvent(String name, Map<String, Object> data, int time) {
        this.name = name;
        this.data = data;
        this.time = time;
    }

    public static AdClientEvent newErrorEvent(Exception ex) {
        Map<String, Object> eventData = new HashMap();
        eventData.put("ex", ex.getClass().getSimpleName());
        eventData.put("ex_msg", ex.getMessage());
        return new AdClientEvent("error", eventData, (int) (System.currentTimeMillis() / 1000));
    }

    public JSONObject getClientEventJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", this.name);
            jsonObject.put(IMBrowserActivity.EXPANDDATA, new JSONObject(this.data));
            jsonObject.put("time", this.time);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}