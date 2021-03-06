package org.codehaus.jackson.map.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public final class ArrayBuilders {
    BooleanBuilder _booleanBuilder;
    ByteBuilder _byteBuilder;
    DoubleBuilder _doubleBuilder;
    FloatBuilder _floatBuilder;
    IntBuilder _intBuilder;
    LongBuilder _longBuilder;
    ShortBuilder _shortBuilder;

    private static final class ArrayIterator<T> implements Iterator<T>, Iterable<T> {
        private final T[] _array;
        private int _index;

        public ArrayIterator(T[] array) {
            this._array = array;
            this._index = 0;
        }

        public boolean hasNext() {
            return this._index < this._array.length;
        }

        public Iterator<T> iterator() {
            return this;
        }

        public T next() {
            if (this._index >= this._array.length) {
                throw new NoSuchElementException();
            }
            Object[] objArr = this._array;
            int i = this._index;
            this._index = i + 1;
            return objArr[i];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static final class BooleanBuilder extends PrimitiveArrayBuilder<boolean[]> {
        public final boolean[] _constructArray(int len) {
            return new boolean[len];
        }
    }

    public static final class ByteBuilder extends PrimitiveArrayBuilder<byte[]> {
        public final byte[] _constructArray(int len) {
            return new byte[len];
        }
    }

    public static final class DoubleBuilder extends PrimitiveArrayBuilder<double[]> {
        public final double[] _constructArray(int len) {
            return new double[len];
        }
    }

    public static final class FloatBuilder extends PrimitiveArrayBuilder<float[]> {
        public final float[] _constructArray(int len) {
            return new float[len];
        }
    }

    public static final class IntBuilder extends PrimitiveArrayBuilder<int[]> {
        public final int[] _constructArray(int len) {
            return new int[len];
        }
    }

    public static final class LongBuilder extends PrimitiveArrayBuilder<long[]> {
        public final long[] _constructArray(int len) {
            return new long[len];
        }
    }

    public static final class ShortBuilder extends PrimitiveArrayBuilder<short[]> {
        public final short[] _constructArray(int len) {
            return new short[len];
        }
    }

    public ArrayBuilders() {
        this._booleanBuilder = null;
        this._byteBuilder = null;
        this._shortBuilder = null;
        this._intBuilder = null;
        this._longBuilder = null;
        this._floatBuilder = null;
        this._doubleBuilder = null;
    }

    public static <T> List<T> addToList(List<T> list, T element) {
        if (list == null) {
            list = new ArrayList();
        }
        list.add(element);
        return list;
    }

    public static <T> Iterable<T> arrayAsIterable(T[] array) {
        return new ArrayIterator(array);
    }

    public static <T> Iterator<T> arrayAsIterator(T[] array) {
        return new ArrayIterator(array);
    }

    public static <T> HashSet<T> arrayToSet(T[] elements) {
        HashSet<T> result = new HashSet();
        if (elements != null) {
            T[] arr$ = elements;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                result.add(arr$[i$]);
                i$++;
            }
        }
        return result;
    }

    public static <T> T[] insertInList(T[] array, T element) {
        int len = array.length;
        Object[] result = (Object[]) Array.newInstance(array.getClass().getComponentType(), len + 1);
        if (len > 0) {
            System.arraycopy(array, 0, result, 1, len);
        }
        result[0] = element;
        return result;
    }

    public static <T> T[] insertInListNoDup(T[] array, T element) {
        Object[] result;
        int len = array.length;
        int ix = 0;
        while (ix < len) {
            if (array[ix] != element) {
                ix++;
            } else if (ix == 0) {
                return array;
            } else {
                result = (Object[]) Array.newInstance(array.getClass().getComponentType(), len);
                System.arraycopy(array, 0, result, 1, ix);
                array[0] = element;
                return result;
            }
        }
        result = (Object[]) ((Object[]) Array.newInstance(array.getClass().getComponentType(), len + 1));
        if (len > 0) {
            System.arraycopy(array, 0, result, 1, len);
        }
        result[0] = element;
        return result;
    }

    public BooleanBuilder getBooleanBuilder() {
        if (this._booleanBuilder == null) {
            this._booleanBuilder = new BooleanBuilder();
        }
        return this._booleanBuilder;
    }

    public ByteBuilder getByteBuilder() {
        if (this._byteBuilder == null) {
            this._byteBuilder = new ByteBuilder();
        }
        return this._byteBuilder;
    }

    public DoubleBuilder getDoubleBuilder() {
        if (this._doubleBuilder == null) {
            this._doubleBuilder = new DoubleBuilder();
        }
        return this._doubleBuilder;
    }

    public FloatBuilder getFloatBuilder() {
        if (this._floatBuilder == null) {
            this._floatBuilder = new FloatBuilder();
        }
        return this._floatBuilder;
    }

    public IntBuilder getIntBuilder() {
        if (this._intBuilder == null) {
            this._intBuilder = new IntBuilder();
        }
        return this._intBuilder;
    }

    public LongBuilder getLongBuilder() {
        if (this._longBuilder == null) {
            this._longBuilder = new LongBuilder();
        }
        return this._longBuilder;
    }

    public ShortBuilder getShortBuilder() {
        if (this._shortBuilder == null) {
            this._shortBuilder = new ShortBuilder();
        }
        return this._shortBuilder;
    }
}