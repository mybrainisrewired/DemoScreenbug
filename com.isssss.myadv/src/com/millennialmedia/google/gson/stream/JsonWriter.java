package com.millennialmedia.google.gson.stream;

import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;

public class JsonWriter implements Closeable, Flushable {
    private static final String[] HTML_SAFE_REPLACEMENT_CHARS;
    private static final String[] REPLACEMENT_CHARS;
    private String deferredName;
    private boolean htmlSafe;
    private String indent;
    private boolean lenient;
    private final Writer out;
    private String separator;
    private boolean serializeNulls;
    private int[] stack;
    private int stackSize;

    static {
        REPLACEMENT_CHARS = new String[128];
        int i = 0;
        while (i <= 31) {
            REPLACEMENT_CHARS[i] = String.format("\\u%04x", new Object[]{Integer.valueOf(i)});
            i++;
        }
        REPLACEMENT_CHARS[34] = "\\\"";
        REPLACEMENT_CHARS[92] = "\\\\";
        REPLACEMENT_CHARS[9] = "\\t";
        REPLACEMENT_CHARS[8] = "\\b";
        REPLACEMENT_CHARS[10] = "\\n";
        REPLACEMENT_CHARS[13] = "\\r";
        REPLACEMENT_CHARS[12] = "\\f";
        HTML_SAFE_REPLACEMENT_CHARS = (String[]) REPLACEMENT_CHARS.clone();
        HTML_SAFE_REPLACEMENT_CHARS[60] = "\\u003c";
        HTML_SAFE_REPLACEMENT_CHARS[62] = "\\u003e";
        HTML_SAFE_REPLACEMENT_CHARS[38] = "\\u0026";
        HTML_SAFE_REPLACEMENT_CHARS[61] = "\\u003d";
        HTML_SAFE_REPLACEMENT_CHARS[39] = "\\u0027";
    }

    public JsonWriter(Writer out) {
        this.stack = new int[32];
        this.stackSize = 0;
        push(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES);
        this.separator = ":";
        this.serializeNulls = true;
        if (out == null) {
            throw new NullPointerException("out == null");
        }
        this.out = out;
    }

    private void beforeName() throws IOException {
        int context = peek();
        if (context == 5) {
            this.out.write(ApiEventType.API_MRAID_SET_VIDEO_VOLUME);
        } else if (context != 3) {
            throw new IllegalStateException("Nesting problem.");
        }
        newline();
        replaceTop(MMAdView.TRANSITION_RANDOM);
    }

    private void beforeValue(boolean root) throws IOException {
        switch (peek()) {
            case MMAdView.TRANSITION_FADE:
                replaceTop(MMAdView.TRANSITION_UP);
                newline();
            case MMAdView.TRANSITION_UP:
                this.out.append(',');
                newline();
            case MMAdView.TRANSITION_RANDOM:
                this.out.append(this.separator);
                replaceTop(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES);
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                if (this.lenient || root) {
                    replaceTop(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES);
                } else {
                    throw new IllegalStateException("JSON must start with an array or an object.");
                }
            case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                if (!this.lenient) {
                    throw new IllegalStateException("JSON must have only one top-level value.");
                }
                replaceTop(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES);
            default:
                throw new IllegalStateException("Nesting problem.");
        }
    }

    private JsonWriter close(int empty, int nonempty, String closeBracket) throws IOException {
        int context = peek();
        if (context != nonempty && context != empty) {
            throw new IllegalStateException("Nesting problem.");
        } else if (this.deferredName != null) {
            throw new IllegalStateException("Dangling name: " + this.deferredName);
        } else {
            this.stackSize--;
            if (context == nonempty) {
                newline();
            }
            this.out.write(closeBracket);
            return this;
        }
    }

    private void newline() throws IOException {
        if (this.indent != null) {
            this.out.write("\n");
            int i = 1;
            int size = this.stackSize;
            while (i < size) {
                this.out.write(this.indent);
                i++;
            }
        }
    }

    private JsonWriter open(int empty, String openBracket) throws IOException {
        beforeValue(true);
        push(empty);
        this.out.write(openBracket);
        return this;
    }

    private int peek() {
        if (this.stackSize != 0) {
            return this.stack[this.stackSize - 1];
        }
        throw new IllegalStateException("JsonWriter is closed.");
    }

    private void push(int newTop) {
        if (this.stackSize == this.stack.length) {
            int[] newStack = new int[(this.stackSize * 2)];
            System.arraycopy(this.stack, 0, newStack, 0, this.stackSize);
            this.stack = newStack;
        }
        int[] iArr = this.stack;
        int i = this.stackSize;
        this.stackSize = i + 1;
        iArr[i] = newTop;
    }

