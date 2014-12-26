package org.codehaus.jackson.annotate;

import com.wmt.data.LocalAudioAll;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonAutoDetect {

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$annotate$JsonAutoDetect$Visibility;

        static {
            $SwitchMap$org$codehaus$jackson$annotate$JsonAutoDetect$Visibility = new int[org.codehaus.jackson.annotate.JsonAutoDetect.Visibility.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$annotate$JsonAutoDetect$Visibility[org.codehaus.jackson.annotate.JsonAutoDetect.Visibility.ANY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$annotate$JsonAutoDetect$Visibility[org.codehaus.jackson.annotate.JsonAutoDetect.Visibility.NONE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$annotate$JsonAutoDetect$Visibility[org.codehaus.jackson.annotate.JsonAutoDetect.Visibility.NON_PRIVATE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$annotate$JsonAutoDetect$Visibility[org.codehaus.jackson.annotate.JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            $SwitchMap$org$codehaus$jackson$annotate$JsonAutoDetect$Visibility[org.codehaus.jackson.annotate.JsonAutoDetect.Visibility.PUBLIC_ONLY.ordinal()] = 5;
        }
    }

    public enum Visibility {
        ANY,
        NON_PRIVATE,
        PROTECTED_AND_PUBLIC,
        PUBLIC_ONLY,
        NONE,
        DEFAULT;

        public boolean isVisible(Member m) {
            switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$annotate$JsonAutoDetect$Visibility[ordinal()]) {
                case LocalAudioAll.SORT_BY_DATE:
                    return true;
                case ClassWriter.COMPUTE_FRAMES:
                    return false;
                case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                    return !Modifier.isPrivate(m.getModifiers());
                case JsonWriteContext.STATUS_EXPECT_VALUE:
                    if (Modifier.isProtected(m.getModifiers())) {
                        return true;
                    }
                    return Modifier.isPublic(m.getModifiers());
                case JsonWriteContext.STATUS_EXPECT_NAME:
                    return Modifier.isPublic(m.getModifiers());
                default:
                    return false;
            }
        }
    }

    Visibility creatorVisibility() default Visibility.DEFAULT;

    Visibility fieldVisibility() default Visibility.DEFAULT;

    Visibility getterVisibility() default Visibility.DEFAULT;

    Visibility isGetterVisibility() default Visibility.DEFAULT;

    Visibility setterVisibility() default Visibility.DEFAULT;

    JsonMethod[] value() default {JsonMethod.ALL};
}