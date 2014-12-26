package com.mopub.common;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;

public final class DiskLruCacheUtil {
    static final Charset US_ASCII;
    static final Charset UTF_8;

    static {
        US_ASCII = Charset.forName("US-ASCII");
        UTF_8 = Charset.forName("UTF-8");
    }

    private DiskLruCacheUtil() {
    }

    static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e2) {
            }
        }
    }

    static void deleteContents(File dir) throws IOException {
        File[] files = dir.listFiles();
        if (files == null) {
            throw new IOException(new StringBuilder("not a readable directory: ").append(dir).toString());
        }
        int length = files.length;
        int i = 0;
        while (i < length) {
            File file = files[i];
            if (file.isDirectory()) {
                deleteContents(file);
            }
            if (file.delete()) {
                i++;
            } else {
                throw new IOException(new StringBuilder("failed to delete file: ").append(file).toString());
            }
        }
    }

    static String readFully(Reader reader) throws IOException {
        StringWriter writer = new StringWriter();
        char[] buffer = new char[1024];
        while (true) {
            int count = reader.read(buffer);
            if (count == -1) {
                String toString = writer.toString();
                reader.close();
                return toString;
            } else {
                writer.write(buffer, 0, count);
            }
        }
    }
}