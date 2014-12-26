package com.mopub.common;

import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public final class DiskLruCache implements Closeable {
    static final long ANY_SEQUENCE_NUMBER = -1;
    private static final String CLEAN = "CLEAN";
    private static final String DIRTY = "DIRTY";
    static final String JOURNAL_FILE = "journal";
    static final String JOURNAL_FILE_BACKUP = "journal.bkp";
    static final String JOURNAL_FILE_TEMP = "journal.tmp";
    static final Pattern LEGAL_KEY_PATTERN;
    static final String MAGIC = "libcore.io.DiskLruCache";
    private static final OutputStream NULL_OUTPUT_STREAM;
    private static final String READ = "READ";
    private static final String REMOVE = "REMOVE";
    static final String VERSION_1 = "1";
    private final int appVersion;
    private final Callable<Void> cleanupCallable;
    private final File directory;
    final ThreadPoolExecutor executorService;
    private final File journalFile;
    private final File journalFileBackup;
    private final File journalFileTmp;
    private Writer journalWriter;
    private final LinkedHashMap<String, Entry> lruEntries;
    private long maxSize;
    private long nextSequenceNumber;
    private int redundantOpCount;
    private long size;
    private final int valueCount;

    public final class Editor {
        private boolean committed;
        private final Entry entry;
        private boolean hasErrors;
        private final boolean[] written;

        private class FaultHidingOutputStream extends FilterOutputStream {
            private FaultHidingOutputStream(OutputStream out) {
                super(out);
            }

            public void close() {
                try {
                    this.out.close();
                } catch (IOException e) {
                    com.mopub.common.DiskLruCache.Editor.this.hasErrors = true;
                }
            }

            public void flush() {
                try {
                    this.out.flush();
                } catch (IOException e) {
                    com.mopub.common.DiskLruCache.Editor.this.hasErrors = true;
                }
            }

            public void write(int oneByte) {
                try {
                    this.out.write(oneByte);
                } catch (IOException e) {
                    com.mopub.common.DiskLruCache.Editor.this.hasErrors = true;
                }
            }

            public void write(byte[] buffer, int offset, int length) {
                try {
                    this.out.write(buffer, offset, length);
                } catch (IOException e) {
                    com.mopub.common.DiskLruCache.Editor.this.hasErrors = true;
                }
            }
        }

        private Editor(Entry entry) {
            this.entry = entry;
            this.written = entry.readable ? null : new boolean[DiskLruCache.this.valueCount];
        }

        public void abort() throws IOException {
            DiskLruCache.this.completeEdit(this, false);
        }

        public void abortUnlessCommitted() {
            if (!this.committed) {
                try {
                    abort();
                } catch (IOException e) {
                }
            }
        }

        public void commit() throws IOException {
            if (this.hasErrors) {
                DiskLruCache.this.completeEdit(this, false);
                DiskLruCache.this.remove(this.entry.key);
            } else {
                DiskLruCache.this.completeEdit(this, true);
            }
            this.committed = true;
        }

        public String getString(int index) throws IOException {
            InputStream in = newInputStream(index);
            return in != null ? DiskLruCache.inputStreamToString(in) : null;
        }

        public InputStream newInputStream(int index) throws IOException {
            synchronized (DiskLruCache.this) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                } else if (this.entry.readable) {
                    try {
                        InputStream fileInputStream = new FileInputStream(this.entry.getCleanFile(index));
                        return fileInputStream;
                    } catch (FileNotFoundException e) {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        }

        public OutputStream newOutputStream(int index) throws IOException {
            OutputStream faultHidingOutputStream;
            synchronized (DiskLruCache.this) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
                FileOutputStream outputStream;
                if (!this.entry.readable) {
                    this.written[index] = true;
                }
                File dirtyFile = this.entry.getDirtyFile(index);
                try {
                    outputStream = new FileOutputStream(dirtyFile);
                } catch (FileNotFoundException e) {
                    DiskLruCache.this.directory.mkdirs();
                    try {
                        outputStream = new FileOutputStream(dirtyFile);
                    } catch (FileNotFoundException e2) {
                        faultHidingOutputStream = NULL_OUTPUT_STREAM;
                    }
                }
                faultHidingOutputStream = new FaultHidingOutputStream(outputStream, null);
            }
            return faultHidingOutputStream;
        }

        public void set(int index, String value) throws IOException {
            Writer writer = null;
            try {
                Writer writer2 = new OutputStreamWriter(newOutputStream(index), DiskLruCacheUtil.UTF_8);
                try {
                    writer2.write(value);
                    DiskLruCacheUtil.closeQuietly(writer2);
                } catch (Throwable th) {
                    th = th;
                    writer = writer2;
                    DiskLruCacheUtil.closeQuietly(writer);
                    throw th;
                }
            } catch (Throwable th2) {
                Throwable th3 = th2;
                DiskLruCacheUtil.closeQuietly(writer);
                throw th3;
            }
        }
    }

    private final class Entry {
        private com.mopub.common.DiskLruCache.Editor currentEditor;
        private final String key;
        private final long[] lengths;
        private boolean readable;
        private long sequenceNumber;

        private Entry(String key) {
            this.key = key;
            this.lengths = new long[DiskLruCache.this.valueCount];
        }

        private IOException invalidLengths(String[] strings) throws IOException {
            throw new IOException(new StringBuilder("unexpected journal line: ").append(Arrays.toString(strings)).toString());
        }

        private void setLengths(String[] strings) throws IOException {
            if (strings.length != DiskLruCache.this.valueCount) {
                throw invalidLengths(strings);
            }
            int i = 0;
            while (i < strings.length) {
                try {
                    this.lengths[i] = Long.parseLong(strings[i]);
                    i++;
                } catch (NumberFormatException e) {
                    throw invalidLengths(strings);
                }
            }
        }

        public File getCleanFile(int i) {
            return new File(DiskLruCache.this.directory, new StringBuilder(String.valueOf(this.key)).append(".").append(i).toString());
        }

        public File getDirtyFile(int i) {
            return new File(DiskLruCache.this.directory, new StringBuilder(String.valueOf(this.key)).append(".").append(i).append(".tmp").toString());
        }

        public String getLengths() throws IOException {
            StringBuilder result = new StringBuilder();
            long[] jArr = this.lengths;
            int length = jArr.length;
            int i = 0;
            while (i < length) {
                result.append(' ').append(jArr[i]);
                i++;
            }
            return result.toString();
        }
    }

    public final class Snapshot implements Closeable {
        private final InputStream[] ins;
        private final String key;
        private final long[] lengths;
        private final long sequenceNumber;

        private Snapshot(String key, long sequenceNumber, InputStream[] ins, long[] lengths) {
            this.key = key;
            this.sequenceNumber = sequenceNumber;
            this.ins = ins;
            this.lengths = lengths;
        }

        public void close() {
            InputStream[] inputStreamArr = this.ins;
            int length = inputStreamArr.length;
            int i = 0;
            while (i < length) {
                DiskLruCacheUtil.closeQuietly(inputStreamArr[i]);
                i++;
            }
        }

        public com.mopub.common.DiskLruCache.Editor edit() throws IOException {
            return DiskLruCache.this.edit(this.key, this.sequenceNumber);
        }

        public InputStream getInputStream(int index) {
            return this.ins[index];
        }

        public long getLength(int index) {
            return this.lengths[index];
        }

        public String getString(int index) throws IOException {
            return DiskLruCache.inputStreamToString(getInputStream(index));
        }
    }

    static {
        LEGAL_KEY_PATTERN = Pattern.compile("[a-z0-9_-]{1,64}");
        NULL_OUTPUT_STREAM = new OutputStream() {
            public void write(int b) throws IOException {
            }
        };
    }

    private DiskLruCache(File directory, int appVersion, int valueCount, long maxSize) {
        this.size = 0;
        this.lruEntries = new LinkedHashMap(0, 0.75f, true);
        this.nextSequenceNumber = 0;
        this.executorService = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue());
        this.cleanupCallable = new Callable<Void>() {
            public Void call() throws Exception {
                synchronized (DiskLruCache.this) {
                    if (DiskLruCache.this.journalWriter == null) {
                    } else {
                        DiskLruCache.this.trimToSize();
                        if (DiskLruCache.this.journalRebuildRequired()) {
                            DiskLruCache.this.rebuildJournal();
                            DiskLruCache.this.redundantOpCount = 0;
                        }
                    }
                }
                return null;
            }
        };
        this.directory = directory;
        this.appVersion = appVersion;
        this.journalFile = new File(directory, JOURNAL_FILE);
        this.journalFileTmp = new File(directory, JOURNAL_FILE_TEMP);
        this.journalFileBackup = new File(directory, JOURNAL_FILE_BACKUP);
        this.valueCount = valueCount;
        this.maxSize = maxSize;
    }

    private void checkNotClosed() {
        if (this.journalWriter == null) {
            throw new IllegalStateException("cache is closed");
        }
    }

    private synchronized void completeEdit(Editor editor, boolean success) throws IOException {
        Entry entry = editor.entry;
        if (entry.currentEditor != editor) {
            throw new IllegalStateException();
        }
        int i;
        if (success) {
            if (!entry.readable) {
                i = 0;
                while (i < this.valueCount) {
                    if (!editor.written[i]) {
                        editor.abort();
                        throw new IllegalStateException(new StringBuilder("Newly created entry didn't create value for index ").append(i).toString());
                    } else if (!entry.getDirtyFile(i).exists()) {
                        editor.abort();
                        break;
                    } else {
                        i++;
                    }
                }
            }
        }
        i = 0;
        while (i < this.valueCount) {
            File dirty = entry.getDirtyFile(i);
            if (!success) {
                deleteIfExists(dirty);
            } else if (dirty.exists()) {
                File clean = entry.getCleanFile(i);
                dirty.renameTo(clean);
                long oldLength = entry.lengths[i];
                long newLength = clean.length();
                entry.lengths[i] = newLength;
                this.size = this.size - oldLength + newLength;
            }
            i++;
        }
        this.redundantOpCount++;
        entry.currentEditor = null;
        if ((entry.readable | success) != 0) {
            entry.readable = true;
            this.journalWriter.write(new StringBuilder("CLEAN ").append(entry.key).append(entry.getLengths()).append('\n').toString());
            if (success) {
                long j = this.nextSequenceNumber;
                this.nextSequenceNumber = 1 + j;
                entry.sequenceNumber = j;
            }
        } else {
            this.lruEntries.remove(entry.key);
            this.journalWriter.write(new StringBuilder("REMOVE ").append(entry.key).append('\n').toString());
        }
        this.journalWriter.flush();
        if (this.size > this.maxSize || journalRebuildRequired()) {
            this.executorService.submit(this.cleanupCallable);
        }
    }

    private static void deleteIfExists(File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }

    private synchronized Editor edit(String key, long expectedSequenceNumber) throws IOException {
        Editor editor = null;
        synchronized (this) {
            checkNotClosed();
            validateKey(key);
            Entry entry = (Entry) this.lruEntries.get(key);
            if (expectedSequenceNumber == -1 || (entry != null && entry.sequenceNumber == expectedSequenceNumber)) {
                if (entry == null) {
                    entry = new Entry(key, null);
                    this.lruEntries.put(key, entry);
                } else if (entry.currentEditor != null) {
                }
                editor = new Editor(entry, null);
                entry.currentEditor = editor;
                this.journalWriter.write(new StringBuilder("DIRTY ").append(key).append('\n').toString());
                this.journalWriter.flush();
            }
        }
        return editor;
    }

    private static String inputStreamToString(InputStream in) throws IOException {
        return DiskLruCacheUtil.readFully(new InputStreamReader(in, DiskLruCacheUtil.UTF_8));
    }

    private boolean journalRebuildRequired() {
        return this.redundantOpCount >= 2000 && this.redundantOpCount >= this.lruEntries.size();
    }

    public static DiskLruCache open(File directory, int appVersion, int valueCount, long maxSize) throws IOException {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        } else if (valueCount <= 0) {
            throw new IllegalArgumentException("valueCount <= 0");
        } else {
            File backupFile = new File(directory, JOURNAL_FILE_BACKUP);
            if (backupFile.exists()) {
                File journalFile = new File(directory, JOURNAL_FILE);
                if (journalFile.exists()) {
                    backupFile.delete();
                } else {
                    renameTo(backupFile, journalFile, false);
                }
            }
            DiskLruCache cache = new DiskLruCache(directory, appVersion, valueCount, maxSize);
            if (cache.journalFile.exists()) {
                try {
                    cache.readJournal();
                    cache.processJournal();
                    cache.journalWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(cache.journalFile, true), DiskLruCacheUtil.US_ASCII));
                    return cache;
                } catch (IOException e) {
                    System.out.println(new StringBuilder("DiskLruCache ").append(directory).append(" is corrupt: ").append(e.getMessage()).append(", removing").toString());
                    cache.delete();
                }
            }
            directory.mkdirs();
            cache = new DiskLruCache(directory, appVersion, valueCount, maxSize);
            cache.rebuildJournal();
            return cache;
        }
    }

    private void processJournal() throws IOException {
        deleteIfExists(this.journalFileTmp);
        Iterator<Entry> i = this.lruEntries.values().iterator();
        while (i.hasNext()) {
            Entry entry = (Entry) i.next();
            int t;
            if (entry.currentEditor == null) {
                t = 0;
                while (t < this.valueCount) {
                    this.size += entry.lengths[t];
                    t++;
                }
            } else {
                entry.currentEditor = null;
                t = 0;
                while (t < this.valueCount) {
                    deleteIfExists(entry.getCleanFile(t));
                    deleteIfExists(entry.getDirtyFile(t));
                    t++;
                }
                i.remove();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void readJournal() throws java.io.IOException {
        throw new UnsupportedOperationException("Method not decompiled: com.mopub.common.DiskLruCache.readJournal():void");
        /*
        r10 = this;
        r4 = new com.mopub.common.DiskLruCacheStrictLineReader;
        r7 = new java.io.FileInputStream;
        r8 = r10.journalFile;
        r7.<init>(r8);
        r8 = com.mopub.common.DiskLruCacheUtil.US_ASCII;
        r4.<init>(r7, r8);
        r3 = r4.readLine();	 Catch:{ all -> 0x008b }
        r6 = r4.readLine();	 Catch:{ all -> 0x008b }
        r0 = r4.readLine();	 Catch:{ all -> 0x008b }
        r5 = r4.readLine();	 Catch:{ all -> 0x008b }
        r1 = r4.readLine();	 Catch:{ all -> 0x008b }
        r7 = "libcore.io.DiskLruCache";
        r7 = r7.equals(r3);	 Catch:{ all -> 0x008b }
        if (r7 == 0) goto L_0x0052;
    L_0x002a:
        r7 = "1";
        r7 = r7.equals(r6);	 Catch:{ all -> 0x008b }
        if (r7 == 0) goto L_0x0052;
    L_0x0032:
        r7 = r10.appVersion;	 Catch:{ all -> 0x008b }
        r7 = java.lang.Integer.toString(r7);	 Catch:{ all -> 0x008b }
        r7 = r7.equals(r0);	 Catch:{ all -> 0x008b }
        if (r7 == 0) goto L_0x0052;
    L_0x003e:
        r7 = r10.valueCount;	 Catch:{ all -> 0x008b }
        r7 = java.lang.Integer.toString(r7);	 Catch:{ all -> 0x008b }
        r7 = r7.equals(r5);	 Catch:{ all -> 0x008b }
        if (r7 == 0) goto L_0x0052;
    L_0x004a:
        r7 = "";
        r7 = r7.equals(r1);	 Catch:{ all -> 0x008b }
        if (r7 != 0) goto L_0x0090;
    L_0x0052:
        r7 = new java.io.IOException;	 Catch:{ all -> 0x008b }
        r8 = new java.lang.StringBuilder;	 Catch:{ all -> 0x008b }
        r9 = "unexpected journal header: [";
        r8.<init>(r9);	 Catch:{ all -> 0x008b }
        r8 = r8.append(r3);	 Catch:{ all -> 0x008b }
        r9 = ", ";
        r8 = r8.append(r9);	 Catch:{ all -> 0x008b }
        r8 = r8.append(r6);	 Catch:{ all -> 0x008b }
        r9 = ", ";
        r8 = r8.append(r9);	 Catch:{ all -> 0x008b }
        r8 = r8.append(r5);	 Catch:{ all -> 0x008b }
        r9 = ", ";
        r8 = r8.append(r9);	 Catch:{ all -> 0x008b }
        r8 = r8.append(r1);	 Catch:{ all -> 0x008b }
        r9 = "]";
        r8 = r8.append(r9);	 Catch:{ all -> 0x008b }
        r8 = r8.toString();	 Catch:{ all -> 0x008b }
        r7.<init>(r8);	 Catch:{ all -> 0x008b }
        throw r7;	 Catch:{ all -> 0x008b }
    L_0x008b:
        r7 = move-exception;
        com.mopub.common.DiskLruCacheUtil.closeQuietly(r4);
        throw r7;
    L_0x0090:
        r2 = 0;
    L_0x0091:
        r7 = r4.readLine();	 Catch:{ EOFException -> 0x009b }
        r10.readJournalLine(r7);	 Catch:{ EOFException -> 0x009b }
        r2 = r2 + 1;
        goto L_0x0091;
    L_0x009b:
        r7 = move-exception;
        r7 = r10.lruEntries;	 Catch:{ all -> 0x008b }
        r7 = r7.size();	 Catch:{ all -> 0x008b }
        r7 = r2 - r7;
        r10.redundantOpCount = r7;	 Catch:{ all -> 0x008b }
        com.mopub.common.DiskLruCacheUtil.closeQuietly(r4);
        return;
        */
    }

    private void readJournalLine(String line) throws IOException {
        int firstSpace = line.indexOf(ApiEventType.API_MRAID_PLAY_AUDIO);
        if (firstSpace == -1) {
            throw new IOException(new StringBuilder("unexpected journal line: ").append(line).toString());
        }
        String key;
        int keyBegin = firstSpace + 1;
        int secondSpace = line.indexOf(ApiEventType.API_MRAID_PLAY_AUDIO, keyBegin);
        if (secondSpace == -1) {
            key = line.substring(keyBegin);
            if (firstSpace == REMOVE.length() && line.startsWith(REMOVE)) {
                this.lruEntries.remove(key);
                return;
            }
        } else {
            key = line.substring(keyBegin, secondSpace);
        }
        Entry entry = (Entry) this.lruEntries.get(key);
        if (entry == null) {
            entry = new Entry(key, null);
            this.lruEntries.put(key, entry);
        }
        if (secondSpace != -1 && firstSpace == CLEAN.length() && line.startsWith(CLEAN)) {
            String[] parts = line.substring(secondSpace + 1).split(" ");
            entry.readable = true;
            entry.currentEditor = null;
            entry.setLengths(parts);
        } else if (secondSpace == -1 && firstSpace == DIRTY.length() && line.startsWith(DIRTY)) {
            entry.currentEditor = new Editor(entry, null);
        } else if (secondSpace != -1 || firstSpace != READ.length() || !line.startsWith(READ)) {
            throw new IOException(new StringBuilder("unexpected journal line: ").append(line).toString());
        }
    }

    private synchronized void rebuildJournal() throws IOException {
        try {
            if (this.journalWriter != null) {
                this.journalWriter.close();
            }
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFileTmp), DiskLruCacheUtil.US_ASCII));
            writer.write(MAGIC);
            writer.write("\n");
            writer.write(VERSION_1);
            writer.write("\n");
            writer.write(Integer.toString(this.appVersion));
            writer.write("\n");
            writer.write(Integer.toString(this.valueCount));
            writer.write("\n");
            writer.write("\n");
            Iterator it = this.lruEntries.values().iterator();
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                if (entry.currentEditor != null) {
                    writer.write(new StringBuilder("DIRTY ").append(entry.key).append('\n').toString());
                } else {
                    writer.write(new StringBuilder("CLEAN ").append(entry.key).append(entry.getLengths()).append('\n').toString());
                }
            }
            writer.close();
            if (this.journalFile.exists()) {
                renameTo(this.journalFile, this.journalFileBackup, true);
            }
            renameTo(this.journalFileTmp, this.journalFile, false);
            this.journalFileBackup.delete();
            this.journalWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFile, true), DiskLruCacheUtil.US_ASCII));
        } catch (Throwable th) {
        }
    }

    private static void renameTo(File from, File to, boolean deleteDestination) throws IOException {
        if (deleteDestination) {
            deleteIfExists(to);
        }
        if (!from.renameTo(to)) {
            throw new IOException();
        }
    }

    private void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            remove((String) ((java.util.Map.Entry) this.lruEntries.entrySet().iterator().next()).getKey());
        }
    }

    private void validateKey(String key) {
        if (!LEGAL_KEY_PATTERN.matcher(key).matches()) {
            throw new IllegalArgumentException(new StringBuilder("keys must match regex [a-z0-9_-]{1,64}: \"").append(key).append("\"").toString());
        }
    }

    public synchronized void close() throws IOException {
        if (this.journalWriter != null) {
            Iterator it = new ArrayList(this.lruEntries.values()).iterator();
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                if (entry.currentEditor != null) {
                    entry.currentEditor.abort();
                }
            }
            trimToSize();
            this.journalWriter.close();
            this.journalWriter = null;
        }
    }

    public void delete() throws IOException {
        close();
        DiskLruCacheUtil.deleteContents(this.directory);
    }

    public Editor edit(String key) throws IOException {
        return edit(key, ANY_SEQUENCE_NUMBER);
    }

    public synchronized void flush() throws IOException {
        checkNotClosed();
        trimToSize();
        this.journalWriter.flush();
    }

    public synchronized Snapshot get(String key) throws IOException {
        Snapshot snapshot = null;
        synchronized (this) {
            checkNotClosed();
            validateKey(key);
            Entry entry = (Entry) this.lruEntries.get(key);
            if (entry != null) {
                if (entry.readable) {
                    InputStream[] ins = new InputStream[this.valueCount];
                    int i = 0;
                    while (i < this.valueCount) {
                        try {
                            ins[i] = new FileInputStream(entry.getCleanFile(i));
                            i++;
                        } catch (FileNotFoundException e) {
                            i = 0;
                            while (i < this.valueCount && ins[i] != null) {
                                DiskLruCacheUtil.closeQuietly(ins[i]);
                                i++;
                            }
                        }
                    }
                    this.redundantOpCount++;
                    this.journalWriter.append(new StringBuilder("READ ").append(key).append('\n').toString());
                    if (journalRebuildRequired()) {
                        this.executorService.submit(this.cleanupCallable);
                    }
                    snapshot = new Snapshot(key, entry.sequenceNumber, ins, entry.lengths, null);
                }
            }
        }
        return snapshot;
    }

    public File getDirectory() {
        return this.directory;
    }

    public synchronized long getMaxSize() {
        return this.maxSize;
    }

    public synchronized boolean isClosed() {
        return this.journalWriter == null;
    }

    public synchronized boolean remove(String key) throws IOException {
        boolean z;
        checkNotClosed();
        validateKey(key);
        Entry entry = (Entry) this.lruEntries.get(key);
        if (entry == null || entry.currentEditor != null) {
            z = false;
        } else {
            int i = 0;
            while (i < this.valueCount) {
                File file = entry.getCleanFile(i);
                if (!file.exists() || file.delete()) {
                    this.size -= entry.lengths[i];
                    entry.lengths[i] = 0;
                    i++;
                } else {
                    throw new IOException(new StringBuilder("failed to delete ").append(file).toString());
                }
            }
            this.redundantOpCount++;
            this.journalWriter.append(new StringBuilder("REMOVE ").append(key).append('\n').toString());
            this.lruEntries.remove(key);
            if (journalRebuildRequired()) {
                this.executorService.submit(this.cleanupCallable);
            }
            z = true;
        }
        return z;
    }

    public synchronized void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
        this.executorService.submit(this.cleanupCallable);
    }

    public synchronized long size() {
        return this.size;
    }
}