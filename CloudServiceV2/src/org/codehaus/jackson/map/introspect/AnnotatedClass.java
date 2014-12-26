package org.codehaus.jackson.map.introspect;

import com.wmt.data.LocalAudioAll;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ClassIntrospector.MixInResolver;
import org.codehaus.jackson.map.util.Annotations;
import org.codehaus.jackson.map.util.ArrayBuilders;
import org.codehaus.jackson.map.util.ClassUtil;

public final class AnnotatedClass extends Annotated {
    private static final AnnotationMap[] NO_ANNOTATION_MAPS;
    protected final AnnotationIntrospector _annotationIntrospector;
    protected final Class<?> _class;
    protected AnnotationMap _classAnnotations;
    protected List<AnnotatedConstructor> _constructors;
    protected List<AnnotatedMethod> _creatorMethods;
    protected AnnotatedConstructor _defaultConstructor;
    protected List<AnnotatedField> _fields;
    protected List<AnnotatedField> _ignoredFields;
    protected List<AnnotatedMethod> _ignoredMethods;
    protected AnnotatedMethodMap _memberMethods;
    protected final MixInResolver _mixInResolver;
    protected final Class<?> _primaryMixIn;
    protected final Collection<Class<?>> _superTypes;

    static {
        NO_ANNOTATION_MAPS = new AnnotationMap[0];
    }

    private AnnotatedClass(Class<?> cls, List<Class<?>> superTypes, AnnotationIntrospector aintr, MixInResolver mir) {
        this._class = cls;
        this._superTypes = superTypes;
        this._annotationIntrospector = aintr;
        this._mixInResolver = mir;
        this._primaryMixIn = this._mixInResolver == null ? null : this._mixInResolver.findMixInClassFor(this._class);
    }

    private AnnotationMap _emptyAnnotationMap() {
        return new AnnotationMap();
    }

    private AnnotationMap[] _emptyAnnotationMaps(int count) {
        if (count == 0) {
            return NO_ANNOTATION_MAPS;
        }
        AnnotationMap[] maps = new AnnotationMap[count];
        int i = 0;
        while (i < count) {
            maps[i] = _emptyAnnotationMap();
            i++;
        }
        return maps;
    }

    private boolean _isIncludableField(Field f) {
        if (f.isSynthetic()) {
            return false;
        }
        int mods = f.getModifiers();
        return (Modifier.isStatic(mods) || Modifier.isTransient(mods)) ? false : true;
    }

    public static AnnotatedClass construct(Class<?> cls, AnnotationIntrospector aintr, MixInResolver mir) {
        AnnotatedClass ac = new AnnotatedClass(cls, ClassUtil.findSuperTypes(cls, null), aintr, mir);
        ac.resolveClassAnnotations();
        return ac;
    }

    public static AnnotatedClass constructWithoutSuperTypes(Class<?> cls, AnnotationIntrospector aintr, MixInResolver mir) {
        AnnotatedClass ac = new AnnotatedClass(cls, Collections.emptyList(), aintr, mir);
        ac.resolveClassAnnotations();
        return ac;
    }

    protected void _addClassMixIns(AnnotationMap annotations, Class<?> toMask) {
        if (this._mixInResolver != null) {
            _addClassMixIns(annotations, toMask, this._mixInResolver.findMixInClassFor(toMask));
        }
    }

