package com.google.android.gms.common.data;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.inmobi.androidsdk.IMBrowserActivity;

public class c<T extends SafeParcelable> extends DataBuffer<T> {
    private static final String[] BF;
    private final Creator<T> BG;

    static {
        BF = new String[]{IMBrowserActivity.EXPANDDATA};
    }

    public c(DataHolder dataHolder, Creator<T> creator) {
        super(dataHolder);
        this.BG = creator;
    }

    public T F(int i) {
        byte[] byteArray = this.BB.getByteArray(IMBrowserActivity.EXPANDDATA, i, 0);
        Parcel obtain = Parcel.obtain();
        obtain.unmarshall(byteArray, 0, byteArray.length);
        obtain.setDataPosition(0);
        SafeParcelable safeParcelable = (SafeParcelable) this.BG.createFromParcel(obtain);
        obtain.recycle();
        return safeParcelable;
    }

    public /* synthetic */ Object get(int x0) {
        return F(x0);
    }
}