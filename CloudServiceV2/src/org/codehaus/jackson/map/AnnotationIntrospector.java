package org.codehaus.jackson.map;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.codehaus.jackson.map.JsonDeserializer.None;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedConstructor;
import org.codehaus.jackson.map.introspect.AnnotatedField;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.AnnotatedParameter;
import org.codehaus.jackson.map.introspect.NopAnnotationIntrospector;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.type.JavaType;

public abstract class AnnotationIntrospector {

    public static class ReferenceProperty {
        private final String _name;
        private final Type _type;

        public enum Type {
            MANAGED_REFERENCE,
            BACK_REFERENCE
        }

        public ReferenceProperty(Type t, String n) {
            this._type = t;
            this._name = n;
        }

        public static org.codehaus.jackson.map.AnnotationIntrospector.ReferenceProperty back(String name) {
            return new org.codehaus.jackson.map.AnnotationIntrospector.ReferenceProperty(Type.BACK_REFERENCE, name);
        }

        public static org.codehaus.jackson.map.AnnotationIntrospector.ReferenceProperty managed(String name) {
            return new org.codehaus.jackson.map.AnnotationIntrospector.ReferenceProperty(Type.MANAGED_REFERENCE, name);
        }

        public String getName() {
            return this._name;
        }

        public Type getType() {
            return this._type;
        }

        public boolean isBackReference() {
            return this._type == Type.BACK_REFERENCE;
        }

        public boolean isManagedReference() {
            return this._type == Type.MANAGED_REFERENCE;
        }
    }

    public static class Pair extends AnnotationIntrospector {
        protected final AnnotationIntrospector _primary;
        protected final AnnotationIntrospector _secondary;

        public Pair(AnnotationIntrospector p, AnnotationIntrospector s) {
            this._primary = p;
            this._secondary = s;
        }

        public static AnnotationIntrospector create(AnnotationIntrospector primary, AnnotationIntrospector secondary) {
            if (primary == null) {
                return secondary;
            }
            return secondary == null ? primary : new org.codehaus.jackson.map.AnnotationIntrospector.Pair(primary, secondary);
        }

        public Collection<AnnotationIntrospector> allIntrospectors() {
            return allIntrospectors(new ArrayList());
        }

        public Collection<AnnotationIntrospector> allIntrospectors(Collection<AnnotationIntrospector> result) {
            this._primary.allIntrospectors(result);
            this._secondary.allIntrospectors(result);
            return result;
        }

        public VisibilityChecker<?> findAutoDetectVisibility(AnnotatedClass ac, VisibilityChecker<?> checker) {
            return this._primary.findAutoDetectVisibility(ac, this._secondary.findAutoDetectVisibility(ac, checker));
        }

        public Boolean findCachability(AnnotatedClass ac) {
            Boolean result = this._primary.findCachability(ac);
            return result == null ? this._secondary.findCachability(ac) : result;
        }

        public Class<? extends JsonDeserializer<?>> findContentDeserializer(Annotated am) {
            Class<? extends JsonDeserializer<?>> result = this._primary.findContentDeserializer(am);
            return (result == null || result == None.class) ? this._secondary.findContentDeserializer(am) : result;
        }

        public Class<? extends JsonSerializer<?>> findContentSerializer(Annotated a) {
            Class<? extends JsonSerializer<?>> result = this._primary.findContentSerializer(a);
            return (result == null || result == JsonSerializer.None.class) ? this._secondary.findContentSerializer(a) : result;
        }

        public String findDeserializablePropertyName(AnnotatedField af) {
            String result = this._primary.findDeserializablePropertyName(af);
            if (result == null) {
                return this._secondary.findDeserializablePropertyName(af);
            }
            if (result.length() != 0) {
                return result;
            }
            String str2 = this._secondary.findDeserializablePropertyName(af);
            return str2 != null ? str2 : result;
        }

        public Class<?> findDeserializationContentType(Annotated am, JavaType baseContentType, String propName) {
            Class<?> result = this._primary.findDeserializationContentType(am, baseContentType, propName);
            return result == null ? this._secondary.findDeserializationContentType(am, baseContentType, propName) : result;
        }

        public Class<?> findDeserializationKeyType(Annotated am, JavaType baseKeyType, String propName) {
            Class<?> result = this._primary.findDeserializationKeyType(am, baseKeyType, propName);
            return result == null ? this._secondary.findDeserializationKeyType(am, baseKeyType, propName) : result;
        }

        public Class<?> findDeserializationType(Annotated am, JavaType baseType, String propName) {
            Class<?> result = this._primary.findDeserializationType(am, baseType, propName);
            return result == null ? this._secondary.findDeserializationType(am, baseType, propName) : result;
        }

