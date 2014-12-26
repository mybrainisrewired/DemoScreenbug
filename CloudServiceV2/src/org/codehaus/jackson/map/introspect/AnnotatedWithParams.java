package org.codehaus.jackson.map.introspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

public abstract class AnnotatedWithParams extends AnnotatedMember {
    protected final AnnotationMap _annotations;
    protected final AnnotationMap[] _paramAnnotations;

    protected AnnotatedWithParams(AnnotationMap classAnn, AnnotationMap[] paramAnnotations) {
        this._annotations = classAnn;
        this._paramAnnotations = paramAnnotations;
    }

    public final void addIfNotPresent(Annotation a) {
        this._annotations.addIfNotPresent(a);
    }

    public final void addOrOverride(Annotation a) {
        this._annotations.add(a);
    }

    public final void addOrOverrideParam(int paramIndex, Annotation a) {
        AnnotationMap old = this._paramAnnotations[paramIndex];
        if (old == null) {
            old = new AnnotationMap();
            this._paramAnnotations[paramIndex] = old;
        }
        old.add(a);
    }

    public final <A extends Annotation> A getAnnotation(Class<A> acls) {
        return this._annotations.get(acls);
    }

    public final int getAnnotationCount() {
        return this._annotations.size();
    }

    public abstract AnnotatedParameter getParameter(int i);

    public final AnnotationMap getParameterAnnotations(int index) {
        return (this._paramAnnotations == null || index < 0 || index > this._paramAnnotations.length) ? null : this._paramAnnotations[index];
    }

    public abstract Class<?> getParameterClass(int i);

    public abstract int getParameterCount();

    public abstract Type getParameterType(int i);

    protected JavaType getType(TypeBindings bindings, TypeVariable<?>[] typeParams) {
        if (typeParams != null && typeParams.length > 0) {
            bindings = bindings.childInstance();
            TypeVariable<?>[] arr$ = typeParams;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                TypeVariable<?> var = arr$[i$];
                bindings._addPlaceholder(var.getName());
                Type lowerBound = var.getBounds()[0];
                bindings.addBinding(var.getName(), lowerBound == null ? TypeFactory.unknownType() : bindings.resolveType(lowerBound));
                i$++;
            }
        }
        return bindings.resolveType(getGenericType());
    }

    public final JavaType resolveParameterType(int index, TypeBindings bindings) {
        return bindings.resolveType(getParameterType(index));
    }
}