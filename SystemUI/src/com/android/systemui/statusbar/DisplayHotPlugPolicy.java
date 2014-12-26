package com.android.systemui.statusbar;

import android.content.Intent;

public interface DisplayHotPlugPolicy {
    void onHdmiPlugChanged(Intent intent);

    void onTvDacPlugChanged(Intent intent);
}