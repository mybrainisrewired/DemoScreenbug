package com.wmt.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.wmt.opengl.GLView;
import com.wmt.opengl.grid.ItemAnimation;
import com.wmt.remotectrl.EventPacket;
import com.wmt.scene.Scene;

public class WmtPopupWindow extends PopupWindow {
    private static final int DISMISS = 1;
    private static final int INIT = 0;
    private static final int MOVEANK = 2;
    private static final String TAG = "WmtPopupWindow";
    public static final int TYPE_OVAL = 1;
    public static final int TYPE_RECT = 0;
    private Activity mActivity;
    private AlertController mAlert;
    private Handler mHandler;
    private boolean mIsMoving;
    private Parameter mParameter;
    private OnTouchListener onTouchInterceptor;

    private class AlertController {
        private static final int bottomMargin = 10;
        private static final int leftMargin = 10;
        private static final int rightMargin = 10;
        private static final int topMargin = 10;
        private LinearLayout buttonLinearLayout;
        private TextView mContentTextView;
        private Context mContext;
        private ImageView mImageIcon;
        private View mLinearView;
        private ListView mListView;
        private RelativeLayout mRelativeLayout;
        private TextView mTitleTextView;

        AlertController(Context context) {
            this.mContext = context;
            this.mRelativeLayout = new RelativeLayout(this.mContext);
            LayoutParams RlLayout = getLayout(PagerAdapter.POSITION_NONE, PagerAdapter.POSITION_NONE);
            this.mImageIcon = new ImageView(this.mContext);
            int id = 1 + 1;
            this.mImageIcon.setId(TYPE_OVAL);
            this.mImageIcon.setLayoutParams(RlLayout);
            this.mImageIcon.setVisibility(GLView.FLAG_POPUP);
            this.mRelativeLayout.addView(this.mImageIcon);
            RlLayout = getLayout(PagerAdapter.POSITION_NONE, PagerAdapter.POSITION_NONE);
            this.mTitleTextView = new TextView(this.mContext);
            int id2 = id + 1;
            this.mTitleTextView.setId(id);
            this.mTitleTextView.setTextSize(16.0f);
            this.mTitleTextView.setTypeface(null, TYPE_OVAL);
            RlLayout.addRule(TYPE_OVAL, this.mImageIcon.getId());
            this.mTitleTextView.setLayoutParams(RlLayout);
            this.mRelativeLayout.addView(this.mTitleTextView);
            RlLayout = getLayout(-1, MOVEANK);
            RlLayout.addRule(ItemAnimation.CUR_ALPHA, this.mImageIcon.getId());
            RlLayout.leftMargin = WmtPopupWindow.this.mParameter.mEdgeWidth + 2;
            RlLayout.rightMargin = WmtPopupWindow.this.mParameter.mEdgeWidth + 2;
            this.mLinearView = new View(this.mContext);
            id = id2 + 1;
            this.mLinearView.setId(id2);
            this.mLinearView.setBackgroundColor(WmtPopupWindow.this.mParameter.mEdgeColor);
            this.mLinearView.setLayoutParams(RlLayout);
            this.mLinearView.setVisibility(GLView.FLAG_POPUP);
            this.mRelativeLayout.addView(this.mLinearView);
            RlLayout = getLayout(PagerAdapter.POSITION_NONE, PagerAdapter.POSITION_NONE);
            this.mContentTextView = new TextView(this.mContext);
            id2 = id + 1;
            this.mContentTextView.setId(id);
            this.mContentTextView.setTextSize(16.0f);
            RlLayout.addRule(ItemAnimation.CUR_ALPHA, this.mLinearView.getId());
            this.mContentTextView.setLayoutParams(RlLayout);
            this.mContentTextView.setVisibility(GLView.FLAG_POPUP);
            this.mRelativeLayout.addView(this.mContentTextView);
            this.buttonLinearLayout = new LinearLayout(context);
            id = id2 + 1;
            this.buttonLinearLayout.setId(id2);
            RlLayout = getLayout(-1, PagerAdapter.POSITION_NONE);
            RlLayout.topMargin /= 2;
            RlLayout.bottomMargin = WmtPopupWindow.this.getAnchorHeight() + 3;
            RlLayout.addRule(ColorBaseSlot.CB_INDEX_BLACK);
            this.buttonLinearLayout.setLayoutParams(RlLayout);
            this.mRelativeLayout.addView(this.buttonLinearLayout);
            RlLayout = getLayout(PagerAdapter.POSITION_NONE, PagerAdapter.POSITION_NONE);
            this.mListView = new ListView(this.mContext);
            this.mListView.setBackgroundColor(INIT);
            this.mListView.setCacheColorHint(INIT);
            id2 = id + 1;
            this.mListView.setId(id);
            RlLayout.addRule(ItemAnimation.CUR_ALPHA, this.mContentTextView.getId());
            RlLayout.addRule(MOVEANK, this.buttonLinearLayout.getId());
            this.mListView.setLayoutParams(RlLayout);
            this.mListView.setVisibility(GLView.FLAG_POPUP);
            this.mRelativeLayout.addView(this.mListView);
            WmtPopupWindow.this.setContentView(this.mRelativeLayout);
        }

