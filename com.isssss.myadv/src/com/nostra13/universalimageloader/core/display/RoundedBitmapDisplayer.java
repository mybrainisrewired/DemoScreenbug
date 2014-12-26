package com.nostra13.universalimageloader.core.display;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.ViewCompat;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.utils.L;

public class RoundedBitmapDisplayer implements BitmapDisplayer {
    private static /* synthetic */ int[] $SWITCH_TABLE$android$widget$ImageView$ScaleType;
    private final int roundPixels;

    static /* synthetic */ int[] $SWITCH_TABLE$android$widget$ImageView$ScaleType() {
        int[] iArr = $SWITCH_TABLE$android$widget$ImageView$ScaleType;
        if (iArr == null) {
            iArr = new int[ScaleType.values().length];
            try {
                iArr[ScaleType.CENTER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ScaleType.CENTER_CROP.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ScaleType.CENTER_INSIDE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[ScaleType.FIT_CENTER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[ScaleType.FIT_END.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[ScaleType.FIT_START.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[ScaleType.FIT_XY.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[ScaleType.MATRIX.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            $SWITCH_TABLE$android$widget$ImageView$ScaleType = iArr;
        }
        return iArr;
    }

    public RoundedBitmapDisplayer(int roundPixels) {
        this.roundPixels = roundPixels;
    }

    private static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int roundPixels, Rect srcRect, Rect destRect, int width, int height) {
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        RectF destRectF = new RectF(destRect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawRoundRect(destRectF, (float) roundPixels, (float) roundPixels, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, srcRect, destRectF, paint);
        return output;
    }

    public static Bitmap roundCorners(Bitmap bitmap, ImageView imageView, int roundPixels) {
        int width;
        int height;
        Rect srcRect;
        Rect destRect;
        int bw = bitmap.getWidth();
        int bh = bitmap.getHeight();
        int vw = imageView.getWidth();
        int vh = imageView.getHeight();
        if (vw <= 0) {
            vw = bw;
        }
        if (vh <= 0) {
            vh = bh;
        }
        int x;
        int y;
        switch ($SWITCH_TABLE$android$widget$ImageView$ScaleType()[imageView.getScaleType().ordinal()]) {
            case MMAdView.TRANSITION_FADE:
            case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                width = Math.min(vw, bw);
                height = Math.min(vh, bh);
                x = (bw - width) / 2;
                y = (bh - height) / 2;
                srcRect = new Rect(x, y, x + width, y + height);
                destRect = new Rect(0, 0, width, height);
                break;
            case MMAdView.TRANSITION_UP:
                int srcWidth;
                int srcHeight;
                if (((float) vw) / ((float) vh) > ((float) bw) / ((float) bh)) {
                    srcWidth = bw;
                    srcHeight = (int) (((float) vh) * (((float) bw) / ((float) vw)));
                    x = 0;
                    y = (bh - srcHeight) / 2;
                } else {
                    srcWidth = (int) (((float) vw) * (((float) bh) / ((float) vh)));
                    srcHeight = bh;
                    x = (bw - srcWidth) / 2;
                    y = 0;
                }
                width = srcWidth;
                height = srcHeight;
                srcRect = new Rect(x, y, x + srcWidth, y + srcHeight);
                destRect = new Rect(0, 0, width, height);
                break;
            case MMAdView.TRANSITION_DOWN:
                int destHeight;
                int destWidth;
                if (((float) vw) / ((float) vh) > ((float) bw) / ((float) bh)) {
                    destHeight = Math.min(vh, bh);
                    destWidth = (int) (((float) bw) / (((float) bh) / ((float) destHeight)));
                } else {
                    destWidth = Math.min(vw, bw);
                    destHeight = (int) (((float) bh) / (((float) bw) / ((float) destWidth)));
                }
                x = (vw - destWidth) / 2;
                y = (vh - destHeight) / 2;
                srcRect = new Rect(0, 0, bw, bh);
                destRect = new Rect(x, y, x + destWidth, y + destHeight);
                width = vw;
                height = vh;
                break;
            case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                width = vw;
                height = vh;
                srcRect = new Rect(0, 0, bw, bh);
                destRect = new Rect(0, 0, width, height);
                break;
            default:
                if (((float) vw) / ((float) vh) > ((float) bw) / ((float) bh)) {
                    width = (int) (((float) bw) / (((float) bh) / ((float) vh)));
                    height = vh;
                } else {
                    width = vw;
                    height = (int) (((float) bh) / (((float) bw) / ((float) vw)));
                }
                srcRect = new Rect(0, 0, bw, bh);
                destRect = new Rect(0, 0, width, height);
                break;
        }
        try {
            return getRoundedCornerBitmap(bitmap, roundPixels, srcRect, destRect, width, height);
        } catch (OutOfMemoryError e) {
            L.e(e, "Can't create bitmap with rounded corners. Not enough memory.", new Object[0]);
            return bitmap;
        }
    }

    public Bitmap display(Bitmap bitmap, ImageView imageView, LoadedFrom loadedFrom) {
        Bitmap roundedBitmap = roundCorners(bitmap, imageView, this.roundPixels);
        imageView.setImageBitmap(roundedBitmap);
        return roundedBitmap;
    }
}