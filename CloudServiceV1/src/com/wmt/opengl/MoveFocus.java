package com.wmt.opengl;

import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public final class MoveFocus {
    public static final int FOCUS_DOWN = 3;
    public static final int FOCUS_LEFT = 0;
    public static final int FOCUS_RIGHT = 1;
    public static final int FOCUS_UP = 2;
    static Comparator<FocusWeight> mComByWeight;

    public static class FocusWeight {
        int major;
        int minor;
        GLView view;
    }

    static {
        mComByWeight = new Comparator<FocusWeight>() {
            public int compare(com.wmt.opengl.MoveFocus.FocusWeight a, com.wmt.opengl.MoveFocus.FocusWeight b) {
                return a.major != b.major ? a.major - b.major : a.minor - b.minor;
            }
        };
    }

    private static void getBottomValue(Rect current, Rect view, FocusWeight weight) {
        int vert = view.bottom - current.bottom;
        if (vert <= 0) {
            weight.major = Integer.MAX_VALUE;
        } else {
            weight.major = horzValue(current, view) * 4095 + vert;
            weight.minor = view.left;
        }
    }

    private static void getLeftValue(Rect current, Rect view, FocusWeight weight) {
        int vert;
        weight.minor = Integer.MAX_VALUE;
        int horz = current.left - view.left;
        if (horz <= 0) {
            horz += 4095;
            vert = vertOffset(current, view, true) * 4095;
        } else {
            vert = vertOffset(current, view) * 4095;
        }
        weight.major = horz + vert;
        weight.minor = view.top;
    }

    private static void getRightValue(Rect current, Rect view, FocusWeight weight) {
        int vert;
        weight.minor = Integer.MAX_VALUE;
        int horz = view.right - current.right;
        if (horz <= 0) {
            horz += 4095;
            vert = vertOffset(current, view, false) * 4095;
        } else {
            vert = vertOffset(current, view) * 4095;
        }
        weight.major = horz + vert;
        weight.minor = view.top;
    }

    private static ArrayList<FocusWeight> getSortedWeightList(ArrayList<FocusWeight> wlist, GLView currentFocus, GLView focusroot, int focusDirection) {
        if (focusroot.isVisibility() && focusDirection >= 0 && focusDirection <= 3) {
            Rect current = new Rect();
            currentFocus.getFocusReferenceRect(current);
            Rect viewRect = new Rect();
            int i = FOCUS_LEFT;
            while (i < focusroot.getChildCount()) {
                GLView v = focusroot.getChild(i);
                if (v.isVisibility()) {
                    getSortedWeightList(wlist, currentFocus, v, focusDirection);
                }
                if (v != currentFocus && v.isVisibility() && v.isFocusable() && v.getChildCount() == 0) {
                    FocusWeight weight = new FocusWeight();
                    weight.view = v;
                    v.getFocusReferenceRect(viewRect);
                    if (focusDirection == 0) {
                        getLeftValue(current, viewRect, weight);
                    } else if (focusDirection == 1) {
                        getRightValue(current, viewRect, weight);
                    } else if (focusDirection == 2) {
                        getTopValue(current, viewRect, weight);
                    } else if (focusDirection == 3) {
                        getBottomValue(current, viewRect, weight);
                    }
                    wlist.add(weight);
                }
                i++;
            }
        }
        return wlist;
    }

    public static GLView getTopLeftView(GLView glView) {
        if (glView == null || !glView.isVisibility()) {
            return null;
        }
        GLView small = glView;
        Rect vRect = new Rect();
        Rect smallRect = new Rect();
        int size = glView.getChildCount();
        if (!(small.isVisibility() && small.isFocusable() && small.getChildCount() == 0)) {
            small = null;
        }
        int i = FOCUS_LEFT;
        while (i < size) {
            GLView v = getTopLeftView(glView.getChild(i));
            if (v != null && v.isVisibility() && v.isFocusable() && v.getChildCount() == 0) {
                if (small != null) {
                    v.getFocusReferenceRect(vRect);
                    small.getFocusReferenceRect(smallRect);
                    if (vRect.left < smallRect.left && vRect.top < smallRect.top) {
                        small = v;
                    } else if (vRect.left == smallRect.left && vRect.top == smallRect.top && vRect.width() < smallRect.width() && vRect.height() < smallRect.height()) {
                        small = v;
                    }
                } else {
                    small = v;
                }
            }
            i++;
        }
        return (small == null && glView.isVisibility() && glView.isFocusable()) ? glView : small;
    }

    private static void getTopValue(Rect current, Rect view, FocusWeight weight) {
        weight.minor = Integer.MAX_VALUE;
        int vert = current.top - view.top;
        if (vert <= 0) {
            weight.major = Integer.MAX_VALUE;
        } else {
            weight.major = horzValue(current, view) * 4095 + vert;
            weight.minor = view.left;
        }
    }

    private static int horzValue(Rect current, Rect view) {
        if (view.left > current.right) {
            return view.left - current.right;
        }
        return view.right < current.left ? current.left - view.right : FOCUS_LEFT;
    }

    public static GLView nextFocus(GLView currentFocus, GLView focusroot, int focusDirection) {
        ArrayList<FocusWeight> list = getSortedWeightList(new ArrayList(), currentFocus, focusroot, focusDirection);
        if (list.size() > 0) {
            Collections.sort(list, mComByWeight);
        }
        if (list.size() > 0) {
            FocusWeight w = (FocusWeight) list.get(FOCUS_LEFT);
            if (w.major < Integer.MAX_VALUE) {
                return w.view;
            }
        }
        return null;
    }

    private static int vertOffset(Rect current, Rect view) {
        if (view.top > current.bottom) {
            return view.top - current.bottom;
        }
        return view.bottom < current.top ? current.top - view.bottom : FOCUS_LEFT;
    }

    private static int vertOffset(Rect current, Rect view, boolean upPrior) {
        int vert;
        if (view.top > current.bottom) {
            vert = view.top - current.bottom;
            return upPrior ? vert + 255 : vert;
        } else if (view.bottom >= current.top) {
            return MotionEventCompat.ACTION_MASK;
        } else {
            vert = current.top - view.bottom;
            return !upPrior ? vert + 255 : vert;
        }
    }
}