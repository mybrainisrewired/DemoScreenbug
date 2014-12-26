package com.google.android.gms.wallet.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import com.google.android.gms.R;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.millennialmedia.android.MMAdView;

public final class WalletFragmentOptions implements SafeParcelable {
    public static final Creator<WalletFragmentOptions> CREATOR;
    private int Ev;
    private WalletFragmentStyle acR;
    private int acq;
    private int mTheme;
    final int xH;

    public final class Builder {
        private Builder() {
        }

        public WalletFragmentOptions build() {
            return WalletFragmentOptions.this;
        }

        public com.google.android.gms.wallet.fragment.WalletFragmentOptions.Builder setEnvironment(int environment) {
            WalletFragmentOptions.this.acq = environment;
            return this;
        }

        public com.google.android.gms.wallet.fragment.WalletFragmentOptions.Builder setFragmentStyle(int styleResourceId) {
            WalletFragmentOptions.this.acR = new WalletFragmentStyle().setStyleResourceId(styleResourceId);
            return this;
        }

        public com.google.android.gms.wallet.fragment.WalletFragmentOptions.Builder setFragmentStyle(WalletFragmentStyle fragmentStyle) {
            WalletFragmentOptions.this.acR = fragmentStyle;
            return this;
        }

        public com.google.android.gms.wallet.fragment.WalletFragmentOptions.Builder setMode(int mode) {
            WalletFragmentOptions.this.Ev = mode;
            return this;
        }

        public com.google.android.gms.wallet.fragment.WalletFragmentOptions.Builder setTheme(int theme) {
            WalletFragmentOptions.this.mTheme = theme;
            return this;
        }
    }

    static {
        CREATOR = new b();
    }

    private WalletFragmentOptions() {
        this.xH = 1;
    }

    WalletFragmentOptions(int versionCode, int environment, int theme, WalletFragmentStyle fragmentStyle, int mode) {
        this.xH = versionCode;
        this.acq = environment;
        this.mTheme = theme;
        this.acR = fragmentStyle;
        this.Ev = mode;
    }

    public static WalletFragmentOptions a(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.WalletFragmentOptions);
        int i = obtainStyledAttributes.getInt(0, 0);
        int i2 = obtainStyledAttributes.getInt(1, 1);
        int resourceId = obtainStyledAttributes.getResourceId(MMAdView.TRANSITION_UP, 0);
        int i3 = obtainStyledAttributes.getInt(MMAdView.TRANSITION_DOWN, 1);
        obtainStyledAttributes.recycle();
        WalletFragmentOptions walletFragmentOptions = new WalletFragmentOptions();
        walletFragmentOptions.mTheme = i;
        walletFragmentOptions.acq = i2;
        walletFragmentOptions.acR = new WalletFragmentStyle().setStyleResourceId(resourceId);
        walletFragmentOptions.acR.I(context);
        walletFragmentOptions.Ev = i3;
        return walletFragmentOptions;
    }

    public static Builder newBuilder() {
        WalletFragmentOptions walletFragmentOptions = new WalletFragmentOptions();
        walletFragmentOptions.getClass();
        return new Builder(null);
    }

    public void I(Context context) {
        if (this.acR != null) {
            this.acR.I(context);
        }
    }

    public int describeContents() {
        return 0;
    }

    public int getEnvironment() {
        return this.acq;
    }

    public WalletFragmentStyle getFragmentStyle() {
        return this.acR;
    }

    public int getMode() {
        return this.Ev;
    }

    public int getTheme() {
        return this.mTheme;
    }

    public void writeToParcel(Parcel dest, int flags) {
        b.a(this, dest, flags);
    }
}