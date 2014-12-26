package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioSystem;
import android.util.Slog;
import android.view.DisplayManager;
import com.android.systemui.recent.RecentsCallback;
import com.android.systemui.statusbar.CommandQueue;

public class DisplayController extends BroadcastReceiver {
    private static final boolean SHOW_HDMIPLUG_IN_CALL = true;
    private static final boolean SHOW_TVPLUG_IN_CALL = true;
    private static final String TAG = "StatusBar.DisplayController";
    private Context mContext;
    private DisplayHotPlugPolicy mDispHotPolicy;
    private final DisplayManager mDisplayManager;

    private class StatusBarPadHotPlug implements DisplayHotPlugPolicy {
        public int mDisplay_mode;

        StatusBarPadHotPlug() {
            this.mDisplay_mode = 3;
        }

        private void onHdmiPlugIn(Intent intent) {
            Slog.d(TAG, "onHdmiPlugIn Starting!\n");
            int hdmi_mode = this.mDisplay_mode == 4 ? CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL : DisplayController.this.mDisplayManager.getMaxHdmiMode();
            if (this.mDisplay_mode == 3) {
                DisplayController.this.mDisplayManager.setDisplayParameter(1, RecentsCallback.SWIPE_DOWN, hdmi_mode);
                DisplayController.this.mDisplayManager.setDisplayMode(RecentsCallback.SWIPE_DOWN);
                AudioSystem.setParameters("routing=1024");
            } else if (this.mDisplay_mode == 4) {
                DisplayController.this.mDisplayManager.setDisplayParameter(1, RecentsCallback.SWIPE_DOWN, hdmi_mode);
                DisplayController.this.mDisplayManager.setDisplayMode(CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
                AudioSystem.setParameters("routing=1024");
            } else if (this.mDisplay_mode == 5) {
                DisplayController.this.mDisplayManager.setDisplayParameter(0, RecentsCallback.SWIPE_DOWN, hdmi_mode);
                DisplayController.this.mDisplayManager.setDisplayMode(5);
                AudioSystem.setParameters("routing=1024");
            } else if (this.mDisplay_mode == 6) {
                DisplayController.this.mDisplayManager.setDisplayParameter(0, RecentsCallback.SWIPE_DOWN, hdmi_mode);
                DisplayController.this.mDisplayManager.setDisplayMode(6);
                AudioSystem.setParameters("routing=1024");
            } else if (this.mDisplay_mode == 7) {
                DisplayController.this.mDisplayManager.setDisplayParameter(0, RecentsCallback.SWIPE_DOWN, hdmi_mode);
                DisplayController.this.mDisplayManager.setDisplayMode(7);
                AudioSystem.setParameters("routing=1024");
            } else if (this.mDisplay_mode == 8) {
                DisplayController.this.mDisplayManager.setDisplayParameter(0, RecentsCallback.SWIPE_DOWN, hdmi_mode);
                DisplayController.this.mDisplayManager.setDisplayMode(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
                AudioSystem.setParameters("routing=1024");
            }
        }

        private void onHdmiPlugOut(Intent intent) {
            Slog.d(TAG, "onHdmiPlugOut Starting!\n");
            if (this.mDisplay_mode == 3 || this.mDisplay_mode == 4) {
                DisplayController.this.mDisplayManager.setDisplayParameter(1, 0, 0);
                DisplayController.this.mDisplayManager.setDisplayMode(0);
                AudioSystem.setParameters("routing=2");
            } else if (this.mDisplay_mode == 5) {
                DisplayController.this.mDisplayManager.setDisplayParameter(0, 1, 0);
                DisplayController.this.mDisplayManager.setDisplayMode(5);
                AudioSystem.setParameters("routing=2");
            } else if (this.mDisplay_mode == 6) {
                DisplayController.this.mDisplayManager.setDisplayParameter(0, 1, 0);
                DisplayController.this.mDisplayManager.setDisplayMode(6);
                AudioSystem.setParameters("routing=2");
            } else if (this.mDisplay_mode == 7) {
                DisplayController.this.mDisplayManager.setDisplayParameter(0, 1, 0);
                DisplayController.this.mDisplayManager.setDisplayMode(7);
                AudioSystem.setParameters("routing=2");
            } else if (this.mDisplay_mode == 8) {
                DisplayController.this.mDisplayManager.setDisplayParameter(0, 1, 0);
                DisplayController.this.mDisplayManager.setDisplayMode(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
                AudioSystem.setParameters("routing=2");
            }
        }

        private void onTvDacCVBSPlugIn(Intent intent) {
            if (this.mDisplay_mode == 3) {
                DisplayController.this.mDisplayManager.setDisplayParameter(1, CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL, 14);
                DisplayController.this.mDisplayManager.setDisplayMode(RecentsCallback.SWIPE_DOWN);
            } else if (this.mDisplay_mode == 4) {
                DisplayController.this.mDisplayManager.setDisplayParameter(1, CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL, 14);
                DisplayController.this.mDisplayManager.setDisplayMode(CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
            } else if (this.mDisplay_mode == 5) {
                DisplayController.this.mDisplayManager.setDisplayParameter(0, CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL, 14);
                DisplayController.this.mDisplayManager.setDisplayMode(5);
            } else if (this.mDisplay_mode == 6) {
                DisplayController.this.mDisplayManager.setDisplayParameter(0, CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL, 14);
                DisplayController.this.mDisplayManager.setDisplayMode(6);
            }
        }

        private void onTvDacPlugOut(Intent intent) {
            Slog.d(TAG, "onTvDacPlugOut Starting!\n");
            if (this.mDisplay_mode == 3 || this.mDisplay_mode == 4) {
                DisplayController.this.mDisplayManager.setDisplayParameter(1, 0, 0);
                DisplayController.this.mDisplayManager.setDisplayMode(0);
            } else if (this.mDisplay_mode == 5) {
                DisplayController.this.mDisplayManager.setDisplayParameter(0, 1, 0);
                DisplayController.this.mDisplayManager.setDisplayMode(5);
            } else if (this.mDisplay_mode == 6) {
                DisplayController.this.mDisplayManager.setDisplayParameter(0, 1, 0);
                DisplayController.this.mDisplayManager.setDisplayMode(6);
            }
        }

        private void onTvDacYPbPrPlugIn(Intent intent) {
            if (this.mDisplay_mode == 3) {
                DisplayController.this.mDisplayManager.setDisplayParameter(1, CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL, CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
                DisplayController.this.mDisplayManager.setDisplayMode(RecentsCallback.SWIPE_DOWN);
            } else if (this.mDisplay_mode == 4) {
                DisplayController.this.mDisplayManager.setDisplayParameter(1, CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL, CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
                DisplayController.this.mDisplayManager.setDisplayMode(CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
            } else if (this.mDisplay_mode == 5) {
                DisplayController.this.mDisplayManager.setDisplayParameter(0, CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL, CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
                DisplayController.this.mDisplayManager.setDisplayMode(5);
            } else if (this.mDisplay_mode == 6) {
                DisplayController.this.mDisplayManager.setDisplayParameter(0, CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL, CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
                DisplayController.this.mDisplayManager.setDisplayMode(6);
            }
        }

        public void onHdmiPlugChanged(Intent intent) {
            if (intent.getIntExtra("hdmistatus", 0) == 1) {
                onHdmiPlugIn(intent);
            } else {
                onHdmiPlugOut(intent);
            }
        }

        public void onTvDacPlugChanged(Intent intent) {
            int tvdacplug = intent.getIntExtra("tvdacstatus", 0);
            if (tvdacplug == 1) {
                onTvDacYPbPrPlugIn(intent);
            } else if (tvdacplug == 2) {
                onTvDacCVBSPlugIn(intent);
            } else {
                onTvDacPlugOut(intent);
            }
        }
    }

    private class StatusBarTVDHotPlug implements DisplayHotPlugPolicy {
        StatusBarTVDHotPlug() {
        }

        public void onHdmiPlugChanged(Intent intent) {
        }

        public void onTvDacPlugChanged(Intent intent) {
        }
    }

    public DisplayController(Context context) {
        this.mDispHotPolicy = null;
        this.mContext = context;
        this.mDisplayManager = (DisplayManager) this.mContext.getSystemService("display");
        this.mDispHotPolicy = new StatusBarPadHotPlug();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.HDMISTATUS_CHANGED");
        filter.addAction("android.intent.action.TVDACSTATUS_CHANGED");
        context.registerReceiver(this, filter);
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("android.intent.action.HDMISTATUS_CHANGED")) {
            this.mDispHotPolicy.onHdmiPlugChanged(intent);
        } else if (action.equals("android.intent.action.TVDACSTATUS_CHANGED")) {
            this.mDispHotPolicy.onTvDacPlugChanged(intent);
        }
    }
}