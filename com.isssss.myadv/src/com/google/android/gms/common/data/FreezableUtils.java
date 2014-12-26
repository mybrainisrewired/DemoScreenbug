package com.google.android.gms.common.data;

import java.util.ArrayList;
import java.util.Iterator;

public final class FreezableUtils {
    public static <T, E extends Freezable<T>> ArrayList<T> freeze(ArrayList<E> list) {
        ArrayList<T> arrayList = new ArrayList(list.size());
        int size = list.size();
        int i = 0;
        while (i < size) {
            arrayList.add(((Freezable) list.get(i)).freeze());
            i++;
        }
        return arrayList;
    }

    public static <T, E extends Freezable<T>> ArrayList<T> freeze(E[] array) {
        ArrayList<T> arrayList = new ArrayList(array.length);
        int i = 0;
        while (i < array.length) {
            arrayList.add(array[i].freeze());
            i++;
        }
        return arrayList;
    }

    public static <T, E extends Freezable<T>> ArrayList<T> freezeIterable(Iterable<E> iterable) {
        ArrayList<T> arrayList = new ArrayList();
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            arrayList.add(((Freezable) it.next()).freeze());
        }
        return arrayList;
    }
}