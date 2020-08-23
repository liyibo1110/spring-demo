package com.github.liyibo1110.spring.demo.context;

import com.github.liyibo1110.spring.demo.beans.BeanWrapper;
import com.github.liyibo1110.spring.demo.beans.config.BeanDefinition;
import com.github.liyibo1110.spring.demo.beans.support.BeanDefinitionReader;
import com.github.liyibo1110.spring.demo.beans.support.DefaultListableBeanFactory;
import com.github.liyibo1110.spring.demo.beans.BeanFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext extends DefaultListableBeanFactory implements BeanFactory {

    private String[] configLocations;

    private BeanDefinitionReader reader;

    /**
     * 单例IOC容器缓存
     */
    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    private Map<String, BeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>();

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
        BeanWrapper beanWrapper = instantiateBean(beanName, new BeanDefinition());

        // 2.保存到IOC容器

        // 3.注入
        populateBean(beanName, new BeanDefinition(), beanWrapper);
        return null;
    }

    private BeanWrapper instantiateBean(String beanName, BeanDefinition beanDefinition) {
        // 1.获取真正的类名
        String className = beanDefinition.getBeanClassName();
        // 2.实例化
        Object instance = null;
        try {
            // 暂时认为默认就是单例Bean
            if(singletonObjects.containsKey(className)) {
                instance = singletonObjects.get(className);
            }else {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                singletonObjects.put(className, instance);
                singletonObjects.put(beanDefinition.getFactoryBeanName(), instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 3.封装成BeanWrapper，容器名字为singletonObjects和factoryBeanInstanceCache
        BeanWrapper beanWrapper = new BeanWrapper(instance);

        return beanWrapper;
    }

    private void populateBean(String beanName, BeanDefinition beanDefinition, BeanWrapper wrapper) {

    }
}
