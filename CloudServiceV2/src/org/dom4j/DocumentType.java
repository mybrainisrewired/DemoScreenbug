package org.dom4j;

import java.util.List;
import org.dom4j.dtd.ExternalDeclaration;
import org.dom4j.dtd.InternalDeclaration;

public interface DocumentType extends Node {
    String getElementName();

    List<ExternalDeclaration> getExternalDeclarations();

    List<InternalDeclaration> getInternalDeclarations();

    String getPublicID();

    String getSystemID();

    void setElementName(String str);

    void setExternalDeclarations(List<ExternalDeclaration> list);

    void setInternalDeclarations(List<InternalDeclaration> list);

    void setPublicID(String str);

    void setSystemID(String str);
}