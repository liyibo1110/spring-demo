package com.github.liyibo1110.spring.framework.aop.support;

import com.github.liyibo1110.spring.framework.aop.aspect.AfterReturningAdviceInterceptor;
import com.github.liyibo1110.spring.framework.aop.aspect.AfterThrowingAdviceInterceptor;
import com.github.liyibo1110.spring.framework.aop.aspect.MethodBeforeAdviceInterceptor;
import com.github.liyibo1110.spring.framework.aop.config.AopConfig;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdvisedSupport {

    private AopConfig config;
    private Class<?> targetClass;
    private Object target;
    private Pattern pointCutClassPattern;
    private Map<Method, List<Object>> methodCache;

    public AdvisedSupport(AopConfig config) {
        this.config = config;
    }

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) throws Exception {

        List<Object> cached = methodCache.get(method);
        if(cached == null) {
            Method m = targetClass.getMethod(method.getName(), method.getParameterTypes());
            // 可能存在问题
            cached = methodCache.get(m);
            methodCache.put(m, cached);
        }
        return cached;
    }

    public boolean pointCutMatch() {
        return pointCutClassPattern.matcher(targetClass.toString()).matches();
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
        parse();
    }

    private void parse() {
        String pointCut = config.getPointCut()
                            .replaceAll("\\.", "\\\\.")
                            .replaceAll("\\\\.\\*", ".*")
                            .replaceAll("\\(", "\\\\(")
                            .replaceAll("\\)", "\\\\)");

        String pointCutForClassRegex = pointCut.substring(0, pointCut.lastIndexOf("\\(") - 4);
        pointCutClassPattern = Pattern.compile("class " + pointCutForClassRegex.substring(pointCutForClassRegex.lastIndexOf(" ") + 1));

        try {
            methodCache = new HashMap<>();
            Pattern pattern = Pattern.compile(pointCut);
            Map<String, Method> aspectMethods = new HashMap<>();
            Class<?> aspectClass = Class.forName(config.getAspectClass());
            for(Method method : aspectClass.getMethods()) {
                aspectMethods.put(method.getName(), method);
            }

            // 处理方法
            for(Method method : targetClass.getMethods()) {
                String methodStr = method.toString();
                if(methodStr.contains("throws")) {
                    methodStr = methodStr.substring(0, methodStr.lastIndexOf("throws")).trim();
                }
                Matcher matcher = pattern.matcher(methodStr);
                if(matcher.matches()) {
                    List<Object> advices = new LinkedList<>();
                    // 封装成MethodIntercept
                    String beforeName = config.getAspectBefore();
                    if(beforeName != null && !"".equals(beforeName)) {
                        advices.add(new MethodBeforeAdviceInterceptor(aspectMethods.get(beforeName), aspectClass.newInstance()));
                    }
                    String afterName = config.getAspectAfter();
                    if(afterName != null && !"".equals(afterName)) {
                        advices.add(new AfterReturningAdviceInterceptor(aspectMethods.get(afterName), aspectClass.newInstance()));
                    }
                    String afterThrowName = config.getAspectAfterThrow();
                    if(afterThrowName != null && !"".equals(afterThrowName)) {
                        AfterThrowingAdviceInterceptor throwingAdvice = new AfterThrowingAdviceInterceptor(aspectMethods.get(afterThrowName), aspectClass.newInstance());
                        throwingAdvice.setThrowName(config.getAspectAfterThrowingName());
                        advices.add(throwingAdvice);
                    }
                    methodCache.put(method, advices);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
