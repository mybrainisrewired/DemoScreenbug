package com.wmt.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import com.wmt.util.Utils;

public class BitmapTexture extends AnimationTexture {
    private static final String TAG = "BitmapTexture";
    private int mDestHeight;
    private int mDestWidth;
    private String mPath;
    private int mResourceId;

    public BitmapTexture() {
        this(-1, -1, -1);
    }

    public BitmapTexture(int resourceId) {
        this(resourceId, -1, -1);
    }

    public BitmapTexture(int destWidth, int destHeight) {
        this(-1, destWidth, destHeight);
    }

    public BitmapTexture(int resourceId, int destWidth, int destHeight) {
        this.mResourceId = resourceId;
        this.mDestWidth = destWidth;
        this.mDestHeight = destHeight;
    }

    public BitmapTexture(String path) {
        this(path, -1, -1);
    }

    public BitmapTexture(String path, int destWidth, int destHeight) {
        this.mPath = path;
        this.mDestWidth = destWidth;
        this.mDestHeight = destHeight;
    }

    protected Bitmap load(Context context) {
        Bitmap bitmap = loadBitmap(context);
        if (bitmap == null || this.mDestWidth == -1) {
            return bitmap;
        }
        if (bitmap.getWidth() == this.mDestWidth && bitmap.getHeight() == this.mDestHeight) {
            return bitmap;
        }
        Bitmap scaleBitmap = Bitmap.createBitmap(this.mDestWidth, this.mDestHeight, bitmap.getConfig());
        Canvas c = new Canvas(scaleBitmap);
        Paint p = new Paint();
        p.setAntiAlias(true);
        c.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new Rect(0, 0, this.mDestWidth, this.mDestHeight), p);
        c.setBitmap(Utils.s_nullBitmap);
        bitmap.recycle();
        return scaleBitmap;
    }

    public Bitmap loadBitmap(Context ctx) {
        if (this.mResourceId > 0) {
            return BitmapFactory.decodeResource(ctx.getResources(), this.mResourceId);
        }
        if (this.mPath != null) {
            return BitmapFactory.decodeFile(this.mPath);
        }
        Log.w(TAG, "loadBitmap must be override when mResourceId < 0");
        return null;
    }
}