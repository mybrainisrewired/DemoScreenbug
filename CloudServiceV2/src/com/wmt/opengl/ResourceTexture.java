package com.wmt.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import java.io.IOException;
import java.io.InputStream;

public final class ResourceTexture extends Texture {
    private final int mResourceId;
    private final boolean mScaled;

    public ResourceTexture(int resourceId, boolean scaled) {
        this.mResourceId = resourceId;
        this.mScaled = scaled;
    }

    protected Bitmap load(Context context) {
        Options options;
        if (this.mScaled) {
            options = new Options();
            options.inPreferredConfig = Config.ARGB_8888;
            return BitmapFactory.decodeResource(context.getResources(), this.mResourceId, options);
        } else {
            InputStream inputStream = context.getResources().openRawResource(this.mResourceId);
            if (inputStream == null) {
                return null;
            }
            try {
                options = new Options();
                options.inPreferredConfig = Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                try {
                    inputStream.close();
                    return bitmap;
                } catch (IOException e) {
                    return bitmap;
                }
            } catch (Exception e2) {
                try {
                    inputStream.close();
                    return null;
                } catch (IOException e3) {
                    return null;
                }
            } catch (Throwable th) {
                try {
                    inputStream.close();
                } catch (IOException e4) {
                }
            }
        }
    }
}