    private void replaceTop(int topOfStack) {
        this.stack[this.stackSize - 1] = topOfStack;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void string(java.lang.String r9_value) throws java.io.IOException {
        throw new UnsupportedOperationException("Method not decompiled: com.millennialmedia.google.gson.stream.JsonWriter.string(java.lang.String):void");
        /*
        r8 = this;
        r6 = r8.htmlSafe;
        if (r6 == 0) goto L_0x0024;
    L_0x0004:
        r5 = HTML_SAFE_REPLACEMENT_CHARS;
    L_0x0006:
        r6 = r8.out;
        r7 = "\"";
        r6.write(r7);
        r2 = 0;
        r3 = r9.length();
        r1 = 0;
    L_0x0013:
        if (r1 >= r3) goto L_0x0045;
    L_0x0015:
        r0 = r9.charAt(r1);
        r6 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        if (r0 >= r6) goto L_0x0027;
    L_0x001d:
        r4 = r5[r0];
        if (r4 != 0) goto L_0x002d;
    L_0x0021:
        r1 = r1 + 1;
        goto L_0x0013;
    L_0x0024:
        r5 = REPLACEMENT_CHARS;
        goto L_0x0006;
    L_0x0027:
        r6 = 8232; // 0x2028 float:1.1535E-41 double:4.067E-320;
        if (r0 != r6) goto L_0x003e;
    L_0x002b:
        r4 = "\\u2028";
    L_0x002d:
        if (r2 >= r1) goto L_0x0036;
    L_0x002f:
        r6 = r8.out;
        r7 = r1 - r2;
        r6.write(r9, r2, r7);
    L_0x0036:
        r6 = r8.out;
        r6.write(r4);
        r2 = r1 + 1;
        goto L_0x0021;
    L_0x003e:
        r6 = 8233; // 0x2029 float:1.1537E-41 double:4.0676E-320;
        if (r0 != r6) goto L_0x0021;
    L_0x0042:
        r4 = "\\u2029";
        goto L_0x002d;
    L_0x0045:
        if (r2 >= r3) goto L_0x004e;
    L_0x0047:
        r6 = r8.out;
        r7 = r3 - r2;
        r6.write(r9, r2, r7);
    L_0x004e:
        r6 = r8.out;
        r7 = "\"";
        r6.write(r7);
        return;
        */
    }

    private void writeDeferredName() throws IOException {
        if (this.deferredName != null) {
            beforeName();
            string(this.deferredName);
            this.deferredName = null;
        }
    }

    public JsonWriter beginArray() throws IOException {
        writeDeferredName();
        return open(1, "[");
    }

    public JsonWriter beginObject() throws IOException {
        writeDeferredName();
        return open(MMAdView.TRANSITION_DOWN, "{");
    }

    public void close() throws IOException {
        this.out.close();
        int size = this.stackSize;
        if (size > 1 || (size == 1 && this.stack[size - 1] != 7)) {
            throw new IOException("Incomplete document");
        }
        this.stackSize = 0;
    }

    public JsonWriter endArray() throws IOException {
        return close(1, MMAdView.TRANSITION_UP, "]");
    }

    public JsonWriter endObject() throws IOException {
        return close(MMAdView.TRANSITION_DOWN, ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, "}");
    }

    public void flush() throws IOException {
        if (this.stackSize == 0) {
            throw new IllegalStateException("JsonWriter is closed.");
        }
        this.out.flush();
    }

    public final boolean getSerializeNulls() {
        return this.serializeNulls;
    }

    public final boolean isHtmlSafe() {
        return this.htmlSafe;
    }

    public boolean isLenient() {
        return this.lenient;
    }

    public JsonWriter name(String name) throws IOException {
        if (name == null) {
            throw new NullPointerException("name == null");
        } else if (this.deferredName != null) {
            throw new IllegalStateException();
        } else if (this.stackSize == 0) {
            throw new IllegalStateException("JsonWriter is closed.");
        } else {
            this.deferredName = name;
            return this;
        }
    }

    public JsonWriter nullValue() throws IOException {
        if (this.deferredName == null || !this.serializeNulls) {
            this.deferredName = null;
        } else {
            writeDeferredName();
            beforeValue(false);
            this.out.write("null");
        }
        return this;
    }

    public final void setHtmlSafe(boolean htmlSafe) {
        this.htmlSafe = htmlSafe;
    }

    public final void setIndent(String indent) {
        if (indent.length() == 0) {
            this.indent = null;
            this.separator = ":";
        } else {
            this.indent = indent;
            this.separator = ": ";
        }
    }

    public final void setLenient(boolean lenient) {
        this.lenient = lenient;
    }

    public final void setSerializeNulls(boolean serializeNulls) {
        this.serializeNulls = serializeNulls;
    }

    public JsonWriter value(double value) throws IOException {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("Numeric values must be finite, but was " + value);
        }
        writeDeferredName();
        beforeValue(false);
        this.out.append(Double.toString(value));
        return this;
    }

    public JsonWriter value(long value) throws IOException {
        writeDeferredName();
        beforeValue(false);
        this.out.write(Long.toString(value));
        return this;
    }

    public JsonWriter value(Number value) throws IOException {
        if (value == null) {
            return nullValue();
        }
        writeDeferredName();
        String string = value.toString();
        if (this.lenient || !(string.equals("-Infinity") || string.equals("Infinity") || string.equals("NaN"))) {
            beforeValue(false);
            this.out.append(string);
            return this;
        } else {
            throw new IllegalArgumentException("Numeric values must be finite, but was " + value);
        }
    }

    public JsonWriter value(String value) throws IOException {
        if (value == null) {
            return nullValue();
        }
        writeDeferredName();
        beforeValue(false);
        string(value);
        return this;
    }

    public JsonWriter value(boolean value) throws IOException {
        writeDeferredName();
        beforeValue(false);
        this.out.write(value ? "true" : "false");
        return this;
    }
}