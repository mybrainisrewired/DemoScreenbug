package org.codehaus.jackson.map.introspect;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;

public interface VisibilityChecker<T extends VisibilityChecker<T>> {

    @JsonAutoDetect(creatorVisibility = Visibility.ANY, fieldVisibility = Visibility.PUBLIC_ONLY, getterVisibility = Visibility.PUBLIC_ONLY, isGetterVisibility = Visibility.PUBLIC_ONLY, setterVisibility = Visibility.ANY)
    public static class Std implements VisibilityChecker<org.codehaus.jackson.map.introspect.VisibilityChecker.Std> {
        protected static final org.codehaus.jackson.map.introspect.VisibilityChecker.Std DEFAULT;
        protected final Visibility _creatorMinLevel;
        protected final Visibility _fieldMinLevel;
        protected final Visibility _getterMinLevel;
        protected final Visibility _isGetterMinLevel;
        protected final Visibility _setterMinLevel;

        static {
            DEFAULT = new org.codehaus.jackson.map.introspect.VisibilityChecker.Std((JsonAutoDetect) org.codehaus.jackson.map.introspect.VisibilityChecker.Std.class.getAnnotation(JsonAutoDetect.class));
        }

        public Std(Visibility getter, Visibility isGetter, Visibility setter, Visibility creator, Visibility field) {
            this._getterMinLevel = getter;
            this._isGetterMinLevel = isGetter;
            this._setterMinLevel = setter;
            this._creatorMinLevel = creator;
            this._fieldMinLevel = field;
        }

        public Std(JsonAutoDetect ann) {
            JsonMethod[] incl = ann.value();
            this._getterMinLevel = hasMethod(incl, JsonMethod.GETTER) ? ann.getterVisibility() : Visibility.NONE;
            this._isGetterMinLevel = hasMethod(incl, JsonMethod.IS_GETTER) ? ann.isGetterVisibility() : Visibility.NONE;
            this._setterMinLevel = hasMethod(incl, JsonMethod.SETTER) ? ann.setterVisibility() : Visibility.NONE;
            this._creatorMinLevel = hasMethod(incl, JsonMethod.CREATOR) ? ann.creatorVisibility() : Visibility.NONE;
            this._fieldMinLevel = hasMethod(incl, JsonMethod.FIELD) ? ann.fieldVisibility() : Visibility.NONE;
        }

        public static org.codehaus.jackson.map.introspect.VisibilityChecker.Std defaultInstance() {
            return DEFAULT;
        }

        private static boolean hasMethod(JsonMethod[] methods, JsonMethod method) {
            JsonMethod[] arr$ = methods;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                JsonMethod curr = arr$[i$];
                if (curr != method && curr != JsonMethod.ALL) {
                    i$++;
                }
                return true;
            }
            return false;
        }

        public boolean isCreatorVisible(Member m) {
            return this._creatorMinLevel.isVisible(m);
        }

        public boolean isCreatorVisible(AnnotatedMember m) {
            return isCreatorVisible(m.getMember());
        }

        public boolean isFieldVisible(Field f) {
            return this._fieldMinLevel.isVisible(f);
        }

        public boolean isFieldVisible(AnnotatedField f) {
            return isFieldVisible(f.getAnnotated());
        }

        public boolean isGetterVisible(Method m) {
            return this._getterMinLevel.isVisible(m);
        }

        public boolean isGetterVisible(AnnotatedMethod m) {
            return isGetterVisible(m.getAnnotated());
        }

        public boolean isIsGetterVisible(Method m) {
            return this._isGetterMinLevel.isVisible(m);
        }

        public boolean isIsGetterVisible(AnnotatedMethod m) {
            return isIsGetterVisible(m.getAnnotated());
        }

        public boolean isSetterVisible(Method m) {
            return this._setterMinLevel.isVisible(m);
        }

        public boolean isSetterVisible(AnnotatedMethod m) {
            return isSetterVisible(m.getAnnotated());
        }

        public String toString() {
            return new StringBuilder("[Visibility:").append(" getter: ").append(this._getterMinLevel).append(", isGetter: ").append(this._isGetterMinLevel).append(", setter: ").append(this._setterMinLevel).append(", creator: ").append(this._creatorMinLevel).append(", field: ").append(this._fieldMinLevel).append("]").toString();
        }

