package com.example.collaboratio.logic;

import com.example.collaboratio.model.SessionCreation;
import com.example.collaboratio.model.UserAccount;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Queries {
    public String check;
    public PreparedStatement insertUser(Connection con, UserAccount userAccount) throws SQLException {
        PreparedStatement insertStatement = con.prepareStatement("""
            
            INSERT INTO user_account (user_name, token, avatar) VALUES (?,?,?)
                
""");
        {
            insertStatement.setString(1, userAccount.getUser_name());
            insertStatement.setString(2, userAccount.getToken());
            insertStatement.setString(3, userAccount.getAvatar());
            int executed = insertStatement.executeUpdate();
        }

     return insertStatement; }

    public PreparedStatement insertSession(Connection con, SessionCreation session) throws SQLException {
        PreparedStatement insertStatement = con.prepareStatement("""
            
            INSERT INTO sessions_created (session_topic, session_problem,session_hints,session_members) VALUES (?,?,?,?)
                
""");
        {
            insertStatement.setString(1, session.getSessionTopic());
            insertStatement.setString(2, session.getSessionProblem());
            insertStatement.setString(3, session.getSessionHints());
            insertStatement.setInt(4, session.getSessionMembers());

            int executed = insertStatement.executeUpdate();
        }

        return insertStatement; }

    public String loginCheck(Connection connection, String username, String token) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("""
            select user_name, token\s
            FROM user_account\s""");{

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                System.out.println(resultSet.getString("user_name"));
                System.out.println(resultSet.getString("token"));

                if (resultSet.getString("user_name").equals(username) && resultSet.getString("token").equals(token)) {
                    System.out.println("Login Success");
                    check = "success";
                    // need to make a break here otherwise check will we "fail"
                    break;
                }else
                    check = "fail";
            }
        }
        return check;
    }
}


