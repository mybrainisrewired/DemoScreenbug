package android.support.v4.util;

import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.util.Map;

public class SimpleArrayMap<K, V> {
    private static final int BASE_SIZE = 4;
    private static final int CACHE_SIZE = 10;
    private static final boolean DEBUG = false;
    private static final String TAG = "ArrayMap";
    static Object[] mBaseCache;
    static int mBaseCacheSize;
    static Object[] mTwiceBaseCache;
    static int mTwiceBaseCacheSize;
    Object[] mArray;
    int[] mHashes;
    int mSize;

    public SimpleArrayMap() {
        this.mHashes = ContainerHelpers.EMPTY_INTS;
        this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        this.mSize = 0;
    }

    public SimpleArrayMap(int capacity) {
        if (capacity == 0) {
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        } else {
            allocArrays(capacity);
        }
        this.mSize = 0;
    }

    public SimpleArrayMap(SimpleArrayMap map) {
        this();
        if (map != null) {
            putAll(map);
        }
    }

    private void allocArrays(int size) {
        Object[] array;
        if (size == 8) {
            synchronized (ArrayMap.class) {
                if (mTwiceBaseCache != null) {
                    array = mTwiceBaseCache;
                    this.mArray = array;
                    mTwiceBaseCache = (Object[]) array[0];
                    this.mHashes = (int[]) array[1];
                    array[1] = null;
                    array[0] = null;
                    mTwiceBaseCacheSize--;
                    return;
                }
            }
        } else if (size == 4) {
            synchronized (ArrayMap.class) {
                if (mBaseCache != null) {
                    array = mBaseCache;
                    this.mArray = array;
                    mBaseCache = (Object[]) array[0];
                    this.mHashes = (int[]) array[1];
                    array[1] = null;
                    array[0] = null;
                    mBaseCacheSize--;
                    return;
                }
            }
        }
        this.mHashes = new int[size];
        this.mArray = new Object[(size << 1)];
    }

    private static void freeArrays(int[] hashes, Object[] array, int size) {
        int i;
        if (hashes.length == 8) {
            synchronized (ArrayMap.class) {
                if (mTwiceBaseCacheSize < 10) {
                    array[0] = mTwiceBaseCache;
                    array[1] = hashes;
                    i = size << 1 - 1;
                    while (i >= 2) {
                        array[i] = null;
                        i--;
                    }
                    mTwiceBaseCache = array;
                    mTwiceBaseCacheSize++;
                }
            }
        } else if (hashes.length == 4) {
            synchronized (ArrayMap.class) {
                if (mBaseCacheSize < 10) {
                    array[0] = mBaseCache;
                    array[1] = hashes;
                    i = size << 1 - 1;
                    while (i >= 2) {
                        array[i] = null;
                        i--;
                    }
                    mBaseCache = array;
                    mBaseCacheSize++;
                }
            }
        }
    }

    public void clear() {
        if (this.mSize != 0) {
            freeArrays(this.mHashes, this.mArray, this.mSize);
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
            this.mSize = 0;
        }
    }

    public boolean containsKey(Object key) {
        return key == null ? indexOfNull() >= 0 : indexOf(key, key.hashCode()) >= 0;
    }

    public boolean containsValue(Object value) {
        return indexOfValue(value) >= 0 ? true : DEBUG;
    }

    public void ensureCapacity(int minimumCapacity) {
        if (this.mHashes.length < minimumCapacity) {
            int[] ohashes = this.mHashes;
            Object[] oarray = this.mArray;
            allocArrays(minimumCapacity);
            if (this.mSize > 0) {
                System.arraycopy(ohashes, 0, this.mHashes, 0, this.mSize);
                System.arraycopy(oarray, 0, this.mArray, 0, this.mSize << 1);
            }
            freeArrays(ohashes, oarray, this.mSize);
        }
    }

