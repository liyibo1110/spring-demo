package com.github.liyibo1110.spring.framework.aop.intercept;

import com.github.liyibo1110.spring.framework.aop.aspect.JoinPoint;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodInvocation implements JoinPoint {

    private Object proxy;
    private Object target;
    private Method method;
    private Object[] arguments;
    private Class<?> targetClass;
    private List<Object> interceptorsAndDynamicMethodMatchers;

    private int currentInterceptorIndex = -1;
    private Map<String, Object> userAttributes;
    
    public MethodInvocation(Object proxy, Object target, Method method, Object[] arguments,
                            Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers) {
        this.proxy = proxy;
        this.target = target;
        this.method = method;
        this.arguments = arguments;
        this.targetClass = targetClass;
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
    }

    public Object proceed() throws Throwable {
        //
        if(currentInterceptorIndex == interceptorsAndDynamicMethodMatchers.size() - 1) {
            return method.invoke(target, arguments);
        }
        Object advice = interceptorsAndDynamicMethodMatchers.get(++currentInterceptorIndex);
        if(advice instanceof MethodInterceptor) {
            MethodInterceptor methodInterceptor = (MethodInterceptor)advice;
            return methodInterceptor.invoke(this);
        }else {
            return proceed();
        }
    }

    @Override
    public Object getThis() {
        return target;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public void setUserAttribute(String key, Object value) {
        if(value != null) {
            if(userAttributes == null) userAttributes = new HashMap<>();
            userAttributes.put(key, value);
        }else {
            if(userAttributes != null) userAttributes.remove(key);
        }
    }

    @Override
    public Object getUserAttribute(String key) {
        return (userAttributes == null) ? null : userAttributes.get(key);
    }
}
