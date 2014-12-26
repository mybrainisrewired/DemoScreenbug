package com.android.ex.carousel;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Rect;
import android.renderscript.Allocation;
import android.renderscript.Allocation.MipmapControl;
import android.renderscript.Element;
import android.renderscript.FileA3D;
import android.renderscript.FileA3D.EntryType;
import android.renderscript.FileA3D.IndexEntry;
import android.renderscript.Float4;
import android.renderscript.Matrix4f;
import android.renderscript.Mesh;
import android.renderscript.Program.TextureType;
import android.renderscript.ProgramFragment;
import android.renderscript.ProgramFragment.Builder;
import android.renderscript.ProgramRaster;
import android.renderscript.ProgramStore;
import android.renderscript.ProgramStore.BlendDstFunc;
import android.renderscript.ProgramStore.BlendSrcFunc;
import android.renderscript.ProgramStore.DepthFunc;
import android.renderscript.ProgramVertex;
import android.renderscript.ProgramVertexFixedFunction;
import android.renderscript.ProgramVertexFixedFunction.Constants;
import android.renderscript.RenderScript;
import android.renderscript.RenderScript.RSMessageHandler;
import android.renderscript.RenderScriptGL;
import android.renderscript.Sampler;
import android.util.Log;
import com.android.ex.carousel.ScriptField_Card.Item;
import com.android.systemui.statusbar.CommandQueue;

public class CarouselRS {
    public static final int CMD_ANIMATION_FINISHED = 500;
    public static final int CMD_ANIMATION_STARTED = 400;
    public static final int CMD_CARD_LONGPRESS = 110;
    public static final int CMD_CARD_SELECTED = 100;
    public static final int CMD_DETAIL_SELECTED = 105;
    public static final int CMD_INVALIDATE_DETAIL_TEXTURE = 610;
    public static final int CMD_INVALIDATE_GEOMETRY = 310;
    public static final int CMD_INVALIDATE_TEXTURE = 210;
    public static final int CMD_PING = 1000;
    public static final int CMD_REQUEST_DETAIL_TEXTURE = 600;
    public static final int CMD_REQUEST_GEOMETRY = 300;
    public static final int CMD_REQUEST_TEXTURE = 200;
    private static final boolean DBG = false;
    private static final int DEFAULT_CARD_COUNT = 0;
    private static final int DEFAULT_ROW_COUNT = 1;
    private static final int DEFAULT_SLOT_COUNT = 10;
    private static final int DEFAULT_VISIBLE_SLOTS = 1;
    public static final int DRAG_MODEL_CYLINDER_INSIDE = 2;
    public static final int DRAG_MODEL_CYLINDER_OUTSIDE = 3;
    public static final int DRAG_MODEL_PLANE = 1;
    public static final int DRAG_MODEL_SCREEN_DELTA = 0;
    public static final int FILL_DIRECTION_CCW = 1;
    public static final int FILL_DIRECTION_CW = -1;
    private static final MipmapControl MIPMAP;
    private static final String TAG = "CarouselRS";
    private static final String mMultiTextureBlendingShader;
    private static final String mMultiTextureShader;
    private static final String mSingleTextureBlendingShader;
    private static final String mSingleTextureShader;
    private Allocation[] mAllocationPool;
    private float[] mAtPoint;
    private CarouselCallback mCallback;
    private ScriptField_Card mCards;
    private float[] mEyePoint;
    private ScriptField_FragmentShaderConstants_s mFSConst;
    private boolean mForceBlendCardsWithZ;
    private ProgramFragment mMultiTextureBlendingFragmentProgram;
    private ProgramFragment mMultiTextureFragmentProgram;
    private int mPrefetchCardCount;
    private ScriptField_ProgramStore_s mProgramStoresCard;
    private RenderScriptGL mRS;
    private ProgramRaster mRasterProgram;
    private Resources mRes;
    private int mRowCount;
    private RSMessageHandler mRsMessage;
    private ScriptC_carousel mScript;
    private ProgramFragment mSingleTextureBlendingFragmentProgram;
    private ProgramFragment mSingleTextureFragmentProgram;
    private float[] mUp;
    private ProgramVertex mVertexProgram;
    private int mVisibleSlots;

