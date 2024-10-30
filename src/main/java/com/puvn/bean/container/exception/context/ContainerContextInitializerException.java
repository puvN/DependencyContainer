package com.puvn.bean.container.exception.context;

import com.puvn.bean.container.exception.bean.BeanContainerError;

public class ContainerContextInitializerException extends RuntimeException {
    public ContainerContextInitializerException(BeanContainerError error) {
        super(error.errorMessage);
    }
}
