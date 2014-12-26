package com.millennialmedia.android;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.json.JSONArray;
import org.json.JSONObject;

class VideoLogEvent implements Parcelable, Externalizable {
    public static final Creator<VideoLogEvent> CREATOR;
    private static final String TAG;
    static final long serialVersionUID = 795553873017368584L;
    String[] activities;
    long position;

    static {
        TAG = VideoLogEvent.class.getName();
        CREATOR = new Creator<VideoLogEvent>() {
            public VideoLogEvent createFromParcel(Parcel in) {
                return new VideoLogEvent(in);
            }

            public VideoLogEvent[] newArray(int size) {
                return new VideoLogEvent[size];
            }
        };
    }

    VideoLogEvent(Parcel in) {
        try {
            this.position = in.readLong();
            this.activities = new String[in.readInt()];
            in.readStringArray(this.activities);
        } catch (Exception e) {
            MMLog.e(TAG, "VideoLogEvent parcel creation exception: ", e);
        }
    }

    VideoLogEvent(JSONObject logObject) {
        deserializeFromObj(logObject);
    }

    private void deserializeFromObj(JSONObject logObject) {
        if (logObject != null) {
            this.position = (long) (logObject.optInt("time") * 1000);
            JSONArray jsonArray = logObject.optJSONArray("urls");
            if (jsonArray != null) {
                this.activities = new String[jsonArray.length()];
                int i = 0;
                while (i < jsonArray.length()) {
                    this.activities[i] = jsonArray.optString(i);
                    i++;
                }
            } else {
                this.activities = new String[0];
            }
        }
    }

    public int describeContents() {
        return 0;
    }

    public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
        this.position = input.readLong();
        int count = input.readInt();
        this.activities = new String[count];
        int i = 0;
        while (i < count) {
            this.activities[i] = (String) input.readObject();
            i++;
        }
    }

    public void writeExternal(ObjectOutput output) throws IOException {
        output.writeLong(this.position);
        output.writeInt(this.activities.length);
        String[] arr$ = this.activities;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            output.writeObject(arr$[i$]);
            i$++;
        }
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.position);
        dest.writeInt(this.activities.length);
        dest.writeStringArray(this.activities);
    }
}