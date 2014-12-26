package org.codehaus.jackson.map.jsontype.impl;

import com.wmt.data.LocalAudioAll;
import java.util.Collection;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.TypeIdResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.type.JavaType;

public class StdTypeResolverBuilder implements TypeResolverBuilder<StdTypeResolverBuilder> {
    protected TypeIdResolver _customIdResolver;
    protected Id _idType;
    protected As _includeAs;
    protected String _typeProperty;

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$As;
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$Id;

        static {
            $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$Id = new int[Id.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$Id[Id.CLASS.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$Id[Id.MINIMAL_CLASS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$Id[Id.NAME.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$Id[Id.CUSTOM.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$Id[Id.NONE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$As = new int[As.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$As[As.WRAPPER_ARRAY.ordinal()] = 1;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$As[As.PROPERTY.ordinal()] = 2;
            } catch (NoSuchFieldError e7) {
            }
            $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$As[As.WRAPPER_OBJECT.ordinal()] = 3;
        }
    }

    public TypeDeserializer buildTypeDeserializer(MapperConfig config, JavaType baseType, Collection<NamedType> subtypes, BeanProperty property) {
        TypeIdResolver idRes = idResolver(config, baseType, subtypes, false, true);
        switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$As[this._includeAs.ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                return new AsArrayTypeDeserializer(baseType, idRes, property);
            case ClassWriter.COMPUTE_FRAMES:
                return new AsPropertyTypeDeserializer(baseType, idRes, property, this._typeProperty);
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                return new AsWrapperTypeDeserializer(baseType, idRes, property);
            default:
                throw new IllegalStateException("Do not know how to construct standard type serializer for inclusion type: " + this._includeAs);
        }
    }

    public TypeSerializer buildTypeSerializer(MapperConfig config, JavaType baseType, Collection<NamedType> subtypes, BeanProperty property) {
        TypeIdResolver idRes = idResolver(config, baseType, subtypes, true, false);
        switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$As[this._includeAs.ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                return new AsArrayTypeSerializer(idRes, property);
            case ClassWriter.COMPUTE_FRAMES:
                return new AsPropertyTypeSerializer(idRes, property, this._typeProperty);
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                return new AsWrapperTypeSerializer(idRes, property);
            default:
                throw new IllegalStateException("Do not know how to construct standard type serializer for inclusion type: " + this._includeAs);
        }
    }

    public String getTypeProperty() {
        return this._typeProperty;
    }

    protected TypeIdResolver idResolver(MapperConfig<?> config, JavaType baseType, Collection<NamedType> subtypes, boolean forSer, boolean forDeser) {
        if (this._customIdResolver != null) {
            return this._customIdResolver;
        }
        if (this._idType == null) {
            throw new IllegalStateException("Can not build, 'init()' not yet called");
        }
        switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$Id[this._idType.ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                return new ClassNameIdResolver(baseType, config.getTypeFactory());
            case ClassWriter.COMPUTE_FRAMES:
                return new MinimalClassNameIdResolver(baseType, config.getTypeFactory());
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                return TypeNameIdResolver.construct(config, baseType, subtypes, forSer, forDeser);
            default:
                throw new IllegalStateException("Do not know how to construct standard type id resolver for idType: " + this._idType);
        }
    }

    public StdTypeResolverBuilder inclusion(As includeAs) {
        if (includeAs == null) {
            throw new IllegalArgumentException("includeAs can not be null");
        }
        this._includeAs = includeAs;
        return this;
    }

    public StdTypeResolverBuilder init(Id idType, TypeIdResolver idRes) {
        if (idType == null) {
            throw new IllegalArgumentException("idType can not be null");
        }
        this._idType = idType;
        this._customIdResolver = idRes;
        this._typeProperty = idType.getDefaultPropertyName();
        return this;
    }

    public StdTypeResolverBuilder typeProperty(String typeIdPropName) {
        if (typeIdPropName == null || typeIdPropName.length() == 0) {
            typeIdPropName = this._idType.getDefaultPropertyName();
        }
        this._typeProperty = typeIdPropName;
        return this;
    }
}