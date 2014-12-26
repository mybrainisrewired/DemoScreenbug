package com.android.volley.toolbox;

import android.util.Log;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonObjectRequest extends JsonRequest<JSONObject> {
    public JsonObjectRequest(int method, String url, Map params, Listener listener, ErrorListener errorListener) {
        super(method, url, params, listener, errorListener);
    }

    public JsonObjectRequest(String url, Map<String, String> params, Listener<JSONObject> listener, ErrorListener errorListener) {
        this(params == null ? 0 : 1, url, params, listener, errorListener);
    }

    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            Log.e("response_CODE", new StringBuilder(String.valueOf(response.statusCode)).append("*******").toString());
            return Response.success(new JSONObject(new String(response.data, "UTF-8")), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException e2) {
            return Response.error(new ParseError(e2));
        }
    }
}