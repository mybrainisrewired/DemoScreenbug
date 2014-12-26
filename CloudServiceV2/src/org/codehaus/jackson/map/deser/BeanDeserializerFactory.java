package org.codehaus.jackson.map.deser;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.AbstractTypeResolver;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.AnnotationIntrospector.ReferenceProperty;
import org.codehaus.jackson.map.BeanDescription;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.BeanProperty.Std;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializerFactory;
import org.codehaus.jackson.map.DeserializerFactory.Config;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.Deserializers;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.KeyDeserializers;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.deser.SettableBeanProperty.FieldProperty;
import org.codehaus.jackson.map.deser.SettableBeanProperty.MethodProperty;
import org.codehaus.jackson.map.deser.SettableBeanProperty.SetterlessProperty;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedField;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.CollectionLikeType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapLikeType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.map.util.ArrayBuilders;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

public class BeanDeserializerFactory extends BasicDeserializerFactory {
    private static final Class<?>[] INIT_CAUSE_PARAMS;
    public static final BeanDeserializerFactory instance;
    protected final Config _factoryConfig;

    public static class ConfigImpl extends Config {
        protected static final AbstractTypeResolver[] NO_ABSTRACT_TYPE_RESOLVERS;
        protected static final KeyDeserializers[] NO_KEY_DESERIALIZERS;
        protected static final BeanDeserializerModifier[] NO_MODIFIERS;
        protected final AbstractTypeResolver[] _abstractTypeResolvers;
        protected final Deserializers[] _additionalDeserializers;
        protected final KeyDeserializers[] _additionalKeyDeserializers;
        protected final BeanDeserializerModifier[] _modifiers;

        static {
            NO_KEY_DESERIALIZERS = new KeyDeserializers[0];
            NO_MODIFIERS = new BeanDeserializerModifier[0];
            NO_ABSTRACT_TYPE_RESOLVERS = new AbstractTypeResolver[0];
        }

        public ConfigImpl() {
            this(null, null, null, null);
        }

        protected ConfigImpl(Deserializers[] allAdditionalDeserializers, KeyDeserializers[] allAdditionalKeyDeserializers, BeanDeserializerModifier[] modifiers, AbstractTypeResolver[] atr) {
            if (allAdditionalDeserializers == null) {
                allAdditionalDeserializers = NO_DESERIALIZERS;
            }
            this._additionalDeserializers = allAdditionalDeserializers;
            if (allAdditionalKeyDeserializers == null) {
                allAdditionalKeyDeserializers = NO_KEY_DESERIALIZERS;
            }
            this._additionalKeyDeserializers = allAdditionalKeyDeserializers;
            if (modifiers == null) {
                modifiers = NO_MODIFIERS;
            }
            this._modifiers = modifiers;
            if (atr == null) {
                atr = NO_ABSTRACT_TYPE_RESOLVERS;
            }
            this._abstractTypeResolvers = atr;
        }

        public Iterable<AbstractTypeResolver> abstractTypeResolvers() {
            return ArrayBuilders.arrayAsIterable(this._abstractTypeResolvers);
        }

        public Iterable<BeanDeserializerModifier> deserializerModifiers() {
            return ArrayBuilders.arrayAsIterable(this._modifiers);
        }

        public Iterable<Deserializers> deserializers() {
            return ArrayBuilders.arrayAsIterable(this._additionalDeserializers);
        }

        public boolean hasAbstractTypeResolvers() {
            return this._abstractTypeResolvers.length > 0;
        }

        public boolean hasDeserializerModifiers() {
            return this._modifiers.length > 0;
        }

        public boolean hasDeserializers() {
            return this._additionalDeserializers.length > 0;
        }

        public boolean hasKeyDeserializers() {
            return this._additionalKeyDeserializers.length > 0;
        }

        public Iterable<KeyDeserializers> keyDeserializers() {
            return ArrayBuilders.arrayAsIterable(this._additionalKeyDeserializers);
        }

        public Config withAbstractTypeResolver(AbstractTypeResolver resolver) {
            if (resolver == null) {
                throw new IllegalArgumentException("Can not pass null resolver");
            }
            return new org.codehaus.jackson.map.deser.BeanDeserializerFactory.ConfigImpl(this._additionalDeserializers, this._additionalKeyDeserializers, this._modifiers, (AbstractTypeResolver[]) ArrayBuilders.insertInListNoDup(this._abstractTypeResolvers, resolver));
        }

        public Config withAdditionalDeserializers(Deserializers additional) {
            if (additional != null) {
                return new org.codehaus.jackson.map.deser.BeanDeserializerFactory.ConfigImpl((Deserializers[]) ArrayBuilders.insertInListNoDup(this._additionalDeserializers, additional), this._additionalKeyDeserializers, this._modifiers, this._abstractTypeResolvers);
            }
            throw new IllegalArgumentException("Can not pass null Deserializers");
        }

