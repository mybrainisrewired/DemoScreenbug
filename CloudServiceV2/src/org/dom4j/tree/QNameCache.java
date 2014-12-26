package org.dom4j.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.dom4j.DocumentFactory;
import org.dom4j.Namespace;
import org.dom4j.QName;

public class QNameCache {
    private DocumentFactory documentFactory;
    protected Map namespaceCache;
    protected Map noNamespaceCache;

    public QNameCache() {
        this.noNamespaceCache = Collections.synchronizedMap(new WeakHashMap());
        this.namespaceCache = Collections.synchronizedMap(new WeakHashMap());
    }

    public QNameCache(DocumentFactory documentFactory) {
        this.noNamespaceCache = Collections.synchronizedMap(new WeakHashMap());
        this.namespaceCache = Collections.synchronizedMap(new WeakHashMap());
        this.documentFactory = documentFactory;
    }

    protected Map createMap() {
        return Collections.synchronizedMap(new HashMap());
    }

    protected QName createQName(String name) {
        return new QName(name);
    }

    protected QName createQName(String name, Namespace namespace) {
        return new QName(name, namespace);
    }

    protected QName createQName(String name, Namespace namespace, String qualifiedName) {
        return new QName(name, namespace, qualifiedName);
    }

    public QName get(String name) {
        QName answer = null;
        if (name != null) {
            answer = this.noNamespaceCache.get(name);
        } else {
            name = "";
        }
        if (answer != null) {
            return answer;
        }
        answer = createQName(name);
        answer.setDocumentFactory(this.documentFactory);
        this.noNamespaceCache.put(name, answer);
        return answer;
    }

    public QName get(String qualifiedName, String uri) {
        int index = qualifiedName.indexOf(Opcodes.ASTORE);
        return index < 0 ? get(qualifiedName, Namespace.get(uri)) : get(qualifiedName.substring(index + 1), Namespace.get(qualifiedName.substring(0, index), uri));
    }

    public QName get(String name, Namespace namespace) {
        Map cache = getNamespaceCache(namespace);
        QName answer = null;
        if (name != null) {
            answer = cache.get(name);
        } else {
            name = "";
        }
        if (answer != null) {
            return answer;
        }
        answer = createQName(name, namespace);
        answer.setDocumentFactory(this.documentFactory);
        cache.put(name, answer);
        return answer;
    }

    public QName get(String localName, Namespace namespace, String qName) {
        Map cache = getNamespaceCache(namespace);
        QName answer = null;
        if (localName != null) {
            answer = cache.get(localName);
        } else {
            localName = "";
        }
        if (answer != null) {
            return answer;
        }
        answer = createQName(localName, namespace, qName);
        answer.setDocumentFactory(this.documentFactory);
        cache.put(localName, answer);
        return answer;
    }

    protected Map getNamespaceCache(Namespace namespace) {
        if (namespace == Namespace.NO_NAMESPACE) {
            return this.noNamespaceCache;
        }
        Map answer = null;
        if (namespace != null) {
            answer = this.namespaceCache.get(namespace);
        }
        if (answer != null) {
            return answer;
        }
        answer = createMap();
        this.namespaceCache.put(namespace, answer);
        return answer;
    }

    public List getQNames() {
        List answer = new ArrayList();
        answer.addAll(this.noNamespaceCache.values());
        Iterator it = this.namespaceCache.values().iterator();
        while (it.hasNext()) {
            answer.addAll(((Map) it.next()).values());
        }
        return answer;
    }

    public QName intern(QName qname) {
        return get(qname.getName(), qname.getNamespace(), qname.getQualifiedName());
    }
}