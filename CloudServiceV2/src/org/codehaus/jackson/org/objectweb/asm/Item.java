package org.codehaus.jackson.org.objectweb.asm;

import com.wmt.data.LocalAudioAll;
import org.codehaus.jackson.impl.JsonWriteContext;

final class Item {
    int a;
    int b;
    int c;
    long d;
    String g;
    String h;
    String i;
    int j;
    Item k;

    Item() {
    }

    Item(int i) {
        this.a = i;
    }

    Item(int i, Item item) {
        this.a = i;
        this.b = item.b;
        this.c = item.c;
        this.d = item.d;
        this.g = item.g;
        this.h = item.h;
        this.i = item.i;
        this.j = item.j;
    }

    void a(double d) {
        this.b = 6;
        this.d = Double.doubleToRawLongBits(d);
        this.j = Integer.MAX_VALUE & (this.b + ((int) d));
    }

    void a(float f) {
        this.b = 4;
        this.c = Float.floatToRawIntBits(f);
        this.j = Integer.MAX_VALUE & (this.b + ((int) f));
    }

    void a(int i) {
        this.b = 3;
        this.c = i;
        this.j = Integer.MAX_VALUE & (this.b + i);
    }

    void a(int i, String str, String str2, String str3) {
        this.b = i;
        this.g = str;
        this.h = str2;
        this.i = str3;
        switch (i) {
            case LocalAudioAll.SORT_BY_DATE:
            case Type.LONG:
            case Type.DOUBLE:
            case Opcodes.FCONST_2:
                this.j = (str.hashCode() + i) & Integer.MAX_VALUE;
            case Opcodes.FCONST_1:
                this.j = ((str.hashCode() * str2.hashCode()) + i) & Integer.MAX_VALUE;
            default:
                this.j = (((str.hashCode() * str2.hashCode()) * str3.hashCode()) + i) & Integer.MAX_VALUE;
        }
    }

    void a(long j) {
        this.b = 5;
        this.d = j;
        this.j = Integer.MAX_VALUE & (this.b + ((int) j));
    }

    boolean a(Item item) {
        switch (this.b) {
            case LocalAudioAll.SORT_BY_DATE:
            case Type.LONG:
            case Type.DOUBLE:
            case Opcodes.FCONST_2:
                return item.g.equals(this.g);
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                return item.c == this.c;
            case JsonWriteContext.STATUS_EXPECT_NAME:
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
            case Opcodes.DCONST_1:
                return item.d == this.d;
            case Opcodes.FCONST_1:
                return item.g.equals(this.g) && item.h.equals(this.h);
            case Opcodes.DCONST_0:
                return item.c == this.c && item.g.equals(this.g);
            default:
                return item.g.equals(this.g) && item.h.equals(this.h) && item.i.equals(this.i);
        }
    }
}