package com.github.liyibo1110.spring.framework.webmvc.servlet;

import com.github.liyibo1110.spring.framework.annotation.Controller;
import com.github.liyibo1110.spring.framework.annotation.RequestMapping;
import com.github.liyibo1110.spring.framework.context.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Request;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DispatcherServlet extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

    private ApplicationContext context;

    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    private Map<HandlerMapping, HandlerAdaptor> handlerAdapters = new HashMap<>();

    private List<ViewResolver> viewResolvers = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            this.doDispatch(request, response);
        } catch (Exception e) {
            // new ModelAndView("500");
            response.getWriter().write("500 Exception, Detail:\r\n" + Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]", "")
                                                                                                           .replaceAll(",\\s", "\r\n"));
            e.printStackTrace();
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // 1、初始化ApplicationContext
        context = new ApplicationContext(config.getInitParameter(CONTEXT_CONFIG_LOCATION));
        // 2、初始化mvc九大组件
        initStrategies(context);
    }

    protected void initStrategies(ApplicationContext context) {
        // 多文件上传组件
        initMultipartResolver(context);
        // 本地语言环境
        initLocaleResolver(context);
        // 模板处理器
        initThemeResolver(context);
        // 处理器映射
        initHandlerMappings(context);
        // 参数适配器
        initHandlerAdapters(context);
        // 异常拦截器
        initHandlerExceptionResolvers(context);
        // 视图预处理器
        initRequestToViewNameTranslator(context);
        // 视图转换器
        initViewResolvers(context);
        // 参数缓存器
        initFlashMapManager(context);
    }

    private void initMultipartResolver(ApplicationContext context) {
    }

    private void initLocaleResolver(ApplicationContext context) {
    }

    private void initThemeResolver(ApplicationContext context) {
    }

    private void initHandlerMappings(ApplicationContext context) {

        String[] beanNames = context.getBeanDefinitionNames();
        try {
            for(String beanName : beanNames) {
                Object controller = context.getBean(beanName);
                Class<?> clazz = controller.getClass();
                if(!clazz.isAnnotationPresent(Controller.class)) continue;

                String baseUrl = "";
                if(clazz.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping mapping = clazz.getAnnotation(RequestMapping.class);
                    baseUrl = mapping.value();
                }

                Method[] methods = clazz.getMethods();
                for(Method method : methods) {
                    if(!method.isAnnotationPresent(RequestMapping.class)) continue;
                    RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                    String regex = ("/" + baseUrl + "/" + mapping.value()).replaceAll("\\*", ".*")
                                                                          .replaceAll("/+", "/");

                    Pattern pattern = Pattern.compile(regex);
                    this.handlerMappings.add(new HandlerMapping(pattern, controller, method));
                    logger.info("Mapped " + regex + "," + method);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initHandlerAdapters(ApplicationContext context) {

        for(HandlerMapping handler : handlerMappings) {
            handlerAdapters.put(handler, new HandlerAdaptor());
        }
    }

    private void initHandlerExceptionResolvers(ApplicationContext context) {
    }

    private void initRequestToViewNameTranslator(ApplicationContext context) {
    }

    private void initViewResolvers(ApplicationContext context) {

        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        File templateRootDir = new File(templateRootPath);
        String[] templates = templateRootDir.list();
        for (int i = 0; i < templates.length; i++) {
            viewResolvers.add(new ViewResolver(templateRoot));
        }
    }

    private void initFlashMapManager(ApplicationContext context) {
    }

    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 1、根据request尝试匹配HandlerMapping
        HandlerMapping handler = getHandler(request);
        if(handler == null) {
            // new ModelAndView("404");
            return;
        }

        // 2、获取HandlerAdapter
        HandlerAdaptor ha = getHandlerAdaptor(handler);

        // 3、调用方法
        ModelAndView mv = ha.handle(request, response, handler);

        processDispatchResult(request, response, mv);
    }

    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {

        // 将ModelAndView对象转换成最终输出的内容
        if(mv == null) return;
    }

    private HandlerAdaptor getHandlerAdaptor(HandlerMapping handler) {

        if(handlerAdapters.isEmpty()) return null;
        HandlerAdaptor ha = handlerAdapters.get(handler);
        if(ha.supports(handler)) return ha;
        return null;
    }

    private HandlerMapping getHandler(HttpServletRequest request) {

        if(handlerMappings.isEmpty()) return null;

        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");

        for(HandlerMapping handler : handlerMappings) {
            try {
                Matcher matcher = handler.getPattern().matcher(url);
                if(!matcher.matches()) continue;
                return handler;
            } catch (Exception e) {
                throw e;
            }
        }
        return null;
    }
}
