package com.wmt.frameworkbridge.video;

import android.content.Context;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManagerImpl;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.wmt.data.LocalAudioAll;
import com.wmt.opengl.GLView;
import com.wmt.opengl.grid.ItemAnimation;

public class MovieDragController {
    public static int DRAG_ACTION_COPY = 0;
    public static int DRAG_ACTION_MOVE = 0;
    private static final String TAG = "MovieDragController";
    private Context mContext;
    private final int[] mCoordinatesTemp;
    private DisplayMetrics mDisplayMetrics;
    private boolean mDragging;
    private MovieDropTarget mDropTarget;
    private InputMethodManager mInputMethodManager;
    private LayoutParams mLayoutParams;
    private float mMotionDownX;
    private float mMotionDownY;
    private View mOrigView;
    private TextView mSubtitleDragView;
    private float mTouchOffsetX;
    private float mTouchOffsetY;
    private WindowManager mWindowManager;
    private IBinder mWindowToken;

    static {
        DRAG_ACTION_MOVE = 0;
        DRAG_ACTION_COPY = 1;
    }

    public MovieDragController(Context context) {
        this.mCoordinatesTemp = new int[2];
        this.mDisplayMetrics = new DisplayMetrics();
        this.mContext = context;
        this.mWindowManager = WindowManagerImpl.getDefault();
    }

    private static int clamp(int val, int min, int max) {
        if (val < min) {
            return min;
        }
        return val >= max ? max - 1 : val;
    }

    private void createSubtitleDragView(int origX, int origY) {
        LayoutParams lp = new LayoutParams(-1, -2, origX, origY, 1002, 768, -3);
        lp.gravity = 51;
        lp.token = this.mWindowToken;
        lp.setTitle("SubtitleTextView");
        this.mLayoutParams = lp;
        this.mSubtitleDragView = (TextView) LayoutInflater.from(this.mContext).inflate(17367147, null);
        CharSequence text = ((TextView) this.mOrigView).getText();
        this.mSubtitleDragView.setTextSize(((TextView) this.mOrigView).getTextSize());
        this.mSubtitleDragView.setText(text);
        this.mSubtitleDragView.setTextColor(((TextView) this.mOrigView).getTextColors());
        this.mSubtitleDragView.setHeight(this.mOrigView.getHeight());
        this.mSubtitleDragView.setBackgroundResource(17302883);
        this.mWindowManager.addView(this.mSubtitleDragView, lp);
    }

    private boolean drop(float x, float y) {
        int[] coordinates = this.mCoordinatesTemp;
        MovieDropTarget dropTarget = this.mDropTarget;
        this.mDropTarget.getLocationOnScreen(coordinates);
        coordinates[0] = (int) (x - ((float) coordinates[0]));
        coordinates[1] = (int) (y - ((float) coordinates[1]));
        if (dropTarget == null) {
            return false;
        }
        dropTarget.onDrop(coordinates[0], coordinates[1], (int) this.mTouchOffsetX, (int) this.mTouchOffsetY);
        return true;
    }

    private void endDrag() {
        if (this.mDragging) {
            this.mDragging = false;
            if (this.mOrigView != null) {
                this.mOrigView.setVisibility(0);
            }
            if (this.mSubtitleDragView != null) {
                removeDragView();
                this.mSubtitleDragView = null;
            }
        }
    }

    private void moveDragView(int x, int y) {
        LayoutParams lp = this.mLayoutParams;
        lp.x = 0;
        lp.y = y;
        this.mWindowManager.updateViewLayout(this.mSubtitleDragView, lp);
    }

    private void recordScreenSize() {
        ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay().getMetrics(this.mDisplayMetrics);
    }

    private void removeDragView() {
        try {
            this.mWindowManager.removeView(this.mSubtitleDragView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelDrag() {
        endDrag();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return this.mDragging;
    }

    public void notifySubtitleContentChanged(String text) {
        if (this.mSubtitleDragView != null) {
            this.mSubtitleDragView.setText(text);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == 0) {
            recordScreenSize();
        }
        int screenX = clamp((int) ev.getRawX(), 0, this.mDisplayMetrics.widthPixels);
        int screenY = clamp((int) ev.getRawY(), 0, this.mDisplayMetrics.heightPixels);
        switch (action) {
            case LocalAudioAll.SORT_BY_TITLE:
                this.mMotionDownX = (float) screenX;
                this.mMotionDownY = (float) screenY;
                break;
            case LocalAudioAll.SORT_BY_DATE:
            case ItemAnimation.CUR_ALPHA:
                if (this.mDragging) {
                    drop((float) screenX, (float) screenY);
                }
                endDrag();
                break;
        }
        return this.mDragging;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (!this.mDragging) {
            return false;
        }
        int screenX = clamp((int) ev.getRawX(), 0, this.mDisplayMetrics.widthPixels);
        int screenY = clamp((int) ev.getRawY(), 0, this.mDisplayMetrics.heightPixels);
        switch (action) {
            case LocalAudioAll.SORT_BY_TITLE:
                this.mMotionDownX = (float) screenX;
                this.mMotionDownY = (float) screenY;
                break;
            case LocalAudioAll.SORT_BY_DATE:
                if (this.mDragging) {
                    drop((float) screenX, (float) screenY);
                }
                endDrag();
                break;
            case ItemAnimation.CUR_Z:
                moveDragView(0, (int) (ev.getRawY() - this.mTouchOffsetY));
                break;
            case ItemAnimation.CUR_ALPHA:
                cancelDrag();
                break;
        }
        return true;
    }

    public void setDropTarget(MovieDropTarget target) {
        if (target == null) {
            Log.e(TAG, "setDropTarget", new IllegalAccessException("add a null DropTarget"));
        } else {
            this.mDropTarget = target;
        }
    }

    public void setWindowToken(IBinder token) {
        this.mWindowToken = token;
    }

    public void startDrag(View v, int dragAction) {
        this.mOrigView = v;
        int[] loc = this.mCoordinatesTemp;
        v.getLocationOnScreen(loc);
        int screenX = loc[0];
        int screenY = loc[1];
        if (this.mInputMethodManager == null) {
            this.mInputMethodManager = (InputMethodManager) this.mContext.getSystemService("input_method");
        }
        this.mInputMethodManager.hideSoftInputFromWindow(this.mWindowToken, 0);
        this.mTouchOffsetX = this.mMotionDownX - ((float) screenX);
        this.mTouchOffsetY = this.mMotionDownY - ((float) screenY);
        this.mDragging = true;
        createSubtitleDragView(screenX, screenY);
        if (dragAction == DRAG_ACTION_MOVE) {
            v.setVisibility(GLView.FLAG_POPUP);
        }
    }
}