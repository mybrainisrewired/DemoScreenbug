package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ef implements Parcelable {
    @Deprecated
    public static final Creator<ef> CREATOR;
    private String mValue;
    private String wp;
    private String wq;

    static {
        CREATOR = new Creator<ef>() {
            public /* synthetic */ Object createFromParcel(Parcel x0) {
                return i(x0);
            }

            @Deprecated
            public ef i(Parcel parcel) {
                return new ef(parcel);
            }

            public /* synthetic */ Object[] newArray(int x0) {
                return u(x0);
            }

            @Deprecated
            public ef[] u(int i) {
                return new ef[i];
            }
        };
    }

    @Deprecated
    ef(Parcel parcel) {
        readFromParcel(parcel);
    }

    public ef(String str, String str2, String str3) {
        this.wp = str;
        this.wq = str2;
        this.mValue = str3;
    }

    @Deprecated
    private void readFromParcel(Parcel in) {
        this.wp = in.readString();
        this.wq = in.readString();
        this.mValue = in.readString();
    }

    @Deprecated
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return this.wp;
    }

    public String getValue() {
        return this.mValue;
    }

    @Deprecated
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.wp);
        out.writeString(this.wq);
        out.writeString(this.mValue);
    }
}