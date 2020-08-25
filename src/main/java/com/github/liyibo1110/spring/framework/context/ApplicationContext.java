package com.github.liyibo1110.spring.framework.context;

import com.github.liyibo1110.spring.framework.annotation.AutoWired;
import com.github.liyibo1110.spring.framework.annotation.Controller;
import com.github.liyibo1110.spring.framework.annotation.Service;
import com.github.liyibo1110.spring.framework.beans.BeanWrapper;
import com.github.liyibo1110.spring.framework.beans.config.BeanDefinition;
import com.github.liyibo1110.spring.framework.beans.support.BeanDefinitionReader;
import com.github.liyibo1110.spring.framework.beans.support.DefaultListableBeanFactory;
import com.github.liyibo1110.spring.framework.beans.BeanFactory;

import java.lang.reflect.Field;
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
                try {
                    getBean(beanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
    public Object getBean(String beanName) throws Exception {
        // 1.初始化
        BeanWrapper beanWrapper = instantiateBean(beanName, beanDefinitionMap.get(beanName));

        // 2.保存到IOC容器
        /*if(factoryBeanInstanceCache.containsKey(beanName)) {
            throw new Exception("The " + beanName + "is exists!");
        }*/
        factoryBeanInstanceCache.put(beanName, beanWrapper);

        // 3.注入
        populateBean(beanName, beanWrapper);
        return factoryBeanInstanceCache.get(beanName).getWrappedInstance();
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

    private void populateBean(String beanName, BeanWrapper beanWrapper) {

        Object instance = beanWrapper.getWrappedInstance();

        Class<?> clazz = beanWrapper.getWrappedClass();
        // 只有加特定注解的类，才能被尝试注入（Spring原版没有这个规定）
        if(clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(Service.class)) {
            return;
        }

        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields) {
            if(!field.isAnnotationPresent(AutoWired.class)) continue;
            AutoWired autoWired = field.getAnnotation(AutoWired.class);
            String autoWiredBeanName = autoWired.value().trim();
            if("".equals(autoWiredBeanName)) {
                autoWiredBeanName = field.getType().getName();
            }

            field.setAccessible(true);
            try {
                field.set(instance, factoryBeanInstanceCache.get(autoWiredBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
