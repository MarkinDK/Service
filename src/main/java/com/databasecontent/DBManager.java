package com.databasecontent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class DBManager {
    private Connection connection;
    private final String url;
    private final String username;
    private final String password;

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public DBManager() throws ClassNotFoundException, SQLException {
            ResourceBundle bundle = ResourceBundle.getBundle("init");
            Class.forName(bundle.getString("driver"));
            url = bundle.getString("dataBaseUrl");
            username = bundle.getString("username");
            password = bundle.getString("password");
            connection = DriverManager.getConnection(url, username, password);
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }
}