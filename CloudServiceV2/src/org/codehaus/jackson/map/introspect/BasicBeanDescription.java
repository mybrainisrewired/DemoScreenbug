package org.codehaus.jackson.map.introspect;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.AnnotationIntrospector.ReferenceProperty;
import org.codehaus.jackson.map.BeanDescription;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.PropertyNamingStrategy;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.map.util.Annotations;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.type.JavaType;

public class BasicBeanDescription extends BeanDescription {
    protected final AnnotationIntrospector _annotationIntrospector;
    protected TypeBindings _bindings;
    protected final AnnotatedClass _classInfo;
    protected final MapperConfig<?> _config;

    public BasicBeanDescription(MapperConfig<?> config, JavaType type, AnnotatedClass ac) {
        super(type);
        this._config = config;
        this._annotationIntrospector = config.getAnnotationIntrospector();
        this._classInfo = ac;
    }

    public static String descFor(AnnotatedElement elem) {
        if (elem instanceof Class) {
            return "class " + ((Class) elem).getName();
        }
        if (elem instanceof Method) {
            Method m = (Method) elem;
            return "method " + m.getName() + " (from class " + m.getDeclaringClass().getName() + ")";
        } else if (!(elem instanceof Constructor)) {
            return "unknown type [" + elem.getClass() + "]";
        } else {
            return "constructor() (from class " + ((Constructor) elem).getDeclaringClass().getName() + ")";
        }
    }

    public static String manglePropertyName(String basename) {
        int len = basename.length();
        if (len == 0) {
            return null;
        }
        StringBuilder sb = null;
        int i = 0;
        while (i < len) {
            char upper = basename.charAt(i);
            char lower = Character.toLowerCase(upper);
            if (upper == lower) {
                break;
            }
            if (sb == null) {
                sb = new StringBuilder(basename);
            }
            sb.setCharAt(i, lower);
            i++;
        }
        return sb != null ? sb.toString() : basename;
    }

    public LinkedHashMap<String, AnnotatedField> _findPropertyFields(VisibilityChecker<?> vchecker, Collection<String> ignoredProperties, boolean forSerialization) {
        LinkedHashMap<String, AnnotatedField> results = new LinkedHashMap();
        PropertyNamingStrategy naming = this._config.getPropertyNamingStrategy();
        Iterator i$ = this._classInfo.fields().iterator();
        while (i$.hasNext()) {
            String propName;
            AnnotatedField af = (AnnotatedField) i$.next();
            if (forSerialization) {
                propName = this._annotationIntrospector.findSerializablePropertyName(af);
            } else {
                propName = this._annotationIntrospector.findDeserializablePropertyName(af);
            }
            if (propName != null) {
                if (propName.length() == 0) {
                    propName = af.getName();
                    if (naming != null) {
                        propName = naming.nameForField(this._config, af, propName);
                    }
                }
            } else if (vchecker.isFieldVisible(af)) {
                propName = af.getName();
                if (naming != null) {
                    propName = naming.nameForField(this._config, af, propName);
                }
            }
            if (ignoredProperties == null || !ignoredProperties.contains(propName)) {
                AnnotatedField old = (AnnotatedField) results.put(propName, af);
                if (old != null && old.getDeclaringClass() == af.getDeclaringClass()) {
                    String oldDesc = old.getFullName();
                    throw new IllegalArgumentException("Multiple fields representing property \"" + propName + "\": " + oldDesc + " vs " + af.getFullName());
                }
            }
        }
        return results;
    }

    public TypeBindings bindingsForBeanType() {
        if (this._bindings == null) {
            this._bindings = new TypeBindings(this._config.getTypeFactory(), this._type);
        }
        return this._bindings;
    }

