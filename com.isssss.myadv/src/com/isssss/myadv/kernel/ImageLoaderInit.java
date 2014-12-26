package com.isssss.myadv.kernel;

import android.content.Context;
import android.os.Environment;
import com.isssss.myadv.utils.FileUtil;
import com.millennialmedia.android.MMAdView;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import java.io.File;

public class ImageLoaderInit {
    private static final String DEFAULT_IMAGE_CACHE_DIR = "images";

    public static void init(Context context) {
        File cacheFile;
        DisplayImageOptions defaultOptions = new Builder().cacheInMemory(true).cacheOnDisc(true).build();
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;
        if (FileUtil.isSDCardExist()) {
            cacheFile = new File(Environment.getExternalStorageDirectory() + DEFAULT_IMAGE_CACHE_DIR);
        } else {
            cacheFile = new File(new StringBuilder(String.valueOf(context.getCacheDir().getAbsolutePath())).append(DEFAULT_IMAGE_CACHE_DIR).toString());
        }
        ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(context).memoryCacheExtraOptions(width, height).threadPoolSize(MMAdView.TRANSITION_DOWN).threadPriority(MMAdView.TRANSITION_DOWN).tasksProcessingOrder(QueueProcessingType.LIFO).denyCacheImageMultipleSizesInMemory().memoryCache(new UsingFreqLimitedMemoryCache((int) (Runtime.getRuntime().maxMemory() / 8))).discCache(new UnlimitedDiscCache(cacheFile)).discCacheFileNameGenerator(new Md5FileNameGenerator()).defaultDisplayImageOptions(defaultOptions).writeDebugLogs().build());
    }
}