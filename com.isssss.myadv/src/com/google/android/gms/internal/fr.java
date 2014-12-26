package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.view.View;
import com.google.android.gms.dynamic.e;
import com.google.android.gms.dynamic.g;
import com.google.android.gms.dynamic.g.a;

public final class fr extends g<fn> {
    private static final fr DK;

    static {
        DK = new fr();
    }

    private fr() {
        super("com.google.android.gms.common.ui.SignInButtonCreatorImpl");
    }

    public static View b(Context context, int i, int i2) throws a {
        return DK.c(context, i, i2);
    }

    private View c(Context context, int i, int i2) throws a {
        try {
            return (View) e.d(((fn) z(context)).a(e.h(context), i, i2));
        } catch (Exception e) {
            throw new a("Could not get button with size " + i + " and color " + i2, e);
        }
    }

    public fn E(IBinder iBinder) {
        return fn.a.D(iBinder);
    }

    public /* synthetic */ Object d(IBinder iBinder) {
        return E(iBinder);
    }
}