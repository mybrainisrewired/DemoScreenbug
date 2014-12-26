package org.dom4j.datatype;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class DatatypeDocumentFactory extends DocumentFactory {
    private static final boolean DO_INTERN_QNAME = false;
    private static final Namespace XSI_NAMESPACE;
    private static final QName XSI_NO_SCHEMA_LOCATION;
    private static final QName XSI_SCHEMA_LOCATION;
    protected static transient DatatypeDocumentFactory singleton;
    private boolean autoLoadSchema;
    private SchemaParser schemaBuilder;
    private SAXReader xmlSchemaReader;

    static {
        singleton = new DatatypeDocumentFactory();
        XSI_NAMESPACE = Namespace.get("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        XSI_SCHEMA_LOCATION = QName.get("schemaLocation", XSI_NAMESPACE);
        XSI_NO_SCHEMA_LOCATION = QName.get("noNamespaceSchemaLocation", XSI_NAMESPACE);
    }

    public DatatypeDocumentFactory() {
        this.xmlSchemaReader = new SAXReader();
        this.autoLoadSchema = true;
        this.schemaBuilder = new SchemaParser(this);
    }

    public static DocumentFactory getInstance() {
        return singleton;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.dom4j.Attribute createAttribute(org.dom4j.Element r7_owner, org.dom4j.QName r8_qname, java.lang.String r9_value) {
        throw new UnsupportedOperationException("Method not decompiled: org.dom4j.datatype.DatatypeDocumentFactory.createAttribute(org.dom4j.Element, org.dom4j.QName, java.lang.String):org.dom4j.Attribute");
        /*
        r6 = this;
        r0 = 0;
        r5 = 32;
        r3 = r6.autoLoadSchema;
        if (r3 == 0) goto L_0x001d;
    L_0x0007:
        r3 = XSI_NO_SCHEMA_LOCATION;
        r3 = r8.equals(r3);
        if (r3 == 0) goto L_0x001d;
    L_0x000f:
        if (r7 == 0) goto L_0x0015;
    L_0x0011:
        r0 = r7.getDocument();
    L_0x0015:
        r6.loadSchema(r0, r9);
    L_0x0018:
        r3 = super.createAttribute(r7, r8, r9);
        return r3;
    L_0x001d:
        r3 = r6.autoLoadSchema;
        if (r3 == 0) goto L_0x0018;
    L_0x0021:
        r3 = XSI_SCHEMA_LOCATION;
        r3 = r8.equals(r3);
        if (r3 == 0) goto L_0x0018;
    L_0x0029:
        if (r7 == 0) goto L_0x002f;
    L_0x002b:
        r0 = r7.getDocument();
    L_0x002f:
        r3 = 0;
        r4 = r9.indexOf(r5);
        r2 = r9.substring(r3, r4);
        r1 = r7.getNamespaceForURI(r2);
        r3 = r9.indexOf(r5);
        r3 = r3 + 1;
        r3 = r9.substring(r3);
        r6.loadSchema(r0, r3, r1);
        goto L_0x0018;
        */
    }

    public DatatypeElementFactory getElementFactory(QName elementQName) {
        DocumentFactory factory = elementQName.getDocumentFactory();
        return factory instanceof DatatypeElementFactory ? (DatatypeElementFactory) factory : null;
    }

    public void loadSchema(Document schemaDocument) {
        this.schemaBuilder.build(schemaDocument);
    }

    protected void loadSchema(Document document, String schemaInstanceURI) {
        try {
            EntityResolver resolver = document.getEntityResolver();
            if (resolver == null) {
                throw new InvalidSchemaException("No EntityResolver available for resolving URI: " + schemaInstanceURI);
            } else {
                InputSource inputSource = resolver.resolveEntity(null, schemaInstanceURI);
                if (resolver == null) {
                    throw new InvalidSchemaException("Could not resolve the URI: " + schemaInstanceURI);
                }
                loadSchema(this.xmlSchemaReader.read(inputSource));
            }
        } catch (Exception e) {
            Exception e2 = e;
            System.out.println("Failed to load schema: " + schemaInstanceURI);
            System.out.println("Caught: " + e2);
            e2.printStackTrace();
            throw new InvalidSchemaException("Failed to load schema: " + schemaInstanceURI);
        }
    }

    protected void loadSchema(Document document, String schemaInstanceURI, Namespace namespace) {
        try {
            EntityResolver resolver = document.getEntityResolver();
            if (resolver == null) {
                throw new InvalidSchemaException("No EntityResolver available for resolving URI: " + schemaInstanceURI);
            } else {
                InputSource inputSource = resolver.resolveEntity(null, schemaInstanceURI);
                if (resolver == null) {
                    throw new InvalidSchemaException("Could not resolve the URI: " + schemaInstanceURI);
                }
                loadSchema(this.xmlSchemaReader.read(inputSource), namespace);
            }
        } catch (Exception e) {
            Exception e2 = e;
            System.out.println("Failed to load schema: " + schemaInstanceURI);
            System.out.println("Caught: " + e2);
            e2.printStackTrace();
            throw new InvalidSchemaException("Failed to load schema: " + schemaInstanceURI);
        }
    }

    public void loadSchema(Document schemaDocument, Namespace targetNamespace) {
        this.schemaBuilder.build(schemaDocument, targetNamespace);
    }
}