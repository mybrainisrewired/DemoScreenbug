package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.Locale;

public class hs implements SafeParcelable {
    public static final ht CREATOR;
    public final String Rl;
    public final String Rm;
    public final int versionCode;

    static {
        CREATOR = new ht();
    }

    public hs(int i, String str, String str2) {
        this.versionCode = i;
        this.Rl = str;
        this.Rm = str2;
    }

    public hs(String str, Locale locale) {
        this.versionCode = 0;
        this.Rl = str;
        this.Rm = locale.toString();
    }

    public int describeContents() {
        ht htVar = CREATOR;
        return 0;
    }

    public boolean equals(hs object) {
        if (this == object) {
            return true;
        }
        if (object == null || !(object instanceof hs)) {
            return false;
        }
        object = object;
        return this.Rm.equals(object.Rm) && this.Rl.equals(object.Rl);
    }

    public int hashCode() {
        return fo.hashCode(new Object[]{this.Rl, this.Rm});
    }

    public String toString() {
        return fo.e(this).a("clientPackageName", this.Rl).a("locale", this.Rm).toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        ht htVar = CREATOR;
        ht.a(this, out, flags);
    }
}