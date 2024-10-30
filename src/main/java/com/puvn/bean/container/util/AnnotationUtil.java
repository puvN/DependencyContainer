package com.puvn.bean.container.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationUtil {

    public static boolean isAnnotatedWith(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        return clazz.isAnnotationPresent(annotationClass);
    }

    public static boolean isAnnotatedWith(Method method, Class<? extends Annotation> annotationClass) {
        return method.isAnnotationPresent(annotationClass);
    }

}
