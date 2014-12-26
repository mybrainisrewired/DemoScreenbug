package org.codehaus.jackson.map.type;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.map.util.ArrayBuilders;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

public final class TypeFactory {
    private static final JavaType[] NO_TYPES;
    @Deprecated
    public static final TypeFactory instance;
    protected final TypeModifier[] _modifiers;
    protected final TypeParser _parser;

    static {
        instance = new TypeFactory();
        NO_TYPES = new JavaType[0];
    }

    private TypeFactory() {
        this._parser = new TypeParser(this);
        this._modifiers = null;
    }

    protected TypeFactory(TypeParser p, TypeModifier[] mods) {
        this._parser = p;
        this._modifiers = mods;
    }

    private JavaType _collectionType(Class rawClass) {
        JavaType[] typeParams = findTypeParameters(rawClass, Collection.class);
        if (typeParams == null) {
            return CollectionType.construct(rawClass, _unknownType());
        }
        if (typeParams.length == 1) {
            return CollectionType.construct(rawClass, typeParams[0]);
        }
        throw new IllegalArgumentException("Strange Collection type " + rawClass.getName() + ": can not determine type parameters");
    }

    protected static HierarchicType _findSuperClassChain(Type currentType, Class<?> target) {
        HierarchicType current = new HierarchicType(currentType);
        Class<?> raw = current.getRawClass();
        if (raw == target) {
            return current;
        }
        Type parent = raw.getGenericSuperclass();
        if (parent != null) {
            HierarchicType sup = _findSuperClassChain(parent, target);
            if (sup != null) {
                sup.setSubType(current);
                current.setSuperType(sup);
                return current;
            }
        }
        return null;
    }

