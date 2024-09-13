package com.puvn.bean.container.context;

import com.google.common.reflect.ClassPath;
import com.puvn.bean.container.annotation.RepositoryBean;
import com.puvn.bean.container.annotation.ServiceBean;
import com.puvn.bean.container.exception.BeanContainerError;
import com.puvn.bean.container.exception.ContainerContextInitializerException;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Prepares a beanMap based on provided package names. Scans packages and adds them into a map which will be used
 * for the actual application context
 */
public class ContainerContextInitializer {

    private static final Logger LOGGER = Logger.getLogger(ContainerContextInitializer.class.getName());

    private final Map<String, Class<?>> beanMap = new HashMap<>();

    public ContainerContextInitializer(String[] packageNames) {
        try {
            constructContext(packageNames);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
            throw new ContainerContextInitializerException(BeanContainerError.CLASS_LOADING_ERROR);
        }
    }

    public Map<String, Class<?>> getContext() {
        return beanMap;
    }

    private void constructContext(String[] packageNames) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ClassPath classPath = ClassPath.from(classLoader);
        for (String pkg : packageNames) {
            Set<ClassPath.ClassInfo> classesInPackage = classPath.getTopLevelClassesRecursive(pkg);

            for (ClassPath.ClassInfo classInfo : classesInPackage) {
                try {
                    Class<?> clazz = classInfo.load();
                    if (isAnnotatedWith(clazz, ServiceBean.class) && isAnnotatedWith(clazz, RepositoryBean.class)) {
                        String errorMessage = String.format("Class %s has invalid definition", clazz.getName());
                        LOGGER.severe(errorMessage);
                        throw new ContainerContextInitializerException(
                                BeanContainerError.MULTIPLE_BEAN_ANNOTATION_ERROR
                        );
                    }
                    if (isAnnotatedWith(clazz, ServiceBean.class) || isAnnotatedWith(clazz, RepositoryBean.class)) {
                        beanMap.put(clazz.getName(), clazz);
                    }
                } catch (Throwable t) {
                    String errorMessage =
                            String.format("Error processing class %s: %s", classInfo.getName(), t.getMessage());
                    LOGGER.severe(errorMessage);
                    throw t;
                }
            }
        }
    }

    private boolean isAnnotatedWith(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        return clazz.isAnnotationPresent(annotationClass);
    }

}
