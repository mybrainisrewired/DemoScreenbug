package com.wmt.opengl;

import com.wmt.media.ImageMan;

public final class GLBuffer {

    static class Buffer {
        final int mNativePtr;

        Buffer() {
            this.mNativePtr = GLBuffer.glbuffer_init();
        }

        protected void finalize() throws Throwable {
            GLBuffer.glbuffer_term(this.mNativePtr);
            super.finalize();
        }
    }

    public static final class FloatBuffer extends Buffer {
        public void glColorPointer(int size) {
            GLBuffer.glbuffer_setColor(this.mNativePtr, size);
        }

        public void glTexturePointer() {
            GLBuffer.glbuffer_setTexture(this.mNativePtr);
        }

        public void glVertexPointer(int vertexSize) {
            GLBuffer.glbuffer_setVertex(this.mNativePtr, vertexSize);
        }

        public void put(float[] data) {
            GLBuffer.glbuffer_putFloatArray(this.mNativePtr, data, 0, data.length);
        }

        public void put(float[] data, int off, int length) {
            GLBuffer.glbuffer_putFloatArray(this.mNativePtr, data, off, length);
        }
    }

    static {
        ImageMan.touch();
    }

    static native int glbuffer_init();

    static native int glbuffer_putFloatArray(int i, float[] fArr, int i2, int i3);

    static native void glbuffer_setColor(int i, int i2);

    static native void glbuffer_setTexture(int i);

    static native void glbuffer_setVertex(int i, int i2);

    static native void glbuffer_term(int i);
}