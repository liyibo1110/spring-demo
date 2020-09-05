package com.github.liyibo1110.spring.framework.aop.aspect;

import java.lang.reflect.Method;

public abstract class AbstractAspectAdvice {

    private Method aspectMethod;
    private Object aspectTarget;

    public AbstractAspectAdvice(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }
}
