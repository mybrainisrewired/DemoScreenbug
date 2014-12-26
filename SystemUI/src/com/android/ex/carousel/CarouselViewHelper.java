package com.android.ex.carousel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.renderscript.Matrix4f;
import android.renderscript.Mesh;
import android.util.Log;
import com.android.ex.carousel.CarouselRS.CarouselCallback;

public class CarouselViewHelper implements CarouselCallback {
    private static final int REQUEST_DETAIL_TEXTURE_N = 2000000;
    private static final int REQUEST_END = 4000000;
    private static final int REQUEST_GEOMETRY_N = 3000000;
    private static final int REQUEST_TEXTURE_N = 1000000;
    private static final int SET_DETAIL_TEXTURE_N = 2;
    private static final int SET_GEOMETRY_N = 3;
    private static final int SET_MATRIX_N = 4;
    private static final int SET_TEXTURE_N = 1;
    private static final String TAG = "CarouselViewHelper";
    private boolean DBG;
    private long HOLDOFF_DELAY;
    private Handler mAsyncHandler;
    private CarouselView mCarouselView;
    private Context mContext;
    private HandlerThread mHandlerThread;
    private Handler mSyncHandler;

    class AsyncHandler extends Handler {
        AsyncHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            int id = msg.arg1;
            if (id >= CarouselViewHelper.this.mCarouselView.getCardCount()) {
                Log.e(TAG, "Index out of range for get, card:" + id);
            } else if (msg.what < 1000000 || msg.what > 4000000) {
                Log.e(TAG, "Unknown message: " + id);
            } else if (msg.what < 2000000) {
                bitmap = CarouselViewHelper.this.getTexture(id);
                if (bitmap != null) {
                    CarouselViewHelper.this.mSyncHandler.obtainMessage(SET_TEXTURE_N, id, 0, bitmap).sendToTarget();
                }
                com.android.ex.carousel.CarouselViewHelper.TextureParameters params = CarouselViewHelper.this.getTextureParameters(id);
                if (params != null) {
                    CarouselViewHelper.this.mSyncHandler.obtainMessage(SET_MATRIX_N, id, 0, params.matrix.getArray()).sendToTarget();
                }
            } else if (msg.what < 3000000) {
                bitmap = CarouselViewHelper.this.getDetailTexture(id);
                if (bitmap != null) {
                    CarouselViewHelper.this.mSyncHandler.obtainMessage(SET_DETAIL_TEXTURE_N, id, 0, bitmap).sendToTarget();
                }
            } else if (msg.what < 4000000) {
                Mesh mesh = CarouselViewHelper.this.getGeometry(id);
                if (mesh != null) {
                    CarouselViewHelper.this.mSyncHandler.obtainMessage(SET_GEOMETRY_N, id, 0, mesh).sendToTarget();
                }
            }
        }
    }

    public static class DetailTextureParameters {
        public float lineOffsetX;
        public float lineOffsetY;
        public float textureOffsetX;
        public float textureOffsetY;

        public DetailTextureParameters(float textureOffsetX, float textureOffsetY) {
            this.textureOffsetX = textureOffsetX;
            this.textureOffsetY = textureOffsetY;
            this.lineOffsetX = 0.0f;
            this.lineOffsetY = 0.0f;
        }

        public DetailTextureParameters(float textureOffsetX, float textureOffsetY, float lineOffsetX, float lineOffsetY) {
            this.textureOffsetX = textureOffsetX;
            this.textureOffsetY = textureOffsetY;
            this.lineOffsetX = lineOffsetX;
            this.lineOffsetY = lineOffsetY;
        }
    }

    class SyncHandler extends Handler {
        SyncHandler() {
        }

        public void handleMessage(Message msg) {
            float ly = 0.0f;
            int id = msg.arg1;
            if (id >= CarouselViewHelper.this.mCarouselView.getCardCount()) {
                Log.e(TAG, "Index out of range for set, card:" + id);
            } else {
                switch (msg.what) {
                    case SET_TEXTURE_N:
                        CarouselViewHelper.this.mCarouselView.setTextureForItem(id, (Bitmap) msg.obj);
                    case SET_DETAIL_TEXTURE_N:
                        float x;
                        float y;
                        float lx;
                        com.android.ex.carousel.CarouselViewHelper.DetailTextureParameters params = CarouselViewHelper.this.getDetailTextureParameters(id);
                        if (params != null) {
                            x = params.textureOffsetX;
                        } else {
                            x = 0.0f;
                        }
                        if (params != null) {
                            y = params.textureOffsetY;
                        } else {
                            y = 0.0f;
                        }
                        if (params != null) {
                            lx = params.lineOffsetX;
                        } else {
                            lx = 0.0f;
                        }
                        if (params != null) {
                            ly = params.lineOffsetY;
                        }
                        CarouselViewHelper.this.mCarouselView.setDetailTextureForItem(id, x, y, lx, ly, (Bitmap) msg.obj);
                    case SET_GEOMETRY_N:
                        CarouselViewHelper.this.mCarouselView.setGeometryForItem(id, (Mesh) msg.obj);
                    case SET_MATRIX_N:
                        CarouselViewHelper.this.mCarouselView.setMatrixForItem(id, (float[]) msg.obj);
                    default:
                        break;
                }
            }
        }
    }

    public static class TextureParameters {
        public Matrix4f matrix;

        public TextureParameters() {
            this.matrix = new Matrix4f();
        }

        public TextureParameters(Matrix4f _matrix) {
            this.matrix = _matrix;
        }
    }

    public CarouselViewHelper(Context context) {
        this.DBG = false;
        this.HOLDOFF_DELAY = 100;
        this.mContext = context;
        this.mHandlerThread = new HandlerThread("CarouselViewHelper.handler");
        this.mHandlerThread.start();
        this.mAsyncHandler = new AsyncHandler(this.mHandlerThread.getLooper());
        this.mSyncHandler = new SyncHandler();
    }

    public CarouselViewHelper(Context context, CarouselView carouselView) {
        this(context);
        setCarouselView(carouselView);
    }

    protected Handler getAsyncHandler() {
        return this.mAsyncHandler;
    }

    protected CarouselView getCarouselView() {
        return this.mCarouselView;
    }

    public Bitmap getDetailTexture(int id) {
        return null;
    }

    public DetailTextureParameters getDetailTextureParameters(int id) {
        return null;
    }

    public Mesh getGeometry(int id) {
        return null;
    }

    public Bitmap getTexture(int id) {
        return null;
    }

    public TextureParameters getTextureParameters(int id) {
        return null;
    }

    public void onAnimationFinished(float carouselRotationAngle) {
    }

    public void onAnimationStarted() {
    }

    public void onCardLongPress(int n, int[] touchPosition, Rect detailCoordinates) {
        if (this.DBG) {
            Log.v(TAG, "onCardLongPress(" + n + ", (" + touchPosition + "), (" + detailCoordinates + ") )");
        }
    }

    public void onCardSelected(int n) {
        if (this.DBG) {
            Log.v(TAG, "onCardSelected(" + n + ")");
        }
    }

    public void onDestroy() {
        this.mHandlerThread.quit();
    }

    public void onDetailSelected(int n, int x, int y) {
        if (this.DBG) {
            Log.v(TAG, "onDetailSelected(" + n + ", " + x + ", " + y + ")");
        }
    }

    public void onInvalidateDetailTexture(int id) {
        if (this.DBG) {
            Log.v(TAG, "onInvalidateDetailTexture(" + id + ")");
        }
        this.mAsyncHandler.removeMessages(2000000 + id);
    }

    public void onInvalidateGeometry(int id) {
        if (this.DBG) {
            Log.v(TAG, "onInvalidateGeometry(" + id + ")");
        }
        this.mAsyncHandler.removeMessages(3000000 + id);
    }

    public void onInvalidateTexture(int id) {
        if (this.DBG) {
            Log.v(TAG, "onInvalidateTexture(" + id + ")");
        }
        this.mAsyncHandler.removeMessages(1000000 + id);
    }

    public void onPause() {
        this.mCarouselView.pause();
    }

    public void onRequestDetailTexture(int id) {
        if (this.DBG) {
            Log.v(TAG, "onRequestDetailTexture(" + id + ")");
        }
        this.mAsyncHandler.removeMessages(2000000 + id);
        this.mAsyncHandler.sendMessageDelayed(this.mAsyncHandler.obtainMessage(2000000 + id, id, 0), this.HOLDOFF_DELAY);
    }

    public void onRequestGeometry(int id) {
        if (this.DBG) {
            Log.v(TAG, "onRequestGeometry(" + id + ")");
        }
        this.mAsyncHandler.removeMessages(3000000 + id);
        this.mAsyncHandler.sendMessage(this.mAsyncHandler.obtainMessage(3000000 + id, id, 0));
    }

    public void onRequestTexture(int id) {
        if (this.DBG) {
            Log.v(TAG, "onRequestTexture(" + id + ")");
        }
        this.mAsyncHandler.removeMessages(1000000 + id);
        this.mAsyncHandler.sendMessageDelayed(this.mAsyncHandler.obtainMessage(1000000 + id, id, 0), this.HOLDOFF_DELAY);
    }

    public void onResume() {
        this.mCarouselView.resume();
    }

    public void setCarouselView(CarouselView carouselView) {
        this.mCarouselView = carouselView;
        this.mCarouselView.setCallback(this);
    }
}