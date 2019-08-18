package com.something.config;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * url filter
 */
public class UrlFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        if ("POST".equalsIgnoreCase(req.getMethod())) {
            req.getSession().setAttribute("msg", null);
            String expect = (String) req.getSession().getAttribute("CHECK_CODE");
            if (expect != null && !expect.equalsIgnoreCase(req.getParameter("captchaCode"))) {
                req.getSession().setAttribute("msg", "captchaCode is incorrect");
                return;
            }
        }
    }

    @Override
    public void destroy() {
    }
}