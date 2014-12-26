package com.nostra13.universalimageloader.core.assist.deque;

import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LinkedBlockingDeque<E> extends AbstractQueue<E> implements BlockingDeque<E>, Serializable {
    private static final long serialVersionUID = -387911632671998426L;
    private final int capacity;
    private transient int count;
    transient Node<E> first;
    transient Node<E> last;
    final ReentrantLock lock;
    private final Condition notEmpty;
    private final Condition notFull;

    private abstract class AbstractItr implements Iterator<E> {
        private Node<E> lastRet;
        Node<E> next;
        E nextItem;

        AbstractItr() {
            ReentrantLock lock = LinkedBlockingDeque.this.lock;
            lock.lock();
            this.next = firstNode();
            this.nextItem = this.next == null ? null : this.next.item;
            lock.unlock();
        }

        private Node<E> succ(Node<E> node) {
            while (true) {
                Node<E> s = nextNode(node);
                if (s == null) {
                    return null;
                }
                if (s.item != null) {
                    return s;
                }
                if (s == node) {
                    return firstNode();
                }
                node = s;
            }
        }

        void advance() {
            ReentrantLock lock = LinkedBlockingDeque.this.lock;
            lock.lock();
            this.next = succ(this.next);
            this.nextItem = this.next == null ? null : this.next.item;
            lock.unlock();
        }

        abstract Node<E> firstNode();

        public boolean hasNext() {
            return this.next != null;
        }

        public E next() {
            if (this.next == null) {
                throw new NoSuchElementException();
            }
            this.lastRet = this.next;
            E x = this.nextItem;
            advance();
            return x;
        }

        abstract Node<E> nextNode(Node<E> node);

        public void remove() {
            Node<E> n = this.lastRet;
            if (n == null) {
                throw new IllegalStateException();
            }
            this.lastRet = null;
            ReentrantLock lock = LinkedBlockingDeque.this.lock;
            lock.lock();
            if (n.item != null) {
                LinkedBlockingDeque.this.unlink(n);
            }
            lock.unlock();
        }
    }

    static final class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(E x) {
            this.item = x;
        }
    }

    private class DescendingItr extends AbstractItr {
        private DescendingItr() {
            super();
        }

        Node<E> firstNode() {
            return LinkedBlockingDeque.this.last;
        }

        Node<E> nextNode(Node<E> n) {
            return n.prev;
        }
    }

    private class Itr extends AbstractItr {
        private Itr() {
            super();
        }

        Node<E> firstNode() {
            return LinkedBlockingDeque.this.first;
        }

        Node<E> nextNode(Node<E> n) {
            return n.next;
        }
    }

    public LinkedBlockingDeque() {
        this(Integer.MAX_VALUE);
    }

    public LinkedBlockingDeque(int capacity) {
        this.lock = new ReentrantLock();
        this.notEmpty = this.lock.newCondition();
        this.notFull = this.lock.newCondition();
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;
    }

    public LinkedBlockingDeque(Collection<? extends E> c) {
        this(Integer.MAX_VALUE);
        ReentrantLock lock = this.lock;
        lock.lock();
        Iterator it = c.iterator();
        while (it.hasNext()) {
            Object e = it.next();
            if (e == null) {
                throw new NullPointerException();
            } else if (!linkLast(new Node(e))) {
                throw new IllegalStateException("Deque full");
            }
        }
        lock.unlock();
    }

    private boolean linkFirst(Node<E> node) {
        if (this.count >= this.capacity) {
            return false;
        }
        Node<E> f = this.first;
        node.next = f;
        this.first = node;
        if (this.last == null) {
            this.last = node;
        } else {
            f.prev = node;
        }
        this.count++;
        this.notEmpty.signal();
        return true;
    }

    private boolean linkLast(Node<E> node) {
        if (this.count >= this.capacity) {
            return false;
        }
        Node<E> l = this.last;
        node.prev = l;
        this.last = node;
        if (this.first == null) {
            this.first = node;
        } else {
            l.next = node;
        }
        this.count++;
        this.notEmpty.signal();
        return true;
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.count = 0;
        this.first = null;
        this.last = null;
        while (true) {
            E item = s.readObject();
            if (item != null) {
                add(item);
            } else {
                return;
            }
        }
    }

    private E unlinkFirst() {
        Node<E> f = this.first;
        if (f == null) {
            return null;
        }
        Node<E> n = f.next;
        E item = f.item;
        f.item = null;
        f.next = f;
        this.first = n;
        if (n == null) {
            this.last = null;
        } else {
            n.prev = null;
        }
        this.count--;
        this.notFull.signal();
        return item;
    }

    private E unlinkLast() {
        Node<E> l = this.last;
        if (l == null) {
            return null;
        }
        Node<E> p = l.prev;
        E item = l.item;
        l.item = null;
        l.prev = l;
        this.last = p;
        if (p == null) {
            this.first = null;
        } else {
            p.next = null;
        }
        this.count--;
        this.notFull.signal();
        return item;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        ReentrantLock lock = this.lock;
        lock.lock();
        s.defaultWriteObject();
        Node<E> p = this.first;
        while (p != null) {
            s.writeObject(p.item);
            p = p.next;
        }
        s.writeObject(null);
        lock.unlock();
    }

    public boolean add(E e) {
        addLast(e);
        return true;
    }

    public void addFirst(E e) {
        if (!offerFirst(e)) {
            throw new IllegalStateException("Deque full");
        }
    }

    public void addLast(E e) {
        if (!offerLast(e)) {
            throw new IllegalStateException("Deque full");
        }
    }

    public void clear() {
        ReentrantLock lock = this.lock;
        lock.lock();
        Node<E> f = this.first;
        while (f != null) {
            f.item = null;
            Node<E> n = f.next;
            f.prev = null;
            f.next = null;
            f = n;
        }
        this.last = null;
        this.first = null;
        this.count = 0;
        this.notFull.signalAll();
        lock.unlock();
    }

    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        ReentrantLock lock = this.lock;
        lock.lock();
        Node<E> p = this.first;
        while (p != null) {
            if (o.equals(p.item)) {
                lock.unlock();
                return true;
            } else {
                p = p.next;
            }
        }
        lock.unlock();
        return false;
    }

    public Iterator<E> descendingIterator() {
        return new DescendingItr(null);
    }

    public int drainTo(Collection<? super E> c) {
        return drainTo(c, MoPubClientPositioning.NO_REPEAT);
    }

    public int drainTo(Collection<? super E> c, int maxElements) {
        if (c == null) {
            throw new NullPointerException();
        } else if (c == this) {
            throw new IllegalArgumentException();
        } else {
            ReentrantLock lock = this.lock;
            lock.lock();
            int n = Math.min(maxElements, this.count);
            int i = 0;
            while (i < n) {
                c.add(this.first.item);
                unlinkFirst();
                i++;
            }
            lock.unlock();
            return n;
        }
    }

    public E element() {
        return getFirst();
    }

    public E getFirst() {
        E x = peekFirst();
        if (x != null) {
            return x;
        }
        throw new NoSuchElementException();
    }

    public E getLast() {
        E x = peekLast();
        if (x != null) {
            return x;
        }
        throw new NoSuchElementException();
    }

    public Iterator<E> iterator() {
        return new Itr(null);
    }

    public boolean offer(E e) {
        return offerLast(e);
    }

    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return offerLast(e, timeout, unit);
    }

    public boolean offerFirst(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        Node<E> node = new Node(e);
        ReentrantLock lock = this.lock;
        lock.lock();
        boolean linkFirst = linkFirst(node);
        lock.unlock();
        return linkFirst;
    }

    public boolean offerFirst(E e, long timeout, TimeUnit unit) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        Node<E> node = new Node(e);
        long nanos = unit.toNanos(timeout);
        ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        while (!linkFirst(node)) {
            if (nanos <= 0) {
                lock.unlock();
                return false;
            } else {
                nanos = this.notFull.awaitNanos(nanos);
            }
        }
        lock.unlock();
        return true;
    }

    public boolean offerLast(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        Node<E> node = new Node(e);
        ReentrantLock lock = this.lock;
        lock.lock();
        boolean linkLast = linkLast(node);
        lock.unlock();
        return linkLast;
    }

    public boolean offerLast(E e, long timeout, TimeUnit unit) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        Node<E> node = new Node(e);
        long nanos = unit.toNanos(timeout);
        ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        while (!linkLast(node)) {
            if (nanos <= 0) {
                lock.unlock();
                return false;
            } else {
                nanos = this.notFull.awaitNanos(nanos);
            }
        }
        lock.unlock();
        return true;
    }

    public E peek() {
        return peekFirst();
    }

    public E peekFirst() {
        ReentrantLock lock = this.lock;
        lock.lock();
        E e = this.first == null ? null : this.first.item;
        lock.unlock();
        return e;
    }

    public E peekLast() {
        ReentrantLock lock = this.lock;
        lock.lock();
        E e = this.last == null ? null : this.last.item;
        lock.unlock();
        return e;
    }

    public E poll() {
        return pollFirst();
    }

    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return pollFirst(timeout, unit);
    }

    public E pollFirst() {
        ReentrantLock lock = this.lock;
        lock.lock();
        E unlinkFirst = unlinkFirst();
        lock.unlock();
        return unlinkFirst;
    }

    public E pollFirst(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        while (true) {
            E x = unlinkFirst();
            if (x != null) {
                lock.unlock();
                return x;
            } else if (nanos <= 0) {
                lock.unlock();
                return null;
            } else {
                nanos = this.notEmpty.awaitNanos(nanos);
            }
        }
    }

    public E pollLast() {
        ReentrantLock lock = this.lock;
        lock.lock();
        E unlinkLast = unlinkLast();
        lock.unlock();
        return unlinkLast;
    }

    public E pollLast(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        while (true) {
            E x = unlinkLast();
            if (x != null) {
                lock.unlock();
                return x;
            } else if (nanos <= 0) {
                lock.unlock();
                return null;
            } else {
                nanos = this.notEmpty.awaitNanos(nanos);
            }
        }
    }

    public E pop() {
        return removeFirst();
    }

    public void push(E e) {
        addFirst(e);
    }

    public void put(E e) throws InterruptedException {
        putLast(e);
    }

    public void putFirst(E e) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        Node<E> node = new Node(e);
        ReentrantLock lock = this.lock;
        lock.lock();
        while (!linkFirst(node)) {
            this.notFull.await();
        }
        lock.unlock();
    }

    public void putLast(E e) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        Node<E> node = new Node(e);
        ReentrantLock lock = this.lock;
        lock.lock();
        while (!linkLast(node)) {
            this.notFull.await();
        }
        lock.unlock();
    }

    public int remainingCapacity() {
        ReentrantLock lock = this.lock;
        lock.lock();
        int i = this.capacity - this.count;
        lock.unlock();
        return i;
    }

    public E remove() {
        return removeFirst();
    }

    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    public E removeFirst() {
        E x = pollFirst();
        if (x != null) {
            return x;
        }
        throw new NoSuchElementException();
    }

    public boolean removeFirstOccurrence(Object o) {
        if (o == null) {
            return false;
        }
        ReentrantLock lock = this.lock;
        lock.lock();
        Node<E> p = this.first;
        while (p != null) {
            if (o.equals(p.item)) {
                unlink(p);
                lock.unlock();
                return true;
            } else {
                p = p.next;
            }
        }
        lock.unlock();
        return false;
    }

    public E removeLast() {
        E x = pollLast();
        if (x != null) {
            return x;
        }
        throw new NoSuchElementException();
    }

    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            return false;
        }
        ReentrantLock lock = this.lock;
        lock.lock();
        Node<E> p = this.last;
        while (p != null) {
            if (o.equals(p.item)) {
                unlink(p);
                lock.unlock();
                return true;
            } else {
                p = p.prev;
            }
        }
        lock.unlock();
        return false;
    }

    public int size() {
        ReentrantLock lock = this.lock;
        lock.lock();
        int i = this.count;
        lock.unlock();
        return i;
    }

    public E take() throws InterruptedException {
        return takeFirst();
    }

    public E takeFirst() throws InterruptedException {
        ReentrantLock lock = this.lock;
        lock.lock();
        while (true) {
            E x = unlinkFirst();
            if (x != null) {
                lock.unlock();
                return x;
            } else {
                this.notEmpty.await();
            }
        }
    }

    public E takeLast() throws InterruptedException {
        ReentrantLock lock = this.lock;
        lock.lock();
        while (true) {
            E x = unlinkLast();
            if (x != null) {
                lock.unlock();
                return x;
            } else {
                this.notEmpty.await();
            }
        }
    }

    public Object[] toArray() {
        ReentrantLock lock = this.lock;
        lock.lock();
        Object[] a = new Object[this.count];
        Node<E> p = this.first;
        int k = 0;
        while (p != null) {
            int k2 = k + 1;
            a[k] = p.item;
            p = p.next;
            k = k2;
        }
        lock.unlock();
        return a;
    }

    public <T> T[] toArray(T[] a) {
        ReentrantLock lock = this.lock;
        lock.lock();
        if (a.length < this.count) {
            a = (Object[]) Array.newInstance(a.getClass().getComponentType(), this.count);
        }
        Node<E> p = this.first;
        int k = 0;
        while (p != null) {
            int k2 = k + 1;
            a[k] = p.item;
            p = p.next;
            k = k2;
        }
        if (a.length > k) {
            a[k] = null;
        }
        lock.unlock();
        return a;
    }

    public String toString() {
        ReentrantLock lock = this.lock;
        lock.lock();
        Node<E> p = this.first;
        if (p == null) {
            lock.unlock();
            return "[]";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            while (true) {
                E e = p.item;
                if (e == this) {
                    e = "(this Collection)";
                }
                sb.append(e);
                p = p.next;
                if (p == null) {
                    String toString = sb.append(']').toString();
                    lock.unlock();
                    return toString;
                } else {
                    sb.append(',').append(' ');
                }
            }
        }
    }

    void unlink(Node<E> x) {
        Node<E> p = x.prev;
        Node<E> n = x.next;
        if (p == null) {
            unlinkFirst();
        } else if (n == null) {
            unlinkLast();
        } else {
            p.next = n;
            n.prev = p;
            x.item = null;
            this.count--;
            this.notFull.signal();
        }
    }
}