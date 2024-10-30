package com.puvn.bean.container.http;

import com.puvn.bean.container.annotation.ControllerBean;
import com.puvn.bean.container.annotation.RequestMapping;
import com.puvn.bean.container.context.ContainerContextInitializer;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.puvn.bean.container.util.AnnotationUtil.isAnnotatedWith;

public class DispatcherHandler {

    private static final Logger LOGGER = Logger.getLogger(ContainerContextInitializer.class.getName());

    public static Map<String, Method> handleControllers(Map<String, Class<?>> applicationContext) {
        Map<String, Class<?>> controllerMap =
                applicationContext.entrySet().stream()
                        .filter(entry -> isAnnotatedWith(entry.getValue(), ControllerBean.class))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (controllerMap.isEmpty()) {
            LOGGER.info("No @ControllerBean's found in the context, no handlers will be created");
            return Collections.emptyMap();
        }
        return collectMapping(controllerMap.values());
    }

    private static Map<String, Method> collectMapping(Collection<Class<?>> controllerClassCollection) {
        Map<String, Method> mappings = new HashMap<>();
        controllerClassCollection.forEach(controllerClass -> {
            for (Method method : controllerClass.getDeclaredMethods()) {
                if (isAnnotatedWith(method, RequestMapping.class)) {
                    String path = method.getAnnotation(RequestMapping.class).value();
                    mappings.put(path, method);
                }
            }
        });
        if (mappings.isEmpty()) {
            LOGGER.info("No @RequestMapping's found in controllers, no handler will be created");
            return Collections.emptyMap();
        }
        return mappings;
    }

}
