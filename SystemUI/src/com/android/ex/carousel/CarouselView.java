package com.android.ex.carousel;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Mesh;
import android.renderscript.RSSurfaceView;
import android.renderscript.RenderScriptGL;
import android.renderscript.RenderScriptGL.SurfaceConfig;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnLongClickListener;
import com.android.ex.carousel.CarouselRS.CarouselCallback;
import com.android.systemui.statusbar.CommandQueue;

public abstract class CarouselView extends RSSurfaceView {
    public static final int DRAG_MODEL_CYLINDER_INSIDE = 2;
    public static final int DRAG_MODEL_CYLINDER_OUTSIDE = 3;
    public static final int DRAG_MODEL_PLANE = 1;
    public static final int DRAG_MODEL_SCREEN_DELTA = 0;
    public static final int FILL_DIRECTION_CCW = 1;
    public static final int FILL_DIRECTION_CW = -1;
    private static final String TAG = "CarouselView";
    private static final boolean USE_DEPTH_BUFFER = true;
    private Context mContext;
    CarouselController mController;
    private RenderScriptGL mRS;
    private CarouselRS mRenderScript;
    private boolean mTracking;

    public static class DetailAlignment {
        public static final int ABOVE = 8;
        public static final int BELOW = 16;
        public static final int CENTER_HORIZONTAL = 256;
        public static final int CENTER_VERTICAL = 1;
        public static final int HORIZONTAL_ALIGNMENT_MASK = 65280;
        public static final int LEFT = 512;
        public static final int RIGHT = 1024;
        public static final int VERTICAL_ALIGNMENT_MASK = 255;
        public static final int VIEW_BOTTOM = 4;
        public static final int VIEW_TOP = 2;
    }

    public static class Info {
        public int resId;

        public Info(int _resId) {
            this.resId = _resId;
        }
    }

    public static class InterpolationMode {
        public static final int ACCELERATE_DECELERATE_CUBIC = 2;
        public static final int DECELERATE_QUADRATIC = 1;
        public static final int LINEAR = 0;
    }

    public CarouselView(Context context) {
        this(context, new CarouselController());
    }

    public CarouselView(Context context, AttributeSet attrs) {
        this(context, attrs, new CarouselController());
    }

