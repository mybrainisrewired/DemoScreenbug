package org.codehaus.jackson.mrbean;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.MethodVisitor;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.org.objectweb.asm.Type;
import org.codehaus.jackson.type.JavaType;

public class BeanBuilder {
    protected Map<String, Property> _beanProperties;
    protected final Class<?> _implementedType;
    protected final TypeFactory _typeFactory;
    protected LinkedHashMap<String, Method> _unsupportedMethods;

    private static class Property {
        protected final String _fieldName;
        protected Method _getter;
        protected final String _name;
        protected Method _setter;

        public Property(String name) {
            this._name = name;
            this._fieldName = "_" + name;
        }

        private TypeDescription getterType(TypeFactory tf) {
            return new TypeDescription(tf.constructType(this._getter.getGenericReturnType(), this._getter.getDeclaringClass()));
        }

        private TypeDescription setterType(TypeFactory tf) {
            return new TypeDescription(tf.constructType(this._setter.getGenericParameterTypes()[0], this._setter.getDeclaringClass()));
        }

        public String getFieldName() {
            return this._fieldName;
        }

        public Method getGetter() {
            return this._getter;
        }

        public String getName() {
            return this._name;
        }

        public Method getSetter() {
            return this._setter;
        }

        public boolean hasConcreteGetter() {
            return this._getter != null && BeanUtil.isConcrete(this._getter);
        }

        public boolean hasConcreteSetter() {
            return this._setter != null && BeanUtil.isConcrete(this._setter);
        }

        public TypeDescription selectType(TypeFactory tf) {
            if (this._getter == null) {
                return setterType(tf);
            }
            if (this._setter == null) {
                return getterType(tf);
            }
            TypeDescription st = setterType(tf);
            TypeDescription gt = getterType(tf);
            TypeDescription specificType = TypeDescription.moreSpecificType(st, gt);
            if (specificType != null) {
                return specificType;
            }
            throw new IllegalArgumentException("Invalid property '" + getName() + "': incompatible types for getter/setter (" + gt + " vs " + st + ")");
        }

        public void setGetter(Method m) {
            this._getter = m;
        }

        public void setSetter(Method m) {
            this._setter = m;
        }
    }

    private static class TypeDescription {
        private final Type _asmType;
        private JavaType _jacksonType;

        public TypeDescription(JavaType type) {
            this._jacksonType = type;
            this._asmType = Type.getType(type.getRawClass());
        }

        public static TypeDescription moreSpecificType(TypeDescription desc1, TypeDescription desc2) {
            Class<?> c1 = desc1.getRawClass();
            Class<?> c2 = desc2.getRawClass();
            if (c1.isAssignableFrom(c2)) {
                return desc2;
            }
            return c2.isAssignableFrom(c1) ? desc1 : null;
        }

        public String erasedSignature() {
            return this._jacksonType.getErasedSignature();
        }

        public String genericSignature() {
            return this._jacksonType.getGenericSignature();
        }

        public int getLoadOpcode() {
            return this._asmType.getOpcode(Opcodes.ILOAD);
        }

        public Class<?> getRawClass() {
            return this._jacksonType.getRawClass();
        }

        public int getReturnOpcode() {
            return this._asmType.getOpcode(Opcodes.IRETURN);
        }

        public boolean hasGenerics() {
            return this._jacksonType.hasGenericTypes();
        }

        public String toString() {
            return this._jacksonType.toString();
        }
    }

    public BeanBuilder(DeserializationConfig config, Class<?> implType) {
        this._beanProperties = new LinkedHashMap();
        this._unsupportedMethods = new LinkedHashMap();
        this._implementedType = implType;
        this._typeFactory = config.getTypeFactory();
    }

    private void addGetter(Method m) {
        Property prop = findProperty(getPropertyName(m.getName()));
        if (prop.getGetter() == null) {
            prop.setGetter(m);
        }
    }

    private void addSetter(Method m) {
        Property prop = findProperty(getPropertyName(m.getName()));
        if (prop.getSetter() == null) {
            prop.setSetter(m);
        }
    }