        private LayoutParams getLayout(int w, int h) {
            LayoutParams RL = new LayoutParams(w, h);
            RL.topMargin = 10;
            RL.leftMargin = WmtPopupWindow.this.mParameter.mEdgeWidth + 10;
            RL.rightMargin = WmtPopupWindow.this.mParameter.mEdgeWidth + 10;
            return RL;
        }

        public void setButton(com.wmt.util.WmtPopupWindow.ButtonParameter[] buttonParameterArray) {
            LayoutParams RlLayout = (LayoutParams) this.buttonLinearLayout.getLayoutParams();
            int buttonWidth = (((WmtPopupWindow.this.getWidth() - RlLayout.leftMargin) - RlLayout.rightMargin) - ((buttonParameterArray.length + 1) * 10)) / buttonParameterArray.length;
            Log.v(TAG, "" + buttonWidth + ":" + WmtPopupWindow.this.getWidth() + ":" + WmtPopupWindow.this.mParameter.mEdgeWidth + ":" + topMargin + ":" + topMargin + ":" + buttonParameterArray.length);
            int i = INIT;
            while (i < buttonParameterArray.length) {
                Button button = new Button(WmtPopupWindow.this.mActivity);
                button.setWidth(buttonWidth);
                button.setText(buttonParameterArray[i].mButtonText);
                if (buttonParameterArray[i].mDrawableId > 0) {
                    button.setBackgroundResource(buttonParameterArray[i].mDrawableId);
                }
                button.setOnClickListener(buttonParameterArray[i].mListener);
                LinearLayout.LayoutParams llLayout = new LinearLayout.LayoutParams(-2, -2);
                llLayout.leftMargin = 10;
                button.setLayoutParams(llLayout);
                this.buttonLinearLayout.addView(button);
                i++;
            }
        }

        public void setIcon(Drawable iconDw) {
            this.mImageIcon.setBackgroundDrawable(iconDw);
            this.mImageIcon.setVisibility(INIT);
            this.mLinearView.setVisibility(INIT);
            ((LayoutParams) this.mLinearView.getLayoutParams()).addRule(ItemAnimation.CUR_ALPHA, this.mImageIcon.getId());
        }

        public void setItems(String[] items, OnItemClickListener listener) {
            this.mListView.setAdapter(new ArrayAdapter(this.mContext, 17367043, items));
            this.mListView.setOnItemClickListener(listener);
            this.mListView.setVisibility(INIT);
        }

        public void setText(String ContentText) {
            this.mContentTextView.setText(ContentText);
            this.mContentTextView.setVisibility(INIT);
        }

        public void setTextBkground(Drawable imageDw) {
            this.mContentTextView.setBackgroundDrawable(imageDw);
            this.mContentTextView.setVisibility(INIT);
        }

        public void setTitle(String title) {
            this.mTitleTextView.setText(title);
            this.mTitleTextView.setVisibility(INIT);
            this.mLinearView.setVisibility(INIT);
            if (this.mImageIcon.getVisibility() != 0) {
                ((LayoutParams) this.mLinearView.getLayoutParams()).addRule(ItemAnimation.CUR_ALPHA, this.mTitleTextView.getId());
            }
        }
    }

    public static class ButtonParameter {
        public String mButtonText;
        public int mDrawableId;
        public OnClickListener mListener;

        public ButtonParameter() {
            this.mButtonText = null;
            this.mListener = null;
            this.mDrawableId = -1;
        }
    }

