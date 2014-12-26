package org.dom4j.swing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.jaxen.VariableContext;

public class XMLTableDefinition implements Serializable, VariableContext {
    private XMLTableColumnDefinition[] columnArray;
    private Map columnNameIndex;
    private List columns;
    private Object rowValue;
    private XPath rowXPath;
    private VariableContext variableContext;

    public XMLTableDefinition() {
        this.columns = new ArrayList();
    }

    public static XMLTableDefinition load(Document definition) {
        return load(definition.getRootElement());
    }

    public static XMLTableDefinition load(Element definition) {
        XMLTableDefinition answer = new XMLTableDefinition();
        answer.setRowExpression(definition.attributeValue("select"));
        Iterator iter = definition.elementIterator("column");
        while (iter.hasNext()) {
            Element element = (Element) iter.next();
            String expression = element.attributeValue("select");
            String name = element.getText();
            String typeName = element.attributeValue("type", "string");
            String columnXPath = element.attributeValue("columnNameXPath");
            int type = XMLTableColumnDefinition.parseType(typeName);
            if (columnXPath != null) {
                answer.addColumnWithXPathName(columnXPath, expression, type);
            } else {
                answer.addColumn(name, expression, type);
            }
        }
        return answer;
    }

    public void addColumn(String name, String expression) {
        addColumn(name, expression, 0);
    }

    public void addColumn(String name, String expression, int type) {
        addColumn(new XMLTableColumnDefinition(name, createColumnXPath(expression), type));
    }

    public void addColumn(XMLTableColumnDefinition column) {
        clearCaches();
        this.columns.add(column);
    }

    public void addColumnWithXPathName(String columnNameXPathExpression, String expression, int type) {
        addColumn(new XMLTableColumnDefinition(createColumnXPath(columnNameXPathExpression), createColumnXPath(expression), type));
    }

    public void addNumberColumn(String name, String expression) {
        addColumn(name, expression, ClassWriter.COMPUTE_FRAMES);
    }

    public void addStringColumn(String name, String expression) {
        addColumn(name, expression, 1);
    }

    public void clear() {
        clearCaches();
        this.columns.clear();
    }

    protected void clearCaches() {
        this.columnArray = null;
        this.columnNameIndex = null;
    }

    protected XPath createColumnXPath(String expression) {
        XPath xpath = createXPath(expression);
        xpath.setVariableContext(this);
        return xpath;
    }

    protected XPath createXPath(String expression) {
        return DocumentHelper.createXPath(expression);
    }

    public XMLTableColumnDefinition getColumn(int index) {
        if (this.columnArray == null) {
            this.columnArray = new XMLTableColumnDefinition[this.columns.size()];
            this.columns.toArray(this.columnArray);
        }
        return this.columnArray[index];
    }

    public XMLTableColumnDefinition getColumn(String columnName) {
        if (this.columnNameIndex == null) {
            this.columnNameIndex = new HashMap();
            Iterator it = this.columns.iterator();
            while (it.hasNext()) {
                XMLTableColumnDefinition column = (XMLTableColumnDefinition) it.next();
                this.columnNameIndex.put(column.getName(), column);
            }
        }
        return (XMLTableColumnDefinition) this.columnNameIndex.get(columnName);
    }

    public Class getColumnClass(int columnIndex) {
        return getColumn(columnIndex).getColumnClass();
    }

    public int getColumnCount() {
        return this.columns.size();
    }

    public String getColumnName(int columnIndex) {
        return getColumn(columnIndex).getName();
    }

    public XPath getColumnNameXPath(int columnIndex) {
        return getColumn(columnIndex).getColumnNameXPath();
    }

    public XPath getColumnXPath(int columnIndex) {
        return getColumn(columnIndex).getXPath();
    }

    public XPath getRowXPath() {
        return this.rowXPath;
    }

    public synchronized Object getValueAt(Object row, int columnIndex) {
        Object answer;
        try {
            XMLTableColumnDefinition column = getColumn(columnIndex);
            synchronized (this) {
                this.rowValue = row;
                answer = column.getValue(row);
                this.rowValue = null;
            }
        } catch (Throwable th) {
        }
        return answer;
    }

    public Object getVariableValue(String namespaceURI, String prefix, String localName) {
        XMLTableColumnDefinition column = getColumn(localName);
        return column != null ? column.getValue(this.rowValue) : null;
    }

    protected void handleException(Exception e) {
        System.out.println("Caught: " + e);
    }

    public void removeColumn(XMLTableColumnDefinition column) {
        clearCaches();
        this.columns.remove(column);
    }

    public void setRowExpression(String xpath) {
        setRowXPath(createXPath(xpath));
    }

    public void setRowXPath(XPath rowXPath) {
        this.rowXPath = rowXPath;
    }
}