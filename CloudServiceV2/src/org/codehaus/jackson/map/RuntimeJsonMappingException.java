package org.codehaus.jackson.map;

public class RuntimeJsonMappingException extends RuntimeException {
    public RuntimeJsonMappingException(String message) {
        super(message);
    }

    public RuntimeJsonMappingException(String message, JsonMappingException cause) {
        super(message, cause);
    }

    public RuntimeJsonMappingException(JsonMappingException cause) {
        super(cause);
    }
}