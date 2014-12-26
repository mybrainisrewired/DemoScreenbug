package org.dom4j.tree;

import java.io.IOException;
import java.io.Writer;
import org.dom4j.Comment;
import org.dom4j.Element;
import org.dom4j.NodeType;
import org.dom4j.Visitor;

public abstract class AbstractComment extends AbstractCharacterData implements Comment {
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String asXML() {
        return "<!--" + getText() + "-->";
    }

    public NodeType getNodeTypeEnum() {
        return NodeType.COMMENT_NODE;
    }

    public String getPath(Element context) {
        Element parent = getParent();
        return (parent == null || parent == context) ? "comment()" : parent.getPath(context) + "/comment()";
    }

    public String getUniquePath(Element context) {
        Element parent = getParent();
        return (parent == null || parent == context) ? "comment()" : parent.getUniquePath(context) + "/comment()";
    }

    protected void toString(StringBuilder builder) {
        super.toString(builder);
        builder.append(" [Comment: \"");
        builder.append(getText());
        builder.append("\"]");
    }

    public void write(Writer writer) throws IOException {
        writer.write("<!--");
        writer.write(getText());
        writer.write("-->");
    }
}