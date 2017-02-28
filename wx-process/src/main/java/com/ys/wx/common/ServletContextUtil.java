package com.ys.wx.common;

import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * Title: ServletContentUtil工具类
 * Description:
 * Author: Maxwell wen
 * Since: 2016年1月12日 下午2:32:26
 * Version: 1.1.0
 * Copyright: (c) 二龙湖基地组织  2015 ~ 2016 版权所有
 */
public class ServletContextUtil implements ServletContextAware {

    private static ServletContext servletContext;

    /**
     * 得到ServletContext
     *
     * @return
     */
    public static ServletContext getServletContext() {
        return servletContext;
    }

    @SuppressWarnings("static-access")
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * 得到servletContext中的对象
     *
     * @param name
     * @return
     */
    public static Object getAppObject(String name) {
        if (servletContext != null) {
            return servletContext.getAttribute(name);
        }
        return null;
    }
}
