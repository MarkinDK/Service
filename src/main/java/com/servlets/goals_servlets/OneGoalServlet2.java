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
import java.sql.SQLException;

@WebServlet(urlPatterns = "/goals/*")
public class OneGoalServlet2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Localizer localizer = (Localizer) getServletContext().getAttribute("Localizer");
        GoalProcessor2 goalProcessor2 = (GoalProcessor2) getServletContext().getAttribute("GoalProcessor2");
        String userEmail = CookieProcessor.getCookieValue(req.getCookies(), "email");
        try {
            int goalId = Integer.parseInt(req.getPathInfo().split("/")[1]);
            new ObjectMapper().writerWithDefaultPrettyPrinter()
                    .writeValue(
                            resp.getWriter(),
                            goalProcessor2.getGoalById(userEmail, goalId));
        }catch (NumberFormatException e){
            e.printStackTrace();
            resp.getWriter().println("<h1>"+ localizer.getMessage(req.getHeader("Accept-Language"),"idError")+"<h1>");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ServletException();
        }
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Localizer localizer = (Localizer) getServletContext().getAttribute("Localizer");
        GoalProcessor2 goalProcessor2 = (GoalProcessor2) getServletContext().getAttribute("GoalProcessor2");
        Goal goal = new ObjectMapper().readValue(req.getReader(), Goal.class);
        goal.setUserEmail(CookieProcessor.getCookieValue(req.getCookies(), "email"));
        try {
            int goalId = Integer.parseInt(req.getPathInfo().split("/")[1]);
            goal.setId(goalId);
            goalProcessor2.putGoal(goal);
            resp.getWriter().println("<h1>"+ localizer.getMessage(req.getHeader("Accept-Language"),"changed")+"<h1>");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resp.getWriter().println("<h1>"+ localizer.getMessage(req.getHeader("Accept-Language"),"idError")+"<h1>");
        } catch (SQLException throwables){
            throwables.printStackTrace();
            throw new ServletException();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Localizer localizer = (Localizer) getServletContext().getAttribute("Localizer");
        GoalProcessor2 goalProcessor2 = (GoalProcessor2) getServletContext().getAttribute("GoalProcessor2");
        String userEmail = CookieProcessor.getCookieValue(req.getCookies(), "email");
        try {
            int goalId = Integer.parseInt(req.getPathInfo().split("/")[1]);
            goalProcessor2.deleteGoalById(userEmail, goalId);
            resp.sendRedirect("/goals");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resp.getWriter().println("<h1>"+ localizer.getMessage(req.getHeader("Accept-Language"),"idError")+"<h1>");
        } catch (SQLException throwables){
            throwables.printStackTrace();
            throw new ServletException();
        }
    }
}
