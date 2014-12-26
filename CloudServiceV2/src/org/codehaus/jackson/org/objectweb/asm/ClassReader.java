package org.codehaus.jackson.org.objectweb.asm;

import android.support.v4.view.MotionEventCompat;
import com.wmt.data.DataManager;
import com.wmt.data.LocalAudioAll;
import java.io.IOException;
import java.io.InputStream;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.smile.SmileConstants;

public class ClassReader {
    public static final int EXPAND_FRAMES = 8;
    public static final int SKIP_CODE = 1;
    public static final int SKIP_DEBUG = 2;
    public static final int SKIP_FRAMES = 4;
    private final int[] a;
    public final byte[] b;
    private final String[] c;
    private final int d;
    public final int header;

    public ClassReader(InputStream inputStream) throws IOException {
        this(a(inputStream));
    }

    public ClassReader(String str) throws IOException {
        this(ClassLoader.getSystemResourceAsStream(new StringBuffer().append(str.replace('.', '/')).append(".class").toString()));
    }

    public ClassReader(byte[] bArr) {
        this(bArr, 0, bArr.length);
    }

    public ClassReader(byte[] bArr, int i, int i2) {
        this.b = bArr;
        this.a = new int[readUnsignedShort(i + 8)];
        int length = this.a.length;
        this.c = new String[length];
        int i3 = 0;
        int i4 = i + 10;
        int i5 = SKIP_CODE;
        int i6 = i4;
        while (i5 < length) {
            this.a[i5] = i6 + 1;
            switch (bArr[i6]) {
                case SKIP_CODE:
                    i4 = readUnsignedShort(i6 + 1) + 3;
                    if (i4 > i3) {
                        i3 = i4;
                    }
                    break;
                case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                case SKIP_FRAMES:
                case Type.ARRAY:
                case Type.OBJECT:
                case Opcodes.T_LONG:
                case Opcodes.FCONST_1:
                    i4 = JsonWriteContext.STATUS_EXPECT_NAME;
                    break;
                case JsonWriteContext.STATUS_EXPECT_NAME:
                case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                    i4 = Type.ARRAY;
                    i5++;
                    break;
                default:
                    i4 = JsonWriteContext.STATUS_OK_AFTER_SPACE;
                    break;
            }
            i5++;
            i6 = i4 + i6;
        }
        this.d = i3;
        this.header = i6;
    }

    private int a(int i, char[] cArr, String str, AnnotationVisitor annotationVisitor) {
        int i2 = 0;
        if (annotationVisitor == null) {
            switch (this.b[i] & 255) {
                case SmileConstants.TOKEN_PREFIX_TINY_ASCII:
                    return a(i + 3, cArr, true, null);
                case Opcodes.DUP_X2:
                    return a(i + 1, cArr, false, null);
                case Opcodes.LSUB:
                    return i + 5;
                default:
                    return i + 3;
            }
        } else {
            int i3 = i + 1;
            switch (this.b[i] & 255) {
                case SmileConstants.TOKEN_PREFIX_TINY_ASCII:
                    return a(i3 + 2, cArr, true, annotationVisitor.visitAnnotation(str, readUTF8(i3, cArr)));
                case DataManager.INCLUDE_LOCAL_VIDEO_ONLY:
                    annotationVisitor.visit(str, new Byte((byte) readInt(this.a[readUnsignedShort(i3)])));
                    return i3 + 2;
                case DataManager.INCLUDE_LOCAL_ALL_ONLY:
                    annotationVisitor.visit(str, new Character((char) readInt(this.a[readUnsignedShort(i3)])));
                    return i3 + 2;
                case DataManager.INCLUDE_LOCAL_AUDIO_ONLY:
                case 70:
                case 73:
                case 74:
                    annotationVisitor.visit(str, readConst(readUnsignedShort(i3), cArr));
                    return i3 + 2;
                case Opcodes.AASTORE:
                    annotationVisitor.visit(str, new Short((short) readInt(this.a[readUnsignedShort(i3)])));
                    return i3 + 2;
                case Opcodes.DUP_X1:
                    annotationVisitor.visit(str, readInt(this.a[readUnsignedShort(i3)]) == 0 ? Boolean.FALSE : Boolean.TRUE);
                    return i3 + 2;
                case Opcodes.DUP_X2:
                    int readUnsignedShort = readUnsignedShort(i3);
                    i3 += 2;
                    if (readUnsignedShort == 0) {
                        return a(i3 - 2, cArr, false, annotationVisitor.visitArray(str));
                    }
                    int i4 = i3 + 1;
                    Object obj;
                    switch (this.b[i3] & 255) {
                        case DataManager.INCLUDE_LOCAL_VIDEO_ONLY:
                            obj = new Object[readUnsignedShort];
                            while (i2 < readUnsignedShort) {
                                obj[i2] = (byte) readInt(this.a[readUnsignedShort(i4)]);
                                i4 += 3;
                                i2++;
                            }
                            annotationVisitor.visit(str, obj);
                            return i4 - 1;
                        case DataManager.INCLUDE_LOCAL_ALL_ONLY:
                            obj = new Object[readUnsignedShort];
                            while (i2 < readUnsignedShort) {
                                obj[i2] = (char) readInt(this.a[readUnsignedShort(i4)]);
                                i4 += 3;
                                i2++;
                            }
                            annotationVisitor.visit(str, obj);
                            return i4 - 1;
                        case DataManager.INCLUDE_LOCAL_AUDIO_ONLY:
                            obj = new Object[readUnsignedShort];
                            while (i2 < readUnsignedShort) {
                                obj[i2] = Double.longBitsToDouble(readLong(this.a[readUnsignedShort(i4)]));
                                i4 += 3;
                                i2++;
                            }
                            annotationVisitor.visit(str, obj);
                            return i4 - 1;
                        case 70:
                            obj = new Object[readUnsignedShort];
                            while (i2 < readUnsignedShort) {
                                obj[i2] = Float.intBitsToFloat(readInt(this.a[readUnsignedShort(i4)]));
                                i4 += 3;
                                i2++;
                            }
                            annotationVisitor.visit(str, obj);
                            return i4 - 1;
                        case 73:
                            obj = new Object[readUnsignedShort];
                            while (i2 < readUnsignedShort) {
                                obj[i2] = readInt(this.a[readUnsignedShort(i4)]);
                                i4 += 3;
                                i2++;
                            }
                            annotationVisitor.visit(str, obj);
                            return i4 - 1;
                        case 74:
                            obj = new Object[readUnsignedShort];
                            while (i2 < readUnsignedShort) {
                                obj[i2] = readLong(this.a[readUnsignedShort(i4)]);
                                i4 += 3;
                                i2++;
                            }
                            annotationVisitor.visit(str, obj);
                            return i4 - 1;
                        case Opcodes.AASTORE:
                            obj = new Object[readUnsignedShort];
                            while (i2 < readUnsignedShort) {
                                obj[i2] = (short) readInt(this.a[readUnsignedShort(i4)]);
                                i4 += 3;
                                i2++;
                            }
                            annotationVisitor.visit(str, obj);
                            return i4 - 1;
                        case Opcodes.DUP_X1:
                            Object obj2 = new Object[readUnsignedShort];
                            i3 = 0;
                            int i5 = i4;
                            while (i3 < readUnsignedShort) {
                                obj2[i3] = readInt(this.a[readUnsignedShort(i5)]) != 0;
                                i5 += 3;
                                i3++;
                            }
                            annotationVisitor.visit(str, obj2);
                            return i5 - 1;
                        default:
                            return a(i4 - 3, cArr, false, annotationVisitor.visitArray(str));
                    }
                case Opcodes.DADD:
                    annotationVisitor.visit(str, Type.getType(readUTF8(i3, cArr)));
                    return i3 + 2;
                case Opcodes.LSUB:
                    annotationVisitor.visitEnum(str, readUTF8(i3, cArr), readUTF8(i3 + 2, cArr));
                    return i3 + 4;
                case Opcodes.DREM:
                    annotationVisitor.visit(str, readUTF8(i3, cArr));
                    return i3 + 2;
                default:
                    return i3;
            }
        }
    }

