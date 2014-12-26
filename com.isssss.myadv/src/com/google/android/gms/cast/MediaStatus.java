package com.google.android.gms.cast;

import com.google.android.gms.internal.eo;
import org.json.JSONException;
import org.json.JSONObject;

public final class MediaStatus {
    public static final long COMMAND_PAUSE = 1;
    public static final long COMMAND_SEEK = 2;
    public static final long COMMAND_SET_VOLUME = 4;
    public static final long COMMAND_SKIP_BACKWARD = 32;
    public static final long COMMAND_SKIP_FORWARD = 16;
    public static final long COMMAND_TOGGLE_MUTE = 8;
    public static final int IDLE_REASON_CANCELED = 2;
    public static final int IDLE_REASON_ERROR = 4;
    public static final int IDLE_REASON_FINISHED = 1;
    public static final int IDLE_REASON_INTERRUPTED = 3;
    public static final int IDLE_REASON_NONE = 0;
    public static final int PLAYER_STATE_BUFFERING = 4;
    public static final int PLAYER_STATE_IDLE = 1;
    public static final int PLAYER_STATE_PAUSED = 3;
    public static final int PLAYER_STATE_PLAYING = 2;
    public static final int PLAYER_STATE_UNKNOWN = 0;
    private long yA;
    private long yB;
    private double yC;
    private boolean yD;
    private JSONObject yn;
    private MediaInfo yo;
    private long yw;
    private double yx;
    private int yy;
    private int yz;

    public MediaStatus(JSONObject json) throws JSONException {
        a(json, IDLE_REASON_NONE);
    }

    public int a(JSONObject jSONObject, int i) throws JSONException {
        int i2;
        long b;
        int i3 = PLAYER_STATE_PLAYING;
        long j = jSONObject.getLong("mediaSessionId");
        if (j != this.yw) {
            this.yw = j;
            i2 = 1;
        } else {
            i2 = 0;
        }
        if (jSONObject.has("playerState")) {
            String string = jSONObject.getString("playerState");
            int i4 = string.equals("IDLE") ? 1 : string.equals("PLAYING") ? 2 : string.equals("PAUSED") ? 3 : string.equals("BUFFERING") ? 4 : 0;
            if (i4 != this.yy) {
                this.yy = i4;
                i2 |= 2;
            }
            if (i4 == 1 && jSONObject.has("idleReason")) {
                string = jSONObject.getString("idleReason");
                if (!string.equals("CANCELLED")) {
                    i3 = string.equals("INTERRUPTED") ? 3 : string.equals("FINISHED") ? 1 : string.equals("ERROR") ? 4 : 0;
                }
                if (i3 != this.yz) {
                    this.yz = i3;
                    i2 |= 2;
                }
            }
        }
        if (jSONObject.has("playbackRate")) {
            double d = jSONObject.getDouble("playbackRate");
            if (this.yx != d) {
                this.yx = d;
                i2 |= 2;
            }
        }
        if (jSONObject.has("currentTime") && (i & 2) == 0) {
            b = eo.b(jSONObject.getDouble("currentTime"));
            if (b != this.yA) {
                this.yA = b;
                i2 |= 2;
            }
        }
        if (jSONObject.has("supportedMediaCommands")) {
            b = jSONObject.getLong("supportedMediaCommands");
            if (b != this.yB) {
                this.yB = b;
                i2 |= 2;
            }
        }
        if (jSONObject.has("volume") && (i & 1) == 0) {
            JSONObject jSONObject2 = jSONObject.getJSONObject("volume");
            double d2 = jSONObject2.getDouble("level");
            if (d2 != this.yC) {
                this.yC = d2;
                i2 |= 2;
            }
            boolean z = jSONObject2.getBoolean("muted");
            if (z != this.yD) {
                this.yD = z;
                i2 |= 2;
            }
        }
        if (jSONObject.has("customData")) {
            this.yn = jSONObject.getJSONObject("customData");
            i2 |= 2;
        }
        if (!jSONObject.has("media")) {
            return i2;
        }
        jSONObject2 = jSONObject.getJSONObject("media");
        this.yo = new MediaInfo(jSONObject2);
        i2 |= 2;
        return jSONObject2.has("metadata") ? i2 | 4 : i2;
    }

    public long dC() {
        return this.yw;
    }

    public JSONObject getCustomData() {
        return this.yn;
    }

    public int getIdleReason() {
        return this.yz;
    }

    public MediaInfo getMediaInfo() {
        return this.yo;
    }

    public double getPlaybackRate() {
        return this.yx;
    }

    public int getPlayerState() {
        return this.yy;
    }

    public long getStreamPosition() {
        return this.yA;
    }

    public double getStreamVolume() {
        return this.yC;
    }

    public boolean isMediaCommandSupported(long mediaCommand) {
        return (this.yB & mediaCommand) != 0;
    }

    public boolean isMute() {
        return this.yD;
    }
}