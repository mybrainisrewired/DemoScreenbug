package org.codehaus.jackson.mrbean;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BeanUtil {
    private static void _addSuperTypes(Class<?> cls, Class<?> endBefore, Collection<Class<?>> result, boolean addClassItself) {
        if (cls != endBefore && cls != null && cls != Object.class) {
            if (addClassItself && !result.contains(cls)) {
                result.add(cls);
                Class[] arr$ = cls.getInterfaces();
                int len$ = arr$.length;
                int i$ = 0;
                while (i$ < len$) {
                    _addSuperTypes(arr$[i$], endBefore, result, true);
                    i$++;
                }
                _addSuperTypes(cls.getSuperclass(), endBefore, result, true);
            }
        }
    }

    public static List<Class<?>> findSuperTypes(Class<?> cls, Class<?> endBefore) {
        return findSuperTypes(cls, endBefore, new ArrayList());
    }

    public static List<Class<?>> findSuperTypes(Class<?> cls, Class<?> endBefore, List<Class<?>> result) {
        _addSuperTypes(cls, endBefore, result, false);
        return result;
    }

    protected static boolean isConcrete(Member member) {
        return (member.getModifiers() & 1536) == 0;
    }
}