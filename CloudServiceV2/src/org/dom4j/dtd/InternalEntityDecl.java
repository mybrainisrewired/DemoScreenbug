package org.dom4j.dtd;

public class InternalEntityDecl implements InternalDeclaration {
    private String name;
    private String value;

    public InternalEntityDecl(String name, String value) {
        this.name = name;
        this.value = value;
    }

    private String escapeEntityValue(String text) {
        StringBuffer result = new StringBuffer();
        int i = 0;
        while (i < text.length()) {
            char c = text.charAt(i);
            switch (c) {
                case '\"':
                    result.append("&#34;");
                    break;
                case '&':
                    result.append("&#38;#38;");
                    break;
                case '\'':
                    result.append("&#39;");
                    break;
                case '<':
                    result.append("&#38;#60;");
                    break;
                case '>':
                    result.append("&#62;");
                    break;
                default:
                    if (c < ' ') {
                        result.append("&#" + c + ";");
                    } else {
                        result.append(c);
                    }
                    break;
            }
            i++;
        }
        return result.toString();
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer("<!ENTITY ");
        if (this.name.startsWith("%")) {
            buffer.append("% ");
            buffer.append(this.name.substring(1));
        } else {
            buffer.append(this.name);
        }
        buffer.append(" \"");
        buffer.append(escapeEntityValue(this.value));
        buffer.append("\">");
        return buffer.toString();
    }
}