package com.google.android.gms.common.api;

import android.os.Looper;
import com.google.android.gms.common.api.a.a;
import com.google.android.gms.common.api.a.c;
import java.util.ArrayList;
import java.util.List;

public final class Batch extends a<BatchResult> {
    private int AM;
    private boolean AN;
    private boolean AO;
    private final PendingResult<?>[] AP;
    private final Object li;

    public static final class Builder {
        private List<PendingResult<?>> AR;
        private Looper AS;

        public Builder(GoogleApiClient googleApiClient) {
            this.AR = new ArrayList();
            this.AS = googleApiClient.getLooper();
        }

        public <R extends Result> BatchResultToken<R> add(PendingResult<R> pendingResult) {
            BatchResultToken<R> batchResultToken = new BatchResultToken(this.AR.size());
            this.AR.add(pendingResult);
            return batchResultToken;
        }

        public Batch build() {
            return new Batch(this.AS, null);
        }
    }

    private Batch(List<PendingResult<?>> pendingResultList, Looper looper) {
        super(new c(looper));
        this.li = new Object();
        this.AM = pendingResultList.size();
        this.AP = new PendingResult[this.AM];
        int i = 0;
        while (i < pendingResultList.size()) {
            PendingResult pendingResult = (PendingResult) pendingResultList.get(i);
            this.AP[i] = pendingResult;
            pendingResult.a(new PendingResult.a() {
                public void l(Status status) {
                    synchronized (Batch.this.li) {
                        if (Batch.this.isCanceled()) {
                        } else {
                            if (status.isCanceled()) {
                                Batch.this.AO = true;
                            } else if (!status.isSuccess()) {
                                Batch.this.AN = true;
                            }
                            Batch.b(Batch.this);
                            if (Batch.this.AM == 0) {
                                if (Batch.this.AO) {
                                    super.cancel();
                                } else {
                                    Batch.this.a(new BatchResult(Batch.this.AN ? new Status(13) : Status.Bv, Batch.this.AP));
                                }
                            }
                        }
                    }
                }
            });
            i++;
        }
    }

    static /* synthetic */ int b(Batch batch) {
        int i = batch.AM;
        batch.AM = i - 1;
        return i;
    }

    public void cancel() {
        super.cancel();
        PendingResult[] pendingResultArr = this.AP;
        int length = pendingResultArr.length;
        int i = 0;
        while (i < length) {
            pendingResultArr[i].cancel();
            i++;
        }
    }

    public BatchResult createFailedResult(Status status) {
        return new BatchResult(status, this.AP);
    }

    public /* synthetic */ Result d(Status status) {
        return createFailedResult(status);
    }
}