package com.wmt.frameworkbridge.video;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.wmt.data.LocalAudioAll;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;

public class MovieDragLayer extends FrameLayout {
    private MovieDragController mDragController;
    private MovieDropContainer mDropContainer;
    private MovieSubtitleResizeLayout mSubtitleResizeLayout;
    private int mXDown;
    private int mYDown;

    public static class LayoutParams extends android.widget.FrameLayout.LayoutParams {
        public boolean customPosition;
        public int x;
        public int y;

        public LayoutParams(int width, int height) {
            super(width, height);
            this.customPosition = false;
        }

        public int getHeight() {
            return this.height;
        }

        public int getWidth() {
            return this.width;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    public MovieDragLayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean handleTouchDown(MotionEvent ev, boolean intercept) {
        Rect hitRect = new Rect();
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        if (this.mSubtitleResizeLayout != null) {
            this.mSubtitleResizeLayout.getHitRect(hitRect);
            hitRect.top -= 35;
            hitRect.bottom += 30;
            if (hitRect.contains(x, y) && this.mSubtitleResizeLayout.beginResizeIfPointInRegion(x - this.mSubtitleResizeLayout.getLeft(), y - this.mSubtitleResizeLayout.getTop())) {
                this.mXDown = x;
                this.mYDown = y;
                requestDisallowInterceptTouchEvent(true);
                return true;
            }
        }
        return false;
    }

    public void addSubtitleResizeLayout(View subtitleView) {
        this.mSubtitleResizeLayout = new MovieSubtitleResizeLayout(getContext(), subtitleView, this);
        this.mSubtitleResizeLayout.setDropContainer(this.mDropContainer);
        LayoutParams lp = new LayoutParams(-1, -1);
        lp.customPosition = true;
        addView(this.mSubtitleResizeLayout, lp);
        this.mSubtitleResizeLayout.updateLayout();
        requestLayout();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return this.mDragController.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == 0 && handleTouchDown(ev, true)) {
            return true;
        }
        removeSubtitleResizeLayout();
        return this.mDragController.onInterceptTouchEvent(ev);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int count = getChildCount();
        int i = 0;
        while (i < count) {
            View child = getChildAt(i);
            android.widget.FrameLayout.LayoutParams flp = (android.widget.FrameLayout.LayoutParams) child.getLayoutParams();
            if (flp instanceof LayoutParams) {
                LayoutParams lp = (LayoutParams) flp;
                if (lp.customPosition) {
                    child.layout(lp.x, lp.y, lp.x + lp.width, lp.y + lp.height);
                }
            }
            i++;
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        boolean handled = false;
        int action = ev.getAction();
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        if (ev.getAction() == 0 && handleTouchDown(ev, false)) {
            return true;
        }
        if (this.mSubtitleResizeLayout != null) {
            handled = true;
            switch (action) {
                case LocalAudioAll.SORT_BY_DATE:
                case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                    this.mSubtitleResizeLayout.commitResizeForDelta(x - this.mXDown, y - this.mYDown);
                    break;
                case ClassWriter.COMPUTE_FRAMES:
                    this.mSubtitleResizeLayout.visualizeResizeForDelta(x - this.mXDown, y - this.mYDown);
                    break;
            }
        }
        return !handled ? this.mDragController.onTouchEvent(ev) : true;
    }

    public void removeSubtitleResizeLayout() {
        removeView(this.mSubtitleResizeLayout);
        this.mSubtitleResizeLayout = null;
    }

    public void setDragController(MovieDragController controller) {
        this.mDragController = controller;
    }

    public void setDropContainer(MovieDropContainer dropContainer) {
        this.mDropContainer = dropContainer;
    }
}