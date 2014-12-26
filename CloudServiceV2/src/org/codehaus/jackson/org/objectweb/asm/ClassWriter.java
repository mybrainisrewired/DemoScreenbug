package org.codehaus.jackson.org.objectweb.asm;

import org.codehaus.jackson.impl.JsonWriteContext;

public class ClassWriter implements ClassVisitor {
    public static final int COMPUTE_FRAMES = 2;
    public static final int COMPUTE_MAXS = 1;
    static final byte[] a;
    MethodWriter A;
    MethodWriter B;
    private short D;
    Item[] E;
    String F;
    private final boolean G;
    private final boolean H;
    boolean I;
    ClassReader J;
    int b;
    int c;
    final ByteVector d;
    Item[] e;
    int f;
    final Item g;
    final Item h;
    final Item i;
    private int j;
    private int k;
    private int l;
    private int m;
    private int n;
    private int[] o;
    private int p;
    private ByteVector q;
    private int r;
    private int s;
    private AnnotationWriter t;
    private AnnotationWriter u;
    private Attribute v;
    private int w;
    private ByteVector x;
    FieldWriter y;
    FieldWriter z;

    static {
        byte[] bArr = new byte[220];
        String str = "AAAAAAAAAAAAAAAABCKLLDDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAADDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMAAAAAAAAAAAAAAAAAAAAIIIIIIIIIIIIIIIIDNOAAAAAAGGGGGGGHHFBFAAFFAAQPIIJJIIIIIIIIIIIIIIIIII";
        int i = 0;
        while (i < bArr.length) {
            bArr[i] = (byte) (str.charAt(i) - 65);
            i++;
        }
        a = bArr;
    }

    public ClassWriter(int i) {
        boolean z = true;
        this.c = 1;
        this.d = new ByteVector();
        this.e = new Item[256];
        this.f = (int) (0.75d * ((double) this.e.length));
        this.g = new Item();
        this.h = new Item();
        this.i = new Item();
        this.H = (i & 1) != 0;
        if ((i & 2) == 0) {
            z = false;
        }
        this.G = z;
    }

    public ClassWriter(ClassReader classReader, int i) {
        this(i);
        classReader.a(this);
        this.J = classReader;
    }

    private Item a(Item item) {
        Item item2 = this.e[item.j % this.e.length];
        while (item2 != null) {
            if (item2.b == item.b && item.a(item2)) {
                return item2;
            }
            item2 = item2.k;
        }
        return item2;
    }

    private void a(int i, int i2, int i3) {
        this.d.b(i, i2).putShort(i3);
    }

    private Item b(String str) {
        this.h.a(Type.DOUBLE, str, null, null);
        Item a = a(this.h);
        if (a != null) {
            return a;
        }
        this.d.b(Type.DOUBLE, newUTF8(str));
        int i = this.c;
        this.c = i + 1;
        a = new Item(i, this.h);
        b(a);
        return a;
    }

    private void b(Item item) {
        int length;
        if (this.c > this.f) {
            length = this.e.length;
            int i = length * 2 + 1;
            Item[] itemArr = new Item[i];
            int i2 = length - 1;
            while (i2 >= 0) {
                Item item2 = this.e[i2];
                while (item2 != null) {
                    int length2 = item2.j % itemArr.length;
                    Item item3 = item2.k;
                    item2.k = itemArr[length2];
                    itemArr[length2] = item2;
                    item2 = item3;
                }
                i2--;
            }
            this.e = itemArr;
            this.f = (int) (((double) i) * 0.75d);
        }
        length = item.j % this.e.length;
        item.k = this.e[length];
        this.e[length] = item;
    }

    private Item c(Item item) {
        this.D = (short) (this.D + 1);
        Item item2 = new Item(this.D, this.g);
        b(item2);
        if (this.E == null) {
            this.E = new Item[16];
        }
        if (this.D == this.E.length) {
            Object obj = new Object[(this.E.length * 2)];
            System.arraycopy(this.E, 0, obj, 0, this.E.length);
            this.E = obj;
        }
        this.E[this.D] = item2;
        return item2;
    }

    int a(int i, int i2) {
        this.h.b = 15;
        this.h.d = ((long) i) | (((long) i2) << 32);
        this.h.j = Integer.MAX_VALUE & ((i + 15) + i2);
        Item a = a(this.h);
        if (a == null) {
            String str = this.E[i].g;
            String str2 = this.E[i2].g;
            this.h.c = c(getCommonSuperClass(str, str2));
            a = new Item(0, this.h);
            b(a);
        }
        return a.c;
    }

