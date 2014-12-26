package org.codehaus.jackson.org.objectweb.asm;

import com.wmt.data.DataManager;
import com.wmt.data.LocalAudioAll;
import com.wmt.remotectrl.ConnectionInstance;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.smile.SmileConstants;

final class Frame {
    static final int[] a;
    Label b;
    int[] c;
    int[] d;
    private int[] e;
    private int[] f;
    private int g;
    private int h;
    private int[] i;

    static {
        int[] iArr = new int[202];
        String str = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDDCDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCDCDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEFEDDDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE";
        int i = 0;
        while (i < iArr.length) {
            iArr[i] = str.charAt(i) - 69;
            i++;
        }
        a = iArr;
    }

    Frame() {
    }

    private int a() {
        if (this.g > 0) {
            int[] iArr = this.f;
            int i = this.g - 1;
            this.g = i;
            return iArr[i];
        } else {
            Label label = this.b;
            int i2 = label.f - 1;
            label.f = i2;
            return 50331648 | (-i2);
        }
    }

    private int a(int i) {
        if (this.e == null || i >= this.e.length) {
            return 33554432 | i;
        }
        int i2 = this.e[i];
        if (i2 != 0) {
            return i2;
        }
        i2 = 33554432 | i;
        this.e[i] = i2;
        return i2;
    }

    private int a(ClassWriter classWriter, int i) {
        int c;
        if (i == 16777222) {
            c = classWriter.c(classWriter.F) | 24117248;
        } else if ((-1048576 & i) != 25165824) {
            return i;
        } else {
            c = classWriter.c(classWriter.E[1048575 & i].g) | 24117248;
        }
        int i2 = 0;
        while (i2 < this.h) {
            int i3 = this.i[i2];
            int i4 = -268435456 & i3;
            int i5 = 251658240 & i3;
            if (i5 == 33554432) {
                i3 = this.c[i3 & 8388607] + i4;
            } else if (i5 == 50331648) {
                i3 = this.d[this.d.length - i3 & 8388607] + i4;
            }
            if (i == i3) {
                return c;
            }
            i2++;
        }
        return i;
    }

    private void a(int i, int i2) {
        if (this.e == null) {
            this.e = new int[10];
        }
        int length = this.e.length;
        if (i >= length) {
            Object obj = new Object[Math.max(i + 1, length * 2)];
            System.arraycopy(this.e, 0, obj, 0, length);
            this.e = obj;
        }
        this.e[i] = i2;
    }

    private void a(String str) {
        char charAt = str.charAt(0);
        if (charAt == '(') {
            c(Type.getArgumentsAndReturnSizes(str) >> 2 - 1);
        } else if (charAt == 'J' || charAt == 'D') {
            c(ClassWriter.COMPUTE_FRAMES);
        } else {
            c(1);
        }
    }

