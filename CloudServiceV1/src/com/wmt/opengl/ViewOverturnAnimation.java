package com.wmt.opengl;

import com.wmt.opengl.GLBuffer.FloatBuffer;
import com.wmt.opengl.grid.ItemAnimation;
import javax.microedition.khronos.opengles.GL11;

public class ViewOverturnAnimation extends GLView {
    private static final int sAnimationTime = 1100;
    private boolean mAnimationMark;
    private FloatBuffer mCoordsBuffer;
    protected int mEndTextureID;
    protected AnimationListener mListener;
    private float mRoatfArc;
    protected int mStartTextureID;
    private long mStartTime;
    private FloatBuffer mVertexBuffer;
    private float[] mVertexs;

    public static interface AnimationListener {
        void onAnimationEnd();

        void onAnimationStart();
    }

    public ViewOverturnAnimation(GLContext glContext, int flag) {
        super(glContext, flag | 16);
        this.mStartTextureID = -1;
        this.mEndTextureID = -1;
        this.mCoordsBuffer = new FloatBuffer();
        this.mVertexBuffer = new FloatBuffer();
        this.mVertexs = new float[12];
        this.mAnimationMark = false;
        this.mRoatfArc = 0.0f;
        setVisibility(false);
    }

    private void calculateVertex(GLCanvas glCanvas, int left, int top) {
        int w = getWidth();
        int h = getHeight();
        float[] fArr = this.mVertexs;
        float[] fArr2 = this.mVertexs;
        float[] fArr3 = this.mVertexs;
        this.mVertexs[11] = -0.3f;
        fArr3[8] = -0.3f;
        fArr2[5] = -0.3f;
        fArr[2] = -0.3f;
        fArr = this.mVertexs;
        fArr2 = this.mVertexs;
        float xPos2GL = glCanvas.getXPos2GL((float) left, -0.3f);
        fArr2[9] = xPos2GL;
        fArr[0] = xPos2GL;
        fArr = this.mVertexs;
        fArr2 = this.mVertexs;
        xPos2GL = glCanvas.getYPos2GL((float) (top + h), -0.3f);
        fArr2[4] = xPos2GL;
        fArr[1] = xPos2GL;
        fArr = this.mVertexs;
        fArr2 = this.mVertexs;
        xPos2GL = glCanvas.getXPos2GL((float) (left + w), -0.3f);
        fArr2[6] = xPos2GL;
        fArr[3] = xPos2GL;
        fArr = this.mVertexs;
        fArr2 = this.mVertexs;
        xPos2GL = glCanvas.getYPos2GL((float) top, -0.3f);
        fArr2[10] = xPos2GL;
        fArr[7] = xPos2GL;
        this.mVertexBuffer.put(this.mVertexs);
    }

    private void initCoords(float xCoords, float yCoords) {
        this.mCoordsBuffer.put(new float[]{0.0f, 0.0f, 0.0f, 0.0f, xCoords, xCoords, yCoords, yCoords});
    }

    public void endAnimation(GLCanvas glCanvas) {
        if (this.mAnimationMark) {
            glCanvas.getGL().glDeleteTextures(ItemAnimation.CUR_Z, new int[]{this.mStartTextureID, this.mEndTextureID}, 0);
            if (this.mListener != null) {
                this.mListener.onAnimationEnd();
            }
            this.mRoatfArc = 0.0f;
            setVisibility(false);
            this.mAnimationMark = false;
        }
    }

    public boolean isAnimationing() {
        return this.mAnimationMark;
    }

    protected void onRender(GLCanvas glCanvas, int left, int top) {
        GL11 gl = glCanvas.getGL();
        long dt = System.currentTimeMillis() - this.mStartTime;
        calculateVertex(glCanvas, left, top);
        gl.glPushMatrix();
        this.mRoatfArc = FloatAnim.getInterpolatedValue(180.0f, this.mRoatfArc - 180.0f, 1100, dt);
        gl.glTranslatef(0.0f, 0.0f, -0.3f);
        gl.glRotatef(this.mRoatfArc, 1.0f, 0.0f, 0.0f);
        gl.glTranslatef(0.0f, 0.0f, 0.3f);
        this.mCoordsBuffer.glTexturePointer();
        this.mVertexBuffer.glVertexPointer(ItemAnimation.CUR_ALPHA);
        if (this.mRoatfArc <= 90.0f) {
            gl.glBindTexture(3553, this.mStartTextureID);
            gl.glDrawArrays(FragmentManagerImpl.ANIM_STYLE_FADE_EXIT, 0, ItemAnimation.CUR_ARC);
        } else {
            gl.glTranslatef(0.0f, 0.0f, -0.3f);
            gl.glRotatef(-180.0f, 1.0f, 0.0f, 0.0f);
            gl.glTranslatef(0.0f, 0.0f, 0.3f);
            gl.glBindTexture(3553, this.mEndTextureID);
            gl.glDrawArrays(FragmentManagerImpl.ANIM_STYLE_FADE_EXIT, 0, ItemAnimation.CUR_ARC);
            gl.glTranslatef(0.0f, 0.0f, -0.3f);
            gl.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
            gl.glTranslatef(0.0f, 0.0f, 0.3f);
        }
        glCanvas.getTextureManager().setBoundTexture(null);
        gl.glPopMatrix();
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.mRoatfArc == 180.0f) {
            endAnimation(glCanvas);
        } else {
            invalidate();
        }
    }

    protected boolean onVisibilityChanged(boolean visibility) {
        return super.onVisibilityChanged(visibility);
    }

    public void setAnimationEndListener(AnimationListener listener) {
        this.mListener = listener;
    }

    public void setTextureIDs(int start, int end, float xCoords, float yCoords) {
        if (this.mAnimationMark) {
            endAnimation(glContext().glCanvas());
        }
        this.mStartTextureID = start;
        this.mEndTextureID = end;
        initCoords(xCoords, yCoords);
    }

    public void startAnimation(GLCanvas glCanvas) {
        setVisibility(true);
        if (this.mAnimationMark) {
            endAnimation(glCanvas);
        }
        this.mStartTime = System.currentTimeMillis();
        this.mRoatfArc = 0.0f;
        this.mAnimationMark = true;
        if (this.mListener != null) {
            this.mListener.onAnimationStart();
        }
    }
}