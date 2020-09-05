package com.github.liyibo1110.spring.framework.aop;

import com.github.liyibo1110.spring.framework.aop.support.AdvisedSupport;

public class CglibAopProxy implements AopProxy {

    private AdvisedSupport advised;

    public CglibAopProxy(AdvisedSupport config) {
        this.advised = config;
    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
