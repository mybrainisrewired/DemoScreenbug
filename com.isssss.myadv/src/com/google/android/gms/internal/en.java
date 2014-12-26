package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.Cast.ApplicationConnectionResult;
import com.google.android.gms.cast.Cast.Listener;
import com.google.android.gms.cast.Cast.MessageReceivedCallback;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.a.d;
import com.google.android.gms.internal.ff.e;
import com.millennialmedia.android.MMAdView;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public final class en extends ff<ep> {
    private static final er zb;
    private static final Object zs;
    private static final Object zt;
    private final Handler mHandler;
    private final Listener xX;
    private double yC;
    private boolean yD;
    private ApplicationMetadata zc;
    private final CastDevice zd;
    private final eq ze;
    private final Map<String, MessageReceivedCallback> zf;
    private final long zg;
    private String zh;
    private boolean zi;
    private boolean zj;
    private final AtomicLong zk;
    private String zl;
    private String zm;
    private Bundle zn;
    private Map<Long, d<Status>> zo;
    private b zp;
    private d<ApplicationConnectionResult> zq;
    private d<Status> zr;

    private static final class a implements ApplicationConnectionResult {
        private final String qL;
        private final Status wJ;
        private final String zA;
        private final boolean zB;
        private final ApplicationMetadata zz;

        public a(Status status) {
            this(status, null, null, null, false);
        }

        public a(Status status, ApplicationMetadata applicationMetadata, String str, String str2, boolean z) {
            this.wJ = status;
            this.zz = applicationMetadata;
            this.zA = str;
            this.qL = str2;
            this.zB = z;
        }

        public ApplicationMetadata getApplicationMetadata() {
            return this.zz;
        }

        public String getApplicationStatus() {
            return this.zA;
        }

        public String getSessionId() {
            return this.qL;
        }

        public Status getStatus() {
            return this.wJ;
        }

        public boolean getWasLaunched() {
            return this.zB;
        }
    }

    private class b implements OnConnectionFailedListener {
        private b() {
        }

        public void onConnectionFailed(ConnectionResult result) {
            en.this.dJ();
        }
    }

    static {
        zb = new er("CastClientImpl");
        zs = new Object();
        zt = new Object();
    }

    public en(Context context, Looper looper, CastDevice castDevice, long j, Listener listener, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, connectionCallbacks, onConnectionFailedListener, (String[]) 0);
        this.zd = castDevice;
        this.xX = listener;
        this.zg = j;
        this.mHandler = new Handler(looper);
        this.zf = new HashMap();
        this.zj = false;
        this.zc = null;
        this.zh = null;
        this.yC = 0.0d;
        this.yD = false;
        this.zk = new AtomicLong(0);
        this.zo = new HashMap();
        this.zp = new b(null);
        registerConnectionFailedListener(this.zp);
        this.ze = new com.google.android.gms.internal.eq.a() {

            class AnonymousClass_1 implements Runnable {
                final /* synthetic */ int zv;

                AnonymousClass_1(int i) {
                    this.zv = i;
                }

                public void run() {
                    if (AnonymousClass_1.this.zu.xX != null) {
                        AnonymousClass_1.this.zu.xX.onApplicationDisconnected(this.zv);
                    }
                }
            }

            class AnonymousClass_2 implements Runnable {
                final /* synthetic */ double yQ;
                final /* synthetic */ boolean yR;
                final /* synthetic */ String zx;

                AnonymousClass_2(String str, double d, boolean z) {
                    this.zx = str;
                    this.yQ = d;
                    this.yR = z;
                }

                public void run() {
                    AnonymousClass_1.this.zu.a(this.zx, this.yQ, this.yR);
                }
            }

            class AnonymousClass_3 implements Runnable {
                final /* synthetic */ String xN;
                final /* synthetic */ String zy;

                AnonymousClass_3(String str, String str2) {
                    this.xN = str;
                    this.zy = str2;
                }

                public void run() {
                    synchronized (AnonymousClass_1.this.zu.zf) {
                        MessageReceivedCallback messageReceivedCallback = (MessageReceivedCallback) AnonymousClass_1.this.zu.zf.get(this.xN);
                    }
                    if (messageReceivedCallback != null) {
                        messageReceivedCallback.onMessageReceived(AnonymousClass_1.this.zu.zd, this.xN, this.zy);
                    } else {
                        zb.b("Discarded message for unknown namespace '%s'", new Object[]{this.xN});
                    }
                }
            }

            private boolean D(int i) {
                synchronized (zt) {
                    if (en.this.zr != null) {
                        en.this.zr.b(new Status(i));
                        en.this.zr = null;
                        return true;
                    } else {
                        return false;
                    }
                }
            }

            private void b(long j, int i) {
                synchronized (en.this.zo) {
                    d dVar = (d) en.this.zo.remove(Long.valueOf(j));
                }
                if (dVar != null) {
                    dVar.b(new Status(i));
                }
            }

            public void A(int i) {
                synchronized (zs) {
                    if (en.this.zq != null) {
                        en.this.zq.b(new a(new Status(i)));
                        en.this.zq = null;
                    }
                }
            }

            public void B(int i) {
                D(i);
            }

            public void C(int i) {
                D(i);
            }

            public void a(ApplicationMetadata applicationMetadata, String str, String str2, boolean z) {
                en.this.zc = applicationMetadata;
                en.this.zl = applicationMetadata.getApplicationId();
                en.this.zm = str2;
                synchronized (zs) {
                    if (en.this.zq != null) {
                        en.this.zq.b(new a(new Status(0), applicationMetadata, str, str2, z));
                        en.this.zq = null;
                    }
                }
            }

            public void a(String str, long j) {
                b(j, 0);
            }

            public void a(String str, long j, int i) {
                b(j, i);
            }

            public void b(String str, double d, boolean z) {
                en.this.mHandler.post(new AnonymousClass_2(str, d, z));
            }

            public void b(String str, byte[] bArr) {
                zb.b("IGNORING: Receive (type=binary, ns=%s) <%d bytes>", new Object[]{str, Integer.valueOf(bArr.length)});
            }

            public void d(String str, String str2) {
                zb.b("Receive (type=text, ns=%s) %s", new Object[]{str, str2});
                en.this.mHandler.post(new AnonymousClass_3(str, str2));
            }

            public void onApplicationDisconnected(int statusCode) {
                en.this.zl = null;
                en.this.zm = null;
                if (!D(statusCode) && en.this.xX != null) {
                    en.this.mHandler.post(new AnonymousClass_1(statusCode));
                }
            }

            public void z(int i) {
                zb.b("ICastDeviceControllerListener.onDisconnected: %d", new Object[]{Integer.valueOf(i)});
                en.this.zj = false;
                en.this.zc = null;
                if (i != 0) {
                    en.this.N(MMAdView.TRANSITION_UP);
                }
            }
        };
    }

    private void a(String str, double d, boolean z) {
        boolean z2;
        if (eo.a(str, this.zh)) {
            z2 = false;
        } else {
            this.zh = str;
            z2 = true;
        }
        if (this.xX != null) {
            if (i != 0 || this.zi) {
                this.xX.onApplicationStatusChanged();
            }
        }
        if (d != this.yC) {
            this.yC = d;
            z2 = true;
        } else {
            z2 = false;
        }
        if (z != this.yD) {
            this.yD = z;
            z2 = true;
        }
        zb.b("hasChange=%b, mFirstStatusUpdate=%b", new Object[]{Boolean.valueOf(z2), Boolean.valueOf(this.zi)});
        if (this.xX != null) {
            if (z2 || this.zi) {
                this.xX.onVolumeChanged();
            }
        }
        this.zi = false;
    }

    private void d(d<ApplicationConnectionResult> dVar) {
        synchronized (zs) {
            if (this.zq != null) {
                this.zq.b(new a(new Status(2002)));
            }
            this.zq = dVar;
        }
    }

    private void dJ() {
        zb.b("removing all MessageReceivedCallbacks", new Object[0]);
        synchronized (this.zf) {
            this.zf.clear();
        }
    }

    private void dK() throws IllegalStateException {
        if (!this.zj) {
            throw new IllegalStateException("not connected to a device");
        }
    }

    private void f(d<Status> dVar) {
        synchronized (zt) {
            if (this.zr != null) {
                dVar.b(new Status(2001));
            } else {
                this.zr = dVar;
            }
        }
    }

    public void V(String str) throws IllegalArgumentException, RemoteException {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("Channel namespace cannot be null or empty");
        }
        synchronized (this.zf) {
            MessageReceivedCallback messageReceivedCallback = (MessageReceivedCallback) this.zf.remove(str);
        }
        if (messageReceivedCallback != null) {
            try {
                ((ep) eM()).aa(str);
            } catch (IllegalStateException e) {
                Throwable th = e;
                zb.a(th, "Error unregistering namespace (%s): %s", new Object[]{str, th.getMessage()});
            }
        }
    }

    public void a(double d) throws IllegalArgumentException, IllegalStateException, RemoteException {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            throw new IllegalArgumentException("Volume cannot be " + d);
        }
        ((ep) eM()).a(d, this.yC, this.yD);
    }

    protected void a(int i, IBinder iBinder, Bundle bundle) {
        if (i == 0 || i == 1001) {
            this.zj = true;
            this.zi = true;
        } else {
            this.zj = false;
        }
        if (i == 1001) {
            this.zn = new Bundle();
            this.zn.putBoolean(Cast.EXTRA_APP_NO_LONGER_RUNNING, true);
            i = 0;
        }
        super.a(i, iBinder, bundle);
    }

    protected void a(fm fmVar, e eVar) throws RemoteException {
        Bundle bundle = new Bundle();
        zb.b("getServiceFromBroker(): mLastApplicationId=%s, mLastSessionId=%s", new Object[]{this.zl, this.zm});
        this.zd.putInBundle(bundle);
        bundle.putLong("com.google.android.gms.cast.EXTRA_CAST_FLAGS", this.zg);
        if (this.zl != null) {
            bundle.putString("last_application_id", this.zl);
            if (this.zm != null) {
                bundle.putString("last_session_id", this.zm);
            }
        }
        fmVar.a((fl)eVar, (int)GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, getContext().getPackageName(), this.ze.asBinder(), bundle);
    }

    public void a(String str, MessageReceivedCallback messageReceivedCallback) throws IllegalArgumentException, IllegalStateException, RemoteException {
        eo.W(str);
        V(str);
        if (messageReceivedCallback != null) {
            synchronized (this.zf) {
                this.zf.put(str, messageReceivedCallback);
            }
            ((ep) eM()).Z(str);
        }
    }

    public void a(String str, d dVar) throws IllegalStateException, RemoteException {
        f(dVar);
        ((ep) eM()).Y(str);
    }

    public void a(String str, String str2, d<Status> dVar) throws IllegalArgumentException, IllegalStateException, RemoteException {
        if (TextUtils.isEmpty(str2)) {
            throw new IllegalArgumentException("The message payload cannot be null or empty");
        } else if (str2.length() > 65536) {
            throw new IllegalArgumentException("Message exceeds maximum size");
        } else {
            eo.W(str);
            dK();
            long incrementAndGet = this.zk.incrementAndGet();
            ((ep) eM()).a(str, str2, incrementAndGet);
            this.zo.put(Long.valueOf(incrementAndGet), dVar);
        }
    }

    public void a(String str, boolean z, d dVar) throws IllegalStateException, RemoteException {
        d(dVar);
        ((ep) eM()).e(str, z);
    }

    public void b(String str, String str2, d dVar) throws IllegalStateException, RemoteException {
        d(dVar);
        ((ep) eM()).e(str, str2);
    }

    protected String bg() {
        return "com.google.android.gms.cast.service.BIND_CAST_DEVICE_CONTROLLER_SERVICE";
    }

    protected String bh() {
        return "com.google.android.gms.cast.internal.ICastDeviceController";
    }

    public Bundle dG() {
        if (this.zn == null) {
            return super.dG();
        }
        Bundle bundle = this.zn;
        this.zn = null;
        return bundle;
    }

    public void dH() throws IllegalStateException, RemoteException {
        ((ep) eM()).dH();
    }

    public double dI() throws IllegalStateException {
        dK();
        return this.yC;
    }

    public void disconnect() {
        dJ();
        try {
            if (isConnected()) {
                ((ep) eM()).disconnect();
            }
            super.disconnect();
        } catch (RemoteException e) {
            RemoteException remoteException = e;
            zb.b("Error while disconnecting the controller interface: %s", new Object[]{remoteException.getMessage()});
            super.disconnect();
        }
    }

    public void e(d dVar) throws IllegalStateException, RemoteException {
        f(dVar);
        ((ep) eM()).dO();
    }

    public ApplicationMetadata getApplicationMetadata() throws IllegalStateException {
        dK();
        return this.zc;
    }

    public String getApplicationStatus() throws IllegalStateException {
        dK();
        return this.zh;
    }

    public boolean isMute() throws IllegalStateException {
        dK();
        return this.yD;
    }

    protected /* synthetic */ IInterface r(IBinder iBinder) {
        return x(iBinder);
    }

    public void v(boolean z) throws IllegalStateException, RemoteException {
        ((ep) eM()).a(z, this.yC, this.yD);
    }

    protected ep x(IBinder iBinder) {
        return com.google.android.gms.internal.ep.a.y(iBinder);
    }
}