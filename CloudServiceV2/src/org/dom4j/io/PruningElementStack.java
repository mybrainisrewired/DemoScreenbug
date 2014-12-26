package org.dom4j.io;

import org.dom4j.Element;
import org.dom4j.ElementHandler;

class PruningElementStack extends ElementStack {
    private ElementHandler elementHandler;
    private int matchingElementIndex;
    private String[] path;

    public PruningElementStack(String[] path, ElementHandler elementHandler) {
        this.path = path;
        this.elementHandler = elementHandler;
        checkPath();
    }

    public PruningElementStack(String[] path, ElementHandler elementHandler, int defaultCapacity) {
        super(defaultCapacity);
        this.path = path;
        this.elementHandler = elementHandler;
        checkPath();
    }

    private void checkPath() {
        if (this.path.length < 2) {
            throw new RuntimeException("Invalid path of length: " + this.path.length + " it must be greater than 2");
        }
        this.matchingElementIndex = this.path.length - 2;
    }

    protected void pathMatches(Element parent, Element selectedNode) {
        this.elementHandler.onEnd(this);
        parent.remove(selectedNode);
    }

    public Element popElement() {
        Element answer = super.popElement();
        if (this.lastElementIndex == this.matchingElementIndex && this.lastElementIndex >= 0 && validElement(answer, this.lastElementIndex + 1)) {
            Element parent = null;
            int i = 0;
            while (i <= this.lastElementIndex) {
                parent = this.stack[i];
                if (!validElement(parent, i)) {
                    parent = null;
                    break;
                } else {
                    i++;
                }
            }
            if (parent != null) {
                pathMatches(parent, answer);
            }
        }
        return answer;
    }

    protected boolean validElement(Element element, int index) {
        String requiredName = this.path[index];
        String name = element.getName();
        if (requiredName == name) {
            return true;
        }
        return (requiredName == null || name == null) ? false : requiredName.equals(name);
    }
}