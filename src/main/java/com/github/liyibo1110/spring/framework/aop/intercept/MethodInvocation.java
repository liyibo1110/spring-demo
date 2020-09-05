package com.github.liyibo1110.spring.framework.aop.intercept;

import java.lang.reflect.Method;
import java.util.List;

public class MethodInvocation {

    public MethodInvocation(Object proxy, Object target, Method method, Object[] arguments,
                            Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers) {

    }

    public Object proceed() throws Throwable {
        return null;
    }
}
