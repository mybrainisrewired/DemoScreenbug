package com.google.android.gms.internal;

import android.util.Base64;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

class e implements n {
    e() {
    }

    public String a(byte[] bArr, boolean z) {
        return Base64.encodeToString(bArr, z ? ApiEventType.API_MRAID_EXPAND : MMAdView.TRANSITION_UP);
    }

    public byte[] a(String str, boolean z) throws IllegalArgumentException {
        return Base64.decode(str, z ? ApiEventType.API_MRAID_EXPAND : MMAdView.TRANSITION_UP);
    }
}