package com.loopme;

import android.content.Context;
import com.loopme.utilites.LanguageManager;
import com.mopub.common.Preconditions;

public class LoopMe {
    private static final String LOG_TAG;
    public static final String SDK_VERSION = "3.0";
    public static String URL_MOBILE;
    private static LoopMe loopMe;
    private static volatile boolean mIsInitializationComplete;
    private static LogLevel mLogLevel;
    private static String mLoopMeId;
    private volatile String mAdvertisingId;

    static {
        LOG_TAG = LoopMe.class.getSimpleName();
        URL_MOBILE = "loopme.me/api/loopme/ads";
        mLogLevel = LogLevel.DEBUG;
    }

    private LoopMe() {
        this.mAdvertisingId = Preconditions.EMPTY_ARGUMENTS;
    }

    static LoopMe getInstance(Context ctx) {
        if (loopMe == null) {
            loopMe = new LoopMe();
            LanguageManager.getInstance(ctx);
            Utilities.init(ctx);
        }
        return loopMe;
    }

    static String getServer() {
        return URL_MOBILE;
    }

    String getAdvertisingId() {
        return this.mAdvertisingId;
    }

    String getLoopMeId() {
        return mLoopMeId;
    }

    boolean isInitComplete() {
        return mIsInitializationComplete;
    }

    void setAdvertisingId(String advId) {
        Utilities.log(LOG_TAG, new StringBuilder("Advertising Id init completed. Id = ").append(advId).toString(), LogLevel.DEBUG);
        this.mAdvertisingId = advId;
        mIsInitializationComplete = true;
    }

    void storeLoopMeId(String loopmeId) {
        mLoopMeId = loopmeId;
    }
}