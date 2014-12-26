package com.facebook.ads.internal.action;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.facebook.ads.VideoAdActivity;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class VideoAppAdAction extends AppAdAction {
    private static final String TAG;
    private final Context context;
    private final Uri uri;

    static {
        TAG = VideoAppAdAction.class.getSimpleName();
    }

    public VideoAppAdAction(Context context, Uri uri) {
        super(context, uri);
        this.context = context;
        this.uri = uri;
    }

    private static void addMapToIntentExtras(Map<String, String> map, Intent intent) {
        Iterator i$ = map.entrySet().iterator();
        while (i$.hasNext()) {
            Entry<String, String> entry = (Entry) i$.next();
            intent.putExtra((String) entry.getKey(), (String) entry.getValue());
        }
    }

    public void execute(Map<String, String> intentExtras) {
        logAdClick(this.context, this.uri);
        String videoURL = this.uri.getQueryParameter("video_url");
        Intent videoIntent = new Intent(this.context, VideoAdActivity.class);
        videoIntent.putExtra(VideoAdActivity.URI_INTENT_EXTRA, this.uri.toString());
        videoIntent.putExtra(VideoAdActivity.VIDEO_PATH_INTENT_EXTRA, videoURL);
        videoIntent.putExtra(VideoAdActivity.MARKET_URI_INTENT_EXTRA, getMarketUri().toString());
        addMapToIntentExtras(intentExtras, videoIntent);
        try {
            this.context.startActivity(videoIntent);
        } catch (Exception e) {
            Log.d(TAG, "Failed to start video", e);
        }
    }
}