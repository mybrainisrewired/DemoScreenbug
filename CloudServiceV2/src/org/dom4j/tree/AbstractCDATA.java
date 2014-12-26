package org.dom4j.tree;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import org.dom4j.CDATA;
import org.dom4j.NodeType;
import org.dom4j.Visitor;

public abstract class AbstractCDATA extends AbstractCharacterData implements CDATA {
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String asXML() {
        StringWriter writer = new StringWriter();
        try {
            write(writer);
        } catch (IOException e) {
        }
        return writer.toString();
    }

    public NodeType getNodeTypeEnum() {
        return NodeType.CDATA_SECTION_NODE;
    }

    protected void toString(StringBuilder builder) {
        super.toString(builder);
        builder.append(" [CDATA: \"");
        builder.append(getText());
        builder.append("\"]");
    }

    public void write(Writer writer) throws IOException {
        writer.write("<![CDATA[");
        if (getText() != null) {
            writer.write(getText());
        }
        writer.write("]]>");
    }
}