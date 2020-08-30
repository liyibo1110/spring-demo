package com.github.liyibo1110.spring.framework.webmvc.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerAdaptor {

    public boolean supports(Object handler) {
        return handler instanceof HandlerMapping;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response,
                               Object handler) throws Exception {
        return null;
    }
}
