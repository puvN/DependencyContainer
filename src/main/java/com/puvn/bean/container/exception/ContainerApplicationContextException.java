package com.puvn.bean.container.exception;

public class ContainerApplicationContextException extends RuntimeException {
    public ContainerApplicationContextException(BeanContainerError error) {
        super(error.errorMessage);
    }
}
