package com.android.ex.carousel;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.renderscript.Float4;
import android.renderscript.Mesh;
import android.renderscript.RenderScriptGL;
import com.android.ex.carousel.CarouselRS.CarouselCallback;
import com.android.systemui.recent.RecentsCallback;

public class CarouselController {
    private static final boolean DBG = false;
    public static final int STORE_CONFIG_ALPHA = 1;
    public static final int STORE_CONFIG_DEPTH_READS = 2;
    public static final int STORE_CONFIG_DEPTH_WRITES = 4;
    private static final String TAG = "CarouselController";
    private final int DEFAULT_DETAIL_ALIGNMENT;
    private final float DEFAULT_DRAG_FACTOR;
    private final float DEFAULT_FRICTION_COEFFICIENT;
    private final float DEFAULT_OVERSCROLL_SLOTS;
    private final int DEFAULT_PREFETCH_CARD_COUNT;
    private final float DEFAULT_RADIUS;
    private final int DEFAULT_ROW_COUNT;
    private final float DEFAULT_ROW_SPACING;
    private final int DEFAULT_SLOT_COUNT;
    private final float DEFAULT_SWAY_SENSITIVITY;
    private final int DEFAULT_VISIBLE_DETAIL_COUNT;
    private float[] mAt;
    private Bitmap mBackgroundBitmap;
    private Float4 mBackgroundColor;
    private int mCardCount;
    private long mCardCreationFadeDuration;
    private float mCardRotation;
    private boolean mCardsFaceTangent;
    private CarouselCallback mCarouselCallback;
    private float mCarouselRotationAngle;
    private Bitmap mDefaultBitmap;
    private float[] mDefaultCardMatrix;
    private int mDefaultGeometry;
    private Bitmap mDefaultLineBitmap;
    private Bitmap mDetailLoadingBitmap;
    private int mDetailTextureAlignment;
    private float mDragFactor;
    private int mDragModel;
    private boolean mDrawRuler;
    private float[] mEye;
    private long mFadeInDuration;
    private int mFillDirection;
    private boolean mFirstCardTop;
    private boolean mForceBlendCardsWithZ;
    private float mFrictionCoefficient;
    private Bitmap mLoadingBitmap;
    private int mLoadingGeometry;
    private float mOverscrollSlots;
    private int mPrefetchCardCount;
    private RenderScriptGL mRS;
    private float mRadius;
    private CarouselRS mRenderScript;
    private float mRezInCardCount;
    private int mRowCount;
    private float mRowSpacing;
    private int mSlotCount;
    private float mStartAngle;
    private int[] mStoreConfigs;
    private float mSwaySensitivity;
    private float[] mUp;
    private int mVisibleDetails;
    private int mVisibleSlots;

    public CarouselController() {
        this.DEFAULT_SLOT_COUNT = 10;
        this.DEFAULT_RADIUS = 20.0f;
        this.DEFAULT_VISIBLE_DETAIL_COUNT = 3;
        this.DEFAULT_PREFETCH_CARD_COUNT = 2;
        this.DEFAULT_ROW_COUNT = 1;
        this.DEFAULT_OVERSCROLL_SLOTS = 1.0f;
        this.DEFAULT_ROW_SPACING = 0.0f;
        this.DEFAULT_SWAY_SENSITIVITY = 0.0f;
        this.DEFAULT_FRICTION_COEFFICIENT = 10.0f;
        this.DEFAULT_DRAG_FACTOR = 0.25f;
        this.DEFAULT_DETAIL_ALIGNMENT = 514;
        this.mDefaultLineBitmap = Bitmap.createBitmap(new int[]{0, -1, 0}, 0, RecentsCallback.SWIPE_DOWN, 3, STORE_CONFIG_ALPHA, Config.ARGB_4444);
        this.mCardCount = 0;
        this.mVisibleSlots = 0;
        this.mVisibleDetails = 3;
        this.mPrefetchCardCount = 2;
        this.mDetailTextureAlignment = 514;
        this.mForceBlendCardsWithZ = false;
        this.mDrawRuler = true;
        this.mRadius = 20.0f;
        this.mCardRotation = 0.0f;
        this.mCardsFaceTangent = false;
        this.mOverscrollSlots = 1.0f;
        this.mSwaySensitivity = 0.0f;
        this.mFrictionCoefficient = 10.0f;
        this.mDragFactor = 0.25f;
        this.mSlotCount = 10;
        this.mRowCount = 1;
        this.mRowSpacing = 0.0f;
        this.mEye = new float[]{20.6829f, 2.77081f, 16.7314f};
        this.mAt = new float[]{14.7255f, -3.40001f, -1.30184f};
        this.mUp = new float[]{0.0f, 1.0f, 0.0f};
        this.mBackgroundColor = new Float4(0.0f, 0.0f, 0.0f, 1.0f);
        this.mRezInCardCount = 0.0f;
        this.mFadeInDuration = 250;
        this.mCardCreationFadeDuration = 0;
        this.mDetailLoadingBitmap = Bitmap.createBitmap(new int[]{0}, 0, 1, 1, STORE_CONFIG_ALPHA, Config.ARGB_4444);
        this.mDragModel = 0;
        this.mFillDirection = 1;
        this.mFirstCardTop = false;
    }

