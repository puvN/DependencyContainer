package com.puvn.bean.container.http.test_beans;

import com.puvn.bean.container.annotation.RequestMapping;

// No @ControllerBean annotation here, but there is a mapping
public class InvalidControllerBean {

    @RequestMapping("/invalid/mapping")
    public void doPost() {

    }

}
