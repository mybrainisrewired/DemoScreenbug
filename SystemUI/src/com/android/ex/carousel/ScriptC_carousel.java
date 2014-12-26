package com.android.ex.carousel;

import android.content.res.Resources;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.FieldPacker;
import android.renderscript.Float4;
import android.renderscript.Matrix4f;
import android.renderscript.Mesh;
import android.renderscript.ProgramFragment;
import android.renderscript.ProgramRaster;
import android.renderscript.ProgramStore;
import android.renderscript.ProgramVertex;
import android.renderscript.RenderScript;
import android.renderscript.Sampler;
import android.renderscript.ScriptC;

public class ScriptC_carousel extends ScriptC {
    public static final boolean const_debugCamera = false;
    public static final boolean const_debugDetails = false;
    public static final boolean const_debugGeometryLoading = false;
    public static final boolean const_debugRays = false;
    public static final boolean const_debugRendering = false;
    public static final boolean const_debugSelection = false;
    public static final boolean const_debugTextureLoading = false;
    private static final int mExportFuncIdx_createCards = 1;
    private static final int mExportFuncIdx_doLongPress = 13;
    private static final int mExportFuncIdx_doMotion = 14;
    private static final int mExportFuncIdx_doStart = 11;
    private static final int mExportFuncIdx_doStop = 12;
    private static final int mExportFuncIdx_invalidateDetailTexture = 6;
    private static final int mExportFuncIdx_invalidateTexture = 5;
    private static final int mExportFuncIdx_lookAt = 2;
    private static final int mExportFuncIdx_setCarouselRotationAngle = 10;
    private static final int mExportFuncIdx_setCarouselRotationAngle2 = 15;
    private static final int mExportFuncIdx_setDetailTexture = 4;
    private static final int mExportFuncIdx_setGeometry = 7;
    private static final int mExportFuncIdx_setMatrix = 8;
    private static final int mExportFuncIdx_setProgramStoresCard = 9;
    private static final int mExportFuncIdx_setRadius = 0;
    private static final int mExportFuncIdx_setTexture = 3;
    private static final int mExportVarIdx_backgroundColor = 27;
    private static final int mExportVarIdx_backgroundTexture = 45;
    private static final int mExportVarIdx_cardCount = 10;
    private static final int mExportVarIdx_cardCreationFadeDuration = 24;
    private static final int mExportVarIdx_cardRotation = 18;
    private static final int mExportVarIdx_cards = 7;
    private static final int mExportVarIdx_cardsFaceTangent = 19;
    private static final int mExportVarIdx_debugCamera = 0;
    private static final int mExportVarIdx_debugDetails = 4;
    private static final int mExportVarIdx_debugGeometryLoading = 3;
    private static final int mExportVarIdx_debugRays = 6;
    private static final int mExportVarIdx_debugRendering = 5;
    private static final int mExportVarIdx_debugSelection = 1;
    private static final int mExportVarIdx_debugTextureLoading = 2;
    private static final int mExportVarIdx_defaultCardMatrix = 50;
    private static final int mExportVarIdx_defaultGeometry = 48;
    private static final int mExportVarIdx_defaultTexture = 43;
    private static final int mExportVarIdx_detailFadeRate = 26;
    private static final int mExportVarIdx_detailLineTexture = 46;
    private static final int mExportVarIdx_detailLoadingTexture = 47;
    private static final int mExportVarIdx_detailTextureAlignment = 15;
    private static final int mExportVarIdx_dragFactor = 22;
    private static final int mExportVarIdx_dragModel = 32;
    private static final int mExportVarIdx_drawRuler = 16;
    private static final int mExportVarIdx_fadeInDuration = 23;
    private static final int mExportVarIdx_fillDirection = 33;
    private static final int mExportVarIdx_firstCardTop = 30;
    private static final int mExportVarIdx_frictionCoeff = 21;
    private static final int mExportVarIdx_linearClamp = 54;
    private static final int mExportVarIdx_loadingGeometry = 49;
    private static final int mExportVarIdx_loadingTexture = 44;
    private static final int mExportVarIdx_modelviewMatrix = 52;
    private static final int mExportVarIdx_multiTextureBlendingFragmentProgram = 40;
    private static final int mExportVarIdx_multiTextureFragmentProgram = 39;
    private static final int mExportVarIdx_overscrollSlots = 31;
    private static final int mExportVarIdx_prefetchCardCount = 14;
    private static final int mExportVarIdx_programStoreBackground = 35;
    private static final int mExportVarIdx_programStoreDetail = 36;
    private static final int mExportVarIdx_programStoresCard = 34;
    private static final int mExportVarIdx_programStoresCardCount = 11;
    private static final int mExportVarIdx_projectionMatrix = 51;
    private static final int mExportVarIdx_radius = 17;
    private static final int mExportVarIdx_rasterProgram = 42;
    private static final int mExportVarIdx_rezInCardCount = 25;
    private static final int mExportVarIdx_rowCount = 28;
    private static final int mExportVarIdx_rowSpacing = 29;
    private static final int mExportVarIdx_shaderConstants = 53;
    private static final int mExportVarIdx_singleTextureBlendingFragmentProgram = 38;
    private static final int mExportVarIdx_singleTextureFragmentProgram = 37;
    private static final int mExportVarIdx_slotCount = 9;
    private static final int mExportVarIdx_startAngle = 8;
    private static final int mExportVarIdx_swaySensitivity = 20;
    private static final int mExportVarIdx_vertexProgram = 41;
    private static final int mExportVarIdx_visibleDetailCount = 13;
    private static final int mExportVarIdx_visibleSlotCount = 12;
    private Element __ALLOCATION;
    private Element __BOOLEAN;
    private Element __F32;
    private Element __F32_4;
    private Element __I32;
    private Element __MESH;
    private Element __PROGRAM_FRAGMENT;
    private Element __PROGRAM_RASTER;
    private Element __PROGRAM_STORE;
    private Element __PROGRAM_VERTEX;
    private Element __SAMPLER;
    private Float4 mExportVar_backgroundColor;
    private Allocation mExportVar_backgroundTexture;
    private int mExportVar_cardCount;
    private int mExportVar_cardCreationFadeDuration;
    private float mExportVar_cardRotation;
    private ScriptField_Card mExportVar_cards;
    private boolean mExportVar_cardsFaceTangent;
    private boolean mExportVar_debugCamera;
    private boolean mExportVar_debugDetails;
    private boolean mExportVar_debugGeometryLoading;
    private boolean mExportVar_debugRays;
    private boolean mExportVar_debugRendering;
    private boolean mExportVar_debugSelection;
    private boolean mExportVar_debugTextureLoading;
    private Matrix4f mExportVar_defaultCardMatrix;
    private Mesh mExportVar_defaultGeometry;
    private Allocation mExportVar_defaultTexture;
    private float mExportVar_detailFadeRate;
    private Allocation mExportVar_detailLineTexture;
    private Allocation mExportVar_detailLoadingTexture;
    private int mExportVar_detailTextureAlignment;
    private float mExportVar_dragFactor;
    private int mExportVar_dragModel;
    private boolean mExportVar_drawRuler;
    private int mExportVar_fadeInDuration;
    private int mExportVar_fillDirection;
    private boolean mExportVar_firstCardTop;
    private float mExportVar_frictionCoeff;
    private Sampler mExportVar_linearClamp;
    private Mesh mExportVar_loadingGeometry;
    private Allocation mExportVar_loadingTexture;
    private Matrix4f mExportVar_modelviewMatrix;
    private ProgramFragment mExportVar_multiTextureBlendingFragmentProgram;
    private ProgramFragment mExportVar_multiTextureFragmentProgram;
    private float mExportVar_overscrollSlots;
    private int mExportVar_prefetchCardCount;
    private ProgramStore mExportVar_programStoreBackground;
    private ProgramStore mExportVar_programStoreDetail;
    private ScriptField_ProgramStore_s mExportVar_programStoresCard;
    private int mExportVar_programStoresCardCount;
    private Matrix4f mExportVar_projectionMatrix;
    private float mExportVar_radius;
    private ProgramRaster mExportVar_rasterProgram;
    private float mExportVar_rezInCardCount;
    private int mExportVar_rowCount;
    private float mExportVar_rowSpacing;
    private ScriptField_FragmentShaderConstants_s mExportVar_shaderConstants;
    private ProgramFragment mExportVar_singleTextureBlendingFragmentProgram;
    private ProgramFragment mExportVar_singleTextureFragmentProgram;
    private int mExportVar_slotCount;
    private float mExportVar_startAngle;
    private float mExportVar_swaySensitivity;
    private ProgramVertex mExportVar_vertexProgram;
    private int mExportVar_visibleDetailCount;
    private int mExportVar_visibleSlotCount;

