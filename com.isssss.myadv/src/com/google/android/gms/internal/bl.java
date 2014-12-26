package com.google.android.gms.internal;

import com.google.android.gms.internal.bs.a;
import com.millennialmedia.android.MMAdView;

public final class bl extends a {
    private final Object li;
    private bn.a nl;
    private bk nm;

    public bl() {
        this.li = new Object();
    }

    public void P() {
        synchronized (this.li) {
            if (this.nm != null) {
                this.nm.X();
            }
        }
    }

    public void a(bk bkVar) {
        synchronized (this.li) {
            this.nm = bkVar;
        }
    }

    public void a(bn.a aVar) {
        synchronized (this.li) {
            this.nl = aVar;
        }
    }

    public void onAdClosed() {
        synchronized (this.li) {
            if (this.nm != null) {
                this.nm.Y();
            }
        }
    }

    public void onAdFailedToLoad(int error) {
        synchronized (this.li) {
            if (this.nl != null) {
                this.nl.f(error == 3 ? 1 : MMAdView.TRANSITION_UP);
                this.nl = null;
            }
        }
    }

    public void onAdLeftApplication() {
        synchronized (this.li) {
            if (this.nm != null) {
                this.nm.Z();
            }
        }
    }

    public void onAdLoaded() {
        synchronized (this.li) {
            if (this.nl != null) {
                this.nl.f(0);
                this.nl = null;
            } else {
                if (this.nm != null) {
                    this.nm.ab();
                }
            }
        }
    }

    public void onAdOpened() {
        synchronized (this.li) {
            if (this.nm != null) {
                this.nm.aa();
            }
        }
    }
}