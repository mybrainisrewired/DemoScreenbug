package org.dom4j.io;

import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class OutputFormat implements Cloneable {
    protected static final String STANDARD_INDENT = "  ";
    private char attributeQuoteChar;
    private boolean doXHTML;
    private String encoding;
    private boolean expandEmptyElements;
    private String indent;
    private String lineSeparator;
    private boolean newLineAfterDeclaration;
    private int newLineAfterNTags;
    private boolean newlines;
    private boolean omitEncoding;
    private boolean padText;
    private boolean suppressDeclaration;
    private boolean trimText;

    public OutputFormat() {
        this.suppressDeclaration = false;
        this.newLineAfterDeclaration = true;
        this.encoding = "UTF-8";
        this.omitEncoding = false;
        this.indent = null;
        this.expandEmptyElements = false;
        this.newlines = false;
        this.lineSeparator = "\n";
        this.trimText = false;
        this.padText = false;
        this.doXHTML = false;
        this.newLineAfterNTags = 0;
        this.attributeQuoteChar = '\"';
    }

    public OutputFormat(String indent) {
        this.suppressDeclaration = false;
        this.newLineAfterDeclaration = true;
        this.encoding = "UTF-8";
        this.omitEncoding = false;
        this.indent = null;
        this.expandEmptyElements = false;
        this.newlines = false;
        this.lineSeparator = "\n";
        this.trimText = false;
        this.padText = false;
        this.doXHTML = false;
        this.newLineAfterNTags = 0;
        this.attributeQuoteChar = '\"';
        this.indent = indent;
    }

    public OutputFormat(String indent, boolean newlines) {
        this.suppressDeclaration = false;
        this.newLineAfterDeclaration = true;
        this.encoding = "UTF-8";
        this.omitEncoding = false;
        this.indent = null;
        this.expandEmptyElements = false;
        this.newlines = false;
        this.lineSeparator = "\n";
        this.trimText = false;
        this.padText = false;
        this.doXHTML = false;
        this.newLineAfterNTags = 0;
        this.attributeQuoteChar = '\"';
        this.indent = indent;
        this.newlines = newlines;
    }

    public OutputFormat(String indent, boolean newlines, String encoding) {
        this.suppressDeclaration = false;
        this.newLineAfterDeclaration = true;
        this.encoding = "UTF-8";
        this.omitEncoding = false;
        this.indent = null;
        this.expandEmptyElements = false;
        this.newlines = false;
        this.lineSeparator = "\n";
        this.trimText = false;
        this.padText = false;
        this.doXHTML = false;
        this.newLineAfterNTags = 0;
        this.attributeQuoteChar = '\"';
        this.indent = indent;
        this.newlines = newlines;
        this.encoding = encoding;
    }

    public static OutputFormat createCompactFormat() {
        OutputFormat format = new OutputFormat();
        format.setIndent(false);
        format.setNewlines(false);
        format.setTrimText(true);
        return format;
    }

    public static OutputFormat createPrettyPrint() {
        OutputFormat format = new OutputFormat();
        format.setIndentSize(ClassWriter.COMPUTE_FRAMES);
        format.setNewlines(true);
        format.setTrimText(true);
        format.setPadText(true);
        return format;
    }

    public char getAttributeQuoteCharacter() {
        return this.attributeQuoteChar;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public String getIndent() {
        return this.indent;
    }

    public String getLineSeparator() {
        return this.lineSeparator;
    }

    public int getNewLineAfterNTags() {
        return this.newLineAfterNTags;
    }

    public boolean isExpandEmptyElements() {
        return this.expandEmptyElements;
    }

    public boolean isNewLineAfterDeclaration() {
        return this.newLineAfterDeclaration;
    }

    public boolean isNewlines() {
        return this.newlines;
    }

    public boolean isOmitEncoding() {
        return this.omitEncoding;
    }

    public boolean isPadText() {
        return this.padText;
    }

    public boolean isSuppressDeclaration() {
        return this.suppressDeclaration;
    }

    public boolean isTrimText() {
        return this.trimText;
    }

    public boolean isXHTML() {
        return this.doXHTML;
    }

    public int parseOptions(String[] args, int i) {
        int size = args.length;
        while (i < size) {
            if (args[i].equals("-suppressDeclaration")) {
                setSuppressDeclaration(true);
            } else if (args[i].equals("-omitEncoding")) {
                setOmitEncoding(true);
            } else if (args[i].equals("-indent")) {
                i++;
                setIndent(args[i]);
            } else if (args[i].equals("-indentSize")) {
                i++;
                setIndentSize(Integer.parseInt(args[i]));
            } else if (args[i].startsWith("-expandEmpty")) {
                setExpandEmptyElements(true);
            } else if (args[i].equals("-encoding")) {
                i++;
                setEncoding(args[i]);
            } else if (args[i].equals("-newlines")) {
                setNewlines(true);
            } else if (args[i].equals("-lineSeparator")) {
                i++;
                setLineSeparator(args[i]);
            } else if (args[i].equals("-trimText")) {
                setTrimText(true);
            } else if (args[i].equals("-padText")) {
                setPadText(true);
            } else if (!args[i].startsWith("-xhtml")) {
                return i;
            } else {
                setXHTML(true);
            }
            i++;
        }
        return i;
    }

    public void setAttributeQuoteCharacter(char quoteChar) {
        if (quoteChar == '\'' || quoteChar == '\"') {
            this.attributeQuoteChar = quoteChar;
        } else {
            throw new IllegalArgumentException("Invalid attribute quote character (" + quoteChar + ")");
        }
    }

    public void setEncoding(String encoding) {
        if (encoding != null) {
            this.encoding = encoding;
        }
    }

    public void setExpandEmptyElements(boolean expandEmptyElements) {
        this.expandEmptyElements = expandEmptyElements;
    }

    public void setIndent(String indent) {
        if (indent != null && indent.length() <= 0) {
            indent = null;
        }
        this.indent = indent;
    }

    public void setIndent(boolean doIndent) {
        if (doIndent) {
            this.indent = STANDARD_INDENT;
        } else {
            this.indent = null;
        }
    }

    public void setIndentSize(int indentSize) {
        StringBuffer indentBuffer = new StringBuffer();
        int i = 0;
        while (i < indentSize) {
            indentBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            i++;
        }
        this.indent = indentBuffer.toString();
    }

    public void setLineSeparator(String separator) {
        this.lineSeparator = separator;
    }

    public void setNewLineAfterDeclaration(boolean newLineAfterDeclaration) {
        this.newLineAfterDeclaration = newLineAfterDeclaration;
    }

    public void setNewLineAfterNTags(int tagCount) {
        this.newLineAfterNTags = tagCount;
    }

    public void setNewlines(boolean newlines) {
        this.newlines = newlines;
    }

    public void setOmitEncoding(boolean omitEncoding) {
        this.omitEncoding = omitEncoding;
    }

    public void setPadText(boolean padText) {
        this.padText = padText;
    }

    public void setSuppressDeclaration(boolean suppressDeclaration) {
        this.suppressDeclaration = suppressDeclaration;
    }

    public void setTrimText(boolean trimText) {
        this.trimText = trimText;
    }

    public void setXHTML(boolean xhtml) {
        this.doXHTML = xhtml;
    }
}