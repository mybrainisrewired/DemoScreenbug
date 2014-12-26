package org.dom4j;

import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class XPathException extends RuntimeException {
    private String xpath;

    public XPathException(String xpath) {
        super("Exception occurred evaluting XPath: " + xpath);
        this.xpath = xpath;
    }

    public XPathException(String xpath, Exception e) {
        super("Exception occurred evaluting XPath: " + xpath + ". Exception: " + e.getMessage());
        this.xpath = xpath;
    }

    public XPathException(String xpath, String reason) {
        super("Exception occurred evaluting XPath: " + xpath + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + reason);
        this.xpath = xpath;
    }

    public String getXPath() {
        return this.xpath;
    }
}