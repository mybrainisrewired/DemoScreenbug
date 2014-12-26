package com.wmt.opengl;

public interface Animation {
    boolean isValid();

    void updateApply(GLCanvas gLCanvas, long j);
}