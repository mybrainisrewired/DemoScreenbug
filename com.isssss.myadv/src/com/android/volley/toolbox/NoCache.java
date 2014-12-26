package com.android.volley.toolbox;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;

public class NoCache implements Cache {
    public void clear() {
    }

    public Entry get(String key) {
        return null;
    }

    public void initialize() {
    }

    public void invalidate(String key, boolean fullExpire) {
    }

    public void put(String key, Entry entry) {
    }

    public void remove(String key) {
    }
}