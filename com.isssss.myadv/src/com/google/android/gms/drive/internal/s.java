package com.google.android.gms.drive.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.drive.events.DriveEvent;
import com.google.android.gms.drive.events.DriveEvent.Listener;
import com.google.android.gms.internal.fq;
import com.millennialmedia.android.MMAdView;

public class s<C extends DriveEvent> extends com.google.android.gms.drive.internal.w.a {
    private final int ES;
    private final a<C> FA;
    private final Listener<C> Fz;

    private static class a<E extends DriveEvent> extends Handler {
        private a(Looper looper) {
            super(looper);
        }

        public void a(Listener<E> listener, E e) {
            sendMessage(obtainMessage(1, new Pair(listener, e)));
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MMAdView.TRANSITION_FADE:
                    Pair pair = (Pair) msg.obj;
                    ((Listener) pair.first).onEvent((DriveEvent) pair.second);
                default:
                    Log.wtf("EventCallback", "Don't know how to handle this event");
            }
        }
    }

    public s(Looper looper, int i, Listener<C> listener) {
        this.ES = i;
        this.Fz = listener;
        this.FA = new a(null);
    }

    public void a(OnEventResponse onEventResponse) throws RemoteException {
        fq.x(this.ES == onEventResponse.getEventType());
        switch (onEventResponse.getEventType()) {
            case MMAdView.TRANSITION_FADE:
                this.FA.a(this.Fz, onEventResponse.fL());
            case MMAdView.TRANSITION_UP:
                this.FA.a(this.Fz, onEventResponse.fM());
            default:
                Log.w("EventCallback", "Unexpected event type:" + onEventResponse.getEventType());
        }
    }
}