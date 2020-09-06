package com.github.liyibo1110.spring.framework.aop.aspect;

import com.github.liyibo1110.spring.framework.aop.intercept.MethodInterceptor;
import com.github.liyibo1110.spring.framework.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

public class AfterReturningAdviceInterceptor extends AbstractAspectAdvice implements MethodInterceptor {

    private JoinPoint joinPoint;

    public AfterReturningAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object returnValue = invocation.proceed();
        this.joinPoint = invocation;
        afterReturning(returnValue, invocation.getMethod(), invocation.getArguments(), invocation.getThis());
        return null;
    }

    private void afterReturning(Object returnValue, Method method,
                                Object[] arguments, Object aThis) throws Throwable {
        super.invokeAdviceMethod(joinPoint, returnValue, null);
    }
}
