package org.dom4j.tree;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Node;

public class ContentListFacade<T extends Node> extends AbstractList<T> {
    private AbstractBranch branch;
    private List<T> branchContent;

    public ContentListFacade(AbstractBranch branch, List<T> branchContent) {
        this.branch = branch;
        this.branchContent = branchContent;
    }

    public void add(int index, T node) {
        this.branch.childAdded(node);
        this.branchContent.add(index, node);
    }

    public boolean add(T node) {
        this.branch.childAdded(node);
        return this.branchContent.add(node);
    }

    public boolean addAll(int index, Collection<? extends T> collection) {
        int count = this.branchContent.size();
        Iterator<? extends T> iter = collection.iterator();
        while (iter.hasNext()) {
            int index2 = index + 1;
            add(index, (Node) iter.next());
            count--;
            index = index2;
        }
        return count == this.branchContent.size();
    }

    public boolean addAll(Collection<? extends T> collection) {
        int count = this.branchContent.size();
        Iterator<? extends T> iter = collection.iterator();
        while (iter.hasNext()) {
            add((Node) iter.next());
            count++;
        }
        return count == this.branchContent.size();
    }

    public void clear() {
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            this.branch.childRemoved((Node) i$.next());
        }
        this.branchContent.clear();
    }

    public boolean contains(T node) {
        return this.branchContent.contains(node);
    }

    public boolean containsAll(Collection<?> c) {
        return this.branchContent.containsAll(c);
    }

    public T get(int index) {
        return (Node) this.branchContent.get(index);
    }

    protected List<T> getBackingList() {
        return this.branchContent;
    }

    public int indexOf(T node) {
        return this.branchContent.indexOf(node);
    }

    public boolean isEmpty() {
        return this.branchContent.isEmpty();
    }

    public int lastIndexOf(T node) {
        return this.branchContent.lastIndexOf(node);
    }

    public T remove(int index) {
        Node node = (Node) this.branchContent.remove(index);
        if (node != null) {
            this.branch.childRemoved(node);
        }
        return node;
    }

    public boolean remove(T node) {
        this.branch.childRemoved(node);
        return this.branchContent.remove(node);
    }

    public boolean removeAll(Collection<?> c) {
        Iterator i$ = c.iterator();
        while (i$.hasNext()) {
            Object object = i$.next();
            if (object instanceof Node) {
                this.branch.childRemoved((Node) object);
            }
        }
        return this.branchContent.removeAll(c);
    }

    public T set(int index, T node) {
        this.branch.childAdded(node);
        return (Node) this.branchContent.set(index, node);
    }

    public int size() {
        return this.branchContent.size();
    }

    public Node[] toArray() {
        return (Node[]) this.branchContent.toArray();
    }

    public Node[] toArray(Node[] a) {
        return (Node[]) this.branchContent.toArray(a);
    }
}