    public AnnotatedMethod findAnyGetter() throws IllegalArgumentException {
        AnnotatedMethod found = null;
        Iterator i$ = this._classInfo.memberMethods().iterator();
        while (i$.hasNext()) {
            AnnotatedMethod am = (AnnotatedMethod) i$.next();
            if (!this._annotationIntrospector.hasAnyGetterAnnotation(am) || found == null) {
                if (Map.class.isAssignableFrom(am.getRawType())) {
                    found = am;
                } else {
                    throw new IllegalArgumentException("Invalid 'any-getter' annotation on method " + am.getName() + "(): return type is not instance of java.util.Map");
                }
            } else {
                throw new IllegalArgumentException("Multiple methods with 'any-getter' annotation (" + found.getName() + "(), " + am.getName() + ")");
            }
        }
        return found;
    }

    public AnnotatedMethod findAnySetter() throws IllegalArgumentException {
        AnnotatedMethod found = null;
        Iterator i$ = this._classInfo.memberMethods().iterator();
        while (i$.hasNext()) {
            AnnotatedMethod am = (AnnotatedMethod) i$.next();
            if (!this._annotationIntrospector.hasAnySetterAnnotation(am) || found == null) {
                int pcount = am.getParameterCount();
                if (pcount != 2) {
                    throw new IllegalArgumentException("Invalid 'any-setter' annotation on method " + am.getName() + "(): takes " + pcount + " parameters, should take 2");
                }
                Class<?> type = am.getParameterClass(0);
                if (type == String.class || type == Object.class) {
                    found = am;
                } else {
                    throw new IllegalArgumentException("Invalid 'any-setter' annotation on method " + am.getName() + "(): first argument not of type String or Object, but " + type.getName());
                }
            } else {
                throw new IllegalArgumentException("Multiple methods with 'any-setter' annotation (" + found.getName() + "(), " + am.getName() + ")");
            }
        }
        return found;
    }

    public Map<String, AnnotatedMember> findBackReferenceProperties() {
        HashMap<String, AnnotatedMember> result = null;
        Iterator i$ = this._classInfo.memberMethods().iterator();
        while (i$.hasNext()) {
            ReferenceProperty prop;
            AnnotatedMethod am = (AnnotatedMethod) i$.next();
            if (am.getParameterCount() == 1) {
                prop = this._annotationIntrospector.findReferenceType(am);
                if (prop != null && prop.isBackReference()) {
                    if (result == null) {
                        result = new HashMap();
                    }
                    if (result.put(prop.getName(), am) != null) {
                        throw new IllegalArgumentException("Multiple back-reference properties with name '" + prop.getName() + "'");
                    }
                }
            }
        }
        i$ = this._classInfo.fields().iterator();
        while (i$.hasNext()) {
            AnnotatedField af = (AnnotatedField) i$.next();
            prop = this._annotationIntrospector.findReferenceType(af);
            if (prop != null && prop.isBackReference()) {
                if (result == null) {
                    result = new HashMap();
                }
                if (result.put(prop.getName(), af) != null) {
                    throw new IllegalArgumentException("Multiple back-reference properties with name '" + prop.getName() + "'");
                }
            }
        }
        return result;
    }

    public List<String> findCreatorPropertyNames() {
        List<String> names = null;
        int i = 0;
        while (i < 2) {
            Iterator i$ = (i == 0 ? getConstructors() : getFactoryMethods()).iterator();
            while (i$.hasNext()) {
                AnnotatedWithParams creator = (AnnotatedWithParams) i$.next();
                int argCount = creator.getParameterCount();
                if (argCount >= 1) {
                    String name = this._annotationIntrospector.findPropertyNameForParam(creator.getParameter(0));
                    if (name != null) {
                        if (names == null) {
                            names = new ArrayList();
                        }
                        names.add(name);
                        int p = 1;
                        while (p < argCount) {
                            names.add(this._annotationIntrospector.findPropertyNameForParam(creator.getParameter(p)));
                            p++;
                        }
                    }
                }
            }
            i++;
        }
        return names == null ? Collections.emptyList() : names;
    }

    public Constructor<?> findDefaultConstructor() {
        AnnotatedConstructor ac = this._classInfo.getDefaultConstructor();
        return ac == null ? null : ac.getAnnotated();
    }

