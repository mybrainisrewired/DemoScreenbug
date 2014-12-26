package org.dom4j.tree;

import java.util.Map;
import org.dom4j.Element;

public class DefaultProcessingInstruction extends FlyweightProcessingInstruction {
    private Element parent;

    public DefaultProcessingInstruction(String target, String values) {
        super(target, values);
    }

    public DefaultProcessingInstruction(String target, Map values) {
        super(target, values);
    }

    public DefaultProcessingInstruction(Element parent, String target, String values) {
        super(target, values);
        this.parent = parent;
    }

    public Element getParent() {
        return this.parent;
    }

    public boolean isReadOnly() {
        return false;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setText(String text) {
        this.text = text;
        this.values = parseValues(text);
    }

    public void setValue(String name, String value) {
        this.values.put(name, value);
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
        this.text = toString(values);
    }

    public boolean supportsParent() {
        return true;
    }
}