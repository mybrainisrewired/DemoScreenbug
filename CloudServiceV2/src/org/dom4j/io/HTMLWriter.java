package org.dom4j.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.org.objectweb.asm.Type;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.NodeType;
import org.xml.sax.SAXException;

public class HTMLWriter extends XMLWriter {
    protected static final OutputFormat DEFAULT_HTML_FORMAT;
    protected static final HashSet<String> DEFAULT_PREFORMATTED_TAGS;
    private static String lineSeparator;
    private LinkedList<FormatState> formatStack;
    private String lastText;
    private int newLineAfterNTags;
    private HashSet<String> omitElementCloseSet;
    private HashSet<String> preformattedTags;
    private int tagsOuput;

    private class FormatState {
        private String indent;
        private boolean newlines;
        private boolean trimText;

        public FormatState(boolean newLines, boolean trimText, String indent) {
            this.newlines = false;
            this.trimText = false;
            this.indent = "";
            this.newlines = newLines;
            this.trimText = trimText;
            this.indent = indent;
        }

        public String getIndent() {
            return this.indent;
        }

        public boolean isNewlines() {
            return this.newlines;
        }

        public boolean isTrimText() {
            return this.trimText;
        }
    }

    static {
        lineSeparator = System.getProperty("line.separator");
        DEFAULT_PREFORMATTED_TAGS = new HashSet();
        DEFAULT_PREFORMATTED_TAGS.add("PRE");
        DEFAULT_PREFORMATTED_TAGS.add("SCRIPT");
        DEFAULT_PREFORMATTED_TAGS.add("STYLE");
        DEFAULT_PREFORMATTED_TAGS.add("TEXTAREA");
        DEFAULT_HTML_FORMAT = new OutputFormat("  ", true);
        DEFAULT_HTML_FORMAT.setTrimText(true);
        DEFAULT_HTML_FORMAT.setSuppressDeclaration(true);
    }

    public HTMLWriter() throws UnsupportedEncodingException {
        super(DEFAULT_HTML_FORMAT);
        this.formatStack = new LinkedList();
        this.lastText = "";
        this.tagsOuput = 0;
        this.newLineAfterNTags = -1;
        this.preformattedTags = DEFAULT_PREFORMATTED_TAGS;
    }

    public HTMLWriter(OutputStream out) throws UnsupportedEncodingException {
        super(out, DEFAULT_HTML_FORMAT);
        this.formatStack = new LinkedList();
        this.lastText = "";
        this.tagsOuput = 0;
        this.newLineAfterNTags = -1;
        this.preformattedTags = DEFAULT_PREFORMATTED_TAGS;
    }

    public HTMLWriter(OutputStream out, OutputFormat format) throws UnsupportedEncodingException {
        super(out, format);
        this.formatStack = new LinkedList();
        this.lastText = "";
        this.tagsOuput = 0;
        this.newLineAfterNTags = -1;
        this.preformattedTags = DEFAULT_PREFORMATTED_TAGS;
    }

    public HTMLWriter(Writer writer) {
        super(writer, DEFAULT_HTML_FORMAT);
        this.formatStack = new LinkedList();
        this.lastText = "";
        this.tagsOuput = 0;
        this.newLineAfterNTags = -1;
        this.preformattedTags = DEFAULT_PREFORMATTED_TAGS;
    }

    public HTMLWriter(Writer writer, OutputFormat format) {
        super(writer, format);
        this.formatStack = new LinkedList();
        this.lastText = "";
        this.tagsOuput = 0;
        this.newLineAfterNTags = -1;
        this.preformattedTags = DEFAULT_PREFORMATTED_TAGS;
    }

    public HTMLWriter(OutputFormat format) throws UnsupportedEncodingException {
        super(format);
        this.formatStack = new LinkedList();
        this.lastText = "";
        this.tagsOuput = 0;
        this.newLineAfterNTags = -1;
        this.preformattedTags = DEFAULT_PREFORMATTED_TAGS;
    }

    private HashSet internalGetOmitElementCloseSet() {
        if (this.omitElementCloseSet == null) {
            this.omitElementCloseSet = new HashSet();
            loadOmitElementCloseSet(this.omitElementCloseSet);
        }
        return this.omitElementCloseSet;
    }

    private String justSpaces(String text) {
        int size = text.length();
        StringBuffer res = new StringBuffer(size);
        int i = 0;
        while (i < size) {
            char c = text.charAt(i);
            switch (c) {
                case Type.OBJECT:
                case Opcodes.FCONST_2:
                    break;
                default:
                    res.append(c);
                    break;
            }
            i++;
        }
        return res.toString();
    }

    private void lazyInitNewLinesAfterNTags() {
        if (getOutputFormat().isNewlines()) {
            this.newLineAfterNTags = 0;
        } else {
            this.newLineAfterNTags = getOutputFormat().getNewLineAfterNTags();
        }
    }

    public static String prettyPrintHTML(String html) throws IOException, UnsupportedEncodingException, DocumentException {
        return prettyPrintHTML(html, true, true, false, true);
    }

