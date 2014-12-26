package org.dom4j.tree;

import java.util.Collections;
import java.util.Map;
import org.dom4j.Element;
import org.dom4j.Node;

public class FlyweightProcessingInstruction extends AbstractProcessingInstruction {
    protected String target;
    protected String text;
    protected Map<String, String> values;

    public FlyweightProcessingInstruction(String target, String text) {
        this.target = target;
        this.text = text;
        this.values = parseValues(text);
    }

    public FlyweightProcessingInstruction(String target, Map<String, String> values) {
        this.target = target;
        this.values = values;
        this.text = toString(values);
    }

    protected Node createXPathResult(Element parent) {
        return new DefaultProcessingInstruction(parent, getTarget(), getText());
    }

    public String getTarget() {
        return this.target;
    }

    public String getText() {
        return this.text;
    }

    public String getValue(String name) {
        String answer = (String) this.values.get(name);
        return answer == null ? "" : answer;
    }

    public Map<String, String> getValues() {
        return Collections.unmodifiableMap(this.values);
    }

    public void setTarget(String target) {
        throw new UnsupportedOperationException("This PI is read-only and cannot be modified");
    }
}