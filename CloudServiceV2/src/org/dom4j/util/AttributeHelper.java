package org.dom4j.util;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.QName;

public class AttributeHelper {
    protected AttributeHelper() {
    }

    protected static boolean booleanValue(Attribute attribute) {
        if (attribute == null) {
            return false;
        }
        Object value = attribute.getData();
        if (value != null) {
            return value instanceof Boolean ? ((Boolean) value).booleanValue() : "true".equalsIgnoreCase(value.toString());
        } else {
            return false;
        }
    }

    public static boolean booleanValue(Element element, String attributeName) {
        return booleanValue(element.attribute(attributeName));
    }

    public static boolean booleanValue(Element element, QName attributeQName) {
        return booleanValue(element.attribute(attributeQName));
    }
}