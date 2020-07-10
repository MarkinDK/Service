package com.servlets;

import com.processors.UserProcessor;
import com.utils.CookieProcessor;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebFilter("/*")
public class AuthFilter implements Filter {
    private UserProcessor userProcessor;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userProcessor = (UserProcessor) filterConfig.getServletContext().getAttribute("UserProcessor");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if ("/login".equals(req.getRequestURI()) || "/registration".equals(req.getRequestURI())) {
            chain.doFilter(req, resp);
            return;
        }
        try {
            if (CookieProcessor.checkCookies(req.getCookies(), userProcessor))
                chain.doFilter(req, resp);
            else resp.sendRedirect("/login");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ServletException();
        }
    }

    @Override
    public void destroy() {

    }
}
