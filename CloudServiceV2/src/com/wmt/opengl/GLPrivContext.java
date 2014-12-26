package com.wmt.opengl;

public interface GLPrivContext extends GLContext {
    void onLoadThreadIdle();

    void requestRootLayout();
}