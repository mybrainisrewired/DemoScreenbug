package com.mopub.mobileads;

import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import java.util.Map;

class MraidCommandFactory {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$mopub$mobileads$MraidCommandFactory$MraidJavascriptCommand;
    protected static MraidCommandFactory instance;

    enum MraidJavascriptCommand {
        CLOSE("close"),
        EXPAND("expand"),
        USECUSTOMCLOSE("usecustomclose"),
        OPEN("open"),
        RESIZE("resize"),
        GET_RESIZE_PROPERTIES("getResizeProperties"),
        SET_RESIZE_PROPERTIES("setResizeProperties"),
        PLAY_VIDEO("playVideo"),
        STORE_PICTURE("storePicture"),
        GET_CURRENT_POSITION("getCurrentPosition"),
        GET_DEFAULT_POSITION("getDefaultPosition"),
        GET_MAX_SIZE("getMaxSize"),
        GET_SCREEN_SIZE("getScreenSize"),
        CREATE_CALENDAR_EVENT("createCalendarEvent"),
        UNSPECIFIED(Preconditions.EMPTY_ARGUMENTS);
        private String mCommand;

        static {
            String str = "close";
            CLOSE = new MraidJavascriptCommand("CLOSE", 0, "close");
            str = "expand";
            EXPAND = new MraidJavascriptCommand("EXPAND", 1, "expand");
            str = "usecustomclose";
            USECUSTOMCLOSE = new MraidJavascriptCommand("USECUSTOMCLOSE", 2, "usecustomclose");
            str = "open";
            OPEN = new MraidJavascriptCommand("OPEN", 3, "open");
            str = "resize";
            RESIZE = new MraidJavascriptCommand("RESIZE", 4, "resize");
            String str2 = "getResizeProperties";
            GET_RESIZE_PROPERTIES = new MraidJavascriptCommand("GET_RESIZE_PROPERTIES", 5, "getResizeProperties");
            str2 = "setResizeProperties";
            SET_RESIZE_PROPERTIES = new MraidJavascriptCommand("SET_RESIZE_PROPERTIES", 6, "setResizeProperties");
            str2 = "playVideo";
            PLAY_VIDEO = new MraidJavascriptCommand("PLAY_VIDEO", 7, "playVideo");
            str2 = "storePicture";
            STORE_PICTURE = new MraidJavascriptCommand("STORE_PICTURE", 8, "storePicture");
            str2 = "getCurrentPosition";
            GET_CURRENT_POSITION = new MraidJavascriptCommand("GET_CURRENT_POSITION", 9, "getCurrentPosition");
            str2 = "getDefaultPosition";
            GET_DEFAULT_POSITION = new MraidJavascriptCommand("GET_DEFAULT_POSITION", 10, "getDefaultPosition");
            str2 = "getMaxSize";
            GET_MAX_SIZE = new MraidJavascriptCommand("GET_MAX_SIZE", 11, "getMaxSize");
            str2 = "getScreenSize";
            GET_SCREEN_SIZE = new MraidJavascriptCommand("GET_SCREEN_SIZE", 12, "getScreenSize");
            str2 = "createCalendarEvent";
            CREATE_CALENDAR_EVENT = new MraidJavascriptCommand("CREATE_CALENDAR_EVENT", 13, "createCalendarEvent");
            String str3 = "UNSPECIFIED";
            str2 = Preconditions.EMPTY_ARGUMENTS;
            UNSPECIFIED = new MraidJavascriptCommand(str3, 14, Preconditions.EMPTY_ARGUMENTS);
            ENUM$VALUES = new MraidJavascriptCommand[]{CLOSE, EXPAND, USECUSTOMCLOSE, OPEN, RESIZE, GET_RESIZE_PROPERTIES, SET_RESIZE_PROPERTIES, PLAY_VIDEO, STORE_PICTURE, GET_CURRENT_POSITION, GET_DEFAULT_POSITION, GET_MAX_SIZE, GET_SCREEN_SIZE, CREATE_CALENDAR_EVENT, UNSPECIFIED};
        }

        private MraidJavascriptCommand(String command) {
            this.mCommand = command;
        }

