package org.dom4j.bean;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.dom4j.DocumentFactory;
import org.dom4j.QName;

public class BeanMetaData {
    private static final DocumentFactory DOCUMENT_FACTORY;
    protected static final Object[] NULL_ARGS;
    private static Map singletonCache;
    private Class beanClass;
    private Map nameMap;
    private PropertyDescriptor[] propertyDescriptors;
    private QName[] qNames;
    private Method[] readMethods;
    private Method[] writeMethods;

    static {
        NULL_ARGS = new Object[0];
        singletonCache = new HashMap();
        DOCUMENT_FACTORY = BeanDocumentFactory.getInstance();
    }

    public BeanMetaData(Class beanClass) {
        this.nameMap = new HashMap();
        this.beanClass = beanClass;
        if (beanClass != null) {
            try {
                this.propertyDescriptors = Introspector.getBeanInfo(beanClass).getPropertyDescriptors();
            } catch (IntrospectionException e) {
                handleException(e);
            }
        }
        if (this.propertyDescriptors == null) {
            this.propertyDescriptors = new PropertyDescriptor[0];
        }
        int size = this.propertyDescriptors.length;
        this.qNames = new QName[size];
        this.readMethods = new Method[size];
        this.writeMethods = new Method[size];
        int i = 0;
        while (i < size) {
            PropertyDescriptor propertyDescriptor = this.propertyDescriptors[i];
            String name = propertyDescriptor.getName();
            QName qName = DOCUMENT_FACTORY.createQName(name);
            this.qNames[i] = qName;
            this.readMethods[i] = propertyDescriptor.getReadMethod();
            this.writeMethods[i] = propertyDescriptor.getWriteMethod();
            Integer index = new Integer(i);
            this.nameMap.put(name, index);
            this.nameMap.put(qName, index);
            i++;
        }
    }

    public static BeanMetaData get(Class beanClass) {
        BeanMetaData answer = (BeanMetaData) singletonCache.get(beanClass);
        if (answer != null) {
            return answer;
        }
        answer = new BeanMetaData(beanClass);
        singletonCache.put(beanClass, answer);
        return answer;
    }

    public int attributeCount() {
        return this.propertyDescriptors.length;
    }

    public BeanAttributeList createAttributeList(BeanElement parent) {
        return new BeanAttributeList(parent, this);
    }

    public Object getData(int index, Object bean) {
        try {
            return this.readMethods[index].invoke(bean, NULL_ARGS);
        } catch (Exception e) {
            handleException(e);
            return null;
        }
    }

    public int getIndex(String name) {
        Integer index = (Integer) this.nameMap.get(name);
        return index != null ? index.intValue() : -1;
    }

    public int getIndex(QName qName) {
        Integer index = (Integer) this.nameMap.get(qName);
        return index != null ? index.intValue() : -1;
    }

    public QName getQName(int index) {
        return this.qNames[index];
    }

    protected void handleException(Exception e) {
    }

    public void setData(int index, Object bean, Object data) {
        try {
            this.writeMethods[index].invoke(bean, new Object[]{data});
        } catch (Exception e) {
            handleException(e);
        }
    }
}