package com.wmt.opengl;

import android.graphics.RectF;
import com.wmt.opengl.GLBuffer.FloatBuffer;
import javax.microedition.khronos.opengles.GL11;

public final class VertexDrawer {
    private static final int ANGLE_SET = 2;
    public static final int BIND_FORCE = 4;
    private static final int DRAWTYPE_PIXEL = 1;
    private static final int SCALE_SET = 4;
    private static final int TEXTURE_REBUILD = 16;
    private static final int VERTEX_MAY_REBUILD = 8;
    public static final int XY_CENTER = 3;
    public static final int X_CENTER = 1;
    public static final int Y_CENTER = 2;
    private float mAngleX;
    private float mAngleY;
    private float mAngleZ;
    private float mCachedHeight;
    private float mCachedWidth;
    private final Draw3DCalculator mCalc;
    private int mCanvasId;
    private float mDrawHeight;
    private float mDrawWidth;
    private int mFlags;
    private float mScaleX;
    private float mScaleY;
    private float mScaleZ;
    private final float[] mTempFloat;
    private Texture mTexture;
    private FloatBuffer mTextureBuffer;
    private RectF mTextureRect;
    private final FloatBuffer mVertexBuffer;
    private float mZ;

    public VertexDrawer(float z, Texture texture) {
        this.mCalc = new Draw3DCalculator();
        this.mVertexBuffer = new FloatBuffer();
        this.mTempFloat = new float[8];
        reset();
        this.mZ = z;
        this.mTexture = texture;
    }

    private void rebuildTextureBuffer(RectF rect) {
        float[] texBuffer = this.mTempFloat;
        float w = ((float) this.mTexture.getWidth()) / this.mTexture.getNormalizedWidth();
        float h = ((float) this.mTexture.getHeight()) / this.mTexture.getNormalizedHeight();
        float l = rect.left / w;
        float t = rect.top / h;
        float r = rect.right / w;
        float b = rect.bottom / h;
        texBuffer[0] = l;
        texBuffer[1] = t;
        texBuffer[2] = l;
        texBuffer[3] = b;
        texBuffer[4] = r;
        texBuffer[5] = b;
        texBuffer[6] = r;
        texBuffer[7] = t;
        this.mTextureBuffer.put(texBuffer);
    }

    private void rebuildVertextBuffer(GLCanvas glCanvas, float z, float width, float height) {
        this.mCalc.init(glCanvas.prespective().fovy, z, glCanvas.getSurfaceWidth(), glCanvas.getSurfaceHeight());
        float halfWidth = this.mCalc.pixel2GL(width) * 0.5f;
        float halfHeight = this.mCalc.pixel2GL(height) * 0.5f;
        float[] vertex = this.mTempFloat;
        vertex[0] = -halfWidth;
        vertex[1] = halfHeight;
        vertex[2] = -halfWidth;
        vertex[3] = -halfHeight;
        vertex[4] = halfWidth;
        vertex[5] = -halfHeight;
        vertex[6] = halfWidth;
        vertex[7] = halfHeight;
        this.mVertexBuffer.put(vertex);
        this.mCanvasId = glCanvas.id();
    }

    public void cutTextureRect(float left, float top, float right, float bottom) {
        RectF r = this.mTextureRect;
        if (r == null) {
            this.mTextureRect = new RectF(left, top, right, bottom);
        } else if (r.left != left || r.top != top || r.right != right || r.bottom != bottom) {
            r.set(left, top, right, bottom);
        } else {
            return;
        }
        this.mFlags |= 24;
    }

    public void cutTextureRect(int left, int top, int right, int bottom) {
        cutTextureRect((float) left, (float) top, (float) right, (float) bottom);
    }

