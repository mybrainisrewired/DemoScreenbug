package com.android.ex.carousel;

import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.FieldPacker;
import android.renderscript.ProgramStore;
import android.renderscript.RSIllegalArgumentException;
import android.renderscript.RenderScript;
import android.renderscript.Script.FieldBase;
import android.renderscript.Type;
import android.renderscript.Type.Builder;
import java.lang.ref.WeakReference;

public class ScriptField_ProgramStore_s extends FieldBase {
    private static WeakReference<Element> mElementCache;
    private FieldPacker mIOBuffer;
    private Item[] mItemArray;

    public static class Item {
        public static final int sizeof = 4;
        ProgramStore programStore;

        Item() {
        }
    }

    static {
        mElementCache = new WeakReference(null);
    }

    private ScriptField_ProgramStore_s(RenderScript rs) {
        this.mItemArray = null;
        this.mIOBuffer = null;
        this.mElement = createElement(rs);
    }

    public ScriptField_ProgramStore_s(RenderScript rs, int count) {
        this.mItemArray = null;
        this.mIOBuffer = null;
        this.mElement = createElement(rs);
        init(rs, count);
    }

    public ScriptField_ProgramStore_s(RenderScript rs, int count, int usages) {
        this.mItemArray = null;
        this.mIOBuffer = null;
        this.mElement = createElement(rs);
        init(rs, count, usages);
    }

    private void copyToArray(Item i, int index) {
        if (this.mIOBuffer == null) {
            this.mIOBuffer = new FieldPacker(getType().getX() * 4);
        }
        this.mIOBuffer.reset(index * 4);
        copyToArrayLocal(i, this.mIOBuffer);
    }

    private void copyToArrayLocal(Item i, FieldPacker fp) {
        fp.addObj(i.programStore);
    }

    public static ScriptField_ProgramStore_s create1D(RenderScript rs, int dimX) {
        return create1D(rs, dimX, 1);
    }

    public static ScriptField_ProgramStore_s create1D(RenderScript rs, int dimX, int usages) {
        ScriptField_ProgramStore_s obj = new ScriptField_ProgramStore_s(rs);
        obj.mAllocation = Allocation.createSized(rs, obj.mElement, dimX, usages);
        return obj;
    }

    public static ScriptField_ProgramStore_s create2D(RenderScript rs, int dimX, int dimY) {
        return create2D(rs, dimX, dimY, 1);
    }

    public static ScriptField_ProgramStore_s create2D(RenderScript rs, int dimX, int dimY, int usages) {
        ScriptField_ProgramStore_s obj = new ScriptField_ProgramStore_s(rs);
        Builder b = new Builder(rs, obj.mElement);
        b.setX(dimX);
        b.setY(dimY);
        obj.mAllocation = Allocation.createTyped(rs, b.create(), usages);
        return obj;
    }

    public static ScriptField_ProgramStore_s createCustom(RenderScript rs, Builder tb, int usages) {
        ScriptField_ProgramStore_s obj = new ScriptField_ProgramStore_s(rs);
        Type t = tb.create();
        if (t.getElement() != obj.mElement) {
            throw new RSIllegalArgumentException("Type.Builder did not match expected element type.");
        }
        obj.mAllocation = Allocation.createTyped(rs, t, usages);
        return obj;
    }

    public static Element createElement(RenderScript rs) {
        Element.Builder eb = new Element.Builder(rs);
        eb.add(Element.PROGRAM_STORE(rs), "programStore");
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

    public synchronized ProgramStore get_programStore(int index) {
        ProgramStore programStore;
        if (this.mItemArray == null) {
            programStore = null;
        } else {
            programStore = this.mItemArray[index].programStore;
        }
        return programStore;
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
            this.mIOBuffer = new FieldPacker(getType().getX() * 4);
        }
    }

    public synchronized void set(Item i, int index, boolean copyNow) {
        if (this.mItemArray == null) {
            this.mItemArray = new Item[getType().getX()];
        }
        this.mItemArray[index] = i;
        if (copyNow) {
            copyToArray(i, index);
            FieldPacker fp = new FieldPacker(4);
            copyToArrayLocal(i, fp);
            this.mAllocation.setFromFieldPacker(index, fp);
        }
    }

    public synchronized void set_programStore(int index, ProgramStore v, boolean copyNow) {
        if (this.mIOBuffer == null) {
            this.mIOBuffer = new FieldPacker(getType().getX() * 4);
        }
        if (this.mItemArray == null) {
            this.mItemArray = new Item[getType().getX()];
        }
        if (this.mItemArray[index] == null) {
            this.mItemArray[index] = new Item();
        }
        this.mItemArray[index].programStore = v;
        if (copyNow) {
            this.mIOBuffer.reset(index * 4);
            this.mIOBuffer.addObj(v);
            FieldPacker fp = new FieldPacker(4);
            fp.addObj(v);
            this.mAllocation.setFromFieldPacker(index, 0, fp);
        }
    }
}