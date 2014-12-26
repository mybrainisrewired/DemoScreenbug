package com.millennialmedia.google.gson.internal.bind;

import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.google.gson.Gson;
import com.millennialmedia.google.gson.TypeAdapter;
import com.millennialmedia.google.gson.TypeAdapterFactory;
import com.millennialmedia.google.gson.internal.LinkedHashTreeMap;
import com.millennialmedia.google.gson.reflect.TypeToken;
import com.millennialmedia.google.gson.stream.JsonReader;
import com.millennialmedia.google.gson.stream.JsonToken;
import com.millennialmedia.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ObjectTypeAdapter extends TypeAdapter<Object> {
    public static final TypeAdapterFactory FACTORY;
    private final Gson gson;

    static /* synthetic */ class AnonymousClass_2 {
        static final /* synthetic */ int[] $SwitchMap$com$millennialmedia$google$gson$stream$JsonToken;

        static {
            $SwitchMap$com$millennialmedia$google$gson$stream$JsonToken = new int[JsonToken.values().length];
            try {
                $SwitchMap$com$millennialmedia$google$gson$stream$JsonToken[JsonToken.BEGIN_ARRAY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$millennialmedia$google$gson$stream$JsonToken[JsonToken.BEGIN_OBJECT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$millennialmedia$google$gson$stream$JsonToken[JsonToken.STRING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$millennialmedia$google$gson$stream$JsonToken[JsonToken.NUMBER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$millennialmedia$google$gson$stream$JsonToken[JsonToken.BOOLEAN.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            $SwitchMap$com$millennialmedia$google$gson$stream$JsonToken[JsonToken.NULL.ordinal()] = 6;
        }
    }

    static {
        FACTORY = new TypeAdapterFactory() {
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
                return type.getRawType() == Object.class ? new ObjectTypeAdapter(null) : null;
            }
        };
    }

    private ObjectTypeAdapter(Gson gson) {
        this.gson = gson;
    }

    public Object read(JsonReader in) throws IOException {
        switch (AnonymousClass_2.$SwitchMap$com$millennialmedia$google$gson$stream$JsonToken[in.peek().ordinal()]) {
            case MMAdView.TRANSITION_FADE:
                List<Object> list = new ArrayList();
                in.beginArray();
                while (in.hasNext()) {
                    list.add(read(in));
                }
                in.endArray();
                return list;
            case MMAdView.TRANSITION_UP:
                Map<String, Object> map = new LinkedHashTreeMap();
                in.beginObject();
                while (in.hasNext()) {
                    map.put(in.nextName(), read(in));
                }
                in.endObject();
                return map;
            case MMAdView.TRANSITION_DOWN:
                return in.nextString();
            case MMAdView.TRANSITION_RANDOM:
                return Double.valueOf(in.nextDouble());
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                return Boolean.valueOf(in.nextBoolean());
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                in.nextNull();
                return null;
            default:
                throw new IllegalStateException();
        }
    }

    public void write(JsonWriter out, Object value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            TypeAdapter<Object> typeAdapter = this.gson.getAdapter(value.getClass());
            if (typeAdapter instanceof ObjectTypeAdapter) {
                out.beginObject();
                out.endObject();
            } else {
                typeAdapter.write(out, value);
            }
        }
    }
}