        public Object findDeserializer(Annotated am) {
            Object result = this._primary.findDeserializer(am);
            return result == null ? this._secondary.findDeserializer(am) : result;
        }

        public Object findDeserializer(Annotated am, BeanProperty property) {
            Object result = this._primary.findDeserializer(am, property);
            return result == null ? this._secondary.findDeserializer(am, property) : result;
        }

        public String findEnumValue(Enum<?> value) {
            String result = this._primary.findEnumValue(value);
            return result == null ? this._secondary.findEnumValue(value) : result;
        }

        public Object findFilterId(AnnotatedClass ac) {
            Object id = this._primary.findFilterId(ac);
            return id == null ? this._secondary.findFilterId(ac) : id;
        }

        public String findGettablePropertyName(AnnotatedMethod am) {
            String result = this._primary.findGettablePropertyName(am);
            if (result == null) {
                return this._secondary.findGettablePropertyName(am);
            }
            if (result.length() != 0) {
                return result;
            }
            String str2 = this._secondary.findGettablePropertyName(am);
            return str2 != null ? str2 : result;
        }

        public Boolean findIgnoreUnknownProperties(AnnotatedClass ac) {
            Boolean result = this._primary.findIgnoreUnknownProperties(ac);
            return result == null ? this._secondary.findIgnoreUnknownProperties(ac) : result;
        }

        public Class<? extends KeyDeserializer> findKeyDeserializer(Annotated am) {
            Class<? extends KeyDeserializer> result = this._primary.findKeyDeserializer(am);
            return (result == null || result == KeyDeserializer.None.class) ? this._secondary.findKeyDeserializer(am) : result;
        }

        public Class<? extends JsonSerializer<?>> findKeySerializer(Annotated a) {
            Class<? extends JsonSerializer<?>> result = this._primary.findKeySerializer(a);
            return (result == null || result == JsonSerializer.None.class) ? this._secondary.findKeySerializer(a) : result;
        }

        public String[] findPropertiesToIgnore(AnnotatedClass ac) {
            String[] result = this._primary.findPropertiesToIgnore(ac);
            return result == null ? this._secondary.findPropertiesToIgnore(ac) : result;
        }

        public TypeResolverBuilder<?> findPropertyContentTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType baseType) {
            TypeResolverBuilder<?> b = this._primary.findPropertyContentTypeResolver(config, am, baseType);
            return b == null ? this._secondary.findPropertyContentTypeResolver(config, am, baseType) : b;
        }

        public String findPropertyNameForParam(AnnotatedParameter param) {
            String result = this._primary.findPropertyNameForParam(param);
            return result == null ? this._secondary.findPropertyNameForParam(param) : result;
        }

