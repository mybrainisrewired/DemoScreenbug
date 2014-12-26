package com.google.android.gms.internal;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public abstract class j extends i {
    private static Method jR;
    private static Method jS;
    private static Method jT;
    private static Method jU;
    private static Method jV;
    private static Method jW;
    private static String jX;
    private static p jY;
    static boolean jZ;
    private static long startTime;

    static class a extends Exception {
        public a(Throwable th) {
            super(th);
        }
    }

    static {
        startTime = 0;
        jZ = false;
    }

    protected j(Context context, n nVar, o oVar) {
        super(context, nVar, oVar);
    }

    static String a(Context context, n nVar) throws a {
        if (jT == null) {
            throw new a();
        }
        try {
            ByteBuffer byteBuffer = (ByteBuffer) jT.invoke(null, new Object[]{context});
            if (byteBuffer != null) {
                return nVar.a(byteBuffer.array(), true);
            }
            throw new a();
        } catch (IllegalAccessException e) {
            throw new a(e);
        } catch (InvocationTargetException e2) {
            throw new a(e2);
        }
    }

    static ArrayList<Long> a(MotionEvent motionEvent, DisplayMetrics displayMetrics) throws a {
        if (jU == null || motionEvent == null) {
            throw new a();
        }
        try {
            return (ArrayList) jU.invoke(null, new Object[]{motionEvent, displayMetrics});
        } catch (IllegalAccessException e) {
            throw new a(e);
        } catch (InvocationTargetException e2) {
            throw new a(e2);
        }
    }

    protected static synchronized void a(String str, Context context, n nVar) {
        synchronized (j.class) {
            if (!jZ) {
                try {
                    jY = new p(nVar, null);
                    jX = str;
                    e(context);
                    startTime = w().longValue();
                    jZ = true;
                } catch (a e) {
                } catch (UnsupportedOperationException e2) {
                }
            }
        }
    }

    static String b(Context context, n nVar) throws a {
        if (jW == null) {
            throw new a();
        }
        try {
            ByteBuffer byteBuffer = (ByteBuffer) jW.invoke(null, new Object[]{context});
            if (byteBuffer != null) {
                return nVar.a(byteBuffer.array(), true);
            }
            throw new a();
        } catch (IllegalAccessException e) {
            throw new a(e);
        } catch (InvocationTargetException e2) {
            throw new a(e2);
        }
    }

    private static String b(byte[] bArr, String str) throws a {
        try {
            return new String(jY.c(bArr, str), "UTF-8");
        } catch (com.google.android.gms.internal.p.a e) {
            throw new a(e);
        } catch (UnsupportedEncodingException e2) {
            throw new a(e2);
        }
    }

    static String d(Context context) throws a {
        if (jV == null) {
            throw new a();
        }
        try {
            String str = (String) jV.invoke(null, new Object[]{context});
            if (str != null) {
                return str;
            }
            throw new a();
        } catch (IllegalAccessException e) {
            throw new a(e);
        } catch (InvocationTargetException e2) {
            throw new a(e2);
        }
    }

    private static void e(Context context) throws a {
        try {
            byte[] b = jY.b(r.getKey());
            byte[] c = jY.c(b, r.A());
            File cacheDir = context.getCacheDir();
            if (cacheDir == null) {
                cacheDir = context.getDir("dex", 0);
                if (cacheDir == null) {
                    throw new a();
                }
            }
            File createTempFile = File.createTempFile("ads", ".jar", cacheDir);
            FileOutputStream fileOutputStream = new FileOutputStream(createTempFile);
            fileOutputStream.write(c, 0, c.length);
            fileOutputStream.close();
            DexClassLoader dexClassLoader = new DexClassLoader(createTempFile.getAbsolutePath(), cacheDir.getAbsolutePath(), null, context.getClassLoader());
            Class loadClass = dexClassLoader.loadClass(b(b, r.B()));
            Class loadClass2 = dexClassLoader.loadClass(b(b, r.H()));
            Class loadClass3 = dexClassLoader.loadClass(b(b, r.F()));
            Class loadClass4 = dexClassLoader.loadClass(b(b, r.L()));
            Class loadClass5 = dexClassLoader.loadClass(b(b, r.D()));
            Class loadClass6 = dexClassLoader.loadClass(b(b, r.J()));
            jR = loadClass.getMethod(b(b, r.C()), new Class[0]);
            jS = loadClass2.getMethod(b(b, r.I()), new Class[0]);
            jT = loadClass3.getMethod(b(b, r.G()), new Class[]{Context.class});
            jU = loadClass4.getMethod(b(b, r.M()), new Class[]{MotionEvent.class, DisplayMetrics.class});
            jV = loadClass5.getMethod(b(b, r.E()), new Class[]{Context.class});
            jW = loadClass6.getMethod(b(b, r.K()), new Class[]{Context.class});
            String name = createTempFile.getName();
            createTempFile.delete();
            new File(cacheDir, name.replace(".jar", ".dex")).delete();
        } catch (FileNotFoundException e) {
            throw new a(e);
        } catch (IOException e2) {
            throw new a(e2);
        } catch (ClassNotFoundException e3) {
            throw new a(e3);
        } catch (com.google.android.gms.internal.p.a e4) {
            throw new a(e4);
        } catch (NoSuchMethodException e5) {
            throw new a(e5);
        } catch (NullPointerException e6) {
            throw new a(e6);
        }
    }

    static String v() throws a {
        if (jX != null) {
            return jX;
        }
        throw new a();
    }

    static Long w() throws a {
        if (jR == null) {
            throw new a();
        }
        try {
            return (Long) jR.invoke(null, new Object[0]);
        } catch (IllegalAccessException e) {
            throw new a(e);
        } catch (InvocationTargetException e2) {
            throw new a(e2);
        }
    }

    static String x() throws a {
        if (jS == null) {
            throw new a();
        }
        try {
            return (String) jS.invoke(null, new Object[0]);
        } catch (IllegalAccessException e) {
            throw new a(e);
        } catch (InvocationTargetException e2) {
            throw new a(e2);
        }
    }

    protected void b(Context context) {
        try {
            a(1, x());
        } catch (a e) {
        } catch (IOException e2) {
        }
        try {
            a(MMAdView.TRANSITION_UP, v());
        } catch (a e3) {
        } catch (IOException e22) {
        }
        try {
            a(ApiEventType.API_MRAID_GET_GALLERY_IMAGE, w().longValue());
        } catch (a e4) {
        } catch (IOException e222) {
        }
        try {
            a(ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE, d(context));
        } catch (a e5) {
        } catch (IOException e2222) {
        }
    }

    protected void c(Context context) {
        try {
            a(MMAdView.TRANSITION_UP, v());
        } catch (a e) {
        } catch (IOException e2) {
        }
        try {
            a(1, x());
        } catch (a e3) {
        } catch (IOException e22) {
        }
        try {
            long longValue = w().longValue();
            a(ApiEventType.API_MRAID_GET_GALLERY_IMAGE, longValue);
            if (startTime != 0) {
                a(ApiEventType.API_MRAID_GET_SCREEN_SIZE, longValue - startTime);
                a(ApiEventType.API_MRAID_STORE_PICTURE, startTime);
            }
        } catch (a e4) {
        } catch (IOException e222) {
        }
        try {
            ArrayList a = a(this.jN, this.jO);
            a(ApiEventType.API_MRAID_IS_VIEWABLE, ((Long) a.get(0)).longValue());
            a(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE, ((Long) a.get(1)).longValue());
            if (a.size() >= 3) {
                a(ApiEventType.API_MRAID_GET_ORIENTATION, ((Long) a.get(MMAdView.TRANSITION_UP)).longValue());
            }
        } catch (a e5) {
        } catch (IOException e2222) {
        }
        try {
            a(ApiEventType.API_MRAID_SEND_SMS, a(context, this.jP));
        } catch (a e6) {
        } catch (IOException e22222) {
        }
        try {
            a(ApiEventType.API_MRAID_MAKE_CALL, b(context, this.jP));
        } catch (a e7) {
        } catch (IOException e222222) {
        }
    }
}