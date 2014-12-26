package com.wmt.opengl;

import android.content.Context;
import com.wmt.opengl.GLView.Event;

public interface GLContext {

    public static interface GLIdleListener {
        boolean onGLIdle(GLContext gLContext, GLCanvas gLCanvas);
    }

    void addIdleListener(GLIdleListener gLIdleListener);

    void boradcastEvent(Event event);

    Perspective getPerspective();

    TextureManager getTextureManager();

    GLToast getToast();

    GLCanvas glCanvas();

    GLView glRootView();

    boolean isInTouchMode();

    void lockRenderThread();

    void postRunnable(Runnable runnable, long j);

    boolean postUIThreadRunnable(Runnable runnable, long j);

    void removeRunnable(Runnable runnable);

    void requestRenderDelay(long j);

    boolean runInGLThread();

    Context uiContext();

    void unlockRenderThread();
}