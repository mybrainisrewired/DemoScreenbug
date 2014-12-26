package org.dom4j.tree;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

class ConcurrentReaderHashMap extends AbstractMap implements Map, Cloneable, Serializable {
    public static int DEFAULT_INITIAL_CAPACITY = 0;
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MAXIMUM_CAPACITY = 1073741824;
    private static final int MINIMUM_CAPACITY = 4;
    protected final BarrierLock barrierLock;
    protected transient int count;
    protected transient Set entrySet;
    protected transient Set keySet;
    protected transient Object lastWrite;
    protected float loadFactor;
    protected transient Entry[] table;
    protected int threshold;
    protected transient Collection values;

    protected static class BarrierLock implements Serializable {
        protected BarrierLock() {
        }
    }

    protected static class Entry implements java.util.Map.Entry {
        protected final int hash;
        protected final Object key;
        protected final Entry next;
        protected volatile Object value;

        Entry(int hash, Object key, Object value, Entry next) {
            this.hash = hash;
            this.key = key;
            this.next = next;
            this.value = value;
        }

        public boolean equals(Object o) {
            if (!(o instanceof java.util.Map.Entry)) {
                return false;
            }
            java.util.Map.Entry e = (java.util.Map.Entry) o;
            return this.key.equals(e.getKey()) && this.value.equals(e.getValue());
        }

        public Object getKey() {
            return this.key;
        }

        public Object getValue() {
            return this.value;
        }

        public int hashCode() {
            return this.key.hashCode() ^ this.value.hashCode();
        }

        public Object setValue(Object value) {
            if (value == null) {
                throw new NullPointerException();
            }
            Object oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public String toString() {
            return this.key + "=" + this.value;
        }
    }

    private class EntrySet extends AbstractSet {
        private EntrySet() {
        }

        public void clear() {
            ConcurrentReaderHashMap.this.clear();
        }

        public boolean contains(Object o) {
            if (!(o instanceof java.util.Map.Entry)) {
                return false;
            }
            java.util.Map.Entry entry = (java.util.Map.Entry) o;
            Object v = ConcurrentReaderHashMap.this.get(entry.getKey());
            return v != null && v.equals(entry.getValue());
        }

        public Iterator iterator() {
            return new HashIterator();
        }

        public boolean remove(Object o) {
            return !(o instanceof java.util.Map.Entry) ? false : ConcurrentReaderHashMap.this.findAndRemoveEntry((java.util.Map.Entry) o);
        }

        public int size() {
            return ConcurrentReaderHashMap.this.size();
        }
    }

    protected class HashIterator implements Iterator, Enumeration {
        protected Object currentKey;
        protected Object currentValue;
        protected Entry entry;
        protected int index;
        protected Entry lastReturned;
        protected final Entry[] tab;

        protected HashIterator() {
            this.entry = null;
            this.lastReturned = null;
            this.tab = ConcurrentReaderHashMap.this.getTableForReading();
            this.index = this.tab.length - 1;
        }

        public boolean hasMoreElements() {
            return hasNext();
        }

        public boolean hasNext() {
            do {
                if (this.entry != null) {
                    Object v = this.entry.value;
                    if (v != null) {
                        this.currentKey = this.entry.key;
                        this.currentValue = v;
                        return true;
                    } else {
                        this.entry = this.entry.next;
                    }
                }
                while (this.entry == null && this.index >= 0) {
                    Entry[] entryArr = this.tab;
                    int i = this.index;
                    this.index = i - 1;
                    this.entry = entryArr[i];
                }
            } while (this.entry != null);
            this.currentValue = null;
            this.currentKey = null;
            return false;
        }

        public Object next() {
            if (this.currentKey != null || hasNext()) {
                Object result = returnValueOfNext();
                this.lastReturned = this.entry;
                this.currentValue = null;
                this.currentKey = null;
                this.entry = this.entry.next;
                return result;
            } else {
                throw new NoSuchElementException();
            }
        }

        public Object nextElement() {
            return next();
        }

        public void remove() {
            if (this.lastReturned == null) {
                throw new IllegalStateException();
            }
            ConcurrentReaderHashMap.this.remove(this.lastReturned.key);
            this.lastReturned = null;
        }

        protected Object returnValueOfNext() {
            return this.entry;
        }
    }

    private class KeySet extends AbstractSet {
        private KeySet() {
        }

