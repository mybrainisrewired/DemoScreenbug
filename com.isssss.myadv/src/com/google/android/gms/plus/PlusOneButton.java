package com.google.android.gms.plus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import com.google.android.gms.internal.fq;
import com.google.android.gms.internal.ft;
import com.google.android.gms.plus.internal.g;

public final class PlusOneButton extends FrameLayout {
    public static final int ANNOTATION_BUBBLE = 1;
    public static final int ANNOTATION_INLINE = 2;
    public static final int ANNOTATION_NONE = 0;
    public static final int DEFAULT_ACTIVITY_REQUEST_CODE = -1;
    public static final int SIZE_MEDIUM = 1;
    public static final int SIZE_SMALL = 0;
    public static final int SIZE_STANDARD = 3;
    public static final int SIZE_TALL = 2;
    private View TT;
    private int TU;
    private int TV;
    private OnPlusOneClickListener TW;
    private int mSize;
    private String ro;

    public static interface OnPlusOneClickListener {
        void onPlusOneClick(Intent intent);
    }

    protected class DefaultOnPlusOneClickListener implements OnClickListener, com.google.android.gms.plus.PlusOneButton.OnPlusOneClickListener {
        private final com.google.android.gms.plus.PlusOneButton.OnPlusOneClickListener TX;

        public DefaultOnPlusOneClickListener(com.google.android.gms.plus.PlusOneButton.OnPlusOneClickListener proxy) {
            this.TX = proxy;
        }

        public void onClick(View view) {
            Intent intent = (Intent) PlusOneButton.this.TT.getTag();
            if (this.TX != null) {
                this.TX.onPlusOneClick(intent);
            } else {
                onPlusOneClick(intent);
            }
        }

        public void onPlusOneClick(Intent intent) {
            Context context = PlusOneButton.this.getContext();
            if (context instanceof Activity && intent != null) {
                ((Activity) context).startActivityForResult(intent, PlusOneButton.this.TV);
            }
        }
    }

    public PlusOneButton(Context context) {
        this(context, null);
    }

    public PlusOneButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mSize = getSize(context, attrs);
        this.TU = getAnnotation(context, attrs);
        this.TV = -1;
        v(getContext());
        if (!isInEditMode()) {
        }
    }

    protected static int getAnnotation(Context context, AttributeSet attrs) {
        String a = ft.a("http://schemas.android.com/apk/lib/com.google.android.gms.plus", "annotation", context, attrs, true, false, "PlusOneButton");
        if ("INLINE".equalsIgnoreCase(a)) {
            return SIZE_TALL;
        }
        return !"NONE".equalsIgnoreCase(a) ? 1 : SIZE_SMALL;
    }

    protected static int getSize(Context context, AttributeSet attrs) {
        String a = ft.a("http://schemas.android.com/apk/lib/com.google.android.gms.plus", "size", context, attrs, true, false, "PlusOneButton");
        if ("SMALL".equalsIgnoreCase(a)) {
            return SIZE_SMALL;
        }
        if ("MEDIUM".equalsIgnoreCase(a)) {
            return 1;
        }
        return "TALL".equalsIgnoreCase(a) ? SIZE_TALL : SIZE_STANDARD;
    }

    private void v(Context context) {
        if (this.TT != null) {
            removeView(this.TT);
        }
        this.TT = g.a(context, this.mSize, this.TU, this.ro, this.TV);
        setOnPlusOneClickListener(this.TW);
        addView(this.TT);
    }

    public void initialize(String url, int activityRequestCode) {
        fq.a(getContext() instanceof Activity, "To use this method, the PlusOneButton must be placed in an Activity. Use initialize(String, OnPlusOneClickListener).");
        this.ro = url;
        this.TV = activityRequestCode;
        v(getContext());
    }

    public void initialize(String url, OnPlusOneClickListener plusOneClickListener) {
        this.ro = url;
        this.TV = 0;
        v(getContext());
        setOnPlusOneClickListener(plusOneClickListener);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.TT.layout(SIZE_SMALL, SIZE_SMALL, right - left, bottom - top);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View view = this.TT;
        measureChild(view, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    public void setAnnotation(int annotation) {
        this.TU = annotation;
        v(getContext());
    }

    public void setOnPlusOneClickListener(OnPlusOneClickListener listener) {
        this.TW = listener;
        this.TT.setOnClickListener(new DefaultOnPlusOneClickListener(listener));
    }

    public void setSize(int size) {
        this.mSize = size;
        v(getContext());
    }
}