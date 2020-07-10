package com.servlets;

import com.processors.GoalProcessor2;
import com.processors.TaskProcessor2;
import com.processors.UserProcessor;
import com.databasecontent.DBManager;
import com.utils.Localizer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@WebListener
public class ContextListener implements ServletContextListener {
    private DBManager dbManager;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        try {
            dbManager = new DBManager();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
        sc.setAttribute("Localizer",new Localizer());
        sc.setAttribute("TaskProcessor2", new TaskProcessor2(dbManager));
        sc.setAttribute("GoalProcessor2", new GoalProcessor2(dbManager));
        sc.setAttribute("UserProcessor", new UserProcessor(dbManager));
        //sc.setAttribute("UserAuthenticator", new UserAuthenticator(dbManager));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            dbManager.closeConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new IllegalStateException(throwables);
        }
    }
}