    public static interface CarouselCallback {
        void onAnimationFinished(float f);

        void onAnimationStarted();

        void onCardLongPress(int i, int[] iArr, Rect rect);

        void onCardSelected(int i);

        void onDetailSelected(int i, int i2, int i3);

        void onInvalidateDetailTexture(int i);

        void onInvalidateGeometry(int i);

        void onInvalidateTexture(int i);

        void onRequestDetailTexture(int i);

        void onRequestGeometry(int i);

        void onRequestTexture(int i);
    }

    static {
        MIPMAP = MipmapControl.MIPMAP_NONE;
        mSingleTextureShader = new String("varying vec2 varTex0;void main() {vec2 t0 = varTex0.xy;vec4 col = texture2D(UNI_Tex0, t0);gl_FragColor = col; }");
        mSingleTextureBlendingShader = new String("varying vec2 varTex0;void main() {vec2 t0 = varTex0.xy;vec4 col = texture2D(UNI_Tex0, t0);gl_FragColor = col * UNI_overallAlpha; }");
        mMultiTextureShader = new String("varying vec2 varTex0;void main() {vec2 t0 = varTex0.xy;vec4 col = texture2D(UNI_Tex0, t0);vec4 col2 = texture2D(UNI_Tex1, t0);gl_FragColor = mix(col, col2, UNI_fadeAmount);}");
        mMultiTextureBlendingShader = new String("varying vec2 varTex0;void main() {vec2 t0 = varTex0.xy;vec4 col = texture2D(UNI_Tex0, t0);vec4 col2 = texture2D(UNI_Tex1, t0);gl_FragColor = mix(col, col2, UNI_fadeAmount) * UNI_overallAlpha;}");
    }

    public CarouselRS(RenderScriptGL rs, Resources res, int resId) {
        this.mEyePoint = new float[]{2.0f, 0.0f, 0.0f};
        this.mAtPoint = new float[]{0.0f, 0.0f, 0.0f};
        this.mUp = new float[]{0.0f, 1.0f, 0.0f};
        this.mRsMessage = new RSMessageHandler() {
            public void run() {
                if (CarouselRS.this.mCallback != null) {
                    switch (this.mID) {
                        case CMD_CARD_SELECTED:
                            CarouselRS.this.mCallback.onCardSelected(this.mData[0]);
                        case CMD_DETAIL_SELECTED:
                            CarouselRS.this.mCallback.onDetailSelected(this.mData[0], this.mData[1], this.mData[2]);
                        case CMD_CARD_LONGPRESS:
                            CarouselRS.this.mCallback.onCardLongPress(this.mData[0], new int[]{this.mData[1], this.mData[2]}, new Rect(this.mData[3], this.mData[4], this.mData[5], this.mData[6]));
                        case CMD_REQUEST_TEXTURE:
                            CarouselRS.this.mCallback.onRequestTexture(this.mData[0]);
                        case CMD_INVALIDATE_TEXTURE:
                            CarouselRS.this.setTexture(this.mData[0], null);
                            CarouselRS.this.mCallback.onInvalidateTexture(this.mData[0]);
                        case CMD_REQUEST_GEOMETRY:
                            CarouselRS.this.mCallback.onRequestGeometry(this.mData[0]);
                        case CMD_INVALIDATE_GEOMETRY:
                            CarouselRS.this.setGeometry(this.mData[0], null);
                            CarouselRS.this.mCallback.onInvalidateGeometry(this.mData[0]);
                        case CMD_ANIMATION_STARTED:
                            CarouselRS.this.mCallback.onAnimationStarted();
                        case CMD_ANIMATION_FINISHED:
                            CarouselRS.this.mCallback.onAnimationFinished(Float.intBitsToFloat(this.mData[0]));
                        case CMD_REQUEST_DETAIL_TEXTURE:
                            CarouselRS.this.mCallback.onRequestDetailTexture(this.mData[0]);
                        case CMD_INVALIDATE_DETAIL_TEXTURE:
                            CarouselRS.this.setDetailTexture(this.mData[0], 0.0f, 0.0f, 0.0f, 0.0f, null);
                            CarouselRS.this.mCallback.onInvalidateDetailTexture(this.mData[0]);
                        case CMD_PING:
                            break;
                        default:
                            Log.e(TAG, "Unknown RSMessage: " + this.mID);
                    }
                }
            }
        };
        this.mRS = rs;
        this.mRes = res;
        this.mScript = new ScriptC_carousel(this.mRS, this.mRes, resId);
        this.mRS.setMessageHandler(this.mRsMessage);
        initProgramStore();
        initFragmentProgram();
        initRasterProgram();
        initVertexProgram();
        setSlotCount(DEFAULT_SLOT_COUNT);
        setVisibleSlots(FILL_DIRECTION_CCW);
        setRowCount(FILL_DIRECTION_CCW);
        createCards(DRAG_MODEL_SCREEN_DELTA);
        setStartAngle(0.0f);
        setCarouselRotationAngle(0.0f);
        setRadius(1.0f);
        setLookAt(this.mEyePoint, this.mAtPoint, this.mUp);
        setRadius(20.0f);
    }

