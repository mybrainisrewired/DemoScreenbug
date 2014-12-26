package org.codehaus.jackson.sym;

public abstract class Name {
    protected final int _hashCode;
    protected final String _name;

    protected Name(String name, int hashCode) {
        this._name = name;
        this._hashCode = hashCode;
    }

    public abstract boolean equals(int i);

    public abstract boolean equals(int i, int i2);

    public boolean equals(Name o) {
        return o == this;
    }

    public abstract boolean equals(int[] iArr, int i);

    public String getName() {
        return this._name;
    }

    public final int hashCode() {
        return this._hashCode;
    }

    public String toString() {
        return this._name;
    }
}