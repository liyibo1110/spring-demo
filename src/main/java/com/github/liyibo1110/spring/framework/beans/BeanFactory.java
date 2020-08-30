package com.github.liyibo1110.spring.framework.beans;

/**
 * 单例工厂的抽象
 */
public interface BeanFactory {

    Object getBean(Class<?> beanClass) throws Exception;

    /**
     * 从IOC容器获取一个Bean实例
     * @param beanName
     * @return
     */
    Object getBean(String beanName) throws Exception;


}
