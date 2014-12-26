package org.codehaus.jackson.org.objectweb.asm;

import android.support.v4.util.TimeUtils;
import android.support.v4.view.MotionEventCompat;
import com.wmt.data.LocalAudioAll;
import com.wmt.remotectrl.EventPacket;
import com.wmt.remotectrl.KeyTouchInputEvent;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.io.CharacterEscapes;
import org.codehaus.jackson.smile.SmileConstants;

class MethodWriter implements MethodVisitor {
    private int A;
    private Handler B;
    private Handler C;
    private int D;
    private ByteVector E;
    private int F;
    private ByteVector G;
    private int H;
    private ByteVector I;
    private Attribute J;
    private boolean K;
    private int L;
    private final int M;
    private Label N;
    private Label O;
    private Label P;
    private int Q;
    private int R;
    private int S;
    MethodWriter a;
    final ClassWriter b;
    private int c;
    private final int d;
    private final int e;
    private final String f;
    String g;
    int h;
    int i;
    int j;
    int[] k;
    private ByteVector l;
    private AnnotationWriter m;
    private AnnotationWriter n;
    private AnnotationWriter[] o;
    private AnnotationWriter[] p;
    private Attribute q;
    private ByteVector r;
    private int s;
    private int t;
    private int u;
    private ByteVector v;
    private int w;
    private int[] x;
    private int y;
    private int[] z;

    MethodWriter(ClassWriter classWriter, int i, String str, String str2, String str3, String[] strArr, boolean z, boolean z2) {
        int i2;
        int i3 = 0;
        this.r = new ByteVector();
        if (classWriter.A == null) {
            classWriter.A = this;
        } else {
            classWriter.B.a = this;
        }
        classWriter.B = this;
        this.b = classWriter;
        this.c = i;
        this.d = classWriter.newUTF8(str);
        this.e = classWriter.newUTF8(str2);
        this.f = str2;
        this.g = str3;
        if (strArr != null && strArr.length > 0) {
            this.j = strArr.length;
            this.k = new int[this.j];
            i2 = 0;
            while (i2 < this.j) {
                this.k[i2] = classWriter.newClass(strArr[i2]);
                i2++;
            }
        }
        if (!z2) {
            i3 = z ? 1 : ClassWriter.COMPUTE_FRAMES;
        }
        this.M = i3;
        if (z || z2) {
            if (z2 && "<init>".equals(str)) {
                this.c |= 262144;
            }
            i2 = Type.getArgumentsAndReturnSizes(this.f) >> 2;
            if ((i & 8) != 0) {
                i2--;
            }
            this.t = i2;
            this.N = new Label();
            Label label = this.N;
            label.a |= 8;
            visitLabel(this.N);
        }
    }

    static int a(byte[] bArr, int i) {
        return ((((bArr[i] & 255) << 24) | ((bArr[i + 1] & 255) << 16)) | ((bArr[i + 2] & 255) << 8)) | (bArr[i + 3] & 255);
    }

    static int a(int[] iArr, int[] iArr2, int i, int i2) {
        int i3 = i2 - i;
        int i4 = 0;
        while (i4 < iArr.length) {
            if (i < iArr[i4] && iArr[i4] <= i2) {
                i3 += iArr2[i4];
            } else if (i2 < iArr[i4] && iArr[i4] <= i) {
                i3 -= iArr2[i4];
            }
            i4++;
        }
        return i3;
    }

    private void a(int i, int i2) {
        while (i < i2) {
            int i3 = this.z[i];
            int i4 = -268435456 & i3;
            if (i4 == 0) {
                i4 = i3 & 1048575;
                switch (i3 & 267386880) {
                    case 24117248:
                        this.v.putByte(Type.LONG).putShort(this.b.newClass(this.b.E[i4].g));
                        break;
                    case 25165824:
                        this.v.putByte(Type.DOUBLE).putShort(this.b.E[i4].c);
                        break;
                    default:
                        this.v.putByte(i4);
                        break;
                }
            } else {
                StringBuffer stringBuffer = new StringBuffer();
                i4 >>= 28;
                while (true) {
                    int i5 = i4 - 1;
                    if (i4 > 0) {
                        stringBuffer.append('[');
                        i4 = i5;
                    } else {
                        if ((i3 & 267386880) == 24117248) {
                            stringBuffer.append('L');
                            stringBuffer.append(this.b.E[i3 & 1048575].g);
                            stringBuffer.append(';');
                        } else {
                            switch (i3 & 15) {
                                case LocalAudioAll.SORT_BY_DATE:
                                    stringBuffer.append('I');
                                    break;
                                case ClassWriter.COMPUTE_FRAMES:
                                    stringBuffer.append('F');
                                    break;
                                case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                                    stringBuffer.append('D');
                                    break;
                                case Type.ARRAY:
                                    stringBuffer.append('Z');
                                    break;
                                case Type.OBJECT:
                                    stringBuffer.append('B');
                                    break;
                                case Opcodes.T_LONG:
                                    stringBuffer.append('C');
                                    break;
                                case Opcodes.FCONST_1:
                                    stringBuffer.append('S');
                                    break;
                                default:
                                    stringBuffer.append('J');
                                    break;
                            }
                        }
                        this.v.putByte(Type.LONG).putShort(this.b.newClass(stringBuffer.toString()));
                    }
                }
            }
            i++;
        }
    }

    private void a(int i, int i2, int i3) {
        int i4 = i2 + 3 + i3;
        if (this.z == null || this.z.length < i4) {
            this.z = new int[i4];
        }
        this.z[0] = i;
        this.z[1] = i2;
        this.z[2] = i3;
        this.y = 3;
    }

    private void a(int i, Label label) {
        Edge edge = new Edge();
        edge.a = i;
        edge.b = label;
        edge.c = this.P.j;
        this.P.j = edge;
    }

    private void a(Object obj) {
        if (obj instanceof String) {
            this.v.putByte(Type.LONG).putShort(this.b.newClass((String) obj));
        } else if (obj instanceof Integer) {
            this.v.putByte(((Integer) obj).intValue());
        } else {
            this.v.putByte(Type.DOUBLE).putShort(((Label) obj).c);
        }
    }

