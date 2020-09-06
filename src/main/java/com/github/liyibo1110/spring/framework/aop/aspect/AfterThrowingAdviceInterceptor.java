package com.github.liyibo1110.spring.framework.aop.aspect;

import com.github.liyibo1110.spring.framework.aop.intercept.MethodInterceptor;
import com.github.liyibo1110.spring.framework.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

public class AfterThrowingAdviceInterceptor extends AbstractAspectAdvice implements MethodInterceptor {

    private String throwName;

    public AfterThrowingAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        }catch (Throwable t) {
            super.invokeAdviceMethod(invocation, null, t.getCause());
            throw t;
        }
    }

    public void setThrowName(String throwName) {
        this.throwName = throwName;
    }
}
