package com.google.android.gms.internal;

import android.graphics.drawable.Drawable;

public final class fa extends fu<a, Drawable> {

    public static final class a {
        public final int CR;
        public final int CS;

        public a(int i, int i2) {
            this.CR = i;
            this.CS = i2;
        }

        public boolean equals(com.google.android.gms.internal.fa.a obj) {
            if (!(obj instanceof com.google.android.gms.internal.fa.a)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            obj = obj;
            return obj.CR == this.CR && obj.CS == this.CS;
        }

        public int hashCode() {
            return fo.hashCode(new Object[]{Integer.valueOf(this.CR), Integer.valueOf(this.CS)});
        }
    }

    public fa() {
        super(10);
    }
}