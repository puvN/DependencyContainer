package com.puvn.bean.container;

import com.puvn.bean.container.annotation.RepositoryBean;
import com.puvn.bean.container.annotation.ServiceBean;
import com.puvn.bean.container.context.ContainerContextInitializer;
import com.puvn.bean.container.exception.bean.BeanContainerError;
import com.puvn.bean.container.exception.context.ContainerContextInitializerException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContainerContextInitializerTest {

    private static final String[] CORRECT_CONTEXT_PACKAGES = {"com.puvn.bean.container.context"};

    private static final String[] INVALID_CONTEXT_PACKAGES = {"com.puvn.bean.container.invalid_context"};

    @Test
    public void should_find_annotated_classes_in_correct_context() {
        ContainerContextInitializer containerContextInitializer =
                new ContainerContextInitializer(CORRECT_CONTEXT_PACKAGES);
        Map<String, Class<?>> context = containerContextInitializer.getContext();
        assertNotNull(context);
        assertEquals(4, context.size());
        Class<?> serviceClass = context.get("com.puvn.bean.container.context.TestService");
        assertNotNull(serviceClass);
        assertTrue(serviceClass.isAnnotationPresent(ServiceBean.class));
        Class<?> repositoryClass = context.get("com.puvn.bean.container.context.TestRepository");
        assertNotNull(repositoryClass);
        assertTrue(repositoryClass.isAnnotationPresent(RepositoryBean.class));
    }

    @Test
    public void should_raise_an_exception_for_invalid_context() {
        var exception = assertThrows(ContainerContextInitializerException.class,
                () -> new ContainerContextInitializer(INVALID_CONTEXT_PACKAGES));
        assertTrue(exception.getMessage().contains(BeanContainerError.MULTIPLE_BEAN_ANNOTATION_ERROR.errorMessage));
    }

    @Test
    public void should_raise_an_exception_if_invalid_context_presents() {
        var allPackages = Arrays.copyOf(CORRECT_CONTEXT_PACKAGES, CORRECT_CONTEXT_PACKAGES.length +
                INVALID_CONTEXT_PACKAGES.length);
        System.arraycopy(INVALID_CONTEXT_PACKAGES, 0, allPackages, INVALID_CONTEXT_PACKAGES.length,
                INVALID_CONTEXT_PACKAGES.length);
        var exception = assertThrows(ContainerContextInitializerException.class,
                () -> new ContainerContextInitializer(allPackages)
        );
        assertTrue(exception.getMessage().contains(BeanContainerError.MULTIPLE_BEAN_ANNOTATION_ERROR.errorMessage));
    }

}