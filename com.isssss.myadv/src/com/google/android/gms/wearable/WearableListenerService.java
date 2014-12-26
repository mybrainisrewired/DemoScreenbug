package com.google.android.gms.wearable;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.internal.ki;
import com.google.android.gms.internal.kk;
import com.millennialmedia.android.MMAdView;

public abstract class WearableListenerService extends Service {
    public static final String BIND_LISTENER_INTENT_ACTION = "com.google.android.gms.wearable.BIND_LISTENER";
    private IBinder DB;
    private volatile int adu;
    private String adv;
    private Handler adw;

    private class a extends com.google.android.gms.internal.kh.a {

        class AnonymousClass_1 implements Runnable {
            final /* synthetic */ DataHolder ady;

            AnonymousClass_1(DataHolder dataHolder) {
                this.ady = dataHolder;
            }

            public void run() {
                b bVar = new b(this.ady);
                a.this.adx.onDataChanged(bVar);
                bVar.close();
            }
        }

        class AnonymousClass_2 implements Runnable {
            final /* synthetic */ ki adA;

            AnonymousClass_2(ki kiVar) {
                this.adA = kiVar;
            }

            public void run() {
                a.this.adx.onMessageReceived(this.adA);
            }
        }

        class AnonymousClass_3 implements Runnable {
            final /* synthetic */ kk adB;

            AnonymousClass_3(kk kkVar) {
                this.adB = kkVar;
            }

            public void run() {
                a.this.adx.onPeerConnected(this.adB);
            }
        }

        class AnonymousClass_4 implements Runnable {
            final /* synthetic */ kk adB;

            AnonymousClass_4(kk kkVar) {
                this.adB = kkVar;
            }

            public void run() {
                a.this.adx.onPeerDisconnected(this.adB);
            }
        }

        private a() {
        }

        public void M(DataHolder dataHolder) {
            if (Log.isLoggable("WearableLS", MMAdView.TRANSITION_DOWN)) {
                Log.d("WearableLS", "onDataItemChanged: " + WearableListenerService.this.adv + ": " + dataHolder);
            }
            WearableListenerService.this.md();
            WearableListenerService.this.adw.post(new AnonymousClass_1(dataHolder));
        }

        public void a(ki kiVar) {
            if (Log.isLoggable("WearableLS", MMAdView.TRANSITION_DOWN)) {
                Log.d("WearableLS", "onMessageReceived: " + kiVar);
            }
            WearableListenerService.this.md();
            WearableListenerService.this.adw.post(new AnonymousClass_2(kiVar));
        }

        public void a(kk kkVar) {
            if (Log.isLoggable("WearableLS", MMAdView.TRANSITION_DOWN)) {
                Log.d("WearableLS", "onPeerConnected: " + WearableListenerService.this.adv + ": " + kkVar);
            }
            WearableListenerService.this.md();
            WearableListenerService.this.adw.post(new AnonymousClass_3(kkVar));
        }

        public void b(kk kkVar) {
            if (Log.isLoggable("WearableLS", MMAdView.TRANSITION_DOWN)) {
                Log.d("WearableLS", "onPeerDisconnected: " + WearableListenerService.this.adv + ": " + kkVar);
            }
            WearableListenerService.this.md();
            WearableListenerService.this.adw.post(new AnonymousClass_4(kkVar));
        }
    }

    public WearableListenerService() {
        this.adu = -1;
    }

    private boolean cM(int i) {
        String str = GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE;
        String[] packagesForUid = getPackageManager().getPackagesForUid(i);
        if (packagesForUid == null) {
            return false;
        }
        int i2 = 0;
        while (i2 < packagesForUid.length) {
            if (str.equals(packagesForUid[i2])) {
                return true;
            }
            i2++;
        }
        return false;
    }

    private void md() throws SecurityException {
        int callingUid = Binder.getCallingUid();
        if (callingUid != this.adu) {
            if (GooglePlayServicesUtil.b(getPackageManager(), GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE) && cM(callingUid)) {
                this.adu = callingUid;
            } else {
                throw new SecurityException("Caller is not GooglePlayServices");
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return BIND_LISTENER_INTENT_ACTION.equals(intent.getAction()) ? this.DB : null;
    }

    public void onCreate() {
        super.onCreate();
        if (Log.isLoggable("WearableLS", MMAdView.TRANSITION_DOWN)) {
            Log.d("WearableLS", "onCreate: " + getPackageName());
        }
        this.adv = getPackageName();
        HandlerThread handlerThread = new HandlerThread("WearableListenerService");
        handlerThread.start();
        this.adw = new Handler(handlerThread.getLooper());
        this.DB = new a(null);
    }

    public void onDataChanged(b dataEvents) {
    }

    public void onDestroy() {
        this.adw.getLooper().quit();
        super.onDestroy();
    }

    public void onMessageReceived(e messageEvent) {
    }

    public void onPeerConnected(f peer) {
    }

    public void onPeerDisconnected(f peer) {
    }
}