        public TypeResolverBuilder<?> findPropertyTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType baseType) {
            TypeResolverBuilder<?> b = this._primary.findPropertyTypeResolver(config, am, baseType);
            return b == null ? this._secondary.findPropertyTypeResolver(config, am, baseType) : b;
        }

        public org.codehaus.jackson.map.AnnotationIntrospector.ReferenceProperty findReferenceType(AnnotatedMember member) {
            org.codehaus.jackson.map.AnnotationIntrospector.ReferenceProperty ref = this._primary.findReferenceType(member);
            return ref == null ? this._secondary.findReferenceType(member) : ref;
        }

        public String findRootName(AnnotatedClass ac) {
            String name1 = this._primary.findRootName(ac);
            if (name1 == null) {
                return this._secondary.findRootName(ac);
            }
            if (name1.length() > 0) {
                return name1;
            }
            String name2 = this._secondary.findRootName(ac);
            return name2 != null ? name2 : name1;
        }

        public String findSerializablePropertyName(AnnotatedField af) {
            String result = this._primary.findSerializablePropertyName(af);
            if (result == null) {
                return this._secondary.findSerializablePropertyName(af);
            }
            if (result.length() != 0) {
                return result;
            }
            String str2 = this._secondary.findSerializablePropertyName(af);
            return str2 != null ? str2 : result;
        }

        public Class<?> findSerializationContentType(Annotated am, JavaType baseType) {
            Class<?> result = this._primary.findSerializationContentType(am, baseType);
            return result == null ? this._secondary.findSerializationContentType(am, baseType) : result;
        }

        public Inclusion findSerializationInclusion(Annotated a, Inclusion defValue) {
            return this._primary.findSerializationInclusion(a, this._secondary.findSerializationInclusion(a, defValue));
        }

        public Class<?> findSerializationKeyType(Annotated am, JavaType baseType) {
            Class<?> result = this._primary.findSerializationKeyType(am, baseType);
            return result == null ? this._secondary.findSerializationKeyType(am, baseType) : result;
        }

        public String[] findSerializationPropertyOrder(AnnotatedClass ac) {
            String[] result = this._primary.findSerializationPropertyOrder(ac);
            return result == null ? this._secondary.findSerializationPropertyOrder(ac) : result;
        }

        public Boolean findSerializationSortAlphabetically(AnnotatedClass ac) {
            Boolean result = this._primary.findSerializationSortAlphabetically(ac);
            return result == null ? this._secondary.findSerializationSortAlphabetically(ac) : result;
        }

        public Class<?> findSerializationType(Annotated a) {
            Class<?> result = this._primary.findSerializationType(a);
            return result == null ? this._secondary.findSerializationType(a) : result;
        }

        public Typing findSerializationTyping(Annotated a) {
            Typing result = this._primary.findSerializationTyping(a);
            return result == null ? this._secondary.findSerializationTyping(a) : result;
        }

        public Class<?>[] findSerializationViews(Annotated a) {
            Class<?>[] result = this._primary.findSerializationViews(a);
            return result == null ? this._secondary.findSerializationViews(a) : result;
        }

        public Object findSerializer(Annotated am) {
            Object result = this._primary.findSerializer(am);
            return result == null ? this._secondary.findSerializer(am) : result;
        }

        public Object findSerializer(Annotated am, BeanProperty property) {
            Object result = this._primary.findSerializer(am, property);
            return result == null ? this._secondary.findSerializer(am, property) : result;
        }

        public String findSettablePropertyName(AnnotatedMethod am) {
            String result = this._primary.findSettablePropertyName(am);
            if (result == null) {
                return this._secondary.findSettablePropertyName(am);
            }
            if (result.length() != 0) {
                return result;
            }
            String str2 = this._secondary.findSettablePropertyName(am);
            return str2 != null ? str2 : result;
        }

        public List<NamedType> findSubtypes(Annotated a) {
            List<NamedType> types1 = this._primary.findSubtypes(a);
            List<NamedType> types2 = this._secondary.findSubtypes(a);
            if (types1 == null || types1.isEmpty()) {
                return types2;
            }
            if (types2 == null || types2.isEmpty()) {
                return types1;
            }
            List<NamedType> result = new ArrayList(types1.size() + types2.size());
            result.addAll(types1);
            result.addAll(types2);
            return result;
        }

        public String findTypeName(AnnotatedClass ac) {
            String name = this._primary.findTypeName(ac);
            return (name == null || name.length() == 0) ? this._secondary.findTypeName(ac) : name;
        }

        public TypeResolverBuilder<?> findTypeResolver(MapperConfig<?> config, AnnotatedClass ac, JavaType baseType) {
            TypeResolverBuilder<?> b = this._primary.findTypeResolver(config, ac, baseType);
            return b == null ? this._secondary.findTypeResolver(config, ac, baseType) : b;
        }

        public boolean hasAnyGetterAnnotation(AnnotatedMethod am) {
            return this._primary.hasAnyGetterAnnotation(am) || this._secondary.hasAnyGetterAnnotation(am);
        }

        public boolean hasAnySetterAnnotation(AnnotatedMethod am) {
            return this._primary.hasAnySetterAnnotation(am) || this._secondary.hasAnySetterAnnotation(am);
        }

        public boolean hasAsValueAnnotation(AnnotatedMethod am) {
            return this._primary.hasAsValueAnnotation(am) || this._secondary.hasAsValueAnnotation(am);
        }

        public boolean hasCreatorAnnotation(Annotated a) {
            return this._primary.hasCreatorAnnotation(a) || this._secondary.hasCreatorAnnotation(a);
        }

        public boolean isHandled(Annotation ann) {
            return this._primary.isHandled(ann) || this._secondary.isHandled(ann);
        }

        public boolean isIgnorableConstructor(AnnotatedConstructor c) {
            return this._primary.isIgnorableConstructor(c) || this._secondary.isIgnorableConstructor(c);
        }

        public boolean isIgnorableField(AnnotatedField f) {
            return this._primary.isIgnorableField(f) || this._secondary.isIgnorableField(f);
        }

        public boolean isIgnorableMethod(AnnotatedMethod m) {
            return this._primary.isIgnorableMethod(m) || this._secondary.isIgnorableMethod(m);
        }

        public Boolean isIgnorableType(AnnotatedClass ac) {
            Boolean result = this._primary.isIgnorableType(ac);
            return result == null ? this._secondary.isIgnorableType(ac) : result;
        }
    }

    public static AnnotationIntrospector nopInstance() {
        return NopAnnotationIntrospector.instance;
    }

    public static AnnotationIntrospector pair(AnnotationIntrospector a1, AnnotationIntrospector a2) {
        return new Pair(a1, a2);
    }

    public Collection<AnnotationIntrospector> allIntrospectors() {
        return Collections.singletonList(this);
    }

    public Collection<AnnotationIntrospector> allIntrospectors(Collection<AnnotationIntrospector> result) {
        result.add(this);
        return result;
    }

    public VisibilityChecker<?> findAutoDetectVisibility(AnnotatedClass ac, VisibilityChecker<?> checker) {
        return checker;
    }

    public abstract Boolean findCachability(AnnotatedClass annotatedClass);

    public abstract Class<? extends JsonDeserializer<?>> findContentDeserializer(Annotated annotated);

    public Class<? extends JsonSerializer<?>> findContentSerializer(Annotated am) {
        return null;
    }

    public abstract String findDeserializablePropertyName(AnnotatedField annotatedField);

    public abstract Class<?> findDeserializationContentType(Annotated annotated, JavaType javaType, String str);

    public abstract Class<?> findDeserializationKeyType(Annotated annotated, JavaType javaType, String str);

    public abstract Class<?> findDeserializationType(Annotated annotated, JavaType javaType, String str);

    public Object findDeserializer(Annotated am) {
        return findDeserializer(am, null);
    }

    @Deprecated
    public Object findDeserializer(Annotated am, BeanProperty property) {
        return property != null ? findDeserializer(am) : null;
    }

    public abstract String findEnumValue(Enum<?> enumR);

    public Object findFilterId(AnnotatedClass ac) {
        return null;
    }

    public abstract String findGettablePropertyName(AnnotatedMethod annotatedMethod);

    public abstract Boolean findIgnoreUnknownProperties(AnnotatedClass annotatedClass);

    public abstract Class<? extends KeyDeserializer> findKeyDeserializer(Annotated annotated);

    public Class<? extends JsonSerializer<?>> findKeySerializer(Annotated am) {
        return null;
    }

    public abstract String[] findPropertiesToIgnore(AnnotatedClass annotatedClass);

    public TypeResolverBuilder<?> findPropertyContentTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType containerType) {
        return null;
    }

    public abstract String findPropertyNameForParam(AnnotatedParameter annotatedParameter);

    public TypeResolverBuilder<?> findPropertyTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType baseType) {
        return null;
    }

    public ReferenceProperty findReferenceType(AnnotatedMember member) {
        return null;
    }

    public abstract String findRootName(AnnotatedClass annotatedClass);

    public abstract String findSerializablePropertyName(AnnotatedField annotatedField);

    public Class<?> findSerializationContentType(Annotated am, JavaType baseType) {
        return null;
    }

    public Inclusion findSerializationInclusion(Annotated a, Inclusion defValue) {
        return defValue;
    }

    public Class<?> findSerializationKeyType(Annotated am, JavaType baseType) {
        return null;
    }

    public abstract String[] findSerializationPropertyOrder(AnnotatedClass annotatedClass);

    public abstract Boolean findSerializationSortAlphabetically(AnnotatedClass annotatedClass);

    public abstract Class<?> findSerializationType(Annotated annotated);

    public abstract Typing findSerializationTyping(Annotated annotated);

    public abstract Class<?>[] findSerializationViews(Annotated annotated);

    public Object findSerializer(Annotated am) {
        return findSerializer(am, null);
    }

    @Deprecated
    public Object findSerializer(Annotated am, BeanProperty property) {
        return property != null ? findSerializer(am) : null;
    }

    public abstract String findSettablePropertyName(AnnotatedMethod annotatedMethod);

    public List<NamedType> findSubtypes(Annotated a) {
        return null;
    }

    public String findTypeName(AnnotatedClass ac) {
        return null;
    }

    public TypeResolverBuilder<?> findTypeResolver(MapperConfig<?> config, AnnotatedClass ac, JavaType baseType) {
        return null;
    }

    public boolean hasAnyGetterAnnotation(AnnotatedMethod am) {
        return false;
    }

    public boolean hasAnySetterAnnotation(AnnotatedMethod am) {
        return false;
    }

    public abstract boolean hasAsValueAnnotation(AnnotatedMethod annotatedMethod);

    public boolean hasCreatorAnnotation(Annotated a) {
        return false;
    }

    public abstract boolean isHandled(Annotation annotation);

    public abstract boolean isIgnorableConstructor(AnnotatedConstructor annotatedConstructor);

    public abstract boolean isIgnorableField(AnnotatedField annotatedField);

    public abstract boolean isIgnorableMethod(AnnotatedMethod annotatedMethod);

    public Boolean isIgnorableType(AnnotatedClass ac) {
        return null;
    }
}