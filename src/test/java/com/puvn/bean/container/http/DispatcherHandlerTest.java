package com.puvn.bean.container.http;

import com.puvn.bean.container.http.test_beans.ValidControllerBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.puvn.bean.container.http.ContextUtil.prepareEmptyControllerContext;
import static com.puvn.bean.container.http.ContextUtil.prepareFullControllerContext;
import static com.puvn.bean.container.http.ContextUtil.prepareInvalidControllerContext;
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
        assertEquals(3, handlers.size());
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

}