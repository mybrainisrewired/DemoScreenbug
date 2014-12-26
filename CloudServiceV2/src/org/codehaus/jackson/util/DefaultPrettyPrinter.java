package org.codehaus.jackson.util;

import java.io.IOException;
import java.util.Arrays;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.PrettyPrinter;
import org.codehaus.jackson.impl.Indenter;

public class DefaultPrettyPrinter implements PrettyPrinter {
    protected Indenter _arrayIndenter;
    protected int _nesting;
    protected Indenter _objectIndenter;
    protected boolean _spacesInObjectEntries;

    public static class FixedSpaceIndenter implements Indenter {
        public boolean isInline() {
            return true;
        }

        public void writeIndentation(JsonGenerator jg, int level) throws IOException, JsonGenerationException {
            jg.writeRaw(' ');
        }
    }

    public static class Lf2SpacesIndenter implements Indenter {
        static final char[] SPACES;
        static final int SPACE_COUNT = 64;
        static final String SYSTEM_LINE_SEPARATOR;

        static {
            String lf = null;
            try {
                lf = System.getProperty("line.separator");
            } catch (Throwable th) {
            }
            if (lf == null) {
                lf = "\n";
            }
            SYSTEM_LINE_SEPARATOR = lf;
            SPACES = new char[64];
            Arrays.fill(SPACES, ' ');
        }

        public boolean isInline() {
            return false;
        }

        public void writeIndentation(JsonGenerator jg, int level) throws IOException, JsonGenerationException {
            jg.writeRaw(SYSTEM_LINE_SEPARATOR);
            level += level;
            while (level > 64) {
                jg.writeRaw(SPACES, 0, (int)SPACE_COUNT);
                level -= SPACES.length;
            }
            jg.writeRaw(SPACES, 0, level);
        }
    }

    public static class NopIndenter implements Indenter {
        public boolean isInline() {
            return true;
        }

        public void writeIndentation(JsonGenerator jg, int level) {
        }
    }

    public DefaultPrettyPrinter() {
        this._arrayIndenter = new FixedSpaceIndenter();
        this._objectIndenter = new Lf2SpacesIndenter();
        this._spacesInObjectEntries = true;
        this._nesting = 0;
    }

    public void beforeArrayValues(JsonGenerator jg) throws IOException, JsonGenerationException {
        this._arrayIndenter.writeIndentation(jg, this._nesting);
    }

    public void beforeObjectEntries(JsonGenerator jg) throws IOException, JsonGenerationException {
        this._objectIndenter.writeIndentation(jg, this._nesting);
    }

    public void indentArraysWith(Indenter i) {
        if (i == null) {
            i = new NopIndenter();
        }
        this._arrayIndenter = i;
    }

    public void indentObjectsWith(Indenter i) {
        if (i == null) {
            i = new NopIndenter();
        }
        this._objectIndenter = i;
    }

    public void spacesInObjectEntries(boolean b) {
        this._spacesInObjectEntries = b;
    }

    public void writeArrayValueSeparator(JsonGenerator jg) throws IOException, JsonGenerationException {
        jg.writeRaw(',');
        this._arrayIndenter.writeIndentation(jg, this._nesting);
    }

    public void writeEndArray(JsonGenerator jg, int nrOfValues) throws IOException, JsonGenerationException {
        if (!this._arrayIndenter.isInline()) {
            this._nesting--;
        }
        if (nrOfValues > 0) {
            this._arrayIndenter.writeIndentation(jg, this._nesting);
        } else {
            jg.writeRaw(' ');
        }
        jg.writeRaw(']');
    }

    public void writeEndObject(JsonGenerator jg, int nrOfEntries) throws IOException, JsonGenerationException {
        if (!this._objectIndenter.isInline()) {
            this._nesting--;
        }
        if (nrOfEntries > 0) {
            this._objectIndenter.writeIndentation(jg, this._nesting);
        } else {
            jg.writeRaw(' ');
        }
        jg.writeRaw('}');
    }

    public void writeObjectEntrySeparator(JsonGenerator jg) throws IOException, JsonGenerationException {
        jg.writeRaw(',');
        this._objectIndenter.writeIndentation(jg, this._nesting);
    }

    public void writeObjectFieldValueSeparator(JsonGenerator jg) throws IOException, JsonGenerationException {
        if (this._spacesInObjectEntries) {
            jg.writeRaw(" : ");
        } else {
            jg.writeRaw(':');
        }
    }

    public void writeRootValueSeparator(JsonGenerator jg) throws IOException, JsonGenerationException {
        jg.writeRaw(' ');
    }

    public void writeStartArray(JsonGenerator jg) throws IOException, JsonGenerationException {
        if (!this._arrayIndenter.isInline()) {
            this._nesting++;
        }
        jg.writeRaw('[');
    }

    public void writeStartObject(JsonGenerator jg) throws IOException, JsonGenerationException {
        jg.writeRaw('{');
        if (!this._objectIndenter.isInline()) {
            this._nesting++;
        }
    }
}