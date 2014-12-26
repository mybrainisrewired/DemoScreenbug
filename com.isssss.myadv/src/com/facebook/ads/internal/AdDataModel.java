package com.facebook.ads.internal;

import java.util.Collection;

public interface AdDataModel {
    Collection<String> getDetectionStrings();

    AdInvalidationBehavior getInvalidationBehavior();
}