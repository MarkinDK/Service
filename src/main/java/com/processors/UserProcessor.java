package com.processors;

import com.databasecontent.DBManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserProcessor {
    private final DBManager dbManager;
    private PreparedStatement createPS;
    private final String checkSQL = "select * from users " +
            "where email = ? " +
            "and password = ?";
    private final String isPresentSQL = "select * from users " +
            "where email = ?";
    private PreparedStatement checkPS;
    private PreparedStatement isPresentPS;
    private String createSQL = "insert users " +
            "(email, password) values (?,?)";

    public UserProcessor(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public boolean create(String email, String password) throws SQLException {
        boolean result;
        synchronized (dbManager) {
            createPS = dbManager.prepareStatement(createSQL);
            createPS.setString(1, email);
            createPS.setString(2, password);
            result = createPS.executeUpdate() > 0;
            createPS.close();
        }
        return result;
    }

    public boolean authorize(String email, String password) throws SQLException {
        boolean result;
        synchronized (dbManager) {
            checkPS = dbManager.getConnection().prepareStatement(checkSQL);
            checkPS.setString(1, email);
            checkPS.setString(2, password);
            result = checkPS.executeQuery().next();
            checkPS.close();
        }
        return result;
    }

    public boolean isPresent(String email) throws SQLException {
        boolean result;
        synchronized (dbManager) {
            isPresentPS = dbManager.getConnection().prepareStatement(isPresentSQL);
            isPresentPS.setString(1, email);
            result = isPresentPS.executeQuery().next();
            isPresentPS.close();
        }
        return result;
    }
}
