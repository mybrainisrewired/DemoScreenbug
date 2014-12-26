package com.inmobi.commons.internal;

import android.content.Context;
import com.inmobi.commons.internal.ActivityRecognitionSampler.ActivitySample;
import com.inmobi.commons.thinICE.icedatacollector.Sample;
import java.util.List;

public interface PayloadCreator {
    String toPayloadString(List<Sample> list, List<ActivitySample> list2, Context context);
}