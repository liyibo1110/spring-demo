package com.github.liyibo1110.spring.demo.context;

import com.github.liyibo1110.spring.demo.beans.config.BeanDefinition;
import com.github.liyibo1110.spring.demo.beans.support.BeanDefinitionReader;
import com.github.liyibo1110.spring.demo.beans.support.DefaultListableBeanFactory;
import com.github.liyibo1110.spring.demo.beans.BeanFactory;

import java.util.List;

public class ApplicationContext extends DefaultListableBeanFactory implements BeanFactory {

    private String[] configLocations;

    private BeanDefinitionReader reader;

    public ApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        refresh();
    }

    @Override
    public void refresh() {

        // 1.定位配置文件
        reader = new BeanDefinitionReader(this.configLocations);

        // 2.加载配置文件，扫描相关类并封装成BeanDefinition
        List<BeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

        // 3.把BeanDefinition存到IOC容器里
        doRegisterBeanDefinition(beanDefinitions);

        // 4.提前初始化非延迟加载的类
        doAutowired();
    }

    private void doAutowired() {

    }

    private void doRegisterBeanDefinition(List<BeanDefinition> beanDefinitions) {

    }

    @Override
    public Object getBean(String beanName) {
        return null;
    }


}
