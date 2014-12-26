package com.google.android.gms.internal;

import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class kq<M extends kp<M>, T> {
    protected final Class<T> adV;
    protected final boolean adW;
    protected final int tag;
    protected final int type;

    private kq(int i, Class<T> cls, int i2, boolean z) {
        this.type = i;
        this.adV = cls;
        this.tag = i2;
        this.adW = z;
    }

    public static <M extends kp<M>, T extends kt> kq<M, T> a(int i, Class<T> cls, int i2) {
        return new kq(i, cls, i2, false);
    }

    protected void a(kv kvVar, List<Object> list) {
        list.add(o(kn.n(kvVar.adZ)));
    }

    protected boolean dd(int i) {
        return i == this.tag;
    }

    final T f(List<kv> list) {
        int i = 0;
        if (list == null) {
            return null;
        }
        kv kvVar;
        if (this.adW) {
            List arrayList = new ArrayList();
            int i2 = 0;
            while (i2 < list.size()) {
                kvVar = (kv) list.get(i2);
                if (dd(kvVar.tag) && kvVar.adZ.length != 0) {
                    a(kvVar, arrayList);
                }
                i2++;
            }
            i2 = arrayList.size();
            if (i2 == 0) {
                return null;
            }
            T cast = this.adV.cast(Array.newInstance(this.adV.getComponentType(), i2));
            while (i < i2) {
                Array.set(cast, i, arrayList.get(i));
                i++;
            }
            return cast;
        } else {
            i = list.size() - 1;
            kv kvVar2 = null;
            while (kvVar2 == null && i >= 0) {
                kvVar = (kv) list.get(i);
                if (!dd(kvVar.tag) || kvVar.adZ.length == 0) {
                    kvVar = kvVar2;
                }
                i--;
                kvVar2 = kvVar;
            }
            return kvVar2 == null ? null : this.adV.cast(o(kn.n(kvVar2.adZ)));
        }
    }

    protected Object o(kn knVar) {
        Class componentType = this.adW ? this.adV.getComponentType() : this.adV;
        try {
            kt ktVar;
            switch (this.type) {
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    ktVar = (kt) componentType.newInstance();
                    knVar.a(ktVar, kw.df(this.tag));
                    return ktVar;
                case ApiEventType.API_MRAID_EXPAND:
                    ktVar = (kt) componentType.newInstance();
                    knVar.a(ktVar);
                    return ktVar;
                default:
                    throw new IllegalArgumentException("Unknown type " + this.type);
            }
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Error creating instance of class " + componentType, e);
        } catch (IllegalAccessException e2) {
            throw new IllegalArgumentException("Error creating instance of class " + componentType, e2);
        } catch (IOException e3) {
            throw new IllegalArgumentException("Error reading extension field", e3);
        }
    }
}