package org.dom4j.datatype;

public class InvalidSchemaException extends IllegalArgumentException {
    public InvalidSchemaException(String reason) {
        super(reason);
    }
}