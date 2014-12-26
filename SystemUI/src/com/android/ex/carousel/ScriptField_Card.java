package com.android.ex.carousel;

import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.FieldPacker;
import android.renderscript.Float2;
import android.renderscript.Matrix4f;
import android.renderscript.Mesh;
import android.renderscript.RSIllegalArgumentException;
import android.renderscript.RenderScript;
import android.renderscript.Script.FieldBase;
import android.renderscript.Type;
import android.renderscript.Type.Builder;
import com.android.systemui.recent.RecentsCallback;
import com.android.systemui.statusbar.CommandQueue;
import java.lang.ref.WeakReference;

public class ScriptField_Card extends FieldBase {
    private static WeakReference<Element> mElementCache;
    private FieldPacker mIOBuffer;
    private Item[] mItemArray;

    public static class Item {
        public static final int sizeof = 160;
        int cardVisible;
        Float2 detailLineOffset;
        Allocation detailTexture;
        Float2 detailTextureOffset;
        Float2[] detailTexturePosition;
        int detailTextureState;
        long detailTextureTimeStamp;
        int detailVisible;
        Mesh geometry;
        int geometryState;
        long geometryTimeStamp;
        Matrix4f matrix;
        int shouldPrefetch;
        Allocation texture;
        int textureState;
        long textureTimeStamp;

        Item() {
            this.detailTextureOffset = new Float2();
            this.detailLineOffset = new Float2();
            this.detailTexturePosition = new Float2[2];
            int $ct = 0;
            while ($ct < 2) {
                this.detailTexturePosition[$ct] = new Float2();
                $ct++;
            }
            this.matrix = new Matrix4f();
        }
    }

    static {
        mElementCache = new WeakReference(null);
    }

    private ScriptField_Card(RenderScript rs) {
        this.mItemArray = null;
        this.mIOBuffer = null;
        this.mElement = createElement(rs);
    }

    public ScriptField_Card(RenderScript rs, int count) {
        this.mItemArray = null;
        this.mIOBuffer = null;
        this.mElement = createElement(rs);
        init(rs, count);
    }

    public ScriptField_Card(RenderScript rs, int count, int usages) {
        this.mItemArray = null;
        this.mIOBuffer = null;
        this.mElement = createElement(rs);
        init(rs, count, usages);
    }

    private void copyToArray(Item i, int index) {
        if (this.mIOBuffer == null) {
            this.mIOBuffer = new FieldPacker(getType().getX() * 160);
        }
        this.mIOBuffer.reset(index * 160);
        copyToArrayLocal(i, this.mIOBuffer);
    }

