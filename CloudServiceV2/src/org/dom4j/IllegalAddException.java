package org.dom4j;

public class IllegalAddException extends IllegalArgumentException {
    public IllegalAddException(String reason) {
        super(reason);
    }

    public IllegalAddException(Branch parent, Node node, String reason) {
        super("The node \"" + node.toString() + "\" could not be added to the branch \"" + parent.getName() + "\" because: " + reason);
    }

    public IllegalAddException(Element parent, Node node, String reason) {
        super("The node \"" + node.toString() + "\" could not be added to the element \"" + parent.getQualifiedName() + "\" because: " + reason);
    }
}