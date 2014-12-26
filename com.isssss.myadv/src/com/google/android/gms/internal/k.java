package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.io.IOException;

public class k extends j {

    class a {
        private String ka;
        private boolean kb;

        public a(String str, boolean z) {
            this.ka = str;
            this.kb = z;
        }

        public String getId() {
            return this.ka;
        }

        public boolean isLimitAdTrackingEnabled() {
            return this.kb;
        }
    }

    private k(Context context, n nVar, o oVar) {
        super(context, nVar, oVar);
    }

    public static k a(String str, Context context) {
        n eVar = new e();
        a(str, context, eVar);
        return new k(context, eVar, new q(239));
    }

    protected void b(Context context) {
        long j = 1;
        super.b(context);
        try {
            a f = f(context);
            if (!f.isLimitAdTrackingEnabled()) {
                j = 0;
            }
            a(ApiEventType.API_MRAID_SEND_MAIL, j);
            String id = f.getId();
            if (id != null) {
                a(ApiEventType.API_MRAID_CREATE_CALENDAR_EVENT, id);
            }
        } catch (GooglePlayServicesNotAvailableException e) {
        } catch (IOException e2) {
            a(ApiEventType.API_MRAID_SEND_MAIL, 1);
        }
    }

    a f(Context context) throws IOException, GooglePlayServicesNotAvailableException {
        int i = 0;
        try {
            String str;
            Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
            String id = advertisingIdInfo.getId();
            if (id == null || !id.matches("^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$")) {
                str = id;
            } else {
                byte[] bArr = new byte[16];
                int i2 = 0;
                while (i < id.length()) {
                    if (i == 8 || i == 13 || i == 18 || i == 23) {
                        i++;
                    }
                    bArr[i2] = (byte) (Character.digit(id.charAt(i), ApiEventType.API_MRAID_GET_ORIENTATION) << 4 + Character.digit(id.charAt(i + 1), ApiEventType.API_MRAID_GET_ORIENTATION));
                    i2++;
                    i += 2;
                }
                str = this.jP.a(bArr, true);
            }
            return new a(str, advertisingIdInfo.isLimitAdTrackingEnabled());
        } catch (GooglePlayServicesRepairableException e) {
            throw new IOException(e);
        }
    }
}