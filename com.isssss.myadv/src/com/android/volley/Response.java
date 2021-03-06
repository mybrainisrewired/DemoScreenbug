package com.android.volley;

import com.android.volley.Cache.Entry;

public class Response<T> {
    public final Entry cacheEntry;
    public final VolleyError error;
    public boolean intermediate;
    public final T result;

    public static interface ErrorListener {
        void onErrorResponse(VolleyError volleyError);
    }

    public static interface Listener<T> {
        void onResponse(T t);
    }

    private Response(VolleyError error) {
        this.intermediate = false;
        this.result = null;
        this.cacheEntry = null;
        this.error = error;
    }

    private Response(T result, Entry cacheEntry) {
        this.intermediate = false;
        this.result = result;
        this.cacheEntry = cacheEntry;
        this.error = null;
    }

    public static <T> Response<T> error(VolleyError error) {
        return new Response(error);
    }

    public static <T> Response<T> success(T result, Entry cacheEntry) {
        return new Response(result, cacheEntry);
    }

    public boolean isSuccess() {
        return this.error == null;
    }
}