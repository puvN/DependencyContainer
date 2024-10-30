package com.puvn.bean.container.exception.http;

import java.io.IOException;

public class HttpServerInitializeException extends RuntimeException {
    public HttpServerInitializeException(IOException error) {
        super(error.getMessage());
    }
}
