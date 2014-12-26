package org.dom4j.tree;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.NodeType;
import org.dom4j.Visitor;
import org.dom4j.dtd.InternalDeclaration;

public abstract class AbstractDocumentType extends AbstractNode implements DocumentType {
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String asXML() {
        StringBuilder builder = new StringBuilder();
        asXML(builder);
        return builder.toString();
    }

    protected void asXML(StringBuilder builder) {
        builder.append("<!DOCTYPE ");
        builder.append(getElementName());
        boolean hasPublicID = false;
        String publicID = getPublicID();
        if (publicID != null && publicID.length() > 0) {
            builder.append(" PUBLIC ");
            builder.append('\"');
            builder.append(publicID);
            builder.append('\"');
            hasPublicID = true;
        }
        String systemID = getSystemID();
        if (systemID != null && systemID.length() > 0) {
            if (!hasPublicID) {
                builder.append(" SYSTEM");
            }
            builder.append(" \"");
            builder.append(systemID);
            builder.append('\"');
        }
        builder.append('>');
    }

    public String getName() {
        return getElementName();
    }

    public NodeType getNodeTypeEnum() {
        return NodeType.DOCUMENT_TYPE_NODE;
    }

    public String getPath(Element context) {
        return "";
    }

    public String getText() {
        List<InternalDeclaration> list = getInternalDeclarations();
        if (list == null || list.size() <= 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        Iterator i$ = list.iterator();
        while (i$.hasNext()) {
            builder.append(((InternalDeclaration) i$.next()).toString());
            builder.append('\n');
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }

    public String getUniquePath(Element context) {
        return "";
    }

    public void setName(String name) {
        setElementName(name);
    }

    protected void toString(StringBuilder builder) {
        super.toString(builder);
        builder.append(" [DocumentType: ");
        asXML(builder);
        builder.append(']');
    }

    public void write(Writer writer) throws IOException {
        writer.write("<!DOCTYPE ");
        writer.write(getElementName());
        boolean hasPublicID = false;
        String publicID = getPublicID();
        if (publicID != null && publicID.length() > 0) {
            writer.write(" PUBLIC \"");
            writer.write(publicID);
            writer.write(34);
            hasPublicID = true;
        }
        String systemID = getSystemID();
        if (systemID != null && systemID.length() > 0) {
            if (!hasPublicID) {
                writer.write(" SYSTEM");
            }
            writer.write(" \"");
            writer.write(systemID);
            writer.write(34);
        }
        List<InternalDeclaration> list = getInternalDeclarations();
        if (list != null && list.size() > 0) {
            writer.write(" [");
            Iterator<InternalDeclaration> iter = list.iterator();
            while (iter.hasNext()) {
                InternalDeclaration decl = (InternalDeclaration) iter.next();
                writer.write("\n  ");
                writer.write(decl.toString());
            }
            writer.write("\n]");
        }
        writer.write(62);
    }
}