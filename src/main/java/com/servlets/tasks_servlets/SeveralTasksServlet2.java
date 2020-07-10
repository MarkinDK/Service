package com.servlets.tasks_servlets;

import com.utils.CookieProcessor;
import com.databasecontent.Task.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.processors.TaskProcessor2;
import com.utils.Localizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;


@WebServlet(urlPatterns = {"/tasks", "/tasks/"})
public class SeveralTasksServlet2 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TaskProcessor2 taskProcessor2 = (TaskProcessor2) getServletContext().getAttribute("TaskProcessor2");
        String userEmail = CookieProcessor.getCookieValue(req.getCookies(), "email");
        String goalIdString = req.getParameter("goalId");
        Task[] tasks = null;
        try {
            if (goalIdString == null) {
                tasks = taskProcessor2.getAllTasksWithoutGoal(userEmail);
            } else {
                int goalId = Integer.parseInt(goalIdString);
                tasks = taskProcessor2.getAllTasksWithGoalId(userEmail, goalId);
            }
        } catch (NumberFormatException | SQLException throwables) {
            throwables.printStackTrace();
        }
        PrintWriter writer = resp.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(writer, tasks);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Localizer localizer = (Localizer) getServletContext().getAttribute("Localizer");
        TaskProcessor2 taskProcessor2 = (TaskProcessor2) getServletContext().getAttribute("TaskProcessor2");
        String userEmail = CookieProcessor.getCookieValue(req.getCookies(), "email");
        Task task = new ObjectMapper().readValue(req.getReader(), Task.class);
        task.setUserEmail(userEmail);
        try {
            taskProcessor2.postTask(task);
            resp.getWriter().println("<h1>"+ localizer.getMessage(req.getHeader("Accept-Language"),"taskAdded")+"<h1>");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ServletException();
        }
    }


}
