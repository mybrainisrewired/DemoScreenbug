package com.google.android.gms.plus.internal;

import android.content.Context;
import com.google.android.gms.common.Scopes;
import java.util.ArrayList;
import java.util.Arrays;

public class i {
    private String[] Um;
    private String Un;
    private String Uo;
    private String Up;
    private PlusCommonExtras Ur;
    private final ArrayList<String> Us;
    private String[] Ut;
    private String wG;

    public i(Context context) {
        this.Us = new ArrayList();
        this.Uo = context.getPackageName();
        this.Un = context.getPackageName();
        this.Ur = new PlusCommonExtras();
        this.Us.add(Scopes.PLUS_LOGIN);
    }

    public i bh(String str) {
        this.wG = str;
        return this;
    }

    public i e(String... strArr) {
        this.Us.clear();
        this.Us.addAll(Arrays.asList(strArr));
        return this;
    }

    public i f(String... strArr) {
        this.Ut = strArr;
        return this;
    }

    public i iY() {
        this.Us.clear();
        return this;
    }

    public h iZ() {
        if (this.wG == null) {
            this.wG = "<<default account>>";
        }
        return new h(this.wG, (String[]) this.Us.toArray(new String[this.Us.size()]), this.Ut, this.Um, this.Un, this.Uo, this.Up, this.Ur);
    }
}