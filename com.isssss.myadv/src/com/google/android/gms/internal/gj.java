package com.google.android.gms.internal;

import android.util.Base64;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;

public final class gj {
    public static String d(byte[] bArr) {
        return bArr == null ? null : Base64.encodeToString(bArr, 0);
    }

    public static String e(byte[] bArr) {
        return bArr == null ? null : Base64.encodeToString(bArr, ApiEventType.API_MRAID_USE_CUSTOM_CLOSE);
    }
}