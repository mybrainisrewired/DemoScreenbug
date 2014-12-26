package com.clouds.object;

public class DeleteAppInfo {
    public String appName;
    public String className;
    public String packageName;
    public String type;

    public DeleteAppInfo(String appName, String packageName, String className, String type) {
        this.appName = appName;
        this.packageName = packageName;
        this.type = type;
        this.className = className;
    }

    public String toString() {
        return new StringBuilder("DeleteAppInfo [appName=").append(this.appName).append(", packageName=").append(this.packageName).append(", type=").append(this.type).append(", className=").append(this.className).append("]").toString();
    }
}