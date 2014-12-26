package com.google.android.gms.common.images;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import com.google.android.gms.common.images.ImageManager.OnImageLoadedListener;
import com.google.android.gms.internal.ex;
import com.google.android.gms.internal.ey;
import com.google.android.gms.internal.ez;
import com.google.android.gms.internal.fa;
import com.google.android.gms.internal.fb;
import com.google.android.gms.internal.fo;
import java.lang.ref.WeakReference;

public abstract class a {
    final a Cm;
    protected int Cn;
    protected int Co;
    private boolean Cp;
    private boolean Cq;
    protected int Cr;

    static final class a {
        public final Uri uri;

        public a(Uri uri) {
            this.uri = uri;
        }

        public boolean equals(a obj) {
            if (obj instanceof a) {
                return this == obj ? true : fo.equal(obj.uri, this.uri);
            } else {
                return false;
            }
        }

        public int hashCode() {
            return fo.hashCode(new Object[]{this.uri});
        }
    }

    public static final class b extends a {
        private WeakReference<ImageView> Cs;

        public b(ImageView imageView, int i) {
            super(null, i);
            fb.d(imageView);
            this.Cs = new WeakReference(imageView);
        }

        public b(ImageView imageView, Uri uri) {
            super(uri, 0);
            fb.d(imageView);
            this.Cs = new WeakReference(imageView);
        }

        private void a(ImageView imageView, Drawable drawable, boolean z, boolean z2, boolean z3) {
            if (z2 || z3) {
                boolean z4 = false;
            } else {
                int i = 1;
            }
            if (i != 0 && imageView instanceof ez) {
                int eB = ((ez) imageView).eB();
                if (this.Co != 0 && eB == this.Co) {
                    return;
                }
            }
            boolean b = b(z, z2);
            Drawable a = b ? a(imageView.getDrawable(), drawable) : drawable;
            imageView.setImageDrawable(a);
            if (imageView instanceof ez) {
                ez ezVar = (ez) imageView;
                ezVar.e(z3 ? this.Cm.uri : null);
                ezVar.L(i != 0 ? this.Co : 0);
            }
            if (b) {
                ((ex) a).startTransition(250);
            }
        }

        protected void a(Drawable drawable, boolean z, boolean z2, boolean z3) {
            ImageView imageView = (ImageView) this.Cs.get();
            if (imageView != null) {
                a(imageView, drawable, z, z2, z3);
            }
        }

        public boolean equals(com.google.android.gms.common.images.a.b obj) {
            if (!(obj instanceof com.google.android.gms.common.images.a.b)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            ImageView imageView = (ImageView) this.Cs.get();
            ImageView imageView2 = (ImageView) obj.Cs.get();
            boolean z = (imageView2 == null || imageView == null || !fo.equal(imageView2, imageView)) ? false : true;
            return z;
        }

        public int hashCode() {
            return 0;
        }
    }

    public static final class c extends a {
        private WeakReference<OnImageLoadedListener> Ct;

        public c(OnImageLoadedListener onImageLoadedListener, Uri uri) {
            super(uri, 0);
            fb.d(onImageLoadedListener);
            this.Ct = new WeakReference(onImageLoadedListener);
        }

        protected void a(Drawable drawable, boolean z, boolean z2, boolean z3) {
            if (!z2) {
                OnImageLoadedListener onImageLoadedListener = (OnImageLoadedListener) this.Ct.get();
                if (onImageLoadedListener != null) {
                    onImageLoadedListener.onImageLoaded(this.Cm.uri, drawable, z3);
                }
            }
        }

        public boolean equals(com.google.android.gms.common.images.a.c obj) {
            if (!(obj instanceof com.google.android.gms.common.images.a.c)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            obj = obj;
            OnImageLoadedListener onImageLoadedListener = (OnImageLoadedListener) this.Ct.get();
            OnImageLoadedListener onImageLoadedListener2 = (OnImageLoadedListener) obj.Ct.get();
            boolean z = onImageLoadedListener2 != null && onImageLoadedListener != null && fo.equal(onImageLoadedListener2, onImageLoadedListener) && fo.equal(obj.Cm, this.Cm);
            return z;
        }

        public int hashCode() {
            return fo.hashCode(new Object[]{this.Cm});
        }
    }

    public a(Uri uri, int i) {
        this.Cn = 0;
        this.Co = 0;
        this.Cp = true;
        this.Cq = false;
        this.Cm = new a(uri);
        this.Co = i;
    }

    private Drawable a(Context context, fa faVar, int i) {
        Resources resources = context.getResources();
        if (this.Cr <= 0) {
            return resources.getDrawable(i);
        }
        com.google.android.gms.internal.fa.a aVar = new com.google.android.gms.internal.fa.a(i, this.Cr);
        Drawable drawable = (Drawable) faVar.get(aVar);
        if (drawable != null) {
            return drawable;
        }
        drawable = resources.getDrawable(i);
        if ((this.Cr & 1) != 0) {
            drawable = a(resources, drawable);
        }
        faVar.put(aVar, drawable);
        return drawable;
    }

    public void J(int i) {
        this.Co = i;
    }

    protected Drawable a(Resources resources, Drawable drawable) {
        return ey.a(resources, drawable);
    }

    protected ex a(Drawable drawable, Drawable drawable2) {
        if (drawable == null) {
            drawable = null;
        } else if (drawable instanceof ex) {
            drawable = ((ex) drawable).ez();
        }
        return new ex(drawable, drawable2);
    }

    void a(Context context, Bitmap bitmap, boolean z) {
        fb.d(bitmap);
        if ((this.Cr & 1) != 0) {
            bitmap = ey.a(bitmap);
        }
        a(new BitmapDrawable(context.getResources(), bitmap), z, false, true);
    }

    void a(Context context, fa faVar) {
        Drawable drawable = null;
        if (this.Cn != 0) {
            drawable = a(context, faVar, this.Cn);
        }
        a(drawable, false, true, false);
    }

    void a(Context context, fa faVar, boolean z) {
        Drawable drawable = null;
        if (this.Co != 0) {
            drawable = a(context, faVar, this.Co);
        }
        a(drawable, z, false, false);
    }

    protected abstract void a(Drawable drawable, boolean z, boolean z2, boolean z3);

    protected boolean b(boolean z, boolean z2) {
        return this.Cp && !z2 && (!z || this.Cq);
    }
}