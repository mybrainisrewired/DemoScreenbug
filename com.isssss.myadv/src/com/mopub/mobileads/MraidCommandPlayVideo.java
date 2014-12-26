package com.mopub.mobileads;

import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import com.mopub.mobileads.MraidView.PlacementType;
import java.util.Map;

class MraidCommandPlayVideo extends MraidCommand {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$mopub$mobileads$MraidView$PlacementType;

    static /* synthetic */ int[] $SWITCH_TABLE$com$mopub$mobileads$MraidView$PlacementType() {
        int[] iArr = $SWITCH_TABLE$com$mopub$mobileads$MraidView$PlacementType;
        if (iArr == null) {
            iArr = new int[PlacementType.values().length];
            try {
                iArr[PlacementType.INLINE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[PlacementType.INTERSTITIAL.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $SWITCH_TABLE$com$mopub$mobileads$MraidView$PlacementType = iArr;
        }
        return iArr;
    }

    public MraidCommandPlayVideo(Map<String, String> params, MraidView view) {
        super(params, view);
    }

    void execute() {
        String url = getStringFromParamsForKey("uri");
        if (url == null || url.equals(Preconditions.EMPTY_ARGUMENTS)) {
            this.mView.fireErrorEvent(MraidJavascriptCommand.PLAY_VIDEO, "Video can't be played with null or empty URL");
        } else {
            this.mView.getDisplayController().showVideo(url);
        }
    }

    protected boolean isCommandDependentOnUserClick(PlacementType placementType) {
        switch ($SWITCH_TABLE$com$mopub$mobileads$MraidView$PlacementType()[placementType.ordinal()]) {
            case MMAdView.TRANSITION_FADE:
                return true;
            case MMAdView.TRANSITION_UP:
                return false;
            default:
                return super.isCommandDependentOnUserClick(placementType);
        }
    }
}