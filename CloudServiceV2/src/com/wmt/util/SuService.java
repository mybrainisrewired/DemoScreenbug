package com.wmt.util;

import android.content.Context;

public class SuService {
    private static final String TAG = "SuService";
    private Context mContext;

    public void init(Context context) {
        Log.i(TAG, TAG);
        this.mContext = context;
        new SuListener(this.mContext).start();
    }
}