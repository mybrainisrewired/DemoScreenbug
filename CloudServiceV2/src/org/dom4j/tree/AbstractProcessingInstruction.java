package org.dom4j.tree;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.dom4j.Element;
import org.dom4j.NodeType;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Visitor;

public abstract class AbstractProcessingInstruction extends AbstractNode implements ProcessingInstruction {
    private String getName(StringTokenizer tokenizer) {
        StringBuilder name = new StringBuilder(tokenizer.nextToken());
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token.equals("=")) {
                break;
            }
            name.append(token);
        }
        return name.toString().trim();
    }

    private String getValue(StringTokenizer tokenizer) {
        String token = tokenizer.nextToken();
        StringBuilder value = new StringBuilder();
        while (tokenizer.hasMoreTokens() && !token.equals("'") && !token.equals("\"")) {
            token = tokenizer.nextToken();
        }
        String quote = token;
        while (tokenizer.hasMoreTokens()) {
            token = tokenizer.nextToken();
            if (quote.equals(token)) {
                break;
            }
            value.append(token);
        }
        return value.toString();
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String asXML() {
        return "<?" + getName() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + getText() + "?>";
    }

    public String getName() {
        return getTarget();
    }

    public NodeType getNodeTypeEnum() {
        return NodeType.PROCESSING_INSTRUCTION_NODE;
    }

    public String getPath(Element context) {
        Element parent = getParent();
        return (parent == null || parent == context) ? "processing-instruction()" : parent.getPath(context) + "/processing-instruction()";
    }

    public String getUniquePath(Element context) {
        Element parent = getParent();
        return (parent == null || parent == context) ? "processing-instruction()" : parent.getUniquePath(context) + "/processing-instruction()";
    }

    protected Map<String, String> parseValues(String text) {
        Map<String, String> data = new HashMap();
        StringTokenizer s = new StringTokenizer(text, " ='\"", true);
        while (s.hasMoreTokens()) {
            String name = getName(s);
            if (s.hasMoreTokens()) {
                data.put(name, getValue(s));
            }
        }
        return data;
    }

    public boolean removeValue(String name) {
        return false;
    }

    public void setName(String name) {
        setTarget(name);
    }

    public void setValue(String name, String value) {
        throw new UnsupportedOperationException("This PI is read-only and cannot be modified");
    }

    public void setValues(Map<String, String> data) {
        throw new UnsupportedOperationException("This PI is read-only and cannot be modified");
    }

    public String toString() {
        return super.toString() + " [ProcessingInstruction: &" + getName() + ";]";
    }

    protected String toString(Map<String, String> values) {
        StringBuilder builder = new StringBuilder();
        Iterator i$ = values.entrySet().iterator();
        while (i$.hasNext()) {
            Entry<String, String> entry = (Entry) i$.next();
            String value = (String) entry.getValue();
            builder.append((String) entry.getKey());
            builder.append("=\"");
            builder.append(value);
            builder.append("\" ");
        }
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }

    public void write(Writer writer) throws IOException {
        writer.write("<?");
        writer.write(getName());
        writer.write(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        writer.write(getText());
        writer.write("?>");
    }
}