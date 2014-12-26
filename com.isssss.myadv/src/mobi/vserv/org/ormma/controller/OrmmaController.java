package mobi.vserv.org.ormma.controller;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.lang.reflect.Field;
import mobi.vserv.org.ormma.controller.util.NavigationStringEnum;
import mobi.vserv.org.ormma.controller.util.TransitionStringEnum;
import mobi.vserv.org.ormma.view.OrmmaView;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class OrmmaController {
    private static final String BOOLEAN_TYPE = "boolean";
    public static final String EXIT = "exit";
    private static final String FLOAT_TYPE = "float";
    public static final String FULL_SCREEN = "fullscreen";
    private static final String INT_TYPE = "int";
    private static final String NAVIGATION_TYPE = "class com.ormma.NavigationStringEnum";
    private static final String STRING_TYPE = "class java.lang.String";
    public static final String STYLE_NORMAL = "normal";
    private static final String TRANSITION_TYPE = "class com.ormma.TransitionStringEnum";
    protected Context mContext;
    protected OrmmaView mOrmmaView;

    public static class ReflectedParcelable implements Parcelable {
        protected ReflectedParcelable(Parcel in) {
            Field[] fields = getClass().getDeclaredFields();
            mobi.vserv.org.ormma.controller.OrmmaController.ReflectedParcelable obj = this;
            int i = 0;
            while (i < fields.length) {
                try {
                    Field f = fields[i];
                    Class<?> type = f.getType();
                    if (type.isEnum()) {
                        String typeStr = type.toString();
                        if (typeStr.equals(NAVIGATION_TYPE)) {
                            f.set(obj, NavigationStringEnum.fromString(in.readString()));
                        } else if (typeStr.equals(TRANSITION_TYPE)) {
                            f.set(obj, TransitionStringEnum.fromString(in.readString()));
                        }
                    } else if (!f.get(this) instanceof Creator) {
                        f.set(obj, in.readValue(null));
                    }
                    i++;
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e2) {
                    e2.printStackTrace();
                }
            }
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags1) {
            Field[] fields = getClass().getDeclaredFields();
            int i = 0;
            while (i < fields.length) {
                try {
                    Field f = fields[i];
                    Class<?> type = f.getType();
                    if (type.isEnum()) {
                        String typeStr = type.toString();
                        if (typeStr.equals(NAVIGATION_TYPE)) {
                            out.writeString(((NavigationStringEnum) f.get(this)).getText());
                        } else if (typeStr.equals(TRANSITION_TYPE)) {
                            out.writeString(((TransitionStringEnum) f.get(this)).getText());
                        }
                    } else {
                        Object dt = f.get(this);
                        if (!dt instanceof Creator) {
                            out.writeValue(dt);
                        }
                    }
                    i++;
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public static class Dimensions extends mobi.vserv.org.ormma.controller.OrmmaController.ReflectedParcelable {
        public static final Creator<mobi.vserv.org.ormma.controller.OrmmaController.Dimensions> CREATOR;
        public int height;
        public int width;
        public int x;
        public int y;

        static {
            CREATOR = new Creator<mobi.vserv.org.ormma.controller.OrmmaController.Dimensions>() {
                public mobi.vserv.org.ormma.controller.OrmmaController.Dimensions createFromParcel(Parcel in) {
                    return new mobi.vserv.org.ormma.controller.OrmmaController.Dimensions(in);
                }

                public mobi.vserv.org.ormma.controller.OrmmaController.Dimensions[] newArray(int size) {
                    return new mobi.vserv.org.ormma.controller.OrmmaController.Dimensions[size];
                }
            };
        }

        public Dimensions() {
            this.x = -1;
            this.y = -1;
            this.width = -1;
            this.height = -1;
        }

        protected Dimensions(Parcel in) {
            super(in);
        }
    }

    public static class PlayerProperties extends mobi.vserv.org.ormma.controller.OrmmaController.ReflectedParcelable {
        public static final Creator<mobi.vserv.org.ormma.controller.OrmmaController.PlayerProperties> CREATOR;
        public boolean audioMuted;
        public boolean autoPlay;
        public boolean doLoop;
        public boolean inline;
        public boolean showControl;
        public String startStyle;
        public String stopStyle;

        static {
            CREATOR = new Creator<mobi.vserv.org.ormma.controller.OrmmaController.PlayerProperties>() {
                public mobi.vserv.org.ormma.controller.OrmmaController.PlayerProperties createFromParcel(Parcel in) {
                    return new mobi.vserv.org.ormma.controller.OrmmaController.PlayerProperties(in);
                }

                public mobi.vserv.org.ormma.controller.OrmmaController.PlayerProperties[] newArray(int size) {
                    return new mobi.vserv.org.ormma.controller.OrmmaController.PlayerProperties[size];
                }
            };
        }

        public PlayerProperties() {
            this.showControl = true;
            this.autoPlay = true;
            this.audioMuted = false;
            this.doLoop = false;
            String str = STYLE_NORMAL;
            this.stopStyle = str;
            this.startStyle = str;
            this.inline = false;
        }

        public PlayerProperties(Parcel in) {
            super(in);
        }

        public boolean doLoop() {
            return this.doLoop;
        }

        public boolean doMute() {
            return this.audioMuted;
        }

        public boolean exitOnComplete() {
            return this.stopStyle.equalsIgnoreCase(EXIT);
        }

        public boolean isAutoPlay() {
            return this.autoPlay;
        }

        public boolean isFullScreen() {
            return this.startStyle.equalsIgnoreCase(FULL_SCREEN);
        }

        public void muteAudio() {
            this.audioMuted = true;
        }

        public void setProperties(boolean audioMuted, boolean autoPlay, boolean controls, boolean inline, boolean loop, String startStyle, String stopStyle) {
            this.autoPlay = autoPlay;
            this.showControl = controls;
            this.doLoop = loop;
            this.audioMuted = audioMuted;
            this.startStyle = startStyle;
            this.stopStyle = stopStyle;
            this.inline = inline;
            if (Defines.ENABLE_lOGGING) {
                Log.i("vserv", new StringBuilder("inline value is ").append(inline).toString());
            }
        }

        public void setStopStyle(String style) {
            this.stopStyle = style;
        }

        public boolean showControl() {
            return this.showControl;
        }
    }

    public static class Properties extends mobi.vserv.org.ormma.controller.OrmmaController.ReflectedParcelable {
        public static final Creator<mobi.vserv.org.ormma.controller.OrmmaController.Properties> CREATOR;
        public int backgroundColor;
        public float backgroundOpacity;
        public boolean useBackground;

        static {
            CREATOR = new Creator<mobi.vserv.org.ormma.controller.OrmmaController.Properties>() {
                public mobi.vserv.org.ormma.controller.OrmmaController.Properties createFromParcel(Parcel in) {
                    return new mobi.vserv.org.ormma.controller.OrmmaController.Properties(in);
                }

                public mobi.vserv.org.ormma.controller.OrmmaController.Properties[] newArray(int size) {
                    return new mobi.vserv.org.ormma.controller.OrmmaController.Properties[size];
                }
            };
        }

        public Properties() {
            this.useBackground = false;
            this.backgroundColor = 0;
            this.backgroundOpacity = 0.0f;
        }

        protected Properties(Parcel in) {
            super(in);
        }
    }

    public OrmmaController(OrmmaView adView, Context context) {
        this.mOrmmaView = adView;
        this.mContext = context;
    }

    protected static Object getFromJSON(JSONObject json, Class<?> c) throws IllegalAccessException, InstantiationException, NumberFormatException, NullPointerException {
        Field[] fields = c.getDeclaredFields();
        Object obj = c.newInstance();
        int i = 0;
        while (i < fields.length) {
            Field f = fields[i];
            String JSONName = f.getName().replace('_', '-');
            String typeStr = f.getType().toString();
            try {
                if (typeStr.equals(INT_TYPE)) {
                    int iVal;
                    String value = json.getString(JSONName).toLowerCase();
                    if (value.startsWith("#")) {
                        iVal = -1;
                        try {
                            if (value.startsWith("#0x")) {
                                iVal = Integer.decode(value.substring(1)).intValue();
                            } else {
                                iVal = Integer.parseInt(value.substring(1), ApiEventType.API_MRAID_GET_ORIENTATION);
                            }
                        } catch (NumberFormatException e) {
                        }
                    } else {
                        iVal = Integer.parseInt(value);
                    }
                    f.set(obj, Integer.valueOf(iVal));
                    i++;
                } else {
                    if (typeStr.equals(STRING_TYPE)) {
                        f.set(obj, json.getString(JSONName));
                    } else if (typeStr.equals(BOOLEAN_TYPE)) {
                        f.set(obj, Boolean.valueOf(json.getBoolean(JSONName)));
                    } else if (typeStr.equals(FLOAT_TYPE)) {
                        f.set(obj, Float.valueOf(Float.parseFloat(json.getString(JSONName))));
                    } else if (typeStr.equals(NAVIGATION_TYPE)) {
                        f.set(obj, NavigationStringEnum.fromString(json.getString(JSONName)));
                    } else if (typeStr.equals(TRANSITION_TYPE)) {
                        f.set(obj, TransitionStringEnum.fromString(json.getString(JSONName)));
                    }
                    i++;
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        return obj;
    }

    public abstract void stopAllListeners();
}