    public CarouselView(Context context, AttributeSet attrs, CarouselController controller) {
        super(context, attrs);
        this.mContext = context;
        this.mController = controller;
        ensureRenderScript();
        setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (!CarouselView.this.interpretLongPressEvents()) {
                    return false;
                }
                CarouselView.this.mController.onLongPress();
                return USE_DEPTH_BUFFER;
            }
        });
    }

    public CarouselView(Context context, CarouselController controller) {
        this(context, null, controller);
    }

    private void ensureRenderScript() {
        if (this.mRS == null) {
            SurfaceConfig sc = new SurfaceConfig();
            sc.setDepth(CommandQueue.FLAG_EXCLUDE_COMPAT_MODE_PANEL, 24);
            this.mRS = createRenderScriptGL(sc);
        }
        if (this.mRenderScript == null) {
            this.mRenderScript = new CarouselRS(this.mRS, this.mContext.getResources(), getRenderScriptInfo().resId);
            this.mRenderScript.resumeRendering();
        }
        this.mController.setRS(this.mRS, this.mRenderScript);
    }

    public void createCards(int n) {
        this.mController.createCards(n);
    }

    public int getCardCount() {
        return this.mController.getCardCount();
    }

    public CarouselController getController() {
        return this.mController;
    }

    public abstract Info getRenderScriptInfo();

    public boolean interpretLongPressEvents() {
        return false;
    }

    public Mesh loadGeometry(int resId) {
        return this.mController.loadGeometry(resId);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ensureRenderScript();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mRenderScript = null;
        if (this.mRS != null) {
            this.mRS = null;
            destroyRenderScriptGL();
        }
        this.mController.setRS(this.mRS, this.mRenderScript);
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int action = event.getAction();
        if (this.mRenderScript != null) {
            switch (action) {
                case DRAG_MODEL_SCREEN_DELTA:
                    this.mTracking = true;
                    this.mController.onTouchStarted(event.getX(), event.getY(), event.getEventTime());
                    break;
                case FILL_DIRECTION_CCW:
                    this.mController.onTouchStopped(event.getX(), event.getY(), event.getEventTime());
                    this.mTracking = false;
                    break;
                case DRAG_MODEL_CYLINDER_INSIDE:
                    if (this.mTracking) {
                        int i = DRAG_MODEL_SCREEN_DELTA;
                        while (i < event.getHistorySize()) {
                            this.mController.onTouchMoved(event.getHistoricalX(i), event.getHistoricalY(i), event.getHistoricalEventTime(i));
                            i++;
                        }
                        this.mController.onTouchMoved(event.getX(), event.getY(), event.getEventTime());
                    }
                    break;
                default:
                    break;
            }
        }
        return USE_DEPTH_BUFFER;
    }

    public void setBackgroundBitmap(Bitmap bitmap) {
        this.mController.setBackgroundBitmap(bitmap);
    }

    public void setBackgroundColor(float red, float green, float blue, float alpha) {
        this.mController.setBackgroundColor(red, green, blue, alpha);
    }

    public void setCallback(CarouselCallback callback) {
        this.mController.setCallback(callback);
    }

    public void setCardRotation(float cardRotation) {
        this.mController.setCardRotation(cardRotation);
    }

    public void setCardsFaceTangent(boolean faceTangent) {
        this.mController.setCardsFaceTangent(faceTangent);
    }

    public void setController(CarouselController controller) {
        this.mController = controller;
        this.mController.setRS(this.mRS, this.mRenderScript);
    }

    public void setDefaultBitmap(Bitmap bitmap) {
        this.mController.setDefaultBitmap(bitmap);
    }

    public void setDefaultCardMatrix(float[] matrix) {
        this.mController.setDefaultCardMatrix(matrix);
    }

    public void setDefaultGeometry(int resId) {
        this.mController.setDefaultGeometry(resId);
    }

    public void setDetailLineBitmap(Bitmap bitmap) {
        this.mController.setDetailLineBitmap(bitmap);
    }

    public void setDetailLoadingBitmap(Bitmap bitmap) {
        this.mController.setDetailLoadingBitmap(bitmap);
    }

    public void setDetailTextureAlignment(int alignment) {
        this.mController.setDetailTextureAlignment(alignment);
    }

    public void setDetailTextureForItem(int n, float offx, float offy, float loffx, float loffy, Bitmap bitmap) {
        this.mController.setDetailTextureForItem(n, offx, offy, loffx, loffy, bitmap);
    }

    public void setDragFactor(float dragFactor) {
        this.mController.setDragFactor(dragFactor);
    }

    public void setDragModel(int model) {
        this.mController.setDragModel(model);
    }

    public void setDrawRuler(boolean drawRuler) {
        this.mController.setDrawRuler(drawRuler);
    }

    public void setFadeInDuration(long t) {
        this.mController.setFadeInDuration(t);
    }

    public void setFirstCardTop(boolean f) {
        this.mController.setFirstCardTop(f);
    }

    public void setForceBlendCardsWithZ(boolean enabled) {
        this.mController.setForceBlendCardsWithZ(enabled);
    }

    public void setFrictionCoefficient(float frictionCoefficient) {
        this.mController.setFrictionCoefficient(frictionCoefficient);
    }

    public void setGeometryForItem(int n, Mesh mesh) {
        this.mController.setGeometryForItem(n, mesh);
    }

    public void setLoadingBitmap(Bitmap bitmap) {
        this.mController.setLoadingBitmap(bitmap);
    }

    public void setLoadingGeometry(int resId) {
        this.mController.setLoadingGeometry(resId);
    }

    public void setLookAt(float[] eye, float[] at, float[] up) {
        this.mController.setLookAt(eye, at, up);
    }

    public void setMatrixForItem(int n, float[] matrix) {
        this.mController.setMatrixForItem(n, matrix);
    }

    public void setOverscrollSlots(float slots) {
        this.mController.setOverscrollSlots(slots);
    }

    public void setPrefetchCardCount(int n) {
        this.mController.setPrefetchCardCount(n);
    }

    public void setRadius(float radius) {
        this.mController.setRadius(radius);
    }

    public void setRezInCardCount(float n) {
        this.mController.setRezInCardCount(n);
    }

    public void setRowCount(int n) {
        this.mController.setRowCount(n);
    }

    public void setRowSpacing(float s) {
        this.mController.setRowSpacing(s);
    }

    public void setSlotCount(int n) {
        this.mController.setSlotCount(n);
    }

    public void setStartAngle(float angle) {
        this.mController.setStartAngle(angle);
    }

    public void setSwaySensitivity(float swaySensitivity) {
        this.mController.setSwaySensitivity(swaySensitivity);
    }

    public void setTextureForItem(int n, Bitmap bitmap) {
        this.mController.setTextureForItem(n, bitmap);
    }

    public void setVisibleDetails(int n) {
        this.mController.setVisibleDetails(n);
    }

    public void setVisibleSlots(int n) {
        this.mController.setVisibleSlots(n);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        super.surfaceChanged(holder, format, w, h);
        this.mController.onSurfaceChanged();
    }
}