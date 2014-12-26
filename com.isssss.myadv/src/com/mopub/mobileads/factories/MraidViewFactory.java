package com.mopub.mobileads.factories;

import android.content.Context;
import com.mopub.mobileads.AdConfiguration;
import com.mopub.mobileads.MraidView;
import com.mopub.mobileads.MraidView.ExpansionStyle;
import com.mopub.mobileads.MraidView.NativeCloseButtonStyle;
import com.mopub.mobileads.MraidView.PlacementType;

public class MraidViewFactory {
    protected static MraidViewFactory instance;

    static {
        instance = new MraidViewFactory();
    }

    public static MraidView create(Context context, AdConfiguration adConfiguration) {
        return instance.internalCreate(context, adConfiguration);
    }

    public static MraidView create(Context context, AdConfiguration adConfiguration, ExpansionStyle expansionStyle, NativeCloseButtonStyle buttonStyle, PlacementType placementType) {
        return instance.internalCreate(context, adConfiguration, expansionStyle, buttonStyle, placementType);
    }

    @Deprecated
    public static void setInstance(MraidViewFactory factory) {
        instance = factory;
    }

    protected MraidView internalCreate(Context context, AdConfiguration adConfiguration) {
        return new MraidView(context, adConfiguration);
    }

    protected MraidView internalCreate(Context context, AdConfiguration adConfiguration, ExpansionStyle expansionStyle, NativeCloseButtonStyle buttonStyle, PlacementType placementType) {
        return new MraidView(context, adConfiguration, expansionStyle, buttonStyle, placementType);
    }
}