    protected void _addClassMixIns(AnnotationMap annotations, Class<?> toMask, Class<?> mixin) {
        if (mixin != null) {
            Annotation a;
            Annotation[] arr$ = mixin.getDeclaredAnnotations();
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                a = arr$[i$];
                if (this._annotationIntrospector.isHandled(a)) {
                    annotations.addIfNotPresent(a);
                }
                i$++;
            }
            i$ = ClassUtil.findSuperTypes(mixin, toMask).iterator();
            while (i$.hasNext()) {
                arr$ = ((Class) i$.next()).getDeclaredAnnotations();
                len$ = arr$.length;
                int i$2 = 0;
                while (i$2 < len$) {
                    a = arr$[i$2];
                    if (this._annotationIntrospector.isHandled(a)) {
                        annotations.addIfNotPresent(a);
                    }
                    i$2++;
                }
            }
        }
    }

    protected void _addConstructorMixIns(Class<?> mixin) {
        MemberKey[] ctorKeys = null;
        int ctorCount = this._constructors == null ? 0 : this._constructors.size();
        Constructor[] arr$ = mixin.getDeclaredConstructors();
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            Constructor ctor = arr$[i$];
            switch (ctor.getParameterTypes().length) {
                case LocalAudioAll.SORT_BY_TITLE:
                    if (this._defaultConstructor != null) {
                        _addMixOvers(ctor, this._defaultConstructor, false);
                    }
                    break;
                default:
                    int i;
                    if (ctorKeys == null) {
                        ctorKeys = new MemberKey[ctorCount];
                        i = 0;
                        while (i < ctorCount) {
                            ctorKeys[i] = new MemberKey(((AnnotatedConstructor) this._constructors.get(i)).getAnnotated());
                            i++;
                        }
                    }
                    MemberKey key = new MemberKey(ctor);
                    i = 0;
                    while (i < ctorCount) {
                        if (key.equals(ctorKeys[i])) {
                            _addMixOvers(ctor, (AnnotatedConstructor) this._constructors.get(i), true);
                        } else {
                            i++;
                        }
                    }
                    break;
            }
            i$++;
        }
    }

    protected void _addFactoryMixIns(Class<?> mixin) {
        MemberKey[] methodKeys = null;
        int methodCount = this._creatorMethods.size();
        Method[] arr$ = mixin.getDeclaredMethods();
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            Method m = arr$[i$];
            if (Modifier.isStatic(m.getModifiers()) && m.getParameterTypes().length != 0) {
                int i;
                if (methodKeys == null) {
                    methodKeys = new MemberKey[methodCount];
                    i = 0;
                    while (i < methodCount) {
                        methodKeys[i] = new MemberKey(((AnnotatedMethod) this._creatorMethods.get(i)).getAnnotated());
                        i++;
                    }
                }
                MemberKey key = new MemberKey(m);
                i = 0;
                while (i < methodCount) {
                    if (key.equals(methodKeys[i])) {
                        _addMixOvers(m, (AnnotatedMethod) this._creatorMethods.get(i), true);
                        break;
                    } else {
                        i++;
                    }
                }
            }
            i$++;
        }
    }

    protected void _addFieldMixIns(Class<?> mixin, Map<String, AnnotatedField> fields) {
        Field[] arr$ = mixin.getDeclaredFields();
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            Field mixinField = arr$[i$];
            if (_isIncludableField(mixinField)) {
                AnnotatedField maskedField = (AnnotatedField) fields.get(mixinField.getName());
                if (maskedField != null) {
                    Annotation[] arr$2 = mixinField.getDeclaredAnnotations();
                    int len$2 = arr$2.length;
                    int i$2 = 0;
                    while (i$2 < len$2) {
                        Annotation a = arr$2[i$2];
                        if (this._annotationIntrospector.isHandled(a)) {
                            maskedField.addOrOverride(a);
                        }
                        i$2++;
                    }
                }
            }
            i$++;
        }
    }

    protected void _addFields(Map<String, AnnotatedField> fields, Class<?> c) {
        Class<?> parent = c.getSuperclass();
        if (parent != null) {
            _addFields(fields, parent);
            Field[] arr$ = c.getDeclaredFields();
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                Field f = arr$[i$];
                if (_isIncludableField(f)) {
                    fields.put(f.getName(), _constructField(f));
                }
                i$++;
            }
            if (this._mixInResolver != null) {
                Class<?> mixin = this._mixInResolver.findMixInClassFor(c);
                if (mixin != null) {
                    _addFieldMixIns(mixin, fields);
                }
            }
        }
    }

    protected void _addMemberMethods(Class<?> cls, MethodFilter methodFilter, AnnotatedMethodMap methods, Class<?> mixInCls, AnnotatedMethodMap mixIns) {
        if (mixInCls != null) {
            _addMethodMixIns(methodFilter, methods, mixInCls, mixIns);
        }
        if (cls != null) {
            Method[] arr$ = cls.getDeclaredMethods();
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                Method m = arr$[i$];
                if (_isIncludableMethod(m, methodFilter)) {
                    AnnotatedMethod old = methods.find(m);
                    if (old == null) {
                        AnnotatedMethod newM = _constructMethod(m);
                        methods.add(newM);
                        old = mixIns.remove(m);
                        if (old != null) {
                            _addMixOvers(old.getAnnotated(), newM, false);
                        }
                    } else {
                        _addMixUnders(m, old);
                        if (old.getDeclaringClass().isInterface() && !m.getDeclaringClass().isInterface()) {
                            methods.add(old.withMethod(m));
                        }
                    }
                }
                i$++;
            }
        }
    }

    protected void _addMethodMixIns(MethodFilter methodFilter, AnnotatedMethodMap methods, Class<?> mixInCls, AnnotatedMethodMap mixIns) {
        Method[] arr$ = mixInCls.getDeclaredMethods();
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            Method m = arr$[i$];
            if (_isIncludableMethod(m, methodFilter)) {
                AnnotatedMethod am = methods.find(m);
                if (am != null) {
                    _addMixUnders(m, am);
                } else {
                    mixIns.add(_constructMethod(m));
                }
            }
            i$++;
        }
    }

    protected void _addMixOvers(Constructor<?> mixin, AnnotatedConstructor target, boolean addParamAnnotations) {
        Annotation[] arr$ = mixin.getDeclaredAnnotations();
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            Annotation a = arr$[i$];
            if (this._annotationIntrospector.isHandled(a)) {
                target.addOrOverride(a);
            }
            i$++;
        }
        if (addParamAnnotations) {
            Annotation[][] pa = mixin.getParameterAnnotations();
            int i = 0;
            int len = pa.length;
            while (i < len) {
                arr$ = pa[i];
                len$ = arr$.length;
                i$ = 0;
                while (i$ < len$) {
                    target.addOrOverrideParam(i, arr$[i$]);
                    i$++;
                }
                i++;
            }
        }
    }

    protected void _addMixOvers(Method mixin, AnnotatedMethod target, boolean addParamAnnotations) {
        Annotation[] arr$ = mixin.getDeclaredAnnotations();
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            Annotation a = arr$[i$];
            if (this._annotationIntrospector.isHandled(a)) {
                target.addOrOverride(a);
            }
            i$++;
        }
        if (addParamAnnotations) {
            Annotation[][] pa = mixin.getParameterAnnotations();
            int i = 0;
            int len = pa.length;
            while (i < len) {
                arr$ = pa[i];
                len$ = arr$.length;
                i$ = 0;
                while (i$ < len$) {
                    target.addOrOverrideParam(i, arr$[i$]);
                    i$++;
                }
                i++;
            }
        }
    }

    protected void _addMixUnders(Method src, AnnotatedMethod target) {
        Annotation[] arr$ = src.getDeclaredAnnotations();
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            Annotation a = arr$[i$];
            if (this._annotationIntrospector.isHandled(a)) {
                target.addIfNotPresent(a);
            }
            i$++;
        }
    }

    protected AnnotationMap _collectRelevantAnnotations(Annotation[] anns) {
        AnnotationMap annMap = new AnnotationMap();
        if (anns != null) {
            Annotation[] arr$ = anns;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                Annotation a = arr$[i$];
                if (this._annotationIntrospector.isHandled(a)) {
                    annMap.add(a);
                }
                i$++;
            }
        }
        return annMap;
    }

    protected AnnotationMap[] _collectRelevantAnnotations(Annotation[][] anns) {
        int len = anns.length;
        AnnotationMap[] result = new AnnotationMap[len];
        int i = 0;
        while (i < len) {
            result[i] = _collectRelevantAnnotations(anns[i]);
            i++;
        }
        return result;
    }

    protected AnnotatedConstructor _constructConstructor(Constructor<?> ctor, boolean defaultCtor) {
        if (this._annotationIntrospector == null) {
            return new AnnotatedConstructor(ctor, _emptyAnnotationMap(), _emptyAnnotationMaps(ctor.getParameterTypes().length));
        }
        return new AnnotatedConstructor(ctor, _collectRelevantAnnotations(ctor.getDeclaredAnnotations()), defaultCtor ? null : _collectRelevantAnnotations(ctor.getParameterAnnotations()));
    }

    protected AnnotatedMethod _constructCreatorMethod(Method m) {
        return this._annotationIntrospector == null ? new AnnotatedMethod(m, _emptyAnnotationMap(), _emptyAnnotationMaps(m.getParameterTypes().length)) : new AnnotatedMethod(m, _collectRelevantAnnotations(m.getDeclaredAnnotations()), _collectRelevantAnnotations(m.getParameterAnnotations()));
    }

    protected AnnotatedField _constructField(Field f) {
        return this._annotationIntrospector == null ? new AnnotatedField(f, _emptyAnnotationMap()) : new AnnotatedField(f, _collectRelevantAnnotations(f.getDeclaredAnnotations()));
    }

    protected AnnotatedMethod _constructMethod(Method m) {
        return this._annotationIntrospector == null ? new AnnotatedMethod(m, _emptyAnnotationMap(), null) : new AnnotatedMethod(m, _collectRelevantAnnotations(m.getDeclaredAnnotations()), null);
    }

    protected boolean _isIncludableMethod(Method m, MethodFilter filter) {
        return ((filter != null && !filter.includeMethod(m)) || m.isSynthetic() || m.isBridge()) ? false : true;
    }

    public Iterable<AnnotatedField> fields() {
        return this._fields == null ? Collections.emptyList() : this._fields;
    }

    public AnnotatedMethod findMethod(String name, Class<?>[] paramTypes) {
        return this._memberMethods.find(name, paramTypes);
    }

    public Class<?> getAnnotated() {
        return this._class;
    }

    public <A extends Annotation> A getAnnotation(Class<A> acls) {
        return this._classAnnotations == null ? null : this._classAnnotations.get(acls);
    }

    public Annotations getAnnotations() {
        return this._classAnnotations;
    }

    public List<AnnotatedConstructor> getConstructors() {
        return this._constructors == null ? Collections.emptyList() : this._constructors;
    }

    public AnnotatedConstructor getDefaultConstructor() {
        return this._defaultConstructor;
    }

    public int getFieldCount() {
        return this._fields == null ? 0 : this._fields.size();
    }

    public Type getGenericType() {
        return this._class;
    }

    public int getMemberMethodCount() {
        return this._memberMethods.size();
    }

    public int getModifiers() {
        return this._class.getModifiers();
    }

    public String getName() {
        return this._class.getName();
    }

    public Class<?> getRawType() {
        return this._class;
    }

    public List<AnnotatedMethod> getStaticMethods() {
        return this._creatorMethods == null ? Collections.emptyList() : this._creatorMethods;
    }

    public boolean hasAnnotations() {
        return this._classAnnotations.size() > 0;
    }

    public Iterable<AnnotatedField> ignoredFields() {
        return this._ignoredFields == null ? Collections.emptyList() : this._ignoredFields;
    }

    public Iterable<AnnotatedMethod> ignoredMemberMethods() {
        return this._ignoredMethods == null ? Collections.emptyList() : this._ignoredMethods;
    }

    public Iterable<AnnotatedMethod> memberMethods() {
        return this._memberMethods;
    }

    protected void resolveClassAnnotations() {
        this._classAnnotations = new AnnotationMap();
        if (this._primaryMixIn != null) {
            _addClassMixIns(this._classAnnotations, this._class, this._primaryMixIn);
        }
        Annotation[] arr$ = this._class.getDeclaredAnnotations();
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            Annotation a = arr$[i$];
            if (this._annotationIntrospector.isHandled(a)) {
                this._classAnnotations.addIfNotPresent(a);
            }
            i$++;
        }
        i$ = this._superTypes.iterator();
        while (i$.hasNext()) {
            Class<?> cls = (Class) i$.next();
            _addClassMixIns(this._classAnnotations, cls);
            arr$ = cls.getDeclaredAnnotations();
            len$ = arr$.length;
            int i$2 = 0;
            while (i$2 < len$) {
                a = arr$[i$2];
                if (this._annotationIntrospector.isHandled(a)) {
                    this._classAnnotations.addIfNotPresent(a);
                }
                i$2++;
            }
        }
        _addClassMixIns(this._classAnnotations, Object.class);
    }

    public void resolveCreators(boolean includeAll) {
        int i;
        this._constructors = null;
        Constructor[] arr$ = this._class.getDeclaredConstructors();
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            Constructor<?> ctor = arr$[i$];
            switch (ctor.getParameterTypes().length) {
                case LocalAudioAll.SORT_BY_TITLE:
                    this._defaultConstructor = _constructConstructor(ctor, true);
                    break;
                default:
                    if (includeAll) {
                        if (this._constructors == null) {
                            this._constructors = new ArrayList();
                        }
                        this._constructors.add(_constructConstructor(ctor, false));
                    }
                    break;
            }
            i$++;
        }
        if (this._primaryMixIn != null) {
            if (!(this._defaultConstructor == null && this._constructors == null)) {
                _addConstructorMixIns(this._primaryMixIn);
            }
        }
        if (this._annotationIntrospector != null) {
            if (this._defaultConstructor != null && this._annotationIntrospector.isIgnorableConstructor(this._defaultConstructor)) {
                this._defaultConstructor = null;
            }
            if (this._constructors != null) {
                i = this._constructors.size();
                while (true) {
                    i--;
                    if (i >= 0) {
                        if (this._annotationIntrospector.isIgnorableConstructor((AnnotatedConstructor) this._constructors.get(i))) {
                            this._constructors.remove(i);
                        }
                    }
                }
            }
        }
        this._creatorMethods = null;
        if (includeAll) {
            Method[] arr$2 = this._class.getDeclaredMethods();
            len$ = arr$2.length;
            i$ = 0;
            while (i$ < len$) {
                Method m = arr$2[i$];
                if (Modifier.isStatic(m.getModifiers()) && m.getParameterTypes().length >= 1) {
                    if (this._creatorMethods == null) {
                        this._creatorMethods = new ArrayList();
                    }
                    this._creatorMethods.add(_constructCreatorMethod(m));
                }
                i$++;
            }
            if (!(this._primaryMixIn == null || this._creatorMethods == null)) {
                _addFactoryMixIns(this._primaryMixIn);
            }
            if (this._annotationIntrospector != null && this._creatorMethods != null) {
                i = this._creatorMethods.size();
                while (true) {
                    i--;
                    if (i < 0) {
                        return;
                    }
                    if (this._annotationIntrospector.isIgnorableMethod((AnnotatedMethod) this._creatorMethods.get(i))) {
                        this._creatorMethods.remove(i);
                    }
                }
            }
        }
    }

    public void resolveFields(boolean collectIgnored) {
        LinkedHashMap<String, AnnotatedField> foundFields = new LinkedHashMap();
        _addFields(foundFields, this._class);
        if (this._annotationIntrospector != null) {
            Iterator<Entry<String, AnnotatedField>> it = foundFields.entrySet().iterator();
            while (it.hasNext()) {
                AnnotatedField f = (AnnotatedField) ((Entry) it.next()).getValue();
                if (this._annotationIntrospector.isIgnorableField(f)) {
                    it.remove();
                    if (collectIgnored) {
                        this._ignoredFields = ArrayBuilders.addToList(this._ignoredFields, f);
                    }
                }
            }
        }
        if (foundFields.isEmpty()) {
            this._fields = Collections.emptyList();
        } else {
            this._fields = new ArrayList(foundFields.size());
            this._fields.addAll(foundFields.values());
        }
    }

    public void resolveMemberMethods(MethodFilter methodFilter, boolean collectIgnored) {
        this._memberMethods = new AnnotatedMethodMap();
        AnnotatedMethodMap mixins = new AnnotatedMethodMap();
        _addMemberMethods(this._class, methodFilter, this._memberMethods, this._primaryMixIn, mixins);
        Iterator i$ = this._superTypes.iterator();
        while (i$.hasNext()) {
            Class<?> cls = (Class) i$.next();
            _addMemberMethods(cls, methodFilter, this._memberMethods, this._mixInResolver == null ? null : this._mixInResolver.findMixInClassFor(cls), mixins);
        }
        if (this._mixInResolver != null) {
            Class<?> mixin = this._mixInResolver.findMixInClassFor(Object.class);
            if (mixin != null) {
                _addMethodMixIns(methodFilter, this._memberMethods, mixin, mixins);
            }
        }
        if (this._annotationIntrospector != null) {
            Iterator<AnnotatedMethod> it;
            AnnotatedMethod am;
            if (!mixins.isEmpty()) {
                it = mixins.iterator();
                while (it.hasNext()) {
                    AnnotatedMethod mixIn = (AnnotatedMethod) it.next();
                    try {
                        Method m = Object.class.getDeclaredMethod(mixIn.getName(), mixIn.getParameterClasses());
                        if (m != null) {
                            am = _constructMethod(m);
                            _addMixOvers(mixIn.getAnnotated(), am, false);
                            this._memberMethods.add(am);
                        }
                    } catch (Exception e) {
                    }
                }
            }
            it = this._memberMethods.iterator();
            while (it.hasNext()) {
                am = it.next();
                if (this._annotationIntrospector.isIgnorableMethod(am)) {
                    it.remove();
                    if (collectIgnored) {
                        this._ignoredMethods = ArrayBuilders.addToList(this._ignoredMethods, am);
                    }
                }
            }
        }
    }

    public String toString() {
        return "[AnnotedClass " + this._class.getName() + "]";
    }
}