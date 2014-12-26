package com.google.android.gms.common.images;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.ImageView;
import com.google.android.gms.internal.fa;
import com.google.android.gms.internal.fb;
import com.google.android.gms.internal.fu;
import com.google.android.gms.internal.gr;
import com.millennialmedia.android.MMAdView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ImageManager {
    private static final Object BY;
    private static HashSet<Uri> BZ;
    private static ImageManager Ca;
    private static ImageManager Cb;
    private final ExecutorService Cc;
    private final b Cd;
    private final fa Ce;
    private final Map<a, ImageReceiver> Cf;
    private final Map<Uri, ImageReceiver> Cg;
    private final Context mContext;
    private final Handler mHandler;

    private final class ImageReceiver extends ResultReceiver {
        private final ArrayList<a> Ch;
        private final Uri mUri;

        ImageReceiver(Uri uri) {
            super(new Handler(Looper.getMainLooper()));
            this.mUri = uri;
            this.Ch = new ArrayList();
        }

        public void b(a aVar) {
            fb.aj("ImageReceiver.addImageRequest() must be called in the main thread");
            this.Ch.add(aVar);
        }

        public void c(a aVar) {
            fb.aj("ImageReceiver.removeImageRequest() must be called in the main thread");
            this.Ch.remove(aVar);
        }

        public void ey() {
            Intent intent = new Intent("com.google.android.gms.common.images.LOAD_IMAGE");
            intent.putExtra("com.google.android.gms.extras.uri", this.mUri);
            intent.putExtra("com.google.android.gms.extras.resultReceiver", this);
            intent.putExtra("com.google.android.gms.extras.priority", MMAdView.TRANSITION_DOWN);
            ImageManager.this.mContext.sendBroadcast(intent);
        }

        public void onReceiveResult(int resultCode, Bundle resultData) {
            ImageManager.this.Cc.execute(new c(this.mUri, (ParcelFileDescriptor) resultData.getParcelable("com.google.android.gms.extra.fileDescriptor")));
        }
    }

    public static interface OnImageLoadedListener {
        void onImageLoaded(Uri uri, Drawable drawable, boolean z);
    }

    private static final class a {
        static int a(ActivityManager activityManager) {
            return activityManager.getLargeMemoryClass();
        }
    }

    private final class c implements Runnable {
        private final ParcelFileDescriptor Cj;
        private final Uri mUri;

        public c(Uri uri, ParcelFileDescriptor parcelFileDescriptor) {
            this.mUri = uri;
            this.Cj = parcelFileDescriptor;
        }

        public void run() {
            fb.ak("LoadBitmapFromDiskRunnable can't be executed in the main thread");
            boolean z = false;
            Bitmap bitmap = null;
            if (this.Cj != null) {
                try {
                    bitmap = BitmapFactory.decodeFileDescriptor(this.Cj.getFileDescriptor());
                } catch (OutOfMemoryError e) {
                    Log.e("ImageManager", "OOM while loading bitmap for uri: " + this.mUri, e);
                    z = true;
                }
                try {
                    this.Cj.close();
                } catch (IOException e2) {
                    Log.e("ImageManager", "closed failed", e2);
                }
            }
            CountDownLatch countDownLatch = new CountDownLatch(1);
            ImageManager.this.mHandler.post(new f(this.mUri, bitmap, z, countDownLatch));
            try {
                countDownLatch.await();
            } catch (InterruptedException e3) {
                Log.w("ImageManager", "Latch interrupted while posting " + this.mUri);
            }
        }
    }

    private final class d implements Runnable {
        private final a Ck;

        public d(a aVar) {
            this.Ck = aVar;
        }

        public void run() {
            fb.aj("LoadImageRunnable must be executed on the main thread");
            ImageReceiver imageReceiver = (ImageReceiver) ImageManager.this.Cf.get(this.Ck);
            if (imageReceiver != null) {
                ImageManager.this.Cf.remove(this.Ck);
                imageReceiver.c(this.Ck);
            }
            a aVar = this.Ck.Cm;
            if (aVar.uri == null) {
                this.Ck.a(ImageManager.this.mContext, ImageManager.this.Ce, true);
            } else {
                Bitmap a = ImageManager.this.a(aVar);
                if (a != null) {
                    this.Ck.a(ImageManager.this.mContext, a, true);
                } else {
                    this.Ck.a(ImageManager.this.mContext, ImageManager.this.Ce);
                    imageReceiver = (ImageReceiver) ImageManager.this.Cg.get(aVar.uri);
                    if (imageReceiver == null) {
                        imageReceiver = new ImageReceiver(aVar.uri);
                        ImageManager.this.Cg.put(aVar.uri, imageReceiver);
                    }
                    imageReceiver.b(this.Ck);
                    if (!this.Ck instanceof com.google.android.gms.common.images.a.c) {
                        ImageManager.this.Cf.put(this.Ck, imageReceiver);
                    }
                    synchronized (BY) {
                        if (!BZ.contains(aVar.uri)) {
                            BZ.add(aVar.uri);
                            imageReceiver.ey();
                        }
                    }
                }
            }
        }
    }

    private static final class e implements ComponentCallbacks2 {
        private final b Cd;

        public e(b bVar) {
            this.Cd = bVar;
        }

        public void onConfigurationChanged(Configuration newConfig) {
        }

        public void onLowMemory() {
            this.Cd.evictAll();
        }

        public void onTrimMemory(int level) {
            if (level >= 60) {
                this.Cd.evictAll();
            } else if (level >= 20) {
                this.Cd.trimToSize(this.Cd.size() / 2);
            }
        }
    }

    private final class f implements Runnable {
        private final CountDownLatch AD;
        private boolean Cl;
        private final Bitmap mBitmap;
        private final Uri mUri;

        public f(Uri uri, Bitmap bitmap, boolean z, CountDownLatch countDownLatch) {
            this.mUri = uri;
            this.mBitmap = bitmap;
            this.Cl = z;
            this.AD = countDownLatch;
        }

        private void a(ImageReceiver imageReceiver, boolean z) {
            ArrayList a = imageReceiver.Ch;
            int size = a.size();
            int i = 0;
            while (i < size) {
                a aVar = (a) a.get(i);
                if (z) {
                    aVar.a(ImageManager.this.mContext, this.mBitmap, false);
                } else {
                    aVar.a(ImageManager.this.mContext, ImageManager.this.Ce, false);
                }
                if (!aVar instanceof com.google.android.gms.common.images.a.c) {
                    ImageManager.this.Cf.remove(aVar);
                }
                i++;
            }
        }

        public void run() {
            fb.aj("OnBitmapLoadedRunnable must be executed in the main thread");
            boolean z = this.mBitmap != null;
            if (ImageManager.this.Cd != null && this.Cl) {
                ImageManager.this.Cd.evictAll();
                System.gc();
                this.Cl = false;
                ImageManager.this.mHandler.post(this);
                return;
            } else if (z) {
                ImageManager.this.Cd.put(new a(this.mUri), this.mBitmap);
            }
            ImageReceiver imageReceiver = (ImageReceiver) ImageManager.this.Cg.remove(this.mUri);
            if (imageReceiver != null) {
                a(imageReceiver, z);
            }
            this.AD.countDown();
            synchronized (BY) {
                BZ.remove(this.mUri);
            }
        }
    }

    private static final class b extends fu<a, Bitmap> {
        public b(Context context) {
            super(w(context));
        }

        private static int w(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            int memoryClass = (((context.getApplicationInfo().flags & 1048576) != 0 ? 1 : 0) == 0 || !gr.fu()) ? activityManager.getMemoryClass() : a.a(activityManager);
            return (int) (((float) (memoryClass * 1048576)) * 0.33f);
        }

        protected int a(a aVar, Bitmap bitmap) {
            return bitmap.getHeight() * bitmap.getRowBytes();
        }

        protected void a(boolean z, a aVar, Bitmap bitmap, Bitmap bitmap2) {
            super.entryRemoved(z, aVar, bitmap, bitmap2);
        }

        protected /* synthetic */ void entryRemoved(boolean x0, Object x1, Object x2, Object x3) {
            a(x0, (a) x1, (Bitmap) x2, (Bitmap) x3);
        }

        protected /* synthetic */ int sizeOf(Object x0, Object x1) {
            return a((a) x0, (Bitmap) x1);
        }
    }

    static {
        BY = new Object();
        BZ = new HashSet();
    }

    private ImageManager(Context context, boolean withMemoryCache) {
        this.mContext = context.getApplicationContext();
        this.mHandler = new Handler(Looper.getMainLooper());
        this.Cc = Executors.newFixedThreadPool(MMAdView.TRANSITION_RANDOM);
        if (withMemoryCache) {
            this.Cd = new b(this.mContext);
            if (gr.fx()) {
                ev();
            }
        } else {
            this.Cd = null;
        }
        this.Ce = new fa();
        this.Cf = new HashMap();
        this.Cg = new HashMap();
    }

    private Bitmap a(a aVar) {
        return this.Cd == null ? null : (Bitmap) this.Cd.get(aVar);
    }

    public static ImageManager a(Context context, boolean z) {
        if (z) {
            if (Cb == null) {
                Cb = new ImageManager(context, true);
            }
            return Cb;
        } else {
            if (Ca == null) {
                Ca = new ImageManager(context, false);
            }
            return Ca;
        }
    }

    public static ImageManager create(Context context) {
        return a(context, false);
    }

    private void ev() {
        this.mContext.registerComponentCallbacks(new e(this.Cd));
    }

    public void a(a aVar) {
        fb.aj("ImageManager.loadImage() must be called in the main thread");
        new d(aVar).run();
    }

    public void loadImage(ImageView imageView, int resId) {
        a(new com.google.android.gms.common.images.a.b(imageView, resId));
    }

    public void loadImage(ImageView imageView, Uri uri) {
        a(new com.google.android.gms.common.images.a.b(imageView, uri));
    }

    public void loadImage(ImageView imageView, Uri uri, int defaultResId) {
        a bVar = new com.google.android.gms.common.images.a.b(imageView, uri);
        bVar.J(defaultResId);
        a(bVar);
    }

    public void loadImage(OnImageLoadedListener listener, Uri uri) {
        a(new com.google.android.gms.common.images.a.c(listener, uri));
    }

    public void loadImage(OnImageLoadedListener listener, Uri uri, int defaultResId) {
        a cVar = new com.google.android.gms.common.images.a.c(listener, uri);
        cVar.J(defaultResId);
        a(cVar);
    }
}