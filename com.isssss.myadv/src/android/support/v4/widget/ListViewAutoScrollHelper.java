package android.support.v4.widget;

import android.view.View;
import android.widget.ListView;

public class ListViewAutoScrollHelper extends AutoScrollHelper {
    private final ListView mTarget;

    public ListViewAutoScrollHelper(ListView target) {
        super(target);
        this.mTarget = target;
    }

    public boolean canTargetScrollHorizontally(int direction) {
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean canTargetScrollVertically(int r11_direction) {
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.widget.ListViewAutoScrollHelper.canTargetScrollVertically(int):boolean");
        /*
        r10 = this;
        r7 = 0;
        r6 = r10.mTarget;
        r3 = r6.getCount();
        if (r3 != 0) goto L_0x000a;
    L_0x0009:
        return r7;
    L_0x000a:
        r0 = r6.getChildCount();
        r1 = r6.getFirstVisiblePosition();
        r4 = r1 + r0;
        if (r11 <= 0) goto L_0x002a;
    L_0x0016:
        if (r4 < r3) goto L_0x0028;
    L_0x0018:
        r8 = r0 + -1;
        r5 = r6.getChildAt(r8);
        r8 = r5.getBottom();
        r9 = r6.getHeight();
        if (r8 <= r9) goto L_0x0009;
    L_0x0028:
        r7 = 1;
        goto L_0x0009;
    L_0x002a:
        if (r11 >= 0) goto L_0x0009;
    L_0x002c:
        if (r1 > 0) goto L_0x0028;
    L_0x002e:
        r2 = r6.getChildAt(r7);
        r8 = r2.getTop();
        if (r8 < 0) goto L_0x0028;
    L_0x0038:
        goto L_0x0009;
        */
    }

    public void scrollTargetBy(int deltaX, int deltaY) {
        ListView target = this.mTarget;
        int firstPosition = target.getFirstVisiblePosition();
        if (firstPosition != -1) {
            View firstView = target.getChildAt(0);
            if (firstView != null) {
                target.setSelectionFromTop(firstPosition, firstView.getTop() - deltaY);
            }
        }
    }
}