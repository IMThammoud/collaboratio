package com.example.collaboratio.controller;

import com.example.collaboratio.model.SessionCreation;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    @GetMapping("/get-info")
    public String getInfo(){

        return "this info was queried via htmx";
    }

    // This queries the the id from T_user_accounts and then uses the id to query the data from T_sessions_created
    // it loops through the amount of .next() is successful and stores the dataset into a List
    public String getDataForCards(String cookie, Connection con) throws SQLException {

        ArrayList<JSONObject> SessionList =  new ArrayList<>();
        int i = 0;

        PreparedStatement fetchId = con.prepareStatement("""
        SELECT id
        FROM T_user_accounts
        WHERE current_session_id = ?""");
        fetchId.setString(1,cookie);

        ResultSet results = fetchId.executeQuery();
        results.next();
        String user_id = results.getString("id");



        PreparedStatement statement = con.prepareStatement("""
        SELECT session_topic, session_problem, session_hints, media,members_amount 
        FROM T_sessions_created
        WHERE host_of_session = ?""");
        statement.setString(1,user_id);

        ResultSet resultSessions = statement.executeQuery();
        while (resultSessions.next()){

            SessionCreation newSession = new SessionCreation(resultSessions.getString(1),
                    resultSessions.getString(2),
                    resultSessions.getString(3),
                    resultSessions.getBlob(4),
                    resultSessions.getInt(5));

            // Make JSON-Objects of SessionData and store them in a list
            JSONObject SessionAsJson = new JSONObject(newSession);
            SessionList.add(SessionAsJson);

            JSONArray SessionArrayJson = new JSONArray(SessionList);


            System.out.println("Content of Object in JSON-ARRAY" + SessionArrayJson.toString());
            i++;
        }

        return SessionList.toString();



    };
}
