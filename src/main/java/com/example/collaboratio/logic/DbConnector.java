package com.example.collaboratio.logic;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {
    // This class is responsible for connecting the Application with a MariaDB Database
    // I Used the DOTENV Library to load Environment variables into dbUrl, dbUsername, dbPassword
    // If used with another DB you can create a "java.env" file in root dir and load the ENVs from there as i did

    public static java.sql.Connection connection;

    // This actually works now!! TO-DO: replace the parameters of connection with the envs down here
    public static String dbUrl = Dotenv.configure().filename("java.env").load().get("DB_URL");
    public static String dbUsername = Dotenv.configure().filename("java.env").load().get("DB_USERNAME");
    public static String dbPassword = Dotenv.configure().filename("java.env").load().get("DB_PASSWORD");

    static {
        try {
            // This prints out the ENVs -- it works!!
            System.out.println(" DB Username and Password " + dbUsername+dbPassword);
            connection = DriverManager.getConnection(
                            dbUrl,
                            dbUsername,
                            dbPassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static Connection connection(){

        return connection;
    }
}