    public LinkedHashMap<String, AnnotatedField> findDeserializableFields(VisibilityChecker<?> vchecker, Collection<String> ignoredProperties) {
        return _findPropertyFields(vchecker, ignoredProperties, false);
    }

    public Method findFactoryMethod(Class<?>... expArgTypes) {
        Iterator it = this._classInfo.getStaticMethods().iterator();
        while (it.hasNext()) {
            AnnotatedMethod am = (AnnotatedMethod) it.next();
            if (isFactoryMethod(am)) {
                Class<?> actualArgType = am.getParameterClass(0);
                Class<?>[] arr$ = expArgTypes;
                int len$ = arr$.length;
                int i$ = 0;
                while (i$ < len$) {
                    if (actualArgType.isAssignableFrom(arr$[i$])) {
                        return am.getAnnotated();
                    }
                    i$++;
                }
            }
        }
        return null;
    }

    public LinkedHashMap<String, AnnotatedMethod> findGetters(VisibilityChecker<?> visibilityChecker, Collection<String> ignoredProperties) {
        LinkedHashMap<String, AnnotatedMethod> results = new LinkedHashMap();
        PropertyNamingStrategy naming = this._config.getPropertyNamingStrategy();
        Iterator i$ = this._classInfo.memberMethods().iterator();
        while (i$.hasNext()) {
            AnnotatedMethod am = (AnnotatedMethod) i$.next();
            if (am.getParameterCount() == 0) {
                String propName = this._annotationIntrospector.findGettablePropertyName(am);
                if (propName == null) {
                    propName = am.getName();
                    if (propName.startsWith("get")) {
                        if (visibilityChecker.isGetterVisible(am)) {
                            propName = okNameForGetter(am, propName);
                        }
                    } else if (visibilityChecker.isIsGetterVisible(am)) {
                        propName = okNameForIsGetter(am, propName);
                    }
                    if (!(propName == null || this._annotationIntrospector.hasAnyGetterAnnotation(am))) {
                        if (naming != null) {
                            propName = naming.nameForGetterMethod(this._config, am, propName);
                        }
                    }
                } else if (propName.length() == 0) {
                    propName = okNameForAnyGetter(am, am.getName());
                    if (propName == null) {
                        propName = am.getName();
                    }
                    if (naming != null) {
                        propName = naming.nameForGetterMethod(this._config, am, propName);
                    }
                }
                if (ignoredProperties == null || !ignoredProperties.contains(propName)) {
                    AnnotatedMethod old = (AnnotatedMethod) results.put(propName, am);
                    if (old != null) {
                        String oldDesc = old.getFullName();
                        throw new IllegalArgumentException("Conflicting getter definitions for property \"" + propName + "\": " + oldDesc + " vs " + am.getFullName());
                    }
                }
            }
        }
        return results;
    }

    public AnnotatedMethod findJsonValueMethod() {
        AnnotatedMethod found = null;
        Iterator i$ = this._classInfo.memberMethods().iterator();
        while (i$.hasNext()) {
            AnnotatedMethod am = (AnnotatedMethod) i$.next();
            if (this._annotationIntrospector.hasAsValueAnnotation(am) && found != null) {
                throw new IllegalArgumentException("Multiple methods with active 'as-value' annotation (" + found.getName() + "(), " + am.getName() + ")");
            } else if (ClassUtil.hasGetterSignature(am.getAnnotated())) {
                found = am;
            } else {
                throw new IllegalArgumentException("Method " + am.getName() + "() marked with an 'as-value' annotation, but does not have valid getter signature (non-static, takes no args, returns a value)");
            }
        }
        return found;
    }

    public AnnotatedMethod findMethod(String name, Class<?>[] paramTypes) {
        return this._classInfo.findMethod(name, paramTypes);
    }

    public LinkedHashMap<String, AnnotatedField> findSerializableFields(VisibilityChecker<?> vchecker, Collection<String> ignoredProperties) {
        return _findPropertyFields(vchecker, ignoredProperties, true);
    }

