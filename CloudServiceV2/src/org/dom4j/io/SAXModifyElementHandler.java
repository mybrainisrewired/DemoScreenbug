package org.dom4j.io;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;

class SAXModifyElementHandler implements ElementHandler {
    private ElementModifier elemModifier;
    private Element modifiedElement;

    public SAXModifyElementHandler(ElementModifier elemModifier) {
        this.elemModifier = elemModifier;
    }

    protected Element getModifiedElement() {
        return this.modifiedElement;
    }

    public void onEnd(ElementPath elementPath) {
        try {
            Element origElement = elementPath.getCurrent();
            Element currentParent = origElement.getParent();
            if (currentParent != null) {
                this.modifiedElement = this.elemModifier.modifyElement((Element) origElement.clone());
                if (this.modifiedElement != null) {
                    this.modifiedElement.setParent(origElement.getParent());
                    this.modifiedElement.setDocument(origElement.getDocument());
                    currentParent.content().set(currentParent.indexOf(origElement), this.modifiedElement);
                }
                origElement.detach();
            } else if (origElement.isRootElement()) {
                this.modifiedElement = this.elemModifier.modifyElement(origElement.clone());
                if (this.modifiedElement != null) {
                    this.modifiedElement.setDocument(origElement.getDocument());
                    origElement.getDocument().setRootElement(this.modifiedElement);
                }
                origElement.detach();
            }
            if (elementPath instanceof ElementStack) {
                ElementStack elementStack = (ElementStack) elementPath;
                elementStack.popElement();
                elementStack.pushElement(this.modifiedElement);
            }
        } catch (Exception e) {
            throw new SAXModifyException(e);
        }
    }

    public void onStart(ElementPath elementPath) {
        this.modifiedElement = elementPath.getCurrent();
    }
}