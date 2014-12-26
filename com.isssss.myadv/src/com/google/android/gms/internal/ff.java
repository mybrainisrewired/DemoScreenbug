package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.data.DataHolder;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.ArrayList;

public abstract class ff<T extends IInterface> implements GooglePlayServicesClient, com.google.android.gms.common.api.Api.a, com.google.android.gms.internal.fg.b {
    public static final String[] Dg;
    private final Looper AS;
    private final fg Bc;
    private T Da;
    private final ArrayList<b<?>> Db;
    private f Dc;
    private volatile int Dd;
    private final String[] De;
    boolean Df;
    private final Context mContext;
    final Handler mHandler;

    final class a extends Handler {
        public a(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            b bVar;
            if (msg.what == 1 && !ff.this.isConnecting()) {
                bVar = (b) msg.obj;
                bVar.dx();
                bVar.unregister();
            } else if (msg.what == 3) {
                ff.this.Bc.a(new ConnectionResult(((Integer) msg.obj).intValue(), null));
            } else if (msg.what == 4) {
                ff.this.M(1);
                ff.this.Da = null;
                ff.this.Bc.O(((Integer) msg.obj).intValue());
            } else if (msg.what == 2 && !ff.this.isConnected()) {
                bVar = (b) msg.obj;
                bVar.dx();
                bVar.unregister();
            } else if (msg.what == 2 || msg.what == 1) {
                ((b) msg.obj).eN();
            } else {
                Log.wtf("GmsClient", "Don't know how to handle this message.");
            }
        }
    }

    protected abstract class b<TListener> {
        private boolean Di;
        private TListener mListener;

        public b(TListener tListener) {
            this.mListener = tListener;
            this.Di = false;
        }

        protected abstract void a(TListener tListener);

        protected abstract void dx();

        public void eN() {
            synchronized (this) {
                Object obj = this.mListener;
                if (this.Di) {
                    Log.w("GmsClient", "Callback proxy " + this + " being reused. This is not safe.");
                }
            }
            if (obj != null) {
                try {
                    a(obj);
                } catch (RuntimeException e) {
                    RuntimeException runtimeException = e;
                    dx();
                    throw runtimeException;
                }
            } else {
                dx();
            }
            synchronized (this) {
                this.Di = true;
            }
            unregister();
        }

        public void eO() {
            synchronized (this) {
                this.mListener = null;
            }
        }

        public void unregister() {
            eO();
            synchronized (ff.this.Db) {
                ff.this.Db.remove(this);
            }
        }
    }

    final class f implements ServiceConnection {
        f() {
        }

        public void onServiceConnected(ComponentName component, IBinder binder) {
            ff.this.z(binder);
        }

        public void onServiceDisconnected(ComponentName component) {
            ff.this.mHandler.sendMessage(ff.this.mHandler.obtainMessage(MMAdView.TRANSITION_RANDOM, Integer.valueOf(1)));
        }
    }

    public static final class c implements ConnectionCallbacks {
        private final GooglePlayServicesClient.ConnectionCallbacks Dj;

        public c(GooglePlayServicesClient.ConnectionCallbacks connectionCallbacks) {
            this.Dj = connectionCallbacks;
        }

        public boolean equals(Object other) {
            return other instanceof com.google.android.gms.internal.ff.c ? this.Dj.equals(((com.google.android.gms.internal.ff.c) other).Dj) : this.Dj.equals(other);
        }

        public void onConnected(Bundle connectionHint) {
            this.Dj.onConnected(connectionHint);
        }

        public void onConnectionSuspended(int cause) {
            this.Dj.onDisconnected();
        }
    }

    public abstract class d<TListener> extends b<TListener> {
        private final DataHolder BB;

        public d(TListener tListener, DataHolder dataHolder) {
            super(tListener);
            this.BB = dataHolder;
        }

        protected final void a(TListener tListener) {
            a(tListener, this.BB);
        }

        protected abstract void a(TListener tListener, DataHolder dataHolder);

        protected void dx() {
            if (this.BB != null) {
                this.BB.close();
            }
        }

        public /* bridge */ /* synthetic */ void eN() {
            super.eN();
        }

        public /* bridge */ /* synthetic */ void eO() {
            super.eO();
        }

        public /* bridge */ /* synthetic */ void unregister() {
            super.unregister();
        }
    }

