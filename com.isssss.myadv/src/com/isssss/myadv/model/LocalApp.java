package com.isssss.myadv.model;

public class LocalApp {
    private long firstShowTime;
    private int showCount;

    public LocalApp() {
        this.showCount = 0;
        this.firstShowTime = 0;
    }

    public long getFirstShowTime() {
        return this.firstShowTime;
    }

    public int getShowCount() {
        return this.showCount;
    }

    public void setFirstShowTime(long firstShowTime) {
        this.firstShowTime = firstShowTime;
    }

    public void setShowCount(int showCount) {
        this.showCount = showCount;
    }
}