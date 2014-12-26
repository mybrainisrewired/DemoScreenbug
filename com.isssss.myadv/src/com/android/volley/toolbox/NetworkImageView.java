package com.android.volley.toolbox;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;

public class NetworkImageView extends ImageView {
    private int mDefaultImageId;
    private int mErrorImageId;
    private ImageContainer mImageContainer;
    private ImageLoader mImageLoader;
    private String mUrl;

    class AnonymousClass_1 implements ImageListener {
        private final /* synthetic */ boolean val$isInLayoutPass;

        class AnonymousClass_1 implements Runnable {
            private final /* synthetic */ ImageContainer val$response;

            AnonymousClass_1(ImageContainer imageContainer) {
                this.val$response = imageContainer;
            }

            public void run() {
                AnonymousClass_1.this.onResponse(this.val$response, false);
            }
        }

        AnonymousClass_1(boolean z) {
            this.val$isInLayoutPass = z;
        }

        public void onErrorResponse(VolleyError error) {
            if (NetworkImageView.this.mErrorImageId != 0) {
                NetworkImageView.this.setImageResource(NetworkImageView.this.mErrorImageId);
            }
        }

        public void onResponse(ImageContainer response, boolean isImmediate) {
            if (isImmediate && this.val$isInLayoutPass) {
                NetworkImageView.this.post(new AnonymousClass_1(response));
            } else if (response.getBitmap() != null) {
                NetworkImageView.this.setImageBitmap(response.getBitmap());
            } else if (NetworkImageView.this.mDefaultImageId != 0) {
                NetworkImageView.this.setImageResource(NetworkImageView.this.mDefaultImageId);
            }
        }
    }

    public NetworkImageView(Context context) {
        this(context, null);
    }

    public NetworkImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void loadImageIfNecessary(boolean r8_isInLayoutPass) {
        throw new UnsupportedOperationException("Method not decompiled: com.android.volley.toolbox.NetworkImageView.loadImageIfNecessary(boolean):void");
        /*
        r7 = this;
        r5 = -2;
        r3 = r7.getWidth();
        r0 = r7.getHeight();
        r4 = r7.getLayoutParams();
        r4 = r4.height;
        if (r4 != r5) goto L_0x0021;
    L_0x0011:
        r4 = r7.getLayoutParams();
        r4 = r4.width;
        if (r4 != r5) goto L_0x0021;
    L_0x0019:
        r1 = 1;
    L_0x001a:
        if (r3 != 0) goto L_0x0023;
    L_0x001c:
        if (r0 != 0) goto L_0x0023;
    L_0x001e:
        if (r1 != 0) goto L_0x0023;
    L_0x0020:
        return;
    L_0x0021:
        r1 = 0;
        goto L_0x001a;
    L_0x0023:
        r4 = r7.mUrl;
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 == 0) goto L_0x003d;
    L_0x002b:
        r4 = r7.mImageContainer;
        if (r4 == 0) goto L_0x0037;
    L_0x002f:
        r4 = r7.mImageContainer;
        r4.cancelRequest();
        r4 = 0;
        r7.mImageContainer = r4;
    L_0x0037:
        r4 = r7.mDefaultImageId;
        r7.setImageResource(r4);
        goto L_0x0020;
    L_0x003d:
        r4 = r7.mImageContainer;
        if (r4 == 0) goto L_0x0061;
    L_0x0041:
        r4 = r7.mImageContainer;
        r4 = r4.getRequestUrl();
        if (r4 == 0) goto L_0x0061;
    L_0x0049:
        r4 = r7.mImageContainer;
        r4 = r4.getRequestUrl();
        r5 = r7.mUrl;
        r4 = r4.equals(r5);
        if (r4 != 0) goto L_0x0020;
    L_0x0057:
        r4 = r7.mImageContainer;
        r4.cancelRequest();
        r4 = r7.mDefaultImageId;
        r7.setImageResource(r4);
    L_0x0061:
        r4 = r7.mImageLoader;
        r5 = r7.mUrl;
        r6 = new com.android.volley.toolbox.NetworkImageView$1;
        r6.<init>(r8);
        r2 = r4.get(r5, r6);
        r7.mImageContainer = r2;
        goto L_0x0020;
        */
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    protected void onDetachedFromWindow() {
        if (this.mImageContainer != null) {
            this.mImageContainer.cancelRequest();
            setImageBitmap(null);
            this.mImageContainer = null;
        }
        super.onDetachedFromWindow();
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        loadImageIfNecessary(true);
    }

    public void setDefaultImageResId(int defaultImage) {
        this.mDefaultImageId = defaultImage;
    }

    public void setErrorImageResId(int errorImage) {
        this.mErrorImageId = errorImage;
    }

    public void setImageUrl(String url, ImageLoader imageLoader) {
        if (url != null && this != null) {
            this.mUrl = url;
            this.mImageLoader = imageLoader;
            this.mImageLoader.putViewMap(url, this);
            loadImageIfNecessary(false);
        }
    }
}