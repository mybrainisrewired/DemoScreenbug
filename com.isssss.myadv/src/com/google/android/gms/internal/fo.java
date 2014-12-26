package com.google.android.gms.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class fo {

    public static final class a {
        private final List<String> DI;
        private final Object DJ;

        private a(Object obj) {
            this.DJ = fq.f(obj);
            this.DI = new ArrayList();
        }

        public com.google.android.gms.internal.fo.a a(String str, Object obj) {
            this.DI.add(((String) fq.f(str)) + "=" + String.valueOf(obj));
            return this;
        }

        public String toString() {
            StringBuilder append = new StringBuilder(100).append(this.DJ.getClass().getSimpleName()).append('{');
            int size = this.DI.size();
            int i = 0;
            while (i < size) {
                append.append((String) this.DI.get(i));
                if (i < size - 1) {
                    append.append(", ");
                }
                i++;
            }
            return append.append('}').toString();
        }
    }

    public static a e(Object obj) {
        return new a(null);
    }

    public static boolean equal(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    public static int hashCode(Object... objects) {
        return Arrays.hashCode(objects);
    }
}