package com.github.liyibo1110.spring.framework.beans.support;

import com.github.liyibo1110.spring.framework.beans.config.BeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BeanDefinitionReader {

    private final String SCAN_PACKAGE = "scanPackage";

    private Properties config = new Properties();

    private List<String> registryBeanClasses = new ArrayList<>();

    public BeanDefinitionReader(String... locations) {

        // 加载配置文件到Properties
        InputStream is = this.getClass()
                             .getClassLoader()
                             .getResourceAsStream(locations[0].replace("classpath:", ""));
        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        doScanner(config.getProperty(SCAN_PACKAGE));
    }

    private void doScanner(String scanPackage) {

        URL url = this.getClass().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File classPath = new File(url.getFile());
        for(File file : classPath.listFiles()) {
            if(file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            }else {
                if(!file.getName().endsWith(".class")) continue;
                String className = scanPackage + "." + file.getName().replace(".class", "");
                registryBeanClasses.add(className);
            }
        }
    }

    /**
     * 将扫描到的所有class文件类名转换成BeanDefinition对象
     * @return
     */
    public List<BeanDefinition> loadBeanDefinitions() {

        List<BeanDefinition> list = new ArrayList<>();
        try {
            for(String className : registryBeanClasses) {

                Class<?> beanClass = Class.forName(className);
                if(beanClass.isInterface()) continue;

                list.add(doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()), beanClass.getName()));

                // 如果类实现了接口，还要搞一个接口的版本
                Class<?>[] interfaces = beanClass.getInterfaces();
                for(Class<?> i : interfaces) {
                    // 直接后来的覆盖之前的
                    list.add(doCreateBeanDefinition(i.getName(), beanClass.getName()));
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return list;
    }

    private BeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName) {

        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;

        /*try {
            Class<?> beanClass = Class.forName(className);
            // 跳过接口
            if(beanClass.isInterface()) return null;
            BeanDefinition beanDefinition = new BeanDefinition();
            beanDefinition.setBeanClassName(className);
            beanDefinition.setFactoryBeanName(beanClass.getSimpleName());
            return beanDefinition;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;*/
    }

    public Properties getConfig() {
        return config;
    }

    private String toLowerFirstCase(String name) {
        char[] chars = name.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
