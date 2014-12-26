package com.millennialmedia.google.gson.internal.bind;

import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.google.gson.Gson;
import com.millennialmedia.google.gson.JsonArray;
import com.millennialmedia.google.gson.JsonElement;
import com.millennialmedia.google.gson.JsonIOException;
import com.millennialmedia.google.gson.JsonNull;
import com.millennialmedia.google.gson.JsonObject;
import com.millennialmedia.google.gson.JsonPrimitive;
import com.millennialmedia.google.gson.JsonSyntaxException;
import com.millennialmedia.google.gson.TypeAdapter;
import com.millennialmedia.google.gson.TypeAdapterFactory;
import com.millennialmedia.google.gson.annotations.SerializedName;
import com.millennialmedia.google.gson.internal.LazilyParsedNumber;
import com.millennialmedia.google.gson.reflect.TypeToken;
import com.millennialmedia.google.gson.stream.JsonReader;
import com.millennialmedia.google.gson.stream.JsonToken;
import com.millennialmedia.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.UUID;

public final class TypeAdapters {
    public static final TypeAdapter<BigDecimal> BIG_DECIMAL;
    public static final TypeAdapter<BigInteger> BIG_INTEGER;
    public static final TypeAdapter<BitSet> BIT_SET;
    public static final TypeAdapterFactory BIT_SET_FACTORY;
    public static final TypeAdapter<Boolean> BOOLEAN;
    public static final TypeAdapter<Boolean> BOOLEAN_AS_STRING;
    public static final TypeAdapterFactory BOOLEAN_FACTORY;
    public static final TypeAdapter<Number> BYTE;
    public static final TypeAdapterFactory BYTE_FACTORY;
    public static final TypeAdapter<Calendar> CALENDAR;
    public static final TypeAdapterFactory CALENDAR_FACTORY;
    public static final TypeAdapter<Character> CHARACTER;
    public static final TypeAdapterFactory CHARACTER_FACTORY;
    public static final TypeAdapter<Class> CLASS;
    public static final TypeAdapterFactory CLASS_FACTORY;
    public static final TypeAdapter<Number> DOUBLE;
    public static final TypeAdapterFactory ENUM_FACTORY;
    public static final TypeAdapter<Number> FLOAT;
    public static final TypeAdapter<InetAddress> INET_ADDRESS;
    public static final TypeAdapterFactory INET_ADDRESS_FACTORY;
    public static final TypeAdapter<Number> INTEGER;
    public static final TypeAdapterFactory INTEGER_FACTORY;
    public static final TypeAdapter<JsonElement> JSON_ELEMENT;
    public static final TypeAdapterFactory JSON_ELEMENT_FACTORY;
    public static final TypeAdapter<Locale> LOCALE;
    public static final TypeAdapterFactory LOCALE_FACTORY;
    public static final TypeAdapter<Number> LONG;
    public static final TypeAdapter<Number> NUMBER;
    public static final TypeAdapterFactory NUMBER_FACTORY;
    public static final TypeAdapter<Number> SHORT;
    public static final TypeAdapterFactory SHORT_FACTORY;
    public static final TypeAdapter<String> STRING;
    public static final TypeAdapter<StringBuffer> STRING_BUFFER;
    public static final TypeAdapterFactory STRING_BUFFER_FACTORY;
    public static final TypeAdapter<StringBuilder> STRING_BUILDER;
    public static final TypeAdapterFactory STRING_BUILDER_FACTORY;
    public static final TypeAdapterFactory STRING_FACTORY;
    public static final TypeAdapterFactory TIMESTAMP_FACTORY;
    public static final TypeAdapter<URI> URI;
    public static final TypeAdapterFactory URI_FACTORY;
    public static final TypeAdapter<URL> URL;
    public static final TypeAdapterFactory URL_FACTORY;
    public static final TypeAdapter<UUID> UUID;
    public static final TypeAdapterFactory UUID_FACTORY;

