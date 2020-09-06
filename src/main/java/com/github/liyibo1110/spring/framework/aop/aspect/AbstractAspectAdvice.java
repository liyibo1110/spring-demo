package com.github.liyibo1110.spring.framework.aop.aspect;

import java.lang.reflect.Method;

public abstract class AbstractAspectAdvice implements Advice {

    private Method aspectMethod;
    private Object aspectTarget;

    public AbstractAspectAdvice(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }

    protected Object invokeAdviceMethod(JoinPoint joinPoint, Object returnValue, Throwable t) throws Throwable {
        Class<?>[] paramTypes = aspectMethod.getParameterTypes();
        if(paramTypes == null || paramTypes.length == 0) {
            return aspectMethod.invoke(aspectTarget);
        }else {
            Object[] args = new Object[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                if(paramTypes[i] == JoinPoint.class) {
                    args[i] = joinPoint;
                }else if(paramTypes[i] == Throwable.class) {
                    args[i] = t;
                }else if(paramTypes[i] == Object.class) {
                    args[i] = returnValue;
                }
            }
            return aspectMethod.invoke(aspectTarget, args);
        }
    }
}