    public void createCards(int n) {
        this.mCardCount = n;
        if (this.mRenderScript != null) {
            this.mRenderScript.createCards(n);
        }
    }

    public CarouselCallback getCallback() {
        return this.mCarouselCallback;
    }

    public int getCardCount() {
        return this.mCardCount;
    }

    public void invalidateDetailTexture(int n, boolean eraseCurrent) {
        if (this.mRenderScript != null && this.mRS != null) {
            this.mRenderScript.invalidateDetailTexture(n, eraseCurrent);
        }
    }

    public void invalidateTexture(int n, boolean eraseCurrent) {
        if (this.mRenderScript != null && this.mRS != null) {
            this.mRenderScript.invalidateTexture(n, eraseCurrent);
        }
    }

    public Mesh loadGeometry(int resId) {
        return this.mRenderScript != null ? this.mRenderScript.loadGeometry(resId) : null;
    }

    public void onLongPress() {
        this.mRenderScript.doLongPress();
    }

    public void onSurfaceChanged() {
        setSlotCount(this.mSlotCount);
        setDefaultCardMatrix(this.mDefaultCardMatrix);
        createCards(this.mCardCount);
        setVisibleSlots(this.mVisibleSlots);
        setVisibleDetails(this.mVisibleDetails);
        setPrefetchCardCount(this.mPrefetchCardCount);
        setOverscrollSlots(this.mOverscrollSlots);
        setRowCount(this.mRowCount);
        setRowSpacing(this.mRowSpacing);
        setFirstCardTop(this.mFirstCardTop);
        setDetailTextureAlignment(this.mDetailTextureAlignment);
        setForceBlendCardsWithZ(this.mForceBlendCardsWithZ);
        setDrawRuler(this.mDrawRuler);
        setCallback(this.mCarouselCallback);
        setDefaultBitmap(this.mDefaultBitmap);
        setLoadingBitmap(this.mLoadingBitmap);
        setDefaultGeometry(this.mDefaultGeometry);
        setLoadingGeometry(this.mLoadingGeometry);
        setBackgroundColor(this.mBackgroundColor.x, this.mBackgroundColor.y, this.mBackgroundColor.z, this.mBackgroundColor.w);
        setBackgroundBitmap(this.mBackgroundBitmap);
        setDetailLineBitmap(this.mDefaultLineBitmap);
        setStartAngle(this.mStartAngle);
        setCarouselRotationAngle(this.mCarouselRotationAngle);
        setRadius(this.mRadius);
        setCardRotation(this.mCardRotation);
        setCardsFaceTangent(this.mCardsFaceTangent);
        setSwaySensitivity(this.mSwaySensitivity);
        setFrictionCoefficient(this.mFrictionCoefficient);
        setDragFactor(this.mDragFactor);
        setDragModel(this.mDragModel);
        setFillDirection(this.mFillDirection);
        setLookAt(this.mEye, this.mAt, this.mUp);
        setRezInCardCount(this.mRezInCardCount);
        setFadeInDuration(this.mFadeInDuration);
        setCardCreationFadeDuration(this.mCardCreationFadeDuration);
        setDetailLoadingBitmap(this.mDetailLoadingBitmap);
        setStoreConfigs(this.mStoreConfigs);
    }

    public void onTouchMoved(float x, float y, long t) {
        this.mRenderScript.doMotion(x, y, t);
    }

    public void onTouchStarted(float x, float y, long t) {
        this.mRenderScript.doStart(x, y, t);
    }

    public void onTouchStopped(float x, float y, long t) {
        this.mRenderScript.doStop(x, y, t);
    }

    public void setBackgroundBitmap(Bitmap bitmap) {
        this.mBackgroundBitmap = bitmap;
        if (this.mRenderScript != null) {
            this.mRenderScript.setBackgroundTexture(bitmap);
        }
    }

    public void setBackgroundColor(float red, float green, float blue, float alpha) {
        this.mBackgroundColor = new Float4(red, green, blue, alpha);
        if (this.mRenderScript != null) {
            this.mRenderScript.setBackgroundColor(this.mBackgroundColor);
        }
    }

