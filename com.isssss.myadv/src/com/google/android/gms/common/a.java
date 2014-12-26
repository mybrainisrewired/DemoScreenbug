package com.google.android.gms.common;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class a implements ServiceConnection {
    boolean Ae;
    private final BlockingQueue<IBinder> Af;

    public a() {
        this.Ae = false;
        this.Af = new LinkedBlockingQueue();
    }

    public IBinder dV() throws InterruptedException {
        if (this.Ae) {
            throw new IllegalStateException();
        }
        this.Ae = true;
        return (IBinder) this.Af.take();
    }

    public void onServiceConnected(ComponentName name, IBinder service) {
        try {
            this.Af.put(service);
        } catch (InterruptedException e) {
        }
    }

    public void onServiceDisconnected(ComponentName name) {
    }
}