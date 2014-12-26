package org.dom4j.dom;

import java.util.ArrayList;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.tree.DefaultDocument;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

public class DOMDocument extends DefaultDocument implements Document {
    private static final DOMDocumentFactory DOCUMENT_FACTORY;

    static {
        DOCUMENT_FACTORY = (DOMDocumentFactory) DOMDocumentFactory.getInstance();
    }

    public DOMDocument() {
        init();
    }

    public DOMDocument(String name) {
        super(name);
        init();
    }

    public DOMDocument(String name, DOMElement rootElement, DOMDocumentType docType) {
        super(name, rootElement, docType);
        init();
    }

    public DOMDocument(DocumentType docType) {
        super(docType);
        init();
    }

    public DOMDocument(Element rootElement) {
        super(rootElement);
        init();
    }

    public DOMDocument(DOMElement rootElement, DOMDocumentType docType) {
        super(rootElement, docType);
        init();
    }

    private void checkNewChildNode(Node newChild) throws DOMException {
        int nodeType = newChild.getNodeType();
        if (nodeType != 1 && nodeType != 8 && nodeType != 7 && nodeType != 10) {
            throw new DOMException((short) 3, "Given node cannot be a child of document");
        }
    }

    private void init() {
        setDocumentFactory(DOCUMENT_FACTORY);
    }

    public Node adoptNode(Node source) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node appendChild(Node newChild) throws DOMException {
        checkNewChildNode(newChild);
        return DOMNodeHelper.appendChild(this, newChild);
    }

    public Node cloneNode(boolean deep) {
        return DOMNodeHelper.cloneNode(this, deep);
    }

    public short compareDocumentPosition(Node other) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Attr createAttribute(String name) throws DOMException {
        return (Attr) getDocumentFactory().createAttribute(null, getDocumentFactory().createQName(name), "");
    }

    public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
        return (Attr) getDocumentFactory().createAttribute(null, getDocumentFactory().createQName(qualifiedName, namespaceURI), null);
    }

    public CDATASection createCDATASection(String data) throws DOMException {
        return (CDATASection) getDocumentFactory().createCDATA(data);
    }

    public Comment createComment(String data) {
        return (Comment) getDocumentFactory().createComment(data);
    }

    public DocumentFragment createDocumentFragment() {
        DOMNodeHelper.notSupported();
        return null;
    }

    public org.w3c.dom.Element createElement(String name) throws DOMException {
        return (org.w3c.dom.Element) getDocumentFactory().createElement(name);
    }

    public org.w3c.dom.Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
        return (org.w3c.dom.Element) getDocumentFactory().createElement(getDocumentFactory().createQName(qualifiedName, namespaceURI));
    }

    public EntityReference createEntityReference(String name) throws DOMException {
        return (EntityReference) getDocumentFactory().createEntity(name, null);
    }

    public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
        return (ProcessingInstruction) getDocumentFactory().createProcessingInstruction(target, data);
    }

    public Text createTextNode(String data) {
        return (Text) getDocumentFactory().createText(data);
    }

    public NamedNodeMap getAttributes() {
        return null;
    }

    public String getBaseURI() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public NodeList getChildNodes() {
        return DOMNodeHelper.createNodeList(content());
    }

    public org.w3c.dom.DocumentType getDoctype() {
        return DOMNodeHelper.asDOMDocumentType(getDocType());
    }

    public org.w3c.dom.Element getDocumentElement() {
        return DOMNodeHelper.asDOMElement(getRootElement());
    }

    protected DocumentFactory getDocumentFactory() {
        return super.getDocumentFactory() == null ? DOCUMENT_FACTORY : super.getDocumentFactory();
    }

    public String getDocumentURI() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public DOMConfiguration getDomConfig() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public org.w3c.dom.Element getElementById(String elementId) {
        return DOMNodeHelper.asDOMElement(elementByID(elementId));
    }

    public NodeList getElementsByTagName(String name) {
        ArrayList list = new ArrayList();
        DOMNodeHelper.appendElementsByTagName(list, this, name);
        return DOMNodeHelper.createNodeList(list);
    }

    public NodeList getElementsByTagNameNS(String namespace, String name) {
        ArrayList list = new ArrayList();
        DOMNodeHelper.appendElementsByTagNameNS(list, this, namespace, name);
        return DOMNodeHelper.createNodeList(list);
    }

    public Object getFeature(String feature, String version) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node getFirstChild() {
        return DOMNodeHelper.asDOMNode(node(0));
    }

    public DOMImplementation getImplementation() {
        return getDocumentFactory() instanceof DOMImplementation ? (DOMImplementation) getDocumentFactory() : DOCUMENT_FACTORY;
    }

    public String getInputEncoding() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node getLastChild() {
        return DOMNodeHelper.asDOMNode(node(nodeCount() - 1));
    }

    public String getLocalName() {
        return DOMNodeHelper.getLocalName(this);
    }

    public String getNamespaceURI() {
        return DOMNodeHelper.getNamespaceURI(this);
    }

    public Node getNextSibling() {
        return DOMNodeHelper.getNextSibling(this);
    }

    public String getNodeName() {
        return "#document";
    }

    public String getNodeValue() throws DOMException {
        return null;
    }

    public Document getOwnerDocument() {
        return null;
    }

    public Node getParentNode() {
        return DOMNodeHelper.getParentNode(this);
    }

    public String getPrefix() {
        return DOMNodeHelper.getPrefix(this);
    }

    public Node getPreviousSibling() {
        return DOMNodeHelper.getPreviousSibling(this);
    }

    public boolean getStrictErrorChecking() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getTextContent() throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getUserData(String key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getXmlEncoding() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean getXmlStandalone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getXmlVersion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasAttributes() {
        return DOMNodeHelper.hasAttributes(this);
    }

    public boolean hasChildNodes() {
        return nodeCount() > 0;
    }

    public Node importNode(Node importedNode, boolean deep) throws DOMException {
        DOMNodeHelper.notSupported();
        return null;
    }

    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        checkNewChildNode(newChild);
        return DOMNodeHelper.insertBefore(this, newChild, refChild);
    }

    public boolean isDefaultNamespace(String namespaceURI) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isEqualNode(Node arg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isSameNode(Node other) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isSupported(String feature, String version) {
        return DOMNodeHelper.isSupported(this, feature, version);
    }

    public String lookupNamespaceURI(String prefix) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String lookupPrefix(String namespaceURI) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void normalizeDocument() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node removeChild(Node oldChild) throws DOMException {
        return DOMNodeHelper.removeChild(this, oldChild);
    }

    public Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        checkNewChildNode(newChild);
        return DOMNodeHelper.replaceChild(this, newChild, oldChild);
    }

    public void setDocumentURI(String documentURI) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setNodeValue(String nodeValue) throws DOMException {
    }

    public void setPrefix(String prefix) throws DOMException {
        DOMNodeHelper.setPrefix(this, prefix);
    }

    public void setStrictErrorChecking(boolean strictErrorChecking) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setTextContent(String textContent) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object setUserData(String key, Object data, UserDataHandler handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setXmlVersion(String xmlVersion) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean supports(String feature, String version) {
        return DOMNodeHelper.supports(this, feature, version);
    }
}