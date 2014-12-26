package com.wmt.opengl;

import android.graphics.Rect;
import android.opengl.GLES11Ext;
import android.os.SystemClock;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.wmt.opengl.grid.ItemAnimation;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL11;

public class GLView {
    static final int FLAG_ATTACHED = 8388608;
    public static final int FLAG_DEFAULT = 7;
    public static final int FLAG_FOCUSABLE = 4;
    static final int FLAG_FOCUSED = 1048576;
    public static final int FLAG_INPUT_ENABLE = 2;
    static final int FLAG_LAYOUT_REQUESTED = 2097152;
    public static final int FLAG_POPUP = 8;
    static final int FLAG_READYDRAW = 16777216;
    static final int FLAG_ROOTVIEW = 4194304;
    public static final int FLAG_SCISSOR = 16;
    public static final int FLAG_VISIBLE = 1;
    public static final int ZORDER_BACKGROUND = -101;
    public static final int ZORDER_NORMAL = 0;
    public static final int ZORDER_POPUP = 101;
    public static final int ZORDER_PROGRESS = 102;
    public static final int ZORDER_TOAST = 110;
    protected final Rect mBounds;
    private ArrayList<GLView> mChildren;
    private Rect mClipRect;
    private int mFlags;
    private GLContext mGLContext;
    GLView mMotionTarget;
    private GLView mParent;
    private int mZorder;

    public static final class Event {
        public int arg1;
        public int arg2;
        public Object arg3;
        public int event;
    }

    public GLView(GLContext glContext, int flag) {
        this.mBounds = new Rect();
        this.mClipRect = null;
        this.mGLContext = glContext;
        this.mFlags = flag;
    }

    private final void fatalError(String error) {
        throw new RuntimeException(error);
    }

    private final GLRootView getCastRootView() {
        return (GLRootView) getRootView();
    }

    private final void removeOneView(GLView child) {
        if (this.mMotionTarget == child) {
            dispatchCancelEvent();
            this.mMotionTarget = null;
        }
        child.mParent = null;
        child.onViewDetached();
        getCastRootView().onViewDetached(child);
    }

    private final boolean setBounds(int left, int top, int right, int bottom) {
        boolean sizeChanged = (right == this.mBounds.right && left == this.mBounds.left && bottom == this.mBounds.bottom && top == this.mBounds.top) ? false : true;
        this.mBounds.set(left, top, right, bottom);
        return sizeChanged;
    }

    public final void addView(GLView child) {
        addView(child, child.isFlagSet(FLAG_POPUP) ? ZORDER_POPUP : ZORDER_NORMAL);
    }

    public final void addView(GLView child, int zOrder) {
        this.mGLContext.lockRenderThread();
        if (child.mParent != null) {
            fatalError("mParent is not null when addView");
        }
        if (this.mChildren == null) {
            this.mChildren = new ArrayList();
        }
        child.mZorder = zOrder;
        int i = ZORDER_NORMAL;
        int n = getChildCount();
        while (i < n) {
            if (zOrder < ((GLView) this.mChildren.get(i)).mZorder) {
                this.mChildren.add(i, child);
                child.mParent = this;
                break;
            } else {
                i++;
            }
        }
        if (child.mParent == null) {
            this.mChildren.add(child);
            child.mParent = this;
        }
        child.requestLayout();
        if (child.isFlagSet(FLAG_POPUP) && this != getRootView()) {
            fatalError("Popup view's parent must be RootView");
        }
        child.onViewAttached();
        getCastRootView().onViewAttached(child);
        this.mGLContext.unlockRenderThread();
    }

    public final boolean clearFocus() {
        return getCastRootView().changeFocusView(this, false);
    }

    final void dispatchCancelEvent() {
        long now = SystemClock.uptimeMillis();
        MotionEvent cancelEvent = MotionEvent.obtain(now, now, ItemAnimation.CUR_ALPHA, 0.0f, 0.0f, ZORDER_NORMAL);
        dispatchTouchEvent(cancelEvent);
        cancelEvent.recycle();
    }

