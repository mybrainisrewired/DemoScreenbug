package com.android.systemui;

import android.app.ActivityManager;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.SystemProperties;
import android.renderscript.Matrix4f;
import android.service.wallpaper.WallpaperService;
import android.service.wallpaper.WallpaperService.Engine;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import com.android.systemui.statusbar.CommandQueue;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;

public class ImageWallpaper extends WallpaperService {
    private static final boolean DEBUG = false;
    static final boolean FIXED_SIZED_SURFACE = true;
    private static final String GL_LOG_TAG = "ImageWallpaperGL";
    private static final String PROPERTY_KERNEL_QEMU = "ro.kernel.qemu";
    private static final String TAG = "ImageWallpaper";
    static final boolean USE_OPENGL = true;
    boolean mIsHwAccelerated;
    WallpaperManager mWallpaperManager;

    class DrawableEngine extends Engine {
        static final int EGL_CONTEXT_CLIENT_VERSION = 12440;
        static final int EGL_OPENGL_ES2_BIT = 4;
        private static final int FLOAT_SIZE_BYTES = 4;
        private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
        private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 20;
        private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 3;
        private static final String sSimpleFS = "precision mediump float;\n\nvarying vec2 outTexCoords;\nuniform sampler2D texture;\n\nvoid main(void) {\n    gl_FragColor = texture2D(texture, outTexCoords);\n}\n\n";
        private static final String sSimpleVS = "attribute vec4 position;\nattribute vec2 texCoords;\nvarying vec2 outTexCoords;\nuniform mat4 projection;\n\nvoid main(void) {\n    outTexCoords = texCoords;\n    gl_Position = projection * position;\n}\n\n";
        Bitmap mBackground;
        int mBackgroundHeight;
        int mBackgroundWidth;
        private EGL10 mEgl;
        private EGLConfig mEglConfig;
        private EGLContext mEglContext;
        private EGLDisplay mEglDisplay;
        private EGLSurface mEglSurface;
        private GL mGL;
        int mLastXTranslation;
        int mLastYTranslation;
        private final Object mLock;
        boolean mOffsetsChanged;
        private WallpaperObserver mReceiver;
        boolean mRedrawNeeded;
        boolean mVisible;
        float mXOffset;
        float mYOffset;

        class WallpaperObserver extends BroadcastReceiver {
            WallpaperObserver() {
            }

            public void onReceive(Context context, Intent intent) {
                synchronized (DrawableEngine.this.mLock) {
                    DrawableEngine drawableEngine = DrawableEngine.this;
                    DrawableEngine.this.mBackgroundHeight = -1;
                    drawableEngine.mBackgroundWidth = -1;
                    DrawableEngine.this.mBackground = null;
                    DrawableEngine.this.mRedrawNeeded = true;
                    DrawableEngine.this.drawFrameLocked();
                }
            }
        }

        public DrawableEngine() {
            super(ImageWallpaper.this);
            this.mLock = new Object[0];
            this.mBackgroundWidth = -1;
            this.mBackgroundHeight = -1;
            this.mVisible = true;
            setFixedSizeAllowed(USE_OPENGL);
        }

        private int buildProgram(String vertex, String fragment) {
            int vertexShader = buildShader(vertex, 35633);
            if (vertexShader == 0) {
                return 0;
            }
            int fragmentShader = buildShader(fragment, 35632);
            if (fragmentShader == 0) {
                return 0;
            }
            int program = GLES20.glCreateProgram();
            GLES20.glAttachShader(program, vertexShader);
            checkGlError();
            GLES20.glAttachShader(program, fragmentShader);
            checkGlError();
            GLES20.glLinkProgram(program);
            checkGlError();
            int[] status = new int[1];
            GLES20.glGetProgramiv(program, 35714, status, TRIANGLE_VERTICES_DATA_POS_OFFSET);
            if (status[0] == 1) {
                return program;
            }
            Log.d(GL_LOG_TAG, "Error while linking program:\n" + GLES20.glGetProgramInfoLog(program));
            GLES20.glDeleteShader(vertexShader);
            GLES20.glDeleteShader(fragmentShader);
            GLES20.glDeleteProgram(program);
            return 0;
        }

