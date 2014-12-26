package com.wmt.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import com.wmt.util.Utils;

public class NinePatchTexture extends Texture {
    Rect mBounds;
    int mResId;

    public NinePatchTexture(int mResId, Rect rect) {
        this.mResId = mResId;
        this.mBounds = rect;
    }

    protected Bitmap load(Context context) {
        Drawable drawable = context.getResources().getDrawable(this.mResId);
        if (drawable instanceof NinePatchDrawable) {
            NinePatchDrawable d = (NinePatchDrawable) drawable;
            d.setBounds(0, 0, this.mBounds.width(), this.mBounds.height());
            Bitmap b = Bitmap.createBitmap(this.mBounds.width(), this.mBounds.height(), Config.ARGB_8888);
            Canvas c = new Canvas(b);
            d.draw(c);
            c.setBitmap(Utils.s_nullBitmap);
            return b;
        } else {
            throw new IllegalArgumentException("You must use a 9.png as mResId when construct Texture9Patch(int mResId, Rect rect) cllled!");
        }
    }
}