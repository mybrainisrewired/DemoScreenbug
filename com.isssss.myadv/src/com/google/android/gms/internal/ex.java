package com.google.android.gms.internal;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.SystemClock;
import com.android.volley.DefaultRetryPolicy;
import com.inmobi.monetization.internal.InvalidManifestConfigException;
import com.millennialmedia.android.MMAdView;

public final class ex extends Drawable implements Callback {
    private int CA;
    private int CB;
    private boolean CC;
    private b CD;
    private Drawable CE;
    private Drawable CF;
    private boolean CG;
    private boolean CH;
    private boolean CI;
    private int CJ;
    private boolean Cp;
    private int Cv;
    private long Cw;
    private int Cx;
    private int Cy;
    private int Cz;

    private static final class a extends Drawable {
        private static final a CK;
        private static final a CL;

        private static final class a extends ConstantState {
            private a() {
            }

            public int getChangingConfigurations() {
                return 0;
            }

            public Drawable newDrawable() {
                return CK;
            }
        }

        static {
            CK = new a();
            CL = new a();
        }

        private a() {
        }

        public void draw(Canvas canvas) {
        }

        public ConstantState getConstantState() {
            return CL;
        }

        public int getOpacity() {
            return InvalidManifestConfigException.MISSING_ACTIVITY_DECLARATION;
        }

        public void setAlpha(int alpha) {
        }

        public void setColorFilter(ColorFilter cf) {
        }
    }

    static final class b extends ConstantState {
        int CM;
        int CN;

        b(b bVar) {
            if (bVar != null) {
                this.CM = bVar.CM;
                this.CN = bVar.CN;
            }
        }

        public int getChangingConfigurations() {
            return this.CM;
        }

        public Drawable newDrawable() {
            return new ex(this);
        }
    }

    public ex(Drawable drawable, Drawable drawable2) {
        this(null);
        if (drawable == null) {
            drawable = a.CK;
        }
        this.CE = drawable;
        drawable.setCallback(this);
        b bVar = this.CD;
        bVar.CN |= drawable.getChangingConfigurations();
        if (drawable2 == null) {
            drawable2 = a.CK;
        }
        this.CF = drawable2;
        drawable2.setCallback(this);
        bVar = this.CD;
        bVar.CN |= drawable2.getChangingConfigurations();
    }

    ex(b bVar) {
        this.Cv = 0;
        this.Cz = 255;
        this.CB = 0;
        this.Cp = true;
        this.CD = new b(bVar);
    }

    public boolean canConstantState() {
        if (!this.CG) {
            boolean z = (this.CE.getConstantState() == null || this.CF.getConstantState() == null) ? false : true;
            this.CH = z;
            this.CG = true;
        }
        return this.CH;
    }

    public void draw(Canvas canvas) {
        boolean z = 1;
        int i = 0;
        switch (this.Cv) {
            case MMAdView.TRANSITION_FADE:
                this.Cw = SystemClock.uptimeMillis();
                this.Cv = 2;
                break;
            case MMAdView.TRANSITION_UP:
                if (this.Cw >= 0) {
                    float uptimeMillis = ((float) (SystemClock.uptimeMillis() - this.Cw)) / ((float) this.CA);
                    if (uptimeMillis < 1.0f) {
                        z = false;
                    }
                    if (i != 0) {
                        this.Cv = 0;
                    }
                    float min = Math.min(uptimeMillis, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    this.CB = (int) (min * ((float) (this.Cy - this.Cx)) + ((float) this.Cx));
                }
                i = i;
                break;
            default:
                i = i;
                break;
        }
        int i2 = this.CB;
        boolean z2 = this.Cp;
        Drawable drawable = this.CE;
        Drawable drawable2 = this.CF;
        if (i != 0) {
            drawable.draw(canvas);
            if (i2 == this.Cz) {
                drawable2.setAlpha(this.Cz);
                drawable2.draw(canvas);
            }
        } else {
            if (z2) {
                drawable.setAlpha(this.Cz - i2);
            }
            drawable.draw(canvas);
            if (z2) {
                drawable.setAlpha(this.Cz);
            }
            if (i2 > 0) {
                drawable2.setAlpha(i2);
                drawable2.draw(canvas);
                drawable2.setAlpha(this.Cz);
            }
            invalidateSelf();
        }
    }

    public Drawable ez() {
        return this.CF;
    }

    public int getChangingConfigurations() {
        return (super.getChangingConfigurations() | this.CD.CM) | this.CD.CN;
    }

    public ConstantState getConstantState() {
        if (!canConstantState()) {
            return null;
        }
        this.CD.CM = getChangingConfigurations();
        return this.CD;
    }

    public int getIntrinsicHeight() {
        return Math.max(this.CE.getIntrinsicHeight(), this.CF.getIntrinsicHeight());
    }

    public int getIntrinsicWidth() {
        return Math.max(this.CE.getIntrinsicWidth(), this.CF.getIntrinsicWidth());
    }

    public int getOpacity() {
        if (!this.CI) {
            this.CJ = Drawable.resolveOpacity(this.CE.getOpacity(), this.CF.getOpacity());
            this.CI = true;
        }
        return this.CJ;
    }

    public void invalidateDrawable(Drawable who) {
        if (gr.fu()) {
            Callback callback = getCallback();
            if (callback != null) {
                callback.invalidateDrawable(this);
            }
        }
    }

    public Drawable mutate() {
        if (this.CC || super.mutate() != this || canConstantState()) {
            this.CE.mutate();
            this.CF.mutate();
            this.CC = true;
        } else {
            throw new IllegalStateException("One or more children of this LayerDrawable does not have constant state; this drawable cannot be mutated.");
        }
        return this;
    }

    protected void onBoundsChange(Rect bounds) {
        this.CE.setBounds(bounds);
        this.CF.setBounds(bounds);
    }

    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        if (gr.fu()) {
            Callback callback = getCallback();
            if (callback != null) {
                callback.scheduleDrawable(this, what, when);
            }
        }
    }

    public void setAlpha(int alpha) {
        if (this.CB == this.Cz) {
            this.CB = alpha;
        }
        this.Cz = alpha;
        invalidateSelf();
    }

    public void setColorFilter(ColorFilter cf) {
        this.CE.setColorFilter(cf);
        this.CF.setColorFilter(cf);
    }

    public void startTransition(int durationMillis) {
        this.Cx = 0;
        this.Cy = this.Cz;
        this.CB = 0;
        this.CA = durationMillis;
        this.Cv = 1;
        invalidateSelf();
    }

    public void unscheduleDrawable(Drawable who, Runnable what) {
        if (gr.fu()) {
            Callback callback = getCallback();
            if (callback != null) {
                callback.unscheduleDrawable(this, what);
            }
        }
    }
}