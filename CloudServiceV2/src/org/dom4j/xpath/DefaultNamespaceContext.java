package org.dom4j.xpath;

import java.io.Serializable;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.jaxen.NamespaceContext;

public class DefaultNamespaceContext implements NamespaceContext, Serializable {
    private final Element element;

    public DefaultNamespaceContext(Element element) {
        this.element = element;
    }

    public static DefaultNamespaceContext create(Element node) {
        Element element = null;
        if (node instanceof Element) {
            element = node;
        } else if (node instanceof Document) {
            element = ((Document) node).getRootElement();
        } else if (node instanceof Node) {
            element = node.getParent();
        }
        return element != null ? new DefaultNamespaceContext(element) : null;
    }

    public String translateNamespacePrefixToUri(String prefix) {
        if (prefix != null && prefix.length() > 0) {
            Namespace ns = this.element.getNamespaceForPrefix(prefix);
            if (ns != null) {
                return ns.getURI();
            }
        }
        return null;
    }
}