    static /* synthetic */ class AnonymousClass_32 {
        static final /* synthetic */ int[] $SwitchMap$com$millennialmedia$google$gson$stream$JsonToken;

        static {
            $SwitchMap$com$millennialmedia$google$gson$stream$JsonToken = new int[JsonToken.values().length];
            try {
                $SwitchMap$com$millennialmedia$google$gson$stream$JsonToken[JsonToken.NUMBER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$millennialmedia$google$gson$stream$JsonToken[JsonToken.BOOLEAN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$millennialmedia$google$gson$stream$JsonToken[JsonToken.STRING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$millennialmedia$google$gson$stream$JsonToken[JsonToken.NULL.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$millennialmedia$google$gson$stream$JsonToken[JsonToken.BEGIN_ARRAY.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$millennialmedia$google$gson$stream$JsonToken[JsonToken.BEGIN_OBJECT.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$millennialmedia$google$gson$stream$JsonToken[JsonToken.END_DOCUMENT.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$millennialmedia$google$gson$stream$JsonToken[JsonToken.NAME.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$millennialmedia$google$gson$stream$JsonToken[JsonToken.END_OBJECT.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            $SwitchMap$com$millennialmedia$google$gson$stream$JsonToken[JsonToken.END_ARRAY.ordinal()] = 10;
        }
    }

    static class AnonymousClass_27 implements TypeAdapterFactory {
        final /* synthetic */ TypeToken val$type;
        final /* synthetic */ TypeAdapter val$typeAdapter;

        AnonymousClass_27(TypeToken typeToken, TypeAdapter typeAdapter) {
            this.val$type = typeToken;
            this.val$typeAdapter = typeAdapter;
        }

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            return typeToken.equals(this.val$type) ? this.val$typeAdapter : null;
        }
    }

    static class AnonymousClass_28 implements TypeAdapterFactory {
        final /* synthetic */ Class val$type;
        final /* synthetic */ TypeAdapter val$typeAdapter;

        AnonymousClass_28(Class cls, TypeAdapter typeAdapter) {
            this.val$type = cls;
            this.val$typeAdapter = typeAdapter;
        }

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            return typeToken.getRawType() == this.val$type ? this.val$typeAdapter : null;
        }

        public String toString() {
            return "Factory[type=" + this.val$type.getName() + ",adapter=" + this.val$typeAdapter + "]";
        }
    }

    static class AnonymousClass_29 implements TypeAdapterFactory {
        final /* synthetic */ Class val$boxed;
        final /* synthetic */ TypeAdapter val$typeAdapter;
        final /* synthetic */ Class val$unboxed;

        AnonymousClass_29(Class cls, Class cls2, TypeAdapter typeAdapter) {
            this.val$unboxed = cls;
            this.val$boxed = cls2;
            this.val$typeAdapter = typeAdapter;
        }

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            Class<? super T> rawType = typeToken.getRawType();
            return (rawType == this.val$unboxed || rawType == this.val$boxed) ? this.val$typeAdapter : null;
        }

        public String toString() {
            return "Factory[type=" + this.val$boxed.getName() + "+" + this.val$unboxed.getName() + ",adapter=" + this.val$typeAdapter + "]";
        }
    }

    static class AnonymousClass_30 implements TypeAdapterFactory {
        final /* synthetic */ Class val$base;
        final /* synthetic */ Class val$sub;
        final /* synthetic */ TypeAdapter val$typeAdapter;

        AnonymousClass_30(Class cls, Class cls2, TypeAdapter typeAdapter) {
            this.val$base = cls;
            this.val$sub = cls2;
            this.val$typeAdapter = typeAdapter;
        }

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            Class<? super T> rawType = typeToken.getRawType();
            return (rawType == this.val$base || rawType == this.val$sub) ? this.val$typeAdapter : null;
        }

        public String toString() {
            return "Factory[type=" + this.val$base.getName() + "+" + this.val$sub.getName() + ",adapter=" + this.val$typeAdapter + "]";
        }
    }

    static class AnonymousClass_31 implements TypeAdapterFactory {
        final /* synthetic */ Class val$clazz;
        final /* synthetic */ TypeAdapter val$typeAdapter;

        AnonymousClass_31(Class cls, TypeAdapter typeAdapter) {
            this.val$clazz = cls;
            this.val$typeAdapter = typeAdapter;
        }

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            return this.val$clazz.isAssignableFrom(typeToken.getRawType()) ? this.val$typeAdapter : null;
        }

        public String toString() {
            return "Factory[typeHierarchy=" + this.val$clazz.getName() + ",adapter=" + this.val$typeAdapter + "]";
        }
    }

    private static final class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
        private final Map<T, String> constantToName;
        private final Map<String, T> nameToConstant;

        public EnumTypeAdapter(Class<T> classOfT) {
            this.nameToConstant = new HashMap();
            this.constantToName = new HashMap();
            try {
                Enum[] arr$ = (Enum[]) classOfT.getEnumConstants();
                int len$ = arr$.length;
                int i$ = 0;
                while (i$ < len$) {
                    T constant = arr$[i$];
                    String name = constant.name();
                    SerializedName annotation = (SerializedName) classOfT.getField(name).getAnnotation(SerializedName.class);
                    if (annotation != null) {
                        name = annotation.value();
                    }
                    this.nameToConstant.put(name, constant);
                    this.constantToName.put(constant, name);
                    i$++;
                }
            } catch (NoSuchFieldException e) {
                throw new AssertionError();
            }
        }

        public T read(JsonReader in) throws IOException {
            if (in.peek() != JsonToken.NULL) {
                return (Enum) this.nameToConstant.get(in.nextString());
            }
            in.nextNull();
            return null;
        }

        public void write(JsonWriter out, T value) throws IOException {
            out.value(value == null ? null : (String) this.constantToName.get(value));
        }
    }

    static {
        CLASS = new TypeAdapter<Class>() {
            public Class read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                } else {
                    throw new UnsupportedOperationException("Attempted to deserialize a java.lang.Class. Forgot to register a type adapter?");
                }
            }

            public void write(JsonWriter out, Class value) throws IOException {
                if (value == null) {
                    out.nullValue();
                } else {
                    throw new UnsupportedOperationException("Attempted to serialize java.lang.Class: " + value.getName() + ". Forgot to register a type adapter?");
                }
            }
        };
        CLASS_FACTORY = newFactory(Class.class, CLASS);
        BIT_SET = new TypeAdapter<BitSet>() {
            public BitSet read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                } else {
                    BitSet bitset = new BitSet();
                    in.beginArray();
                    int i = 0;
                    JsonToken tokenType = in.peek();
                    while (tokenType != JsonToken.END_ARRAY) {
                        boolean set;
                        switch (AnonymousClass_32.$SwitchMap$com$millennialmedia$google$gson$stream$JsonToken[tokenType.ordinal()]) {
                            case MMAdView.TRANSITION_FADE:
                                if (in.nextInt() != 0) {
                                    set = true;
                                } else {
                                    set = false;
                                }
                                break;
                            case MMAdView.TRANSITION_UP:
                                set = in.nextBoolean();
                                break;
                            case MMAdView.TRANSITION_DOWN:
                                String stringValue = in.nextString();
                                try {
                                    set = Integer.parseInt(stringValue) != 0;
                                } catch (NumberFormatException e) {
                                    throw new JsonSyntaxException("Error: Expecting: bitset number value (1, 0), Found: " + stringValue);
                                }
                                break;
                            default:
                                throw new JsonSyntaxException("Invalid bitset value type: " + tokenType);
                        }
                        if (set) {
                            bitset.set(i);
                        }
                        i++;
                        tokenType = in.peek();
                    }
                    in.endArray();
                    return bitset;
                }
            }

            public void write(JsonWriter out, BitSet src) throws IOException {
                if (src == null) {
                    out.nullValue();
                } else {
                    out.beginArray();
                    int i = 0;
                    while (i < src.length()) {
                        out.value((long) (src.get(i) ? 1 : 0));
                        i++;
                    }
                    out.endArray();
                }
            }
        };
        BIT_SET_FACTORY = newFactory(BitSet.class, BIT_SET);
        BOOLEAN = new TypeAdapter<Boolean>() {
            public Boolean read(JsonReader in) throws IOException {
                if (in.peek() != JsonToken.NULL) {
                    return in.peek() == JsonToken.STRING ? Boolean.valueOf(Boolean.parseBoolean(in.nextString())) : Boolean.valueOf(in.nextBoolean());
                } else {
                    in.nextNull();
                    return null;
                }
            }

            public void write(JsonWriter out, Boolean value) throws IOException {
                if (value == null) {
                    out.nullValue();
                } else {
                    out.value(value.booleanValue());
                }
            }
        };
        BOOLEAN_AS_STRING = new TypeAdapter<Boolean>() {
            public Boolean read(JsonReader in) throws IOException {
                if (in.peek() != JsonToken.NULL) {
                    return Boolean.valueOf(in.nextString());
                }
                in.nextNull();
                return null;
            }

            public void write(JsonWriter out, Boolean value) throws IOException {
                out.value(value == null ? "null" : value.toString());
            }
        };
        BOOLEAN_FACTORY = newFactory(Boolean.TYPE, Boolean.class, BOOLEAN);
        BYTE = new TypeAdapter<Number>() {
            public Number read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                } else {
                    try {
                        return Byte.valueOf((byte) in.nextInt());
                    } catch (NumberFormatException e) {
                        throw new JsonSyntaxException(e);
                    }
                }
            }

