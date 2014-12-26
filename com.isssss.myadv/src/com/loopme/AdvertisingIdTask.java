package com.loopme;

import android.content.Context;
import android.os.AsyncTask;
import com.mopub.common.Preconditions;

public class AdvertisingIdTask extends AsyncTask<Context, Void, Void> {
    private static final String LOG_TAG;
    private boolean silentMode;

    static {
        LOG_TAG = AdvertisingIdTask.class.getSimpleName();
    }

    public AdvertisingIdTask(boolean mode) {
        this.silentMode = mode;
    }

    protected Void doInBackground(Context... params) {
        Context context = params[0];
        try {
            LoopMe.getInstance(context).setAdvertisingId(AdvertisingIdClient.getAdvertisingIdInfo(context).getId());
        } catch (Exception e) {
            Utilities.log(LOG_TAG, new StringBuilder("Exception: ").append(e.getMessage()).toString(), LogLevel.ERROR);
            LoopMe.getInstance(context).setAdvertisingId(Preconditions.EMPTY_ARGUMENTS);
        }
        return null;
    }

    protected void onPostExecute(Void result) {
        BaseLoopMeHolder.get().fetchAd(this.silentMode);
        super.onPostExecute(result);
    }
}