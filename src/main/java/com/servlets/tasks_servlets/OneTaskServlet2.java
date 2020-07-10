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
import java.sql.SQLException;

@WebServlet(urlPatterns = "/tasks/*")
public class OneTaskServlet2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TaskProcessor2 taskProcessor2 = (TaskProcessor2) getServletContext().getAttribute("TaskProcessor2");
        String userEmail = CookieProcessor.getCookieValue(req.getCookies(), "email");
        try {
            int taskId = Integer.parseInt(req.getPathInfo().split("/")[1]);
            new ObjectMapper().writerWithDefaultPrettyPrinter()
                    .writeValue(
                            resp.getWriter(),
                            taskProcessor2.getTaskById(userEmail, taskId));
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Localizer localizer = (Localizer) getServletContext().getAttribute("Localizer");
        TaskProcessor2 taskProcessor2 = (TaskProcessor2) getServletContext().getAttribute("TaskProcessor2");
        Task task = new ObjectMapper().readValue(req.getReader(), Task.class);
        task.setUserEmail(CookieProcessor.getCookieValue(req.getCookies(), "email"));
        try {
            int taskId = Integer.parseInt(req.getPathInfo().split("/")[1]);
            task.setId(taskId);
            taskProcessor2.putTask(task);
            resp.getWriter().println("<h1>" + localizer.getMessage(req.getHeader("Accept-Language"), "changed") + "<h1>");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resp.getWriter().println("<h1>" + localizer.getMessage(req.getHeader("Accept-Language"), "idError") + "<h1>");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ServletException();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Localizer localizer = (Localizer) getServletContext().getAttribute("Localizer");
        TaskProcessor2 taskProcessor2 = (TaskProcessor2) getServletContext().getAttribute("TaskProcessor2");
        String userEmail = CookieProcessor.getCookieValue(req.getCookies(), "email");
        try {
            int taskId = Integer.parseInt(req.getPathInfo().split("/")[1]);
            taskProcessor2.deleteTask(userEmail, taskId);
            resp.sendRedirect("/tasks");
        }catch (NumberFormatException e){
            e.printStackTrace();
            resp.getWriter().println("<h1>"+ localizer.getMessage(req.getHeader("Accept-Language"),"idError")+"<h1>");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ServletException();
        }
    }
}
