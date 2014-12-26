package com.clouds.server;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.clmobile.utils.Utils;
import com.clouds.constant.ReceiverActionConstant;

public class MainActivity extends Activity {
    String TAG;

    public MainActivity() {
        this.TAG = "MainActivity";
    }

    private void checkService(Context context, String action) {
        Intent serviceIntent = new Intent();
        serviceIntent.setClass(context, CloudService.class);
        serviceIntent.setAction(action);
        context.startService(serviceIntent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkService(this, ReceiverActionConstant.ACTION_NETWORK_IS_ONLINE);
        Utils.startPluginService(this);
        Log.e(this.TAG, "start MainActivity completed!");
    }
}