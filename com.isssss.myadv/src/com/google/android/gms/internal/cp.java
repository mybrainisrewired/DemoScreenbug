package com.google.android.gms.internal;

import com.google.android.gms.ads.purchase.InAppPurchaseListener;
import com.google.android.gms.internal.co.a;

public final class cp extends a {
    private final InAppPurchaseListener mp;

    public cp(InAppPurchaseListener inAppPurchaseListener) {
        this.mp = inAppPurchaseListener;
    }

    public void a(cn cnVar) {
        this.mp.onInAppPurchaseRequested(new cq(cnVar));
    }
}