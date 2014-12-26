package com.wmt.opengl;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import com.wmt.data.LocalAudioAll;
import com.wmt.opengl.grid.ItemAnimation;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public final class Utils {
    public static final FloatBuffer sReflectionBuffer;
    private static float[] sReflectionColors;
    public static final Paint s_antiAliasPaint;

    static {
        sReflectionColors = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.8f, 0.8f, 0.8f, 0.8f, 0.8f, 0.8f, 0.8f, 0.8f, 0.0f, 0.0f, 0.0f, 0.0f};
        sReflectionBuffer = allocFloatBuffer(sReflectionColors);
        s_antiAliasPaint = new Paint();
        s_antiAliasPaint.setAntiAlias(true);
    }

    public static FloatBuffer allocFloatBuffer(float[] data) {
        ByteBuffer bb = ByteBuffer.allocateDirect(data.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer ret = bb.asFloatBuffer();
        ret.put(data);
        ret.position(0);
        return ret;
    }

    public static ShortBuffer allocShortBuffer(short[] data) {
        ByteBuffer bb = ByteBuffer.allocateDirect(data.length * 2);
        bb.order(ByteOrder.nativeOrder());
        ShortBuffer ret = bb.asShortBuffer();
        ret.put(data);
        ret.position(0);
        return ret;
    }

    public static void checkGLError(GL10 gl) {
        int glError = gl.glGetError();
        if (glError != 0) {
            Exception e = new Exception("Debug");
            e.fillInStackTrace();
            Log.w("Utils", "checkGLError: 0x" + Integer.toHexString(glError), e);
        }
    }

    public static void endGLReflection(GL11 gl11) {
        gl11.glDisableClientState(32886);
        gl11.glPopMatrix();
    }

    public static boolean isPowerOf2(int n) {
        return ((-n) & n) == n;
    }

    public static boolean isSameSign(int a, int b) {
        switch ((a ^ b) >> 31) {
            case LocalAudioAll.SORT_BY_DATE:
                return true;
            default:
                return false;
        }
    }

    public static String makeString(String oldString) {
        return oldString.replaceAll("/", "");
    }

    public static int nextPowerOf2(int n) {
        n--;
        n |= n >>> 16;
        n |= n >>> 8;
        n |= n >>> 4;
        n |= n >>> 2;
        return n | (n >>> 1) + 1;
    }

    public static final Bitmap preapareGLBitmap(Bitmap bitmap, Texture texture) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (isPowerOf2(width) && isPowerOf2(height)) {
            texture.mNormalizedWidth = 1.0f;
            texture.mNormalizedHeight = 1.0f;
            return bitmap;
        } else {
            int paddedWidth = nextPowerOf2(width);
            int paddedHeight = nextPowerOf2(height);
            Config config = bitmap.getConfig();
            if (config == null) {
                config = Config.ARGB_8888;
            }
            Bitmap padded = Bitmap.createBitmap(paddedWidth, paddedHeight, config);
            Canvas canvas = new Canvas(padded);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
            bitmap.recycle();
            bitmap = padded;
            texture.mNormalizedWidth = ((float) width) / ((float) paddedWidth);
            texture.mNormalizedHeight = ((float) height) / ((float) paddedHeight);
            canvas.setBitmap(com.wmt.util.Utils.s_nullBitmap);
            return bitmap;
        }
    }

    public static final Bitmap resizeBitmap(Bitmap bitmap, int maxSize) {
        int srcWidth = bitmap.getWidth();
        int srcHeight = bitmap.getHeight();
        int width = maxSize;
        int height = maxSize;
        boolean needsResize = false;
        if (srcWidth > srcHeight) {
            if (srcWidth > maxSize) {
                needsResize = true;
                height = (maxSize * srcHeight) / srcWidth;
            }
        } else if (srcHeight > maxSize) {
            needsResize = true;
            width = (maxSize * srcWidth) / srcHeight;
        }
        return needsResize ? Bitmap.createScaledBitmap(bitmap, width, height, true) : bitmap;
    }

    public static void startGLReflection(GLCanvas glCanvas, float refCenterX, float refCenterY, float z, FloatBuffer colorsBuffer) {
        FloatBuffer buffer = colorsBuffer;
        if (buffer == null) {
            buffer = sReflectionBuffer;
        }
        GL11 gl = glCanvas.getGL();
        float reflectionGLX = glCanvas.getXPos2GL(refCenterX, z);
        float reflectionGLY = glCanvas.getYPos2GL(refCenterY, z);
        gl.glPushMatrix();
        gl.glEnableClientState(32886);
        gl.glColorPointer(ItemAnimation.CUR_ARC, 5126, 0, buffer);
        gl.glTexEnvf(8960, 8704, 8448.0f);
        gl.glTranslatef(reflectionGLX, reflectionGLY, z);
        gl.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
        gl.glTranslatef(-reflectionGLX, -reflectionGLY, -z);
    }
}