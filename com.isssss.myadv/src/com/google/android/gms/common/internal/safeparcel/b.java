package com.google.android.gms.common.internal.safeparcel;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.List;

public class b {
    private static int D(Parcel parcel, int i) {
        parcel.writeInt(-65536 | i);
        parcel.writeInt(0);
        return parcel.dataPosition();
    }

    private static void E(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        int i2 = dataPosition - i;
        parcel.setDataPosition(i - 4);
        parcel.writeInt(i2);
        parcel.setDataPosition(dataPosition);
    }

    public static void F(Parcel parcel, int i) {
        E(parcel, i);
    }

    public static void a(Parcel parcel, int i, byte b) {
        b(parcel, i, MMAdView.TRANSITION_RANDOM);
        parcel.writeInt(b);
    }

    public static void a(Parcel parcel, int i, double d) {
        b(parcel, i, ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
        parcel.writeDouble(d);
    }

    public static void a(Parcel parcel, int i, float f) {
        b(parcel, i, MMAdView.TRANSITION_RANDOM);
        parcel.writeFloat(f);
    }

    public static void a(Parcel parcel, int i, long j) {
        b(parcel, i, ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
        parcel.writeLong(j);
    }

    public static void a(Parcel parcel, int i, Bundle bundle, boolean z) {
        if (bundle != null) {
            int D = D(parcel, i);
            parcel.writeBundle(bundle);
            E(parcel, D);
        } else if (z) {
            b(parcel, i, 0);
        }
    }

    public static void a(Parcel parcel, int i, IBinder iBinder, boolean z) {
        if (iBinder != null) {
            int D = D(parcel, i);
            parcel.writeStrongBinder(iBinder);
            E(parcel, D);
        } else if (z) {
            b(parcel, i, 0);
        }
    }

    public static void a(Parcel parcel, int i, Parcel parcel2, boolean z) {
        if (parcel2 != null) {
            int D = D(parcel, i);
            parcel.appendFrom(parcel2, 0, parcel2.dataSize());
            E(parcel, D);
        } else if (z) {
            b(parcel, i, 0);
        }
    }

    public static void a(Parcel parcel, int i, Parcelable parcelable, int i2, boolean z) {
        if (parcelable != null) {
            int D = D(parcel, i);
            parcelable.writeToParcel(parcel, i2);
            E(parcel, D);
        } else if (z) {
            b(parcel, i, 0);
        }
    }

    public static void a(Parcel parcel, int i, Boolean bool, boolean z) {
        int i2 = 0;
        if (bool != null) {
            b(parcel, i, MMAdView.TRANSITION_RANDOM);
            if (bool.booleanValue()) {
                i2 = 1;
            }
            parcel.writeInt(i2);
        } else if (z) {
            b(parcel, i, 0);
        }
    }

    public static void a(Parcel parcel, int i, Integer num, boolean z) {
        if (num != null) {
            b(parcel, i, MMAdView.TRANSITION_RANDOM);
            parcel.writeInt(num.intValue());
        } else if (z) {
            b(parcel, i, 0);
        }
    }

    public static void a(Parcel parcel, int i, String str, boolean z) {
        if (str != null) {
            int D = D(parcel, i);
            parcel.writeString(str);
            E(parcel, D);
        } else if (z) {
            b(parcel, i, 0);
        }
    }

    public static void a(Parcel parcel, int i, List<String> list, boolean z) {
        if (list != null) {
            int D = D(parcel, i);
            parcel.writeStringList(list);
            E(parcel, D);
        } else if (z) {
            b(parcel, i, 0);
        }
    }

    public static void a(Parcel parcel, int i, short s) {
        b(parcel, i, MMAdView.TRANSITION_RANDOM);
        parcel.writeInt(s);
    }

    public static void a(Parcel parcel, int i, boolean z) {
        b(parcel, i, MMAdView.TRANSITION_RANDOM);
        parcel.writeInt(z ? 1 : 0);
    }

    public static void a(Parcel parcel, int i, byte[] bArr, boolean z) {
        if (bArr != null) {
            int D = D(parcel, i);
            parcel.writeByteArray(bArr);
            E(parcel, D);
        } else if (z) {
            b(parcel, i, 0);
        }
    }

    public static void a(Parcel parcel, int i, int[] iArr, boolean z) {
        if (iArr != null) {
            int D = D(parcel, i);
            parcel.writeIntArray(iArr);
            E(parcel, D);
        } else if (z) {
            b(parcel, i, 0);
        }
    }

    public static <T extends Parcelable> void a(Parcel parcel, int i, T[] tArr, int i2, boolean z) {
        if (tArr != null) {
            int D = D(parcel, i);
            int length = tArr.length;
            parcel.writeInt(length);
            int i3 = 0;
            while (i3 < length) {
                Parcelable parcelable = tArr[i3];
                if (parcelable == null) {
                    parcel.writeInt(0);
                } else {
                    a(parcel, parcelable, i2);
                }
                i3++;
            }
            E(parcel, D);
        } else if (z) {
            b(parcel, i, 0);
        }
    }

    public static void a(Parcel parcel, int i, String[] strArr, boolean z) {
        if (strArr != null) {
            int D = D(parcel, i);
            parcel.writeStringArray(strArr);
            E(parcel, D);
        } else if (z) {
            b(parcel, i, 0);
        }
    }

    public static void a(Parcel parcel, int i, byte[][] bArr, boolean z) {
        int i2 = 0;
        if (bArr != null) {
            int D = D(parcel, i);
            int length = bArr.length;
            parcel.writeInt(length);
            while (i2 < length) {
                parcel.writeByteArray(bArr[i2]);
                i2++;
            }
            E(parcel, D);
        } else if (z) {
            b(parcel, i, 0);
        }
    }

    private static <T extends Parcelable> void a(Parcel parcel, T t, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(1);
        int dataPosition2 = parcel.dataPosition();
        t.writeToParcel(parcel, i);
        int dataPosition3 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition3 - dataPosition2);
        parcel.setDataPosition(dataPosition3);
    }

    private static void b(Parcel parcel, int i, int i2) {
        if (i2 >= 65535) {
            parcel.writeInt(-65536 | i);
            parcel.writeInt(i2);
        } else {
            parcel.writeInt((i2 << 16) | i);
        }
    }

    public static <T extends Parcelable> void b(Parcel parcel, int i, List<T> list, boolean z) {
        if (list != null) {
            int D = D(parcel, i);
            int size = list.size();
            parcel.writeInt(size);
            int i2 = 0;
            while (i2 < size) {
                Parcelable parcelable = (Parcelable) list.get(i2);
                if (parcelable == null) {
                    parcel.writeInt(0);
                } else {
                    a(parcel, parcelable, 0);
                }
                i2++;
            }
            E(parcel, D);
        } else if (z) {
            b(parcel, i, 0);
        }
    }

    public static void c(Parcel parcel, int i, int i2) {
        b(parcel, i, MMAdView.TRANSITION_RANDOM);
        parcel.writeInt(i2);
    }

    public static void c(Parcel parcel, int i, List list, boolean z) {
        if (list != null) {
            int D = D(parcel, i);
            parcel.writeList(list);
            E(parcel, D);
        } else if (z) {
            b(parcel, i, 0);
        }
    }

    public static int p(Parcel parcel) {
        return D(parcel, 20293);
    }
}