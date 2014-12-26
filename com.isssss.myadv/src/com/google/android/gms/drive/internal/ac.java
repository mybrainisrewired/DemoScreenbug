package com.google.android.gms.drive.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.drive.events.ChangeEvent;
import com.google.android.gms.drive.events.ConflictEvent;
import com.millennialmedia.android.MMAdView;

public class ac implements Creator<OnEventResponse> {
    static void a(OnEventResponse onEventResponse, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, onEventResponse.xH);
        b.c(parcel, MMAdView.TRANSITION_UP, onEventResponse.ES);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, onEventResponse.FH, i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, onEventResponse.FI, i, false);
        b.F(parcel, p);
    }

    public OnEventResponse Q(Parcel parcel) {
        ConflictEvent conflictEvent = null;
        int i = 0;
        int o = a.o(parcel);
        ChangeEvent changeEvent = null;
        int i2 = 0;
        while (parcel.dataPosition() < o) {
            ChangeEvent changeEvent2;
            int i3;
            ConflictEvent conflictEvent2;
            int n = a.n(parcel);
            ConflictEvent conflictEvent3;
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    conflictEvent3 = conflictEvent;
                    changeEvent2 = changeEvent;
                    i3 = i;
                    i = a.g(parcel, n);
                    conflictEvent2 = conflictEvent3;
                    break;
                case MMAdView.TRANSITION_UP:
                    i = i2;
                    ChangeEvent changeEvent3 = changeEvent;
                    i3 = a.g(parcel, n);
                    conflictEvent2 = conflictEvent;
                    changeEvent2 = changeEvent3;
                    break;
                case MMAdView.TRANSITION_DOWN:
                    i3 = i;
                    i = i2;
                    conflictEvent3 = conflictEvent;
                    changeEvent2 = (ChangeEvent) a.a(parcel, n, ChangeEvent.CREATOR);
                    conflictEvent2 = conflictEvent3;
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    conflictEvent2 = (ConflictEvent) a.a(parcel, n, ConflictEvent.CREATOR);
                    changeEvent2 = changeEvent;
                    i3 = i;
                    i = i2;
                    break;
                default:
                    a.b(parcel, n);
                    conflictEvent2 = conflictEvent;
                    changeEvent2 = changeEvent;
                    i3 = i;
                    i = i2;
                    break;
            }
            i2 = i;
            i = i3;
            changeEvent = changeEvent2;
            conflictEvent = conflictEvent2;
        }
        if (parcel.dataPosition() == o) {
            return new OnEventResponse(i2, i, changeEvent, conflictEvent);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public OnEventResponse[] au(int i) {
        return new OnEventResponse[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return Q(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return au(x0);
    }
}