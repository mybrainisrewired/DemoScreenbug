package com.nostra13.universalimageloader.cache.disc;

import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
import java.io.File;

public abstract class BaseDiscCache implements DiscCacheAware {
    private static final String ERROR_ARG_NULL = "\"%s\" argument must be not null";
    protected File cacheDir;
    private FileNameGenerator fileNameGenerator;

    public BaseDiscCache(File cacheDir) {
        this(cacheDir, DefaultConfigurationFactory.createFileNameGenerator());
    }

    public BaseDiscCache(File cacheDir, FileNameGenerator fileNameGenerator) {
        if (cacheDir == null) {
            throw new IllegalArgumentException(String.format(ERROR_ARG_NULL, new Object[]{"cacheDir"}));
        } else if (fileNameGenerator == null) {
            throw new IllegalArgumentException(String.format(ERROR_ARG_NULL, new Object[]{"fileNameGenerator"}));
        } else {
            this.cacheDir = cacheDir;
            this.fileNameGenerator = fileNameGenerator;
        }
    }

    public void clear() {
        File[] files = this.cacheDir.listFiles();
        if (files != null) {
            int length = files.length;
            int i = 0;
            while (i < length) {
                files[i].delete();
                i++;
            }
        }
    }

    public File get(String key) {
        return new File(this.cacheDir, this.fileNameGenerator.generate(key));
    }
}