    protected final class h extends b<Boolean> {
        public final Bundle Dm;
        public final IBinder Dn;
        public final int statusCode;

        public h(int i, IBinder iBinder, Bundle bundle) {
            super(Boolean.valueOf(true));
            this.statusCode = i;
            this.Dn = iBinder;
            this.Dm = bundle;
        }

        protected /* synthetic */ void a(Object obj) {
            b((Boolean) obj);
        }

        protected void b(Boolean bool) {
            if (bool == null) {
                ff.this.M(1);
            } else {
                switch (this.statusCode) {
                    case MMAdView.TRANSITION_NONE:
                        try {
                            if (ff.this.bh().equals(this.Dn.getInterfaceDescriptor())) {
                                ff.this.Da = ff.this.r(this.Dn);
                                if (ff.this.Da != null) {
                                    ff.this.M(MMAdView.TRANSITION_DOWN);
                                    ff.this.Bc.bV();
                                    return;
                                }
                            }
                        } catch (RemoteException e) {
                        }
                        fh.x(ff.this.mContext).b(ff.this.bg(), ff.this.Dc);
                        ff.this.Dc = null;
                        ff.this.M(1);
                        ff.this.Da = null;
                        ff.this.Bc.a(new ConnectionResult(8, null));
                    case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                        ff.this.M(1);
                        throw new IllegalStateException("A fatal developer error has occurred. Check the logs for further information.");
                    default:
                        PendingIntent pendingIntent = this.Dm != null ? (PendingIntent) this.Dm.getParcelable("pendingIntent") : null;
                        if (ff.this.Dc != null) {
                            fh.x(ff.this.mContext).b(ff.this.bg(), ff.this.Dc);
                            ff.this.Dc = null;
                        }
                        ff.this.M(1);
                        ff.this.Da = null;
                        ff.this.Bc.a(new ConnectionResult(this.statusCode, pendingIntent));
                }
            }
        }

        protected void dx() {
        }
    }

    public static final class e extends com.google.android.gms.internal.fl.a {
        private ff Dk;

        public e(ff ffVar) {
            this.Dk = ffVar;
        }

        public void b(int i, IBinder iBinder, Bundle bundle) {
            fq.b((Object)"onPostInitComplete can be called only once per call to getServiceFromBroker", this.Dk);
            this.Dk.a(i, iBinder, bundle);
            this.Dk = null;
        }
    }

    public static final class g implements OnConnectionFailedListener {
        private final GooglePlayServicesClient.OnConnectionFailedListener Dl;

        public g(GooglePlayServicesClient.OnConnectionFailedListener onConnectionFailedListener) {
            this.Dl = onConnectionFailedListener;
        }

        public boolean equals(Object other) {
            return other instanceof com.google.android.gms.internal.ff.g ? this.Dl.equals(((com.google.android.gms.internal.ff.g) other).Dl) : this.Dl.equals(other);
        }

        public void onConnectionFailed(ConnectionResult result) {
            this.Dl.onConnectionFailed(result);
        }
    }

    static {
        Dg = new String[]{"service_esmobile", "service_googleme"};
    }

    protected ff(Context context, Object obj, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, String... strArr) {
        this.Db = new ArrayList();
        this.Dd = 1;
        this.Df = false;
        this.mContext = (Context) fq.f(context);
        this.AS = (Looper) fq.b(obj, (Object)"Looper must not be null");
        this.Bc = new fg(context, obj, this);
        this.mHandler = new a(obj);
        b(strArr);
        this.De = strArr;
        registerConnectionCallbacks((ConnectionCallbacks) fq.f(connectionCallbacks));
        registerConnectionFailedListener((OnConnectionFailedListener) fq.f(onConnectionFailedListener));
    }

    protected ff(Context context, GooglePlayServicesClient.ConnectionCallbacks connectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener onConnectionFailedListener, String... strArr) {
        this(context, context.getMainLooper(), new c(connectionCallbacks), new g(onConnectionFailedListener), strArr);
    }

    private void M(int i) {
        int i2 = this.Dd;
        this.Dd = i;
        if (i2 == i) {
            return;
        }
        if (i == 3) {
            onConnected();
        } else if (i2 == 3 && i == 1) {
            onDisconnected();
        }
    }

