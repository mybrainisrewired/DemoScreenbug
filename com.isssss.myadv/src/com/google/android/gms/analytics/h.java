package com.google.android.gms.analytics;

import android.content.Context;
import android.support.v4.media.TransportMediator;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

class h implements m {
    private static final Object sf;
    private static h st;
    private final Context mContext;
    private String su;
    private boolean sv;
    private final Object sw;

    class AnonymousClass_1 extends Thread {
        AnonymousClass_1(String str) {
            super(str);
        }

        public void run() {
            synchronized (h.this.sw) {
                h.this.su = h.this.cf();
                h.this.sv = true;
                h.this.sw.notifyAll();
            }
        }
    }

    static {
        sf = new Object();
    }

    protected h(Context context) {
        this.sv = false;
        this.sw = new Object();
        this.mContext = context;
        ce();
    }

    private boolean D(String str) {
        boolean z = false;
        try {
            aa.y("Storing clientId.");
            FileOutputStream openFileOutput = this.mContext.openFileOutput("gaClientId", 0);
            openFileOutput.write(str.getBytes());
            openFileOutput.close();
            return true;
        } catch (FileNotFoundException e) {
            aa.w("Error creating clientId file.");
            return z;
        } catch (IOException e2) {
            aa.w("Error writing to clientId file.");
            return z;
        }
    }

    public static h cb() {
        h hVar;
        synchronized (sf) {
            hVar = st;
        }
        return hVar;
    }

    private String cc() {
        if (!this.sv) {
            synchronized (this.sw) {
                if (!this.sv) {
                    aa.y("Waiting for clientId to load");
                    do {
                        try {
                            this.sw.wait();
                        } catch (InterruptedException e) {
                            aa.w("Exception while waiting for clientId: " + e);
                        }
                    } while (!this.sv);
                }
            }
        }
        aa.y("Loaded clientId");
        return this.su;
    }

    private void ce() {
        new AnonymousClass_1("client_id_fetcher").start();
    }

    public static void n(Context context) {
        synchronized (sf) {
            if (st == null) {
                st = new h(context);
            }
        }
    }

    public boolean C(String str) {
        return "&cid".equals(str);
    }

    protected String cd() {
        String toLowerCase = UUID.randomUUID().toString().toLowerCase();
        try {
            return !D(toLowerCase) ? "0" : toLowerCase;
        } catch (Exception e) {
            return null;
        }
    }

    String cf() {
        String str = null;
        try {
            FileInputStream openFileInput = this.mContext.openFileInput("gaClientId");
            byte[] bArr = new byte[128];
            int read = openFileInput.read(bArr, 0, TransportMediator.FLAG_KEY_MEDIA_NEXT);
            if (openFileInput.available() > 0) {
                aa.w("clientId file seems corrupted, deleting it.");
                openFileInput.close();
                this.mContext.deleteFile("gaClientId");
            } else if (read <= 0) {
                aa.w("clientId file seems empty, deleting it.");
                openFileInput.close();
                this.mContext.deleteFile("gaClientId");
            } else {
                String str2 = new String(bArr, 0, read);
                try {
                    openFileInput.close();
                    str = str2;
                } catch (FileNotFoundException e) {
                    str = str2;
                } catch (IOException e2) {
                    str = str2;
                    aa.w("Error reading clientId file, deleting it.");
                    this.mContext.deleteFile("gaClientId");
                }
            }
        } catch (FileNotFoundException e3) {
        } catch (IOException e4) {
            aa.w("Error reading clientId file, deleting it.");
            this.mContext.deleteFile("gaClientId");
        }
        return str == null ? cd() : str;
    }

    public String getValue(String field) {
        return "&cid".equals(field) ? cc() : null;
    }
}