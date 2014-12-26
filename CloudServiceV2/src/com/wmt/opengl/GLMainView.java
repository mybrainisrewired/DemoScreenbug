package com.wmt.opengl;

import android.content.Context;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.wmt.opengl.GLContext.GLIdleListener;
import com.wmt.opengl.GLView.Event;
import com.wmt.util.Deque;
import com.wmt.util.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

public class GLMainView extends GLSurfaceView implements Renderer, GLPrivContext {
    private static final int EVENT_KEY = 2;
    private static final int FLAG_INITED = 4;
    private static final int FLAG_NEED_LAYOUT = 2;
    private static final int FLAG_TOUCH_MODE = 1;
    private static final String TAG = "GLMainView";
    static Comparator<RunnableItem> sComparator;
    private final Deque<Event> mEventQueue;
    StringTexture mFPStexture;
    private int mFlags;
    private int mFrameCount;
    private long mFrameCountingStart;
    private int mFrameNumber;
    private long mFrameTime;
    private final GLCanvas mGLCanvas;
    private Handler mHandler;
    private final LinkedList<GLIdleListener> mIdleListeners;
    private boolean mInDownState;
    private boolean mInDrawFrameFunc;
    private boolean mIsEmulator;
    private long mLastDrawFinishTime;
    private long mLastDrawFrameTime;
    private final Perspective mPerspective;
    private final ReentrantLock mRenderLock;
    private boolean mRenderRequested;
    private GLRootView mRootView;
    private ArrayList<RunnableItem> mRunnableQueue;
    private TextureManager mTextureManager;
    private GLToast mToast;

    static final class RunnableItem {
        long occurTime;
        Runnable run;

        RunnableItem() {
        }
    }

    static {
        sComparator = new Comparator<RunnableItem>() {
            public int compare(RunnableItem a, RunnableItem b) {
                if (a.occurTime > b.occurTime) {
                    return FLAG_TOUCH_MODE;
                }
                return a.occurTime < b.occurTime ? -1 : 0;
            }
        };
    }

    public GLMainView(Context context) {
        super(context);
        this.mEventQueue = new Deque();
        this.mIdleListeners = new LinkedList();
        this.mRenderRequested = false;
        this.mLastDrawFrameTime = 0;
        this.mFrameTime = 0;
        this.mFrameCountingStart = 0;
        this.mPerspective = new Perspective();
        this.mGLCanvas = new GLCanvas();
        this.mHandler = new Handler();
        this.mRunnableQueue = new ArrayList();
        this.mRenderLock = new ReentrantLock();
        this.mIsEmulator = Utils.isEmulator();
        this.mFrameNumber = 0;
        init(context);
    }

