package com.puvn.bean.container.exception;

public class BeanContainerApplicationException extends RuntimeException {
    public BeanContainerApplicationException(BeanContainerError error) {
        super(error.errorMessage);
    }
}
