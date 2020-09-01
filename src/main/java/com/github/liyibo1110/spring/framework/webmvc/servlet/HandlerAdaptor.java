package com.github.liyibo1110.spring.framework.webmvc.servlet;

import com.github.liyibo1110.spring.framework.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HandlerAdaptor {

    public boolean supports(Object handler) {
        return handler instanceof HandlerMapping;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response,
                               Object handler) throws Exception {

        HandlerMapping handlerMapping = (HandlerMapping)handler;
        Map<String, Integer> paramIndexMapping = new HashMap<>();

        /**
         * 保存请求的自定义参数名称和方法形参位置对应
         * RequestMapping注解并不是必须的，如果不加，只能通过字节码解析之类的技术来获取形参的名字
         */
        Annotation[][] pa = handlerMapping.getMethod().getParameterAnnotations();
        for (int i = 0; i < pa.length; i++) {
            for(Annotation a : pa[i]) {
                if(a instanceof RequestMapping) {
                    String paramName = ((RequestMapping)a).value();
                    if(!"".equals(paramName.trim())) paramIndexMapping.put(paramName, i);
                }
            }
        }

        /**
         * 还要额外保存request和response之类的非自定义参数的位置对应（因为不一定在首位定义）
         */
        Class<?>[] paramTypes = handlerMapping.getMethod().getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> type = paramTypes[i];
            if(type == HttpServletRequest.class || type == HttpServletResponse.class) {
                paramIndexMapping.put(type.getName(), i);
            }
        }

        // 参数赋值
        Map<String, String[]> params = request.getParameterMap();
        Object[] paramValues = new Object[paramTypes.length];

        for(Map.Entry<String, String[]> entry : params.entrySet()) {
            String value = Arrays.toString(entry.getValue()).replaceAll("\\[|\\]", "")
                                                            .replaceAll("\\s", "");
            if(!paramIndexMapping.containsKey(entry.getKey())) continue;
            int index = paramIndexMapping.get(entry.getKey());
            paramValues[index] = caseStringValue(value, paramTypes[index]);
        }

        // 额外注入request和response
        if(paramIndexMapping.containsKey(HttpServletRequest.class.getName())) {
            int index = paramIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[index] = request;
        }
        if(paramIndexMapping.containsKey(HttpServletResponse.class.getName())) {
            int index = paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[index] = response;
        }

        // 真正调用控制器方法
        Object result = handlerMapping.getMethod().invoke(handlerMapping.getController(), paramValues);
        if(result == null || result instanceof Void) return null;

        if(handlerMapping.getMethod().getReturnType() == ModelAndView.class) {
            return (ModelAndView)result;
        }

        return null;
    }

    private Object caseStringValue(String value, Class<?> type) {

        // 象征性的写死2种类型
        if(Integer.class == type) {
            return Integer.valueOf(value);
        }else if(Double.class == type) {
            return Double.valueOf(value);
        }
        return value;
    }
}