    public ScriptC_carousel(RenderScript rs, Resources resources, int id) {
        super(rs, resources, id);
        this.mExportVar_debugCamera = false;
        this.__BOOLEAN = Element.BOOLEAN(rs);
        this.mExportVar_debugSelection = false;
        this.mExportVar_debugTextureLoading = false;
        this.mExportVar_debugGeometryLoading = false;
        this.mExportVar_debugDetails = false;
        this.mExportVar_debugRendering = false;
        this.mExportVar_debugRays = false;
        this.__F32 = Element.F32(rs);
        this.__I32 = Element.I32(rs);
        this.__F32_4 = Element.F32_4(rs);
        this.mExportVar_dragModel = 0;
        this.__PROGRAM_STORE = Element.PROGRAM_STORE(rs);
        this.__PROGRAM_FRAGMENT = Element.PROGRAM_FRAGMENT(rs);
        this.__PROGRAM_VERTEX = Element.PROGRAM_VERTEX(rs);
        this.__PROGRAM_RASTER = Element.PROGRAM_RASTER(rs);
        this.__ALLOCATION = Element.ALLOCATION(rs);
        this.__MESH = Element.MESH(rs);
        this.__SAMPLER = Element.SAMPLER(rs);
    }

    public void bind_cards(ScriptField_Card v) {
        this.mExportVar_cards = v;
        if (v == null) {
            bindAllocation(null, mExportVarIdx_cards);
        } else {
            bindAllocation(v.getAllocation(), mExportVarIdx_cards);
        }
    }

