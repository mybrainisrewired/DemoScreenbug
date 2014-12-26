package com.wmt.opengl;

import android.graphics.Color;
import com.wmt.opengl.GLBuffer.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;

public final class GLCanvas {
    private static final int VERTEX_NUM = 4;
    private static final float[] sDefaultTextureCoords;
    private static final short[] sIdxOrder;
    private float mAlpha;
    final Draw3DCalculator mDraw3DCalculator;
    private GL11 mGL;
    private GL11Ext mGL11Ext;
    private int mId;
    private ShortBuffer mIdxOrderBuffer;
    private final float[] mPOTPaddedCoordsArray;
    private final FloatBuffer mPOTPaddedTextureCoordsBuffer;
    Perspective mPrespective;
    private int mSurfaceHeight;
    private int mSurfaceWidth;
    private int mTextureCoordBufferIndex;
    private java.nio.FloatBuffer mTextureCoordsBuffer;
    private TextureManager mTextureManager;
    private final float[] mVertexArray;
    private final FloatBuffer mVertexBuffer;

    static {
        sIdxOrder = new short[]{(short) 0, (short) 1, (short) 2, (short) 3};
        sDefaultTextureCoords = new float[]{0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f};
    }

    public GLCanvas() {
        this.mDraw3DCalculator = new Draw3DCalculator();
        this.mAlpha = 1.0f;
        this.mVertexBuffer = new FloatBuffer();
        this.mVertexArray = new float[12];
        this.mPOTPaddedTextureCoordsBuffer = new FloatBuffer();
        this.mPOTPaddedCoordsArray = new float[8];
    }

    private void initBuffers(GL11 oldGL, GL11 gl) {
        if (oldGL == null) {
            this.mIdxOrderBuffer = Utils.allocShortBuffer(sIdxOrder);
            this.mTextureCoordsBuffer = Utils.allocFloatBuffer(sDefaultTextureCoords);
        }
        if (oldGL != gl) {
            this.mTextureCoordBufferIndex = 0;
        }
        int[] buffer = new int[1];
        if (this.mTextureCoordBufferIndex == 0) {
            gl.glGenBuffers(1, buffer, 0);
            this.mTextureCoordBufferIndex = buffer[0];
            gl.glBindBuffer(34962, this.mTextureCoordBufferIndex);
            gl.glBufferData(34962, this.mTextureCoordsBuffer.capacity() * 4, this.mTextureCoordsBuffer, 35044);
            gl.glBindBuffer(34962, 0);
        }
    }

    private void setTextureCoordBuffer(float x, float y) {
        float[] coordArray = this.mPOTPaddedCoordsArray;
        coordArray[0] = 0.0f;
        coordArray[1] = 0.0f;
        coordArray[2] = 0.0f;
        coordArray[3] = y;
        coordArray[4] = x;
        coordArray[5] = y;
        coordArray[6] = x;
        coordArray[7] = 0.0f;
        this.mPOTPaddedTextureCoordsBuffer.put(coordArray);
    }

    private void setVertexBuffer(float z, float left, float top, float right, float bottom) {
        float[] vertexArray = this.mVertexArray;
        vertexArray[0] = left;
        vertexArray[1] = top;
        vertexArray[2] = z;
        vertexArray[3] = left;
        vertexArray[4] = bottom;
        vertexArray[5] = z;
        vertexArray[6] = right;
        vertexArray[7] = bottom;
        vertexArray[8] = z;
        vertexArray[9] = right;
        vertexArray[10] = top;
        vertexArray[11] = z;
        this.mVertexBuffer.put(vertexArray);
    }

