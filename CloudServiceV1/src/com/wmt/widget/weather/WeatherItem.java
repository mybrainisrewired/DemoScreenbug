package com.wmt.widget.weather;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class WeatherItem implements Parcelable {
    public static final Creator<WeatherItem> CREATOR;
    public static final int MAX_DAY = 4;
    public ForecastConditions[] mForecastConditions;
    public ForecastInformation mForecastInformation;

    public static class ForecastConditions implements Parcelable {
        public static final Creator<com.wmt.widget.weather.WeatherItem.ForecastConditions> CREATOR;
        public Bitmap mBitmap;
        public String mCondition;
        public String mDate;
        public String mHigh;
        public String mHumidity;
        public String mIconPath;
        public String mLow;
        public String mTempC;
        public String mWindCondition;

        static {
            CREATOR = new Creator<com.wmt.widget.weather.WeatherItem.ForecastConditions>() {
                public com.wmt.widget.weather.WeatherItem.ForecastConditions createFromParcel(Parcel source) {
                    com.wmt.widget.weather.WeatherItem.ForecastConditions mForecastConditions = new com.wmt.widget.weather.WeatherItem.ForecastConditions();
                    mForecastConditions.mDate = source.readString();
                    mForecastConditions.mTempC = source.readString();
                    mForecastConditions.mLow = source.readString();
                    mForecastConditions.mHigh = source.readString();
                    mForecastConditions.mCondition = source.readString();
                    mForecastConditions.mHumidity = source.readString();
                    mForecastConditions.mWindCondition = source.readString();
                    mForecastConditions.mIconPath = source.readString();
                    mForecastConditions.mBitmap = (Bitmap) source.readValue(com.wmt.widget.weather.WeatherItem.ForecastConditions.class.getClassLoader());
                    return mForecastConditions;
                }

                public com.wmt.widget.weather.WeatherItem.ForecastConditions[] newArray(int size) {
                    return new com.wmt.widget.weather.WeatherItem.ForecastConditions[size];
                }
            };
        }

        public ForecastConditions() {
            this.mDate = "";
            this.mTempC = "";
            this.mLow = "";
            this.mHigh = "";
            this.mCondition = "";
            this.mHumidity = "";
            this.mWindCondition = "";
            this.mIconPath = "";
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mDate);
            dest.writeString(this.mTempC);
            dest.writeString(this.mLow);
            dest.writeString(this.mHigh);
            dest.writeString(this.mCondition);
            dest.writeString(this.mHumidity);
            dest.writeString(this.mWindCondition);
            dest.writeString(this.mIconPath);
            dest.writeValue(this.mBitmap);
        }
    }

    public static class ForecastInformation implements Parcelable {
        public static final Creator<com.wmt.widget.weather.WeatherItem.ForecastInformation> CREATOR;
        public String mCity;
        public String mForcastDate;
        public String mPostalCode;

        static {
            CREATOR = new Creator<com.wmt.widget.weather.WeatherItem.ForecastInformation>() {
                public com.wmt.widget.weather.WeatherItem.ForecastInformation createFromParcel(Parcel source) {
                    com.wmt.widget.weather.WeatherItem.ForecastInformation mForecastInformation = new com.wmt.widget.weather.WeatherItem.ForecastInformation();
                    mForecastInformation.mCity = source.readString();
                    mForecastInformation.mPostalCode = source.readString();
                    mForecastInformation.mForcastDate = source.readString();
                    return mForecastInformation;
                }

                public com.wmt.widget.weather.WeatherItem.ForecastInformation[] newArray(int size) {
                    return new com.wmt.widget.weather.WeatherItem.ForecastInformation[size];
                }
            };
        }

        public ForecastInformation() {
            this.mCity = "";
            this.mPostalCode = "";
            this.mForcastDate = "";
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mCity);
            dest.writeString(this.mPostalCode);
            dest.writeString(this.mForcastDate);
        }
    }

    static {
        CREATOR = new Creator<WeatherItem>() {
            public WeatherItem createFromParcel(Parcel source) {
                WeatherItem mWeatherItem = new WeatherItem();
                mWeatherItem.mForecastInformation = (com.wmt.widget.weather.WeatherItem.ForecastInformation) source.readValue(WeatherItem.class.getClassLoader());
                int i = 0;
                while (i < 4) {
                    mWeatherItem.mForecastConditions[i] = (com.wmt.widget.weather.WeatherItem.ForecastConditions) source.readValue(WeatherItem.class.getClassLoader());
                    i++;
                }
                return mWeatherItem;
            }

            public WeatherItem[] newArray(int size) {
                return new WeatherItem[size];
            }
        };
    }

    public WeatherItem() {
        this.mForecastInformation = new ForecastInformation();
        this.mForecastConditions = new ForecastConditions[4];
        int i = 0;
        while (i < 4) {
            this.mForecastConditions[i] = new ForecastConditions();
            i++;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mForecastInformation);
        int i = 0;
        while (i < 4) {
            dest.writeValue(this.mForecastConditions[i]);
            i++;
        }
    }
}