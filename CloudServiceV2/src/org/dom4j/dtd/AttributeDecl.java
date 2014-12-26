package org.dom4j.dtd;

import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class AttributeDecl implements Declaration {
    private String attributeName;
    private String elementName;
    private String type;
    private String value;
    private String valueDefault;

    public AttributeDecl(String elementName, String attributeName, String type, String valueDefault, String value) {
        this.elementName = elementName;
        this.attributeName = attributeName;
        this.type = type;
        this.value = value;
        this.valueDefault = valueDefault;
    }

    public String getAttributeName() {
        return this.attributeName;
    }

    public String getElementName() {
        return this.elementName;
    }

    public String getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

    public String getValueDefault() {
        return this.valueDefault;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setValueDefault(String valueDefault) {
        this.valueDefault = valueDefault;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer("<!ATTLIST ");
        buffer.append(this.elementName);
        buffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        buffer.append(this.attributeName);
        buffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        buffer.append(this.type);
        buffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        if (this.valueDefault != null) {
            buffer.append(this.valueDefault);
            if (this.valueDefault.equals("#FIXED")) {
                buffer.append(" \"");
                buffer.append(this.value);
                buffer.append("\"");
            }
        } else {
            buffer.append("\"");
            buffer.append(this.value);
            buffer.append("\"");
        }
        buffer.append(">");
        return buffer.toString();
    }
}