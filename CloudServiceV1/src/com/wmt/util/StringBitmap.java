package com.wmt.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;

public final class StringBitmap {

    public static final class Config {
        public static final int ALIGN_BOTTOM = 4;
        public static final int ALIGN_HCENTER = 0;
        public static final int ALIGN_LEFT = 1;
        public static final int ALIGN_RIGHT = 2;
        public static final int ALIGN_TOP = 3;
        public static final int ALIGN_VCENTER = 5;
        public static final com.wmt.util.StringBitmap.Config DEFAULT_CONFIG_SCALED;
        public static final com.wmt.util.StringBitmap.Config DEFAULT_CONFIG_TRUNCATED;
        private static final int DEFAULT_FONTSIZE = 20;
        private static final int FADE_WIDTH = 30;
        public static final int OVERFLOW_CLIP = 0;
        public static final int OVERFLOW_ELLIPSIZE = 1;
        public static final int OVERFLOW_FADE = 2;
        public static final int SIZE_BOUNDS_TO_TEXT = 3;
        public static final int SIZE_EXACT = 1;
        public static final int SIZE_TEXT_TO_BOUNDS = 2;
        public float a;
        public float b;
        public boolean bold;
        public int fontSize;
        public float g;
        public int height;
        public boolean italic;
        public int overflowMode;
        public float r;
        public int shadowRadius;
        public int sizeMode;
        public boolean strikeThrough;
        public boolean underline;
        public int width;
        public int xalignment;
        public int yalignment;

        static {
            DEFAULT_CONFIG_SCALED = new com.wmt.util.StringBitmap.Config(0, 0);
            DEFAULT_CONFIG_TRUNCATED = new com.wmt.util.StringBitmap.Config(0, 2);
        }

        public Config(int fontSize, int sizeMode) {
            this.fontSize = 20;
            this.r = 1.0f;
            this.g = 1.0f;
            this.b = 1.0f;
            this.a = 1.0f;
            this.shadowRadius = 4;
            this.underline = false;
            this.bold = false;
            this.italic = false;
            this.strikeThrough = false;
            this.width = 1500;
            this.height = 250;
            this.xalignment = 5;
            this.yalignment = 4;
            this.sizeMode = 3;
            this.overflowMode = 2;
            if (fontSize != 0) {
                this.fontSize = fontSize;
            }
            if (sizeMode != 0) {
                this.sizeMode = sizeMode;
            }
        }

        public Config(int fontSize, int width, int height) {
            this.fontSize = 20;
            this.r = 1.0f;
            this.g = 1.0f;
            this.b = 1.0f;
            this.a = 1.0f;
            this.shadowRadius = 4;
            this.underline = false;
            this.bold = false;
            this.italic = false;
            this.strikeThrough = false;
            this.width = 1500;
            this.height = 250;
            this.xalignment = 5;
            this.yalignment = 4;
            this.sizeMode = 3;
            this.overflowMode = 2;
            this.fontSize = fontSize;
            this.width = width;
            this.height = height;
            this.sizeMode = 1;
        }

        public void setRGB(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public void setRGB(int r, int g, int b) {
            this.r = (float) r;
            this.g = (float) g;
            this.b = (float) b;
        }
    }

    private static class StringBitmapInformation {
        int mBackgroundRes;
        com.wmt.util.StringBitmap.Config mConfig;
        int mHeight;
        String mString;
        int mWidth;

        private StringBitmapInformation() {
            this.mBackgroundRes = -1;
        }
    }

    public static Bitmap buildBitmap(Context context, String string) {
        return buildBitmap(context, string, Config.DEFAULT_CONFIG_SCALED, 0, 0, -1);
    }

    public static Bitmap buildBitmap(Context context, String string, int res) {
        return buildBitmap(context, string, Config.DEFAULT_CONFIG_SCALED, 0, 0, res);
    }

    public static Bitmap buildBitmap(Context context, String string, Config config) {
        return buildBitmap(context, string, config, config.width, config.height, -1);
    }

    public static Bitmap buildBitmap(Context context, String string, Config config, int res) {
        return buildBitmap(context, string, config, config.width, config.height, res);
    }

    public static Bitmap buildBitmap(Context context, String string, Config config, int width, int height) {
        return buildBitmap(context, string, config, width, height, -1);
    }

