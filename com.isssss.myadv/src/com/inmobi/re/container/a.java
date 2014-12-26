package com.inmobi.re.container;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.inmobi.commons.internal.Log;
import com.inmobi.re.controller.util.Constants;

// compiled from: IMWebView.java
class a implements OnCompletionListener {
    final /* synthetic */ IMWebView a;

    a(IMWebView iMWebView) {
        this.a = iMWebView;
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        try {
            mediaPlayer.stop();
            this.a.q.setVisibility(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
            this.a.f();
            this.a.y.setContentView(this.a.p);
        } catch (Exception e) {
            Log.internal(Constants.RENDERING_LOG_TAG, "Media Player onCompletion", e);
        }
    }
}