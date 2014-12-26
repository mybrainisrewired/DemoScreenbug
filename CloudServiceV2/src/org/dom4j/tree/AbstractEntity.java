package org.dom4j.tree;

import java.io.IOException;
import java.io.Writer;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.NodeType;
import org.dom4j.Visitor;

public abstract class AbstractEntity extends AbstractNode implements Entity {
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String asXML() {
        return "&" + getName() + ";";
    }

    public NodeType getNodeTypeEnum() {
        return NodeType.ENTITY_REFERENCE_NODE;
    }

    public String getPath(Element context) {
        Element parent = getParent();
        return (parent == null || parent == context) ? "text()" : parent.getPath(context) + "/text()";
    }

    public String getStringValue() {
        return "&" + getName() + ";";
    }

    public String getUniquePath(Element context) {
        Element parent = getParent();
        return (parent == null || parent == context) ? "text()" : parent.getUniquePath(context) + "/text()";
    }

    public String toString() {
        return super.toString() + " [Entity: &" + getName() + ";]";
    }

    public void write(Writer writer) throws IOException {
        writer.write("&");
        writer.write(getName());
        writer.write(";");
    }
}