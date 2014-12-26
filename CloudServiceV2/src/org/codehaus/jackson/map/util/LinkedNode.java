package org.codehaus.jackson.map.util;

public final class LinkedNode<T> {
    final LinkedNode<T> _next;
    final T _value;

    public LinkedNode(T value, LinkedNode<T> next) {
        this._value = value;
        this._next = next;
    }

    public static <ST> boolean contains(LinkedNode<ST> linkedNode, ST value) {
        while (linkedNode != null) {
            if (linkedNode.value() == value) {
                return true;
            }
            linkedNode = linkedNode.next();
        }
        return false;
    }

    public LinkedNode<T> next() {
        return this._next;
    }

    public T value() {
        return this._value;
    }
}