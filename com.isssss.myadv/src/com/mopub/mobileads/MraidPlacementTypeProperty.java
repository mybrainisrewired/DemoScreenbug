package com.mopub.mobileads;

import com.mopub.mobileads.MraidView.PlacementType;

// compiled from: MraidProperty.java
class MraidPlacementTypeProperty extends MraidProperty {
    private final PlacementType mPlacementType;

    MraidPlacementTypeProperty(PlacementType placementType) {
        this.mPlacementType = placementType;
    }

    public static MraidPlacementTypeProperty createWithType(PlacementType placementType) {
        return new MraidPlacementTypeProperty(placementType);
    }

    public String toJsonPair() {
        return new StringBuilder("placementType: '").append(this.mPlacementType.toString().toLowerCase()).append("'").toString();
    }
}