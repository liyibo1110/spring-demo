package com.github.liyibo1110.spring.demo.service;

public interface ModifyService {

    public String add(String name, String addr);

    public String edit(Integer id, String name);

    public String remove(Integer id);
}
