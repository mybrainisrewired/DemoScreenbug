package com.wmt.widget.weather;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.util.Log;
import android.widget.Toast;
import com.wmt.res.CommRes;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WeatherUtils {
    private static final String ACTION_DATE_CHANGE = "com.wmt.weather.Service.intent.action.DATE_CHANGE";
    private static final String ACTION_NOTIFY_REFRESH = "com.wmt.weather.Service.intent.action.NOTIFY_REFRESH";
    private static final String ACTION_NO_CITY = "com.wmt.weather.Service.intent.action.NO_WEATHER_CITY";
    private static final String ACTION_UNIT_CHANGE = "com.wmt.weather.Service.intent.action.UNIT_CHANGE";
    private static final String ACTION_UPDATE_FAILURE = "com.wmt.weather.Service.intent.action.UPDATE_FAILURE";
    private static final String ACTION_WEATHER_INFO = "com.wmt.weather.Service.intent.action.WEATHER_INFO";
    private static final int BEING_LOAD_CITY = 8;
    private static final int BEING_UPDATE_CITY = 7;
    private static final int CHECK_FAILURE = 2;
    public static final String CITY = "city";
    public static final String CITY_SELECTED = "city_actived";
    public static final Uri CONTENT_URI;
    private static final int DATE_OVERDUE = 3;
    private static final boolean DEBUG = false;
    private static final String KEY_MY_CITY = "my_cities";
    private static final String KEY_REASON = "failure_reason";
    private static final int MSG_CHANGE_CITY = 4;
    private static final int MSG_DATE_CHANGED = 7;
    private static final int MSG_IS_CITY_CHANGE = 10;
    private static final int MSG_IS_DATE_CHANGE = 8;
    private static final int MSG_NO_SELECT_CITY = 9;
    private static final int MSG_ONLY_ONE_CITY = 11;
    private static final int MSG_SET_WEATHER_ITEM = 3;
    private static final int MSG_SET_WIDGET_UNIT = 5;
    private static final int MSG_UPDATE_FAILURE = 6;
    private static final int NO_NETWORK = 1;
    private static final int NO_SELECTED_CITY = 5;
    private static final int NO_SUCH_CITY = 0;
    private static final String TAG = "WeatherUtils";
    public static final String UNIT = "unit";
    public static final String WEATHER_DATA = "weather_data";
    public static final String WEATHER_SHARED = "weather_prefers";
    public boolean mBDegreeUnit;
    private Callbacks mCallbacks;
    private String mCheckFailedString;
    private Context mContext;
    private int mCurCityPos;
    public String mCurFailedString;
    public int mCurValidDay;
    private int mDayOfMonth;
    private MyHandler mHandler;
    public boolean mIsNoCity;
    public boolean mIsRefresh;
    private String mLoadingString;
    public String mNewCity;
    private String mNextCity;
    public String mNoCityString;
    private String mNoNetworkString;
    private String mNoSuchCityString;
    private String mOnlyCityString;
    private String mOverdueString;
    private Bundle mReceiveBundle;
    private int mTotalSize;
    private String mUpdatingString;
    private WeatherItem mWeatherItem;
    private BroadcastReceiver mWeatherReceiver;

    public static interface Callbacks {
        void loadWeatherBitmap(WeatherItem weatherItem);

        void srcBmpRecycle();
    }

    private class MyHandler extends Handler {
        private MyHandler() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SET_WEATHER_ITEM:
                    WeatherUtils.this.setWeatherItem();
                case MSG_CHANGE_CITY:
                    WeatherUtils.this.changeCity(WeatherUtils.this.mReceiveBundle);
                case NO_SELECTED_CITY:
                    WeatherUtils.this.setWidgetUnit(WeatherUtils.this.mReceiveBundle);
                case MSG_UPDATE_FAILURE:
                    WeatherUtils.this.notifyUpdateFailure(WeatherUtils.this.mReceiveBundle);
                case MSG_DATE_CHANGED:
                    WeatherUtils.this.sendDateChanged();
                case MSG_IS_DATE_CHANGE:
                    WeatherUtils.this.handleDateChanged();
                case MSG_NO_SELECT_CITY:
                    WeatherUtils.this.mIsRefresh = false;
                    WeatherUtils.this.updateFailedString(NO_SELECTED_CITY);
                    WeatherUtils.this.recycleWeatherItemBitmap(WeatherUtils.this.mWeatherItem);
                    WeatherUtils.this.loadNewWeatherBitmap();
                case MSG_IS_CITY_CHANGE:
                    WeatherUtils.this.changeCitySelected();
                case MSG_ONLY_ONE_CITY:
                    Toast.makeText(WeatherUtils.this.mContext, WeatherUtils.this.mOnlyCityString, NO_SUCH_CITY).show();
                default:
                    break;
            }
        }
    }

    static {
        CONTENT_URI = Uri.parse("content://com.wmt.weather/weather");
    }

    public WeatherUtils(Context context) {
        this.mIsRefresh = false;
        this.mIsNoCity = false;
        this.mHandler = new MyHandler(null);
        this.mWeatherReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                WeatherUtils.this.mReceiveBundle = intent.getBundleExtra("weatherItem");
                if (ACTION_WEATHER_INFO.equals(action)) {
                    WeatherUtils.this.mIsRefresh = false;
                    WeatherUtils.this.mHandler.removeMessages(MSG_SET_WEATHER_ITEM);
                    WeatherUtils.this.mHandler.sendEmptyMessageAtTime(MSG_SET_WEATHER_ITEM, 0);
                } else if (ACTION_UPDATE_FAILURE.equals(action)) {
                    WeatherUtils.this.mIsRefresh = false;
                    WeatherUtils.this.mHandler.removeMessages(MSG_UPDATE_FAILURE);
                    WeatherUtils.this.mHandler.sendEmptyMessageDelayed(MSG_UPDATE_FAILURE, 0);
                } else if (ACTION_NOTIFY_REFRESH.equals(action)) {
                    WeatherUtils.this.mHandler.removeMessages(MSG_CHANGE_CITY);
                    WeatherUtils.this.mHandler.sendEmptyMessageDelayed(MSG_CHANGE_CITY, 0);
                } else if (ACTION_UNIT_CHANGE.equals(action)) {
                    WeatherUtils.this.mHandler.removeMessages(NO_SELECTED_CITY);
                    WeatherUtils.this.mHandler.sendEmptyMessageDelayed(NO_SELECTED_CITY, 0);
                } else if (ACTION_NO_CITY.equals(action)) {
                    WeatherUtils.this.mHandler.removeMessages(MSG_NO_SELECT_CITY);
                    WeatherUtils.this.mHandler.sendEmptyMessageDelayed(MSG_NO_SELECT_CITY, 0);
                } else if (ACTION_DATE_CHANGE.equals(action)) {
                    WeatherUtils.this.mHandler.removeMessages(MSG_DATE_CHANGED);
                    WeatherUtils.this.mHandler.sendEmptyMessageDelayed(MSG_DATE_CHANGED, 0);
                } else if ("android.intent.action.TIME_TICK".equals(action)) {
                    WeatherUtils.this.mHandler.removeMessages(MSG_IS_DATE_CHANGE);
                    WeatherUtils.this.mHandler.sendEmptyMessageDelayed(MSG_IS_DATE_CHANGE, 0);
                }
            }
        };
        init(context);
    }

    private void changeCity(Bundle mBundleCity) {
        if (mBundleCity != null) {
            try {
                this.mIsRefresh = false;
                this.mNewCity = mBundleCity.getString(KEY_MY_CITY);
                recycleWeatherItemBitmap(this.mWeatherItem);
                updateFailedString(mBundleCity.getInt(KEY_REASON));
                loadNewWeatherBitmap();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void changeCitySelected() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CITY_SELECTED, Integer.valueOf(NO_NETWORK));
        ContentResolver resolver = this.mContext.getContentResolver();
        resolver.update(CONTENT_URI, contentValues, "city='" + preprocessCity(this.mNewCity) + "'", null);
        contentValues.put(CITY_SELECTED, Integer.valueOf(NO_SUCH_CITY));
        resolver.update(CONTENT_URI, contentValues, "city!='" + preprocessCity(this.mNewCity) + "'", null);
        Log.d(TAG, " changeCitySelected select city is " + this.mNewCity);
    }

    private void getCurPosition() {
        int curPos = NO_SUCH_CITY;
        Cursor c = null;
        String[] projection = new String[]{CITY, CITY_SELECTED};
        this.mTotalSize = 0;
        try {
            c = this.mContext.getContentResolver().query(CONTENT_URI, projection, null, null, null);
            if (c != null && c.moveToFirst()) {
                this.mTotalSize = c.getCount();
                do {
                    curPos++;
                    if (c.getInt(NO_NETWORK) == 1) {
                        break;
                    }
                } while (c.moveToNext());
            }
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            try {
                e.printStackTrace();
                if (c != null) {
                    c.close();
                }
            } catch (Throwable th) {
                if (c != null) {
                    c.close();
                }
            }
        }
        this.mCurCityPos = curPos;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.wmt.widget.weather.WeatherItem getWeatherItem(android.content.Context r14_context) {
        throw new UnsupportedOperationException("Method not decompiled: com.wmt.widget.weather.WeatherUtils.getWeatherItem(android.content.Context):com.wmt.widget.weather.WeatherItem");
        /*
        r13 = this;
        r1 = 2;
        r12 = 1;
        r11 = 0;
        r6 = 0;
        r9 = 0;
        r3 = "city_actived ==1 ";
        r0 = 3;
        r2 = new java.lang.String[r0];
        r0 = "city";
        r2[r11] = r0;
        r0 = "unit";
        r2[r12] = r0;
        r0 = "weather_data";
        r2[r1] = r0;
        r0 = r14.getContentResolver();	 Catch:{ Exception -> 0x0064 }
        r1 = CONTENT_URI;	 Catch:{ Exception -> 0x0064 }
        r4 = 0;
        r5 = 0;
        r6 = r0.query(r1, r2, r3, r4, r5);	 Catch:{ Exception -> 0x0064 }
        if (r6 == 0) goto L_0x0046;
    L_0x0024:
        r0 = r6.moveToFirst();	 Catch:{ Exception -> 0x0064 }
        if (r0 == 0) goto L_0x0046;
    L_0x002a:
        r0 = 0;
        r0 = r6.getString(r0);	 Catch:{ Exception -> 0x0064 }
        r13.mNewCity = r0;	 Catch:{ Exception -> 0x0064 }
        r0 = 1;
        r0 = r6.getInt(r0);	 Catch:{ Exception -> 0x0064 }
        if (r0 != 0) goto L_0x0062;
    L_0x0038:
        r0 = r11;
    L_0x0039:
        r13.mBDegreeUnit = r0;	 Catch:{ Exception -> 0x0064 }
        r0 = 2;
        r9 = r6.getBlob(r0);	 Catch:{ Exception -> 0x0064 }
        r0 = r6.moveToNext();	 Catch:{ Exception -> 0x0064 }
        if (r0 != 0) goto L_0x002a;
    L_0x0046:
        if (r6 == 0) goto L_0x004b;
    L_0x0048:
        r6.close();
    L_0x004b:
        r10 = 0;
        if (r9 == 0) goto L_0x0061;
    L_0x004e:
        r7 = android.os.Parcel.obtain();
        r0 = r9.length;
        r7.unmarshall(r9, r11, r0);
        r7.setDataPosition(r11);
        r0 = com.wmt.widget.weather.WeatherItem.CREATOR;
        r10 = r0.createFromParcel(r7);
        r10 = (com.wmt.widget.weather.WeatherItem) r10;
    L_0x0061:
        return r10;
    L_0x0062:
        r0 = r12;
        goto L_0x0039;
    L_0x0064:
        r8 = move-exception;
        r8.printStackTrace();	 Catch:{ all -> 0x006e }
        if (r6 == 0) goto L_0x004b;
    L_0x006a:
        r6.close();
        goto L_0x004b;
    L_0x006e:
        r0 = move-exception;
        if (r6 == 0) goto L_0x0074;
    L_0x0071:
        r6.close();
    L_0x0074:
        throw r0;
        */
    }

    private void handleDateChanged() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            int day = cal.get(NO_SELECTED_CITY);
            if (day != this.mDayOfMonth) {
                this.mIsRefresh = false;
                this.mDayOfMonth = day;
                loadNewWeatherBitmap();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(Context context) {
        try {
            this.mNoNetworkString = CommRes.getResourceString(context, "R.string.check_failed_network");
            this.mCheckFailedString = CommRes.getResourceString(context, "R.string.check_failed_data");
            this.mNoSuchCityString = CommRes.getResourceString(context, "R.string.no_such_city");
            this.mNoCityString = CommRes.getResourceString(context, "R.string.no_selected_city");
            this.mUpdatingString = CommRes.getResourceString(context, "R.string.being_update_city");
            this.mLoadingString = CommRes.getResourceString(context, "R.string.being_load_city");
            this.mOnlyCityString = CommRes.getResourceString(context, "R.string.only_city");
            this.mOverdueString = CommRes.getResourceString(context, "R.string.check_failed_overdue");
        } catch (Exception e) {
            Log.e(TAG, "No com.wmt.weather resources found!!!");
        }
        this.mContext = context;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        this.mDayOfMonth = cal.get(NO_SELECTED_CITY);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void notifyUpdateFailure(android.os.Bundle r5_bundleWhat) {
        throw new UnsupportedOperationException("Method not decompiled: com.wmt.widget.weather.WeatherUtils.notifyUpdateFailure(android.os.Bundle):void");
        /*
        r4 = this;
        r1 = 0;
        if (r5 == 0) goto L_0x0009;
    L_0x0003:
        r2 = "failure_reason";
        r1 = r5.getInt(r2);	 Catch:{ Exception -> 0x0048 }
    L_0x0009:
        r2 = r4.mWeatherItem;	 Catch:{ Exception -> 0x0048 }
        r4.recycleWeatherItemBitmap(r2);	 Catch:{ Exception -> 0x0048 }
        r2 = r4.mContext;	 Catch:{ Exception -> 0x0048 }
        r2 = r4.getWeatherItem(r2);	 Catch:{ Exception -> 0x0048 }
        r4.mWeatherItem = r2;	 Catch:{ Exception -> 0x0048 }
        r2 = r4.mWeatherItem;	 Catch:{ Exception -> 0x0048 }
        if (r2 != 0) goto L_0x0028;
    L_0x001a:
        r2 = "WeatherUtils";
        r3 = "mWeatherItem = null";
        android.util.Log.v(r2, r3);	 Catch:{ Exception -> 0x0048 }
        r4.updateFailedString(r1);	 Catch:{ Exception -> 0x0048 }
    L_0x0024:
        r4.loadNewWeatherBitmap();	 Catch:{ Exception -> 0x0048 }
    L_0x0027:
        return;
    L_0x0028:
        r2 = r4.mWeatherItem;	 Catch:{ Exception -> 0x0048 }
        r2 = r2.mForecastInformation;	 Catch:{ Exception -> 0x0048 }
        r2 = r2.mCity;	 Catch:{ Exception -> 0x0048 }
        if (r2 == 0) goto L_0x003e;
    L_0x0030:
        r2 = r4.mWeatherItem;	 Catch:{ Exception -> 0x0048 }
        r2 = r2.mForecastInformation;	 Catch:{ Exception -> 0x0048 }
        r2 = r2.mCity;	 Catch:{ Exception -> 0x0048 }
        r3 = "";
        r2 = r2.equals(r3);	 Catch:{ Exception -> 0x0048 }
        if (r2 == 0) goto L_0x0024;
    L_0x003e:
        r2 = r4.mWeatherItem;	 Catch:{ Exception -> 0x0048 }
        r4.recycleWeatherItemBitmap(r2);	 Catch:{ Exception -> 0x0048 }
        r2 = 0;
        r4.updateFailedString(r2);	 Catch:{ Exception -> 0x0048 }
        goto L_0x0024;
    L_0x0048:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0027;
        */
    }

    private int paserValidDay(String Date) {
        long j = 0;
        if (Date == null || Date.equals("")) {
            return 404;
        }
        int i = NO_SUCH_CITY;
        try {
            long diff = new Date().getTime() - new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(Date).getTime();
            if (diff > 0) {
                j = 4 - diff / 86400000;
            }
            return (int) j;
        } catch (Exception e) {
            e.printStackTrace();
            return i;
        }
    }

    private static String preprocessCity(String city) {
        if (city == null) {
            return city;
        }
        try {
            return city.contains("'") ? city.replaceAll("'", "''") : city;
        } catch (Exception e) {
            e.printStackTrace();
            return city;
        }
    }

    private void recycleWeatherItemBitmap(WeatherItem weatherItem) {
        if (weatherItem != null) {
            int i = NO_SUCH_CITY;
            while (i < 4) {
                if (weatherItem.mForecastConditions[i].mBitmap != null) {
                    weatherItem.mForecastConditions[i].mBitmap.recycle();
                    weatherItem.mForecastConditions[i].mBitmap = null;
                }
                i++;
            }
            this.mWeatherItem = null;
            System.gc();
        }
    }

    private void refreshWeather(Context context) {
        Cursor c = null;
        byte[] getData = null;
        try {
            c = context.getContentResolver().query(CONTENT_URI, new String[]{CITY, UNIT, WEATHER_DATA}, "city_actived ==1 ", null, null);
            if (c == null || !c.moveToFirst()) {
                Log.d(TAG, "update: no selected city");
                this.mHandler.removeMessages(MSG_NO_SELECT_CITY);
                this.mHandler.sendEmptyMessageDelayed(MSG_NO_SELECT_CITY, 0);
                if (c != null) {
                    c.close();
                    return;
                } else {
                    return;
                }
            }
            do {
                this.mNewCity = c.getString(NO_SUCH_CITY);
                this.mBDegreeUnit = c.getLong(NO_NETWORK) > 0;
                getData = c.getBlob(CHECK_FAILURE);
            } while (c.moveToNext());
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            try {
                e.printStackTrace();
                if (c != null) {
                    c.close();
                }
            } catch (Throwable th) {
                if (c != null) {
                    c.close();
                }
            }
        }
        if (getData != null) {
            Parcel dest = Parcel.obtain();
            dest.unmarshall(getData, NO_SUCH_CITY, getData.length);
            dest.setDataPosition(NO_SUCH_CITY);
            this.mWeatherItem = (WeatherItem) WeatherItem.CREATOR.createFromParcel(dest);
        } else {
            recycleWeatherItemBitmap(this.mWeatherItem);
            updateFailedString(CHECK_FAILURE);
        }
        loadNewWeatherBitmap();
    }

    private void registerReceiver(Context context) {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_WEATHER_INFO);
            intentFilter.addAction(ACTION_NOTIFY_REFRESH);
            intentFilter.addAction(ACTION_UNIT_CHANGE);
            intentFilter.addAction(ACTION_DATE_CHANGE);
            intentFilter.addAction(ACTION_UPDATE_FAILURE);
            intentFilter.addAction(ACTION_NO_CITY);
            intentFilter.addAction("android.intent.action.TIME_TICK");
            context.registerReceiver(this.mWeatherReceiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void sendDateChanged() {
        throw new UnsupportedOperationException("Method not decompiled: com.wmt.widget.weather.WeatherUtils.sendDateChanged():void");
        /*
        r3 = this;
        r1 = r3.mWeatherItem;
        if (r1 == 0) goto L_0x0011;
    L_0x0004:
        r1 = r3.mWeatherItem;	 Catch:{ Exception -> 0x0019 }
        r1 = r1.mForecastInformation;	 Catch:{ Exception -> 0x0019 }
        r1 = r1.mForcastDate;	 Catch:{ Exception -> 0x0019 }
        r1 = r3.paserValidDay(r1);	 Catch:{ Exception -> 0x0019 }
        r2 = 4;
        if (r1 != r2) goto L_0x0012;
    L_0x0011:
        return;
    L_0x0012:
        r1 = 0;
        r3.mIsRefresh = r1;	 Catch:{ Exception -> 0x0019 }
        r3.loadNewWeatherBitmap();	 Catch:{ Exception -> 0x0019 }
        goto L_0x0011;
    L_0x0019:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0011;
        */
    }

    private void setWeatherItem() {
        try {
            recycleWeatherItemBitmap(this.mWeatherItem);
            this.mWeatherItem = getWeatherItem(this.mContext);
            if (this.mWeatherItem == null) {
                Log.v(TAG, "mWeatherItem = null");
                updateFailedString(CHECK_FAILURE);
            } else if (this.mWeatherItem.mForecastInformation.mCity == null || this.mWeatherItem.mForecastInformation.mCity.equals("")) {
                recycleWeatherItemBitmap(this.mWeatherItem);
                updateFailedString(NO_SUCH_CITY);
            }
            loadNewWeatherBitmap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setWidgetUnit(Bundle mBundleUnit) {
        if (mBundleUnit != null) {
            try {
                this.mBDegreeUnit = mBundleUnit.getBoolean(UNIT);
                this.mIsRefresh = false;
                loadNewWeatherBitmap();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void unRegisterReceiver() {
        try {
            this.mContext.unregisterReceiver(this.mWeatherReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateFailedString(int type) {
        switch (type) {
            case NO_SUCH_CITY:
                this.mCurFailedString = this.mNoSuchCityString;
            case NO_NETWORK:
                this.mCurFailedString = this.mNoNetworkString;
            case MSG_SET_WEATHER_ITEM:
                this.mCurFailedString = this.mOverdueString;
            case NO_SELECTED_CITY:
                this.mCurFailedString = this.mNoCityString;
            case MSG_DATE_CHANGED:
                this.mCurFailedString = this.mUpdatingString;
            case MSG_IS_DATE_CHANGE:
                this.mCurFailedString = this.mLoadingString;
            default:
                this.mCurFailedString = this.mCheckFailedString;
        }
    }

    public String getCityIndexString() {
        return this.mCurCityPos + "/" + this.mTotalSize;
    }

    public void getNextCity() {
        getCurPosition();
        Cursor c = null;
        try {
            c = this.mContext.getContentResolver().query(CONTENT_URI, new String[]{CITY}, null, null, null);
            if (c != null && c.moveToFirst() && this.mTotalSize == 1) {
                this.mHandler.removeMessages(MSG_ONLY_ONE_CITY);
                this.mHandler.sendEmptyMessageDelayed(MSG_ONLY_ONE_CITY, 500);
                if (c != null) {
                    c.close();
                    return;
                } else {
                    return;
                }
            } else {
                if (this.mCurCityPos == this.mTotalSize) {
                    this.mCurCityPos = 0;
                }
                c.moveToPosition(this.mCurCityPos);
                this.mNextCity = c.getString(NO_SUCH_CITY);
            }
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            try {
                e.printStackTrace();
                if (c != null) {
                    c.close();
                }
            } catch (Throwable th) {
                if (c != null) {
                    c.close();
                }
            }
        }
        this.mNewCity = this.mNextCity;
        changeCitySelected();
        Log.d(TAG, "the next city is " + this.mNewCity);
        Intent intent = new Intent();
        intent.setAction(ACTION_WEATHER_INFO);
        this.mContext.sendBroadcast(intent);
    }

    public void loadNewWeatherBitmap() {
        this.mIsNoCity = false;
        this.mCurValidDay = 4;
        this.mCallbacks.srcBmpRecycle();
        if (this.mWeatherItem != null) {
            int validDay = paserValidDay(this.mWeatherItem.mForecastInformation.mForcastDate);
            if (validDay <= 0) {
                recycleWeatherItemBitmap(this.mWeatherItem);
                updateFailedString(MSG_SET_WEATHER_ITEM);
            } else if (validDay < 4) {
                Log.d(TAG, "validDay below four");
                this.mCurValidDay = validDay;
                if (this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay].mIconPath == null) {
                    recycleWeatherItemBitmap(this.mWeatherItem);
                    updateFailedString(CHECK_FAILURE);
                }
            } else if (validDay == 404) {
                recycleWeatherItemBitmap(this.mWeatherItem);
                updateFailedString(CHECK_FAILURE);
            }
        }
        getCurPosition();
        this.mCallbacks.loadWeatherBitmap(this.mWeatherItem);
    }

    public void setmCallbacks(Callbacks mCallbacks) {
        this.mCallbacks = mCallbacks;
    }

    public void start() {
        registerReceiver(this.mContext);
        refreshWeather(this.mContext);
    }

    public void stop() {
        unRegisterReceiver();
        recycleWeatherItemBitmap(this.mWeatherItem);
    }
}