package org.codehaus.jackson.map.deser;

import com.wmt.data.MediaItem;
import java.lang.reflect.Type;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.deser.StdDeserializer.AtomicBooleanDeserializer;
import org.codehaus.jackson.map.deser.StdDeserializer.BigDecimalDeserializer;
import org.codehaus.jackson.map.deser.StdDeserializer.BigIntegerDeserializer;
import org.codehaus.jackson.map.deser.StdDeserializer.BooleanDeserializer;
import org.codehaus.jackson.map.deser.StdDeserializer.ByteDeserializer;
import org.codehaus.jackson.map.deser.StdDeserializer.CalendarDeserializer;
import org.codehaus.jackson.map.deser.StdDeserializer.CharacterDeserializer;
import org.codehaus.jackson.map.deser.StdDeserializer.ClassDeserializer;
import org.codehaus.jackson.map.deser.StdDeserializer.DoubleDeserializer;
import org.codehaus.jackson.map.deser.StdDeserializer.FloatDeserializer;
import org.codehaus.jackson.map.deser.StdDeserializer.IntegerDeserializer;
import org.codehaus.jackson.map.deser.StdDeserializer.LongDeserializer;
import org.codehaus.jackson.map.deser.StdDeserializer.NumberDeserializer;
import org.codehaus.jackson.map.deser.StdDeserializer.ShortDeserializer;
import org.codehaus.jackson.map.deser.StdDeserializer.SqlDateDeserializer;
import org.codehaus.jackson.map.deser.StdDeserializer.StackTraceElementDeserializer;
import org.codehaus.jackson.map.deser.StdDeserializer.StringDeserializer;
import org.codehaus.jackson.map.deser.StdDeserializer.TokenBufferDeserializer;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

class StdDeserializers {
    final HashMap<JavaType, JsonDeserializer<Object>> _deserializers;

    private StdDeserializers() {
        this._deserializers = new HashMap();
        add(new UntypedObjectDeserializer());
        StdDeserializer<?> strDeser = new StringDeserializer();
        add(strDeser, String.class);
        add(strDeser, CharSequence.class);
        add(new ClassDeserializer());
        add(new BooleanDeserializer(Boolean.class, null));
        add(new ByteDeserializer(Byte.class, null));
        add(new ShortDeserializer(Short.class, null));
        add(new CharacterDeserializer(Character.class, null));
        add(new IntegerDeserializer(Integer.class, null));
        add(new LongDeserializer(Long.class, null));
        add(new FloatDeserializer(Float.class, null));
        add(new DoubleDeserializer(Double.class, null));
        add(new BooleanDeserializer(Boolean.TYPE, Boolean.FALSE));
        add(new ByteDeserializer(Byte.TYPE, Byte.valueOf((byte) 0)));
        add(new ShortDeserializer(Short.TYPE, Short.valueOf((short) 0)));
        add(new CharacterDeserializer(Character.TYPE, Character.valueOf('\u0000')));
        add(new IntegerDeserializer(Integer.TYPE, Integer.valueOf(0)));
        add(new LongDeserializer(Long.TYPE, Long.valueOf(0)));
        add(new FloatDeserializer(Float.TYPE, Float.valueOf(0.0f)));
        add(new DoubleDeserializer(Double.TYPE, Double.valueOf(MediaItem.INVALID_LATLNG)));
        add(new NumberDeserializer());
        add(new BigDecimalDeserializer());
        add(new BigIntegerDeserializer());
        add(new DateDeserializer());
        add(new SqlDateDeserializer());
        add(new TimestampDeserializer());
        add(new CalendarDeserializer());
        add(new CalendarDeserializer(GregorianCalendar.class), GregorianCalendar.class);
        Iterator i$ = FromStringDeserializer.all().iterator();
        while (i$.hasNext()) {
            add((FromStringDeserializer) i$.next());
        }
        add(new StackTraceElementDeserializer());
        add(new TokenBufferDeserializer());
        add(new AtomicBooleanDeserializer());
    }

    private void add(StdDeserializer<?> stdDeser) {
        add(stdDeser, stdDeser.getValueClass());
    }

    private void add(JsonDeserializer stdDeser, Type valueClass) {
        this._deserializers.put(TypeFactory.defaultInstance().constructType(valueClass), stdDeser);
    }

    public static HashMap<JavaType, JsonDeserializer<Object>> constructAll() {
        return new StdDeserializers()._deserializers;
    }
}