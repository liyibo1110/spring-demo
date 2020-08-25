package com.github.liyibo1110.spring.demo.service;

public class ModifyServiceImpl implements ModifyService {

    @Override
    public String add(String name, String addr) {
        return "modifyService add, name=" + name + ", addr=" + addr;
    }

    @Override
    public String edit(Integer id, String name) {
        return "modifyService edit, id=" + id + ", name=" + name;
    }

    @Override
    public String remove(Integer id) {
        return "modifyService remove, id=" + id;
    }
}
