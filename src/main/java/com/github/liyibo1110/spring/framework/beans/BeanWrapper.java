package com.github.liyibo1110.spring.framework.beans;

public class BeanWrapper {

    private Object wrappedInstance;
    private Class<?> wrappedClass;

    public BeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }

    public Object getWrappedInstance() {
        return wrappedInstance;
    }

    /**
     * 可能是代理后动态生成的Class，不一定是原来的
     * @return
     */
    public Class<?> getWrappedClass() {
        return wrappedInstance.getClass();
    }
}