    public boolean draw(GLCanvas glCanvas, float x, float y, int flags) {
        GL11 gl = glCanvas.getGL();
        Utils.checkGLError(gl);
        TextureManager tm = glCanvas.getTextureManager();
        if (!((flags & 4) != 0 ? tm.bindForce(this.mTexture) : tm.bind(this.mTexture))) {
            return false;
        }
        float glX;
        float glY;
        if (!(this.mTextureRect == null || (this.mFlags & 16) == 0)) {
            if (this.mTextureBuffer == null) {
                this.mTextureBuffer = new FloatBuffer();
            }
            rebuildTextureBuffer(this.mTextureRect);
            this.mFlags &= -17;
        }
        boolean rebuildVertex = false;
        if ((this.mFlags & 8) != 0) {
            float width;
            float height;
            if ((this.mFlags & 1) != 0) {
                width = this.mDrawWidth;
                height = this.mDrawHeight;
            } else if (this.mTextureRect == null) {
                width = this.mDrawWidth * ((float) this.mTexture.getWidth());
                height = this.mDrawHeight * ((float) this.mTexture.getHeight());
            } else {
                width = this.mDrawWidth * this.mTextureRect.width();
                height = this.mDrawHeight * this.mTextureRect.height();
            }
            if (!(this.mCachedWidth == width && this.mCachedHeight == height)) {
                this.mCachedWidth = width;
                this.mCachedHeight = height;
                rebuildVertex = true;
            }
            this.mFlags &= -9;
        }
        if (!(!rebuildVertex && this.mCanvasId == glCanvas.id() && this.mZ == this.mCalc.getPlaneZ())) {
            rebuildVertextBuffer(glCanvas, this.mZ, this.mCachedWidth, this.mCachedHeight);
        }
        if ((flags & 1) != 0) {
            glX = x;
        } else {
            glX = x + this.mCachedWidth / 2.0f;
        }
        if ((flags & 2) != 0) {
            glY = y;
        } else {
            glY = y + this.mCachedHeight / 2.0f;
        }
        glX = this.mCalc.xPos2GL(glX);
        glY = this.mCalc.yPos2GL(glY);
        gl.glPushMatrix();
        gl.glTranslatef(glX, glY, this.mZ);
        if ((this.mFlags & 4) != 0) {
            gl.glScalef(this.mScaleX, this.mScaleY, this.mScaleZ);
        }
        if ((this.mFlags & 2) != 0) {
            if (this.mAngleX != 0.0f) {
                gl.glRotatef(this.mAngleX, 1.0f, 0.0f, 0.0f);
            }
            if (this.mAngleY != 0.0f) {
                gl.glRotatef(this.mAngleY, 0.0f, 1.0f, 0.0f);
            }
            if (this.mAngleZ != 0.0f) {
                gl.glRotatef(this.mAngleZ, 0.0f, 0.0f, 1.0f);
            }
        }
        glCanvas.draw3D(this.mTexture, this.mVertexBuffer, this.mTextureBuffer, Y_CENTER);
        gl.glPopMatrix();
        Utils.checkGLError(gl);
        return true;
    }

    public boolean draw(GLCanvas glCanvas, int x, int y, int flags) {
        return draw(glCanvas, (float) x, (float) y, flags);
    }

    public Texture getTexture() {
        return this.mTexture;
    }

    public float getZ() {
        return this.mZ;
    }

    protected void reset() {
        this.mZ = 0.0f;
        this.mTexture = null;
        this.mTextureRect = null;
        this.mTextureBuffer = null;
        this.mScaleZ = 1.0f;
        this.mScaleY = 1.0f;
        this.mScaleX = 1.0f;
        this.mAngleZ = 0.0f;
        this.mAngleY = 0.0f;
        this.mAngleX = 0.0f;
        this.mDrawWidth = 1.0f;
        this.mDrawHeight = 1.0f;
        this.mCanvasId = -1;
        this.mFlags = 8;
    }

    public void scale(float scaleX, float scaleY, float scaleZ) {
        this.mScaleX = scaleX;
        this.mScaleY = scaleY;
        this.mScaleZ = scaleZ;
        if (scaleX == 1.0f && scaleY == 1.0f && scaleZ == 1.0f) {
            this.mFlags &= -5;
        } else {
            this.mFlags |= 4;
        }
    }

    public void setDrawScale(float scaleX, float scaleY) {
        this.mDrawWidth = scaleX;
        this.mDrawHeight = scaleY;
        this.mFlags &= -2;
        this.mFlags |= 8;
    }

    public void setDrawSize(float width, float height) {
        this.mDrawWidth = width;
        this.mDrawHeight = height;
        this.mFlags |= 9;
    }

    public void setRotate(float x, float y, float z) {
        this.mAngleX = x;
        this.mAngleY = y;
        this.mAngleZ = z;
        if (x == 0.0f && y == 0.0f && z == 0.0f) {
            this.mFlags &= -3;
        } else {
            this.mFlags |= 2;
        }
    }

    public void setTexture(Texture t) {
        this.mTexture = t;
        this.mFlags |= 8;
    }

    public void setZ(float z) {
        this.mZ = z;
    }
}