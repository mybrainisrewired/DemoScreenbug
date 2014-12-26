package com.android.systemui.statusbar.policy;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.ImageView;
import com.android.systemui.statusbar.CommandQueue;
import java.util.ArrayList;

public class BluetoothController extends BroadcastReceiver {
    private static final String TAG = "StatusBar.BluetoothController";
    private int mContentDescriptionId;
    private Context mContext;
    private boolean mEnabled;
    private int mIconId;
    private ArrayList<ImageView> mIconViews;

    public BluetoothController(Context context) {
        this.mIconViews = new ArrayList();
        this.mIconId = 2130837618;
        this.mContentDescriptionId = 0;
        this.mEnabled = false;
        this.mContext = context;
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        filter.addAction("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED");
        context.registerReceiver(this, filter);
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            handleAdapterStateChange(adapter.getState());
            handleConnectionStateChange(adapter.getConnectionState());
        }
        refreshViews();
    }

    public void addIconView(ImageView v) {
        this.mIconViews.add(v);
    }

    public void handleAdapterStateChange(int adapterState) {
        this.mEnabled = adapterState == 12;
    }

    public void handleConnectionStateChange(int connectionState) {
        if (connectionState == 2) {
            this.mIconId = 2130837619;
            this.mContentDescriptionId = 2131296318;
        } else {
            this.mIconId = 2130837618;
            this.mContentDescriptionId = 2131296319;
        }
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
            handleAdapterStateChange(intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE));
        } else if (action.equals("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED")) {
            handleConnectionStateChange(intent.getIntExtra("android.bluetooth.adapter.extra.CONNECTION_STATE", 0));
        }
        refreshViews();
    }

    public void refreshViews() {
        int N = this.mIconViews.size();
        int i = 0;
        while (i < N) {
            ImageView v = (ImageView) this.mIconViews.get(i);
            v.setImageResource(this.mIconId);
            v.setVisibility(this.mEnabled ? 0 : CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            v.setContentDescription(this.mContentDescriptionId == 0 ? null : this.mContext.getString(this.mContentDescriptionId));
            i++;
        }
    }
}