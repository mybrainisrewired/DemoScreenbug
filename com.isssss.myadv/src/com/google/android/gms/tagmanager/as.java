package com.google.android.gms.tagmanager;

import android.content.Context;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.LinkedBlockingQueue;

class as extends Thread implements ar {
    private static as Ya;
    private final LinkedBlockingQueue<Runnable> XZ;
    private volatile at Yb;
    private volatile boolean mClosed;
    private final Context mContext;
    private volatile boolean tx;

    class AnonymousClass_1 implements Runnable {
        final /* synthetic */ ar Yc;
        final /* synthetic */ long Yd;
        final /* synthetic */ String Ye;

        AnonymousClass_1(ar arVar, long j, String str) {
            this.Yc = arVar;
            this.Yd = j;
            this.Ye = str;
        }

        public void run() {
            if (as.this.Yb == null) {
                cx lG = cx.lG();
                lG.a(as.this.mContext, this.Yc);
                as.this.Yb = lG.lH();
            }
            as.this.Yb.e(this.Yd, this.Ye);
        }
    }

    private as(Context context) {
        super("GAThread");
        this.XZ = new LinkedBlockingQueue();
        this.tx = false;
        this.mClosed = false;
        if (context != null) {
            this.mContext = context.getApplicationContext();
        } else {
            this.mContext = context;
        }
        start();
    }

    static as H(Context context) {
        if (Ya == null) {
            Ya = new as(context);
        }
        return Ya;
    }

    private String a(Throwable th) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        th.printStackTrace(printStream);
        printStream.flush();
        return new String(byteArrayOutputStream.toByteArray());
    }

    public void a(Runnable runnable) {
        this.XZ.add(runnable);
    }

    void b(String str, long j) {
        a(new AnonymousClass_1(this, j, str));
    }

    public void bC(String str) {
        b(str, System.currentTimeMillis());
    }

    public void run() {
        while (!this.mClosed) {
            try {
                Runnable runnable = (Runnable) this.XZ.take();
                if (!this.tx) {
                    runnable.run();
                }
            } catch (InterruptedException e) {
                try {
                    bh.x(e.toString());
                } catch (Throwable th) {
                    bh.w("Error on GAThread: " + a(th));
                    bh.w("Google Analytics is shutting down.");
                    this.tx = true;
                }
            }
        }
    }
}