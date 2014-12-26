package org.codehaus.jackson.map.introspect;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JacksonAnnotation;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonClass;
import org.codehaus.jackson.annotate.JsonContentClass;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonGetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonIgnoreType;
import org.codehaus.jackson.annotate.JsonKeyClass;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.annotate.JsonRawValue;
import org.codehaus.jackson.annotate.JsonSetter;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeName;
import org.codehaus.jackson.annotate.JsonValue;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.AnnotationIntrospector.ReferenceProperty;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonDeserializer.None;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.annotate.JsonCachable;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;
import org.codehaus.jackson.map.annotate.JsonView;
import org.codehaus.jackson.map.annotate.NoClass;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.jsontype.impl.StdTypeResolverBuilder;
import org.codehaus.jackson.map.ser.impl.RawSerializer;
import org.codehaus.jackson.type.JavaType;

public class JacksonAnnotationIntrospector extends AnnotationIntrospector {
    protected StdTypeResolverBuilder _constructStdTypeResolverBuilder() {
        return new StdTypeResolverBuilder();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected org.codehaus.jackson.map.jsontype.TypeResolverBuilder<?> _findTypeResolver(org.codehaus.jackson.map.MapperConfig<?> r9_config, org.codehaus.jackson.map.introspect.Annotated r10_ann, org.codehaus.jackson.type.JavaType r11_baseType) {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector._findTypeResolver(org.codehaus.jackson.map.MapperConfig, org.codehaus.jackson.map.introspect.Annotated, org.codehaus.jackson.type.JavaType):org.codehaus.jackson.map.jsontype.TypeResolverBuilder<?>");
        /*
        r8 = this;
        r5 = 0;
        r6 = org.codehaus.jackson.annotate.JsonTypeInfo.class;
        r3 = r10.getAnnotation(r6);
        r3 = (org.codehaus.jackson.annotate.JsonTypeInfo) r3;
        r6 = org.codehaus.jackson.map.annotate.JsonTypeResolver.class;
        r4 = r10.getAnnotation(r6);
        r4 = (org.codehaus.jackson.map.annotate.JsonTypeResolver) r4;
        if (r4 == 0) goto L_0x0048;
    L_0x0013:
        if (r3 != 0) goto L_0x0017;
    L_0x0015:
        r0 = r5;
    L_0x0016:
        return r0;
    L_0x0017:
        r6 = r4.value();
        r0 = r9.typeResolverBuilderInstance(r10, r6);
    L_0x001f:
        r6 = org.codehaus.jackson.map.annotate.JsonTypeIdResolver.class;
        r2 = r10.getAnnotation(r6);
        r2 = (org.codehaus.jackson.map.annotate.JsonTypeIdResolver) r2;
        if (r2 != 0) goto L_0x0059;
    L_0x0029:
        r1 = r5;
    L_0x002a:
        if (r1 == 0) goto L_0x002f;
    L_0x002c:
        r1.init(r11);
    L_0x002f:
        r5 = r3.use();
        r0 = r0.init(r5, r1);
        r5 = r3.include();
        r0 = r0.inclusion(r5);
        r5 = r3.property();
        r0 = r0.typeProperty(r5);
        goto L_0x0016;
    L_0x0048:
        if (r3 == 0) goto L_0x0052;
    L_0x004a:
        r6 = r3.use();
        r7 = org.codehaus.jackson.annotate.JsonTypeInfo.Id.NONE;
        if (r6 != r7) goto L_0x0054;
    L_0x0052:
        r0 = r5;
        goto L_0x0016;
    L_0x0054:
        r0 = r8._constructStdTypeResolverBuilder();
        goto L_0x001f;
    L_0x0059:
        r5 = r2.value();
        r1 = r9.typeIdResolverInstance(r10, r5);
        goto L_0x002a;
        */
    }

    protected boolean _isIgnorable(Annotated a) {
        JsonIgnore ann = (JsonIgnore) a.getAnnotation(JsonIgnore.class);
        return ann != null && ann.value();
    }

    public VisibilityChecker<?> findAutoDetectVisibility(AnnotatedClass ac, VisibilityChecker<?> checker) {
        JsonAutoDetect ann = (JsonAutoDetect) ac.getAnnotation(JsonAutoDetect.class);
        return ann == null ? checker : checker.with(ann);
    }

    public Boolean findCachability(AnnotatedClass ac) {
        JsonCachable ann = (JsonCachable) ac.getAnnotation(JsonCachable.class);
        if (ann == null) {
            return null;
        }
        return ann.value() ? Boolean.TRUE : Boolean.FALSE;
    }

    public Class<? extends JsonDeserializer<?>> findContentDeserializer(Annotated a) {
        JsonDeserialize ann = (JsonDeserialize) a.getAnnotation(JsonDeserialize.class);
        if (ann != null) {
            Class<? extends JsonDeserializer<?>> deserClass = ann.contentUsing();
            if (deserClass != None.class) {
                return deserClass;
            }
        }
        return null;
    }

    public Class<? extends JsonSerializer<?>> findContentSerializer(Annotated a) {
        JsonSerialize ann = (JsonSerialize) a.getAnnotation(JsonSerialize.class);
        if (ann != null) {
            Class<? extends JsonSerializer<?>> serClass = ann.contentUsing();
            if (serClass != JsonSerializer.None.class) {
                return serClass;
            }
        }
        return null;
    }

    public String findDeserializablePropertyName(AnnotatedField af) {
        JsonProperty pann = (JsonProperty) af.getAnnotation(JsonProperty.class);
        if (pann != null) {
            return pann.value();
        }
        return (af.hasAnnotation(JsonDeserialize.class) || af.hasAnnotation(JsonView.class)) ? "" : null;
    }

    public Class<?> findDeserializationContentType(Annotated am, JavaType baseContentType, String propName) {
        Class<?> cls;
        JsonDeserialize ann = (JsonDeserialize) am.getAnnotation(JsonDeserialize.class);
        if (ann != null) {
            cls = ann.contentAs();
            if (cls != NoClass.class) {
                return cls;
            }
        }
        JsonContentClass oldAnn = (JsonContentClass) am.getAnnotation(JsonContentClass.class);
        if (oldAnn != null) {
            cls = oldAnn.value();
            if (cls != NoClass.class) {
                return cls;
            }
        }
        return null;
    }

    public Class<?> findDeserializationKeyType(Annotated am, JavaType baseKeyType, String propName) {
        Class<?> cls;
        JsonDeserialize ann = (JsonDeserialize) am.getAnnotation(JsonDeserialize.class);
        if (ann != null) {
            cls = ann.keyAs();
            if (cls != NoClass.class) {
                return cls;
            }
        }
        JsonKeyClass oldAnn = (JsonKeyClass) am.getAnnotation(JsonKeyClass.class);
        if (oldAnn != null) {
            cls = oldAnn.value();
            if (cls != NoClass.class) {
                return cls;
            }
        }
        return null;
    }

    public Class<?> findDeserializationType(Annotated am, JavaType baseType, String propName) {
        Class<?> cls;
        JsonDeserialize ann = (JsonDeserialize) am.getAnnotation(JsonDeserialize.class);
        if (ann != null) {
            cls = ann.as();
            if (cls != NoClass.class) {
                return cls;
            }
        }
        JsonClass oldAnn = (JsonClass) am.getAnnotation(JsonClass.class);
        if (oldAnn != null) {
            cls = oldAnn.value();
            if (cls != NoClass.class) {
                return cls;
            }
        }
        return null;
    }

    public Class<? extends JsonDeserializer<?>> findDeserializer(Annotated a, BeanProperty property) {
        JsonDeserialize ann = (JsonDeserialize) a.getAnnotation(JsonDeserialize.class);
        if (ann != null) {
            Class<? extends JsonDeserializer<?>> deserClass = ann.using();
            if (deserClass != None.class) {
                return deserClass;
            }
        }
        return null;
    }

    public String findEnumValue(Enum<?> value) {
        return value.name();
    }

    public Object findFilterId(AnnotatedClass ac) {
        JsonFilter ann = (JsonFilter) ac.getAnnotation(JsonFilter.class);
        if (ann != null) {
            String id = ann.value();
            if (id.length() > 0) {
                return id;
            }
        }
        return null;
    }

    public String findGettablePropertyName(AnnotatedMethod am) {
        JsonProperty pann = (JsonProperty) am.getAnnotation(JsonProperty.class);
        if (pann != null) {
            return pann.value();
        }
        JsonGetter ann = (JsonGetter) am.getAnnotation(JsonGetter.class);
        if (ann != null) {
            return ann.value();
        }
        return (am.hasAnnotation(JsonSerialize.class) || am.hasAnnotation(JsonView.class)) ? "" : null;
    }

    public Boolean findIgnoreUnknownProperties(AnnotatedClass ac) {
        JsonIgnoreProperties ignore = (JsonIgnoreProperties) ac.getAnnotation(JsonIgnoreProperties.class);
        return ignore == null ? null : Boolean.valueOf(ignore.ignoreUnknown());
    }

    public Class<? extends KeyDeserializer> findKeyDeserializer(Annotated a) {
        JsonDeserialize ann = (JsonDeserialize) a.getAnnotation(JsonDeserialize.class);
        if (ann != null) {
            Class<? extends KeyDeserializer> deserClass = ann.keyUsing();
            if (deserClass != KeyDeserializer.None.class) {
                return deserClass;
            }
        }
        return null;
    }

    public Class<? extends JsonSerializer<?>> findKeySerializer(Annotated a) {
        JsonSerialize ann = (JsonSerialize) a.getAnnotation(JsonSerialize.class);
        if (ann != null) {
            Class<? extends JsonSerializer<?>> serClass = ann.keyUsing();
            if (serClass != JsonSerializer.None.class) {
                return serClass;
            }
        }
        return null;
    }

    public String[] findPropertiesToIgnore(AnnotatedClass ac) {
        JsonIgnoreProperties ignore = (JsonIgnoreProperties) ac.getAnnotation(JsonIgnoreProperties.class);
        return ignore == null ? null : ignore.value();
    }

    public TypeResolverBuilder<?> findPropertyContentTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType containerType) {
        if (containerType.isContainerType()) {
            return _findTypeResolver(config, am, containerType);
        }
        throw new IllegalArgumentException("Must call method with a container type (got " + containerType + ")");
    }

