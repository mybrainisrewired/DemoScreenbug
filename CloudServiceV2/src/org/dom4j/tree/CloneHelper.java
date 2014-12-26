package org.dom4j.tree;

import com.clouds.util.SMSConstant;
import java.lang.reflect.Field;

final class CloneHelper {
    CloneHelper() {
    }

    public static <T> void setFinalContent(Class<T> clazz, T object) {
        setFinalField(clazz, object, SMSConstant.S_content, new LazyList());
    }

    public static <T> void setFinalField(Class<T> clazz, T object, String fieldName, Object value) {
        try {
            Field headerField = clazz.getDeclaredField(fieldName);
            headerField.setAccessible(true);
            headerField.set(object, value);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e2) {
            throw new RuntimeException(e2);
        }
    }

    public static <T> void setFinalLazyList(Class<T> clazz, T object, String fieldName) {
        setFinalField(clazz, object, fieldName, new LazyList());
    }
}