    public void bind_programStoresCard(ScriptField_ProgramStore_s v) {
        this.mExportVar_programStoresCard = v;
        if (v == null) {
            bindAllocation(null, mExportVarIdx_programStoresCard);
        } else {
            bindAllocation(v.getAllocation(), mExportVarIdx_programStoresCard);
        }
    }

    public void bind_shaderConstants(ScriptField_FragmentShaderConstants_s v) {
        this.mExportVar_shaderConstants = v;
        if (v == null) {
            bindAllocation(null, mExportVarIdx_shaderConstants);
        } else {
            bindAllocation(v.getAllocation(), mExportVarIdx_shaderConstants);
        }
    }

    public Float4 get_backgroundColor() {
        return this.mExportVar_backgroundColor;
    }

    public Allocation get_backgroundTexture() {
        return this.mExportVar_backgroundTexture;
    }

    public int get_cardCount() {
        return this.mExportVar_cardCount;
    }

    public int get_cardCreationFadeDuration() {
        return this.mExportVar_cardCreationFadeDuration;
    }

    public float get_cardRotation() {
        return this.mExportVar_cardRotation;
    }

    public ScriptField_Card get_cards() {
        return this.mExportVar_cards;
    }

    public boolean get_cardsFaceTangent() {
        return this.mExportVar_cardsFaceTangent;
    }

    public boolean get_debugCamera() {
        return this.mExportVar_debugCamera;
    }

    public boolean get_debugDetails() {
        return this.mExportVar_debugDetails;
    }

    public boolean get_debugGeometryLoading() {
        return this.mExportVar_debugGeometryLoading;
    }

    public boolean get_debugRays() {
        return this.mExportVar_debugRays;
    }

    public boolean get_debugRendering() {
        return this.mExportVar_debugRendering;
    }

    public boolean get_debugSelection() {
        return this.mExportVar_debugSelection;
    }

    public boolean get_debugTextureLoading() {
        return this.mExportVar_debugTextureLoading;
    }

    public Matrix4f get_defaultCardMatrix() {
        return this.mExportVar_defaultCardMatrix;
    }

    public Mesh get_defaultGeometry() {
        return this.mExportVar_defaultGeometry;
    }

    public Allocation get_defaultTexture() {
        return this.mExportVar_defaultTexture;
    }

    public float get_detailFadeRate() {
        return this.mExportVar_detailFadeRate;
    }

    public Allocation get_detailLineTexture() {
        return this.mExportVar_detailLineTexture;
    }

    public Allocation get_detailLoadingTexture() {
        return this.mExportVar_detailLoadingTexture;
    }

    public int get_detailTextureAlignment() {
        return this.mExportVar_detailTextureAlignment;
    }

    public float get_dragFactor() {
        return this.mExportVar_dragFactor;
    }

