package com.mopub.mobileads.util.vast;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;
import com.mopub.common.CacheService;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.AsyncTasks;
import com.mopub.mobileads.VastVideoDownloadTask;
import com.mopub.mobileads.VastVideoDownloadTask.VastVideoDownloadTaskListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class VastManager implements VastXmlManagerAggregatorListener {
    private static final double AREA_WEIGHT = 60.0d;
    private static final double ASPECT_RATIO_WEIGHT = 40.0d;
    private static final List<String> COMPANION_IMAGE_MIME_TYPES;
    private static final List<String> VIDEO_MIME_TYPES;
    private int mScreenArea;
    private double mScreenAspectRatio;
    private VastManagerListener mVastManagerListener;
    private VastXmlManagerAggregator mVastXmlManagerAggregator;

    public static interface VastManagerListener {
        void onVastVideoConfigurationPrepared(VastVideoConfiguration vastVideoConfiguration);
    }

    class AnonymousClass_1 implements VastVideoDownloadTaskListener {
        private final /* synthetic */ VastVideoConfiguration val$vastVideoConfiguration;

        AnonymousClass_1(VastVideoConfiguration vastVideoConfiguration) {
            this.val$vastVideoConfiguration = vastVideoConfiguration;
        }

        public void onComplete(boolean success) {
            if (success && VastManager.this.updateDiskMediaFileUrl(this.val$vastVideoConfiguration)) {
                if (VastManager.this.mVastManagerListener != null) {
                    VastManager.this.mVastManagerListener.onVastVideoConfigurationPrepared(this.val$vastVideoConfiguration);
                }
            } else if (VastManager.this.mVastManagerListener != null) {
                VastManager.this.mVastManagerListener.onVastVideoConfigurationPrepared(null);
            }
        }
    }

    static {
        VIDEO_MIME_TYPES = Arrays.asList(new String[]{"video/mp4", "video/3gpp"});
        COMPANION_IMAGE_MIME_TYPES = Arrays.asList(new String[]{"image/jpeg", "image/png", "image/bmp", "image/gif"});
    }

    public VastManager(Context context) {
        initializeScreenDimensions(context);
    }

    private double calculateFitness(int width, int height) {
        return 40.0d * Math.abs(Math.log((((double) width) / ((double) height)) / this.mScreenAspectRatio)) + 60.0d * Math.abs(Math.log(((double) (width * height)) / ((double) this.mScreenArea)));
    }

    private VastVideoConfiguration createVastVideoConfigurationFromXml(List<VastXmlManager> xmlManagers) {
        VastVideoConfiguration vastVideoConfiguration = new VastVideoConfiguration();
        List<MediaXmlManager> mediaXmlManagers = new ArrayList();
        List<ImageCompanionAdXmlManager> companionXmlManagers = new ArrayList();
        Iterator it = xmlManagers.iterator();
        while (it.hasNext()) {
            VastXmlManager xmlManager = (VastXmlManager) it.next();
            vastVideoConfiguration.addImpressionTrackers(xmlManager.getImpressionTrackers());
            vastVideoConfiguration.addStartTrackers(xmlManager.getVideoStartTrackers());
            vastVideoConfiguration.addFirstQuartileTrackers(xmlManager.getVideoFirstQuartileTrackers());
            vastVideoConfiguration.addMidpointTrackers(xmlManager.getVideoMidpointTrackers());
            vastVideoConfiguration.addThirdQuartileTrackers(xmlManager.getVideoThirdQuartileTrackers());
            vastVideoConfiguration.addCompleteTrackers(xmlManager.getVideoCompleteTrackers());
            vastVideoConfiguration.addClickTrackers(xmlManager.getClickTrackers());
            if (vastVideoConfiguration.getClickThroughUrl() == null) {
                vastVideoConfiguration.setClickThroughUrl(xmlManager.getClickThroughUrl());
            }
            mediaXmlManagers.addAll(xmlManager.getMediaXmlManagers());
            companionXmlManagers.addAll(xmlManager.getCompanionAdXmlManagers());
        }
        vastVideoConfiguration.setNetworkMediaFileUrl(getBestMediaFileUrl(mediaXmlManagers));
        vastVideoConfiguration.setVastCompanionAd(getBestCompanionAd(companionXmlManagers));
        return vastVideoConfiguration;
    }

    private void initializeScreenDimensions(Context context) {
        Display display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        int x = display.getWidth();
        int y = display.getHeight();
        int screenWidth = Math.max(x, y);
        int screenHeight = Math.min(x, y);
        this.mScreenAspectRatio = ((double) screenWidth) / ((double) screenHeight);
        this.mScreenArea = screenWidth * screenHeight;
    }

    private boolean updateDiskMediaFileUrl(VastVideoConfiguration vastVideoConfiguration) {
        String networkMediaFileUrl = vastVideoConfiguration.getNetworkMediaFileUrl();
        if (!CacheService.containsKeyDiskCache(networkMediaFileUrl)) {
            return false;
        }
        vastVideoConfiguration.setDiskMediaFileUrl(CacheService.getFilePathDiskCache(networkMediaFileUrl));
        return true;
    }

    public void cancel() {
        if (this.mVastXmlManagerAggregator != null) {
            this.mVastXmlManagerAggregator.cancel(true);
            this.mVastXmlManagerAggregator = null;
        }
    }

    VastCompanionAd getBestCompanionAd(List<ImageCompanionAdXmlManager> managers) {
        List<ImageCompanionAdXmlManager> companionXmlManagers = new ArrayList(managers);
        double bestCompanionFitness = Double.POSITIVE_INFINITY;
        ImageCompanionAdXmlManager bestCompanionXmlManager = null;
        Iterator<ImageCompanionAdXmlManager> xmlManagerIterator = companionXmlManagers.iterator();
        while (xmlManagerIterator.hasNext()) {
            ImageCompanionAdXmlManager companionXmlManager = (ImageCompanionAdXmlManager) xmlManagerIterator.next();
            String imageType = companionXmlManager.getType();
            String imageUrl = companionXmlManager.getImageUrl();
            if (!COMPANION_IMAGE_MIME_TYPES.contains(imageType) || imageUrl == null) {
                xmlManagerIterator.remove();
            } else {
                Integer imageWidth = companionXmlManager.getWidth();
                Integer imageHeight = companionXmlManager.getHeight();
                if (imageWidth != null && imageWidth.intValue() > 0 && imageHeight != null && imageHeight.intValue() > 0) {
                    double companionFitness = calculateFitness(imageWidth.intValue(), imageHeight.intValue());
                    if (companionFitness < bestCompanionFitness) {
                        bestCompanionFitness = companionFitness;
                        bestCompanionXmlManager = companionXmlManager;
                    }
                }
            }
        }
        if (bestCompanionXmlManager == null && !companionXmlManagers.isEmpty()) {
            bestCompanionXmlManager = companionXmlManagers.get(0);
        }
        return bestCompanionXmlManager != null ? new VastCompanionAd(bestCompanionXmlManager.getWidth(), bestCompanionXmlManager.getHeight(), bestCompanionXmlManager.getImageUrl(), bestCompanionXmlManager.getClickThroughUrl(), new ArrayList(bestCompanionXmlManager.getClickTrackers())) : null;
    }

    String getBestMediaFileUrl(List<MediaXmlManager> managers) {
        List<MediaXmlManager> mediaXmlManagers = new ArrayList(managers);
        double bestMediaFitness = Double.POSITIVE_INFINITY;
        String bestMediaFileUrl = null;
        Iterator<MediaXmlManager> xmlManagerIterator = mediaXmlManagers.iterator();
        while (xmlManagerIterator.hasNext()) {
            MediaXmlManager mediaXmlManager = (MediaXmlManager) xmlManagerIterator.next();
            String mediaType = mediaXmlManager.getType();
            String mediaUrl = mediaXmlManager.getMediaUrl();
            if (!VIDEO_MIME_TYPES.contains(mediaType) || mediaUrl == null) {
                xmlManagerIterator.remove();
            } else {
                Integer mediaWidth = mediaXmlManager.getWidth();
                Integer mediaHeight = mediaXmlManager.getHeight();
                if (mediaWidth != null && mediaWidth.intValue() > 0 && mediaHeight != null && mediaHeight.intValue() > 0) {
                    double mediaFitness = calculateFitness(mediaWidth.intValue(), mediaHeight.intValue());
                    if (mediaFitness < bestMediaFitness) {
                        bestMediaFitness = mediaFitness;
                        bestMediaFileUrl = mediaUrl;
                    }
                }
            }
        }
        return (bestMediaFileUrl != null || mediaXmlManagers.isEmpty()) ? bestMediaFileUrl : ((MediaXmlManager) mediaXmlManagers.get(0)).getMediaUrl();
    }

    @Deprecated
    int getScreenArea() {
        return this.mScreenArea;
    }

    @Deprecated
    double getScreenAspectRatio() {
        return this.mScreenAspectRatio;
    }

    public void onAggregationComplete(List<VastXmlManager> vastXmlManagers) {
        this.mVastXmlManagerAggregator = null;
        if (vastXmlManagers != null) {
            VastVideoConfiguration vastVideoConfiguration = createVastVideoConfigurationFromXml(vastXmlManagers);
            if (!updateDiskMediaFileUrl(vastVideoConfiguration)) {
                VastVideoDownloadTask vastVideoDownloadTask = new VastVideoDownloadTask(new AnonymousClass_1(vastVideoConfiguration));
                try {
                    AsyncTasks.safeExecuteOnExecutor(vastVideoDownloadTask, new String[]{vastVideoConfiguration.getNetworkMediaFileUrl()});
                } catch (Exception e) {
                    MoPubLog.d("Failed to download vast video", e);
                    if (this.mVastManagerListener != null) {
                        this.mVastManagerListener.onVastVideoConfigurationPrepared(null);
                    }
                }
            } else if (this.mVastManagerListener != null) {
                this.mVastManagerListener.onVastVideoConfigurationPrepared(vastVideoConfiguration);
            }
        } else if (this.mVastManagerListener != null) {
            this.mVastManagerListener.onVastVideoConfigurationPrepared(null);
        }
    }

    public void prepareVastVideoConfiguration(String vastXml, VastManagerListener vastManagerListener) {
        if (this.mVastXmlManagerAggregator == null) {
            this.mVastManagerListener = vastManagerListener;
            this.mVastXmlManagerAggregator = new VastXmlManagerAggregator(this);
            try {
                AsyncTasks.safeExecuteOnExecutor(this.mVastXmlManagerAggregator, new String[]{vastXml});
            } catch (Exception e) {
                MoPubLog.d("Failed to aggregate vast xml", e);
                if (this.mVastManagerListener != null) {
                    this.mVastManagerListener.onVastVideoConfigurationPrepared(null);
                }
            }
        }
    }
}