    public Inclusion findSerializationInclusion(Inclusion defValue) {
        return this._annotationIntrospector.findSerializationInclusion(this._classInfo, defValue);
    }

    public LinkedHashMap<String, AnnotatedMethod> findSetters(VisibilityChecker<?> vchecker) {
        LinkedHashMap<String, AnnotatedMethod> results = new LinkedHashMap();
        PropertyNamingStrategy naming = this._config.getPropertyNamingStrategy();
        Iterator i$ = this._classInfo.memberMethods().iterator();
        while (i$.hasNext()) {
            AnnotatedMethod am = (AnnotatedMethod) i$.next();
            if (am.getParameterCount() == 1) {
                String propName = this._annotationIntrospector.findSettablePropertyName(am);
                if (propName != null) {
                    if (propName.length() == 0) {
                        propName = okNameForSetter(am);
                        if (propName == null) {
                            propName = am.getName();
                        }
                        if (naming != null) {
                            propName = naming.nameForSetterMethod(this._config, am, propName);
                        }
                    }
                } else if (vchecker.isSetterVisible(am)) {
                    propName = okNameForSetter(am);
                    if (propName != null) {
                        if (naming != null) {
                            propName = naming.nameForSetterMethod(this._config, am, propName);
                        }
                    }
                }
                AnnotatedMethod old = (AnnotatedMethod) results.put(propName, am);
                if (old == null || old.getDeclaringClass() != am.getDeclaringClass()) {
                    results.put(propName, old);
                } else {
                    String oldDesc = old.getFullName();
                    throw new IllegalArgumentException("Conflicting setter definitions for property \"" + propName + "\": " + oldDesc + " vs " + am.getFullName());
                }
            }
        }
        return results;
    }

    public Constructor<?> findSingleArgConstructor(Class<?>... argTypes) {
        Iterator it = this._classInfo.getConstructors().iterator();
        while (it.hasNext()) {
            AnnotatedConstructor ac = (AnnotatedConstructor) it.next();
            if (ac.getParameterCount() == 1) {
                Class<?> actArg = ac.getParameterClass(0);
                Class<?>[] arr$ = argTypes;
                int len$ = arr$.length;
                int i$ = 0;
                while (i$ < len$) {
                    if (arr$[i$] == actArg) {
                        return ac.getAnnotated();
                    }
                    i$++;
                }
            }
        }
        return null;
    }

    public Annotations getClassAnnotations() {
        return this._classInfo.getAnnotations();
    }

    public AnnotatedClass getClassInfo() {
        return this._classInfo;
    }

    public List<AnnotatedConstructor> getConstructors() {
        return this._classInfo.getConstructors();
    }

    public List<AnnotatedMethod> getFactoryMethods() {
        List<AnnotatedMethod> candidates = this._classInfo.getStaticMethods();
        if (candidates.isEmpty()) {
            return candidates;
        }
        ArrayList<AnnotatedMethod> result = new ArrayList();
        Iterator i$ = candidates.iterator();
        while (i$.hasNext()) {
            AnnotatedMethod am = (AnnotatedMethod) i$.next();
            if (isFactoryMethod(am)) {
                result.add(am);
            }
        }
        return result;
    }

    public boolean hasKnownClassAnnotations() {
        return this._classInfo.hasAnnotations();
    }

    public Object instantiateBean(boolean fixAccess) {
        AnnotatedConstructor ac = this._classInfo.getDefaultConstructor();
        if (ac == null) {
            return null;
        }
        if (fixAccess) {
            ac.fixAccess();
        }
        try {
            return ac.getAnnotated().newInstance(new Object[0]);
        } catch (Exception e) {
            Throwable t = e;
            while (t.getCause() != null) {
                t = t.getCause();
            }
            if (t instanceof Error) {
                throw ((Error) t);
            } else if (t instanceof RuntimeException) {
                throw ((RuntimeException) t);
            } else {
                throw new IllegalArgumentException("Failed to instantiate bean of type " + this._classInfo.getAnnotated().getName() + ": (" + t.getClass().getName() + ") " + t.getMessage(), t);
            }
        }
    }

