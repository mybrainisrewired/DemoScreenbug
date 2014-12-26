package com.clouds.db;

public class DownloadInfo {
    private int compeleteSize;
    private int endPos;
    private int startPos;
    private int threadId;
    private String url;

    public DownloadInfo(int threadId, int startPos, int endPos, int compeleteSize, String url) {
        this.threadId = threadId;
        this.startPos = startPos;
        this.endPos = endPos;
        this.compeleteSize = compeleteSize;
        this.url = url;
    }

    public int getCompeleteSize() {
        return this.compeleteSize;
    }

    public int getEndPos() {
        return this.endPos;
    }

    public int getStartPos() {
        return this.startPos;
    }

    public int getThreadId() {
        return this.threadId;
    }

    public String getUrl() {
        return this.url;
    }

    public void setCompeleteSize(int compeleteSize) {
        this.compeleteSize = compeleteSize;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toString() {
        return new StringBuilder("DownloadInfo [threadId=").append(this.threadId).append(", startPos=").append(this.startPos).append(", endPos=").append(this.endPos).append(", compeleteSize=").append(this.compeleteSize).append("]").toString();
    }
}