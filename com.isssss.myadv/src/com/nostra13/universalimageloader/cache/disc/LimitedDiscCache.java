package com.nostra13.universalimageloader.cache.disc;

import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class LimitedDiscCache extends BaseDiscCache {
    private static final int INVALID_SIZE = -1;
    private final AtomicInteger cacheSize;
    private final Map<File, Long> lastUsageDates;
    private final int sizeLimit;

    public LimitedDiscCache(File cacheDir, int sizeLimit) {
        this(cacheDir, DefaultConfigurationFactory.createFileNameGenerator(), sizeLimit);
    }

    public LimitedDiscCache(File cacheDir, FileNameGenerator fileNameGenerator, int sizeLimit) {
        super(cacheDir, fileNameGenerator);
        this.lastUsageDates = Collections.synchronizedMap(new HashMap());
        this.sizeLimit = sizeLimit;
        this.cacheSize = new AtomicInteger();
        calculateCacheSizeAndFillUsageMap();
    }

    private void calculateCacheSizeAndFillUsageMap() {
        new Thread(new Runnable() {
            public void run() {
                int size = 0;
                File[] cachedFiles = LimitedDiscCache.this.cacheDir.listFiles();
                if (cachedFiles != null) {
                    int length = cachedFiles.length;
                    int i = 0;
                    while (i < length) {
                        File cachedFile = cachedFiles[i];
                        size += LimitedDiscCache.this.getSize(cachedFile);
                        LimitedDiscCache.this.lastUsageDates.put(cachedFile, Long.valueOf(cachedFile.lastModified()));
                        i++;
                    }
                    LimitedDiscCache.this.cacheSize.set(size);
                }
            }
        }).start();
    }

    private int removeNext() {
        if (this.lastUsageDates.isEmpty()) {
            return INVALID_SIZE;
        }
        Long oldestUsage = null;
        File mostLongUsedFile = null;
        Set<Entry<File, Long>> entries = this.lastUsageDates.entrySet();
        synchronized (this.lastUsageDates) {
            Iterator it = entries.iterator();
            while (it.hasNext()) {
                Entry<File, Long> entry = (Entry) it.next();
                if (mostLongUsedFile == null) {
                    mostLongUsedFile = (File) entry.getKey();
                    oldestUsage = (Long) entry.getValue();
                } else {
                    Long lastValueUsage = (Long) entry.getValue();
                    if (lastValueUsage.longValue() < oldestUsage.longValue()) {
                        oldestUsage = lastValueUsage;
                        mostLongUsedFile = (File) entry.getKey();
                    }
                }
            }
        }
        if (mostLongUsedFile == null) {
            return 0;
        }
        if (mostLongUsedFile.exists()) {
            int fileSize = getSize(mostLongUsedFile);
            if (!mostLongUsedFile.delete()) {
                return fileSize;
            }
            this.lastUsageDates.remove(mostLongUsedFile);
            return fileSize;
        } else {
            this.lastUsageDates.remove(mostLongUsedFile);
            return 0;
        }
    }

    public void clear() {
        this.lastUsageDates.clear();
        this.cacheSize.set(0);
        super.clear();
    }

    public File get(String key) {
        File file = super.get(key);
        Long currentTime = Long.valueOf(System.currentTimeMillis());
        file.setLastModified(currentTime.longValue());
        this.lastUsageDates.put(file, currentTime);
        return file;
    }

    protected abstract int getSize(File file);

    public void put(String key, File file) {
        int valueSize = getSize(file);
        int curCacheSize = this.cacheSize.get();
        while (curCacheSize + valueSize > this.sizeLimit) {
            int freedSize = removeNext();
            if (freedSize == -1) {
                break;
            }
            curCacheSize = this.cacheSize.addAndGet(-freedSize);
        }
        this.cacheSize.addAndGet(valueSize);
        Long currentTime = Long.valueOf(System.currentTimeMillis());
        file.setLastModified(currentTime.longValue());
        this.lastUsageDates.put(file, currentTime);
    }
}