    public static Bitmap buildBitmap(Context context, String string, Config config, int width, int height, int res) {
        StringBitmapInformation information = new StringBitmapInformation();
        information.mString = string;
        information.mConfig = config;
        information.mWidth = width;
        information.mHeight = height;
        information.mBackgroundRes = res;
        return load(context, information);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.Paint computePaint(java.lang.String r16_string, com.wmt.util.StringBitmap.Config r17_config, int r18_width, int r19_height) {
        throw new UnsupportedOperationException("Method not decompiled: com.wmt.util.StringBitmap.computePaint(java.lang.String, com.wmt.util.StringBitmap$Config, int, int):android.graphics.Paint");
        /*
        r6 = r16;
        r7 = r18;
        r9 = new android.graphics.Paint;
        r9.<init>();
        r12 = 1;
        r9.setAntiAlias(r12);
        r0 = r17;
        r12 = r0.a;
        r13 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r12 = r12 * r13;
        r1 = (int) r12;
        r0 = r17;
        r12 = r0.r;
        r13 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r12 = r12 * r13;
        r10 = (int) r12;
        r0 = r17;
        r12 = r0.g;
        r13 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r12 = r12 * r13;
        r5 = (int) r12;
        r0 = r17;
        r12 = r0.b;
        r13 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r12 = r12 * r13;
        r2 = (int) r12;
        r3 = android.graphics.Color.argb(r1, r10, r5, r2);
        r9.setColor(r3);
        r0 = r17;
        r12 = r0.shadowRadius;
        r12 = (float) r12;
        r13 = 0;
        r14 = 0;
        r15 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        r9.setShadowLayer(r12, r13, r14, r15);
        r0 = r17;
        r12 = r0.underline;
        r9.setUnderlineText(r12);
        r0 = r17;
        r12 = r0.bold;
        if (r12 == 0) goto L_0x008f;
    L_0x004d:
        r12 = android.graphics.Typeface.DEFAULT_BOLD;
    L_0x004f:
        r9.setTypeface(r12);
        r0 = r17;
        r12 = r0.strikeThrough;
        r9.setStrikeThruText(r12);
        r0 = r17;
        r12 = r0.xalignment;
        r13 = 1;
        if (r12 != r13) goto L_0x0092;
    L_0x0060:
        r12 = android.graphics.Paint.Align.LEFT;
        r9.setTextAlign(r12);
    L_0x0065:
        r0 = r17;
        r12 = r0.italic;
        if (r12 == 0) goto L_0x0070;
    L_0x006b:
        r12 = -1098907648; // 0xffffffffbe800000 float:-0.25 double:NaN;
        r9.setTextSkewX(r12);
    L_0x0070:
        r11 = r6;
        r0 = r17;
        r12 = r0.fontSize;
        r12 = (float) r12;
        r9.setTextSize(r12);
        r0 = r17;
        r12 = r0.sizeMode;
        r13 = 2;
        if (r12 != r13) goto L_0x008e;
    L_0x0080:
        r4 = r9.getTextSize();
        r8 = 0;
        r8 = r9.measureText(r11);
        r12 = (float) r7;
        r12 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1));
        if (r12 >= 0) goto L_0x00a5;
    L_0x008e:
        return r9;
    L_0x008f:
        r12 = android.graphics.Typeface.DEFAULT;
        goto L_0x004f;
    L_0x0092:
        r0 = r17;
        r12 = r0.xalignment;
        r13 = 2;
        if (r12 != r13) goto L_0x009f;
    L_0x0099:
        r12 = android.graphics.Paint.Align.RIGHT;
        r9.setTextAlign(r12);
        goto L_0x0065;
    L_0x009f:
        r12 = android.graphics.Paint.Align.CENTER;
        r9.setTextAlign(r12);
        goto L_0x0065;
    L_0x00a5:
        r12 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r12 = r4 - r12;
        r9.setTextSize(r12);
        r12 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r12 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1));
        if (r12 > 0) goto L_0x0080;
    L_0x00b2:
        goto L_0x008e;
        */
    }

    private static Bitmap load(Context context, StringBitmapInformation information) {
        String mString = information.mString;
        Config mConfig = information.mConfig;
        int mWidth = information.mWidth;
        int mHeight = information.mHeight;
        int mBackgroundRes = information.mBackgroundRes;
        if (mString == null) {
            return null;
        }
        Paint paint = computePaint(mString, mConfig, mWidth, mHeight);
        String stringToDraw = mString;
        Config config = mConfig;
        android.graphics.Bitmap.Config bmConfig = android.graphics.Bitmap.Config.ARGB_8888;
        FontMetricsInt metrics = paint.getFontMetricsInt();
        int padding = config.shadowRadius + 1;
        int ascent = metrics.ascent - padding;
        int descent = metrics.descent + padding;
        int backWidth = mWidth;
        int backHeight = mHeight;
        String string = mString;
        Rect bounds = new Rect();
        paint.getTextBounds(string, 0, string.length(), bounds);
        if (config.sizeMode == 3) {
            backWidth = bounds.width() + padding * 2;
            backHeight = descent - ascent + padding;
        }
        if (backWidth <= 0 || backHeight <= 0) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(backWidth, backHeight, bmConfig);
        Bitmap bitmap2 = bitmap;
        int x = config.xalignment == 1 ? padding : config.xalignment == 2 ? backWidth - padding : backWidth / 2;
        int y = config.yalignment == 3 ? (-metrics.top) + padding : config.yalignment == 4 ? backHeight - descent : (backHeight - (descent + ascent)) / 2;
        canvas.drawText(stringToDraw, (float) x, (float) y, paint);
        if (mBackgroundRes != -1) {
            canvas.drawBitmap(((BitmapDrawable) context.getResources().getDrawable(mBackgroundRes)).getBitmap(), 0.0f, 0.0f, null);
        }
        if (bounds.width() > backWidth && config.overflowMode == 2) {
            float gradientLeft = (float) (backWidth - 30);
            LinearGradient gradient = new LinearGradient(gradientLeft, 0.0f, (float) backWidth, 0.0f, -1, 16777215, TileMode.CLAMP);
            paint = new Paint();
            paint.setSubpixelText(true);
            paint.setShader(gradient);
            paint.setDither(true);
            paint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));
            canvas.drawRect(gradientLeft, 0.0f, (float) backWidth, (float) backHeight, paint);
        }
        canvas.setBitmap(Utils.s_nullBitmap);
        return bitmap;
    }
}