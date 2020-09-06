package com.github.liyibo1110.spring.framework.aop.aspect;

import com.github.liyibo1110.spring.framework.aop.intercept.MethodInterceptor;
import com.github.liyibo1110.spring.framework.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

public class MethodBeforeAdviceInterceptor extends AbstractAspectAdvice implements MethodInterceptor {

    private JoinPoint joinPoint;

    public MethodBeforeAdviceInterceptor(Method aspectMethod, Object target) {
        super(aspectMethod, target);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        this.joinPoint = invocation;
        before(invocation.getMethod(), invocation.getArguments(), invocation.getThis());
        return invocation.proceed();
    }

    private void before(Method method, Object[] args, Object target) throws Throwable {
        // method.invoke(target, args);
        super.invokeAdviceMethod(joinPoint, null, null);
    }
}
