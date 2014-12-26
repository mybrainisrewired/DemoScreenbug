package com.mopub.mobileads;

import com.mopub.common.Preconditions;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.AsyncTasks;
import com.mopub.mobileads.factories.AdFetchTaskFactory;

public class AdFetcher {
    public static final String AD_CONFIGURATION_KEY = "Ad-Configuration";
    public static final String CLICKTHROUGH_URL_KEY = "Clickthrough-Url";
    public static final String HTML_RESPONSE_BODY_KEY = "Html-Response-Body";
    public static final String REDIRECT_URL_KEY = "Redirect-Url";
    public static final String SCROLLABLE_KEY = "Scrollable";
    private AdViewController mAdViewController;
    private AdFetchTask mCurrentTask;
    private final TaskTracker mTaskTracker;
    private int mTimeoutMilliseconds;
    private String mUserAgent;

    enum FetchStatus {
        NOT_SET,
        FETCH_CANCELLED,
        INVALID_SERVER_RESPONSE_BACKOFF,
        INVALID_SERVER_RESPONSE_NOBACKOFF,
        CLEAR_AD_TYPE,
        AD_WARMING_UP;

        static {
            NOT_SET = new FetchStatus("NOT_SET", 0);
            FETCH_CANCELLED = new FetchStatus("FETCH_CANCELLED", 1);
            INVALID_SERVER_RESPONSE_BACKOFF = new FetchStatus("INVALID_SERVER_RESPONSE_BACKOFF", 2);
            INVALID_SERVER_RESPONSE_NOBACKOFF = new FetchStatus("INVALID_SERVER_RESPONSE_NOBACKOFF", 3);
            CLEAR_AD_TYPE = new FetchStatus("CLEAR_AD_TYPE", 4);
            AD_WARMING_UP = new FetchStatus("AD_WARMING_UP", 5);
            ENUM$VALUES = new FetchStatus[]{NOT_SET, FETCH_CANCELLED, INVALID_SERVER_RESPONSE_BACKOFF, INVALID_SERVER_RESPONSE_NOBACKOFF, CLEAR_AD_TYPE, AD_WARMING_UP};
        }
    }

    public AdFetcher(AdViewController adview, String userAgent) {
        this.mTimeoutMilliseconds = 10000;
        this.mAdViewController = adview;
        this.mUserAgent = userAgent;
        this.mTaskTracker = new TaskTracker();
    }

    private long getCurrentTaskId() {
        return this.mTaskTracker.getCurrentTaskId();
    }

    public void cancelFetch() {
        if (this.mCurrentTask != null) {
            MoPubLog.i(new StringBuilder("Canceling fetch ad for task #").append(getCurrentTaskId()).toString());
            this.mCurrentTask.cancel(true);
        }
    }

    void cleanup() {
        cancelFetch();
        this.mAdViewController = null;
        this.mUserAgent = Preconditions.EMPTY_ARGUMENTS;
    }

    public void fetchAdForUrl(String url) {
        this.mTaskTracker.newTaskStarted();
        MoPubLog.i(new StringBuilder("Fetching ad for task #").append(getCurrentTaskId()).toString());
        if (this.mCurrentTask != null) {
            this.mCurrentTask.cancel(true);
        }
        this.mCurrentTask = AdFetchTaskFactory.create(this.mTaskTracker, this.mAdViewController, this.mUserAgent, this.mTimeoutMilliseconds);
        try {
            AsyncTasks.safeExecuteOnExecutor(this.mCurrentTask, new String[]{url});
        } catch (Exception e) {
            MoPubLog.d("Error executing AdFetchTask", e);
        }
    }

    protected void setTimeout(int milliseconds) {
        this.mTimeoutMilliseconds = milliseconds;
    }
}