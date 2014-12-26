package org.dom4j.tree;

import java.util.List;
import org.dom4j.dtd.ExternalDeclaration;
import org.dom4j.dtd.InternalDeclaration;

public class DefaultDocumentType extends AbstractDocumentType {
    protected String elementName;
    private List<ExternalDeclaration> externalDeclarations;
    private List<InternalDeclaration> internalDeclarations;
    private String publicID;
    private String systemID;

    public DefaultDocumentType(String elementName, String systemID) {
        this.elementName = elementName;
        this.systemID = systemID;
    }

    public DefaultDocumentType(String elementName, String publicID, String systemID) {
        this.elementName = elementName;
        this.publicID = publicID;
        this.systemID = systemID;
    }

    public String getElementName() {
        return this.elementName;
    }

    public List<ExternalDeclaration> getExternalDeclarations() {
        return this.externalDeclarations;
    }

    public List<InternalDeclaration> getInternalDeclarations() {
        return this.internalDeclarations;
    }

    public String getPublicID() {
        return this.publicID;
    }

    public String getSystemID() {
        return this.systemID;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public void setExternalDeclarations(List<ExternalDeclaration> externalDeclarations) {
        this.externalDeclarations = externalDeclarations;
    }

    public void setInternalDeclarations(List<InternalDeclaration> internalDeclarations) {
        this.internalDeclarations = internalDeclarations;
    }

    public void setPublicID(String publicID) {
        this.publicID = publicID;
    }

    public void setSystemID(String systemID) {
        this.systemID = systemID;
    }
}