    int a(String str, int i) {
        this.g.b = 14;
        this.g.c = i;
        this.g.g = str;
        this.g.j = Integer.MAX_VALUE & ((str.hashCode() + 14) + i);
        Item a = a(this.g);
        if (a == null) {
            a = c(this.g);
        }
        return a.a;
    }

    Item a(double d) {
        this.g.a(d);
        Item a = a(this.g);
        if (a != null) {
            return a;
        }
        this.d.putByte(FragmentManagerImpl.ANIM_STYLE_FADE_EXIT).putLong(this.g.d);
        a = new Item(this.c, this.g);
        b(a);
        this.c += 2;
        return a;
    }

    Item a(float f) {
        this.g.a(f);
        Item a = a(this.g);
        if (a != null) {
            return a;
        }
        this.d.putByte(JsonWriteContext.STATUS_EXPECT_VALUE).putInt(this.g.c);
        int i = this.c;
        this.c = i + 1;
        a = new Item(i, this.g);
        b(a);
        return a;
    }

    Item a(int i) {
        this.g.a(i);
        Item a = a(this.g);
        if (a != null) {
            return a;
        }
        this.d.putByte(JsonWriteContext.STATUS_OK_AFTER_SPACE).putInt(i);
        int i2 = this.c;
        this.c = i2 + 1;
        a = new Item(i2, this.g);
        b(a);
        return a;
    }

    Item a(long j) {
        this.g.a(j);
        Item a = a(this.g);
        if (a != null) {
            return a;
        }
        this.d.putByte(JsonWriteContext.STATUS_EXPECT_NAME).putLong(j);
        a = new Item(this.c, this.g);
        b(a);
        this.c += 2;
        return a;
    }

    Item a(Object obj) {
        if (obj instanceof Integer) {
            return a(((Integer) obj).intValue());
        }
        if (obj instanceof Byte) {
            return a(((Byte) obj).intValue());
        }
        if (obj instanceof Character) {
            return a(((Character) obj).charValue());
        }
        if (obj instanceof Short) {
            return a(((Short) obj).intValue());
        }
        if (obj instanceof Boolean) {
            return a(((Boolean) obj).booleanValue() ? COMPUTE_MAXS : 0);
        } else if (obj instanceof Float) {
            return a(((Float) obj).floatValue());
        } else {
            if (obj instanceof Long) {
                return a(((Long) obj).longValue());
            }
            if (obj instanceof Double) {
                return a(((Double) obj).doubleValue());
            }
            if (obj instanceof String) {
                return b((String) obj);
            }
            if (obj instanceof Type) {
                Type type = (Type) obj;
                return a(type.getSort() == 10 ? type.getInternalName() : type.getDescriptor());
            } else {
                throw new IllegalArgumentException(new StringBuffer().append("value ").append(obj).toString());
            }
        }
    }

    Item a(String str) {
        this.h.a(Type.LONG, str, null, null);
        Item a = a(this.h);
        if (a != null) {
            return a;
        }
        this.d.b(Type.LONG, newUTF8(str));
        int i = this.c;
        this.c = i + 1;
        a = new Item(i, this.h);
        b(a);
        return a;
    }

    Item a(String str, String str2) {
        this.h.a(Opcodes.FCONST_1, str, str2, null);
        Item a = a(this.h);
        if (a != null) {
            return a;
        }
        a((int)Opcodes.FCONST_1, newUTF8(str), newUTF8(str2));
        int i = this.c;
        this.c = i + 1;
        a = new Item(i, this.h);
        b(a);
        return a;
    }

    Item a(String str, String str2, String str3) {
        this.i.a(Type.ARRAY, str, str2, str3);
        Item a = a(this.i);
        if (a != null) {
            return a;
        }
        a((int)Type.ARRAY, newClass(str), newNameType(str2, str3));
        int i = this.c;
        this.c = i + 1;
        a = new Item(i, this.i);
        b(a);
        return a;
    }

    Item a(String str, String str2, String str3, boolean z) {
        int i = z ? Opcodes.T_LONG : Type.OBJECT;
        this.i.a(i, str, str2, str3);
        Item a = a(this.i);
        if (a != null) {
            return a;
        }
        a(i, newClass(str), newNameType(str2, str3));
        int i2 = this.c;
        this.c = i2 + 1;
        Item item = new Item(i2, this.i);
        b(item);
        return item;
    }