    public static class Parameter {
        public boolean mAnchor;
        private int mAnchorHeight;
        public int mAngle;
        public int mAutoDismiss;
        public int mBackgroundDrawableId;
        public int mEdgeColor;
        public int mEdgeWidth;
        public int mHeight;
        public int mInnerColor;
        public int mLayoutId;
        public boolean mOutregion;
        public int mStartMoveTime;
        public int mType;
        public int mWidth;

        public Parameter() {
            this.mLayoutId = -1;
            this.mBackgroundDrawableId = -1;
            this.mWidth = -1;
            this.mHeight = -1;
            this.mAngle = 10;
            this.mEdgeWidth = 3;
            this.mType = 0;
            this.mInnerColor = -1442840576;
            this.mEdgeColor = -1426063361;
            this.mAnchor = true;
            this.mOutregion = false;
            this.mAutoDismiss = -1;
            this.mStartMoveTime = -1;
            this.mAnchorHeight = -1;
        }
    }

    public WmtPopupWindow(Activity a, Parameter p) {
        View view;
        if (p.mLayoutId < 0) {
            view = new View(a);
        } else {
            view = a.getLayoutInflater().inflate(p.mLayoutId, null);
        }
        this(view, p.mWidth, p.mHeight);
        this.mActivity = a;
        this.mParameter = p;
        if (p.mBackgroundDrawableId >= 0) {
            Drawable dw = this.mActivity.getResources().getDrawable(p.mBackgroundDrawableId);
            setWidth(dw.getIntrinsicWidth());
            setHeight(dw.getIntrinsicHeight());
            setBackgroundDrawable(dw);
        } else if ((p.mWidth < -2 || p.mWidth > -1 || p.mHeight < -2 || p.mHeight > -1) && (p.mWidth <= 0 || p.mHeight <= 0)) {
            throw new IllegalArgumentException("mWidth and mHeight parameter error");
        } else {
            autosetBkDrawable(p);
        }
        if (p.mLayoutId < 0) {
            this.mAlert = new AlertController(this.mActivity);
        }
    }

