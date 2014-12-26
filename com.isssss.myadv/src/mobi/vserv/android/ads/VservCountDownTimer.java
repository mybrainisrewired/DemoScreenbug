package mobi.vserv.android.ads;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

public abstract class VservCountDownTimer {
    private static final int MSG = 1;
    private final long mCountdownInterval;
    private Handler mHandler;
    private final long mMillisInFuture;
    private long mStartTime;
    private long mStopTimeInFuture;
    private int mTickCounter;

    public VservCountDownTimer(long millisInFuture, long countDownInterval) {
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                synchronized (VservCountDownTimer.this) {
                    long millisLeft = VservCountDownTimer.this.mStopTimeInFuture - SystemClock.elapsedRealtime();
                    if (millisLeft <= 0) {
                        VservCountDownTimer.this.onFinish();
                    } else if (millisLeft < VservCountDownTimer.this.mCountdownInterval) {
                        VservCountDownTimer.this.onTick(millisLeft);
                        sendMessageDelayed(obtainMessage(MSG), millisLeft);
                    } else {
                        long lastTickStart = SystemClock.elapsedRealtime();
                        VservCountDownTimer.this.onTick(millisLeft);
                        long now = SystemClock.elapsedRealtime();
                        long extraDelay = now - VservCountDownTimer.this.mStartTime - ((long) VservCountDownTimer.this.mTickCounter) * VservCountDownTimer.this.mCountdownInterval;
                        VservCountDownTimer vservCountDownTimer = VservCountDownTimer.this;
                        vservCountDownTimer.mTickCounter = vservCountDownTimer.mTickCounter + 1;
                        long delay = VservCountDownTimer.this.mCountdownInterval + lastTickStart - now - extraDelay;
                        while (delay < 0) {
                            delay += VservCountDownTimer.this.mCountdownInterval;
                        }
                        sendMessageDelayed(obtainMessage(MSG), delay);
                    }
                }
            }
        };
        Log.i("vserv", "Init VservCountDownTimer");
        this.mMillisInFuture = millisInFuture;
        this.mCountdownInterval = countDownInterval;
        this.mTickCounter = 0;
    }

    public final void cancel() {
        this.mHandler.removeMessages(MSG);
    }

    public abstract void onFinish();

    public abstract void onTick(long j);

    public final synchronized VservCountDownTimer start() {
        VservCountDownTimer this;
        if (this.mMillisInFuture <= 0) {
            onFinish();
            this = this;
        } else {
            this.mStartTime = SystemClock.elapsedRealtime();
            this.mStopTimeInFuture = this.mStartTime + this.mMillisInFuture;
            this.mHandler.sendMessage(this.mHandler.obtainMessage(MSG));
            this = this;
        }
        return this;
    }
}