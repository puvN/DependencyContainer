package com.puvn.bean.container.context;

import com.puvn.bean.container.annotation.ControllerBean;
import com.puvn.bean.container.annotation.RepositoryBean;
import com.puvn.bean.container.annotation.ServiceBean;
import com.puvn.bean.container.exception.bean.BeanContainerError;
import com.puvn.bean.container.exception.context.ContainerContextInitializerException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import static com.puvn.bean.container.util.AnnotationUtil.isAnnotatedWith;

/**
 * Prepares a classMap based on provided package names. Scans packages and adds them into a map which will be used
 * for the actual application context
 */
public class ContainerContextInitializer extends Context {

    private static final Logger LOGGER = Logger.getLogger(ContainerContextInitializer.class.getName());

    public ContainerContextInitializer(String[] packageNames) {
        try {
            constructContext(packageNames);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
            throw new ContainerContextInitializerException(BeanContainerError.CLASS_LOADING_ERROR);
        }
    }

    public Map<String, Class<?>> getContext() {
        return this.classMap;
    }

    private void constructContext(String[] packageNames) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Set<Class<?>> annotatedClasses = new HashSet<>();

        for (String pkg : packageNames) {
            String path = pkg.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File directory = new File(resource.getFile());

                if (directory.exists() && directory.isDirectory()) {
                    findClassesInDirectory(directory, pkg, annotatedClasses);
                }
            }
        }

        for (Class<?> clazz : annotatedClasses) {
            checkAndAddToClassMap(clazz);
        }
    }

    private void findClassesInDirectory(File directory, String packageName, Set<Class<?>> classes) {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                findClassesInDirectory(file, packageName + "." + file.getName(), classes);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().replace(".class", "");
                try {
                    Class<?> clazz = Class.forName(className);
                    if (isAnnotatedWith(clazz, ServiceBean.class) ||
                            isAnnotatedWith(clazz, RepositoryBean.class) ||
                            isAnnotatedWith(clazz, ControllerBean.class)) {
                        classes.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    LOGGER.warning("Could not load class " + className);
                }
            }
        }
    }

    private void checkAndAddToClassMap(Class<?> clazz) throws ContainerContextInitializerException {
        if (isAnnotatedWith(clazz, ServiceBean.class) && isAnnotatedWith(clazz, RepositoryBean.class)) {
            String errorMessage = String.format("Class %s has invalid definition", clazz.getName());
            LOGGER.severe(errorMessage);
            throw new ContainerContextInitializerException(
                    BeanContainerError.MULTIPLE_BEAN_ANNOTATION_ERROR
            );
        }
        classMap.put(clazz.getName(), clazz);
    }

}
