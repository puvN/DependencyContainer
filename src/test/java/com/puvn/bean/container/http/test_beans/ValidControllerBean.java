package com.puvn.bean.container.http.test_beans;

import com.puvn.bean.container.annotation.ControllerBean;
import com.puvn.bean.container.annotation.RequestMapping;

@ControllerBean
public class ValidControllerBean {

    @RequestMapping("/root/var/entity")
    public void doPost() {}

    @RequestMapping("/root/get/entity")
    public void doGet() {}

    @RequestMapping("/root/patch/entity")
    public void doPatch() {}

}
