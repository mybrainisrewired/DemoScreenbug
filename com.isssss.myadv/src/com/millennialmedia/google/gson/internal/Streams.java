package com.millennialmedia.google.gson.internal;

import com.millennialmedia.google.gson.JsonElement;
import com.millennialmedia.google.gson.JsonIOException;
import com.millennialmedia.google.gson.JsonNull;
import com.millennialmedia.google.gson.JsonParseException;
import com.millennialmedia.google.gson.JsonSyntaxException;
import com.millennialmedia.google.gson.internal.bind.TypeAdapters;
import com.millennialmedia.google.gson.stream.JsonReader;
import com.millennialmedia.google.gson.stream.JsonWriter;
import com.millennialmedia.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.io.Writer;

public final class Streams {

    private static final class AppendableWriter extends Writer {
        private final Appendable appendable;
        private final CurrentWrite currentWrite;

        static class CurrentWrite implements CharSequence {
            char[] chars;

            CurrentWrite() {
            }

            public char charAt(int i) {
                return this.chars[i];
            }

            public int length() {
                return this.chars.length;
            }

            public CharSequence subSequence(int start, int end) {
                return new String(this.chars, start, end - start);
            }
        }

        private AppendableWriter(Appendable appendable) {
            this.currentWrite = new CurrentWrite();
            this.appendable = appendable;
        }

        public void close() {
        }

        public void flush() {
        }

        public void write(int i) throws IOException {
            this.appendable.append((char) i);
        }

        public void write(char[] chars, int offset, int length) throws IOException {
            this.currentWrite.chars = chars;
            this.appendable.append(this.currentWrite, offset, offset + length);
        }
    }

    public static JsonElement parse(JsonReader reader) throws JsonParseException {
        try {
            reader.peek();
            return (JsonElement) TypeAdapters.JSON_ELEMENT.read(reader);
        } catch (EOFException e) {
            e = e;
            if (true) {
                return JsonNull.INSTANCE;
            }
            Throwable e2;
            throw new JsonSyntaxException(e2);
        } catch (MalformedJsonException e3) {
            throw new JsonSyntaxException(e3);
        } catch (IOException e4) {
            throw new JsonIOException(e4);
        } catch (NumberFormatException e5) {
            throw new JsonSyntaxException(e5);
        }
    }

    public static void write(JsonElement element, JsonWriter writer) throws IOException {
        TypeAdapters.JSON_ELEMENT.write(writer, element);
    }

    public static Writer writerForAppendable(Appendable appendable) {
        return appendable instanceof Writer ? (Writer) appendable : new AppendableWriter(null);
    }
}