    public int get_dragModel() {
        return this.mExportVar_dragModel;
    }

    public boolean get_drawRuler() {
        return this.mExportVar_drawRuler;
    }

    public int get_fadeInDuration() {
        return this.mExportVar_fadeInDuration;
    }

    public int get_fillDirection() {
        return this.mExportVar_fillDirection;
    }

    public boolean get_firstCardTop() {
        return this.mExportVar_firstCardTop;
    }

    public float get_frictionCoeff() {
        return this.mExportVar_frictionCoeff;
    }

    public Sampler get_linearClamp() {
        return this.mExportVar_linearClamp;
    }

    public Mesh get_loadingGeometry() {
        return this.mExportVar_loadingGeometry;
    }

    public Allocation get_loadingTexture() {
        return this.mExportVar_loadingTexture;
    }

    public Matrix4f get_modelviewMatrix() {
        return this.mExportVar_modelviewMatrix;
    }

    public ProgramFragment get_multiTextureBlendingFragmentProgram() {
        return this.mExportVar_multiTextureBlendingFragmentProgram;
    }

    public ProgramFragment get_multiTextureFragmentProgram() {
        return this.mExportVar_multiTextureFragmentProgram;
    }

    public float get_overscrollSlots() {
        return this.mExportVar_overscrollSlots;
    }

    public int get_prefetchCardCount() {
        return this.mExportVar_prefetchCardCount;
    }

    public ProgramStore get_programStoreBackground() {
        return this.mExportVar_programStoreBackground;
    }

    public ProgramStore get_programStoreDetail() {
        return this.mExportVar_programStoreDetail;
    }

    public ScriptField_ProgramStore_s get_programStoresCard() {
        return this.mExportVar_programStoresCard;
    }

    public int get_programStoresCardCount() {
        return this.mExportVar_programStoresCardCount;
    }

    public Matrix4f get_projectionMatrix() {
        return this.mExportVar_projectionMatrix;
    }

    public float get_radius() {
        return this.mExportVar_radius;
    }

    public ProgramRaster get_rasterProgram() {
        return this.mExportVar_rasterProgram;
    }

    public float get_rezInCardCount() {
        return this.mExportVar_rezInCardCount;
    }

    public int get_rowCount() {
        return this.mExportVar_rowCount;
    }

    public float get_rowSpacing() {
        return this.mExportVar_rowSpacing;
    }

    public ScriptField_FragmentShaderConstants_s get_shaderConstants() {
        return this.mExportVar_shaderConstants;
    }

    public ProgramFragment get_singleTextureBlendingFragmentProgram() {
        return this.mExportVar_singleTextureBlendingFragmentProgram;
    }

    public ProgramFragment get_singleTextureFragmentProgram() {
        return this.mExportVar_singleTextureFragmentProgram;
    }

    public int get_slotCount() {
        return this.mExportVar_slotCount;
    }

    public float get_startAngle() {
        return this.mExportVar_startAngle;
    }

    public float get_swaySensitivity() {
        return this.mExportVar_swaySensitivity;
    }

    public ProgramVertex get_vertexProgram() {
        return this.mExportVar_vertexProgram;
    }

    public int get_visibleDetailCount() {
        return this.mExportVar_visibleDetailCount;
    }

    public int get_visibleSlotCount() {
        return this.mExportVar_visibleSlotCount;
    }

    public void invoke_createCards(int start, int total) {
        FieldPacker createCards_fp = new FieldPacker(8);
        createCards_fp.addI32(start);
        createCards_fp.addI32(total);
        invoke(mExportVarIdx_debugSelection, createCards_fp);
    }

    public void invoke_doLongPress() {
        invoke(mExportVarIdx_visibleDetailCount);
    }

    public void invoke_doMotion(float x, float y, long eventTime) {
        FieldPacker doMotion_fp = new FieldPacker(16);
        doMotion_fp.addF32(x);
        doMotion_fp.addF32(y);
        doMotion_fp.addI64(eventTime);
        invoke(mExportVarIdx_prefetchCardCount, doMotion_fp);
    }

    public void invoke_doStart(float x, float y, long eventTime) {
        FieldPacker doStart_fp = new FieldPacker(16);
        doStart_fp.addF32(x);
        doStart_fp.addF32(y);
        doStart_fp.addI64(eventTime);
        invoke(mExportVarIdx_programStoresCardCount, doStart_fp);
    }

