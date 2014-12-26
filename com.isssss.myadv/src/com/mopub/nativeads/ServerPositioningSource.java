package com.mopub.nativeads;

import android.content.Context;
import android.os.Handler;
import com.mopub.common.DownloadResponse;
import com.mopub.common.DownloadTask;
import com.mopub.common.DownloadTask.DownloadTaskListener;
import com.mopub.common.HttpClient;
import com.mopub.common.HttpResponses;
import com.mopub.common.Preconditions;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.AsyncTasks;
import com.mopub.mobileads.MoPubView;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;
import com.mopub.nativeads.PositioningSource.PositioningListener;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class ServerPositioningSource implements PositioningSource {
    private static final double DEFAULT_RETRY_TIME_MILLISECONDS = 1000.0d;
    private static final double EXPONENTIAL_BACKOFF_FACTOR = 2.0d;
    private static final String FIXED_KEY = "fixed";
    private static final String INTERVAL_KEY = "interval";
    @VisibleForTesting
    static int MAXIMUM_RETRY_TIME_MILLISECONDS = 0;
    private static final int MAX_VALUE = 65536;
    private static final String POSITION_KEY = "position";
    private static final String REPEATING_KEY = "repeating";
    private static final String SECTION_KEY = "section";
    private final Context mContext;
    private DownloadTask mDownloadTask;
    private final DownloadTaskProvider mDownloadTaskProvider;
    private PositioningListener mListener;
    private int mRetryCount;
    private final Handler mRetryHandler;
    private final Runnable mRetryRunnable;
    private String mRetryUrl;
    private DownloadTaskListener mTaskListener;

    @VisibleForTesting
    static class DownloadTaskProvider {
        DownloadTaskProvider() {
        }

        DownloadTask get(DownloadTaskListener listener) {
            return new DownloadTask(listener);
        }
    }

    static {
        MAXIMUM_RETRY_TIME_MILLISECONDS = 300000;
    }

    ServerPositioningSource(Context context) {
        this(context, new DownloadTaskProvider());
    }

    @VisibleForTesting
    ServerPositioningSource(Context context, DownloadTaskProvider downloadTaskProvider) {
        this.mTaskListener = new DownloadTaskListener() {
            public void onComplete(String url, DownloadResponse downloadResponse) {
                if (downloadResponse != null) {
                    ServerPositioningSource.this.mDownloadTask = null;
                    if (downloadResponse.getStatusCode() != 200) {
                        MoPubLog.e("Invalid positioning download response ");
                        ServerPositioningSource.this.handleFailure();
                    } else {
                        try {
                            ServerPositioningSource.this.handleSuccess(ServerPositioningSource.this.parseJsonResponse(HttpResponses.asResponseString(downloadResponse)));
                        } catch (JSONException e) {
                            MoPubLog.e("Error parsing JSON: ", e);
                            ServerPositioningSource.this.handleFailure();
                        }
                    }
                }
            }
        };
        this.mContext = context.getApplicationContext();
        this.mDownloadTaskProvider = downloadTaskProvider;
        this.mRetryHandler = new Handler();
        this.mRetryRunnable = new Runnable() {
            public void run() {
                ServerPositioningSource.this.requestPositioningInternal();
            }
        };
    }

    private void handleFailure() {
        int delay = (int) (1000.0d * Math.pow(EXPONENTIAL_BACKOFF_FACTOR, (double) (this.mRetryCount + 1)));
        if (delay >= MAXIMUM_RETRY_TIME_MILLISECONDS) {
            MoPubLog.d("Error downloading positioning information");
            this.mListener.onFailed();
            this.mListener = null;
        } else {
            this.mRetryCount++;
            this.mRetryHandler.postDelayed(this.mRetryRunnable, (long) delay);
        }
    }

    private void handleSuccess(MoPubClientPositioning positioning) {
        this.mListener.onLoad(positioning);
        this.mListener = null;
        this.mRetryCount = 0;
    }

    private void parseFixedJson(JSONArray fixed, MoPubClientPositioning positioning) throws JSONException {
        int i = 0;
        while (i < fixed.length()) {
            JSONObject positionObject = fixed.getJSONObject(i);
            int section = positionObject.optInt(SECTION_KEY, 0);
            if (section < 0) {
                throw new JSONException(new StringBuilder("Invalid section ").append(section).append(" in JSON response").toString());
            }
            if (section <= 0) {
                int position = positionObject.getInt(POSITION_KEY);
                if (position >= 0 && position <= 65536) {
                    positioning.addFixedPosition(position);
                }
                throw new JSONException(new StringBuilder("Invalid position ").append(position).append(" in JSON response").toString());
            }
            i++;
        }
    }

    private void parseRepeatingJson(JSONObject repeatingObject, MoPubClientPositioning positioning) throws JSONException {
        int interval = repeatingObject.getInt(INTERVAL_KEY);
        if (interval < 2 || interval > 65536) {
            throw new JSONException(new StringBuilder("Invalid interval ").append(interval).append(" in JSON response").toString());
        }
        positioning.enableRepeatingPositions(interval);
    }

    private void requestPositioningInternal() {
        MoPubLog.d(new StringBuilder("Loading positioning from: ").append(this.mRetryUrl).toString());
        this.mDownloadTask = this.mDownloadTaskProvider.get(this.mTaskListener);
        HttpGet httpGet = HttpClient.initializeHttpGet(this.mRetryUrl, this.mContext);
        AsyncTasks.safeExecuteOnExecutor(this.mDownloadTask, new HttpUriRequest[]{httpGet});
    }

    public void loadPositions(String adUnitId, PositioningListener listener) {
        if (this.mDownloadTask != null) {
            this.mDownloadTask.cancel(true);
            this.mDownloadTask = null;
        }
        if (this.mRetryCount > 0) {
            this.mRetryHandler.removeCallbacks(this.mRetryRunnable);
            this.mRetryCount = 0;
        }
        this.mListener = listener;
        this.mRetryUrl = new PositioningUrlGenerator(this.mContext).withAdUnitId(adUnitId).generateUrlString(MoPubView.HOST);
        requestPositioningInternal();
    }

    @VisibleForTesting
    MoPubClientPositioning parseJsonResponse(String json) throws JSONException {
        if (json == null || json.equals(Preconditions.EMPTY_ARGUMENTS)) {
            throw new JSONException("Empty response");
        }
        JSONObject jsonObject = new JSONObject(json);
        String error = jsonObject.optString("error", null);
        if (error != null) {
            throw new JSONException(error);
        }
        JSONArray fixed = jsonObject.optJSONArray(FIXED_KEY);
        JSONObject repeating = jsonObject.optJSONObject(REPEATING_KEY);
        MoPubClientPositioning positioning = new MoPubClientPositioning();
        if (fixed == null && repeating == null) {
            throw new JSONException("Must contain fixed or repeating positions");
        }
        if (fixed != null) {
            parseFixedJson(fixed, positioning);
        }
        if (repeating != null) {
            parseRepeatingJson(repeating, positioning);
        }
        return positioning;
    }
}