package com.google.android.gms.common.images;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.fo;
import com.google.android.gms.plus.PlusShare;
import org.json.JSONException;
import org.json.JSONObject;

public final class WebImage implements SafeParcelable {
    public static final Creator<WebImage> CREATOR;
    private final Uri Cu;
    private final int kr;
    private final int ks;
    private final int xH;

    static {
        CREATOR = new b();
    }

    WebImage(int versionCode, Uri url, int width, int height) {
        this.xH = versionCode;
        this.Cu = url;
        this.kr = width;
        this.ks = height;
    }

    public WebImage(Uri url) throws IllegalArgumentException {
        this(url, 0, 0);
    }

    public WebImage(Uri url, int width, int height) throws IllegalArgumentException {
        this(1, url, width, height);
        if (url == null) {
            throw new IllegalArgumentException("url cannot be null");
        } else if (width < 0 || height < 0) {
            throw new IllegalArgumentException("width and height must not be negative");
        }
    }

    public WebImage(JSONObject json) throws IllegalArgumentException {
        this(d(json), json.optInt(MMLayout.KEY_WIDTH, 0), json.optInt(MMLayout.KEY_HEIGHT, 0));
    }

    private static Uri d(JSONObject jSONObject) {
        if (!jSONObject.has(PlusShare.KEY_CALL_TO_ACTION_URL)) {
            return null;
        }
        try {
            return Uri.parse(jSONObject.getString(PlusShare.KEY_CALL_TO_ACTION_URL));
        } catch (JSONException e) {
            return null;
        }
    }

    public JSONObject dB() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(PlusShare.KEY_CALL_TO_ACTION_URL, this.Cu.toString());
            jSONObject.put(MMLayout.KEY_WIDTH, this.kr);
            jSONObject.put(MMLayout.KEY_HEIGHT, this.ks);
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(WebImage other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof WebImage)) {
            return false;
        }
        other = other;
        return fo.equal(this.Cu, other.Cu) && this.kr == other.kr && this.ks == other.ks;
    }

    public int getHeight() {
        return this.ks;
    }

    public Uri getUrl() {
        return this.Cu;
    }

    int getVersionCode() {
        return this.xH;
    }

    public int getWidth() {
        return this.kr;
    }

    public int hashCode() {
        return fo.hashCode(new Object[]{this.Cu, Integer.valueOf(this.kr), Integer.valueOf(this.ks)});
    }

    public String toString() {
        return String.format("Image %dx%d %s", new Object[]{Integer.valueOf(this.kr), Integer.valueOf(this.ks), this.Cu.toString()});
    }

    public void writeToParcel(Parcel out, int flags) {
        b.a(this, out, flags);
    }
}