        public void clear() {
            ConcurrentReaderHashMap.this.clear();
        }

        public boolean contains(Object o) {
            return ConcurrentReaderHashMap.this.containsKey(o);
        }

        public Iterator iterator() {
            return new KeyIterator();
        }

        public boolean remove(Object o) {
            return ConcurrentReaderHashMap.this.remove(o) != null;
        }

        public int size() {
            return ConcurrentReaderHashMap.this.size();
        }
    }

    private class Values extends AbstractCollection {
        private Values() {
        }

        public void clear() {
            ConcurrentReaderHashMap.this.clear();
        }

        public boolean contains(Object o) {
            return ConcurrentReaderHashMap.this.containsValue(o);
        }

        public Iterator iterator() {
            return new ValueIterator();
        }

        public int size() {
            return ConcurrentReaderHashMap.this.size();
        }
    }

    protected class KeyIterator extends HashIterator {
        protected KeyIterator() {
            super();
        }

        protected Object returnValueOfNext() {
            return this.currentKey;
        }
    }

    protected class ValueIterator extends HashIterator {
        protected ValueIterator() {
            super();
        }

        protected Object returnValueOfNext() {
            return this.currentValue;
        }
    }

    static {
        DEFAULT_INITIAL_CAPACITY = 32;
    }

