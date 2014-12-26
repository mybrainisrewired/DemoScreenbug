package org.codehaus.jackson.org.objectweb.asm.signature;

import com.wmt.data.DataManager;
import com.wmt.remotectrl.ConnectionInstance;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

public class SignatureReader {
    private final String a;

    public SignatureReader(String str) {
        this.a = str;
    }

    private static int a(String str, int i, SignatureVisitor signatureVisitor) {
        int i2 = i + 1;
        char charAt = str.charAt(i);
        switch (charAt) {
            case DataManager.INCLUDE_LOCAL_VIDEO_ONLY:
            case DataManager.INCLUDE_LOCAL_ALL_ONLY:
            case DataManager.INCLUDE_LOCAL_AUDIO_ONLY:
            case 'F':
            case 'I':
            case 'J':
            case Opcodes.AASTORE:
            case Opcodes.SASTORE:
            case Opcodes.DUP_X1:
                signatureVisitor.visitBaseType(charAt);
                return i2;
            case Opcodes.BASTORE:
                int indexOf = str.indexOf(59, i2);
                signatureVisitor.visitTypeVariable(str.substring(i2, indexOf));
                return indexOf + 1;
            case Opcodes.DUP_X2:
                return a(str, i2, signatureVisitor.visitArrayType());
            default:
                boolean z = false;
                int i3 = i2;
                int i4 = i2;
                boolean z2 = false;
                while (true) {
                    int i5 = i4 + 1;
                    char charAt2 = str.charAt(i4);
                    int i6;
                    String substring;
                    switch (charAt2) {
                        case Opcodes.V1_2:
                        case ';':
                            if (i6 == 0) {
                                substring = str.substring(i3, i5 - 1);
                                if (i2 != 0) {
                                    signatureVisitor.visitInnerClassType(substring);
                                } else {
                                    signatureVisitor.visitClassType(substring);
                                }
                            }
                            if (charAt2 == ';') {
                                signatureVisitor.visitEnd();
                                return i5;
                            } else {
                                z2 = true;
                                z = false;
                                i3 = i5;
                                i4 = i5;
                            }
                            break;
                        case '<':
                            substring = str.substring(i3, i5 - 1);
                            if (i2 != 0) {
                                signatureVisitor.visitInnerClassType(substring);
                            } else {
                                signatureVisitor.visitClassType(substring);
                            }
                            i6 = i5;
                            while (true) {
                                charAt2 = str.charAt(i6);
                                switch (charAt2) {
                                    case ConnectionInstance.SERVER_RESOLUTION_UPDATE:
                                        i6++;
                                        signatureVisitor.visitTypeArgument();
                                        break;
                                    case ConnectionInstance.SERVER_SCREEN_IMAGE_UPDATE:
                                    case ConnectionInstance.CONNECTION_FAILED:
                                        i6 = a(str, i6 + 1, signatureVisitor.visitTypeArgument(charAt2));
                                        break;
                                    case '>':
                                        i4 = i6;
                                        z = true;
                                        break;
                                    default:
                                        i6 = a(str, i6, signatureVisitor.visitTypeArgument(SignatureVisitor.INSTANCEOF));
                                        break;
                                }
                            }
                            break;
                        default:
                            i4 = i5;
                            break;
                    }
                }
                break;
        }
    }

    public void accept(SignatureVisitor signatureVisitor) {
        int i = 0;
        String str = this.a;
        int length = str.length();
        if (str.charAt(0) == '<') {
            i = ClassWriter.COMPUTE_FRAMES;
            char charAt;
            do {
                int indexOf = str.indexOf(Opcodes.ASTORE, i);
                signatureVisitor.visitFormalTypeParameter(str.substring(i - 1, indexOf));
                i = indexOf + 1;
                charAt = str.charAt(i);
                indexOf = (charAt == 'L' || charAt == '[' || charAt == 'T') ? a(str, i, signatureVisitor.visitClassBound()) : i;
                while (true) {
                    i = indexOf + 1;
                    charAt = str.charAt(indexOf);
                    if (charAt != ':') {
                        break;
                    }
                    indexOf = a(str, i, signatureVisitor.visitInterfaceBound());
                }
            } while (charAt != '>');
        }
        if (str.charAt(i) == '(') {
            i++;
            while (str.charAt(i) != ')') {
                i = a(str, i, signatureVisitor.visitParameterType());
            }
            i = a(str, i + 1, signatureVisitor.visitReturnType());
            while (i < length) {
                i = a(str, i + 1, signatureVisitor.visitExceptionType());
            }
        } else {
            i = a(str, i, signatureVisitor.visitSuperclass());
            while (i < length) {
                i = a(str, i, signatureVisitor.visitInterface());
            }
        }
    }

    public void acceptType(SignatureVisitor signatureVisitor) {
        a(this.a, 0, signatureVisitor);
    }
}