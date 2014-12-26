package org.codehaus.jackson.map.util;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.io.SerializedString;
import org.codehaus.jackson.map.AnnotationIntrospector;

public final class EnumValues {
    private final EnumMap<?, SerializedString> _values;

    private EnumValues(Map<Enum<?>, SerializedString> v) {
        this._values = new EnumMap(v);
    }

    public static EnumValues construct(Class<Enum<?>> enumClass, AnnotationIntrospector intr) {
        return constructFromName(enumClass, intr);
    }

    public static EnumValues constructFromName(Class enumClass, AnnotationIntrospector intr) {
        Enum[] values = (Enum[]) ClassUtil.findEnumType(enumClass).getEnumConstants();
        if (values != null) {
            Map<Enum<?>, SerializedString> map = new HashMap();
            Enum[] arr$ = values;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                Enum<?> en = arr$[i$];
                map.put(en, new SerializedString(intr.findEnumValue(en)));
                i$++;
            }
            return new EnumValues(map);
        } else {
            throw new IllegalArgumentException("Can not determine enum constants for Class " + enumClass.getName());
        }
    }

    public static EnumValues constructFromToString(Class enumClass, AnnotationIntrospector intr) {
        Enum[] values = (Enum[]) ClassUtil.findEnumType(enumClass).getEnumConstants();
        if (values != null) {
            Map<Enum<?>, SerializedString> map = new HashMap();
            Enum[] arr$ = values;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                Enum<?> en = arr$[i$];
                map.put(en, new SerializedString(en.toString()));
                i$++;
            }
            return new EnumValues(map);
        } else {
            throw new IllegalArgumentException("Can not determine enum constants for Class " + enumClass.getName());
        }
    }

    public SerializedString serializedValueFor(Enum<?> key) {
        return (SerializedString) this._values.get(key);
    }

    @Deprecated
    public String valueFor(Enum<?> key) {
        SerializedString sstr = (SerializedString) this._values.get(key);
        return sstr == null ? null : sstr.getValue();
    }

    public Collection<SerializedString> values() {
        return this._values.values();
    }
}