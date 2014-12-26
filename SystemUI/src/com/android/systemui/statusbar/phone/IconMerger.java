package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.android.systemui.R;
import com.android.systemui.statusbar.CommandQueue;

public class IconMerger extends LinearLayout {
    private static final boolean DEBUG = false;
    private static final String TAG = "IconMerger";
    private int mIconSize;
    private View mMoreView;

    class AnonymousClass_1 implements Runnable {
        final /* synthetic */ boolean val$moreRequired;

        AnonymousClass_1(boolean z) {
            this.val$moreRequired = z;
        }

        public void run() {
            IconMerger.this.mMoreView.setVisibility(this.val$moreRequired ? 0 : CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
        }
    }

    public IconMerger(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mIconSize = context.getResources().getDimensionPixelSize(R.dimen.status_bar_icon_size);
    }

    private void checkOverflow(int width) {
        boolean moreRequired = true;
        if (this.mMoreView != null) {
            boolean overflowShown;
            int N = getChildCount();
            int visibleChildren = 0;
            int i = 0;
            while (i < N) {
                if (getChildAt(i).getVisibility() != 8) {
                    visibleChildren++;
                }
                i++;
            }
            if (this.mMoreView.getVisibility() == 0) {
                overflowShown = true;
            } else {
                overflowShown = false;
            }
            if (overflowShown) {
                visibleChildren--;
            }
            if (this.mIconSize * visibleChildren <= width) {
                moreRequired = false;
            }
            if (moreRequired != overflowShown) {
                post(new AnonymousClass_1(moreRequired));
            }
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        checkOverflow(r - l);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        setMeasuredDimension(width - width % this.mIconSize, getMeasuredHeight());
    }

    public void setOverflowIndicator(View v) {
        this.mMoreView = v;
    }
}