    public void setCallback(CarouselCallback callback) {
        this.mCarouselCallback = callback;
        if (this.mRenderScript != null) {
            this.mRenderScript.setCallback(callback);
        }
    }

    public void setCardCreationFadeDuration(long t) {
        this.mCardCreationFadeDuration = t;
        if (this.mRenderScript != null) {
            this.mRenderScript.setCardCreationFadeDuration(t);
        }
    }

    public void setCardRotation(float cardRotation) {
        this.mCardRotation = cardRotation;
        if (this.mRenderScript != null) {
            this.mRenderScript.setCardRotation(cardRotation);
        }
    }

    public void setCardsFaceTangent(boolean faceTangent) {
        this.mCardsFaceTangent = faceTangent;
        if (this.mRenderScript != null) {
            this.mRenderScript.setCardsFaceTangent(faceTangent);
        }
    }

    public void setCarouselRotationAngle(float angle) {
        this.mCarouselRotationAngle = angle;
        if (this.mRenderScript != null) {
            this.mRenderScript.setCarouselRotationAngle(angle);
        }
    }

    public void setCarouselRotationAngle(float endAngle, int milliseconds, int interpolationMode, float maxAnimatedArc) {
        if (this.mRenderScript != null) {
            this.mRenderScript.setCarouselRotationAngle(endAngle, milliseconds, interpolationMode, maxAnimatedArc);
        }
    }

    public void setDefaultBitmap(Bitmap bitmap) {
        this.mDefaultBitmap = bitmap;
        if (this.mRenderScript != null) {
            this.mRenderScript.setDefaultBitmap(bitmap);
        }
    }

    public void setDefaultCardMatrix(float[] matrix) {
        this.mDefaultCardMatrix = matrix;
        if (this.mRenderScript != null) {
            this.mRenderScript.setDefaultCardMatrix(matrix);
        }
    }

    public void setDefaultGeometry(int resId) {
        this.mDefaultGeometry = resId;
        if (this.mRenderScript != null) {
            this.mRenderScript.setDefaultGeometry(this.mRenderScript.loadGeometry(resId));
        }
    }

    public void setDetailLineBitmap(Bitmap bitmap) {
        this.mDefaultLineBitmap = bitmap;
        if (this.mRenderScript != null) {
            this.mRenderScript.setDetailLineTexture(bitmap);
        }
    }

    public void setDetailLoadingBitmap(Bitmap bitmap) {
        this.mDetailLoadingBitmap = bitmap;
        if (this.mRenderScript != null) {
            this.mRenderScript.setDetailLoadingTexture(bitmap);
        }
    }

    public void setDetailTextureAlignment(int alignment) {
        int xBits = alignment & 65280;
        if (xBits == 0 || ((xBits - 1) & xBits) != 0) {
            throw new IllegalArgumentException("Must specify exactly one horizontal alignment flag");
        }
        int yBits = alignment & 255;
        if (yBits == 0 || ((yBits - 1) & yBits) != 0) {
            throw new IllegalArgumentException("Must specify exactly one vertical alignment flag");
        }
        this.mDetailTextureAlignment = alignment;
        if (this.mRenderScript != null) {
            this.mRenderScript.setDetailTextureAlignment(alignment);
        }
    }

    public void setDetailTextureForItem(int n, float offx, float offy, float loffx, float loffy, Bitmap bitmap) {
        if (this.mRenderScript != null && this.mRS != null) {
            this.mRenderScript.setDetailTexture(n, offx, offy, loffx, loffy, bitmap);
        }
    }

    public void setDragFactor(float dragFactor) {
        this.mDragFactor = dragFactor;
        if (this.mRenderScript != null) {
            this.mRenderScript.setDragFactor(dragFactor);
        }
    }

    public void setDragModel(int model) {
        this.mDragModel = model;
        if (this.mRenderScript != null) {
            this.mRenderScript.setDragModel(model);
        }
    }

    public void setDrawRuler(boolean drawRuler) {
        this.mDrawRuler = drawRuler;
        if (this.mRenderScript != null) {
            this.mRenderScript.setDrawRuler(drawRuler);
        }
    }

    public void setFadeInDuration(long t) {
        this.mFadeInDuration = t;
        if (this.mRenderScript != null) {
            this.mRenderScript.setFadeInDuration(t);
        }
    }

    public void setFillDirection(int direction) {
        this.mFillDirection = direction;
        if (this.mRenderScript != null) {
            this.mRenderScript.setFillDirection(direction);
        }
    }

