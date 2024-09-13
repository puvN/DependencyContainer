package com.puvn.bean.container.exception;

public class ContainerContextInitializerException extends RuntimeException {
    public ContainerContextInitializerException(BeanContainerError error) {
        super(error.errorMessage);
    }
}
