package com.inmobi.commons.internal;

import com.inmobi.commons.thinICE.icedatacollector.IceDataCollector;
import com.inmobi.commons.thinICE.icedatacollector.ThinICEListener;
import java.util.List;

// compiled from: ThinICE.java
static class b implements ThinICEListener {
    b() {
    }

    public void onSamplingTerminated(List list) {
        ThinICE.b(list);
        IceDataCollector.stop();
        ActivityRecognitionSampler.stop();
    }
}