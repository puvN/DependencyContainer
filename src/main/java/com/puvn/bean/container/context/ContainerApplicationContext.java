package com.puvn.bean.container.context;

import com.puvn.bean.container.exception.bean.BeanContainerError;
import com.puvn.bean.container.exception.context.ContainerApplicationContextException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ContainerApplicationContext extends Context {

    private static final Logger LOGGER = Logger.getLogger(ContainerApplicationContext.class.getName());

    private final Map<String, Object> beanMap = new HashMap<>();

    public ContainerApplicationContext(Map<String, Class<?>> classMap) {
        super(classMap);
        initializeContext(classMap);
    }

    public Map<String, Object> getInitializedContext() {
        return beanMap;
    }

    private void initializeContext(Map<String, Class<?>> classMap) {
        classMap.forEach((className, clazz) -> {
            if (!beanMap.containsKey(className)) {
                // Instantiate only real classes, not interfaces or abstract classes
                if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
                    try {
                        createBean(clazz);
                    } catch (Exception e) {
                        String message = String.format("Error while instantiating bean: %s", className);
                        LOGGER.severe(message);
                        throw new ContainerApplicationContextException(BeanContainerError.BEAN_CREATION_ERROR);
                    }
                }
            }
        });
    }

    private Object createBean(Class<?> clazz) throws Exception {
        Constructor<?> constructor = clazz.getConstructors()[0];
        Object[] dependencies = resolveConstructorDependencies(constructor);
        Object beanInstance = constructor.newInstance(dependencies);
        beanMap.put(clazz.getName(), beanInstance);
        return beanInstance;
    }

    private Object[] resolveConstructorDependencies(Constructor<?> constructor) throws Exception {
        Class<?>[] paramTypes = constructor.getParameterTypes();
        Object[] dependencies = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> dependencyClass = paramTypes[i];
            dependencies[i] = getOrCreateBean(dependencyClass);
        }
        return dependencies;
    }

    private Object getOrCreateBean(Class<?> clazz) throws Exception {
        if (beanMap.containsKey(clazz.getName())) {
            return beanMap.get(clazz.getName());
        }
        return createBean(clazz);
    }

}
