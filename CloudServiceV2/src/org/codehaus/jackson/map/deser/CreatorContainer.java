package org.codehaus.jackson.map.deser;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import org.codehaus.jackson.map.introspect.AnnotatedConstructor;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.util.ClassUtil;

public class CreatorContainer {
    final BasicBeanDescription _beanDesc;
    final boolean _canFixAccess;
    protected Constructor<?> _defaultConstructor;
    AnnotatedConstructor _delegatingConstructor;
    AnnotatedMethod _delegatingFactory;
    AnnotatedConstructor _intConstructor;
    AnnotatedMethod _intFactory;
    AnnotatedConstructor _longConstructor;
    AnnotatedMethod _longFactory;
    AnnotatedConstructor _propertyBasedConstructor;
    SettableBeanProperty[] _propertyBasedConstructorProperties;
    AnnotatedMethod _propertyBasedFactory;
    SettableBeanProperty[] _propertyBasedFactoryProperties;
    AnnotatedConstructor _strConstructor;
    AnnotatedMethod _strFactory;

    public CreatorContainer(BasicBeanDescription beanDesc, boolean canFixAccess) {
        this._propertyBasedFactoryProperties = null;
        this._propertyBasedConstructorProperties = null;
        this._beanDesc = beanDesc;
        this._canFixAccess = canFixAccess;
    }

    public void addDelegatingConstructor(AnnotatedConstructor ctor) {
        this._delegatingConstructor = verifyNonDup(ctor, this._delegatingConstructor, "long");
    }

    public void addDelegatingFactory(AnnotatedMethod factory) {
        this._delegatingFactory = verifyNonDup(factory, this._delegatingFactory, "long");
    }

    public void addIntConstructor(AnnotatedConstructor ctor) {
        this._intConstructor = verifyNonDup(ctor, this._intConstructor, "int");
    }

    public void addIntFactory(AnnotatedMethod factory) {
        this._intFactory = verifyNonDup(factory, this._intFactory, "int");
    }

    public void addLongConstructor(AnnotatedConstructor ctor) {
        this._longConstructor = verifyNonDup(ctor, this._longConstructor, "long");
    }

    public void addLongFactory(AnnotatedMethod factory) {
        this._longFactory = verifyNonDup(factory, this._longFactory, "long");
    }

    public void addPropertyConstructor(AnnotatedConstructor ctor, SettableBeanProperty[] properties) {
        this._propertyBasedConstructor = verifyNonDup(ctor, this._propertyBasedConstructor, "property-based");
        if (properties.length > 1) {
            HashMap<String, Integer> names = new HashMap();
            int i = 0;
            int len = properties.length;
            while (i < len) {
                String name = properties[i].getName();
                Integer old = (Integer) names.put(name, Integer.valueOf(i));
                if (old != null) {
                    throw new IllegalArgumentException("Duplicate creator property \"" + name + "\" (index " + old + " vs " + i + ")");
                }
                i++;
            }
        }
        this._propertyBasedConstructorProperties = properties;
    }

    public void addPropertyFactory(AnnotatedMethod factory, SettableBeanProperty[] properties) {
        this._propertyBasedFactory = verifyNonDup(factory, this._propertyBasedFactory, "property-based");
        this._propertyBasedFactoryProperties = properties;
    }

    public void addStringConstructor(AnnotatedConstructor ctor) {
        this._strConstructor = verifyNonDup(ctor, this._strConstructor, "String");
    }

    public void addStringFactory(AnnotatedMethod factory) {
        this._strFactory = verifyNonDup(factory, this._strFactory, "String");
    }

    public Delegating delegatingCreator() {
        return (this._delegatingConstructor == null && this._delegatingFactory == null) ? null : new Delegating(this._beanDesc, this._delegatingConstructor, this._delegatingFactory);
    }

    public Constructor<?> getDefaultConstructor() {
        return this._defaultConstructor;
    }

    public NumberBased numberCreator() {
        return (this._intConstructor == null && this._intFactory == null && this._longConstructor == null && this._longFactory == null) ? null : new NumberBased(this._beanDesc.getBeanClass(), this._intConstructor, this._intFactory, this._longConstructor, this._longFactory);
    }

    public PropertyBased propertyBasedCreator() {
        return (this._propertyBasedConstructor == null && this._propertyBasedFactory == null) ? null : new PropertyBased(this._propertyBasedConstructor, this._propertyBasedConstructorProperties, this._propertyBasedFactory, this._propertyBasedFactoryProperties);
    }

    public void setDefaultConstructor(Constructor<?> ctor) {
        this._defaultConstructor = ctor;
    }

    public StringBased stringCreator() {
        return (this._strConstructor == null && this._strFactory == null) ? null : new StringBased(this._beanDesc.getBeanClass(), this._strConstructor, this._strFactory);
    }

    protected AnnotatedConstructor verifyNonDup(AnnotatedConstructor newOne, AnnotatedConstructor oldOne, String type) {
        if (oldOne != null) {
            throw new IllegalArgumentException("Conflicting " + type + " constructors: already had " + oldOne + ", encountered " + newOne);
        }
        if (this._canFixAccess) {
            ClassUtil.checkAndFixAccess(newOne.getAnnotated());
        }
        return newOne;
    }

    protected AnnotatedMethod verifyNonDup(AnnotatedMethod newOne, AnnotatedMethod oldOne, String type) {
        if (oldOne != null) {
            throw new IllegalArgumentException("Conflicting " + type + " factory methods: already had " + oldOne + ", encountered " + newOne);
        }
        if (this._canFixAccess) {
            ClassUtil.checkAndFixAccess(newOne.getAnnotated());
        }
        return newOne;
    }
}