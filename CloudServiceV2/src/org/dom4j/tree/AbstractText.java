package org.dom4j.tree;

import java.io.IOException;
import java.io.Writer;
import org.dom4j.NodeType;
import org.dom4j.Text;
import org.dom4j.Visitor;

public abstract class AbstractText extends AbstractCharacterData implements Text {
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String asXML() {
        return getText();
    }

    public NodeType getNodeTypeEnum() {
        return NodeType.TEXT_NODE;
    }

    public String toString() {
        return super.toString() + " [Text: \"" + getText() + "\"]";
    }

    public void write(Writer writer) throws IOException {
        writer.write(getText());
    }
}