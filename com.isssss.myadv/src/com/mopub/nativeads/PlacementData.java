package com.mopub.nativeads;

import com.mopub.common.logging.MoPubLog;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;
import java.util.Iterator;
import java.util.List;

class PlacementData {
    private static final int MAX_ADS = 200;
    public static final int NOT_FOUND = -1;
    private final NativeAdData[] mAdDataObjects;
    private final int[] mAdjustedAdPositions;
    private int mDesiredCount;
    private final int[] mDesiredInsertionPositions;
    private final int[] mDesiredOriginalPositions;
    private final int[] mOriginalAdPositions;
    private int mPlacedCount;

    private PlacementData(int[] desiredInsertionPositions) {
        this.mDesiredOriginalPositions = new int[200];
        this.mDesiredInsertionPositions = new int[200];
        this.mDesiredCount = 0;
        this.mOriginalAdPositions = new int[200];
        this.mAdjustedAdPositions = new int[200];
        this.mAdDataObjects = new NativeAdData[200];
        this.mPlacedCount = 0;
        this.mDesiredCount = Math.min(desiredInsertionPositions.length, MAX_ADS);
        System.arraycopy(desiredInsertionPositions, 0, this.mDesiredInsertionPositions, 0, this.mDesiredCount);
        System.arraycopy(desiredInsertionPositions, 0, this.mDesiredOriginalPositions, 0, this.mDesiredCount);
    }

