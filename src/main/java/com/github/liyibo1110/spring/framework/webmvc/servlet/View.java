package com.github.liyibo1110.spring.framework.webmvc.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class View {

    private final String DEFAULT_CONTENT_TYPE = "text/html:charset=utf-8";

    private final Pattern pattern = Pattern.compile("\\$\\{[^\\}]+\\}", Pattern.CASE_INSENSITIVE);

    private File viewFile;

    public View(File viewFile) {
        this.viewFile = viewFile;
    }

    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        StringBuilder sb = new StringBuilder();
        RandomAccessFile raf = new RandomAccessFile(viewFile, "r");

        String line = null;
        while((line = raf.readLine()) != null) {
            line = new String(line.getBytes("ISO-8859-1"), "utf-8");
            Matcher matcher = pattern.matcher(line);
            while(matcher.find()) {
                String paramName = matcher.group();
                paramName = paramName.replaceAll("\\$\\{|\\}", "");
                Object paramValue = model.get(paramName);
                if(paramValue == null) continue;
                line = matcher.replaceFirst(paramValue.toString());
                matcher = pattern.matcher(line);
            }
            sb.append(line);
        }
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(sb.toString());
    }
}
