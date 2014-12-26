package org.codehaus.jackson.mrbean;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.Versioned;
import org.codehaus.jackson.map.AbstractTypeResolver;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.util.VersionUtil;

public class AbstractTypeMaterializer extends AbstractTypeResolver implements Versioned {
    protected static final int DEFAULT_FEATURE_FLAGS;
    public static final String DEFAULT_PACKAGE_FOR_GENERATED = "org.codehaus.jackson.generated.";
    protected final MyClassLoader _classLoader;
    protected String _defaultPackage;
    protected int _featureFlags;

    public enum Feature {
        FAIL_ON_UNMATERIALIZED_METHOD(false);
        final boolean _defaultState;

        private Feature(boolean defaultState) {
            this._defaultState = defaultState;
        }

        protected static int collectDefaults() {
            int flags = DEFAULT_FEATURE_FLAGS;
            org.codehaus.jackson.mrbean.AbstractTypeMaterializer.Feature[] arr$ = values();
            int len$ = arr$.length;
            int i$ = DEFAULT_FEATURE_FLAGS;
            while (i$ < len$) {
                org.codehaus.jackson.mrbean.AbstractTypeMaterializer.Feature f = arr$[i$];
                if (f.enabledByDefault()) {
                    flags |= f.getMask();
                }
                i$++;
            }
            return flags;
        }

        public boolean enabledByDefault() {
            return this._defaultState;
        }

        public int getMask() {
            return 1 << ordinal();
        }
    }

    private static class MyClassLoader extends ClassLoader {
        public MyClassLoader(ClassLoader parent) {
            super(parent);
        }

        public Class<?> loadAndResolve(String className, byte[] byteCode, Class<?> targetClass) throws IllegalArgumentException {
            Class<?> old = findLoadedClass(className);
            if (old != null && targetClass.isAssignableFrom(old)) {
                return old;
            }
            try {
                Class<?> impl = defineClass(className, byteCode, DEFAULT_FEATURE_FLAGS, byteCode.length);
                resolveClass(impl);
                return impl;
            } catch (LinkageError e) {
                LinkageError e2 = e;
                throw new IllegalArgumentException("Failed to load class '" + className + "': " + e2.getMessage(), e2);
            }
        }
    }

    static {
        DEFAULT_FEATURE_FLAGS = Feature.collectDefaults();
    }

    public AbstractTypeMaterializer() {
        this(null);
    }

    public AbstractTypeMaterializer(ClassLoader parentClassLoader) {
        this._featureFlags = DEFAULT_FEATURE_FLAGS;
        this._defaultPackage = DEFAULT_PACKAGE_FOR_GENERATED;
        if (parentClassLoader == null) {
            parentClassLoader = getClass().getClassLoader();
        }
        this._classLoader = new MyClassLoader(parentClassLoader);
    }

    public void disable(Feature f) {
        this._featureFlags &= f.getMask() ^ -1;
    }

    public void enable(Feature f) {
        this._featureFlags |= f.getMask();
    }

    public final boolean isEnabled(Feature f) {
        return (this._featureFlags & f.getMask()) != 0;
    }

    protected Class<?> materializeClass(DeserializationConfig config, Class<?> cls) {
        String newName = this._defaultPackage + cls.getName();
        return this._classLoader.loadAndResolve(newName, new BeanBuilder(config, cls).implement(isEnabled(Feature.FAIL_ON_UNMATERIALIZED_METHOD)).build(newName), cls);
    }

    public JavaType resolveAbstractType(DeserializationConfig config, JavaType type) {
        return (type.isContainerType() || type.isPrimitive() || type.isEnumType() || type.isThrowable()) ? null : config.constructType(materializeClass(config, type.getRawClass()));
    }

    public void set(Feature f, boolean state) {
        if (state) {
            enable(f);
        } else {
            disable(f);
        }
    }

    public void setDefaultPackage(String defPkg) {
        if (!defPkg.endsWith(".")) {
            defPkg = defPkg + ".";
        }
        this._defaultPackage = defPkg;
    }

    public Version version() {
        return VersionUtil.versionFor(getClass());
    }
}