    public boolean bindMixed(Texture from, Texture to, float ratio) {
        GL11 gl = this.mGL;
        boolean bind = true & this.mTextureManager.bind(from);
        gl.glActiveTexture(33985);
        this.mTextureManager.setBoundTexture(null);
        if (!(bind & this.mTextureManager.bind(to))) {
            return false;
        }
        gl.glEnable(3553);
        gl.glTexEnvf(8960, 8704, 34160.0f);
        gl.glTexEnvf(8960, 34161, 34165.0f);
        gl.glTexEnvf(8960, 34162, 34165.0f);
        gl.glTexEnvfv(8960, 8705, new float[]{1.0f, 1.0f, 1.0f, ratio}, 0);
        gl.glTexEnvf(8960, 34178, 34166.0f);
        gl.glTexEnvf(8960, 34194, 770.0f);
        gl.glTexEnvf(8960, 34186, 34166.0f);
        gl.glTexEnvf(8960, 34202, 770.0f);
        return true;
    }

    public void draw2D(float x, float y, float z, float width, float height) {
        this.mGL11Ext.glDrawTexfOES(x, ((float) this.mSurfaceHeight) - y - height, z, width, height);
    }

    public void draw2D(Texture texture, float x, float y) {
        if (this.mTextureManager.bind(texture)) {
            float width = (float) texture.getWidth();
            float height = (float) texture.getHeight();
            this.mGL11Ext.glDrawTexfOES(x, ((float) this.mSurfaceHeight) - y - height, 0.0f, width, height);
        }
    }

    public void draw2D(Texture texture, float x, float y, float z) {
        if (this.mTextureManager.bind(texture)) {
            float width = (float) texture.getWidth();
            float height = (float) texture.getHeight();
            this.mGL11Ext.glDrawTexfOES(x, ((float) this.mSurfaceHeight) - y - height, z, width, height);
        }
    }

    public void draw2D(Texture texture, float x, float y, float width, float height) {
        if (this.mTextureManager.bind(texture)) {
            this.mGL11Ext.glDrawTexfOES(x, ((float) this.mSurfaceHeight) - y - height, 0.0f, width, height);
        }
    }

    public void draw2D(Texture texture, float x, float y, float z, float width, float height) {
        if (this.mTextureManager.bind(texture)) {
            this.mGL11Ext.glDrawTexfOES(x, ((float) this.mSurfaceHeight) - y - height, z, width, height);
        }
    }

    public void draw2D(Texture texture, int x, int y, int width, int height) {
        if (this.mTextureManager.bind(texture)) {
            this.mGL11Ext.glDrawTexiOES(x, this.mSurfaceHeight - y - height, 0, width, height);
        }
    }

    public void draw3D(Texture texture, float x, float y, float width, float height) {
        draw3D(texture, x, y, -this.mPrespective.zNear, width, height);
    }

    public void draw3D(Texture texture, float x, float y, float z, float width, float height) {
        if (this.mDraw3DCalculator.getPlaneZ() != z) {
            this.mDraw3DCalculator.init(this.mPrespective.fovy, z, this.mSurfaceWidth, this.mSurfaceHeight);
        }
        setVertexBuffer(z, this.mDraw3DCalculator.xPos2GL(x), this.mDraw3DCalculator.yPos2GL(y), this.mDraw3DCalculator.xPos2GL(x + width), this.mDraw3DCalculator.yPos2GL(y + height));
        draw3D(texture, this.mVertexBuffer, null, JsonWriteContext.STATUS_OK_AFTER_SPACE);
    }

    public void draw3D(Texture texture, FloatBuffer vertexBuffer, FloatBuffer textureBuffer, int vertexSize) {
        Utils.checkGLError(this.mGL);
        vertexBuffer.glVertexPointer(vertexSize);
        if (textureBuffer != null) {
            textureBuffer.glTexturePointer();
            this.mGL.glDrawArrays(FragmentManagerImpl.ANIM_STYLE_FADE_EXIT, 0, VERTEX_NUM);
        } else if (texture.isPOTPadded()) {
            setTextureCoordBuffer(texture.getNormalizedWidth(), texture.getNormalizedHeight());
            this.mPOTPaddedTextureCoordsBuffer.glTexturePointer();
            this.mGL.glDrawArrays(FragmentManagerImpl.ANIM_STYLE_FADE_EXIT, 0, VERTEX_NUM);
        } else {
            this.mGL.glBindBuffer(34962, this.mTextureCoordBufferIndex);
            this.mGL.glTexCoordPointer(ClassWriter.COMPUTE_FRAMES, 5126, 0, 0);
            this.mGL.glDrawArrays(FragmentManagerImpl.ANIM_STYLE_FADE_EXIT, 0, VERTEX_NUM);
            this.mGL.glBindBuffer(34962, 0);
        }
        Utils.checkGLError(this.mGL);
    }

