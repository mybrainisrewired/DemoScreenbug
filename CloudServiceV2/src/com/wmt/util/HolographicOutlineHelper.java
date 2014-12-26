package com.wmt.util;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.TableMaskFilter;
import android.support.v4.view.MotionEventCompat;
import com.wmt.remotectrl.EventPacket;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

public class HolographicOutlineHelper {
    private static final int EXTRA_THICK = 2;
    public static final int MAX_OUTER_BLUR_RADIUS;
    private static final int MEDIUM = 1;
    public static final int MIN_OUTER_BLUR_RADIUS;
    private static final int THICK = 0;
    private static final MaskFilter sCoarseClipTable;
    private static final BlurMaskFilter sExtraThickInnerBlurMaskFilter;
    private static final BlurMaskFilter sExtraThickOuterBlurMaskFilter;
    private static final BlurMaskFilter sMediumInnerBlurMaskFilter;
    private static final BlurMaskFilter sMediumOuterBlurMaskFilter;
    private static final BlurMaskFilter sThickInnerBlurMaskFilter;
    private static final BlurMaskFilter sThickOuterBlurMaskFilter;
    private static final BlurMaskFilter sThinOuterBlurMaskFilter;
    private final Paint mAlphaClipPaint;
    private final Paint mBlurPaint;
    private final Paint mErasePaint;
    private final Paint mHolographicPaint;
    private int[] mTempOffset;

    static {
        MIN_OUTER_BLUR_RADIUS = 1;
        MAX_OUTER_BLUR_RADIUS = 12;
        sExtraThickOuterBlurMaskFilter = new BlurMaskFilter(12.0f, Blur.OUTER);
        sThickOuterBlurMaskFilter = new BlurMaskFilter(6.0f, Blur.OUTER);
        sMediumOuterBlurMaskFilter = new BlurMaskFilter(2.0f, Blur.OUTER);
        sThinOuterBlurMaskFilter = new BlurMaskFilter(1.0f, Blur.OUTER);
        sExtraThickInnerBlurMaskFilter = new BlurMaskFilter(6.0f, Blur.NORMAL);
        sThickInnerBlurMaskFilter = new BlurMaskFilter(4.0f, Blur.NORMAL);
        sMediumInnerBlurMaskFilter = new BlurMaskFilter(2.0f, Blur.NORMAL);
        sCoarseClipTable = TableMaskFilter.CreateClipTable(MAX_OUTER_BLUR_RADIUS, EventPacket.SHOW_IME);
    }

    public HolographicOutlineHelper() {
        this.mHolographicPaint = new Paint();
        this.mBlurPaint = new Paint();
        this.mErasePaint = new Paint();
        this.mAlphaClipPaint = new Paint();
        this.mTempOffset = new int[2];
        this.mHolographicPaint.setFilterBitmap(true);
        this.mHolographicPaint.setAntiAlias(true);
        this.mBlurPaint.setFilterBitmap(true);
        this.mBlurPaint.setAntiAlias(true);
        this.mErasePaint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
        this.mErasePaint.setFilterBitmap(true);
        this.mErasePaint.setAntiAlias(true);
        this.mAlphaClipPaint.setMaskFilter(TableMaskFilter.CreateClipTable(Opcodes.GETFIELD, MotionEventCompat.ACTION_MASK));
    }

    public static float highlightAlphaInterpolator(float r) {
        return (float) Math.pow((double) ((1.0f - r) * 0.6f), 1.5d);
    }

    public static float viewAlphaInterpolator(float r) {
        return r < 0.95f ? (float) Math.pow((double) (r / 0.95f), 1.5d) : 1.0f;
    }

    public void applyExpensiveOutlineWithBlur(Bitmap srcDst, Canvas srcDstCanvas, int color, int outlineColor, int thickness) {
        applyExpensiveOutlineWithBlur(srcDst, srcDstCanvas, color, outlineColor, this.mAlphaClipPaint, thickness);
    }

