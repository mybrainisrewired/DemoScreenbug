package com.google.android.gms.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.millennialmedia.android.MMAdView;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public final class fh implements Callback {
    private static final Object Du;
    private static fh Dv;
    private final HashMap<String, a> Dw;
    private final Context lp;
    private final Handler mHandler;

    final class a {
        private boolean DA;
        private IBinder DB;
        private ComponentName DC;
        private final String Dx;
        private final a Dy;
        private final HashSet<f> Dz;
        private int mState;

        public class a implements ServiceConnection {
            public void onServiceConnected(ComponentName component, IBinder binder) {
                synchronized (a.this.DD.Dw) {
                    a.this.DB = binder;
                    a.this.DC = component;
                    Iterator it = a.this.Dz.iterator();
                    while (it.hasNext()) {
                        ((f) it.next()).onServiceConnected(component, binder);
                    }
                    a.this.mState = 1;
                }
            }

            public void onServiceDisconnected(ComponentName component) {
                synchronized (a.this.DD.Dw) {
                    a.this.DB = null;
                    a.this.DC = component;
                    Iterator it = a.this.Dz.iterator();
                    while (it.hasNext()) {
                        ((f) it.next()).onServiceDisconnected(component);
                    }
                    a.this.mState = MMAdView.TRANSITION_UP;
                }
            }
        }

        public a(String str) {
            this.Dx = str;
            this.Dy = new a();
            this.Dz = new HashSet();
            this.mState = 0;
        }

        public void a(f fVar) {
            this.Dz.add(fVar);
        }

        public void b(f fVar) {
            this.Dz.remove(fVar);
        }

        public boolean c(f fVar) {
            return this.Dz.contains(fVar);
        }

        public a eP() {
            return this.Dy;
        }

        public String eQ() {
            return this.Dx;
        }

        public boolean eR() {
            return this.Dz.isEmpty();
        }

        public IBinder getBinder() {
            return this.DB;
        }

        public ComponentName getComponentName() {
            return this.DC;
        }

        public int getState() {
            return this.mState;
        }

        public boolean isBound() {
            return this.DA;
        }

        public void y(boolean z) {
            this.DA = z;
        }
    }

    static {
        Du = new Object();
    }

    private fh(Context context) {
        this.mHandler = new Handler(context.getMainLooper(), this);
        this.Dw = new HashMap();
        this.lp = context.getApplicationContext();
    }

    public static fh x(Context context) {
        synchronized (Du) {
            if (Dv == null) {
                Dv = new fh(context.getApplicationContext());
            }
        }
        return Dv;
    }

    public boolean a(String str, f fVar) {
        boolean isBound;
        synchronized (this.Dw) {
            a aVar = (a) this.Dw.get(str);
            if (aVar == null) {
                aVar = new a(str);
                aVar.a(fVar);
                aVar.y(this.lp.bindService(new Intent(str).setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE), aVar.eP(), 129));
                this.Dw.put(str, aVar);
            } else {
                this.mHandler.removeMessages(0, aVar);
                if (aVar.c(fVar)) {
                    throw new IllegalStateException("Trying to bind a GmsServiceConnection that was already connected before.  startServiceAction=" + str);
                }
                aVar.a(fVar);
                switch (aVar.getState()) {
                    case MMAdView.TRANSITION_FADE:
                        fVar.onServiceConnected(aVar.getComponentName(), aVar.getBinder());
                        break;
                    case MMAdView.TRANSITION_UP:
                        aVar.y(this.lp.bindService(new Intent(str).setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE), aVar.eP(), 129));
                        break;
                    default:
                        break;
                }
            }
            isBound = aVar.isBound();
        }
        return isBound;
    }

    public void b(String str, f fVar) {
        synchronized (this.Dw) {
            a aVar = (a) this.Dw.get(str);
            if (aVar == null) {
                throw new IllegalStateException("Nonexistent connection status for service action: " + str);
            } else if (aVar.c(fVar)) {
                aVar.b(fVar);
                if (aVar.eR()) {
                    this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0, aVar), 5000);
                }
            } else {
                throw new IllegalStateException("Trying to unbind a GmsServiceConnection  that was not bound before.  startServiceAction=" + str);
            }
        }
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MMAdView.TRANSITION_NONE:
                a aVar = (a) msg.obj;
                synchronized (this.Dw) {
                    if (aVar.eR()) {
                        this.lp.unbindService(aVar.eP());
                        this.Dw.remove(aVar.eQ());
                    }
                }
                return true;
            default:
                return false;
        }
    }
}