package com.servlets.authorization;

import com.utils.CookieProcessor;
import com.processors.UserProcessor;
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

@WebServlet(urlPatterns = "/registration")
public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserProcessor userProcessor = (UserProcessor) getServletContext().getAttribute("UserProcessor");
        try {
            if (CookieProcessor.checkCookies(req.getCookies(),userProcessor)) {
                resp.sendRedirect("/tasks");
            } else if (userProcessor.isPresent(CookieProcessor.getCookieValue(req.getCookies(), "email")))
                resp.sendRedirect("/login");
            else req.getRequestDispatcher("/registration.html").forward(req, resp);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ServletException(throwables);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserProcessor userProcessor = (UserProcessor) getServletContext().getAttribute("UserProcessor");
        Localizer localizer = (Localizer) getServletContext().getAttribute("Localizer");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        if (Pattern.matches("^[a-zA-Z0-9]+([.\\-][a-zA-Z0-9]+)*@[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*(\\.[a-z]{2,})$", email))
            try {
                if (userProcessor.create(email, DigestUtils.md5Hex(password))) {
                    CookieProcessor.setAuthCookies(resp, email, DigestUtils.md5Hex(password));
                    resp.sendRedirect("/tasks");
                } else {
                    resp.getWriter()
                            .println("<font color=red>" + localizer.getMessage(
                                    req.getHeader("Accept-Language"),
                                    "error") + "</font>");
                    getServletContext()
                            .getRequestDispatcher("/registration.html")
                            .include(req, resp);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                throw new ServletException(throwables);
            }
        else {
            resp.getWriter()
                    .println("<font color=red>" + localizer.getMessage(
                            req.getHeader("Accept-Language"),
                            "wrongEmailSample") + "</font>");
            getServletContext()
                    .getRequestDispatcher("/registration.html")
                    .include(req, resp);
        }
    }


}
