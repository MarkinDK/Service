package com.servlets.goals_servlets;

import com.utils.CookieProcessor;
import com.databasecontent.Goal;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.processors.GoalProcessor2;
import com.utils.Localizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/goals","/goals/"})
public class SeveralGoalsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        GoalProcessor2 goalProcessor = (GoalProcessor2) getServletContext().getAttribute("GoalProcessor2");
        String userEmail = CookieProcessor.getCookieValue(req.getCookies(), "email");
        Goal[] goals;
        try {
            goals = goalProcessor.getAllGoals(userEmail);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ServletException();
        }
        PrintWriter writer = resp.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(writer, goals);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Localizer localizer = (Localizer) getServletContext().getAttribute("Localizer");
        GoalProcessor2 goalProcessor = (GoalProcessor2) getServletContext().getAttribute("GoalProcessor2");
        String userEmail = CookieProcessor.getCookieValue(req.getCookies(), "email");
        Goal goal = new ObjectMapper().readValue(req.getReader(), Goal.class);
        goal.setUserEmail(userEmail);
        try {
            goalProcessor.postGoal(goal);
            resp.getWriter().println("<h1>"+ localizer.getMessage(req.getHeader("Accept-Language"),"goalAdded")+"<h1>");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ServletException();
        }
    }

}