    public void applyExpensiveOutlineWithBlur(Bitmap srcDst, Canvas srcDstCanvas, int color, int outlineColor, Paint alphaClipPaint, int thickness) {
        BlurMaskFilter outerBlurMaskFilter;
        BlurMaskFilter innerBlurMaskFilter;
        if (alphaClipPaint == null) {
            alphaClipPaint = this.mAlphaClipPaint;
        }
        Bitmap glowShape = srcDst.extractAlpha(alphaClipPaint, this.mTempOffset);
        switch (thickness) {
            case MAX_OUTER_BLUR_RADIUS:
                outerBlurMaskFilter = sThickOuterBlurMaskFilter;
                break;
            case MEDIUM:
                outerBlurMaskFilter = sMediumOuterBlurMaskFilter;
                break;
            case EXTRA_THICK:
                outerBlurMaskFilter = sExtraThickOuterBlurMaskFilter;
                break;
            default:
                throw new RuntimeException("Invalid blur thickness");
        }
        this.mBlurPaint.setMaskFilter(outerBlurMaskFilter);
        int[] outerBlurOffset = new int[2];
        Bitmap thickOuterBlur = glowShape.extractAlpha(this.mBlurPaint, outerBlurOffset);
        if (thickness == 2) {
            this.mBlurPaint.setMaskFilter(sMediumOuterBlurMaskFilter);
        } else {
            this.mBlurPaint.setMaskFilter(sThinOuterBlurMaskFilter);
        }
        int[] brightOutlineOffset = new int[2];
        Bitmap brightOutline = glowShape.extractAlpha(this.mBlurPaint, brightOutlineOffset);
        srcDstCanvas.setBitmap(glowShape);
        srcDstCanvas.drawColor(ColorBaseSlot.INVALID_COLOR, Mode.SRC_OUT);
        switch (thickness) {
            case MAX_OUTER_BLUR_RADIUS:
                innerBlurMaskFilter = sThickInnerBlurMaskFilter;
                break;
            case MEDIUM:
                innerBlurMaskFilter = sMediumInnerBlurMaskFilter;
                break;
            case EXTRA_THICK:
                innerBlurMaskFilter = sExtraThickInnerBlurMaskFilter;
                break;
            default:
                throw new RuntimeException("Invalid blur thickness");
        }
        this.mBlurPaint.setMaskFilter(innerBlurMaskFilter);
        int[] thickInnerBlurOffset = new int[2];
        Bitmap thickInnerBlur = glowShape.extractAlpha(this.mBlurPaint, thickInnerBlurOffset);
        srcDstCanvas.setBitmap(thickInnerBlur);
        srcDstCanvas.drawBitmap(glowShape, (float) (-thickInnerBlurOffset[0]), (float) (-thickInnerBlurOffset[1]), this.mErasePaint);
        srcDstCanvas.drawRect(0.0f, 0.0f, (float) (-thickInnerBlurOffset[0]), (float) thickInnerBlur.getHeight(), this.mErasePaint);
        srcDstCanvas.drawRect(0.0f, 0.0f, (float) thickInnerBlur.getWidth(), (float) (-thickInnerBlurOffset[1]), this.mErasePaint);
        srcDstCanvas.setBitmap(srcDst);
        srcDstCanvas.drawColor(MAX_OUTER_BLUR_RADIUS, Mode.CLEAR);
        this.mHolographicPaint.setColor(color);
        srcDstCanvas.drawBitmap(thickInnerBlur, (float) thickInnerBlurOffset[0], (float) thickInnerBlurOffset[1], this.mHolographicPaint);
        srcDstCanvas.drawBitmap(thickOuterBlur, (float) outerBlurOffset[0], (float) outerBlurOffset[1], this.mHolographicPaint);
        this.mHolographicPaint.setColor(outlineColor);
        srcDstCanvas.drawBitmap(brightOutline, (float) brightOutlineOffset[0], (float) brightOutlineOffset[1], this.mHolographicPaint);
        srcDstCanvas.setBitmap(null);
        brightOutline.recycle();
        thickOuterBlur.recycle();
        thickInnerBlur.recycle();
        glowShape.recycle();
    }

    public void applyExtraThickExpensiveOutlineWithBlur(Bitmap srcDst, Canvas srcDstCanvas, int color, int outlineColor) {
        applyExpensiveOutlineWithBlur(srcDst, srcDstCanvas, color, outlineColor, EXTRA_THICK);
    }

    public void applyMediumExpensiveOutlineWithBlur(Bitmap srcDst, Canvas srcDstCanvas, int color, int outlineColor) {
        applyExpensiveOutlineWithBlur(srcDst, srcDstCanvas, color, outlineColor, MEDIUM);
    }

    public void applyMediumExpensiveOutlineWithBlur(Bitmap srcDst, Canvas srcDstCanvas, int color, int outlineColor, Paint alphaClipPaint) {
        applyExpensiveOutlineWithBlur(srcDst, srcDstCanvas, color, outlineColor, alphaClipPaint, MEDIUM);
    }

    public void applyOuterBlur(Bitmap bitmap, Canvas canvas, int color) {
        this.mBlurPaint.setMaskFilter(sThickOuterBlurMaskFilter);
        Bitmap glow = bitmap.extractAlpha(this.mBlurPaint, this.mTempOffset);
        this.mHolographicPaint.setMaskFilter(sCoarseClipTable);
        this.mHolographicPaint.setAlpha(Opcodes.FCMPG);
        this.mHolographicPaint.setColor(color);
        canvas.drawBitmap(glow, (float) this.mTempOffset[0], (float) this.mTempOffset[1], this.mHolographicPaint);
        glow.recycle();
    }

    public void applyThickExpensiveOutlineWithBlur(Bitmap srcDst, Canvas srcDstCanvas, int color, int outlineColor) {
        applyExpensiveOutlineWithBlur(srcDst, srcDstCanvas, color, outlineColor, MAX_OUTER_BLUR_RADIUS);
    }
}