package com.mopub.mobileads;

import android.os.AsyncTask;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.ResponseHeader;
import com.mopub.mobileads.factories.HttpClientFactory;
import com.mopub.mobileads.util.HttpResponses;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;

public class AdFetchTask extends AsyncTask<String, Void, AdLoadTask> {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$mopub$mobileads$AdFetcher$FetchStatus = null;
    private static final double EXPONENTIAL_BACKOFF_FACTOR = 1.5d;
    private static final int MAXIMUM_REFRESH_TIME_MILLISECONDS = 600000;
    private AdViewController mAdViewController;
    private Exception mException;
    private FetchStatus mFetchStatus;
    private HttpClient mHttpClient;
    private long mTaskId;
    private TaskTracker mTaskTracker;
    private String mUserAgent;

    static /* synthetic */ int[] $SWITCH_TABLE$com$mopub$mobileads$AdFetcher$FetchStatus() {
        int[] iArr = $SWITCH_TABLE$com$mopub$mobileads$AdFetcher$FetchStatus;
        if (iArr == null) {
            iArr = new int[FetchStatus.values().length];
            try {
                iArr[FetchStatus.AD_WARMING_UP.ordinal()] = 6;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[FetchStatus.CLEAR_AD_TYPE.ordinal()] = 5;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[FetchStatus.FETCH_CANCELLED.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[FetchStatus.INVALID_SERVER_RESPONSE_BACKOFF.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[FetchStatus.INVALID_SERVER_RESPONSE_NOBACKOFF.ordinal()] = 4;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[FetchStatus.NOT_SET.ordinal()] = 1;
            } catch (NoSuchFieldError e6) {
            }
            $SWITCH_TABLE$com$mopub$mobileads$AdFetcher$FetchStatus = iArr;
        }
        return iArr;
    }

    public AdFetchTask(TaskTracker taskTracker, AdViewController adViewController, String userAgent, int timeoutMilliseconds) {
        this.mFetchStatus = FetchStatus.NOT_SET;
        this.mTaskTracker = taskTracker;
        this.mAdViewController = adViewController;
        this.mHttpClient = HttpClientFactory.create(timeoutMilliseconds);
        this.mTaskId = this.mTaskTracker.getCurrentTaskId();
        this.mUserAgent = userAgent;
    }

    private void cleanup() {
        this.mTaskTracker = null;
        this.mException = null;
        this.mFetchStatus = FetchStatus.NOT_SET;
    }

    private void exponentialBackoff() {
        if (this.mAdViewController != null) {
            int refreshTimeMilliseconds = (int) (((double) this.mAdViewController.getRefreshTimeMilliseconds()) * 1.5d);
            if (refreshTimeMilliseconds > 600000) {
                refreshTimeMilliseconds = MAXIMUM_REFRESH_TIME_MILLISECONDS;
            }
            this.mAdViewController.setRefreshTimeMilliseconds(refreshTimeMilliseconds);
        }
    }

    private AdLoadTask fetch(String url) throws Exception {
        HttpGet httpget = new HttpGet(url);
        httpget.addHeader(ResponseHeader.USER_AGENT.getKey(), this.mUserAgent);
        if (!isStateValid()) {
            return null;
        }
        HttpResponse response = this.mHttpClient.execute(httpget);
        if (!isResponseValid(response)) {
            return null;
        }
        this.mAdViewController.configureUsingHttpResponse(response);
        return responseContainsContent(response) ? AdLoadTask.fromHttpResponse(response, this.mAdViewController) : null;
    }

    private boolean isMostCurrentTask() {
        return this.mTaskTracker == null ? false : this.mTaskTracker.isMostCurrentTask(this.mTaskId);
    }

    private boolean isResponseValid(HttpResponse response) {
        if (response == null || response.getEntity() == null) {
            MoPubLog.d("MoPub server returned null response.");
            this.mFetchStatus = FetchStatus.INVALID_SERVER_RESPONSE_NOBACKOFF;
            return false;
        } else {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 400) {
                MoPubLog.d(new StringBuilder("Server error: returned HTTP status code ").append(Integer.toString(statusCode)).append(". Please try again.").toString());
                this.mFetchStatus = FetchStatus.INVALID_SERVER_RESPONSE_BACKOFF;
                return false;
            } else if (statusCode == 200) {
                return true;
            } else {
                MoPubLog.d(new StringBuilder("MoPub server returned invalid response: HTTP status code ").append(Integer.toString(statusCode)).append(".").toString());
                this.mFetchStatus = FetchStatus.INVALID_SERVER_RESPONSE_NOBACKOFF;
                return false;
            }
        }
    }

    private boolean isStateValid() {
        if (isCancelled()) {
            this.mFetchStatus = FetchStatus.FETCH_CANCELLED;
            return false;
        } else if (this.mAdViewController != null && !this.mAdViewController.isDestroyed()) {
            return true;
        } else {
            MoPubLog.d("Error loading ad: AdViewController has already been GCed or destroyed.");
            return false;
        }
    }

    private boolean responseContainsContent(HttpResponse response) {
        if ("1".equals(HttpResponses.extractHeader(response, ResponseHeader.WARMUP))) {
            MoPubLog.d(new StringBuilder("Ad Unit (").append(this.mAdViewController.getAdUnitId()).append(") is still warming up. ").append("Please try again in a few minutes.").toString());
            this.mFetchStatus = FetchStatus.AD_WARMING_UP;
            return false;
        } else {
            if (!"clear".equals(HttpResponses.extractHeader(response, ResponseHeader.AD_TYPE))) {
                return true;
            }
            MoPubLog.d(new StringBuilder("No ads found for adunit (").append(this.mAdViewController.getAdUnitId()).append(").").toString());
            this.mFetchStatus = FetchStatus.CLEAR_AD_TYPE;
            return false;
        }
    }

    private void shutdownHttpClient() {
        if (this.mHttpClient != null) {
            ClientConnectionManager manager = this.mHttpClient.getConnectionManager();
            if (manager != null) {
                manager.shutdown();
            }
            this.mHttpClient = null;
        }
    }

    protected AdLoadTask doInBackground(String... urls) {
        AdLoadTask result = null;
        try {
            result = fetch(urls[0]);
            shutdownHttpClient();
            return result;
        } catch (Exception e) {
            this.mException = e;
            shutdownHttpClient();
            return result;
        }
    }

    protected void onCancelled() {
        if (isMostCurrentTask()) {
            MoPubLog.d("Ad loading was cancelled.");
            if (this.mException != null) {
                MoPubLog.d(new StringBuilder("Exception caught while loading ad: ").append(this.mException).toString());
            }
            this.mTaskTracker.markTaskCompleted(this.mTaskId);
            cleanup();
        } else {
            MoPubLog.d("Ad response is stale.");
            cleanup();
        }
    }

    protected void onPostExecute(AdLoadTask adLoadTask) {
        if (!isMostCurrentTask()) {
            MoPubLog.d("Ad response is stale.");
            cleanup();
        } else if (this.mAdViewController == null || this.mAdViewController.isDestroyed()) {
            if (adLoadTask != null) {
                adLoadTask.cleanup();
            }
            this.mTaskTracker.markTaskCompleted(this.mTaskId);
            cleanup();
        } else {
            if (adLoadTask == null) {
                MoPubErrorCode errorCode;
                if (this.mException != null) {
                    MoPubLog.d(new StringBuilder("Exception caught while loading ad: ").append(this.mException).toString());
                }
                switch ($SWITCH_TABLE$com$mopub$mobileads$AdFetcher$FetchStatus()[this.mFetchStatus.ordinal()]) {
                    case MMAdView.TRANSITION_FADE:
                        errorCode = MoPubErrorCode.UNSPECIFIED;
                        break;
                    case MMAdView.TRANSITION_UP:
                        errorCode = MoPubErrorCode.CANCELLED;
                        break;
                    case MMAdView.TRANSITION_DOWN:
                    case MMAdView.TRANSITION_RANDOM:
                        errorCode = MoPubErrorCode.SERVER_ERROR;
                        break;
                    case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                        errorCode = MoPubErrorCode.NO_FILL;
                        break;
                    default:
                        errorCode = MoPubErrorCode.UNSPECIFIED;
                        break;
                }
                this.mAdViewController.adDidFail(errorCode);
                if (this.mFetchStatus == FetchStatus.INVALID_SERVER_RESPONSE_BACKOFF) {
                    exponentialBackoff();
                    this.mFetchStatus = FetchStatus.NOT_SET;
                }
            } else {
                adLoadTask.execute();
                adLoadTask.cleanup();
            }
            this.mTaskTracker.markTaskCompleted(this.mTaskId);
            cleanup();
        }
    }
}