        private int buildShader(String source, int type) {
            int shader = GLES20.glCreateShader(type);
            GLES20.glShaderSource(shader, source);
            checkGlError();
            GLES20.glCompileShader(shader);
            checkGlError();
            int[] status = new int[1];
            GLES20.glGetShaderiv(shader, 35713, status, TRIANGLE_VERTICES_DATA_POS_OFFSET);
            if (status[0] == 1) {
                return shader;
            }
            Log.d(GL_LOG_TAG, "Error while compiling shader:\n" + GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            return 0;
        }

        private void checkEglError() {
            int error = this.mEgl.eglGetError();
            if (error != 12288) {
                Log.w(GL_LOG_TAG, "EGL error = " + GLUtils.getEGLErrorString(error));
            }
        }

        private void checkGlError() {
            int error = GLES20.glGetError();
            if (error != 0) {
                Log.w(GL_LOG_TAG, "GL error = 0x" + Integer.toHexString(error), new Throwable());
            }
        }

        private EGLConfig chooseEglConfig() {
            int[] configsCount = new int[1];
            EGLConfig[] configs = new EGLConfig[1];
            if (this.mEgl.eglChooseConfig(this.mEglDisplay, getConfig(), configs, 1, configsCount)) {
                return configsCount[0] > 0 ? configs[0] : null;
            } else {
                throw new IllegalArgumentException("eglChooseConfig failed " + GLUtils.getEGLErrorString(this.mEgl.eglGetError()));
            }
        }

        private FloatBuffer createMesh(int left, int top, float right, float bottom) {
            float[] verticesData = new float[]{(float) left, bottom, 0.0f, 0.0f, 1.0f, right, bottom, 0.0f, 1.0f, 1.0f, (float) left, (float) top, 0.0f, 0.0f, 0.0f, right, (float) top, 0.0f, 1.0f, 0.0f};
            FloatBuffer triangleVertices = ByteBuffer.allocateDirect(verticesData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            triangleVertices.put(verticesData).position(TRIANGLE_VERTICES_DATA_POS_OFFSET);
            return triangleVertices;
        }

        private void drawWallpaperWithCanvas(SurfaceHolder sh, int w, int h, int x, int y) {
            Canvas c = sh.lockCanvas();
            if (c != null) {
                c.translate((float) x, (float) y);
                if (w < 0 || h < 0) {
                    c.save(CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL);
                    c.clipRect(0.0f, 0.0f, (float) this.mBackgroundWidth, (float) this.mBackgroundHeight, Op.DIFFERENCE);
                    c.drawColor(-16777216);
                    c.restore();
                }
                if (this.mBackground != null) {
                    c.drawBitmap(this.mBackground, 0.0f, 0.0f, null);
                }
                sh.unlockCanvasAndPost(c);
            }
        }

        private boolean drawWallpaperWithOpenGL(SurfaceHolder sh, int w, int h, int left, int top) {
            if (!initGL(sh)) {
                return DEBUG;
            }
            float right = (float) (this.mBackgroundWidth + left);
            float bottom = (float) (this.mBackgroundHeight + top);
            Rect frame = sh.getSurfaceFrame();
            Matrix4f ortho = new Matrix4f();
            ortho.loadOrtho(0.0f, (float) frame.width(), (float) frame.height(), 0.0f, -1.0f, 1.0f);
            Buffer triangleVertices = createMesh(left, top, right, bottom);
            int texture = loadTexture(this.mBackground);
            int program = buildProgram(sSimpleVS, sSimpleFS);
            int attribPosition = GLES20.glGetAttribLocation(program, "position");
            int attribTexCoords = GLES20.glGetAttribLocation(program, "texCoords");
            int uniformTexture = GLES20.glGetUniformLocation(program, "texture");
            int uniformProjection = GLES20.glGetUniformLocation(program, "projection");
            checkGlError();
            GLES20.glViewport(TRIANGLE_VERTICES_DATA_POS_OFFSET, TRIANGLE_VERTICES_DATA_POS_OFFSET, frame.width(), frame.height());
            GLES20.glBindTexture(3553, texture);
            GLES20.glUseProgram(program);
            GLES20.glEnableVertexAttribArray(attribPosition);
            GLES20.glEnableVertexAttribArray(attribTexCoords);
            GLES20.glUniform1i(uniformTexture, TRIANGLE_VERTICES_DATA_POS_OFFSET);
            GLES20.glUniformMatrix4fv(uniformProjection, 1, DEBUG, ortho.getArray(), TRIANGLE_VERTICES_DATA_POS_OFFSET);
            checkGlError();
            if (w < 0 || h < 0) {
                GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                GLES20.glClear(16384);
            }
            triangleVertices.position(TRIANGLE_VERTICES_DATA_POS_OFFSET);
            GLES20.glVertexAttribPointer(attribPosition, TRIANGLE_VERTICES_DATA_UV_OFFSET, 5126, DEBUG, TRIANGLE_VERTICES_DATA_STRIDE_BYTES, triangleVertices);
            triangleVertices.position(TRIANGLE_VERTICES_DATA_UV_OFFSET);
            GLES20.glVertexAttribPointer(attribTexCoords, TRIANGLE_VERTICES_DATA_UV_OFFSET, 5126, DEBUG, TRIANGLE_VERTICES_DATA_STRIDE_BYTES, triangleVertices);
            GLES20.glDrawArrays(5, TRIANGLE_VERTICES_DATA_POS_OFFSET, FLOAT_SIZE_BYTES);
            if (this.mEgl.eglSwapBuffers(this.mEglDisplay, this.mEglSurface)) {
                checkEglError();
                finishGL();
                return USE_OPENGL;
            } else {
                throw new RuntimeException("Cannot swap buffers");
            }
        }

        private void finishGL() {
            this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            this.mEgl.eglDestroySurface(this.mEglDisplay, this.mEglSurface);
            this.mEgl.eglDestroyContext(this.mEglDisplay, this.mEglContext);
        }

        private int[] getConfig() {
            return new int[]{12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 0, 12325, 0, 12326, 0, 12344};
        }

        private boolean initGL(SurfaceHolder surfaceHolder) {
            this.mEgl = (EGL10) EGLContext.getEGL();
            this.mEglDisplay = this.mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            if (this.mEglDisplay == EGL10.EGL_NO_DISPLAY) {
                throw new RuntimeException("eglGetDisplay failed " + GLUtils.getEGLErrorString(this.mEgl.eglGetError()));
            }
            if (this.mEgl.eglInitialize(this.mEglDisplay, new int[2])) {
                this.mEglConfig = chooseEglConfig();
                if (this.mEglConfig == null) {
                    throw new RuntimeException("eglConfig not initialized");
                }
                this.mEglContext = createContext(this.mEgl, this.mEglDisplay, this.mEglConfig);
                this.mEglSurface = this.mEgl.eglCreateWindowSurface(this.mEglDisplay, this.mEglConfig, surfaceHolder, null);
                if (this.mEglSurface == null || this.mEglSurface == EGL10.EGL_NO_SURFACE) {
                    int error = this.mEgl.eglGetError();
                    if (error == 12299) {
                        Log.e(GL_LOG_TAG, "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
                        return DEBUG;
                    } else {
                        throw new RuntimeException("createWindowSurface failed " + GLUtils.getEGLErrorString(error));
                    }
                } else if (this.mEgl.eglMakeCurrent(this.mEglDisplay, this.mEglSurface, this.mEglSurface, this.mEglContext)) {
                    this.mGL = this.mEglContext.getGL();
                    return USE_OPENGL;
                } else {
                    throw new RuntimeException("eglMakeCurrent failed " + GLUtils.getEGLErrorString(this.mEgl.eglGetError()));
                }
            } else {
                throw new RuntimeException("eglInitialize failed " + GLUtils.getEGLErrorString(this.mEgl.eglGetError()));
            }
        }

        private int loadTexture(Bitmap bitmap) {
            int[] textures = new int[1];
            GLES20.glActiveTexture(33984);
            GLES20.glGenTextures(1, textures, TRIANGLE_VERTICES_DATA_POS_OFFSET);
            checkGlError();
            int texture = textures[0];
            GLES20.glBindTexture(3553, texture);
            checkGlError();
            GLES20.glTexParameteri(3553, 10241, 9729);
            GLES20.glTexParameteri(3553, 10240, 9729);
            GLES20.glTexParameteri(3553, 10242, 33071);
            GLES20.glTexParameteri(3553, 10243, 33071);
            GLUtils.texImage2D(3553, TRIANGLE_VERTICES_DATA_POS_OFFSET, 6408, bitmap, 5121, 0);
            checkGlError();
            bitmap.recycle();
            return texture;
        }

        EGLContext createContext(EGL10 egl, EGLDisplay eglDisplay, EGLConfig eglConfig) {
            return egl.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, new int[]{12440, 2, 12344});
        }

        void drawFrameLocked() {
            if (!this.mVisible) {
                return;
            }
            if (this.mRedrawNeeded || this.mOffsetsChanged) {
                boolean updateWallpaper;
                if (this.mBackgroundWidth < 0 || this.mBackgroundHeight < 0) {
                    updateWallpaper = true;
                } else {
                    updateWallpaper = false;
                }
                if (updateWallpaper || this.mBackground == null) {
                    updateWallpaper = true;
                } else {
                    updateWallpaper = false;
                }
                if (updateWallpaper) {
                    updateWallpaperLocked();
                }
                SurfaceHolder sh = getSurfaceHolder();
                Rect frame = sh.getSurfaceFrame();
                int availw = frame.width() - this.mBackgroundWidth;
                int availh = frame.height() - this.mBackgroundHeight;
                int xPixels = availw < 0 ? (int) (((float) availw) * this.mXOffset + 0.5f) : availw / 2;
                int yPixels = availh < 0 ? (int) (((float) availh) * this.mYOffset + 0.5f) : availh / 2;
                this.mOffsetsChanged = false;
                if (this.mRedrawNeeded || xPixels != this.mLastXTranslation || yPixels != this.mLastYTranslation) {
                    this.mRedrawNeeded = false;
                    this.mLastXTranslation = xPixels;
                    this.mLastYTranslation = yPixels;
                    if (!ImageWallpaper.this.mIsHwAccelerated) {
                        drawWallpaperWithCanvas(sh, availw, availh, xPixels, yPixels);
                    } else if (!drawWallpaperWithOpenGL(sh, availw, availh, xPixels, yPixels)) {
                        drawWallpaperWithCanvas(sh, availw, availh, xPixels, yPixels);
                    }
                    this.mBackground = null;
                    ImageWallpaper.this.mWallpaperManager.forgetLoadedWallpaper();
                }
            }
        }

        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            updateSurfaceSize(surfaceHolder);
            setOffsetNotificationsEnabled(DEBUG);
        }

        public void onDesiredSizeChanged(int desiredWidth, int desiredHeight) {
            super.onDesiredSizeChanged(desiredWidth, desiredHeight);
            SurfaceHolder surfaceHolder = getSurfaceHolder();
            if (surfaceHolder != null) {
                updateSurfaceSize(surfaceHolder);
            }
        }

        public void onDestroy() {
            super.onDestroy();
            if (this.mReceiver != null) {
                ImageWallpaper.this.unregisterReceiver(this.mReceiver);
            }
        }

        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixels, int yPixels) {
            synchronized (this.mLock) {
                if (!(this.mXOffset == xOffset && this.mYOffset == yOffset)) {
                    this.mXOffset = xOffset;
                    this.mYOffset = yOffset;
                    this.mOffsetsChanged = true;
                }
                drawFrameLocked();
            }
        }

        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            synchronized (this.mLock) {
                this.mRedrawNeeded = true;
                drawFrameLocked();
            }
        }

        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
        }

        public void onVisibilityChanged(boolean visible) {
            synchronized (this.mLock) {
                if (this.mVisible != visible) {
                    this.mVisible = visible;
                    drawFrameLocked();
                }
            }
        }

        void updateSurfaceSize(SurfaceHolder surfaceHolder) {
            surfaceHolder.setFixedSize(getDesiredMinimumWidth(), getDesiredMinimumHeight());
        }

        void updateWallpaperLocked() {
            int width;
            int i = TRIANGLE_VERTICES_DATA_POS_OFFSET;
            Throwable exception = null;
            try {
                this.mBackground = ImageWallpaper.this.mWallpaperManager.getBitmap();
            } catch (RuntimeException e) {
                exception = e;
            } catch (OutOfMemoryError e2) {
                exception = e2;
            }
            if (exception != null) {
                this.mBackground = null;
                Log.w(TAG, "Unable to load wallpaper!", exception);
                try {
                    ImageWallpaper.this.mWallpaperManager.clear();
                } catch (IOException e3) {
                    Log.w(TAG, "Unable reset to default wallpaper!", e3);
                }
            }
            if (this.mBackground != null) {
                width = this.mBackground.getWidth();
            } else {
                width = 0;
            }
            this.mBackgroundWidth = width;
            if (this.mBackground != null) {
                i = this.mBackground.getHeight();
            }
            this.mBackgroundHeight = i;
        }
    }

    private static boolean isEmulator() {
        return "1".equals(SystemProperties.get(PROPERTY_KERNEL_QEMU, "0"));
    }

    public void onCreate() {
        super.onCreate();
        this.mWallpaperManager = (WallpaperManager) getSystemService("wallpaper");
        if (!isEmulator()) {
            this.mIsHwAccelerated = ActivityManager.isHighEndGfx(((WindowManager) getSystemService("window")).getDefaultDisplay());
        }
    }

    public Engine onCreateEngine() {
        return new DrawableEngine();
    }
}