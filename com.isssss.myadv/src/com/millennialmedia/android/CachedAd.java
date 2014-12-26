package com.millennialmedia.android;

import android.content.Context;
import android.os.Parcel;
import com.inmobi.commons.analytics.db.AnalyticsEvent;
import java.io.Externalizable;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONObject;

abstract class CachedAd implements Externalizable {
    static final int INTERSTITIAL = 2;
    static final int NATIVE = 3;
    private static final String TAG = "CachedAd";
    static final int VIDEO = 1;
    static final long serialVersionUID = 316862728709355974L;
    String acid;
    String contentUrl;
    long deferredViewStart;
    boolean downloadAllOrNothing;
    int downloadPriority;
    Date expiration;
    private String id;

    CachedAd() {
        this.downloadAllOrNothing = false;
        this.deferredViewStart = System.currentTimeMillis();
    }

    protected CachedAd(Parcel in) {
        this.downloadAllOrNothing = false;
        try {
            this.id = in.readString();
            this.acid = in.readString();
            this.expiration = (Date) in.readSerializable();
            this.deferredViewStart = in.readLong();
            boolean[] yo = new boolean[1];
            in.readBooleanArray(yo);
            this.downloadAllOrNothing = yo[0];
            this.contentUrl = in.readString();
            this.downloadPriority = in.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static CachedAd parseJSON(String json) {
        if (MMSDK.logLevel >= 5) {
            MMLog.v(TAG, "Received cached ad.");
            int length = json.length();
            if (length > 1000) {
                int e = 999;
                int s = 0;
                while (e < length) {
                    MMLog.v(TAG, json.substring(s, e));
                    s = e;
                    e += 1000;
                    if (e > length) {
                        e = length - 1;
                        break;
                    }
                }
                MMLog.v(TAG, json.substring(s, e));
            } else {
                MMLog.v(TAG, json);
            }
        }
        return json.length() > 0 ? new VideoAd(json) : null;
    }

    abstract boolean canShow(Context context, MMAdImpl mMAdImpl, boolean z);

    void delete(Context context) {
        File dir = AdCache.getInternalCacheDirectory(context);
        if (dir != null && dir.isDirectory()) {
            try {
                File[] files = dir.listFiles(new FileFilter() {
                    public boolean accept(File file) {
                        return file.isFile() && file.getName().startsWith(CachedAd.this.id);
                    }
                });
                MMLog.v(TAG, String.format("Deleting %d files for %s.", new Object[]{Integer.valueOf(files.length), this.id}));
                int i = 0;
                while (i < files.length) {
                    files[i].delete();
                    i++;
                }
            } catch (Exception e) {
                MMLog.e(TAG, "Exception deleting cached ad: ", e);
            }
        }
    }

    protected void deserializeFromObj(JSONObject videoObject) {
        this.id = videoObject.optString(AnalyticsEvent.EVENT_ID, null);
        this.acid = videoObject.optString("vid", null);
        this.contentUrl = videoObject.optString("content-url", null);
        String exp = videoObject.optString("expiration", null);
        if (exp != null) {
            try {
                this.expiration = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZZZ").parse(exp);
            } catch (ParseException e) {
                MMLog.e(TAG, "Exception deserializing cached ad: ", e);
            }
        }
    }

    abstract boolean download(Context context);

    public boolean equals(CachedAd other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CachedAd)) {
            return false;
        }
        return this.id.equals(other.id);
    }

    String getId() {
        return this.id;
    }

    abstract int getType();

    abstract String getTypeString();

    boolean isExpired() {
        return this.expiration != null && this.expiration.getTime() <= System.currentTimeMillis();
    }

    abstract boolean isOnDisk(Context context);

    boolean isValid() {
        return this.id != null && this.id.length() > 0 && this.contentUrl != null && this.contentUrl.length() > 0;
    }

    public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
        this.id = (String) input.readObject();
        this.acid = (String) input.readObject();
        this.expiration = (Date) input.readObject();
        this.deferredViewStart = input.readLong();
        this.contentUrl = (String) input.readObject();
    }

    abstract boolean saveAssets(Context context);

    void setId(String idIn) {
        this.id = idIn;
    }

    abstract void show(Context context, long j);

    public void writeExternal(ObjectOutput output) throws IOException {
        output.writeObject(this.id);
        output.writeObject(this.acid);
        output.writeObject(this.expiration);
        output.writeLong(this.deferredViewStart);
        output.writeObject(this.contentUrl);
    }

    protected void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.acid);
        dest.writeSerializable(this.expiration);
        dest.writeLong(this.deferredViewStart);
        dest.writeBooleanArray(new boolean[]{this.downloadAllOrNothing});
        dest.writeString(this.contentUrl);
        dest.writeInt(this.downloadPriority);
    }
}