    private Allocation allocationFromBitmap(Bitmap bitmap, MipmapControl mipmap) {
        return bitmap == null ? null : Allocation.createFromBitmap(this.mRS, bitmap, mipmap, DRAG_MODEL_CYLINDER_INSIDE);
    }

    private Allocation allocationFromPool(int n, Bitmap bitmap, MipmapControl mipmap) {
        int count = (this.mVisibleSlots + (this.mPrefetchCardCount * 2)) * this.mRowCount;
        if (this.mAllocationPool == null || this.mAllocationPool.length != count) {
            Allocation[] tmp = new Allocation[count];
            int oldsize = this.mAllocationPool == null ? DRAG_MODEL_SCREEN_DELTA : this.mAllocationPool.length;
            int i = DRAG_MODEL_SCREEN_DELTA;
            while (i < Math.min(count, oldsize)) {
                tmp[i] = this.mAllocationPool[i];
                i++;
            }
            this.mAllocationPool = tmp;
        }
        Allocation allocation = this.mAllocationPool[n % count];
        if (allocation == null) {
            allocation = allocationFromBitmap(bitmap, mipmap);
            this.mAllocationPool[n % count] = allocation;
            return allocation;
        } else if (bitmap == null) {
            return allocation;
        } else {
            if (bitmap.getWidth() == allocation.getType().getX() && bitmap.getHeight() == allocation.getType().getY()) {
                allocation.copyFrom(bitmap);
                return allocation;
            } else {
                Log.v(TAG, "Warning, bitmap has different size. Taking slow path");
                allocation = allocationFromBitmap(bitmap, mipmap);
                this.mAllocationPool[n % count] = allocation;
                return allocation;
            }
        }
    }

    private Element elementForBitmap(Bitmap bitmap, Config defaultConfig) {
        Config config = bitmap.getConfig();
        if (config == null) {
            config = defaultConfig;
        }
        if (config == Config.ALPHA_8) {
            return Element.A_8(this.mRS);
        }
        if (config == Config.RGB_565) {
            return Element.RGB_565(this.mRS);
        }
        if (config == Config.ARGB_4444) {
            return Element.RGBA_4444(this.mRS);
        }
        if (config == Config.ARGB_8888) {
            return Element.RGBA_8888(this.mRS);
        }
        throw new IllegalArgumentException("Unknown configuration");
    }

