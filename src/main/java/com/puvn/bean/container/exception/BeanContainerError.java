package com.puvn.bean.container.exception;

public enum BeanContainerError {

    // Bean Container Application Exceptions
    NOT_ANNOTATED_EXCEPTION("Main class is not annotated with @BeanContainer annotation"),
    NO_PACKAGES_SPECIFIED("No packages specified in the @BeanContainer annotation"),
    NO_WILDCARD_NAMES_ALLOWED("No wildcard package names allowed"),

    // Container Initializer Exceptions
    CLASS_LOADING_ERROR("Could not get classpath from classloader during packages scan"),
    MULTIPLE_BEAN_ANNOTATION_ERROR("There is more than one annotation in a bean");

    BeanContainerError(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public final String errorMessage;

}