    public boolean equals(SimpleArrayMap<K, V> object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Map)) {
            return false;
        }
        Map<?, ?> map = (Map) object;
        if (size() != map.size()) {
            return false;
        }
        int i = 0;
        while (i < this.mSize) {
            try {
                K key = keyAt(i);
                V mine = valueAt(i);
                Object theirs = map.get(key);
                if (mine == null) {
                    return false;
                }
                if (!mine.equals(theirs)) {
                    return false;
                }
                i++;
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e2) {
                return false;
            }
        }
        return true;
    }

    public V get(Object key) {
        int index = key == null ? indexOfNull() : indexOf(key, key.hashCode());
        return index >= 0 ? this.mArray[index << 1 + 1] : null;
    }

    public int hashCode() {
        int[] hashes = this.mHashes;
        Object[] array = this.mArray;
        int result = 0;
        int i = 0;
        int v = 1;
        int s = this.mSize;
        while (i < s) {
            Object value = array[v];
            result += (value == null ? 0 : value.hashCode()) ^ hashes[i];
            i++;
            v += 2;
        }
        return result;
    }

    int indexOf(Object key, int hash) {
        int N = this.mSize;
        if (N == 0) {
            return -1;
        }
        int index = ContainerHelpers.binarySearch(this.mHashes, N, hash);
        if (index < 0 || key.equals(this.mArray[index << 1])) {
            return index;
        }
        int end = index + 1;
        while (end < N && this.mHashes[end] == hash) {
            if (key.equals(this.mArray[end << 1])) {
                return end;
            }
            end++;
        }
        int i = index - 1;
        while (i >= 0 && this.mHashes[i] == hash) {
            if (key.equals(this.mArray[i << 1])) {
                return i;
            }
            i--;
        }
        return end ^ -1;
    }

    int indexOfNull() {
        int N = this.mSize;
        if (N == 0) {
            return -1;
        }
        int index = ContainerHelpers.binarySearch(this.mHashes, N, 0);
        if (index < 0 || this.mArray[index << 1] == null) {
            return index;
        }
        int end = index + 1;
        while (end < N && this.mHashes[end] == 0) {
            if (this.mArray[end << 1] == null) {
                return end;
            }
            end++;
        }
        int i = index - 1;
        while (i >= 0 && this.mHashes[i] == 0) {
            if (this.mArray[i << 1] == null) {
                return i;
            }
            i--;
        }
        return end ^ -1;
    }

    int indexOfValue(Object value) {
        int N = this.mSize * 2;
        Object[] array = this.mArray;
        int i;
        if (value == null) {
            i = 1;
            while (i < N) {
                if (array[i] == null) {
                    return i >> 1;
                }
                i += 2;
            }
        } else {
            i = 1;
            while (i < N) {
                if (value.equals(array[i])) {
                    return i >> 1;
                }
                i += 2;
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        return this.mSize <= 0 ? true : DEBUG;
    }

    public K keyAt(int index) {
        return this.mArray[index << 1];
    }

    public V put(K key, V value) {
        int hash;
        int index;
        int n = ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES;
        if (key == null) {
            hash = 0;
            index = indexOfNull();
        } else {
            hash = key.hashCode();
            index = indexOf(key, hash);
        }
        if (index >= 0) {
            index = index << 1 + 1;
            V old = this.mArray[index];
            this.mArray[index] = value;
            return old;
        } else {
            index ^= -1;
            if (this.mSize >= this.mHashes.length) {
                if (this.mSize >= 8) {
                    n = this.mSize + this.mSize >> 1;
                } else if (this.mSize < 4) {
                    n = 4;
                }
                int[] ohashes = this.mHashes;
                Object[] oarray = this.mArray;
                allocArrays(n);
                if (this.mHashes.length > 0) {
                    System.arraycopy(ohashes, 0, this.mHashes, 0, ohashes.length);
                    System.arraycopy(oarray, 0, this.mArray, 0, oarray.length);
                }
                freeArrays(ohashes, oarray, this.mSize);
            }
            if (index < this.mSize) {
                System.arraycopy(this.mHashes, index, this.mHashes, index + 1, this.mSize - index);
                System.arraycopy(this.mArray, index << 1, this.mArray, (index + 1) << 1, (this.mSize - index) << 1);
            }
            this.mHashes[index] = hash;
            this.mArray[index << 1] = key;
            this.mArray[index << 1 + 1] = value;
            this.mSize++;
            return null;
        }
    }

    public void putAll(SimpleArrayMap<? extends K, ? extends V> array) {
        int N = array.mSize;
        ensureCapacity(this.mSize + N);
        if (this.mSize != 0) {
            int i = 0;
            while (i < N) {
                put(array.keyAt(i), array.valueAt(i));
                i++;
            }
        } else if (N > 0) {
            System.arraycopy(array.mHashes, 0, this.mHashes, 0, N);
            System.arraycopy(array.mArray, 0, this.mArray, 0, N << 1);
            this.mSize = N;
        }
    }

    public V remove(Object key) {
        int index = key == null ? indexOfNull() : indexOf(key, key.hashCode());
        return index >= 0 ? removeAt(index) : null;
    }

    public V removeAt(int index) {
        int n = ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES;
        Object old = this.mArray[index << 1 + 1];
        if (this.mSize <= 1) {
            freeArrays(this.mHashes, this.mArray, this.mSize);
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
            this.mSize = 0;
        } else if (this.mHashes.length <= 8 || this.mSize >= this.mHashes.length / 3) {
            this.mSize--;
            if (index < this.mSize) {
                System.arraycopy(this.mHashes, index + 1, this.mHashes, index, this.mSize - index);
                System.arraycopy(this.mArray, (index + 1) << 1, this.mArray, index << 1, (this.mSize - index) << 1);
            }
            this.mArray[this.mSize << 1] = null;
            this.mArray[this.mSize << 1 + 1] = null;
        } else {
            if (this.mSize > 8) {
                n = this.mSize + this.mSize >> 1;
            }
            int[] ohashes = this.mHashes;
            Object[] oarray = this.mArray;
            allocArrays(n);
            this.mSize--;
            if (index > 0) {
                System.arraycopy(ohashes, 0, this.mHashes, 0, index);
                System.arraycopy(oarray, 0, this.mArray, 0, index << 1);
            }
            if (index < this.mSize) {
                System.arraycopy(ohashes, index + 1, this.mHashes, index, this.mSize - index);
                System.arraycopy(oarray, (index + 1) << 1, this.mArray, index << 1, (this.mSize - index) << 1);
            }
        }
        return old;
    }

    public V setValueAt(int index, V value) {
        index = index << 1 + 1;
        V old = this.mArray[index];
        this.mArray[index] = value;
        return old;
    }

    public int size() {
        return this.mSize;
    }

    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder(this.mSize * 28);
        buffer.append('{');
        int i = 0;
        while (i < this.mSize) {
            if (i > 0) {
                buffer.append(", ");
            }
            SimpleArrayMap<K, V> key = keyAt(i);
            if (key != this) {
                buffer.append(key);
            } else {
                buffer.append("(this Map)");
            }
            buffer.append('=');
            SimpleArrayMap<K, V> value = valueAt(i);
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

    public V valueAt(int index) {
        return this.mArray[index << 1 + 1];
    }
}