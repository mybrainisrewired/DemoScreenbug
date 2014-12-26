package com.millennialmedia.android;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

class InterstitialAd extends CachedAd implements Parcelable, Externalizable {
    public static final Creator<InterstitialAd> CREATOR;
    static final String EXTRA_AD_URL = "EXTRA_AD_URL";
    static final String EXTRA_CONTENT = "EXTRA_CONTENT";
    private static final String TAG;
    static final long serialVersionUID = 5158660334173309853L;
    String adUrl;
    String content;
    HttpMMHeaders mmHeaders;

    static {
        TAG = InterstitialAd.class.getName();
        CREATOR = new Creator<InterstitialAd>() {
            public InterstitialAd createFromParcel(Parcel in) {
                return new InterstitialAd(in);
            }

            public InterstitialAd[] newArray(int size) {
                return new InterstitialAd[size];
            }
        };
    }

    InterstitialAd(Parcel in) {
        super(in);
        try {
            this.content = in.readString();
            this.adUrl = in.readString();
            this.mmHeaders = (HttpMMHeaders) in.readParcelable(HttpMMHeaders.class.getClassLoader());
        } catch (Exception e) {
            MMLog.e(TAG, "Interstitial problem reading parcel: ", e);
        }
    }

    private Intent getExpandExtrasIntent(Context context, long adImplInternalId) {
        Intent intent = new Intent();
        OverlaySettings settings = new OverlaySettings();
        settings.creatorAdImplId = adImplInternalId;
        settings.content = this.content;
        settings.adUrl = this.adUrl;
        settings.setWebMMHeaders(this.mmHeaders);
        settings.isFromInterstitial = true;
        intent.putExtra("settings", settings);
        intent.putExtra("internalId", adImplInternalId);
        return intent;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    boolean canShow(android.content.Context r7_context, com.millennialmedia.android.MMAdImpl r8_adImpl, boolean r9_checkDeferredViewStart) {
        throw new UnsupportedOperationException("Method not decompiled: com.millennialmedia.android.InterstitialAd.canShow(android.content.Context, com.millennialmedia.android.MMAdImpl, boolean):boolean");
        /*
        r6 = this;
        r0 = 1;
        r1 = 0;
        if (r9 == 0) goto L_0x002d;
    L_0x0004:
        r2 = r6.content;
        if (r2 == 0) goto L_0x002b;
    L_0x0008:
        r2 = r6.content;
        r2 = r2.length();
        if (r2 <= 0) goto L_0x002b;
    L_0x0010:
        r2 = r6.adUrl;
        if (r2 == 0) goto L_0x002b;
    L_0x0014:
        r2 = r6.adUrl;
        r2 = r2.length();
        if (r2 <= 0) goto L_0x002b;
    L_0x001c:
        r2 = com.millennialmedia.android.HandShake.sharedHandShake(r7);
        r3 = r8.adType;
        r4 = r6.deferredViewStart;
        r2 = r2.canDisplayCachedAd(r3, r4);
        if (r2 == 0) goto L_0x002b;
    L_0x002a:
        return r0;
    L_0x002b:
        r0 = r1;
        goto L_0x002a;
    L_0x002d:
        r2 = r6.content;
        if (r2 == 0) goto L_0x0045;
    L_0x0031:
        r2 = r6.content;
        r2 = r2.length();
        if (r2 <= 0) goto L_0x0045;
    L_0x0039:
        r2 = r6.adUrl;
        if (r2 == 0) goto L_0x0045;
    L_0x003d:
        r2 = r6.adUrl;
        r2 = r2.length();
        if (r2 > 0) goto L_0x002a;
    L_0x0045:
        r0 = r1;
        goto L_0x002a;
        */
    }

    public int describeContents() {
        return 0;
    }

    boolean download(Context context) {
        return true;
    }

    int getType() {
        return MMAdView.TRANSITION_UP;
    }

    String getTypeString() {
        return "Interstitial";
    }

    boolean isOnDisk(Context context) {
        return true;
    }

    public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
        super.readExternal(input);
        this.content = (String) input.readObject();
        this.adUrl = (String) input.readObject();
        this.mmHeaders = (HttpMMHeaders) input.readObject();
    }

    boolean saveAssets(Context context) {
        return true;
    }

    void show(Context context, long adImplInternalId) {
        IntentUtils.startAdViewOverlayActivity(context, getExpandExtrasIntent(context, adImplInternalId));
    }

    public void writeExternal(ObjectOutput output) throws IOException {
        super.writeExternal(output);
        output.writeObject(this.content);
        output.writeObject(this.adUrl);
        output.writeObject(this.mmHeaders);
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.content);
        dest.writeString(this.adUrl);
        dest.writeParcelable(this.mmHeaders, flags);
    }
}