    public void invoke_doStop(float x, float y, long eventTime) {
        FieldPacker doStop_fp = new FieldPacker(16);
        doStop_fp.addF32(x);
        doStop_fp.addF32(y);
        doStop_fp.addI64(eventTime);
        invoke(mExportVarIdx_visibleSlotCount, doStop_fp);
    }

    public void invoke_invalidateDetailTexture(int n, boolean eraseCurrent) {
        FieldPacker invalidateDetailTexture_fp = new FieldPacker(8);
        invalidateDetailTexture_fp.addI32(n);
        invalidateDetailTexture_fp.addBoolean(eraseCurrent);
        invalidateDetailTexture_fp.skip(mExportVarIdx_debugGeometryLoading);
        invoke(mExportVarIdx_debugRays, invalidateDetailTexture_fp);
    }

    public void invoke_invalidateTexture(int n, boolean eraseCurrent) {
        FieldPacker invalidateTexture_fp = new FieldPacker(8);
        invalidateTexture_fp.addI32(n);
        invalidateTexture_fp.addBoolean(eraseCurrent);
        invalidateTexture_fp.skip(mExportVarIdx_debugGeometryLoading);
        invoke(mExportVarIdx_debugRendering, invalidateTexture_fp);
    }

    public void invoke_lookAt(float fromX, float fromY, float fromZ, float atX, float atY, float atZ, float upX, float upY, float upZ) {
        FieldPacker lookAt_fp = new FieldPacker(36);
        lookAt_fp.addF32(fromX);
        lookAt_fp.addF32(fromY);
        lookAt_fp.addF32(fromZ);
        lookAt_fp.addF32(atX);
        lookAt_fp.addF32(atY);
        lookAt_fp.addF32(atZ);
        lookAt_fp.addF32(upX);
        lookAt_fp.addF32(upY);
        lookAt_fp.addF32(upZ);
        invoke(mExportVarIdx_debugTextureLoading, lookAt_fp);
    }

    public void invoke_setCarouselRotationAngle(float carouselRotationAngle) {
        FieldPacker setCarouselRotationAngle_fp = new FieldPacker(4);
        setCarouselRotationAngle_fp.addF32(carouselRotationAngle);
        invoke(mExportVarIdx_cardCount, setCarouselRotationAngle_fp);
    }

    public void invoke_setCarouselRotationAngle2(float endAngle, int milliseconds, int interpolationMode, float maxAnimatedArc) {
        FieldPacker setCarouselRotationAngle2_fp = new FieldPacker(16);
        setCarouselRotationAngle2_fp.addF32(endAngle);
        setCarouselRotationAngle2_fp.addI32(milliseconds);
        setCarouselRotationAngle2_fp.addI32(interpolationMode);
        setCarouselRotationAngle2_fp.addF32(maxAnimatedArc);
        invoke(mExportVarIdx_detailTextureAlignment, setCarouselRotationAngle2_fp);
    }

    public void invoke_setDetailTexture(int n, float offx, float offy, float loffx, float loffy, Allocation texture) {
        FieldPacker setDetailTexture_fp = new FieldPacker(24);
        setDetailTexture_fp.addI32(n);
        setDetailTexture_fp.addF32(offx);
        setDetailTexture_fp.addF32(offy);
        setDetailTexture_fp.addF32(loffx);
        setDetailTexture_fp.addF32(loffy);
        setDetailTexture_fp.addObj(texture);
        invoke(mExportVarIdx_debugDetails, setDetailTexture_fp);
    }

    public void invoke_setGeometry(int n, Mesh geometry) {
        FieldPacker setGeometry_fp = new FieldPacker(8);
        setGeometry_fp.addI32(n);
        setGeometry_fp.addObj(geometry);
        invoke(mExportVarIdx_cards, setGeometry_fp);
    }

    public void invoke_setMatrix(int n, Matrix4f matrix) {
        FieldPacker setMatrix_fp = new FieldPacker(68);
        setMatrix_fp.addI32(n);
        setMatrix_fp.addMatrix(matrix);
        invoke(mExportVarIdx_startAngle, setMatrix_fp);
    }

    public void invoke_setProgramStoresCard(int n, ProgramStore programStore) {
        FieldPacker setProgramStoresCard_fp = new FieldPacker(8);
        setProgramStoresCard_fp.addI32(n);
        setProgramStoresCard_fp.addObj(programStore);
        invoke(mExportVarIdx_slotCount, setProgramStoresCard_fp);
    }

