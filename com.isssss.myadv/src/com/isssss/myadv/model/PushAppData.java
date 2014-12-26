package com.isssss.myadv.model;

import com.mopub.common.Preconditions;
import java.io.Serializable;

public class PushAppData implements Serializable {
    public static final int BANNER_CREATED = 3;
    public static final int BROWSER_DOWNLOAD = 1;
    public static final int DIRECT_DOWNLOAD = 0;
    public static final int SHORTCUT_CREATED = 2;
    private static final long serialVersionUID = 8420689230771569754L;
    protected String mApkURL;
    protected String mDescription;
    protected long mID;
    protected String mIconURL;
    protected int mOpenType;
    protected String mTitle;

    public PushAppData() {
        this.mID = 0;
        this.mDescription = Preconditions.EMPTY_ARGUMENTS;
        this.mApkURL = Preconditions.EMPTY_ARGUMENTS;
        this.mIconURL = Preconditions.EMPTY_ARGUMENTS;
        this.mTitle = Preconditions.EMPTY_ARGUMENTS;
        this.mOpenType = 0;
    }

    public String getmApkURL() {
        return this.mApkURL;
    }

    public String getmDescription() {
        return this.mDescription;
    }

    public long getmID() {
        return this.mID;
    }

    public String getmIconURL() {
        return this.mIconURL;
    }

    public int getmOpenType() {
        return this.mOpenType;
    }

    public String getmTitle() {
        return this.mTitle;
    }

    public void setmApkURL(String mApkURL) {
        this.mApkURL = mApkURL;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setmID(long mID) {
        this.mID = mID;
    }

    public void setmIconURL(String mIconURL) {
        this.mIconURL = mIconURL;
    }

    public void setmOpenType(int mOpenType) {
        this.mOpenType = mOpenType;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String toString() {
        return new StringBuilder("id = ").append(this.mID).append("\n").append("description = ").append(this.mDescription).append("\n").append("apkUrl = ").append(this.mApkURL).append("\n").append("iconUrl = ").append(this.mIconURL).append("\n").append("title = ").append(this.mTitle).append("\n").append("openType = ").append(this.mOpenType).append("\n").toString();
    }
}