package com.github.liyibo1110.spring.demo.controller;

import com.github.liyibo1110.spring.demo.service.ModifyService;
import com.github.liyibo1110.spring.demo.service.QueryService;
import com.github.liyibo1110.spring.framework.annotation.AutoWired;
import com.github.liyibo1110.spring.framework.annotation.Controller;
import com.github.liyibo1110.spring.framework.annotation.RequestMapping;
import com.github.liyibo1110.spring.framework.annotation.RequestParam;
import com.github.liyibo1110.spring.framework.webmvc.servlet.ModelAndView;

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
    public ModelAndView query(HttpServletRequest request, HttpServletResponse response,
                      @RequestParam("name") String name){
        String result = queryService.query(name);
        return out(response,result);
    }

    @RequestMapping("/add*.json")
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response,
                    @RequestParam("name") String name, @RequestParam("addr") String addr){
        String result = modifyService.add(name,addr);
        return out(response,result);
    }

    @RequestMapping("/remove.json")
    public ModelAndView remove(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam("id") Integer id){
        String result = modifyService.remove(id);
        return out(response,result);
    }

    @RequestMapping("/edit.json")
    public ModelAndView edit(HttpServletRequest request, HttpServletResponse response,
                     @RequestParam("id") Integer id, @RequestParam("name") String name){
        String result = modifyService.edit(id,name);
        return out(response,result);
    }



    private ModelAndView out(HttpServletResponse response, String str){
        try {
            response.getWriter().write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
