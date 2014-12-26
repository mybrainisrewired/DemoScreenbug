package org.dom4j.swing;

import com.clouds.util.SMSConstant;
import java.io.Serializable;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.XPath;

public class XMLTableColumnDefinition implements Serializable {
    public static final int NODE_TYPE = 3;
    public static final int NUMBER_TYPE = 2;
    public static final int OBJECT_TYPE = 0;
    public static final int STRING_TYPE = 1;
    private XPath columnNameXPath;
    private String name;
    private int type;
    private XPath xpath;

    public XMLTableColumnDefinition(String name, String expression, int type) {
        this.name = name;
        this.type = type;
        this.xpath = createXPath(expression);
    }

    public XMLTableColumnDefinition(String name, XPath xpath, int type) {
        this.name = name;
        this.xpath = xpath;
        this.type = type;
    }

    public XMLTableColumnDefinition(XPath columnXPath, XPath xpath, int type) {
        this.xpath = xpath;
        this.columnNameXPath = columnXPath;
        this.type = type;
    }

    public static int parseType(String typeName) {
        if (typeName != null && typeName.length() > 0 && typeName.equals("string")) {
            return STRING_TYPE;
        }
        if (typeName.equals(SMSConstant.S_number)) {
            return NUMBER_TYPE;
        }
        if (typeName.equals("node")) {
            return NODE_TYPE;
        }
        return OBJECT_TYPE;
    }

    protected XPath createXPath(String expression) {
        return DocumentHelper.createXPath(expression);
    }

    public Class getColumnClass() {
        switch (this.type) {
            case STRING_TYPE:
                return String.class;
            case NUMBER_TYPE:
                return Number.class;
            case NODE_TYPE:
                return Node.class;
            default:
                return Object.class;
        }
    }

    public XPath getColumnNameXPath() {
        return this.columnNameXPath;
    }

    public String getName() {
        return this.name;
    }

    public int getType() {
        return this.type;
    }

    public Object getValue(Object row) {
        switch (this.type) {
            case STRING_TYPE:
                return this.xpath.valueOf(row);
            case NUMBER_TYPE:
                return this.xpath.numberValueOf(row);
            case NODE_TYPE:
                return this.xpath.selectSingleNode(row);
            default:
                return this.xpath.evaluate(row);
        }
    }

    public XPath getXPath() {
        return this.xpath;
    }

    protected void handleException(Exception e) {
        System.out.println("Caught: " + e);
    }

    public void setColumnNameXPath(XPath columnNameXPath) {
        this.columnNameXPath = columnNameXPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setXPath(XPath xPath) {
        this.xpath = xPath;
    }
}