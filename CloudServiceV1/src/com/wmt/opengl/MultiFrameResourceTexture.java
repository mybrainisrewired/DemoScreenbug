package com.wmt.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;

public final class MultiFrameResourceTexture extends Texture {
    public static final String TAG = "MultiFrameResourceTexture";
    private int mFrame;
    private int mMaxFrame;
    private final int mResourceId;
    private final boolean mScaled;

    public MultiFrameResourceTexture(int resourceId, boolean scaled, int frame, int maxFrame) {
        this.mResourceId = resourceId;
        this.mScaled = scaled;
        this.mFrame = frame;
        this.mMaxFrame = maxFrame;
    }

    protected Bitmap load(Context context) {
        Bitmap bitmap = null;
        Bitmap retBmp = null;
        if (this.mFrame < 0 || this.mFrame >= this.mMaxFrame || this.mMaxFrame <= 0) {
            Log.e(TAG, "mFrame = " + this.mFrame + ", mMaxFrame = " + this.mMaxFrame);
            return null;
        } else {
            Options options;
            if (this.mScaled) {
                options = new Options();
                options.inPreferredConfig = Config.ARGB_8888;
                bitmap = BitmapFactory.decodeResource(context.getResources(), this.mResourceId, options);
            } else {
                InputStream inputStream = context.getResources().openRawResource(this.mResourceId);
                if (inputStream != null) {
                    try {
                        options = new Options();
                        options.inPreferredConfig = Config.ARGB_8888;
                        bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                        }
                    } catch (Exception e2) {
                        try {
                            inputStream.close();
                        } catch (IOException e3) {
                        }
                    } catch (Throwable th) {
                        try {
                            inputStream.close();
                        } catch (IOException e4) {
                        }
                    }
                }
            }
            if (bitmap != null) {
                int width = bitmap.getWidth();
                retBmp = Bitmap.createBitmap(bitmap, (this.mFrame * width) / this.mMaxFrame, 0, width / this.mMaxFrame, bitmap.getHeight());
            }
            return retBmp;
        }
    }
}