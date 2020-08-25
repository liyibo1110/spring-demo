package com.github.liyibo1110.spring.framework.beans;

/**
 * 单例工厂的抽象
 */
public interface BeanFactory {

    /**
     * 从IOC容器获取一个Bean实例
     * @param beanName
     * @return
     */
    Object getBean(String beanName) throws Exception;
}