    public GLMainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mEventQueue = new Deque();
        this.mIdleListeners = new LinkedList();
        this.mRenderRequested = false;
        this.mLastDrawFrameTime = 0;
        this.mFrameTime = 0;
        this.mFrameCountingStart = 0;
        this.mPerspective = new Perspective();
        this.mGLCanvas = new GLCanvas();
        this.mHandler = new Handler();
        this.mRunnableQueue = new ArrayList();
        this.mRenderLock = new ReentrantLock();
        this.mIsEmulator = Utils.isEmulator();
        this.mFrameNumber = 0;
        init(context);
    }

    private void onDrawFrameLocked(GL10 gl10) {
        GL11 gl = (GL11) gl10;
        this.mTextureManager.bindGL(gl);
        idleListenerRunLocked();
        this.mInDrawFrameFunc = true;
        this.mRenderRequested = false;
        int w = getWidth();
        int h = getHeight();
        synchronized (this.mRunnableQueue) {
            while (!this.mRunnableQueue.isEmpty()) {
                RunnableItem r = (RunnableItem) this.mRunnableQueue.get(0);
                if (r.occurTime <= SystemClock.uptimeMillis()) {
                    r.run.run();
                    this.mRunnableQueue.remove(0);
                }
            }
            break;
        }
        if ((this.mFlags & 2) != 0) {
            this.mRootView.requestLayout();
            this.mRootView.layout(0, 0, w, h);
            this.mFlags &= -3;
        }
        long realdt = SystemClock.uptimeMillis() - this.mLastDrawFrameTime;
        this.mLastDrawFrameTime = SystemClock.uptimeMillis();
        if (this.mFrameNumber >= 5) {
            int delay = this.mIsEmulator ? 50000 : Opcodes.V1_6;
            if (this.mTextureManager.checkLoadedQueue(realdt < ((long) delay) ? FLAG_NEED_LAYOUT : JsonWriteContext.STATUS_OK_AFTER_SPACE)) {
                requestRender();
            }
        } else if (this.mTextureManager.checkLoadedQueue(FLAG_TOUCH_MODE)) {
            requestRender();
        } else {
            requestRender();
        }
        this.mFrameNumber++;
        processGLViewEvent();
        gl.glMatrixMode(5888);
        gl.glClear(16640);
        gl.glLoadIdentity();
        gl.glEnable(3089);
        gl.glScissor(0, 0, w, h);
        gl.glEnable(3042);
        this.mRootView.renderTree(this.mGLCanvas, 0, 0);
        gl.glDisable(3042);
        synchronized (this.mRunnableQueue) {
            if (!this.mRunnableQueue.isEmpty()) {
                requestRenderDelay(((RunnableItem) this.mRunnableQueue.get(0)).occurTime - SystemClock.uptimeMillis());
            }
        }
        if (!(this.mRenderRequested || !this.mTextureManager.isLoadThreadIdle() || this.mIdleListeners.isEmpty())) {
            super.requestRender();
        }
        this.mTextureManager.bindGL(null);
        this.mInDrawFrameFunc = false;
    }

    private void outputFps() {
        long now = System.nanoTime();
        if (this.mFrameCountingStart == 0) {
            this.mFrameCountingStart = now;
        } else if (now - this.mFrameCountingStart > 1000000000) {
            this.mFPStexture = new StringTexture("FPS:" + (((double) ((int) (((((double) this.mFrameCount) * 1.0E9d) / ((double) (now - this.mFrameCountingStart))) * 10.0d))) / 10.0d));
            this.mFrameCountingStart = now;
            this.mFrameCount = 0;
        }
        if (this.mFPStexture != null) {
            this.mGLCanvas.draw2D(this.mFPStexture, 2.0f, 40.0f);
        }
        this.mFrameCount++;
    }

    private void processGLViewEvent() {
        synchronized (this.mEventQueue) {
            while (true) {
                Event event = (Event) this.mEventQueue.pollFirst();
                if (event != null) {
                    this.mRootView.boradcastGLViewEvent(this.mRootView, event);
                }
            }
        }
    }

    public void addIdleListener(GLIdleListener listener) {
        this.mRenderLock.lock();
        this.mIdleListeners.addLast(listener);
        requestRender();
        this.mRenderLock.unlock();
    }

    public void boradcastEvent(Event event) {
        synchronized (this.mEventQueue) {
            this.mEventQueue.addLast(event);
            requestRender();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean canDisable = true;
        this.mRenderLock.lock();
        if (this.mRootView != null) {
            canDisable = this.mRootView.processKeyEvent(event);
        }
        if (canDisable) {
            this.mRenderLock.unlock();
            return canDisable;
        } else {
            this.mRenderLock.unlock();
            return false;
        }
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean handled = false;
        int action = event.getAction();
        if (action == 3 || action == 1) {
            this.mInDownState = false;
        } else if (!(this.mInDownState || action == 0)) {
            return handled;
        }
        this.mRenderLock.lock();
        if (this.mRootView != null && this.mRootView.processTouchEvent(event)) {
            handled = true;
        }
        if (action == 0 && handled) {
            this.mInDownState = true;
        }
        this.mRenderLock.unlock();
        return handled;
    }

    public int getMaxFrameRate() {
        return this.mFrameTime != 0 ? (int) (1000 / this.mFrameTime) : 60;
    }

    public Perspective getPerspective() {
        return this.mPerspective;
    }

    public TextureManager getTextureManager() {
        return this.mTextureManager;
    }

    public GLToast getToast() {
        return this.mToast;
    }

    public GLCanvas glCanvas() {
        return this.mGLCanvas;
    }

    public GLView glRootView() {
        return this.mRootView;
    }

    void idleListenerRunLocked() {
        if (!this.mRenderRequested && this.mTextureManager.isLoadThreadIdle() && !this.mIdleListeners.isEmpty()) {
            GLIdleListener listener = (GLIdleListener) this.mIdleListeners.removeFirst();
            boolean ret = listener.onGLIdle(this, this.mGLCanvas);
            Log.i(TAG, "OnGLIdea for listener:" + listener + ", ret=" + ret);
            if (ret) {
                this.mIdleListeners.addLast(listener);
                requestRender();
            } else if (!this.mIdleListeners.isEmpty()) {
                requestRender();
            }
        }
    }

    protected void init(Context context) {
        this.mRootView = new GLRootView(this);
        this.mToast = new GLToast(this);
        this.mRootView.addView(this.mToast, Opcodes.FDIV);
        setBackgroundDrawable(null);
        setEGLConfigChooser(JsonWriteContext.STATUS_EXPECT_NAME, FragmentManagerImpl.ANIM_STYLE_FADE_EXIT, 5, 0, Segment.TOKENS_PER_SEGMENT, 0);
        getHolder().setFormat(FLAG_INITED);
        this.mTextureManager = new TextureManager(this);
        this.mGLCanvas.setTextureManager(this.mTextureManager);
        setRenderer(this);
        setRenderMode(0);
        setFocusable(true);
        setFocusableInTouchMode(false);
    }

    public boolean isInTouchMode() {
        return (this.mFlags & 1) != 0;
    }

    public void lockRenderThread() {
        this.mRenderLock.lock();
    }

    protected void onDetachedFromWindow() {
        Log.d(TAG, "onDetachedFromWindow");
        synchronized (this.mEventQueue) {
            this.mEventQueue.clear();
        }
        synchronized (this.mTextureManager) {
            this.mTextureManager.clear();
        }
        super.onDetachedFromWindow();
    }

    public void onDrawFrame(GL10 gl) {
        Utils.checkGLError(gl);
        this.mRenderLock.lock();
        onDrawFrameLocked(gl);
        this.mRenderLock.unlock();
        Utils.checkGLError(gl);
        if (this.mFrameTime > 0) {
            long end = SystemClock.uptimeMillis();
            if (this.mLastDrawFinishTime != 0) {
                long wait = this.mLastDrawFinishTime + this.mFrameTime - end;
                if (wait > 0) {
                    SystemClock.sleep(wait);
                }
            }
            this.mLastDrawFinishTime = SystemClock.uptimeMillis();
        }
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        this.mRootView.onFocusChanged(gainFocus);
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    public void onLoadThreadIdle() {
        if (!this.mIdleListeners.isEmpty()) {
            super.requestRender();
        }
    }

    public void onSurfaceChanged(GL10 gl1, int width, int height) {
        GL11 gl = (GL11) gl1;
        Utils.checkGLError(gl);
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(5889);
        gl.glLoadIdentity();
        this.mPerspective.fovy = 45.0f;
        this.mPerspective.aspect = ((float) width) / ((float) height);
        this.mPerspective.zNear = 0.1f;
        this.mPerspective.zFar = 100.0f;
        this.mGLCanvas.setContext(gl, this.mTextureManager, this.mPerspective, width, height);
        GLU.gluPerspective(gl, this.mPerspective.fovy, this.mPerspective.aspect, this.mPerspective.zNear, this.mPerspective.zFar);
        gl.glMatrixMode(5888);
        requestRootLayout();
        this.mRootView.disableReadyDraw(this.mRootView);
        this.mFrameNumber = 0;
        Utils.checkGLError(gl);
    }

    public void onSurfaceCreated(GL10 gl1, EGLConfig config) {
        GL11 gl = (GL11) gl1;
        Utils.checkGLError(gl);
        Log.d(TAG, "-------->surface created ");
        gl.glEnable(3024);
        gl.glDisable(2896);
        gl.glEnable(3553);
        gl.glTexEnvf(8960, 8704, 7681.0f);
        gl.glEnableClientState(32884);
        gl.glEnableClientState(32888);
        gl.glEnable(2929);
        gl.glDepthFunc(515);
        gl.glBlendFunc(FLAG_TOUCH_MODE, 771);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepthf(1.0f);
        this.mTextureManager.resetTextures();
        this.mFrameNumber = 0;
        this.mInDownState = false;
        this.mFlags |= 4;
        Utils.checkGLError(gl);
    }

    public void postRunnable(Runnable r, long ms) {
        RunnableItem item = new RunnableItem();
        item.run = r;
        item.occurTime = SystemClock.uptimeMillis() + ms;
        synchronized (this.mRunnableQueue) {
            this.mRunnableQueue.add(item);
            Collections.sort(this.mRunnableQueue, sComparator);
            requestRenderDelay(ms);
        }
    }

    public boolean postUIThreadRunnable(Runnable r, long delayMillis) {
        return this.mHandler.postDelayed(r, delayMillis);
    }

    public void removeRunnable(Runnable r) {
        synchronized (this.mRunnableQueue) {
            int i = 0;
            int n = this.mRunnableQueue.size();
            while (i < n) {
                if (((RunnableItem) this.mRunnableQueue.get(i)).run == r) {
                    this.mRunnableQueue.remove(i);
                    break;
                } else {
                    i++;
                }
            }
        }
    }

    public void requestRender() {
        if (!this.mRenderRequested) {
            this.mRenderRequested = true;
            super.requestRender();
        }
    }

    public void requestRenderDelay(long ms) {
        if (ms <= 0) {
            requestRender();
        } else {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    GLMainView.this.requestRender();
                }
            }, ms);
        }
    }

    public void requestRootLayout() {
        this.mRenderLock.lock();
        if ((this.mFlags & 2) != 0) {
            this.mRenderLock.unlock();
        } else {
            this.mFlags |= 2;
            if ((this.mFlags & 4) != 0) {
                requestRender();
            }
            this.mRenderLock.unlock();
        }
    }

    public boolean runInGLThread() {
        return this.mInDrawFrameFunc;
    }

    public void setMaxFrameRate(int fps) {
        if (fps == 0) {
            this.mFrameTime = 0;
        } else {
            this.mFrameTime = (long) (1000.0d / ((double) fps));
        }
    }

    public Context uiContext() {
        return getContext();
    }

    public void unlockRenderThread() {
        this.mRenderLock.unlock();
    }
}