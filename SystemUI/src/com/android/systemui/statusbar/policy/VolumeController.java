package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.media.AudioManager;
import android.os.Vibrator;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.ToggleSlider.Listener;

public class VolumeController implements Listener {
    private static final int STREAM = 5;
    private static final String TAG = "StatusBar.VolumeController";
    private AudioManager mAudioManager;
    private Context mContext;
    private ToggleSlider mControl;
    private final boolean mHasVibrator;
    private boolean mMute;
    private int mVolume;

    public VolumeController(Context context, ToggleSlider control) {
        boolean z = false;
        this.mContext = context;
        this.mControl = control;
        Vibrator vibrator = (Vibrator) context.getSystemService("vibrator");
        this.mHasVibrator = vibrator == null ? false : vibrator.hasVibrator();
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
        if (this.mAudioManager.getRingerMode() != 2) {
            z = true;
        }
        this.mMute = z;
        this.mVolume = this.mAudioManager.getStreamVolume(STREAM);
        control.setMax(this.mAudioManager.getStreamMaxVolume(STREAM));
        control.setValue(this.mVolume);
        control.setChecked(this.mMute);
        control.setOnChangedListener(this);
    }

    public void onChanged(ToggleSlider view, boolean tracking, boolean mute, int level) {
        if (!tracking) {
            if (mute) {
                this.mAudioManager.setRingerMode(this.mHasVibrator ? 1 : 0);
            } else {
                this.mAudioManager.setRingerMode(CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL);
                this.mAudioManager.setStreamVolume(STREAM, level, CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
            }
        }
    }
}