    protected static HierarchicType _findSuperInterfaceChain(Type currentType, Class<?> target) {
        HierarchicType current = new HierarchicType(currentType);
        Class<?> raw = current.getRawClass();
        if (raw == target) {
            return current;
        }
        HierarchicType sup;
        Type[] parents = raw.getGenericInterfaces();
        if (parents != null) {
            Type[] arr$ = parents;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                sup = _findSuperInterfaceChain(arr$[i$], target);
                if (sup != null) {
                    sup.setSubType(current);
                    current.setSuperType(sup);
                    return current;
                } else {
                    i$++;
                }
            }
        }
        Type parent = raw.getGenericSuperclass();
        if (parent != null) {
            sup = _findSuperInterfaceChain(parent, target);
            if (sup != null) {
                sup.setSubType(current);
                current.setSuperType(sup);
                return current;
            }
        }
        return null;
    }

    protected static HierarchicType _findSuperTypeChain(Class<?> subtype, Class<?> supertype) {
        return supertype.isInterface() ? _findSuperInterfaceChain(subtype, supertype) : _findSuperClassChain(subtype, supertype);
    }

    private JavaType _mapType(Class rawClass) {
        JavaType[] typeParams = findTypeParameters(rawClass, Map.class);
        if (typeParams == null) {
            return MapType.construct(rawClass, _unknownType(), _unknownType());
        }
        if (typeParams.length == 2) {
            return MapType.construct(rawClass, typeParams[0], typeParams[1]);
        }
        throw new IllegalArgumentException("Strange Map type " + rawClass.getName() + ": can not determine type parameters");
    }

    @Deprecated
    public static JavaType arrayType(Type elementType) {
        return instance.constructArrayType(instance.constructType(elementType));
    }

    @Deprecated
    public static JavaType arrayType(JavaType elementType) {
        return instance.constructArrayType(elementType);
    }

    @Deprecated
    public static JavaType collectionType(Class collectionType, Type elementType) {
        return instance.constructCollectionType(collectionType, instance.constructType(elementType));
    }

    @Deprecated
    public static JavaType collectionType(Class collectionType, JavaType elementType) {
        return instance.constructCollectionType(collectionType, elementType);
    }

    public static TypeFactory defaultInstance() {
        return instance;
    }

    @Deprecated
    public static JavaType fastSimpleType(Class<?> cls) {
        return instance.uncheckedSimpleType(cls);
    }

    @Deprecated
    public static JavaType[] findParameterTypes(Class clz, Class expType) {
        return instance.findTypeParameters(clz, expType);
    }

    @Deprecated
    public static JavaType[] findParameterTypes(Class<?> clz, Class<?> expType, TypeBindings bindings) {
        return instance.findTypeParameters(clz, expType, bindings);
    }

    @Deprecated
    public static JavaType[] findParameterTypes(JavaType type, Class expType) {
        return instance.findTypeParameters(type, expType);
    }

    public static JavaType fromCanonical(String canonical) throws IllegalArgumentException {
        return instance.constructFromCanonical(canonical);
    }

    @Deprecated
    public static JavaType fromClass(Class<?> clz) {
        return instance._fromClass(clz, null);
    }

    @Deprecated
    public static JavaType fromType(Type type) {
        return instance._constructType(type, null);
    }

    @Deprecated
    public static JavaType fromTypeReference(TypeReference<?> ref) {
        return type(ref.getType());
    }

    @Deprecated
    public static JavaType mapType(Class mapClass, Type keyType, Type valueType) {
        return instance.constructMapType(mapClass, type(keyType), instance.constructType(valueType));
    }

    @Deprecated
    public static JavaType mapType(Class mapType, JavaType keyType, JavaType valueType) {
        return instance.constructMapType(mapType, keyType, valueType);
    }

    @Deprecated
    public static JavaType parametricType(Class parametrized, Class... parameterClasses) {
        return instance.constructParametricType(parametrized, parameterClasses);
    }

    @Deprecated
    public static JavaType parametricType(Class parametrized, JavaType... parameterTypes) {
        return instance.constructParametricType(parametrized, parameterTypes);
    }

    public static Class<?> rawClass(Type t) {
        return t instanceof Class ? (Class) t : defaultInstance().constructType(t).getRawClass();
    }

    @Deprecated
    public static JavaType specialize(JavaType baseType, Class<?> subclass) {
        return instance.constructSpecializedType(baseType, subclass);
    }

    @Deprecated
    public static JavaType type(Type t) {
        return instance._constructType(t, null);
    }

    @Deprecated
    public static JavaType type(Type type, Class context) {
        return instance.constructType(type, context);
    }

    @Deprecated
    public static JavaType type(Type type, TypeBindings bindings) {
        return instance._constructType(type, bindings);
    }

    @Deprecated
    public static JavaType type(Type type, JavaType context) {
        return instance.constructType(type, context);
    }

    @Deprecated
    public static JavaType type(TypeReference<?> ref) {
        return instance.constructType(ref.getType());
    }

    public static JavaType unknownType() {
        return defaultInstance()._unknownType();
    }

    public JavaType _constructType(Type type, TypeBindings context) {
        JavaType resultType;
        if (type instanceof Class) {
            Class cls = (Class) type;
            if (context == null) {
                context = new TypeBindings(this, cls);
            }
            resultType = _fromClass(cls, context);
        } else if (type instanceof ParameterizedType) {
            resultType = _fromParamType((ParameterizedType) type, context);
        } else if (type instanceof GenericArrayType) {
            resultType = _fromArrayType((GenericArrayType) type, context);
        } else if (type instanceof TypeVariable) {
            resultType = _fromVariable((TypeVariable) type, context);
        } else if (type instanceof WildcardType) {
            resultType = _fromWildcard((WildcardType) type, context);
        } else {
            throw new IllegalArgumentException("Unrecognized Type: " + type.toString());
        }
        if (!(this._modifiers == null || resultType.isContainerType())) {
            TypeModifier[] arr$ = this._modifiers;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                resultType = arr$[i$].modifyType(resultType, type, context, this);
                i$++;
            }
        }
        return resultType;
    }

    protected JavaType _fromArrayType(GenericArrayType type, TypeBindings context) {
        return ArrayType.construct(_constructType(type.getGenericComponentType(), context));
    }

    protected JavaType _fromClass(Class<?> clz, TypeBindings context) {
        if (clz.isArray()) {
            return ArrayType.construct(_constructType(clz.getComponentType(), null));
        }
        if (clz.isEnum()) {
            return new SimpleType(clz);
        }
        if (Map.class.isAssignableFrom(clz)) {
            return _mapType(clz);
        }
        return Collection.class.isAssignableFrom(clz) ? _collectionType(clz) : new SimpleType(clz);
    }

    protected JavaType _fromParamType(ParameterizedType type, TypeBindings context) {
        JavaType[] pt;
        Class<?> rawType = (Class) type.getRawType();
        Type[] args = type.getActualTypeArguments();
        int paramCount = args == null ? 0 : args.length;
        if (paramCount == 0) {
            pt = NO_TYPES;
        } else {
            pt = new JavaType[paramCount];
            int i = 0;
            while (i < paramCount) {
                pt[i] = _constructType(args[i], context);
                i++;
            }
        }
        if (Map.class.isAssignableFrom(rawType)) {
            JavaType[] mapParams = findTypeParameters(constructSimpleType(rawType, pt), Map.class);
            if (mapParams.length == 2) {
                return MapType.construct(rawType, mapParams[0], mapParams[1]);
            }
            throw new IllegalArgumentException("Could not find 2 type parameters for Map class " + rawType.getName() + " (found " + mapParams.length + ")");
        } else if (!Collection.class.isAssignableFrom(rawType)) {
            return paramCount == 0 ? new SimpleType(rawType) : constructSimpleType(rawType, pt);
        } else {
            JavaType[] collectionParams = findTypeParameters(constructSimpleType(rawType, pt), Collection.class);
            if (collectionParams.length == 1) {
                return CollectionType.construct(rawType, collectionParams[0]);
            }
            throw new IllegalArgumentException("Could not find 1 type parameter for Collection class " + rawType.getName() + " (found " + collectionParams.length + ")");
        }
    }

    protected JavaType _fromParameterizedClass(Class<?> clz, List<JavaType> paramTypes) {
        if (clz.isArray()) {
            return ArrayType.construct(_constructType(clz.getComponentType(), null));
        }
        if (clz.isEnum()) {
            return new SimpleType(clz);
        }
        if (!Map.class.isAssignableFrom(clz)) {
            return Collection.class.isAssignableFrom(clz) ? paramTypes.size() >= 1 ? CollectionType.construct(clz, (JavaType) paramTypes.get(0)) : _collectionType(clz) : paramTypes.size() == 0 ? new SimpleType(clz) : constructSimpleType(clz, (JavaType[]) paramTypes.toArray(new JavaType[paramTypes.size()]));
        } else {
            if (paramTypes.size() <= 0) {
                return _mapType(clz);
            }
            return MapType.construct(clz, (JavaType) paramTypes.get(0), paramTypes.size() >= 2 ? (JavaType) paramTypes.get(1) : _unknownType());
        }
    }

    protected JavaType _fromVariable(TypeVariable<?> type, TypeBindings context) {
        if (context == null) {
            return _unknownType();
        }
        String name = type.getName();
        JavaType actualType = context.findType(name);
        if (actualType != null) {
            return actualType;
        }
        Type[] bounds = type.getBounds();
        context._addPlaceholder(name);
        return _constructType(bounds[0], context);
    }

    protected JavaType _fromWildcard(WildcardType type, TypeBindings context) {
        return _constructType(type.getUpperBounds()[0], context);
    }

    protected JavaType _resolveVariableViaSubTypes(HierarchicType leafType, String variableName, TypeBindings bindings) {
        if (leafType != null && leafType.isGeneric()) {
            TypeVariable<?>[] typeVariables = leafType.getRawClass().getTypeParameters();
            int i = 0;
            int len = typeVariables.length;
            while (i < len) {
                if (variableName.equals(typeVariables[i].getName())) {
                    Type type = leafType.asGeneric().getActualTypeArguments()[i];
                    return type instanceof TypeVariable ? _resolveVariableViaSubTypes(leafType.getSubType(), ((TypeVariable) type).getName(), bindings) : _constructType(type, bindings);
                } else {
                    i++;
                }
            }
        }
        return _unknownType();
    }

    protected JavaType _unknownType() {
        return new SimpleType(Object.class, null, null);
    }

    protected ArrayType constructArrayType(Class<?> elementType) {
        return ArrayType.construct(_constructType(elementType, null));
    }

    protected ArrayType constructArrayType(JavaType elementType) {
        return ArrayType.construct(elementType);
    }

    public CollectionLikeType constructCollectionLikeType(Class<?> collectionClass, Type elementClass) {
        return CollectionLikeType.construct(collectionClass, constructType(elementClass));
    }

    public CollectionLikeType constructCollectionLikeType(Class<?> collectionClass, JavaType elementType) {
        return CollectionLikeType.construct(collectionClass, elementType);
    }

    public CollectionType constructCollectionType(Class<? extends Collection> collectionClass, Type elementClass) {
        return CollectionType.construct(collectionClass, constructType(elementClass));
    }

    public CollectionType constructCollectionType(Class<? extends Collection> collectionClass, JavaType elementType) {
        return CollectionType.construct(collectionClass, elementType);
    }

    public JavaType constructFromCanonical(String canonical) throws IllegalArgumentException {
        return this._parser.parse(canonical);
    }

    public MapLikeType constructMapLikeType(Class<?> mapClass, Type keyClass, Type valueClass) {
        return MapType.construct(mapClass, constructType(keyClass), constructType(valueClass));
    }

    public MapLikeType constructMapLikeType(Class<?> mapClass, JavaType keyType, JavaType valueType) {
        return MapLikeType.construct(mapClass, keyType, valueType);
    }

    public MapType constructMapType(Class<? extends Map> mapClass, Type keyClass, Type valueClass) {
        return MapType.construct(mapClass, constructType(keyClass), constructType(valueClass));
    }

    public MapType constructMapType(Class<? extends Map> mapClass, JavaType keyType, JavaType valueType) {
        return MapType.construct(mapClass, keyType, valueType);
    }

    public JavaType constructParametricType(Class parametrized, Class<?>... parameterClasses) {
        int len = parameterClasses.length;
        JavaType[] pt = new JavaType[len];
        int i = 0;
        while (i < len) {
            pt[i] = _fromClass(parameterClasses[i], null);
            i++;
        }
        return constructParametricType(parametrized, pt);
    }

    public JavaType constructParametricType(Class parametrized, JavaType... parameterTypes) {
        if (parametrized.isArray()) {
            if (parameterTypes.length == 1) {
                return constructArrayType(parameterTypes[0]);
            }
            throw new IllegalArgumentException("Need exactly 1 parameter type for arrays (" + parametrized.getName() + ")");
        } else if (Map.class.isAssignableFrom(parametrized)) {
            if (parameterTypes.length == 2) {
                return constructMapType(parametrized, parameterTypes[0], parameterTypes[1]);
            }
            throw new IllegalArgumentException("Need exactly 2 parameter types for Map types (" + parametrized.getName() + ")");
        } else if (!Collection.class.isAssignableFrom(parametrized)) {
            return constructSimpleType(parametrized, parameterTypes);
        } else {
            if (parameterTypes.length == 1) {
                return constructCollectionType(parametrized, parameterTypes[0]);
            }
            throw new IllegalArgumentException("Need exactly 1 parameter type for Collection types (" + parametrized.getName() + ")");
        }
    }

    public JavaType constructSimpleType(Class<?> rawType, JavaType[] parameterTypes) {
        TypeVariable<?>[] typeVars = rawType.getTypeParameters();
        if (typeVars.length != parameterTypes.length) {
            throw new IllegalArgumentException("Parameter type mismatch for " + rawType.getName() + ": expected " + typeVars.length + " parameters, was given " + parameterTypes.length);
        }
        String[] names = new String[typeVars.length];
        int i = 0;
        int len = typeVars.length;
        while (i < len) {
            names[i] = typeVars[i].getName();
            i++;
        }
        return new SimpleType(rawType, names, parameterTypes);
    }

    public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
        if (!(baseType instanceof SimpleType) || (!subclass.isArray() && !Map.class.isAssignableFrom(subclass) && !Collection.class.isAssignableFrom(subclass))) {
            return baseType.narrowBy(subclass);
        }
        if (baseType.getRawClass().isAssignableFrom(subclass)) {
            JavaType subtype = instance._fromClass(subclass, new TypeBindings(this, baseType.getRawClass()));
            Object h = baseType.getValueHandler();
            if (h != null) {
                subtype.setValueHandler(h);
            }
            h = baseType.getTypeHandler();
            return h != null ? subtype.withTypeHandler(h) : subtype;
        } else {
            throw new IllegalArgumentException("Class " + subclass.getClass().getName() + " not subtype of " + baseType);
        }
    }

    public JavaType constructType(Type type) {
        return _constructType(type, null);
    }

    public JavaType constructType(Type type, Class context) {
        return _constructType(type, new TypeBindings(this, context));
    }

    public JavaType constructType(Type type, TypeBindings bindings) {
        return _constructType(type, bindings);
    }

    public JavaType constructType(Type type, JavaType context) {
        return _constructType(type, new TypeBindings(this, context));
    }

    public JavaType constructType(TypeReference<?> typeRef) {
        return _constructType(typeRef.getType(), null);
    }

    public JavaType[] findTypeParameters(Class clz, Class<?> expType) {
        return findTypeParameters(clz, expType, new TypeBindings(this, clz));
    }

    public JavaType[] findTypeParameters(Class<?> clz, Class<?> expType, TypeBindings bindings) {
        HierarchicType subType = _findSuperTypeChain(clz, expType);
        if (subType == null) {
            throw new IllegalArgumentException("Class " + clz.getName() + " is not a subtype of " + expType.getName());
        }
        HierarchicType superType = subType;
        while (superType.getSuperType() != null) {
            superType = superType.getSuperType();
            Class raw = superType.getRawClass();
            TypeBindings newBindings = new TypeBindings(this, raw);
            if (superType.isGeneric()) {
                Type[] actualTypes = superType.asGeneric().getActualTypeArguments();
                TypeVariable<?>[] vars = raw.getTypeParameters();
                int len = actualTypes.length;
                int i = 0;
                while (i < len) {
                    newBindings.addBinding(vars[i].getName(), instance._constructType(actualTypes[i], bindings));
                    i++;
                }
            }
            bindings = newBindings;
        }
        return !superType.isGeneric() ? null : bindings.typesAsArray();
    }

    public JavaType[] findTypeParameters(JavaType type, Class<?> expType) {
        Class<?> raw = type.getRawClass();
        if (raw != expType) {
            return findTypeParameters(raw, expType, new TypeBindings(this, type));
        }
        int count = type.containedTypeCount();
        if (count == 0) {
            return null;
        }
        JavaType[] result = new JavaType[count];
        int i = 0;
        while (i < count) {
            result[i] = type.containedType(i);
            i++;
        }
        return result;
    }

    public JavaType uncheckedSimpleType(Class<?> cls) {
        return new SimpleType(cls, null, null);
    }

    public TypeFactory withModifier(TypeModifier mod) {
        if (this._modifiers != null) {
            return new TypeFactory(this._parser, (TypeModifier[]) ArrayBuilders.insertInListNoDup(this._modifiers, mod));
        }
        return new TypeFactory(this._parser, new TypeModifier[]{mod});
    }
}