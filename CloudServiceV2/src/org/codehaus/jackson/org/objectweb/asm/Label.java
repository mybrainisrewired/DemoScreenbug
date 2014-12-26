package org.codehaus.jackson.org.objectweb.asm;

public class Label {
    int a;
    int b;
    int c;
    private int d;
    private int[] e;
    int f;
    int g;
    Frame h;
    Label i;
    public Object info;
    Edge j;
    Label k;

    private void a(int i, int i2) {
        if (this.e == null) {
            this.e = new int[6];
        }
        if (this.d >= this.e.length) {
            Object obj = new Object[(this.e.length + 6)];
            System.arraycopy(this.e, 0, obj, 0, this.e.length);
            this.e = obj;
        }
        int[] iArr = this.e;
        int i3 = this.d;
        this.d = i3 + 1;
        iArr[i3] = i;
        iArr = this.e;
        i3 = this.d;
        this.d = i3 + 1;
        iArr[i3] = i2;
    }

    Label a() {
        return this.h == null ? this : this.h.b;
    }

    void a(long j, int i) {
        if ((this.a & 1024) == 0) {
            this.a |= 1024;
            this.e = new int[((i - 1) / 32 + 1)];
        }
        int[] iArr = this.e;
        int i2 = (int) (j >>> 32);
        iArr[i2] = iArr[i2] | ((int) j);
    }

    void a(MethodWriter methodWriter, ByteVector byteVector, int i, boolean z) {
        if ((this.a & 2) == 0) {
            if (z) {
                a(-1 - i, byteVector.b);
                byteVector.putInt(-1);
            } else {
                a(i, byteVector.b);
                byteVector.putShort(-1);
            }
        } else if (z) {
            byteVector.putInt(this.c - i);
        } else {
            byteVector.putShort(this.c - i);
        }
    }

    boolean a(long j) {
        return ((this.a & 1024) == 0 || (this.e[(int) (j >>> 32)] & ((int) j)) == 0) ? false : true;
    }

    boolean a(Label label) {
        if ((this.a & 1024) == 0 || (label.a & 1024) == 0) {
            return false;
        }
        int i = 0;
        while (i < this.e.length) {
            if ((this.e[i] & label.e[i]) != 0) {
                return true;
            }
            i++;
        }
        return false;
    }

    boolean a(MethodWriter methodWriter, int i, byte[] bArr) {
        int i2 = 0;
        this.a |= 2;
        this.c = i;
        boolean z = false;
        while (i2 < this.d) {
            int i3 = i2 + 1;
            int i4 = this.e[i2];
            i2 = i3 + 1;
            i3 = this.e[i3];
            int i5;
            if (i4 >= 0) {
                i4 = i - i4;
                if (i4 < -32768 || i4 > 32767) {
                    int i6 = bArr[i3 - 1] & 255;
                    if (i6 <= 168) {
                        bArr[i3 - 1] = (byte) (i6 + 49);
                    } else {
                        bArr[i3 - 1] = (byte) (i6 + 20);
                    }
                    z = true;
                }
                i5 = i3 + 1;
                bArr[i3] = (byte) (i4 >>> 8);
                bArr[i5] = (byte) i4;
            } else {
                i4 = i4 + i + 1;
                i5 = i3 + 1;
                bArr[i3] = (byte) (i4 >>> 24);
                i3 = i5 + 1;
                bArr[i5] = (byte) (i4 >>> 16);
                i5 = i3 + 1;
                bArr[i3] = (byte) (i4 >>> 8);
                bArr[i5] = (byte) i4;
            }
        }
        return z;
    }

    void b(Label label, long j, int i) {
        while (i != 0) {
            Label label2 = i.k;
            i.k = null;
            if (label != null) {
                if ((i.a & 2048) != 0) {
                    this = label2;
                } else {
                    i.a |= 2048;
                    if (!((i.a & 256) == 0 || i.a(label))) {
                        Edge edge = new Edge();
                        edge.a = i.f;
                        edge.b = label.j.b;
                        edge.c = i.j;
                        i.j = edge;
                    }
                }
            } else if (i.a(j)) {
                this = label2;
            } else {
                i.a(j, i);
            }
            Label label3 = label2;
            Edge edge2 = i.j;
            while (edge2 != null) {
                if (((i.a & 128) == 0 || edge2 != i.j.c) && edge2.b.k == null) {
                    edge2.b.k = label3;
                    label3 = edge2.b;
                }
                edge2 = edge2.c;
            }
            this = label3;
        }
    }

    public int getOffset() {
        if ((this.a & 2) != 0) {
            return this.c;
        }
        throw new IllegalStateException("Label offset position has not been resolved yet");
    }

    public String toString() {
        return new StringBuffer().append("L").append(System.identityHashCode(this)).toString();
    }
}