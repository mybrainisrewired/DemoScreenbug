package com.isssss.myadv.kernel;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestManager {
    private static RequestQueue mRequestQueue;

    private RequestManager() {
    }

    public static RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue != null) {
            return mRequestQueue;
        }
        RequestQueue newRequestQueue = Volley.newRequestQueue(context);
        mRequestQueue = newRequestQueue;
        return newRequestQueue;
    }

    public static void init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }
}