package com.github.liyibo1110.spring.framework.aop.aspect;

import com.github.liyibo1110.spring.framework.aop.intercept.MethodInterceptor;
import com.github.liyibo1110.spring.framework.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

public class MethodBeforeAdviceInterceptor extends AbstractAspectAdvice implements MethodInterceptor {

    public MethodBeforeAdviceInterceptor(Method aspectMethod, Object target) {
        super(aspectMethod, target);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return null;
    }
}
