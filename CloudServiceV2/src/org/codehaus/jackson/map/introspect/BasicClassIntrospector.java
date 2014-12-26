package org.codehaus.jackson.map.introspect;

import com.wmt.data.LocalAudioAll;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ClassIntrospector;
import org.codehaus.jackson.map.ClassIntrospector.MixInResolver;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.type.JavaType;

public class BasicClassIntrospector extends ClassIntrospector<BasicBeanDescription> {
    public static final GetterMethodFilter DEFAULT_GETTER_FILTER;
    public static final SetterAndGetterMethodFilter DEFAULT_SETTER_AND_GETTER_FILTER;
    public static final SetterMethodFilter DEFAULT_SETTER_FILTER;
    public static final BasicClassIntrospector instance;

    public static class GetterMethodFilter implements MethodFilter {
        private GetterMethodFilter() {
        }

        public boolean includeMethod(Method m) {
            return ClassUtil.hasGetterSignature(m);
        }
    }

    public static class SetterMethodFilter implements MethodFilter {
        public boolean includeMethod(Method m) {
            if (Modifier.isStatic(m.getModifiers())) {
                return false;
            }
            switch (m.getParameterTypes().length) {
                case LocalAudioAll.SORT_BY_DATE:
                    return true;
                case ClassWriter.COMPUTE_FRAMES:
                    return true;
                default:
                    return false;
            }
        }
    }

    public static final class SetterAndGetterMethodFilter extends org.codehaus.jackson.map.introspect.BasicClassIntrospector.SetterMethodFilter {
        public boolean includeMethod(Method m) {
            if (super.includeMethod(m)) {
                return true;
            }
            if (!ClassUtil.hasGetterSignature(m)) {
                return false;
            }
            Class<?> rt = m.getReturnType();
            return Collection.class.isAssignableFrom(rt) || Map.class.isAssignableFrom(rt);
        }
    }

    static {
        DEFAULT_GETTER_FILTER = new GetterMethodFilter();
        DEFAULT_SETTER_FILTER = new SetterMethodFilter();
        DEFAULT_SETTER_AND_GETTER_FILTER = new SetterAndGetterMethodFilter();
        instance = new BasicClassIntrospector();
    }

    public BasicBeanDescription forClassAnnotations(MapperConfig<?> cfg, Class<?> c, MixInResolver r) {
        boolean useAnnotations = cfg.isAnnotationProcessingEnabled();
        AnnotationIntrospector ai = cfg.getAnnotationIntrospector();
        if (!useAnnotations) {
            ai = null;
        }
        return new BasicBeanDescription(cfg, cfg.constructType(c), AnnotatedClass.construct(c, ai, r));
    }

    public BasicBeanDescription forCreation(DeserializationConfig cfg, JavaType type, MixInResolver r) {
        boolean useAnnotations = cfg.isAnnotationProcessingEnabled();
        AnnotationIntrospector ai = cfg.getAnnotationIntrospector();
        Class rawClass = type.getRawClass();
        if (!useAnnotations) {
            ai = null;
        }
        AnnotatedClass ac = AnnotatedClass.construct(rawClass, ai, r);
        ac.resolveCreators(true);
        return new BasicBeanDescription(cfg, type, ac);
    }

    public BasicBeanDescription forDeserialization(DeserializationConfig cfg, JavaType type, MixInResolver r) {
        boolean useAnnotations = cfg.isAnnotationProcessingEnabled();
        AnnotationIntrospector ai = cfg.getAnnotationIntrospector();
        Class rawClass = type.getRawClass();
        if (!useAnnotations) {
            ai = null;
        }
        AnnotatedClass ac = AnnotatedClass.construct(rawClass, ai, r);
        ac.resolveMemberMethods(getDeserializationMethodFilter(cfg), true);
        ac.resolveCreators(true);
        ac.resolveFields(true);
        return new BasicBeanDescription(cfg, type, ac);
    }

    public BasicBeanDescription forDirectClassAnnotations(MapperConfig<?> cfg, Class<?> c, MixInResolver r) {
        boolean useAnnotations = cfg.isAnnotationProcessingEnabled();
        AnnotationIntrospector ai = cfg.getAnnotationIntrospector();
        if (!useAnnotations) {
            ai = null;
        }
        return new BasicBeanDescription(cfg, cfg.constructType(c), AnnotatedClass.constructWithoutSuperTypes(c, ai, r));
    }

    public BasicBeanDescription forSerialization(SerializationConfig cfg, JavaType type, MixInResolver r) {
        AnnotatedClass ac = AnnotatedClass.construct(type.getRawClass(), cfg.getAnnotationIntrospector(), r);
        ac.resolveMemberMethods(getSerializationMethodFilter(cfg), false);
        ac.resolveCreators(true);
        ac.resolveFields(false);
        return new BasicBeanDescription(cfg, type, ac);
    }

    protected MethodFilter getDeserializationMethodFilter(DeserializationConfig cfg) {
        return cfg.isEnabled(Feature.USE_GETTERS_AS_SETTERS) ? DEFAULT_SETTER_AND_GETTER_FILTER : DEFAULT_SETTER_FILTER;
    }

    protected MethodFilter getSerializationMethodFilter(SerializationConfig cfg) {
        return DEFAULT_GETTER_FILTER;
    }
}