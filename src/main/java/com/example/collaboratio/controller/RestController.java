package com.example.collaboratio.controller;

import com.example.collaboratio.logic.DbConnector;
import com.example.collaboratio.model.SessionCreation;

import com.google.gson.Gson;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @GetMapping("/get-info")
    public String getInfo(){

        return "this info was queried via htmx";
    }

    @GetMapping("/getCards")
    // This queries the id from T_user_accounts and then uses the id to query the data from T_sessions_created
    // it loops through the amount of .next() is successful and stores the dataset into a List
    public String getDataForCards(@CookieValue("JSESSIONID") String cookie) throws SQLException {

        List<SessionCreation> SessionList = new ArrayList<>();
        String listJson = null;
        Gson serialize = new Gson();
        int i = 0;

        PreparedStatement fetchId = DbConnector.connection.prepareStatement("""
        SELECT id
        FROM T_user_accounts
        WHERE current_session_id = ?""");
        fetchId.setString(1,cookie);

        ResultSet results = fetchId.executeQuery();
        results.next();
        String user_id = results.getString("id");




        PreparedStatement statement = DbConnector.connection.prepareStatement("""
        SELECT session_topic, session_problem, session_hints, media,members_amount, host_of_session ,id
        FROM T_sessions_created
        WHERE host_of_session = ?""");
        statement.setString(1,user_id);
        System.out.println("Catched UserID: " + user_id);

        ResultSet resultSessions = statement.executeQuery();
        while (resultSessions.next()){

            SessionCreation newSession = new SessionCreation(resultSessions.getString(1),
                    resultSessions.getString(2),
                    resultSessions.getString(3),
                    resultSessions.getBlob(4),
                    resultSessions.getInt(5));

            // Adding the SessionID (primary key) manually to the object and appending it to the JSON that way.
            newSession.setSessionId(resultSessions.getInt(6));


            // Make JSON-Objects of SessionData and store them in a list
            SessionList.add(newSession);

            System.out.println("Content of Object in JSON-ARRAY" + SessionList.get(i).getSessionId());
            // Iterator to check how often this loop is repeated
            i++;
        }            listJson = serialize.toJson(SessionList);


        return listJson;



    }
}
