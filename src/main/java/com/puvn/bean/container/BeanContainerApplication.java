package com.puvn.bean.container;

import com.puvn.bean.container.annotation.BeanContainer;
import com.puvn.bean.container.context.ContainerContextInitializer;
import com.puvn.bean.container.exception.BeanContainerApplicationException;
import com.puvn.bean.container.exception.BeanContainerError;

import java.util.Map;

public class BeanContainerApplication {

    public static void run(Class<?> mainClass, String[] args) {
        new BeanContainerApplication(mainClass, args);
    }

    public BeanContainerApplication(Class<?> mainClass, String[] args) {
        validateMainClassAnnotation(mainClass);
        String[] packageNames = getValidatedPackageNames(mainClass);
        ContainerContextInitializer containerContextInitializer = constructContext(packageNames);
        Map<String, Class<?>> constructedContext = containerContextInitializer.getContext();
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
