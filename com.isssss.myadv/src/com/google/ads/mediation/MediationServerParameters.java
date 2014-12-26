package com.google.ads.mediation;

import com.google.android.gms.internal.dw;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

@Deprecated
public abstract class MediationServerParameters {

    public static final class MappingException extends Exception {
        public MappingException(String message) {
            super(message);
        }
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    protected static @interface Parameter {
        String name();

        boolean required() default true;
    }

    protected void a() {
    }

    public void load(Map<String, String> parameters) throws MappingException {
        Map hashMap = new HashMap();
        Field[] fields = getClass().getFields();
        int length = fields.length;
        int i = 0;
        while (i < length) {
            Field field = fields[i];
            Parameter parameter = (Parameter) field.getAnnotation(Parameter.class);
            if (parameter != null) {
                hashMap.put(parameter.name(), field);
            }
            i++;
        }
        if (hashMap.isEmpty()) {
            dw.z("No server options fields detected. To suppress this message either add a field with the @Parameter annotation, or override the load() method.");
        }
        Iterator it = parameters.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            Field field2 = (Field) hashMap.remove(entry.getKey());
            if (field2 != null) {
                try {
                    field2.set(this, entry.getValue());
                } catch (IllegalAccessException e) {
                    dw.z("Server option \"" + ((String) entry.getKey()) + "\" could not be set: Illegal Access");
                } catch (IllegalArgumentException e2) {
                    dw.z("Server option \"" + ((String) entry.getKey()) + "\" could not be set: Bad Type");
                }
            } else {
                dw.v("Unexpected server option: " + ((String) entry.getKey()) + " = \"" + ((String) entry.getValue()) + "\"");
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        Iterator it2 = hashMap.values().iterator();
        while (it2.hasNext()) {
            field2 = (Field) it2.next();
            if (((Parameter) field2.getAnnotation(Parameter.class)).required()) {
                dw.z("Required server option missing: " + ((Parameter) field2.getAnnotation(Parameter.class)).name());
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(((Parameter) field2.getAnnotation(Parameter.class)).name());
            }
        }
        if (stringBuilder.length() > 0) {
            throw new MappingException("Required server option(s) missing: " + stringBuilder.toString());
        }
        a();
    }
}