    public void drawMixed2D(Texture from, Texture to, float ratio, float x, float y, float z, float width, float height) {
        GL11 gl = this.mGL;
        if (this.mTextureManager.bind(from)) {
            gl.glActiveTexture(33985);
            this.mTextureManager.setBoundTexture(null);
            if (this.mTextureManager.bind(to)) {
                gl.glEnable(3553);
                gl.glTexEnvf(8960, 8704, 34160.0f);
                gl.glTexEnvf(8960, 34161, 34165.0f);
                gl.glTexEnvf(8960, 34162, 34165.0f);
                gl.glTexEnvfv(8960, 8705, new float[]{1.0f, 1.0f, 1.0f, ratio}, 0);
                gl.glTexEnvf(8960, 34178, 34166.0f);
                gl.glTexEnvf(8960, 34194, 770.0f);
                gl.glTexEnvf(8960, 34186, 34166.0f);
                gl.glTexEnvf(8960, 34202, 770.0f);
                this.mGL11Ext.glDrawTexfOES(x, ((float) this.mSurfaceHeight) - y - height, z, width, height);
                gl.glDisable(3553);
            }
            gl.glActiveTexture(33984);
            this.mTextureManager.setBoundTexture(null);
        }
    }

    public void drawWithEffect(float x, float y, float width, float height, float anchorX, float anchorY, float alpha, float scale) {
        if (scale != 1.0f) {
            float originX = x + anchorX * width;
            float originY = y + anchorY * height;
            width *= scale;
            height *= scale;
            x = originX - anchorX * width;
            y = originY - anchorY * height;
        }
        if (alpha != 1.0f) {
            setAlpha(alpha);
        }
        draw2D(x, y, 0.0f, width, height);
    }