    private WmtPopupWindow(View mConttentView, int width, int height) {
        super(mConttentView, width, height);
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case INIT:
                        Log.v(TAG, "INIT");
                        if (WmtPopupWindow.this.getContentView().getMeasuredWidth() <= 0) {
                            WmtPopupWindow.this.mHandler.sendMessageDelayed(WmtPopupWindow.this.mHandler.obtainMessage(INIT, msg.arg1, msg.arg2, msg.obj), 10);
                        } else {
                            com.wmt.util.WmtPopupWindow.Parameter p = (com.wmt.util.WmtPopupWindow.Parameter) msg.obj;
                            p.mWidth = WmtPopupWindow.this.getContentView().getMeasuredWidth();
                            p.mHeight = WmtPopupWindow.this.getContentView().getMeasuredHeight();
                            WmtPopupWindow.this.autosetBkDrawable(p);
                            WmtPopupWindow.this.show(msg.arg1, msg.arg2);
                        }
                    case TYPE_OVAL:
                        WmtPopupWindow.this.mHandler.removeMessages(TYPE_OVAL);
                        WmtPopupWindow.this.cancel();
                    case MOVEANK:
                        WmtPopupWindow.this.mIsMoving = true;
                        WmtPopupWindow.this.changeBackground();
                    default:
                        break;
                }
            }
        };
        this.onTouchInterceptor = new OnTouchListener() {
            private final float OFFSET;
            private int nowX;
            private int nowY;
            private int offX;
            private int offY;
            private int oldX;
            private int oldY;

            {
                this.OFFSET = 10.0f;
            }

            public boolean onTouch(View arg0, MotionEvent event) {
                switch (event.getAction()) {
                    case INIT:
                        this.oldX = (int) event.getRawX();
                        this.oldY = (int) event.getRawY();
                        this.offX = (int) event.getX();
                        this.offY = (int) event.getY();
                        WmtPopupWindow.this.mIsMoving = false;
                        WmtPopupWindow.this.mHandler.removeMessages(MOVEANK);
                        WmtPopupWindow.this.mHandler.sendMessageDelayed(WmtPopupWindow.this.mHandler.obtainMessage(MOVEANK), (long) WmtPopupWindow.this.mParameter.mStartMoveTime);
                        break;
                    case TYPE_OVAL:
                        WmtPopupWindow.this.mHandler.removeMessages(MOVEANK);
                        if (WmtPopupWindow.this.mIsMoving) {
                            WmtPopupWindow.this.mIsMoving = false;
                            WmtPopupWindow.this.changeBackground();
                        }
                        break;
                    case MOVEANK:
                        if (WmtPopupWindow.this.mIsMoving) {
                            this.nowX = (int) event.getRawX();
                            this.nowY = (int) event.getRawY();
                            if (((float) Math.abs(this.nowX - this.oldX)) > 10.0f || ((float) Math.abs(this.nowY - this.oldY)) > 10.0f) {
                                WmtPopupWindow.this.update(this.nowX - this.offX, this.nowY - this.offY, WmtPopupWindow.this.getWidth(), WmtPopupWindow.this.getHeight(), true);
                            }
                        }
                        break;
                }
                return false;
            }
        };
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
    }

    private void autosetBkDrawable(Parameter p) {
        Drawable dw = getBackBitmap(p);
        setWidth(dw.getIntrinsicWidth());
        setHeight(dw.getIntrinsicHeight());
        setBackgroundDrawable(dw);
    }

    private void changeBackground() {
        int i = INIT;
        Drawable Background = getBackground();
        if (Background != null) {
            if (this.mIsMoving) {
                Background.setAlpha(EventPacket.EVENT_ALIVE_PING);
            } else {
                Background.setAlpha(MotionEventCompat.ACTION_MASK);
            }
            View view = getContentView();
            if (view != null) {
                boolean z;
                if (this.mIsMoving) {
                    z = false;
                } else {
                    z = true;
                }
                view.setEnabled(z);
                if (this.mIsMoving) {
                    i = ItemAnimation.CUR_ARC;
                }
                view.setVisibility(i);
            }
        }
    }

    private int getAnchorHeight() {
        return this.mParameter.mAnchorHeight;
    }

    private static Drawable getBackBitmap(Parameter p) {
        int width = p.mWidth;
        int height = p.mHeight;
        int angle = p.mAngle;
        int edgeWidth = p.mEdgeWidth;
        int type = p.mType;
        int innerColor = p.mInnerColor;
        int edgeColor = p.mEdgeColor;
        boolean anchor = p.mAnchor;
        int HPER = height < 50 ? MOVEANK : height < 100 ? ItemAnimation.CUR_ALPHA : height < 200 ? FragmentManagerImpl.ANIM_STYLE_FADE_ENTER : height < 300 ? GLView.FLAG_DEFAULT : Scene.NO_FLING;
        p.mAnchorHeight = anchor ? height / HPER : INIT;
        Bitmap mutablePhoto = Bitmap.createBitmap(width, p.mAnchorHeight + height, Config.ARGB_8888);
        Canvas canvas = new Canvas(mutablePhoto);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        float f = (float) width;
        float f2 = (float) height;
        RectF rectF = rectf;
        paint.setStrokeWidth((float) p.mEdgeWidth);
        int vStart = (width - (width / 10)) / 2;
        int vStop = ((width / 10) + width) / 2;
        if (p.mAnchorHeight > 0) {
            Path mSavePath = new Path();
            mSavePath.moveTo(0.0f, 0.0f);
            mSavePath.lineTo((float) width, 0.0f);
            mSavePath.lineTo((float) width, (float) height);
            mSavePath.lineTo((float) vStop, (float) height);
            mSavePath.lineTo((float) ((edgeWidth + 1) / 2 + vStop), (float) (height - edgeWidth));
            mSavePath.lineTo((float) (vStart - (edgeWidth + 1) / 2), (float) (height - edgeWidth));
            mSavePath.lineTo((float) vStart, (float) height);
            mSavePath.lineTo(0.0f, (float) height);
            mSavePath.lineTo(0.0f, 0.0f);
            mSavePath.close();
            canvas.save();
            canvas.clipPath(mSavePath);
        }
        if (type == 1) {
            paint.setColor(innerColor);
            canvas.drawOval(rectf, paint);
            paint.setStyle(Style.STROKE);
            paint.setColor(edgeColor);
            canvas.drawOval(rectf, paint);
        } else {
            paint.setStyle(Style.FILL);
            paint.setColor(innerColor);
            canvas.drawRoundRect(rectf, (float) angle, (float) angle, paint);
            paint.setStyle(Style.STROKE);
            paint.setColor(edgeColor);
            canvas.drawRoundRect(rectf, (float) angle, (float) angle, paint);
        }
        if (p.mAnchorHeight > 0) {
            canvas.restore();
            Path mPathinner = new Path();
            mPathinner.moveTo((float) (vStart - edgeWidth), (float) (height - edgeWidth));
            mPathinner.lineTo((float) (width / 2), (float) (height / HPER + height));
            mPathinner.lineTo((float) (vStop + edgeWidth), (float) (height - edgeWidth));
            mPathinner.close();
            paint.setStyle(Style.STROKE);
            paint.setColor(edgeColor);
            canvas.drawLine((float) (vStart - edgeWidth), (float) (height - 1), (float) (width / 2), (float) (height / HPER + height - 2), paint);
            canvas.drawLine((float) (width / 2), (float) (height / HPER + height - 2), (float) (vStop + edgeWidth), (float) (height - 1), paint);
            paint.setStrokeWidth(1.0f);
            canvas.drawLine((float) (width / 2), (float) (height / HPER + height - 2), (float) (width / 2), (float) (height / HPER + height), paint);
            paint.setStyle(Style.FILL);
            paint.setColor(innerColor);
            canvas.drawPath(mPathinner, paint);
        }
        BitmapDrawable bdw = new BitmapDrawable(null, mutablePhoto);
        canvas.setBitmap(Utils.s_nullBitmap);
        return bdw;
    }

    public static ButtonParameter getDefaultButtonParameter() {
        return new ButtonParameter();
    }

    public static Parameter getDefaultParameter() {
        return new Parameter();
    }

    private void requestAutoDismiss(Parameter p) {
        this.mHandler.removeMessages(TYPE_OVAL);
        if (p.mAutoDismiss > 0) {
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(TYPE_OVAL), (long) p.mAutoDismiss);
        }
    }

    public void cancel() {
        if (isShowing()) {
            dismiss();
        }
    }

    public Point getAnchorPos() {
        Point anchorPoint = new Point();
        anchorPoint.x = getWidth() >> 1;
        anchorPoint.y = getHeight();
        return anchorPoint;
    }

    public WmtPopupWindow setButton(ButtonParameter[] buttonParameterArray) {
        this.mAlert.setButton(buttonParameterArray);
        return this;
    }

    public WmtPopupWindow setIcon(Drawable iconDw) {
        if (iconDw != null) {
            this.mAlert.setIcon(iconDw);
        }
        return this;
    }

    public WmtPopupWindow setItems(Drawable icon, String[] items, String title, OnItemClickListener listener) {
        this.mAlert.setIcon(icon);
        this.mAlert.setTitle(title);
        this.mAlert.setItems(items, listener);
        return this;
    }

    public WmtPopupWindow setItems(String[] items, OnItemClickListener listener) {
        this.mAlert.setItems(items, listener);
        return this;
    }

    public WmtPopupWindow setText(String ContentText) {
        if (ContentText != null) {
            this.mAlert.setText(ContentText);
        }
        return this;
    }

    public WmtPopupWindow setTextBkground(Drawable imageDw) {
        this.mAlert.setTextBkground(imageDw);
        return this;
    }

    public WmtPopupWindow setTitle(String title) {
        if (title != null) {
            this.mAlert.setTitle(title);
        }
        return this;
    }

    public void show() {
        show(this.mActivity.getWindowManager().getDefaultDisplay().getWidth() / 2, (this.mActivity.getWindowManager().getDefaultDisplay().getHeight() - getHeight()) / 2 + getHeight());
    }

    public void show(int offX, int offY) {
        this.mIsMoving = false;
        this.mHandler.removeMessages(MOVEANK);
        cancel();
        changeBackground();
        Point point = getAnchorPos();
        offX -= point.x;
        offY -= point.y;
        if (!this.mParameter.mOutregion) {
            Rect frame = new Rect();
            this.mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int mStatusBarHeight = frame.top;
            if (offY < mStatusBarHeight) {
                offY = mStatusBarHeight;
            }
        }
        showAtLocation(this.mActivity.getWindow().getDecorView(), INIT, offX, offY);
        if (this.mParameter.mAutoDismiss > 0) {
            requestAutoDismiss(this.mParameter);
        }
        if (getBackground() == null) {
            cancel();
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(INIT, offX, offY, this.mParameter), 10);
        } else if (this.mParameter.mStartMoveTime > 0) {
            setTouchInterceptor(this.onTouchInterceptor);
        } else {
            setTouchInterceptor(null);
        }
    }
}