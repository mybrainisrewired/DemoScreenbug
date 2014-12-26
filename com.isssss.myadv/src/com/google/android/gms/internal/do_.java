package com.google.android.gms.internal;

public abstract class do_ {
    private final Runnable kW;
    private volatile Thread qY;

    public do_() {
        this.kW = new Runnable() {
            public final void run() {
                do_.this.qY = Thread.currentThread();
                do_.this.aY();
            }
        };
    }

    public abstract void aY();

    public final void cancel() {
        onStop();
        if (this.qY != null) {
            this.qY.interrupt();
        }
    }

    public abstract void onStop();

    public final void start() {
        dp.execute(this.kW);
    }
}