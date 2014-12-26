package com.google.android.gms.internal;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.MediaController;
import android.widget.VideoView;
import com.google.android.gms.tagmanager.DataLayer;
import com.millennialmedia.android.MMAdView;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public final class cg extends FrameLayout implements OnCompletionListener, OnErrorListener, OnPreparedListener {
    private final dz lC;
    private final MediaController os;
    private final a ot;
    private final VideoView ou;
    private long ov;
    private String ow;

    private static final class a {
        private final Runnable kW;
        private volatile boolean ox;

        class AnonymousClass_1 implements Runnable {
            private final WeakReference<cg> oy;
            final /* synthetic */ cg oz;

            AnonymousClass_1(cg cgVar) {
                this.oz = cgVar;
                this.oy = new WeakReference(this.oz);
            }

            public void run() {
                cg cgVar = (cg) this.oy.get();
                if (!a.this.ox && cgVar != null) {
                    cgVar.aV();
                    a.this.aW();
                }
            }
        }

        public a(cg cgVar) {
            this.ox = false;
            this.kW = new AnonymousClass_1(cgVar);
        }

        public void aW() {
            dv.rp.postDelayed(this.kW, 250);
        }

        public void cancel() {
            this.ox = true;
            dv.rp.removeCallbacks(this.kW);
        }
    }

    public cg(Context context, dz dzVar) {
        super(context);
        this.lC = dzVar;
        this.ou = new VideoView(context);
        addView(this.ou, new LayoutParams(-1, -1, 17));
        this.os = new MediaController(context);
        this.ot = new a(this);
        this.ot.aW();
        this.ou.setOnCompletionListener(this);
        this.ou.setOnPreparedListener(this);
        this.ou.setOnErrorListener(this);
    }

    private static void a(dz dzVar, String str) {
        a(dzVar, str, new HashMap(1));
    }

    public static void a(dz dzVar, String str, String str2) {
        int i = str2 == null ? 1 : 0;
        Map hashMap = new HashMap(i != 0 ? MMAdView.TRANSITION_UP : MMAdView.TRANSITION_DOWN);
        hashMap.put("what", str);
        if (i == 0) {
            hashMap.put("extra", str2);
        }
        a(dzVar, "error", hashMap);
    }

    private static void a(dz dzVar, String str, String str2, String str3) {
        Map hashMap = new HashMap(2);
        hashMap.put(str2, str3);
        a(dzVar, str, hashMap);
    }

    private static void a(dz dzVar, String str, Map map) {
        map.put(DataLayer.EVENT_KEY, str);
        dzVar.a("onVideoEvent", map);
    }

    public void aU() {
        if (TextUtils.isEmpty(this.ow)) {
            a(this.lC, "no_src", null);
        } else {
            this.ou.setVideoPath(this.ow);
        }
    }

    public void aV() {
        long currentPosition = (long) this.ou.getCurrentPosition();
        if (this.ov != currentPosition) {
            a(this.lC, "timeupdate", "time", String.valueOf(((float) currentPosition) / 1000.0f));
            this.ov = currentPosition;
        }
    }

    public void b(MotionEvent motionEvent) {
        this.ou.dispatchTouchEvent(motionEvent);
    }

    public void destroy() {
        this.ot.cancel();
        this.ou.stopPlayback();
    }

    public void k(boolean z) {
        if (z) {
            this.ou.setMediaController(this.os);
        } else {
            this.os.hide();
            this.ou.setMediaController(null);
        }
    }

    public void o(String str) {
        this.ow = str;
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        a(this.lC, "ended");
    }

    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        a(this.lC, String.valueOf(what), String.valueOf(extra));
        return true;
    }

    public void onPrepared(MediaPlayer mediaPlayer) {
        a(this.lC, "canplaythrough", "duration", String.valueOf(((float) this.ou.getDuration()) / 1000.0f));
    }

    public void pause() {
        this.ou.pause();
    }

    public void play() {
        this.ou.start();
    }

    public void seekTo(int timeInMilliseconds) {
        this.ou.seekTo(timeInMilliseconds);
    }
}