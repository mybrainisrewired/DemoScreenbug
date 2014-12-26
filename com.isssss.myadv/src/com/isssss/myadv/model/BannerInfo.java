package com.isssss.myadv.model;

public class BannerInfo {
    private String apkPath;
    private String apkUrl;
    private int appID;
    private String backgroundPath;
    private String backgroundUrl;
    private String description;
    private int downType;
    private String iconPath;
    private String iconUrl;
    private int isDownloaded;
    private String title;

    public BannerInfo() {
        this.appID = 0;
        this.title = new String();
        this.description = new String();
        this.iconUrl = new String();
        this.iconPath = new String();
        this.backgroundUrl = new String();
        this.backgroundPath = new String();
        this.apkPath = new String();
        this.apkUrl = new String();
        this.downType = 0;
        this.isDownloaded = 0;
    }

    public String getApkPath() {
        return this.apkPath;
    }

    public String getApkUrl() {
        return this.apkUrl;
    }

    public int getAppID() {
        return this.appID;
    }

    public String getBackgroundPath() {
        return this.backgroundPath;
    }

    public String getBackgroundUrl() {
        return this.backgroundUrl;
    }

    public String getDescription() {
        return this.description;
    }

    public int getDownType() {
        return this.downType;
    }

    public int getDownloaded() {
        return this.isDownloaded;
    }

    public String getIconPath() {
        return this.iconPath;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public String getTitle() {
        return this.title;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public void setAppID(int appID) {
        this.appID = appID;
    }

    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDownType(int downType) {
        this.downType = downType;
    }

    public void setDownloaded(int isDownloaded) {
        this.isDownloaded = isDownloaded;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}