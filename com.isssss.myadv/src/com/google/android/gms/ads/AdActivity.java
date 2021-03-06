package com.google.android.gms.ads;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.google.android.gms.internal.cj;
import com.google.android.gms.internal.ck;
import com.google.android.gms.internal.dw;

public final class AdActivity extends Activity {
    public static final String CLASS_NAME = "com.google.android.gms.ads.AdActivity";
    public static final String SIMPLE_CLASS_NAME = "AdActivity";
    private ck ko;

    private void N() {
        if (this.ko != null) {
            try {
                this.ko.N();
            } catch (RemoteException e) {
                dw.c("Could not forward setContentViewSet to ad overlay:", e);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ko = cj.a(this);
        if (this.ko == null) {
            dw.z("Could not create ad overlay.");
            finish();
        } else {
            try {
                this.ko.onCreate(savedInstanceState);
            } catch (RemoteException e) {
                dw.c("Could not forward onCreate to ad overlay:", e);
                finish();
            }
        }
    }

    protected void onDestroy() {
        try {
            if (this.ko != null) {
                this.ko.onDestroy();
            }
        } catch (RemoteException e) {
            dw.c("Could not forward onDestroy to ad overlay:", e);
        }
        super.onDestroy();
    }

    protected void onPause() {
        try {
            if (this.ko != null) {
                this.ko.onPause();
            }
        } catch (RemoteException e) {
            dw.c("Could not forward onPause to ad overlay:", e);
            finish();
        }
        super.onPause();
    }

    protected void onRestart() {
        super.onRestart();
        try {
            if (this.ko != null) {
                this.ko.onRestart();
            }
        } catch (RemoteException e) {
            dw.c("Could not forward onRestart to ad overlay:", e);
            finish();
        }
    }

    protected void onResume() {
        super.onResume();
        try {
            if (this.ko != null) {
                this.ko.onResume();
            }
        } catch (RemoteException e) {
            dw.c("Could not forward onResume to ad overlay:", e);
            finish();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        try {
            if (this.ko != null) {
                this.ko.onSaveInstanceState(outState);
            }
        } catch (RemoteException e) {
            dw.c("Could not forward onSaveInstanceState to ad overlay:", e);
            finish();
        }
        super.onSaveInstanceState(outState);
    }

    protected void onStart() {
        super.onStart();
        try {
            if (this.ko != null) {
                this.ko.onStart();
            }
        } catch (RemoteException e) {
            dw.c("Could not forward onStart to ad overlay:", e);
            finish();
        }
    }

    protected void onStop() {
        try {
            if (this.ko != null) {
                this.ko.onStop();
            }
        } catch (RemoteException e) {
            dw.c("Could not forward onStop to ad overlay:", e);
            finish();
        }
        super.onStop();
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        N();
    }

    public void setContentView(View view) {
        super.setContentView(view);
        N();
    }

    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        N();
    }
}