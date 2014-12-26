package com.mopub.mobileads.factories;

import com.mopub.mobileads.AdFetchTask;
import com.mopub.mobileads.AdViewController;
import com.mopub.mobileads.TaskTracker;

public class AdFetchTaskFactory {
    protected static AdFetchTaskFactory instance;

    static {
        instance = new AdFetchTaskFactory();
    }

    public static AdFetchTask create(TaskTracker taskTracker, AdViewController adViewController, String userAgent, int timeoutMilliseconds) {
        return instance.internalCreate(taskTracker, adViewController, userAgent, timeoutMilliseconds);
    }

    @Deprecated
    public static void setInstance(AdFetchTaskFactory factory) {
        instance = factory;
    }

    protected AdFetchTask internalCreate(TaskTracker taskTracker, AdViewController adViewController, String userAgent, int timeoutMilliseconds) {
        return new AdFetchTask(taskTracker, adViewController, userAgent, timeoutMilliseconds);
    }
}