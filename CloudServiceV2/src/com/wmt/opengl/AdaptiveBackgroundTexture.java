package com.wmt.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import com.wmt.util.Utils;

public final class AdaptiveBackgroundTexture extends Texture {
    private static final int BLUE_MASK = 255;
    private static final int GREEN_MASK = 65280;
    private static final int GREEN_MASK_SHIFT = 8;
    private static int[] KERNEL_NORM = null;
    private static final int KERNEL_SIZE = 9;
    private static final int MAX_COLOR_VALUE = 255;
    private static final int MULTIPLY_COLOR = -5592406;
    private static final int NUM_COLORS = 256;
    private static final int RADIUS = 4;
    private static final int RED_MASK = 16711680;
    private static final int RED_MASK_SHIFT = 16;
    private static final int START_FADE_X = 96;
    private static final int THUMBNAIL_MAX_X = 128;
    private Texture mBaseTexture;
    private final int mHeight;
    private final Bitmap mSource;
    private final int mWidth;

    public AdaptiveBackgroundTexture(Bitmap source, int width, int height) {
        this.mSource = source;
        this.mWidth = width;
        this.mHeight = height;
        this.mBaseTexture = null;
    }

    public AdaptiveBackgroundTexture(Texture texture, int width, int height) {
        this.mBaseTexture = texture;
        this.mSource = null;
        this.mWidth = width;
        this.mHeight = height;
    }

    private static void boxBlurFilter(int[] in, int[] out, int width, int height, int startFadeX) {
        int inPos = 0;
        int maxX = width - 1;
        int y = 0;
        while (y < height) {
            int red = 0;
            int green = 0;
            int blue = 0;
            int i = -4;
            while (i <= 4) {
                int argb = in[FloatUtils.clamp(i, 0, maxX) + inPos];
                red += (16711680 & argb) >> 16;
                green += (65280 & argb) >> 8;
                blue += argb & 255;
                i++;
            }
            int alpha = y < startFadeX ? MAX_COLOR_VALUE : (((height - y) - 1) * 255) / (height - startFadeX);
            int outPos = y;
            int x = 0;
            while (x != width) {
                out[outPos] = (((alpha << 24) | (KERNEL_NORM[red] << 16)) | (KERNEL_NORM[green] << 8)) | KERNEL_NORM[blue];
                int prevX = FloatUtils.clamp(x - 4, 0, maxX);
                int nextX = FloatUtils.clamp(x + 4 + 1, 0, maxX);
                int prevArgb = in[inPos + prevX];
                int nextArgb = in[inPos + nextX];
                red += ((16711680 & nextArgb) - (16711680 & prevArgb)) >> 16;
                green += ((65280 & nextArgb) - (65280 & prevArgb)) >> 8;
                blue += nextArgb & 255 - prevArgb & 255;
                outPos += height;
                x++;
            }
            inPos += width;
            y++;
        }
    }

    private void initKernelNorm() {
        if (KERNEL_NORM == null) {
            KERNEL_NORM = new int[2304];
            int i = 2303;
            while (i >= 0) {
                KERNEL_NORM[i] = i / 9;
                i--;
            }
        }
    }

    protected Bitmap load(Context context) {
        initKernelNorm();
        Bitmap source = this.mSource;
        if (source != null || this.mBaseTexture == null) {
            return null;
        }
        int cropWidth;
        int cropHeight;
        int cropX;
        int cropY;
        float scale;
        source = this.mBaseTexture.externalLoad(context);
        if (source == null) {
            return null;
        }
        source = Utils.resizeBitmap(source, THUMBNAIL_MAX_X);
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        int destWidth = this.mWidth;
        int destHeight = this.mHeight;
        float fitX = ((float) sourceWidth) / ((float) destWidth);
        float fitY = ((float) sourceHeight) / ((float) destHeight);
        if (fitX < fitY) {
            cropWidth = sourceWidth;
            cropHeight = (int) (((float) destHeight) * fitX);
            cropX = 0;
            cropY = (sourceHeight - cropHeight) / 2;
            scale = 1.0f / fitX;
        } else {
            cropWidth = (int) (((float) destHeight) * fitY);
            cropHeight = sourceHeight;
            cropX = (sourceWidth - cropWidth) / 2;
            cropY = 0;
            scale = 1.0f / fitY;
        }
        int numPixels = cropWidth * cropHeight;
        int[] in = new int[numPixels];
        int[] tmp = new int[numPixels];
        source.getPixels(in, 0, cropWidth, cropX, cropY, cropWidth, cropHeight);
        boxBlurFilter(in, tmp, cropWidth, cropHeight, cropWidth);
        boxBlurFilter(tmp, in, cropHeight, cropWidth, START_FADE_X);
        Bitmap filtered = Bitmap.createBitmap(in, cropWidth, cropHeight, Config.ARGB_8888);
        Bitmap output = Bitmap.createBitmap(destWidth, destHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColorFilter(new LightingColorFilter(-5592406, 0));
        canvas.scale(scale, scale);
        canvas.drawBitmap(filtered, 0.0f, 0.0f, paint);
        canvas.drawColor(2132811808, Mode.DARKEN);
        filtered.recycle();
        canvas.setBitmap(Utils.s_nullBitmap);
        return output;
    }
}