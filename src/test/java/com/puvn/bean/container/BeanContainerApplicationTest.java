package com.puvn.bean.container;

import com.puvn.testing.application.AnnotatedTestingApplication;
import com.puvn.testing.application.NoMainAnnotationTestingApplication;
import com.puvn.bean.container.exception.BeanContainerApplicationException;
import com.puvn.bean.container.exception.BeanContainerError;
import com.puvn.testing.application.WildCardPackageTestingApplication;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BeanContainerApplicationTest {

    @Test
    public void should_raise_exception_if_no_main_annotation_present() {
        var exception = assertThrows(BeanContainerApplicationException.class,
                () -> new BeanContainerApplication(NoMainAnnotationTestingApplication.class, new String[0]));
        assertTrue(exception.getMessage().contains(BeanContainerError.NOT_ANNOTATED_EXCEPTION.errorMessage));
    }

    @Test
    public void should_raise_exception_if_no_packages_in_the_main_annotation() {
        var exception = assertThrows(BeanContainerApplicationException.class,
                () -> new BeanContainerApplication(AnnotatedTestingApplication.class, new String[0]));
        assertTrue(exception.getMessage().contains(BeanContainerError.NO_PACKAGES_SPECIFIED.errorMessage));
    }

    @Test
    public void should_raise_exception_if_wildcard_package_is_set() {
        var exception = assertThrows(BeanContainerApplicationException.class,
                () -> new BeanContainerApplication(WildCardPackageTestingApplication.class, new String[0]));
        assertTrue(exception.getMessage().contains(BeanContainerError.NO_WILDCARD_NAMES_ALLOWED.errorMessage));
    }

}