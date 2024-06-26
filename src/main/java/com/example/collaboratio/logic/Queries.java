package com.example.collaboratio.logic;

import com.example.collaboratio.model.NewUser;
import com.example.collaboratio.model.SessionCreation;
import com.example.collaboratio.model.UserAccount;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.SessionAttribute;

import javax.management.relation.RelationSupport;
import javax.sql.rowset.serial.SerialBlob;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Security;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Queries {
    public static Connection connection;
    {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/logindata",
                    "trondl",
                    "bedepe");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public String check ="fail";

    public PreparedStatement insertNewUser(Connection con, NewUser newuser) throws SQLException {
        PreparedStatement insertStatement = con.prepareStatement("""
            INSERT INTO T_user_accounts (user_name, token) VALUES (?,?)
            """);
        {
            insertStatement.setString(1,newuser.userName);
            insertStatement.setString(2, newuser.userPassword);
            int executed = insertStatement.executeUpdate();
        }
        return insertStatement;
    }
    public PreparedStatement insertUserAndSessionToken(Connection con, UserAccount userAccount) throws SQLException {
        PreparedStatement insertStatement = con.prepareStatement("""
            
            INSERT INTO T_user_accounts (user_name, token, avatar, current_session_token) VALUES (?,?,?,?)
                
""");
        {
            insertStatement.setString(1, userAccount.getUser_name());
            insertStatement.setString(2, userAccount.getToken());
            insertStatement.setString(3, userAccount.getAvatar());
            insertStatement.setString(4, "uniqueSessionToken");
            int executed = insertStatement.executeUpdate();
        }

     return insertStatement; }

    public PreparedStatement insertSession(Connection con, SessionCreation session, String mycookie, Blob media) throws SQLException {

        PreparedStatement lookForId = con.prepareStatement("""
        SELECT id FROM T_user_accounts WHERE current_session_id = ?""");
        {

            lookForId.setString(1,mycookie);

            ResultSet results = lookForId.executeQuery();
            results.next();
            System.out.println(results.getInt("id"));}

        PreparedStatement insertStatement = con.prepareStatement("""
            
            INSERT INTO T_sessions_created (session_topic, session_problem,session_hints,members_amount,host_of_session,media) VALUES (?,?,?,?,?,?)
                
""");
        {
            insertStatement.setString(1, session.getSessionTopic());
            insertStatement.setString(2, session.getSessionProblem());
            insertStatement.setString(3, session.getSessionHints());
            insertStatement.setInt(4, session.getSessionMembers());

            insertStatement.setInt(5, lookForId.getResultSet().getInt(1));
            insertStatement.setBlob(6,media);

            int executed = insertStatement.executeUpdate();
        }

        return insertStatement; }

    public String loginCheck(Connection connection, String username, String token, HttpSession session) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("""
            select user_name, token\s
            FROM T_user_accounts
            WHERE user_name =  ? """);
            statement.setString(1,username);
           {

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
                try {
                    if (resultSet.getString("user_name").equals(username) && resultSet.getString("token").equals(token)) {
                        System.out.println("Login Success");
                        PreparedStatement sessionStatement = connection.prepareStatement("""
                        UPDATE T_user_accounts SET current_session_id = ? WHERE user_name = ? AND token = ?""");
                        sessionStatement.setString(1,session.getId());
                        sessionStatement.setString(2,username);
                        sessionStatement.setString(3, token);{
                            ResultSet resultSetSession = sessionStatement.executeQuery();
                            resultSetSession.next();
                        }
                        check = "success";
                        System.out.println(session.toString());
                        resultSet.close();
                        // need to make a break here otherwise check will we "fail"

                    }
                } catch (Exception e){
                    System.out.println(e);
                }


           }
        return check;}

    public boolean checkSessionID(String cookie, Connection con) throws SQLException {

        PreparedStatement sessionCheckerStatement = con.prepareStatement("""
        SELECT current_session_id FROM T_user_accounts WHERE current_session_id = ?""");
        sessionCheckerStatement.setString(1,cookie);{

            ResultSet sessionCheckerResults = sessionCheckerStatement.executeQuery();
            sessionCheckerResults.next();
            // if cookie matches the stored Session in DB, this function will return true and user can navigate through webapp
            return sessionCheckerResults.getString("current_session_id").equals(cookie);
        }


    }

    public String getUserName(String cookie, Connection con) throws SQLException {
        PreparedStatement statement = con.prepareStatement("""
        SELECT user_name 
        FROM T_user_accounts
        WHERE current_session_id = ?""");
        statement.setString(1,cookie);
        {
            ResultSet results = statement.executeQuery();
            results.next();

            return results.getString("user_name");
        }
    }

    public String getUserId(String cookie, Connection con) throws SQLException {
        PreparedStatement statement = con.prepareStatement("""
        SELECT id 
        FROM T_user_accounts
        WHERE current_session_id = ?""");
        statement.setString(1,cookie);
        {
            ResultSet results = statement.executeQuery();
            results.next();

            return results.getString("id");
        }
    }




    }



