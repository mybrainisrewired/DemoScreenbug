package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.ContentProviderClient;
import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.millennialmedia.android.MMAdView;
import java.util.HashMap;
import java.util.Iterator;

public class hb {
    private final hf<ha> Ok;
    private ContentProviderClient Ol;
    private boolean Om;
    private HashMap<LocationListener, b> On;
    private final Context mContext;

    private static class a extends Handler {
        private final LocationListener Oo;

        public a(LocationListener locationListener) {
            this.Oo = locationListener;
        }

        public a(LocationListener locationListener, Looper looper) {
            super(looper);
            this.Oo = locationListener;
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MMAdView.TRANSITION_FADE:
                    this.Oo.onLocationChanged(new Location((Location) msg.obj));
                default:
                    Log.e("LocationClientHelper", "unknown message in LocationHandler.handleMessage");
            }
        }
    }

    private static class b extends com.google.android.gms.location.a.a {
        private Handler Op;

        b(LocationListener locationListener, Looper looper) {
            this.Op = looper == null ? new a(locationListener) : new a(locationListener, looper);
        }

        public void onLocationChanged(Location location) {
            if (this.Op == null) {
                Log.e("LocationClientHelper", "Received a location in client after calling removeLocationUpdates.");
            } else {
                Message obtain = Message.obtain();
                obtain.what = 1;
                obtain.obj = location;
                this.Op.sendMessage(obtain);
            }
        }

        public void release() {
            this.Op = null;
        }
    }

    public hb(Context context, hf<ha> hfVar) {
        this.Ol = null;
        this.Om = false;
        this.On = new HashMap();
        this.mContext = context;
        this.Ok = hfVar;
    }

    public Location getLastLocation() {
        this.Ok.bT();
        try {
            return ((ha) this.Ok.eM()).aW(this.mContext.getPackageName());
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }

    public void hQ() {
        if (this.Om) {
            setMockMode(false);
        }
    }

    public void removeAllListeners() {
        try {
            synchronized (this.On) {
                Iterator it = this.On.values().iterator();
                while (it.hasNext()) {
                    com.google.android.gms.location.a aVar = (b) it.next();
                    if (aVar != null) {
                        ((ha) this.Ok.eM()).a(aVar);
                    }
                }
                this.On.clear();
            }
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }

    public void removeLocationUpdates(PendingIntent callbackIntent) {
        this.Ok.bT();
        try {
            ((ha) this.Ok.eM()).a(callbackIntent);
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }

    public void removeLocationUpdates(Object listener) {
        this.Ok.bT();
        fq.b(listener, (Object)"Invalid null listener");
        synchronized (this.On) {
            try {
                com.google.android.gms.location.a aVar = (b) this.On.remove(listener);
                if (this.Ol != null && this.On.isEmpty()) {
                    this.Ol.release();
                    this.Ol = null;
                }
                if (aVar != null) {
                    aVar.release();
                    ((ha) this.Ok.eM()).a(aVar);
                }
            } catch (RemoteException e) {
                throw new IllegalStateException(e);
            } catch (Throwable th) {
            }
        }
    }

    public void requestLocationUpdates(LocationRequest request, PendingIntent callbackIntent) {
        this.Ok.bT();
        try {
            ((ha) this.Ok.eM()).a(request, callbackIntent);
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }

    public void requestLocationUpdates(LocationRequest request, LocationListener listener, Looper looper) {
        this.Ok.bT();
        if (looper == null) {
            fq.b(Looper.myLooper(), (Object)"Can't create handler inside thread that has not called Looper.prepare()");
        }
        synchronized (this.On) {
            try {
                b bVar = (b) this.On.get(listener);
                com.google.android.gms.location.a bVar2 = bVar == null ? new b(listener, looper) : bVar;
                this.On.put(listener, bVar2);
                ((ha) this.Ok.eM()).a(request, bVar2, this.mContext.getPackageName());
            } catch (RemoteException e) {
                throw new IllegalStateException(e);
            } catch (Throwable th) {
            }
        }
    }

    public void setMockLocation(Location mockLocation) {
        this.Ok.bT();
        try {
            ((ha) this.Ok.eM()).setMockLocation(mockLocation);
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }

    public void setMockMode(boolean isMockMode) {
        this.Ok.bT();
        try {
            ((ha) this.Ok.eM()).setMockMode(isMockMode);
            this.Om = isMockMode;
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }
}