    public void invoke_setRadius(float rad) {
        FieldPacker setRadius_fp = new FieldPacker(4);
        setRadius_fp.addF32(rad);
        invoke(mExportVarIdx_debugCamera, setRadius_fp);
    }

    public void invoke_setTexture(int n, Allocation texture) {
        FieldPacker setTexture_fp = new FieldPacker(8);
        setTexture_fp.addI32(n);
        setTexture_fp.addObj(texture);
        invoke(mExportVarIdx_debugGeometryLoading, setTexture_fp);
    }

    public void set_backgroundColor(Float4 v) {
        this.mExportVar_backgroundColor = v;
        FieldPacker fp = new FieldPacker(16);
        fp.addF32(v);
        setVar(mExportVarIdx_backgroundColor, fp, this.__F32_4, new int[]{1});
    }

    public void set_backgroundTexture(Allocation v) {
        this.mExportVar_backgroundTexture = v;
        setVar(mExportVarIdx_backgroundTexture, v);
    }

    public void set_cardCount(int v) {
        this.mExportVar_cardCount = v;
        setVar(mExportVarIdx_cardCount, v);
    }

    public void set_cardCreationFadeDuration(int v) {
        this.mExportVar_cardCreationFadeDuration = v;
        setVar(mExportVarIdx_cardCreationFadeDuration, v);
    }

    public void set_cardRotation(float v) {
        this.mExportVar_cardRotation = v;
        setVar(mExportVarIdx_cardRotation, v);
    }

    public void set_cardsFaceTangent(boolean v) {
        this.mExportVar_cardsFaceTangent = v;
        setVar(mExportVarIdx_cardsFaceTangent, v);
    }

    public void set_defaultCardMatrix(Matrix4f v) {
        this.mExportVar_defaultCardMatrix = v;
        FieldPacker fp = new FieldPacker(64);
        fp.addMatrix(v);
        setVar(mExportVarIdx_defaultCardMatrix, fp);
    }

    public void set_defaultGeometry(Mesh v) {
        this.mExportVar_defaultGeometry = v;
        setVar(mExportVarIdx_defaultGeometry, v);
    }

    public void set_defaultTexture(Allocation v) {
        this.mExportVar_defaultTexture = v;
        setVar(mExportVarIdx_defaultTexture, v);
    }

    public void set_detailFadeRate(float v) {
        this.mExportVar_detailFadeRate = v;
        setVar(mExportVarIdx_detailFadeRate, v);
    }

    public void set_detailLineTexture(Allocation v) {
        this.mExportVar_detailLineTexture = v;
        setVar(mExportVarIdx_detailLineTexture, v);
    }

    public void set_detailLoadingTexture(Allocation v) {
        this.mExportVar_detailLoadingTexture = v;
        setVar(mExportVarIdx_detailLoadingTexture, v);
    }

    public void set_detailTextureAlignment(int v) {
        this.mExportVar_detailTextureAlignment = v;
        setVar(mExportVarIdx_detailTextureAlignment, v);
    }

    public void set_dragFactor(float v) {
        this.mExportVar_dragFactor = v;
        setVar(mExportVarIdx_dragFactor, v);
    }

    public void set_dragModel(int v) {
        this.mExportVar_dragModel = v;
        setVar(mExportVarIdx_dragModel, v);
    }

    public void set_drawRuler(boolean v) {
        this.mExportVar_drawRuler = v;
        setVar(mExportVarIdx_drawRuler, v);
    }

    public void set_fadeInDuration(int v) {
        this.mExportVar_fadeInDuration = v;
        setVar(mExportVarIdx_fadeInDuration, v);
    }

    public void set_fillDirection(int v) {
        this.mExportVar_fillDirection = v;
        setVar(mExportVarIdx_fillDirection, v);
    }

    public void set_firstCardTop(boolean v) {
        this.mExportVar_firstCardTop = v;
        setVar(mExportVarIdx_firstCardTop, v);
    }

    public void set_frictionCoeff(float v) {
        this.mExportVar_frictionCoeff = v;
        setVar(mExportVarIdx_frictionCoeff, v);
    }

    public void set_linearClamp(Sampler v) {
        this.mExportVar_linearClamp = v;
        setVar(mExportVarIdx_linearClamp, v);
    }

