package com.processors;

import com.databasecontent.DBManager;
import com.databasecontent.Task.Task;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class TaskProcessor2 {
    private PreparedStatement getAllTasksWithoutGoalPS;
    private PreparedStatement getAllTasksWithGoalIdPS;
    private PreparedStatement postTaskPS;
    private PreparedStatement putTaskPS;
    private PreparedStatement getTaskByIdPS;
    private PreparedStatement deleteTaskByIdPS;
    private final DBManager dbManager;

    private final String getAllTasksWithoutGoalSQL = "select * from tasks " +
            "where (userEmail = ?)";

    private final String getAllTasksWithGoalIdSQL = "select * from tasks " +
            "where (userEmail = ? " +
            "and goalId = ?)";
    private final String postTaskSQL = "insert tasks " +
            "(title, description, completeBy, status, " +
            "goalId, userEmail) " +
            "values (?,?,?,?,?,?)";//id???
    private final String putTaskSQL = "update tasks " +
            "set title = ?, description = ?, completeBy = ?, status = ? " +
            "where (goalId = ? " +
            "and userEmail = ? " +
            "and id = ?) ";
    private final String getTaskByIdSQL = "select * from tasks " +
            "where (userEmail = ? " +
            "and id = ?)";
    private final String deleteTaskByIdSQL = "delete from tasks " +
            "where (userEmail = ? " +
            "and id = ?)";

    public TaskProcessor2(DBManager dbManager) {
        this.dbManager = dbManager;
    }


    public Task[] getAllTasksWithoutGoal(String userEmail) throws SQLException {
        Task[] tasks;
        synchronized (dbManager) {
            getAllTasksWithoutGoalPS = dbManager.prepareStatement(getAllTasksWithoutGoalSQL);
            getAllTasksWithoutGoalPS.setString(1, userEmail);
            tasks = getArray(getAllTasksWithoutGoalPS.executeQuery());
            getAllTasksWithoutGoalPS.close();
        }
        return tasks;
    }

    private void taskFiller(ResultSet rs, Task task) throws SQLException {
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(Date.valueOf(rs.getDate("completeBy").toString()));
        task.setCompleteBy(calendar);
        task.setCompleted(rs.getBoolean("status"));
        task.setId(rs.getInt("id"));
        task.setGoalId(rs.getInt("goalId"));
        task.setUserEmail(rs.getString("userEmail"));
    }

    private Task[] getArray(ResultSet rs) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        while (rs.next()) {
            Task task = new Task();
            taskFiller(rs, task);
            tasks.add(task);
        }
        rs.close();
        return tasks.size() > 0 ? tasks.toArray(new Task[tasks.size()]) : null;
    }

    public Task[] getAllTasksWithGoalId(String userEmail, int goalId) throws SQLException {
        Task[] tasks;
        synchronized (dbManager) {
            getAllTasksWithGoalIdPS = dbManager.prepareStatement(getAllTasksWithGoalIdSQL);
            getAllTasksWithGoalIdPS.setString(1, userEmail);
            getAllTasksWithGoalIdPS.setInt(2, goalId);
            tasks = getArray(getAllTasksWithGoalIdPS.executeQuery());
            getAllTasksWithGoalIdPS.close();
        }
        return tasks;
    }

    public boolean postTask(Task task) throws SQLException {
        boolean result;
        synchronized (dbManager) {
            postTaskPS = dbManager.prepareStatement(postTaskSQL);
            setParams(postTaskPS, task);
            result = postTaskPS.executeUpdate() > 0;
            postTaskPS.close();
        }
        return result;
    }

    public boolean putTask(Task task) throws SQLException {
        boolean result;
        synchronized (dbManager) {
            putTaskPS = dbManager.prepareStatement(putTaskSQL);
            setParams(putTaskPS, task);
            putTaskPS.setInt(7, task.getId());
            result = putTaskPS.executeUpdate() > 0;
            putTaskPS.close();
        }
        return result;
    }

    public boolean deleteTask(String userEmail, int taskId) throws SQLException {
        boolean result;
        synchronized (dbManager) {
            deleteTaskByIdPS = dbManager.prepareStatement(deleteTaskByIdSQL);
            deleteTaskByIdPS.setString(1, userEmail);
            deleteTaskByIdPS.setInt(2, taskId);
            result = deleteTaskByIdPS.executeUpdate() > 0;
            deleteTaskByIdPS.close();
        }
        return result;
    }

    private void setParams(PreparedStatement ps, Task task) throws SQLException {
        ps.setString(1, task.getTitle());
        ps.setString(2, task.getDescription());
        ps.setDate(3, getNowDateToDate(task.getCompleteBy()));
        ps.setBoolean(4, task.isCompleted());
        ps.setObject(5, task.getGoalId());
        ps.setString(6, task.getUserEmail());
    }

    private Date getNowDateToDate(Calendar c) {
        String date = "";
        date += c.get(Calendar.YEAR) + "-";
        if ((c.get(Calendar.MONTH) + 1) < 10) {
            date += "0" + (c.get(Calendar.MONTH) + 1);
        } else {
            date += (c.get(Calendar.MONTH) + 1);
        }
        if (c.get(Calendar.DAY_OF_MONTH) < 10) {
            date += "-0" + c.get(Calendar.DAY_OF_MONTH);
        } else {
            date += "-" + c.get(Calendar.DAY_OF_MONTH);
        }
        return Date.valueOf(date);
    }

    public Task getTaskById(String userEmail, int taskId) throws SQLException {
        Task task = null;
        synchronized (dbManager) {
            getTaskByIdPS = dbManager.prepareStatement(getTaskByIdSQL);
            getTaskByIdPS.setString(1, userEmail);
            getTaskByIdPS.setString(2, String.valueOf(taskId));
            ResultSet rs = getTaskByIdPS.executeQuery();
            if (rs.next()) {
                task = new Task();
                taskFiller(rs, task);
            }
            rs.close();
            getTaskByIdPS.close();
        }
        return task;
    }
}