    public String findPropertyNameForParam(AnnotatedParameter param) {
        if (param != null) {
            JsonProperty pann = (JsonProperty) param.getAnnotation(JsonProperty.class);
            if (pann != null) {
                return pann.value();
            }
        }
        return null;
    }

    public TypeResolverBuilder<?> findPropertyTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType baseType) {
        return baseType.isContainerType() ? null : _findTypeResolver(config, am, baseType);
    }

    public ReferenceProperty findReferenceType(AnnotatedMember member) {
        JsonManagedReference ref1 = (JsonManagedReference) member.getAnnotation(JsonManagedReference.class);
        if (ref1 != null) {
            return ReferenceProperty.managed(ref1.value());
        }
        JsonBackReference ref2 = (JsonBackReference) member.getAnnotation(JsonBackReference.class);
        return ref2 != null ? ReferenceProperty.back(ref2.value()) : null;
    }

    public String findRootName(AnnotatedClass ac) {
        return null;
    }

    public String findSerializablePropertyName(AnnotatedField af) {
        JsonProperty pann = (JsonProperty) af.getAnnotation(JsonProperty.class);
        if (pann != null) {
            return pann.value();
        }
        return (af.hasAnnotation(JsonSerialize.class) || af.hasAnnotation(JsonView.class)) ? "" : null;
    }

    public Class<?> findSerializationContentType(Annotated am, JavaType baseType) {
        JsonSerialize ann = (JsonSerialize) am.getAnnotation(JsonSerialize.class);
        if (ann != null) {
            Class<?> cls = ann.contentAs();
            if (cls != NoClass.class) {
                return cls;
            }
        }
        return null;
    }

    public Inclusion findSerializationInclusion(Annotated a, Inclusion defValue) {
        JsonSerialize ann = (JsonSerialize) a.getAnnotation(JsonSerialize.class);
        if (ann != null) {
            return ann.include();
        }
        JsonWriteNullProperties oldAnn = (JsonWriteNullProperties) a.getAnnotation(JsonWriteNullProperties.class);
        if (oldAnn != null) {
            return oldAnn.value() ? Inclusion.ALWAYS : Inclusion.NON_NULL;
        } else {
            return defValue;
        }
    }

    public Class<?> findSerializationKeyType(Annotated am, JavaType baseType) {
        JsonSerialize ann = (JsonSerialize) am.getAnnotation(JsonSerialize.class);
        if (ann != null) {
            Class<?> cls = ann.keyAs();
            if (cls != NoClass.class) {
                return cls;
            }
        }
        return null;
    }

    public String[] findSerializationPropertyOrder(AnnotatedClass ac) {
        JsonPropertyOrder order = (JsonPropertyOrder) ac.getAnnotation(JsonPropertyOrder.class);
        return order == null ? null : order.value();
    }

    public Boolean findSerializationSortAlphabetically(AnnotatedClass ac) {
        JsonPropertyOrder order = (JsonPropertyOrder) ac.getAnnotation(JsonPropertyOrder.class);
        return order == null ? null : Boolean.valueOf(order.alphabetic());
    }

    public Class<?> findSerializationType(Annotated am) {
        JsonSerialize ann = (JsonSerialize) am.getAnnotation(JsonSerialize.class);
        if (ann != null) {
            Class<?> cls = ann.as();
            if (cls != NoClass.class) {
                return cls;
            }
        }
        return null;
    }

    public Typing findSerializationTyping(Annotated a) {
        JsonSerialize ann = (JsonSerialize) a.getAnnotation(JsonSerialize.class);
        return ann == null ? null : ann.typing();
    }

    public Class<?>[] findSerializationViews(Annotated a) {
        JsonView ann = (JsonView) a.getAnnotation(JsonView.class);
        return ann == null ? null : ann.value();
    }

    public Object findSerializer(Annotated a, BeanProperty property) {
        JsonSerialize ann = (JsonSerialize) a.getAnnotation(JsonSerialize.class);
        if (ann != null) {
            Class<? extends JsonSerializer<?>> serClass = ann.using();
            if (serClass != JsonSerializer.None.class) {
                return serClass;
            }
        }
        JsonRawValue annRaw = (JsonRawValue) a.getAnnotation(JsonRawValue.class);
        return (annRaw == null || !annRaw.value()) ? null : new RawSerializer(a.getRawType());
    }

    public String findSettablePropertyName(AnnotatedMethod am) {
        JsonProperty pann = (JsonProperty) am.getAnnotation(JsonProperty.class);
        if (pann != null) {
            return pann.value();
        }
        JsonSetter ann = (JsonSetter) am.getAnnotation(JsonSetter.class);
        if (ann != null) {
            return ann.value();
        }
        return (am.hasAnnotation(JsonDeserialize.class) || am.hasAnnotation(JsonView.class)) ? "" : null;
    }

    public List<NamedType> findSubtypes(Annotated a) {
        JsonSubTypes t = (JsonSubTypes) a.getAnnotation(JsonSubTypes.class);
        if (t == null) {
            return null;
        }
        Type[] types = t.value();
        List<NamedType> result = new ArrayList(types.length);
        Type[] arr$ = types;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            Type type = arr$[i$];
            result.add(new NamedType(type.value(), type.name()));
            i$++;
        }
        return result;
    }

    public String findTypeName(AnnotatedClass ac) {
        JsonTypeName tn = (JsonTypeName) ac.getAnnotation(JsonTypeName.class);
        return tn == null ? null : tn.value();
    }

    public TypeResolverBuilder<?> findTypeResolver(MapperConfig<?> config, AnnotatedClass ac, JavaType baseType) {
        return _findTypeResolver(config, ac, baseType);
    }

    public boolean hasAnyGetterAnnotation(AnnotatedMethod am) {
        return am.hasAnnotation(JsonAnyGetter.class);
    }

    public boolean hasAnySetterAnnotation(AnnotatedMethod am) {
        return am.hasAnnotation(JsonAnySetter.class);
    }

    public boolean hasAsValueAnnotation(AnnotatedMethod am) {
        JsonValue ann = (JsonValue) am.getAnnotation(JsonValue.class);
        return ann != null && ann.value();
    }

    public boolean hasCreatorAnnotation(Annotated a) {
        return a.hasAnnotation(JsonCreator.class);
    }

    public boolean isHandled(Annotation ann) {
        return ann.annotationType().getAnnotation(JacksonAnnotation.class) != null;
    }

    public boolean isIgnorableConstructor(AnnotatedConstructor c) {
        return _isIgnorable(c);
    }

    public boolean isIgnorableField(AnnotatedField f) {
        return _isIgnorable(f);
    }

    public boolean isIgnorableMethod(AnnotatedMethod m) {
        return _isIgnorable(m);
    }

    public Boolean isIgnorableType(AnnotatedClass ac) {
        JsonIgnoreType ignore = (JsonIgnoreType) ac.getAnnotation(JsonIgnoreType.class);
        return ignore == null ? null : Boolean.valueOf(ignore.value());
    }
}