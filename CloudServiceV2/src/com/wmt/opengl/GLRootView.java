package com.wmt.opengl;

import android.view.KeyEvent;
import android.view.MotionEvent;
import com.wmt.opengl.GLView.Event;
import java.util.ArrayList;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.Type;

public class GLRootView extends GLView {
    private GLView mCurrentFocusView;
    public final ArrayList<GLView> mFocusStack;
    private boolean mFocusable;
    private GLView mLastKeyView;
    private GLPrivContext mPrivContext;

    public GLRootView(GLPrivContext privContext) {
        super(privContext, 4194305);
        this.mFocusStack = new ArrayList();
        this.mFocusable = false;
        this.mPrivContext = privContext;
        setFlag(25165824);
    }

    private GLView getFocusView(GLView parent) {
        if (parent == null) {
            return this.mCurrentFocusView;
        }
        int i = 0;
        while (i < parent.getChildCount()) {
            GLView glView = parent.getChild(i);
            if (glView != null && glView.isVisibility() && glView.getChildCount() != 0 && glView.isAttached()) {
                glView = getFocusView(glView);
            }
            if (glView != null && glView.isFlagSet(1048576) && glView.isVisibility() && glView.isAttached()) {
                return glView;
            }
            i++;
        }
        return this.mCurrentFocusView;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.wmt.opengl.GLView getStackTop() {
        throw new UnsupportedOperationException("Method not decompiled: com.wmt.opengl.GLRootView.getStackTop():com.wmt.opengl.GLView");
        /*
        r3 = this;
        r2 = r3.mFocusStack;
        r0 = r2.size();
        r1 = 0;
        if (r0 <= 0) goto L_0x001b;
    L_0x0009:
        r0 = r0 + -1;
        if (r0 < 0) goto L_0x001b;
    L_0x000d:
        r2 = r3.mFocusStack;
        r1 = r2.get(r0);
        r1 = (com.wmt.opengl.GLView) r1;
        r2 = r3.checkViewVisibility(r1);
        if (r2 == 0) goto L_0x001c;
    L_0x001b:
        return r1;
    L_0x001c:
        r1 = 0;
        goto L_0x0009;
        */
    }

    private boolean processKeyEventToPar(GLView glView, KeyEvent event) {
        if (glView == null || glView.getParent() == null) {
            return false;
        }
        if (!glView.getParent().onKeyEvent(event)) {
            return processKeyEventToPar(glView.getParent(), event);
        }
        this.mLastKeyView = glView.getParent();
        return true;
    }

    public boolean addFoucsView(GLView glView) {
        if (!glView.isFlagSet(JsonWriteContext.STATUS_EXPECT_VALUE)) {
            return false;
        }
        if (this.mFocusable) {
            glView.setFlag(1048576);
        }
        if (this.mCurrentFocusView != glView) {
            if (this.mCurrentFocusView != null) {
                this.mCurrentFocusView.unsetFlag(1048576);
                this.mCurrentFocusView.onFocusChanged(false);
                if (this.mFocusable) {
                    glView.onFocusChanged(true);
                }
                this.mCurrentFocusView = glView;
            } else {
                if (this.mFocusable) {
                    glView.onFocusChanged(true);
                }
                this.mCurrentFocusView = glView;
            }
            this.mFocusStack.remove(glView);
            this.mFocusStack.add(glView);
            return true;
        } else if (!this.mFocusable) {
            return true;
        } else {
            glView.onFocusChanged(true);
            return true;
        }
    }

    void boradcastGLViewEvent(GLView view, Event event) {
        this.mPrivContext.lockRenderThread();
        view.onGLViewEvent(event);
        int i = 0;
        int n = view.getChildCount();
        while (i < n) {
            boradcastGLViewEvent(view.getChild(i), event);
            i++;
        }
        this.mPrivContext.unlockRenderThread();
    }

    boolean changeFocusView(GLView glView, boolean requireFocus) {
        if (glView == null) {
            return false;
        }
        if (requireFocus) {
            addFoucsView(glView);
        } else {
            removeFoucsView(glView);
        }
        return true;
    }

    public boolean checkViewVisibility(GLView view) {
        while (view != null && view.isVisibility()) {
            if (view != this) {
                if (view.getParent() == null) {
                }
            }
            return true;
        }
        return false;
    }

    public final void disableReadyDraw(GLView view) {
        this.mPrivContext.lockRenderThread();
        view.unsetFlag(16777216);
        int i = 0;
        int n = view.getChildCount();
        while (i < n) {
            disableReadyDraw(view.getChild(i));
            i++;
        }
        this.mPrivContext.unlockRenderThread();
    }

    GLView getPopupGlView() {
        this.mPrivContext.lockRenderThread();
        int i = 0;
        int n = getChildCount();
        while (i < n) {
            GLView v = getChild(i);
            if (v.isFlagSet(Type.DOUBLE)) {
                this.mPrivContext.unlockRenderThread();
                return v;
            } else {
                i++;
            }
        }
        this.mPrivContext.unlockRenderThread();
        return null;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void onFocusChanged(boolean r4_getFocus) {
        throw new UnsupportedOperationException("Method not decompiled: com.wmt.opengl.GLRootView.onFocusChanged(boolean):void");
        /*
        r3 = this;
        r2 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;
        r3.mFocusable = r4;
        if (r4 == 0) goto L_0x0044;
    L_0x0006:
        r0 = r3.mCurrentFocusView;
        if (r0 == 0) goto L_0x0035;
    L_0x000a:
        r0 = r3.mCurrentFocusView;
        r0 = r0.isFocused();
        if (r0 != 0) goto L_0x0035;
    L_0x0012:
        r0 = r3.mCurrentFocusView;
        r0 = r3.checkViewVisibility(r0);
        if (r0 == 0) goto L_0x0035;
    L_0x001a:
        r0 = r3.mCurrentFocusView;
        r0 = r0.isAttached();
        if (r0 == 0) goto L_0x0035;
    L_0x0022:
        r0 = r3.mCurrentFocusView;
        if (r0 == 0) goto L_0x0031;
    L_0x0026:
        r0 = r3.mCurrentFocusView;
        r1 = 1;
        r0.onFocusChanged(r1);
        r0 = r3.mCurrentFocusView;
        r0.setFlag(r2);
    L_0x0031:
        r3.invalidate();
        return;
    L_0x0035:
        r0 = r3.getStackTop();
        r3.mCurrentFocusView = r0;
        if (r0 != 0) goto L_0x0022;
    L_0x003d:
        r0 = com.wmt.opengl.MoveFocus.getTopLeftView(r3);
        r3.mCurrentFocusView = r0;
        goto L_0x0022;
    L_0x0044:
        r0 = r3.mCurrentFocusView;
        if (r0 == 0) goto L_0x0031;
    L_0x0048:
        r0 = r3.mCurrentFocusView;
        r0 = r0.isFocused();
        if (r0 == 0) goto L_0x0031;
    L_0x0050:
        r0 = r3.mCurrentFocusView;
        r0.unsetFlag(r2);
        r0 = r3.mCurrentFocusView;
        r1 = 0;
        r0.onFocusChanged(r1);
        goto L_0x0031;
        */
    }

    protected void onLayout(boolean sizeChanged) {
        LayoutParam lp = new LayoutParam();
        this.mPrivContext.lockRenderThread();
        int i = 0;
        int n = getChildCount();
        while (i < n) {
            GLView v = getChild(i);
            v.onGetLayoutParam(lp);
            lp.convertToPixel(glContext().glCanvas());
            v.requestLayout();
            v.layout(lp.leftValue, lp.topValue, lp.rightValue, lp.bottomValue);
            i++;
        }
        this.mPrivContext.unlockRenderThread();
    }

    protected void onRender(GLCanvas glCanvas, int xoff, int yoff) {
    }

    void onViewAttached(GLView child) {
        child.setFlag(8388608);
    }

    void onViewDetached(GLView child) {
        child.unsetFlag(25165824);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean processKeyEvent(android.view.KeyEvent r11_event) {
        throw new UnsupportedOperationException("Method not decompiled: com.wmt.opengl.GLRootView.processKeyEvent(android.view.KeyEvent):boolean");
        /*
        r10 = this;
        r9 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;
        r7 = 0;
        r6 = 1;
        r0 = r11.getAction();
        r3 = r11.getKeyCode();
        r5 = r10.getPopupGlView();
        r2 = 0;
        if (r5 == 0) goto L_0x0024;
    L_0x0013:
        r8 = r5.isVisibility();
        if (r8 == 0) goto L_0x0024;
    L_0x0019:
        r5.onKeyEvent(r11);
        r10.mCurrentFocusView = r5;
        r10.mLastKeyView = r5;
        r10.invalidate();
    L_0x0023:
        return r6;
    L_0x0024:
        r8 = r11.getAction();
        if (r8 != r6) goto L_0x003f;
    L_0x002a:
        r8 = r10.mLastKeyView;
        if (r8 == 0) goto L_0x003d;
    L_0x002e:
        r7 = r10.mLastKeyView;
        r7 = r7.onKeyEvent(r11);
        if (r7 != 0) goto L_0x0023;
    L_0x0036:
        r6 = r10.mLastKeyView;
        r6 = r10.processKeyEventToPar(r6, r11);
        goto L_0x0023;
    L_0x003d:
        r6 = r7;
        goto L_0x0023;
    L_0x003f:
        r8 = 0;
        r10.mLastKeyView = r8;
        r8 = r10.mCurrentFocusView;
        if (r8 != 0) goto L_0x004e;
    L_0x0046:
        r8 = r10.getFocusView(r10);
        r10.mCurrentFocusView = r8;
        if (r8 == 0) goto L_0x005a;
    L_0x004e:
        r8 = r10.mCurrentFocusView;
        if (r8 == 0) goto L_0x00ae;
    L_0x0052:
        r8 = r10.mCurrentFocusView;
        r8 = r8.isVisibility();
        if (r8 != 0) goto L_0x00ae;
    L_0x005a:
        r6 = r10.mCurrentFocusView;
        if (r6 == 0) goto L_0x006b;
    L_0x005e:
        r6 = r10.mCurrentFocusView;
        r6 = r6.isVisibility();
        if (r6 != 0) goto L_0x006b;
    L_0x0066:
        r6 = r10.mCurrentFocusView;
        r10.removeFoucsView(r6);
    L_0x006b:
        r6 = 19;
        if (r3 == r6) goto L_0x007b;
    L_0x006f:
        r6 = 20;
        if (r3 == r6) goto L_0x007b;
    L_0x0073:
        r6 = 21;
        if (r3 == r6) goto L_0x007b;
    L_0x0077:
        r6 = 22;
        if (r3 != r6) goto L_0x009f;
    L_0x007b:
        r6 = com.wmt.opengl.MoveFocus.getTopLeftView(r10);
        r10.mCurrentFocusView = r6;
        r6 = r10.mCurrentFocusView;
        if (r6 == 0) goto L_0x009f;
    L_0x0085:
        r6 = r10.mCurrentFocusView;
        r6.setFlag(r9);
        r6 = r10.mCurrentFocusView;
        r6.onKeyEvent(r11);
        r6 = r10.mCurrentFocusView;
        r6.onFocusChanged(r7);
        r6 = r10.mFocusStack;
        r7 = r10.mCurrentFocusView;
        r6.add(r7);
        r2 = 1;
        r10.invalidate();
    L_0x009f:
        r6 = r10.mCurrentFocusView;
        r10.mLastKeyView = r6;
    L_0x00a3:
        r6 = r10.mCurrentFocusView;
        if (r6 != 0) goto L_0x00a8;
    L_0x00a7:
        r2 = 0;
    L_0x00a8:
        r10.invalidate();
        r6 = r2;
        goto L_0x0023;
    L_0x00ae:
        r8 = r10.mCurrentFocusView;
        if (r8 == 0) goto L_0x00c8;
    L_0x00b2:
        r8 = r10.mCurrentFocusView;
        r8 = r8.isFocused();
        if (r8 != 0) goto L_0x00c8;
    L_0x00ba:
        r8 = r10.mCurrentFocusView;
        r8 = r8.isAttached();
        if (r8 != 0) goto L_0x00c8;
    L_0x00c2:
        r8 = r10.getFocusView(r10);
        r10.mCurrentFocusView = r8;
    L_0x00c8:
        r8 = r10.mCurrentFocusView;
        r10.mLastKeyView = r8;
        r8 = r10.mCurrentFocusView;
        if (r8 == 0) goto L_0x0146;
    L_0x00d0:
        r8 = r10.mCurrentFocusView;
        r8 = r8.onKeyEvent(r11);
        if (r8 != 0) goto L_0x0146;
    L_0x00d8:
        r4 = 0;
        r1 = -1;
        switch(r3) {
            case 19: goto L_0x0111;
            case 20: goto L_0x0113;
            case 21: goto L_0x0115;
            case 22: goto L_0x0117;
            default: goto L_0x00dd;
        };
    L_0x00dd:
        r8 = -1;
        if (r1 == r8) goto L_0x013e;
    L_0x00e0:
        if (r0 != 0) goto L_0x013e;
    L_0x00e2:
        r8 = r10.mCurrentFocusView;
        r4 = com.wmt.opengl.MoveFocus.nextFocus(r8, r10, r1);
        if (r4 == 0) goto L_0x0119;
    L_0x00ea:
        r8 = r10.mCurrentFocusView;
        r8.unsetFlag(r9);
        r8 = r10.mCurrentFocusView;
        r8.onFocusChanged(r7);
        r7 = r10.mFocusStack;
        r8 = r10.mCurrentFocusView;
        r7.remove(r8);
        r4.setFlag(r9);
        r10.mCurrentFocusView = r4;
        r7 = r10.mCurrentFocusView;
        r7.onFocusChanged(r6);
        r6 = r10.mFocusStack;
        r7 = r10.mCurrentFocusView;
        r6.add(r7);
        r2 = 1;
        r10.invalidate();
        goto L_0x00a3;
    L_0x0111:
        r1 = 2;
        goto L_0x00dd;
    L_0x0113:
        r1 = 3;
        goto L_0x00dd;
    L_0x0115:
        r1 = 0;
        goto L_0x00dd;
    L_0x0117:
        r1 = 1;
        goto L_0x00dd;
    L_0x0119:
        r2 = 0;
        r6 = r10.toString();
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "fail to get next focus, current focus is ";
        r7 = r7.append(r8);
        r8 = r10.mCurrentFocusView;
        r7 = r7.append(r8);
        r8 = "!!";
        r7 = r7.append(r8);
        r7 = r7.toString();
        com.wmt.util.Log.e(r6, r7);
        goto L_0x00a3;
    L_0x013e:
        r6 = r10.mCurrentFocusView;
        r2 = r10.processKeyEventToPar(r6, r11);
        goto L_0x00a3;
    L_0x0146:
        r2 = 1;
        goto L_0x00a3;
        */
    }

    protected boolean processTouchEvent(MotionEvent event) {
        this.mPrivContext.lockRenderThread();
        if (event.getAction() == 0) {
            int i = getChildCount() - 1;
            while (i >= 0) {
                GLView child = getChild(i);
                if (child.isFlagSet(JsonWriteContext.STATUS_OK_AFTER_SPACE) && child.isFlagSet(Type.DOUBLE)) {
                    if (this.mMotionTarget != null) {
                        this.mMotionTarget.dispatchCancelEvent();
                        this.mMotionTarget = null;
                    }
                    if (child.mBounds.contains((int) event.getX(), (int) event.getY())) {
                        this.mMotionTarget = child;
                        break;
                    } else {
                        removeView(child);
                        invalidate();
                    }
                } else {
                    i--;
                }
            }
        }
        boolean dispatchTouchEvent = dispatchTouchEvent(event);
        this.mPrivContext.unlockRenderThread();
        return dispatchTouchEvent;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void removeFoucsView(com.wmt.opengl.GLView r5_glView) {
        throw new UnsupportedOperationException("Method not decompiled: com.wmt.opengl.GLRootView.removeFoucsView(com.wmt.opengl.GLView):void");
        /*
        r4 = this;
        r3 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;
        r2 = r5.isFlagSet(r3);
        if (r2 == 0) goto L_0x000f;
    L_0x0008:
        r5.unsetFlag(r3);
        r2 = 0;
        r5.onFocusChanged(r2);
    L_0x000f:
        r2 = r4.mFocusStack;
        r2 = r2.remove(r5);
        if (r2 == 0) goto L_0x0043;
    L_0x0017:
        r2 = r4.mCurrentFocusView;
        if (r2 != r5) goto L_0x0043;
    L_0x001b:
        r2 = r4.mFocusStack;
        r0 = r2.size();
        r1 = 0;
        if (r0 <= 0) goto L_0x0044;
    L_0x0024:
        r0 = r0 + -1;
        if (r0 < 0) goto L_0x0044;
    L_0x0028:
        r2 = r4.mFocusStack;
        r1 = r2.get(r0);
        r1 = (com.wmt.opengl.GLView) r1;
        r2 = r4.checkViewVisibility(r1);
        if (r2 == 0) goto L_0x0024;
    L_0x0036:
        r2 = r4.mFocusable;
        if (r2 == 0) goto L_0x0041;
    L_0x003a:
        r1.setFlag(r3);
        r2 = 1;
        r1.onFocusChanged(r2);
    L_0x0041:
        r4.mCurrentFocusView = r1;
    L_0x0043:
        return;
    L_0x0044:
        r2 = 0;
        r4.mCurrentFocusView = r2;
        goto L_0x0043;
        */
    }

    public final void requestAllLayout(GLView view) {
        this.mPrivContext.lockRenderThread();
        view.setFlag(2097152);
        int i = 0;
        int n = view.getChildCount();
        while (i < n) {
            requestAllLayout(view.getChild(i));
            i++;
        }
        this.mPrivContext.unlockRenderThread();
    }

    public void requestLayout() {
        requestAllLayout(this);
        this.mPrivContext.requestRootLayout();
    }
}