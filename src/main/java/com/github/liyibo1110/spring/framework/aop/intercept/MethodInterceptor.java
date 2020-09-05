package com.github.liyibo1110.spring.framework.aop.intercept;

public interface MethodInterceptor {

    Object invoke(MethodInvocation invocation) throws Throwable;
}
