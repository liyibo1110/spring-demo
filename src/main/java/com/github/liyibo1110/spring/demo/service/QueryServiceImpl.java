package com.github.liyibo1110.spring.demo.service;

import java.time.LocalDateTime;

public class QueryServiceImpl implements QueryService {

    @Override
    public String query(String name) {
        String now = LocalDateTime.now().toString();
        String json = "{name:\"" + name + "\", time:\"" + now + "\"}";
        return json;
    }
}
