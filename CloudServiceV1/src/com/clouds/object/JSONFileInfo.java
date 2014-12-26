package com.clouds.object;

public class JSONFileInfo {
    private String className;
    private String deviceType;
    private String pkgName;
    private String serverurl;
    private String sha256;
    private int size;
    private String type;
    private String url;

    public JSONFileInfo(String sha256, String url, int size) {
        this.sha256 = sha256;
        this.url = url;
        this.size = size;
    }

    public JSONFileInfo(String sha256, String url, String type, int size) {
        this.sha256 = sha256;
        this.url = url;
        this.type = type;
        this.size = size;
    }

    public JSONFileInfo(String sha256, String url, String type, String serverurl, String deviceType, int size) {
        this.sha256 = sha256;
        this.url = url;
        this.type = type;
        this.serverurl = serverurl;
        this.deviceType = deviceType;
        this.size = size;
    }

    public JSONFileInfo(String sha256, String url, String type, String serverurl, String deviceType, String pkgName, String className, int size) {
        this.sha256 = sha256;
        this.url = url;
        this.type = type;
        this.serverurl = serverurl;
        this.deviceType = deviceType;
        this.pkgName = pkgName;
        this.className = className;
        this.size = size;
    }

    public String getClassName() {
        return this.className;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public String getPkgName() {
        return this.pkgName;
    }

    public String getServerurl() {
        return this.serverurl;
    }

    public String getSha256() {
        return this.sha256;
    }

    public int getSize() {
        return this.size;
    }

    public String getType() {
        return this.type;
    }

    public String getUrl() {
        return this.url;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public void setServerurl(String serverurl) {
        this.serverurl = serverurl;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toString() {
        return new StringBuilder("JSONListInfo [sha256=").append(this.sha256).append(", url=").append(this.url).append(", type=").append(this.type).append(", serverurl=").append(this.serverurl).append(", size=").append(this.size).append("]").toString();
    }
}