    public ConcurrentReaderHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, 0.75f);
    }

    public ConcurrentReaderHashMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public ConcurrentReaderHashMap(int initialCapacity, float loadFactor) {
        this.barrierLock = new BarrierLock();
        this.keySet = null;
        this.entrySet = null;
        this.values = null;
        if (loadFactor <= 0.0f) {
            throw new IllegalArgumentException("Illegal Load factor: " + loadFactor);
        }
        this.loadFactor = loadFactor;
        int cap = p2capacity(initialCapacity);
        this.table = new Entry[cap];
        this.threshold = (int) (((float) cap) * loadFactor);
    }

    public ConcurrentReaderHashMap(Map t) {
        this(Math.max(((int) (((float) t.size()) / 0.75f)) + 1, Segment.TOKENS_PER_SEGMENT), 0.75f);
        putAll(t);
    }

    private static int hash(Object x) {
        int h = x.hashCode();
        return h << 7 - h + h >>> 9 + h >>> 17;
    }

    private int p2capacity(int initialCapacity) {
        int cap = initialCapacity;
        if (cap > 1073741824 || cap < 0) {
            return MAXIMUM_CAPACITY;
        }
        int result = MINIMUM_CAPACITY;
        while (result < cap) {
            result <<= 1;
        }
        return result;
    }

    private synchronized void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.table = new Entry[s.readInt()];
        int size = s.readInt();
        int i = 0;
        while (i < size) {
            put(s.readObject(), s.readObject());
            i++;
        }
    }

    private synchronized void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(this.table.length);
        s.writeInt(this.count);
        int index = this.table.length - 1;
        while (index >= 0) {
            Entry entry = this.table[index];
            while (entry != null) {
                s.writeObject(entry.key);
                s.writeObject(entry.value);
                entry = entry.next;
            }
            index--;
        }
    }

    public synchronized int capacity() {
        return this.table.length;
    }

    public synchronized void clear() {
        Entry[] tab = this.table;
        int i = 0;
        while (i < tab.length) {
            Entry e = tab[i];
            while (e != null) {
                e.value = null;
                e = e.next;
            }
            tab[i] = null;
            i++;
        }
        this.count = 0;
        recordModification(tab);
    }

    public synchronized Object clone() {
        ConcurrentReaderHashMap t;
        try {
            t = (ConcurrentReaderHashMap) super.clone();
            t.keySet = null;
            t.entrySet = null;
            t.values = null;
            Entry[] tab = this.table;
            t.table = new Entry[tab.length];
            Entry[] ttab = t.table;
            int i = 0;
            while (i < tab.length) {
                Entry e = tab[i];
                Entry first = null;
                while (e != null) {
                    Entry first2 = new Entry(e.hash, e.key, e.value, first);
                    e = e.next;
                    first = first2;
                }
                ttab[i] = first;
                i++;
            }
        } catch (CloneNotSupportedException e2) {
            throw new InternalError();
        } catch (Throwable th) {
        }
        return t;
    }

    public boolean contains(Object value) {
        return containsValue(value);
    }

    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    public boolean containsValue(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        Entry[] tab = getTableForReading();
        int i = 0;
        while (i < tab.length) {
            Entry e = tab[i];
            while (e != null) {
                if (value.equals(e.value)) {
                    return true;
                }
                e = e.next;
            }
            i++;
        }
        return false;
    }

    public Enumeration elements() {
        return new ValueIterator();
    }

    public Set entrySet() {
        Set es = this.entrySet;
        if (es != null) {
            return es;
        }
        es = new EntrySet(null);
        this.entrySet = es;
        return es;
    }

    protected boolean eq(Object x, Object y) {
        return x == y || x.equals(y);
    }

    protected synchronized boolean findAndRemoveEntry(java.util.Map.Entry entry) {
        boolean z;
        Object key = entry.getKey();
        Object v = get(key);
        if (v == null || !v.equals(entry.getValue())) {
            z = false;
        } else {
            remove(key);
            z = true;
        }
        return z;
    }

    public Object get(Object key) {
        int hash = hash(key);
        Entry[] tab = this.table;
        int index = hash & (tab.length - 1);
        Entry first = tab[index];
        Entry e = first;
        while (true) {
            if (e == null) {
                Entry[] reread = getTableForReading();
                if (tab == reread && first == tab[index]) {
                    return null;
                }
                tab = reread;
                index = hash & (tab.length - 1);
                first = tab[index];
                e = first;
            } else if (e.hash == hash && eq(key, e.key)) {
                Object value = e.value;
                if (value != null) {
                    return value;
                }
                synchronized (this) {
                    tab = this.table;
                }
                index = hash & (tab.length - 1);
                first = tab[index];
                e = first;
            } else {
                e = e.next;
            }
        }
    }

    protected final Entry[] getTableForReading() {
        Entry[] entryArr;
        synchronized (this.barrierLock) {
            entryArr = this.table;
        }
        return entryArr;
    }

    public synchronized boolean isEmpty() {
        return this.count == 0;
    }

    public Set keySet() {
        Set ks = this.keySet;
        if (ks != null) {
            return ks;
        }
        ks = new KeySet(null);
        this.keySet = ks;
        return ks;
    }

    public Enumeration keys() {
        return new KeyIterator();
    }

    public float loadFactor() {
        return this.loadFactor;
    }

    public Object put(Object key, Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        Object oldValue;
        int hash = hash(key);
        Entry[] tab = this.table;
        int index = hash & (tab.length - 1);
        Entry first = tab[index];
        Entry e = first;
        while (e != null) {
            if (e.hash == hash && eq(key, e.key)) {
                break;
            }
            e = e.next;
        }
        synchronized (this) {
            if (tab == this.table) {
                if (e != null) {
                    oldValue = e.value;
                    if (first == tab[index] && oldValue != null) {
                        e.value = value;
                    }
                } else if (first == tab[index]) {
                    Entry newEntry = new Entry(hash, key, value, first);
                    tab[index] = newEntry;
                    int i = this.count + 1;
                    this.count = i;
                    if (i >= this.threshold) {
                        rehash();
                    } else {
                        recordModification(newEntry);
                    }
                    oldValue = null;
                }
            }
            oldValue = sput(key, value, hash);
        }
        return oldValue;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void putAll(java.util.Map r7_t) {
        throw new UnsupportedOperationException("Method not decompiled: org.dom4j.tree.ConcurrentReaderHashMap.putAll(java.util.Map):void");
        /*
        r6 = this;
        monitor-enter(r6);
        r3 = r7.size();	 Catch:{ all -> 0x0011 }
        if (r3 != 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r6);
        return;
    L_0x0009:
        r5 = r6.threshold;	 Catch:{ all -> 0x0011 }
        if (r3 < r5) goto L_0x0014;
    L_0x000d:
        r6.rehash();	 Catch:{ all -> 0x0011 }
        goto L_0x0009;
    L_0x0011:
        r5 = move-exception;
        monitor-exit(r6);
        throw r5;
    L_0x0014:
        r5 = r7.entrySet();	 Catch:{ all -> 0x0011 }
        r1 = r5.iterator();	 Catch:{ all -> 0x0011 }
    L_0x001c:
        r5 = r1.hasNext();	 Catch:{ all -> 0x0011 }
        if (r5 == 0) goto L_0x0007;
    L_0x0022:
        r0 = r1.next();	 Catch:{ all -> 0x0011 }
        r0 = (java.util.Map.Entry) r0;	 Catch:{ all -> 0x0011 }
        r2 = r0.getKey();	 Catch:{ all -> 0x0011 }
        r4 = r0.getValue();	 Catch:{ all -> 0x0011 }
        r6.put(r2, r4);	 Catch:{ all -> 0x0011 }
        goto L_0x001c;
        */
    }

    protected final void recordModification(Object x) {
        synchronized (this.barrierLock) {
            this.lastWrite = x;
        }
    }

    protected void rehash() {
        Entry[] oldTable = this.table;
        int oldCapacity = oldTable.length;
        if (oldCapacity >= 1073741824) {
            this.threshold = Integer.MAX_VALUE;
        } else {
            int newCapacity = oldCapacity << 1;
            int mask = newCapacity - 1;
            this.threshold = (int) (((float) newCapacity) * this.loadFactor);
            Entry[] newTable = new Entry[newCapacity];
            int i = 0;
            while (i < oldCapacity) {
                Entry e = oldTable[i];
                if (e != null) {
                    int idx = e.hash & mask;
                    Entry next = e.next;
                    if (next == null) {
                        newTable[idx] = e;
                    } else {
                        int k;
                        Entry lastRun = e;
                        int lastIdx = idx;
                        Entry last = next;
                        while (last != null) {
                            k = last.hash & mask;
                            if (k != lastIdx) {
                                lastIdx = k;
                                lastRun = last;
                            }
                            last = last.next;
                        }
                        newTable[lastIdx] = lastRun;
                        Entry p = e;
                        while (p != lastRun) {
                            k = p.hash & mask;
                            newTable[k] = new Entry(p.hash, p.key, p.value, newTable[k]);
                            p = p.next;
                        }
                    }
                }
                i++;
            }
            this.table = newTable;
            recordModification(newTable);
        }
    }

    public Object remove(Object key) {
        Object obj = null;
        int hash = hash(key);
        Entry[] tab = this.table;
        int index = hash & (tab.length - 1);
        Entry first = tab[index];
        Entry entry = first;
        entry = first;
        while (entry != null) {
            if (entry.hash == hash && eq(key, entry.key)) {
                break;
            }
            entry = entry.next;
        }
        synchronized (this) {
            if (tab == this.table) {
                if (entry != null) {
                    obj = entry.value;
                    if (first == tab[index] && obj != null) {
                        entry.value = null;
                        this.count--;
                        Entry p = first;
                        Entry head = entry.next;
                        while (p != entry) {
                            Entry head2 = new Entry(p.hash, p.key, p.value, head);
                            p = p.next;
                            head = head2;
                        }
                        tab[index] = head;
                        recordModification(head);
                    }
                } else if (first == tab[index]) {
                }
            }
            obj = sremove(key, hash);
        }
        return obj;
    }

    public synchronized int size() {
        return this.count;
    }

    protected Object sput(Object key, Object value, int hash) {
        Entry[] tab = this.table;
        int index = hash & (tab.length - 1);
        Entry first = tab[index];
        Entry e = first;
        while (e != null) {
            if (e.hash == hash && eq(key, e.key)) {
                Object oldValue = e.value;
                e.value = value;
                return oldValue;
            } else {
                e = e.next;
            }
        }
        Entry newEntry = new Entry(hash, key, value, first);
        tab[index] = newEntry;
        int i = this.count + 1;
        this.count = i;
        if (i >= this.threshold) {
            rehash();
        } else {
            recordModification(newEntry);
        }
        return null;
    }

    protected Object sremove(Object key, int hash) {
        Entry[] tab = this.table;
        int index = hash & (tab.length - 1);
        Entry first = tab[index];
        Entry e = first;
        while (e != null) {
            if (e.hash == hash && eq(key, e.key)) {
                Object oldValue = e.value;
                e.value = null;
                this.count--;
                Entry p = first;
                Entry head = e.next;
                while (p != e) {
                    Entry head2 = new Entry(p.hash, p.key, p.value, head);
                    p = p.next;
                    head = head2;
                }
                tab[index] = head;
                recordModification(head);
                return oldValue;
            } else {
                e = e.next;
            }
        }
        return null;
    }

    public Collection values() {
        Collection vs = this.values;
        if (vs != null) {
            return vs;
        }
        vs = new Values(null);
        this.values = vs;
        return vs;
    }
}