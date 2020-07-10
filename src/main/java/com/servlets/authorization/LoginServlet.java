package com.servlets.authorization;

import com.processors.UserProcessor;
import com.utils.CookieProcessor;
import com.utils.Localizer;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserProcessor userProcessor = (UserProcessor) getServletContext().getAttribute("UserProcessor");
        try {
            if (CookieProcessor.checkCookies(req.getCookies(), userProcessor)) {
                resp.sendRedirect("/tasks");
            } else req.getRequestDispatcher("/login.html").forward(req, resp);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ServletException();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserProcessor userProcessor = (UserProcessor) getServletContext().getAttribute("UserProcessor");
        Localizer localizer = (Localizer) getServletContext().getAttribute("Localizer");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        if (Pattern.matches("^[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*(\\.(me)|(ru)|(com)|(fr)|(en))", email))
            try {
                if (userProcessor.isPresent(email)) {
                    if (userProcessor.authorize(email, DigestUtils.md5Hex(password))) {
                        CookieProcessor.setAuthCookies(resp, email, DigestUtils.md5Hex(password));
                        resp.sendRedirect("/tasks");
                    } else {
                        resp.getWriter()
                                .println("<font color=red>" + localizer
                                        .getMessage(req.getHeader("Accept-Language"),
                                                "wrongLogin") + "</font>");
                        //.println("<font color=red>Either email or password is wrong.</font>");
                        getServletContext()
                                .getRequestDispatcher("/login.html")
                                .include(req, resp);
                    }
                } else resp.sendRedirect("/registration");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                throw new ServletException(throwables);
            }
        else {
            resp.getWriter()
                    .println("<font color=blue>" +
                            localizer.getMessage(
                                    req.getHeader("Accept-Language"),
                                    "wrongEmailSample")
                            + "</font>");
            getServletContext()
                    .getRequestDispatcher("/login.html")
                    .include(req, resp);
        }
    }

}