            public void write(JsonWriter out, Number value) throws IOException {
                out.value(value);
            }
        };
        BYTE_FACTORY = newFactory(Byte.TYPE, Byte.class, BYTE);
        SHORT = new TypeAdapter<Number>() {
            public Number read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                } else {
                    try {
                        return Short.valueOf((short) in.nextInt());
                    } catch (NumberFormatException e) {
                        throw new JsonSyntaxException(e);
                    }
                }
            }

            public void write(JsonWriter out, Number value) throws IOException {
                out.value(value);
            }
        };
        SHORT_FACTORY = newFactory(Short.TYPE, Short.class, SHORT);
        INTEGER = new TypeAdapter<Number>() {
            public Number read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                } else {
                    try {
                        return Integer.valueOf(in.nextInt());
                    } catch (NumberFormatException e) {
                        throw new JsonSyntaxException(e);
                    }
                }
            }

            public void write(JsonWriter out, Number value) throws IOException {
                out.value(value);
            }
        };
        INTEGER_FACTORY = newFactory(Integer.TYPE, Integer.class, INTEGER);
        LONG = new TypeAdapter<Number>() {
            public Number read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                } else {
                    try {
                        return Long.valueOf(in.nextLong());
                    } catch (NumberFormatException e) {
                        throw new JsonSyntaxException(e);
                    }
                }
            }

            public void write(JsonWriter out, Number value) throws IOException {
                out.value(value);
            }
        };
        FLOAT = new TypeAdapter<Number>() {
            public Number read(JsonReader in) throws IOException {
                if (in.peek() != JsonToken.NULL) {
                    return Float.valueOf((float) in.nextDouble());
                }
                in.nextNull();
                return null;
            }

            public void write(JsonWriter out, Number value) throws IOException {
                out.value(value);
            }
        };
        DOUBLE = new TypeAdapter<Number>() {
            public Number read(JsonReader in) throws IOException {
                if (in.peek() != JsonToken.NULL) {
                    return Double.valueOf(in.nextDouble());
                }
                in.nextNull();
                return null;
            }

            public void write(JsonWriter out, Number value) throws IOException {
                out.value(value);
            }
        };
        NUMBER = new TypeAdapter<Number>() {
            public Number read(JsonReader in) throws IOException {
                JsonToken jsonToken = in.peek();
                switch (AnonymousClass_32.$SwitchMap$com$millennialmedia$google$gson$stream$JsonToken[jsonToken.ordinal()]) {
                    case MMAdView.TRANSITION_FADE:
                        return new LazilyParsedNumber(in.nextString());
                    case MMAdView.TRANSITION_RANDOM:
                        in.nextNull();
                        return null;
                    default:
                        throw new JsonSyntaxException("Expecting number, got: " + jsonToken);
                }
            }

            public void write(JsonWriter out, Number value) throws IOException {
                out.value(value);
            }
        };
        NUMBER_FACTORY = newFactory(Number.class, NUMBER);
        CHARACTER = new TypeAdapter<Character>() {
            public Character read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                } else {
                    String str = in.nextString();
                    if (str.length() == 1) {
                        return Character.valueOf(str.charAt(0));
                    }
                    throw new JsonSyntaxException("Expecting character, got: " + str);
                }
            }

            public void write(JsonWriter out, Character value) throws IOException {
                out.value(value == null ? null : String.valueOf(value));
            }
        };
        CHARACTER_FACTORY = newFactory(Character.TYPE, Character.class, CHARACTER);
        STRING = new TypeAdapter<String>() {
            public String read(JsonReader in) throws IOException {
                JsonToken peek = in.peek();
                if (peek != JsonToken.NULL) {
                    return peek == JsonToken.BOOLEAN ? Boolean.toString(in.nextBoolean()) : in.nextString();
                } else {
                    in.nextNull();
                    return null;
                }
            }

            public void write(JsonWriter out, String value) throws IOException {
                out.value(value);
            }
        };
        BIG_DECIMAL = new TypeAdapter<BigDecimal>() {
            public BigDecimal read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                } else {
                    try {
                        return new BigDecimal(in.nextString());
                    } catch (NumberFormatException e) {
                        throw new JsonSyntaxException(e);
                    }
                }
            }

            public void write(JsonWriter out, Number value) throws IOException {
                out.value(value);
            }
        };
        BIG_INTEGER = new TypeAdapter<BigInteger>() {
            public BigInteger read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                } else {
                    try {
                        return new BigInteger(in.nextString());
                    } catch (NumberFormatException e) {
                        throw new JsonSyntaxException(e);
                    }
                }
            }

            public void write(JsonWriter out, Number value) throws IOException {
                out.value(value);
            }
        };
        STRING_FACTORY = newFactory(String.class, STRING);
        STRING_BUILDER = new TypeAdapter<StringBuilder>() {
            public StringBuilder read(JsonReader in) throws IOException {
                if (in.peek() != JsonToken.NULL) {
                    return new StringBuilder(in.nextString());
                }
                in.nextNull();
                return null;
            }

            public void write(JsonWriter out, StringBuilder value) throws IOException {
                out.value(value == null ? null : value.toString());
            }
        };
        STRING_BUILDER_FACTORY = newFactory(StringBuilder.class, STRING_BUILDER);
        STRING_BUFFER = new TypeAdapter<StringBuffer>() {
            public StringBuffer read(JsonReader in) throws IOException {
                if (in.peek() != JsonToken.NULL) {
                    return new StringBuffer(in.nextString());
                }
                in.nextNull();
                return null;
            }

            public void write(JsonWriter out, StringBuffer value) throws IOException {
                out.value(value == null ? null : value.toString());
            }
        };
        STRING_BUFFER_FACTORY = newFactory(StringBuffer.class, STRING_BUFFER);
        URL = new TypeAdapter<URL>() {
            public URL read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                } else {
                    String nextString = in.nextString();
                    return !"null".equals(nextString) ? new URL(nextString) : null;
                }
            }

            public void write(JsonWriter out, URL value) throws IOException {
                out.value(value == null ? null : value.toExternalForm());
            }
        };
        URL_FACTORY = newFactory(URL.class, URL);
        URI = new TypeAdapter<URI>() {
            public URI read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                } else {
                    try {
                        String nextString = in.nextString();
                        return !"null".equals(nextString) ? new URI(nextString) : null;
                    } catch (URISyntaxException e) {
                        throw new JsonIOException(e);
                    }
                }
            }

            public void write(JsonWriter out, URI value) throws IOException {
                out.value(value == null ? null : value.toASCIIString());
            }
        };
        URI_FACTORY = newFactory(URI.class, URI);
        INET_ADDRESS = new TypeAdapter<InetAddress>() {
            public InetAddress read(JsonReader in) throws IOException {
                if (in.peek() != JsonToken.NULL) {
                    return InetAddress.getByName(in.nextString());
                }
                in.nextNull();
                return null;
            }

            public void write(JsonWriter out, InetAddress value) throws IOException {
                out.value(value == null ? null : value.getHostAddress());
            }
        };
        INET_ADDRESS_FACTORY = newTypeHierarchyFactory(InetAddress.class, INET_ADDRESS);
        UUID = new TypeAdapter<UUID>() {
            public UUID read(JsonReader in) throws IOException {
                if (in.peek() != JsonToken.NULL) {
                    return UUID.fromString(in.nextString());
                }
                in.nextNull();
                return null;
            }

            public void write(JsonWriter out, UUID value) throws IOException {
                out.value(value == null ? null : value.toString());
            }
        };
        UUID_FACTORY = newFactory(UUID.class, UUID);
        TIMESTAMP_FACTORY = new TypeAdapterFactory() {

            class AnonymousClass_1 extends TypeAdapter<Timestamp> {
                final /* synthetic */ TypeAdapter val$dateTypeAdapter;

                AnonymousClass_1(TypeAdapter typeAdapter) {
                    this.val$dateTypeAdapter = typeAdapter;
                }

                public Timestamp read(JsonReader in) throws IOException {
                    Date date = (Date) this.val$dateTypeAdapter.read(in);
                    return date != null ? new Timestamp(date.getTime()) : null;
                }

                public void write(JsonWriter out, Timestamp value) throws IOException {
                    this.val$dateTypeAdapter.write(out, value);
                }
            }

            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                return typeToken.getRawType() != Timestamp.class ? null : new AnonymousClass_1(gson.getAdapter(Date.class));
            }
        };
        CALENDAR = new TypeAdapter<Calendar>() {
            private static final String DAY_OF_MONTH = "dayOfMonth";
            private static final String HOUR_OF_DAY = "hourOfDay";
            private static final String MINUTE = "minute";
            private static final String MONTH = "month";
            private static final String SECOND = "second";
            private static final String YEAR = "year";

            public Calendar read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                } else {
                    in.beginObject();
                    int i = 0;
                    int i2 = 0;
                    int i3 = 0;
                    int i4 = 0;
                    int i5 = 0;
                    int i6 = 0;
                    while (in.peek() != JsonToken.END_OBJECT) {
                        String name = in.nextName();
                        int value = in.nextInt();
                        if (YEAR.equals(name)) {
                            i = value;
                        } else if (MONTH.equals(name)) {
                            i2 = value;
                        } else if (DAY_OF_MONTH.equals(name)) {
                            i3 = value;
                        } else if (HOUR_OF_DAY.equals(name)) {
                            i4 = value;
                        } else if (MINUTE.equals(name)) {
                            i5 = value;
                        } else if (SECOND.equals(name)) {
                            i6 = value;
                        }
                    }
                    in.endObject();
                    return new GregorianCalendar(i, i2, i3, i4, i5, i6);
                }
            }

            public void write(JsonWriter out, Calendar value) throws IOException {
                if (value == null) {
                    out.nullValue();
                } else {
                    out.beginObject();
                    out.name(YEAR);
                    out.value((long) value.get(1));
                    out.name(MONTH);
                    out.value((long) value.get(MMAdView.TRANSITION_UP));
                    out.name(DAY_OF_MONTH);
                    out.value((long) value.get(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES));
                    out.name(HOUR_OF_DAY);
                    out.value((long) value.get(ApiEventType.API_MRAID_EXPAND));
                    out.name(MINUTE);
                    out.value((long) value.get(ApiEventType.API_MRAID_RESIZE));
                    out.name(SECOND);
                    out.value((long) value.get(ApiEventType.API_MRAID_CLOSE));
                    out.endObject();
                }
            }
        };
        CALENDAR_FACTORY = newFactoryForMultipleTypes(Calendar.class, GregorianCalendar.class, CALENDAR);
        LOCALE = new TypeAdapter<Locale>() {
            public Locale read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                } else {
                    StringTokenizer tokenizer = new StringTokenizer(in.nextString(), "_");
                    String language = null;
                    String country = null;
                    String variant = null;
                    if (tokenizer.hasMoreElements()) {
                        language = tokenizer.nextToken();
                    }
                    if (tokenizer.hasMoreElements()) {
                        country = tokenizer.nextToken();
                    }
                    if (tokenizer.hasMoreElements()) {
                        variant = tokenizer.nextToken();
                    }
                    if (country == null && variant == null) {
                        return new Locale(language);
                    }
                    return variant == null ? new Locale(language, country) : new Locale(language, country, variant);
                }
            }

            public void write(JsonWriter out, Locale value) throws IOException {
                out.value(value == null ? null : value.toString());
            }
        };
        LOCALE_FACTORY = newFactory(Locale.class, LOCALE);
        JSON_ELEMENT = new TypeAdapter<JsonElement>() {
            public JsonElement read(JsonReader in) throws IOException {
                switch (AnonymousClass_32.$SwitchMap$com$millennialmedia$google$gson$stream$JsonToken[in.peek().ordinal()]) {
                    case MMAdView.TRANSITION_FADE:
                        return new JsonPrimitive(new LazilyParsedNumber(in.nextString()));
                    case MMAdView.TRANSITION_UP:
                        return new JsonPrimitive(Boolean.valueOf(in.nextBoolean()));
                    case MMAdView.TRANSITION_DOWN:
                        return new JsonPrimitive(in.nextString());
                    case MMAdView.TRANSITION_RANDOM:
                        in.nextNull();
                        return JsonNull.INSTANCE;
                    case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                        JsonElement array = new JsonArray();
                        in.beginArray();
                        while (in.hasNext()) {
                            array.add(read(in));
                        }
                        in.endArray();
                        return array;
                    case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                        JsonElement object = new JsonObject();
                        in.beginObject();
                        while (in.hasNext()) {
                            object.add(in.nextName(), read(in));
                        }
                        in.endObject();
                        return object;
                    default:
                        throw new IllegalArgumentException();
                }
            }

            public void write(JsonWriter out, JsonElement value) throws IOException {
                if (value == null || value.isJsonNull()) {
                    out.nullValue();
                } else if (value.isJsonPrimitive()) {
                    JsonPrimitive primitive = value.getAsJsonPrimitive();
                    if (primitive.isNumber()) {
                        out.value(primitive.getAsNumber());
                    } else if (primitive.isBoolean()) {
                        out.value(primitive.getAsBoolean());
                    } else {
                        out.value(primitive.getAsString());
                    }
                } else if (value.isJsonArray()) {
                    out.beginArray();
                    i$ = value.getAsJsonArray().iterator();
                    while (i$.hasNext()) {
                        write(out, (JsonElement) i$.next());
                    }
                    out.endArray();
                } else if (value.isJsonObject()) {
                    out.beginObject();
                    i$ = value.getAsJsonObject().entrySet().iterator();
                    while (i$.hasNext()) {
                        Entry<String, JsonElement> e = (Entry) i$.next();
                        out.name((String) e.getKey());
                        write(out, (JsonElement) e.getValue());
                    }
                    out.endObject();
                } else {
                    throw new IllegalArgumentException("Couldn't write " + value.getClass());
                }
            }
        };
        JSON_ELEMENT_FACTORY = newTypeHierarchyFactory(JsonElement.class, JSON_ELEMENT);
        ENUM_FACTORY = newEnumTypeHierarchyFactory();
    }

    private TypeAdapters() {
    }

    public static TypeAdapterFactory newEnumTypeHierarchyFactory() {
        return new TypeAdapterFactory() {
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                Class rawType = typeToken.getRawType();
                if (!Enum.class.isAssignableFrom(rawType) || rawType == Enum.class) {
                    return null;
                }
                if (!rawType.isEnum()) {
                    rawType = rawType.getSuperclass();
                }
                return new EnumTypeAdapter(rawType);
            }
        };
    }

    public static <TT> TypeAdapterFactory newFactory(TypeToken<TT> type, TypeAdapter<TT> typeAdapter) {
        return new AnonymousClass_27(type, typeAdapter);
    }

    public static <TT> TypeAdapterFactory newFactory(Class<TT> type, TypeAdapter<TT> typeAdapter) {
        return new AnonymousClass_28(type, typeAdapter);
    }

    public static <TT> TypeAdapterFactory newFactory(Class<TT> unboxed, Class<TT> boxed, TypeAdapter<? super TT> typeAdapter) {
        return new AnonymousClass_29(unboxed, boxed, typeAdapter);
    }

    public static <TT> TypeAdapterFactory newFactoryForMultipleTypes(Class<TT> base, Class<? extends TT> sub, TypeAdapter<? super TT> typeAdapter) {
        return new AnonymousClass_30(base, sub, typeAdapter);
    }

    public static <TT> TypeAdapterFactory newTypeHierarchyFactory(Class<TT> clazz, TypeAdapter<TT> typeAdapter) {
        return new AnonymousClass_31(clazz, typeAdapter);
    }
}