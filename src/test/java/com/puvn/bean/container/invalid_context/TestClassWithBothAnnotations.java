package com.puvn.bean.container.invalid_context;

import com.puvn.bean.container.annotation.RepositoryBean;
import com.puvn.bean.container.annotation.ServiceBean;

// Error: two bean annotations on one class, can't instantiate it
@RepositoryBean
@ServiceBean
public class TestClassWithBothAnnotations {
}
