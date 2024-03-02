package com.example.collaboratio.logic;

import com.example.collaboratio.model.NewUser;
import com.example.collaboratio.model.SessionCreation;
import com.example.collaboratio.model.UserAccount;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.management.relation.RelationSupport;
import java.security.Security;
import java.sql.*;

public class Queries {
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

    }



