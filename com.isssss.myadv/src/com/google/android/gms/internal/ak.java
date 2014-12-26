package com.google.android.gms.internal;

import android.content.Context;
import android.os.Parcel;
import android.util.DisplayMetrics;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.a;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;

public final class ak implements SafeParcelable {
    public static final al CREATOR;
    public final int height;
    public final int heightPixels;
    public final String lS;
    public final boolean lT;
    public final ak[] lU;
    public final int versionCode;
    public final int width;
    public final int widthPixels;

    static {
        CREATOR = new al();
    }

    public ak() {
        this(2, "interstitial_mb", 0, 0, true, 0, 0, null);
    }

    ak(int i, String str, int i2, int i3, boolean z, int i4, int i5, ak[] akVarArr) {
        this.versionCode = i;
        this.lS = str;
        this.height = i2;
        this.heightPixels = i3;
        this.lT = z;
        this.width = i4;
        this.widthPixels = i5;
        this.lU = akVarArr;
    }

    public ak(Context context, AdSize adSize) {
        this(context, new AdSize[]{adSize});
    }

    public ak(Context context, AdSize[] adSizeArr) {
        int i;
        int i2;
        int i3 = 0;
        AdSize adSize = adSizeArr[0];
        this.versionCode = 2;
        this.lT = false;
        this.width = adSize.getWidth();
        this.height = adSize.getHeight();
        int i4 = this.width == -1;
        int i5 = this.height == -2;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        if (i4 != 0) {
            this.widthPixels = a(displayMetrics);
            i = (int) (((float) this.widthPixels) / displayMetrics.density);
        } else {
            i2 = this.width;
            this.widthPixels = dv.a(displayMetrics, this.width);
            i = i2;
        }
        i2 = i5 != 0 ? c(displayMetrics) : this.height;
        this.heightPixels = dv.a(displayMetrics, i2);
        if (i4 == 0 && i5 == 0) {
            this.lS = adSize.toString();
        } else {
            this.lS = i + "x" + i2 + "_as";
        }
        if (adSizeArr.length > 1) {
            this.lU = new ak[adSizeArr.length];
            while (i3 < adSizeArr.length) {
                this.lU[i3] = new ak(context, adSizeArr[i3]);
                i3++;
            }
        } else {
            this.lU = null;
        }
    }

    public ak(ak akVar, ak[] akVarArr) {
        this(2, akVar.lS, akVar.height, akVar.heightPixels, akVar.lT, akVar.width, akVar.widthPixels, akVarArr);
    }

    public static int a(DisplayMetrics displayMetrics) {
        return displayMetrics.widthPixels;
    }

    public static int b(DisplayMetrics displayMetrics) {
        return (int) (((float) c(displayMetrics)) * displayMetrics.density);
    }

    private static int c(DisplayMetrics displayMetrics) {
        int i = (int) (((float) displayMetrics.heightPixels) / displayMetrics.density);
        return i <= 400 ? ApiEventType.API_MRAID_PLAY_AUDIO : i <= 720 ? ApiEventType.API_MRAID_CLOSE_VIDEO : com.google.ads.AdSize.LARGE_AD_HEIGHT;
    }

    public AdSize aA() {
        return a.a(this.width, this.height, this.lS);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        al.a(this, out, flags);
    }
}