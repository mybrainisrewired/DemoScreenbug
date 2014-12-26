package org.dom4j.io;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;

class ElementStack implements ElementPath {
    private DispatchHandler handler;
    protected int lastElementIndex;
    protected Element[] stack;

    public ElementStack() {
        this(50);
    }

    public ElementStack(int defaultCapacity) {
        this.lastElementIndex = -1;
        this.handler = null;
        this.stack = new Element[defaultCapacity];
    }

    private String getHandlerPath(String path) {
        if (this.handler == null) {
            setDispatchHandler(new DispatchHandler());
        }
        if (path.startsWith("/")) {
            return path;
        }
        return getPath().equals("/") ? getPath() + path : getPath() + "/" + path;
    }

    public void addHandler(String path, ElementHandler elementHandler) {
        this.handler.addHandler(getHandlerPath(path), elementHandler);
    }

    public void clear() {
        this.lastElementIndex = -1;
    }

    public boolean containsHandler(String path) {
        return this.handler.containsHandler(path);
    }

    public Element getCurrent() {
        return peekElement();
    }

    public DispatchHandler getDispatchHandler() {
        return this.handler;
    }

    public Element getElement(int depth) {
        try {
            return this.stack[depth];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public String getPath() {
        if (this.handler == null) {
            setDispatchHandler(new DispatchHandler());
        }
        return this.handler.getPath();
    }

    public Element peekElement() {
        return this.lastElementIndex < 0 ? null : this.stack[this.lastElementIndex];
    }

    public Element popElement() {
        if (this.lastElementIndex < 0) {
            return null;
        }
        Element[] elementArr = this.stack;
        int i = this.lastElementIndex;
        this.lastElementIndex = i - 1;
        return elementArr[i];
    }

    public void pushElement(Element element) {
        int length = this.stack.length;
        int i = this.lastElementIndex + 1;
        this.lastElementIndex = i;
        if (i >= length) {
            reallocate(length * 2);
        }
        this.stack[this.lastElementIndex] = element;
    }

    protected void reallocate(int size) {
        Element[] oldStack = this.stack;
        this.stack = new Element[size];
        System.arraycopy(oldStack, 0, this.stack, 0, oldStack.length);
    }

    public void removeHandler(String path) {
        this.handler.removeHandler(getHandlerPath(path));
    }

    public void setDispatchHandler(DispatchHandler dispatchHandler) {
        this.handler = dispatchHandler;
    }

    public int size() {
        return this.lastElementIndex + 1;
    }
}