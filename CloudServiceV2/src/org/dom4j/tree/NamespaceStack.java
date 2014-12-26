package org.dom4j.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.dom4j.DocumentFactory;
import org.dom4j.Namespace;
import org.dom4j.QName;

public class NamespaceStack {
    private Map currentNamespaceCache;
    private Namespace defaultNamespace;
    private DocumentFactory documentFactory;
    private ArrayList namespaceCacheList;
    private ArrayList namespaceStack;
    private Map rootNamespaceCache;

    public NamespaceStack() {
        this.namespaceStack = new ArrayList();
        this.namespaceCacheList = new ArrayList();
        this.rootNamespaceCache = new HashMap();
        this.documentFactory = DocumentFactory.getInstance();
    }

    public NamespaceStack(DocumentFactory documentFactory) {
        this.namespaceStack = new ArrayList();
        this.namespaceCacheList = new ArrayList();
        this.rootNamespaceCache = new HashMap();
        this.documentFactory = documentFactory;
    }

    public Namespace addNamespace(String prefix, String uri) {
        Namespace namespace = createNamespace(prefix, uri);
        push(namespace);
        return namespace;
    }

    public void clear() {
        this.namespaceStack.clear();
        this.namespaceCacheList.clear();
        this.rootNamespaceCache.clear();
        this.currentNamespaceCache = null;
    }

    public boolean contains(Namespace namespace) {
        Namespace current;
        String prefix = namespace.getPrefix();
        if (prefix == null || prefix.length() == 0) {
            current = getDefaultNamespace();
        } else {
            current = getNamespaceForPrefix(prefix);
        }
        if (current == null) {
            return false;
        }
        return current == namespace ? true : namespace.getURI().equals(current.getURI());
    }

    protected Namespace createNamespace(String prefix, String namespaceURI) {
        return this.documentFactory.createNamespace(prefix, namespaceURI);
    }

    protected QName createQName(String localName, String qualifiedName, Namespace namespace) {
        return this.documentFactory.createQName(localName, namespace);
    }

    protected Namespace findDefaultNamespace() {
        int i = this.namespaceStack.size() - 1;
        while (i >= 0) {
            Namespace namespace = (Namespace) this.namespaceStack.get(i);
            if (namespace != null && (namespace.getPrefix() == null || namespace.getPrefix().length() == 0)) {
                return namespace;
            }
            i--;
        }
        return null;
    }

    public QName getAttributeQName(String namespaceURI, String localName, String qualifiedName) {
        if (qualifiedName == null) {
            qualifiedName = localName;
        }
        Map map = getNamespaceCache();
        QName answer = (QName) map.get(qualifiedName);
        if (answer != null) {
            return answer;
        }
        Namespace namespace;
        if (localName == null) {
            localName = qualifiedName;
        }
        if (namespaceURI == null) {
            namespaceURI = "";
        }
        String prefix = "";
        int index = qualifiedName.indexOf(":");
        if (index > 0) {
            prefix = qualifiedName.substring(0, index);
            namespace = createNamespace(prefix, namespaceURI);
            if (localName.trim().length() == 0) {
                localName = qualifiedName.substring(index + 1);
            }
        } else {
            namespace = Namespace.NO_NAMESPACE;
            if (localName.trim().length() == 0) {
                localName = qualifiedName;
            }
        }
        answer = pushQName(localName, qualifiedName, namespace, prefix);
        map.put(qualifiedName, answer);
        return answer;
    }

    public Namespace getDefaultNamespace() {
        if (this.defaultNamespace == null) {
            this.defaultNamespace = findDefaultNamespace();
        }
        return this.defaultNamespace;
    }

    public DocumentFactory getDocumentFactory() {
        return this.documentFactory;
    }

    public Namespace getNamespace(int index) {
        return (Namespace) this.namespaceStack.get(index);
    }

    protected Map getNamespaceCache() {
        if (this.currentNamespaceCache == null) {
            int index = this.namespaceStack.size() - 1;
            if (index < 0) {
                this.currentNamespaceCache = this.rootNamespaceCache;
            } else {
                this.currentNamespaceCache = (Map) this.namespaceCacheList.get(index);
                if (this.currentNamespaceCache == null) {
                    this.currentNamespaceCache = new HashMap();
                    this.namespaceCacheList.set(index, this.currentNamespaceCache);
                }
            }
        }
        return this.currentNamespaceCache;
    }

    public Namespace getNamespaceForPrefix(String prefix) {
        if (prefix == null) {
            prefix = "";
        }
        int i = this.namespaceStack.size() - 1;
        while (i >= 0) {
            Namespace namespace = (Namespace) this.namespaceStack.get(i);
            if (prefix.equals(namespace.getPrefix())) {
                return namespace;
            }
            i--;
        }
        return null;
    }

    public QName getQName(String namespaceURI, String localName, String qualifiedName) {
        if (localName == null) {
            localName = qualifiedName;
        } else if (qualifiedName == null) {
            qualifiedName = localName;
        }
        if (namespaceURI == null) {
            namespaceURI = "";
        }
        String prefix = "";
        int index = qualifiedName.indexOf(":");
        if (index > 0) {
            prefix = qualifiedName.substring(0, index);
            if (localName.trim().length() == 0) {
                localName = qualifiedName.substring(index + 1);
            }
        } else if (localName.trim().length() == 0) {
            localName = qualifiedName;
        }
        return pushQName(localName, qualifiedName, createNamespace(prefix, namespaceURI), prefix);
    }

    public String getURI(String prefix) {
        Namespace namespace = getNamespaceForPrefix(prefix);
        return namespace != null ? namespace.getURI() : null;
    }

    public Namespace pop() {
        return remove(this.namespaceStack.size() - 1);
    }

    public Namespace pop(String prefix) {
        if (prefix == null) {
            prefix = "";
        }
        Namespace namespace = null;
        int i = this.namespaceStack.size() - 1;
        while (i >= 0) {
            Namespace ns = (Namespace) this.namespaceStack.get(i);
            if (prefix.equals(ns.getPrefix())) {
                remove(i);
                namespace = ns;
                break;
            } else {
                i--;
            }
        }
        if (namespace == null) {
            System.out.println("Warning: missing namespace prefix ignored: " + prefix);
        }
        return namespace;
    }

    public void push(String prefix, String uri) {
        if (uri == null) {
            uri = "";
        }
        push(createNamespace(prefix, uri));
    }

    public void push(Namespace namespace) {
        this.namespaceStack.add(namespace);
        this.namespaceCacheList.add(null);
        this.currentNamespaceCache = null;
        String prefix = namespace.getPrefix();
        if (prefix == null || prefix.length() == 0) {
            this.defaultNamespace = namespace;
        }
    }

    protected QName pushQName(String localName, String qualifiedName, Namespace namespace, String prefix) {
        if (prefix == null || prefix.length() == 0) {
            this.defaultNamespace = null;
        }
        return createQName(localName, qualifiedName, namespace);
    }

    protected Namespace remove(int index) {
        Namespace namespace = (Namespace) this.namespaceStack.remove(index);
        this.namespaceCacheList.remove(index);
        this.defaultNamespace = null;
        this.currentNamespaceCache = null;
        return namespace;
    }

    public void setDocumentFactory(DocumentFactory documentFactory) {
        this.documentFactory = documentFactory;
    }

    public int size() {
        return this.namespaceStack.size();
    }

    public String toString() {
        return super.toString() + " Stack: " + this.namespaceStack.toString();
    }
}