    private void a(ClassWriter classWriter, String str) {
        int b = b(classWriter, str);
        if (b != 0) {
            b(b);
            if (b == 16777220 || b == 16777219) {
                b(16777216);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean a(org.codehaus.jackson.org.objectweb.asm.ClassWriter r8, int r9, int[] r10, int r11) {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.org.objectweb.asm.Frame.a(org.codehaus.jackson.org.objectweb.asm.ClassWriter, int, int[], int):boolean");
        /*
        r7 = 267386880; // 0xff00000 float:2.3665827E-29 double:1.321066716E-315;
        r1 = 0;
        r6 = -268435456; // 0xfffffffff0000000 float:-1.58456325E29 double:NaN;
        r2 = 16777221; // 0x1000005 float:2.35099E-38 double:8.2890485E-317;
        r5 = 24117248; // 0x1700000 float:4.4081038E-38 double:1.19155037E-316;
        r3 = r10[r11];
        if (r3 != r9) goto L_0x0010;
    L_0x000e:
        r0 = r1;
    L_0x000f:
        return r0;
    L_0x0010:
        r0 = 268435455; // 0xfffffff float:2.5243547E-29 double:1.326247364E-315;
        r0 = r0 & r9;
        if (r0 != r2) goto L_0x007b;
    L_0x0016:
        if (r3 != r2) goto L_0x001a;
    L_0x0018:
        r0 = r1;
        goto L_0x000f;
    L_0x001a:
        r0 = r2;
    L_0x001b:
        if (r3 != 0) goto L_0x0021;
    L_0x001d:
        r10[r11] = r0;
        r0 = 1;
        goto L_0x000f;
    L_0x0021:
        r4 = r3 & r7;
        if (r4 == r5) goto L_0x0029;
    L_0x0025:
        r4 = r3 & r6;
        if (r4 == 0) goto L_0x0069;
    L_0x0029:
        if (r0 != r2) goto L_0x002d;
    L_0x002b:
        r0 = r1;
        goto L_0x000f;
    L_0x002d:
        r2 = -1048576; // 0xfffffffffff00000 float:NaN double:NaN;
        r2 = r2 & r0;
        r4 = -1048576; // 0xfffffffffff00000 float:NaN double:NaN;
        r4 = r4 & r3;
        if (r2 != r4) goto L_0x0057;
    L_0x0035:
        r2 = r3 & r7;
        if (r2 != r5) goto L_0x004f;
    L_0x0039:
        r2 = r0 & r6;
        r2 = r2 | r5;
        r4 = 1048575; // 0xfffff float:1.469367E-39 double:5.18065E-318;
        r0 = r0 & r4;
        r4 = 1048575; // 0xfffff float:1.469367E-39 double:5.18065E-318;
        r4 = r4 & r3;
        r0 = r8.a(r0, r4);
        r0 = r0 | r2;
    L_0x0049:
        if (r3 == r0) goto L_0x0079;
    L_0x004b:
        r10[r11] = r0;
        r0 = 1;
        goto L_0x000f;
    L_0x004f:
        r0 = "java/lang/Object";
        r0 = r8.c(r0);
        r0 = r0 | r5;
        goto L_0x0049;
    L_0x0057:
        r2 = r0 & r7;
        if (r2 == r5) goto L_0x005e;
    L_0x005b:
        r0 = r0 & r6;
        if (r0 == 0) goto L_0x0066;
    L_0x005e:
        r0 = "java/lang/Object";
        r0 = r8.c(r0);
        r0 = r0 | r5;
        goto L_0x0049;
    L_0x0066:
        r0 = 16777216; // 0x1000000 float:2.3509887E-38 double:8.289046E-317;
        goto L_0x0049;
    L_0x0069:
        if (r3 != r2) goto L_0x0076;
    L_0x006b:
        r2 = r0 & r7;
        if (r2 == r5) goto L_0x0049;
    L_0x006f:
        r2 = r0 & r6;
        if (r2 != 0) goto L_0x0049;
    L_0x0073:
        r0 = 16777216; // 0x1000000 float:2.3509887E-38 double:8.289046E-317;
        goto L_0x0049;
    L_0x0076:
        r0 = 16777216; // 0x1000000 float:2.3509887E-38 double:8.289046E-317;
        goto L_0x0049;
    L_0x0079:
        r0 = r1;
        goto L_0x000f;
    L_0x007b:
        r0 = r9;
        goto L_0x001b;
        */
    }

    private static int b(ClassWriter classWriter, String str) {
        int i = 16777217;
        int indexOf = str.charAt(0) == '(' ? str.indexOf(ConnectionInstance.ALL_DISCONNECTED) + 1 : 0;
        switch (str.charAt(indexOf)) {
            case DataManager.INCLUDE_LOCAL_VIDEO_ONLY:
            case DataManager.INCLUDE_LOCAL_ALL_ONLY:
            case 'I':
            case Opcodes.AASTORE:
            case Opcodes.DUP_X1:
                return 16777217;
            case DataManager.INCLUDE_LOCAL_AUDIO_ONLY:
                return 16777219;
            case 'F':
                return 16777218;
            case 'J':
                return 16777220;
            case 'L':
                return 24117248 | classWriter.c(str.substring(indexOf + 1, str.length() - 1));
            case Opcodes.SASTORE:
                return 0;
            default:
                int i2 = indexOf + 1;
                while (str.charAt(i2) == '[') {
                    i2++;
                }
                switch (str.charAt(i2)) {
                    case DataManager.INCLUDE_LOCAL_VIDEO_ONLY:
                        i = 16777226;
                        break;
                    case DataManager.INCLUDE_LOCAL_ALL_ONLY:
                        i = 16777227;
                        break;
                    case DataManager.INCLUDE_LOCAL_AUDIO_ONLY:
                        i = 16777219;
                        break;
                    case 'F':
                        i = 16777218;
                        break;
                    case 'I':
                        break;
                    case 'J':
                        i = 16777220;
                        break;
                    case Opcodes.AASTORE:
                        i = 16777228;
                        break;
                    case Opcodes.DUP_X1:
                        i = 16777225;
                        break;
                    default:
                        i = classWriter.c(str.substring(i2 + 1, str.length() - 1)) | 24117248;
                        break;
                }
                return ((i2 - indexOf) << 28) | i;
        }
    }

    private void b(int i) {
        if (this.f == null) {
            this.f = new int[10];
        }
        int length = this.f.length;
        if (this.g >= length) {
            Object obj = new Object[Math.max(this.g + 1, length * 2)];
            System.arraycopy(this.f, 0, obj, 0, length);
            this.f = obj;
        }
        int[] iArr = this.f;
        int i2 = this.g;
        this.g = i2 + 1;
        iArr[i2] = i;
        length = this.b.f + this.g;
        if (length > this.b.g) {
            this.b.g = length;
        }
    }

    private void c(int i) {
        if (this.g >= i) {
            this.g -= i;
        } else {
            Label label = this.b;
            label.f -= i - this.g;
            this.g = 0;
        }
    }

    private void d(int i) {
        if (this.i == null) {
            this.i = new int[2];
        }
        int length = this.i.length;
        if (this.h >= length) {
            Object obj = new Object[Math.max(this.h + 1, length * 2)];
            System.arraycopy(this.i, 0, obj, 0, length);
            this.i = obj;
        }
        int[] iArr = this.i;
        int i2 = this.h;
        this.h = i2 + 1;
        iArr[i2] = i;
    }

    void a(int i, int i2, ClassWriter classWriter, Item item) {
        int a;
        int a2;
        int a3;
        String str;
        switch (i) {
            case LocalAudioAll.SORT_BY_TITLE:
            case Opcodes.INEG:
            case Opcodes.LNEG:
            case Opcodes.FNEG:
            case Opcodes.DNEG:
            case Opcodes.I2B:
            case Opcodes.I2C:
            case Opcodes.I2S:
            case Opcodes.GOTO:
            case Opcodes.RETURN:
                break;
            case LocalAudioAll.SORT_BY_DATE:
                b(16777221);
            case ClassWriter.COMPUTE_FRAMES:
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
            case JsonWriteContext.STATUS_EXPECT_VALUE:
            case JsonWriteContext.STATUS_EXPECT_NAME:
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
            case Type.LONG:
            case Type.DOUBLE:
            case Segment.TOKENS_PER_SEGMENT:
            case Opcodes.SIPUSH:
            case Opcodes.ILOAD:
                b(16777217);
            case Type.ARRAY:
            case Type.OBJECT:
            case Opcodes.LLOAD:
                b(16777220);
                b(16777216);
            case Opcodes.T_LONG:
            case Opcodes.FCONST_1:
            case Opcodes.FCONST_2:
            case Opcodes.FLOAD:
                b(16777218);
            case Opcodes.DCONST_0:
            case Opcodes.DCONST_1:
            case Opcodes.DLOAD:
                b(16777219);
                b(16777216);
            case Opcodes.LDC:
                switch (item.b) {
                    case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                        b(16777217);
                    case JsonWriteContext.STATUS_EXPECT_VALUE:
                        b(16777218);
                    case JsonWriteContext.STATUS_EXPECT_NAME:
                        b(16777220);
                        b(16777216);
                    case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                        b(16777219);
                        b(16777216);
                    case Type.LONG:
                        b(24117248 | classWriter.c("java/lang/Class"));
                    default:
                        b(24117248 | classWriter.c("java/lang/String"));
                }
            case Opcodes.ALOAD:
                b(a(i2));
            case Opcodes.V1_2:
            case Opcodes.V1_7:
            case Opcodes.CALOAD:
            case Opcodes.SALOAD:
                c(ClassWriter.COMPUTE_FRAMES);
                b(16777217);
            case Opcodes.V1_3:
            case Opcodes.D2L:
                c(ClassWriter.COMPUTE_FRAMES);
                b(16777220);
                b(16777216);
            case SmileConstants.TOKEN_PREFIX_KEY_SHARED_LONG:
                c(ClassWriter.COMPUTE_FRAMES);
                b(16777218);
            case Opcodes.V1_5:
            case Opcodes.L2D:
                c(ClassWriter.COMPUTE_FRAMES);
                b(16777219);
                b(16777216);
            case Opcodes.V1_6:
                c(1);
                b(a() - 268435456);
            case Opcodes.ISTORE:
            case SmileConstants.MAX_SHORT_NAME_UNICODE_BYTES:
            case Opcodes.ASTORE:
                a(i2, a());
                if (i2 > 0) {
                    a = a(i2 - 1);
                    if (a == 16777220 || a == 16777219) {
                        a(i2 - 1, 16777216);
                    } else if ((251658240 & a) != 16777216) {
                        a(i2 - 1, a | 8388608);
                    }
                }
            case Opcodes.LSTORE:
            case Opcodes.DSTORE:
                c(1);
                a(i2, a());
                a(i2 + 1, 16777216);
                if (i2 > 0) {
                    a = a(i2 - 1);
                    if (a == 16777220 || a == 16777219) {
                        a(i2 - 1, 16777216);
                    } else if ((251658240 & a) != 16777216) {
                        a(i2 - 1, a | 8388608);
                    }
                }
            case Opcodes.IASTORE:
            case Opcodes.FASTORE:
            case Opcodes.AASTORE:
            case Opcodes.BASTORE:
            case Opcodes.CASTORE:
            case Opcodes.SASTORE:
                c(JsonWriteContext.STATUS_OK_AFTER_SPACE);
            case Opcodes.LASTORE:
            case Opcodes.DASTORE:
                c(JsonWriteContext.STATUS_EXPECT_VALUE);
            case Opcodes.POP:
            case Opcodes.IFEQ:
            case Opcodes.IFNE:
            case Opcodes.IFLT:
            case Opcodes.IFGE:
            case Opcodes.IFGT:
            case Opcodes.IFLE:
            case Opcodes.TABLESWITCH:
            case Opcodes.LOOKUPSWITCH:
            case Opcodes.IRETURN:
            case Opcodes.FRETURN:
            case Opcodes.ARETURN:
            case Opcodes.ATHROW:
            case Opcodes.MONITORENTER:
            case Opcodes.MONITOREXIT:
            case Opcodes.IFNULL:
            case Opcodes.IFNONNULL:
                c(1);
            case Opcodes.POP2:
            case Opcodes.IF_ICMPEQ:
            case SmileConstants.TOKEN_PREFIX_SHORT_UNICODE:
            case Opcodes.IF_ICMPLT:
            case Opcodes.IF_ICMPGE:
            case Opcodes.IF_ICMPGT:
            case Opcodes.IF_ICMPLE:
            case Opcodes.IF_ACMPEQ:
            case Opcodes.IF_ACMPNE:
            case Opcodes.LRETURN:
            case Opcodes.DRETURN:
                c(ClassWriter.COMPUTE_FRAMES);
            case Opcodes.DUP:
                a = a();
                b(a);
                b(a);
            case Opcodes.DUP_X1:
                a = a();
                a2 = a();
                b(a);
                b(a2);
                b(a);
            case Opcodes.DUP_X2:
                a = a();
                a2 = a();
                a3 = a();
                b(a);
                b(a3);
                b(a2);
                b(a);
            case Opcodes.DUP2:
                a = a();
                a2 = a();
                b(a2);
                b(a);
                b(a2);
                b(a);
            case Opcodes.DUP2_X1:
                a = a();
                a2 = a();
                a3 = a();
                b(a2);
                b(a);
                b(a3);
                b(a2);
                b(a);
            case Opcodes.DUP2_X2:
                a = a();
                a2 = a();
                a3 = a();
                int a4 = a();
                b(a2);
                b(a);
                b(a4);
                b(a3);
                b(a2);
                b(a);
            case Opcodes.SWAP:
                a = a();
                a2 = a();
                b(a);
                b(a2);
            case SmileConstants.TOKEN_PREFIX_SMALL_ASCII:
            case Opcodes.ISUB:
            case Opcodes.IMUL:
            case Opcodes.IDIV:
            case Opcodes.IREM:
            case Opcodes.ISHL:
            case Opcodes.ISHR:
            case Opcodes.IUSHR:
            case Opcodes.IAND:
            case SmileConstants.TOKEN_PREFIX_TINY_UNICODE:
            case Opcodes.IXOR:
            case Opcodes.L2I:
            case Opcodes.D2I:
            case Opcodes.FCMPL:
            case Opcodes.FCMPG:
                c(ClassWriter.COMPUTE_FRAMES);
                b(16777217);
            case Opcodes.LADD:
            case Opcodes.LSUB:
            case Opcodes.LMUL:
            case Opcodes.LDIV:
            case Opcodes.LREM:
            case Opcodes.LAND:
            case Opcodes.LOR:
            case Opcodes.LXOR:
                c(JsonWriteContext.STATUS_EXPECT_VALUE);
                b(16777220);
                b(16777216);
            case Opcodes.FADD:
            case Opcodes.FSUB:
            case Opcodes.FMUL:
            case Opcodes.FDIV:
            case Opcodes.FREM:
            case Opcodes.L2F:
            case Opcodes.D2F:
                c(ClassWriter.COMPUTE_FRAMES);
                b(16777218);
            case Opcodes.DADD:
            case Opcodes.DSUB:
            case Opcodes.DMUL:
            case Opcodes.DDIV:
            case Opcodes.DREM:
                c(JsonWriteContext.STATUS_EXPECT_VALUE);
                b(16777219);
                b(16777216);
            case Opcodes.LSHL:
            case Opcodes.LSHR:
            case Opcodes.LUSHR:
                c(JsonWriteContext.STATUS_OK_AFTER_SPACE);
                b(16777220);
                b(16777216);
            case Opcodes.IINC:
                a(i2, 16777217);
            case Opcodes.I2L:
            case Opcodes.F2L:
                c(1);
                b(16777220);
                b(16777216);
            case Opcodes.I2F:
                c(1);
                b(16777218);
            case Opcodes.I2D:
            case Opcodes.F2D:
                c(1);
                b(16777219);
                b(16777216);
            case Opcodes.F2I:
            case Opcodes.ARRAYLENGTH:
            case Opcodes.INSTANCEOF:
                c(1);
                b(16777217);
            case Opcodes.LCMP:
            case Opcodes.DCMPL:
            case Opcodes.DCMPG:
                c(JsonWriteContext.STATUS_EXPECT_VALUE);
                b(16777217);
            case Opcodes.JSR:
            case Opcodes.RET:
                throw new RuntimeException("JSR/RET are not supported with computeFrames option");
            case Opcodes.GETSTATIC:
                a(classWriter, item.i);
            case Opcodes.PUTSTATIC:
                a(item.i);
            case Opcodes.GETFIELD:
                c(1);
                a(classWriter, item.i);
            case Opcodes.PUTFIELD:
                a(item.i);
                a();
            case Opcodes.INVOKEVIRTUAL:
            case Opcodes.INVOKESPECIAL:
            case Opcodes.INVOKESTATIC:
            case Opcodes.INVOKEINTERFACE:
                a(item.i);
                if (i != 184) {
                    a = a();
                    if (i == 183 && item.h.charAt(0) == '<') {
                        d(a);
                    }
                }
                a(classWriter, item.i);
            case Opcodes.INVOKEDYNAMIC:
                a(item.h);
                a(classWriter, item.h);
            case Opcodes.NEW:
                b(25165824 | classWriter.a(item.g, i2));
            case Opcodes.NEWARRAY:
                a();
                switch (i2) {
                    case JsonWriteContext.STATUS_EXPECT_VALUE:
                        b(285212681);
                    case JsonWriteContext.STATUS_EXPECT_NAME:
                        b(285212683);
                    case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                        b(285212674);
                    case Type.LONG:
                        b(285212675);
                    case Type.DOUBLE:
                        b(285212682);
                    case Type.ARRAY:
                        b(285212684);
                    case Type.OBJECT:
                        b(285212673);
                    default:
                        b(285212676);
                }
            case Opcodes.ANEWARRAY:
                str = item.g;
                a();
                if (str.charAt(0) == '[') {
                    a(classWriter, new StringBuffer().append('[').append(str).toString());
                } else {
                    b(classWriter.c(str) | 292552704);
                }
            case SmileConstants.TOKEN_PREFIX_SMALL_INT:
                str = item.g;
                a();
                if (str.charAt(0) == '[') {
                    a(classWriter, str);
                } else {
                    b(classWriter.c(str) | 24117248);
                }
            default:
                c(i2);
                a(classWriter, item.g);
        }
    }

    void a(ClassWriter classWriter, int i, Type[] typeArr, int i2) {
        int i3 = 1;
        int i4 = 0;
        this.c = new int[i2];
        this.d = new int[0];
        if ((i & 8) != 0) {
            i3 = 0;
        } else if ((262144 & i) == 0) {
            this.c[0] = 24117248 | classWriter.c(classWriter.F);
        } else {
            this.c[0] = 16777222;
        }
        while (i4 < typeArr.length) {
            int b = b(classWriter, typeArr[i4].getDescriptor());
            int i5 = i3 + 1;
            this.c[i3] = b;
            if (b == 16777220 || b == 16777219) {
                i3 = i5 + 1;
                this.c[i5] = 16777216;
            } else {
                i3 = i5;
            }
            i4++;
        }
        while (i3 < i2) {
            i4 = i3 + 1;
            this.c[i3] = 16777216;
            i3 = i4;
        }
    }

    boolean a(ClassWriter classWriter, Frame frame, int i) {
        int i2;
        int i3;
        boolean z = 0;
        int length = this.c.length;
        int length2 = this.d.length;
        if (frame.c == null) {
            frame.c = new int[length];
            z = 1;
        }
        int i4 = 0;
        int i5 = i2;
        while (i4 < length) {
            int i6;
            if (this.e == null || i4 >= this.e.length) {
                i2 = this.c[i4];
            } else {
                i2 = this.e[i4];
                if (i2 == 0) {
                    i2 = this.c[i4];
                } else {
                    i6 = -268435456 & i2;
                    i3 = 251658240 & i2;
                    if (i3 != 16777216) {
                        i6 = i3 == 33554432 ? i6 + this.c[8388607 & i2] : i6 + this.d[length2 - 8388607 & i2];
                        i2 = ((i2 & 8388608) == 0 || !(i6 == 16777220 || i6 == 16777219)) ? i6 : 16777216;
                    }
                }
            }
            if (this.i != null) {
                i2 = a(classWriter, i2);
            }
            i5 |= a(classWriter, i2, frame.c, i4);
            i4++;
        }
        if (i > 0) {
            i6 = 0;
            i2 = i5;
            while (i6 < length) {
                i5 = a(classWriter, this.c[i6], frame.c, i6) | i2;
                i6++;
                i2 = i5;
            }
            if (frame.d == null) {
                frame.d = new int[1];
                i2 = 1;
            }
            return a(classWriter, i, frame.d, 0) | i2;
        } else {
            length = this.d.length + this.b.f;
            if (frame.d == null) {
                frame.d = new int[(this.g + length)];
                z = 1;
            } else {
                i2 = i5;
            }
            i5 = 0;
            boolean z2 = z;
            while (i5 < length) {
                i2 = this.d[i5];
                if (this.i != null) {
                    i2 = a(classWriter, i2);
                }
                z2 |= a(classWriter, i2, frame.d, i5);
                i5++;
            }
            i2 = 0;
            while (i2 < this.g) {
                i5 = this.f[i2];
                i4 = -268435456 & i5;
                i3 = 251658240 & i5;
                if (i3 != 16777216) {
                    i4 = i3 == 33554432 ? i4 + this.c[8388607 & i5] : i4 + this.d[length2 - 8388607 & i5];
                    i5 = ((i5 & 8388608) == 0 || !(i4 == 16777220 || i4 == 16777219)) ? i4 : 16777216;
                }
                if (this.i != null) {
                    i5 = a(classWriter, i5);
                }
                z2 |= a(classWriter, i5, frame.d, length + i2);
                i2++;
            }
            return z2;
        }
    }
}