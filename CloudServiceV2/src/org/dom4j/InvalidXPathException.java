package org.dom4j;

import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class InvalidXPathException extends IllegalArgumentException {
    public InvalidXPathException(String xpath) {
        super("Invalid XPath expression: " + xpath);
    }

    public InvalidXPathException(String xpath, String reason) {
        super("Invalid XPath expression: " + xpath + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + reason);
    }
}