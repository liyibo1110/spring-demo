package com.github.liyibo1110.spring.demo.controller;

import com.github.liyibo1110.spring.demo.service.ModifyService;
import com.github.liyibo1110.spring.demo.service.QueryService;
import com.github.liyibo1110.spring.framework.annotation.AutoWired;
import com.github.liyibo1110.spring.framework.annotation.Controller;
import com.github.liyibo1110.spring.framework.annotation.RequestMapping;
import com.github.liyibo1110.spring.framework.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/web")
public class MyController {

    @AutoWired
    QueryService queryService;
    @AutoWired
    ModifyService modifyService;

    @RequestMapping("/query.json")
    public void query(HttpServletRequest request, HttpServletResponse response,
                      @RequestParam("name") String name){
        String result = queryService.query(name);
        out(response,result);
    }

    @RequestMapping("/add*.json")
    public void add(HttpServletRequest request, HttpServletResponse response,
                    @RequestParam("name") String name, @RequestParam("addr") String addr){
        String result = modifyService.add(name,addr);
        out(response,result);
    }

    @RequestMapping("/remove.json")
    public void remove(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam("id") Integer id){
        String result = modifyService.remove(id);
        out(response,result);
    }

    @RequestMapping("/edit.json")
    public void edit(HttpServletRequest request, HttpServletResponse response,
                     @RequestParam("id") Integer id, @RequestParam("name") String name){
        String result = modifyService.edit(id,name);
        out(response,result);
    }



    private void out(HttpServletResponse response, String str){
        try {
            response.getWriter().write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
