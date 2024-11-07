package com.puvn.bean.container.http;

import com.puvn.bean.container.http.test_beans.EmptyControllerBean;
import com.puvn.bean.container.http.test_beans.InvalidControllerBean;
import com.puvn.bean.container.http.test_beans.ValidControllerBean;

import java.util.HashMap;
import java.util.Map;

public class ContextUtil {

    static Map<String, Class<?>> prepareFullControllerContext() {
        Map<String, Class<?>> applicationContext = new HashMap<>();
        applicationContext.put("validControllerBean", ValidControllerBean.class);
        applicationContext.put("invalidControllerBean", InvalidControllerBean.class);
        applicationContext.put("emptyControllerBean", EmptyControllerBean.class);
        return applicationContext;
    }

    static Map<String, Class<?>> prepareEmptyControllerContext() {
        Map<String, Class<?>> applicationContext = new HashMap<>();
        applicationContext.put("emptyControllerBean", EmptyControllerBean.class);
        return applicationContext;
    }

    static Map<String, Class<?>> prepareInvalidControllerContext() {
        Map<String, Class<?>> applicationContext = new HashMap<>();
        applicationContext.put("invalidControllerBean", InvalidControllerBean.class);
        return applicationContext;
    }

}