    private static int binarySearch(int[] array, int startIndex, int endIndex, int value) {
        int lo = startIndex;
        int hi = endIndex - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int midVal = array[mid];
            if (midVal < value) {
                lo = mid + 1;
            } else if (midVal <= value) {
                return mid;
            } else {
                hi = mid - 1;
            }
        }
        return lo ^ -1;
    }

    private static int binarySearchFirstEquals(int[] array, int count, int value) {
        int index = binarySearch(array, 0, count, value);
        if (index < 0) {
            return index ^ -1;
        }
        int duplicateValue = array[index];
        while (index >= 0 && array[index] == duplicateValue) {
            index--;
        }
        return index + 1;
    }

    private static int binarySearchGreaterThan(int[] array, int count, int value) {
        int index = binarySearch(array, 0, count, value);
        if (index < 0) {
            return index ^ -1;
        }
        int duplicateValue = array[index];
        while (index < count && array[index] == duplicateValue) {
            index++;
        }
        return index;
    }

    static PlacementData empty() {
        return new PlacementData(new int[0]);
    }

    static PlacementData fromAdPositioning(MoPubClientPositioning adPositioning) {
        int numAds;
        List<Integer> fixed = adPositioning.getFixedPositions();
        int interval = adPositioning.getRepeatingInterval();
        int size = interval == Integer.MAX_VALUE ? fixed.size() : MAX_ADS;
        int[] desiredInsertionPositions = new int[size];
        int numAds2 = 0;
        int lastPos = 0;
        Iterator it = fixed.iterator();
        while (it.hasNext()) {
            lastPos = ((Integer) it.next()).intValue() - numAds2;
            numAds = numAds2 + 1;
            desiredInsertionPositions[numAds2] = lastPos;
            numAds2 = numAds;
        }
        numAds = numAds2;
        while (numAds < size) {
            lastPos = lastPos + interval - 1;
            numAds2 = numAds + 1;
            desiredInsertionPositions[numAds] = lastPos;
            numAds = numAds2;
        }
        return new PlacementData(desiredInsertionPositions);
    }

    void clearAds() {
        if (this.mPlacedCount != 0) {
            clearAdsInRange(0, this.mAdjustedAdPositions[this.mPlacedCount - 1] + 1);
        }
    }

    int clearAdsInRange(int adjustedStartRange, int adjustedEndRange) {
        int[] clearOriginalPositions = new int[this.mPlacedCount];
        int[] clearAdjustedPositions = new int[this.mPlacedCount];
        int clearCount = 0;
        int i = 0;
        while (i < this.mPlacedCount) {
            int originalPosition = this.mOriginalAdPositions[i];
            int adjustedPosition = this.mAdjustedAdPositions[i];
            if (adjustedStartRange <= adjustedPosition && adjustedPosition < adjustedEndRange) {
                clearOriginalPositions[clearCount] = originalPosition;
                clearAdjustedPositions[clearCount] = adjustedPosition - clearCount;
                this.mAdDataObjects[i].getAd().destroy();
                this.mAdDataObjects[i] = null;
                clearCount++;
            } else if (clearCount > 0) {
                int newIndex = i - clearCount;
                this.mOriginalAdPositions[newIndex] = originalPosition;
                this.mAdjustedAdPositions[newIndex] = adjustedPosition - clearCount;
                this.mAdDataObjects[newIndex] = this.mAdDataObjects[i];
            }
            i++;
        }
        if (clearCount == 0) {
            return 0;
        }
        int desiredIndex = binarySearchFirstEquals(this.mDesiredInsertionPositions, this.mDesiredCount, clearAdjustedPositions[0]);
        i = this.mDesiredCount - 1;
        while (i >= desiredIndex) {
            this.mDesiredOriginalPositions[i + clearCount] = this.mDesiredOriginalPositions[i];
            this.mDesiredInsertionPositions[i + clearCount] = this.mDesiredInsertionPositions[i] - clearCount;
            i--;
        }
        i = 0;
        while (i < clearCount) {
            this.mDesiredOriginalPositions[desiredIndex + i] = clearOriginalPositions[i];
            this.mDesiredInsertionPositions[desiredIndex + i] = clearAdjustedPositions[i];
            i++;
        }
        this.mDesiredCount += clearCount;
        this.mPlacedCount -= clearCount;
        return clearCount;
    }

    int getAdjustedCount(int originalCount) {
        return originalCount == 0 ? 0 : getAdjustedPosition(originalCount - 1) + 1;
    }

    int getAdjustedPosition(int originalPosition) {
        return originalPosition + binarySearchGreaterThan(this.mOriginalAdPositions, this.mPlacedCount, originalPosition);
    }

    int getOriginalCount(int count) {
        if (count == 0) {
            return 0;
        }
        int originalPos = getOriginalPosition(count - 1);
        return originalPos != -1 ? originalPos + 1 : NOT_FOUND;
    }

    int getOriginalPosition(int position) {
        int index = binarySearch(this.mAdjustedAdPositions, 0, this.mPlacedCount, position);
        return index < 0 ? position - index ^ -1 : NOT_FOUND;
    }

    NativeAdData getPlacedAd(int position) {
        int index = binarySearch(this.mAdjustedAdPositions, 0, this.mPlacedCount, position);
        return index < 0 ? null : this.mAdDataObjects[index];
    }

    int[] getPlacedAdPositions() {
        int[] positions = new int[this.mPlacedCount];
        System.arraycopy(this.mAdjustedAdPositions, 0, positions, 0, this.mPlacedCount);
        return positions;
    }

    void insertItem(int originalPosition) {
        int i = binarySearchFirstEquals(this.mDesiredOriginalPositions, this.mDesiredCount, originalPosition);
        while (i < this.mDesiredCount) {
            int[] iArr = this.mDesiredOriginalPositions;
            iArr[i] = iArr[i] + 1;
            iArr = this.mDesiredInsertionPositions;
            iArr[i] = iArr[i] + 1;
            i++;
        }
        i = binarySearchFirstEquals(this.mOriginalAdPositions, this.mPlacedCount, originalPosition);
        while (i < this.mPlacedCount) {
            iArr = this.mOriginalAdPositions;
            iArr[i] = iArr[i] + 1;
            iArr = this.mAdjustedAdPositions;
            iArr[i] = iArr[i] + 1;
            i++;
        }
    }

    boolean isPlacedAd(int position) {
        return binarySearch(this.mAdjustedAdPositions, 0, this.mPlacedCount, position) >= 0;
    }

    void moveItem(int originalPosition, int newPosition) {
        removeItem(originalPosition);
        insertItem(newPosition);
    }

    int nextInsertionPosition(int position) {
        int index = binarySearchGreaterThan(this.mDesiredInsertionPositions, this.mDesiredCount, position);
        return index == this.mDesiredCount ? NOT_FOUND : this.mDesiredInsertionPositions[index];
    }

    void placeAd(int adjustedPosition, NativeAdData adData) {
        int desiredIndex = binarySearchFirstEquals(this.mDesiredInsertionPositions, this.mDesiredCount, adjustedPosition);
        if (desiredIndex == this.mDesiredCount || this.mDesiredInsertionPositions[desiredIndex] != adjustedPosition) {
            MoPubLog.w("Attempted to insert an ad at an invalid position");
        } else {
            int num;
            int[] iArr;
            int originalPosition = this.mDesiredOriginalPositions[desiredIndex];
            int placeIndex = binarySearchGreaterThan(this.mOriginalAdPositions, this.mPlacedCount, originalPosition);
            if (placeIndex < this.mPlacedCount) {
                num = this.mPlacedCount - placeIndex;
                System.arraycopy(this.mOriginalAdPositions, placeIndex, this.mOriginalAdPositions, placeIndex + 1, num);
                System.arraycopy(this.mAdjustedAdPositions, placeIndex, this.mAdjustedAdPositions, placeIndex + 1, num);
                System.arraycopy(this.mAdDataObjects, placeIndex, this.mAdDataObjects, placeIndex + 1, num);
            }
            this.mOriginalAdPositions[placeIndex] = originalPosition;
            this.mAdjustedAdPositions[placeIndex] = adjustedPosition;
            this.mAdDataObjects[placeIndex] = adData;
            this.mPlacedCount++;
            num = this.mDesiredCount - desiredIndex - 1;
            System.arraycopy(this.mDesiredInsertionPositions, desiredIndex + 1, this.mDesiredInsertionPositions, desiredIndex, num);
            System.arraycopy(this.mDesiredOriginalPositions, desiredIndex + 1, this.mDesiredOriginalPositions, desiredIndex, num);
            this.mDesiredCount--;
            int i = desiredIndex;
            while (i < this.mDesiredCount) {
                iArr = this.mDesiredInsertionPositions;
                iArr[i] = iArr[i] + 1;
                i++;
            }
            i = placeIndex + 1;
            while (i < this.mPlacedCount) {
                iArr = this.mAdjustedAdPositions;
                iArr[i] = iArr[i] + 1;
                i++;
            }
        }
    }

    int previousInsertionPosition(int position) {
        int index = binarySearchFirstEquals(this.mDesiredInsertionPositions, this.mDesiredCount, position);
        return index == 0 ? NOT_FOUND : this.mDesiredInsertionPositions[index - 1];
    }

    void removeItem(int originalPosition) {
        int i = binarySearchGreaterThan(this.mDesiredOriginalPositions, this.mDesiredCount, originalPosition);
        while (i < this.mDesiredCount) {
            int[] iArr = this.mDesiredOriginalPositions;
            iArr[i] = iArr[i] - 1;
            iArr = this.mDesiredInsertionPositions;
            iArr[i] = iArr[i] - 1;
            i++;
        }
        i = binarySearchGreaterThan(this.mOriginalAdPositions, this.mPlacedCount, originalPosition);
        while (i < this.mPlacedCount) {
            iArr = this.mOriginalAdPositions;
            iArr[i] = iArr[i] - 1;
            iArr = this.mAdjustedAdPositions;
            iArr[i] = iArr[i] - 1;
            i++;
        }
    }

    boolean shouldPlaceAd(int position) {
        return binarySearch(this.mDesiredInsertionPositions, 0, this.mDesiredCount, position) >= 0;
    }
}