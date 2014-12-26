package org.dom4j.tree;

import java.io.IOException;
import java.io.Writer;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.NodeType;
import org.dom4j.Visitor;

public abstract class AbstractAttribute extends AbstractNode implements Attribute {
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String asXML() {
        return getQualifiedName() + "=\"" + getValue() + "\"";
    }

    protected Node createXPathResult(Element parent) {
        return new DefaultAttribute(parent, getQName(), getValue());
    }

    public Object getData() {
        return getValue();
    }

    public String getName() {
        return getQName().getName();
    }

    public Namespace getNamespace() {
        return getQName().getNamespace();
    }

    public String getNamespacePrefix() {
        return getQName().getNamespacePrefix();
    }

    public String getNamespaceURI() {
        return getQName().getNamespaceURI();
    }

    public NodeType getNodeTypeEnum() {
        return NodeType.ATTRIBUTE_NODE;
    }

    public String getPath(Element context) {
        StringBuffer result = new StringBuffer();
        Element parent = getParent();
        if (!(parent == null || parent == context)) {
            result.append(parent.getPath(context));
            result.append("/");
        }
        result.append("@");
        String uri = getNamespaceURI();
        String prefix = getNamespacePrefix();
        if (uri == null || uri.length() == 0 || prefix == null || prefix.length() == 0) {
            result.append(getName());
        } else {
            result.append(getQualifiedName());
        }
        return result.toString();
    }

    public String getQualifiedName() {
        return getQName().getQualifiedName();
    }

    public String getText() {
        return getValue();
    }

    public String getUniquePath(Element context) {
        StringBuffer result = new StringBuffer();
        Element parent = getParent();
        if (!(parent == null || parent == context)) {
            result.append(parent.getUniquePath(context));
            result.append("/");
        }
        result.append("@");
        String uri = getNamespaceURI();
        String prefix = getNamespacePrefix();
        if (uri == null || uri.length() == 0 || prefix == null || prefix.length() == 0) {
            result.append(getName());
        } else {
            result.append(getQualifiedName());
        }
        return result.toString();
    }

    public void setData(Object data) {
        setValue(data == null ? null : data.toString());
    }

    public void setNamespace(Namespace namespace) {
        throw new UnsupportedOperationException("This Attribute is read only and cannot be changed");
    }

    public void setText(String text) {
        setValue(text);
    }

    public void setValue(String value) {
        throw new UnsupportedOperationException("This Attribute is read only and cannot be changed");
    }

    protected void toString(StringBuilder builder) {
        super.toString(builder);
        builder.append(" [Attribute: name ");
        builder.append(getQualifiedName());
        builder.append(" value \"");
        builder.append(getValue());
        builder.append("\"]");
    }

    public void write(Writer writer) throws IOException {
        writer.write(getQualifiedName());
        writer.write("=\"");
        writer.write(getValue());
        writer.write("\"");
    }
}