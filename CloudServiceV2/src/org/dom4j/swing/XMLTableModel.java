package org.dom4j.swing;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;

public class XMLTableModel extends AbstractTableModel {
    private XMLTableDefinition definition;
    private List rows;
    private Object source;

    public XMLTableModel(Document tableDefinition, Object source) {
        this(XMLTableDefinition.load(tableDefinition), source);
    }

    public XMLTableModel(Element tableDefinition, Object source) {
        this(XMLTableDefinition.load(tableDefinition), source);
    }

    public XMLTableModel(XMLTableDefinition definition, Object source) {
        this.definition = definition;
        this.source = source;
    }

    public Class getColumnClass(int columnIndex) {
        return this.definition.getColumnClass(columnIndex);
    }

    public int getColumnCount() {
        return this.definition.getColumnCount();
    }

    public String getColumnName(int columnIndex) {
        XPath xpath = this.definition.getColumnNameXPath(columnIndex);
        if (xpath == null) {
            return this.definition.getColumnName(columnIndex);
        }
        System.out.println("Evaluating column xpath: " + xpath + " value: " + xpath.valueOf(this.source));
        return xpath.valueOf(this.source);
    }

    public XMLTableDefinition getDefinition() {
        return this.definition;
    }

    public int getRowCount() {
        return getRows().size();
    }

    public Object getRowValue(int rowIndex) {
        return getRows().get(rowIndex);
    }

    public List getRows() {
        if (this.rows == null) {
            this.rows = this.definition.getRowXPath().selectNodes(this.source);
        }
        return this.rows;
    }

    public Object getSource() {
        return this.source;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            return this.definition.getValueAt(getRowValue(rowIndex), columnIndex);
        } catch (Exception e) {
            handleException(e);
            return null;
        }
    }

    protected void handleException(Exception e) {
        System.out.println("Caught: " + e);
    }

    public void setDefinition(XMLTableDefinition definition) {
        this.definition = definition;
    }

    public void setSource(Object source) {
        this.source = source;
        this.rows = null;
    }
}