    public void N(int i) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(MMAdView.TRANSITION_RANDOM, Integer.valueOf(i)));
    }

    protected void a(int i, IBinder iBinder, Bundle bundle) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(1, new h(i, iBinder, bundle)));
    }

    public final void a(b<?> bVar) {
        synchronized (this.Db) {
            this.Db.add(bVar);
        }
        this.mHandler.sendMessage(this.mHandler.obtainMessage(MMAdView.TRANSITION_UP, bVar));
    }

    protected abstract void a(fm fmVar, e eVar) throws RemoteException;

    protected void b(String... strArr) {
    }

    protected final void bT() {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected. Call connect() and wait for onConnected() to be called.");
        }
    }

    protected abstract String bg();

    protected abstract String bh();

    public void connect() {
        this.Df = true;
        M(MMAdView.TRANSITION_UP);
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.mContext);
        if (isGooglePlayServicesAvailable != 0) {
            M(1);
            this.mHandler.sendMessage(this.mHandler.obtainMessage(MMAdView.TRANSITION_DOWN, Integer.valueOf(isGooglePlayServicesAvailable)));
        } else {
            if (this.Dc != null) {
                Log.e("GmsClient", "Calling connect() while still connected, missing disconnect().");
                this.Da = null;
                fh.x(this.mContext).b(bg(), this.Dc);
            }
            this.Dc = new f();
            if (!fh.x(this.mContext).a(bg(), this.Dc)) {
                Log.e("GmsClient", "unable to connect to service: " + bg());
                this.mHandler.sendMessage(this.mHandler.obtainMessage(MMAdView.TRANSITION_DOWN, Integer.valueOf(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES)));
            }
        }
    }

    public Bundle dG() {
        return null;
    }

    public void disconnect() {
        this.Df = false;
        synchronized (this.Db) {
            int size = this.Db.size();
            int i = 0;
            while (i < size) {
                ((b) this.Db.get(i)).eO();
                i++;
            }
            this.Db.clear();
        }
        M(1);
        this.Da = null;
        if (this.Dc != null) {
            fh.x(this.mContext).b(bg(), this.Dc);
            this.Dc = null;
        }
    }

    public final String[] eL() {
        return this.De;
    }

    protected final T eM() {
        bT();
        return this.Da;
    }

    public boolean em() {
        return this.Df;
    }

    public final Context getContext() {
        return this.mContext;
    }

    public final Looper getLooper() {
        return this.AS;
    }

    public boolean isConnected() {
        return this.Dd == 3;
    }

    public boolean isConnecting() {
        return this.Dd == 2;
    }

    public boolean isConnectionCallbacksRegistered(GooglePlayServicesClient.ConnectionCallbacks listener) {
        return this.Bc.isConnectionCallbacksRegistered(new c(listener));
    }

    public boolean isConnectionFailedListenerRegistered(GooglePlayServicesClient.OnConnectionFailedListener listener) {
        return this.Bc.isConnectionFailedListenerRegistered(listener);
    }

    protected void onConnected() {
    }

    protected void onDisconnected() {
    }

    protected abstract T r(IBinder iBinder);

    public void registerConnectionCallbacks(GooglePlayServicesClient.ConnectionCallbacks listener) {
        this.Bc.registerConnectionCallbacks(new c(listener));
    }

    public void registerConnectionCallbacks(ConnectionCallbacks listener) {
        this.Bc.registerConnectionCallbacks(listener);
    }

    public void registerConnectionFailedListener(GooglePlayServicesClient.OnConnectionFailedListener listener) {
        this.Bc.registerConnectionFailedListener(listener);
    }

    public void registerConnectionFailedListener(OnConnectionFailedListener listener) {
        this.Bc.registerConnectionFailedListener(listener);
    }

    public void unregisterConnectionCallbacks(GooglePlayServicesClient.ConnectionCallbacks listener) {
        this.Bc.unregisterConnectionCallbacks(new c(listener));
    }

    public void unregisterConnectionFailedListener(GooglePlayServicesClient.OnConnectionFailedListener listener) {
        this.Bc.unregisterConnectionFailedListener(listener);
    }

    protected final void z(IBinder iBinder) {
        try {
            a(com.google.android.gms.internal.fm.a.C(iBinder), new e(this));
        } catch (RemoteException e) {
            Log.w("GmsClient", "service died");
        }
    }
}