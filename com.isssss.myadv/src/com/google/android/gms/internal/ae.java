package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.internal.ea.a;
import org.json.JSONObject;

public class ae implements ad {
    private final dz lC;

    class AnonymousClass_1 implements a {
        final /* synthetic */ ad.a lD;

        AnonymousClass_1(ad.a aVar) {
            this.lD = aVar;
        }

        public void a(dz dzVar) {
            this.lD.ay();
        }
    }

    public ae(Context context, dx dxVar) {
        this.lC = dz.a(context, new ak(), false, false, null, dxVar);
    }

    public void a(ad.a aVar) {
        this.lC.bI().a(new AnonymousClass_1(aVar));
    }

    public void a(String str, bb bbVar) {
        this.lC.bI().a(str, bbVar);
    }

    public void a(String str, JSONObject jSONObject) {
        this.lC.a(str, jSONObject);
    }

    public void d(String str) {
        this.lC.loadUrl(str);
    }

    public void e(String str) {
        this.lC.bI().a(str, null);
    }
}