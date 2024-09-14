package com.puvn.bean.container;

import com.puvn.bean.container.context.ContainerApplicationContext;
import com.puvn.bean.container.context.TestAbstractClass;
import com.puvn.bean.container.context.TestAnnotatedAbstractClass;
import com.puvn.bean.container.context.TestAnnotatedInterface;
import com.puvn.bean.container.context.TestInterface;
import com.puvn.bean.container.context.TestRepository;
import com.puvn.bean.container.context.TestService;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ContainerApplicationContextTest {

    private final Map<String, Class<?>> classMap =
            Map.of("com.puvn.bean.container.context.TestAbstractClass", TestAbstractClass.class,
                    "com.puvn.bean.container.context.TestAnnotatedAbstractClass", TestAnnotatedAbstractClass.class,
                    "com.puvn.bean.container.context.TestAnnotatedInterface", TestAnnotatedInterface.class,
                    "com.puvn.bean.container.context.TestInterface", TestInterface.class,
                    "com.puvn.bean.container.context.TestRepository", TestRepository.class,
                    "com.puvn.bean.container.context.TestService", TestService.class);

    @Test
    public void should_instantiate_only_classes() {
        ContainerApplicationContext containerApplicationContext = new ContainerApplicationContext(classMap);
        Map<String, Object> applicationContext = containerApplicationContext.getInitializedContext();
        assertNotNull(applicationContext);
        // Expecting only TestService and TestRepository to be instantiated
        assertEquals(2, applicationContext.size());
        assertInstanceOf(TestRepository.class, applicationContext.get("com.puvn.bean.container.context.TestRepository"));
        assertInstanceOf(TestService.class, applicationContext.get("com.puvn.bean.container.context.TestService"));

    }

}