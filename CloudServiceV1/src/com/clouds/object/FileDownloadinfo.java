package com.clouds.object;

public class FileDownloadinfo {
    private String appType;
    private int fileSize;
    private String localfile;
    private String url;

    public FileDownloadinfo(String url, String localfile) {
        this.url = url;
        this.localfile = localfile;
    }

    public FileDownloadinfo(String url, String localfile, String appType) {
        this.url = url;
        this.localfile = localfile;
        this.appType = appType;
    }

    public FileDownloadinfo(String url, String localfile, String appType, int fileSize) {
        this.url = url;
        this.localfile = localfile;
        this.appType = appType;
        this.fileSize = fileSize;
    }

    public String getAppType() {
        return this.appType;
    }

    public int getFileSize() {
        return this.fileSize;
    }

    public String getLocalfile() {
        return this.localfile;
    }

    public String getUrl() {
        return this.url;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public void setLocalfile(String localfile) {
        this.localfile = localfile;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toString() {
        return new StringBuilder("DownLoadAPPinfo [url=").append(this.url).append(", localfile=").append(this.localfile).append(", appType=").append(this.appType).append(", fileSize=").append(this.fileSize).append("]").toString();
    }
}