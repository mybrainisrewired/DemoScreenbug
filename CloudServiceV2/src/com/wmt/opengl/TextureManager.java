package com.wmt.opengl;

import android.graphics.Bitmap;
import android.opengl.GLUtils;
import android.util.Log;
import com.wmt.data.LocalAudioAll;
import com.wmt.opengl.DirectLinkedList.Entry;
import com.wmt.util.Deque;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import javax.microedition.khronos.opengles.GL11;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public final class TextureManager {
    private static final int BIND_FORCE = 5;
    private static final int BIND_MODE_All = 2;
    private static final int BIND_MODE_NORMAL = 1;
    private static final int BIND_TRY = 4;
    public static final int DEFAULE_WEIGHTS = 1;
    public static final int LOADMODE_ALL = 1;
    public static final int LOADMODE_CHECK = 3;
    public static final int LOADMODE_QUOTA = 2;
    private static final int MAX_UPLOAD_RETRY = 10;
    private static final int QUEUE_HIGH = 2;
    private static final int QUEUE_NORMAL = 1;
    private static final String TAG = "TextureManager";
    private static final int UPLOAD_NOW = 3;
    private static int s_GLSupportNPOT;
    private final int MAX_LOADING_COUNT;
    private final int NO_LIMIT_LOADING_COUNT;
    private final DirectLinkedList<TextureReference> mActiveTextureList;
    private int mBindMode;
    private Texture mBoundTexture;
    private GLPrivContext mGLContext;
    private GL11 mGLObject;
    private int mLimitLoadingCount;
    private TextureLoadThread mLoadThread;
    private Deque<Texture> mLoadedQueue;
    private Deque<Texture> mRequestLoadQueue;
    private final int[] mTextureId;
    private final ReferenceQueue mUnreferencedTextureQueue;
    private int mWeights;

    private final class TextureLoadThread extends Thread {
        Texture mCurrentTexture;
        private boolean mExitMark;

        TextureLoadThread() {
            super("TextureLoad");
            this.mExitMark = false;
        }

        private void guardedRun() throws Exception {
            while (!this.mExitMark) {
                synchronized (TextureManager.this.mRequestLoadQueue) {
                    this.mCurrentTexture = (Texture) TextureManager.this.mRequestLoadQueue.pollFirst();
                    if (this.mCurrentTexture == null) {
                        TextureManager.this.mGLContext.onLoadThreadIdle();
                        TextureManager.this.mRequestLoadQueue.wait();
                    }
                }
                if (!this.mExitMark) {
                    if (this.mCurrentTexture != null && TextureManager.this.loadTextureBitmap(this.mCurrentTexture)) {
                        synchronized (TextureManager.this.mLoadedQueue) {
                            TextureManager.this.mLoadedQueue.addLast(this.mCurrentTexture);
                        }
                        TextureManager.this.mGLContext.requestRenderDelay(0);
                    }
                } else {
                    return;
                }
            }
        }

        void exit() {
            synchronized (TextureManager.this.mRequestLoadQueue) {
                this.mExitMark = true;
                TextureManager.this.mRequestLoadQueue.clear();
                TextureManager.this.mRequestLoadQueue.notify();
            }
        }

        public void run() {
            Texture texture = null;
            try {
                guardedRun();
                this.mCurrentTexture = texture;
            } catch (Exception e) {
                this.mCurrentTexture = texture;
            }
        }
    }

    private static final class TextureReference extends WeakReference<Texture> {
        public final Entry<TextureReference> activeListEntry;
        public final GL11 gl;
        public final int textureId;

        public TextureReference(Texture texture, GL11 gl, ReferenceQueue referenceQueue, int textureId) {
            super(texture, referenceQueue);
            this.activeListEntry = new Entry(this);
            this.textureId = textureId;
            this.gl = gl;
        }
    }

    static {
        s_GLSupportNPOT = 0;
    }

    public TextureManager(GLPrivContext glContext) {
        int i = QUEUE_NORMAL;
        this.mRequestLoadQueue = new Deque();
        this.mLoadedQueue = new Deque();
        this.MAX_LOADING_COUNT = 8;
        this.NO_LIMIT_LOADING_COUNT = Integer.MAX_VALUE;
        this.mLimitLoadingCount = 8;
        this.mWeights = 1;
        this.mBoundTexture = null;
        this.mActiveTextureList = new DirectLinkedList();
        this.mUnreferencedTextureQueue = new ReferenceQueue();
        this.mTextureId = new int[1];
        this.mBindMode = 1;
        this.mGLContext = glContext;
        this.mLoadThread = new TextureLoadThread();
        TextureLoadThread textureLoadThread = this.mLoadThread;
        if (this.mLoadThread.getPriority() - 1 >= 1) {
            i = this.mLoadThread.getPriority() - 1;
        }
        textureLoadThread.setPriority(i);
        this.mLoadThread.start();
    }

    private void handleLowMemory() {
        Log.i(TAG, "low memory gc come ");
        System.gc();
    }

    private boolean loadTextureBitmap(Texture texture) {
        boolean loadTextureBitmapLocked;
        synchronized (texture) {
            loadTextureBitmapLocked = loadTextureBitmapLocked(texture);
        }
        return loadTextureBitmapLocked;
    }

    private boolean loadTextureBitmapLocked(Texture texture) {
        Bitmap bitmap = null;
        int i = BIND_TRY;
        try {
            if (texture.mLoadedBitmap != null) {
                Log.d(TAG, "loadTextureBitmap not need load bitmap");
                texture.mState = 2;
                return true;
            } else {
                Bitmap bitmap2 = texture.externalLoad(this.mGLContext.uiContext());
                if (bitmap2 != null) {
                    potProcess(texture, bitmap2);
                    texture.mState = 2;
                    return true;
                } else {
                    texture.mState = 4;
                    Exception e = new Exception("Debug");
                    e.fillInStackTrace();
                    Log.i(TAG, "Texture.load return null : " + texture, e);
                    return false;
                }
            }
        } catch (Exception e2) {
            Log.v(TAG, "Texture " + texture + " load exception", e2);
            texture.mLoadedBitmap = bitmap;
            texture.mState = i;
        } catch (OutOfMemoryError e3) {
            Log.w(TAG, "Texture load out of memory" + texture, e3);
            texture.mLoadedBitmap = bitmap;
            texture.mState = i;
        }
    }

    private static void potProcess(Texture texture, Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        texture.mWidth = (int) (((float) width) * texture.getScale());
        texture.mHeight = (int) (((float) height) * texture.getScale());
        if (s_GLSupportNPOT >= 0 || (Utils.isPowerOf2(width) && Utils.isPowerOf2(height))) {
            texture.mNormalizedWidth = 1.0f;
            texture.mNormalizedHeight = 1.0f;
            if (texture.mLoadedBitmap != bitmap) {
                texture.recycleAfterLoaded();
            }
            texture.mLoadedBitmap = bitmap;
            texture.setPOTPadded(false);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private boolean queueLoad(Texture texture, boolean highPriority) {
        boolean queued = false;
        Deque<Texture> inputQueue = this.mRequestLoadQueue;
        synchronized (inputQueue) {
            if (highPriority) {
                inputQueue.addFirst(texture);
                texture.mState = 1;
                queued = true;
                if (inputQueue.size() >= this.mLimitLoadingCount) {
                    ((Texture) inputQueue.pollLast()).mState = 0;
                }
            } else if (inputQueue.size() < this.mLimitLoadingCount) {
                inputQueue.addLast(texture);
                texture.mState = 1;
                queued = true;
            }
            if (queued) {
                inputQueue.notify();
            }
        }
        return queued;
    }

    private void resetTexturesStateLocked() {
        if (!this.mActiveTextureList.isEmpty()) {
            Entry<TextureReference> iter = this.mActiveTextureList.getHead();
            while (iter != null) {
                Texture texture = (Texture) ((TextureReference) iter.value).get();
                if (texture != null) {
                    if (texture.mLoadedBitmap == null || texture.mLoadedBitmap.isRecycled()) {
                        texture.mState = 0;
                    } else {
                        texture.mState = 2;
                    }
                    texture.mTextureId = -1;
                }
                iter = iter.next;
            }
        }
    }

    private boolean textureOp(Texture texture, int op) {
        if (texture == null) {
            Exception e = new Exception("Debug");
            e.fillInStackTrace();
            Log.w(TAG, "texture is null when op=" + op, e);
            return false;
        } else {
            GL11 gl = this.mGLObject;
            if (s_GLSupportNPOT == 0 && gl != null) {
                boolean support;
                int i;
                String extensions = MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + gl.glGetString(7939) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR;
                if (extensions.indexOf(" GL_OES_texture_npot ") >= 0) {
                    support = true;
                } else {
                    support = false;
                }
                if (extensions.indexOf(" GL_ARB_texture_non_power_of_two ") >= 0) {
                    i = 1;
                } else {
                    i = 0;
                }
                support |= i;
                if (support) {
                    s_GLSupportNPOT = 1;
                } else {
                    s_GLSupportNPOT = -1;
                }
                Log.d(TAG, "SupportNPOT : " + support);
            }
            if (this.mBindMode == 2) {
                op = BIND_FORCE;
            }
            switch (texture.mState) {
                case LocalAudioAll.SORT_BY_TITLE:
                    if (op == 4) {
                        if (texture.isBindForce() || texture.getClass().equals(ResourceTexture.class)) {
                            op = BIND_FORCE;
                        }
                    }
                    if (op == 1 || op == 4) {
                        return op == 1 && queueLoad(texture, false);
                    } else if (op == 2) {
                        return queueLoad(texture, true);
                    } else {
                        if (op == 3 || op == 5) {
                            loadTextureBitmap(texture);
                            return upload2GLTexture(gl, texture);
                        } else {
                            throw new IllegalArgumentException("Unknown op : " + op);
                        }
                    }
                case QUEUE_HIGH:
                    return (op == 1 || op == 2) ? true : upload2GLTexture(gl, texture);
                case UPLOAD_NOW:
                    if (op == 1 || op == 2 || op == 3) {
                        return true;
                    }
                    if (op != 5 && op != 4) {
                        throw new IllegalArgumentException("Unknown op : " + op);
                    } else if (texture == this.mBoundTexture) {
                        return true;
                    } else {
                        Utils.checkGLError(gl);
                        gl.glBindTexture(3553, texture.mTextureId);
                        Utils.checkGLError(gl);
                        this.mBoundTexture = texture;
                        return true;
                    }
                default:
                    return false;
            }
        }
    }

    private void tryReloadTexture(Texture texture) {
        if (texture == null || texture.mLoadCounter >= 10) {
            texture.mState = 5;
        } else {
            texture.mLoadCounter++;
            texture.mTextureId = -1;
            texture.mLoadedBitmap = null;
            texture.mState = 0;
            Log.i(TAG, "try reload texture");
        }
    }

    private boolean upload2GLTexture(GL11 gl, Texture texture) {
        boolean upload2GLTextureLocked;
        synchronized (texture) {
            upload2GLTextureLocked = upload2GLTextureLocked(gl, texture);
        }
        return upload2GLTextureLocked;
    }

    private boolean upload2GLTextureLocked(GL11 gl, Texture texture) {
        Exception e;
        if (texture.isUploaded()) {
            return true;
        }
        Bitmap bitmap = texture.mLoadedBitmap;
        Utils.checkGLError(gl);
        if (bitmap != null) {
            int textureId;
            if (texture.mTextureId == -1) {
                this.mTextureId[0] = -1;
                gl.glGenTextures(QUEUE_NORMAL, this.mTextureId, 0);
                textureId = this.mTextureId[0];
                if (textureId == -1) {
                    e = new Exception("Debug");
                    e.fillInStackTrace();
                    Log.w(TAG, "glGenTextures error, not running in GL thread?", e);
                    return false;
                }
            } else {
                textureId = texture.mTextureId;
            }
            gl.glBindTexture(3553, textureId);
            int width = texture.mWidth;
            int height = texture.mHeight;
            gl.glTexParameteriv(3553, 35741, new int[]{0, height, width, -height}, 0);
            gl.glTexParameteri(3553, 10242, 33071);
            gl.glTexParameteri(3553, 10243, 33071);
            gl.glTexParameterf(3553, 10241, 9729.0f);
            gl.glTexParameterf(3553, 10240, 9729.0f);
            try {
                GLUtils.texImage2D(3553, 0, bitmap, 0);
                this.mBoundTexture = texture;
                int glError = gl.glGetError();
                texture.recycleAfterLoaded();
                if (glError == 1285) {
                    handleLowMemory();
                }
                if (glError != 0) {
                    Log.i(TAG, "Texture upload fail, glError " + glError);
                    tryReloadTexture(texture);
                } else {
                    texture.mState = 3;
                    if (texture.mTextureId == -1) {
                        texture.mTextureId = textureId;
                        TextureReference textureRef = new TextureReference(texture, gl, this.mUnreferencedTextureQueue, textureId);
                        synchronized (this.mActiveTextureList) {
                            this.mActiveTextureList.add(textureRef.activeListEntry);
                        }
                    } else {
                        synchronized (this.mActiveTextureList) {
                            if (!this.mActiveTextureList.isEmpty()) {
                                Entry<TextureReference> iter = this.mActiveTextureList.getHead();
                                while (iter != null) {
                                    Texture t = (Texture) ((TextureReference) iter.value).get();
                                    if (t == texture) {
                                        if (t.mTextureId != texture.mTextureId) {
                                            new Exception("Debug").fillInStackTrace();
                                            Log.d(TAG, "Texture Id not equal, 1:" + texture + "\n2:" + t);
                                            break;
                                        }
                                    } else {
                                        iter = iter.next;
                                    }
                                }
                                if (iter == null) {
                                    e = new Exception("Debug");
                                    e.fillInStackTrace();
                                    Log.d(TAG, "Active Texture List can't find " + texture, e);
                                }
                            }
                        }
                    }
                    this.mGLContext.requestRenderDelay(0);
                    return true;
                }
            } catch (Exception e2) {
                e = e2;
                Log.v(TAG, "texture " + texture + " texImage2D failed");
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        } else {
            e = new Exception("Debug");
            e.fillInStackTrace();
            Log.i(TAG, "upload2GLTexture(" + texture + ") failed, stack:\n", e);
            tryReloadTexture(texture);
        }
        return false;
    }

    public boolean bind(Texture texture) {
        return textureOp(texture, BIND_TRY);
    }

    public boolean bindForce(Texture texture) {
        return textureOp(texture, BIND_FORCE);
    }

    public void bindForceAllTexture() {
        this.mBindMode = 2;
    }

    public void bindGL(GL11 gl) {
        this.mGLObject = gl;
    }

    public boolean bindReplace(Texture texture, Bitmap newBitmap) {
        Exception e;
        if (texture == null || newBitmap == null) {
            e = new Exception("Debug");
            e.fillInStackTrace();
            Log.w(TAG, "bindReplace either texture or newBitmap is null", e);
            return false;
        } else {
            GL11 gl = this.mGLObject;
            switch (texture.mState) {
                case LocalAudioAll.SORT_BY_TITLE:
                case QUEUE_HIGH:
                case UPLOAD_NOW:
                case BIND_TRY:
                case BIND_FORCE:
                    int oldTextureId = texture.mTextureId;
                    texture.clear();
                    texture.mTextureId = oldTextureId;
                    potProcess(texture, newBitmap);
                    return upload2GLTexture(gl, texture);
                case QUEUE_NORMAL:
                    e = new Exception("Debug");
                    e.fillInStackTrace();
                    Log.d(TAG, "bindReplace but status=STATE_QUEUED. Did you call prime before?", e);
                    return false;
                default:
                    return false;
            }
        }
    }

    public boolean checkLoadedQueue(int loadmode) {
        GL11 gl = this.mGLObject;
        int unref = 0;
        while (true) {
            TextureReference textureReference = (TextureReference) this.mUnreferencedTextureQueue.poll();
            Texture texture;
            if (textureReference != null) {
                this.mTextureId[0] = textureReference.textureId;
                if (textureReference.gl == gl) {
                    gl.glDeleteTextures(QUEUE_NORMAL, this.mTextureId, 0);
                }
                texture = (Texture) textureReference.get();
                if (texture != null) {
                    texture.clear();
                }
                synchronized (this.mActiveTextureList) {
                    this.mActiveTextureList.remove(textureReference.activeListEntry);
                }
                unref++;
                Utils.checkGLError(gl);
            } else {
                if (unref > 0) {
                    Log.d(TAG, "Remove unref textures, count=" + unref);
                }
                if (loadmode == 3) {
                    return !this.mLoadedQueue.isEmpty();
                } else {
                    boolean processAll;
                    if (loadmode == 1) {
                        processAll = true;
                    } else {
                        processAll = false;
                    }
                    Deque<Texture> outputQueue = this.mLoadedQueue;
                    int weights = this.mWeights;
                    while (true) {
                        synchronized (outputQueue) {
                            texture = outputQueue.pollFirst();
                        }
                        if (texture != null) {
                            if (!texture.isUploaded()) {
                                weights -= texture.getWeight();
                                if (weights >= 0 || processAll) {
                                    upload2GLTexture(gl, texture);
                                } else {
                                    synchronized (outputQueue) {
                                        outputQueue.addFirst(texture);
                                    }
                                }
                            }
                            if (processAll || weights > 0) {
                            }
                        }
                        return !outputQueue.isEmpty();
                    }
                }
            }
        }
    }

    public void clear() {
        try {
            Texture texture;
            this.mLoadThread.exit();
            synchronized (this.mActiveTextureList) {
                if (!this.mActiveTextureList.isEmpty()) {
                    Entry<TextureReference> iter = this.mActiveTextureList.getHead();
                    while (iter != null) {
                        texture = (Texture) ((TextureReference) iter.value).get();
                        if (texture != null) {
                            texture.clear();
                        }
                        iter = iter.next;
                    }
                }
                this.mActiveTextureList.clear();
            }
            synchronized (this.mRequestLoadQueue) {
                while (true) {
                    texture = this.mRequestLoadQueue.pollFirst();
                    if (texture != null) {
                        texture.mState = 0;
                    } else {
                        this.mRequestLoadQueue.clear();
                    }
                }
            }
            synchronized (this.mLoadedQueue) {
                while (true) {
                    texture = (Texture) this.mLoadedQueue.pollFirst();
                    if (texture != null) {
                        texture.recycleAfterLoaded();
                        texture.mState = 0;
                    } else {
                        this.mLoadedQueue.clear();
                    }
                }
            }
        } catch (Exception e) {
        } catch (Throwable th) {
        }
    }

    public int getWeightsQuota() {
        return this.mWeights;
    }

    public boolean isLoadThreadIdle() {
        return this.mLoadThread.mCurrentTexture == null && this.mRequestLoadQueue.isEmpty();
    }

    public void onPause() {
        System.gc();
        synchronized (this.mActiveTextureList) {
            resetTexturesStateLocked();
            this.mLimitLoadingCount = Integer.MAX_VALUE;
            if (!this.mActiveTextureList.isEmpty()) {
                int totalCount = this.mActiveTextureList.size();
                int unrefCount = 0;
                Entry<TextureReference> iter = this.mActiveTextureList.getHead();
                while (iter != null) {
                    Texture texture = (Texture) ((TextureReference) iter.value).get();
                    if (texture != null) {
                        prime(texture, false);
                    } else {
                        unrefCount++;
                    }
                    iter = iter.next;
                }
                Log.i(TAG, "onPause  activate texture total=" + totalCount + ",unref=" + unrefCount);
            }
            this.mActiveTextureList.clear();
        }
    }

    public void onResume() {
        this.mLimitLoadingCount = 8;
    }

    public boolean prime(Texture texture, boolean highPriority) {
        return textureOp(texture, highPriority ? QUEUE_HIGH : QUEUE_NORMAL);
    }

    public void resetTextures() {
        synchronized (this.mActiveTextureList) {
            Log.i(TAG, "resetTextures, active size = " + this.mActiveTextureList.size());
            resetTexturesStateLocked();
            this.mActiveTextureList.clear();
        }
    }

    public void setBoundTexture(Texture texture) {
        this.mBoundTexture = texture;
    }

    public void setWeightsQuota(int weights) {
        this.mWeights = weights;
    }

    public void unBindForceAllTexture() {
        this.mBindMode = 1;
    }

    public boolean uploadTexture(Texture texture) {
        return textureOp(texture, UPLOAD_NOW);
    }
}