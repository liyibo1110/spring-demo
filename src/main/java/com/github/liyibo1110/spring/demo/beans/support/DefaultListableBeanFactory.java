package com.github.liyibo1110.spring.demo.beans.support;

import com.github.liyibo1110.spring.demo.beans.config.BeanDefinition;
import com.github.liyibo1110.spring.demo.context.support.AbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultListableBeanFactory extends AbstractApplicationContext {

    /**
     * 伪IOC容器
     */
    protected final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

}
