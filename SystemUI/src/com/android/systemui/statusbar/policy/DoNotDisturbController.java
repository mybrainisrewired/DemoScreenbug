package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class DoNotDisturbController implements OnCheckedChangeListener, OnSharedPreferenceChangeListener {
    private static final String TAG = "StatusBar.DoNotDisturbController";
    private CompoundButton mCheckBox;
    private Context mContext;
    private boolean mDoNotDisturb;
    SharedPreferences mPrefs;

    public DoNotDisturbController(Context context, CompoundButton checkbox) {
        boolean z = false;
        this.mContext = context;
        this.mPrefs = Prefs.read(context);
        this.mPrefs.registerOnSharedPreferenceChangeListener(this);
        this.mDoNotDisturb = this.mPrefs.getBoolean(Prefs.DO_NOT_DISTURB_PREF, false);
        this.mCheckBox = checkbox;
        checkbox.setOnCheckedChangeListener(this);
        if (!this.mDoNotDisturb) {
            z = true;
        }
        checkbox.setChecked(z);
    }

    public void onCheckedChanged(CompoundButton view, boolean checked) {
        boolean value = !checked;
        if (value != this.mDoNotDisturb) {
            Editor editor = Prefs.edit(this.mContext);
            editor.putBoolean(Prefs.DO_NOT_DISTURB_PREF, value);
            editor.apply();
        }
    }

    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        boolean z = false;
        boolean val = prefs.getBoolean(Prefs.DO_NOT_DISTURB_PREF, false);
        if (val != this.mDoNotDisturb) {
            this.mDoNotDisturb = val;
            CompoundButton compoundButton = this.mCheckBox;
            if (!val) {
                z = true;
            }
            compoundButton.setChecked(z);
        }
    }

    public void release() {
        this.mPrefs.unregisterOnSharedPreferenceChangeListener(this);
    }
}