        public org.codehaus.jackson.map.introspect.VisibilityChecker.Std with(JsonAutoDetect ann) {
            if (ann == null) {
                return this;
            }
            org.codehaus.jackson.map.introspect.VisibilityChecker.Std curr = this;
            JsonMethod[] incl = ann.value();
            return withGetterVisibility(hasMethod(incl, JsonMethod.GETTER) ? ann.getterVisibility() : Visibility.NONE).withIsGetterVisibility(hasMethod(incl, JsonMethod.IS_GETTER) ? ann.isGetterVisibility() : Visibility.NONE).withSetterVisibility(hasMethod(incl, JsonMethod.SETTER) ? ann.setterVisibility() : Visibility.NONE).withCreatorVisibility(hasMethod(incl, JsonMethod.CREATOR) ? ann.creatorVisibility() : Visibility.NONE).withFieldVisibility(hasMethod(incl, JsonMethod.FIELD) ? ann.fieldVisibility() : Visibility.NONE);
        }

        public org.codehaus.jackson.map.introspect.VisibilityChecker.Std withCreatorVisibility(Visibility v) {
            if (v == Visibility.DEFAULT) {
                v = DEFAULT._creatorMinLevel;
            }
            if (this._creatorMinLevel == v) {
                return this;
            }
            return new org.codehaus.jackson.map.introspect.VisibilityChecker.Std(this._getterMinLevel, this._isGetterMinLevel, this._setterMinLevel, v, this._fieldMinLevel);
        }

        public org.codehaus.jackson.map.introspect.VisibilityChecker.Std withFieldVisibility(Visibility v) {
            if (v == Visibility.DEFAULT) {
                v = DEFAULT._fieldMinLevel;
            }
            return this._fieldMinLevel == v ? this : new org.codehaus.jackson.map.introspect.VisibilityChecker.Std(this._getterMinLevel, this._isGetterMinLevel, this._setterMinLevel, this._creatorMinLevel, v);
        }

        public org.codehaus.jackson.map.introspect.VisibilityChecker.Std withGetterVisibility(Visibility v) {
            if (v == Visibility.DEFAULT) {
                v = DEFAULT._getterMinLevel;
            }
            if (this._getterMinLevel == v) {
                return this;
            }
            return new org.codehaus.jackson.map.introspect.VisibilityChecker.Std(v, this._isGetterMinLevel, this._setterMinLevel, this._creatorMinLevel, this._fieldMinLevel);
        }

        public org.codehaus.jackson.map.introspect.VisibilityChecker.Std withIsGetterVisibility(Visibility v) {
            if (v == Visibility.DEFAULT) {
                v = DEFAULT._isGetterMinLevel;
            }
            if (this._isGetterMinLevel == v) {
                return this;
            }
            return new org.codehaus.jackson.map.introspect.VisibilityChecker.Std(this._getterMinLevel, v, this._setterMinLevel, this._creatorMinLevel, this._fieldMinLevel);
        }

        public org.codehaus.jackson.map.introspect.VisibilityChecker.Std withSetterVisibility(Visibility v) {
            if (v == Visibility.DEFAULT) {
                v = DEFAULT._setterMinLevel;
            }
            if (this._setterMinLevel == v) {
                return this;
            }
            return new org.codehaus.jackson.map.introspect.VisibilityChecker.Std(this._getterMinLevel, this._isGetterMinLevel, v, this._creatorMinLevel, this._fieldMinLevel);
        }
    }

    boolean isCreatorVisible(Member member);

    boolean isCreatorVisible(AnnotatedMember annotatedMember);

    boolean isFieldVisible(Field field);

    boolean isFieldVisible(AnnotatedField annotatedField);

    boolean isGetterVisible(Method method);

    boolean isGetterVisible(AnnotatedMethod annotatedMethod);

    boolean isIsGetterVisible(Method method);

    boolean isIsGetterVisible(AnnotatedMethod annotatedMethod);

    boolean isSetterVisible(Method method);

    boolean isSetterVisible(AnnotatedMethod annotatedMethod);

    T with(JsonAutoDetect jsonAutoDetect);

    T withCreatorVisibility(Visibility visibility);

    T withFieldVisibility(Visibility visibility);

    T withGetterVisibility(Visibility visibility);

    T withIsGetterVisibility(Visibility visibility);

    T withSetterVisibility(Visibility visibility);
}