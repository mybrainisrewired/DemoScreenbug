package com.google.android.gms.tagmanager;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import com.google.android.gms.internal.c.f;
import com.google.android.gms.internal.it.a;
import com.google.android.gms.internal.ks;
import com.google.android.gms.internal.kt;
import com.google.android.gms.tagmanager.cq.c;
import com.google.android.gms.tagmanager.cq.g;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;

class cp implements f {
    private final String WJ;
    private bg<a> Zf;
    private final ExecutorService Zm;
    private final Context mContext;

    class AnonymousClass_2 implements Runnable {
        final /* synthetic */ a Zo;

        AnonymousClass_2(a aVar) {
            this.Zo = aVar;
        }

        public void run() {
            cp.this.c(this.Zo);
        }
    }

    cp(Context context, String str) {
        this.mContext = context;
        this.WJ = str;
        this.Zm = Executors.newSingleThreadExecutor();
    }

    private c a(ByteArrayOutputStream byteArrayOutputStream) {
        c cVar = null;
        try {
            return ba.bG(byteArrayOutputStream.toString("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            bh.v("Tried to convert binary resource to string for JSON parsing; not UTF-8 format");
            return cVar;
        } catch (JSONException e2) {
            bh.z("Resource is a UTF-8 encoded string but doesn't contain a JSON container");
            return cVar;
        }
    }

    private c k(byte[] bArr) {
        c cVar = null;
        try {
            return cq.b(f.a(bArr));
        } catch (ks e) {
            bh.z("Resource doesn't contain a binary container");
            return cVar;
        } catch (g e2) {
            bh.z("Resource doesn't contain a binary container");
            return cVar;
        }
    }

    public void a(bg<a> bgVar) {
        this.Zf = bgVar;
    }

    public void b(a aVar) {
        this.Zm.execute(new AnonymousClass_2(aVar));
    }

    boolean c(a aVar) {
        boolean z = false;
        File lc = lc();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(lc);
            try {
                fileOutputStream.write(kt.d(aVar));
                z = true;
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    bh.z("error closing stream for writing resource to disk");
                }
            } catch (IOException e2) {
                try {
                    bh.z("Error writing resource to disk. Removing resource from disk.");
                    lc.delete();
                    try {
                        fileOutputStream.close();
                    } catch (IOException e3) {
                        bh.z("error closing stream for writing resource to disk");
                    }
                } catch (Throwable th) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e4) {
                        bh.z("error closing stream for writing resource to disk");
                    }
                }
            }
        } catch (FileNotFoundException e5) {
            bh.w("Error opening resource file for writing");
        }
        return z;
    }

    public c ca(int i) {
        c cVar = null;
        bh.y("Atttempting to load container from resource ID " + i);
        try {
            InputStream openRawResource = this.mContext.getResources().openRawResource(i);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            cq.b(openRawResource, byteArrayOutputStream);
            c a = a(byteArrayOutputStream);
            return a != null ? a : k(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            bh.z("Error reading default container resource with ID " + i);
            return cVar;
        } catch (NotFoundException e2) {
            bh.z("No default container resource found.");
            return cVar;
        }
    }

    public void km() {
        this.Zm.execute(new Runnable() {
            public void run() {
                cp.this.lb();
            }
        });
    }

    void lb() {
        if (this.Zf == null) {
            throw new IllegalStateException("callback must be set before execute");
        }
        this.Zf.kl();
        bh.y("Start loading resource from disk ...");
        if ((cd.kT().kU() == a.YU || cd.kT().kU() == a.YV) && this.WJ.equals(cd.kT().getContainerId())) {
            this.Zf.a(bg.a.Yy);
        } else {
            try {
                InputStream fileInputStream = new FileInputStream(lc());
                try {
                    OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    cq.b(fileInputStream, byteArrayOutputStream);
                    this.Zf.i(a.l(byteArrayOutputStream.toByteArray()));
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        bh.z("error closing stream for reading resource from disk");
                    }
                } catch (IOException e2) {
                    try {
                        bh.z("error reading resource from disk");
                        this.Zf.a(bg.a.Yz);
                        try {
                            fileInputStream.close();
                        } catch (IOException e3) {
                            bh.z("error closing stream for reading resource from disk");
                        }
                    } catch (Throwable th) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e4) {
                            bh.z("error closing stream for reading resource from disk");
                        }
                    }
                }
                bh.y("Load resource from disk finished.");
            } catch (FileNotFoundException e5) {
                bh.v("resource not on disk");
                this.Zf.a(bg.a.Yy);
            }
        }
    }

    File lc() {
        return new File(this.mContext.getDir("google_tagmanager", 0), "resource_" + this.WJ);
    }

    public synchronized void release() {
        this.Zm.shutdown();
    }
}