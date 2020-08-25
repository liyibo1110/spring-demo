package com.github.liyibo1110.spring.framework.context;

/**
 * 一种获取IOC容器的抽象，
 * 会通过一个监听器扫描所有类，只要实现这个接口，将自动调用setApplicationContext()方法进行注入。
 */
public interface ApplicationContextAware {

    void setApplicationContext(ApplicationContext applicationContext);
}
