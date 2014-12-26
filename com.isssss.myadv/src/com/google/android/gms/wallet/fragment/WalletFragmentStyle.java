package com.google.android.gms.wallet.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import com.google.android.gms.R;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public final class WalletFragmentStyle implements SafeParcelable {
    public static final Creator<WalletFragmentStyle> CREATOR;
    Bundle acT;
    int acU;
    final int xH;

    static {
        CREATOR = new c();
    }

    public WalletFragmentStyle() {
        this.xH = 1;
        this.acT = new Bundle();
    }

    WalletFragmentStyle(int versionCode, Bundle attributes, int styleResourceId) {
        this.xH = versionCode;
        this.acT = attributes;
        this.acU = styleResourceId;
    }

    private void a(TypedArray typedArray, int i, String str) {
        if (!this.acT.containsKey(str)) {
            TypedValue peekValue = typedArray.peekValue(i);
            if (peekValue != null) {
                this.acT.putLong(str, Dimension.a(peekValue));
            }
        }
    }

    private void a(TypedArray typedArray, int i, String str, String str2) {
        if (!this.acT.containsKey(str) && !this.acT.containsKey(str2)) {
            TypedValue peekValue = typedArray.peekValue(i);
            if (peekValue == null) {
                return;
            }
            if (peekValue.type < 28 || peekValue.type > 31) {
                this.acT.putInt(str2, peekValue.resourceId);
            } else {
                this.acT.putInt(str, peekValue.data);
            }
        }
    }

    private void b(TypedArray typedArray, int i, String str) {
        if (!this.acT.containsKey(str)) {
            TypedValue peekValue = typedArray.peekValue(i);
            if (peekValue != null) {
                this.acT.putInt(str, peekValue.data);
            }
        }
    }

    public void I(Context context) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(this.acU <= 0 ? R.style.WalletFragmentDefaultStyle : this.acU, R.styleable.WalletFragmentStyle);
        a(obtainStyledAttributes, 1, "buyButtonWidth");
        a(obtainStyledAttributes, 0, "buyButtonHeight");
        b(obtainStyledAttributes, MMAdView.TRANSITION_UP, "buyButtonText");
        b(obtainStyledAttributes, MMAdView.TRANSITION_DOWN, "buyButtonAppearance");
        b(obtainStyledAttributes, MMAdView.TRANSITION_RANDOM, "maskedWalletDetailsTextAppearance");
        b(obtainStyledAttributes, ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, "maskedWalletDetailsHeaderTextAppearance");
        a(obtainStyledAttributes, ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, "maskedWalletDetailsBackgroundColor", "maskedWalletDetailsBackgroundResource");
        b(obtainStyledAttributes, ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, "maskedWalletDetailsButtonTextAppearance");
        a(obtainStyledAttributes, ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, "maskedWalletDetailsButtonBackgroundColor", "maskedWalletDetailsButtonBackgroundResource");
        b(obtainStyledAttributes, ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, "maskedWalletDetailsLogoTextColor");
        b(obtainStyledAttributes, ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, "maskedWalletDetailsLogoImageType");
        obtainStyledAttributes.recycle();
    }

    public int a(String str, DisplayMetrics displayMetrics, int i) {
        return this.acT.containsKey(str) ? Dimension.a(this.acT.getLong(str), displayMetrics) : i;
    }

    public int describeContents() {
        return 0;
    }

    public WalletFragmentStyle setBuyButtonAppearance(int buyButtonAppearance) {
        this.acT.putInt("buyButtonAppearance", buyButtonAppearance);
        return this;
    }

    public WalletFragmentStyle setBuyButtonHeight(int height) {
        this.acT.putLong("buyButtonHeight", Dimension.cz(height));
        return this;
    }

    public WalletFragmentStyle setBuyButtonHeight(int unit, float height) {
        this.acT.putLong("buyButtonHeight", Dimension.a(unit, height));
        return this;
    }

    public WalletFragmentStyle setBuyButtonText(int buyButtonText) {
        this.acT.putInt("buyButtonText", buyButtonText);
        return this;
    }

    public WalletFragmentStyle setBuyButtonWidth(int width) {
        this.acT.putLong("buyButtonWidth", Dimension.cz(width));
        return this;
    }

    public WalletFragmentStyle setBuyButtonWidth(int unit, float width) {
        this.acT.putLong("buyButtonWidth", Dimension.a(unit, width));
        return this;
    }

    public WalletFragmentStyle setMaskedWalletDetailsBackgroundColor(int color) {
        this.acT.remove("maskedWalletDetailsBackgroundResource");
        this.acT.putInt("maskedWalletDetailsBackgroundColor", color);
        return this;
    }

    public WalletFragmentStyle setMaskedWalletDetailsBackgroundResource(int resourceId) {
        this.acT.remove("maskedWalletDetailsBackgroundColor");
        this.acT.putInt("maskedWalletDetailsBackgroundResource", resourceId);
        return this;
    }

    public WalletFragmentStyle setMaskedWalletDetailsButtonBackgroundColor(int color) {
        this.acT.remove("maskedWalletDetailsButtonBackgroundResource");
        this.acT.putInt("maskedWalletDetailsButtonBackgroundColor", color);
        return this;
    }

    public WalletFragmentStyle setMaskedWalletDetailsButtonBackgroundResource(int resourceId) {
        this.acT.remove("maskedWalletDetailsButtonBackgroundColor");
        this.acT.putInt("maskedWalletDetailsButtonBackgroundResource", resourceId);
        return this;
    }

    public WalletFragmentStyle setMaskedWalletDetailsButtonTextAppearance(int resourceId) {
        this.acT.putInt("maskedWalletDetailsButtonTextAppearance", resourceId);
        return this;
    }

    public WalletFragmentStyle setMaskedWalletDetailsHeaderTextAppearance(int resourceId) {
        this.acT.putInt("maskedWalletDetailsHeaderTextAppearance", resourceId);
        return this;
    }

    public WalletFragmentStyle setMaskedWalletDetailsLogoImageType(int imageType) {
        this.acT.putInt("maskedWalletDetailsLogoImageType", imageType);
        return this;
    }

    public WalletFragmentStyle setMaskedWalletDetailsLogoTextColor(int color) {
        this.acT.putInt("maskedWalletDetailsLogoTextColor", color);
        return this;
    }

    public WalletFragmentStyle setMaskedWalletDetailsTextAppearance(int resourceId) {
        this.acT.putInt("maskedWalletDetailsTextAppearance", resourceId);
        return this;
    }

    public WalletFragmentStyle setStyleResourceId(int id) {
        this.acU = id;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        c.a(this, dest, flags);
    }
}