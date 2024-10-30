package com.puvn.bean.container.exception.context;

import com.puvn.bean.container.exception.bean.BeanContainerError;

public class ContainerApplicationContextException extends RuntimeException {
    public ContainerApplicationContextException(BeanContainerError error) {
        super(error.errorMessage);
    }
}