    public void set_loadingGeometry(Mesh v) {
        this.mExportVar_loadingGeometry = v;
        setVar(mExportVarIdx_loadingGeometry, v);
    }

    public void set_loadingTexture(Allocation v) {
        this.mExportVar_loadingTexture = v;
        setVar(mExportVarIdx_loadingTexture, v);
    }

    public void set_modelviewMatrix(Matrix4f v) {
        this.mExportVar_modelviewMatrix = v;
        FieldPacker fp = new FieldPacker(64);
        fp.addMatrix(v);
        setVar(mExportVarIdx_modelviewMatrix, fp);
    }

    public void set_multiTextureBlendingFragmentProgram(ProgramFragment v) {
        this.mExportVar_multiTextureBlendingFragmentProgram = v;
        setVar(mExportVarIdx_multiTextureBlendingFragmentProgram, v);
    }

    public void set_multiTextureFragmentProgram(ProgramFragment v) {
        this.mExportVar_multiTextureFragmentProgram = v;
        setVar(mExportVarIdx_multiTextureFragmentProgram, v);
    }

    public void set_overscrollSlots(float v) {
        this.mExportVar_overscrollSlots = v;
        setVar(mExportVarIdx_overscrollSlots, v);
    }

    public void set_prefetchCardCount(int v) {
        this.mExportVar_prefetchCardCount = v;
        setVar(mExportVarIdx_prefetchCardCount, v);
    }

    public void set_programStoreBackground(ProgramStore v) {
        this.mExportVar_programStoreBackground = v;
        setVar(mExportVarIdx_programStoreBackground, v);
    }

    public void set_programStoreDetail(ProgramStore v) {
        this.mExportVar_programStoreDetail = v;
        setVar(mExportVarIdx_programStoreDetail, v);
    }

    public void set_programStoresCardCount(int v) {
        this.mExportVar_programStoresCardCount = v;
        setVar(mExportVarIdx_programStoresCardCount, v);
    }

    public void set_projectionMatrix(Matrix4f v) {
        this.mExportVar_projectionMatrix = v;
        FieldPacker fp = new FieldPacker(64);
        fp.addMatrix(v);
        setVar(mExportVarIdx_projectionMatrix, fp);
    }

    public void set_radius(float v) {
        this.mExportVar_radius = v;
        setVar(mExportVarIdx_radius, v);
    }

    public void set_rasterProgram(ProgramRaster v) {
        this.mExportVar_rasterProgram = v;
        setVar(mExportVarIdx_rasterProgram, v);
    }

    public void set_rezInCardCount(float v) {
        this.mExportVar_rezInCardCount = v;
        setVar(mExportVarIdx_rezInCardCount, v);
    }

    public void set_rowCount(int v) {
        this.mExportVar_rowCount = v;
        setVar(mExportVarIdx_rowCount, v);
    }

    public void set_rowSpacing(float v) {
        this.mExportVar_rowSpacing = v;
        setVar(mExportVarIdx_rowSpacing, v);
    }

    public void set_singleTextureBlendingFragmentProgram(ProgramFragment v) {
        this.mExportVar_singleTextureBlendingFragmentProgram = v;
        setVar(mExportVarIdx_singleTextureBlendingFragmentProgram, v);
    }

    public void set_singleTextureFragmentProgram(ProgramFragment v) {
        this.mExportVar_singleTextureFragmentProgram = v;
        setVar(mExportVarIdx_singleTextureFragmentProgram, v);
    }

    public void set_slotCount(int v) {
        this.mExportVar_slotCount = v;
        setVar(mExportVarIdx_slotCount, v);
    }

    public void set_startAngle(float v) {
        this.mExportVar_startAngle = v;
        setVar(mExportVarIdx_startAngle, v);
    }

    public void set_swaySensitivity(float v) {
        this.mExportVar_swaySensitivity = v;
        setVar(mExportVarIdx_swaySensitivity, v);
    }

    public void set_vertexProgram(ProgramVertex v) {
        this.mExportVar_vertexProgram = v;
        setVar(mExportVarIdx_vertexProgram, v);
    }

    public void set_visibleDetailCount(int v) {
        this.mExportVar_visibleDetailCount = v;
        setVar(mExportVarIdx_visibleDetailCount, v);
    }

    public void set_visibleSlotCount(int v) {
        this.mExportVar_visibleSlotCount = v;
        setVar(mExportVarIdx_visibleSlotCount, v);
    }
}