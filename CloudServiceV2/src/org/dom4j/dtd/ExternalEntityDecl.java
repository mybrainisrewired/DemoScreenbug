package org.dom4j.dtd;

public class ExternalEntityDecl implements Declaration {
    private String name;
    private String publicID;
    private String systemID;

    public ExternalEntityDecl(String name, String publicID, String systemID) {
        this.name = name;
        this.publicID = publicID;
        this.systemID = systemID;
    }

    public String getName() {
        return this.name;
    }

    public String getPublicID() {
        return this.publicID;
    }

    public String getSystemID() {
        return this.systemID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPublicID(String publicID) {
        this.publicID = publicID;
    }

    public void setSystemID(String systemID) {
        this.systemID = systemID;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer("<!ENTITY ");
        if (this.name.startsWith("%")) {
            buffer.append("% ");
            buffer.append(this.name.substring(1));
        } else {
            buffer.append(this.name);
        }
        if (this.publicID != null) {
            buffer.append(" PUBLIC \"");
            buffer.append(this.publicID);
            buffer.append("\" ");
            if (this.systemID != null) {
                buffer.append("\"");
                buffer.append(this.systemID);
                buffer.append("\" ");
            }
        } else if (this.systemID != null) {
            buffer.append(" SYSTEM \"");
            buffer.append(this.systemID);
            buffer.append("\" ");
        }
        buffer.append(">");
        return buffer.toString();
    }
}