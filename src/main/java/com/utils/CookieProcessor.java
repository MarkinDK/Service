package com.utils;

import com.processors.UserProcessor;
import com.sun.istack.internal.NotNull;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

public class CookieProcessor {
    public static void setAuthCookies(HttpServletResponse resp, String email, String password) {
        Cookie emailCookie = new Cookie("email", email);
        emailCookie.setMaxAge(60 * 60);
        resp.addCookie(emailCookie);
        Cookie passwordCookie = new Cookie("password", password);
        passwordCookie.setMaxAge(60 * 60);
        resp.addCookie(passwordCookie);
    }

    public static boolean checkCookies(Cookie[] cookies, UserProcessor userProcessor) throws SQLException {
        if (cookies != null) {
            String email = getCookieValue(cookies, "email");
            String password = getCookieValue(cookies, "password");
            if (email != null && password != null) return userProcessor.authorize(email, password);
        }
        return false;
    }

    public static String getCookieValue(Cookie[] cookies, @NotNull String name) {
        if (cookies != null) {
            for (Cookie c : cookies)
                if (name.equals(c.getName())) return c.getValue();
        }
        return null;
    }
}
