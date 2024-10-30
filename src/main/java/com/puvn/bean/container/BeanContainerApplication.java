package com.puvn.bean.container;

import com.puvn.bean.container.annotation.BeanContainer;
import com.puvn.bean.container.context.ContainerApplicationContext;
import com.puvn.bean.container.context.ContainerContextInitializer;
import com.puvn.bean.container.exception.bean.BeanContainerApplicationException;
import com.puvn.bean.container.exception.bean.BeanContainerError;
import com.puvn.bean.container.http.SimpleHttpServer;

import java.util.Map;

public class BeanContainerApplication {

    private static final int HTTP_SERVER_PORT = 8080;

    public static void run(Class<?> mainClass, String[] args) {
        new BeanContainerApplication(mainClass, args);
    }

    public BeanContainerApplication(Class<?> mainClass, String[] args) {
        // 1. Main class validation
        validateMainClassAnnotation(mainClass);
        // 2. Context construction based on provided package names
        String[] packageNames = getValidatedPackageNames(mainClass);
        ContainerContextInitializer containerContextInitializer = constructContext(packageNames);
        Map<String, Class<?>> constructedContext = containerContextInitializer.getContext();
        // 3. Initialize http server if there were any controllers found
        SimpleHttpServer.initialize(HTTP_SERVER_PORT, constructedContext);
        // 4. Context initialization based on constructed context
        ContainerApplicationContext containerApplicationContext = prepareContext(constructedContext);
        Map<String, Object> applicationContext = containerApplicationContext.getInitializedContext();
    }

    private ContainerApplicationContext prepareContext(Map<String, Class<?>> constructedContext) {
        return new ContainerApplicationContext(constructedContext);
    }

    private ContainerContextInitializer constructContext(String[] packageNames) {
        return new ContainerContextInitializer(packageNames);
    }

    private String[] getValidatedPackageNames(Class<?> mainClass) {
        String[] packageNames = mainClass.getAnnotation(BeanContainer.class).packages();
        if (packageNames.length == 0) {
            throw new BeanContainerApplicationException(BeanContainerError.NO_PACKAGES_SPECIFIED);
        }
        for (String _package: packageNames) {
            if (_package.contains("*")) {
                throw new BeanContainerApplicationException(BeanContainerError.NO_WILDCARD_NAMES_ALLOWED);
            }
        }
        return packageNames;
    }

    private void validateMainClassAnnotation(Class<?> mainClass) {
        if (!mainClass.isAnnotationPresent(BeanContainer.class)) {
            throw new BeanContainerApplicationException(BeanContainerError.NOT_ANNOTATED_EXCEPTION);
        }
    }

}
