package com.github.liyibo1110;

import com.github.liyibo1110.spring.framework.context.ApplicationContext;

public class Test {

    public static void main(String[] args) {
        ApplicationContext context = new ApplicationContext("classpath:application.properties");
        System.out.println(context);
    }
}
