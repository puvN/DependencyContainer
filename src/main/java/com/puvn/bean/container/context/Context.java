package com.puvn.bean.container.context;

import java.util.HashMap;
import java.util.Map;

public abstract class Context {

    final Map<String, Class<?>> classMap;

    public Context() {
        this.classMap = new HashMap<>();
    }

    public Context(Map<String, Class<?>> classMap) {
        this.classMap = classMap;
    }

}
