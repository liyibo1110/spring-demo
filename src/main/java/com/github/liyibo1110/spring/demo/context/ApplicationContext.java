package com.github.liyibo1110.spring.demo.context;

import com.github.liyibo1110.spring.demo.beans.BeanWrapper;
import com.github.liyibo1110.spring.demo.beans.config.BeanDefinition;
import com.github.liyibo1110.spring.demo.beans.support.BeanDefinitionReader;
import com.github.liyibo1110.spring.demo.beans.support.DefaultListableBeanFactory;
import com.github.liyibo1110.spring.demo.beans.BeanFactory;

import java.util.List;
import java.util.Map;

public class ApplicationContext extends DefaultListableBeanFactory implements BeanFactory {

    private String[] configLocations;

    private BeanDefinitionReader reader;

    public ApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refresh() throws Exception {

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
        for(Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            if(!entry.getValue().isLazyInit()) {
                getBean(beanName);
            }
        }
    }

    private void doRegisterBeanDefinition(List<BeanDefinition> beanDefinitions) throws Exception {

        for(BeanDefinition beanDefinition : beanDefinitions) {
            if(beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception("The \"" + beanDefinition.getFactoryBeanName() + "\" is exists!");
            }
            beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
    }

    @Override
    public Object getBean(String beanName) {
        // 1.初始化

        // 2.注入
        return null;
    }

    private void instantiateBean(String beanName, BeanDefinition beanDefinition) {

    }

    private void populateBean(String beanName, BeanDefinition beanDefinition, BeanWrapper wrapper) {

    }
}