    private int a(int i, char[] cArr, boolean z, AnnotationVisitor annotationVisitor) {
        int readUnsignedShort = readUnsignedShort(i);
        int i2 = i + 2;
        int i3;
        if (z) {
            i3 = readUnsignedShort;
            readUnsignedShort = i2;
            i2 = i3;
            while (i2 > 0) {
                i2--;
                readUnsignedShort = a(readUnsignedShort + 2, cArr, readUTF8(readUnsignedShort, cArr), annotationVisitor);
            }
        } else {
            i3 = readUnsignedShort;
            readUnsignedShort = i2;
            i2 = i3;
            while (i2 > 0) {
                i2--;
                readUnsignedShort = a(readUnsignedShort, cArr, null, annotationVisitor);
            }
        }
        if (annotationVisitor != null) {
            annotationVisitor.visitEnd();
        }
        return readUnsignedShort;
    }

    private int a(Object[] objArr, int i, int i2, char[] cArr, Label[] labelArr) {
        int i3 = i2 + 1;
        switch (this.b[i2] & 255) {
            case LocalAudioAll.SORT_BY_TITLE:
                objArr[i] = Opcodes.TOP;
                return i3;
            case SKIP_CODE:
                objArr[i] = Opcodes.INTEGER;
                return i3;
            case SKIP_DEBUG:
                objArr[i] = Opcodes.FLOAT;
                return i3;
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                objArr[i] = Opcodes.DOUBLE;
                return i3;
            case SKIP_FRAMES:
                objArr[i] = Opcodes.LONG;
                return i3;
            case JsonWriteContext.STATUS_EXPECT_NAME:
                objArr[i] = Opcodes.NULL;
                return i3;
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                objArr[i] = Opcodes.UNINITIALIZED_THIS;
                return i3;
            case Type.LONG:
                objArr[i] = readClass(i3, cArr);
                return i3 + 2;
            default:
                objArr[i] = readLabel(readUnsignedShort(i3), labelArr);
                return i3 + 2;
        }
    }

    private String a(int i, int i2, char[] cArr) {
        int i3 = i + i2;
        byte[] bArr = this.b;
        int i4 = 0;
        boolean z = false;
        int i5 = 0;
        while (i < i3) {
            int i6;
            int i7 = i + 1;
            byte b = bArr[i];
            switch (z) {
                case LocalAudioAll.SORT_BY_TITLE:
                    int i8 = b & 255;
                    if (i8 < 128) {
                        i6 = i5 + 1;
                        cArr[i5] = (char) i8;
                    } else if (i8 >= 224 || i8 <= 191) {
                        i4 = (char) (i8 & 15);
                        z = SKIP_DEBUG;
                        i6 = i5;
                    } else {
                        i4 = (char) (i8 & 31);
                        z = true;
                        i6 = i5;
                    }
                    break;
                case SKIP_CODE:
                    int i9 = i5 + 1;
                    cArr[i5] = (char) ((b & 63) | (i4 << 6));
                    i6 = i9;
                    z = false;
                    break;
                case SKIP_DEBUG:
                    i4 = (char) ((i4 << 6) | (b & 63));
                    z = true;
                    i6 = i5;
                    break;
                default:
                    i6 = i5;
                    break;
            }
            i5 = i6;
            i = i7;
        }
        return new String(cArr, 0, i5);
    }

    private Attribute a(Attribute[] attributeArr, String str, int i, int i2, char[] cArr, int i3, Label[] labelArr) {
        int i4 = 0;
        while (i4 < attributeArr.length) {
            if (attributeArr[i4].type.equals(str)) {
                return attributeArr[i4].read(this, i, i2, cArr, i3, labelArr);
            }
            i4++;
        }
        return new Attribute(str).read(this, i, i2, null, -1, null);
    }

    private void a(int i, String str, char[] cArr, boolean z, MethodVisitor methodVisitor) {
        int i2 = i + 1;
        int i3 = this.b[i] & 255;
        int length = Type.getArgumentTypes(str).length - i3;
        int i4 = 0;
        while (i4 < length) {
            AnnotationVisitor visitParameterAnnotation = methodVisitor.visitParameterAnnotation(i4, "Ljava/lang/Synthetic;", false);
            if (visitParameterAnnotation != null) {
                visitParameterAnnotation.visitEnd();
            }
            i4++;
        }
        int i5 = i4;
        while (i5 < i3 + length) {
            i4 = readUnsignedShort(i2);
            i2 += 2;
            while (i4 > 0) {
                i2 = a(i2 + 2, cArr, true, methodVisitor.visitParameterAnnotation(i5, readUTF8(i2, cArr), z));
                i4--;
            }
            i5++;
        }
    }