    public static String prettyPrintHTML(String html, boolean newlines, boolean trim, boolean isXHTML, boolean expandEmpty) throws IOException, UnsupportedEncodingException, DocumentException {
        Writer sw = new StringWriter();
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setNewlines(newlines);
        format.setTrimText(trim);
        format.setXHTML(isXHTML);
        format.setExpandEmptyElements(expandEmpty);
        HTMLWriter writer = new HTMLWriter(sw, format);
        writer.write(DocumentHelper.parseText(html));
        writer.flush();
        return sw.toString();
    }

    public static String prettyPrintXHTML(String html) throws IOException, UnsupportedEncodingException, DocumentException {
        return prettyPrintHTML(html, true, true, true, false);
    }

    public void endCDATA() throws SAXException {
    }

    public Set getOmitElementCloseSet() {
        return (Set) internalGetOmitElementCloseSet().clone();
    }

    public Set<String> getPreformattedTags() {
        return Collections.unmodifiableSet(this.preformattedTags);
    }

    public boolean isPreformattedTag(String qualifiedName) {
        return this.preformattedTags != null && this.preformattedTags.contains(qualifiedName.toUpperCase());
    }

    protected void loadOmitElementCloseSet(Set<String> set) {
        set.add("AREA");
        set.add("BASE");
        set.add("BR");
        set.add("COL");
        set.add("HR");
        set.add("IMG");
        set.add("INPUT");
        set.add("LINK");
        set.add("META");
        set.add("P");
        set.add("PARAM");
    }

    protected boolean omitElementClose(String qualifiedName) {
        return internalGetOmitElementCloseSet().contains(qualifiedName.toUpperCase());
    }

    public void setOmitElementCloseSet(Set<String> newSet) {
        this.omitElementCloseSet = new HashSet();
        if (newSet != null) {
            this.omitElementCloseSet = new HashSet();
            Iterator i$ = newSet.iterator();
            while (i$.hasNext()) {
                String tag = (String) i$.next();
                if (tag != null) {
                    this.omitElementCloseSet.add(tag.toUpperCase());
                }
            }
        }
    }

    public void setPreformattedTags(Set<String> newSet) {
        this.preformattedTags = new HashSet();
        if (newSet != null) {
            Iterator i$ = newSet.iterator();
            while (i$.hasNext()) {
                String tag = (String) i$.next();
                if (tag != null) {
                    this.preformattedTags.add(tag.toUpperCase());
                }
            }
        }
    }

    public void startCDATA() throws SAXException {
    }

    protected void writeCDATA(String text) throws IOException {
        if (getOutputFormat().isXHTML()) {
            super.writeCDATA(text);
        } else {
            this.writer.write(text);
        }
        this.lastOutputNodeType = NodeType.CDATA_SECTION_NODE;
    }

    protected void writeClose(String qualifiedName) throws IOException {
        if (!omitElementClose(qualifiedName)) {
            super.writeClose(qualifiedName);
        }
    }

    protected void writeDeclaration() throws IOException {
    }

    protected void writeElement(Element element) throws IOException {
        if (this.newLineAfterNTags == -1) {
            lazyInitNewLinesAfterNTags();
        }
        if (this.newLineAfterNTags > 0 && this.tagsOuput > 0 && this.tagsOuput % this.newLineAfterNTags == 0) {
            this.writer.write(lineSeparator);
        }
        this.tagsOuput++;
        String qualifiedName = element.getQualifiedName();
        String saveLastText = this.lastText;
        int size = element.nodeCount();
        if (isPreformattedTag(qualifiedName)) {
            OutputFormat currentFormat = getOutputFormat();
            boolean saveNewlines = currentFormat.isNewlines();
            boolean saveTrimText = currentFormat.isTrimText();
            String currentIndent = currentFormat.getIndent();
            this.formatStack.addFirst(new FormatState(saveNewlines, saveTrimText, currentIndent));
            super.writePrintln();
            if (saveLastText.trim().length() == 0 && currentIndent != null && currentIndent.length() > 0) {
                this.writer.write(justSpaces(saveLastText));
            }
            currentFormat.setNewlines(false);
            currentFormat.setTrimText(false);
            currentFormat.setIndent("");
            super.writeElement(element);
            FormatState state = (FormatState) this.formatStack.poll();
            currentFormat.setNewlines(state.isNewlines());
            currentFormat.setTrimText(state.isTrimText());
            currentFormat.setIndent(state.getIndent());
        } else {
            super.writeElement(element);
        }
    }

    protected void writeEmptyElementClose(String qualifiedName) throws IOException {
        if (getOutputFormat().isXHTML()) {
            if (omitElementClose(qualifiedName)) {
                this.writer.write(" />");
            } else {
                super.writeEmptyElementClose(qualifiedName);
            }
        } else if (omitElementClose(qualifiedName)) {
            this.writer.write(">");
        } else {
            super.writeEmptyElementClose(qualifiedName);
        }
    }

    protected void writeEntity(Entity entity) throws IOException {
        this.writer.write(entity.getText());
        this.lastOutputNodeType = NodeType.ENTITY_REFERENCE_NODE;
    }

    protected void writeString(String text) throws IOException {
        if (!text.equals("\n")) {
            this.lastText = text;
            if (this.formatStack.isEmpty()) {
                super.writeString(text.trim());
            } else {
                super.writeString(text);
            }
        } else if (!this.formatStack.isEmpty()) {
            super.writeString(lineSeparator);
        }
    }
}