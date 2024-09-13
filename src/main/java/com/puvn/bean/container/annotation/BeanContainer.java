package com.puvn.bean.container.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A central annotation for Bean container. A simple analogue for "SpringBootApplication". Should be placed on a main
 * java class of you application where main void is located. Packages field is for packages where beans are located,
 * "com.example.services or com.example.mycompany.classes. Bean Container Application will scan those packages
 * and inject dependencies only for annotated classes
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BeanContainer {

    String[] packages();

}
