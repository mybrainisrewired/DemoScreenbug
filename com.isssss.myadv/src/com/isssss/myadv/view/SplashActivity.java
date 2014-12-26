package com.isssss.myadv.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.isssss.myadv.kernel.MyAlarm;

public class SplashActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyAlarm.startAlarm(getApplicationContext());
        finish();
        Log.d(" /usr/data ", "Program entrance");
    }
}