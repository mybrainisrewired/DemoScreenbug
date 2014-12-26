package com.android.systemui.statusbar.policy;

import android.content.Intent;

public interface DisplayHotPlugPolicy {
    void onHdmiPlugChanged(Intent intent);

    void onTvDacPlugChanged(Intent intent);
}