    private void a(Label label, Label[] labelArr) {
        int i = 0;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a((int)Opcodes.LOOKUPSWITCH, 0, null, null);
                a(0, label);
                Label a = label.a();
                a.a |= 16;
                int i2 = 0;
                while (i2 < labelArr.length) {
                    a(0, labelArr[i2]);
                    Label a2 = labelArr[i2].a();
                    a2.a |= 16;
                    i2++;
                }
            } else {
                this.Q--;
                a(this.Q, label);
                while (i < labelArr.length) {
                    a(this.Q, labelArr[i]);
                    i++;
                }
            }
            e();
        }
    }

    static void a(byte[] bArr, int i, int i2) {
        bArr[i] = (byte) (i2 >>> 8);
        bArr[i + 1] = (byte) i2;
    }

    static void a(int[] iArr, int[] iArr2, Label label) {
        if ((label.a & 4) == 0) {
            label.c = a(iArr, iArr2, 0, label.c);
            label.a |= 4;
        }
    }

    static short b(byte[] bArr, int i) {
        return (short) (((bArr[i] & 255) << 8) | (bArr[i + 1] & 255));
    }

    private void b() {
        if (this.x != null) {
            if (this.v == null) {
                this.v = new ByteVector();
            }
            c();
            this.u++;
        }
        this.x = this.z;
        this.z = null;
    }

    private void b(Frame frame) {
        int i = 0;
        int[] iArr = frame.c;
        int[] iArr2 = frame.d;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (i2 < iArr.length) {
            int i5 = iArr[i2];
            if (i5 == 16777216) {
                i4++;
            } else {
                i3 += i4 + 1;
                i4 = 0;
            }
            if (i5 == 16777220 || i5 == 16777219) {
                i2++;
            }
            i2++;
        }
        i2 = 0;
        i4 = 0;
        while (i2 < iArr2.length) {
            i5 = iArr2[i2];
            i4++;
            if (i5 == 16777220 || i5 == 16777219) {
                i2++;
            }
            i2++;
        }
        a(frame.b.c, i3, i4);
        i2 = 0;
        while (i3 > 0) {
            i4 = iArr[i2];
            int[] iArr3 = this.z;
            int i6 = this.y;
            this.y = i6 + 1;
            iArr3[i6] = i4;
            if (i4 == 16777220 || i4 == 16777219) {
                i2++;
            }
            i2++;
            i3--;
        }
        while (i < iArr2.length) {
            i2 = iArr2[i];
            int[] iArr4 = this.z;
            i4 = this.y;
            this.y = i4 + 1;
            iArr4[i4] = i2;
            if (i2 == 16777220 || i2 == 16777219) {
                i++;
            }
            i++;
        }
        b();
    }

    static int c(byte[] bArr, int i) {
        return ((bArr[i] & 255) << 8) | (bArr[i + 1] & 255);
    }

    private void c() {
        boolean z = SmileConstants.TOKEN_PREFIX_TINY_ASCII;
        int i = 0;
        int i2 = this.z[1];
        int i3 = this.z[2];
        if ((this.b.b & 65535) < 50) {
            this.v.putShort(this.z[0]).putShort(i2);
            a((int)JsonWriteContext.STATUS_OK_AFTER_SPACE, i2 + 3);
            this.v.putShort(i3);
            a(i2 + 3, i2 + 3 + i3);
        } else {
            int i4;
            int i5;
            int i6 = this.x[1];
            int i7 = this.u == 0 ? this.z[0] : this.z[0] - this.x[0] - 1;
            if (i3 == 0) {
                i4 = i2 - i6;
                switch (i4) {
                    case -3:
                    case CharacterEscapes.ESCAPE_CUSTOM:
                    case LocalAudioAll.SORT_BY_DEFAULT:
                        z = 248;
                        i6 = i2;
                        break;
                    case LocalAudioAll.SORT_BY_TITLE:
                        z = i7 < 64 ? false : 251;
                        break;
                    case LocalAudioAll.SORT_BY_DATE:
                    case ClassWriter.COMPUTE_FRAMES:
                    case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                        z = SmileConstants.INT_MARKER_END_OF_STRING;
                        break;
                    default:
                        z = true;
                        break;
                }
                i5 = i6;
            } else if (i2 == i6 && i3 == 1) {
                if (i7 >= 63) {
                    z = 247;
                }
                i4 = 0;
                i5 = i6;
            } else {
                i4 = 0;
                z = true;
                i5 = i6;
            }
            if (!z) {
                i6 = 3;
                while (i < i5) {
                    if (this.z[i6] != this.x[i6]) {
                        z = true;
                    } else {
                        i6++;
                        i++;
                    }
                }
            }
            switch (z) {
                case LocalAudioAll.SORT_BY_TITLE:
                    this.v.putByte(i7);
                case SmileConstants.TOKEN_PREFIX_TINY_ASCII:
                    this.v.putByte(i7 + 64);
                    a(i2 + 3, i2 + 4);
                case true:
                    this.v.putByte(247).putShort(i7);
                    a(i2 + 3, i2 + 4);
                case true:
                    this.v.putByte(i4 + 251).putShort(i7);
                case true:
                    this.v.putByte(251).putShort(i7);
                case SmileConstants.INT_MARKER_END_OF_STRING:
                    this.v.putByte(i4 + 251).putShort(i7);
                    a(i5 + 3, i2 + 3);
                default:
                    this.v.putByte(MotionEventCompat.ACTION_MASK).putShort(i7).putShort(i2);
                    a((int)JsonWriteContext.STATUS_OK_AFTER_SPACE, i2 + 3);
                    this.v.putShort(i3);
                    a(i2 + 3, i2 + 3 + i3);
            }
        }
    }

    private void d() {
        byte[] bArr = this.r.a;
        int[] iArr = new int[0];
        int[] iArr2 = new int[0];
        boolean[] zArr = new boolean[this.r.b];
        boolean z = JsonWriteContext.STATUS_OK_AFTER_SPACE;
        while (true) {
            int i;
            if (i == 3) {
                z = ClassWriter.COMPUTE_FRAMES;
            }
            int i2 = i;
            int i3 = 0;
            while (i3 < bArr.length) {
                int i4 = bArr[i3] & 255;
                int i5 = 0;
                switch (ClassWriter.a[i4]) {
                    case LocalAudioAll.SORT_BY_TITLE:
                    case JsonWriteContext.STATUS_EXPECT_VALUE:
                        i3++;
                        break;
                    case LocalAudioAll.SORT_BY_DATE:
                    case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                    case Type.OBJECT:
                        i3 += 2;
                        break;
                    case ClassWriter.COMPUTE_FRAMES:
                    case JsonWriteContext.STATUS_EXPECT_NAME:
                    case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                    case Opcodes.T_LONG:
                    case Opcodes.FCONST_1:
                        i3 += 3;
                        break;
                    case Type.LONG:
                        i3 += 5;
                        break;
                    case Type.DOUBLE:
                        if (i4 > 201) {
                            i4 = i4 < 218 ? i4 - 49 : i4 - 20;
                            i = c(bArr, i3 + 1) + i3;
                        } else {
                            i = b(bArr, i3 + 1) + i3;
                        }
                        i = a(iArr, iArr2, i3, i);
                        boolean z2;
                        if ((i < -32768 || i > 32767) && !zArr[i3]) {
                            z2 = (i == 167 || i == 168) ? ClassWriter.COMPUTE_FRAMES : JsonWriteContext.STATUS_EXPECT_NAME;
                            zArr[i3] = true;
                        } else {
                            z2 = false;
                        }
                        i3 += 3;
                        i5 = i;
                        break;
                    case Type.ARRAY:
                        i3 += 5;
                        break;
                    case Opcodes.FCONST_2:
                        if (i2 == 1) {
                            i5 = -(a(iArr, iArr2, 0, i3) & 3);
                        } else if (!zArr[i3]) {
                            i5 = i3 & 3;
                            zArr[i3] = true;
                        }
                        i3 = i3 + 4 - i3 & 3;
                        i3 += ((a(bArr, i3 + 8) - a(bArr, i3 + 4)) + 1) * 4 + 12;
                        break;
                    case Opcodes.DCONST_0:
                        if (i2 == 1) {
                            i5 = -(a(iArr, iArr2, 0, i3) & 3);
                        } else if (!zArr[i3]) {
                            i5 = i3 & 3;
                            zArr[i3] = true;
                        }
                        i3 = i3 + 4 - i3 & 3;
                        i3 += a(bArr, i3 + 4) * 8 + 8;
                        break;
                    case Segment.TOKENS_PER_SEGMENT:
                        i3 = (bArr[i3 + 1] & 255) == 132 ? i3 + 6 : i3 + 4;
                        break;
                    default:
                        i3 += 4;
                        break;
                }
                if (i5 != 0) {
                    Object obj = new Object[(iArr.length + 1)];
                    Object obj2 = new Object[(iArr2.length + 1)];
                    System.arraycopy(iArr, 0, obj, 0, iArr.length);
                    System.arraycopy(iArr2, 0, obj2, 0, iArr2.length);
                    obj[iArr.length] = i3;
                    obj2[iArr2.length] = i5;
                    Object obj3;
                    Object obj4;
                    if (i5 > 0) {
                        i2 = JsonWriteContext.STATUS_OK_AFTER_SPACE;
                        obj3 = obj2;
                        obj4 = obj;
                    } else {
                        obj3 = obj2;
                        obj4 = obj;
                    }
                }
            }
            if (i2 < 3) {
                i2--;
            }
            if (i2 == 0) {
                ByteVector byteVector = new ByteVector(this.r.b);
                i3 = 0;
                while (i3 < this.r.b) {
                    i = bArr[i3] & 255;
                    int i6;
                    switch (ClassWriter.a[i]) {
                        case LocalAudioAll.SORT_BY_TITLE:
                        case JsonWriteContext.STATUS_EXPECT_VALUE:
                            byteVector.putByte(i);
                            i3++;
                            break;
                        case LocalAudioAll.SORT_BY_DATE:
                        case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                        case Type.OBJECT:
                            byteVector.putByteArray(bArr, i3, ClassWriter.COMPUTE_FRAMES);
                            i3 += 2;
                            break;
                        case ClassWriter.COMPUTE_FRAMES:
                        case JsonWriteContext.STATUS_EXPECT_NAME:
                        case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                        case Opcodes.T_LONG:
                        case Opcodes.FCONST_1:
                            byteVector.putByteArray(bArr, i3, JsonWriteContext.STATUS_OK_AFTER_SPACE);
                            i3 += 3;
                            break;
                        case Type.LONG:
                            byteVector.putByteArray(bArr, i3, JsonWriteContext.STATUS_EXPECT_NAME);
                            i3 += 5;
                            break;
                        case Type.DOUBLE:
                            if (i > 201) {
                                i = i < 218 ? i - 49 : i - 20;
                                i2 = c(bArr, i3 + 1) + i3;
                            } else {
                                i2 = b(bArr, i3 + 1) + i3;
                            }
                            i4 = a(iArr, iArr2, i3, i2);
                            if (zArr[i3]) {
                                if (i == 167) {
                                    byteVector.putByte(EventPacket.SHOW_IME);
                                    i2 = i4;
                                } else if (i == 168) {
                                    byteVector.putByte(EventPacket.HIDE_IME);
                                    i2 = i4;
                                } else {
                                    byteVector.putByte(i <= 166 ? (i + 1) ^ 1 - 1 : i ^ 1);
                                    byteVector.putShort(Type.DOUBLE);
                                    byteVector.putByte(EventPacket.SHOW_IME);
                                    i2 = i4 - 3;
                                }
                                byteVector.putInt(i2);
                            } else {
                                byteVector.putByte(i);
                                byteVector.putShort(i4);
                            }
                            i3 += 3;
                            break;
                        case Type.ARRAY:
                            i2 = a(iArr, iArr2, i3, a(bArr, i3 + 1) + i3);
                            byteVector.putByte(i);
                            byteVector.putInt(i2);
                            i3 += 5;
                            break;
                        case Opcodes.FCONST_2:
                            i2 = i3 + 4 - i3 & 3;
                            byteVector.putByte(Opcodes.TABLESWITCH);
                            byteVector.putByteArray(null, 0, (4 - (byteVector.b % 4)) % 4);
                            i2 += 4;
                            byteVector.putInt(a(iArr, iArr2, i3, a(bArr, i2) + i3));
                            i = a(bArr, i2);
                            i4 = i2 + 4;
                            byteVector.putInt(i);
                            i2 = a(bArr, i4) - i + 1;
                            i = i4 + 4;
                            byteVector.putInt(a(bArr, i - 4));
                            i6 = i2;
                            i2 = i;
                            i = i6;
                            while (i > 0) {
                                i4 = i2 + 4;
                                byteVector.putInt(a(iArr, iArr2, i3, i3 + a(bArr, i2)));
                                i--;
                                i2 = i4;
                            }
                            break;
                        case Opcodes.DCONST_0:
                            i2 = i3 + 4 - i3 & 3;
                            byteVector.putByte(Opcodes.LOOKUPSWITCH);
                            byteVector.putByteArray(null, 0, (4 - (byteVector.b % 4)) % 4);
                            i4 = i2 + 4;
                            byteVector.putInt(a(iArr, iArr2, i3, a(bArr, i2) + i3));
                            i2 = a(bArr, i4);
                            i = i4 + 4;
                            byteVector.putInt(i2);
                            i6 = i2;
                            i2 = i;
                            i = i6;
                            while (i > 0) {
                                byteVector.putInt(a(bArr, i2));
                                i2 += 4;
                                i4 = i2 + 4;
                                byteVector.putInt(a(iArr, iArr2, i3, i3 + a(bArr, i2)));
                                i--;
                                i2 = i4;
                            }
                            break;
                        case Segment.TOKENS_PER_SEGMENT:
                            if ((bArr[i3 + 1] & 255) == 132) {
                                byteVector.putByteArray(bArr, i3, FragmentManagerImpl.ANIM_STYLE_FADE_EXIT);
                                i3 += 6;
                            } else {
                                byteVector.putByteArray(bArr, i3, JsonWriteContext.STATUS_EXPECT_VALUE);
                                i3 += 4;
                            }
                            break;
                        default:
                            byteVector.putByteArray(bArr, i3, JsonWriteContext.STATUS_EXPECT_VALUE);
                            i3 += 4;
                            break;
                    }
                    i3 = i2;
                }
                if (this.u > 0) {
                    if (this.M == 0) {
                        this.u = 0;
                        this.v = null;
                        this.x = null;
                        this.z = null;
                        Frame frame = new Frame();
                        frame.b = this.N;
                        frame.a(this.b, this.c, Type.getArgumentTypes(this.f), this.t);
                        b(frame);
                        Label label = this.N;
                        while (label != null) {
                            i2 = label.c - 3;
                            if ((label.a & 32) != 0 || (i2 >= 0 && zArr[i2])) {
                                a(iArr, iArr2, label);
                                b(label.h);
                            }
                            label = label.i;
                        }
                    } else {
                        this.b.I = true;
                    }
                }
                Handler handler = this.B;
                while (handler != null) {
                    a(iArr, iArr2, handler.a);
                    a(iArr, iArr2, handler.b);
                    a(iArr, iArr2, handler.c);
                    handler = handler.f;
                }
                i = 0;
                while (i < 2) {
                    ByteVector byteVector2 = i == 0 ? this.E : this.G;
                    if (byteVector2 != null) {
                        byte[] bArr2 = byteVector2.a;
                        i3 = 0;
                        while (i3 < byteVector2.b) {
                            int c = c(bArr2, i3);
                            int a = a(iArr, iArr2, 0, c);
                            a(bArr2, i3, a);
                            a(bArr2, i3 + 2, a(iArr, iArr2, 0, c + c(bArr2, i3 + 2)) - a);
                            i3 += 10;
                        }
                    }
                    i++;
                }
                if (this.I != null) {
                    byte[] bArr3 = this.I.a;
                    i3 = 0;
                    while (i3 < this.I.b) {
                        a(bArr3, i3, a(iArr, iArr2, 0, c(bArr3, i3)));
                        i3 += 4;
                    }
                }
                Attribute attribute = this.J;
                while (attribute != null) {
                    Label[] labels = attribute.getLabels();
                    if (labels != null) {
                        i3 = labels.length - 1;
                        while (i3 >= 0) {
                            a(iArr, iArr2, labels[i3]);
                            i3--;
                        }
                    }
                    attribute = attribute.a;
                }
                this.r = byteVector;
                return;
            } else {
                i3 = i2;
            }
        }
    }

    private void e() {
        if (this.M == 0) {
            Label label = new Label();
            label.h = new Frame();
            label.h.b = label;
            label.a(this, this.r.b, this.r.a);
            this.O.i = label;
            this.O = label;
        } else {
            this.P.g = this.R;
        }
        this.P = null;
    }

    final int a() {
        if (this.h != 0) {
            return this.i + 6;
        }
        int i;
        int length;
        if (this.K) {
            d();
        }
        int i2 = Type.DOUBLE;
        if (this.r.b > 0) {
            this.b.newUTF8("Code");
            i = this.r.b + 18 + this.A * 8 + 8;
            if (this.E != null) {
                this.b.newUTF8("LocalVariableTable");
                i += this.E.b + 8;
            }
            if (this.G != null) {
                this.b.newUTF8("LocalVariableTypeTable");
                i += this.G.b + 8;
            }
            if (this.I != null) {
                this.b.newUTF8("LineNumberTable");
                i += this.I.b + 8;
            }
            if (this.v != null) {
                this.b.newUTF8(((this.b.b & 65535) >= 50 ? 1 : false) != 0 ? "StackMapTable" : "StackMap");
                i2 = i + this.v.b + 8;
            } else {
                i2 = i;
            }
            if (this.J != null) {
                i2 += this.J.a(this.b, this.r.a, this.r.b, this.s, this.t);
            }
        }
        if (this.j > 0) {
            this.b.newUTF8("Exceptions");
            i2 += this.j * 2 + 8;
        }
        if ((this.c & 4096) != 0) {
            if ((this.b.b & 65535) < 49 || (this.c & 262144) != 0) {
                this.b.newUTF8("Synthetic");
                i2 += 6;
            }
        }
        if ((this.c & 131072) != 0) {
            this.b.newUTF8("Deprecated");
            i2 += 6;
        }
        if (this.g != null) {
            this.b.newUTF8("Signature");
            this.b.newUTF8(this.g);
            i2 += 8;
        }
        if (this.l != null) {
            this.b.newUTF8("AnnotationDefault");
            i2 += this.l.b + 6;
        }
        if (this.m != null) {
            this.b.newUTF8("RuntimeVisibleAnnotations");
            i2 += this.m.a() + 8;
        }
        if (this.n != null) {
            this.b.newUTF8("RuntimeInvisibleAnnotations");
            i2 += this.n.a() + 8;
        }
        if (this.o != null) {
            this.b.newUTF8("RuntimeVisibleParameterAnnotations");
            length = i2 + (this.o.length - this.S) * 2 + 7;
            i = this.o.length - 1;
            while (i >= this.S) {
                length += this.o[i] == null ? 0 : this.o[i].a();
                i--;
            }
        } else {
            length = i2;
        }
        if (this.p != null) {
            this.b.newUTF8("RuntimeInvisibleParameterAnnotations");
            length += (this.p.length - this.S) * 2 + 7;
            i = this.p.length - 1;
            while (i >= this.S) {
                length += this.p[i] == null ? 0 : this.p[i].a();
                i--;
            }
        }
        i2 = length;
        return this.q != null ? i2 + this.q.a(this.b, null, 0, -1, -1) : i2;
    }

    final void a(ByteVector byteVector) {
        boolean z = 1;
        byteVector.putShort(((393216 | ((this.c & 262144) / 64)) ^ -1) & this.c).putShort(this.d).putShort(this.e);
        if (this.h != 0) {
            byteVector.putByteArray(this.b.J.b, this.h, this.i);
        } else {
            int i = this.r.b > 0 ? 1 : 0;
            if (this.j > 0) {
                i++;
            }
            if ((this.c & 4096) != 0) {
                if ((this.b.b & 65535) < 49 || (this.c & 262144) != 0) {
                    i++;
                }
            }
            if ((this.c & 131072) != 0) {
                i++;
            }
            if (this.g != null) {
                i++;
            }
            if (this.l != null) {
                i++;
            }
            if (this.m != null) {
                i++;
            }
            if (this.n != null) {
                i++;
            }
            if (this.o != null) {
                i++;
            }
            if (this.p != null) {
                i++;
            }
            if (this.q != null) {
                i += this.q.a();
            }
            byteVector.putShort(i);
            if (this.r.b > 0) {
                i = this.r.b + 12 + this.A * 8;
                if (this.E != null) {
                    i += this.E.b + 8;
                }
                if (this.G != null) {
                    i += this.G.b + 8;
                }
                if (this.I != null) {
                    i += this.I.b + 8;
                }
                int i2 = this.v != null ? i + this.v.b + 8 : i;
                if (this.J != null) {
                    i2 += this.J.a(this.b, this.r.a, this.r.b, this.s, this.t);
                }
                byteVector.putShort(this.b.newUTF8("Code")).putInt(i2);
                byteVector.putShort(this.s).putShort(this.t);
                byteVector.putInt(this.r.b).putByteArray(this.r.a, 0, this.r.b);
                byteVector.putShort(this.A);
                if (this.A > 0) {
                    Handler handler = this.B;
                    while (handler != null) {
                        byteVector.putShort(handler.a.c).putShort(handler.b.c).putShort(handler.c.c).putShort(handler.e);
                        handler = handler.f;
                    }
                }
                i = this.E != null ? 1 : 0;
                if (this.G != null) {
                    i++;
                }
                if (this.I != null) {
                    i++;
                }
                if (this.v != null) {
                    i++;
                }
                if (this.J != null) {
                    i += this.J.a();
                }
                byteVector.putShort(i);
                if (this.E != null) {
                    byteVector.putShort(this.b.newUTF8("LocalVariableTable"));
                    byteVector.putInt(this.E.b + 2).putShort(this.D);
                    byteVector.putByteArray(this.E.a, 0, this.E.b);
                }
                if (this.G != null) {
                    byteVector.putShort(this.b.newUTF8("LocalVariableTypeTable"));
                    byteVector.putInt(this.G.b + 2).putShort(this.F);
                    byteVector.putByteArray(this.G.a, 0, this.G.b);
                }
                if (this.I != null) {
                    byteVector.putShort(this.b.newUTF8("LineNumberTable"));
                    byteVector.putInt(this.I.b + 2).putShort(this.H);
                    byteVector.putByteArray(this.I.a, 0, this.I.b);
                }
                if (this.v != null) {
                    if ((this.b.b & 65535) < 50) {
                        z = false;
                    }
                    byteVector.putShort(this.b.newUTF8(i != 0 ? "StackMapTable" : "StackMap"));
                    byteVector.putInt(this.v.b + 2).putShort(this.u);
                    byteVector.putByteArray(this.v.a, 0, this.v.b);
                }
                if (this.J != null) {
                    this.J.a(this.b, this.r.a, this.r.b, this.t, this.s, byteVector);
                }
            }
            if (this.j > 0) {
                byteVector.putShort(this.b.newUTF8("Exceptions")).putInt(this.j * 2 + 2);
                byteVector.putShort(this.j);
                i = 0;
                while (i < this.j) {
                    byteVector.putShort(this.k[i]);
                    i++;
                }
            }
            if ((this.c & 4096) != 0) {
                if ((this.b.b & 65535) < 49 || (this.c & 262144) != 0) {
                    byteVector.putShort(this.b.newUTF8("Synthetic")).putInt(0);
                }
            }
            if ((this.c & 131072) != 0) {
                byteVector.putShort(this.b.newUTF8("Deprecated")).putInt(0);
            }
            if (this.g != null) {
                byteVector.putShort(this.b.newUTF8("Signature")).putInt(ClassWriter.COMPUTE_FRAMES).putShort(this.b.newUTF8(this.g));
            }
            if (this.l != null) {
                byteVector.putShort(this.b.newUTF8("AnnotationDefault"));
                byteVector.putInt(this.l.b);
                byteVector.putByteArray(this.l.a, 0, this.l.b);
            }
            if (this.m != null) {
                byteVector.putShort(this.b.newUTF8("RuntimeVisibleAnnotations"));
                this.m.a(byteVector);
            }
            if (this.n != null) {
                byteVector.putShort(this.b.newUTF8("RuntimeInvisibleAnnotations"));
                this.n.a(byteVector);
            }
            if (this.o != null) {
                byteVector.putShort(this.b.newUTF8("RuntimeVisibleParameterAnnotations"));
                AnnotationWriter.a(this.o, this.S, byteVector);
            }
            if (this.p != null) {
                byteVector.putShort(this.b.newUTF8("RuntimeInvisibleParameterAnnotations"));
                AnnotationWriter.a(this.p, this.S, byteVector);
            }
            if (this.q != null) {
                this.q.a(this.b, null, 0, -1, -1, byteVector);
            }
        }
    }

    public AnnotationVisitor visitAnnotation(String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        byteVector.putShort(this.b.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.b, true, byteVector, byteVector, 2);
        if (z) {
            annotationWriter.g = this.m;
            this.m = annotationWriter;
        } else {
            annotationWriter.g = this.n;
            this.n = annotationWriter;
        }
        return annotationWriter;
    }

    public AnnotationVisitor visitAnnotationDefault() {
        this.l = new ByteVector();
        return new AnnotationWriter(this.b, false, this.l, null, 0);
    }

    public void visitAttribute(Attribute attribute) {
        if (attribute.isCodeAttribute()) {
            attribute.a = this.J;
            this.J = attribute;
        } else {
            attribute.a = this.q;
            this.q = attribute;
        }
    }

    public void visitCode() {
    }

    public void visitEnd() {
    }

    public void visitFieldInsn(int i, String str, String str2, String str3) {
        int i2 = 1;
        int i3 = CharacterEscapes.ESCAPE_CUSTOM;
        Item a = this.b.a(str, str2, str3);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, 0, this.b, a);
            } else {
                char charAt = str3.charAt(0);
                switch (i) {
                    case Opcodes.GETSTATIC:
                        i3 = this.Q;
                        if (charAt == 'D' || charAt == 'J') {
                            i2 = ClassWriter.COMPUTE_FRAMES;
                        }
                        i2 += i3;
                        break;
                    case Opcodes.PUTSTATIC:
                        int i4 = this.Q;
                        i2 = (charAt == 'D' || charAt == 'J') ? -2 : -1;
                        i2 += i4;
                        break;
                    case Opcodes.GETFIELD:
                        i3 = this.Q;
                        if (!(charAt == 'D' || charAt == 'J')) {
                            i2 = 0;
                        }
                        i2 += i3;
                        break;
                    default:
                        i2 = this.Q;
                        if (charAt == 'D' || charAt == 'J') {
                            i3 = -3;
                        }
                        i2 += i3;
                        break;
                }
                if (i2 > this.R) {
                    this.R = i2;
                }
                this.Q = i2;
            }
        }
        this.r.b(i, a.a);
    }

    public void visitFrame(int i, int i2, Object[] objArr, int i3, Object[] objArr2) {
        int i4 = 0;
        if (this.M != 0) {
            if (i == -1) {
                a(this.r.b, i2, i3);
                int i5 = 0;
                while (i5 < i2) {
                    int[] iArr;
                    int i6;
                    if (objArr[i5] instanceof String) {
                        iArr = this.z;
                        i6 = this.y;
                        this.y = i6 + 1;
                        iArr[i6] = this.b.c((String) objArr[i5]) | 24117248;
                    } else if (objArr[i5] instanceof Integer) {
                        iArr = this.z;
                        i6 = this.y;
                        this.y = i6 + 1;
                        iArr[i6] = ((Integer) objArr[i5]).intValue();
                    } else {
                        iArr = this.z;
                        i6 = this.y;
                        this.y = i6 + 1;
                        iArr[i6] = this.b.a("", ((Label) objArr[i5]).c) | 25165824;
                    }
                    i5++;
                }
                while (i4 < i3) {
                    int[] iArr2;
                    int i7;
                    if (objArr2[i4] instanceof String) {
                        iArr2 = this.z;
                        i7 = this.y;
                        this.y = i7 + 1;
                        iArr2[i7] = this.b.c((String) objArr2[i4]) | 24117248;
                    } else if (objArr2[i4] instanceof Integer) {
                        iArr2 = this.z;
                        i7 = this.y;
                        this.y = i7 + 1;
                        iArr2[i7] = ((Integer) objArr2[i4]).intValue();
                    } else {
                        iArr2 = this.z;
                        i7 = this.y;
                        this.y = i7 + 1;
                        iArr2[i7] = this.b.a("", ((Label) objArr2[i4]).c) | 25165824;
                    }
                    i4++;
                }
                b();
            } else {
                int i8;
                if (this.v == null) {
                    this.v = new ByteVector();
                    i8 = this.r.b;
                } else {
                    i8 = this.r.b - this.w - 1;
                    if (i8 < 0) {
                        if (i != 3) {
                            throw new IllegalStateException();
                        }
                        return;
                    }
                }
                switch (i) {
                    case LocalAudioAll.SORT_BY_TITLE:
                        this.v.putByte(MotionEventCompat.ACTION_MASK).putShort(i8).putShort(i2);
                        i8 = 0;
                        while (i8 < i2) {
                            a(objArr[i8]);
                            i8++;
                        }
                        this.v.putShort(i3);
                        while (i4 < i3) {
                            a(objArr2[i4]);
                            i4++;
                        }
                        break;
                    case LocalAudioAll.SORT_BY_DATE:
                        this.v.putByte(i2 + 251).putShort(i8);
                        i8 = 0;
                        while (i8 < i2) {
                            a(objArr[i8]);
                            i8++;
                        }
                        break;
                    case ClassWriter.COMPUTE_FRAMES:
                        this.v.putByte(251 - i2).putShort(i8);
                        break;
                    case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                        if (i8 < 64) {
                            this.v.putByte(i8);
                        } else {
                            this.v.putByte(251).putShort(i8);
                        }
                        break;
                    case JsonWriteContext.STATUS_EXPECT_VALUE:
                        if (i8 < 64) {
                            this.v.putByte(i8 + 64);
                        } else {
                            this.v.putByte(247).putShort(i8);
                        }
                        a(objArr2[0]);
                        break;
                }
                this.w = this.r.b;
                this.u++;
            }
        }
    }

    public void visitIincInsn(int i, int i2) {
        if (this.P != null && this.M == 0) {
            this.P.h.a((int)Opcodes.IINC, i, null, null);
        }
        if (this.M != 2) {
            int i3 = i + 1;
            if (i3 > this.t) {
                this.t = i3;
            }
        }
        if (i > 255 || i2 > 127 || i2 < -128) {
            this.r.putByte(SmileConstants.MIN_BUFFER_FOR_POSSIBLE_SHORT_STRING).b(Opcodes.IINC, i).putShort(i2);
        } else {
            this.r.putByte(Opcodes.IINC).a(i, i2);
        }
    }

    public void visitInsn(int i) {
        this.r.putByte(i);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, 0, null, null);
            } else {
                int i2 = this.Q + Frame.a[i];
                if (i2 > this.R) {
                    this.R = i2;
                }
                this.Q = i2;
            }
            if ((i >= 172 && i <= 177) || i == 191) {
                e();
            }
        }
    }

    public void visitIntInsn(int i, int i2) {
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, i2, null, null);
            } else if (i != 188) {
                int i3 = this.Q + 1;
                if (i3 > this.R) {
                    this.R = i3;
                }
                this.Q = i3;
            }
        }
        if (i == 17) {
            this.r.b(i, i2);
        } else {
            this.r.a(i, i2);
        }
    }

    public void visitJumpInsn(int i, Label label) {
        Label label2 = null;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, 0, null, null);
                Label a = label.a();
                a.a |= 16;
                a(0, label);
                if (i != 167) {
                    label2 = new Label();
                }
            } else if (i == 168) {
                if ((label.a & 512) == 0) {
                    label.a |= 512;
                    this.L++;
                }
                label2 = this.P;
                label2.a |= 128;
                a(this.Q + 1, label);
                label2 = new Label();
            } else {
                this.Q += Frame.a[i];
                a(this.Q, label);
            }
        }
        if ((label.a & 2) == 0 || label.c - this.r.b >= -32768) {
            this.r.putByte(i);
            label.a(this, this.r, this.r.b - 1, false);
        } else {
            if (i == 167) {
                this.r.putByte(EventPacket.SHOW_IME);
            } else if (i == 168) {
                this.r.putByte(EventPacket.HIDE_IME);
            } else {
                if (label2 != null) {
                    label2.a |= 16;
                }
                this.r.putByte(i <= 166 ? (i + 1) ^ 1 - 1 : i ^ 1);
                this.r.putShort(Type.DOUBLE);
                this.r.putByte(EventPacket.SHOW_IME);
            }
            label.a(this, this.r, this.r.b - 1, true);
        }
        if (this.P != null) {
            if (label2 != null) {
                visitLabel(label2);
            }
            if (i == 167) {
                e();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void visitLabel(org.codehaus.jackson.org.objectweb.asm.Label r5) {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.org.objectweb.asm.MethodWriter.visitLabel(org.codehaus.jackson.org.objectweb.asm.Label):void");
        /*
        r4 = this;
        r3 = 0;
        r0 = r4.K;
        r1 = r4.r;
        r1 = r1.b;
        r2 = r4.r;
        r2 = r2.a;
        r1 = r5.a(r4, r1, r2);
        r0 = r0 | r1;
        r4.K = r0;
        r0 = r5.a;
        r0 = r0 & 1;
        if (r0 == 0) goto L_0x0019;
    L_0x0018:
        return;
    L_0x0019:
        r0 = r4.M;
        if (r0 != 0) goto L_0x0078;
    L_0x001d:
        r0 = r4.P;
        if (r0 == 0) goto L_0x003e;
    L_0x0021:
        r0 = r5.c;
        r1 = r4.P;
        r1 = r1.c;
        if (r0 != r1) goto L_0x003b;
    L_0x0029:
        r0 = r4.P;
        r1 = r0.a;
        r2 = r5.a;
        r2 = r2 & 16;
        r1 = r1 | r2;
        r0.a = r1;
        r0 = r4.P;
        r0 = r0.h;
        r5.h = r0;
        goto L_0x0018;
    L_0x003b:
        r4.a(r3, r5);
    L_0x003e:
        r4.P = r5;
        r0 = r5.h;
        if (r0 != 0) goto L_0x004f;
    L_0x0044:
        r0 = new org.codehaus.jackson.org.objectweb.asm.Frame;
        r0.<init>();
        r5.h = r0;
        r0 = r5.h;
        r0.b = r5;
    L_0x004f:
        r0 = r4.O;
        if (r0 == 0) goto L_0x0075;
    L_0x0053:
        r0 = r5.c;
        r1 = r4.O;
        r1 = r1.c;
        if (r0 != r1) goto L_0x0071;
    L_0x005b:
        r0 = r4.O;
        r1 = r0.a;
        r2 = r5.a;
        r2 = r2 & 16;
        r1 = r1 | r2;
        r0.a = r1;
        r0 = r4.O;
        r0 = r0.h;
        r5.h = r0;
        r0 = r4.O;
        r4.P = r0;
        goto L_0x0018;
    L_0x0071:
        r0 = r4.O;
        r0.i = r5;
    L_0x0075:
        r4.O = r5;
        goto L_0x0018;
    L_0x0078:
        r0 = r4.M;
        r1 = 1;
        if (r0 != r1) goto L_0x0018;
    L_0x007d:
        r0 = r4.P;
        if (r0 == 0) goto L_0x008c;
    L_0x0081:
        r0 = r4.P;
        r1 = r4.R;
        r0.g = r1;
        r0 = r4.Q;
        r4.a(r0, r5);
    L_0x008c:
        r4.P = r5;
        r4.Q = r3;
        r4.R = r3;
        r0 = r4.O;
        if (r0 == 0) goto L_0x009a;
    L_0x0096:
        r0 = r4.O;
        r0.i = r5;
    L_0x009a:
        r4.O = r5;
        goto L_0x0018;
        */
    }

    public void visitLdcInsn(Object obj) {
        int i;
        Item a = this.b.a(obj);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a((int)Opcodes.LDC, 0, this.b, a);
            } else {
                i = (a.b == 5 || a.b == 6) ? this.Q + 2 : this.Q + 1;
                if (i > this.R) {
                    this.R = i;
                }
                this.Q = i;
            }
        }
        i = a.a;
        if (a.b == 5 || a.b == 6) {
            this.r.b(KeyTouchInputEvent.EV_REP, i);
        } else if (i >= 256) {
            this.r.b(TimeUtils.HUNDRED_DAY_FIELD_LEN, i);
        } else {
            this.r.a(Opcodes.LDC, i);
        }
    }

    public void visitLineNumber(int i, Label label) {
        if (this.I == null) {
            this.I = new ByteVector();
        }
        this.H++;
        this.I.putShort(label.c);
        this.I.putShort(i);
    }

    public void visitLocalVariable(String str, String str2, String str3, Label label, Label label2, int i) {
        int i2 = ClassWriter.COMPUTE_FRAMES;
        if (str3 != null) {
            if (this.G == null) {
                this.G = new ByteVector();
            }
            this.F++;
            this.G.putShort(label.c).putShort(label2.c - label.c).putShort(this.b.newUTF8(str)).putShort(this.b.newUTF8(str3)).putShort(i);
        }
        if (this.E == null) {
            this.E = new ByteVector();
        }
        this.D++;
        this.E.putShort(label.c).putShort(label2.c - label.c).putShort(this.b.newUTF8(str)).putShort(this.b.newUTF8(str2)).putShort(i);
        if (this.M != 2) {
            char charAt = str2.charAt(0);
            if (!(charAt == 'J' || charAt == 'D')) {
                i2 = 1;
            }
            i2 += i;
            if (i2 > this.t) {
                this.t = i2;
            }
        }
    }

    public void visitLookupSwitchInsn(Label label, int[] iArr, Label[] labelArr) {
        int i = 0;
        int i2 = this.r.b;
        this.r.putByte(Opcodes.LOOKUPSWITCH);
        this.r.putByteArray(null, 0, (4 - (this.r.b % 4)) % 4);
        label.a(this, this.r, i2, true);
        this.r.putInt(labelArr.length);
        while (i < labelArr.length) {
            this.r.putInt(iArr[i]);
            labelArr[i].a(this, this.r, i2, true);
            i++;
        }
        a(label, labelArr);
    }

    public void visitMaxs(int i, int i2) {
        Label a;
        Label a2;
        Label label;
        Edge edge;
        Label label2;
        int length;
        int i3;
        int i4;
        if (this.M == 0) {
            Handler handler = this.B;
            while (handler != null) {
                a = handler.a.a();
                a2 = handler.c.a();
                Label a3 = handler.b.a();
                int c = 24117248 | this.b.c(handler.d == null ? "java/lang/Throwable" : handler.d);
                a2.a |= 16;
                label = a;
                while (label != a3) {
                    edge = new Edge();
                    edge.a = c;
                    edge.b = a2;
                    edge.c = label.j;
                    label.j = edge;
                    label = label.i;
                }
                handler = handler.f;
            }
            Frame frame = this.N.h;
            frame.a(this.b, this.c, Type.getArgumentTypes(this.f), this.t);
            b(frame);
            a2 = this.N;
            boolean z = false;
            while (a2 != null) {
                label2 = a2.k;
                a2.k = null;
                Frame frame2 = a2.h;
                if ((a2.a & 16) != 0) {
                    a2.a |= 32;
                }
                a2.a |= 64;
                length = frame2.d.length + a2.g;
                if (length <= i3) {
                    length = i3;
                }
                Edge edge2 = a2.j;
                while (edge2 != null) {
                    a = edge2.b.a();
                    if (frame2.a(this.b, a.h, edge2.a) && a.k == null) {
                        a.k = label2;
                    } else {
                        a = label2;
                    }
                    edge2 = edge2.c;
                    label2 = a;
                }
                a2 = label2;
                i3 = length;
            }
            a2 = this.N;
            length = i3;
            while (a2 != null) {
                Frame frame3 = a2.h;
                if ((a2.a & 32) != 0) {
                    b(frame3);
                }
                if ((a2.a & 64) == 0) {
                    a = a2.i;
                    int i5 = a2.c;
                    i4 = (a == null ? this.r.b : a.c) - 1;
                    if (i4 >= i5) {
                        length = Math.max(length, 1);
                        i3 = i5;
                        while (i3 < i4) {
                            this.r.a[i3] = (byte) 0;
                            i3++;
                        }
                        this.r.a[i4] = (byte) -65;
                        a(i5, 0, 1);
                        int[] iArr = this.z;
                        i5 = this.y;
                        this.y = i5 + 1;
                        iArr[i5] = this.b.c("java/lang/Throwable") | 24117248;
                        b();
                    }
                }
                a2 = a2.i;
            }
            this.s = length;
        } else if (this.M == 1) {
            Handler handler2 = this.B;
            while (handler2 != null) {
                label = handler2.a;
                label2 = handler2.c;
                a2 = handler2.b;
                while (label != a2) {
                    Edge edge3 = new Edge();
                    edge3.a = Integer.MAX_VALUE;
                    edge3.b = label2;
                    if ((label.a & 128) == 0) {
                        edge3.c = label.j;
                        label.j = edge3;
                    } else {
                        edge3.c = label.j.c.c;
                        label.j.c.c = edge3;
                    }
                    label = label.i;
                }
                handler2 = handler2.f;
            }
            if (this.L > 0) {
                this.N.b(null, 1, this.L);
                a = this.N;
                length = 0;
                while (a != null) {
                    if ((a.a & 128) != 0) {
                        label2 = a.j.c.b;
                        if ((label2.a & 1024) == 0) {
                            length++;
                            label2.b(null, ((((long) length) / 32) << 32) | (1 << (length % 32)), this.L);
                        }
                    }
                    a = a.i;
                }
                a = this.N;
                while (a != null) {
                    if ((a.a & 128) != 0) {
                        label = this.N;
                        while (label != null) {
                            label.a &= -2049;
                            label = label.i;
                        }
                        a.j.c.b.b(a, 0, this.L);
                    }
                    a = a.i;
                }
            }
            label2 = this.N;
            i3 = 0;
            while (label2 != null) {
                a2 = label2.k;
                i4 = label2.f;
                length = label2.g + i4;
                if (length <= i3) {
                    length = i3;
                }
                edge = label2.j;
                Edge edge4 = (label2.a & 128) != 0 ? edge.c : edge;
                while (edge4 != null) {
                    label2 = edge4.b;
                    if ((label2.a & 8) == 0) {
                        label2.f = edge4.a == Integer.MAX_VALUE ? 1 : edge4.a + i4;
                        label2.a |= 8;
                        label2.k = a2;
                        a = label2;
                    } else {
                        a = a2;
                    }
                    edge4 = edge4.c;
                    a2 = a;
                }
                label2 = a2;
                i3 = length;
            }
            this.s = i3;
        } else {
            this.s = i;
            this.t = i2;
        }
    }

    public void visitMethodInsn(int i, String str, String str2, String str3) {
        boolean z = i == 185;
        Item a = i == 186 ? this.b.a(str2, str3) : this.b.a(str, str2, str3, z);
        int i2 = a.c;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, 0, this.b, a);
            } else {
                int argumentsAndReturnSizes;
                if (i2 == 0) {
                    argumentsAndReturnSizes = Type.getArgumentsAndReturnSizes(str3);
                    a.c = argumentsAndReturnSizes;
                } else {
                    argumentsAndReturnSizes = i2;
                }
                i2 = (i == 184 || i == 186) ? this.Q - argumentsAndReturnSizes >> 2 + argumentsAndReturnSizes & 3 + 1 : this.Q - argumentsAndReturnSizes >> 2 + argumentsAndReturnSizes & 3;
                if (i2 > this.R) {
                    this.R = i2;
                }
                this.Q = i2;
                i2 = argumentsAndReturnSizes;
            }
        }
        if (z) {
            if (i2 == 0) {
                i2 = Type.getArgumentsAndReturnSizes(str3);
                a.c = i2;
            }
            this.r.b(Opcodes.INVOKEINTERFACE, a.a).a(i2 >> 2, 0);
        } else {
            this.r.b(i, a.a);
            if (i == 186) {
                this.r.putShort(0);
            }
        }
    }

    public void visitMultiANewArrayInsn(String str, int i) {
        Item a = this.b.a(str);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a((int)Opcodes.MULTIANEWARRAY, i, this.b, a);
            } else {
                this.Q += 1 - i;
            }
        }
        this.r.b(Opcodes.MULTIANEWARRAY, a.a).putByte(i);
    }

    public AnnotationVisitor visitParameterAnnotation(int i, String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        if ("Ljava/lang/Synthetic;".equals(str)) {
            this.S = Math.max(this.S, i + 1);
            return new AnnotationWriter(this.b, false, byteVector, null, 0);
        } else {
            byteVector.putShort(this.b.newUTF8(str)).putShort(0);
            AnnotationVisitor annotationWriter = new AnnotationWriter(this.b, true, byteVector, byteVector, 2);
            if (z) {
                if (this.o == null) {
                    this.o = new AnnotationWriter[Type.getArgumentTypes(this.f).length];
                }
                annotationWriter.g = this.o[i];
                this.o[i] = annotationWriter;
                return annotationWriter;
            } else {
                if (this.p == null) {
                    this.p = new AnnotationWriter[Type.getArgumentTypes(this.f).length];
                }
                annotationWriter.g = this.p[i];
                this.p[i] = annotationWriter;
                return annotationWriter;
            }
        }
    }

    public void visitTableSwitchInsn(int i, int i2, Label label, Label[] labelArr) {
        int i3 = 0;
        int i4 = this.r.b;
        this.r.putByte(Opcodes.TABLESWITCH);
        this.r.putByteArray(null, 0, (4 - (this.r.b % 4)) % 4);
        label.a(this, this.r, i4, true);
        this.r.putInt(i).putInt(i2);
        while (i3 < labelArr.length) {
            labelArr[i3].a(this, this.r, i4, true);
            i3++;
        }
        a(label, labelArr);
    }

    public void visitTryCatchBlock(Label label, Label label2, Label label3, String str) {
        this.A++;
        Handler handler = new Handler();
        handler.a = label;
        handler.b = label2;
        handler.c = label3;
        handler.d = str;
        handler.e = str != null ? this.b.newClass(str) : 0;
        if (this.C == null) {
            this.B = handler;
        } else {
            this.C.f = handler;
        }
        this.C = handler;
    }

    public void visitTypeInsn(int i, String str) {
        Item a = this.b.a(str);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, this.r.b, this.b, a);
            } else if (i == 187) {
                int i2 = this.Q + 1;
                if (i2 > this.R) {
                    this.R = i2;
                }
                this.Q = i2;
            }
        }
        this.r.b(i, a.a);
    }

    public void visitVarInsn(int i, int i2) {
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, i2, null, null);
            } else if (i == 169) {
                Label label = this.P;
                label.a |= 256;
                this.P.f = this.Q;
                e();
            } else {
                int i3;
                i3 = this.Q + Frame.a[i];
                if (i3 > this.R) {
                    this.R = i3;
                }
                this.Q = i3;
            }
        }
        if (this.M != 2) {
            i3 = (i == 22 || i == 24 || i == 55 || i == 57) ? i2 + 2 : i2 + 1;
            if (i3 > this.t) {
                this.t = i3;
            }
        }
        if (i2 < 4 && i != 169) {
            this.r.putByte(i < 54 ? (i - 21) << 2 + 26 + i2 : (i - 54) << 2 + 59 + i2);
        } else if (i2 >= 256) {
            this.r.putByte(SmileConstants.MIN_BUFFER_FOR_POSSIBLE_SHORT_STRING).b(i, i2);
        } else {
            this.r.a(i, i2);
        }
        if (i >= 54 && this.M == 0 && this.A > 0) {
            visitLabel(new Label());
        }
    }
}