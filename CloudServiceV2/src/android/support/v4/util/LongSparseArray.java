package android.support.v4.util;

import org.codehaus.jackson.impl.JsonWriteContext;

public class LongSparseArray<E> implements Cloneable {
    private static final Object DELETED;
    private boolean mGarbage;
    private long[] mKeys;
    private int mSize;
    private Object[] mValues;

    static {
        DELETED = new Object();
    }

    public LongSparseArray() {
        this(10);
    }

    public LongSparseArray(int initialCapacity) {
        this.mGarbage = false;
        initialCapacity = idealLongArraySize(initialCapacity);
        this.mKeys = new long[initialCapacity];
        this.mValues = new Object[initialCapacity];
        this.mSize = 0;
    }

    private static int binarySearch(long[] a, int start, int len, long key) {
        int high = start + len;
        int low = start - 1;
        while (high - low > 1) {
            int guess = (high + low) / 2;
            if (a[guess] < key) {
                low = guess;
            } else {
                high = guess;
            }
        }
        if (high == start + len) {
            return (start + len) ^ -1;
        }
        return a[high] != key ? high ^ -1 : high;
    }

    private void gc() {
        int n = this.mSize;
        int o = 0;
        long[] keys = this.mKeys;
        Object[] values = this.mValues;
        int i = 0;
        while (i < n) {
            Object val = values[i];
            if (val != DELETED) {
                if (i != o) {
                    keys[o] = keys[i];
                    values[o] = val;
                    values[i] = null;
                }
                o++;
            }
            i++;
        }
        this.mGarbage = false;
        this.mSize = o;
    }

    public static int idealByteArraySize(int need) {
        int i = JsonWriteContext.STATUS_EXPECT_VALUE;
        while (i < 32) {
            if (need <= 1 << i - 12) {
                return 1 << i - 12;
            }
            i++;
        }
        return need;
    }

    public static int idealLongArraySize(int need) {
        return idealByteArraySize(need * 8) / 8;
    }

    public void append(long key, E value) {
        if (this.mSize == 0 || key > this.mKeys[this.mSize - 1]) {
            if (this.mGarbage && this.mSize >= this.mKeys.length) {
                gc();
            }
            int pos = this.mSize;
            if (pos >= this.mKeys.length) {
                int n = idealLongArraySize(pos + 1);
                long[] nkeys = new long[n];
                Object[] nvalues = new Object[n];
                System.arraycopy(this.mKeys, 0, nkeys, 0, this.mKeys.length);
                System.arraycopy(this.mValues, 0, nvalues, 0, this.mValues.length);
                this.mKeys = nkeys;
                this.mValues = nvalues;
            }
            this.mKeys[pos] = key;
            this.mValues[pos] = value;
            this.mSize = pos + 1;
        } else {
            put(key, value);
        }
    }

    public void clear() {
        int n = this.mSize;
        Object[] values = this.mValues;
        int i = 0;
        while (i < n) {
            values[i] = null;
            i++;
        }
        this.mSize = 0;
        this.mGarbage = false;
    }

    public LongSparseArray<E> clone() {
        try {
            LongSparseArray<E> clone = (LongSparseArray) super.clone();
            clone.mKeys = (long[]) this.mKeys.clone();
            clone.mValues = (Object[]) this.mValues.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public void delete(long key) {
        int i = binarySearch(this.mKeys, 0, this.mSize, key);
        if (i >= 0 && this.mValues[i] != DELETED) {
            this.mValues[i] = DELETED;
            this.mGarbage = true;
        }
    }

    public E get(long key) {
        return get(key, null);
    }

    public E get(long key, E valueIfKeyNotFound) {
        int i = binarySearch(this.mKeys, 0, this.mSize, key);
        return (i < 0 || this.mValues[i] == DELETED) ? valueIfKeyNotFound : this.mValues[i];
    }

    public int indexOfKey(long key) {
        if (this.mGarbage) {
            gc();
        }
        return binarySearch(this.mKeys, 0, this.mSize, key);
    }

    public int indexOfValue(E value) {
        if (this.mGarbage) {
            gc();
        }
        int i = 0;
        while (i < this.mSize) {
            if (this.mValues[i] == value) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public long keyAt(int index) {
        if (this.mGarbage) {
            gc();
        }
        return this.mKeys[index];
    }

    public void put(long key, E value) {
        int i = binarySearch(this.mKeys, 0, this.mSize, key);
        if (i >= 0) {
            this.mValues[i] = value;
        } else {
            i ^= -1;
            if (i >= this.mSize || this.mValues[i] != DELETED) {
                if (this.mGarbage && this.mSize >= this.mKeys.length) {
                    gc();
                    i = binarySearch(this.mKeys, 0, this.mSize, key) ^ -1;
                }
                if (this.mSize >= this.mKeys.length) {
                    int n = idealLongArraySize(this.mSize + 1);
                    long[] nkeys = new long[n];
                    Object[] nvalues = new Object[n];
                    System.arraycopy(this.mKeys, 0, nkeys, 0, this.mKeys.length);
                    System.arraycopy(this.mValues, 0, nvalues, 0, this.mValues.length);
                    this.mKeys = nkeys;
                    this.mValues = nvalues;
                }
                if (this.mSize - i != 0) {
                    System.arraycopy(this.mKeys, i, this.mKeys, i + 1, this.mSize - i);
                    System.arraycopy(this.mValues, i, this.mValues, i + 1, this.mSize - i);
                }
                this.mKeys[i] = key;
                this.mValues[i] = value;
                this.mSize++;
            } else {
                this.mKeys[i] = key;
                this.mValues[i] = value;
            }
        }
    }

    public void remove(long key) {
        delete(key);
    }

    public void removeAt(int index) {
        if (this.mValues[index] != DELETED) {
            this.mValues[index] = DELETED;
            this.mGarbage = true;
        }
    }

    public void setValueAt(int index, E value) {
        if (this.mGarbage) {
            gc();
        }
        this.mValues[index] = value;
    }

    public int size() {
        if (this.mGarbage) {
            gc();
        }
        return this.mSize;
    }

    public E valueAt(int index) {
        if (this.mGarbage) {
            gc();
        }
        return this.mValues[index];
    }
}