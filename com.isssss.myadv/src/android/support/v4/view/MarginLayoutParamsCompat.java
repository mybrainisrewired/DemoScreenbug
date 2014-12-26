package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.ViewGroup.MarginLayoutParams;

public class MarginLayoutParamsCompat {
    static final MarginLayoutParamsCompatImpl IMPL;

    static interface MarginLayoutParamsCompatImpl {
        int getLayoutDirection(MarginLayoutParams marginLayoutParams);

        int getMarginEnd(MarginLayoutParams marginLayoutParams);

        int getMarginStart(MarginLayoutParams marginLayoutParams);

        boolean isMarginRelative(MarginLayoutParams marginLayoutParams);

        void resolveLayoutDirection(MarginLayoutParams marginLayoutParams, int i);

        void setLayoutDirection(MarginLayoutParams marginLayoutParams, int i);

        void setMarginEnd(MarginLayoutParams marginLayoutParams, int i);

        void setMarginStart(MarginLayoutParams marginLayoutParams, int i);
    }

    static class MarginLayoutParamsCompatImplBase implements MarginLayoutParamsCompatImpl {
        MarginLayoutParamsCompatImplBase() {
        }

        public int getLayoutDirection(MarginLayoutParams lp) {
            return 0;
        }

        public int getMarginEnd(MarginLayoutParams lp) {
            return lp.rightMargin;
        }

        public int getMarginStart(MarginLayoutParams lp) {
            return lp.leftMargin;
        }

        public boolean isMarginRelative(MarginLayoutParams lp) {
            return false;
        }

        public void resolveLayoutDirection(MarginLayoutParams lp, int layoutDirection) {
        }

        public void setLayoutDirection(MarginLayoutParams lp, int layoutDirection) {
        }

        public void setMarginEnd(MarginLayoutParams lp, int marginEnd) {
            lp.rightMargin = marginEnd;
        }

        public void setMarginStart(MarginLayoutParams lp, int marginStart) {
            lp.leftMargin = marginStart;
        }
    }

    static class MarginLayoutParamsCompatImplJbMr1 implements MarginLayoutParamsCompatImpl {
        MarginLayoutParamsCompatImplJbMr1() {
        }

        public int getLayoutDirection(MarginLayoutParams lp) {
            return MarginLayoutParamsCompatJellybeanMr1.getLayoutDirection(lp);
        }

        public int getMarginEnd(MarginLayoutParams lp) {
            return MarginLayoutParamsCompatJellybeanMr1.getMarginEnd(lp);
        }

        public int getMarginStart(MarginLayoutParams lp) {
            return MarginLayoutParamsCompatJellybeanMr1.getMarginStart(lp);
        }

        public boolean isMarginRelative(MarginLayoutParams lp) {
            return MarginLayoutParamsCompatJellybeanMr1.isMarginRelative(lp);
        }

        public void resolveLayoutDirection(MarginLayoutParams lp, int layoutDirection) {
            MarginLayoutParamsCompatJellybeanMr1.resolveLayoutDirection(lp, layoutDirection);
        }

        public void setLayoutDirection(MarginLayoutParams lp, int layoutDirection) {
            MarginLayoutParamsCompatJellybeanMr1.setLayoutDirection(lp, layoutDirection);
        }

        public void setMarginEnd(MarginLayoutParams lp, int marginEnd) {
            MarginLayoutParamsCompatJellybeanMr1.setMarginEnd(lp, marginEnd);
        }

        public void setMarginStart(MarginLayoutParams lp, int marginStart) {
            MarginLayoutParamsCompatJellybeanMr1.setMarginStart(lp, marginStart);
        }
    }

    static {
        if (VERSION.SDK_INT >= 17) {
            IMPL = new MarginLayoutParamsCompatImplJbMr1();
        } else {
            IMPL = new MarginLayoutParamsCompatImplBase();
        }
    }

    public static int getLayoutDirection(MarginLayoutParams lp) {
        return IMPL.getLayoutDirection(lp);
    }

    public static int getMarginEnd(MarginLayoutParams lp) {
        return IMPL.getMarginEnd(lp);
    }

    public static int getMarginStart(MarginLayoutParams lp) {
        return IMPL.getMarginStart(lp);
    }

    public static boolean isMarginRelative(MarginLayoutParams lp) {
        return IMPL.isMarginRelative(lp);
    }

    public static void resolveLayoutDirection(MarginLayoutParams lp, int layoutDirection) {
        IMPL.resolveLayoutDirection(lp, layoutDirection);
    }

    public static void setLayoutDirection(MarginLayoutParams lp, int layoutDirection) {
        IMPL.setLayoutDirection(lp, layoutDirection);
    }

    public static void setMarginEnd(MarginLayoutParams lp, int marginEnd) {
        IMPL.setMarginEnd(lp, marginEnd);
    }

    public static void setMarginStart(MarginLayoutParams lp, int marginStart) {
        IMPL.setMarginStart(lp, marginStart);
    }
}