    public void setFirstCardTop(boolean f) {
        this.mFirstCardTop = f;
        if (this.mRenderScript != null) {
            this.mRenderScript.setFirstCardTop(f);
        }
    }

    public void setForceBlendCardsWithZ(boolean enabled) {
        this.mForceBlendCardsWithZ = enabled;
        if (this.mRenderScript != null) {
            this.mRenderScript.setForceBlendCardsWithZ(enabled);
        }
    }

    public void setFrictionCoefficient(float frictionCoefficient) {
        this.mFrictionCoefficient = frictionCoefficient;
        if (this.mRenderScript != null) {
            this.mRenderScript.setFrictionCoefficient(frictionCoefficient);
        }
    }

    public void setGeometryForItem(int n, int resId) {
        if (this.mRenderScript != null) {
            this.mRenderScript.setGeometry(n, this.mRenderScript.loadGeometry(resId));
        }
    }

    public void setGeometryForItem(int n, Mesh mesh) {
        if (this.mRenderScript != null) {
            this.mRenderScript.setGeometry(n, mesh);
        }
    }

    public void setLoadingBitmap(Bitmap bitmap) {
        this.mLoadingBitmap = bitmap;
        if (this.mRenderScript != null) {
            this.mRenderScript.setLoadingBitmap(bitmap);
        }
    }

    public void setLoadingGeometry(int resId) {
        this.mLoadingGeometry = resId;
        if (this.mRenderScript != null) {
            this.mRenderScript.setLoadingGeometry(this.mRenderScript.loadGeometry(resId));
        }
    }

    public void setLookAt(float[] eye, float[] at, float[] up) {
        this.mEye = eye;
        this.mAt = at;
        this.mUp = up;
        if (this.mRenderScript != null) {
            this.mRenderScript.setLookAt(eye, at, up);
        }
    }

    public void setMatrixForItem(int n, float[] matrix) {
        if (this.mRenderScript != null) {
            this.mRenderScript.setMatrix(n, matrix);
        }
    }

    public void setOverscrollSlots(float slots) {
        this.mOverscrollSlots = slots;
        if (this.mRenderScript != null) {
            this.mRenderScript.setOverscrollSlots(slots);
        }
    }

    public void setPrefetchCardCount(int n) {
        this.mPrefetchCardCount = n;
        if (this.mRenderScript != null) {
            this.mRenderScript.setPrefetchCardCount(n);
        }
    }

    public void setRS(RenderScriptGL rs, CarouselRS renderScript) {
        this.mRS = rs;
        this.mRenderScript = renderScript;
    }

    public void setRadius(float radius) {
        this.mRadius = radius;
        if (this.mRenderScript != null) {
            this.mRenderScript.setRadius(radius);
        }
    }

    public void setRezInCardCount(float n) {
        this.mRezInCardCount = n;
        if (this.mRenderScript != null) {
            this.mRenderScript.setRezInCardCount(n);
        }
    }

    public void setRowCount(int n) {
        this.mRowCount = n;
        if (this.mRenderScript != null) {
            this.mRenderScript.setRowCount(n);
        }
    }

    public void setRowSpacing(float s) {
        this.mRowSpacing = s;
        if (this.mRenderScript != null) {
            this.mRenderScript.setRowSpacing(s);
        }
    }

    public void setSlotCount(int n) {
        this.mSlotCount = n;
        if (this.mRenderScript != null) {
            this.mRenderScript.setSlotCount(n);
        }
    }

    public void setStartAngle(float angle) {
        this.mStartAngle = angle;
        if (this.mRenderScript != null) {
            this.mRenderScript.setStartAngle(angle);
        }
    }

    public void setStoreConfigs(int[] configs) {
        this.mStoreConfigs = configs;
        if (this.mRenderScript != null) {
            this.mRenderScript.setStoreConfigs(configs);
        }
    }

    public void setSwaySensitivity(float swaySensitivity) {
        this.mSwaySensitivity = swaySensitivity;
        if (this.mRenderScript != null) {
            this.mRenderScript.setSwaySensitivity(swaySensitivity);
        }
    }

    public void setTextureForItem(int n, Bitmap bitmap) {
        if (this.mRenderScript != null && this.mRS != null) {
            this.mRenderScript.setTexture(n, bitmap);
        }
    }

    public void setVisibleDetails(int n) {
        this.mVisibleDetails = n;
        if (this.mRenderScript != null) {
            this.mRenderScript.setVisibleDetails(n);
        }
    }

    public void setVisibleSlots(int n) {
        this.mVisibleSlots = n;
        if (this.mRenderScript != null) {
            this.mRenderScript.setVisibleSlots(n);
        }
    }
}