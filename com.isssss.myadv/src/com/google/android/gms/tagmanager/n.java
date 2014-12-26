package com.google.android.gms.tagmanager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tagmanager.ContainerHolder.ContainerAvailableListener;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;

class n implements ContainerHolder {
    private final Looper AS;
    private Container WR;
    private Container WS;
    private b WT;
    private a WU;
    private boolean WV;
    private TagManager WW;
    private Status wJ;

    public static interface a {
        void br(String str);

        String ke();

        void kg();
    }

    private class b extends Handler {
        private final ContainerAvailableListener WX;

        public b(ContainerAvailableListener containerAvailableListener, Looper looper) {
            super(looper);
            this.WX = containerAvailableListener;
        }

        public void bs(String str) {
            sendMessage(obtainMessage(1, str));
        }

        protected void bt(String str) {
            this.WX.onContainerAvailable(n.this, str);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MMAdView.TRANSITION_FADE:
                    bt((String) msg.obj);
                default:
                    bh.w("Don't know how to handle this message.");
            }
        }
    }

    public n(Status status) {
        this.wJ = status;
        this.AS = null;
    }

    public n(TagManager tagManager, Looper looper, Container container, a aVar) {
        this.WW = tagManager;
        if (looper == null) {
            looper = Looper.getMainLooper();
        }
        this.AS = looper;
        this.WR = container;
        this.WU = aVar;
        this.wJ = Status.Bv;
        tagManager.a(this);
    }

    private void kf() {
        if (this.WT != null) {
            this.WT.bs(this.WS.kc());
        }
    }

    public synchronized void a(Container container) {
        if (!this.WV) {
            if (container == null) {
                bh.w("Unexpected null container.");
            } else {
                this.WS = container;
                kf();
            }
        }
    }

    public synchronized void bp(String str) {
        if (!this.WV) {
            this.WR.bp(str);
        }
    }

    void br(String str) {
        if (this.WV) {
            bh.w("setCtfeUrlPathAndQuery called on a released ContainerHolder.");
        } else {
            this.WU.br(str);
        }
    }

    public synchronized Container getContainer() {
        Container container = null;
        synchronized (this) {
            if (this.WV) {
                bh.w("ContainerHolder is released.");
            } else {
                if (this.WS != null) {
                    this.WR = this.WS;
                    this.WS = null;
                }
                container = this.WR;
            }
        }
        return container;
    }

    String getContainerId() {
        if (!this.WV) {
            return this.WR.getContainerId();
        }
        bh.w("getContainerId called on a released ContainerHolder.");
        return Preconditions.EMPTY_ARGUMENTS;
    }

    public Status getStatus() {
        return this.wJ;
    }

    String ke() {
        if (!this.WV) {
            return this.WU.ke();
        }
        bh.w("setCtfeUrlPathAndQuery called on a released ContainerHolder.");
        return Preconditions.EMPTY_ARGUMENTS;
    }

    public synchronized void refresh() {
        if (this.WV) {
            bh.w("Refreshing a released ContainerHolder.");
        } else {
            this.WU.kg();
        }
    }

    public synchronized void release() {
        if (this.WV) {
            bh.w("Releasing a released ContainerHolder.");
        } else {
            this.WV = true;
            this.WW.b(this);
            this.WR.release();
            this.WR = null;
            this.WS = null;
            this.WU = null;
            this.WT = null;
        }
    }

    public synchronized void setContainerAvailableListener(ContainerAvailableListener listener) {
        if (this.WV) {
            bh.w("ContainerHolder is released.");
        } else if (listener == null) {
            this.WT = null;
        } else {
            this.WT = new b(listener, this.AS);
            if (this.WS != null) {
                kf();
            }
        }
    }
}