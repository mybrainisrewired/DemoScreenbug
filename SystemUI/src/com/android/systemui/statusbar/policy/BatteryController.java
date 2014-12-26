package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.R;
import java.util.ArrayList;

public class BatteryController extends BroadcastReceiver {
    private static final String TAG = "StatusBar.BatteryController";
    private Context mContext;
    private ArrayList<ImageView> mIconViews;
    private ArrayList<TextView> mLabelViews;

    public BatteryController(Context context) {
        this.mIconViews = new ArrayList();
        this.mLabelViews = new ArrayList();
        this.mContext = context;
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.BATTERY_CHANGED");
        context.registerReceiver(this, filter);
    }

    public void addIconView(ImageView v) {
        this.mIconViews.add(v);
    }

    public void addLabelView(TextView v) {
        this.mLabelViews.add(v);
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BATTERY_CHANGED")) {
            int level = intent.getIntExtra("level", 0);
            boolean plugged;
            if (intent.getIntExtra("plugged", 0) != 0) {
                plugged = true;
            } else {
                plugged = false;
            }
            int icon = plugged ? R.drawable.stat_sys_battery_charge : R.drawable.stat_sys_battery;
            int N = this.mIconViews.size();
            int i = 0;
            while (i < N) {
                ImageView v = (ImageView) this.mIconViews.get(i);
                v.setImageResource(icon);
                v.setImageLevel(level);
                v.setContentDescription(this.mContext.getString(R.string.accessibility_battery_level, new Object[]{Integer.valueOf(level)}));
                i++;
            }
            N = this.mLabelViews.size();
            i = 0;
            while (i < N) {
                ((TextView) this.mLabelViews.get(i)).setText(this.mContext.getString(R.string.status_bar_settings_battery_meter_format, new Object[]{Integer.valueOf(level)}));
                i++;
            }
        }
    }
}