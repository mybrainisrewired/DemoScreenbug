package org.dom4j.tree;

import org.dom4j.CharacterData;
import org.dom4j.Element;

public abstract class AbstractCharacterData extends AbstractNode implements CharacterData {
    public void appendText(String text) {
        setText(getText() + text);
    }

    public String getPath(Element context) {
        Element parent = getParent();
        return (parent == null || parent == context) ? "text()" : parent.getPath(context) + "/text()";
    }

    public String getUniquePath(Element context) {
        Element parent = getParent();
        return (parent == null || parent == context) ? "text()" : parent.getUniquePath(context) + "/text()";
    }
}