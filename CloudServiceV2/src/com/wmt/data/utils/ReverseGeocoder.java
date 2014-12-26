package com.wmt.data.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import com.wmt.util.Utils;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class ReverseGeocoder {
    public static final int EARTH_RADIUS_METERS = 6378137;
    private static final String GEO_CACHE_FILE = "rev_geocoding";
    private static final int GEO_CACHE_MAX_BYTES = 512000;
    private static final int GEO_CACHE_MAX_ENTRIES = 1000;
    private static final int GEO_CACHE_VERSION = 0;
    public static final int LAT_MAX = 90;
    public static final int LAT_MIN = -90;
    public static final int LON_MAX = 180;
    public static final int LON_MIN = -180;
    private static final int MAX_COUNTRY_NAME_LENGTH = 8;
    private static final int MAX_LOCALITY_MILE_RANGE = 20;
    private static final String TAG = "ReverseGeocoder";
    private static Address sCurrentAddress;
    private ConnectivityManager mConnectivityManager;
    private Context mContext;
    private BlobCache mGeoCache;
    private Geocoder mGeocoder;

    public static class SetLatLong {
        public double mMaxLatLatitude;
        public double mMaxLatLongitude;
        public double mMaxLonLatitude;
        public double mMaxLonLongitude;
        public double mMinLatLatitude;
        public double mMinLatLongitude;
        public double mMinLonLatitude;
        public double mMinLonLongitude;

        public SetLatLong() {
            this.mMinLatLatitude = 90.0d;
            this.mMaxLatLatitude = -90.0d;
            this.mMinLonLongitude = 180.0d;
            this.mMaxLonLongitude = -180.0d;
        }
    }

    public ReverseGeocoder(Context context) {
        this.mContext = context;
        this.mGeocoder = new Geocoder(this.mContext);
        this.mGeoCache = BlobCacheManager.getCache(context, GEO_CACHE_FILE, GEO_CACHE_MAX_ENTRIES, GEO_CACHE_MAX_BYTES, GEO_CACHE_VERSION);
        this.mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
    }

    private String checkNull(String locality) {
        if (locality == null) {
            return "";
        }
        return locality.equals("null") ? "" : locality;
    }

    private String getLocalityAdminForAddress(Address addr, boolean approxLocation) {
        if (addr == null) {
            return "";
        }
        String localityAdminStr = addr.getLocality();
        if (localityAdminStr == null || "null".equals(localityAdminStr)) {
            return null;
        }
        String adminArea;
        if (approxLocation) {
            adminArea = addr.getAdminArea();
        } else {
            adminArea = addr.getAdminArea();
        }
        return (adminArea == null || adminArea.length() <= 0) ? localityAdminStr : localityAdminStr + ", " + adminArea;
    }

    public static final String readUTF(DataInputStream dis) throws IOException {
        String retVal = dis.readUTF();
        return retVal.length() == 0 ? null : retVal;
    }

    private String valueIfEqual(String a, String b) {
        return (a == null || b == null || !a.equalsIgnoreCase(b)) ? null : a;
    }

    public static final void writeUTF(DataOutputStream dos, String string) throws IOException {
        if (string == null) {
            dos.writeUTF("");
        } else {
            dos.writeUTF(string);
        }
    }

    public String computeAddress(SetLatLong set) {
        double setMinLatitude = set.mMinLatLatitude;
        double setMinLongitude = set.mMinLatLongitude;
        double setMaxLatitude = set.mMaxLatLatitude;
        double setMaxLongitude = set.mMaxLatLongitude;
        if (Math.abs(set.mMaxLatLatitude - set.mMinLatLatitude) < Math.abs(set.mMaxLonLongitude - set.mMinLonLongitude)) {
            setMinLatitude = set.mMinLonLatitude;
            setMinLongitude = set.mMinLonLongitude;
            setMaxLatitude = set.mMaxLonLatitude;
            setMaxLongitude = set.mMaxLonLongitude;
        }
        Address addr1 = lookupAddress(setMinLatitude, setMinLongitude, true);
        Address addr2 = lookupAddress(setMaxLatitude, setMaxLongitude, true);
        if (addr1 == null) {
            addr1 = addr2;
        }
        if (addr2 == null) {
            addr2 = addr1;
        }
        if (addr1 == null || addr2 == null) {
            return null;
        }
        String closestCommonLocation;
        LocationManager locationManager = (LocationManager) this.mContext.getSystemService("location");
        Location location = null;
        List<String> providers = locationManager.getAllProviders();
        int i = GEO_CACHE_VERSION;
        while (i < providers.size()) {
            String provider = (String) providers.get(i);
            location = provider != null ? locationManager.getLastKnownLocation(provider) : null;
            if (location != null) {
                break;
            }
            i++;
        }
        String currentCity = "";
        String currentAdminArea = "";
        String currentCountry = Locale.getDefault().getCountry();
        if (location != null) {
            Address currentAddress = lookupAddress(location.getLatitude(), location.getLongitude(), true);
            if (currentAddress == null) {
                currentAddress = sCurrentAddress;
            } else {
                sCurrentAddress = currentAddress;
            }
            if (!(currentAddress == null || currentAddress.getCountryCode() == null)) {
                currentCity = checkNull(currentAddress.getLocality());
                currentCountry = checkNull(currentAddress.getCountryCode());
                currentAdminArea = checkNull(currentAddress.getAdminArea());
            }
        }
        String addr1Locality = checkNull(addr1.getLocality());
        String addr2Locality = checkNull(addr2.getLocality());
        String addr1AdminArea = checkNull(addr1.getAdminArea());
        String addr2AdminArea = checkNull(addr2.getAdminArea());
        String addr1CountryCode = checkNull(addr1.getCountryCode());
        String addr2CountryCode = checkNull(addr2.getCountryCode());
        if (currentCity.equals(addr1Locality) || currentCity.equals(addr2Locality)) {
            String str = currentCity;
            if (currentCity.equals(addr1Locality)) {
                str = addr2Locality;
                if (str.length() == 0) {
                    str = addr2AdminArea;
                    if (!currentCountry.equals(addr2CountryCode)) {
                        str = str + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + addr2CountryCode;
                    }
                }
                addr2Locality = addr1Locality;
                addr2AdminArea = addr1AdminArea;
                addr2CountryCode = addr1CountryCode;
            } else {
                str = addr1Locality;
                if (str.length() == 0) {
                    str = addr1AdminArea;
                    if (!currentCountry.equals(addr1CountryCode)) {
                        str = str + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + addr1CountryCode;
                    }
                }
                addr1Locality = addr2Locality;
                addr1AdminArea = addr2AdminArea;
                addr1CountryCode = addr2CountryCode;
            }
            closestCommonLocation = valueIfEqual(addr1.getAddressLine(GEO_CACHE_VERSION), addr2.getAddressLine(GEO_CACHE_VERSION));
            if (closestCommonLocation != null && !"null".equals(closestCommonLocation)) {
                return !currentCity.equals(str) ? closestCommonLocation + " - " + str : closestCommonLocation;
            } else {
                closestCommonLocation = valueIfEqual(addr1.getThoroughfare(), addr2.getThoroughfare());
                if (!(closestCommonLocation == null || "null".equals(closestCommonLocation))) {
                    return closestCommonLocation;
                }
            }
        }
        closestCommonLocation = valueIfEqual(addr1Locality, addr2Locality);
        String countryCode;
        if (closestCommonLocation == null || "".equals(closestCommonLocation)) {
            if (currentAdminArea.equals(addr1AdminArea) && currentAdminArea.equals(addr2AdminArea)) {
                if ("".equals(addr1Locality)) {
                    addr1Locality = addr2Locality;
                }
                if ("".equals(addr2Locality)) {
                    addr2Locality = addr1Locality;
                }
                if (!"".equals(addr1Locality)) {
                    return addr1Locality.equals(addr2Locality) ? addr1Locality + ", " + currentAdminArea : addr1Locality + " - " + addr2Locality;
                }
            }
            float[] distanceFloat = new float[1];
            Location.distanceBetween(setMinLatitude, setMinLongitude, setMaxLatitude, setMaxLongitude, distanceFloat);
            if (((int) Utils.toMile((double) distanceFloat[0])) < 20) {
                closestCommonLocation = getLocalityAdminForAddress(addr1, true);
                if (closestCommonLocation != null) {
                    return closestCommonLocation;
                }
                closestCommonLocation = getLocalityAdminForAddress(addr2, true);
                if (closestCommonLocation != null) {
                    return closestCommonLocation;
                }
            }
            closestCommonLocation = valueIfEqual(addr1AdminArea, addr2AdminArea);
            if (closestCommonLocation == null || "".equals(closestCommonLocation)) {
                closestCommonLocation = valueIfEqual(addr1CountryCode, addr2CountryCode);
                if (closestCommonLocation != null && !"".equals(closestCommonLocation)) {
                    return closestCommonLocation;
                }
                String addr1Country = addr1.getCountryName();
                String addr2Country = addr2.getCountryName();
                if (addr1Country == null) {
                    addr1Country = addr1CountryCode;
                }
                if (addr2Country == null) {
                    addr2Country = addr2CountryCode;
                }
                if (addr1Country == null || addr2Country == null) {
                    return null;
                }
                return (addr1Country.length() > 8 || addr2Country.length() > 8) ? addr1CountryCode + " - " + addr2CountryCode : addr1Country + " - " + addr2Country;
            } else {
                countryCode = addr1CountryCode;
                return (countryCode.equals(currentCountry) || countryCode == null || countryCode.length() <= 0) ? closestCommonLocation : closestCommonLocation + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + countryCode;
            }
        } else {
            String adminArea = addr1AdminArea;
            countryCode = addr1CountryCode;
            if (adminArea == null || adminArea.length() <= 0) {
                return closestCommonLocation;
            }
            return !countryCode.equals(currentCountry) ? closestCommonLocation + ", " + adminArea + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + countryCode : closestCommonLocation + ", " + adminArea;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.location.Address lookupAddress(double r25_latitude, double r27_longitude, boolean r29_useCache) {
        throw new UnsupportedOperationException("Method not decompiled: com.wmt.data.utils.ReverseGeocoder.lookupAddress(double, double, boolean):android.location.Address");
        /*
        r24 = this;
        r3 = 4636033603912859648; // 0x4056800000000000 float:0.0 double:90.0;
        r3 = r3 + r25;
        r5 = 4611686018427387904; // 0x4000000000000000 float:0.0 double:2.0;
        r3 = r3 * r5;
        r5 = 4636033603912859648; // 0x4056800000000000 float:0.0 double:90.0;
        r3 = r3 * r5;
        r5 = 4640537203540230144; // 0x4066800000000000 float:0.0 double:180.0;
        r5 = r5 + r27;
        r3 = r3 + r5;
        r5 = 4708606483430899712; // 0x415854a640000000 float:2.0 double:6378137.0;
        r3 = r3 * r5;
        r0 = (long) r3;
        r19 = r0;
        r12 = 0;
        if (r29 == 0) goto L_0x0034;
    L_0x0024:
        r0 = r24;
        r3 = r0.mGeoCache;	 Catch:{ Exception -> 0x0107 }
        if (r3 == 0) goto L_0x0034;
    L_0x002a:
        r0 = r24;
        r3 = r0.mGeoCache;	 Catch:{ Exception -> 0x0107 }
        r0 = r19;
        r12 = r3.lookup(r0);	 Catch:{ Exception -> 0x0107 }
    L_0x0034:
        r9 = 0;
        r0 = r24;
        r3 = r0.mConnectivityManager;	 Catch:{ Exception -> 0x0107 }
        r21 = r3.getActiveNetworkInfo();	 Catch:{ Exception -> 0x0107 }
        if (r12 == 0) goto L_0x0042;
    L_0x003f:
        r3 = r12.length;	 Catch:{ Exception -> 0x0107 }
        if (r3 != 0) goto L_0x010b;
    L_0x0042:
        if (r21 == 0) goto L_0x004a;
    L_0x0044:
        r3 = r21.isConnected();	 Catch:{ Exception -> 0x0107 }
        if (r3 != 0) goto L_0x004c;
    L_0x004a:
        r9 = 0;
    L_0x004b:
        return r9;
    L_0x004c:
        r0 = r24;
        r3 = r0.mGeocoder;	 Catch:{ Exception -> 0x0107 }
        r8 = 1;
        r4 = r25;
        r6 = r27;
        r10 = r3.getFromLocation(r4, r6, r8);	 Catch:{ Exception -> 0x0107 }
        r3 = r10.isEmpty();	 Catch:{ Exception -> 0x0107 }
        if (r3 != 0) goto L_0x004b;
    L_0x005f:
        r3 = 0;
        r9 = r10.get(r3);	 Catch:{ Exception -> 0x0107 }
        r9 = (android.location.Address) r9;	 Catch:{ Exception -> 0x0107 }
        r11 = new java.io.ByteArrayOutputStream;	 Catch:{ Exception -> 0x0107 }
        r11.<init>();	 Catch:{ Exception -> 0x0107 }
        r15 = new java.io.DataOutputStream;	 Catch:{ Exception -> 0x0107 }
        r15.<init>(r11);	 Catch:{ Exception -> 0x0107 }
        r18 = r9.getLocale();	 Catch:{ Exception -> 0x0107 }
        r3 = r18.getLanguage();	 Catch:{ Exception -> 0x0107 }
        writeUTF(r15, r3);	 Catch:{ Exception -> 0x0107 }
        r3 = r18.getCountry();	 Catch:{ Exception -> 0x0107 }
        writeUTF(r15, r3);	 Catch:{ Exception -> 0x0107 }
        r3 = r18.getVariant();	 Catch:{ Exception -> 0x0107 }
        writeUTF(r15, r3);	 Catch:{ Exception -> 0x0107 }
        r3 = r9.getThoroughfare();	 Catch:{ Exception -> 0x0107 }
        writeUTF(r15, r3);	 Catch:{ Exception -> 0x0107 }
        r22 = r9.getMaxAddressLineIndex();	 Catch:{ Exception -> 0x0107 }
        r0 = r22;
        r15.writeInt(r0);	 Catch:{ Exception -> 0x0107 }
        r16 = 0;
    L_0x009b:
        r0 = r16;
        r1 = r22;
        if (r0 >= r1) goto L_0x00ad;
    L_0x00a1:
        r0 = r16;
        r3 = r9.getAddressLine(r0);	 Catch:{ Exception -> 0x0107 }
        writeUTF(r15, r3);	 Catch:{ Exception -> 0x0107 }
        r16 = r16 + 1;
        goto L_0x009b;
    L_0x00ad:
        r3 = r9.getFeatureName();	 Catch:{ Exception -> 0x0107 }
        writeUTF(r15, r3);	 Catch:{ Exception -> 0x0107 }
        r3 = r9.getLocality();	 Catch:{ Exception -> 0x0107 }
        writeUTF(r15, r3);	 Catch:{ Exception -> 0x0107 }
        r3 = r9.getAdminArea();	 Catch:{ Exception -> 0x0107 }
        writeUTF(r15, r3);	 Catch:{ Exception -> 0x0107 }
        r3 = r9.getSubAdminArea();	 Catch:{ Exception -> 0x0107 }
        writeUTF(r15, r3);	 Catch:{ Exception -> 0x0107 }
        r3 = r9.getCountryName();	 Catch:{ Exception -> 0x0107 }
        writeUTF(r15, r3);	 Catch:{ Exception -> 0x0107 }
        r3 = r9.getCountryCode();	 Catch:{ Exception -> 0x0107 }
        writeUTF(r15, r3);	 Catch:{ Exception -> 0x0107 }
        r3 = r9.getPostalCode();	 Catch:{ Exception -> 0x0107 }
        writeUTF(r15, r3);	 Catch:{ Exception -> 0x0107 }
        r3 = r9.getPhone();	 Catch:{ Exception -> 0x0107 }
        writeUTF(r15, r3);	 Catch:{ Exception -> 0x0107 }
        r3 = r9.getUrl();	 Catch:{ Exception -> 0x0107 }
        writeUTF(r15, r3);	 Catch:{ Exception -> 0x0107 }
        r15.flush();	 Catch:{ Exception -> 0x0107 }
        r0 = r24;
        r3 = r0.mGeoCache;	 Catch:{ Exception -> 0x0107 }
        if (r3 == 0) goto L_0x0102;
    L_0x00f5:
        r0 = r24;
        r3 = r0.mGeoCache;	 Catch:{ Exception -> 0x0107 }
        r4 = r11.toByteArray();	 Catch:{ Exception -> 0x0107 }
        r0 = r19;
        r3.insert(r0, r4);	 Catch:{ Exception -> 0x0107 }
    L_0x0102:
        r15.close();	 Catch:{ Exception -> 0x0107 }
        goto L_0x004b;
    L_0x0107:
        r3 = move-exception;
        r9 = 0;
        goto L_0x004b;
    L_0x010b:
        r14 = new java.io.DataInputStream;	 Catch:{ Exception -> 0x0107 }
        r3 = new java.io.ByteArrayInputStream;	 Catch:{ Exception -> 0x0107 }
        r3.<init>(r12);	 Catch:{ Exception -> 0x0107 }
        r14.<init>(r3);	 Catch:{ Exception -> 0x0107 }
        r17 = readUTF(r14);	 Catch:{ Exception -> 0x0107 }
        r13 = readUTF(r14);	 Catch:{ Exception -> 0x0107 }
        r23 = readUTF(r14);	 Catch:{ Exception -> 0x0107 }
        r18 = 0;
        if (r17 == 0) goto L_0x0130;
    L_0x0125:
        if (r13 != 0) goto L_0x0152;
    L_0x0127:
        r18 = new java.util.Locale;	 Catch:{ Exception -> 0x0107 }
        r0 = r18;
        r1 = r17;
        r0.<init>(r1);	 Catch:{ Exception -> 0x0107 }
    L_0x0130:
        r3 = r18.getLanguage();	 Catch:{ Exception -> 0x0107 }
        r4 = java.util.Locale.getDefault();	 Catch:{ Exception -> 0x0107 }
        r4 = r4.getLanguage();	 Catch:{ Exception -> 0x0107 }
        r3 = r3.equals(r4);	 Catch:{ Exception -> 0x0107 }
        if (r3 != 0) goto L_0x016a;
    L_0x0142:
        r14.close();	 Catch:{ Exception -> 0x0107 }
        r8 = 0;
        r3 = r24;
        r4 = r25;
        r6 = r27;
        r9 = r3.lookupAddress(r4, r6, r8);	 Catch:{ Exception -> 0x0107 }
        goto L_0x004b;
    L_0x0152:
        if (r23 != 0) goto L_0x015e;
    L_0x0154:
        r18 = new java.util.Locale;	 Catch:{ Exception -> 0x0107 }
        r0 = r18;
        r1 = r17;
        r0.<init>(r1, r13);	 Catch:{ Exception -> 0x0107 }
        goto L_0x0130;
    L_0x015e:
        r18 = new java.util.Locale;	 Catch:{ Exception -> 0x0107 }
        r0 = r18;
        r1 = r17;
        r2 = r23;
        r0.<init>(r1, r13, r2);	 Catch:{ Exception -> 0x0107 }
        goto L_0x0130;
    L_0x016a:
        r9 = new android.location.Address;	 Catch:{ Exception -> 0x0107 }
        r0 = r18;
        r9.<init>(r0);	 Catch:{ Exception -> 0x0107 }
        r3 = readUTF(r14);	 Catch:{ Exception -> 0x0107 }
        r9.setThoroughfare(r3);	 Catch:{ Exception -> 0x0107 }
        r22 = r14.readInt();	 Catch:{ Exception -> 0x0107 }
        r16 = 0;
    L_0x017e:
        r0 = r16;
        r1 = r22;
        if (r0 >= r1) goto L_0x0190;
    L_0x0184:
        r3 = readUTF(r14);	 Catch:{ Exception -> 0x0107 }
        r0 = r16;
        r9.setAddressLine(r0, r3);	 Catch:{ Exception -> 0x0107 }
        r16 = r16 + 1;
        goto L_0x017e;
    L_0x0190:
        r3 = readUTF(r14);	 Catch:{ Exception -> 0x0107 }
        r9.setFeatureName(r3);	 Catch:{ Exception -> 0x0107 }
        r3 = readUTF(r14);	 Catch:{ Exception -> 0x0107 }
        r9.setLocality(r3);	 Catch:{ Exception -> 0x0107 }
        r3 = readUTF(r14);	 Catch:{ Exception -> 0x0107 }
        r9.setAdminArea(r3);	 Catch:{ Exception -> 0x0107 }
        r3 = readUTF(r14);	 Catch:{ Exception -> 0x0107 }
        r9.setSubAdminArea(r3);	 Catch:{ Exception -> 0x0107 }
        r3 = readUTF(r14);	 Catch:{ Exception -> 0x0107 }
        r9.setCountryName(r3);	 Catch:{ Exception -> 0x0107 }
        r3 = readUTF(r14);	 Catch:{ Exception -> 0x0107 }
        r9.setCountryCode(r3);	 Catch:{ Exception -> 0x0107 }
        r3 = readUTF(r14);	 Catch:{ Exception -> 0x0107 }
        r9.setPostalCode(r3);	 Catch:{ Exception -> 0x0107 }
        r3 = readUTF(r14);	 Catch:{ Exception -> 0x0107 }
        r9.setPhone(r3);	 Catch:{ Exception -> 0x0107 }
        r3 = readUTF(r14);	 Catch:{ Exception -> 0x0107 }
        r9.setUrl(r3);	 Catch:{ Exception -> 0x0107 }
        r14.close();	 Catch:{ Exception -> 0x0107 }
        goto L_0x004b;
        */
    }
}