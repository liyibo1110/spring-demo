package com.github.liyibo1110.spring.framework.webmvc.servlet;

import java.io.File;
import java.util.Locale;

public class ViewResolver {

    private final String DEFAULT_TEMPLATE_SUFFIX = ".html";

    private File templateRootDir;

    public ViewResolver(String templateRoot) {
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        templateRootDir = new File(templateRootPath);
    }

    public View resolveViewName(String viewName, Locale locale) {

        if(viewName == null || "".equals(viewName.trim())) return null;
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName : viewName + DEFAULT_TEMPLATE_SUFFIX;
        File f = new File((templateRootDir.getPath() + "/" + viewName).replaceAll("/+", "/"));
        return new View(f);
    }
}
