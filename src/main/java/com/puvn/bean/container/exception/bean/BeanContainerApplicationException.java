package com.puvn.bean.container.exception.bean;

public class BeanContainerApplicationException extends RuntimeException {
    public BeanContainerApplicationException(BeanContainerError error) {
        super(error.errorMessage);
    }
}