    protected boolean dispatchTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int action = event.getAction();
        if (this.mMotionTarget == null || action != 0) {
            dispatchTouchEvent(event, x, y, this.mMotionTarget, false);
            if (action == 3 || action == 1) {
                this.mMotionTarget = null;
            }
            return true;
        } else {
            MotionEvent cancel = MotionEvent.obtain(event);
            cancel.setAction(ItemAnimation.CUR_ALPHA);
            dispatchTouchEvent(cancel, x, y, this.mMotionTarget, false);
            this.mMotionTarget = null;
            if (action == 0) {
                int i = getChildCount() - 1;
                while (i >= 0) {
                    GLView child = getChild(i);
                    if (child.isFlagSet(ItemAnimation.CUR_ALPHA) && dispatchTouchEvent(event, x, y, child, true)) {
                        this.mMotionTarget = child;
                        return true;
                    } else {
                        i--;
                    }
                }
            }
            return onTouchEvent(event);
        }
    }

    protected boolean dispatchTouchEvent(MotionEvent event, int x, int y, GLView child, boolean checkBounds) {
        Rect rect = child.mBounds;
        int left = rect.left;
        int top = rect.top;
        if (!checkBounds || rect.contains(x, y)) {
            event.offsetLocation((float) (-left), (float) (-top));
            if (child.dispatchTouchEvent(event)) {
                event.offsetLocation((float) left, (float) top);
                return true;
            } else {
                event.offsetLocation((float) left, (float) top);
            }
        }
        return false;
    }

    public final Rect getBounds() {
        return this.mBounds;
    }

    public final GLView getChild(int index) {
        return (this.mChildren == null || index < 0 || index >= this.mChildren.size()) ? null : (GLView) this.mChildren.get(index);
    }

    public final int getChildCount() {
        return this.mChildren == null ? ZORDER_NORMAL : this.mChildren.size();
    }

    protected void getFocusReferenceRect(Rect rect) {
        getSurfaceRect(rect);
    }

    public final int getHeight() {
        return this.mBounds.bottom - this.mBounds.top;
    }

    public final GLView getParent() {
        return this.mParent;
    }

    public final int getPositionX() {
        return this.mBounds.left;
    }

    public final int getPositionY() {
        return this.mBounds.top;
    }

    public final GLView getRootView() {
        return this.mGLContext.glRootView();
    }

    public final void getSurfaceRect(Rect out) {
        out.top = 0;
        out.left = 0;
        out.right = getWidth();
        out.bottom = getHeight();
        offsetAscRect(null, out);
    }

    public int getViewAsTexture() {
        GLCanvas glCanvas = glContext().glCanvas();
        GL11 gl = glCanvas.getGL();
        int w = glCanvas.getSurfaceWidth();
        int h = glCanvas.getSurfaceHeight();
        int[] textureId = new int[1];
        int[] frameId = new int[1];
        int viewWidth = w;
        int viewHight = h;
        textureId[0] = -1;
        frameId[0] = -1;
        gl.glGenTextures(FLAG_VISIBLE, textureId, ZORDER_NORMAL);
        gl.glBindTexture(3553, textureId[0]);
        gl.glTexParameteriv(3553, 35741, new int[]{0, viewHight, viewWidth, -viewHight}, ZORDER_NORMAL);
        gl.glTexParameteri(3553, 10242, 33071);
        gl.glTexParameteri(3553, 10243, 33071);
        gl.glTexParameterf(3553, 10241, 9729.0f);
        gl.glTexParameterf(3553, 10240, 9729.0f);
        gl.glTexImage2D(3553, ZORDER_NORMAL, 6407, w, h, ZORDER_NORMAL, 6407, 5121, null);
        Utils.checkGLError(gl);
        GLES11Ext.glGenerateMipmapOES(3553);
        GLES11Ext.glGenFramebuffersOES(FLAG_VISIBLE, frameId, ZORDER_NORMAL);
        GLES11Ext.glBindFramebufferOES(36160, frameId[0]);
        GLES11Ext.glFramebufferTexture2DOES(36160, 36064, 3553, textureId[0], ZORDER_NORMAL);
        GLES11Ext.glBindFramebufferOES(36160, frameId[0]);
        glCanvas.getTextureManager().bindGL(gl);
        glCanvas.getTextureManager().setBoundTexture(null);
        gl.glFinish();
        gl.glMatrixMode(5888);
        gl.glClear(AccessibilityEventCompat.TYPE_ANNOUNCEMENT);
        gl.glLoadIdentity();
        gl.glEnable(3089);
        gl.glScissor(ZORDER_NORMAL, ZORDER_NORMAL, glCanvas.getSurfaceWidth(), glCanvas.getSurfaceHeight());
        gl.glDisable(36197);
        gl.glEnable(3553);
        gl.glEnable(3042);
        gl.glBindTexture(3553, ZORDER_NORMAL);
        renderTree(glCanvas, ZORDER_NORMAL, ZORDER_NORMAL);
        gl.glFinish();
        GLES11Ext.glBindFramebufferOES(36160, ZORDER_NORMAL);
        GLES11Ext.glDeleteFramebuffersOES(FLAG_VISIBLE, frameId, ZORDER_NORMAL);
        return textureId[0];
    }

    public final int getWidth() {
        return this.mBounds.right - this.mBounds.left;
    }

    public final GLContext glContext() {
        return this.mGLContext;
    }

    public final void invalidate() {
        this.mGLContext.requestRenderDelay(0);
    }

    public final boolean isAttached() {
        return isFlagSet(FLAG_ATTACHED);
    }

    final boolean isFlagSet(int bit) {
        return (this.mFlags & bit) == bit;
    }

    public final boolean isFocusable() {
        return isFlagSet(FLAG_FOCUSABLE);
    }

    public final boolean isFocused() {
        return isFlagSet(FLAG_FOCUSED);
    }

    public final boolean isInputEnable() {
        return isFlagSet(FLAG_INPUT_ENABLE);
    }

    public final boolean isScissorEnable() {
        return isFlagSet(FLAG_SCISSOR);
    }

    public final boolean isVisibility() {
        return isFlagSet(FLAG_VISIBLE);
    }

    public final void layout(int left, int top, int right, int bottom) {
        if (setBounds(left, top, right, bottom)) {
            unsetFlag(FLAG_LAYOUT_REQUESTED);
            onLayout(true);
        } else if (isFlagSet(FLAG_LAYOUT_REQUESTED)) {
            unsetFlag(FLAG_LAYOUT_REQUESTED);
            onLayout(false);
        }
        setFlag(FLAG_READYDRAW);
    }

    public final void layoutWH(int left, int top, int width, int height) {
        layout(left, top, left + width, top + height);
    }

    public final boolean offsetAscRect(GLView ascendant, Rect out) {
        GLView view = this;
        int dx = ZORDER_NORMAL;
        int dy = ZORDER_NORMAL;
        while (view != null) {
            if (ascendant == view) {
                out.offset(dx, dy);
                return true;
            } else {
                dx += view.mBounds.left;
                dy += view.mBounds.top;
                view = view.mParent;
            }
        }
        if (ascendant != null) {
            return false;
        }
        out.offset(dx, dy);
        return true;
    }

    protected void onFocusChanged(boolean getFocus) {
    }

    protected void onGLViewEvent(Event event) {
    }

    protected void onGetLayoutParam(LayoutParam lp) {
        lp.setUnit(ZORDER_NORMAL);
        lp.setValue(this.mBounds.left, this.mBounds.top, this.mBounds.right, this.mBounds.bottom);
    }

    protected boolean onKeyEvent(KeyEvent event) {
        return false;
    }

    protected void onLayout(boolean changeSize) {
        if (getChildCount() > 0) {
            fatalError("override onLayout to place your child views");
        }
    }

    protected void onRender(GLCanvas glCanvas, int left, int top) {
    }

    protected boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    protected void onViewAttached() {
    }

    protected void onViewDetached() {
    }

    protected boolean onVisibilityChanged(boolean visibility) {
        return true;
    }

    public final void removeAllView() {
        this.mGLContext.lockRenderThread();
        int i = ZORDER_NORMAL;
        int n = getChildCount();
        while (i < n) {
            removeOneView((GLView) this.mChildren.get(i));
            i++;
        }
        this.mChildren.clear();
        this.mGLContext.unlockRenderThread();
    }

    public final boolean removeView(GLView child) {
        this.mGLContext.lockRenderThread();
        if (this.mChildren == null) {
            this.mGLContext.unlockRenderThread();
            return false;
        } else if (this.mChildren.remove(child)) {
            removeOneView(child);
            this.mGLContext.unlockRenderThread();
            return true;
        } else {
            this.mGLContext.unlockRenderThread();
            return false;
        }
    }

    public void renderTree(GLCanvas glCanvas, int xoff, int yoff) {
        xoff += this.mBounds.left;
        yoff += this.mBounds.top;
        boolean scissorEnable = isScissorEnable();
        if (scissorEnable) {
            if (this.mClipRect == null) {
                this.mClipRect = new Rect();
            }
            getSurfaceRect(this.mClipRect);
            glCanvas.getGL().glScissor(this.mClipRect.left, glCanvas.getSurfaceHeight() - this.mClipRect.bottom, this.mClipRect.width(), this.mClipRect.height());
        }
        onRender(glCanvas, xoff, yoff);
        int i = ZORDER_NORMAL;
        int n = getChildCount();
        while (i < n) {
            GLView child = (GLView) this.mChildren.get(i);
            if (child.isFlagSet(16777217)) {
                child.renderTree(glCanvas, xoff, yoff);
            }
            i++;
        }
        if (scissorEnable) {
            glCanvas.getGL().glScissor(ZORDER_NORMAL, ZORDER_NORMAL, glCanvas.getSurfaceWidth(), glCanvas.getSurfaceHeight());
        }
    }

    public final boolean requestFocus() {
        return getCastRootView().changeFocusView(this, true);
    }

    public void requestLayout() {
        this.mFlags |= 2097152;
        if (this.mParent != null) {
            this.mParent.requestLayout();
        } else {
            fatalError("Call  parent view's addView firstly then add sub views." + this);
        }
    }

    final void setFlag(int bit) {
        this.mFlags |= bit;
    }

    public final void setFocusable(boolean enable) {
        if (enable) {
            setFlag(FLAG_FOCUSABLE);
        } else {
            unsetFlag(FLAG_FOCUSABLE);
        }
    }

    public final void setInputEnable(boolean enable) {
        if (enable) {
            setFlag(FLAG_INPUT_ENABLE);
        } else {
            unsetFlag(FLAG_INPUT_ENABLE);
        }
    }

    public final void setScissorEnable(boolean enable) {
        if (enable) {
            setFlag(FLAG_SCISSOR);
        } else {
            unsetFlag(FLAG_SCISSOR);
        }
    }

    public final void setVisibility(boolean visibility) {
        if (visibility != isFlagSet(FLAG_VISIBLE) && onVisibilityChanged(visibility)) {
            if (visibility) {
                clearFocus();
                setFlag(FLAG_VISIBLE);
            } else {
                unsetFlag(FLAG_VISIBLE);
            }
            invalidate();
        }
    }

    final void unsetFlag(int bit) {
        this.mFlags &= bit ^ -1;
    }
}