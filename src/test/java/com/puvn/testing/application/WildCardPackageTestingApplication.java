package com.puvn.testing.application;

import com.puvn.bean.container.annotation.BeanContainer;

@BeanContainer(packages = {"com.puvn.services", "com.puvn.components.*"})
public class WildCardPackageTestingApplication {
}
