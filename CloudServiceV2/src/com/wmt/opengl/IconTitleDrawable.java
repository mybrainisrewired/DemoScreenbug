package com.wmt.opengl;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils.TruncateAt;

public final class IconTitleDrawable extends Drawable {
    private boolean isDrawLine;
    private final Config mConfig;
    private final Drawable mIcon;
    private boolean mIsDrawIcon;
    private final String mTitle;
    private StaticLayout mTitleLayout;
    private final int mTitleWidth;
    private int mTitleY;

    public static final class Config {
        private final int mIconLeft;
        private final int mIconSize;
        private final TextPaint mPaint;
        private final int mTitleLeft;

        public Config(int iconSpan, int iconSize, TextPaint paint) {
            this.mIconLeft = (iconSpan - iconSize) / 2;
            this.mTitleLeft = this.mIconLeft;
            this.mIconSize = iconSize;
            this.mPaint = paint;
        }
    }

    public IconTitleDrawable(String title, Drawable icon, Config config) {
        this.mTitleLayout = null;
        this.isDrawLine = true;
        this.mIsDrawIcon = false;
        this.mTitle = title;
        this.mTitleWidth = (int) StaticLayout.getDesiredWidth(this.mTitle, config.mPaint);
        this.mIcon = icon;
        this.mConfig = config;
    }

    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        int x = bounds.left + this.mConfig.mTitleLeft;
        int y = this.mTitleY;
        canvas.translate((float) x, (float) y);
        this.mTitleLayout.draw(canvas);
        canvas.translate((float) (-x), (float) (-y));
        Drawable icon = this.mIcon;
        x = bounds.right - this.mConfig.mIconSize - this.mConfig.mIconLeft - 15;
        canvas.translate((float) x, 0.0f);
        if (icon != null && this.mIsDrawIcon) {
            icon.draw(canvas);
        }
        canvas.translate((float) (-x), 0.0f);
        if (this.isDrawLine) {
            canvas.drawLine((float) bounds.left, (float) bounds.bottom, (float) bounds.right, (float) bounds.bottom, this.mConfig.mPaint);
        }
    }

    public int getIntrinsicHeight() {
        Config config = this.mConfig;
        return Math.max(config.mIconSize, config.mPaint.getFontMetricsInt(null));
    }

    public int getIntrinsicWidth() {
        return this.mConfig.mTitleLeft + this.mTitleWidth + this.mConfig.mIconSize + 15;
    }

    public int getOpacity() {
        return -3;
    }

    protected void onBoundsChange(Rect bounds) {
        int left = bounds.left;
        int top = bounds.top;
        int right = bounds.right;
        int height = bounds.bottom - top;
        Config config = this.mConfig;
        int iconLeft = left + config.mIconLeft;
        int iconSize = config.mIconSize;
        Drawable icon = this.mIcon;
        if (icon != null) {
            icon.setBounds(iconLeft, top + (height - iconSize) / 2, iconLeft + iconSize, top + iconSize);
        }
        int outerWidth = right - config.mTitleLeft;
        String title = this.mTitle;
        this.mTitleLayout = new StaticLayout(title, 0, title.length(), config.mPaint, outerWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true, TruncateAt.MIDDLE, outerWidth);
        this.mTitleY = (height - this.mTitleLayout.getHeight()) / 2 + top;
    }

    public void setAlpha(int alpha) {
    }

    public void setColorFilter(ColorFilter cf) {
    }

    public void setDrawIcon(boolean bDrawIcon) {
        this.mIsDrawIcon = bDrawIcon;
    }

    public void setDrawLine(boolean is) {
        this.isDrawLine = is;
    }
}