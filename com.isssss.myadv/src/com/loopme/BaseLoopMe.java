package com.loopme;

import android.app.Activity;

abstract class BaseLoopMe {
    private static String LOG_TAG;
    protected final int TIMEOUT;
    protected Activity mActivity;
    protected AdParams mAdParams;
    protected String mAppKey;
    protected volatile boolean mInit;
    protected volatile boolean mIsReady;
    protected boolean mTestMode;

    static {
        LOG_TAG = BaseLoopMe.class.getSimpleName();
    }

    BaseLoopMe() {
        this.TIMEOUT = 10000;
    }

    private String getRequestUrl() {
        return new AdRequest(this.mActivity).getRequestUrl(this.mAppKey);
    }

    private String getServer() {
        return LoopMe.URL_MOBILE;
    }

    private void setServer(String server) {
        LoopMe.URL_MOBILE = server;
    }

    void fetchAd(boolean silentMode) {
    }

    protected Activity getActivity() {
        return this.mActivity;
    }

    abstract AdFormat getAdFormat();

    AdParams getAdParams() {
        return this.mAdParams;
    }

    public String getAppKey() {
        return this.mAppKey;
    }

    public boolean getTestMode() {
        return this.mTestMode;
    }

    protected void setAdParams(AdParams params) {
        this.mAdParams = params;
    }

    protected void setInitStatus(boolean status) {
        this.mInit = status;
    }

    public void setTestMode(boolean mode) {
        Utilities.log(LOG_TAG, new StringBuilder("Setting test mode to ").append(mode).toString(), LogLevel.DEBUG);
        this.mTestMode = mode;
    }
}