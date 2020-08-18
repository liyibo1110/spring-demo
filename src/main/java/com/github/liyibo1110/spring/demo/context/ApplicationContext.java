package com.github.liyibo1110.spring.demo.context;

import com.github.liyibo1110.spring.demo.beans.support.DefaultListableBeanFactory;
import com.github.liyibo1110.spring.demo.beans.BeanFactory;

public class ApplicationContext extends DefaultListableBeanFactory implements BeanFactory {

    @Override
    public void refresh() {

    }

    @Override
    public Object getBean(String beanName) {
        return null;
    }


}