        private static MraidJavascriptCommand fromString(String string) {
            MraidJavascriptCommand[] values = values();
            int length = values.length;
            int i = 0;
            while (i < length) {
                MraidJavascriptCommand command = values[i];
                if (command.mCommand.equals(string)) {
                    return command;
                }
                i++;
            }
            return UNSPECIFIED;
        }

        String getCommand() {
            return this.mCommand;
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$mopub$mobileads$MraidCommandFactory$MraidJavascriptCommand() {
        int[] iArr = $SWITCH_TABLE$com$mopub$mobileads$MraidCommandFactory$MraidJavascriptCommand;
        if (iArr == null) {
            iArr = new int[MraidJavascriptCommand.values().length];
            try {
                iArr[MraidJavascriptCommand.CLOSE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[MraidJavascriptCommand.CREATE_CALENDAR_EVENT.ordinal()] = 14;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[MraidJavascriptCommand.EXPAND.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[MraidJavascriptCommand.GET_CURRENT_POSITION.ordinal()] = 10;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[MraidJavascriptCommand.GET_DEFAULT_POSITION.ordinal()] = 11;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[MraidJavascriptCommand.GET_MAX_SIZE.ordinal()] = 12;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[MraidJavascriptCommand.GET_RESIZE_PROPERTIES.ordinal()] = 6;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[MraidJavascriptCommand.GET_SCREEN_SIZE.ordinal()] = 13;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[MraidJavascriptCommand.OPEN.ordinal()] = 4;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[MraidJavascriptCommand.PLAY_VIDEO.ordinal()] = 8;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[MraidJavascriptCommand.RESIZE.ordinal()] = 5;
            } catch (NoSuchFieldError e11) {
            }
            try {
                iArr[MraidJavascriptCommand.SET_RESIZE_PROPERTIES.ordinal()] = 7;
            } catch (NoSuchFieldError e12) {
            }
            try {
                iArr[MraidJavascriptCommand.STORE_PICTURE.ordinal()] = 9;
            } catch (NoSuchFieldError e13) {
            }
            try {
                iArr[MraidJavascriptCommand.UNSPECIFIED.ordinal()] = 15;
            } catch (NoSuchFieldError e14) {
            }
            try {
                iArr[MraidJavascriptCommand.USECUSTOMCLOSE.ordinal()] = 3;
            } catch (NoSuchFieldError e15) {
            }
            $SWITCH_TABLE$com$mopub$mobileads$MraidCommandFactory$MraidJavascriptCommand = iArr;
        }
        return iArr;
    }

    static {
        instance = new MraidCommandFactory();
    }

    MraidCommandFactory() {
    }

    public static MraidCommand create(String command, Map<String, String> params, MraidView view) {
        return instance.internalCreate(command, params, view);
    }

    @Deprecated
    public static void setInstance(MraidCommandFactory factory) {
        instance = factory;
    }

    protected MraidCommand internalCreate(String command, Map<String, String> params, MraidView view) {
        switch ($SWITCH_TABLE$com$mopub$mobileads$MraidCommandFactory$MraidJavascriptCommand()[MraidJavascriptCommand.access$2(command).ordinal()]) {
            case MMAdView.TRANSITION_FADE:
                return new MraidCommandClose(params, view);
            case MMAdView.TRANSITION_UP:
                return new MraidCommandExpand(params, view);
            case MMAdView.TRANSITION_DOWN:
                return new MraidCommandUseCustomClose(params, view);
            case MMAdView.TRANSITION_RANDOM:
                return new MraidCommandOpen(params, view);
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                return new MraidCommandResize(params, view);
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                return new MraidCommandGetResizeProperties(params, view);
            case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                return new MraidCommandSetResizeProperties(params, view);
            case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                return new MraidCommandPlayVideo(params, view);
            case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                return new MraidCommandStorePicture(params, view);
            case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                return new MraidCommandGetCurrentPosition(params, view);
            case ApiEventType.API_MRAID_EXPAND:
                return new MraidCommandGetDefaultPosition(params, view);
            case ApiEventType.API_MRAID_RESIZE:
                return new MraidCommandGetMaxSize(params, view);
            case ApiEventType.API_MRAID_CLOSE:
                return new MraidCommandGetScreenSize(params, view);
            case ApiEventType.API_MRAID_IS_VIEWABLE:
                return new MraidCommandCreateCalendarEvent(params, view);
            default:
                return null;
        }
    }
}