package com.processors;

import com.databasecontent.DBManager;
import com.databasecontent.Goal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GoalProcessor2 {

    private PreparedStatement getAllGoalsPS;
    private PreparedStatement postGoalPS;
    private PreparedStatement putGoalByIdPS;
    private PreparedStatement getGoalByIdPS;
    private PreparedStatement deleteGoalByIdPS;
    private final DBManager dbManager;
    private final String getGoalByIdSQL = "select * from goals " +
            "where (userEmail = ? " +
            "and id = ?)";
    private final String getAllGoalsSQL = "select * from goals " +
            "where (userEmail = ?)";
    private final String deleteGoalByIdSQL = "delete from goals " +
            "where (userEmail = ? " +
            "and id = ?)";
    private final String postGoalSQL = "insert goals " +
            "(title, description, userEmail) " +
            "values (?,?,?)";
    private final String putGoalSQL = "update goals " +
            "set title = ?, description = ?" +
            "where (userEmail = ? " +
            "and id =? ) ";

    public GoalProcessor2(DBManager dbManager) {
        this.dbManager = dbManager;
    }


    public Goal[] getAllGoals(String userEmail) throws SQLException {
        Goal[] goals;
        synchronized (dbManager) {
            getAllGoalsPS = dbManager.prepareStatement(getAllGoalsSQL);
            getAllGoalsPS.setString(1, userEmail);
            goals = getArray(getAllGoalsPS.executeQuery());
            getAllGoalsPS.close();
        }
        return goals;
    }

    private Goal[] getArray(ResultSet rs) throws SQLException {
        List<Goal> goals = new ArrayList<>();
        Goal goal;
        while (rs.next()) {
            goal = new Goal();
            goalFiller(rs, goal);
            goals.add(goal);
        }
        rs.close();
        return goals.size() > 0 ? goals.toArray(new Goal[goals.size()]) : null;
    }

    private void goalFiller(ResultSet rs, Goal goal) throws SQLException {
        goal.setTitle(rs.getString("title"));
        goal.setDescription(rs.getString("description"));
        goal.setUserEmail(rs.getString("userEmail"));
        goal.setId(rs.getInt("id"));
    }

    public boolean postGoal(Goal goal) throws SQLException {
        boolean result;
        synchronized (dbManager) {
            postGoalPS = dbManager.prepareStatement(postGoalSQL);
            setParams(postGoalPS, goal);
            result = postGoalPS.executeUpdate() > 0;
            postGoalPS.close();
        }
        return result;
    }

    public boolean putGoal(Goal goal) throws SQLException {
        boolean result;
        synchronized (dbManager) {
            putGoalByIdPS = dbManager.prepareStatement(putGoalSQL);
            setParams(putGoalByIdPS, goal);
            putGoalByIdPS.setInt(4, goal.getId());
            result = putGoalByIdPS.executeUpdate() > 0;
            putGoalByIdPS.close();
        }
        return result;
    }

    private void setParams(PreparedStatement ps, Goal goal) throws SQLException {
        ps.setString(1, goal.getDescription());
        ps.setString(2, goal.getTitle());
        ps.setString(3, goal.getUserEmail());
    }

    public Goal getGoalById(String userEmail, int id) throws SQLException {
        Goal goal = null;
        synchronized (dbManager) {
            getGoalByIdPS = dbManager.prepareStatement(getGoalByIdSQL);
            getGoalByIdPS.setString(1, userEmail);
            getGoalByIdPS.setInt(2, id);
            ResultSet rs = getGoalByIdPS.executeQuery();
            if (rs.next()) {
                goal = new Goal();
                goalFiller(rs, goal);
            }
            rs.close();
            getGoalByIdPS.close();
        }
        return goal;
    }

    public boolean deleteGoalById(String userEmail, int id) throws SQLException {
        boolean result;
        synchronized (dbManager) {
            deleteGoalByIdPS = dbManager.prepareStatement(deleteGoalByIdSQL);
            deleteGoalByIdPS.setString(1, userEmail);
            deleteGoalByIdPS.setInt(2, id);
            result = deleteGoalByIdPS.executeUpdate() > 0;
            deleteGoalByIdPS.close();
        }
        return result;
    }
}
