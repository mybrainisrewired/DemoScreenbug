package com.millennialmedia.android;

import java.util.Stack;

class ComponentRegistry {
    static Stack<BridgeMMBanner> bannerBridges;
    static Stack<BridgeMMCachedVideo> cachedVideoBridges;
    static Stack<BridgeMMCalendar> calendarBridges;
    static Stack<BridgeMMDevice> deviceBridges;
    static Stack<ExampleComponent> examples;
    static Stack<BridgeMMInlineVideo> inlineVideoBridges;
    static Stack<BridgeMMInterstitial> interstitialBridges;
    static Stack<LoggingComponent> loggingComponents;
    static Stack<BridgeMMMedia> mediaBridges;
    static Stack<BridgeMMNotification> notificationBridges;
    static Stack<BridgeMMSpeechkit> speechkitBridges;

    static {
        examples = new Stack();
        bannerBridges = new Stack();
        cachedVideoBridges = new Stack();
        calendarBridges = new Stack();
        deviceBridges = new Stack();
        inlineVideoBridges = new Stack();
        interstitialBridges = new Stack();
        mediaBridges = new Stack();
        notificationBridges = new Stack();
        speechkitBridges = new Stack();
        loggingComponents = new Stack();
    }

    ComponentRegistry() {
    }

    static void addBannerBridge(BridgeMMBanner bannerBridge) {
        bannerBridges.push(bannerBridge);
    }

    static void addCachedVideoBridge(BridgeMMCachedVideo cachedVideoBridge) {
        cachedVideoBridges.push(cachedVideoBridge);
    }

    static void addCalendarBridge(BridgeMMCalendar calendarBridge) {
        calendarBridges.push(calendarBridge);
    }

    static void addDeviceBridge(BridgeMMDevice deviceBridge) {
        deviceBridges.push(deviceBridge);
    }

    static void addExample(ExampleComponent testComponent) {
        examples.push(testComponent);
    }

    static void addInlineVideoBridge(BridgeMMInlineVideo inlineVideoBridge) {
        inlineVideoBridges.push(inlineVideoBridge);
    }

    static void addInterstitialBridge(BridgeMMInterstitial interstitialBridge) {
        interstitialBridges.push(interstitialBridge);
    }

    static void addLoggingComponent(LoggingComponent loggingComponent) {
        loggingComponents.push(loggingComponent);
    }

    static void addMediaBridge(BridgeMMMedia mediaBridge) {
        mediaBridges.push(mediaBridge);
    }

    static void addNotificationBridge(BridgeMMNotification notificationBridge) {
        notificationBridges.push(notificationBridge);
    }

    static void addSpeechkitBridge(BridgeMMSpeechkit speechkitBridge) {
        speechkitBridges.push(speechkitBridge);
    }

    static BridgeMMBanner getBannerBridge() {
        return (BridgeMMBanner) getComponent(bannerBridges);
    }

    static BridgeMMCachedVideo getCachedVideoBridge() {
        return (BridgeMMCachedVideo) getComponent(cachedVideoBridges);
    }

    static BridgeMMCalendar getCalendarBridge() {
        return (BridgeMMCalendar) getComponent(calendarBridges);
    }

    private static <T> T getComponent(Stack<T> components) {
        return components.isEmpty() ? null : components.lastElement();
    }

    static BridgeMMDevice getDeviceBridge() {
        return (BridgeMMDevice) getComponent(deviceBridges);
    }

    static ExampleComponent getExample() {
        return (ExampleComponent) getComponent(examples);
    }

    static BridgeMMInlineVideo getInlineVideoBridge() {
        return (BridgeMMInlineVideo) getComponent(inlineVideoBridges);
    }

    static BridgeMMInterstitial getInterstitialBridge() {
        return (BridgeMMInterstitial) getComponent(interstitialBridges);
    }

    static LoggingComponent getLoggingComponent() {
        return (LoggingComponent) getComponent(loggingComponents);
    }

    static BridgeMMMedia getMediaBridge() {
        return (BridgeMMMedia) getComponent(mediaBridges);
    }

    static BridgeMMNotification getNotificationBridge() {
        return (BridgeMMNotification) getComponent(notificationBridges);
    }

    static BridgeMMSpeechkit getSpeechkitBridge() {
        return (BridgeMMSpeechkit) getComponent(speechkitBridges);
    }

    static void removeBannerBridge(boolean force) {
        removeComponent(force, bannerBridges);
    }

    static void removeCachedVideoBridge(boolean force) {
        removeComponent(force, cachedVideoBridges);
    }

    static void removeCalendarBridge(boolean force) {
        removeComponent(force, calendarBridges);
    }

    private static <T> void removeComponent(boolean force, Stack<T> components) {
        if (!components.isEmpty()) {
            if (components.size() != 1 || force) {
                components.pop();
            }
        }
    }

    static void removeDeviceBridge(boolean force) {
        removeComponent(force, deviceBridges);
    }

    static void removeExample(boolean force) {
        removeComponent(force, examples);
    }

    static void removeInlineVideoBridge(boolean force) {
        removeComponent(force, inlineVideoBridges);
    }

    static void removeInterstitialBridge(boolean force) {
        removeComponent(force, interstitialBridges);
    }

    static void removeLoggingComponent(boolean force) {
        removeComponent(force, loggingComponents);
    }

    static void removeMediaBridge(boolean force) {
        removeComponent(force, mediaBridges);
    }

    static void removeNotificationBridge(boolean force) {
        removeComponent(force, notificationBridges);
    }

    static void removeSpeechkitBridge(boolean force) {
        removeComponent(force, speechkitBridges);
    }
}