    private Item getCard(int n) {
        try {
            return this.mCards.get(n);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    private Item getOrCreateCard(int n) {
        Item item = getCard(n);
        return item == null ? new Item() : item;
    }

    private void initFragmentProgram() {
        Builder pfbSingle = new Builder(this.mRS);
        pfbSingle.setShader(mSingleTextureShader);
        pfbSingle.addTexture(TextureType.TEXTURE_2D);
        this.mSingleTextureFragmentProgram = pfbSingle.create();
        this.mSingleTextureFragmentProgram.bindSampler(Sampler.CLAMP_LINEAR(this.mRS), DRAG_MODEL_SCREEN_DELTA);
        this.mFSConst = new ScriptField_FragmentShaderConstants_s(this.mRS, 1);
        this.mScript.bind_shaderConstants(this.mFSConst);
        Builder pfbSingleBlend = new Builder(this.mRS);
        pfbSingleBlend.setShader(mSingleTextureBlendingShader);
        pfbSingleBlend.addTexture(TextureType.TEXTURE_2D);
        pfbSingleBlend.addConstant(this.mFSConst.getAllocation().getType());
        this.mSingleTextureBlendingFragmentProgram = pfbSingleBlend.create();
        this.mSingleTextureBlendingFragmentProgram.bindConstants(this.mFSConst.getAllocation(), DRAG_MODEL_SCREEN_DELTA);
        this.mSingleTextureBlendingFragmentProgram.bindSampler(Sampler.CLAMP_LINEAR(this.mRS), DRAG_MODEL_SCREEN_DELTA);
        Builder pfbMulti = new Builder(this.mRS);
        pfbMulti.setShader(mMultiTextureShader);
        pfbMulti.addTexture(TextureType.TEXTURE_2D);
        pfbMulti.addTexture(TextureType.TEXTURE_2D);
        pfbMulti.addConstant(this.mFSConst.getAllocation().getType());
        this.mMultiTextureFragmentProgram = pfbMulti.create();
        this.mMultiTextureFragmentProgram.bindConstants(this.mFSConst.getAllocation(), DRAG_MODEL_SCREEN_DELTA);
        this.mMultiTextureFragmentProgram.bindSampler(Sampler.CLAMP_LINEAR(this.mRS), DRAG_MODEL_SCREEN_DELTA);
        this.mMultiTextureFragmentProgram.bindSampler(Sampler.CLAMP_LINEAR(this.mRS), FILL_DIRECTION_CCW);
        Builder pfbMultiBlend = new Builder(this.mRS);
        pfbMultiBlend.setShader(mMultiTextureBlendingShader);
        pfbMultiBlend.addTexture(TextureType.TEXTURE_2D);
        pfbMultiBlend.addTexture(TextureType.TEXTURE_2D);
        pfbMultiBlend.addConstant(this.mFSConst.getAllocation().getType());
        this.mMultiTextureBlendingFragmentProgram = pfbMultiBlend.create();
        this.mMultiTextureBlendingFragmentProgram.bindConstants(this.mFSConst.getAllocation(), DRAG_MODEL_SCREEN_DELTA);
        this.mMultiTextureBlendingFragmentProgram.bindSampler(Sampler.CLAMP_LINEAR(this.mRS), DRAG_MODEL_SCREEN_DELTA);
        this.mMultiTextureBlendingFragmentProgram.bindSampler(Sampler.CLAMP_LINEAR(this.mRS), FILL_DIRECTION_CCW);
        this.mScript.set_linearClamp(Sampler.CLAMP_LINEAR(this.mRS));
        this.mScript.set_singleTextureFragmentProgram(this.mSingleTextureFragmentProgram);
        this.mScript.set_singleTextureBlendingFragmentProgram(this.mSingleTextureBlendingFragmentProgram);
        this.mScript.set_multiTextureFragmentProgram(this.mMultiTextureFragmentProgram);
        this.mScript.set_multiTextureBlendingFragmentProgram(this.mMultiTextureBlendingFragmentProgram);
    }

    private void initProgramStore() {
        resizeProgramStoresCard(FILL_DIRECTION_CCW);
        DepthFunc depthFunc = this.mForceBlendCardsWithZ ? DepthFunc.LESS : DepthFunc.ALWAYS;
        this.mScript.set_programStoreBackground(new ProgramStore.Builder(this.mRS).setBlendFunc(BlendSrcFunc.ONE, BlendDstFunc.ZERO).setDitherEnabled(true).setDepthFunc(depthFunc).setDepthMaskEnabled(this.mForceBlendCardsWithZ).create());
        setProgramStoreCard(DRAG_MODEL_SCREEN_DELTA, new ProgramStore.Builder(this.mRS).setBlendFunc(BlendSrcFunc.ONE, BlendDstFunc.ONE_MINUS_SRC_ALPHA).setDitherEnabled(true).setDepthFunc(depthFunc).setDepthMaskEnabled(this.mForceBlendCardsWithZ).create());
        this.mScript.set_programStoreDetail(new ProgramStore.Builder(this.mRS).setBlendFunc(BlendSrcFunc.ONE, BlendDstFunc.ONE_MINUS_SRC_ALPHA).setDitherEnabled(true).setDepthFunc(DepthFunc.ALWAYS).setDepthMaskEnabled(DBG).create());
    }

    private void initRasterProgram() {
        this.mRasterProgram = new ProgramRaster.Builder(this.mRS).create();
        this.mScript.set_rasterProgram(this.mRasterProgram);
    }

    private void initVertexProgram() {
        this.mVertexProgram = new ProgramVertexFixedFunction.Builder(this.mRS).create();
        Constants pva = new Constants(this.mRS);
        ((ProgramVertexFixedFunction) this.mVertexProgram).bindConstants(pva);
        Matrix4f proj = new Matrix4f();
        proj.loadProjectionNormalized(FILL_DIRECTION_CCW, FILL_DIRECTION_CCW);
        pva.setProjection(proj);
        this.mScript.set_vertexProgram(this.mVertexProgram);
    }

    private Matrix4f matrixFromFloat(float[] matrix) {
        int dimensions;
        if (matrix == null || matrix.length == 0) {
            dimensions = DRAG_MODEL_SCREEN_DELTA;
        } else if (matrix.length == 16) {
            dimensions = CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL;
        } else if (matrix.length == 9) {
            dimensions = DRAG_MODEL_CYLINDER_OUTSIDE;
        } else {
            throw new IllegalArgumentException("matrix length not 0,9 or 16");
        }
        Matrix4f rsMatrix = new Matrix4f();
        int i = DRAG_MODEL_SCREEN_DELTA;
        while (i < dimensions) {
            int j = DRAG_MODEL_SCREEN_DELTA;
            while (j < dimensions) {
                rsMatrix.set(i, j, matrix[i * dimensions + j]);
                j++;
            }
            i++;
        }
        return rsMatrix;
    }

    private void resizeProgramStoresCard(int count) {
        RenderScript renderScript;
        if (this.mProgramStoresCard != null) {
            renderScript = this.mRS;
        } else {
            renderScript = this.mRS;
        }
        if (count <= 0) {
            count = FILL_DIRECTION_CCW;
        }
        this.mProgramStoresCard = new ScriptField_ProgramStore_s(renderScript, count);
        this.mScript.bind_programStoresCard(this.mProgramStoresCard);
    }

    private void setCard(int n, Item item) {
        try {
            this.mCards.set(item, n, DBG);
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.w(TAG, "setCard(" + n + "): Texture " + n + " doesn't exist");
        }
    }

    private void setProgramStoreCard(int n, ProgramStore programStore) {
        ScriptField_ProgramStore_s.Item item = this.mProgramStoresCard.get(n);
        if (item == null) {
            item = new ScriptField_ProgramStore_s.Item();
        }
        item.programStore = programStore;
        this.mProgramStoresCard.set(item, n, DBG);
        this.mScript.invoke_setProgramStoresCard(n, programStore);
    }

    public void createCards(int count) {
        if (this.mCards == null || count <= 0) {
            this.mCards = new ScriptField_Card(this.mRS, count > 0 ? count : FILL_DIRECTION_CCW);
            this.mScript.bind_cards(this.mCards);
            this.mScript.invoke_createCards(DRAG_MODEL_SCREEN_DELTA, count);
        } else {
            int oldSize = this.mCards.getAllocation().getType().getX();
            this.mCards.resize(count);
            this.mScript.invoke_createCards(oldSize, count);
        }
    }

    public void doLongPress() {
        this.mScript.invoke_doLongPress();
    }

    public void doMotion(float x, float y, long t) {
        this.mScript.invoke_doMotion(x, y, t);
    }

    public void doStart(float x, float y, long t) {
        this.mScript.invoke_doStart(x, y, t);
    }

    public void doStop(float x, float y, long t) {
        this.mScript.invoke_doStop(x, y, t);
    }

    void invalidateDetailTexture(int n, boolean eraseCurrent) {
        if (n < 0) {
            throw new IllegalArgumentException("Index cannot be negative");
        }
        synchronized (this) {
            Item item = getCard(n);
            if (item == null) {
            } else {
                if (eraseCurrent && item.detailTexture != null) {
                    item.detailTexture.destroy();
                    item.detailTexture = null;
                }
                setCard(n, item);
                this.mScript.invoke_invalidateDetailTexture(n, eraseCurrent);
            }
        }
    }

    void invalidateTexture(int n, boolean eraseCurrent) {
        if (n < 0) {
            throw new IllegalArgumentException("Index cannot be negative");
        }
        synchronized (this) {
            Item item = getCard(n);
            if (item == null) {
            } else {
                if (eraseCurrent && item.texture != null) {
                    item.texture.destroy();
                    item.texture = null;
                }
                setCard(n, item);
                this.mScript.invoke_invalidateTexture(n, eraseCurrent);
            }
        }
    }

    public Mesh loadGeometry(int resId) {
        if (resId == 0) {
            return null;
        }
        FileA3D model = FileA3D.createFromResource(this.mRS, this.mRes, resId);
        if (model == null) {
            return null;
        }
        IndexEntry entry = model.getIndexEntry(DRAG_MODEL_SCREEN_DELTA);
        return (entry == null || entry.getEntryType() != EntryType.MESH) ? null : (Mesh) entry.getObject();
    }

    public void pauseRendering() {
        this.mRS.bindRootScript(null);
    }

    public void resumeRendering() {
        this.mRS.bindRootScript(this.mScript);
    }

    public void setBackgroundColor(Float4 color) {
        this.mScript.set_backgroundColor(color);
    }

    public void setBackgroundTexture(Bitmap bitmap) {
        Allocation texture = null;
        if (bitmap != null) {
            texture = Allocation.createFromBitmap(this.mRS, bitmap, MIPMAP, DRAG_MODEL_CYLINDER_INSIDE);
        }
        this.mScript.set_backgroundTexture(texture);
    }

    public void setCallback(CarouselCallback callback) {
        this.mCallback = callback;
    }

    public void setCardCreationFadeDuration(long t) {
        this.mScript.set_cardCreationFadeDuration((int) t);
    }

    public void setCardRotation(float cardRotation) {
        this.mScript.set_cardRotation(cardRotation);
    }

    public void setCardsFaceTangent(boolean faceTangent) {
        this.mScript.set_cardsFaceTangent(faceTangent);
    }

    public void setCarouselRotationAngle(float theta) {
        this.mScript.invoke_setCarouselRotationAngle(theta);
    }

    public void setCarouselRotationAngle(float endAngle, int milliseconds, int interpolationMode, float maxAnimatedArc) {
        this.mScript.invoke_setCarouselRotationAngle2(endAngle, milliseconds, interpolationMode, maxAnimatedArc);
    }

    public void setDefaultBitmap(Bitmap bitmap) {
        this.mScript.set_defaultTexture(allocationFromBitmap(bitmap, MIPMAP));
    }

    public void setDefaultCardMatrix(float[] matrix) {
        this.mScript.set_defaultCardMatrix(matrixFromFloat(matrix));
    }

    public void setDefaultGeometry(Mesh mesh) {
        this.mScript.set_defaultGeometry(mesh);
    }

    public void setDetailLineTexture(Bitmap bitmap) {
        Allocation texture = null;
        if (bitmap != null) {
            texture = Allocation.createFromBitmap(this.mRS, bitmap, MIPMAP, DRAG_MODEL_CYLINDER_INSIDE);
        }
        this.mScript.set_detailLineTexture(texture);
    }

    public void setDetailLoadingTexture(Bitmap bitmap) {
        Allocation texture = null;
        if (bitmap != null) {
            texture = Allocation.createFromBitmap(this.mRS, bitmap, MIPMAP, DRAG_MODEL_CYLINDER_INSIDE);
        }
        this.mScript.set_detailLoadingTexture(texture);
    }

    void setDetailTexture(int n, float offx, float offy, float loffx, float loffy, Bitmap bitmap) {
        if (n < 0) {
            throw new IllegalArgumentException("Index cannot be negative");
        }
        synchronized (this) {
            Item item = getOrCreateCard(n);
            if (bitmap != null) {
                item.detailTexture = allocationFromBitmap(bitmap, MIPMAP);
                float width = (float) bitmap.getWidth();
                float height = (float) bitmap.getHeight();
            } else if (item.detailTexture != null) {
                item.detailTexture.destroy();
                item.detailTexture = null;
            }
            setCard(n, item);
            this.mScript.invoke_setDetailTexture(n, offx, offy, loffx, loffy, item.detailTexture);
        }
    }

    public void setDetailTextureAlignment(int alignment) {
        this.mScript.set_detailTextureAlignment(alignment);
    }

    public void setDragFactor(float dragFactor) {
        this.mScript.set_dragFactor(dragFactor);
    }

    public void setDragModel(int model) {
        this.mScript.set_dragModel(model);
    }

    public void setDrawRuler(boolean drawRuler) {
        this.mScript.set_drawRuler(drawRuler);
    }

    public void setFadeInDuration(long t) {
        this.mScript.set_fadeInDuration((int) t);
    }

    public void setFillDirection(int direction) {
        this.mScript.set_fillDirection(direction);
    }

    public void setFirstCardTop(boolean first) {
        this.mScript.set_firstCardTop(first);
    }

    public void setForceBlendCardsWithZ(boolean enabled) {
        this.mForceBlendCardsWithZ = enabled;
        initProgramStore();
    }

    public void setFrictionCoefficient(float frictionCoeff) {
        this.mScript.set_frictionCoeff(frictionCoeff);
    }

    public void setGeometry(int n, Mesh geometry) {
        if (n < 0) {
            throw new IllegalArgumentException("Index cannot be negative");
        }
        synchronized (this) {
            Item item = getOrCreateCard(n);
            if (geometry != null) {
                item.geometry = geometry;
            } else if (item.geometry != null) {
                item.geometry = null;
            }
            setCard(n, item);
            this.mScript.invoke_setGeometry(n, item.geometry);
        }
    }

    public void setLoadingBitmap(Bitmap bitmap) {
        this.mScript.set_loadingTexture(allocationFromBitmap(bitmap, MIPMAP));
    }

    public void setLoadingGeometry(Mesh mesh) {
        this.mScript.set_loadingGeometry(mesh);
    }

    public void setLookAt(float[] eye, float[] at, float[] up) {
        int i = DRAG_MODEL_SCREEN_DELTA;
        while (i < 3) {
            this.mEyePoint[i] = eye[i];
            this.mAtPoint[i] = at[i];
            this.mUp[i] = up[i];
            i++;
        }
        this.mScript.invoke_lookAt(eye[0], eye[1], eye[2], at[0], at[1], at[2], up[0], up[1], up[2]);
    }

    public void setMatrix(int n, float[] matrix) {
        if (n < 0) {
            throw new IllegalArgumentException("Index cannot be negative");
        }
        synchronized (this) {
            Item item = getOrCreateCard(n);
            if (matrix != null) {
                item.matrix = matrixFromFloat(matrix);
            } else {
                item.matrix = null;
            }
            setCard(n, item);
            this.mScript.invoke_setMatrix(n, item.matrix);
        }
    }

    public void setOverscrollSlots(float slots) {
        this.mScript.set_overscrollSlots(slots);
    }

    public void setPrefetchCardCount(int count) {
        this.mPrefetchCardCount = count;
        this.mScript.set_prefetchCardCount(count);
    }

    public void setRadius(float radius) {
        this.mScript.invoke_setRadius(radius);
    }

    public void setRezInCardCount(float alpha) {
        this.mScript.set_rezInCardCount(alpha);
    }

    public void setRowCount(int count) {
        this.mRowCount = count;
        this.mScript.set_rowCount(count);
    }

    public void setRowSpacing(float spacing) {
        this.mScript.set_rowSpacing(spacing);
    }

    public void setSlotCount(int n) {
        this.mScript.set_slotCount(n);
    }

    public void setStartAngle(float theta) {
        this.mScript.set_startAngle(theta);
    }

    public void setStoreConfigs(int[] configs) {
        if (configs == null) {
            initProgramStore();
        } else {
            int count = configs.length;
            resizeProgramStoresCard(count);
            int i = DRAG_MODEL_SCREEN_DELTA;
            while (i < count) {
                boolean depthWrites;
                int config = configs[i];
                boolean alpha;
                if ((config & 1) != 0) {
                    alpha = true;
                } else {
                    alpha = false;
                }
                boolean depthReads;
                if ((config & 2) != 0) {
                    depthReads = true;
                } else {
                    depthReads = false;
                }
                if ((config & 4) != 0) {
                    depthWrites = true;
                } else {
                    depthWrites = false;
                }
                setProgramStoreCard(i, new ProgramStore.Builder(this.mRS).setBlendFunc(BlendSrcFunc.ONE, alpha ? BlendDstFunc.ONE_MINUS_SRC_ALPHA : BlendDstFunc.ZERO).setDitherEnabled(true).setDepthFunc(depthReads ? DepthFunc.LESS : DepthFunc.ALWAYS).setDepthMaskEnabled(depthWrites).create());
                i++;
            }
        }
    }

    public void setSwaySensitivity(float swaySensitivity) {
        this.mScript.set_swaySensitivity(swaySensitivity);
    }

    public void setTexture(int n, Bitmap bitmap) {
        if (n < 0) {
            throw new IllegalArgumentException("Index cannot be negative");
        }
        synchronized (this) {
            Item item = getOrCreateCard(n);
            if (bitmap != null) {
                item.texture = allocationFromPool(n, bitmap, MIPMAP);
            } else if (item.texture != null) {
                item.texture = null;
            }
            setCard(n, item);
            this.mScript.invoke_setTexture(n, item.texture);
        }
    }

    public void setVisibleDetails(int count) {
        this.mScript.set_visibleDetailCount(count);
    }

    public void setVisibleSlots(int count) {
        this.mVisibleSlots = count;
        this.mScript.set_visibleSlotCount(count);
    }
}