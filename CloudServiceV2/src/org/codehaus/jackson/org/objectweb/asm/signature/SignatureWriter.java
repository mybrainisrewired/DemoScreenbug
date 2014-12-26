package org.codehaus.jackson.org.objectweb.asm.signature;

public class SignatureWriter implements SignatureVisitor {
    private final StringBuffer a;
    private boolean b;
    private boolean c;
    private int d;

    public SignatureWriter() {
        this.a = new StringBuffer();
    }

    private void a() {
        if (this.b) {
            this.b = false;
            this.a.append('>');
        }
    }

    private void b() {
        if (this.d % 2 != 0) {
            this.a.append('>');
        }
        this.d /= 2;
    }

    public String toString() {
        return this.a.toString();
    }

    public SignatureVisitor visitArrayType() {
        this.a.append('[');
        return this;
    }

    public void visitBaseType(char c) {
        this.a.append(c);
    }

    public SignatureVisitor visitClassBound() {
        return this;
    }

    public void visitClassType(String str) {
        this.a.append('L');
        this.a.append(str);
        this.d *= 2;
    }

    public void visitEnd() {
        b();
        this.a.append(';');
    }

    public SignatureVisitor visitExceptionType() {
        this.a.append('^');
        return this;
    }

    public void visitFormalTypeParameter(String str) {
        if (!this.b) {
            this.b = true;
            this.a.append('<');
        }
        this.a.append(str);
        this.a.append(':');
    }

    public void visitInnerClassType(String str) {
        b();
        this.a.append('.');
        this.a.append(str);
        this.d *= 2;
    }

    public SignatureVisitor visitInterface() {
        return this;
    }

    public SignatureVisitor visitInterfaceBound() {
        this.a.append(':');
        return this;
    }

    public SignatureVisitor visitParameterType() {
        a();
        if (!this.c) {
            this.c = true;
            this.a.append('(');
        }
        return this;
    }

    public SignatureVisitor visitReturnType() {
        a();
        if (!this.c) {
            this.a.append('(');
        }
        this.a.append(')');
        return this;
    }

    public SignatureVisitor visitSuperclass() {
        a();
        return this;
    }

    public SignatureVisitor visitTypeArgument(char c) {
        if (this.d % 2 == 0) {
            this.d++;
            this.a.append('<');
        }
        if (c != '=') {
            this.a.append(c);
        }
        return this;
    }

    public void visitTypeArgument() {
        if (this.d % 2 == 0) {
            this.d++;
            this.a.append('<');
        }
        this.a.append('*');
    }

    public void visitTypeVariable(String str) {
        this.a.append('T');
        this.a.append(str);
        this.a.append(';');
    }
}