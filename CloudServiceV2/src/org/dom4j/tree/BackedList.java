package org.dom4j.tree;

import java.util.Iterator;
import java.util.List;
import org.dom4j.Node;

public class BackedList<T extends Node> extends LazyList<T> {
    private final AbstractBranch branch;
    private final List<Node> branchContent;

    public BackedList(AbstractBranch branch, List<Node> branchContent) {
        this.branch = branch;
        this.branchContent = branchContent;
    }

    public BackedList(AbstractBranch branch, List<Node> branchContent, List<T> initialContent) {
        this(branch, branchContent);
        addAll(initialContent);
    }

    public void add(int index, T node) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index value: " + index + " is less than zero");
        } else if (index > size()) {
            throw new IndexOutOfBoundsException("Index value: " + index + " cannot be greater than " + "the size: " + size());
        } else {
            int realIndex;
            if (size() == 0) {
                realIndex = this.branchContent.size();
            } else if (index < size()) {
                realIndex = this.branchContent.indexOf(get(index));
            } else {
                realIndex = this.branchContent.indexOf(get(size() - 1)) + 1;
            }
            this.branch.addNode(realIndex, node);
            super.add(index, node);
        }
    }

    public boolean add(T node) {
        this.branch.addNode(node);
        return super.add(node);
    }

    public void addLocal(T node) {
        super.add(node);
    }

    public void clear() {
        Iterator<T> iter = iterator();
        while (iter.hasNext()) {
            Node node = (Node) iter.next();
            this.branchContent.remove(node);
            this.branch.childRemoved(node);
        }
        super.clear();
    }

    public T remove(int index) {
        Node node = (Node) super.remove(index);
        if (node != null) {
            this.branch.removeNode(node);
        }
        return node;
    }

    public boolean remove(Node node) {
        this.branch.removeNode(node);
        return super.remove(node);
    }

    public T set(int index, T node) {
        int realIndex = this.branchContent.indexOf(get(index));
        if (realIndex < 0) {
            realIndex = index == 0 ? 0 : Integer.MAX_VALUE;
        }
        if (realIndex < this.branchContent.size()) {
            this.branch.removeNode((Node) get(index));
            this.branch.addNode(realIndex, node);
        } else {
            this.branch.removeNode((Node) get(index));
            this.branch.addNode(node);
        }
        this.branch.childAdded(node);
        return (Node) super.set(index, node);
    }
}