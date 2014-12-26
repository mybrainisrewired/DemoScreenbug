package org.dom4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.dom4j.tree.QNameCache;
import org.dom4j.util.SingletonStrategy;

public class QName implements Serializable {
    private static SingletonStrategy singleton;
    private DocumentFactory documentFactory;
    private int hashCode;
    private String name;
    private transient Namespace namespace;
    private String qualifiedName;

    static {
        singleton = null;
        String defaultSingletonClass = "org.dom4j.util.SimpleSingleton";
        Class clazz = null;
        try {
            clazz = Class.forName(System.getProperty("org.dom4j.QName.singleton.strategy", defaultSingletonClass));
        } catch (Exception e) {
            try {
                clazz = Class.forName(defaultSingletonClass);
            } catch (Exception e2) {
            }
        }
        singleton = (SingletonStrategy) clazz.newInstance();
        singleton.setSingletonClassName(QNameCache.class.getName());
    }

    public QName(String name) {
        this(name, Namespace.NO_NAMESPACE);
    }

    public QName(String name, Namespace namespace) {
        if (name == null) {
            name = "";
        }
        this.name = name;
        if (namespace == null) {
            namespace = Namespace.NO_NAMESPACE;
        }
        this.namespace = namespace;
    }

    public QName(String name, Namespace namespace, String qualifiedName) {
        if (name == null) {
            name = "";
        }
        this.name = name;
        this.qualifiedName = qualifiedName;
        if (namespace == null) {
            namespace = Namespace.NO_NAMESPACE;
        }
        this.namespace = namespace;
    }

    public static QName get(String name) {
        return getCache().get(name);
    }

    public static QName get(String qualifiedName, String uri) {
        return uri == null ? getCache().get(qualifiedName) : getCache().get(qualifiedName, uri);
    }

    public static QName get(String name, String prefix, String uri) {
        if ((prefix == null || prefix.length() == 0) && uri == null) {
            return get(name);
        }
        if (prefix == null || prefix.length() == 0) {
            return getCache().get(name, Namespace.get(uri));
        }
        return uri == null ? get(name) : getCache().get(name, Namespace.get(prefix, uri));
    }

    public static QName get(String name, Namespace namespace) {
        return getCache().get(name, namespace);
    }

    public static QName get(String localName, Namespace namespace, String qualifiedName) {
        return getCache().get(localName, namespace, qualifiedName);
    }

    private static QNameCache getCache() {
        return (QNameCache) singleton.instance();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String prefix = (String) in.readObject();
        String uri = (String) in.readObject();
        in.defaultReadObject();
        this.namespace = Namespace.get(prefix, uri);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(this.namespace.getPrefix());
        out.writeObject(this.namespace.getURI());
        out.defaultWriteObject();
    }

    public boolean equals(QName object) {
        if (this == object) {
            return true;
        }
        if (object instanceof QName) {
            QName that = object;
            if (hashCode() == that.hashCode()) {
                return getName().equals(that.getName()) && getNamespaceURI().equals(that.getNamespaceURI());
            }
        }
        return false;
    }

    public DocumentFactory getDocumentFactory() {
        return this.documentFactory;
    }

    public String getName() {
        return this.name;
    }

    public Namespace getNamespace() {
        return this.namespace;
    }

    public String getNamespacePrefix() {
        return this.namespace == null ? "" : this.namespace.getPrefix();
    }

    public String getNamespaceURI() {
        return this.namespace == null ? "" : this.namespace.getURI();
    }

    public String getQualifiedName() {
        if (this.qualifiedName == null) {
            String prefix = getNamespacePrefix();
            if (prefix == null || prefix.length() <= 0) {
                this.qualifiedName = this.name;
            } else {
                this.qualifiedName = prefix + ":" + this.name;
            }
        }
        return this.qualifiedName;
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = getName().hashCode() ^ getNamespaceURI().hashCode();
            if (this.hashCode == 0) {
                this.hashCode = 47806;
            }
        }
        return this.hashCode;
    }

    public void setDocumentFactory(DocumentFactory documentFactory) {
        this.documentFactory = documentFactory;
    }

    public String toString() {
        return super.toString() + " [name: " + getName() + " namespace: \"" + getNamespace() + "\"]";
    }
}