package org.dom4j.tree;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class LazyList<E> extends AbstractSequentialList<E> implements Serializable {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final long serialVersionUID = 0;
    protected final transient Entry<E> header;
    protected transient E[] indexedList;
    protected transient int size;

    protected static class Entry<E> {
        E element;
        Entry<E> next;
        Entry<E> previous;

        Entry(E element, Entry<E> next, Entry<E> previous) {
            this.element = element;
            this.next = next;
            this.previous = previous;
        }
    }

    protected class LazyListIterator implements ListIterator<E> {
        private int expectedModCount;
        private int indexNext;
        private Entry<E> lastReturned;
        private Entry<E> next;

        LazyListIterator(int index) {
            this.lastReturned = LazyList.this.header;
            this.expectedModCount = LazyList.this.modCount;
            this.next = LazyList.this.getEntryHeader(index);
        }

        public void add(E e) {
            checkForComodification();
            this.lastReturned = LazyList.this.header;
            LazyList.this.addElement(e, this.next);
            this.indexNext++;
            this.expectedModCount++;
        }

        final void checkForComodification() {
            if (LazyList.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        final void checkForOutside() {
            if (this.lastReturned == LazyList.this.header) {
                throw new IllegalStateException();
            }
        }

        public boolean hasNext() {
            return this.indexNext != LazyList.this.size;
        }

        public boolean hasPrevious() {
            return this.indexNext != 0;
        }

        public E next() {
            checkForComodification();
            if (this.indexNext == LazyList.this.size) {
                throw new NoSuchElementException();
            }
            this.lastReturned = this.next;
            this.next = this.next.next;
            this.indexNext++;
            return this.lastReturned.element;
        }

        public int nextIndex() {
            return this.indexNext;
        }

        public E previous() {
            checkForComodification();
            if (this.indexNext == 0) {
                throw new NoSuchElementException();
            }
            this.next = this.next.previous;
            this.lastReturned = this.next;
            this.indexNext--;
            return this.lastReturned.element;
        }

        public int previousIndex() {
            return this.indexNext - 1;
        }

        public void remove() {
            checkForOutside();
            checkForComodification();
            Entry<E> lastNext = this.lastReturned.next;
            LazyList.this.removeEntry(this.lastReturned);
            if (this.next == this.lastReturned) {
                this.next = lastNext;
            } else {
                this.indexNext--;
            }
            this.lastReturned = LazyList.this.header;
            this.expectedModCount++;
        }

        public void set(E e) {
            checkForOutside();
            checkForComodification();
            this.lastReturned.element = e;
        }
    }

    private static class Range<E> {
        Entry<E> lower;
        Entry<E> upper;

        private Range() {
        }
    }

    static {
        $assertionsDisabled = !LazyList.class.desiredAssertionStatus();
    }

    public LazyList() {
        this.indexedList = null;
        this.size = 0;
        this.header = new Entry(null, null, null);
        this.header.next = this.header;
        this.header.previous = this.header;
    }

    protected LazyList(Entry<E> header) {
        this.indexedList = null;
        this.size = 0;
        this.header = header;
    }

    private Range<E> getRange(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > this.size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException(MessageFormat.format("FromIndex: {0}, ToIndex: {1}, Size: {2}", new Object[]{Integer.valueOf(fromIndex), Integer.valueOf(toIndex), Integer.valueOf(this.size)}));
        } else {
            int[] length = new int[]{fromIndex, toIndex - fromIndex, this.size - toIndex};
            Range<E> range = new Range();
            int i;
            if (length[0] < length[2]) {
                range.lower = this.header;
                i = 0;
                while (i <= fromIndex) {
                    range.lower = range.lower.next;
                    i++;
                }
                if (length[1] < length[2]) {
                    range.upper = range.lower;
                    i = fromIndex;
                    while (i <= toIndex) {
                        range.upper = range.upper.next;
                        i++;
                    }
                } else {
                    range.upper = this.header;
                    i = this.size;
                    while (i > toIndex) {
                        range.upper = range.upper.previous;
                        i--;
                    }
                }
            } else {
                range.upper = this.header;
                i = this.size;
                while (i > toIndex) {
                    range.upper = range.upper.previous;
                    i--;
                }
                if (length[0] < length[1]) {
                    range.lower = this.header;
                    i = 0;
                    while (i <= fromIndex) {
                        range.lower = range.lower.next;
                        i++;
                    }
                } else {
                    range.lower = range.upper;
                    i = toIndex;
                    while (i > fromIndex) {
                        range.lower = range.lower.previous;
                        i--;
                    }
                }
            }
            return range;
        }
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        CloneHelper.setFinalField(LazyList.class, this, "header", new Entry(null, null, null));
        if ($assertionsDisabled || this.header != null) {
            Entry entry = this.header;
            Entry entry2 = this.header;
            Entry entry3 = this.header;
            entry2.previous = entry3;
            entry.next = entry3;
            int size = stream.readInt();
            Object[] tempIndexedList = new Object[size];
            int i = 0;
            while (i < size) {
                E element = stream.readObject();
                addElement(element, this.header);
                tempIndexedList[i] = element;
                i++;
            }
            this.indexedList = tempIndexedList;
        } else {
            throw new AssertionError();
        }
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(this.size);
        Entry<E> entry = this.header.next;
        while (entry != this.header) {
            stream.writeObject(entry.element);
            entry = entry.next;
        }
    }

    public void add(int index, E element) {
        addElement(element, index == this.size ? this.header : getEntry(index));
    }

    public boolean add(E element) {
        addElement(element, this.header);
        return true;
    }

    public boolean addAll(int index, Collection<? extends E> collection) {
        if (collection.isEmpty()) {
            return false;
        }
        Entry<E> entry = getEntryHeader(index).next;
        Iterator i$ = collection.iterator();
        while (i$.hasNext()) {
            addElement(i$.next(), entry);
        }
        return true;
    }

    public boolean addAll(Collection<? extends E> collection) {
        return addAll(0, collection);
    }

    protected Entry<E> addElement(E e, Entry<E> entry) {
        this.indexedList = null;
        Entry<E> newEntry = new Entry(e, entry, entry.previous);
        newEntry.previous.next = newEntry;
        newEntry.next.previous = newEntry;
        this.size++;
        this.modCount++;
        return newEntry;
    }

    public void clear() {
        this.indexedList = null;
        this.size = 0;
        this.header.element = null;
        this.header.next = this.header;
        this.header.previous = this.header;
        this.modCount++;
    }

    public LazyList<E> clone() {
        LazyList<E> clone = new LazyList();
        Entry<E> entry = this.header.next;
        while (entry != this.header) {
            clone.add(entry.element);
            entry = entry.next;
        }
        return clone;
    }

    protected void createIndexedList() {
        if (this.indexedList == null) {
            this.indexedList = new Object[this.size];
            int index = 0;
            Iterator i$ = iterator();
            while (i$.hasNext()) {
                int index2 = index + 1;
                this.indexedList[index] = i$.next();
                index = index2;
            }
        }
    }

    public E get(int index) {
        createIndexedList();
        return this.indexedList[index];
    }

    protected Entry<E> getEntry(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException(MessageFormat.format("Index: {0}, Size: {1}", new Object[]{Integer.valueOf(index), Integer.valueOf(this.size)}));
        } else if (index == 0) {
            return this.header.next;
        } else {
            Entry<E> e = this.header;
            int i;
            if (index < (this.size >> 1)) {
                i = 0;
                while (i <= index) {
                    e = e.next;
                    i++;
                }
                return e;
            } else {
                i = this.size;
                while (i > index) {
                    e = e.previous;
                    i--;
                }
                return e;
            }
        }
    }

    protected Entry<E> getEntryHeader(int index) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException(MessageFormat.format("Index: {0}, Size: {1}", new Object[]{Integer.valueOf(index), Integer.valueOf(this.size)}));
        } else if (index == 0) {
            return this.header.next;
        } else {
            Entry<E> e = this.header;
            int i;
            if (index < (this.size >> 1)) {
                i = 0;
                while (i < index) {
                    e = e.next;
                    i++;
                }
                return e;
            } else {
                i = this.size;
                while (i > index) {
                    e = e.previous;
                    i--;
                }
                return e;
            }
        }
    }

    public ListIterator<E> listIterator(int index) {
        return new LazyListIterator(index);
    }

    public E remove(int index) {
        return removeEntry(getEntry(index));
    }

    protected E removeEntry(Entry<E> entry) {
        if (entry == this.header) {
            throw new NoSuchElementException();
        }
        this.indexedList = null;
        entry.previous.next = entry.next;
        entry.next.previous = entry.previous;
        this.size--;
        this.modCount++;
        return entry.element;
    }

    protected void removeRange(int fromIndex, int toIndex) {
        Range<E> range = getRange(fromIndex, toIndex);
        range.lower.previous.next = range.upper;
        range.upper.previous = range.lower.previous;
    }

    public E set(int index, E element) {
        Entry<E> entry = getEntry(index);
        E oldValue = entry.element;
        entry.element = element;
        if (this.indexedList != null) {
            this.indexedList[index] = element;
        }
        return oldValue;
    }

    public int size() {
        return this.size;
    }

    public List<E> subList(int fromIndex, int toIndex) {
        Range<E> range = getRange(fromIndex, toIndex);
        Entry<E> newHeader = new Entry(null, range.lower, range.upper);
        Entry<E> lastEntry = newHeader;
        while (range.lower != range.upper) {
            lastEntry.next = new Entry(range.lower.element, null, lastEntry);
            lastEntry = lastEntry.next;
            range.lower = range.lower.next;
        }
        newHeader.previous = lastEntry;
        return new LazyList(newHeader);
    }
}