    protected boolean isCglibGetCallbacks(AnnotatedMethod am) {
        Class<?> rt = am.getRawType();
        if (rt == null || !rt.isArray()) {
            return false;
        }
        Package pkg = rt.getComponentType().getPackage();
        if (pkg == null) {
            return false;
        }
        String pname = pkg.getName();
        return pname.startsWith("net.sf.cglib") || pname.startsWith("org.hibernate.repackage.cglib");
    }

    protected boolean isFactoryMethod(AnnotatedMethod am) {
        if (!getBeanClass().isAssignableFrom(am.getRawType())) {
            return false;
        }
        if (this._annotationIntrospector.hasCreatorAnnotation(am)) {
            return true;
        }
        return "valueOf".equals(am.getName());
    }

    protected boolean isGroovyMetaClassGetter(AnnotatedMethod am) {
        Class<?> rt = am.getRawType();
        if (rt == null || rt.isArray()) {
            return false;
        }
        Package pkg = rt.getPackage();
        return pkg != null && pkg.getName().startsWith("groovy.lang");
    }

    protected boolean isGroovyMetaClassSetter(AnnotatedMethod am) {
        Package pkg = am.getParameterClass(0).getPackage();
        return pkg != null && pkg.getName().startsWith("groovy.lang");
    }

    protected String mangleGetterName(Annotated a, String basename) {
        return manglePropertyName(basename);
    }

    protected String mangleSetterName(Annotated a, String basename) {
        return manglePropertyName(basename);
    }

    public String okNameForAnyGetter(AnnotatedMethod am, String name) {
        String str = okNameForIsGetter(am, name);
        return str == null ? okNameForGetter(am, name) : str;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String okNameForGetter(org.codehaus.jackson.map.introspect.AnnotatedMethod r3_am, java.lang.String r4_name) {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.introspect.BasicBeanDescription.okNameForGetter(org.codehaus.jackson.map.introspect.AnnotatedMethod, java.lang.String):java.lang.String");
        /*
        r2 = this;
        r0 = 0;
        r1 = "get";
        r1 = r4.startsWith(r1);
        if (r1 == 0) goto L_0x0017;
    L_0x0009:
        r1 = "getCallbacks";
        r1 = r1.equals(r4);
        if (r1 == 0) goto L_0x0018;
    L_0x0011:
        r1 = r2.isCglibGetCallbacks(r3);
        if (r1 == 0) goto L_0x0026;
    L_0x0017:
        return r0;
    L_0x0018:
        r1 = "getMetaClass";
        r1 = r1.equals(r4);
        if (r1 == 0) goto L_0x0026;
    L_0x0020:
        r1 = r2.isGroovyMetaClassGetter(r3);
        if (r1 != 0) goto L_0x0017;
    L_0x0026:
        r0 = 3;
        r0 = r4.substring(r0);
        r0 = r2.mangleGetterName(r3, r0);
        goto L_0x0017;
        */
    }

    public String okNameForIsGetter(AnnotatedMethod am, String name) {
        if (!name.startsWith("is")) {
            return null;
        }
        Class<?> rt = am.getRawType();
        return (rt == Boolean.class || rt == Boolean.TYPE) ? mangleGetterName(am, name.substring(ClassWriter.COMPUTE_FRAMES)) : null;
    }

    public String okNameForSetter(AnnotatedMethod am) {
        String name = am.getName();
        if (!name.startsWith("set")) {
            return null;
        }
        name = mangleSetterName(am, name.substring(JsonWriteContext.STATUS_OK_AFTER_SPACE));
        if (name == null) {
            return null;
        }
        return ("metaClass".equals(name) && isGroovyMetaClassSetter(am)) ? null : name;
    }
}