package org.codehaus.jackson.smile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.smile.SmileGenerator.Feature;

public class Tool {
    public static final String SUFFIX = ".lzf";
    public final JsonFactory jsonFactory;
    public final SmileFactory smileFactory;

    public Tool() {
        this.jsonFactory = new JsonFactory();
        this.smileFactory = new SmileFactory();
        this.smileFactory.configure(Feature.CHECK_SHARED_NAMES, true);
        this.smileFactory.configure(Feature.CHECK_SHARED_STRING_VALUES, true);
        this.smileFactory.configure(Feature.ENCODE_BINARY_AS_7BIT, true);
        this.smileFactory.configure(Feature.WRITE_HEADER, true);
        this.smileFactory.configure(Feature.WRITE_END_MARKER, false);
        this.smileFactory.configure(SmileParser.Feature.REQUIRE_HEADER, false);
    }

    private void decode(InputStream in) throws IOException {
        JsonParser jp = this.smileFactory.createJsonParser(in);
        JsonGenerator jg = this.jsonFactory.createJsonGenerator(System.out, JsonEncoding.UTF8);
        while (true) {
            if (jp.nextToken() == null && jp.nextToken() == null) {
                jp.close();
                jg.close();
                return;
            } else {
                jg.copyCurrentEvent(jp);
            }
        }
    }

    private void encode(InputStream in) throws IOException {
        JsonParser jp = this.jsonFactory.createJsonParser(in);
        JsonGenerator jg = this.smileFactory.createJsonGenerator(System.out, JsonEncoding.UTF8);
        while (jp.nextToken() != null) {
            jg.copyCurrentEvent(jp);
        }
        jp.close();
        jg.close();
    }

    private InputStream inputStream(String filename) throws IOException {
        if (filename == null) {
            return System.in;
        }
        File src = new File(filename);
        if (!src.exists()) {
            System.err.println("File '" + filename + "' does not exist.");
            System.exit(1);
        }
        return new FileInputStream(src);
    }

    public static void main(String[] args) throws IOException {
        new Tool().process(args);
    }

    private void process(String[] args) throws IOException {
        String oper = null;
        String filename = null;
        if (args.length == 2) {
            oper = args[0];
            filename = args[1];
        } else if (args.length == 1) {
            oper = args[0];
        } else {
            showUsage();
        }
        if ("-e".equals(oper)) {
            encode(inputStream(filename));
        } else if ("-d".equals(oper)) {
            decode(inputStream(filename));
        } else if ("-v".equals(oper)) {
            verify(inputStream(filename), inputStream(filename));
        } else {
            showUsage();
        }
    }

    private void verify(InputStream in, InputStream in2) throws IOException {
        String text1;
        String text2;
        JsonParser jp = this.jsonFactory.createJsonParser(in);
        OutputStream bytes = new ByteArrayOutputStream(4000);
        JsonGenerator jg = this.smileFactory.createJsonGenerator(bytes, JsonEncoding.UTF8);
        while (jp.nextToken() != null) {
            jg.copyCurrentEvent(jp);
        }
        jp.close();
        jg.close();
        jp = this.jsonFactory.createJsonParser(in2);
        byte[] smile = bytes.toByteArray();
        JsonParser jp2 = this.smileFactory.createJsonParser(smile);
        int count = 0;
        do {
            JsonToken t = jp.nextToken();
            if (t != null) {
                JsonToken t2 = jp2.nextToken();
                count++;
                if (t != t2) {
                    throw new IOException("Input and encoded differ, token #" + count + "; expected " + t + ", got " + t2);
                }
                text1 = jp.getText();
                text2 = jp2.getText();
            } else {
                System.out.println("OK: verified " + count + " tokens (from " + smile.length + " bytes of Smile encoded data), input and encoded contents are identical");
                return;
            }
        } while (text1.equals(text2));
        throw new IOException("Input and encoded differ, token #" + count + "; expected text '" + text1 + "', got '" + text2 + "'");
    }

    protected void showUsage() {
        System.err.println("Usage: java " + getClass().getName() + " -e/-d [file]");
        System.err.println(" (if no file given, reads from stdin -- always writes to stdout)");
        System.err.println(" -d: decode Smile encoded input as JSON");
        System.err.println(" -e: encode JSON (text) input as Smile");
        System.err.println(" -v: encode JSON (text) input as Smile; read back, verify, do not write out");
        System.exit(1);
    }
}