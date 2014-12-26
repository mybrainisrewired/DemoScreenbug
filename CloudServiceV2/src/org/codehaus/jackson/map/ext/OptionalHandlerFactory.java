package org.codehaus.jackson.map.ext;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.deser.StdDeserializer;
import org.codehaus.jackson.map.util.Provider;
import org.codehaus.jackson.type.JavaType;

public class OptionalHandlerFactory {
    private static final String CLASS_NAME_DOM_DOCUMENT = "org.w3c.dom.Node";
    private static final String CLASS_NAME_DOM_NODE = "org.w3c.dom.Node";
    private static final String DESERIALIZERS_FOR_JAVAX_XML = "org.codehaus.jackson.map.ext.CoreXMLDeserializers";
    private static final String DESERIALIZERS_FOR_JODA_DATETIME = "org.codehaus.jackson.map.ext.JodaDeserializers";
    private static final String DESERIALIZER_FOR_DOM_DOCUMENT = "org.codehaus.jackson.map.ext.DOMDeserializer$DocumentDeserializer";
    private static final String DESERIALIZER_FOR_DOM_NODE = "org.codehaus.jackson.map.ext.DOMDeserializer$NodeDeserializer";
    private static final String PACKAGE_PREFIX_JAVAX_XML = "javax.xml.";
    private static final String PACKAGE_PREFIX_JODA_DATETIME = "org.joda.time.";
    private static final String SERIALIZERS_FOR_JAVAX_XML = "org.codehaus.jackson.map.ext.CoreXMLSerializers";
    private static final String SERIALIZERS_FOR_JODA_DATETIME = "org.codehaus.jackson.map.ext.JodaSerializers";
    private static final String SERIALIZER_FOR_DOM_NODE = "org.codehaus.jackson.map.ext.DOMSerializer";
    public static final OptionalHandlerFactory instance;

    static {
        instance = new OptionalHandlerFactory();
    }

    protected OptionalHandlerFactory() {
    }

    private boolean doesImplement(Class<?> actualType, String classNameToImplement) {
        Class<?> type = actualType;
        while (type != null) {
            if (type.getName().equals(classNameToImplement) || hasInterface(type, classNameToImplement)) {
                return true;
            }
            type = type.getSuperclass();
        }
        return false;
    }

    private boolean hasInterface(Class<?> type, String interfaceToImplement) {
        Class<?>[] interfaces = type.getInterfaces();
        Class<?>[] arr$ = interfaces;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            if (arr$[i$].getName().equals(interfaceToImplement)) {
                return true;
            }
            i$++;
        }
        arr$ = interfaces;
        len$ = arr$.length;
        i$ = 0;
        while (i$ < len$) {
            if (hasInterface(arr$[i$], interfaceToImplement)) {
                return true;
            }
            i$++;
        }
        return false;
    }

    private boolean hasInterfaceStartingWith(Class<?> type, String prefix) {
        Class<?>[] interfaces = type.getInterfaces();
        Class<?>[] arr$ = interfaces;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            if (arr$[i$].getName().startsWith(prefix)) {
                return true;
            }
            i$++;
        }
        arr$ = interfaces;
        len$ = arr$.length;
        i$ = 0;
        while (i$ < len$) {
            if (hasInterfaceStartingWith(arr$[i$], prefix)) {
                return true;
            }
            i$++;
        }
        return false;
    }

    private boolean hasSupertypeStartingWith(Class<?> rawType, String prefix) {
        Class<?> supertype = rawType.getSuperclass();
        while (supertype != null) {
            if (supertype.getName().startsWith(prefix)) {
                return true;
            }
            supertype = supertype.getSuperclass();
        }
        Class<?> cls = rawType;
        while (cls != null) {
            if (hasInterfaceStartingWith(cls, prefix)) {
                return true;
            }
            cls = cls.getSuperclass();
        }
        return false;
    }

    private Object instantiate(String className) {
        try {
            return Class.forName(className).newInstance();
        } catch (LinkageError e) {
            return null;
        } catch (Exception e2) {
            return null;
        }
    }

    public JsonDeserializer<?> findDeserializer(JavaType type, DeserializationConfig config, DeserializerProvider p) {
        String factoryName;
        Class<?> rawType = type.getRawClass();
        String className = rawType.getName();
        if (className.startsWith(PACKAGE_PREFIX_JODA_DATETIME)) {
            factoryName = DESERIALIZERS_FOR_JODA_DATETIME;
        } else if (className.startsWith(PACKAGE_PREFIX_JAVAX_XML) || hasSupertypeStartingWith(rawType, PACKAGE_PREFIX_JAVAX_XML)) {
            factoryName = DESERIALIZERS_FOR_JAVAX_XML;
        } else if (doesImplement(rawType, CLASS_NAME_DOM_NODE)) {
            return (JsonDeserializer) instantiate(DESERIALIZER_FOR_DOM_DOCUMENT);
        } else {
            return doesImplement(rawType, CLASS_NAME_DOM_NODE) ? (JsonDeserializer) instantiate(DESERIALIZER_FOR_DOM_NODE) : null;
        }
        Object ob = instantiate(factoryName);
        if (ob == null) {
            return null;
        }
        Collection<StdDeserializer<?>> entries = ((Provider) ob).provide();
        Iterator i$ = entries.iterator();
        while (i$.hasNext()) {
            JsonDeserializer deser = (StdDeserializer) i$.next();
            if (rawType == deser.getValueClass()) {
                return deser;
            }
        }
        i$ = entries.iterator();
        while (i$.hasNext()) {
            deser = i$.next();
            if (deser.getValueClass().isAssignableFrom(rawType)) {
                return deser;
            }
        }
        return null;
    }

    public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type) {
        String factoryName;
        Class<?> rawType = type.getRawClass();
        String className = rawType.getName();
        if (className.startsWith(PACKAGE_PREFIX_JODA_DATETIME)) {
            factoryName = SERIALIZERS_FOR_JODA_DATETIME;
        } else if (!className.startsWith(PACKAGE_PREFIX_JAVAX_XML) && !hasSupertypeStartingWith(rawType, PACKAGE_PREFIX_JAVAX_XML)) {
            return doesImplement(rawType, CLASS_NAME_DOM_NODE) ? (JsonSerializer) instantiate(SERIALIZER_FOR_DOM_NODE) : null;
        } else {
            factoryName = SERIALIZERS_FOR_JAVAX_XML;
        }
        Object ob = instantiate(factoryName);
        if (ob == null) {
            return null;
        }
        Collection<Entry<Class<?>, JsonSerializer<?>>> entries = ((Provider) ob).provide();
        Iterator i$ = entries.iterator();
        while (i$.hasNext()) {
            Entry<Class<?>, JsonSerializer<?>> entry = (Entry) i$.next();
            if (rawType == entry.getKey()) {
                return (JsonSerializer) entry.getValue();
            }
        }
        i$ = entries.iterator();
        while (i$.hasNext()) {
            entry = i$.next();
            if (((Class) entry.getKey()).isAssignableFrom(rawType)) {
                return (JsonSerializer) entry.getValue();
            }
        }
        return null;
    }
}