        public Config withAdditionalKeyDeserializers(KeyDeserializers additional) {
            if (additional == null) {
                throw new IllegalArgumentException("Can not pass null KeyDeserializers");
            }
            return new org.codehaus.jackson.map.deser.BeanDeserializerFactory.ConfigImpl(this._additionalDeserializers, (KeyDeserializers[]) ArrayBuilders.insertInListNoDup(this._additionalKeyDeserializers, additional), this._modifiers, this._abstractTypeResolvers);
        }

        public Config withDeserializerModifier(BeanDeserializerModifier modifier) {
            if (modifier == null) {
                throw new IllegalArgumentException("Can not pass null modifier");
            }
            return new org.codehaus.jackson.map.deser.BeanDeserializerFactory.ConfigImpl(this._additionalDeserializers, this._additionalKeyDeserializers, (BeanDeserializerModifier[]) ArrayBuilders.insertInListNoDup(this._modifiers, modifier), this._abstractTypeResolvers);
        }
    }

    static {
        INIT_CAUSE_PARAMS = new Class[]{Throwable.class};
        instance = new BeanDeserializerFactory(null);
    }

    @Deprecated
    public BeanDeserializerFactory() {
        this(null);
    }

    public BeanDeserializerFactory(Config config) {
        if (config == null) {
            config = new ConfigImpl();
        }
        this._factoryConfig = config;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void _addDeserializerConstructors(org.codehaus.jackson.map.DeserializationConfig r18_config, org.codehaus.jackson.map.introspect.BasicBeanDescription r19_beanDesc, org.codehaus.jackson.map.introspect.VisibilityChecker<?> r20_vchecker, org.codehaus.jackson.map.AnnotationIntrospector r21_intr, org.codehaus.jackson.map.deser.CreatorContainer r22_creators) throws org.codehaus.jackson.map.JsonMappingException {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.deser.BeanDeserializerFactory._addDeserializerConstructors(org.codehaus.jackson.map.DeserializationConfig, org.codehaus.jackson.map.introspect.BasicBeanDescription, org.codehaus.jackson.map.introspect.VisibilityChecker, org.codehaus.jackson.map.AnnotationIntrospector, org.codehaus.jackson.map.deser.CreatorContainer):void");
        /*
        r17 = this;
        r1 = r19.getConstructors();
        r10 = r1.iterator();
    L_0x0008:
        r1 = r10.hasNext();
        if (r1 == 0) goto L_0x0107;
    L_0x000e:
        r9 = r10.next();
        r9 = (org.codehaus.jackson.map.introspect.AnnotatedConstructor) r9;
        r8 = r9.getParameterCount();
        r1 = 1;
        if (r8 < r1) goto L_0x0008;
    L_0x001b:
        r0 = r21;
        r11 = r0.hasCreatorAnnotation(r9);
        r0 = r20;
        r12 = r0.isCreatorVisible(r9);
        r1 = 1;
        if (r8 != r1) goto L_0x0095;
    L_0x002a:
        r1 = 0;
        r6 = r9.getParameter(r1);
        r0 = r21;
        r4 = r0.findPropertyNameForParam(r6);
        if (r4 == 0) goto L_0x003d;
    L_0x0037:
        r1 = r4.length();
        if (r1 != 0) goto L_0x007c;
    L_0x003d:
        r1 = 0;
        r15 = r9.getParameterClass(r1);
        r1 = java.lang.String.class;
        if (r15 != r1) goto L_0x0050;
    L_0x0046:
        if (r11 != 0) goto L_0x004a;
    L_0x0048:
        if (r12 == 0) goto L_0x0008;
    L_0x004a:
        r0 = r22;
        r0.addStringConstructor(r9);
        goto L_0x0008;
    L_0x0050:
        r1 = java.lang.Integer.TYPE;
        if (r15 == r1) goto L_0x0058;
    L_0x0054:
        r1 = java.lang.Integer.class;
        if (r15 != r1) goto L_0x0062;
    L_0x0058:
        if (r11 != 0) goto L_0x005c;
    L_0x005a:
        if (r12 == 0) goto L_0x0008;
    L_0x005c:
        r0 = r22;
        r0.addIntConstructor(r9);
        goto L_0x0008;
    L_0x0062:
        r1 = java.lang.Long.TYPE;
        if (r15 == r1) goto L_0x006a;
    L_0x0066:
        r1 = java.lang.Long.class;
        if (r15 != r1) goto L_0x0074;
    L_0x006a:
        if (r11 != 0) goto L_0x006e;
    L_0x006c:
        if (r12 == 0) goto L_0x0008;
    L_0x006e:
        r0 = r22;
        r0.addLongConstructor(r9);
        goto L_0x0008;
    L_0x0074:
        if (r11 == 0) goto L_0x0008;
    L_0x0076:
        r0 = r22;
        r0.addDelegatingConstructor(r9);
        goto L_0x0008;
    L_0x007c:
        r1 = 1;
        r14 = new org.codehaus.jackson.map.deser.SettableBeanProperty[r1];
        r16 = 0;
        r5 = 0;
        r1 = r17;
        r2 = r18;
        r3 = r19;
        r1 = r1.constructCreatorProperty(r2, r3, r4, r5, r6);
        r14[r16] = r1;
        r0 = r22;
        r0.addPropertyConstructor(r9, r14);
        goto L_0x0008;
    L_0x0095:
        if (r11 != 0) goto L_0x0099;
    L_0x0097:
        if (r12 == 0) goto L_0x0008;
    L_0x0099:
        r7 = 0;
        r13 = 0;
        r14 = new org.codehaus.jackson.map.deser.SettableBeanProperty[r8];
        r5 = 0;
    L_0x009e:
        if (r5 >= r8) goto L_0x00fe;
    L_0x00a0:
        r6 = r9.getParameter(r5);
        if (r6 != 0) goto L_0x00e4;
    L_0x00a6:
        r4 = 0;
    L_0x00a7:
        if (r4 == 0) goto L_0x00af;
    L_0x00a9:
        r1 = r4.length();
        if (r1 != 0) goto L_0x00eb;
    L_0x00af:
        r1 = 1;
    L_0x00b0:
        r13 = r13 | r1;
        if (r13 != 0) goto L_0x00ed;
    L_0x00b3:
        r1 = 1;
    L_0x00b4:
        r7 = r7 | r1;
        if (r13 == 0) goto L_0x00ef;
    L_0x00b7:
        if (r7 != 0) goto L_0x00bb;
    L_0x00b9:
        if (r11 == 0) goto L_0x00ef;
    L_0x00bb:
        r1 = new java.lang.IllegalArgumentException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Argument #";
        r2 = r2.append(r3);
        r2 = r2.append(r5);
        r3 = " of constructor ";
        r2 = r2.append(r3);
        r2 = r2.append(r9);
        r3 = " has no property name annotation; must have name when multiple-paramater constructor annotated as Creator";
        r2 = r2.append(r3);
        r2 = r2.toString();
        r1.<init>(r2);
        throw r1;
    L_0x00e4:
        r0 = r21;
        r4 = r0.findPropertyNameForParam(r6);
        goto L_0x00a7;
    L_0x00eb:
        r1 = 0;
        goto L_0x00b0;
    L_0x00ed:
        r1 = 0;
        goto L_0x00b4;
    L_0x00ef:
        r1 = r17;
        r2 = r18;
        r3 = r19;
        r1 = r1.constructCreatorProperty(r2, r3, r4, r5, r6);
        r14[r5] = r1;
        r5 = r5 + 1;
        goto L_0x009e;
    L_0x00fe:
        if (r7 == 0) goto L_0x0008;
    L_0x0100:
        r0 = r22;
        r0.addPropertyConstructor(r9, r14);
        goto L_0x0008;
    L_0x0107:
        return;
        */
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void _addDeserializerFactoryMethods(org.codehaus.jackson.map.DeserializationConfig r14_config, org.codehaus.jackson.map.introspect.BasicBeanDescription r15_beanDesc, org.codehaus.jackson.map.introspect.VisibilityChecker<?> r16_vchecker, org.codehaus.jackson.map.AnnotationIntrospector r17_intr, org.codehaus.jackson.map.deser.CreatorContainer r18_creators) throws org.codehaus.jackson.map.JsonMappingException {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.deser.BeanDeserializerFactory._addDeserializerFactoryMethods(org.codehaus.jackson.map.DeserializationConfig, org.codehaus.jackson.map.introspect.BasicBeanDescription, org.codehaus.jackson.map.introspect.VisibilityChecker, org.codehaus.jackson.map.AnnotationIntrospector, org.codehaus.jackson.map.deser.CreatorContainer):void");
        /*
        r13 = this;
        r1 = r15.getFactoryMethods();
        r9 = r1.iterator();
    L_0x0008:
        r1 = r9.hasNext();
        if (r1 == 0) goto L_0x00ea;
    L_0x000e:
        r8 = r9.next();
        r8 = (org.codehaus.jackson.map.introspect.AnnotatedMethod) r8;
        r7 = r8.getParameterCount();
        r1 = 1;
        if (r7 < r1) goto L_0x0008;
    L_0x001b:
        r0 = r17;
        r10 = r0.hasCreatorAnnotation(r8);
        r1 = 1;
        if (r7 != r1) goto L_0x008f;
    L_0x0024:
        r1 = 0;
        r1 = r8.getParameter(r1);
        r0 = r17;
        r4 = r0.findPropertyNameForParam(r1);
        if (r4 == 0) goto L_0x0037;
    L_0x0031:
        r1 = r4.length();
        if (r1 != 0) goto L_0x0097;
    L_0x0037:
        r1 = 0;
        r12 = r8.getParameterClass(r1);
        r1 = java.lang.String.class;
        if (r12 != r1) goto L_0x0050;
    L_0x0040:
        if (r10 != 0) goto L_0x004a;
    L_0x0042:
        r0 = r16;
        r1 = r0.isCreatorVisible(r8);
        if (r1 == 0) goto L_0x0008;
    L_0x004a:
        r0 = r18;
        r0.addStringFactory(r8);
        goto L_0x0008;
    L_0x0050:
        r1 = java.lang.Integer.TYPE;
        if (r12 == r1) goto L_0x0058;
    L_0x0054:
        r1 = java.lang.Integer.class;
        if (r12 != r1) goto L_0x0068;
    L_0x0058:
        if (r10 != 0) goto L_0x0062;
    L_0x005a:
        r0 = r16;
        r1 = r0.isCreatorVisible(r8);
        if (r1 == 0) goto L_0x0008;
    L_0x0062:
        r0 = r18;
        r0.addIntFactory(r8);
        goto L_0x0008;
    L_0x0068:
        r1 = java.lang.Long.TYPE;
        if (r12 == r1) goto L_0x0070;
    L_0x006c:
        r1 = java.lang.Long.class;
        if (r12 != r1) goto L_0x0080;
    L_0x0070:
        if (r10 != 0) goto L_0x007a;
    L_0x0072:
        r0 = r16;
        r1 = r0.isCreatorVisible(r8);
        if (r1 == 0) goto L_0x0008;
    L_0x007a:
        r0 = r18;
        r0.addLongFactory(r8);
        goto L_0x0008;
    L_0x0080:
        r0 = r17;
        r1 = r0.hasCreatorAnnotation(r8);
        if (r1 == 0) goto L_0x0008;
    L_0x0088:
        r0 = r18;
        r0.addDelegatingFactory(r8);
        goto L_0x0008;
    L_0x008f:
        r0 = r17;
        r1 = r0.hasCreatorAnnotation(r8);
        if (r1 == 0) goto L_0x0008;
    L_0x0097:
        r11 = new org.codehaus.jackson.map.deser.SettableBeanProperty[r7];
        r5 = 0;
    L_0x009a:
        if (r5 >= r7) goto L_0x00e3;
    L_0x009c:
        r6 = r8.getParameter(r5);
        r0 = r17;
        r4 = r0.findPropertyNameForParam(r6);
        if (r4 == 0) goto L_0x00ae;
    L_0x00a8:
        r1 = r4.length();
        if (r1 != 0) goto L_0x00d7;
    L_0x00ae:
        r1 = new java.lang.IllegalArgumentException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Argument #";
        r2 = r2.append(r3);
        r2 = r2.append(r5);
        r3 = " of factory method ";
        r2 = r2.append(r3);
        r2 = r2.append(r8);
        r3 = " has no property name annotation; must have when multiple-paramater static method annotated as Creator";
        r2 = r2.append(r3);
        r2 = r2.toString();
        r1.<init>(r2);
        throw r1;
    L_0x00d7:
        r1 = r13;
        r2 = r14;
        r3 = r15;
        r1 = r1.constructCreatorProperty(r2, r3, r4, r5, r6);
        r11[r5] = r1;
        r5 = r5 + 1;
        goto L_0x009a;
    L_0x00e3:
        r0 = r18;
        r0.addPropertyFactory(r8, r11);
        goto L_0x0008;
    L_0x00ea:
        return;
        */
    }

    protected JsonDeserializer<?> _findCustomArrayDeserializer(ArrayType type, DeserializationConfig config, DeserializerProvider provider, BeanProperty property, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        Iterator i$ = this._factoryConfig.deserializers().iterator();
        while (i$.hasNext()) {
            JsonDeserializer<?> deser = ((Deserializers) i$.next()).findArrayDeserializer(type, config, provider, property, elementTypeDeserializer, elementDeserializer);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    protected JsonDeserializer<Object> _findCustomBeanDeserializer(JavaType type, DeserializationConfig config, DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        Iterator i$ = this._factoryConfig.deserializers().iterator();
        while (i$.hasNext()) {
            JsonDeserializer<?> deser = ((Deserializers) i$.next()).findBeanDeserializer(type, config, provider, beanDesc, property);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    protected JsonDeserializer<?> _findCustomCollectionDeserializer(CollectionType type, DeserializationConfig config, DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        Iterator i$ = this._factoryConfig.deserializers().iterator();
        while (i$.hasNext()) {
            JsonDeserializer<?> deser = ((Deserializers) i$.next()).findCollectionDeserializer(type, config, provider, beanDesc, property, elementTypeDeserializer, elementDeserializer);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    protected JsonDeserializer<?> _findCustomCollectionLikeDeserializer(CollectionLikeType type, DeserializationConfig config, DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        Iterator i$ = this._factoryConfig.deserializers().iterator();
        while (i$.hasNext()) {
            JsonDeserializer<?> deser = ((Deserializers) i$.next()).findCollectionLikeDeserializer(type, config, provider, beanDesc, property, elementTypeDeserializer, elementDeserializer);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    protected JsonDeserializer<?> _findCustomEnumDeserializer(Class<?> type, DeserializationConfig config, BasicBeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        Iterator i$ = this._factoryConfig.deserializers().iterator();
        while (i$.hasNext()) {
            JsonDeserializer<?> deser = ((Deserializers) i$.next()).findEnumDeserializer(type, config, beanDesc, property);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    protected JsonDeserializer<?> _findCustomMapDeserializer(MapType type, DeserializationConfig config, DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property, KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        Iterator i$ = this._factoryConfig.deserializers().iterator();
        while (i$.hasNext()) {
            JsonDeserializer<?> deser = ((Deserializers) i$.next()).findMapDeserializer(type, config, provider, beanDesc, property, keyDeserializer, elementTypeDeserializer, elementDeserializer);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    protected JsonDeserializer<?> _findCustomMapLikeDeserializer(MapLikeType type, DeserializationConfig config, DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property, KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        Iterator i$ = this._factoryConfig.deserializers().iterator();
        while (i$.hasNext()) {
            JsonDeserializer<?> deser = ((Deserializers) i$.next()).findMapLikeDeserializer(type, config, provider, beanDesc, property, keyDeserializer, elementTypeDeserializer, elementDeserializer);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    protected JsonDeserializer<?> _findCustomTreeNodeDeserializer(Class<? extends JsonNode> type, DeserializationConfig config, BeanProperty property) throws JsonMappingException {
        Iterator i$ = this._factoryConfig.deserializers().iterator();
        while (i$.hasNext()) {
            JsonDeserializer<?> deser = ((Deserializers) i$.next()).findTreeNodeDeserializer(type, config, property);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    protected JavaType _mapAbstractType2(DeserializationConfig config, JavaType type) throws JsonMappingException {
        JavaType concrete;
        Class<?> currClass = type.getRawClass();
        if (this._factoryConfig.hasAbstractTypeResolvers()) {
            Iterator i$ = this._factoryConfig.abstractTypeResolvers().iterator();
            while (i$.hasNext()) {
                concrete = ((AbstractTypeResolver) i$.next()).findTypeMapping(config, type);
                if (concrete != null && concrete.getRawClass() != currClass) {
                    return concrete;
                }
            }
        }
        AbstractTypeResolver resolver = config.getAbstractTypeResolver();
        if (resolver != null) {
            concrete = resolver.findTypeMapping(config, type);
            if (!(concrete == null || concrete.getRawClass() == currClass)) {
                return concrete;
            }
        }
        return null;
    }

    protected void addBeanProps(DeserializationConfig config, BasicBeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
        SettableBeanProperty prop;
        VisibilityChecker<?> vchecker = config.getDefaultVisibilityChecker();
        if (!config.isEnabled(Feature.AUTO_DETECT_SETTERS)) {
            vchecker = vchecker.withSetterVisibility(Visibility.NONE);
        }
        if (!config.isEnabled(Feature.AUTO_DETECT_FIELDS)) {
            vchecker = vchecker.withFieldVisibility(Visibility.NONE);
        }
        vchecker = config.getAnnotationIntrospector().findAutoDetectVisibility(beanDesc.getClassInfo(), vchecker);
        Map<String, AnnotatedMethod> setters = beanDesc.findSetters(vchecker);
        AnnotatedMethod anySetter = beanDesc.findAnySetter();
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        Boolean B = intr.findIgnoreUnknownProperties(beanDesc.getClassInfo());
        if (B != null) {
            builder.setIgnoreUnknownProperties(B.booleanValue());
        }
        HashSet<String> ignored = ArrayBuilders.arrayToSet(intr.findPropertiesToIgnore(beanDesc.getClassInfo()));
        Iterator i$ = ignored.iterator();
        while (i$.hasNext()) {
            builder.addIgnorable((String) i$.next());
        }
        AnnotatedClass ac = beanDesc.getClassInfo();
        i$ = ac.ignoredMemberMethods().iterator();
        while (i$.hasNext()) {
            String name = beanDesc.okNameForSetter((AnnotatedMethod) i$.next());
            if (name != null) {
                builder.addIgnorable(name);
            }
        }
        i$ = ac.ignoredFields().iterator();
        while (i$.hasNext()) {
            builder.addIgnorable(((AnnotatedField) i$.next()).getName());
        }
        HashMap<Class<?>, Boolean> ignoredTypes = new HashMap();
        i$ = setters.entrySet().iterator();
        while (i$.hasNext()) {
            Entry<String, AnnotatedMethod> en = (Entry) i$.next();
            name = en.getKey();
            if (!ignored.contains(name)) {
                AnnotatedMethod setter = (AnnotatedMethod) en.getValue();
                if (isIgnorableType(config, beanDesc, setter.getParameterClass(0), ignoredTypes)) {
                    builder.addIgnorable(name);
                } else {
                    prop = constructSettableProperty(config, beanDesc, name, setter);
                    if (prop != null) {
                        builder.addProperty(prop);
                    }
                }
            }
        }
        if (anySetter != null) {
            builder.setAnySetter(constructAnySetter(config, beanDesc, anySetter));
        }
        HashSet<String> addedProps = new HashSet(setters.keySet());
        i$ = beanDesc.findDeserializableFields(vchecker, addedProps).entrySet().iterator();
        while (i$.hasNext()) {
            Entry<String, AnnotatedField> en2 = (Entry) i$.next();
            name = en2.getKey();
            if (!(ignored.contains(name) || builder.hasProperty(name))) {
                AnnotatedField field = (AnnotatedField) en2.getValue();
                if (isIgnorableType(config, beanDesc, field.getRawType(), ignoredTypes)) {
                    builder.addIgnorable(name);
                } else {
                    prop = constructSettableProperty(config, beanDesc, name, field);
                    if (prop != null) {
                        builder.addProperty(prop);
                        addedProps.add(name);
                    }
                }
            }
        }
        if (config.isEnabled(Feature.USE_GETTERS_AS_SETTERS)) {
            i$ = beanDesc.findGetters(vchecker, addedProps).entrySet().iterator();
            while (i$.hasNext()) {
                en = i$.next();
                AnnotatedMethod getter = (AnnotatedMethod) en.getValue();
                Class<?> rt = getter.getRawType();
                if (Collection.class.isAssignableFrom(rt) || Map.class.isAssignableFrom(rt)) {
                    name = en.getKey();
                    if (!(ignored.contains(name) || builder.hasProperty(name))) {
                        builder.addProperty(constructSetterlessProperty(config, beanDesc, name, getter));
                        addedProps.add(name);
                    }
                }
            }
        }
    }

    protected void addReferenceProperties(DeserializationConfig config, BasicBeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
        Map<String, AnnotatedMember> refs = beanDesc.findBackReferenceProperties();
        if (refs != null) {
            Iterator i$ = refs.entrySet().iterator();
            while (i$.hasNext()) {
                Entry<String, AnnotatedMember> en = (Entry) i$.next();
                String name = (String) en.getKey();
                AnnotatedMember m = (AnnotatedMember) en.getValue();
                if (m instanceof AnnotatedMethod) {
                    builder.addBackReferenceProperty(name, constructSettableProperty(config, beanDesc, m.getName(), (AnnotatedMethod) m));
                } else {
                    builder.addBackReferenceProperty(name, constructSettableProperty(config, beanDesc, m.getName(), (AnnotatedField) m));
                }
            }
        }
    }

    public JsonDeserializer<Object> buildBeanDeserializer(DeserializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        if (type.isAbstract()) {
            return new AbstractDeserializer(type);
        }
        Iterator i$;
        BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(beanDesc);
        builder.setCreators(findDeserializerCreators(config, beanDesc));
        addBeanProps(config, beanDesc, builder);
        addReferenceProperties(config, beanDesc, builder);
        if (this._factoryConfig.hasDeserializerModifiers()) {
            i$ = this._factoryConfig.deserializerModifiers().iterator();
            while (i$.hasNext()) {
                builder = ((BeanDeserializerModifier) i$.next()).updateBuilder(config, beanDesc, builder);
            }
        }
        JsonDeserializer<Object> deserializer = builder.build(property);
        if (!this._factoryConfig.hasDeserializerModifiers()) {
            return deserializer;
        }
        i$ = this._factoryConfig.deserializerModifiers().iterator();
        while (i$.hasNext()) {
            deserializer = i$.next().modifyDeserializer(config, beanDesc, deserializer);
        }
        return deserializer;
    }

    public JsonDeserializer<Object> buildThrowableDeserializer(DeserializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        Iterator i$;
        BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(beanDesc);
        builder.setCreators(findDeserializerCreators(config, beanDesc));
        addBeanProps(config, beanDesc, builder);
        AnnotatedMethod am = beanDesc.findMethod("initCause", INIT_CAUSE_PARAMS);
        if (am != null) {
            SettableBeanProperty prop = constructSettableProperty(config, beanDesc, "cause", am);
            if (prop != null) {
                builder.addProperty(prop);
            }
        }
        builder.addIgnorable("localizedMessage");
        builder.addIgnorable("message");
        if (this._factoryConfig.hasDeserializerModifiers()) {
            i$ = this._factoryConfig.deserializerModifiers().iterator();
            while (i$.hasNext()) {
                builder = ((BeanDeserializerModifier) i$.next()).updateBuilder(config, beanDesc, builder);
            }
        }
        JsonDeserializer<?> deserializer = builder.build(property);
        if (deserializer instanceof BeanDeserializer) {
            deserializer = new ThrowableDeserializer((BeanDeserializer) deserializer);
        }
        if (this._factoryConfig.hasDeserializerModifiers()) {
            i$ = this._factoryConfig.deserializerModifiers().iterator();
            while (i$.hasNext()) {
                deserializer = i$.next().modifyDeserializer(config, beanDesc, deserializer);
            }
        }
        return deserializer;
    }

    protected SettableAnyProperty constructAnySetter(DeserializationConfig config, BasicBeanDescription beanDesc, AnnotatedMember setter) throws JsonMappingException {
        if (config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            setter.fixAccess();
        }
        JavaType type = beanDesc.bindingsForBeanType().resolveType(setter.getParameterType(1));
        Std property = new Std(setter.getName(), type, beanDesc.getClassAnnotations(), setter);
        type = resolveType(config, beanDesc, type, setter, property);
        JsonDeserializer<Object> deser = findDeserializerFromAnnotation(config, setter, property);
        if (deser == null) {
            return new SettableAnyProperty(property, setter, modifyTypeByAnnotation(config, setter, type, property.getName()));
        }
        SettableAnyProperty prop = new SettableAnyProperty(property, setter, type);
        prop.setValueDeserializer(deser);
        return prop;
    }

    protected BeanDeserializerBuilder constructBeanDeserializerBuilder(BasicBeanDescription beanDesc) {
        return new BeanDeserializerBuilder(beanDesc);
    }

    protected SettableBeanProperty constructSettableProperty(DeserializationConfig config, BasicBeanDescription beanDesc, String name, Annotated field) throws JsonMappingException {
        if (config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            field.fixAccess();
        }
        JavaType t0 = beanDesc.bindingsForBeanType().resolveType(field.getGenericType());
        Std property = new Std(name, t0, beanDesc.getClassAnnotations(), field);
        JavaType type = resolveType(config, beanDesc, t0, field, property);
        if (type != t0) {
            property = property.withType(type);
        }
        JsonDeserializer<Object> propDeser = findDeserializerFromAnnotation(config, field, property);
        type = modifyTypeByAnnotation(config, field, type, name);
        SettableBeanProperty prop = new FieldProperty(name, type, (TypeDeserializer) type.getTypeHandler(), beanDesc.getClassAnnotations(), field);
        if (propDeser != null) {
            prop.setValueDeserializer(propDeser);
        }
        ReferenceProperty ref = config.getAnnotationIntrospector().findReferenceType(field);
        if (ref != null && ref.isManagedReference()) {
            prop.setManagedReferenceName(ref.getName());
        }
        return prop;
    }

    protected SettableBeanProperty constructSettableProperty(DeserializationConfig config, BasicBeanDescription beanDesc, String name, Annotated setter) throws JsonMappingException {
        if (config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            setter.fixAccess();
        }
        JavaType t0 = beanDesc.bindingsForBeanType().resolveType(setter.getParameterType(0));
        Std property = new Std(name, t0, beanDesc.getClassAnnotations(), setter);
        JavaType type = resolveType(config, beanDesc, t0, setter, property);
        if (type != t0) {
            property = property.withType(type);
        }
        JsonDeserializer<Object> propDeser = findDeserializerFromAnnotation(config, setter, property);
        type = modifyTypeByAnnotation(config, setter, type, name);
        SettableBeanProperty prop = new MethodProperty(name, type, (TypeDeserializer) type.getTypeHandler(), beanDesc.getClassAnnotations(), setter);
        if (propDeser != null) {
            prop.setValueDeserializer(propDeser);
        }
        ReferenceProperty ref = config.getAnnotationIntrospector().findReferenceType(setter);
        if (ref != null && ref.isManagedReference()) {
            prop.setManagedReferenceName(ref.getName());
        }
        return prop;
    }

    protected SettableBeanProperty constructSetterlessProperty(DeserializationConfig config, BasicBeanDescription beanDesc, String name, AnnotatedMethod getter) throws JsonMappingException {
        if (config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            getter.fixAccess();
        }
        JavaType type = getter.getType(beanDesc.bindingsForBeanType());
        JsonDeserializer<Object> propDeser = findDeserializerFromAnnotation(config, getter, new Std(name, type, beanDesc.getClassAnnotations(), getter));
        type = modifyTypeByAnnotation(config, getter, type, name);
        SettableBeanProperty prop = new SetterlessProperty(name, type, (TypeDeserializer) type.getTypeHandler(), beanDesc.getClassAnnotations(), getter);
        if (propDeser != null) {
            prop.setValueDeserializer(propDeser);
        }
        return prop;
    }

    public JsonDeserializer<Object> createBeanDeserializer(DeserializationConfig config, DeserializerProvider p, JavaType type, BeanProperty property) throws JsonMappingException {
        if (type.isAbstract()) {
            type = mapAbstractType(config, type);
        }
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspect(type);
        JsonDeserializer<Object> ad = findDeserializerFromAnnotation(config, beanDesc.getClassInfo(), property);
        if (ad != null) {
            return ad;
        }
        JavaType newType = modifyTypeByAnnotation(config, beanDesc.getClassInfo(), type, null);
        if (newType.getRawClass() != type.getRawClass()) {
            type = newType;
            beanDesc = config.introspect(type);
        }
        JsonDeserializer<Object> custom = _findCustomBeanDeserializer(type, config, p, beanDesc, property);
        if (custom != null) {
            return custom;
        }
        if (type.isThrowable()) {
            return buildThrowableDeserializer(config, type, beanDesc, property);
        }
        if (type.isAbstract()) {
            JavaType concreteType = materializeAbstractType(config, beanDesc);
            if (concreteType != null) {
                return buildBeanDeserializer(config, concreteType, config.introspect(concreteType), property);
            }
        }
        JsonDeserializer<Object> deser = findStdBeanDeserializer(config, p, type, property);
        if (deser != null) {
            return deser;
        }
        return !isPotentialBeanType(type.getRawClass()) ? null : buildBeanDeserializer(config, type, beanDesc, property);
    }

    public KeyDeserializer createKeyDeserializer(DeserializationConfig config, JavaType type, BeanProperty property) throws JsonMappingException {
        if (this._factoryConfig.hasKeyDeserializers()) {
            BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspectClassAnnotations(type.getRawClass());
            Iterator i$ = this._factoryConfig.keyDeserializers().iterator();
            while (i$.hasNext()) {
                KeyDeserializer deser = ((KeyDeserializers) i$.next()).findKeyDeserializer(type, config, beanDesc, property);
                if (deser != null) {
                    return deser;
                }
            }
        }
        return null;
    }

    protected CreatorContainer findDeserializerCreators(DeserializationConfig config, BasicBeanDescription beanDesc) throws JsonMappingException {
        boolean fixAccess = config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS);
        CreatorContainer creators = new CreatorContainer(beanDesc, fixAccess);
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        if (beanDesc.getType().isConcrete()) {
            Constructor<?> defaultCtor = beanDesc.findDefaultConstructor();
            if (defaultCtor != null) {
                if (fixAccess) {
                    ClassUtil.checkAndFixAccess(defaultCtor);
                }
                creators.setDefaultConstructor(defaultCtor);
            }
        }
        VisibilityChecker<?> vchecker = config.getDefaultVisibilityChecker();
        if (!config.isEnabled(Feature.AUTO_DETECT_CREATORS)) {
            vchecker = vchecker.withCreatorVisibility(Visibility.NONE);
        }
        vchecker = config.getAnnotationIntrospector().findAutoDetectVisibility(beanDesc.getClassInfo(), vchecker);
        _addDeserializerConstructors(config, beanDesc, vchecker, intr, creators);
        _addDeserializerFactoryMethods(config, beanDesc, vchecker, intr, creators);
        return creators;
    }

    public final Config getConfig() {
        return this._factoryConfig;
    }

    protected boolean isIgnorableType(DeserializationConfig config, BasicBeanDescription beanDesc, Class<?> type, Map<Class<?>, Boolean> ignoredTypes) {
        Boolean status = (Boolean) ignoredTypes.get(type);
        if (status == null) {
            status = config.getAnnotationIntrospector().isIgnorableType(((BasicBeanDescription) config.introspectClassAnnotations(type)).getClassInfo());
            if (status == null) {
                status = Boolean.FALSE;
            }
        }
        return status.booleanValue();
    }

    protected boolean isPotentialBeanType(Class<?> type) {
        String typeStr = ClassUtil.canBeABeanType(type);
        if (typeStr != null) {
            throw new IllegalArgumentException("Can not deserialize Class " + type.getName() + " (of type " + typeStr + ") as a Bean");
        } else if (ClassUtil.isProxyType(type)) {
            throw new IllegalArgumentException("Can not deserialize Proxy class " + type.getName() + " as a Bean");
        } else {
            typeStr = ClassUtil.isLocalType(type);
            if (typeStr == null) {
                return true;
            }
            throw new IllegalArgumentException("Can not deserialize Class " + type.getName() + " (of type " + typeStr + ") as a Bean");
        }
    }

    protected JavaType mapAbstractType(DeserializationConfig config, JavaType type) throws JsonMappingException {
        while (true) {
            JavaType next = _mapAbstractType2(config, type);
            if (next == null) {
                return type;
            }
            Class<?> prevCls = type.getRawClass();
            Class<?> nextCls = next.getRawClass();
            if (prevCls != nextCls && prevCls.isAssignableFrom(nextCls)) {
                type = next;
            }
            throw new IllegalArgumentException("Invalid abstract type resolution from " + type + " to " + next + ": latter is not a subtype of former");
        }
    }

    protected JavaType materializeAbstractType(DeserializationConfig config, BasicBeanDescription beanDesc) throws JsonMappingException {
        AbstractTypeResolver resolver = config.getAbstractTypeResolver();
        if (resolver == null && !this._factoryConfig.hasAbstractTypeResolvers()) {
            return null;
        }
        JavaType abstractType = beanDesc.getType();
        if (config.getAnnotationIntrospector().findTypeResolver(config, beanDesc.getClassInfo(), abstractType) != null) {
            return null;
        }
        JavaType concrete;
        if (resolver != null) {
            concrete = resolver.resolveAbstractType(config, abstractType);
            if (concrete != null) {
                return concrete;
            }
        }
        Iterator i$ = this._factoryConfig.abstractTypeResolvers().iterator();
        while (i$.hasNext()) {
            concrete = ((AbstractTypeResolver) i$.next()).resolveAbstractType(config, abstractType);
            if (concrete != null) {
                return concrete;
            }
        }
        return null;
    }

    public DeserializerFactory withConfig(Config config) {
        if (this._factoryConfig == config) {
            return this;
        }
        if (getClass() != BeanDeserializerFactory.class) {
            throw new IllegalStateException("Subtype of BeanDeserializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalDeserializers': can not instantiate subtype with " + "additional deserializer definitions");
        }
        this(config);
        return this;
    }
}