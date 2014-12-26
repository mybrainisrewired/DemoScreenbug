package com.mopub.mobileads;

import com.google.android.gms.plus.PlusShare;
import com.millennialmedia.android.MMAdView;
import com.mopub.mobileads.MraidView.PlacementType;
import java.util.Map;

class MraidCommandExpand extends MraidCommand {
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

    MraidCommandExpand(Map<String, String> params, MraidView view) {
        super(params, view);
    }

    void execute() {
        int width = getIntFromParamsForKey("w");
        int height = getIntFromParamsForKey("h");
        String url = getStringFromParamsForKey(PlusShare.KEY_CALL_TO_ACTION_URL);
        boolean shouldUseCustomClose = getBooleanFromParamsForKey("shouldUseCustomClose");
        boolean shouldLockOrientation = getBooleanFromParamsForKey("lockOrientation");
        if (width <= 0) {
            width = this.mView.getDisplayController().mScreenWidth;
        }
        if (height <= 0) {
            height = this.mView.getDisplayController().mScreenHeight;
        }
        this.mView.getDisplayController().expand(url, width, height, shouldUseCustomClose, shouldLockOrientation);
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