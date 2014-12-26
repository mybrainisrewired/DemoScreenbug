package com.google.android.gms.cast;

import android.text.TextUtils;
import com.google.android.gms.internal.eo;
import com.google.android.gms.internal.fo;
import com.google.android.gms.internal.gp;
import org.json.JSONException;
import org.json.JSONObject;

public final class MediaInfo {
    public static final int STREAM_TYPE_BUFFERED = 1;
    public static final int STREAM_TYPE_INVALID = -1;
    public static final int STREAM_TYPE_LIVE = 2;
    public static final int STREAM_TYPE_NONE = 0;
    private final String yi;
    private int yj;
    private String yk;
    private MediaMetadata yl;
    private long ym;
    private JSONObject yn;

    public static class Builder {
        private final MediaInfo yo;

        public Builder(String contentId) throws IllegalArgumentException {
            if (TextUtils.isEmpty(contentId)) {
                throw new IllegalArgumentException("Content ID cannot be empty");
            }
            this.yo = new MediaInfo(contentId);
        }

        public MediaInfo build() throws IllegalArgumentException {
            this.yo.dA();
            return this.yo;
        }

        public com.google.android.gms.cast.MediaInfo.Builder setContentType(String contentType) throws IllegalArgumentException {
            this.yo.setContentType(contentType);
            return this;
        }

        public com.google.android.gms.cast.MediaInfo.Builder setCustomData(JSONObject customData) {
            this.yo.b(customData);
            return this;
        }

        public com.google.android.gms.cast.MediaInfo.Builder setMetadata(MediaMetadata metadata) {
            this.yo.a(metadata);
            return this;
        }

        public com.google.android.gms.cast.MediaInfo.Builder setStreamDuration(long duration) throws IllegalArgumentException {
            this.yo.k(duration);
            return this;
        }

        public com.google.android.gms.cast.MediaInfo.Builder setStreamType(int streamType) throws IllegalArgumentException {
            this.yo.setStreamType(streamType);
            return this;
        }
    }

    MediaInfo(String contentId) throws IllegalArgumentException {
        if (TextUtils.isEmpty(contentId)) {
            throw new IllegalArgumentException("content ID cannot be null or empty");
        }
        this.yi = contentId;
        this.yj = -1;
    }

    MediaInfo(JSONObject json) throws JSONException {
        this.yi = json.getString("contentId");
        String string = json.getString("streamType");
        if ("NONE".equals(string)) {
            this.yj = 0;
        } else if ("BUFFERED".equals(string)) {
            this.yj = 1;
        } else if ("LIVE".equals(string)) {
            this.yj = 2;
        } else {
            this.yj = -1;
        }
        this.yk = json.getString("contentType");
        if (json.has("metadata")) {
            JSONObject jSONObject = json.getJSONObject("metadata");
            this.yl = new MediaMetadata(jSONObject.getInt("metadataType"));
            this.yl.c(jSONObject);
        }
        this.ym = eo.b(json.optDouble("duration", 0.0d));
        this.yn = json.optJSONObject("customData");
    }

    void a(MediaMetadata mediaMetadata) {
        this.yl = mediaMetadata;
    }

    void b(JSONObject jSONObject) {
        this.yn = jSONObject;
    }

    void dA() throws IllegalArgumentException {
        if (TextUtils.isEmpty(this.yi)) {
            throw new IllegalArgumentException("content ID cannot be null or empty");
        } else if (TextUtils.isEmpty(this.yk)) {
            throw new IllegalArgumentException("content type cannot be null or empty");
        } else if (this.yj == -1) {
            throw new IllegalArgumentException("a valid stream type must be specified");
        }
    }

    public JSONObject dB() {
        JSONObject jSONObject = new JSONObject();
        try {
            Object obj;
            jSONObject.put("contentId", this.yi);
            switch (this.yj) {
                case STREAM_TYPE_BUFFERED:
                    obj = "BUFFERED";
                    break;
                case STREAM_TYPE_LIVE:
                    obj = "LIVE";
                    break;
                default:
                    obj = "NONE";
                    break;
            }
            jSONObject.put("streamType", obj);
            if (this.yk != null) {
                jSONObject.put("contentType", this.yk);
            }
            if (this.yl != null) {
                jSONObject.put("metadata", this.yl.dB());
            }
            jSONObject.put("duration", eo.m(this.ym));
            if (this.yn != null) {
                jSONObject.put("customData", this.yn);
            }
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public boolean equals(MediaInfo other) {
        boolean z = STREAM_TYPE_BUFFERED;
        if (this == other) {
            return true;
        }
        if (!(other instanceof MediaInfo)) {
            return false;
        }
        other = other;
        if ((this.yn == null) != (other.yn == null)) {
            return false;
        }
        if (this.yn != null && other.yn != null && !gp.d(this.yn, other.yn)) {
            return false;
        }
        if (!(eo.a(this.yi, other.yi) && this.yj == other.yj && eo.a(this.yk, other.yk) && eo.a(this.yl, other.yl) && this.ym == other.ym)) {
            z = false;
        }
        return z;
    }

    public String getContentId() {
        return this.yi;
    }

    public String getContentType() {
        return this.yk;
    }

    public JSONObject getCustomData() {
        return this.yn;
    }

    public MediaMetadata getMetadata() {
        return this.yl;
    }

    public long getStreamDuration() {
        return this.ym;
    }

    public int getStreamType() {
        return this.yj;
    }

    public int hashCode() {
        return fo.hashCode(new Object[]{this.yi, Integer.valueOf(this.yj), this.yk, this.yl, Long.valueOf(this.ym), String.valueOf(this.yn)});
    }

    void k(long j) throws IllegalArgumentException {
        if (j < 0) {
            throw new IllegalArgumentException("Stream duration cannot be negative");
        }
        this.ym = j;
    }

    void setContentType(String contentType) throws IllegalArgumentException {
        if (TextUtils.isEmpty(contentType)) {
            throw new IllegalArgumentException("content type cannot be null or empty");
        }
        this.yk = contentType;
    }

    void setStreamType(int streamType) throws IllegalArgumentException {
        if (streamType < -1 || streamType > 2) {
            throw new IllegalArgumentException("invalid stream type");
        }
        this.yj = streamType;
    }
}