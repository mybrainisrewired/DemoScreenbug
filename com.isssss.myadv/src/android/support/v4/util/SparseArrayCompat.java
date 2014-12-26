package android.support.v4.util;

public class SparseArrayCompat<E> implements Cloneable {
    private static final Object DELETED;
    private boolean mGarbage;
    private int[] mKeys;
    private int mSize;
    private Object[] mValues;

    static {
        DELETED = new Object();
    }

    public SparseArrayCompat() {
        this(10);
    }

    public SparseArrayCompat(int initialCapacity) {
        this.mGarbage = false;
        if (initialCapacity == 0) {
            this.mKeys = ContainerHelpers.EMPTY_INTS;
            this.mValues = ContainerHelpers.EMPTY_OBJECTS;
        } else {
            initialCapacity = ContainerHelpers.idealIntArraySize(initialCapacity);
            this.mKeys = new int[initialCapacity];
            this.mValues = new Object[initialCapacity];
        }
        this.mSize = 0;
    }

    private void gc() {
        int n = this.mSize;
        int o = 0;
        int[] keys = this.mKeys;
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

    public void append(int key, E value) {
        if (this.mSize == 0 || key > this.mKeys[this.mSize - 1]) {
            if (this.mGarbage && this.mSize >= this.mKeys.length) {
                gc();
            }
            int pos = this.mSize;
            if (pos >= this.mKeys.length) {
                int n = ContainerHelpers.idealIntArraySize(pos + 1);
                int[] nkeys = new int[n];
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

    public SparseArrayCompat<E> clone() {
        try {
            SparseArrayCompat<E> clone = (SparseArrayCompat) super.clone();
            clone.mKeys = (int[]) this.mKeys.clone();
            clone.mValues = (Object[]) this.mValues.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public void delete(int key) {
        int i = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        if (i >= 0 && this.mValues[i] != DELETED) {
            this.mValues[i] = DELETED;
            this.mGarbage = true;
        }
    }

    public E get(int key) {
        return get(key, null);
    }

    public E get(int key, E valueIfKeyNotFound) {
        int i = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        return (i < 0 || this.mValues[i] == DELETED) ? valueIfKeyNotFound : this.mValues[i];
    }

    public int indexOfKey(int key) {
        if (this.mGarbage) {
            gc();
        }
        return ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
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

    public int keyAt(int index) {
        if (this.mGarbage) {
            gc();
        }
        return this.mKeys[index];
    }

    public void put(int key, E value) {
        int i = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        if (i >= 0) {
            this.mValues[i] = value;
        } else {
            i ^= -1;
            if (i >= this.mSize || this.mValues[i] != DELETED) {
                if (this.mGarbage && this.mSize >= this.mKeys.length) {
                    gc();
                    i = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key) ^ -1;
                }
                if (this.mSize >= this.mKeys.length) {
                    int n = ContainerHelpers.idealIntArraySize(this.mSize + 1);
                    int[] nkeys = new int[n];
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

    public void remove(int key) {
        delete(key);
    }

    public void removeAt(int index) {
        if (this.mValues[index] != DELETED) {
            this.mValues[index] = DELETED;
            this.mGarbage = true;
        }
    }

    public void removeAtRange(int index, int size) {
        int end = Math.min(this.mSize, index + size);
        int i = index;
        while (i < end) {
            removeAt(i);
            i++;
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

    public String toString() {
        if (size() <= 0) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder(this.mSize * 28);
        buffer.append('{');
        int i = 0;
        while (i < this.mSize) {
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(keyAt(i));
            buffer.append('=');
            SparseArrayCompat<E> value = valueAt(i);
            if (value != this) {
                buffer.append(value);
            } else {
                buffer.append("(this Map)");
            }
            i++;
        }
        buffer.append('}');
        return buffer.toString();
    }

    public E valueAt(int index) {
        if (this.mGarbage) {
            gc();
        }
        return this.mValues[index];
    }
}