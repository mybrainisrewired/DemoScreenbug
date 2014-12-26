package com.google.android.gms.tagmanager;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

class cx extends cw {
    private static cx aam;
    private static final Object sF;
    private Context aac;
    private at aad;
    private volatile ar aae;
    private int aaf;
    private boolean aag;
    private boolean aah;
    private boolean aai;
    private au aaj;
    private bn aak;
    private boolean aal;
    private boolean connected;
    private Handler handler;

    static {
        sF = new Object();
    }

    private cx() {
        this.aaf = 1800000;
        this.aag = true;
        this.aah = false;
        this.connected = true;
        this.aai = true;
        this.aaj = new au() {
            public void r(boolean z) {
                cx.this.a(z, cx.this.connected);
            }
        };
        this.aal = false;
    }

    private void cj() {
        this.aak = new bn(this);
        this.aak.o(this.aac);
    }

    private void ck() {
        this.handler = new Handler(this.aac.getMainLooper(), new Callback() {
            public boolean handleMessage(Message msg) {
                if (1 == msg.what && sF.equals(msg.obj)) {
                    cx.this.bW();
                    if (cx.this.aaf > 0 && !cx.this.aal) {
                        cx.this.handler.sendMessageDelayed(cx.this.handler.obtainMessage(1, sF), (long) cx.this.aaf);
                    }
                }
                return true;
            }
        });
        if (this.aaf > 0) {
            this.handler.sendMessageDelayed(this.handler.obtainMessage(1, sF), (long) this.aaf);
        }
    }

    public static cx lG() {
        if (aam == null) {
            aam = new cx();
        }
        return aam;
    }

    synchronized void a(Context context, ar arVar) {
        if (this.aac == null) {
            this.aac = context.getApplicationContext();
            if (this.aae == null) {
                this.aae = arVar;
            }
        }
    }

    synchronized void a(boolean z, boolean z2) {
        if (!(this.aal == z && this.connected == z2)) {
            if (z || !z2) {
                if (this.aaf > 0) {
                    this.handler.removeMessages(1, sF);
                }
            }
            if (!z && z2 && this.aaf > 0) {
                this.handler.sendMessageDelayed(this.handler.obtainMessage(1, sF), (long) this.aaf);
            }
            StringBuilder append = new StringBuilder().append("PowerSaveMode ");
            String str = (z || !z2) ? "initiated." : "terminated.";
            bh.y(append.append(str).toString());
            this.aal = z;
            this.connected = z2;
        }
    }

    public synchronized void bW() {
        if (this.aah) {
            this.aae.a(new Runnable() {
                public void run() {
                    cx.this.aad.bW();
                }
            });
        } else {
            bh.y("Dispatch call queued. Dispatch will run once initialization is complete.");
            this.aag = true;
        }
    }

    synchronized void cm() {
        if (!this.aal && this.connected && this.aaf > 0) {
            this.handler.removeMessages(1, sF);
            this.handler.sendMessage(this.handler.obtainMessage(1, sF));
        }
    }

    synchronized at lH() {
        if (this.aad == null && this.aac == null) {
            throw new IllegalStateException("Cant get a store unless we have a context");
        }
        this.aad = new ca(this.aaj, this.aac);
        if (this.handler == null) {
            ck();
        }
        this.aah = true;
        if (this.aag) {
            bW();
            this.aag = false;
        }
        if (this.aak == null && this.aai) {
            cj();
        }
        return this.aad;
    }

    synchronized void s(boolean z) {
        a(this.aal, z);
    }
}