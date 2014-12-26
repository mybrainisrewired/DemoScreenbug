package com.mopub.nativeads;

import com.mopub.common.Preconditions.NoThrow;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MoPubNativeAdPositioning {

    public static class MoPubClientPositioning {
        public static final int NO_REPEAT = Integer.MAX_VALUE;
        private final ArrayList<Integer> mFixedPositions;
        private int mRepeatInterval;

        public MoPubClientPositioning() {
            this.mFixedPositions = new ArrayList();
            this.mRepeatInterval = Integer.MAX_VALUE;
        }

        public com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning addFixedPosition(int position) {
            if (NoThrow.checkArgument(position >= 0)) {
                int index = Collections.binarySearch(this.mFixedPositions, Integer.valueOf(position));
                if (index < 0) {
                    this.mFixedPositions.add(index ^ -1, Integer.valueOf(position));
                }
            }
            return this;
        }

        public com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning enableRepeatingPositions(int interval) {
            boolean z = true;
            if (interval <= 1) {
                z = false;
            }
            if (NoThrow.checkArgument(z, "Repeating interval must be greater than 1")) {
                this.mRepeatInterval = interval;
            } else {
                this.mRepeatInterval = Integer.MAX_VALUE;
            }
            return this;
        }

        List<Integer> getFixedPositions() {
            return this.mFixedPositions;
        }

        int getRepeatingInterval() {
            return this.mRepeatInterval;
        }
    }

    public static class MoPubServerPositioning {
    }

    @Deprecated
    public static final class Builder extends com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning {
        public com.mopub.nativeads.MoPubNativeAdPositioning.Builder addFixedPosition(int position) {
            super.addFixedPosition(position);
            return this;
        }

        @Deprecated
        public com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning build() {
            return this;
        }

        public com.mopub.nativeads.MoPubNativeAdPositioning.Builder enableRepeatingPositions(int interval) {
            super.enableRepeatingPositions(interval);
            return this;
        }
    }

    public static MoPubClientPositioning clientPositioning() {
        return new MoPubClientPositioning();
    }

    static MoPubClientPositioning clone(MoPubClientPositioning positioning) {
        MoPubClientPositioning clone = new MoPubClientPositioning();
        clone.mFixedPositions.addAll(positioning.mFixedPositions);
        clone.mRepeatInterval = positioning.mRepeatInterval;
        return clone;
    }

    @Deprecated
    public static Builder newBuilder() {
        return new Builder();
    }

    public static MoPubServerPositioning serverPositioning() {
        return new MoPubServerPositioning();
    }
}