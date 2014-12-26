package android.support.v4.widget;

import android.content.Context;
import android.os.Build.VERSION;
import android.widget.Scroller;

class ScrollerCompat {
    Scroller mScroller;

    static class ScrollerCompatImplIcs extends ScrollerCompat {
        public ScrollerCompatImplIcs(Context context) {
            super(context);
        }

        public float getCurrVelocity() {
            return ScrollerCompatIcs.getCurrVelocity(this.mScroller);
        }
    }

    ScrollerCompat(Context context) {
        this.mScroller = new Scroller(context);
    }

    public static ScrollerCompat from(Context context) {
        return VERSION.SDK_INT >= 14 ? new ScrollerCompatImplIcs(context) : new ScrollerCompat(context);
    }

    public void abortAnimation() {
        this.mScroller.abortAnimation();
    }

    public boolean computeScrollOffset() {
        return this.mScroller.computeScrollOffset();
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
        this.mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
    }

    public float getCurrVelocity() {
        return 0.0f;
    }

    public int getCurrX() {
        return this.mScroller.getCurrX();
    }

    public int getCurrY() {
        return this.mScroller.getCurrY();
    }

    public int getDuration() {
        return this.mScroller.getDuration();
    }

    public boolean isFinished() {
        return this.mScroller.isFinished();
    }

    public void startScroll(int startX, int startY, int dx, int dy) {
        this.mScroller.startScroll(startX, startY, dx, dy);
    }

    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        this.mScroller.startScroll(startX, startY, dx, dy, duration);
    }
}