    int c(String str) {
        this.g.a(Opcodes.FCONST_2, str, null, null);
        Item a = a(this.g);
        if (a == null) {
            a = c(this.g);
        }
        return a.a;
    }

    protected String getCommonSuperClass(String str, String str2) {
        try {
            Class forName = Class.forName(str.replace('/', '.'));
            Class forName2 = Class.forName(str2.replace('/', '.'));
            if (forName.isAssignableFrom(forName2)) {
                return str;
            }
            if (forName2.isAssignableFrom(forName)) {
                return str2;
            }
            if (forName.isInterface() || forName2.isInterface()) {
                return "java/lang/Object";
            }
            do {
                forName = forName.getSuperclass();
            } while (!forName.isAssignableFrom(forName2));
            return forName.getName().replace('.', '/');
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public int newClass(String str) {
        return a(str).a;
    }

    public int newConst(Object obj) {
        return a(obj).a;
    }

    public int newField(String str, String str2, String str3) {
        return a(str, str2, str3).a;
    }

    public int newMethod(String str, String str2, String str3, boolean z) {
        return a(str, str2, str3, z).a;
    }

    public int newNameType(String str, String str2) {
        return a(str, str2).a;
    }

    public int newUTF8(String str) {
        this.g.a(COMPUTE_MAXS, str, null, null);
        Item a = a(this.g);
        if (a == null) {
            this.d.putByte(COMPUTE_MAXS).putUTF8(str);
            int i = this.c;
            this.c = i + 1;
            a = new Item(i, this.g);
            b(a);
        }
        return a.a;
    }

    public byte[] toByteArray() {
        int i;
        int i2 = this.n * 2 + 24;
        FieldWriter fieldWriter = this.y;
        int i3 = 0;
        while (fieldWriter != null) {
            int i4 = i3 + 1;
            i2 += fieldWriter.a();
            fieldWriter = fieldWriter.a;
            i3 = i4;
        }
        MethodWriter methodWriter = this.A;
        int i5 = 0;
        while (methodWriter != null) {
            i4 = i5 + 1;
            i2 += methodWriter.a();
            methodWriter = methodWriter.a;
            i5 = i4;
        }
        if (this.l != 0) {
            i = COMPUTE_MAXS;
            i2 += 8;
            newUTF8("Signature");
        } else {
            i = 0;
        }
        if (this.p != 0) {
            i++;
            i2 += 8;
            newUTF8("SourceFile");
        }
        if (this.q != null) {
            i++;
            i2 += this.q.b + 4;
            newUTF8("SourceDebugExtension");
        }
        if (this.r != 0) {
            i++;
            i2 += 10;
            newUTF8("EnclosingMethod");
        }
        if ((this.j & 131072) != 0) {
            i++;
            i2 += 6;
            newUTF8("Deprecated");
        }
        if ((this.j & 4096) != 0) {
            if ((this.b & 65535) < 49 || (this.j & 262144) != 0) {
                i++;
                i2 += 6;
                newUTF8("Synthetic");
            }
        }
        if (this.x != null) {
            i++;
            i2 += this.x.b + 8;
            newUTF8("InnerClasses");
        }
        if (this.t != null) {
            i++;
            i2 += this.t.a() + 8;
            newUTF8("RuntimeVisibleAnnotations");
        }
        if (this.u != null) {
            i++;
            i2 += this.u.a() + 8;
            newUTF8("RuntimeInvisibleAnnotations");
        }
        int i6 = i2;
        if (this.v != null) {
            i6 += this.v.a(this, null, 0, -1, -1);
            i += this.v.a();
        }
        ByteVector byteVector = new ByteVector(this.d.b + i6);
        byteVector.putInt(-889275714).putInt(this.b);
        byteVector.putShort(this.c).putByteArray(this.d.a, 0, this.d.b);
        byteVector.putShort(((393216 | ((this.j & 262144) / 64)) ^ -1) & this.j).putShort(this.k).putShort(this.m);
        byteVector.putShort(this.n);
        i2 = 0;
        while (i2 < this.n) {
            byteVector.putShort(this.o[i2]);
            i2++;
        }
        byteVector.putShort(i3);
        FieldWriter fieldWriter2 = this.y;
        while (fieldWriter2 != null) {
            fieldWriter2.a(byteVector);
            fieldWriter2 = fieldWriter2.a;
        }
        byteVector.putShort(i5);
        MethodWriter methodWriter2 = this.A;
        while (methodWriter2 != null) {
            methodWriter2.a(byteVector);
            methodWriter2 = methodWriter2.a;
        }
        byteVector.putShort(i);
        if (this.l != 0) {
            byteVector.putShort(newUTF8("Signature")).putInt(COMPUTE_FRAMES).putShort(this.l);
        }
        if (this.p != 0) {
            byteVector.putShort(newUTF8("SourceFile")).putInt(COMPUTE_FRAMES).putShort(this.p);
        }
        if (this.q != null) {
            i = this.q.b - 2;
            byteVector.putShort(newUTF8("SourceDebugExtension")).putInt(i);
            byteVector.putByteArray(this.q.a, COMPUTE_FRAMES, i);
        }
        if (this.r != 0) {
            byteVector.putShort(newUTF8("EnclosingMethod")).putInt(JsonWriteContext.STATUS_EXPECT_VALUE);
            byteVector.putShort(this.r).putShort(this.s);
        }
        if ((this.j & 131072) != 0) {
            byteVector.putShort(newUTF8("Deprecated")).putInt(0);
        }
        if ((this.j & 4096) != 0) {
            if ((this.b & 65535) < 49 || (this.j & 262144) != 0) {
                byteVector.putShort(newUTF8("Synthetic")).putInt(0);
            }
        }
        if (this.x != null) {
            byteVector.putShort(newUTF8("InnerClasses"));
            byteVector.putInt(this.x.b + 2).putShort(this.w);
            byteVector.putByteArray(this.x.a, 0, this.x.b);
        }
        if (this.t != null) {
            byteVector.putShort(newUTF8("RuntimeVisibleAnnotations"));
            this.t.a(byteVector);
        }
        if (this.u != null) {
            byteVector.putShort(newUTF8("RuntimeInvisibleAnnotations"));
            this.u.a(byteVector);
        }
        if (this.v != null) {
            this.v.a(this, null, 0, -1, -1, byteVector);
        }
        if (!this.I) {
            return byteVector.a;
        }
        Object classWriter = new ClassWriter(2);
        new ClassReader(byteVector.a).accept(classWriter, JsonWriteContext.STATUS_EXPECT_VALUE);
        return classWriter.toByteArray();
    }

    public void visit(int i, int i2, String str, String str2, String str3, String[] strArr) {
        int i3 = 0;
        this.b = i;
        this.j = i2;
        this.k = newClass(str);
        this.F = str;
        if (str2 != null) {
            this.l = newUTF8(str2);
        }
        this.m = str3 == null ? 0 : newClass(str3);
        if (strArr != null && strArr.length > 0) {
            this.n = strArr.length;
            this.o = new int[this.n];
            while (i3 < this.n) {
                this.o[i3] = newClass(strArr[i3]);
                i3++;
            }
        }
    }

    public AnnotationVisitor visitAnnotation(String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        byteVector.putShort(newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this, true, byteVector, byteVector, 2);
        if (z) {
            annotationWriter.g = this.t;
            this.t = annotationWriter;
        } else {
            annotationWriter.g = this.u;
            this.u = annotationWriter;
        }
        return annotationWriter;
    }

    public void visitAttribute(Attribute attribute) {
        attribute.a = this.v;
        this.v = attribute;
    }

    public void visitEnd() {
    }

    public FieldVisitor visitField(int i, String str, String str2, String str3, Object obj) {
        return new FieldWriter(this, i, str, str2, str3, obj);
    }

    public void visitInnerClass(String str, String str2, String str3, int i) {
        int i2 = 0;
        if (this.x == null) {
            this.x = new ByteVector();
        }
        this.w++;
        this.x.putShort(str == null ? 0 : newClass(str));
        this.x.putShort(str2 == null ? 0 : newClass(str2));
        ByteVector byteVector = this.x;
        if (str3 != null) {
            i2 = newUTF8(str3);
        }
        byteVector.putShort(i2);
        this.x.putShort(i);
    }

    public MethodVisitor visitMethod(int i, String str, String str2, String str3, String[] strArr) {
        return new MethodWriter(this, i, str, str2, str3, strArr, this.H, this.G);
    }

    public void visitOuterClass(String str, String str2, String str3) {
        this.r = newClass(str);
        if (str2 != null && str3 != null) {
            this.s = newNameType(str2, str3);
        }
    }

    public void visitSource(String str, String str2) {
        if (str != null) {
            this.p = newUTF8(str);
        }
        if (str2 != null) {
            this.q = new ByteVector().putUTF8(str2);
        }
    }
}