    public void fillRect(int argb, float x, float y, float z, float width, float height) {
        if (this.mDraw3DCalculator.getPlaneZ() != z) {
            this.mDraw3DCalculator.init(this.mPrespective.fovy, z, this.mSurfaceWidth, this.mSurfaceHeight);
        }
        setVertexBuffer(z, this.mDraw3DCalculator.xPos2GL(x), this.mDraw3DCalculator.yPos2GL(y), this.mDraw3DCalculator.xPos2GL(x + width), this.mDraw3DCalculator.yPos2GL(y + height));
        this.mGL.glColor4f(((float) Color.red(argb)) / 255.0f, ((float) Color.green(argb)) / 255.0f, ((float) Color.blue(argb)) / 255.0f, ((float) Color.alpha(argb)) / 255.0f);
        this.mVertexBuffer.glVertexPointer(JsonWriteContext.STATUS_OK_AFTER_SPACE);
        this.mGL.glDisable(3553);
        this.mGL.glDrawElements(FragmentManagerImpl.ANIM_STYLE_FADE_EXIT, sIdxOrder.length, 5123, this.mIdxOrderBuffer);
        this.mGL.glEnable(3553);
        this.mGL.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public float getAlpha() {
        return this.mAlpha;
    }

    public GL11 getGL() {
        return this.mGL;
    }

    public GL11Ext getGLExt() {
        return this.mGL11Ext;
    }

    public float getPixel2GL() {
        return this.mDraw3DCalculator.getPixel2GLScale();
    }

    public float getPixel2GL(float z) {
        return Draw3DCalculator.getPixel2GLScale(this.mPrespective.fovy, z, this.mSurfaceHeight);
    }

    public int getSurfaceHeight() {
        return this.mSurfaceHeight;
    }

    public int getSurfaceWidth() {
        return this.mSurfaceWidth;
    }

    public TextureManager getTextureManager() {
        return this.mTextureManager;
    }

    public float getXPos2GL(float x, float z) {
        if (this.mDraw3DCalculator.getPlaneZ() != z) {
            this.mDraw3DCalculator.init(this.mPrespective.fovy, z, this.mSurfaceWidth, this.mSurfaceHeight);
        }
        return this.mDraw3DCalculator.xPos2GL(x);
    }

    public float getYPos2GL(float y, float z) {
        if (this.mDraw3DCalculator.getPlaneZ() != z) {
            this.mDraw3DCalculator.init(this.mPrespective.fovy, z, this.mSurfaceWidth, this.mSurfaceHeight);
        }
        return this.mDraw3DCalculator.yPos2GL(y);
    }

    public int id() {
        return this.mId;
    }

    public void lineRect(int argb, float x, float y, float z, float width, float height) {
        if (this.mDraw3DCalculator.getPlaneZ() != z) {
            this.mDraw3DCalculator.init(this.mPrespective.fovy, z, this.mSurfaceWidth, this.mSurfaceHeight);
        }
        setVertexBuffer(z, this.mDraw3DCalculator.xPos2GL(x), this.mDraw3DCalculator.yPos2GL(y), this.mDraw3DCalculator.xPos2GL(x + width), this.mDraw3DCalculator.yPos2GL(y + height));
        this.mGL.glColor4f(((float) Color.red(argb)) / 255.0f, ((float) Color.green(argb)) / 255.0f, ((float) Color.blue(argb)) / 255.0f, ((float) Color.alpha(argb)) / 255.0f);
        this.mVertexBuffer.glVertexPointer(JsonWriteContext.STATUS_OK_AFTER_SPACE);
        this.mGL.glDisable(3553);
        this.mGL.glDrawElements(ClassWriter.COMPUTE_FRAMES, sIdxOrder.length, 5123, this.mIdxOrderBuffer);
        this.mGL.glEnable(3553);
        this.mGL.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public Perspective prespective() {
        return this.mPrespective;
    }

    public void resetColor() {
        GL11 gl = this.mGL;
        gl.glTexEnvf(8960, 8704, 8448.0f);
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void setAlpha(float alpha) {
        GL11 gl = this.mGL;
        gl.glTexEnvf(8960, 8704, 8448.0f);
        gl.glColor4f(alpha, alpha, alpha, alpha);
        this.mAlpha = alpha;
    }

    public void setColor(float red, float green, float blue, float alpha) {
        GL11 gl = this.mGL;
        gl.glTexEnvf(8960, 8704, 8448.0f);
        gl.glColor4f(red, green, blue, alpha);
    }

    public void setContext(GL11 gl, TextureManager textureManager, Perspective prespective, int winwidth, int winheight) {
        this.mId++;
        this.mGL11Ext = (GL11Ext) gl;
        this.mTextureManager = textureManager;
        this.mSurfaceWidth = winwidth;
        this.mSurfaceHeight = winheight;
        this.mPrespective = prespective;
        initBuffers(this.mGL, gl);
        this.mDraw3DCalculator.init(this.mPrespective.fovy, prespective.zNear, this.mSurfaceWidth, this.mSurfaceHeight);
        this.mGL = gl;
    }

    public void setTextureManager(TextureManager textureManager) {
        this.mTextureManager = textureManager;
    }

    public void unbindMixed() {
        GL11 gl = this.mGL;
        gl.glDisable(3553);
        gl.glActiveTexture(33984);
        this.mTextureManager.setBoundTexture(null);
    }
}