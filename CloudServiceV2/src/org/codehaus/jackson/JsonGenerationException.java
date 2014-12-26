package org.codehaus.jackson;

public class JsonGenerationException extends JsonProcessingException {
    static final long serialVersionUID = 123;

    public JsonGenerationException(String msg) {
        super(msg, (JsonLocation) 0);
    }

    public JsonGenerationException(String msg, Throwable rootCause) {
        super(msg, (JsonLocation) 0, rootCause);
    }

    public JsonGenerationException(Throwable rootCause) {
        super(rootCause);
    }
}