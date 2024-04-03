package com.example.collaboratio.logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {
    public static java.sql.Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(
                            "jdbc:mariadb://localhost:3306/logindata",
                            "trondl",
                            "bedepe");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static Connection connection(){

        return connection;
    }
}
