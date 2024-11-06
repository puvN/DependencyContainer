package com.puvn.bean.container.http;

import com.puvn.bean.container.http.test_beans.EmptyControllerBean;
import com.puvn.bean.container.http.test_beans.InvalidControllerBean;
import com.puvn.bean.container.http.test_beans.ValidControllerBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DispatcherHandlerTest {

    Map<String, Class<?>> applicationContext;

    @BeforeEach
    void setUp() {
        applicationContext = new HashMap<>();
    }

    @Test
    void shouldHandleValidControllerBean() {
        applicationContext = prepareFullControllerContext();
        Map<String, Method> handlers = DispatcherHandler.handleControllers(applicationContext);
        assertNotNull(handlers);
        assertEquals(1, handlers.size());
        var key = "/root/var/entity";
        assertTrue(handlers.containsKey(key));
        var method = handlers.get(key);
        assertEquals("public void com.puvn.bean.container.http.test_beans.ValidControllerBean.doPost()",
                method.toString());
        assertEquals("doPost", method.getName());
        assertEquals(ValidControllerBean.class, method.getDeclaringClass());
    }

    @Test
    void shouldBeEmptyForEmptyControllerBean() {
        applicationContext = prepareEmptyControllerContext();
        Map<String, Method> handlers = DispatcherHandler.handleControllers(applicationContext);
        assertNotNull(handlers);
        assertEquals(0, handlers.size());
    }

    @Test
    void shouldBeEmptyForInvalidControllerBean() {
        applicationContext = prepareInvalidControllerContext();
        Map<String, Method> handlers = DispatcherHandler.handleControllers(applicationContext);
        assertNotNull(handlers);
        assertEquals(0, handlers.size());
    }

    private Map<String, Class<?>> prepareFullControllerContext() {
        Map<String, Class<?>> applicationContext = new HashMap<>();
        applicationContext.put("validControllerBean", ValidControllerBean.class);
        applicationContext.put("invalidControllerBean", InvalidControllerBean.class);
        applicationContext.put("emptyControllerBean", EmptyControllerBean.class);
        return applicationContext;
    }

    private Map<String, Class<?>> prepareEmptyControllerContext() {
        Map<String, Class<?>> applicationContext = new HashMap<>();
        applicationContext.put("emptyControllerBean", EmptyControllerBean.class);
        return applicationContext;
    }

    private Map<String, Class<?>> prepareInvalidControllerContext() {
        Map<String, Class<?>> applicationContext = new HashMap<>();
        applicationContext.put("invalidControllerBean", InvalidControllerBean.class);
        return applicationContext;
    }

}