    private static String buildGetterName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    private static String buildSetterName(String fieldName) {
        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    private static void createField(ClassWriter cw, Property prop, TypeDescription type) {
        String sig;
        if (type.hasGenerics()) {
            sig = type.genericSignature();
        } else {
            sig = null;
        }
        ClassWriter classWriter = cw;
        classWriter.visitField(1, prop.getFieldName(), type.erasedSignature(), sig, null).visitEnd();
    }

    private static void createGetter(ClassWriter cw, String internalClassName, Property prop, TypeDescription propertyType) {
        String desc;
        String methodName;
        String sig;
        Method getter = prop.getGetter();
        if (getter != null) {
            desc = Type.getMethodDescriptor(getter);
            methodName = getter.getName();
        } else {
            desc = "()" + propertyType.erasedSignature();
            methodName = buildGetterName(prop.getName());
        }
        if (propertyType.hasGenerics()) {
            sig = "()" + propertyType.genericSignature();
        } else {
            sig = null;
        }
        MethodVisitor mv = cw.visitMethod(1, methodName, desc, sig, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitFieldInsn(Opcodes.GETFIELD, internalClassName, prop.getFieldName(), propertyType.erasedSignature());
        mv.visitInsn(propertyType.getReturnOpcode());
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private static void createSetter(ClassWriter cw, String internalClassName, Property prop, TypeDescription propertyType) {
        String desc;
        String methodName;
        String sig;
        Method setter = prop.getSetter();
        if (setter != null) {
            desc = Type.getMethodDescriptor(setter);
            methodName = setter.getName();
        } else {
            desc = "(" + propertyType.erasedSignature() + ")V";
            methodName = buildSetterName(prop.getName());
        }
        if (propertyType.hasGenerics()) {
            sig = "(" + propertyType.genericSignature() + ")V";
        } else {
            sig = null;
        }
        MethodVisitor mv = cw.visitMethod(1, methodName, desc, sig, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(propertyType.getLoadOpcode(), 1);
        mv.visitFieldInsn(Opcodes.PUTFIELD, internalClassName, prop.getFieldName(), propertyType.erasedSignature());
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private static void createUnimplementedMethod(ClassWriter cw, String internalClassName, Method method) {
        String exceptionName = getInternalClassName(UnsupportedOperationException.class.getName());
        String sig = Type.getMethodDescriptor(method);
        String name = method.getName();
        MethodVisitor mv = cw.visitMethod(1, name, sig, null, null);
        mv.visitTypeInsn(Opcodes.NEW, exceptionName);
        mv.visitInsn(Opcodes.DUP);
        mv.visitLdcInsn("Unimplemented method '" + name + "' (not a setter/getter, could not materialize)");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, exceptionName, "<init>", "(Ljava/lang/String;)V");
        mv.visitInsn(Opcodes.ATHROW);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private Property findProperty(String propName) {
        Property prop = (Property) this._beanProperties.get(propName);
        if (prop != null) {
            return prop;
        }
        prop = new Property(propName);
        this._beanProperties.put(propName, prop);
        return prop;
    }

    private static void generateDefaultConstructor(ClassWriter cw, String superName) {
        MethodVisitor mv = cw.visitMethod(1, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, superName, "<init>", "()V");
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private static String getInternalClassName(String className) {
        return className.replace(".", "/");
    }

    private static String getPropertyName(String methodName) {
        String body = methodName.substring(methodName.startsWith("is") ? ClassWriter.COMPUTE_FRAMES : JsonWriteContext.STATUS_OK_AFTER_SPACE);
        StringBuilder sb = new StringBuilder(body);
        sb.setCharAt(0, Character.toLowerCase(body.charAt(0)));
        return sb.toString();
    }

    private static final boolean returnsBoolean(Method m) {
        Class<?> rt = m.getReturnType();
        return rt == Boolean.class || rt == Boolean.TYPE;
    }

    public byte[] build(String className) {
        String superName;
        ClassWriter cw = new ClassWriter(1);
        String internalClass = getInternalClassName(className);
        String implName = getInternalClassName(this._implementedType.getName());
        if (this._implementedType.isInterface()) {
            superName = "java/lang/Object";
            cw.visit(Opcodes.V1_5, 33, internalClass, null, superName, new String[]{implName});
        } else {
            superName = implName;
            cw.visit(Opcodes.V1_5, 33, internalClass, null, implName, null);
        }
        cw.visitSource(className + ".java", null);
        generateDefaultConstructor(cw, superName);
        Iterator i$ = this._beanProperties.values().iterator();
        while (i$.hasNext()) {
            Property prop = (Property) i$.next();
            TypeDescription type = prop.selectType(this._typeFactory);
            createField(cw, prop, type);
            if (!prop.hasConcreteGetter()) {
                createGetter(cw, internalClass, prop, type);
            }
            if (!prop.hasConcreteSetter()) {
                createSetter(cw, internalClass, prop, type);
            }
        }
        i$ = this._unsupportedMethods.values().iterator();
        while (i$.hasNext()) {
            createUnimplementedMethod(cw, internalClass, (Method) i$.next());
        }
        cw.visitEnd();
        return cw.toByteArray();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.codehaus.jackson.mrbean.BeanBuilder implement(boolean r13_failOnUnrecognized) {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.mrbean.BeanBuilder.implement(boolean):org.codehaus.jackson.mrbean.BeanBuilder");
        /*
        r12 = this;
        r5 = new java.util.ArrayList;
        r5.<init>();
        r9 = r12._implementedType;
        r5.add(r9);
        r9 = r12._implementedType;
        r10 = java.lang.Object.class;
        org.codehaus.jackson.mrbean.BeanUtil.findSuperTypes(r9, r10, r5);
        r2 = r5.iterator();
    L_0x0015:
        r9 = r2.hasNext();
        if (r9 == 0) goto L_0x0096;
    L_0x001b:
        r4 = r2.next();
        r4 = (java.lang.Class) r4;
        r1 = r4.getDeclaredMethods();
        r6 = r1.length;
        r3 = 0;
    L_0x0027:
        if (r3 >= r6) goto L_0x0015;
    L_0x0029:
        r7 = r1[r3];
        r8 = r7.getName();
        r9 = r7.getParameterTypes();
        r0 = r9.length;
        if (r0 != 0) goto L_0x0052;
    L_0x0036:
        r9 = "get";
        r9 = r8.startsWith(r9);
        if (r9 != 0) goto L_0x004c;
    L_0x003e:
        r9 = "is";
        r9 = r8.startsWith(r9);
        if (r9 == 0) goto L_0x0061;
    L_0x0046:
        r9 = returnsBoolean(r7);
        if (r9 == 0) goto L_0x0061;
    L_0x004c:
        r12.addGetter(r7);
    L_0x004f:
        r3 = r3 + 1;
        goto L_0x0027;
    L_0x0052:
        r9 = 1;
        if (r0 != r9) goto L_0x0061;
    L_0x0055:
        r9 = "set";
        r9 = r8.startsWith(r9);
        if (r9 == 0) goto L_0x0061;
    L_0x005d:
        r12.addSetter(r7);
        goto L_0x004f;
    L_0x0061:
        r9 = org.codehaus.jackson.mrbean.BeanUtil.isConcrete(r7);
        if (r9 != 0) goto L_0x004f;
    L_0x0067:
        r9 = r12._unsupportedMethods;
        r9 = r9.containsKey(r8);
        if (r9 != 0) goto L_0x004f;
    L_0x006f:
        if (r13 == 0) goto L_0x0090;
    L_0x0071:
        r9 = new java.lang.IllegalArgumentException;
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r11 = "Unrecognized abstract method '";
        r10 = r10.append(r11);
        r10 = r10.append(r8);
        r11 = "' (not a getter or setter) -- to avoid exception, disable AbstractTypeMaterializer.Feature.FAIL_ON_UNMATERIALIZED_METHOD";
        r10 = r10.append(r11);
        r10 = r10.toString();
        r9.<init>(r10);
        throw r9;
    L_0x0090:
        r9 = r12._unsupportedMethods;
        r9.put(r8, r7);
        goto L_0x004f;
    L_0x0096:
        return r12;
        */
    }
}