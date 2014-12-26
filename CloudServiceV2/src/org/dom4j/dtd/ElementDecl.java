package org.dom4j.dtd;

import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class ElementDecl implements Declaration {
    private String model;
    private String name;

    public ElementDecl(String name, String model) {
        this.name = name;
        this.model = model;
    }

    public String getModel() {
        return this.model;
    }

    public String getName() {
        return this.name;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "<!ELEMENT " + this.name + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + this.model + ">";
    }
}