    private static byte[] a(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IOException("Class not found");
        }
        Object obj = new Object[inputStream.available()];
        int i = 0;
        while (true) {
            int read = inputStream.read(obj, i, obj.length - i);
            if (read != -1) {
                read += i;
                if (read == obj.length) {
                    int read2 = inputStream.read();
                    if (read2 < 0) {
                        return obj;
                    }
                    Object obj2 = new Object[(obj.length + 1000)];
                    System.arraycopy(obj, 0, obj2, 0, read);
                    i = read + 1;
                    obj2[read] = (byte) read2;
                    obj = obj2;
                } else {
                    i = read;
                }
            } else if (i >= obj.length) {
                return obj;
            } else {
                Object obj3 = new Object[i];
                System.arraycopy(obj, 0, obj3, 0, i);
                return obj3;
            }
        }
    }

    void a(ClassWriter classWriter) {
        int i;
        char[] cArr = new char[this.d];
        int length = this.a.length;
        Item[] itemArr = new Item[length];
        int i2 = 1;
        while (i2 < length) {
            i = this.a[i2];
            byte b = this.b[i - 1];
            Item item = new Item(i2);
            switch (b) {
                case SKIP_CODE:
                    String str = this.c[i2];
                    if (str == null) {
                        i = this.a[i2];
                        String[] strArr = this.c;
                        str = a(i + 2, readUnsignedShort(i), cArr);
                        strArr[i2] = str;
                    }
                    item.a(b, str, null, null);
                    i = i2;
                    break;
                case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                    item.a(readInt(i));
                    i = i2;
                    break;
                case SKIP_FRAMES:
                    item.a(Float.intBitsToFloat(readInt(i)));
                    i = i2;
                    break;
                case JsonWriteContext.STATUS_EXPECT_NAME:
                    item.a(readLong(i));
                    i = i2 + 1;
                    break;
                case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                    item.a(Double.longBitsToDouble(readLong(i)));
                    i = i2 + 1;
                    break;
                case Type.ARRAY:
                case Type.OBJECT:
                case Opcodes.T_LONG:
                    int i3 = this.a[readUnsignedShort(i + 2)];
                    item.a(b, readClass(i, cArr), readUTF8(i3, cArr), readUTF8(i3 + 2, cArr));
                    i = i2;
                    break;
                case Opcodes.FCONST_1:
                    item.a(b, readUTF8(i, cArr), readUTF8(i + 2, cArr), null);
                    i = i2;
                    break;
                default:
                    item.a(b, readUTF8(i, cArr), null, null);
                    i = i2;
                    break;
            }
            i2 = item.j % itemArr.length;
            item.k = itemArr[i2];
            itemArr[i2] = item;
            i2 = i + 1;
        }
        i = this.a[1] - 1;
        classWriter.d.putByteArray(this.b, i, this.header - i);
        classWriter.e = itemArr;
        classWriter.f = (int) (0.75d * ((double) length));
        classWriter.c = length;
    }

    public void accept(ClassVisitor classVisitor, int i) {
        accept(classVisitor, new Attribute[0], i);
    }

    public void accept(ClassVisitor classVisitor, Attribute[] attributeArr, int i) {
        String str;
        int i2;
        String str2;
        int i3;
        int i4;
        Attribute a;
        boolean z;
        int i5;
        byte[] bArr = this.b;
        char[] cArr = new char[this.d];
        boolean z2 = 0;
        boolean z3 = 0;
        boolean z4 = 0;
        int i6 = this.header;
        int readUnsignedShort = readUnsignedShort(i6);
        String readClass = readClass(i6 + 2, cArr);
        int i7 = this.a[readUnsignedShort(i6 + 4)];
        String readUTF8 = i7 == 0 ? null : readUTF8(i7, cArr);
        String[] strArr = new String[readUnsignedShort(i6 + 6)];
        int i8 = 0;
        i7 = i6 + 8;
        i6 = 0;
        int i9 = i7;
        while (i6 < strArr.length) {
            strArr[i6] = readClass(i9, cArr);
            i6++;
            i9 += 2;
        }
        int i10 = (i & 1) != 0 ? 1 : 0;
        int i11 = (i & 2) != 0 ? 1 : 0;
        int i12 = (i & 8) != 0 ? 1 : 0;
        i7 = i9 + 2;
        int i13 = readUnsignedShort(i9);
        while (i13 > 0) {
            i6 = readUnsignedShort(i7 + 6);
            i7 += 8;
            while (i6 > 0) {
                i7 += readInt(i7 + 2) + 6;
                i6--;
            }
            i13--;
        }
        i6 = readUnsignedShort(i7);
        i7 += 2;
        i13 = i6;
        while (i13 > 0) {
            i6 = readUnsignedShort(i7 + 6);
            i7 += 8;
            while (i6 > 0) {
                i7 += readInt(i7 + 2) + 6;
                i6--;
            }
            i13--;
        }
        String str3 = null;
        boolean z5 = 0;
        boolean z6 = 0;
        boolean z7 = 0;
        boolean z8 = 0;
        boolean z9 = 0;
        int i14 = readUnsignedShort(i7);
        int i15 = i7 + 2;
        while (i14 > 0) {
            Attribute attribute;
            int i16;
            String readUTF82 = readUTF8(i15, cArr);
            String str4;
            String str5;
            Attribute attribute2;
            if ("SourceFile".equals(readUTF82)) {
                str = str;
                readUTF82 = str;
                str4 = str;
                str5 = readUTF8(i15 + 6, cArr);
                i2 = i8;
                attribute2 = attribute;
                str2 = str;
                i3 = i;
                i4 = i16;
            } else if ("InnerClasses".equals(readUTF82)) {
                str = str;
                readUTF82 = str;
                str4 = str;
                str5 = str;
                i2 = i15 + 6;
                attribute2 = attribute;
                str2 = str;
                i3 = i;
                i4 = i16;
            } else if ("EnclosingMethod".equals(readUTF82)) {
                readUTF82 = readClass(i15 + 6, cArr);
                i6 = readUnsignedShort(i15 + 8);
                if (i6 != 0) {
                    str = readUTF8(this.a[i6], cArr);
                    str2 = readUTF8(this.a[i6] + 2, cArr);
                } else {
                    str2 = str;
                    str = str;
                }
                str4 = str;
                str5 = str;
                i2 = i8;
                attribute2 = attribute;
                i4 = i16;
                i3 = i;
            } else if ("Signature".equals(readUTF82)) {
                str3 = readUTF8(i15 + 6, cArr);
                str2 = str;
                str = str;
                readUTF82 = str;
                str4 = str;
                str5 = str;
                i2 = i8;
                attribute2 = attribute;
                i4 = i16;
                i3 = i;
            } else if ("RuntimeVisibleAnnotations".equals(readUTF82)) {
                str = str;
                readUTF82 = str;
                str4 = str;
                str5 = str;
                i2 = i8;
                attribute2 = attribute;
                i3 = i15 + 6;
                str2 = str;
                i4 = i16;
            } else if ("Deprecated".equals(readUTF82)) {
                readUnsignedShort |= 131072;
                str2 = str;
                str = str;
                readUTF82 = str;
                str4 = str;
                str5 = str;
                i2 = i8;
                attribute2 = attribute;
                i4 = i16;
                i3 = i;
            } else if ("Synthetic".equals(readUTF82)) {
                readUnsignedShort |= 266240;
                str2 = str;
                str = str;
                readUTF82 = str;
                str4 = str;
                str5 = str;
                i2 = i8;
                attribute2 = attribute;
                i4 = i16;
                i3 = i;
            } else if ("SourceDebugExtension".equals(readUTF82)) {
                i6 = readInt(i15 + 2);
                str = str;
                readUTF82 = str;
                str4 = a(i15 + 6, i6, new char[i6]);
                str5 = str;
                i2 = i8;
                attribute2 = attribute;
                str2 = str;
                i3 = i;
                i4 = i16;
            } else if ("RuntimeInvisibleAnnotations".equals(readUTF82)) {
                str = str;
                readUTF82 = str;
                str4 = str;
                str5 = str;
                i2 = i8;
                attribute2 = attribute;
                i3 = i;
                str2 = str;
                i4 = i15 + 6;
            } else {
                a = a(attributeArr, readUTF82, i15 + 6, readInt(i15 + 2), cArr, -1, null);
                if (a != null) {
                    a.a = attribute;
                    str = str;
                    readUTF82 = str;
                    str4 = str;
                    str5 = str;
                    i2 = i8;
                    attribute2 = a;
                    str2 = str;
                    i3 = i;
                    i4 = i16;
                } else {
                    str2 = str;
                    str = str;
                    readUTF82 = str;
                    str4 = str;
                    str5 = str;
                    i2 = i8;
                    attribute2 = attribute;
                    i4 = i16;
                    i3 = i;
                }
            }
            z5 = z;
            i14--;
            i8 = i2;
            i15 += readInt(i15 + 2) + 6;
            z4 = z;
            z3 = z9;
            z2 = z8;
            z6 = z;
            String str6 = str2;
            String str7 = str;
            String str8 = readUTF82;
        }
        classVisitor.visit(readInt(SKIP_FRAMES), readUnsignedShort, readClass, str3, readUTF8, strArr);
        if (i11 == 0) {
            if (!(str == null && str == null)) {
                classVisitor.visitSource(str, str);
            }
        }
        if (str8 != null) {
            classVisitor.visitOuterClass(str8, str7, str6);
        }
        int i17 = 1;
        while (i17 >= 0) {
            i7 = i17 == 0 ? i16 : i;
            if (i7 != 0) {
                i6 = i7 + 2;
                i7 = readUnsignedShort(i7);
                while (i7 > 0) {
                    i7--;
                    i6 = a(i6 + 2, cArr, true, classVisitor.visitAnnotation(readUTF8(i6, cArr), i17 != 0));
                }
            }
            i17--;
        }
        while (attribute != null) {
            a = attribute.a;
            attribute.a = null;
            classVisitor.visitAttribute(attribute);
            attribute = a;
        }
        if (i8 != 0) {
            i17 = readUnsignedShort(i8);
            i5 = i8 + 2;
            while (i17 > 0) {
                classVisitor.visitInnerClass(readUnsignedShort(i5) == 0 ? null : readClass(i5, cArr), readUnsignedShort(i5 + 2) == 0 ? null : readClass(i5 + 2, cArr), readUnsignedShort(i5 + 4) == 0 ? null : readUTF8(i5 + 4, cArr), readUnsignedShort(i5 + 6));
                i17--;
                i5 += 8;
            }
        }
        int i18 = i9 + 2;
        int i19 = readUnsignedShort(i9);
        while (i19 > 0) {
            Attribute attribute3;
            int readUnsignedShort2 = readUnsignedShort(i18);
            String readUTF83 = readUTF8(i18 + 2, cArr);
            readClass = readUTF8(i18 + 4, cArr);
            int i20 = 0;
            str3 = null;
            z8 = 0;
            z9 = 0;
            boolean z10 = 0;
            int i21 = readUnsignedShort(i18 + 6);
            i18 += 8;
            while (i21 > 0) {
                boolean z11;
                readUTF82 = readUTF8(i18, cArr);
                Attribute attribute4;
                if ("ConstantValue".equals(readUTF82)) {
                    i6 = readUnsignedShort(i18 + 6);
                    i7 = readUnsignedShort2;
                    attribute4 = attribute3;
                    i17 = i4;
                    i5 = i3;
                } else if ("Signature".equals(readUTF82)) {
                    str3 = readUTF8(i18 + 6, cArr);
                    i6 = i20;
                    i7 = readUnsignedShort2;
                    attribute4 = attribute3;
                    i17 = i4;
                    i5 = i3;
                } else if ("Deprecated".equals(readUTF82)) {
                    i7 = 131072 | readUnsignedShort2;
                    attribute4 = attribute3;
                    i17 = i4;
                    i5 = i3;
                    i6 = i20;
                } else if ("Synthetic".equals(readUTF82)) {
                    i7 = 266240 | readUnsignedShort2;
                    attribute4 = attribute3;
                    i17 = i4;
                    i5 = i3;
                    i6 = i20;
                } else if ("RuntimeVisibleAnnotations".equals(readUTF82)) {
                    i7 = readUnsignedShort2;
                    attribute4 = attribute3;
                    i17 = i4;
                    i5 = i18 + 6;
                    i6 = i20;
                } else if ("RuntimeInvisibleAnnotations".equals(readUTF82)) {
                    i7 = readUnsignedShort2;
                    attribute4 = attribute3;
                    i17 = i18 + 6;
                    i5 = i3;
                    i6 = i20;
                } else {
                    a = a(attributeArr, readUTF82, i18 + 6, readInt(i18 + 2), cArr, -1, null);
                    if (a != null) {
                        a.a = attribute3;
                        i7 = readUnsignedShort2;
                        attribute4 = a;
                        i17 = i4;
                        i5 = i3;
                        i6 = i20;
                    } else {
                        i6 = i20;
                        i7 = readUnsignedShort2;
                        attribute4 = attribute3;
                        i17 = i4;
                        i5 = i3;
                    }
                }
                i20 = i6;
                i21--;
                readUnsignedShort2 = i7;
                i18 += readInt(i18 + 2) + 6;
                z10 = z11;
                z9 = z;
                z8 = z;
            }
            FieldVisitor visitField = classVisitor.visitField(readUnsignedShort2, readUTF83, readClass, str3, i20 == 0 ? null : readConst(i20, cArr));
            if (visitField != null) {
                i17 = 1;
                while (i17 >= 0) {
                    i7 = i17 == 0 ? i4 : i3;
                    if (i7 != 0) {
                        i6 = i7 + 2;
                        i7 = readUnsignedShort(i7);
                        while (i7 > 0) {
                            i7--;
                            i6 = a(i6 + 2, cArr, true, visitField.visitAnnotation(readUTF8(i6, cArr), i17 != 0));
                        }
                    }
                    i17--;
                }
                while (attribute3 != null) {
                    a = attribute3.a;
                    attribute3.a = null;
                    visitField.visitAttribute(attribute3);
                    attribute3 = a;
                }
                visitField.visitEnd();
            }
            i19--;
        }
        int i22 = i18 + 2;
        int i23 = readUnsignedShort(i18);
        while (i23 > 0) {
            boolean z12;
            String[] strArr2;
            i16 = i22 + 6;
            i18 = readUnsignedShort(i22);
            readUTF83 = readUTF8(i22 + 2, cArr);
            readClass = readUTF8(i22 + 4, cArr);
            str3 = null;
            boolean z13 = 0;
            z5 = 0;
            z9 = 0;
            z10 = 0;
            boolean z14 = 0;
            z7 = 0;
            i3 = 0;
            i20 = 0;
            int i24 = readUnsignedShort(i22 + 6);
            i22 += 8;
            while (i24 > 0) {
                int i25;
                Attribute attribute5;
                boolean z15;
                readUTF82 = readUTF8(i22, cArr);
                i5 = readInt(i22 + 2);
                i17 = i22 + 6;
                Attribute attribute6;
                if ("Code".equals(readUTF82)) {
                    if (i10 == 0) {
                        i6 = readUnsignedShort2;
                        i7 = i9;
                        i13 = i4;
                        i2 = i20;
                        i25 = i17;
                        i20 = i18;
                        attribute6 = attribute5;
                        i9 = i19;
                        i4 = i8;
                    }
                    i6 = readUnsignedShort2;
                    i7 = i9;
                    i13 = i4;
                    i2 = i20;
                    i25 = i3;
                    i20 = i18;
                    attribute6 = attribute5;
                    i9 = i19;
                    i4 = i8;
                } else if ("Exceptions".equals(readUTF82)) {
                    i6 = readUnsignedShort2;
                    i7 = i9;
                    i13 = i4;
                    i2 = i17;
                    i25 = i3;
                    i20 = i18;
                    attribute6 = attribute5;
                    i9 = i19;
                    i4 = i8;
                } else if ("Signature".equals(readUTF82)) {
                    str3 = readUTF8(i17, cArr);
                    i6 = readUnsignedShort2;
                    i7 = i9;
                    i13 = i4;
                    i2 = i20;
                    i25 = i3;
                    i20 = i18;
                    attribute6 = attribute5;
                    i9 = i19;
                    i4 = i8;
                } else if ("Deprecated".equals(readUTF82)) {
                    i7 = i9;
                    i13 = i4;
                    i2 = i20;
                    i25 = i3;
                    i20 = 131072 | i18;
                    i9 = i19;
                    i4 = i8;
                    i6 = readUnsignedShort2;
                    attribute6 = attribute5;
                } else if ("RuntimeVisibleAnnotations".equals(readUTF82)) {
                    i6 = readUnsignedShort2;
                    i7 = i9;
                    i13 = i4;
                    i2 = i20;
                    i25 = i3;
                    i20 = i18;
                    attribute6 = attribute5;
                    i9 = i19;
                    i4 = i17;
                } else if ("AnnotationDefault".equals(readUTF82)) {
                    i6 = readUnsignedShort2;
                    i7 = i9;
                    i13 = i17;
                    i2 = i20;
                    i25 = i3;
                    i4 = i8;
                    attribute6 = attribute5;
                    i9 = i19;
                    i20 = i18;
                } else if ("Synthetic".equals(readUTF82)) {
                    i7 = i9;
                    i13 = i4;
                    i2 = i20;
                    i25 = i3;
                    i20 = 266240 | i18;
                    i9 = i19;
                    i4 = i8;
                    i6 = readUnsignedShort2;
                    attribute6 = attribute5;
                } else if ("RuntimeInvisibleAnnotations".equals(readUTF82)) {
                    i6 = readUnsignedShort2;
                    i7 = i9;
                    i13 = i4;
                    i2 = i20;
                    i25 = i3;
                    i20 = i18;
                    attribute6 = attribute5;
                    i9 = i17;
                    i4 = i8;
                } else if ("RuntimeVisibleParameterAnnotations".equals(readUTF82)) {
                    i6 = readUnsignedShort2;
                    i7 = i17;
                    i13 = i4;
                    i2 = i20;
                    i25 = i3;
                    i9 = i19;
                    attribute6 = attribute5;
                    i20 = i18;
                    i4 = i8;
                } else if ("RuntimeInvisibleParameterAnnotations".equals(readUTF82)) {
                    i6 = i17;
                    i7 = i9;
                    i13 = i4;
                    i2 = i20;
                    i25 = i3;
                    attribute6 = attribute5;
                    i20 = i18;
                    i9 = i19;
                    i4 = i8;
                } else {
                    a = a(attributeArr, readUTF82, i17, i5, cArr, -1, null);
                    if (a != null) {
                        a.a = attribute5;
                        i7 = i9;
                        i13 = i4;
                        i2 = i20;
                        i25 = i3;
                        i20 = i18;
                        i9 = i19;
                        i4 = i8;
                        int i26 = readUnsignedShort2;
                        attribute6 = a;
                        i6 = i26;
                    }
                    i6 = readUnsignedShort2;
                    i7 = i9;
                    i13 = i4;
                    i2 = i20;
                    i25 = i3;
                    i20 = i18;
                    attribute6 = attribute5;
                    i9 = i19;
                    i4 = i8;
                }
                i24--;
                i3 = i25;
                i18 = i20;
                i22 = i5 + i17;
                z7 = z14;
                z5 = z10;
                z13 = z9;
                z14 = z15;
                i20 = i2;
                z10 = z12;
                z9 = z11;
            }
            if (i20 == 0) {
                i7 = i20;
                strArr2 = null;
            } else {
                String[] strArr3 = new String[readUnsignedShort(i20)];
                i7 = i20 + 2;
                i6 = 0;
                while (i6 < strArr3.length) {
                    strArr3[i6] = readClass(i7, cArr);
                    i7 += 2;
                    i6++;
                }
                strArr2 = strArr3;
            }
            MethodVisitor visitMethod = classVisitor.visitMethod(i18, readUTF83, readClass, str3, strArr2);
            if (visitMethod != null) {
                if (visitMethod instanceof MethodWriter) {
                    MethodWriter methodWriter = (MethodWriter) visitMethod;
                    if (methodWriter.b.J == this && str3 == methodWriter.g) {
                        z11 = 0;
                        if (strArr2 == null) {
                            z12 = methodWriter.j == 0 ? SKIP_CODE : 0;
                        } else {
                            if (strArr2.length == methodWriter.j) {
                                z11 = SKIP_CODE;
                                i17 = i7;
                                i7 = strArr2.length - 1;
                                while (i7 >= 0) {
                                    i17 -= 2;
                                    if (methodWriter.k[i7] != readUnsignedShort(i17)) {
                                        z12 = 0;
                                    } else {
                                        i7--;
                                    }
                                }
                            }
                            z12 = z11;
                        }
                        if (i7 != 0) {
                            methodWriter.h = i16;
                            methodWriter.i = i22 - i16;
                            i23--;
                        }
                    }
                }
                if (i4 != 0) {
                    AnnotationVisitor visitAnnotationDefault = visitMethod.visitAnnotationDefault();
                    a(i4, cArr, null, visitAnnotationDefault);
                    if (visitAnnotationDefault != null) {
                        visitAnnotationDefault.visitEnd();
                    }
                }
                i17 = 1;
                while (i17 >= 0) {
                    i7 = i17 == 0 ? i19 : i8;
                    if (i7 != 0) {
                        i6 = i7 + 2;
                        i7 = readUnsignedShort(i7);
                        while (i7 > 0) {
                            i7--;
                            i6 = a(i6 + 2, cArr, true, visitMethod.visitAnnotation(readUTF8(i6, cArr), i17 != 0));
                        }
                    }
                    i17--;
                }
                if (i9 != 0) {
                    a(i9, readClass, cArr, true, visitMethod);
                }
                if (readUnsignedShort2 != 0) {
                    a(readUnsignedShort2, readClass, cArr, false, visitMethod);
                }
                while (attribute5 != null) {
                    a = attribute5.a;
                    attribute5.a = null;
                    visitMethod.visitAttribute(attribute5);
                    attribute5 = a;
                }
            }
            if (!(visitMethod == null || i3 == 0)) {
                Attribute attribute7;
                Object[] objArr;
                Object[] objArr2;
                Object[] objArr3;
                int readUnsignedShort3 = readUnsignedShort(i3);
                int readUnsignedShort4 = readUnsignedShort(i3 + 2);
                int readInt = readInt(i3 + 4);
                int i27 = i3 + 8;
                int i28 = i27 + readInt;
                visitMethod.visitCode();
                Label[] labelArr = new Label[(readInt + 2)];
                readLabel(readInt + 1, labelArr);
                i7 = i27;
                while (i7 < i28) {
                    i17 = i7 - i27;
                    switch (ClassWriter.a[bArr[i7] & 255]) {
                        case LocalAudioAll.SORT_BY_TITLE:
                        case SKIP_FRAMES:
                            i6 = i7 + 1;
                            break;
                        case SKIP_CODE:
                        case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                        case Type.OBJECT:
                            i6 = i7 + 2;
                            break;
                        case SKIP_DEBUG:
                        case JsonWriteContext.STATUS_EXPECT_NAME:
                        case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                        case Opcodes.T_LONG:
                        case Opcodes.FCONST_1:
                            i6 = i7 + 3;
                            break;
                        case Type.LONG:
                            i6 = i7 + 5;
                            break;
                        case EXPAND_FRAMES:
                            readLabel(readShort(i7 + 1) + i17, labelArr);
                            i6 = i7 + 3;
                            break;
                        case Type.ARRAY:
                            readLabel(readInt(i7 + 1) + i17, labelArr);
                            i6 = i7 + 5;
                            break;
                        case Opcodes.FCONST_2:
                            i7 = i7 + 4 - i17 & 3;
                            readLabel(readInt(i7) + i17, labelArr);
                            i6 = i7 + 12;
                            i7 = readInt(i7 + 8) - readInt(i7 + 4) + 1;
                            while (i7 > 0) {
                                readLabel(readInt(i6) + i17, labelArr);
                                i7--;
                                i6 += 4;
                            }
                            break;
                        case Opcodes.DCONST_0:
                            i7 = i7 + 4 - i17 & 3;
                            readLabel(readInt(i7) + i17, labelArr);
                            i6 = i7 + 8;
                            i7 = readInt(i7 + 4);
                            while (i7 > 0) {
                                readLabel(readInt(i6 + 4) + i17, labelArr);
                                i7--;
                                i6 += 8;
                            }
                            break;
                        case Segment.TOKENS_PER_SEGMENT:
                            i6 = (bArr[i7 + 1] & 255) == 132 ? i7 + 6 : i7 + 4;
                            break;
                        default:
                            i6 = i7 + 4;
                            break;
                    }
                    i7 = i6;
                }
                i6 = readUnsignedShort(i7);
                i7 += 2;
                while (i6 > 0) {
                    Label readLabel = readLabel(readUnsignedShort(i7), labelArr);
                    Label readLabel2 = readLabel(readUnsignedShort(i7 + 2), labelArr);
                    Label readLabel3 = readLabel(readUnsignedShort(i7 + 4), labelArr);
                    i2 = readUnsignedShort(i7 + 6);
                    if (i2 == 0) {
                        visitMethod.visitTryCatchBlock(readLabel, readLabel2, readLabel3, null);
                    } else {
                        visitMethod.visitTryCatchBlock(readLabel, readLabel2, readLabel3, readUTF8(this.a[i2], cArr));
                    }
                    i7 += 8;
                    i6--;
                }
                int i29 = 0;
                int i30 = 0;
                i2 = 0;
                i5 = 0;
                z = 0;
                z5 = 0;
                boolean z16 = SKIP_CODE;
                boolean z17 = 0;
                int i31 = readUnsignedShort(i7);
                int i32 = i7 + 2;
                while (i31 > 0) {
                    String readUTF84 = readUTF8(i32, cArr);
                    Label readLabel4;
                    if ("LocalVariableTable".equals(readUTF84)) {
                        if (i11 == 0) {
                            i6 = i32 + 6;
                            i7 = readUnsignedShort(i32 + 6);
                            i13 = i32 + 8;
                            while (i7 > 0) {
                                int readUnsignedShort5 = readUnsignedShort(i13);
                                if (labelArr[readUnsignedShort5] == null) {
                                    Label readLabel5 = readLabel(readUnsignedShort5, labelArr);
                                    readLabel5.a |= 1;
                                }
                                readUnsignedShort5 += readUnsignedShort(i13 + 2);
                                if (labelArr[readUnsignedShort5] == null) {
                                    readLabel4 = readLabel(readUnsignedShort5, labelArr);
                                    readLabel4.a |= 1;
                                }
                                i13 += 10;
                                i7--;
                            }
                            i7 = i17;
                            i13 = i5;
                            i17 = i2;
                            i5 = i30;
                            i2 = i6;
                            i6 = i15;
                        }
                        i6 = i15;
                        i7 = i17;
                        i13 = i5;
                        i17 = i2;
                        i5 = i30;
                        i2 = i29;
                    } else if ("LocalVariableTypeTable".equals(readUTF84)) {
                        i7 = i17;
                        i13 = i5;
                        i17 = i2;
                        i5 = i32 + 6;
                        i6 = i15;
                        i2 = i29;
                    } else {
                        if ("LineNumberTable".equals(readUTF84)) {
                            if (i11 == 0) {
                                i6 = readUnsignedShort(i32 + 6);
                                i7 = i32 + 8;
                                while (i6 > 0) {
                                    i13 = readUnsignedShort(i7);
                                    if (labelArr[i13] == null) {
                                        readLabel4 = readLabel(i13, labelArr);
                                        readLabel4.a |= 1;
                                    }
                                    labelArr[i13].b = readUnsignedShort(i7 + 2);
                                    i7 += 4;
                                    i6--;
                                }
                            }
                        } else if ("StackMapTable".equals(readUTF84)) {
                            if ((i & 4) == 0) {
                                i17 = i32 + 8;
                                i5 = i30;
                                i2 = i29;
                                i13 = readInt(i32 + 2);
                                i7 = readUnsignedShort(i32 + 6);
                                i6 = i15;
                            }
                        } else if (!"StackMap".equals(readUTF84)) {
                            i13 = 0;
                            Attribute attribute8 = attribute7;
                            while (i13 < attributeArr.length) {
                                if (attributeArr[i13].type.equals(readUTF84)) {
                                    a = attributeArr[i13].read(this, i32 + 6, readInt(i32 + 2), cArr, i27 - 8, labelArr);
                                    if (a != null) {
                                        a.a = attribute8;
                                        i13++;
                                        attribute8 = a;
                                    }
                                }
                                a = attribute8;
                                i13++;
                                attribute8 = a;
                            }
                            i6 = i15;
                            i13 = i5;
                            attribute7 = attribute8;
                            i5 = i30;
                            i7 = i17;
                            i17 = i2;
                            i2 = i29;
                        } else if ((i & 4) == 0) {
                            i17 = i32 + 8;
                            i13 = readInt(i32 + 2);
                            i7 = readUnsignedShort(i32 + 6);
                            z15 = 0;
                            i5 = i30;
                            i2 = i29;
                        }
                        i6 = i15;
                        i7 = i17;
                        i13 = i5;
                        i17 = i2;
                        i5 = i30;
                        i2 = i29;
                    }
                    z16 = z15;
                    i30 = i5;
                    i29 = i2;
                    i31--;
                    i32 += readInt(i32 + 2) + 6;
                    i5 = i13;
                    i2 = i17;
                    i17 = i7;
                }
                if (i2 != 0) {
                    objArr = new Object[readUnsignedShort4];
                    objArr2 = new Object[readUnsignedShort3];
                    if (i12 != 0) {
                        if ((i18 & 8) != 0) {
                            i6 = 0;
                        } else if ("<init>".equals(readUTF83)) {
                            i6 = SKIP_CODE;
                            objArr[0] = Opcodes.UNINITIALIZED_THIS;
                        } else {
                            i6 = SKIP_CODE;
                            objArr[0] = readClass(this.header + 2, cArr);
                        }
                        i13 = 1;
                        while (true) {
                            readUnsignedShort = i13 + 1;
                            switch (readClass.charAt(i13)) {
                                case DataManager.INCLUDE_LOCAL_VIDEO_ONLY:
                                case DataManager.INCLUDE_LOCAL_ALL_ONLY:
                                case 'I':
                                case Opcodes.AASTORE:
                                case Opcodes.DUP_X1:
                                    i7 = i6 + 1;
                                    objArr[i6] = Opcodes.INTEGER;
                                    i6 = i7;
                                    i13 = readUnsignedShort;
                                    break;
                                case DataManager.INCLUDE_LOCAL_AUDIO_ONLY:
                                    i7 = i6 + 1;
                                    objArr[i6] = Opcodes.DOUBLE;
                                    i6 = i7;
                                    i13 = readUnsignedShort;
                                    break;
                                case 'F':
                                    i7 = i6 + 1;
                                    objArr[i6] = Opcodes.FLOAT;
                                    i6 = i7;
                                    i13 = readUnsignedShort;
                                    break;
                                case 'J':
                                    i7 = i6 + 1;
                                    objArr[i6] = Opcodes.LONG;
                                    i6 = i7;
                                    i13 = readUnsignedShort;
                                    break;
                                case 'L':
                                    while (readClass.charAt(readUnsignedShort) != ';') {
                                        readUnsignedShort++;
                                    }
                                    i7 = i6 + 1;
                                    i20 = i13 + 1;
                                    i13 = readUnsignedShort + 1;
                                    objArr[i6] = readClass.substring(i20, readUnsignedShort);
                                    i6 = i7;
                                    break;
                                case Opcodes.DUP_X2:
                                    while (readClass.charAt(readUnsignedShort) == '[') {
                                        readUnsignedShort++;
                                    }
                                    if (readClass.charAt(readUnsignedShort) == 'L') {
                                        i7 = readUnsignedShort + 1;
                                        while (readClass.charAt(i7) != ';') {
                                            i7++;
                                        }
                                        readUnsignedShort = i7;
                                    }
                                    i7 = i6 + 1;
                                    readUnsignedShort++;
                                    objArr[i6] = readClass.substring(i13, readUnsignedShort);
                                    i6 = i7;
                                    i13 = readUnsignedShort;
                                    break;
                                default:
                                    break;
                            }
                        }
                    } else {
                        i6 = 0;
                    }
                    z12 = -1;
                    i13 = i2;
                    while (i13 < i2 + i5 - 2) {
                        if (bArr[i13] == (byte) 8) {
                            readUnsignedShort = readUnsignedShort(i13 + 1);
                            if (readUnsignedShort >= 0 && readUnsignedShort < readInt && (bArr[i27 + readUnsignedShort] & 255) == 187) {
                                readLabel(readUnsignedShort, labelArr);
                            }
                        }
                        i13++;
                    }
                    i19 = i6;
                    objArr3 = objArr;
                } else {
                    objArr2 = null;
                    i6 = 0;
                    i7 = 0;
                }
                objArr = objArr3;
                i20 = 0;
                i8 = 0;
                int i33 = i19;
                z11 = z12;
                i19 = 0;
                i6 = i17;
                i17 = i2;
                int i34 = i27;
                while (i34 < i28) {
                    String readUTF85;
                    Label[] labelArr2;
                    int[] iArr;
                    Label[] labelArr3;
                    i31 = i34 - i27;
                    Label label = labelArr[i31];
                    if (label != null) {
                        visitMethod.visitLabel(label);
                        if (i11 == 0 && label.b > 0) {
                            visitMethod.visitLineNumber(label.b, label);
                        }
                    }
                    int i35 = i6;
                    while (objArr != null) {
                        if (i13 == i31 || i13 == -1) {
                            if (i15 == 0 || i12 != 0) {
                                visitMethod.visitFrame(-1, i33, objArr, i20, objArr2);
                            } else if (i13 != -1) {
                                visitMethod.visitFrame(i19, i8, objArr, i20, objArr2);
                            }
                            if (i35 > 0) {
                                if (i15 != 0) {
                                    i4 = i17 + 1;
                                    readUnsignedShort = bArr[i17] & 255;
                                    i19 = i13;
                                } else {
                                    readUnsignedShort = MotionEventCompat.ACTION_MASK;
                                    i19 = -1;
                                    i4 = i17;
                                }
                                z11 = 0;
                                if (readUnsignedShort < 64) {
                                    i17 = 3;
                                    i7 = 0;
                                    i6 = readUnsignedShort;
                                } else if (readUnsignedShort < 128) {
                                    i6 = readUnsignedShort - 64;
                                    i4 = a(objArr2, 0, i4, cArr, labelArr);
                                    z = SKIP_FRAMES;
                                    z12 = SKIP_CODE;
                                } else {
                                    i18 = readUnsignedShort(i4);
                                    i4 += 2;
                                    if (readUnsignedShort == 247) {
                                        i4 = a(objArr2, 0, i4, cArr, labelArr);
                                        i17 = 4;
                                        i7 = 1;
                                        i6 = i18;
                                    } else if (readUnsignedShort >= 248 && readUnsignedShort < 251) {
                                        i7 = 251 - readUnsignedShort;
                                        i33 -= i7;
                                        i17 = 2;
                                        i13 = i7;
                                        i7 = 0;
                                        i6 = i18;
                                    } else if (readUnsignedShort == 251) {
                                        i17 = 3;
                                        i7 = 0;
                                        i6 = i18;
                                    } else if (readUnsignedShort < 255) {
                                        if (i12 != 0) {
                                            i6 = i33;
                                        } else {
                                            z15 = 0;
                                        }
                                        i5 = i4;
                                        i17 = i6;
                                        i6 = readUnsignedShort - 251;
                                        while (i6 > 0) {
                                            i20 = i17 + 1;
                                            i5 = a(objArr, i17, i5, cArr, labelArr);
                                            i6--;
                                            i17 = i20;
                                        }
                                        i7 = readUnsignedShort - 251;
                                        i33 += i7;
                                        i17 = 1;
                                        i4 = i5;
                                        i13 = i7;
                                        i7 = 0;
                                        i6 = i18;
                                    } else {
                                        readUnsignedShort = readUnsignedShort(i4);
                                        i5 = i4 + 2;
                                        i17 = 0;
                                        i6 = readUnsignedShort;
                                        while (i6 > 0) {
                                            i20 = i17 + 1;
                                            i5 = a(objArr, i17, i5, cArr, labelArr);
                                            i6--;
                                            i17 = i20;
                                        }
                                        i6 = readUnsignedShort(i5);
                                        i4 = i5 + 2;
                                        i9 = 0;
                                        i7 = i6;
                                        while (i7 > 0) {
                                            i13 = i9 + 1;
                                            i4 = a(objArr2, i9, i4, cArr, labelArr);
                                            i7--;
                                            i9 = i13;
                                        }
                                        i7 = i6;
                                        i13 = readUnsignedShort;
                                        i17 = 0;
                                        i33 = readUnsignedShort;
                                        i6 = i18;
                                    }
                                }
                                i6 = i6 + 1 + i19;
                                readLabel(i6, labelArr);
                                i20 = i7;
                                i8 = i13;
                                i19 = i17;
                                i35--;
                                i13 = i6;
                                i17 = i4;
                            } else {
                                objArr = null;
                            }
                        } else {
                            i5 = bArr[i34] & 255;
                            switch (ClassWriter.a[i5]) {
                                case LocalAudioAll.SORT_BY_TITLE:
                                    visitMethod.visitInsn(i5);
                                    i7 = i34 + 1;
                                    break;
                                case SKIP_CODE:
                                    visitMethod.visitIntInsn(i5, bArr[i34 + 1]);
                                    i7 = i34 + 2;
                                    break;
                                case SKIP_DEBUG:
                                    visitMethod.visitIntInsn(i5, readShort(i34 + 1));
                                    i7 = i34 + 3;
                                    break;
                                case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                                    visitMethod.visitVarInsn(i5, bArr[i34 + 1] & 255);
                                    i7 = i34 + 2;
                                    break;
                                case SKIP_FRAMES:
                                    if (i5 <= 54) {
                                        i6 = i5 - 59;
                                        visitMethod.visitVarInsn(i6 >> 2 + 54, i6 & 3);
                                    } else {
                                        i6 = i5 - 26;
                                        visitMethod.visitVarInsn(i6 >> 2 + 21, i6 & 3);
                                    }
                                    i7 = i34 + 1;
                                    break;
                                case JsonWriteContext.STATUS_EXPECT_NAME:
                                    visitMethod.visitTypeInsn(i5, readClass(i34 + 1, cArr));
                                    i7 = i34 + 3;
                                    break;
                                case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                                case Type.LONG:
                                    i7 = this.a[readUnsignedShort(i34 + 1)];
                                    if (i5 != 186) {
                                        str2 = Opcodes.INVOKEDYNAMIC_OWNER;
                                    } else {
                                        str2 = readClass(i7, cArr);
                                        i7 = this.a[readUnsignedShort(i7 + 2)];
                                    }
                                    readUTF85 = readUTF8(i7, cArr);
                                    str = readUTF8(i7 + 2, cArr);
                                    if (i5 >= 182) {
                                        visitMethod.visitFieldInsn(i5, str2, readUTF85, str);
                                    } else {
                                        visitMethod.visitMethodInsn(i5, str2, readUTF85, str);
                                    }
                                    i7 = (i5 == 185 || i5 == 186) ? i34 + 5 : i34 + 3;
                                    break;
                                case EXPAND_FRAMES:
                                    visitMethod.visitJumpInsn(i5, labelArr[readShort(i34 + 1) + i31]);
                                    i7 = i34 + 3;
                                    break;
                                case Type.ARRAY:
                                    visitMethod.visitJumpInsn(i5 - 33, labelArr[readInt(i34 + 1) + i31]);
                                    i7 = i34 + 5;
                                    break;
                                case Type.OBJECT:
                                    visitMethod.visitLdcInsn(readConst(bArr[i34 + 1] & 255, cArr));
                                    i7 = i34 + 2;
                                    break;
                                case Opcodes.T_LONG:
                                    visitMethod.visitLdcInsn(readConst(readUnsignedShort(i34 + 1), cArr));
                                    i7 = i34 + 3;
                                    break;
                                case Opcodes.FCONST_1:
                                    visitMethod.visitIincInsn(bArr[i34 + 1] & 255, bArr[i34 + 2]);
                                    i7 = i34 + 3;
                                    break;
                                case Opcodes.FCONST_2:
                                    i6 = i34 + 4 - i31 & 3;
                                    i5 = i31 + readInt(i6);
                                    i2 = readInt(i6 + 4);
                                    readUnsignedShort = readInt(i6 + 8);
                                    i7 = i6 + 12;
                                    labelArr2 = new Label[(readUnsignedShort - i2 + 1)];
                                    i6 = 0;
                                    while (i6 < labelArr2.length) {
                                        labelArr2[i6] = labelArr[readInt(i7) + i31];
                                        i7 += 4;
                                        i6++;
                                    }
                                    visitMethod.visitTableSwitchInsn(i2, readUnsignedShort, labelArr[i5], labelArr2);
                                    break;
                                case Opcodes.DCONST_0:
                                    i6 = i34 + 4 - i31 & 3;
                                    i5 = i31 + readInt(i6);
                                    i2 = readInt(i6 + 4);
                                    i7 = i6 + 8;
                                    iArr = new int[i2];
                                    labelArr3 = new Label[i2];
                                    i6 = 0;
                                    while (i6 < iArr.length) {
                                        iArr[i6] = readInt(i7);
                                        labelArr3[i6] = labelArr[readInt(i7 + 4) + i31];
                                        i7 += 8;
                                        i6++;
                                    }
                                    visitMethod.visitLookupSwitchInsn(labelArr[i5], iArr, labelArr3);
                                    break;
                                case Segment.TOKENS_PER_SEGMENT:
                                    i6 = bArr[i34 + 1] & 255;
                                    if (i6 != 132) {
                                        visitMethod.visitIincInsn(readUnsignedShort(i34 + 2), readShort(i34 + 4));
                                        i7 = i34 + 6;
                                    } else {
                                        visitMethod.visitVarInsn(i6, readUnsignedShort(i34 + 2));
                                        i7 = i34 + 4;
                                    }
                                    break;
                                default:
                                    visitMethod.visitMultiANewArrayInsn(readClass(i34 + 1, cArr), bArr[i34 + 3] & 255);
                                    i7 = i34 + 4;
                                    break;
                            }
                            i6 = i35;
                            i34 = i7;
                        }
                    }
                    i5 = bArr[i34] & 255;
                    switch (ClassWriter.a[i5]) {
                        case LocalAudioAll.SORT_BY_TITLE:
                            visitMethod.visitInsn(i5);
                            i7 = i34 + 1;
                            break;
                        case SKIP_CODE:
                            visitMethod.visitIntInsn(i5, bArr[i34 + 1]);
                            i7 = i34 + 2;
                            break;
                        case SKIP_DEBUG:
                            visitMethod.visitIntInsn(i5, readShort(i34 + 1));
                            i7 = i34 + 3;
                            break;
                        case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                            visitMethod.visitVarInsn(i5, bArr[i34 + 1] & 255);
                            i7 = i34 + 2;
                            break;
                        case SKIP_FRAMES:
                            if (i5 <= 54) {
                                i6 = i5 - 26;
                                visitMethod.visitVarInsn(i6 >> 2 + 21, i6 & 3);
                            } else {
                                i6 = i5 - 59;
                                visitMethod.visitVarInsn(i6 >> 2 + 54, i6 & 3);
                            }
                            i7 = i34 + 1;
                            break;
                        case JsonWriteContext.STATUS_EXPECT_NAME:
                            visitMethod.visitTypeInsn(i5, readClass(i34 + 1, cArr));
                            i7 = i34 + 3;
                            break;
                        case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                        case Type.LONG:
                            i7 = this.a[readUnsignedShort(i34 + 1)];
                            if (i5 != 186) {
                                str2 = readClass(i7, cArr);
                                i7 = this.a[readUnsignedShort(i7 + 2)];
                            } else {
                                str2 = Opcodes.INVOKEDYNAMIC_OWNER;
                            }
                            readUTF85 = readUTF8(i7, cArr);
                            str = readUTF8(i7 + 2, cArr);
                            if (i5 >= 182) {
                                visitMethod.visitMethodInsn(i5, str2, readUTF85, str);
                            } else {
                                visitMethod.visitFieldInsn(i5, str2, readUTF85, str);
                            }
                            break;
                        case EXPAND_FRAMES:
                            visitMethod.visitJumpInsn(i5, labelArr[readShort(i34 + 1) + i31]);
                            i7 = i34 + 3;
                            break;
                        case Type.ARRAY:
                            visitMethod.visitJumpInsn(i5 - 33, labelArr[readInt(i34 + 1) + i31]);
                            i7 = i34 + 5;
                            break;
                        case Type.OBJECT:
                            visitMethod.visitLdcInsn(readConst(bArr[i34 + 1] & 255, cArr));
                            i7 = i34 + 2;
                            break;
                        case Opcodes.T_LONG:
                            visitMethod.visitLdcInsn(readConst(readUnsignedShort(i34 + 1), cArr));
                            i7 = i34 + 3;
                            break;
                        case Opcodes.FCONST_1:
                            visitMethod.visitIincInsn(bArr[i34 + 1] & 255, bArr[i34 + 2]);
                            i7 = i34 + 3;
                            break;
                        case Opcodes.FCONST_2:
                            i6 = i34 + 4 - i31 & 3;
                            i5 = i31 + readInt(i6);
                            i2 = readInt(i6 + 4);
                            readUnsignedShort = readInt(i6 + 8);
                            i7 = i6 + 12;
                            labelArr2 = new Label[(readUnsignedShort - i2 + 1)];
                            i6 = 0;
                            while (i6 < labelArr2.length) {
                                labelArr2[i6] = labelArr[readInt(i7) + i31];
                                i7 += 4;
                                i6++;
                            }
                            visitMethod.visitTableSwitchInsn(i2, readUnsignedShort, labelArr[i5], labelArr2);
                            break;
                        case Opcodes.DCONST_0:
                            i6 = i34 + 4 - i31 & 3;
                            i5 = i31 + readInt(i6);
                            i2 = readInt(i6 + 4);
                            i7 = i6 + 8;
                            iArr = new int[i2];
                            labelArr3 = new Label[i2];
                            i6 = 0;
                            while (i6 < iArr.length) {
                                iArr[i6] = readInt(i7);
                                labelArr3[i6] = labelArr[readInt(i7 + 4) + i31];
                                i7 += 8;
                                i6++;
                            }
                            visitMethod.visitLookupSwitchInsn(labelArr[i5], iArr, labelArr3);
                            break;
                        case Segment.TOKENS_PER_SEGMENT:
                            i6 = bArr[i34 + 1] & 255;
                            if (i6 != 132) {
                                visitMethod.visitVarInsn(i6, readUnsignedShort(i34 + 2));
                                i7 = i34 + 4;
                            } else {
                                visitMethod.visitIincInsn(readUnsignedShort(i34 + 2), readShort(i34 + 4));
                                i7 = i34 + 6;
                            }
                            break;
                        default:
                            visitMethod.visitMultiANewArrayInsn(readClass(i34 + 1, cArr), bArr[i34 + 3] & 255);
                            i7 = i34 + 4;
                            break;
                    }
                    i6 = i35;
                    i34 = i7;
                }
                Label label2 = labelArr[i28 - i27];
                if (label2 != null) {
                    visitMethod.visitLabel(label2);
                }
                if (i11 == 0 && i29 != 0) {
                    ?[] Arr = null;
                    if (i30 != 0) {
                        i7 = readUnsignedShort(i30) * 3;
                        i13 = i30 + 2;
                        Arr = new ?[i7];
                        while (i7 > 0) {
                            i7--;
                            Arr[i7] = i13 + 6;
                            i7--;
                            Arr[i7] = readUnsignedShort(i13 + 8);
                            i7--;
                            Arr[i7] = readUnsignedShort(i13);
                            i13 += 10;
                        }
                    }
                    i17 = i29 + 2;
                    i13 = readUnsignedShort(i29);
                    while (i13 > 0) {
                        i5 = readUnsignedShort(i17);
                        i2 = readUnsignedShort(i17 + 2);
                        i9 = readUnsignedShort(i17 + 8);
                        str3 = null;
                        if (Arr != null) {
                            i7 = 0;
                            while (i7 < Arr.length) {
                                if (Arr[i7] == i5 && Arr[i7 + 1] == i9) {
                                    str3 = readUTF8(Arr[i7 + 2], cArr);
                                } else {
                                    i7 += 3;
                                }
                            }
                        }
                        visitMethod.visitLocalVariable(readUTF8(i17 + 4, cArr), readUTF8(i17 + 6, cArr), str3, labelArr[i5], labelArr[i5 + i2], i9);
                        i17 += 10;
                        i13--;
                    }
                }
                while (attribute7 != null) {
                    a = attribute7.a;
                    attribute7.a = null;
                    visitMethod.visitAttribute(attribute7);
                    attribute7 = a;
                }
                visitMethod.visitMaxs(readUnsignedShort3, readUnsignedShort4);
            }
            if (visitMethod != null) {
                visitMethod.visitEnd();
            }
            i23--;
        }
        classVisitor.visitEnd();
    }

    public int getAccess() {
        return readUnsignedShort(this.header);
    }

    public String getClassName() {
        return readClass(this.header + 2, new char[this.d]);
    }

    public String[] getInterfaces() {
        int i = this.header + 6;
        int readUnsignedShort = readUnsignedShort(i);
        String[] strArr = new String[readUnsignedShort];
        if (readUnsignedShort > 0) {
            char[] cArr = new char[this.d];
            int i2 = 0;
            while (i2 < readUnsignedShort) {
                i += 2;
                strArr[i2] = readClass(i, cArr);
                i2++;
            }
        }
        return strArr;
    }

    public int getItem(int i) {
        return this.a[i];
    }

    public String getSuperName() {
        int i = this.a[readUnsignedShort(this.header + 4)];
        return i == 0 ? null : readUTF8(i, new char[this.d]);
    }

    public int readByte(int i) {
        return this.b[i] & 255;
    }

    public String readClass(int i, char[] cArr) {
        return readUTF8(this.a[readUnsignedShort(i)], cArr);
    }

    public Object readConst(int i, char[] cArr) {
        int i2 = this.a[i];
        switch (this.b[i2 - 1]) {
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                return new Integer(readInt(i2));
            case SKIP_FRAMES:
                return new Float(Float.intBitsToFloat(readInt(i2)));
            case JsonWriteContext.STATUS_EXPECT_NAME:
                return new Long(readLong(i2));
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                return new Double(Double.longBitsToDouble(readLong(i2)));
            case Type.LONG:
                return Type.getObjectType(readUTF8(i2, cArr));
            default:
                return readUTF8(i2, cArr);
        }
    }

    public int readInt(int i) {
        byte[] bArr = this.b;
        return (bArr[i + 3] & 255) | ((((bArr[i] & 255) << 24) | ((bArr[i + 1] & 255) << 16)) | ((bArr[i + 2] & 255) << 8));
    }

    protected Label readLabel(int i, Label[] labelArr) {
        if (labelArr[i] == null) {
            labelArr[i] = new Label();
        }
        return labelArr[i];
    }

    public long readLong(int i) {
        return (((long) readInt(i)) << 32) | (((long) readInt(i + 4)) & 4294967295L);
    }

    public short readShort(int i) {
        byte[] bArr = this.b;
        return (short) ((bArr[i + 1] & 255) | ((bArr[i] & 255) << 8));
    }

    public String readUTF8(int i, char[] cArr) {
        int readUnsignedShort = readUnsignedShort(i);
        String str = this.c[readUnsignedShort];
        if (str != null) {
            return str;
        }
        int i2 = this.a[readUnsignedShort];
        String[] strArr = this.c;
        str = a(i2 + 2, readUnsignedShort(i2), cArr);
        strArr[readUnsignedShort] = str;
        return str;
    }

    public int readUnsignedShort(int i) {
        byte[] bArr = this.b;
        return (bArr[i + 1] & 255) | ((bArr[i] & 255) << 8);
    }
}