    private void copyToArrayLocal(Item i, FieldPacker fp) {
        fp.addObj(i.texture);
        fp.addObj(i.detailTexture);
        fp.addF32(i.detailTextureOffset);
        fp.addF32(i.detailLineOffset);
        int ct2 = 0;
        while (ct2 < 2) {
            fp.addF32(i.detailTexturePosition[ct2]);
            ct2++;
        }
        fp.addObj(i.geometry);
        fp.addMatrix(i.matrix);
        fp.addI32(i.textureState);
        fp.addI32(i.detailTextureState);
        fp.addI32(i.geometryState);
        fp.addI32(i.cardVisible);
        fp.addI32(i.detailVisible);
        fp.addI32(i.shouldPrefetch);
        fp.skip(CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
        fp.addI64(i.textureTimeStamp);
        fp.addI64(i.detailTextureTimeStamp);
        fp.addI64(i.geometryTimeStamp);
    }

    public static ScriptField_Card create1D(RenderScript rs, int dimX) {
        return create1D(rs, dimX, 1);
    }

    public static ScriptField_Card create1D(RenderScript rs, int dimX, int usages) {
        ScriptField_Card obj = new ScriptField_Card(rs);
        obj.mAllocation = Allocation.createSized(rs, obj.mElement, dimX, usages);
        return obj;
    }

    public static ScriptField_Card create2D(RenderScript rs, int dimX, int dimY) {
        return create2D(rs, dimX, dimY, 1);
    }

    public static ScriptField_Card create2D(RenderScript rs, int dimX, int dimY, int usages) {
        ScriptField_Card obj = new ScriptField_Card(rs);
        Builder b = new Builder(rs, obj.mElement);
        b.setX(dimX);
        b.setY(dimY);
        obj.mAllocation = Allocation.createTyped(rs, b.create(), usages);
        return obj;
    }

    public static ScriptField_Card createCustom(RenderScript rs, Builder tb, int usages) {
        ScriptField_Card obj = new ScriptField_Card(rs);
        Type t = tb.create();
        if (t.getElement() != obj.mElement) {
            throw new RSIllegalArgumentException("Type.Builder did not match expected element type.");
        }
        obj.mAllocation = Allocation.createTyped(rs, t, usages);
        return obj;
    }

    public static Element createElement(RenderScript rs) {
        Element.Builder eb = new Element.Builder(rs);
        eb.add(Element.ALLOCATION(rs), "texture");
        eb.add(Element.ALLOCATION(rs), "detailTexture");
        eb.add(Element.F32_2(rs), "detailTextureOffset");
        eb.add(Element.F32_2(rs), "detailLineOffset");
        eb.add(Element.F32_2(rs), "detailTexturePosition", CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL);
        eb.add(Element.MESH(rs), "geometry");
        eb.add(Element.MATRIX_4X4(rs), "matrix");
        eb.add(Element.I32(rs), "textureState");
        eb.add(Element.I32(rs), "detailTextureState");
        eb.add(Element.I32(rs), "geometryState");
        eb.add(Element.I32(rs), "cardVisible");
        eb.add(Element.I32(rs), "detailVisible");
        eb.add(Element.I32(rs), "shouldPrefetch");
        eb.add(Element.U32(rs), "#rs_padding_1");
        eb.add(Element.I64(rs), "textureTimeStamp");
        eb.add(Element.I64(rs), "detailTextureTimeStamp");
        eb.add(Element.I64(rs), "geometryTimeStamp");
        return eb.create();
    }

    public static Builder createTypeBuilder(RenderScript rs) {
        return new Builder(rs, createElement(rs));
    }

    public synchronized void copyAll() {
        int ct = 0;
        while (ct < this.mItemArray.length) {
            copyToArray(this.mItemArray[ct], ct);
            ct++;
        }
        this.mAllocation.setFromFieldPacker(0, this.mIOBuffer);
    }

    public synchronized Item get(int index) {
        Item item;
        if (this.mItemArray == null) {
            item = null;
        } else {
            item = this.mItemArray[index];
        }
        return item;
    }

    public synchronized int get_cardVisible(int index) {
        int i;
        if (this.mItemArray == null) {
            i = 0;
        } else {
            i = this.mItemArray[index].cardVisible;
        }
        return i;
    }

    public synchronized Float2 get_detailLineOffset(int index) {
        Float2 float2;
        if (this.mItemArray == null) {
            float2 = null;
        } else {
            float2 = this.mItemArray[index].detailLineOffset;
        }
        return float2;
    }

    public synchronized Allocation get_detailTexture(int index) {
        Allocation allocation;
        if (this.mItemArray == null) {
            allocation = null;
        } else {
            allocation = this.mItemArray[index].detailTexture;
        }
        return allocation;
    }

    public synchronized Float2 get_detailTextureOffset(int index) {
        Float2 float2;
        if (this.mItemArray == null) {
            float2 = null;
        } else {
            float2 = this.mItemArray[index].detailTextureOffset;
        }
        return float2;
    }

    public synchronized Float2[] get_detailTexturePosition(int index) {
        Float2[] float2Arr;
        if (this.mItemArray == null) {
            float2Arr = null;
        } else {
            float2Arr = this.mItemArray[index].detailTexturePosition;
        }
        return float2Arr;
    }

    public synchronized int get_detailTextureState(int index) {
        int i;
        if (this.mItemArray == null) {
            i = 0;
        } else {
            i = this.mItemArray[index].detailTextureState;
        }
        return i;
    }

    public synchronized long get_detailTextureTimeStamp(int index) {
        long j;
        if (this.mItemArray == null) {
            j = 0;
        } else {
            j = this.mItemArray[index].detailTextureTimeStamp;
        }
        return j;
    }

    public synchronized int get_detailVisible(int index) {
        int i;
        if (this.mItemArray == null) {
            i = 0;
        } else {
            i = this.mItemArray[index].detailVisible;
        }
        return i;
    }

    public synchronized Mesh get_geometry(int index) {
        Mesh mesh;
        if (this.mItemArray == null) {
            mesh = null;
        } else {
            mesh = this.mItemArray[index].geometry;
        }
        return mesh;
    }

    public synchronized int get_geometryState(int index) {
        int i;
        if (this.mItemArray == null) {
            i = 0;
        } else {
            i = this.mItemArray[index].geometryState;
        }
        return i;
    }

    public synchronized long get_geometryTimeStamp(int index) {
        long j;
        if (this.mItemArray == null) {
            j = 0;
        } else {
            j = this.mItemArray[index].geometryTimeStamp;
        }
        return j;
    }

    public synchronized Matrix4f get_matrix(int index) {
        Matrix4f matrix4f;
        if (this.mItemArray == null) {
            matrix4f = null;
        } else {
            matrix4f = this.mItemArray[index].matrix;
        }
        return matrix4f;
    }

    public synchronized int get_shouldPrefetch(int index) {
        int i;
        if (this.mItemArray == null) {
            i = 0;
        } else {
            i = this.mItemArray[index].shouldPrefetch;
        }
        return i;
    }

    public synchronized Allocation get_texture(int index) {
        Allocation allocation;
        if (this.mItemArray == null) {
            allocation = null;
        } else {
            allocation = this.mItemArray[index].texture;
        }
        return allocation;
    }

    public synchronized int get_textureState(int index) {
        int i;
        if (this.mItemArray == null) {
            i = 0;
        } else {
            i = this.mItemArray[index].textureState;
        }
        return i;
    }

    public synchronized long get_textureTimeStamp(int index) {
        long j;
        if (this.mItemArray == null) {
            j = 0;
        } else {
            j = this.mItemArray[index].textureTimeStamp;
        }
        return j;
    }

    public synchronized void resize(int newSize) {
        if (this.mItemArray != null) {
            int oldSize = this.mItemArray.length;
            int copySize = Math.min(oldSize, newSize);
            if (newSize != oldSize) {
                Item[] ni = new Item[newSize];
                System.arraycopy(this.mItemArray, 0, ni, 0, copySize);
                this.mItemArray = ni;
            }
        }
        this.mAllocation.resize(newSize);
        if (this.mIOBuffer != null) {
            this.mIOBuffer = new FieldPacker(getType().getX() * 160);
        }
    }

    public synchronized void set(Item i, int index, boolean copyNow) {
        if (this.mItemArray == null) {
            this.mItemArray = new Item[getType().getX()];
        }
        this.mItemArray[index] = i;
        if (copyNow) {
            copyToArray(i, index);
            FieldPacker fp = new FieldPacker(160);
            copyToArrayLocal(i, fp);
            this.mAllocation.setFromFieldPacker(index, fp);
        }
    }

    public synchronized void set_cardVisible(int index, int v, boolean copyNow) {
        if (this.mIOBuffer == null) {
            this.mIOBuffer = new FieldPacker(getType().getX() * 160);
        }
        if (this.mItemArray == null) {
            this.mItemArray = new Item[getType().getX()];
        }
        if (this.mItemArray[index] == null) {
            this.mItemArray[index] = new Item();
        }
        this.mItemArray[index].cardVisible = v;
        if (copyNow) {
            this.mIOBuffer.reset(index * 160 + 120);
            this.mIOBuffer.addI32(v);
            FieldPacker fp = new FieldPacker(4);
            fp.addI32(v);
            this.mAllocation.setFromFieldPacker(index, 10, fp);
        }
    }

    public synchronized void set_detailLineOffset(int index, Float2 v, boolean copyNow) {
        if (this.mIOBuffer == null) {
            this.mIOBuffer = new FieldPacker(getType().getX() * 160);
        }
        if (this.mItemArray == null) {
            this.mItemArray = new Item[getType().getX()];
        }
        if (this.mItemArray[index] == null) {
            this.mItemArray[index] = new Item();
        }
        this.mItemArray[index].detailLineOffset = v;
        if (copyNow) {
            this.mIOBuffer.reset(index * 160 + 16);
            this.mIOBuffer.addF32(v);
            FieldPacker fp = new FieldPacker(8);
            fp.addF32(v);
            this.mAllocation.setFromFieldPacker(index, RecentsCallback.SWIPE_DOWN, fp);
        }
    }

    public synchronized void set_detailTexture(int index, Allocation v, boolean copyNow) {
        if (this.mIOBuffer == null) {
            this.mIOBuffer = new FieldPacker(getType().getX() * 160);
        }
        if (this.mItemArray == null) {
            this.mItemArray = new Item[getType().getX()];
        }
        if (this.mItemArray[index] == null) {
            this.mItemArray[index] = new Item();
        }
        this.mItemArray[index].detailTexture = v;
        if (copyNow) {
            this.mIOBuffer.reset(index * 160 + 4);
            this.mIOBuffer.addObj(v);
            FieldPacker fp = new FieldPacker(4);
            fp.addObj(v);
            this.mAllocation.setFromFieldPacker(index, 1, fp);
        }
    }

    public synchronized void set_detailTextureOffset(int index, Float2 v, boolean copyNow) {
        if (this.mIOBuffer == null) {
            this.mIOBuffer = new FieldPacker(getType().getX() * 160);
        }
        if (this.mItemArray == null) {
            this.mItemArray = new Item[getType().getX()];
        }
        if (this.mItemArray[index] == null) {
            this.mItemArray[index] = new Item();
        }
        this.mItemArray[index].detailTextureOffset = v;
        if (copyNow) {
            this.mIOBuffer.reset(index * 160 + 8);
            this.mIOBuffer.addF32(v);
            FieldPacker fp = new FieldPacker(8);
            fp.addF32(v);
            this.mAllocation.setFromFieldPacker(index, CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL, fp);
        }
    }

    public synchronized void set_detailTexturePosition(int index, Float2[] v, boolean copyNow) {
        if (this.mIOBuffer == null) {
            this.mIOBuffer = new FieldPacker(getType().getX() * 160);
        }
        if (this.mItemArray == null) {
            this.mItemArray = new Item[getType().getX()];
        }
        if (this.mItemArray[index] == null) {
            this.mItemArray[index] = new Item();
        }
        this.mItemArray[index].detailTexturePosition = v;
        if (copyNow) {
            this.mIOBuffer.reset(index * 160 + 24);
            int ct1 = 0;
            while (ct1 < 2) {
                this.mIOBuffer.addF32(v[ct1]);
                ct1++;
            }
            FieldPacker fp = new FieldPacker(16);
            ct1 = 0;
            while (ct1 < 2) {
                fp.addF32(v[ct1]);
                ct1++;
            }
            this.mAllocation.setFromFieldPacker(index, CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL, fp);
        }
    }

    public synchronized void set_detailTextureState(int index, int v, boolean copyNow) {
        if (this.mIOBuffer == null) {
            this.mIOBuffer = new FieldPacker(getType().getX() * 160);
        }
        if (this.mItemArray == null) {
            this.mItemArray = new Item[getType().getX()];
        }
        if (this.mItemArray[index] == null) {
            this.mItemArray[index] = new Item();
        }
        this.mItemArray[index].detailTextureState = v;
        if (copyNow) {
            this.mIOBuffer.reset(index * 160 + 112);
            this.mIOBuffer.addI32(v);
            FieldPacker fp = new FieldPacker(4);
            fp.addI32(v);
            this.mAllocation.setFromFieldPacker(index, CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL, fp);
        }
    }

    public synchronized void set_detailTextureTimeStamp(int index, long v, boolean copyNow) {
        if (this.mIOBuffer == null) {
            this.mIOBuffer = new FieldPacker(getType().getX() * 160);
        }
        if (this.mItemArray == null) {
            this.mItemArray = new Item[getType().getX()];
        }
        if (this.mItemArray[index] == null) {
            this.mItemArray[index] = new Item();
        }
        this.mItemArray[index].detailTextureTimeStamp = v;
        if (copyNow) {
            this.mIOBuffer.reset(index * 160 + 144);
            this.mIOBuffer.addI64(v);
            FieldPacker fp = new FieldPacker(8);
            fp.addI64(v);
            this.mAllocation.setFromFieldPacker(index, 15, fp);
        }
    }

    public synchronized void set_detailVisible(int index, int v, boolean copyNow) {
        if (this.mIOBuffer == null) {
            this.mIOBuffer = new FieldPacker(getType().getX() * 160);
        }
        if (this.mItemArray == null) {
            this.mItemArray = new Item[getType().getX()];
        }
        if (this.mItemArray[index] == null) {
            this.mItemArray[index] = new Item();
        }
        this.mItemArray[index].detailVisible = v;
        if (copyNow) {
            this.mIOBuffer.reset(index * 160 + 124);
            this.mIOBuffer.addI32(v);
            FieldPacker fp = new FieldPacker(4);
            fp.addI32(v);
            this.mAllocation.setFromFieldPacker(index, 11, fp);
        }
    }

    public synchronized void set_geometry(int index, Mesh v, boolean copyNow) {
        if (this.mIOBuffer == null) {
            this.mIOBuffer = new FieldPacker(getType().getX() * 160);
        }
        if (this.mItemArray == null) {
            this.mItemArray = new Item[getType().getX()];
        }
        if (this.mItemArray[index] == null) {
            this.mItemArray[index] = new Item();
        }
        this.mItemArray[index].geometry = v;
        if (copyNow) {
            this.mIOBuffer.reset(index * 160 + 40);
            this.mIOBuffer.addObj(v);
            FieldPacker fp = new FieldPacker(4);
            fp.addObj(v);
            this.mAllocation.setFromFieldPacker(index, 5, fp);
        }
    }

    public synchronized void set_geometryState(int index, int v, boolean copyNow) {
        if (this.mIOBuffer == null) {
            this.mIOBuffer = new FieldPacker(getType().getX() * 160);
        }
        if (this.mItemArray == null) {
            this.mItemArray = new Item[getType().getX()];
        }
        if (this.mItemArray[index] == null) {
            this.mItemArray[index] = new Item();
        }
        this.mItemArray[index].geometryState = v;
        if (copyNow) {
            this.mIOBuffer.reset(index * 160 + 116);
            this.mIOBuffer.addI32(v);
            FieldPacker fp = new FieldPacker(4);
            fp.addI32(v);
            this.mAllocation.setFromFieldPacker(index, 9, fp);
        }
    }

    public synchronized void set_geometryTimeStamp(int index, long v, boolean copyNow) {
        if (this.mIOBuffer == null) {
            this.mIOBuffer = new FieldPacker(getType().getX() * 160);
        }
        if (this.mItemArray == null) {
            this.mItemArray = new Item[getType().getX()];
        }
        if (this.mItemArray[index] == null) {
            this.mItemArray[index] = new Item();
        }
        this.mItemArray[index].geometryTimeStamp = v;
        if (copyNow) {
            this.mIOBuffer.reset(index * 160 + 152);
            this.mIOBuffer.addI64(v);
            FieldPacker fp = new FieldPacker(8);
            fp.addI64(v);
            this.mAllocation.setFromFieldPacker(index, CommandQueue.FLAG_EXCLUDE_COMPAT_MODE_PANEL, fp);
        }
    }

    public synchronized void set_matrix(int index, Matrix4f v, boolean copyNow) {
        if (this.mIOBuffer == null) {
            this.mIOBuffer = new FieldPacker(getType().getX() * 160);
        }
        if (this.mItemArray == null) {
            this.mItemArray = new Item[getType().getX()];
        }
        if (this.mItemArray[index] == null) {
            this.mItemArray[index] = new Item();
        }
        this.mItemArray[index].matrix = v;
        if (copyNow) {
            this.mIOBuffer.reset(index * 160 + 44);
            this.mIOBuffer.addMatrix(v);
            FieldPacker fp = new FieldPacker(64);
            fp.addMatrix(v);
            this.mAllocation.setFromFieldPacker(index, 6, fp);
        }
    }

    public synchronized void set_shouldPrefetch(int index, int v, boolean copyNow) {
        if (this.mIOBuffer == null) {
            this.mIOBuffer = new FieldPacker(getType().getX() * 160);
        }
        if (this.mItemArray == null) {
            this.mItemArray = new Item[getType().getX()];
        }
        if (this.mItemArray[index] == null) {
            this.mItemArray[index] = new Item();
        }
        this.mItemArray[index].shouldPrefetch = v;
        if (copyNow) {
            this.mIOBuffer.reset(index * 160 + 128);
            this.mIOBuffer.addI32(v);
            FieldPacker fp = new FieldPacker(4);
            fp.addI32(v);
            this.mAllocation.setFromFieldPacker(index, 12, fp);
        }
    }

    public synchronized void set_texture(int index, Allocation v, boolean copyNow) {
        if (this.mIOBuffer == null) {
            this.mIOBuffer = new FieldPacker(getType().getX() * 160);
        }
        if (this.mItemArray == null) {
            this.mItemArray = new Item[getType().getX()];
        }
        if (this.mItemArray[index] == null) {
            this.mItemArray[index] = new Item();
        }
        this.mItemArray[index].texture = v;
        if (copyNow) {
            this.mIOBuffer.reset(index * 160);
            this.mIOBuffer.addObj(v);
            FieldPacker fp = new FieldPacker(4);
            fp.addObj(v);
            this.mAllocation.setFromFieldPacker(index, 0, fp);
        }
    }

    public synchronized void set_textureState(int index, int v, boolean copyNow) {
        if (this.mIOBuffer == null) {
            this.mIOBuffer = new FieldPacker(getType().getX() * 160);
        }
        if (this.mItemArray == null) {
            this.mItemArray = new Item[getType().getX()];
        }
        if (this.mItemArray[index] == null) {
            this.mItemArray[index] = new Item();
        }
        this.mItemArray[index].textureState = v;
        if (copyNow) {
            this.mIOBuffer.reset(index * 160 + 108);
            this.mIOBuffer.addI32(v);
            FieldPacker fp = new FieldPacker(4);
            fp.addI32(v);
            this.mAllocation.setFromFieldPacker(index, 7, fp);
        }
    }

    public synchronized void set_textureTimeStamp(int index, long v, boolean copyNow) {
        if (this.mIOBuffer == null) {
            this.mIOBuffer = new FieldPacker(getType().getX() * 160);
        }
        if (this.mItemArray == null) {
            this.mItemArray = new Item[getType().getX()];
        }
        if (this.mItemArray[index] == null) {
            this.mItemArray[index] = new Item();
        }
        this.mItemArray[index].textureTimeStamp = v;
        if (copyNow) {
            this.mIOBuffer.reset(index * 160 + 136);
            this.mIOBuffer.addI64(v);
            FieldPacker fp = new FieldPacker(8);
            fp.addI64(v);
            this.mAllocation.setFromFieldPacker(index, 14, fp);
        }
    }
}